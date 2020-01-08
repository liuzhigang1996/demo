package app.cib.action.sys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.RBFactory;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.srv.TxnPrompt;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.dao.srv.TransferPromptDao;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.TransferPromptService;
import app.cib.util.Constants;

public class TransferPromptAction extends CibAction implements Approvable {
	
	public void load() throws NTBException{
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		RBFactory rbList = RBFactory.getInstance(
				"app.cib.resource.bnk.2ndMenu");
		ArrayList retList = new ArrayList();
		HashMap<String,String> menu2ndKey = rbList.getAllString();
		int count = 1;
		for(Map.Entry<String,String> entry : menu2ndKey.entrySet()){
			TxnPrompt txnPrompt = transferPromptService.loadByTxnType(entry.getKey(),TransferPromptDao.STATUS_NORMAL);
			Map resultMap = new HashMap();
			convertPojo2Map(txnPrompt, resultMap);
			resultMap.put("number", count);
			count++;
			retList.add(resultMap);
		}
		Map resultData = new HashMap();
		resultData.put("retList", retList);
		this.setResultData(resultData);
	}
	
	public void view() throws NTBException{
		String txnType = this.getParameter("menu2nd");
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		try{
			txnPrompt = transferPromptService.loadByTxnType(txnType,TransferPromptDao.STATUS_NORMAL);
			if(txnPrompt == null){
				throw new NTBException("DBError");
			}
		}catch (Exception e) {
			throw new NTBException("DBError");
		}
		Map resultData = new HashMap();
		String descriptionE = txnPrompt.getDescription("E");
		String descriptionC = txnPrompt.getDescription("C");
		resultData.put("descriptionC", descriptionC);
		resultData.put("descriptionE", descriptionE);
		resultData.put("menu2nd", txnType);
		this.setResultData(resultData);
	}
	
