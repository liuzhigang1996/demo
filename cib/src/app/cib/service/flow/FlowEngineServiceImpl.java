package app.cib.service.flow;

import java.util.*;

import app.cib.bo.bnk.*;
import app.cib.bo.flow.*;
import app.cib.bo.sys.*;
import app.cib.core.*;
import app.cib.dao.flow.*;
import app.cib.dao.sys.*;
import app.cib.service.sys.*;
import app.cib.util.*;
import app.cib.util.otp.SMSOTPUtil;

import com.neturbo.set.core.*;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.*;
import com.neturbo.set.utils.*;
import org.apache.commons.beanutils.*;

/**
 * 處锟�锟剿斤拷锟阶碉拷service
 * 
 * @author
 * 
 */
public class FlowEngineServiceImpl implements FlowEngineService {
	private static String checkoutLock = "checkoutLock";

	private FlowProcessDao flowProcessDao = null;

	private FlowWorkDao flowWorkDao = null;
	
	private GenericJdbcDao genericJdbcDao = null;

	private TxnSubtypeDao txnSubtypeDao = null;

	private CorpUserDao corpUserDao = null;

	private FlowProcessHisDao flowProcessHisDao = null;

	private FlowWorkHisDao flowWorkHisDao = null;

	private static String msgResource = Config.getProperty("MessageResources");

	private static String generalError = "err.sys.GeneralError";

	
	public String getFirstApprover(String approvers) {
		String approver = null;

		StrTokenizer approverTokens = new StrTokenizer(approvers, ";");
		while (approverTokens.hasMoreTokens()) {
			approver = approverTokens.nextToken();
		}

		return approver;
	}

	public String getNextApprover(String approvers, String curApprover) {
		String approver = null;
		String nextApprover = null;

		StrTokenizer approverTokens = new StrTokenizer(approvers, ";");

		if (approverTokens.hasMoreTokens()) {
			approver = approverTokens.nextToken();

			while (approverTokens.hasMoreTokens()) {
				nextApprover = approver;
				approver = approverTokens.nextToken();
				if (approver.equals(curApprover)) {
					break;
				}
			}
		}

		return nextApprover;
	}

	private String getNextLevel(FlowWork flowWork) {
		String nextLevel = null;
		FlowProcess flowProcess = flowWork.getFlowProcess();

		if (TXN_CATEGORY_FINANCE.equals(flowProcess.getTxnCategory())) {

			List approveRuleItemList = extractRuleStr(flowProcess
					.getApproveRule());
			HashMap item = null;

			for (int i = 0; i < approveRuleItemList.size(); i++) {
				item = (HashMap) approveRuleItemList.get(i);

				if (item.get("Level").equals(flowWork.getApproveLevel())) {
					int curCount = flowWork.getLevelFinishCount().intValue();
					int ruleCount = Integer.parseInt(((String) item
							.get("Count")));
					if (curCount < ruleCount) {
						nextLevel = flowWork.getApproveLevel();
					} else {
						if (i > 0) {
							item = (HashMap) approveRuleItemList.get(i - 1);
							nextLevel = (String) item.get("Level");
						}
					}
					break;
				}

			}
		}

		return nextLevel;
	}

