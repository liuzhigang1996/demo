package app.cib.service.bat;

import java.util.*;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bat.ScheduleTransferBatch;
import app.cib.bo.rpt.ApproverSuspendReport;
import app.cib.bo.txn.*;
import app.cib.core.CibIdGenerator;
import app.cib.dao.bat.SchTxnBatchDao;
import app.cib.service.txn.TransferService;
import app.cib.util.Constants;
import app.cib.util.TransferConstants;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-10-30
 */
public class SchTxnBatchServiceImpl implements SchTxnBatchService {
	
	private SchTxnBatchDao schBatchDao;
	private GenericJdbcDao jdao;

	public SchTxnBatchDao getSchBatchDao() {
		return schBatchDao;
	}

	public void setSchBatchDao(SchTxnBatchDao schBatchDao) {
		this.schBatchDao = schBatchDao;
	}

	public GenericJdbcDao getJdao() {
		return jdao;
	}

	public void setJdao(GenericJdbcDao jdao) {
		this.jdao = jdao;
	}

	public List listNextBatchDayReport(String corpId, String nextBatchDay) throws NTBException {
		
		Map conditionMap = new HashMap();
		conditionMap.put("corporaitonId", corpId);
		conditionMap.put("scheduleDate", nextBatchDay);
		
		List reportList = schBatchDao.list(ScheduleTransferBatch.class, conditionMap);
		
		return reportList;
	}

	public void cancel(String batchId) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		
		//update schedule transfer batch
		ScheduleTransferBatch schTransBatch = 
			(ScheduleTransferBatch) schBatchDao.load(ScheduleTransferBatch.class, batchId);
		schTransBatch.setStatus(ScheduleTransferBatch.STATUS_CANCELED);
		
		//update transfer bank/macau/oversea
		Object transfer = null;
		if (schTransBatch.getBeneficiaryType().equals(TransferConstants.TRANSFER_TYPE_BANK)) {
			transfer = transferService.viewInBANK(schTransBatch.getTransId());
			((TransferBank)transfer).setStatus(Constants.STATUS_CANCELED);
		} else if (schTransBatch.getBeneficiaryType().equals(TransferConstants.TRANSFER_TYPE_MACAU)) {
			transfer = transferService.viewInMacau(schTransBatch.getTransId());
			((TransferMacau)transfer).setStatus(Constants.STATUS_CANCELED);
		} else if (schTransBatch.getBeneficiaryType().equals(TransferConstants.TRANSFER_TYPE_OVERSEA)) {
			transfer = transferService.viewInOversea(schTransBatch.getTransId());
			((TransferOversea)transfer).setStatus(Constants.STATUS_CANCELED);
		} else {
			throw new NTBException();
		}
		
		schBatchDao.update(schTransBatch);
		schBatchDao.update(transfer);
	}
	
	public void cancelByCorpId(String corpId) throws NTBException {
		String hql = "from  ScheduleTransferBatch as s where s.corporaitonId = ? and s.status = ?";
		
		List list = this.schBatchDao.list(hql, new Object[]{corpId, ScheduleTransferBatch.STATUS_PENDING});
		for(int i=0; i<list.size(); i++) {
			ScheduleTransferBatch schTransBatch = (ScheduleTransferBatch) list.get(i);
			schTransBatch.setStatus(ScheduleTransferBatch.STATUS_CANCELED);
			schBatchDao.update(schTransBatch);
		}
	}
	
	public void cancelByUserId(String userId) throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("userId", userId);
		conditionMap.put("status", ScheduleTransferBatch.STATUS_PENDING);
		
		List list = schBatchDao.list(ScheduleTransferBatch.class, conditionMap);
		for (int i=0; i<list.size(); i++) {
			ScheduleTransferBatch schTransBatch = (ScheduleTransferBatch) list.get(i);
			
			ApproverSuspendReport report = new ApproverSuspendReport();
			report.setReportId(CibIdGenerator.getIdForOperation("APPROVER_SUSPEND_REPORT"));
			report.setTransId(schTransBatch.getBatchId());
			report.setCorpId(schTransBatch.getCorporaitonId());
			report.setUserId(schTransBatch.getUserId());
			report.setTransType(ApproverSuspendReport.TRANS_TYPE_SCHEDULE_JOB);
			report.setSubType(schTransBatch.getBeneficiaryType());
			report.setOperationType("");
			report.setOriginalStatus(schTransBatch.getStatus());
			schBatchDao.getHibernateTemplate().save(report);

			schTransBatch.setStatus(ScheduleTransferBatch.STATUS_CANCELED);
			schBatchDao.update(schTransBatch);
		}
		
	}

	public List listSchBatchHistory(String beneficiaryType, String corpId, String userId, 
			String dateFrom, String dateTo) throws NTBException {
		List recList = schBatchDao.listRecords(beneficiaryType, corpId, userId, dateFrom, dateTo);
        return recList;
	}

}
