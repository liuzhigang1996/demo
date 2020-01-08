package app.cib.service.bat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import app.cib.bo.bat.ScheduleTransfer;
import app.cib.bo.bat.ScheduleTransferHis;
import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;
import app.cib.core.CibTransClient;
import app.cib.dao.bat.SchTransferDao;
import app.cib.dao.bat.SchTxnHisDao;
import app.cib.util.Constants;
import app.cib.util.UploadReporter;

import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;

public class SchTransferServiceImpl implements SchTransferService{
	CibTransClient cibTransClient;
	private static String SELECT_SCHEDULE_ID = "Select SCHEDULE_ID from SCHEDULE_TRANSFER where TRANS_ID=?";
	private static String SELECT_TRANS_ID = "Select TRANS_ID from SCHEDULE_TRANSFER where SCHEDULE_ID=?";
	 
	private GenericJdbcDao genericJdbcDao;
	private SchTransferDao schTransferDao;
	private SchTxnHisDao schTxnHisDao;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public SchTransferDao getSchTransferDao() {
		return schTransferDao;
	}

	public void setSchTransferDao(SchTransferDao schTransferDao) {
		this.schTransferDao = schTransferDao;
	}

	public SchTxnHisDao getSchTxnHisDao() {
		return schTxnHisDao;
	}

	public void setSchTxnHisDao(SchTxnHisDao schTxnHisDao) {
		this.schTxnHisDao = schTxnHisDao;
	}

	public List listSchTransfer(String beneficiaryType, String corpId) throws NTBException {
        if ((corpId != null) && (!corpId.equals(""))) {
        	List schList =  schTransferDao.listSchTransfer(beneficiaryType, corpId);
        	ScheduleTransfer schTransfer = null;
        	ScheduleTransferHis schTransferHis = null;
        	for(int i=0; i<schList.size(); i++) {
        		schTransfer = (ScheduleTransfer) schList.get(i);
        		schTransferHis = schTxnHisDao.getHisByScheduleId(schTransfer.getScheduleId());
        		if(!schTransferHis.getOperation().equals(Constants.OPERATION_NEW) 
        				&& schTransferHis.getAuthStatus().equals(Constants.AUTH_STATUS_REJECTED)) {
        		} else {
            		schTransfer.setStatus(schTransferHis.getStatus());
        		}
        	}
        	return schList;
        } else {
            throw new NTBException("err.txn.CorpIDIsNullOrEmpty");
        }
	}

	public void addTransferBank(TransferBank transferBank) throws NTBException {
		if (transferBank != null) {
			transferBank.setRecordType(TransferBank.TRANSFER_TYPE_SCHEDULED);
            schTransferDao.add(transferBank);
        } else {
            throw new NTBException("err.txn.pojoTransferIsNull");
        }
	}
	
	public void addTransferMacau(TransferMacau transferMacau) throws NTBException {
		if (transferMacau != null) {
			transferMacau.setRecordType(TransferMacau.TRANSFER_TYPE_SCHEDULED);
            schTransferDao.add(transferMacau);
        } else {
            throw new NTBException("err.txn.pojoTransferIsNull");
        }
	}

	public void addTransferOverseas(TransferOversea transferOverseas) throws NTBException {
		if (transferOverseas != null) {
			transferOverseas.setRecordType(TransferOversea.TRANSFER_TYPE_SCHEDULED);
            schTransferDao.add(transferOverseas);
        } else {
            throw new NTBException("err.txn.pojoTransferIsNull");
        }
	}

