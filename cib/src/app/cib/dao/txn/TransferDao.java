package app.cib.dao.txn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.core.Config;

import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;
import app.cib.util.Constants;
import app.cib.util.TransferConstants;


public class TransferDao extends GenericHibernateDao {
    String tablename = null;
   
    // add by mxl 1031
    public List listFileRequestMtS( String batchType, Date dateFrom,
            Date dateTo, String corpID, String userID,
            String toAccount){
    	String tableName = "FileRequest";
    	List valueList = new ArrayList();
    	String hql = "from " + tableName + " as tb where tb.batchType= ? ";
    	valueList.add(batchType);
    	
    	if ((dateFrom != null) && (!dateFrom.equals(""))) {
            hql = hql + "and tb.requestTime >= ? ";
            valueList.add(dateFrom);
        }
    	
        if ((dateTo != null) && (!dateTo.equals(""))) {
             hql = hql + "and tb.requestTime <= ? ";
             valueList.add(dateTo);
         }
    	
        if ((corpID != null) && (!corpID.equals(""))) {
            hql = hql + "and  tb.corpId = ? ";
            valueList.add(corpID);
        }
        if ((userID != null) && (!userID.equals(""))) {
            hql = hql + "and  tb.userId = ? ";
            valueList.add(userID);
        }
        if ((toAccount != null) && (!toAccount.equals("")) && (!toAccount.equals("0"))) {
        	hql = hql + "and  tb.toAccount = ? ";
            valueList.add(toAccount);
        }
        //by wen 20110127
        hql += " and tb.status is not null ";
        
        hql += "order by tb.requestTime DESC";
        return this.list(hql, valueList.toArray());
    	
    }
   
    // add by mxl 1031
    public List listFileRequest( String batchType, Date dateFrom,
            Date dateTo, String corpID, String userID,
            String fromAccount){
    	String tableName = "FileRequest";
    	List valueList = new ArrayList();
    	String hql = "from " + tableName + " as tb where tb.batchType= ? ";
    	valueList.add(batchType);
    	
    	if ((dateFrom != null) && (!dateFrom.equals(""))) {
            hql = hql + "and tb.requestTime >= ? ";
            valueList.add(dateFrom);
        }
    	
        if ((dateTo != null) && (!dateTo.equals(""))) {
             hql = hql + "and tb.requestTime <= ? ";
             valueList.add(dateTo);
         }
    	
        if ((corpID != null) && (!corpID.equals(""))) {
            hql = hql + "and  tb.corpId = ? ";
            valueList.add(corpID);
        }
        if ((userID != null) && (!userID.equals(""))) {
            hql = hql + "and  tb.userId = ? ";
            valueList.add(userID);
        }
        if ((fromAccount != null) && (!fromAccount.equals("")) && (!fromAccount.equals("0"))) {
        	hql = hql + "and  tb.fromAccount = ? ";
            valueList.add(fromAccount);
        }
        //by wen 20110127
        hql += " and tb.status is not null ";
        
        hql += "order by tb.requestTime DESC";
        return this.list(hql, valueList.toArray());
    	
    }
    
