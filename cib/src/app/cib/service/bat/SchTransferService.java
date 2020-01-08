package app.cib.service.bat;

import java.util.List;

import app.cib.bo.bat.ScheduleTransfer;
import app.cib.bo.bat.ScheduleTransferHis;
import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;

import com.neturbo.set.exception.NTBException;

public interface SchTransferService {
     // add by mxl
   	public void addTransferBank(TransferBank transferBank) throws NTBException;
	public void addTransferMacau(TransferMacau transferMacau) throws NTBException;
	public void addTransferOverseas(TransferOversea transferOverseas) throws NTBException;
    //public String getScheduleId(String transId) throws NTBException;
	//public String getTransId(String scheduleId) throws NTBException;
	// add by hjs 2006-11-28
	public void deleteByUserId(String userId) throws NTBException;
	public void checkSeparator(String days) throws NTBException;
	// add by mxl 0109
	public void checkScheduleName(String scheduleName) throws NTBException;
    public void addSchTransfer(ScheduleTransfer scheduleTransfer) throws NTBException;
    public void updateSchTransfer(ScheduleTransfer scheduleTransfer) throws NTBException;
   	public ScheduleTransfer loadSchTransfer(String scheduleId) throws NTBException;
	public List listSchTransfer(String beneficiaryType, String corpId) throws NTBException;
    
	public void addSchTransferHis(ScheduleTransferHis schTransferHis) throws NTBException;
	public void updateSchTransferHis(ScheduleTransferHis schTransferHis) throws NTBException;
	public ScheduleTransferHis loadSchTransferHis(String seqNo) throws NTBException;
	//public List listSchTransferHis(String beneficiaryType, String corpId, String userId) throws NTBException;
	
	public boolean isPending(String scheduleId) throws NTBException;
	public List listOperationHistory(String beneficiaryType, String corpId, String userId) throws NTBException;
	// add by mxl 0201 report table
	public void uploadSchTransferInBankReprot(ScheduleTransfer scheduleTransfer, TransferBank transferBank) throws NTBException;
	public void uploadSchTransferInMacauReprot(ScheduleTransfer scheduleTransfer, TransferMacau transferMacau) throws NTBException;
	public void uploadSchTransferInOverseaReprot(ScheduleTransfer scheduleTransfer, TransferOversea transferOversea) throws NTBException;
	
	//add by hjs 20070206
	public void removeByCorpId(String corpId) throws NTBException;
	public String sortFrequenceDays(String frequenceDays) throws NTBException;

}