	public String getTransId(String scheduleID) throws NTBException {
		Map typeMap = new HashMap();
        String transId = null;
        try {
            List idList = null;
            idList = genericJdbcDao.query(SELECT_TRANS_ID, new Object[] {scheduleID});
            typeMap = (Map) idList.get(0);
            transId = (String) typeMap.get("TRANS_ID");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transId;
	}
	
	public String getScheduleId(String transID) throws NTBException {
		Map typeMap = new HashMap();
        String scheduleId = null;
        try {
            List idList = null;
            idList = genericJdbcDao.query(SELECT_SCHEDULE_ID, new Object[] {transID});
            typeMap = (Map) idList.get(0);
            scheduleId = (String) typeMap.get("SCHEDULE_ID");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleId;
	}
	
	
	
	
	/**
	 * add by hjs 2006-11-28
	 * 
	 * for approver related transation suspend report
	 * @param userId
	 * @throws NTBException
	 */
	public void deleteByUserId(String userId) throws NTBException {
		String hql = "from ScheduleTransfer as st where st.userId = ? and (st.status = ? or st.status = ?)";
		
		List list = schTransferDao.list(hql, new Object[]{userId, Constants.STATUS_NORMAL, Constants.STATUS_PENDING_APPROVAL});
		for (int i=0; i<list.size(); i++) {
			ScheduleTransfer schTransfer = (ScheduleTransfer) list.get(i);
			/*
			ApproverSuspendReport report = new ApproverSuspendReport();
			report.setReportId(CibIdGenerator.getIdForOperation("APPROVER_SUSPEND_REPORT"));
			report.setTransId(schTransfer.getScheduleId());
			report.setCorpId(schTransfer.getCorporaitonId());
			report.setUserId(schTransfer.getUserId());
			report.setTransType(ApproverSuspendReport.TRANS_TYPE_SCHEDULE);
			report.setSubType(schTransfer.getBeneficiaryType());
			report.setOperationType(schTransfer.getAuthStatus());
			report.setOriginalStatus(schTransfer.getStatus());
			schTransferDao.getHibernateTemplate().save(report);
			*/
			schTransfer.setStatus(Constants.STATUS_REMOVED);
			schTransferDao.update(schTransfer);
			
		}
	}
	

	/**
	 * add by hjs 2006-12-14
	 * @param days
	 * @throws NTBException
	 */
	public void checkSeparator(String days) throws NTBException {
		String[] dayArray = days.split(",");
		int dayPerMonth = 0;
		for (int i=0; i<dayArray.length; i++) {
			try {
				dayPerMonth = Integer.parseInt(dayArray[i]);
			} catch (Exception e) {
				throw new NTBException("err.bat.DaysPerMonthFieldError");
			}
			if (dayPerMonth<1 || dayPerMonth>31) {
				throw new NTBException("err.bat.DaysPerMonthFieldError");
			}
		}
	}

	public void addSchTransfer(ScheduleTransfer scheduleTransfer) throws NTBException {
		 if (scheduleTransfer != null) {
			 schTransferDao.add(scheduleTransfer);
	     } else {
	    	 throw new NTBException("err.txn.pojoTransferIsNull");
	     }
	}

	public void updateSchTransfer(ScheduleTransfer scheduleTransfer) throws NTBException {
		 if (scheduleTransfer != null) {
			 schTransferDao.update(scheduleTransfer);
	     } else {
	    	 throw new NTBException("err.txn.pojoTransferIsNull");
	     }
	}

	public ScheduleTransfer loadSchTransfer(String scheduleId) throws NTBException {
        if ((scheduleId != null) && (!scheduleId.equals(""))) {
            return (ScheduleTransfer) schTransferDao.load(ScheduleTransfer.class, scheduleId);
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }
	}

	public void addSchTransferHis(ScheduleTransferHis schTransferHis) throws NTBException {
		schTxnHisDao.add(schTransferHis);
		
	}

	public void updateSchTransferHis(ScheduleTransferHis schTransferHis) throws NTBException {
		schTxnHisDao.update(schTransferHis);
		
	}

	public ScheduleTransferHis loadSchTransferHis(String seqNo) throws NTBException {
		return (ScheduleTransferHis) schTxnHisDao.load(ScheduleTransferHis.class, seqNo);
	}
	
	/*
	public List listSchTransferHis(String beneficiaryType, String corpId, String userId) throws NTBException {
		return this.schTxnHisDao.listRecords(beneficiaryType, corpId, userId);
	}
	*/
	
	public boolean isPending(String scheduleId) throws NTBException {
        Map conditionMap = new HashMap();
        conditionMap.put("scheduleId", scheduleId);
        conditionMap.put("status", Constants.STATUS_PENDING_APPROVAL);
        conditionMap.put("authStatus", Constants.AUTH_STATUS_SUBMITED);
        List hisList = schTxnHisDao.list(ScheduleTransferHis.class, conditionMap);
        return hisList.size()>0;
	}

	public List listOperationHistory(String beneficiaryType, String corpId, String userId) throws NTBException {
		return this.schTxnHisDao.listRecords(beneficiaryType, corpId, userId);
	}

	public void checkScheduleName(String scheduleName) throws NTBException {
		String[] scheduleNameArray = scheduleName.split("");
		for (int i=0; i<scheduleNameArray.length;i++) {
			System.out.println("i="+scheduleNameArray[i]);
			if (scheduleNameArray[i].equals("#")) {
				throw new NTBException("err.bat.ScheduleNameError");
			}
		}
				
	}

	public void uploadSchTransferInBankReprot(ScheduleTransfer scheduleTransfer, TransferBank transferBank) throws NTBException {
		Map uploadMap = new HashMap();
		uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
        uploadMap.put("TRANS_TIME", CibTransClient.getCurrentTime());
        uploadMap.put("USER_ID", transferBank.getUserId());
        uploadMap.put("BATCH_ID", scheduleTransfer.getScheduleId());
        uploadMap.put("TRANS_ID", scheduleTransfer.getTransId());
        uploadMap.put("CORPORAITON_ID", transferBank.getCorpId());
        uploadMap.put("BENEFICIARY_TYPE", "1");
        uploadMap.put("SCHEDULE_DATE", scheduleTransfer.getFrequnceDays());
        uploadMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
        uploadMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
        uploadMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
        UploadReporter.write("RP_STLOG", uploadMap);
	}

	public void uploadSchTransferInMacauReprot(ScheduleTransfer scheduleTransfer, TransferMacau transferMacau) throws NTBException {
		Map uploadMap = new HashMap();
		uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
        uploadMap.put("TRANS_TIME", CibTransClient.getCurrentTime());
        uploadMap.put("USER_ID", transferMacau.getUserId());
        uploadMap.put("BATCH_ID", scheduleTransfer.getScheduleId());
        uploadMap.put("TRANS_ID", scheduleTransfer.getTransId());
        uploadMap.put("CORPORAITON_ID", transferMacau.getCorpId());
        uploadMap.put("BENEFICIARY_TYPE", "2");
        uploadMap.put("SCHEDULE_DATE", scheduleTransfer.getFrequnceDays());
        uploadMap.put("FROM_ACCOUNT", transferMacau.getFromAccount());
        uploadMap.put("FROM_CURRENCY", transferMacau.getFromCurrency());
        uploadMap.put("FROM_AMOUNT", transferMacau.getDebitAmount());
        UploadReporter.write("RP_STLOG", uploadMap);
		
	}

	public void uploadSchTransferInOverseaReprot(ScheduleTransfer scheduleTransfer, TransferOversea transferOversea) throws NTBException {
		Map uploadMap = new HashMap();
		uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
        uploadMap.put("TRANS_TIME", CibTransClient.getCurrentTime());
        uploadMap.put("USER_ID", transferOversea.getUserId());
        uploadMap.put("BATCH_ID", scheduleTransfer.getScheduleId());
        uploadMap.put("TRANS_ID", scheduleTransfer.getTransId());
        uploadMap.put("CORPORAITON_ID", transferOversea.getCorpId());
        uploadMap.put("BENEFICIARY_TYPE", "3");
        uploadMap.put("SCHEDULE_DATE", scheduleTransfer.getFrequnceDays());
        uploadMap.put("FROM_ACCOUNT", transferOversea.getFromAccount());
        uploadMap.put("FROM_CURRENCY", transferOversea.getFromCurrency());
        uploadMap.put("FROM_AMOUNT", transferOversea.getDebitAmount());
        UploadReporter.write("RP_STLOG", uploadMap);
		
	}
	
	//add bys hjs 20070206
	public void removeByCorpId(String corpId) throws NTBException {
		String hql = "from ScheduleTransfer as sch where sch.corporaitonId = ? and sch.status = ? and sch.authStatus = ?";
		List list = schTransferDao.list(hql, new Object[]{corpId, Constants.STATUS_NORMAL, Constants.AUTH_STATUS_COMPLETED});
		for(int i=0; i<list.size(); i++) {
			ScheduleTransfer schTransfer = (ScheduleTransfer) list.get(i);
			schTransfer.setStatus(Constants.STATUS_REMOVED);
			schTransferDao.update(schTransfer);
		}
		
	}
	
	/*** add by hjs 20070207 ***/
	public String sortFrequenceDays(String frequenceDays) throws NTBException {
		HashSet daySet = new HashSet();
		String[] daysArray = frequenceDays.split(",");
		List tmpList = new ArrayList(Arrays.asList(daysArray));
		Collections.sort(tmpList, new SchTransferServiceImpl().new c());
		String s = "";
		Object day = null;
		for(int i=0; i<tmpList.size(); i++) {
			day = tmpList.get(i);
			if (daySet.contains(day)) {
				continue;
			}
			daySet.add(day);
			s += tmpList.get(i) + ",";
		}
		return s.equals("") ? s : s.substring(0, s.length()-1);
	}
	
	/*** add by hjs 20070207 ***/
	private class c implements Comparator {
		public int compare(Object o1, Object o2) {
			if(Integer.parseInt(o1.toString()) > Integer.parseInt(o2.toString())) {
				return 1;
			}
			if(Integer.parseInt(o1.toString()) < Integer.parseInt(o2.toString())) {
				return -1;
			}
			return 0;
		}
		
	}

}