	// modify by hjs 20070120
	private List worksToMaps(List works) {
		List list = null;
		if (null != works) {
			list = new ArrayList();
			HashMap item = null;
			Object work = null;

			for (int i = 0; i < works.size(); i++) {
				work = works.get(i);
				// work = (FlowWork) works.get(i);
				item = new HashMap();
				try {
					item.putAll(BeanUtils.describe(work));
					if (work instanceof FlowWork) {
						item.putAll(BeanUtils.describe(((FlowWork) work)
								.getFlowProcess()));
					} else {
						item.putAll(BeanUtils.describe(((FlowWorkHis) work)
								.getFlowProcessHis()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				list.add(item);
			}
		}
		return list;
	}

	public List extractRuleStr(String approveRuleStr) {
		ArrayList list = new ArrayList();

		if (null != approveRuleStr) {
			StrTokenizer tokens = new StrTokenizer(approveRuleStr, "-");
			String token = null;
			HashMap item = null;
			char charOne = ' ';
			while (tokens.hasMoreTokens()) {
				token = tokens.nextToken();
				item = new HashMap();
				item.put("Level", token.substring(0, 1));
				charOne = token.charAt(0);
				if (charOne >= '0' && charOne <= '9') {
					item.put("Count", token);
				} else {
					item.put("Count", token.substring(1));
				}

				list.add(item);
			}
		}
		return list;
	}

	public String combineRuleStr(List items) {
		StringBuffer buffer = new StringBuffer();
		HashMap item = null;

		for (int i = 0; i < items.size(); i++) {
			item = (HashMap) items.get(i);
			if (i > 0) {
				buffer.append("-");
			}
			buffer.append(item.get("Level")).append(item.get("Count"));
		}

		return buffer.toString();
	}

	private String getDoneStatus(FlowWork flowWork) {
		FlowProcess process = flowWork.getFlowProcess();
		String doneStatus = null;

		if (process.getApproveRule().equals(process.getApproveStatus())) {
			doneStatus = process.getApproveStatus();
		} else {

			if (TXN_CATEGORY_FINANCE.equals(process.getTxnCategory())) {
				List statusItems = extractRuleStr(process.getApproveStatus());
				HashMap item = null;
				String count = null;

				for (int i = 0; i < statusItems.size(); i++) {
					item = (HashMap) statusItems.get(i);
					if (flowWork.getApproveLevel().equals(item.get("Level"))) {
						count = (String) item.get("Count");
						item.put("Count",
								Integer.toString(Integer.parseInt(count) + 1));
						break;
					}
				}

				doneStatus = combineRuleStr(statusItems);
			} else {
				doneStatus = Integer.toString(Integer.parseInt(process
						.getApproveStatus()) + 1);
			}
		}
		return doneStatus;
	}

	public boolean assignWork(String workId, String assignedUser, NTBUser user)
			throws NTBException {
		FlowWork flowWork = (FlowWork) flowWorkDao.load(FlowWork.class, workId);
		flowWork.setAssignedDealer(assignedUser);
		if (WORK_STATUS_CHECKOUT.equals(flowWork.getWorkStatus())) {
			flowWork.setWorkDealer(null);
			flowWork.setWorkDealerName(null);
			flowWork.setDealBeginTime(null);
			flowWork.setWorkStatus(WORK_STATUS_NEW);
		}
		flowWorkDao.update(flowWork);
		return true;
	}

	public void cancelProcess(String processId, NTBUser user)
			throws NTBException {
		FlowProcess flowProcess = (FlowProcess) flowProcessDao.load(
				FlowProcess.class, processId);
		flowProcess.setProcStatus(PROCESS_STATUS_CANCEL);
		// Jet added 2008-07-10
		flowProcess.setProcFinishTime(new Date());

		flowProcessDao.update(flowProcess);

		cancelRestWorks(processId);

		backupHis(processId);

	}

	public void cancelProcess(String processId, NTBUser user, CibAction bean)
			throws NTBException {
		FlowProcess flowProcess = (FlowProcess) flowProcessDao.load(
				FlowProcess.class, processId);
		boolean flag = false;

		try {
			Approvable approvable = (Approvable) (Class.forName(flowProcess
					.getTxnBean()).newInstance());
			flag = approvable.cancel(flowProcess.getTxnType(),
					flowProcess.getTransNo(), bean);
		} catch (Exception e) {
			Log.error(
					"Exception occur at FlowEngineServiceImpl.cancelProcess(String processId, NTBUser user, CibAction bean) :",
					e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			}

		}

		if (flag) {
			flowProcess.setProcStatus(PROCESS_STATUS_CANCEL);
			// Jet added 2008-07-10
			flowProcess.setProcFinishTime(new Date());

			flowProcessDao.update(flowProcess);

			cancelRestWorks(processId);

			backupHis(processId);
		}

	}

	public boolean checkoutWork(String workId, NTBUser user)
			throws NTBException {
		boolean flag = false;

		try {
			Date curTime = new Date();
			String userId = user.getUserId();
			String userName = user.getUserName();

			synchronized (checkoutLock) {
				FlowWork flowWork = (FlowWork) flowWorkDao.load(FlowWork.class,
						workId);
				if (FlowEngineService.WORK_STATUS_NEW.equals(flowWork
						.getWorkStatus())
						|| (FlowEngineService.WORK_STATUS_CHECKOUT
								.equals(flowWork.getWorkStatus()) && userId
								.equals(flowWork.getWorkDealer()))) {
					flowWork.setWorkStatus(WORK_STATUS_CHECKOUT);
					flowWork.setWorkDealer(userId);
					flowWork.setWorkDealerName(userName);
					flowWork.setDealBeginTime(curTime);
					flowWorkDao.update(flowWork);
					flag = true;
				}
			}
		} catch (Exception e) {
			Log.error("Process error at FlowEngineServiceImpl."
					+ "checkoutWork(String workId, NTBUser user): ", e);
		}

		return flag;
	}

	public boolean doWork(String workId, String action, String memo,
			NTBUser user, CibAction bean) throws NTBException {
		boolean actionSucessFlag = true;
		boolean finishFlag = false;
		Date curTime = new Date();

		MailService mailService = (MailService) Config.getAppContext().getBean(
				"MailService");
		List userList = new ArrayList();

		try {
			FlowWork flowWork = (FlowWork) flowWorkDao.load(FlowWork.class,
					workId);

			if (!(WORK_STATUS_CHECKOUT.equals(flowWork.getWorkStatus()) && user
					.getUserId().equals(flowWork.getWorkDealer()))) {
				// by wen 20110321
				Log.info(">>> WorkHolderChanged WorkId: "
						+ flowWork.getWorkId());
				Log.info(">>> Work original Status :"
						+ flowWork.getWorkStatus());
				Log.info(">>> Work original Dealer :"
						+ flowWork.getWorkDealer());
				Log.info(">>> Work actual Dealer :" + user.getUserId());
				// end 20110321
				throw new NTBException("err.flow.WorkHolderChanged");
			}

			FlowWork nextWork = null;

			flowWork.setDealAction(action);
			flowWork.setDealMemo(memo);
			flowWork.setWorkStatus(WORK_STATUS_FINISH);
			flowWork.setWorkDealer(user.getUserId());
			flowWork.setWorkDealerName(user.getUserName());
			flowWork.setDealEndTime(curTime);
			FlowProcess process = flowWork.getFlowProcess();
			process.setLatestDealer(user.getUserId());
			process.setLatestDealerName(user.getUserName());
			process.setLatestDealTime(curTime);

			if (ACTION_REJECT.equals(action)) {
				process.setProcStatus(PROCESS_STATUS_REJECT);
				// Jet added 2008-07-10
				process.setProcFinishTime(curTime);

				String doneStatus = getDoneStatus(flowWork);
				Approvable approvable = (Approvable) (Class.forName(process
						.getTxnBean()).newInstance());
				actionSucessFlag = approvable.reject(process.getTxnType(),
						process.getTransNo(), bean);
				if (actionSucessFlag) {
					finishFlag = true;
					// add by hjs 20070619
					// for sending e-mail to notify those approvers that
					// approved on the same request when one approver reject a
					// request
					List workList = listWorkByProcDealed(process.getProcId(),
							null);
					for (int i = 0; i < workList.size(); i++) {
						String userId = ((Map) workList.get(i)).get(
								"workDealer").toString();
						userList.add(userId);
					}
				}
				process.setApproveStatus(doneStatus);

			} else if (ACTION_APPROVE.equals(action)
					|| ACTION_EXECUTE.equals(action)) {
				int curLevelFinishCount = flowWork.getLevelFinishCount()
						.intValue();
				flowWork.setLevelFinishCount(new Integer(
						curLevelFinishCount + 1));
				String doneStatus = getDoneStatus(flowWork);
				if (doneStatus.equals(process.getApproveRule())) {
					if ("Y".equals(process.getAllowExecutor())
							&& !process.getApproveStatus().equals(
									process.getApproveRule())) {
						process.setProcStatus(PROCESS_STATUS_EXECUTE_PENDING);
						nextWork = new FlowWork();
						String nextWorkId = CibIdGenerator
								.getIdForOperation(ID_TYPE_WORK);
						nextWork.setWorkId(nextWorkId);
						nextWork.setLevelFinishCount(new Integer(0));
						nextWork.setFlowProcess(process);
						nextWork.setWorkCreateTime(curTime);
						nextWork.setWorkCreator(user.getUserId());
						nextWork.setWorkCreatorName(user.getUserName());
						nextWork.setWorkStatus(WORK_STATUS_NEW);

					} else {
						process.setProcStatus(PROCESS_STATUS_FINISH);

						process.setProcFinishTime(curTime);
						Approvable approvable = (Approvable) (Class
								.forName(process.getTxnBean()).newInstance());
						Log.info("process.getTxnType() = " + process.getTxnType()
								+" | process.getTransNo() = " + process.getTransNo()
								+"  | bean = " + bean.getClass()) ;
						
						String transCode = SMSOTPUtil.getTranCode(process.getTxnType(), process.getTransNo());
						SMSOTPUtil.updateTxnCodeToSmsOtpLog(bean.getParameter("smsFlowNo"), transCode);
						actionSucessFlag = approvable.approve(
								process.getTxnType(), process.getTransNo(),
								bean);
						if (actionSucessFlag) {
							finishFlag = true;
							userList.add(process.getProcCreator());
						}
					}

				} else {
					if (APPROVE_TYPE_UNASSIGNED
							.equals(process.getApproveType())) {
						nextWork = new FlowWork();
						String nextWorkId = CibIdGenerator
								.getIdForOperation(ID_TYPE_WORK);
						nextWork.setWorkId(nextWorkId);

						if (TXN_CATEGORY_FINANCE.equals(process
								.getTxnCategory())) {
							String nextWorkLevel = getNextLevel(flowWork);
							nextWork.setApproveLevel(nextWorkLevel);
							if (flowWork.getApproveLevel()
									.equals(nextWorkLevel)) {
								nextWork.setLevelFinishCount(flowWork
										.getLevelFinishCount());
							} else {
								nextWork.setLevelFinishCount(new Integer(0));
							}

						} else {
							nextWork.setLevelFinishCount(flowWork
									.getLevelFinishCount());
						}
						nextWork.setFlowProcess(process);
						nextWork.setWorkCreateTime(curTime);
						nextWork.setWorkCreator(user.getUserId());
						nextWork.setWorkCreatorName(user.getUserName());
						nextWork.setWorkStatus(WORK_STATUS_NEW);
					}
				}
				process.setApproveStatus(doneStatus);
			}

			if (actionSucessFlag) {
				flowWorkDao.update(flowWork);
				flowProcessDao.update(process);

				if (null != nextWork) {
					flowWorkDao.add(nextWork);
					//add by linrui for flowwork 20190724
					//BeanUtils.copyProperties(flowWork, nextWork);
					//flowWorkDao.update(flowWork);
				}

				if (ACTION_REJECT.equals(action)
						&& APPROVE_TYPE_ASSIGNED.equals(process
								.getApproveType())) {
					cancelRestWorks(process.getProcId());
				}
				// 锟斤拷锟斤拷锟斤拷探锟斤拷锟斤拷锟竭憋拷锟杰撅拷锟斤拷锟斤拷锟紿is锟�删锟斤拷原锟斤拷录
				if (finishFlag) {
					backupHis(process.getProcId());
				}
				/*if (userList.size() > 0) {
					Map mailData = bean.getResultData();
					mailData.put("userId", bean.getUser().getUserId());
					mailData.put("userName", bean.getUser().getUserName());
					mailData.put("requestTime", new Date());
					mailData.put("userType", (user instanceof BankUser)?"B":"C");//add by linrui 20180529
					String[] userArray = new String[userList.size()];
					for (int iUser = 0; iUser < userArray.length; iUser++) {
						userArray[iUser] = (String) userList.get(iUser);
					}
					if (FlowEngineService.ACTION_APPROVE.equals(action)) {
						mailService.toRequester_Approved(process.getTxnType(),
								userArray, mailData);
					} else if (FlowEngineService.ACTION_REJECT.equals(action)) {
						mailService.toRequester_Rejected(process.getTxnType(),
								userArray, mailData);
					}
				}*/
			}
		} catch (Exception e) {
			Log.error("Process error at FlowEngineServiceImpl.doWork"
					+ "(String workId, String action, String memo,"
					+ "NTBUser user, CibAction bean): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			}

			actionSucessFlag = false;
		}

		return actionSucessFlag;
	}

	public List listWork(NTBUser user) throws NTBException {
		List works = null;

		try {
			if (user instanceof AbstractCorpUser) {
				CorpUser corpUser = (CorpUser) user;
				String corpType = corpUser.getCorporation().getCorpType();
				String allowFinancialController = corpUser.getCorporation()
						.getAllowFinancialController();
				String financialControllerFlag = corpUser
						.getFinancialControllerFlag();

				if (Constants.ROLE_APPROVER.equals(corpUser.getRoleId())) {
					if (Constants.CORP_TYPE_MIDDLE.equals(corpType)
							|| Constants.CORP_TYPE_SMALL.equals(corpType)) {
						works = flowWorkDao.findByCorpSelf(
								corpUser.getCorpId(), corpUser.getUserId(),
								corpUser.getAuthLevel(),
								allowFinancialController,
								financialControllerFlag);
					} else {
						works = flowWorkDao.findByCorp(corpUser.getCorpId(),
								corpUser.getUserId(), corpUser.getAuthLevel(),
								allowFinancialController,
								financialControllerFlag);
					}
				} else if (Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())) {
					works = flowWorkDao.findByCorpWithoutLevel(
							corpUser.getCorpId(), corpUser.getUserId());

				} else if (Constants.ROLE_ADMINISTRATOR.equals(corpUser
						.getRoleId())) {
					works = flowWorkDao.findByCorp(corpUser.getCorpId(),
							corpUser.getUserId());
				}

			} else {
				AbstractBankUser bankUser = (AbstractBankUser) user;
				if (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
					works = flowWorkDao.findByCorp(CORPID_BANK,
							bankUser.getUserId());
				}
			}
		} catch (Exception e) {
			Log.error("Process error at FlowEngineServiceImpl."
					+ "listWork(NTBUser user): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			}
		}

		if (null != works) {
			works = worksToMaps(works);
		}

		works = addProgressInfo(works, user);

		return works;
	}

	public List listWorkByTrans(String txnType, String transNo, NTBUser user)
			throws NTBException {
		List works = null;

		try {
			FlowProcess flowProcess = flowProcessDao.loadByTrans(txnType,
					transNo);
			if (null != flowProcess) {
				works = flowWorkDao.findByProcIdDealed(flowProcess.getProcId());
			}
		} catch (Exception e) {
			Log.error("Process error at FlowEngineServiceImpl."
					+ "listWorkByTrans(String txnType, "
					+ "String transNo, NTBUser user): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			}

		}
		works = worksToMaps(works);
		return works;
	}

	public String startProcess(String txnType, String txnCategory,
			Class txnBean, String currency, double amount, String toCurrency,
			double toAmount, double amountMopEq, String transNo, String desc,
			NTBUser user, String assignedUser, String allowExecutor,
			String ruleFlag) throws NTBException {

		Date curTime = new Date();
		String procId = CibIdGenerator.getIdForOperation(ID_TYPE_PROCESS);
		String userId = user.getUserId();
		String userName = user.getUserName();
		String workId = null;
		String corpId = null;
		String approveRuleStr = null;
		String approveStatusStr = null;
		String approveType = null;

		FlowProcess flowProcess = new FlowProcess();
		Log.debug("New Process: procId=" + procId + ", txnType=" + txnType
				+ ", transNo=" + transNo);
		flowProcess.setProcId(procId);
		flowProcess.setTxnType(txnType);
		flowProcess.setTxnCategory(txnCategory);
		flowProcess.setTxnBean(txnBean.getName());
		flowProcess.setCurrency(currency);
		flowProcess.setAmount(new Double(amount));
		flowProcess.setToCurrency(toCurrency);
		flowProcess.setToAmount(new Double(toAmount));
		flowProcess.setTransNo(transNo);
		flowProcess.setTransDesc(desc);
		flowProcess.setAllowExecutor(allowExecutor);
		if (null == ruleFlag) {
			flowProcess.setRuleFlag(FlowEngineService.RULE_FLAG_FROM);
		} else {
			flowProcess.setRuleFlag(ruleFlag);
		}

		if (null == assignedUser || "".equals(assignedUser)) {
			approveType = APPROVE_TYPE_UNASSIGNED;
		} else {
			flowProcess.setApprovers(assignedUser);
			approveType = APPROVE_TYPE_ASSIGNED;
		}
		flowProcess.setApproveType(approveType);
		flowProcess.setProcCreator(userId);
		flowProcess.setProcCreatorName(userName);
		flowProcess.setProcCreateTime(curTime);
		flowProcess.setLatestDealer(userId);
		flowProcess.setLatestDealerName(userName);
		flowProcess.setLatestDealTime(curTime);
		flowProcess.setProcStatus(PROCESS_STATUS_NEW);

		if (user instanceof AbstractCorpUser) {
			corpId = ((AbstractCorpUser) user).getCorpId();
		} else {
			corpId = CORPID_BANK;
		}

		flowProcess.setCorpId(corpId);

		if (TXN_CATEGORY_FINANCE.equals(txnCategory)) {
			TxnSubtype txnSubtype = (TxnSubtype) txnSubtypeDao.load(
					TxnSubtype.class, txnType);

			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");

			ApproveRule approveRule = null;

			if (null != txnSubtype) {
				if (ruleFlag == null) {
					ruleFlag = RULE_FLAG_FROM;
				}
				if (RULE_FLAG_FROM.equals(ruleFlag)) {
					approveRule = approveRuleService.getApproveRule(
							txnSubtype.getTxnType(), corpId, currency, amount,
							amountMopEq);
				} else if (RULE_FLAG_TO.equals(ruleFlag)) {
					approveRule = approveRuleService.getApproveRule(
							txnSubtype.getTxnType(), corpId, toCurrency,
							toAmount, amountMopEq);
				}
			} else {
				throw new NTBException("err.flow.TxnTypeNotDefined");
			}

			// Jet modify: only for middle, middle without adms, small company,
			// not by 1 approver any more 2007-08-22
			int aCount = approveRule.getLevelA() == null ? 0 : approveRule
					.getLevelA().intValue();
			int bCount = approveRule.getLevelB() == null ? 0 : approveRule
					.getLevelB().intValue();
			int cCount = approveRule.getLevelC() == null ? 0 : approveRule
					.getLevelC().intValue();
			int dCount = approveRule.getLevelD() == null ? 0 : approveRule
					.getLevelD().intValue();
			int eCount = approveRule.getLevelE() == null ? 0 : approveRule
					.getLevelE().intValue();
			int fCount = approveRule.getLevelF() == null ? 0 : approveRule
					.getLevelF().intValue();
			int gCount = approveRule.getLevelG() == null ? 0 : approveRule
					.getLevelG().intValue();
			/*if (Constants.TXN_SUBTYPE_STOP_CHEQUE.equals(txnType)
					|| Constants.TXN_SUBTYPE_CHEQUE_PROTECTION.equals(txnType)
					|| Constants.TXN_SUBTYPE_AUTOPAYMENT_ADD.equals(txnType)
					|| Constants.TXN_SUBTYPE_AUTOPAYMENT_EDIT.equals(txnType)
					|| Constants.TXN_SUBTYPE_AUTOPAYMENT_DELETE.equals(txnType)
					|| Constants.TXN_SUBTYPE_CHEQUE_BOOK_REQUEST.equals(txnType)
			) {
				if (user instanceof CorpUser) {
					CorpUser corpUser = (CorpUser) user;
					// int approverCount = corpUser.getCorpApproverCount();
					String corpType = corpUser.getCorporation().getCorpType();
					if (Constants.CORP_TYPE_SMALL.equals(corpType)
							|| Constants.CORP_TYPE_MIDDLE.equals(corpType)
							|| Constants.CORP_TYPE_MIDDLE_NO_ADMIN
									.equals(corpType)) {
						// if (approverCount == 1) {
						aCount = 0;
						bCount = 0;
						cCount = 0;
						dCount = 0;
						eCount = 0;
						fCount = 0;
						gCount = 1;
						// }
					}
				}
			}*/

			if (null != approveRule) {

				StringBuffer approveRuleBuffer = new StringBuffer();
				StringBuffer approveStatusBuffer = new StringBuffer();

				if (RULE_TYPE_SINGLE.equals(approveRule.getRuleType())) {
					approveRuleBuffer.append(approveRule.getSingleLevel())
							.append("1");
					approveStatusBuffer.append(approveRule.getSingleLevel())
							.append("0");
				} else {
					boolean beginFlag = false;
					// 锟斤拷装
					if (aCount > 0) {
						if (beginFlag) {
							approveRuleBuffer.append("-");
							approveStatusBuffer.append("-");
						}
						approveRuleBuffer.append("A").append(aCount);
						approveStatusBuffer.append("A").append(0);
						beginFlag = true;
					}
					if (bCount > 0) {
						if (beginFlag) {
							approveRuleBuffer.append("-");
							approveStatusBuffer.append("-");
						}
						approveRuleBuffer.append("B").append(bCount);
						approveStatusBuffer.append("B").append(0);
						beginFlag = true;
					}
					if (cCount > 0) {
						if (beginFlag) {
							approveRuleBuffer.append("-");
							approveStatusBuffer.append("-");
						}
						approveRuleBuffer.append("C").append(cCount);
						approveStatusBuffer.append("C").append(0);
						beginFlag = true;
					}
					if (dCount > 0) {
						if (beginFlag) {
							approveRuleBuffer.append("-");
							approveStatusBuffer.append("-");
						}
						approveRuleBuffer.append("D").append(dCount);
						approveStatusBuffer.append("D").append(0);
						beginFlag = true;
					}
					if (eCount > 0) {
						if (beginFlag) {
							approveRuleBuffer.append("-");
							approveStatusBuffer.append("-");
						}
						approveRuleBuffer.append("E").append(eCount);
						approveStatusBuffer.append("E").append(0);
						beginFlag = true;
					}
					if (fCount > 0) {
						if (beginFlag) {
							approveRuleBuffer.append("-");
							approveStatusBuffer.append("-");
						}
						approveRuleBuffer.append("F").append(fCount);
						approveStatusBuffer.append("F").append(0);
						beginFlag = true;
					}
					if (gCount > 0) {
						if (beginFlag) {
							approveRuleBuffer.append("-");
							approveStatusBuffer.append("-");
						}
						approveRuleBuffer.append("G").append(gCount);
						approveStatusBuffer.append("G").append(0);
						beginFlag = true;
					}

				}
				approveRuleStr = approveRuleBuffer.toString();
				approveStatusStr = approveStatusBuffer.toString();
				flowProcess.setApproveRule(approveRuleStr);
				flowProcess.setApproveStatus(approveStatusStr);
			}
		} else {
			String aRule = null;
			try {
				RBFactory noneFinanceRule = RBFactory
						.getInstance("noneFinanceRule");
				aRule = noneFinanceRule.getString(txnType);
			} catch (Exception e) {
				throw new NTBException("err.flow.NoneFinanceRuleNotDefined");
			}

			if (!txnType.equals(aRule)) {
				flowProcess.setApproveRule(aRule);
			} else {
				throw new NTBException("err.flow.NoneFinanceRuleNotDefined");
			}

			flowProcess.setApproveStatus("0");
		}

		flowProcessDao.add(flowProcess);

		FlowWork firstWork = new FlowWork();
		firstWork.setFlowProcess(flowProcess);
		firstWork.setLevelFinishCount(new Integer(0));
		firstWork.setWorkCreator(userId);
		firstWork.setWorkCreateTime(curTime);
		firstWork.setWorkStatus(WORK_STATUS_FINISH);
		firstWork.setWorkCreator(userId);
		firstWork.setWorkCreatorName(userName);
		firstWork.setWorkCreateTime(curTime);
		firstWork.setWorkDealer(userId);
		firstWork.setWorkDealerName(userName);
		firstWork.setDealAction(ACTION_REQUEST);
		firstWork.setDealBeginTime(curTime);
		firstWork.setDealEndTime(curTime);
		firstWork.setDealMemo(desc);
		workId = CibIdGenerator.getIdForOperation(ID_TYPE_WORK);
		firstWork.setWorkId(workId);
		flowWorkDao.add(firstWork);

		if (CORPID_BANK.equals(corpId)) {
			FlowWork flowWork = new FlowWork();
			flowWork.setFlowProcess(flowProcess);
			flowWork.setLevelFinishCount(new Integer(0));
			flowWork.setWorkCreator(userId);
			flowWork.setWorkCreatorName(userName);
			flowWork.setWorkCreateTime(curTime);
			flowWork.setWorkStatus(WORK_STATUS_NEW);
			workId = CibIdGenerator.getIdForOperation(ID_TYPE_WORK);
			flowWork.setWorkId(workId);
			flowWorkDao.add(flowWork);
		} else {
			if (TXN_CATEGORY_FINANCE.equals(txnCategory)) {
				List approveItemList = extractRuleStr(approveRuleStr);

				HashMap approveItem = null;
				String approveLevel = null;

				if (APPROVE_TYPE_ASSIGNED.equals(approveType)) {

					int approveCount = 0;
					StrTokenizer approverTokens = new StrTokenizer(
							assignedUser, ";");

					for (int i = 0; i < approveItemList.size(); i++) {
						approveItem = (HashMap) approveItemList.get(i);
						approveLevel = (String) approveItem.get("Level");
						approveCount = Integer.parseInt((String) approveItem
								.get("Count"));

						for (int j = 0; j < approveCount; j++) {
							FlowWork flowWork = new FlowWork();
							flowWork.setFlowProcess(flowProcess);
							flowWork.setLevelFinishCount(new Integer(0));
							flowWork.setWorkCreator(userId);
							flowWork.setWorkCreatorName(userName);
							flowWork.setWorkCreateTime(curTime);
							flowWork.setWorkStatus(WORK_STATUS_NEW);
							flowWork.setApproveLevel(approveLevel);

							// 锟斤拷锟街革拷锟斤拷锟斤拷没锟絀D锟斤拷锟斤拷疲锟斤拷锟叫达拷锟絝lowWork
							String assignedUserId = approverTokens.nextToken();
							/*
							//modified by lzg for GAPMC-EB-001-0040
							/*CorpUser corpUserObj = (CorpUser) corpUserDao.load(
									CorpUser.class, assignedUserId);
							CorpUser corpUserObj = null;
							Object[] conditionObj = {assignedUserId,flowProcess.getCorpId()};
							String selectCorpUser = "select * from corp_user where user_id = ? and corp_id = ?";
							List retList = genericJdbcDao.load(CorpUser.class, conditionObj, selectCorpUser);
							if(retList !=null && retList.size() == 1){
								corpUserObj = (CorpUser)retList.get(0);
							}
							//modified by lzg end 
							*/
							CorpUser corpUserObj = (CorpUser) corpUserDao.loadByUserIdAndCoprId(assignedUserId, flowProcess.getCorpId());
							
							String assignedUserName = corpUserObj.getUserName();

							flowWork.setAssignedDealer(assignedUserId);
							flowWork.setWorkDealer(assignedUserId);
							flowWork.setWorkDealerName(assignedUserName);

							workId = CibIdGenerator
									.getIdForOperation(ID_TYPE_WORK);
							flowWork.setWorkId(workId);
							flowWorkDao.add(flowWork);
						}
					}

				} else {
					approveItem = (HashMap) approveItemList.get(approveItemList
							.size() - 1);
					approveLevel = (String) approveItem.get("Level");
					FlowWork flowWork = new FlowWork();
					flowWork.setFlowProcess(flowProcess);
					flowWork.setLevelFinishCount(new Integer(0));
					flowWork.setWorkCreator(userId);
					flowWork.setWorkCreatorName(userName);
					flowWork.setWorkCreateTime(curTime);
					flowWork.setWorkStatus(WORK_STATUS_NEW);
					flowWork.setApproveLevel(approveLevel);
					workId = CibIdGenerator.getIdForOperation(ID_TYPE_WORK);
					flowWork.setWorkId(workId);

					flowWorkDao.add(flowWork);

				}

			} else {
				workId = CibIdGenerator.getIdForOperation(ID_TYPE_WORK);
				FlowWork flowWork = new FlowWork();
				flowWork.setFlowProcess(flowProcess);
				flowWork.setLevelFinishCount(new Integer(0));
				flowWork.setWorkCreator(userId);
				flowWork.setWorkCreatorName(userName);
				flowWork.setWorkCreateTime(curTime);
				flowWork.setWorkStatus(WORK_STATUS_NEW);
				flowWork.setWorkId(workId);
				flowWorkDao.add(flowWork);
			}
		}

		return procId;
	}

	public boolean undoCheckoutWork(String workId, String userId)
			throws NTBException {
		boolean flag = false;

		if (null != userId && null != workId) {
			synchronized (checkoutLock) {
				try {
					FlowWork flowWork = (FlowWork) flowWorkDao.load(
							FlowWork.class, workId);

					if (null != flowWork
							&& WORK_STATUS_CHECKOUT.equals(flowWork
									.getWorkStatus())
							&& userId.equals(flowWork.getWorkDealer())) {
						flowWork.setWorkStatus(WORK_STATUS_NEW);
						flowWork.setWorkDealer(null);
						flowWork.setWorkDealerName(null);
						flowWork.setDealBeginTime(null);
						flowWorkDao.update(flowWork);
						flag = true;
					}
				} catch (Exception e) {
					Log.error("Process error at FlowEngineServiceImpl."
							+ "undoCheckoutWork(String workId, "
							+ "String userId): ", e);
				}
			}
		}
		return flag;
	}

	public FlowProcessDao getFlowProcessDao() {
		return flowProcessDao;
	}

	public void setFlowProcessDao(FlowProcessDao flowProcessDao) {
		this.flowProcessDao = flowProcessDao;
	}
	
	//add by lzg for GAPMC-EB-001-0040
	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}
	
	//add by lzg end
	
	public FlowWorkDao getFlowWorkDao() {
		return flowWorkDao;
	}

	public void setFlowWorkDao(FlowWorkDao flowWorkDao) {
		this.flowWorkDao = flowWorkDao;
	}

	public FlowWork viewWork(String workId, NTBUser user) throws NTBException {
		// return (FlowWork) flowWorkDao.load(FlowWork.class, workId);
		// Jet modify 2008-10-28 锟斤拷锟絝lowProcess锟揭诧拷锟斤拷锟斤拷录锟斤拷去锟斤拷锟斤拷史锟斤拷
		FlowWork flowWork = (FlowWork) flowWorkDao.get(FlowWork.class, workId);
		if (flowWork == null) {
			FlowWorkHis flowWorkHis = (FlowWorkHis) flowWorkHisDao.load(
					FlowWorkHis.class, workId);
			FlowWork flowWorkNew = new FlowWork();
			if (flowWorkHis != null) {
				FlowProcess flowProcessNew = new FlowProcess();
				FlowProcessHis flowProcessHis = flowWorkHis.getFlowProcessHis();
				if (flowProcessHis != null) {
					flowProcessNew.setProcId(flowProcessHis.getProcId());
					flowProcessNew.setAllowExecutor(flowProcessHis
							.getAllowExecutor());
					flowProcessNew.setAmount(flowProcessHis.getAmount());
					flowProcessNew.setApprovers(flowProcessHis.getApprovers());
					flowProcessNew.setApproveRule(flowProcessHis
							.getApproveRule());
					flowProcessNew.setApproveStatus(flowProcessHis
							.getApproveStatus());
					flowProcessNew.setApproveType(flowProcessHis
							.getApproveType());
					flowProcessNew.setCorpId(flowProcessHis.getCorpId());
					flowProcessNew.setCurrency(flowProcessHis.getCurrency());
					flowProcessNew.setLatestDealer(flowProcessHis
							.getLatestDealer());
					flowProcessNew.setLatestDealerName(flowProcessHis
							.getLatestDealerName());
					flowProcessNew.setLatestDealTime(flowProcessHis
							.getLatestDealTime());
					flowProcessNew.setProcCreateTime(flowProcessHis
							.getProcCreateTime());
					flowProcessNew.setProcCreator(flowProcessHis
							.getProcCreator());
					flowProcessNew.setProcCreatorName(flowProcessHis
							.getProcCreatorName());
					flowProcessNew.setProcFinishTime(flowProcessHis
							.getProcFinishTime());
					flowProcessNew
							.setProcStatus(flowProcessHis.getProcStatus());
					flowProcessNew.setRuleFlag(flowProcessHis.getRuleFlag());
					flowProcessNew.setToAmount(flowProcessHis.getToAmount());
					flowProcessNew
							.setToCurrency(flowProcessHis.getToCurrency());
					flowProcessNew.setTransDesc(flowProcessHis.getTransDesc());
					flowProcessNew.setTransNo(flowProcessHis.getTransNo());
					flowProcessNew.setTxnBean(flowProcessHis.getTxnBean());
					flowProcessNew.setTxnCategory(flowProcessHis
							.getTxnCategory());
					flowProcessNew.setTxnType(flowProcessHis.getTxnType());
				}
				flowWorkNew.setWorkId(flowWorkHis.getWorkId());
				flowWorkNew.setApproveLevel(flowWorkHis.getApproveLevel());
				flowWorkNew.setAssignedDealer(flowWorkHis.getAssignedDealer());
				flowWorkNew.setDealAction(flowWorkHis.getDealAction());
				flowWorkNew.setDealBeginTime(flowWorkHis.getDealBeginTime());
				flowWorkNew.setDealEndTime(flowWorkHis.getDealEndTime());
				flowWorkNew.setDealMemo(flowWorkHis.getDealMemo());
				flowWorkNew.setLevelFinishCount(flowWorkHis
						.getLevelFinishCount());
				flowWorkNew.setFlowProcess(flowProcessNew);
				flowWorkNew.setWorkCreateTime(flowWorkHis.getWorkCreateTime());
				flowWorkNew.setWorkCreator(flowWorkHis.getWorkCreator());
				flowWorkNew
						.setWorkCreatorName(flowWorkHis.getWorkCreatorName());
				flowWorkNew.setWorkDealer(flowWorkHis.getWorkDealer());
				flowWorkNew.setWorkDealerName(flowWorkHis.getWorkDealerName());
				flowWorkNew.setWorkStatus(flowWorkHis.getWorkStatus());
			}
			return flowWorkNew;
		} else {
			return flowWork;
		}
	}

	public FlowWorkHis viewWorkHis(String workId, NTBUser user)
			throws NTBException {
		return (FlowWorkHis) flowWorkDao.load(FlowWorkHis.class, workId);
	}

	public List listWorkByUserAll(NTBUser user, Date fromDate, Date toDate)
			throws NTBException {
		List works = null;

		if (user instanceof AbstractCorpUser) {
			AbstractCorpUser corpUser = (AbstractCorpUser) user;
			if (Constants.ROLE_APPROVER.equals(corpUser.getRoleId())
					|| Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_ADMINISTRATOR
							.equals(corpUser.getRoleId())) {
				works = flowWorkDao.findByUserAll(corpUser.getCorpId(),
						corpUser.getUserId(), fromDate, toDate);
			}
		} else {
			AbstractBankUser bankUser = (AbstractBankUser) user;
			if (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
				works = flowWorkDao.findByUserAll(CORPID_BANK,
						bankUser.getUserId(), fromDate, toDate);
			}
		}

		if (null != works) {
			works = worksToMaps(works);
		}

		works = addProgressInfo(works, user);

		return works;
	}

	// add by hjs 20070123
	public List listWorkByUserAllForReport(NTBUser user, Date fromDate,
			Date toDate) throws NTBException {
		List works = null;

		if (user instanceof AbstractCorpUser) {
			AbstractCorpUser corpUser = (AbstractCorpUser) user;
			if (Constants.ROLE_APPROVER.equals(corpUser.getRoleId())
					|| Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_ADMINISTRATOR
							.equals(corpUser.getRoleId())) {
				works = flowWorkDao.findByUserAllForReport(
						corpUser.getCorpId(), corpUser.getUserId(), fromDate,
						toDate);
			}
		} else {
			AbstractBankUser bankUser = (AbstractBankUser) user;
			if (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
				works = flowWorkDao.findByUserAllForReport(CORPID_BANK,
						bankUser.getUserId(), fromDate, toDate);
			}
		}

		if (null != works) {
			works = worksToMaps(works);
		}

		works = addProgressInfo(works, user);

		return works;
	}
	
	//add by lzg 20190816
	public List listWorkByUserExpired(NTBUser user, String userId,
			String dateFrom, String dateTo) throws NTBException {
		List works = null;
		
		if (user instanceof AbstractCorpUser) {
			AbstractCorpUser corpUser = (AbstractCorpUser) user;
			if (Constants.ROLE_OPERATOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_APPROVER.equals(corpUser.getRoleId())
					|| Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_ADMINISTRATOR
							.equals(corpUser.getRoleId())) {
				if (!CorpUser.VIEW_ALL_TRANS_ON.equals(corpUser
						.getViewAllTransFlag())) {
					works = flowWorkDao.findByUserExpired(corpUser.getCorpId(),
							corpUser.getUserId());
					
				}else{
					works = flowWorkDao.findByUserExpired(corpUser.getCorpId(), userId, dateFrom, dateTo);
				}
			}
		}else{
			AbstractBankUser bankUser = (AbstractBankUser) user;
			if (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
				works = flowWorkDao.findByUserDealing(CORPID_BANK,
						bankUser.getUserId());
			}
		}
		if (null != works) {
			works = worksToMaps(works);
		}
		works = addProgressInfo(works, user);

		return works;
	}
	//add by lzg end
	//add by linrui 20190916 for cr589
	public List listWorkByUserExpired(NTBUser user, String userId,
			String dateFrom, String dateTo, String sortOrder) throws NTBException {
		List works = null;
		
		if (user instanceof AbstractCorpUser) {
			AbstractCorpUser corpUser = (AbstractCorpUser) user;
			if (Constants.ROLE_OPERATOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_APPROVER.equals(corpUser.getRoleId())
					|| Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_ADMINISTRATOR
					.equals(corpUser.getRoleId())) {
				if (!CorpUser.VIEW_ALL_TRANS_ON.equals(corpUser
						.getViewAllTransFlag())) {
					works = flowWorkDao.findByUserExpired(corpUser.getCorpId(),
							corpUser.getUserId(), sortOrder);
					
				}else{
					works = flowWorkDao.findByUserExpired(corpUser.getCorpId(), userId, dateFrom, dateTo, sortOrder);
				}
			}
		}else{
			AbstractBankUser bankUser = (AbstractBankUser) user;
			if (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
				works = flowWorkDao.findByUserDealing(CORPID_BANK,
						bankUser.getUserId(), sortOrder);
			}
		}
		if (null != works) {
			works = worksToMaps(works);
		}
		works = addProgressInfo(works, user);
		
		return works;
	}
	//add by linrui 20190916 for cr589
	
	
	
	public List listWorkByUserDealing(NTBUser user, String userId,
			String dateFrom, String dateTo) throws NTBException {
		List works = null;

		if (user instanceof AbstractCorpUser) {
			AbstractCorpUser corpUser = (AbstractCorpUser) user;
			if (Constants.ROLE_OPERATOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_APPROVER.equals(corpUser.getRoleId())
					|| Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_ADMINISTRATOR
							.equals(corpUser.getRoleId())) {
				if (!CorpUser.VIEW_ALL_TRANS_ON.equals(corpUser
						.getViewAllTransFlag())) {
					works = flowWorkDao.findByUserDealing(corpUser.getCorpId(),
							corpUser.getUserId());
				} else {
					works = flowWorkDao.findByUserDealing(corpUser.getCorpId(),
							userId, dateFrom, dateTo);
				}
			}

		} else {
			AbstractBankUser bankUser = (AbstractBankUser) user;
			if (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
				works = flowWorkDao.findByUserDealing(CORPID_BANK,
						bankUser.getUserId());
			}
		}

		if (null != works) {
			works = worksToMaps(works);
		}

		works = addProgressInfo(works, user);

		return works;
	}
	//add by linrui 20190916 for cr589
	public List listWorkByUserDealing(NTBUser user, String userId,
			String dateFrom, String dateTo, String sortOrder) throws NTBException {
		List works = null;
		if (user instanceof AbstractCorpUser) {
			AbstractCorpUser corpUser = (AbstractCorpUser) user;
			if (Constants.ROLE_OPERATOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_APPROVER.equals(corpUser.getRoleId())
					|| Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_ADMINISTRATOR
					.equals(corpUser.getRoleId())) {
				if (!CorpUser.VIEW_ALL_TRANS_ON.equals(corpUser
						.getViewAllTransFlag())) {
					works = flowWorkDao.findByUserDealing(corpUser.getCorpId(),
							corpUser.getUserId() ,sortOrder);
				} else {
					works = flowWorkDao.findByUserDealing(corpUser.getCorpId(),
							userId, dateFrom, dateTo, sortOrder);
				}
			}
			
		} else {
			AbstractBankUser bankUser = (AbstractBankUser) user;
			if (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
				works = flowWorkDao.findByUserDealing(CORPID_BANK,
						bankUser.getUserId(), sortOrder);
			}
		}
		
		if (null != works) {
			works = worksToMaps(works);
		}
		
		works = addProgressInfo(works, user);
		
		return works;
	}

	public List listWorkByUserDealed(NTBUser user, String userId,
			String procStatus, String dateFrom, String dateTo)
			throws NTBException {
		List works = null;

		if (user instanceof AbstractCorpUser) {
			AbstractCorpUser corpUser = (AbstractCorpUser) user;
			if (Constants.ROLE_OPERATOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_APPROVER.equals(corpUser.getRoleId())
					|| Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_ADMINISTRATOR
							.equals(corpUser.getRoleId())) {
				if (!CorpUser.VIEW_ALL_TRANS_ON.equals(corpUser
						.getViewAllTransFlag())) {
					/*
					 * works =
					 * flowWorkDao.findByUserDealing(corpUser.getCorpId(),
					 * corpUser.getUserId());
					 */
				} else {
					works = flowWorkHisDao.listRecords(corpUser.getCorpId(),
							userId, procStatus, dateFrom, dateTo);
				}
			}

		} else {
			/*
			 * AbstractBankUser bankUser = (AbstractBankUser) user; if
			 * (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
			 * works = flowWorkDao.findByUserDealing(CORPID_BANK, bankUser
			 * .getUserId()); }
			 */
		}

		if (null != works) {
			works = worksToMaps(works);
		}

		works = addProgressInfo(works, user);

		return works;
	}
	//add by linrui 20190916 for cr589
	public List listWorkByUserDealed(NTBUser user, String userId,
			String procStatus, String dateFrom, String dateTo, String sortOrder)
					throws NTBException {
		List works = null;
		
		if (user instanceof AbstractCorpUser) {
			AbstractCorpUser corpUser = (AbstractCorpUser) user;
			if (Constants.ROLE_OPERATOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_APPROVER.equals(corpUser.getRoleId())
					|| Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_ADMINISTRATOR
					.equals(corpUser.getRoleId())) {
				if (!CorpUser.VIEW_ALL_TRANS_ON.equals(corpUser
						.getViewAllTransFlag())) {
					/*
					 * works =
					 * flowWorkDao.findByUserDealing(corpUser.getCorpId(),
					 * corpUser.getUserId());
					 */
				} else {
					works = flowWorkHisDao.listRecords(corpUser.getCorpId(),
							userId, procStatus, dateFrom, dateTo, sortOrder);
				}
			}
			
		} else {
			/*
			 * AbstractBankUser bankUser = (AbstractBankUser) user; if
			 * (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
			 * works = flowWorkDao.findByUserDealing(CORPID_BANK, bankUser
			 * .getUserId()); }
			 */
		}
		
		if (null != works) {
			works = worksToMaps(works);
		}
		
		works = addProgressInfo(works, user);
		
		return works;
	}

	public TxnSubtypeDao getTxnSubtypeDao() {
		return txnSubtypeDao;
	}

	public void setTxnSubtypeDao(TxnSubtypeDao txnSubtypeDao) {
		this.txnSubtypeDao = txnSubtypeDao;
	}

	public FlowProcess viewFlowProcess(String txnType, String transNo)
			throws NTBException {
		return flowProcessDao.loadByTrans(txnType, transNo);
	}

	// 锟睫革拷指锟斤拷锟斤拷权锟斤拷
	public boolean changeAssignedUser(String procId, String assignedUser,
			CibAction bean) throws NTBException {
		boolean flag = true;

		MailService mailService = (MailService) Config.getAppContext().getBean(
				"MailService");
		List userList = new ArrayList();
		// For mail content
		String originalUserList = "";
		String newUserList = "";

		try {
			FlowProcess flowProcess = (FlowProcess) flowProcessDao.load(
					FlowProcess.class, procId);
			if (null != flowProcess
					&& null != flowProcess.getProcId()
					&& null != assignedUser
					&& !assignedUser.equals(flowProcess.getApprovers())
					&& !flowProcess.getApproveRule().equals(
							flowProcess.getApproveStatus())) {
				flowProcess.setApprovers(assignedUser);
				flowProcessDao.update(flowProcess);

				List works = flowWorkDao.findByProcIdAll(procId);
				StrTokenizer approverTokens = new StrTokenizer(assignedUser,
						";");

				FlowWork flowWork = null;
				String curUser = null;
				if (null != works && works.size() > 1) {
					for (int i = 1; i < works.size(); i++) {
						curUser = approverTokens.nextToken();
						flowWork = (FlowWork) works.get(i);
						// 锟斤拷锟皆拷锟饺拷锟斤拷斜锟�
						originalUserList += flowWork.getAssignedDealer()
								+ " - " + flowWork.getWorkDealerName() + "<br>";
						if (!curUser.equals(flowWork.getAssignedDealer())
								&& (FlowEngineService.WORK_STATUS_NEW
										.equals(flowWork.getWorkStatus()) || FlowEngineService.WORK_STATUS_CHECKOUT
										.equals(flowWork.getWorkStatus()))) {
							// 锟斤拷臃锟斤拷始锟斤拷亩锟斤拷锟�
							userList.add(flowWork.getAssignedDealer());
							userList.add(curUser);
							// 锟斤拷锟街革拷锟斤拷锟斤拷没锟絀D锟斤拷锟斤拷锟�
							flowWork.setAssignedDealer(curUser);
							flowWork.setWorkStatus(FlowEngineService.WORK_STATUS_NEW);

							/*CorpUser corpUserObj = (CorpUser) corpUserDao.load(
									CorpUser.class, curUser);*/
							//mod by linrui 20190718
							CorpUser corpUserObj = (CorpUser) corpUserDao.loadByUserIdAndCoprId(curUser, flowProcess.getCorpId());
							
							String assignedUserName = corpUserObj.getUserName();
							flowWork.setWorkDealer(curUser);
							flowWork.setWorkDealerName(assignedUserName);
							flowWork.setDealBeginTime(null);
							flowWorkDao.update(flowWork);
						}
						// 锟斤拷锟斤拷锟斤拷薷牡锟斤拷锟饺拷锟斤拷斜锟�
						newUserList += flowWork.getAssignedDealer() + " - "
								+ flowWork.getWorkDealerName() + "<br>";
					}

				}
				// by wen 20110321
				Log.info("changeAssignedUser() >> originalUserList: "
						+ originalUserList);
				Log.info("changeAssignedUser() >> newUserList: " + newUserList);
				// end 20110321
			}

			if (flag && userList.size() > 0) {
				// 锟斤拷锟绞硷拷
				Map mailData = bean.getResultData();
				NTBUser curUserObj = bean.getUser();
				mailData.put("userId", curUserObj.getUserId());
				mailData.put("userName", curUserObj.getUserName());
				mailData.put("newUserList", newUserList);
				mailData.put("originalUserList", originalUserList);
				mailData.put("userType", (bean.getUser() instanceof BankUser)?"B":"C");//add by linrui 20180529
				String[] userArray = new String[userList.size()];
				for (int iUser = 0; iUser < userArray.length; iUser++) {
					userArray[iUser] = (String) userList.get(iUser);
				}
				mailService.toApprover_Changed(flowProcess.getTxnType(),
						userArray, mailData);
			}
		} catch (Exception e) {
			Log.error("Process error at FlowEngineServiceImpl."
					+ "changeAssignedUser(String procId,"
					+ " String assignedUser): ", e);

			if (e instanceof NTBException) {
				throw (NTBException) e;
			}

			flag = false;
		}

		return flag;
	}

	public Map viewFlowProcessByBatch(String[] procIds) throws NTBException {
		// return (FlowProcess) flowProcessDao.load(FlowProcess.class, procId);
		// Jet modify 2008-10-28 锟斤拷锟絝lowProcess锟揭诧拷锟斤拷锟斤拷录锟斤拷去锟斤拷锟斤拷史锟斤拷
		List flowProcessList = flowProcessDao.loadByProcIds(procIds);
		Map returnMap = new LinkedHashMap();
		for (int i = 0; i < flowProcessList.size(); i++) {
			FlowProcess processObj = (FlowProcess) flowProcessList.get(i);
			returnMap.put(processObj.getProcId(), processObj);
		}
		String[] hisProcIds = new String[procIds.length - returnMap.size()];
		int count = 0;
		for (int i = 0; i < procIds.length; i++) {
			if (!returnMap.containsKey(procIds[i])) {
				hisProcIds[count++] = procIds[i];
			}
		}

		List flowProcessHisList = flowProcessHisDao.loadByProcIds(procIds);
		for (int i = 0; i < flowProcessHisList.size(); i++) {
			FlowProcessHis flowProcessHis = (FlowProcessHis) flowProcessHisList
					.get(i);
			FlowProcess flowProcessNew = new FlowProcess();
			if (flowProcessHis != null) {
				flowProcessNew.setProcId(flowProcessHis.getProcId());
				flowProcessNew.setAllowExecutor(flowProcessHis
						.getAllowExecutor());
				flowProcessNew.setAmount(flowProcessHis.getAmount());
				flowProcessNew.setApprovers(flowProcessHis.getApprovers());
				flowProcessNew.setApproveRule(flowProcessHis.getApproveRule());
				flowProcessNew.setApproveStatus(flowProcessHis
						.getApproveStatus());
				flowProcessNew.setApproveType(flowProcessHis.getApproveType());
				flowProcessNew.setCorpId(flowProcessHis.getCorpId());
				flowProcessNew.setCurrency(flowProcessHis.getCurrency());
				flowProcessNew
						.setLatestDealer(flowProcessHis.getLatestDealer());
				flowProcessNew.setLatestDealerName(flowProcessHis
						.getLatestDealerName());
				flowProcessNew.setLatestDealTime(flowProcessHis
						.getLatestDealTime());
				flowProcessNew.setProcCreateTime(flowProcessHis
						.getProcCreateTime());
				flowProcessNew.setProcCreator(flowProcessHis.getProcCreator());
				flowProcessNew.setProcCreatorName(flowProcessHis
						.getProcCreatorName());
				flowProcessNew.setProcFinishTime(flowProcessHis
						.getProcFinishTime());
				flowProcessNew.setProcStatus(flowProcessHis.getProcStatus());
				flowProcessNew.setRuleFlag(flowProcessHis.getRuleFlag());
				flowProcessNew.setToAmount(flowProcessHis.getToAmount());
				flowProcessNew.setToCurrency(flowProcessHis.getToCurrency());
				flowProcessNew.setTransDesc(flowProcessHis.getTransDesc());
				flowProcessNew.setTransNo(flowProcessHis.getTransNo());
				flowProcessNew.setTxnBean(flowProcessHis.getTxnBean());
				flowProcessNew.setTxnCategory(flowProcessHis.getTxnCategory());
				flowProcessNew.setTxnType(flowProcessHis.getTxnType());
			}
			returnMap.put(flowProcessNew.getProcId(), flowProcessNew);

		}
		return returnMap;
	}

	public FlowProcess viewFlowProcess(String procId) throws NTBException {
		// return (FlowProcess) flowProcessDao.load(FlowProcess.class, procId);
		// Jet modify 2008-10-28 锟斤拷锟絝lowProcess锟揭诧拷锟斤拷锟斤拷录锟斤拷去锟斤拷锟斤拷史锟斤拷
		FlowProcess flowProcess = (FlowProcess) flowProcessDao.get(
				FlowProcess.class, procId);
		if (flowProcess == null) {
			FlowProcessHis flowProcessHis = (FlowProcessHis) flowProcessHisDao
					.load(FlowProcessHis.class, procId);
			FlowProcess flowProcessNew = new FlowProcess();
			if (flowProcessHis != null) {
				flowProcessNew.setProcId(flowProcessHis.getProcId());
				flowProcessNew.setAllowExecutor(flowProcessHis
						.getAllowExecutor());
				flowProcessNew.setAmount(flowProcessHis.getAmount());
				flowProcessNew.setApprovers(flowProcessHis.getApprovers());
				flowProcessNew.setApproveRule(flowProcessHis.getApproveRule());
				flowProcessNew.setApproveStatus(flowProcessHis
						.getApproveStatus());
				flowProcessNew.setApproveType(flowProcessHis.getApproveType());
				flowProcessNew.setCorpId(flowProcessHis.getCorpId());
				flowProcessNew.setCurrency(flowProcessHis.getCurrency());
				flowProcessNew
						.setLatestDealer(flowProcessHis.getLatestDealer());
				flowProcessNew.setLatestDealerName(flowProcessHis
						.getLatestDealerName());
				flowProcessNew.setLatestDealTime(flowProcessHis
						.getLatestDealTime());
				flowProcessNew.setProcCreateTime(flowProcessHis
						.getProcCreateTime());
				flowProcessNew.setProcCreator(flowProcessHis.getProcCreator());
				flowProcessNew.setProcCreatorName(flowProcessHis
						.getProcCreatorName());
				flowProcessNew.setProcFinishTime(flowProcessHis
						.getProcFinishTime());
				flowProcessNew.setProcStatus(flowProcessHis.getProcStatus());
				flowProcessNew.setRuleFlag(flowProcessHis.getRuleFlag());
				flowProcessNew.setToAmount(flowProcessHis.getToAmount());
				flowProcessNew.setToCurrency(flowProcessHis.getToCurrency());
				flowProcessNew.setTransDesc(flowProcessHis.getTransDesc());
				flowProcessNew.setTransNo(flowProcessHis.getTransNo());
				flowProcessNew.setTxnBean(flowProcessHis.getTxnBean());
				flowProcessNew.setTxnCategory(flowProcessHis.getTxnCategory());
				flowProcessNew.setTxnType(flowProcessHis.getTxnType());
			}
			return flowProcessNew;
		} else {
			return flowProcess;
		}
	}

	public boolean[] doMultiWork(String[] workIds, String action, String memo,
			NTBUser user, CibAction bean) throws NTBException {
		boolean[] boolRs = new boolean[workIds.length];
		Date curTime = new Date();

		MailService mailService = (MailService) Config.getAppContext().getBean(
				"MailService");

		FlowProcess process = null;
		FlowWork flowWork = null;
		FlowWork nextWork = null;
		for (int i = 0; i < workIds.length; i++) {
			boolRs[i] = true;
			List userList = new ArrayList();

			try {
				flowWork = (FlowWork) flowWorkDao.load(FlowWork.class,
						workIds[i]);

				boolean finishFlag = false;

				if (!(WORK_STATUS_CHECKOUT.equals(flowWork.getWorkStatus()) && user
						.getUserId().equals(flowWork.getWorkDealer()))) {
					// by wen 20110321
					Log.info(">>> WorkHolderChanged WorkId: "
							+ flowWork.getWorkId());
					Log.info(">>> Work original Status :"
							+ flowWork.getWorkStatus());
					Log.info(">>> Work original Dealer :"
							+ flowWork.getWorkDealer());
					Log.info(">>> Work actual Dealer :" + user.getUserId());
					// end 20110321
					throw new NTBException("err.flow.WorkHolderChanged");
				}

				flowWork.setDealAction(action);
				flowWork.setDealMemo(memo);
				flowWork.setWorkStatus(WORK_STATUS_FINISH);
				flowWork.setWorkDealer(user.getUserId());
				flowWork.setWorkDealerName(user.getUserName());
				flowWork.setDealEndTime(curTime);
				process = flowWork.getFlowProcess();
				process.setLatestDealer(user.getUserId());
				process.setLatestDealerName(user.getUserName());
				process.setLatestDealTime(curTime);

				if (ACTION_REJECT.equals(action)) {
					process.setProcStatus(PROCESS_STATUS_REJECT);
					// Jet added 2008-07-10
					process.setProcFinishTime(curTime);

					Approvable approvable = (Approvable) (Class.forName(process
							.getTxnBean()).newInstance());
					boolRs[i] = approvable.reject(process.getTxnType(),
							process.getTransNo(), bean);
					if (boolRs[i]) {
						userList.add(process.getProcCreator());
						approvable.viewDetail(process.getTxnType(),
								process.getTransNo(), bean);
						finishFlag = true;
					}

				} else if (ACTION_APPROVE.equals(action)
						|| ACTION_EXECUTE.equals(action)) {
					int curLevelFinishCount = flowWork.getLevelFinishCount()
							.intValue();
					flowWork.setLevelFinishCount(new Integer(
							curLevelFinishCount + 1));
					String doneStatus = getDoneStatus(flowWork);
					if (doneStatus.equals(process.getApproveRule())) {
						if ("Y".equals(process.getAllowExecutor())
								&& !process.getApproveStatus().equals(
										process.getApproveRule())) {
							process.setProcStatus(PROCESS_STATUS_EXECUTE_PENDING);
							nextWork = new FlowWork();
							String nextWorkId = CibIdGenerator
									.getIdForOperation(ID_TYPE_WORK);
							nextWork.setWorkId(nextWorkId);
							nextWork.setLevelFinishCount(new Integer(0));
							nextWork.setFlowProcess(process);
							nextWork.setWorkCreateTime(curTime);
							nextWork.setWorkCreator(user.getUserId());
							nextWork.setWorkCreatorName(user.getUserName());
							nextWork.setWorkStatus(FlowEngineService.WORK_STATUS_NEW);

						} else {
							process.setProcStatus(PROCESS_STATUS_FINISH);
							process.setProcFinishTime(curTime);
							Approvable approvable = (Approvable) (Class
									.forName(process.getTxnBean())
									.newInstance());
							boolRs[i] = approvable.approve(
									process.getTxnType(), process.getTransNo(),
									bean);
							if (boolRs[i]) {
								userList.add(process.getProcCreator());
								approvable.viewDetail(process.getTxnType(),
										process.getTransNo(), bean);
								finishFlag = true;
							}
						}

					} else {
						if (APPROVE_TYPE_UNASSIGNED.equals(process
								.getApproveType())) {
							nextWork = new FlowWork();
							String nextWorkId = CibIdGenerator
									.getIdForOperation(ID_TYPE_WORK);
							nextWork.setWorkId(nextWorkId);

							if (TXN_CATEGORY_FINANCE.equals(process
									.getTxnCategory())) {
								String nextWorkLevel = getNextLevel(flowWork);
								nextWork.setApproveLevel(nextWorkLevel);
								if (flowWork.getApproveLevel().equals(
										nextWorkLevel)) {
									nextWork.setLevelFinishCount(flowWork
											.getLevelFinishCount());
								} else {
									nextWork.setLevelFinishCount(new Integer(0));
								}

							} else {
								nextWork.setLevelFinishCount(flowWork
										.getLevelFinishCount());
							}
							nextWork.setFlowProcess(process);
							nextWork.setWorkCreateTime(curTime);
							nextWork.setWorkCreator(user.getUserId());
							nextWork.setWorkCreatorName(user.getUserName());
							nextWork.setWorkStatus(WORK_STATUS_NEW);
						}
					}
					process.setApproveStatus(doneStatus);
				}

				if (boolRs[i]) {
					flowWorkDao.update(flowWork);
					flowProcessDao.update(process);
					if (null != nextWork) {
						flowWorkDao.add(nextWork);
					}

					if (ACTION_REJECT.equals(action)
							&& APPROVE_TYPE_ASSIGNED.equals(process
									.getApproveType())) {
						cancelRestWorks(process.getProcId());
					}

					// 锟斤拷锟斤拷锟斤拷探锟斤拷锟斤拷锟竭憋拷锟杰撅拷锟斤拷锟斤拷锟紿is锟�删锟斤拷原锟斤拷录
					if (finishFlag) {
						backupHis(process.getProcId());
					}

					if (userList.size() > 0) {
						// Add by Nabai for change of interface 20060928
						Map mailData = bean.getResultData();
						mailData.put("userType", (bean.getUser() instanceof BankUser)?"B":"C");//add by linrui 20180529
						String[] userArray = new String[userList.size()];
						for (int iUser = 0; iUser < userArray.length; iUser++) {
							userArray[iUser] = (String) userList.get(iUser);
						}
						mailService.toRequester_Approved(process.getTxnType(),
								userArray, mailData);
					}
				}
			} catch (Exception e) {
				Log.error("Process error at FlowEngineServiceImpl.doMultiWork"
						+ "(String[] workIds, String action, String memo,"
						+ "NTBUser user, CibAction bean): ", e);

				boolRs[i] = false;
			}
		}
		return boolRs;
	}

	/**
	 * add by su_jj 20110307
	 */
	public Object[] execMultiWork(String[] workIds, String action, String memo,
			NTBUser user, CibAction bean) throws NTBException {
		boolean[] boolRs = new boolean[workIds.length];
		Object[] exception = new Object[workIds.length];
		Date curTime = new Date();
		RBFactory rbErrMsg = RBFactory.getInstance(msgResource);
		MailService mailService = (MailService) Config.getAppContext().getBean(
				"MailService");

		// by wen 20110413 Log all wordid
		Log.info(">>> doMultiWork(), work count: " + workIds.length);
		for (int i = 0; i < workIds.length; i++) {
			Log.info(">>> list WorkId[" + i + "]: " + workIds[i]);
		}
		// end 20110413

		// by wen 20110413 check duplicate workid
		Map dupMap = new HashMap();
		// end 20110413

		FlowProcess process = null;
		FlowWork flowWork = null;
		FlowWork nextWork = null;
		// System.out.println("------->rbErrMsg:"+rbErrMsg);
		for (int i = 0; i < workIds.length; i++) {
			boolRs[i] = true;
			List userList = new ArrayList();

			if (i < workIds.length - 1) {
				// 20130403 锟斤拷锟斤拷锟斤拷锟今“达拷锟斤拷锟斤拷锟斤拷志锟斤拷锟斤拷锟斤拷锟叫讹拷页锟斤拷锟斤拷锟斤拷状态锟斤拷锟斤拷锟斤拷锟斤拷锟揭伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟角帮拷锟斤拷锟阶刺�
				setWorksStatus4ProgressBar(workIds[i], bean);
			}

			try {
				flowWork = (FlowWork) flowWorkDao.load(FlowWork.class,
						workIds[i]);

				// by wen 20110321
				Log.info(">>> doMultiWork() processing flowWork.WorkId :"
						+ flowWork.getWorkId());
				Log.info(">>> doMultiWork() processing flowWork.WorkDealer :"
						+ flowWork.getWorkDealer());
				Log.info(">>> doMultiWork() processing flowWork.WorkStatus :"
						+ flowWork.getWorkStatus());
				// end 20110321

				// by wen 20110413 check duplicate workid
				if (dupMap.get(workIds[i]) != null) {
					Log.info(">>> work duplicate submitted, workId= "
							+ workIds[i]);

					continue;
				} else {
					dupMap.put(workIds[i], workIds[i]);
				}
				// end 20110413

				boolean finishFlag = false;

				if (!(WORK_STATUS_CHECKOUT.equals(flowWork.getWorkStatus()) && user
						.getUserId().equals(flowWork.getWorkDealer()))) {
					// by wen 20110321
					Log.info(">>> WorkHolderChanged WorkId: "
							+ flowWork.getWorkId());
					Log.info(">>> Work original Status :"
							+ flowWork.getWorkStatus());
					Log.info(">>> Work original Dealer :"
							+ flowWork.getWorkDealer());
					Log.info(">>> Work actual Dealer :" + user.getUserId());
					// end 20110321
					throw new NTBException("err.flow.WorkHolderChanged");
				}

				flowWork.setDealAction(action);
				flowWork.setDealMemo(memo);
				flowWork.setWorkStatus(WORK_STATUS_FINISH);
				flowWork.setWorkDealer(user.getUserId());
				flowWork.setWorkDealerName(user.getUserName());
				flowWork.setDealEndTime(curTime);
				process = flowWork.getFlowProcess();
				process.setLatestDealer(user.getUserId());
				process.setLatestDealerName(user.getUserName());
				process.setLatestDealTime(curTime);

				if (ACTION_REJECT.equals(action)) {
					process.setProcStatus(PROCESS_STATUS_REJECT);
					// Jet added 2008-07-10
					process.setProcFinishTime(curTime);

					Approvable approvable = (Approvable) (Class.forName(process
							.getTxnBean()).newInstance());
					boolRs[i] = approvable.reject(process.getTxnType(),
							process.getTransNo(), bean);

					// by wen 20110413
					Log.info(">>> Work rejected, workId="
							+ flowWork.getWorkId() + ",result:" + boolRs[i]);
					// end 20110413

					if (boolRs[i]) {
						userList.add(process.getProcCreator());
						approvable.viewDetail(process.getTxnType(),
								process.getTransNo(), bean);
						finishFlag = true;
					}

				} else if (ACTION_APPROVE.equals(action)
						|| ACTION_EXECUTE.equals(action)) {
					int curLevelFinishCount = flowWork.getLevelFinishCount()
							.intValue();
					flowWork.setLevelFinishCount(new Integer(
							curLevelFinishCount + 1));
					String doneStatus = getDoneStatus(flowWork);
					if (doneStatus.equals(process.getApproveRule())) {
						if ("Y".equals(process.getAllowExecutor())
								&& !process.getApproveStatus().equals(
										process.getApproveRule())) {
							process.setProcStatus(PROCESS_STATUS_EXECUTE_PENDING);
							nextWork = new FlowWork();
							String nextWorkId = CibIdGenerator
									.getIdForOperation(ID_TYPE_WORK);
							nextWork.setWorkId(nextWorkId);
							nextWork.setLevelFinishCount(new Integer(0));
							nextWork.setFlowProcess(process);
							nextWork.setWorkCreateTime(curTime);
							nextWork.setWorkCreator(user.getUserId());
							nextWork.setWorkCreatorName(user.getUserName());
							nextWork.setWorkStatus(FlowEngineService.WORK_STATUS_NEW);

						} else {
							process.setProcStatus(PROCESS_STATUS_FINISH);
							process.setProcFinishTime(curTime);
							Approvable approvable = (Approvable) (Class
									.forName(process.getTxnBean())
									.newInstance());
							boolRs[i] = approvable.approve(
									process.getTxnType(), process.getTransNo(),
									bean);

							// by wen 20110413
							Log.info(">>> Work approved, workId="
									+ flowWork.getWorkId() + ",result:"
									+ boolRs[i]);
							// end 20110413

							if (boolRs[i]) {
								userList.add(process.getProcCreator());
								approvable.viewDetail(process.getTxnType(),
										process.getTransNo(), bean);
								finishFlag = true;
							}
						}

					} else {
						if (APPROVE_TYPE_UNASSIGNED.equals(process
								.getApproveType())) {
							nextWork = new FlowWork();
							String nextWorkId = CibIdGenerator
									.getIdForOperation(ID_TYPE_WORK);
							nextWork.setWorkId(nextWorkId);

							if (TXN_CATEGORY_FINANCE.equals(process
									.getTxnCategory())) {
								String nextWorkLevel = getNextLevel(flowWork);
								nextWork.setApproveLevel(nextWorkLevel);
								if (flowWork.getApproveLevel().equals(
										nextWorkLevel)) {
									nextWork.setLevelFinishCount(flowWork
											.getLevelFinishCount());
								} else {
									nextWork.setLevelFinishCount(new Integer(0));
								}

							} else {
								nextWork.setLevelFinishCount(flowWork
										.getLevelFinishCount());
							}
							nextWork.setFlowProcess(process);
							nextWork.setWorkCreateTime(curTime);
							nextWork.setWorkCreator(user.getUserId());
							nextWork.setWorkCreatorName(user.getUserName());
							nextWork.setWorkStatus(WORK_STATUS_NEW);
						}
					}
					process.setApproveStatus(doneStatus);
				}

				if (boolRs[i]) {
					flowWorkDao.update(flowWork);
					flowProcessDao.update(process);
					if (null != nextWork) {
						flowWorkDao.add(nextWork);
						//add by linrui for flowwork 20190724
						//BeanUtils.copyProperties(flowWork, nextWork);
						//flowWorkDao.update(flowWork);
					}

					if (ACTION_REJECT.equals(action)
							&& APPROVE_TYPE_ASSIGNED.equals(process
									.getApproveType())) {
						cancelRestWorks(process.getProcId());
					}

					// 锟斤拷锟斤拷锟斤拷探锟斤拷锟斤拷锟竭憋拷锟杰撅拷锟斤拷锟斤拷锟紿is锟�删锟斤拷原锟斤拷录
					if (finishFlag) {
						backupHis(process.getProcId());
					}

					if (userList.size() > 0) {
						// Add by Nabai for change of interface 20060928
						Map mailData = bean.getResultData();
						
						mailData.put("userId", bean.getUser().getUserId());
						mailData.put("userName", bean.getUser().getUserName());
						mailData.put("requestTime", new Date());
						mailData.put("userType", (user instanceof BankUser)?"B":"C");//add by linrui 20180529
						String[] userArray = new String[userList.size()];
						for (int iUser = 0; iUser < userArray.length; iUser++) {
							userArray[iUser] = (String) userList.get(iUser);
						}
						mailService.toRequester_Approved(process.getTxnType(),
								userArray, mailData);
					}
					exception[i] = "success";
				} else {
					exception[i] = rbErrMsg.getString(generalError);
				}
			} catch (NTBException ntbe) {
				Log.info("catch ntbException...");
				Log.error("Process error at FlowEngineServiceImpl.doMultiWork"
						+ "(String[] workIds, String action, String memo,"
						+ "NTBUser user, CibAction bean): ", ntbe);
				boolRs[i] = false;
				exception[i] = getErrorMsg(ntbe);
				if (exception[i] == null || exception[i] == "")
					exception[i] = rbErrMsg.getString(generalError);

			} catch (Exception e) {
				Log.info("catch exception...");
				Log.error("Process error at FlowEngineServiceImpl.doMultiWork"
						+ "(String[] workIds, String action, String memo,"
						+ "NTBUser user, CibAction bean): ", e);
				boolRs[i] = false;
				exception[i] = e.getMessage();
				if (exception[i] == null || exception[i] == "")
					exception[i] = rbErrMsg.getString(generalError);
			}
		}
		return exception;
	}

	public List listWorkByProcDealed(String procId, NTBUser user)
			throws NTBException {
		List works = flowWorkDao.findByProcIdDealed(procId);

		if (null != works) {
			works = worksToMaps(works);
		}

		return works;
	}

	public void undoAllCheckoutWork(String userId) throws NTBException {
		List checkoutWorks = flowWorkDao.findUserCheckout(userId);

		if (null != checkoutWorks && checkoutWorks.size() > 0) {
			FlowWork flowWork = null;
			for (int i = 0; i < checkoutWorks.size(); i++) {
				flowWork = (FlowWork) checkoutWorks.get(i);
				undoCheckoutWork(flowWork.getWorkId(), userId);
			}
		}
	}

	public List listWorkChecked(NTBUser user) throws NTBException {
		List works = null;

		try {
			if (user instanceof CorpUser) {
				CorpUser corpUser = (CorpUser) user;
				String corpType = corpUser.getCorporation().getCorpType();
				String allowFinancialController = corpUser.getCorporation()
						.getAllowFinancialController();
				String financialControllerFlag = corpUser
						.getFinancialControllerFlag();

				if (Constants.ROLE_APPROVER.equals(corpUser.getRoleId())) {
					if (Constants.CORP_TYPE_MIDDLE.equals(corpType)
							|| Constants.CORP_TYPE_SMALL.equals(corpType)) {
						works = flowWorkDao.findByCorpCheckedSelf(
								corpUser.getCorpId(), corpUser.getUserId(),
								corpUser.getAuthLevel(),
								allowFinancialController,
								financialControllerFlag);
					} else {
						works = flowWorkDao.findByCorpChecked(
								corpUser.getCorpId(), corpUser.getUserId(),
								corpUser.getAuthLevel(),
								allowFinancialController,
								financialControllerFlag);
					}
				} else if (Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())) {
					works = flowWorkDao.findByCorpWithoutLevelChecked(
							corpUser.getCorpId(), corpUser.getUserId());
				} else if (Constants.ROLE_ADMINISTRATOR.equals(corpUser
						.getRoleId())) {
					works = flowWorkDao.findByCorpChecked(corpUser.getCorpId(),
							corpUser.getUserId());
				}

			} else {
				AbstractBankUser bankUser = (AbstractBankUser) user;
				if (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
					works = flowWorkDao.findByCorpChecked(CORPID_BANK,
							bankUser.getUserId());
				}
			}
		} catch (Exception e) {
			Log.error("Process error at FlowEngineServiceImpl."
					+ "listWork(NTBUser user): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			}
		}

		if (null != works) {
			works = worksToMaps(works);
		}
		// Log.info("*** listWorkCheck add progress info start :" + new Date());
		works = addProgressInfo(works, user);
		// Log.info("*** listWorkCheck add progress info end :" + new Date());

		return works;
	}
	//add by linrui 20190916 for cr589
	public List listWorkChecked(NTBUser user, String sortOrder) throws NTBException {
		List works = null;
		
		try {
			if (user instanceof CorpUser) {
				CorpUser corpUser = (CorpUser) user;
				String corpType = corpUser.getCorporation().getCorpType();
				String allowFinancialController = corpUser.getCorporation()
						.getAllowFinancialController();
				String financialControllerFlag = corpUser
						.getFinancialControllerFlag();
				
				if (Constants.ROLE_APPROVER.equals(corpUser.getRoleId())) {
					if (Constants.CORP_TYPE_MIDDLE.equals(corpType)
							|| Constants.CORP_TYPE_SMALL.equals(corpType)) {
						works = flowWorkDao.findByCorpCheckedSelf(
								corpUser.getCorpId(), corpUser.getUserId(),
								corpUser.getAuthLevel(),
								allowFinancialController,
								financialControllerFlag,
								sortOrder);
					} else {
						works = flowWorkDao.findByCorpChecked(
								corpUser.getCorpId(), corpUser.getUserId(),
								corpUser.getAuthLevel(),
								allowFinancialController,
								financialControllerFlag,
								sortOrder);
					}
				} else if (Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())) {
					works = flowWorkDao.findByCorpWithoutLevelChecked(
							corpUser.getCorpId(), corpUser.getUserId(), sortOrder);
				} else if (Constants.ROLE_ADMINISTRATOR.equals(corpUser
						.getRoleId())) {
					works = flowWorkDao.findByCorpChecked(corpUser.getCorpId(),
							corpUser.getUserId(), sortOrder);
				}
				
			} else {
				AbstractBankUser bankUser = (AbstractBankUser) user;
				if (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
					works = flowWorkDao.findByCorpChecked(CORPID_BANK,
							bankUser.getUserId(), sortOrder);
				}
			}
		} catch (Exception e) {
			Log.error("Process error at FlowEngineServiceImpl."
					+ "listWork(NTBUser user): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			}
		}
		
		if (null != works) {
			works = worksToMaps(works);
		}
		// Log.info("*** listWorkCheck add progress info start :" + new Date());
		works = addProgressInfo(works, user);
		// Log.info("*** listWorkCheck add progress info end :" + new Date());
		
		return works;
	}

	public String getLatestApprover(String procId, String curStatus,
			String approvers) throws NTBException {
		String approver = null;

		FlowProcess flowProcess = (FlowProcess) flowProcessDao.load(
				FlowProcess.class, procId);
		if (null != flowProcess && null != flowProcess.getProcId()) {
			String approveRule = flowProcess.getApproveRule();
			String approveStatus = flowProcess.getApproveStatus();
			StrTokenizer approverTokens = new StrTokenizer(approvers, ";");
			int ruleCount = 0;
			int statusCount = 0;

			List ruleItems = extractRuleStr(approveRule);
			List statusItems = extractRuleStr(approveStatus);
			HashMap ruleItem = null;
			HashMap statusItem = null;

			if (null != ruleItems && null != statusItems) {
				for (int i = 0; i < ruleItems.size(); i++) {
					ruleItem = (HashMap) ruleItems.get(i);
					statusItem = (HashMap) statusItems.get(i);
					ruleCount = Integer
							.parseInt((String) ruleItem.get("Count"));
					statusCount = Integer.parseInt((String) statusItem
							.get("Count"));
					if (statusCount < ruleCount) {
						for (int j = 0; j < ruleCount; j++) {
							if (j < ruleCount - statusCount) {
								approver = approverTokens.nextToken();
							} else {
								break;
							}
						}
					} else {
						break;
					}
				}
			}

			if (!curStatus.equals(approveStatus)) {
				throw new NTBException("err.flow.TransactionStateChanged");
			}
		}

		return approver;

	}

	public boolean checkApproveComplete(String txnType, String transNo)
			throws NTBException {
		boolean flag = false;
		FlowProcess flowProcess = viewFlowProcess(txnType, transNo);

		if (null != flowProcess) {
			if ((PROCESS_STATUS_EXECUTE_PENDING.equals(flowProcess
					.getProcStatus()) || PROCESS_STATUS_FINISH
					.equals(flowProcess.getProcStatus()))
					|| PROCESS_STATUS_REJECT
							.equals(flowProcess.getProcStatus())
					|| PROCESS_STATUS_CANCEL
							.equals(flowProcess.getProcStatus())) {
				flag = true;
			}
		}
		return flag;
	}

	private void cancelRestWorks(String procId) throws NTBException {
		List works = flowWorkDao.findByProcIdPending(procId);

		if (null != works) {
			FlowWork flowWork = null;
			for (int i = 0; i < works.size(); i++) {
				flowWork = (FlowWork) works.get(i);
				flowWork.setWorkStatus(WORK_STATUS_CANCEL);
				flowWorkDao.update(flowWork);
			}
		}

	}

	public String getProcessStatus(String procId) throws NTBException {
		StringBuffer buffer = new StringBuffer();

		List works = flowWorkDao.findByProcIdAll(procId);

		if (null != works && works.size() > 1) {
			FlowWork flowWork = null;
			for (int i = 1; i < works.size(); i++) {
				flowWork = (FlowWork) works.get(i);
				buffer.append(flowWork.getWorkStatus());
				buffer.append(";");
			}
			if (buffer.length() > 1) {
				buffer.deleteCharAt(buffer.length() - 1);
			}
		}

		return buffer.toString();
	}

	public int getApprovedCount(String status) throws NTBException {
		int count = 0;
		List items = extractRuleStr(status);

		if (null != items) {
			HashMap item = null;

			for (int i = 0; i < items.size(); i++) {
				item = (HashMap) items.get(i);
				count += Integer.parseInt((String) item.get("Count"));
			}
		}
		return count;
	}

	// Modified by Na Bai 2013-12-31
	// 锟斤拷玫锟斤拷锟斤拷锟斤拷锟斤拷锟较�
	public List getProgress(String procID, NTBUser user)
			throws NTBException {
		List works = flowWorkDao.findByProcIdAll(procID);
		FlowWork flowWork = (FlowWork) works.get(0);
		FlowProcess flowProcess = flowWork.getFlowProcess();

		List progress = new ArrayList();
		if (null != flowProcess) {
			HashMap item = null;
			RBFactory rbRole = RBFactory
					.getInstance("app.cib.resource.common.role");

			if (FlowEngineService.TXN_CATEGORY_FINANCE.equals(flowProcess
					.getTxnCategory())) {
				for (int i = 0; i < works.size(); i++) {
					flowWork = (FlowWork) works.get(i);
					item = new HashMap();
					if (FlowEngineService.WORK_STATUS_FINISH.equals(flowWork
							.getWorkStatus())) {
						item.put("FinishFlag", "1");
						String dealer = flowWork.getWorkDealerName();
						if (flowWork.getApproveLevel() != null) {
							dealer += "(" + flowWork.getApproveLevel() + ")";
						}
						item.put("Dealer", dealer);
						// add by hjs 20070809
						item.put("DealerId", flowWork.getWorkDealer());
						progress.add(item);
					} else {
						if (FlowEngineService.APPROVE_TYPE_ASSIGNED
								.equals(flowProcess.getApproveType())) {
							item.put("FinishFlag", "0");
							if (null != flowWork.getApproveLevel()) {
								item.put("Dealer", flowWork.getApproveLevel());
								progress.add(item);
							}
						}
					}
				}

				if (FlowEngineService.APPROVE_TYPE_UNASSIGNED
						.equals(flowProcess.getApproveType())) {
					addPendingProgress(progress, flowProcess.getApproveRule(),
							flowProcess.getApproveStatus());
				}

				if ("Y".equals(flowProcess.getAllowExecutor())) {
					if (FlowEngineService.PROCESS_STATUS_FINISH
							.equals(flowProcess.getProcStatus())) {
						if (progress.size() > 0) {
							String dealerName = rbRole
									.getString(CorpUser.ROLE_CORP_EXECUTER);
							item = (HashMap) progress.get(progress.size() - 1);
							String dealer = (String) item.get("Dealer");
							dealer += "(" + dealerName + ")";
							((HashMap) progress.get(progress.size() - 1)).put(
									"Dealer", dealer);
						}
					}
					else {
						String dealerName = rbRole
								.getString(CorpUser.ROLE_CORP_EXECUTER);
						item = new HashMap();
						item.put("FinishFlag", "0");
						item.put("Dealer", dealerName);
						progress.add(item);
					}
				}

			} else if (FlowEngineService.TXN_CATEGORY_NONFINANCE
					.equals(flowProcess.getTxnCategory())) {
				String dealerName = null;
				if (user instanceof AbstractCorpUser) {
					dealerName = rbRole
							.getString(CorpUser.ROLE_CORP_ADMINISRATOR);
				} else {
					dealerName = rbRole
							.getString(BankUser.ROLE_BANK_SUPERVISOR);
				}

				for (int i = 0; i < works.size(); i++) {
					flowWork = (FlowWork) works.get(i);
					item = new HashMap();

					if (FlowEngineService.WORK_STATUS_FINISH.equals(flowWork
							.getWorkStatus())) {
						item.put("FinishFlag", "1");
						item.put("Dealer", flowWork.getWorkDealerName());
					} else {
						item.put("FinishFlag", "0");
						item.put("Dealer", dealerName);
					}
					progress.add(item);
				}

				int total = Integer.parseInt(flowProcess.getApproveRule());
				int current = Integer.parseInt(flowProcess.getApproveStatus());

				for (; current < total - 1; current++) {
					item = new HashMap();
					item.put("FinishFlag", "0");
					item.put("Dealer", dealerName);
					progress.add(item);
				}

			}
		}

		return progress;
	}

	// Modified by Na Bai 2013-12-31
	// 锟斤拷锟斤拷锟斤拷锟斤拷锟较拷锟斤拷锟斤拷曳锟斤拷锟組ap锟斤拷
	public Map getProgressByBatch(String[] procIDs, NTBUser user)
			throws NTBException {
		// 锟斤拷询锟斤拷菘锟斤拷锟斤拷锟斤拷锟斤拷锟较�
		Map procWithWorkList = flowWorkDao.findByProcIdsAll(procIDs);
		RBFactory rbRole = RBFactory
				.getInstance("app.cib.resource.common.role");

		// 准锟斤拷锟斤拷锟截碉拷Map
		Map progressInfoMap = new LinkedHashMap();
		// 锟斤拷锟斤拷锟斤拷锟叫碉拷procID
		for (int pi = 0; pi < procIDs.length; pi++) {
			String procId = procIDs[pi];
			// 锟斤拷锟侥筹拷锟絧rocId锟铰碉拷锟斤拷锟斤拷实锟斤拷锟叫憋拷
			List works = (List) procWithWorkList.get(procId);
			if(null==works){
				continue ;
			}
			FlowWork flowWork = (FlowWork) works.get(0);
			FlowProcess flowProcess = flowWork.getFlowProcess();

			// 锟斤拷锟斤拷锟斤拷实锟斤拷锟叫憋拷锟斤拷装锟斤拷item Map锟叫�锟皆憋拷锟斤拷示
			List progress = new ArrayList();
			HashMap item = null;
			if (FlowEngineService.TXN_CATEGORY_FINANCE.equals(flowProcess
					.getTxnCategory())) {
				for (int i = 0; i < works.size(); i++) {
					flowWork = (FlowWork) works.get(i);
					item = new HashMap();
					if (FlowEngineService.WORK_STATUS_FINISH.equals(flowWork
							.getWorkStatus())) {
						item.put("FinishFlag", "1");
						String dealer = flowWork.getWorkDealerName();
						if (flowWork.getApproveLevel() != null) {
							dealer += "(" + flowWork.getApproveLevel() + ")";
						}
						item.put("Dealer", dealer);
						// add by hjs 20070809
						item.put("DealerId", flowWork.getWorkDealer());
						progress.add(item);
					} else {
						if (FlowEngineService.APPROVE_TYPE_ASSIGNED
								.equals(flowProcess.getApproveType())) {
							item.put("FinishFlag", "0");
							if (null != flowWork.getApproveLevel()) {
								item.put("Dealer", flowWork.getApproveLevel());
								progress.add(item);
							}
						}
					}
				}

				if (FlowEngineService.APPROVE_TYPE_UNASSIGNED
						.equals(flowProcess.getApproveType())) {
					addPendingProgress(progress, flowProcess.getApproveRule(),
							flowProcess.getApproveStatus());
				}

				if ("Y".equals(flowProcess.getAllowExecutor())) {
					if (FlowEngineService.PROCESS_STATUS_FINISH
							.equals(flowProcess.getProcStatus())) {
						if (progress.size() > 0) {
							String dealerName = rbRole
									.getString(CorpUser.ROLE_CORP_EXECUTER);
							item = (HashMap) progress.get(progress.size() - 1);
							String dealer = (String) item.get("Dealer");
							dealer += "(" + dealerName + ")";
							((HashMap) progress.get(progress.size() - 1)).put(
									"Dealer", dealer);
						}
					} else {
						String dealerName = rbRole
								.getString(CorpUser.ROLE_CORP_EXECUTER);
						item = new HashMap();
						item.put("FinishFlag", "0");
						item.put("Dealer", dealerName);
						progress.add(item);
					}
				}

			} else if (FlowEngineService.TXN_CATEGORY_NONFINANCE
					.equals(flowProcess.getTxnCategory())) {
				String dealerName = null;
				if (user instanceof AbstractCorpUser) {
					dealerName = rbRole
							.getString(CorpUser.ROLE_CORP_ADMINISRATOR);
				} else {
					dealerName = rbRole
							.getString(BankUser.ROLE_BANK_SUPERVISOR);
				}

				for (int i = 0; i < works.size(); i++) {
					flowWork = (FlowWork) works.get(i);
					item = new HashMap();

					if (FlowEngineService.WORK_STATUS_FINISH.equals(flowWork
							.getWorkStatus())) {
						item.put("FinishFlag", "1");
						item.put("Dealer", flowWork.getWorkDealerName());
					} else {
						item.put("FinishFlag", "0");
						item.put("Dealer", dealerName);
					}
					progress.add(item);
				}

				int total = Integer.parseInt(flowProcess.getApproveRule());
				int current = Integer.parseInt(flowProcess.getApproveStatus());

				for (; current < total - 1; current++) {
					item = new HashMap();
					item.put("FinishFlag", "0");
					item.put("Dealer", dealerName);
					progress.add(item);
				}

			}
			// 锟斤拷item Map锟叫憋拷锟斤拷敕碉拷氐锟組ap锟斤拷
			progressInfoMap.put(procId, progress);
		}

		// 锟斤拷锟截斤拷锟斤拷锟较拷锟組ap
		return progressInfoMap;
	}

	private void addPendingProgress(List progress, String rule, String status) {
		List ruleItems = extractRuleStr(rule);
		List statusItems = extractRuleStr(status);
		HashMap item = null;
		HashMap ruleItem = null;
		HashMap statusItem = null;
		int ruleCount = 0;
		int statusCount = 0;

		for (int i = 0; i < ruleItems.size(); i++) {
			ruleItem = (HashMap) ruleItems.get(i);
			statusItem = (HashMap) statusItems.get(i);

			ruleCount = Integer.parseInt((String) ruleItem.get("Count"));
			statusCount = Integer.parseInt((String) statusItem.get("Count"));
			boolean firstFlag = true;

			if (statusCount != ruleCount) {
				if (firstFlag) {
					item = new HashMap();
					item.put("FinishFlag", "0");
					item.put("Dealer", ruleItem.get("Level"));
					progress.add(item);
					statusCount += 1;
					firstFlag = false;
				}

				for (; statusCount < ruleCount; statusCount++) {
					item = new HashMap();
					item.put("FinishFlag", "0");
					item.put("Dealer", ruleItem.get("Level"));
					progress.add(item);
				}
			}
		}

	}

	// Modified by Na Bai 2013-12-31
	// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟教斤拷锟斤拷锟较�
	private List addProgressInfo(List works, NTBUser user) throws NTBException {
		if (null != works) {
			List workReturn = new ArrayList();
			List workBatch = new ArrayList();
			for (int i = 0; i < works.size(); i++) {
				workBatch.add(works.get(i));
				// 为锟斤拷锟叫э拷剩锟矫�0锟斤拷锟斤拷锟斤拷锟斤拷一锟斤拷锟斤拷锟教斤拷锟�
				if (workBatch.size() >= 50) {
					workReturn.addAll(addProgressInfoByBatch(workBatch, user));
					workBatch.clear();
				}
			}
			// 锟斤拷锟斤拷锟斤拷锟斤拷一锟斤拷锟斤拷锟教斤拷锟�
			if (workBatch.size() > 0) {
				workReturn.addAll(addProgressInfoByBatch(workBatch, user));
				workBatch.clear();
			}
			return workReturn;
		}
		return null;
	}

	// Modified by Na Bai 2013-12-31
	// 锟斤拷锟斤拷锟斤拷锟叫憋拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟较�
	private List addProgressInfoByBatch(List works, NTBUser user)
			throws NTBException {
		if (null != works) {
			Map workMap = null;
			String[] procIds = new String[works.size()];
			// 锟饺伙拷锟斤拷锟斤拷械锟斤拷锟斤拷瘢锟斤拷锟斤拷procId锟斤拷锟介；
			for (int i = 0; i < works.size(); i++) {
				workMap = (Map) works.get(i);
				String procId = (String) workMap.get("procId");
				procIds[i] = procId;
			}

			// 锟斤拷锟絧rocId锟斤拷锟斤拷锟斤拷械锟斤拷锟斤拷锟斤拷锟较�
			Map progressMap = getProgressByBatch(procIds, user);
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟较�
			for (int i = 0; i < works.size(); i++) {
				workMap = (Map) works.get(i);
				String procId = (String) workMap.get("procId");
				workMap.put("progressList", progressMap.get(procId));
			}
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟叫憋拷
			return works;
		}
		return null;
	}

	public List listWorkByCorpDealing(NTBUser loginUser, NTBUser queryUser,
			Date fromDate, Date toDate) throws NTBException {
		List works = null;
		if (null != queryUser) {
			if (queryUser instanceof AbstractCorpUser) {
				AbstractCorpUser corpUser = (AbstractCorpUser) queryUser;
				if (Constants.ROLE_OPERATOR.equals(corpUser.getRoleId())
						|| Constants.ROLE_APPROVER.equals(corpUser.getRoleId())
						|| Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())
						|| Constants.ROLE_ADMINISTRATOR.equals(corpUser
								.getRoleId())) {
					works = flowWorkDao.findByUserDealing(corpUser.getCorpId(),
							corpUser.getUserId(), fromDate, toDate);
				}

			} else {
				AbstractBankUser bankUser = (AbstractBankUser) queryUser;
				if (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
					works = flowWorkDao.findByUserDealing(CORPID_BANK,
							bankUser.getUserId(), fromDate, toDate);
				}
			}
		} else {
			if (loginUser instanceof AbstractCorpUser) {
				AbstractCorpUser corpUser = (AbstractCorpUser) loginUser;
				works = flowWorkDao.findByCorpDealing(corpUser.getCorpId(),
						fromDate, toDate);
			} else {
				works = flowWorkDao.findByCorpDealing(CORPID_BANK, fromDate,
						toDate);
			}
		}

		if (null != works) {
			works = worksToMaps(works);
		}

		return works;
	}

	public List listWorksByCorpAll(NTBUser loginUser, NTBUser queryUser,
			Date fromDate, Date toDate) throws NTBException {
		List works = null;

		if (null != queryUser) {
			works = listWorkByUserAllForReport(queryUser, fromDate, toDate);
		} else {
			if (loginUser instanceof AbstractCorpUser) {
				AbstractCorpUser corpUser = (AbstractCorpUser) loginUser;
				works = flowWorkDao.findByCorpAll(corpUser.getCorpId(),
						fromDate, toDate);
			} else {
				works = flowWorkDao
						.findByCorpAll(CORPID_BANK, fromDate, toDate);
			}

			if (null != works) {
				works = worksToMaps(works);
			}

		}

		return works;
	}

	private void backupHis(String procId) throws NTBException {
		FlowProcess flowProcess = (FlowProcess) flowProcessDao.load(
				FlowProcess.class, procId);
		FlowProcessHis flowProcessHis = new FlowProcessHis();

		flowProcessHis.setProcId(flowProcess.getProcId());
		flowProcessHis.setAllowExecutor(flowProcess.getAllowExecutor());
		flowProcessHis.setAmount(flowProcess.getAmount());
		flowProcessHis.setApprovers(flowProcess.getApprovers());
		flowProcessHis.setApproveRule(flowProcess.getApproveRule());
		flowProcessHis.setApproveStatus(flowProcess.getApproveStatus());
		flowProcessHis.setApproveType(flowProcess.getApproveType());
		flowProcessHis.setCorpId(flowProcess.getCorpId());
		flowProcessHis.setCurrency(flowProcess.getCurrency());
		flowProcessHis.setLatestDealer(flowProcess.getLatestDealer());
		flowProcessHis.setLatestDealerName(flowProcess.getLatestDealerName());
		flowProcessHis.setLatestDealTime(flowProcess.getLatestDealTime());
		flowProcessHis.setProcCreateTime(flowProcess.getProcCreateTime());
		flowProcessHis.setProcCreator(flowProcess.getProcCreator());
		flowProcessHis.setProcCreatorName(flowProcess.getProcCreatorName());
		flowProcessHis.setProcFinishTime(flowProcess.getProcFinishTime());
		flowProcessHis.setProcStatus(flowProcess.getProcStatus());
		flowProcessHis.setRuleFlag(flowProcess.getRuleFlag());
		flowProcessHis.setToAmount(flowProcess.getToAmount());
		flowProcessHis.setToCurrency(flowProcess.getToCurrency());
		flowProcessHis.setTransDesc(flowProcess.getTransDesc());
		flowProcessHis.setTransNo(flowProcess.getTransNo());
		flowProcessHis.setTxnBean(flowProcess.getTxnBean());
		flowProcessHis.setTxnCategory(flowProcess.getTxnCategory());
		flowProcessHis.setTxnType(flowProcess.getTxnType());

		flowProcessHisDao.add(flowProcessHis);

		Set works = flowProcess.getFlowWorkSet();
		Iterator ir = works.iterator();
		FlowWorkHis flowWorkHis = null;
		FlowWork flowWork = null;
		while (ir.hasNext()) {
			flowWork = (FlowWork) ir.next();

			flowWorkHis = new FlowWorkHis();
			flowWorkHis.setWorkId(flowWork.getWorkId());
			flowWorkHis.setApproveLevel(flowWork.getApproveLevel());
			flowWorkHis.setAssignedDealer(flowWork.getAssignedDealer());
			flowWorkHis.setDealAction(flowWork.getDealAction());
			flowWorkHis.setDealBeginTime(flowWork.getDealBeginTime());
			flowWorkHis.setDealEndTime(flowWork.getDealEndTime());
			flowWorkHis.setDealMemo(flowWork.getDealMemo());
			flowWorkHis.setLevelFinishCount(flowWork.getLevelFinishCount());
			flowWorkHis.setFlowProcessHis(flowProcessHis);
			flowWorkHis.setWorkCreateTime(flowWork.getWorkCreateTime());
			flowWorkHis.setWorkCreator(flowWork.getWorkCreator());
			flowWorkHis.setWorkCreatorName(flowWork.getWorkCreatorName());
			flowWorkHis.setWorkDealer(flowWork.getWorkDealer());
			flowWorkHis.setWorkDealerName(flowWork.getWorkDealerName());
			flowWorkHis.setWorkStatus(flowWork.getWorkStatus());

			Log.info("Backup Work =" + flowWork.getWorkId());
			flowWorkHisDao.add(flowWorkHis);
			// flowWorkDao.delete(flowWork);

		}
		// flowProcessDao.delete(flowProcess);

	}

	public FlowProcessHisDao getFlowProcessHisDao() {
		return flowProcessHisDao;
	}

	public void setFlowProcessHisDao(FlowProcessHisDao flowProcessHisDao) {
		this.flowProcessHisDao = flowProcessHisDao;
	}

	public FlowWorkHisDao getFlowWorkHisDao() {
		return flowWorkHisDao;
	}

	public CorpUserDao getCorpUserDao() {
		return corpUserDao;
	}

	public void setFlowWorkHisDao(FlowWorkHisDao flowWorkHisDao) {
		this.flowWorkHisDao = flowWorkHisDao;
	}

	public void setCorpUserDao(CorpUserDao corpUserDao) {
		this.corpUserDao = corpUserDao;
	}

	/**
	 * add by hjs
	 */
	public List listProcByCorpDealing(String corpId, String userId,
			String dateFrom, String dateTo) throws NTBException {
		return flowProcessHisDao.listProcessHis(corpId, userId, dateFrom,
				dateTo);
	}

	public boolean checkOutstandingStatus(CorpUser user, String corpType)
			throws NTBException {
		List selfPendingList = null;
		if (Constants.CORP_TYPE_MIDDLE.equals(corpType)
				|| Constants.CORP_TYPE_SMALL.equals(corpType)) {
			selfPendingList = this.flowWorkDao.findAssignedPendingListByUserId(
					user.getCorpId(), user.getUserId(), 2);
		} else {
			selfPendingList = this.flowWorkDao.findAssignedPendingListByUserId(
					user.getCorpId(), user.getUserId(), 1);
		}
		List outstandingList = flowWorkDao.findByUserDealing(user.getCorpId(),
				user.getUserId(), "", "");
		return !(selfPendingList.size() + outstandingList.size() > 0);
	}

	// add by su_jj 20110307
	private String getErrorMsg(NTBException errData) {
		String ermsg_str = "";
		if (errData != null) {
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫憋拷
			NTBErrorArray errArray = errData.getErrorArray();

			if (errArray != null) {
				for (int i = 0; i < errArray.size(); i++) {
					NTBError fieldError = (NTBError) errArray.getError(i);
					String fieldName = fieldError.getField();
					String errCode = fieldError.getErrorCode();
					String errLabel = fieldError.getLabel();

					String errStr = "";
					if (errCode != null) {
						// 锟斤拷锟斤拷锟斤拷锟斤拷敕拷锟缴达拷锟斤拷锟斤拷息
						// RBFactory rbErrMsg =
						// RBFactory.getInstance(msgResource,
						// locale.toString());
						if (errData instanceof NTBHostException) {
							DBRCFactory dbHostMsg = DBRCFactory
									.getInstance("hostErrMsg");
							if (dbHostMsg != null) {
								errStr = dbHostMsg.getString(errCode);
								if (!errCode.equals(errStr)) {
									errStr = errCode + " - " + errStr;
								}
							}
						} else {
							// add by hjs 2006-12-06
							RBFactory rbErrMsg = RBFactory
									.getInstance(msgResource);
							if (rbErrMsg != null) {
								errStr = rbErrMsg.getString(errCode);
							}
							Object[] parameters = errData.getParameters();
							if (parameters != null) {
								for (int j = 0; j < parameters.length; j++) {
									errStr = Utils.replaceStr(errStr, "[%"
											+ String.valueOf(j + 1) + "]",
											Utils.null2Empty(parameters[j]));
								}
							}
						}

						if (errStr == null) {
							errStr = errCode.toString();
						}
						ermsg_str = ermsg_str + "<br/>" + errStr;
					}
				}
			} else if (errData instanceof NTBHostException) {
				DBRCFactory dbHostMsg = DBRCFactory.getInstance("hostErrMsg");
				String errStr = "";
				String errCode = errData.getErrorCode();
				if (dbHostMsg != null) {
					errStr = dbHostMsg.getString(errCode);
					if (!errCode.equals(errStr)) {
						errStr = errCode + " - " + errStr;
					}
				}
				ermsg_str = ermsg_str + "<br/>" + errStr;
			} else {
				// add by hjs 2006-12-06
				RBFactory rbErrMsg = RBFactory.getInstance(msgResource);
				String errStr = "";
				String errCode = errData.getErrorCode();
				if (rbErrMsg != null) {
					errStr = rbErrMsg.getString(errCode);
				}
				Object[] parameters = errData.getParameters();
				if (parameters != null) {
					for (int j = 0; j < parameters.length; j++) {
						errStr = Utils.replaceStr(errStr,
								"[%" + String.valueOf(j + 1) + "]",
								Utils.null2Empty(parameters[j]));
					}
				}
				ermsg_str = ermsg_str + "<br/>" + errStr;
			}
		}
		return ermsg_str;
	}

	public void setWorksStatus4ProgressBar(String id, CibAction bean) {

		if (id == null || "".equals(id)) {
			return;
		}

		Map worksStatus4pb = (HashMap) bean.getResultData().get(
				"worksStatus4pb");
		if (worksStatus4pb == null) {
			worksStatus4pb = new HashMap();
		}

		if ("init".equals(id)) {
			bean.getResultData().put("worksStatus4pb", new HashMap()); // 锟结交锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷前锟斤拷锟矫匡拷
		} else {
			worksStatus4pb.put(id, "Done!"); // 每锟斤拷锟斤拷锟斤拷锟斤拷桑锟斤拷锟斤拷锟斤拷媒锟斤拷锟斤拷识
		}

		bean.getResultData().put("worksStatus4pb", worksStatus4pb);
	}
	
	public List loadProcess(String procId){
		try {
			return flowProcessDao.loadByProcIds(new String[]{procId});
		} catch (NTBException e) {
			e.printStackTrace();
		}
		return null;
	}
	//addd by linrui 20190916 for CR589
	public List listWorkByUserAll(NTBUser user, Date fromDate, Date toDate, String sortOrder)
			throws NTBException {
		List works = null;

		if (user instanceof AbstractCorpUser) {
			AbstractCorpUser corpUser = (AbstractCorpUser) user;
			if (Constants.ROLE_APPROVER.equals(corpUser.getRoleId())
					|| Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())
					|| Constants.ROLE_ADMINISTRATOR
							.equals(corpUser.getRoleId())) {
				works = flowWorkDao.findByUserAll(corpUser.getCorpId(),
						corpUser.getUserId(), fromDate, toDate, sortOrder);
			}
		} else {
			AbstractBankUser bankUser = (AbstractBankUser) user;
			if (Constants.ROLE_BANK_SUPERVISOR.equals(bankUser.getRoleId())) {
				works = flowWorkDao.findByUserAll(CORPID_BANK,
						bankUser.getUserId(), fromDate, toDate, sortOrder);
			}
		}

		if (null != works) {
			works = worksToMaps(works);
		}

		works = addProgressInfo(works, user);

		return works;
	}
	
	
}