    public List listHistory(String business_type, Date dateFrom,
                            Date dateTo, String corpID, String userID,
                            String fromAccount){
        String tableName = null;
        if (TransferConstants.TRANSFER_BANK_1TO1.equals(business_type) || TransferConstants.TRANSFER_BANK_3RD_1TO1.equals(business_type)) {
            tableName = "TransferBank";
        } else if (TransferConstants.TRANSFER_MACAU_1TO1.equals(business_type)) {
            tableName = "TransferMacau";
        } else if (TransferConstants.TRANSFER_OVERSEA_1TO1.equals(business_type)) {
            tableName = "TransferOversea";
        }

        List valueList = new ArrayList();
        String hql = "from " + tableName + " as tb where tb.recordType= ? ";
        valueList.add(TransferBank.TRANSFER_TYPE_GENERAL);

        if ((dateFrom != null) && (!dateFrom.equals(""))) {
            hql = hql + "and tb.requestTime >= ? ";
            valueList.add(dateFrom);
        }
        if ((dateTo != null) && (!dateTo.equals(""))) {
            hql = hql + "and tb.requestTime <= ? ";
            valueList.add(dateTo);
        }
        if ((corpID != null) && (!corpID.equals(""))) {
            hql = hql + "and  tb.corpId = ? ";
            valueList.add(corpID);
        }

        if ((userID != null) && (!userID.equals(""))) {
            hql = hql + "and  tb.userId = ? ";
            valueList.add(userID);
        }
        if ((fromAccount != null) && (!fromAccount.equals("")) && (!fromAccount.equals("0"))) {
            hql = hql + "and  tb.fromAccount = ? ";
            valueList.add(fromAccount);
        }
        
        if (TransferConstants.TRANSFER_BANK_1TO1.equals(business_type)){
        	hql = hql + " and tb.ownerAccFlag = ? " ;
        	valueList.add(Constants.YES);
        }
        
        if (TransferConstants.TRANSFER_BANK_3RD_1TO1.equals(business_type)){
        	hql = hql + " and tb.ownerAccFlag = ? " ;
        	valueList.add(Constants.NO);
        }
        
        hql += "order by tb.requestTime DESC";
        return this.list(hql, valueList.toArray());

    }
  
    public List listSchHistory(String beneficiaryType, Date dateFrom,
            Date dateTo, String corpID, String userID
            ){
    	 String tableName = "ScheduleTransferBatch";
    	 List valueList = new ArrayList();
         String hql = "from " + tableName + " as tb where tb.beneficiaryType= ? ";
         valueList.add(beneficiaryType);
    	
         if ((dateFrom != null) && (!dateFrom.equals(""))) {
             hql = hql + "and tb.scheduleDate >= ? ";
             valueList.add(dateFrom);
         }
         if ((dateTo != null) && (!dateTo.equals(""))) {
             hql = hql + "and tb.scheduleDate <= ? ";
             valueList.add(dateTo);
         }
         if ((corpID != null) && (!corpID.equals(""))) {
             hql = hql + "and  tb.corporaitonId = ? ";
             valueList.add(corpID);
         }

         if ((userID != null) && (!userID.equals(""))) {
             hql = hql + "and  tb.userId = ? ";
             valueList.add(userID);
         }
         hql += "order by tb.scheduleDate DESC";
         return this.list(hql, valueList.toArray());
    }

    public List listHistoryBank(String dateFrom, String dateTo, String corpID,
                               String userID, String fromAccount)  {

        String hql = "from TransferBank as tb where tb.recordType= ? ";

        List valueList = new ArrayList();
        valueList.add(TransferBank.TRANSFER_TYPE_GENERAL);

        if (dateFrom != null) {
            hql = hql + "and tb.requestTime >= ? ";
            valueList.add(dateFrom);
        }
        if (dateTo != null) {
            hql = hql + "and tb.requestTime <= ? ";
            valueList.add(dateTo);
        }
        if (corpID != null) {
            hql = hql + "and  tb.corpId = ? ";
            valueList.add(corpID);
        }

        if (userID != null) {
            hql = hql + "and  tb.userId = ? ";
            valueList.add(userID);
        }
        if (fromAccount != null) {
            hql = hql + "and  tb.fromAccount = ? ";
            valueList.add(fromAccount);
        }
        return this.list(hql, valueList.toArray());

    }


    public List listHistoryMacau(String dateFrom, String dateTo, String corpID,
                                 String userID, String fromAccount) {

        String hql = "from TransferMacau as tb where tb.recordType= ? ";
        List valueList = new ArrayList();
        valueList.add(TransferBank.TRANSFER_TYPE_GENERAL);

        if (dateFrom != null) {
            hql = hql + "and tb.requestTime >= ? ";
            valueList.add(dateFrom);
        }
        if (dateTo != null) {
            hql = hql + "and tb.requestTime <= ? ";
            valueList.add(dateTo);

        }
        if (corpID != null) {
            hql = hql + "and  tb.corpId = ? ";
            valueList.add(corpID);
        }

        if (userID != null) {
            hql = hql + "and  tb.userId = ? ";
            valueList.add(userID);
        }
        if (fromAccount != null) {
            hql = hql + "and  tb.fromAccount = ? ";
            valueList.add(fromAccount);
        }
        return this.list(hql, valueList.toArray());

    }