	public void viewLoginMessage() throws NTBException{
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		String txnType = "8";
		try{
			txnPrompt = transferPromptService.loadByTxnType(txnType,TransferPromptDao.STATUS_NORMAL);
			if(txnPrompt == null){
				throw new NTBException("DBError");
			}
		}catch (Exception e) {
			throw new NTBException("DBError");
		}
		Map resultData = new HashMap();
		List cMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"C");
		List eMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"E");
		resultData.put("menu2nd", txnPrompt.getTxnType());
		if(cMessageList !=null){
			resultData.put("cMessageList", cMessageList);
			resultData.put("cMessageCount", cMessageList.size());
		}
		if(eMessageList != null){
			resultData.put("eMessageList", eMessageList);	
			resultData.put("eMessageCount", eMessageList.size());	
		}
		this.setResultData(resultData);
	}
	
	public void cancel() throws NTBException{
		TxnPrompt txnPrompt = (TxnPrompt) this.getUsrSessionDataValue("txnPrompt");
		Map resultData = new HashMap();
		resultData.put("descriptionE", txnPrompt.getDescription("E"));
		resultData.put("descriptionC", txnPrompt.getDescription("C"));
		resultData.put("menu2nd", txnPrompt.getTxnType());
		this.setResultData(resultData);
	}
	public void cancelLoginMessage() throws NTBException{
		TxnPrompt txnPrompt = (TxnPrompt) this.getUsrSessionDataValue("txnPrompt");
		Map resultData = new HashMap();
		resultData.put("cMessageCount", this.getUsrSessionDataValue("cMessageCount"));
		resultData.put("eMessageCount", this.getUsrSessionDataValue("eMessageCount"));
		resultData.put("cMessageList", this.getUsrSessionDataValue("cMessageList"));
		resultData.put("eMessageList", this.getUsrSessionDataValue("eMessageList"));
		resultData.put("menu2nd", this.getUsrSessionDataValue("menu2nd"));
		this.setResultData(resultData);
	}
	
	public void editLoad() throws NTBException{
		String txnType = this.getParameter("menu2nd");
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		txnPrompt  = transferPromptService.loadByTxnType(txnType,TransferPromptDao.STATUS_NORMAL);
		if(txnPrompt == null)
			throw new NTBException("DBError");
		Map resultData = new HashMap();
		TxnPrompt penddingTxnPrompt = transferPromptService.loadByTxnType(txnType,TransferPromptDao.STATUS_PEDNING_APPAOVEL);
		if(penddingTxnPrompt != null)
			throw new NTBException("err.sys.OperationPending");
		
		String descriptionE = txnPrompt.getDescription("E");
		String descriptionC = txnPrompt.getDescription("C");
		resultData.put("descriptionC", descriptionC);
		resultData.put("descriptionE", descriptionE);
		resultData.put("menu2nd", txnType);
		this.setResultData(resultData);
	}
	
	public void editLoginMessageLoad() throws NTBException{
		String txnType = this.getParameter("menu2nd");
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		txnPrompt  = transferPromptService.loadByTxnType(txnType,TransferPromptDao.STATUS_NORMAL);
		if(txnPrompt == null)
			throw new NTBException("DBError");
		Map resultData = new HashMap();
		TxnPrompt penddingTxnPrompt = transferPromptService.loadByTxnType(txnType,TransferPromptDao.STATUS_PEDNING_APPAOVEL);
		if(penddingTxnPrompt != null)
			throw new NTBException("err.sys.OperationPending");
		
		List cMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"C");
		List eMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"E");
		resultData.put("menu2nd", txnType);
		if(cMessageList !=null){
			resultData.put("cMessageList", cMessageList);
			resultData.put("cMessageCount", cMessageList.size());
		}
		if(eMessageList != null){
			resultData.put("eMessageList", eMessageList);	
			resultData.put("eMessageCount", eMessageList.size());	
		}
		this.setResultData(resultData);
	}
	public void clearLoad() throws NTBException{
		String txnType = this.getParameter("menu2nd");
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		txnPrompt  = transferPromptService.loadByTxnType(txnType,TransferPromptDao.STATUS_NORMAL);
		if(txnPrompt == null)
			throw new NTBException("DBError");
		Map resultData = new HashMap();
		TxnPrompt penddingTxnPrompt = transferPromptService.loadByTxnType(txnType,TransferPromptDao.STATUS_PEDNING_APPAOVEL);
		if(penddingTxnPrompt != null)
			throw new NTBException("err.sys.OperationPending");
		String descriptionE = txnPrompt.getDescription("E");
		String descriptionC = txnPrompt.getDescription("C");
		resultData.put("descriptionC", descriptionC);
		resultData.put("descriptionE", descriptionE);
		resultData.put("menu2nd", txnType);
		this.setResultData(resultData);
	}
	
	public void clearLoginMessageLoad() throws NTBException{
		String txnType = this.getParameter("menu2nd");
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		txnPrompt  = transferPromptService.loadByTxnType(txnType,TransferPromptDao.STATUS_NORMAL);
		if(txnPrompt == null)
			throw new NTBException("DBError");
		Map resultData = new HashMap();
		TxnPrompt penddingTxnPrompt = transferPromptService.loadByTxnType(txnType,TransferPromptDao.STATUS_PEDNING_APPAOVEL);
		if(penddingTxnPrompt != null)
			throw new NTBException("err.sys.OperationPending");
		List cMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"C");
		List eMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"E");
		resultData.put("menu2nd", txnPrompt.getTxnType());
		if(cMessageList !=null){
			resultData.put("cMessageList", cMessageList);
			resultData.put("cMessageCount", cMessageList.size());
		}
		if(eMessageList != null){
			resultData.put("eMessageList", eMessageList);	
			resultData.put("eMessageCount", eMessageList.size());	
		}
		this.setResultData(resultData);
		setUsrSessionDataValue("cMessageCount", cMessageList.size());
		setUsrSessionDataValue("eMessageCount", eMessageList.size());
		setUsrSessionDataValue("cMessageList", cMessageList);
		setUsrSessionDataValue("eMessageList", eMessageList);
		setUsrSessionDataValue("menu2nd", txnType);
	}
	
	public void edit() throws NTBException{
		String descriptionE = this.getParameter("descriptionE");
		String descriptionC = this.getParameter("descriptionC");
		
		String txnType = this.getParameter("menu2nd");
		Map resultData = this.getResultData();
		TxnPrompt txnPrompt = new TxnPrompt();
		//out side 4000 skip
		txnPrompt.setDescription(descriptionE,"E");
		txnPrompt.setDescription(descriptionC,"C");
		txnPrompt.setTxnType(txnType);
		resultData.put("descriptionE", txnPrompt.getDescription("E"));
		resultData.put("descriptionC", txnPrompt.getDescription("C"));
		resultData.put("menu2nd", txnPrompt.getTxnType());
		setResultData(resultData);
		setUsrSessionDataValue("txnPrompt", txnPrompt);
		/*String lang = Utils.null2EmptyWithTrim(this.getParameter("lang"));
		String txnType = Utils.null2EmptyWithTrim(this.getParameter("txnType"));
		Map resultData = this.getResultData();
		TxnPrompt txnprompt = new TxnPrompt();
		//out side 4000 put to content2 3 4
		//int contentSize = content.length();
		txnprompt.setMessageContent1(content);
		txnprompt.setTxnType(txnType);
		txnprompt.setLang(lang);
		setUsrSessionDataValue("txnprompt", txnprompt);
		setResultData(getParameters());*/
	}
	public void editLoginMessage() throws NTBException{
		
		int countC = Integer.parseInt(this.getParameter("cMessageCount"));
		int countE = Integer.parseInt(this.getParameter("eMessageCount"));
		String txnType = this.getParameter("menu2nd");
		Map resultData = this.getResultData();
		
		ArrayList<HashMap<String,String>> cMessageList = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < countC; i++) {
			HashMap<String,String> messageMap = new HashMap<String,String>();
			messageMap.put("CTitle", this.getParameter("CTitle"+i));
			messageMap.put("CContent", this.getParameter("CContent"+i));
			cMessageList.add(messageMap);
		}
		ArrayList<HashMap<String,String>> eMessageList = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < countE; i++) {
			HashMap<String,String> messageMap = new HashMap<String,String>();
			messageMap.put("ETitle", this.getParameter("ETitle"+i));
			messageMap.put("EContent", this.getParameter("EContent"+i));
			eMessageList.add(messageMap);
		}
		resultData.put("cMessageCount", countC);
		resultData.put("eMessageCount", countE);
		resultData.put("cMessageList", cMessageList);
		resultData.put("eMessageList", eMessageList);
		resultData.put("menu2nd", txnType);
		setResultData(resultData);
		setUsrSessionDataValue("cMessageCount", countC);
		setUsrSessionDataValue("eMessageCount", countE);
		setUsrSessionDataValue("cMessageList", cMessageList);
		setUsrSessionDataValue("eMessageList", eMessageList);
		setUsrSessionDataValue("menu2nd", txnType);
		
	}
	
	public void clear() throws NTBException{
		
		String txnType = this.getParameter("menu2nd");
		Map resultData = this.getResultData();
		TxnPrompt txnPrompt = new TxnPrompt();
		txnPrompt.clearDescription();
		txnPrompt.setTxnType(txnType);
		resultData.put("descriptionE", txnPrompt.getDescription("E"));
		resultData.put("descriptionC", txnPrompt.getDescription("C"));
		resultData.put("menu2nd", txnPrompt.getTxnType());
		setResultData(resultData);
		setUsrSessionDataValue("txnPrompt", txnPrompt);
		/*String lang = Utils.null2EmptyWithTrim(this.getParameter("lang"));
		String txnType = Utils.null2EmptyWithTrim(this.getParameter("txnType"));
		Map resultData = this.getResultData();
		TxnPrompt txnprompt = new TxnPrompt();
		//out side 4000 put to content2 3 4
		//int contentSize = content.length();
		txnprompt.setMessageContent1(content);
		txnprompt.setTxnType(txnType);
		txnprompt.setLang(lang);
		setUsrSessionDataValue("txnprompt", txnprompt);
		setResultData(getParameters());*/
	}
	public void clearLoginMessage() throws NTBException{
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		String txnType = this.getParameter("menu2nd");
		Map resultData = this.getResultData();
		setResultData(resultData);
	}
	
	public void editConfirm() throws NTBException{
		String txnType = this.getParameter("menu2nd");
		TxnPrompt txnPrompt = (TxnPrompt) getUsrSessionDataValue("txnPrompt");
		
		Map resultData = new HashMap();
		resultData.put("menu2nd", txnType);
		resultData.put("descriptionE", txnPrompt.getDescription("E"));
		resultData.put("descriptionC", txnPrompt.getDescription("C"));
		this.setResultData(resultData);
		FlowEngineService flowEngineService = (FlowEngineService) Config.getAppContext().getBean("FlowEngineService");
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		String messageId = CibIdGenerator.getIdForOperation("TxnPrompt");
		String procId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_MESSAGE_UPDATE/*need modify*/,
				FlowEngineService.TXN_CATEGORY_NONFINANCE,
				TransferPromptAction.class, null, 0, null, 0, 0, messageId,
				null, getUser(), null, null, null);
		try {
			BankUser bankUser = (BankUser) this.getUser();
			//set message id
			txnPrompt.setMessageId(messageId);
			//setstatus
			txnPrompt.setStatus(TransferPromptDao.STATUS_PEDNING_APPAOVEL);
			//set operator
			txnPrompt.setOperator(bankUser.getUserName());
			
			transferPromptService.add(txnPrompt);
		} catch (Exception e) {
			flowEngineService.cancelProcess(procId, getUser());
			Log.debug("Process error at MessageManageAction.addConfirm(): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {

			}
		}
		//getResultData().put("messageId", messageId);
	}
	public void editLoginMessageConfirm() throws NTBException{
		
		String txnType =(String) this.getUsrSessionDataValue("menu2nd");
		TxnPrompt txnPrompt = new TxnPrompt();
		txnPrompt.setTxnType(txnType);
		List cMessageList = (ArrayList)this.getUsrSessionDataValue("cMessageList");
		List eMessageList = (ArrayList)this.getUsrSessionDataValue("eMessageList");
		
		Map resultData = new HashMap();
		resultData.put("cMessageCount", this.getUsrSessionDataValue("cMessageCount"));
		resultData.put("eMessageCount", this.getUsrSessionDataValue("eMessageCount"));
		resultData.put("cMessageList",cMessageList);
		resultData.put("eMessageList", eMessageList);
		resultData.put("menu2nd",txnType);
		this.setResultData(resultData);
		FlowEngineService flowEngineService = (FlowEngineService) Config.getAppContext().getBean("FlowEngineService");
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		String messageId = CibIdGenerator.getIdForOperation("TxnPrompt");
		String procId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_MESSAGE_UPDATE/*need modify*/,
				FlowEngineService.TXN_CATEGORY_NONFINANCE,
				TransferPromptAction.class, null, 0, null, 0, 0, messageId,
				null, getUser(), null, null, null);
		try {
			BankUser bankUser = (BankUser) this.getUser();
			//set message id
			txnPrompt.setMessageId(messageId);
			//setstatus
			txnPrompt.setStatus(TransferPromptDao.STATUS_PEDNING_APPAOVEL);
			//set operator
			txnPrompt.setOperator(bankUser.getUserName());
			
			//add content
			transferPromptService.addLoginMessageShow(cMessageList,messageId,"C");
			transferPromptService.addLoginMessageShow(eMessageList,messageId,"E");
			
			transferPromptService.add(txnPrompt);
		} catch (Exception e) {
			flowEngineService.cancelProcess(procId, getUser());
			Log.debug("Process error at MessageManageAction.addConfirm(): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {
				
			}
		}
		//getResultData().put("messageId", messageId);
	}
	
	public void clearConfirm() throws NTBException{
		String txnType = this.getParameter("menu2nd");
		TxnPrompt txnPrompt = (TxnPrompt) getUsrSessionDataValue("txnPrompt");
		
		Map resultData = new HashMap();
		resultData.put("menu2nd", txnType);
		resultData.put("descriptionE", txnPrompt.getDescription("E"));
		resultData.put("descriptionC", txnPrompt.getDescription("C"));
		this.setResultData(resultData);
		FlowEngineService flowEngineService = (FlowEngineService) Config.getAppContext().getBean("FlowEngineService");
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		String messageId = CibIdGenerator.getIdForOperation("TxnPrompt");
		String procId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_MESSAGE_UPDATE/*need modify*/,
				FlowEngineService.TXN_CATEGORY_NONFINANCE,
				TransferPromptAction.class, null, 0, null, 0, 0, messageId,
				null, getUser(), null, null, null);
		try {
			BankUser bankUser = (BankUser) this.getUser();
			//set message id
			txnPrompt.setMessageId(messageId);
			//setstatus
			txnPrompt.setStatus(TransferPromptDao.STATUS_PEDNING_APPAOVEL);
			//set operator
			txnPrompt.setOperator(bankUser.getUserName());
			
			transferPromptService.add(txnPrompt);
		} catch (Exception e) {
			flowEngineService.cancelProcess(procId, getUser());
			Log.debug("Process error at MessageManageAction.addConfirm(): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {
				
			}
		}
		//getResultData().put("messageId", messageId);
	}
	public void clearLoginMessageConfirm() throws NTBException{
		String txnType = this.getParameter("menu2nd");
		TxnPrompt txnPrompt = new TxnPrompt();
		txnPrompt.setTxnType(txnType);
		
		Map resultData = new HashMap();
		resultData.put("menu2nd",txnType);
		this.setResultData(resultData);
		FlowEngineService flowEngineService = (FlowEngineService) Config.getAppContext().getBean("FlowEngineService");
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		String messageId = CibIdGenerator.getIdForOperation("TxnPrompt");
		String procId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_MESSAGE_UPDATE/*need modify*/,
				FlowEngineService.TXN_CATEGORY_NONFINANCE,
				TransferPromptAction.class, null, 0, null, 0, 0, messageId,
				null, getUser(), null, null, null);
		try {
			BankUser bankUser = (BankUser) this.getUser();
			//set message id
			txnPrompt.setMessageId(messageId);
			//setstatus
			txnPrompt.setStatus(TransferPromptDao.STATUS_PEDNING_APPAOVEL);
			//set operator
			txnPrompt.setOperator(bankUser.getUserName());
			
			transferPromptService.add(txnPrompt);
		} catch (Exception e) {
			flowEngineService.cancelProcess(procId, getUser());
			Log.debug("Process error at MessageManageAction.addConfirm(): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {
				
			}
		}
		//getResultData().put("messageId", messageId);
	}
	
	@Override
	public String viewDetail(String txnType, String id, CibAction bean) throws NTBException {
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		if(txnType.equals(Constants.TXN_SUBTYPE_MESSAGE_UPDATE)){
			TxnPrompt txnPrompt  =  transferPromptService.load(id);
			Map resultData = bean.getResultData();
			if(!"8".equals(txnPrompt.getTxnType())){
				resultData.put("descriptionE", txnPrompt.getDescription("E"));
				resultData.put("descriptionC", txnPrompt.getDescription("C"));
				resultData.put("menu2nd", txnPrompt.getTxnType());
				this.setResultData(resultData);
				return "/WEB-INF/pages/bank/txn_pro/editApproveView.jsp";
			}else{
				List cMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"C");
				List eMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"E");
				resultData.put("menu2nd", txnPrompt.getTxnType());
				String clearflag = "Y";
				if(cMessageList !=null){
					resultData.put("cMessageList", cMessageList);
					resultData.put("cMessageCount", cMessageList.size());
					clearflag = "N";
				}
				if(eMessageList != null){
					resultData.put("eMessageList", eMessageList);	
					resultData.put("eMessageCount", eMessageList.size());	
					clearflag = "N";
				}
				resultData.put("loadFlag", "editLoginMessage");
				resultData.put("clearflag", clearflag);
				this.setResultData(resultData);
				return "/WEB-INF/pages/bank/txn_pro/editLoginMessageApproveView.jsp";
			}
		}
		return null;
	}

	@Override
	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		if(txnType.equals(Constants.TXN_SUBTYPE_MESSAGE_UPDATE)){
			TxnPrompt txnPrompt = transferPromptService.load(id);
			
			TxnPrompt txnPromptHis = transferPromptService.loadByTxnType(txnPrompt.getTxnType(), TransferPromptDao.STATUS_NORMAL);
			if(txnPromptHis == null)
				throw new NTBException("DBError");
			txnPromptHis.setStatus(TransferPromptDao.STATUS_DELETE);
			BankUser bankUser = (BankUser) bean.getUser();
			txnPromptHis.setApprover(bankUser.getUserName());
			transferPromptService.update(txnPromptHis);
			
			txnPrompt.setStatus(TransferPromptDao.STATUS_NORMAL);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(new Date());
			txnPrompt.setModifiedTime(date);
			txnPrompt.setApprover(bankUser.getUserName());
			transferPromptService.update(txnPrompt);
			
			if("8".equals(txnPrompt.getTxnType())){
				List cMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"C");
				List eMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"E");
				Map resultData = new HashMap();
				resultData.put("menu2nd", txnPrompt.getTxnType());
				String clearflag = "Y";
				if(cMessageList !=null){
					resultData.put("cMessageList", cMessageList);
					resultData.put("cMessageCount", cMessageList.size());
					clearflag = "N";
				}
				if(eMessageList != null){
					resultData.put("eMessageList", eMessageList);	
					resultData.put("eMessageCount", eMessageList.size());	
					clearflag = "N";
				}
				resultData.put("loadFlag", "editLoginMessage");
				resultData.put("clearflag", clearflag);
				this.setResultData(resultData);
			}
			
		}
		return true;
	}

	@Override
	public boolean reject(String txnType, String id, CibAction bean) throws NTBException {
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		if(txnType.equals(Constants.TXN_SUBTYPE_MESSAGE_UPDATE)){
			TxnPrompt txnPrompt = transferPromptService.load(id);
			txnPrompt.setStatus(TransferPromptDao.STATUS_DELETE);
			BankUser bankUser = (BankUser) bean.getUser();
			transferPromptService.update(txnPrompt);
			if("8".equals(txnPrompt.getTxnType())){
				List cMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"C");
				List eMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"E");
				Map resultData = new HashMap();
				resultData.put("menu2nd", txnPrompt.getTxnType());
				if(cMessageList !=null){
					resultData.put("cMessageList", cMessageList);
					resultData.put("cMessageCount", cMessageList.size());
				}
				if(eMessageList != null){
					resultData.put("eMessageList", eMessageList);	
					resultData.put("eMessageCount", eMessageList.size());	
				}
				resultData.put("loadFlag", "editLoginMessage");
				this.setResultData(resultData);
			}
		}
		return true;
	}

	@Override
	public boolean cancel(String txnType, String id, CibAction bean) throws NTBException {
		// TODO Auto-generated method stub
		return false;
	}
	
}
