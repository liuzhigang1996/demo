package app.cib.dao.flow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.type.StringType;
import net.sf.hibernate.type.TimestampType;
import net.sf.hibernate.type.Type;
import app.cib.bo.flow.FlowProcess;
import app.cib.bo.flow.FlowProcessHis;
import app.cib.bo.flow.FlowWork;
import app.cib.bo.flow.FlowWorkHis;
import app.cib.service.flow.FlowEngineService;

import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;

public class FlowWorkDao extends GenericHibernateDao {

	private static final String defalutPattern = Config
			.getProperty("DefaultDatePattern");

	public List findByProcIdPending(String procId) throws NTBException {
		List works = null;
		try {
			works = getHibernateTemplate().find(FIND_BY_PROCID_PENDING,
					new Object[] { procId });
		} catch (Exception e) {
			Log.error("Error finding pending works by proc", e);
			throw new NTBException("err.sys.DBError");
		}

		
		return works;
	}

	private static final String FIND_BY_PROCID_PENDING = "from FlowWork as flowWork where (flowWork.workStatus='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' or flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "') and flowWork.flowProcess.procId = ? order by flowWork.workId";

	public List findByProcIdDealed(String procId) throws NTBException {
		try {
			return getHibernateTemplate().find(FIND_BY_PROCID_DEALED,
					new Object[] { procId });
		} catch (Exception e) {
			Log.error("Error dealed pending works by proc", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	private static final String FIND_BY_PROCID_DEALED = "from FlowWork as flowWork "
			+ "where flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork.flowProcess.procId = ? "
			+ "order by flowWork.dealEndTime";

	// Modified by Na Bai 2013-12-31
	// Ϊ�Ż����̲�ѯ���� findByProcIdsAll����in��佫�����ѯ�ϲ���һ��
	public Map findByProcIdsAll(String[] procIds) throws NTBException {
		try {

			// ��flowProcess�Ҳ�����¼��ȥ����ʷ��
			// ��װin ���
			StringBuffer procIdsStr = new StringBuffer("");
			for (int i = 0; i < procIds.length; i++) {
				if (i > 0) {
					procIdsStr.append(",");
				}
				procIdsStr.append("'");
				procIdsStr.append(procIds[i]);
				procIdsStr.append("'");
			}

			// ��װhql
			String FIND_BY_PROCIDS_ALL = "from FlowWork as flowWork where "
					+ "flowWork.flowProcess.procId in ("
					+ procIdsStr.toString()
					+ ") order by flowWork.flowProcess.procCreateTime desc,flowWork.workId ";

			// ��ѯFlowWork��
			List flowWorkList = getHibernateTemplate().find(
					FIND_BY_PROCIDS_ALL, new Object[] {});
			LinkedHashMap returnMap = new LinkedHashMap();

			// �ѽ������procId�Ž�Map
			String prevProcId = "";
			String currProcId = "";
			List currWorkList = null;
			for (int i = 0; i < flowWorkList.size(); i++) {
				FlowWork flowWork = (FlowWork) flowWorkList.get(i);
				currProcId = flowWork.getFlowProcess().getProcId();
				if (!currProcId.equals(prevProcId)) {
					currWorkList = new ArrayList();
					returnMap.put(currProcId, currWorkList);
					prevProcId = currProcId;
				}
				currWorkList.add(flowWork);
			}

			// ɸѡ�Ѿ��鵽��procId���Ѿ��鵽����Ҫ����ʷ���в�ѯ
			String[] hisProcIds = new String[procIds.length - returnMap.size()];
			int count = 0;
			for (int i = 0; i < procIds.length; i++) {
				if (!returnMap.containsKey(procIds[i])) {
					hisProcIds[count++] = procIds[i];
				}
			}

			List flowWorkListNew = new ArrayList();
			// Ϊ��ʷ����װin ���
			procIdsStr = new StringBuffer("");
			for (int i = 0; i < hisProcIds.length; i++) {
				if (i > 0) {
					procIdsStr.append(",");
				}
				procIdsStr.append("'");
				procIdsStr.append(hisProcIds[i]);
				procIdsStr.append("'");
			}

			if("".equals(procIdsStr.toString())){
				procIdsStr = new StringBuffer("''") ;
			}
			// ��װ��ʷ���hql
			String FIND_HIS_BY_PROCIDS_ALL = "from FlowWorkHis as flowWork where "
					+ "flowWork.flowProcessHis.procId in ("
					+ procIdsStr.toString()
					+ ") order by flowWork.flowProcessHis.procCreateTime desc,flowWork.workId ";
			flowWorkList = getHibernateTemplate().find(FIND_HIS_BY_PROCIDS_ALL,
					new Object[] {});

			// �ѽ������procId�Ž�Map
			for (int i = 0; i < flowWorkList.size(); i++) {
				FlowWork flowWorkNew = new FlowWork();
				FlowWorkHis flowWorkHis = (FlowWorkHis) flowWorkList.get(i);
				if (flowWorkHis != null) {
					FlowProcess flowProcessNew = new FlowProcess();
					FlowProcessHis flowProcessHis = flowWorkHis
							.getFlowProcessHis();
					if (flowProcessHis != null) {
						flowProcessNew.setProcId(flowProcessHis.getProcId());
						flowProcessNew.setAllowExecutor(flowProcessHis
								.getAllowExecutor());
						flowProcessNew.setAmount(flowProcessHis.getAmount());
						flowProcessNew.setApprovers(flowProcessHis
								.getApprovers());
						flowProcessNew.setApproveRule(flowProcessHis
								.getApproveRule());
						flowProcessNew.setApproveStatus(flowProcessHis
								.getApproveStatus());
						flowProcessNew.setApproveType(flowProcessHis
								.getApproveType());
						flowProcessNew.setCorpId(flowProcessHis.getCorpId());
						flowProcessNew
								.setCurrency(flowProcessHis.getCurrency());
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
						flowProcessNew.setProcStatus(flowProcessHis
								.getProcStatus());
						flowProcessNew
								.setRuleFlag(flowProcessHis.getRuleFlag());
						flowProcessNew
								.setToAmount(flowProcessHis.getToAmount());
						flowProcessNew.setToCurrency(flowProcessHis
								.getToCurrency());
						flowProcessNew.setTransDesc(flowProcessHis
								.getTransDesc());
						flowProcessNew.setTransNo(flowProcessHis.getTransNo());
						flowProcessNew.setTxnBean(flowProcessHis.getTxnBean());
						flowProcessNew.setTxnCategory(flowProcessHis
								.getTxnCategory());
						flowProcessNew.setTxnType(flowProcessHis.getTxnType());
					}
					flowWorkNew.setWorkId(flowWorkHis.getWorkId());
					flowWorkNew.setApproveLevel(flowWorkHis.getApproveLevel());
					flowWorkNew.setAssignedDealer(flowWorkHis
							.getAssignedDealer());
					flowWorkNew.setDealAction(flowWorkHis.getDealAction());
					flowWorkNew
							.setDealBeginTime(flowWorkHis.getDealBeginTime());
					flowWorkNew.setDealEndTime(flowWorkHis.getDealEndTime());
					flowWorkNew.setDealMemo(flowWorkHis.getDealMemo());
					flowWorkNew.setLevelFinishCount(flowWorkHis
							.getLevelFinishCount());
					flowWorkNew.setFlowProcess(flowProcessNew);
					flowWorkNew.setWorkCreateTime(flowWorkHis
							.getWorkCreateTime());
					flowWorkNew.setWorkCreator(flowWorkHis.getWorkCreator());
					flowWorkNew.setWorkCreatorName(flowWorkHis
							.getWorkCreatorName());
					flowWorkNew.setWorkDealer(flowWorkHis.getWorkDealer());
					flowWorkNew.setWorkDealerName(flowWorkHis
							.getWorkDealerName());
					flowWorkNew.setWorkStatus(flowWorkHis.getWorkStatus());
				}
				currProcId = flowWorkHis.getFlowProcessHis().getProcId();
				if (!currProcId.equals(prevProcId)) {
					currWorkList = new ArrayList();
					returnMap.put(currProcId, currWorkList);
					prevProcId = currProcId;
				}
				currWorkList.add(flowWorkNew);

			}
			
			//����Map
			return returnMap;
		} catch (Exception e) {
			Log.error("Error finding works by proc", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public List findByProcIdAll(String procId) throws NTBException {
		try {
			// Jet modify 2008-10-28 ���flowProcess�Ҳ�����¼��ȥ����ʷ��
			List flowWorkList = getHibernateTemplate().find(FIND_BY_PROCID_ALL,
					new Object[] { procId });
			if (flowWorkList.size() == 0) {
				List flowWorkListNew = new ArrayList();
				flowWorkList = getHibernateTemplate().find(
						FIND_HIS_BY_PROCID_ALL, new Object[] { procId });
				if (flowWorkList.size() > 0) {
					for (int i = 0; i < flowWorkList.size(); i++) {
						FlowWork flowWorkNew = new FlowWork();
						FlowWorkHis flowWorkHis = (FlowWorkHis) flowWorkList
								.get(i);
						if (flowWorkHis != null) {
							FlowProcess flowProcessNew = new FlowProcess();
							FlowProcessHis flowProcessHis = flowWorkHis
									.getFlowProcessHis();
							if (flowProcessHis != null) {
								flowProcessNew.setProcId(flowProcessHis
										.getProcId());
								flowProcessNew.setAllowExecutor(flowProcessHis
										.getAllowExecutor());
								flowProcessNew.setAmount(flowProcessHis
										.getAmount());
								flowProcessNew.setApprovers(flowProcessHis
										.getApprovers());
								flowProcessNew.setApproveRule(flowProcessHis
										.getApproveRule());
								flowProcessNew.setApproveStatus(flowProcessHis
										.getApproveStatus());
								flowProcessNew.setApproveType(flowProcessHis
										.getApproveType());
								flowProcessNew.setCorpId(flowProcessHis
										.getCorpId());
								flowProcessNew.setCurrency(flowProcessHis
										.getCurrency());
								flowProcessNew.setLatestDealer(flowProcessHis
										.getLatestDealer());
								flowProcessNew
										.setLatestDealerName(flowProcessHis
												.getLatestDealerName());
								flowProcessNew.setLatestDealTime(flowProcessHis
										.getLatestDealTime());
								flowProcessNew.setProcCreateTime(flowProcessHis
										.getProcCreateTime());
								flowProcessNew.setProcCreator(flowProcessHis
										.getProcCreator());
								flowProcessNew
										.setProcCreatorName(flowProcessHis
												.getProcCreatorName());
								flowProcessNew.setProcFinishTime(flowProcessHis
										.getProcFinishTime());
								flowProcessNew.setProcStatus(flowProcessHis
										.getProcStatus());
								flowProcessNew.setRuleFlag(flowProcessHis
										.getRuleFlag());
								flowProcessNew.setToAmount(flowProcessHis
										.getToAmount());
								flowProcessNew.setToCurrency(flowProcessHis
										.getToCurrency());
								flowProcessNew.setTransDesc(flowProcessHis
										.getTransDesc());
								flowProcessNew.setTransNo(flowProcessHis
										.getTransNo());
								flowProcessNew.setTxnBean(flowProcessHis
										.getTxnBean());
								flowProcessNew.setTxnCategory(flowProcessHis
										.getTxnCategory());
								flowProcessNew.setTxnType(flowProcessHis
										.getTxnType());
							}
							flowWorkNew.setWorkId(flowWorkHis.getWorkId());
							flowWorkNew.setApproveLevel(flowWorkHis
									.getApproveLevel());
							flowWorkNew.setAssignedDealer(flowWorkHis
									.getAssignedDealer());
							flowWorkNew.setDealAction(flowWorkHis
									.getDealAction());
							flowWorkNew.setDealBeginTime(flowWorkHis
									.getDealBeginTime());
							flowWorkNew.setDealEndTime(flowWorkHis
									.getDealEndTime());
							flowWorkNew.setDealMemo(flowWorkHis.getDealMemo());
							flowWorkNew.setLevelFinishCount(flowWorkHis
									.getLevelFinishCount());
							flowWorkNew.setFlowProcess(flowProcessNew);
							flowWorkNew.setWorkCreateTime(flowWorkHis
									.getWorkCreateTime());
							flowWorkNew.setWorkCreator(flowWorkHis
									.getWorkCreator());
							flowWorkNew.setWorkCreatorName(flowWorkHis
									.getWorkCreatorName());
							flowWorkNew.setWorkDealer(flowWorkHis
									.getWorkDealer());
							flowWorkNew.setWorkDealerName(flowWorkHis
									.getWorkDealerName());
							flowWorkNew.setWorkStatus(flowWorkHis
									.getWorkStatus());
						}
						flowWorkListNew.add(flowWorkNew);
					}
				}
				return flowWorkListNew;
			} else {
				return flowWorkList;
			}
		} catch (Exception e) {
			Log.error("Error finding works by proc", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	private static final String FIND_BY_PROCID_ALL = "from FlowWork as flowWork "
			+ "where flowWork.flowProcess.procId = ? "
			+ "order by flowWork.workId";

	// Jet added 2008-10-28
	private static final String FIND_HIS_BY_PROCID_ALL = "from FlowWorkHis as flowWork "
			+ "where flowWork.flowProcessHis.procId = ? "
			+ "order by flowWork.workId";

	public List findByCorp(String corpId, String userId) throws NTBException {
		try {
			return getHibernateTemplate().find(FIND_BY_CORP_1,
					new Object[] { userId, corpId, userId });
		} catch (Exception e) {
			Log.error("Error works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	private static final String FIND_BY_CORP_1 = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "') or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and flowWork.workDealer = ? )) "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_NONFINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";

	public List findByCorpChecked(String corpId, String userId)
			throws NTBException {
		try {
			return getHibernateTemplate().find(FIND_BY_CORP_CHECKED_1,
					new Object[] { corpId, userId });
		} catch (Exception e) {
			Log.error("Error finding checked works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	//add by linrui 20190916 for cr589
	public List findByCorpChecked(String corpId, String userId, String sortOrder)
			throws NTBException {
		sortOrder = sortOrder.equals("1")?"desc":"asc";
		try {
			return getHibernateTemplate().find(FIND_BY_CORP_CHECKED_1_NOORDER + sortOrder,
					new Object[] { corpId, userId });
		} catch (Exception e) {
			Log.error("Error finding checked works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public List findByCorpCheckedSelf(String corpId, String userId)
			throws NTBException {
		try {
			return getHibernateTemplate().find(FIND_BY_CORP_CHECKED_1_SELF,
					new Object[] { corpId, userId });
		} catch (Exception e) {
			Log.error("Error finding checked works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	private static final String FIND_BY_CORP_CHECKED_1 = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "') or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "')) and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_NONFINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";
	private static final String FIND_BY_CORP_CHECKED_1_NOORDER = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "') or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "')) and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_NONFINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime ";

	private static final String FIND_BY_CORP_CHECKED_1_SELF = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "') or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "')) and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_NONFINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? and flowWork1.dealAction != 'R' "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";

	public List findByCorp(String corpId, String userId, String aLevel,
			String allowFinancialController, String financialControllerFlag)
			throws NTBException {
		try {
			if ("Y".equals(allowFinancialController)) {
				if ("1".equals(financialControllerFlag)) {
					return getHibernateTemplate().find(
							FIND_BY_CORP_2,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				} else {
					return getHibernateTemplate().find(
							FIND_BY_CORP_2_FINANCIAL_CONTROLLER,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				}
			} else {
				return getHibernateTemplate()
						.find(FIND_BY_CORP_2,
								new Object[] { aLevel, userId, userId, corpId,
										userId });
			}
		} catch (Exception e) {
			Log.error("Error finding works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public List findByCorpSelf(String corpId, String userId, String aLevel,
			String allowFinancialController, String financialControllerFlag)
			throws NTBException {
		try {
			if ("Y".equals(allowFinancialController)) {
				if ("1".equals(financialControllerFlag)) {
					return getHibernateTemplate().find(
							FIND_BY_CORP_2_SELF,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				} else {
					return getHibernateTemplate().find(
							FIND_BY_CORP_2_SELF_FINANCIAL_CONTROLLER,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				}
			} else {
				return getHibernateTemplate()
						.find(FIND_BY_CORP_2_SELF,
								new Object[] { aLevel, userId, userId, corpId,
										userId });
			}
		} catch (Exception e) {
			Log.error("Error finding works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	private static final String FIND_BY_CORP_2 = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ? )"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? ) )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and flowWork.workDealer = ? )) "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";

	private static final String FIND_BY_CORP_2_FINANCIAL_CONTROLLER = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ? "
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ ")"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =?"
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ " ) )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and flowWork.workDealer = ? "
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ ")) "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";

	private static final String FIND_BY_CORP_2_SELF = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ? )"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? ) )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and flowWork.workDealer = ? )) "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? and flowWork1.dealAction != 'R' "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";

	private static final String FIND_BY_CORP_2_SELF_FINANCIAL_CONTROLLER = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ? "
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ ")"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =?  "
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ " ) )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and flowWork.workDealer = ? "
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ ")) "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? and flowWork1.dealAction != 'R' "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";

	public List findByCorpChecked(String corpId, String userId, String aLevel,
			String allowFinancialController, String financialControllerFlag)
			throws NTBException {
		try {
			if ("Y".equals(allowFinancialController)) {
				if ("1".equals(financialControllerFlag)) {
					return getHibernateTemplate().find(
							FIND_BY_CORP_CHECKED_2,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				} else {
					return getHibernateTemplate().find(
							FIND_BY_CORP_CHECKED_2_FINANCIAL_CONTROLLER,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				}
			} else {
				return getHibernateTemplate()
						.find(FIND_BY_CORP_CHECKED_2,
								new Object[] { aLevel, userId, userId, corpId,
										userId });
			}
		} catch (Exception e) {
			Log.error("Error finding checked works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	//add by linrui 20190916 for cr589
	public List findByCorpChecked(String corpId, String userId, String aLevel,
			String allowFinancialController, String financialControllerFlag, String sortOrder)
					throws NTBException {
		sortOrder = sortOrder.equals("1")?"desc":"asc";
		try {
			if ("Y".equals(allowFinancialController)) {
				if ("1".equals(financialControllerFlag)) {
					return getHibernateTemplate().find(
							FIND_BY_CORP_CHECKED_2_NOORDER + sortOrder,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				} else {
					return getHibernateTemplate().find(
							FIND_BY_CORP_CHECKED_2_FINANCIAL_CONTROLLER_NOORDER + sortOrder,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				}
			} else {
				return getHibernateTemplate()
						.find(FIND_BY_CORP_CHECKED_2_NOORDER + sortOrder,
								new Object[] { aLevel, userId, userId, corpId,
										userId });
			}
		} catch (Exception e) {
			Log.error("Error finding checked works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public List findByCorpCheckedSelf(String corpId, String userId,
			String aLevel, String allowFinancialController,
			String financialControllerFlag) throws NTBException {
		try {
			if ("Y".equals(allowFinancialController)) {
				if ("1".equals(financialControllerFlag)) {
					return getHibernateTemplate().find(
							FIND_BY_CORP_CHECKED_2_SELF,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				} else {
					return getHibernateTemplate().find(
							FIND_BY_CORP_CHECKED_2_SELF_FINANCIAL_CONTROLLER,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				}
			} else {
				return getHibernateTemplate()
						.find(FIND_BY_CORP_CHECKED_2_SELF,
								new Object[] { aLevel, userId, userId, corpId,
										userId });
			}
		} catch (Exception e) {
			Log.error("Error finding checked works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	//add by linrui 20190916 for cr589
	public List findByCorpCheckedSelf(String corpId, String userId,
			String aLevel, String allowFinancialController,
			String financialControllerFlag, String sortOrder) throws NTBException {
		sortOrder = sortOrder.equals("1")?"desc":"asc";
		try {
			if ("Y".equals(allowFinancialController)) {
				if ("1".equals(financialControllerFlag)) {
					return getHibernateTemplate().find(
							FIND_BY_CORP_CHECKED_2_SELF_NOORDER + sortOrder,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				} else {
					return getHibernateTemplate().find(
							FIND_BY_CORP_CHECKED_2_SELF_FINANCIAL_CONTROLLER_NOORDER + sortOrder,
							new Object[] { aLevel, userId, userId, corpId,
									userId });
				}
			} else {
				return getHibernateTemplate()
						.find(FIND_BY_CORP_CHECKED_2_SELF_NOORDER + sortOrder,
								new Object[] { aLevel, userId, userId, corpId,
										userId });
			}
		} catch (Exception e) {
			Log.error("Error finding checked works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	/*
	 * FIND_BY_CORP_CHECKED_2 from FlowWork as flowWork where
	 * flowWork.flowProcess.procStatus = 'FlowEngineService.PROCESS_STATUS_NEW'
	 * and ( ( flowWork.workStatus ='FlowEngineService.WORK_STATUS_NEW' and ( (
	 * flowWork.flowProcess.approveType =
	 * 'FlowEngineService.APPROVE_TYPE_UNASSIGNED' and flowWork.approveLevel >=
	 * ? ) or (
	 * flowWork.flowProcess.approveType='FlowEngineService.APPROVE_TYPE_ASSIGNED'
	 * and flowWork.assignedDealer =? ) ) ) or ( flowWork.workStatus =
	 * 'FlowEngineService.WORK_STATUS_CHECKOUT' and (
	 * flowWork.flowProcess.approveType
	 * ='FlowEngineService.APPROVE_TYPE_UNASSIGNED' or (
	 * flowWork.flowProcess.approveType
	 * ='FlowEngineService.APPROVE_TYPE_ASSIGNED' and flowWork.assignedDealer =?
	 * ) ) ) ) and flowWork.flowProcess.corpId = ? and
	 * flowWork.flowProcess.txnCategory =
	 * 'FlowEngineService.TXN_CATEGORY_FINANCE' and ( not exists ( from FlowWork
	 * as flowWork1 where flowWork1.workStatus =
	 * 'FlowEngineService.WORK_STATUS_FINISH' and flowWork1.workDealer = ? and
	 * flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) order by
	 * flowWork.flowProcess.procCreateTime desc
	 */
	private static final String FIND_BY_CORP_CHECKED_2 = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ( ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ?)"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? ) )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and ( flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? ) ))) )  "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";
	private static final String FIND_BY_CORP_CHECKED_2_NOORDER = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ( ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ?)"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? ) )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and ( flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? ) ))) )  "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime ";

	private static final String FIND_BY_CORP_CHECKED_2_FINANCIAL_CONTROLLER = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ( ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ?"
			// if not financial controller, check this
			+ " and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ ")"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =?"
			// if not financial controller, check this
			+ " and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ " ) )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and (( flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "'"
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ ") or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? "
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ ") ))) )  "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";
	private static final String FIND_BY_CORP_CHECKED_2_FINANCIAL_CONTROLLER_NOORDER = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ( ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ?"
			// if not financial controller, check this
			+ " and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ ")"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =?"
			// if not financial controller, check this
			+ " and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ " ) )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and (( flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "'"
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ ") or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? "
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1 "
			+ ") ))) )  "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime ";

	/*
	 * from FlowWork as flowWork where flowWork.flowProcess.procStatus =
	 * 'FlowEngineService.PROCESS_STATUS_NEW' and ( ( flowWork.workStatus
	 * ='FlowEngineService.WORK_STATUS_NEW' and ( (
	 * flowWork.flowProcess.approveType =
	 * 'FlowEngineService.APPROVE_TYPE_UNASSIGNED' and flowWork.approveLevel >=
	 * ? ) or (
	 * flowWork.flowProcess.approveType='FlowEngineService.APPROVE_TYPE_ASSIGNED'
	 * and flowWork.assignedDealer =? ) ) ) or ( flowWork.workStatus =
	 * 'FlowEngineService.WORK_STATUS_CHECKOUT' and (
	 * flowWork.flowProcess.approveType
	 * ='FlowEngineService.APPROVE_TYPE_UNASSIGNED' or (
	 * flowWork.flowProcess.approveType
	 * ='FlowEngineService.APPROVE_TYPE_ASSIGNED' and flowWork.assignedDealer =?
	 * ) ) ) ) and flowWork.flowProcess.corpId = ? and
	 * flowWork.flowProcess.txnCategory =
	 * 'FlowEngineService.TXN_CATEGORY_FINANCE' and ( not exists ( from FlowWork
	 * as flowWork1 where flowWork1.workStatus =
	 * 'FlowEngineService.WORK_STATUS_FINISH' and flowWork1.workDealer = ? and
	 * flowWork1.dealAction != 'R' and flowWork1.flowProcess.procId =
	 * flowWork.flowProcess.procId ) ) order by flowWork.flowProcess.procCreateTime desc
	 */
	private static final String FIND_BY_CORP_CHECKED_2_SELF = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ( ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ?)"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =?) )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and ( flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? ) ))) )  "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? and flowWork1.dealAction != 'R' "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";
	private static final String FIND_BY_CORP_CHECKED_2_SELF_NOORDER = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ( ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ?)"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =?) )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and ( flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? ) ))) )  "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? and flowWork1.dealAction != 'R' "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime ";

	private static final String FIND_BY_CORP_CHECKED_2_SELF_FINANCIAL_CONTROLLER = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ( ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ?"
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1"
			+ ")"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =?"
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1"
			+ ") )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and (( flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "'"
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1"
			+ ") or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? "
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1"
			+ ") ))) )  "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? and flowWork1.dealAction != 'R' "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";
	private static final String FIND_BY_CORP_CHECKED_2_SELF_FINANCIAL_CONTROLLER_NOORDER = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_NEW
			+ "' and ( ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "' and ( (flowWork.flowProcess.approveType = '"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "' and flowWork.approveLevel >= ?"
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1"
			+ ")"
			+ "or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =?"
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1"
			+ ") )) "
			+ "or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and (( flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_UNASSIGNED
			+ "'"
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1"
			+ ") or (flowWork.flowProcess.approveType='"
			+ FlowEngineService.APPROVE_TYPE_ASSIGNED
			+ "' and flowWork.assignedDealer =? "
			// if not financial controller, check this
			+ "  and (select count(*) from FlowWork as t1 where t1.flowProcess.procId=flowWork.flowProcess.procId and t1.dealAction='A' group by t1.flowProcess.procId)>=1"
			+ ") ))) )  "
			+ "and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? and flowWork1.dealAction != 'R' "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime ";

	public List findByCorpWithoutLevel(String corpId, String userId)
			throws NTBException {
		try {
			return getHibernateTemplate().find(FIND_BY_CORP_WITHOUT_LEVEL,
					new Object[] { userId, corpId, userId });
		} catch (Exception e) {
			Log.error("Error finding works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	private static final String FIND_BY_CORP_WITHOUT_LEVEL = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_EXECUTE_PENDING
			+ "' and ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "') or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and flowWork.workDealer = ? )) "
			+ "and flowWork.approveLevel is null and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";

	public List findByCorpWithoutLevelChecked(String corpId, String userId)
			throws NTBException {
		try {
			return getHibernateTemplate().find(
					FIND_BY_CORP_WITHOUT_LEVEL_CHECKED,
					new Object[] { corpId, userId });
		} catch (Exception e) {
			Log.error("Error finding works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	//ADD by linrui 20190916 for cr589
	public List findByCorpWithoutLevelChecked(String corpId, String userId, String sortOrder)
			throws NTBException {
		sortOrder = sortOrder.equals("1")?"desc":"asc";
		try {
			return getHibernateTemplate().find(
					FIND_BY_CORP_WITHOUT_LEVEL_CHECKED_NOORDER + sortOrder,
					new Object[] { corpId, userId });
		} catch (Exception e) {
			Log.error("Error finding works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	private static final String FIND_BY_CORP_WITHOUT_LEVEL_CHECKED = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_EXECUTE_PENDING
			+ "' and ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "') or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "')) and flowWork.approveLevel is null and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime desc";
	private static final String FIND_BY_CORP_WITHOUT_LEVEL_CHECKED_NOORDER = "from FlowWork as flowWork where "
			+ "flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_EXECUTE_PENDING
			+ "' and ((flowWork.workStatus ='"
			+ FlowEngineService.WORK_STATUS_NEW
			+ "') or (flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "')) and flowWork.approveLevel is null and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.txnCategory = '"
			+ FlowEngineService.TXN_CATEGORY_FINANCE
			+ "' and (not exists (from FlowWork as flowWork1 where flowWork1.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork1.workDealer = ? "
			+ "and flowWork1.flowProcess.procId = flowWork.flowProcess.procId ) ) "
			+ "order by flowWork.flowProcess.procCreateTime ";

	public List findByUserDealing(String corpId, String userId)
			throws NTBException {
		try {
			return getHibernateTemplate().find(FIND_BY_USER_DEALING,
					new Object[] { userId, corpId });
		} catch (Exception e) {
			Log.error("Error finding works by user", e);
			throw new NTBException("err.sys.DBError");
		}

	}
	//add by linrui 20190916 for cr589
	public List findByUserDealing(String corpId, String userId, String sortOrder)
			throws NTBException {
		sortOrder = sortOrder.equals("1")?"desc":"asc";
		try {
			return getHibernateTemplate().find(FIND_BY_USER_DEALING_NOORDER + sortOrder,
					new Object[] { userId, corpId });
		} catch (Exception e) {
			Log.error("Error finding works by user", e);
			throw new NTBException("err.sys.DBError");
		}
		
	}
	
	//add by lzg 20190816
	public List findByUserExpired(String corpId, String userId,
			String dateFrom, String dateTo) throws NTBException {
		List valueList = new ArrayList();
		String hql = "";
		hql += "from FlowWork as t where t.workStatus = ? and t.flowProcess.corpId = ? ";
		valueList.add(FlowEngineService.WORK_STATUS_EXPIRED);
		valueList.add(corpId);
		hql += "and t.flowProcess.procStatus = ? ";
		valueList.add(FlowEngineService.PROCESS_STATUS_EXPIRED);
		if (!Utils.null2EmptyWithTrim(userId).equals("")
				&& !Utils.null2EmptyWithTrim(userId).equals("0")) {
			hql += "and t.workDealer = ? ";
			valueList.add(userId);
		}
		
		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern,
					"yyyy-MM-dd");
			Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			hql += "and t.dealEndTime >= ? ";
			valueList.add(timeFrom);
		}
		
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
			Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			hql += "and t.dealEndTime <= ? ";
			valueList.add(timeTo);
		}
		hql += "order by t.flowProcess.procCreateTime desc";

		return this.list(hql, valueList.toArray());
	}
	//add by linrui 20190916 for cr589
	public List findByUserExpired(String corpId, String userId,
			String dateFrom, String dateTo, String sortOrder) throws NTBException {
		List valueList = new ArrayList();
		sortOrder = sortOrder.equals("1")?"desc":"asc";
		String hql = "";
		hql += "from FlowWork as t where t.workStatus = ? and t.flowProcess.corpId = ? ";
		valueList.add(FlowEngineService.WORK_STATUS_EXPIRED);
		valueList.add(corpId);
		hql += "and t.flowProcess.procStatus = ? ";
		valueList.add(FlowEngineService.PROCESS_STATUS_EXPIRED);
		if (!Utils.null2EmptyWithTrim(userId).equals("")
				&& !Utils.null2EmptyWithTrim(userId).equals("0")) {
			hql += "and t.workDealer = ? ";
			valueList.add(userId);
		}
		
		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern,
					"yyyy-MM-dd");
			Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			hql += "and t.dealEndTime >= ? ";
			valueList.add(timeFrom);
		}
		
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
			Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			hql += "and t.dealEndTime <= ? ";
			valueList.add(timeTo);
		}
		hql += "order by t.flowProcess.procCreateTime " + sortOrder;
		
		return this.list(hql, valueList.toArray());
	}
	
	public List findByUserExpired(String corpId, String userId) throws NTBException {
		try {
			return getHibernateTemplate().find(FIND_BY_USER_EXPIRED,
					new Object[] { userId, corpId });
		} catch (Exception e) {
			Log.error("Error finding works by user", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	//add by linrui 20190916
	public List findByUserExpired(String corpId, String userId, String sortOrder) throws NTBException {
		sortOrder = sortOrder.equals("1")?"desc":"asc";
		try {
			return getHibernateTemplate().find(FIND_BY_USER_EXPIRED_NOORDER + sortOrder,
					new Object[] { userId, corpId });
		} catch (Exception e) {
			Log.error("Error finding works by user", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	//add by lzg end
	
	
	
	// add by hjs 20070425 - for function(VIEW ALL TRANSACTION)
	public List findByUserDealing(String corpId, String userId,
			String dateFrom, String dateTo) throws NTBException {

		List valueList = new ArrayList();
       
		String hql = "";
		hql += "from FlowWork as t where t.workStatus = ? and t.flowProcess.corpId = ? ";
		hql += "and t.flowProcess.procStatus not in(?, ?, ?) ";

		valueList.add(FlowEngineService.WORK_STATUS_FINISH);
		valueList.add(corpId);
		valueList.add(FlowEngineService.PROCESS_STATUS_REJECT);
		valueList.add(FlowEngineService.PROCESS_STATUS_CANCEL);
		valueList.add(FlowEngineService.PROCESS_STATUS_FINISH);

		if (!Utils.null2EmptyWithTrim(userId).equals("")
				&& !Utils.null2EmptyWithTrim(userId).equals("0")) {
			hql += "and t.workDealer = ? ";
			valueList.add(userId);
		}
		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern,
					"yyyy-MM-dd");
			Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			hql += "and t.dealEndTime >= ? ";
			valueList.add(timeFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
			Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			hql += "and t.dealEndTime <= ? ";
			valueList.add(timeTo);
		}

		hql += "order by t.flowProcess.procCreateTime desc";

		return this.list(hql, valueList.toArray());

	}
	//add by linrui 20190916 for cr589
	public List findByUserDealing(String corpId, String userId,
			String dateFrom, String dateTo, String sortOrder) throws NTBException {
		sortOrder = sortOrder.equals("1")?"desc":"asc";
		List valueList = new ArrayList();
		
		String hql = "";
		hql += "from FlowWork as t where t.workStatus = ? and t.flowProcess.corpId = ? ";
		hql += "and t.flowProcess.procStatus not in(?, ?, ?) ";
		
		valueList.add(FlowEngineService.WORK_STATUS_FINISH);
		valueList.add(corpId);
		valueList.add(FlowEngineService.PROCESS_STATUS_REJECT);
		valueList.add(FlowEngineService.PROCESS_STATUS_CANCEL);
		valueList.add(FlowEngineService.PROCESS_STATUS_FINISH);
		
		if (!Utils.null2EmptyWithTrim(userId).equals("")
				&& !Utils.null2EmptyWithTrim(userId).equals("0")) {
			hql += "and t.workDealer = ? ";
			valueList.add(userId);
		}
		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern,
					"yyyy-MM-dd");
			Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			hql += "and t.dealEndTime >= ? ";
			valueList.add(timeFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
			Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			hql += "and t.dealEndTime <= ? ";
			valueList.add(timeTo);
		}
		
		hql += "order by t.flowProcess.procCreateTime " + sortOrder;
		
		return this.list(hql, valueList.toArray());
		
	}

	private static final String FIND_BY_USER_DEALING = "from FlowWork as flowWork where flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork.workDealer = ?  and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.procStatus not in ('"
			+ FlowEngineService.PROCESS_STATUS_REJECT
			+ "','"
			+ FlowEngineService.PROCESS_STATUS_CANCEL
			+ "','"
			+ FlowEngineService.PROCESS_STATUS_FINISH
			+ "') order by flowWork.flowProcess.procCreateTime desc";
	private static final String FIND_BY_USER_DEALING_NOORDER = "from FlowWork as flowWork where flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork.workDealer = ?  and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.procStatus not in ('"
			+ FlowEngineService.PROCESS_STATUS_REJECT
			+ "','"
			+ FlowEngineService.PROCESS_STATUS_CANCEL
			+ "','"
			+ FlowEngineService.PROCESS_STATUS_FINISH
			+ "') order by flowWork.flowProcess.procCreateTime ";
	
	private static final String FIND_BY_USER_EXPIRED = "from FlowWork as flowWork where flowWork.workStatus = '"
		+ FlowEngineService.WORK_STATUS_EXPIRED
		+ "' and flowWork.workDealer = ?  and flowWork.flowProcess.corpId = ? "
		+ "and flowWork.flowProcess.procStatus = '"
		+ FlowEngineService.PROCESS_STATUS_EXPIRED
		+"' order by flowWork.flowProcess.procCreateTime desc";
	private static final String FIND_BY_USER_EXPIRED_NOORDER = "from FlowWork as flowWork where flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_EXPIRED
			+ "' and flowWork.workDealer = ?  and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.procStatus = '"
			+ FlowEngineService.PROCESS_STATUS_EXPIRED
			+"' order by flowWork.flowProcess.procCreateTime ";

	public List findByUserDealing(String corpId, String userId, Date fromDate,
			Date toDate) throws NTBException {
		try {
			StringType stringType = new StringType();
			TimestampType timestampType = new TimestampType();
			return getHibernateTemplate().find(
					FIND_BY_USER_DEALING_1,
					new Object[] { userId, corpId,
							new Timestamp(fromDate.getTime()),
							new Timestamp(toDate.getTime()) },
					new Type[] { stringType, stringType, timestampType,
							timestampType });
		} catch (Exception e) {
			Log.error("Error finding dealing works by user", e);
			throw new NTBException("err.sys.DBError");
		}

	}

	private static final String FIND_BY_USER_DEALING_1 = "from FlowWork as flowWork where flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork.workDealer = ?  and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.dealEndTime >= ? and flowWork.dealEndTime <= ? "
			+ "and flowWork.flowProcess.procStatus not in ('"
			+ FlowEngineService.PROCESS_STATUS_REJECT
			+ "','"
			+ FlowEngineService.PROCESS_STATUS_CANCEL
			+ "','"
			+ FlowEngineService.PROCESS_STATUS_FINISH
			+ "') order by flowWork.flowProcess.procCreateTime desc";

	public List findByCorpDealing(String corpId) throws NTBException {
		try {
			StringType stringType = new StringType();
			return getHibernateTemplate().find(FIND_BY_CORP_DEALING,
					new Object[] { corpId }, new Type[] { stringType });
		} catch (Exception e) {
			Log.error("Error finding dealing works by corp", e);
			throw new NTBException("err.sys.DBError");
		}

	}

	private static final String FIND_BY_CORP_DEALING = "from FlowWork as flowWork where flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.flowProcess.procStatus not in ('"
			+ FlowEngineService.PROCESS_STATUS_REJECT
			+ "','"
			+ FlowEngineService.PROCESS_STATUS_CANCEL
			+ "','"
			+ FlowEngineService.PROCESS_STATUS_FINISH
			+ "') order by flowWork.flowProcess.procCreateTime desc";

	public List findByCorpDealing(String corpId, Date fromDate, Date toDate)
			throws NTBException {
		try {
			StringType stringType = new StringType();
			TimestampType timestampType = new TimestampType();
			return getHibernateTemplate().find(
					FIND_BY_CORP_DEALING_1,
					new Object[] { corpId, new Timestamp(fromDate.getTime()),
							new Timestamp(toDate.getTime()) },
					new Type[] { stringType, timestampType, timestampType });
		} catch (Exception e) {
			Log.error("Error finding dealing works by corp", e);
			throw new NTBException("err.sys.DBError");
		}

	}

	private static final String FIND_BY_CORP_DEALING_1 = "from FlowWork as flowWork where flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork.flowProcess.corpId = ? "
			+ "and flowWork.dealEndTime >= ? and flowWork.dealEndTime <= ? "
			+ "and flowWork.flowProcess.procStatus not in ('"
			+ FlowEngineService.PROCESS_STATUS_REJECT
			+ "','"
			+ FlowEngineService.PROCESS_STATUS_CANCEL
			+ "','"
			+ FlowEngineService.PROCESS_STATUS_FINISH
			+ "') order by flowWork.flowProcess.procCreateTime desc";

	public List findByUserAll(String corpId, String userId, Date fromDate,
			Date toDate) throws NTBException {
		try {
			StringType stringType = new StringType();
			TimestampType timestampType = new TimestampType();
			return getHibernateTemplate().find(
					FIND_BY_USER_ALL,
					new Object[] { userId, corpId,
							new Timestamp(fromDate.getTime()),
							new Timestamp(toDate.getTime()) },
					new Type[] { stringType, stringType, timestampType,
							timestampType });

		} catch (Exception e) {
			Log.error("Error finding works by user", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	//add by linrui 20190916 for CR589
	public List findByUserAll(String corpId, String userId, Date fromDate,
			Date toDate, String sortOrder) throws NTBException {
		sortOrder = sortOrder.equals("1")?"desc":"asc";
		try {
			StringType stringType = new StringType();
			TimestampType timestampType = new TimestampType();
			return getHibernateTemplate().find(
					FIND_BY_USER_ALL_NO_SORT + sortOrder,
					new Object[] { userId, corpId,
							new Timestamp(fromDate.getTime()),
							new Timestamp(toDate.getTime()) },
					new Type[] { stringType, stringType, timestampType,
							timestampType });
			
		} catch (Exception e) {
			Log.error("Error finding works by user", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public List findByUserAllForReport(String corpId, String userId,
			Date fromDate, Date toDate) throws NTBException {
		try {
			StringType stringType = new StringType();
			TimestampType timestampType = new TimestampType();
			return getHibernateTemplate().find(
					FIND_BY_USER_ALL_FOR_REPORT,
					new Object[] { userId, corpId,
							new Timestamp(fromDate.getTime()),
							new Timestamp(toDate.getTime()) },
					new Type[] { stringType, stringType, timestampType,
							timestampType });

		} catch (Exception e) {
			Log.error("Error finding works by user", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	private static final String FIND_BY_USER_ALL = "from FlowWorkHis as flowWork where flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork.workDealer = ? and flowWork.flowProcessHis.corpId = ? "
			+ "and flowWork.dealEndTime >= ? and flowWork.dealEndTime <= ? "
			+ "order by flowWork.flowProcessHis.procCreateTime desc";
	//add by linrui 20190916 for CR589
	private static final String FIND_BY_USER_ALL_NO_SORT = "from FlowWorkHis as flowWork where flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork.workDealer = ? and flowWork.flowProcessHis.corpId = ? "
			+ "and flowWork.dealEndTime >= ? and flowWork.dealEndTime <= ? "
			+ "order by flowWork.flowProcessHis.procCreateTime ";

	private static final String FIND_BY_USER_ALL_FOR_REPORT = "from FlowWorkHis as flowWork where flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork.workCreator = ? and flowWork.flowProcessHis.corpId = ? "
			+ "and flowWork.dealEndTime >= ? and flowWork.dealEndTime <= ? "
			+ "order by flowWork.flowProcessHis.procCreateTime desc";

	public List findByCorpAll(String corpId, Date fromDate, Date toDate)
			throws NTBException {
		try {
			StringType stringType = new StringType();
			TimestampType timestampType = new TimestampType();
			return getHibernateTemplate().find(
					FIND_BY_CORP_ALL,
					new Object[] { corpId, new Timestamp(fromDate.getTime()),
							new Timestamp(toDate.getTime()) },
					new Type[] { stringType, timestampType, timestampType });

		} catch (Exception e) {
			Log.error("Error finding works by corp", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	// modify by hjs 20070123
	private static final String FIND_BY_CORP_ALL = "from FlowWorkHis as flowWork where flowWork.workStatus = '"
			+ FlowEngineService.WORK_STATUS_FINISH
			+ "' and flowWork.flowProcessHis.corpId = ? "
			+ "and flowWork.dealEndTime >= ? and flowWork.dealEndTime <= ? "
			+ "order by flowWork.flowProcessHis.procCreateTime desc, flowWork.dealEndTime";

	public List findUserCheckout(String userId) throws NTBException {
		try {
			return getHibernateTemplate().find(FIND_USER_CHECKOUT,
					new Object[] { userId });
		} catch (Exception e) {
			Log.error("Error checkout works by user", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	private static final String FIND_USER_CHECKOUT = "from FlowWork as flowWork where "
			+ "flowWork.workStatus='"
			+ FlowEngineService.WORK_STATUS_CHECKOUT
			+ "' and flowWork.workDealer=? order by flowWork.workId";

	public List findAssignedPendingListByUserId(String corpId, String userId,
			int type) throws NTBException {
		final String templateHql = "" + "from FlowWork as flowWork " + "where "
				+ "flowWork.flowProcess.procStatus = '"
				+ FlowEngineService.PROCESS_STATUS_NEW
				+ "' "
				+ "and "
				+ "("
				+ "	("
				+ "		flowWork.workStatus ='"
				+ FlowEngineService.WORK_STATUS_NEW
				+ "' "
				+ "		and "
				+ "		("
				+ "			flowWork.flowProcess.approveType='"
				+ FlowEngineService.APPROVE_TYPE_ASSIGNED
				+ "' "
				+ "			and "
				+ "			flowWork.assignedDealer =? "
				+ "		) "
				+ "	) "
				+ "	or "
				+ "	("
				+ "		flowWork.workStatus = '"
				+ FlowEngineService.WORK_STATUS_CHECKOUT
				+ "' "
				+ "		and "
				+ "		("
				+ "			flowWork.flowProcess.approveType='"
				+ FlowEngineService.APPROVE_TYPE_ASSIGNED
				+ "' "
				+ "			and "
				+ "			flowWork.assignedDealer =? "
				+ "		)"
				+ "	)"
				+ ")"
				+ "and "
				+ "flowWork.flowProcess.corpId = ? "
				+ "and "
				+ "flowWork.flowProcess.txnCategory = '"
				+ FlowEngineService.TXN_CATEGORY_FINANCE
				+ "' "
				+ "and "
				+ "(not exists (%1)) " + "order by flowWork.flowProcess.procCreateTime desc";
		String variableHql1 = "from FlowWork as flowWork1 where flowWork1.workStatus = '"
				+ FlowEngineService.WORK_STATUS_FINISH
				+ "' and flowWork1.workDealer = ? and flowWork1.flowProcess.procId = flowWork.flowProcess.procId";
		String variableHql2 = "from FlowWork as flowWork1 where flowWork1.workStatus = '"
				+ FlowEngineService.WORK_STATUS_FINISH
				+ "' and flowWork1.workDealer = ? and flowWork1.dealAction != 'R' and flowWork1.flowProcess.procId = flowWork.flowProcess.procId";
		if (type == 1) {
			return this.list(Utils.replaceStr(templateHql, "%1", variableHql1),
					new Object[] { userId, userId, corpId, userId });
		} else {
			return this.list(Utils.replaceStr(templateHql, "%1", variableHql2),
					new Object[] { userId, userId, corpId, userId });
		}
	}

}