    public List listHistoryOversea(String dateFrom, String dateTo,
                                   String corpID, String userID,
                                   String fromAccount){

        String hql = "from TransferOversea as tb where tb.recordType= ? ";
        List valueList = new ArrayList();
        valueList.add(TransferOversea.TRANSFER_TYPE_GENERAL);

        if (dateFrom != null) {
            hql = hql + "and tb.requestTime >= ? ";
            valueList.add(dateFrom);
        }
        if (dateTo != null) {
            hql = hql + "and tb.requestTime <= ? ";
            valueList.add(dateTo);
        }
        if (corpID != null) {
            hql = hql + "and  tb.corpId = ? ";
            valueList.add(corpID);
        }

        if (userID != null) {
            hql = hql + "and  tb.userId = ? ";
            valueList.add(userID);
        }
        if (fromAccount != null) {
            hql = hql + "and  tb.fromAccount = ? ";
            valueList.add(fromAccount);
        }
        return this.list(hql, valueList.toArray());

    }
    public List listScheduleBANK(String corpID, String userID) {

        String hql = "from TransferBank as tb where tb.corpId = ? and (tb.operation = ? or tb.operation = ?) and tb.userId = ? and tb.recordType = ?";
        List valueList = new ArrayList();
        valueList.add(corpID);
        valueList.add(Constants.OPERATION_NEW);
        valueList.add(Constants.OPERATION_UPDATE);
        valueList.add(userID);
        valueList.add(TransferBank.TRANSFER_TYPE_SCHEDULED);
        return this.list(hql, valueList.toArray());

    }
    public List listScheduleOversea(String corpID, String userID)  {

        String hql = "from TransferOversea as tb where tb.corpId = ? and (tb.operation = ? or tb.operation = ?) and tb.userId = ? and tb.recordType = ?";
        List valueList = new ArrayList();

        valueList.add(corpID);
        valueList.add(Constants.OPERATION_NEW);
        valueList.add(Constants.OPERATION_UPDATE);
        valueList.add(userID);
        valueList.add(TransferOversea.TRANSFER_TYPE_SCHEDULED);
        return this.list(hql, valueList.toArray());

    }

    public List listScheduleMacau(String corpID, String userID){

        String hql = "from TransferMacau as tb where tb.corpId = ? and (tb.operation = ? or tb.operation = ?) and tb.userId = ? and tb.recordType = ?";
        List valueList = new ArrayList();
        valueList.add(corpID);
        valueList.add(Constants.OPERATION_NEW);
        valueList.add(Constants.OPERATION_UPDATE);
        valueList.add(userID);
        valueList.add(TransferMacau.TRANSFER_TYPE_SCHEDULED);
        return this.list(hql, valueList.toArray());

    }
    //add by mxl 1102 
	public List listTransferBankStm( String batchId){
		String tableName = "TransferBankStm";
    	List valueList = new ArrayList();
    	String hql = "from " + tableName + " as tb where tb.batchId= ? order by tb.lineNo";
    	valueList.add(batchId);
    	return this.list(hql, valueList.toArray());
	
	}
     //add by mxl 1102 
	public List listTransferMacauStm( String batchId){
		String tableName = "TransferMacauStm";
    	List valueList = new ArrayList();
    	String hql = "from " + tableName + " as tb where tb.batchId= ? order by tb.lineNo";
    	valueList.add(batchId);
    	return this.list(hql, valueList.toArray());
	
	}
     //add by mxl 1102 
	public List listTransferOverseaStm( String batchId){
		String tableName = "TransferOverseaStm";
    	List valueList = new ArrayList();
    	String hql = "from " + tableName + " as tb where tb.batchId= ? order by tb.lineNo";
    	valueList.add(batchId);
    	return this.list(hql, valueList.toArray());
	
	}
}
