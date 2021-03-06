package app.cib.service.bat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.bat.FileRequestFileBean;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibIdGenerator;
import app.cib.dao.srv.RequestDao;
import app.cib.service.srv.RequestService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.txn.TransferService;
import app.cib.service.utl.UtilService;
import app.cib.util.Constants;
import app.cib.util.ErrConstants;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBErrorArray;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.xml.XMLElement;

public class BankDraftBatchServiceImpl implements  BankDraftBatchService{
	private static final String saveFilePath = Config.getProperty("BatchFileUploadDir") + "/";
	private Map fileRequestHeader = new HashMap();
	private String BATCH_ID = "";
	private int LINE_NO = 0;
	private NTBErrorArray errArray = new NTBErrorArray();
	private boolean isFileHeaderOk = false;
	private boolean isBatchHeaderOk = false;
	private Set referenceSet = new HashSet();
	private String fromAccount = null;
	private List normalList = new ArrayList();
	private List errList = new ArrayList();
	
	//Add by heyongjiang 20100830
	private String bankDraftCurrency = null;
		
	private BigDecimal normalTotalAmt =  new BigDecimal("0");
	private BigDecimal errTotalAmt =  new BigDecimal("0");

	private CorpUser corpUser;
	private GenericJdbcDao genericJdbcDao;
	private RequestDao requestDao;
	public RequestDao getTransferDao() {
        return requestDao;
    }

    public void setRequestDao(RequestDao requestDao) {
        this.requestDao = requestDao;
    }
	
	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}


	public FileRequestFileBean parseFile(CorpUser corpUser,  InputStream inStream) throws NTBException {
		FileRequestFileBean fileRequestFileBean = new FileRequestFileBean();
		//File saveFile = new File(saveFilePath + fileName);
		BATCH_ID = CibIdGenerator.getRefNoForTransaction();
		String fileName = this.getFileName();
        File saveFile = new File(saveFilePath + fileName);
        
        
        FileOutputStream outStream = null;
    	InputStream inStreamForParsing = null;
       
		
		this.corpUser = corpUser;
    
        // initial
    	fileRequestHeader = new HashMap();
    	errArray = new NTBErrorArray();
    	LINE_NO = 0;
    	isFileHeaderOk = false;
    	isBatchHeaderOk = false;
    	normalList = new ArrayList();
    	errList = new ArrayList();
    	normalTotalAmt = new BigDecimal("0");
    	errTotalAmt = new BigDecimal("0");
    	referenceSet = new HashSet();
    	try {
    		
            //锟斤拷锟侥诧拷写锟斤拷锟斤拷锟斤拷锟�
			outStream = new FileOutputStream(saveFile);
			byte[] buffer = new byte[4096];
			int readCount = 1;
			while ((readCount = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, readCount);
			}
			outStream.close();
			inStream.close();
           
             // 锟斤拷锟斤拷锟侥硷拷
			inStreamForParsing = new FileInputStream(saveFile);
			
	        XMLElement payrollDoc = BatchXMLFactory.getBatchXML("BANK_DRAFT");
	        String tableHeader = payrollDoc.getAttribute("table_header");
	        String tableDetail = payrollDoc.getAttribute("table_detail");
	        
	        FileParser parser = new FileParser(payrollDoc, inStreamForParsing);
	        parser.setGenericJdbcDao(this.genericJdbcDao);
	        
	        FileRecordProcessor pro = new MyRecordProcessor(tableDetail);
	        parser.parseRecord(pro);
	        
	        int totalAmt = ((MyRecordProcessor) pro).getCount();
	        
            // no header exception 20110623 by wen
	        if(!pro.isBatchHeaderExists()){
	        	errArray.addError("err.bat.NoBatchHeaderLine");
	        }
            //no trailer exception 20110623 by wen
	        if(!pro.isTrailerExists()){
	        	errArray.addError("err.bat.NoTrailerLine");
	        }
	        
	        //operation when batch header exists 20110624 by wen
	        if(pro.isBatchHeaderExists()){
	        	 // check consistency
		        //double totalAmtInFile = Double.parseDouble(fileRequestHeader.get("TO_AMOUNT").toString());
		        BigDecimal totalAmtInFile = (BigDecimal) (fileRequestHeader.get("TO_AMOUNT"));
		        if (totalAmtInFile.compareTo(normalTotalAmt.add(errTotalAmt)) !=0 ) {
	    			cancelUpload(BATCH_ID, outStream, inStreamForParsing, saveFile);
		        }
		        int totalNoInFile = Integer.parseInt(fileRequestHeader.get("TOTAL_NUMBER").toString());
		        if (totalNoInFile !=  normalList.size() + errList.size()) {
	    			cancelUpload(BATCH_ID, outStream, inStreamForParsing, saveFile);
		        }
	        }	       
	
        	if (errArray.size() == 0) {
        		fileRequestHeader.put("BATCH_ID", BATCH_ID);
        		fileRequestHeader.put("FILE_NAME", fileName);
        		fileRequestHeader.put("BATCH_TYPE", FileRequest.BANK_DRAFT_BATCH_TYPE);
        		fileRequestHeader.put("CORP_ID", corpUser.getCorpId());
        		fileRequestHeader.put("USER_ID", corpUser.getUserId());
        		fileRequestHeader.put("REMARK", "");
        		fileRequestHeader.put("REQUEST_TIME", new Date());
        		
        		fileRequestHeader.put("EXECUTE_TIME", null);
        		fileRequestHeader.put("AUTH_STATUS", Constants.STATUS_PENDING_APPROVAL);
        		fileRequestHeader.put("BATCH_RESULT", "P");
        		fileRequestHeader.remove("RECORD_ID");
        		//fileRequestHeader.remove("FH");
        		fileRequestHeader.remove("BH");
        		//addPayrollByJdbc(tableHeader, fileRequestHeader);
                //锟睫达拷锟斤拷锟铰硷拷锟叫碏ILRE_QEQUEST锟斤拷
        		if (errList.size() == 0) {
            		addPayrollByJdbc(tableHeader, fileRequestHeader);
        		} else {
        			//锟斤拷锟斤拷锟斤拷锟剿达拷写锟斤拷锟斤拷锟斤拷
        			cancelUpload(BATCH_ID, outStream, inStreamForParsing, saveFile);
        		}
        	} else  {
        		cancelUpload(BATCH_ID, outStream, inStreamForParsing, saveFile);
        		Log.error("parse head file error");
        		throw new NTBException(errArray);
        	}
	        fileRequestFileBean.setNormalList(normalList);
	        fileRequestFileBean.setErrList(errList);
	        fileRequestFileBean.setNormalTotleAmt(normalTotalAmt.doubleValue());
	        fileRequestFileBean.setErrTotleAmt(errTotalAmt.doubleValue());
	        fileRequestFileBean.setAllCount(totalAmt);
	        fileRequestFileBean.setFileRequestHeader(fileRequestHeader);
	        //fileRequestFileBean.setPayrollHeader(fileRequestHeader);
	        
	        return fileRequestFileBean;
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			cancelUpload(BATCH_ID, outStream, inStreamForParsing, saveFile);
			if (e instanceof NTBException) {
				throw (NTBException)e;
			} else {
				throw new NTBException(ErrConstants.GENERAL_ERROR);
			}
		}
		
	}
	public void cancelUpload(String BATCH_ID, OutputStream outStream,InputStream inStreamForParsing, File saveFile) {
		try {
			if(!BATCH_ID.equals("")) {
				clearUnavailableDataByPaybatchId(BATCH_ID);
			}
		} catch (Exception e) {
			Log.warn("clear unavailable data failed", e);
		}
		try {
			if (outStream != null)
				outStream.close();
			if (inStreamForParsing != null)
				inStreamForParsing.close();
		} catch (IOException e) {
			Log.warn("delete unavailable file failed", e);
		}
		saveFile.delete();
	}
	private class MyRecordProcessor implements FileRecordProcessor {

        String tableName;
        Map fileHeader = null;
        Map batchHeader = null;
        int count = 0;
        
        String fromAcc = "";
        
    	private boolean batchHeaderExists = false; //20110623 by wen
    	private boolean fileHeaderExists = false; //20110623 by wen
    	private boolean trailerExists = false; //20110623 by wen

        MyRecordProcessor(String tableName) {
            this.tableName = tableName;
        }

        public int processRecord(Map recordData) throws Exception {

            String recordId = (String) recordData.get("RECORD_ID");
            if (FileRecordProcessor.RECORD_FILE_HEADER.equals(recordId)) {
            	fileHeader = (Map) recordData.get("FH");
            	validateFileHeader(fileHeader);
                return FileRecordProcessor.FILE_RECORD_NOPROCESS;
                
            } else if (FileRecordProcessor.RECORD_BATCH_HEADER.equals(recordId)) {
            	
            	batchHeaderExists = true; //by wen 20110623
            	
            	fileHeader = (Map) recordData.get("BH");
                // validate batch header
            	validateFileHeader(fileHeader);
                return FileRecordProcessor.FILE_RECORD_NOPROCESS;
                
            } else if (FileRecordProcessor.RECORD_FILE_END2.equals(recordId)) {
               	
               	trailerExists = true; // by wen 20110623
               	
                   return FileRecordProcessor.FILE_RECORD_STOP;
                   
            } 
            
            if(batchHeaderExists){ // by wen 20110624 check batch detail when batch header exists
            	
               if (FileRecordProcessor.RECORD_BATCH_DETAIL.equals(recordId)) {
               	 Map recordDetail = (Map) recordData.get("BD");
                    //validate line
                    validateDetail(recordData, recordDetail);
                    return FileRecordProcessor.FILE_RECORD_BATCH;
                   
               } 
            	
            }
            
            return FileRecordProcessor.FILE_RECORD_NOPROCESS;
            
        }

        private void validateFileHeader(Map recordDetail) throws Exception {
        	ApplicationContext appContext = Config.getAppContext();
        	RequestService requestService = (RequestService) appContext.getBean("RequestService");
        	CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
        	
        	//boolean errFlag = false;
            //锟叫断革拷锟斤拷锟斤拷欠锟斤拷锟斤拷峤�add by mxl 1225
    		if ( requestService.listFileRequestBythreekeys(recordDetail.get("CORP_ID").toString(),recordDetail.get("ORDER_DATE").toString(),recordDetail.get("BATCH_REFERENCE").toString(),FileRequest.BANK_DRAFT_BATCH_TYPE).size()>0) {
    			errArray.addError("err.bat.TheUploadBatchFileHasBeenSubmitted");
    		}
            Set keySet = recordDetail.keySet();
            for (Iterator it = keySet.iterator(); it.hasNext(); ) {
                String keyName = (String) it.next();
                Object keyObj = recordDetail.get(keyName);
                if (keyObj instanceof String) {
                    keyObj = new String(
                    		((String) keyObj).getBytes(),
                    		Config.getProperty("DBCharset")).trim();
                }
                recordDetail.put(keyName, keyObj);
                
                // check validate
           		if (keyName.equals("FROM_ACCOUNT")) {
           			//add by mxl 0129
           			fromAccount = keyObj.toString().trim();
					//modify by hjs 20070813
					boolean flag = corpAccountService.checkAccountByUser(corpUser, fromAccount);
					if (!flag) {
						Log.error("FROM_ACCOUNT is not the privilege");
						errArray.addError("err.bat.FromAccountBatchIsNotPrivilage");
						continue;
					}
        			
        		} else if (keyName.equals("TOTAL_NUMBER")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("TOTAL_NUMBER is empty");
            			errArray.addError("err.bat.TotalNumberIsEmpty");
            			continue;
            		} 
        		} else if (keyName.equals("ORDER_DATE")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("ORDER_DATE is empty");
            			errArray.addError("err.bat.OrderDateIsEmpty");
            			continue;
            		}
            		try {
        				//锟角凤拷锟斤拷锟节革拷式
        	            (new SimpleDateFormat("yyyyMMdd")).parse(keyObj.toString().trim());
        			} catch (Exception e) {
            			Log.error("ORDER_DATE format error");
            			errArray.addError("err.bat.OrderDateFormatError");
            			continue;
        			}
        		} else if (keyName.equals("TO_CURRENCY")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("TO_CURRENCY is empty");
            			errArray.addError("err.bat.ToCurrencyIsEmpty");
            			continue;
            		} 
            		//Add by heyongjiang 20100830
            		else {
            			bankDraftCurrency = keyObj.toString().trim().toUpperCase();
            		}
            		//Add by heyongjiang end
        		} else if (keyName.equals("TO_AMOUNT")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("TO_AMOUNT is empty");
            			errArray.addError("err.bat.ToAmountIsEmpty");
            			continue;
            		} 
        		} else if (keyName.equals("BATCH_STATUS")) {
        			/*
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("BATCH_STATUS is empty");
            			errArray.addError("err.bat.BatchStatusIsEmpty");
            			continue;
            		} 
            		*/
        		} else if (keyName.equals("CORP_ID")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("CORP_ID is empty");
            			errArray.addError("err.bat.CorpIdIsEmpty");
            			continue;
            		}
            		String corpId = corpUser.getCorpId();
            		if (!keyObj.toString().trim().equals(corpId)) {
            			Log.error("CORP_ID is error");
            			errArray.addError("err.bat.CorpIdIsError");
            			continue;
            		}
        		} 
        		
            }
        	
            fileRequestHeader.putAll(recordDetail);
        	
        }
        
//        private void validateBatchDetail(Map recordDetail) throws Exception {
//            boolean errFlag = false;
//    		
//            Set keySet = recordDetail.keySet();
//            for (Iterator it = keySet.iterator(); it.hasNext(); ) {
//                String keyName = (String) it.next();
//                Object keyObj = recordDetail.get(keyName);
//                if (keyObj instanceof String) {
//                    keyObj = new String(
//                    		((String) keyObj).getBytes(),
//                    		Config.getProperty("DBCharset")).trim();
//                }
//                recordDetail.put(keyName, keyObj);
//                
//                // check validate
//           		if (keyName.equals("FROM_ACCOUNT")) {
//            		if (keyObj.equals("")) {
//            			errFlag = true;
//            			break;
//            		} 
//        			
//        		} else if (keyName.equals("TOTAL_NUMBER")) {
//            		if (keyObj.equals("")) {
//            			errFlag = true;
//            			break;
//            		} 
//        		} else if (keyName.equals("ORDER_DATE")) {
//            		if (keyObj.equals("")) {
//            			errFlag = true;
//            			break;
//            		} 
//        		} else if (keyName.equals("TO_CURRENCY")) {
//            		if (keyObj.equals("")) {
//            			errFlag = true;
//            			break;
//            		} 
//        		} else if (keyName.equals("TO_AMOUNT")) {
//            		if (keyObj.equals("")) {
//            			errFlag = true;
//            			break;
//            		} 
//        		} 
//        		
//            }
//        	
//            fileRequestHeader.putAll(recordDetail);
//        	if (!errFlag)
//        		isFileHeaderOk = true;
//        }
        
        private void validateDetail(Map recordData, Map recordDetail) throws Exception {
        	ApplicationContext appContext = Config.getAppContext();
        	CorpAccountService corpAccountService = (CorpAccountService)appContext.getBean("CorpAccountService");
        	
    		boolean errFlag = false;
    		String errType = "";
    		BigDecimal amount = null;
    		String chargeAccout = null;
            Set keySet = recordDetail.keySet();
            for (Iterator it = keySet.iterator(); it.hasNext(); ) {
                String keyName = (String) it.next();
                Object keyObj = recordDetail.get(keyName);
                if (keyObj instanceof String) {
                    keyObj = new String(
                    		((String) keyObj).getBytes(),
                    		Config.getProperty("DBCharset")).trim();
                }
                recordData.put(keyName, keyObj);
    
                // check validate
        		if (keyName.equals("CHARGE_ACCOUNT")) {
        			if (keyObj.toString().trim().equals("")) {
        				errType += "charge__account_empty,";
            			errFlag = true;
            			continue;
            		}
        			if (!keyObj.toString().trim().equals("")) {
        				chargeAccout = keyObj.toString().trim();
        				//add by mxl 0110 锟斤拷锟絚hargeAccount全为0锟斤拷chargeAccount锟斤拷debitAccount
        	            if ( chargeAccout.trim().equals("0000000000")) { 
        	            	recordData.put("CHARGE_ACCOUNT", fromAccount);
        	            } else {
    						//modify by hjs 20070813
    						boolean flag = corpAccountService.checkAccountByUser(corpUser,
    								keyObj.toString().trim());
    						if (!flag) {
    	        				errType += "charge_account_error,";
    	            			errFlag = true;
    	            			continue;
    						}
        	            }
            		}
        			
        		} else if (keyName.equals("SENDER_REFERENCE")) {
        			if (!keyObj.toString().trim().equals("")) {
        				keyObj = Utils.removePrefixZero(keyObj.toString());
            			if (referenceSet.contains(keyObj.toString().trim())) {
                			errType += "reference_duplicated,";
                			errFlag = true;
                			continue;
            			} else {
            				referenceSet.add(keyObj.toString().trim());
            			}
            		}
        		} else if (keyName.equals("DRAFT_AMOUNT")) {
            		if (keyObj.toString().trim().equals("")) {
            			errType += "draft_amt_empty,";
            			errFlag = true;
            			continue;
            		} else {
            			amount = (BigDecimal) keyObj;
            		}
        		}  else if (keyName.equals("PRINT_NAME_INDICATOR")) {
        			if (keyObj.toString().trim().equals("") ) {
            			errType += "print_empty,";
            			errFlag = true;
            			continue;
            		}
        			if ( !((keyObj.toString().trim().equals("Y") || (keyObj.toString().trim().equals("N")) ))) {
            			errType += "print_err,";
            			errFlag = true;
            			continue;
            		}
        			
        		}  /*else if (keyName.equals("BANK_ADDRESS_1")) {
        			if (keyObj.toString().trim().equals("")) {
            			errType += "address1_empty,";
            			errFlag = true;
            			continue;
            		}
        		} else if (keyName.equals("BANK_ADDRESS_2")) {
        			if (!keyObj.toString().trim().equals("")) {
            			errType += "address2_not_empty,";
            			errFlag = true;
            			continue;
            		}
        		} else if (keyName.equals("BENEFICIARY_NAME")) {
        			if (keyObj.toString().trim().equals("")) {
            			errType += "name_empty,";
            			errFlag = true;
            			continue;
        			}
            	} else {
        			errType += "unknown,";
        			errFlag = true;
        			continue;
        		}*/
            }
            
            //Add by heyongjiang 20100830
            //modified by lzg 20190602
           /* String purpose = Utils.null2Empty(recordData.get("PURPOSE"));
            TransferService transferService = (TransferService) appContext
			.getBean("TransferService");
            if(purpose.equals("")){            	
            	int checkPurpose = transferService.checkNeedPurpose(corpUser
        				.getCorpId(), fromAccount, String.valueOf(amount.doubleValue()), null, null, bankDraftCurrency, "MO");
            	if(checkPurpose != 0){
            		errFlag = true;
            		errType += "purpose_empty";
            	}
            } else {
            	//check purposeCode
            	recordData.put("PURPOSE", purpose);
            	Log.info("In BankDraftBatch uploadFile the purpose is" + purpose);
            	
            	if(purpose.length() <= 2){
            		String description = transferService.getPurposeDescription(purpose);
            		if(description != null && !"".equals(description)){
            			recordData.put("PURPOSE", description);
            		}
            	}else{
            		recordData.put("PURPOSE", purpose);
            	}
            	
        	}*/
            //modified by lzg end
            //Add by heyongjiang end
            
            recordData.put("TRANS_ID", CibIdGenerator.getIdForBatchRecord("BANK_DRAFT"));
            recordData.put("BATCH_ID", BATCH_ID);
            recordData.put("CORP_ID", corpUser.getCorpId());
            recordData.put("USER_ID", corpUser.getUserId());
            recordData.put("REMARK", "");
            recordData.put("DETAIL_RESULT", "P");
            recordData.put("EXECUTE_TIME", new Date());
            recordData.put("REQUEST_TIME", new Date());
            //recordData.put("STATUS","0");
            recordData.put("LINE_NO", new Integer(++LINE_NO));
            recordData.remove("RECORD_ID");
            recordData.remove("BD");
            //normalList.add(recordData);
            // if error
    		if (errFlag) {
    			errList.add(recordData);
    			recordData.put("PROBLEM_TYPE", errType);
    			if (amount != null) {
    				errTotalAmt = errTotalAmt.add(amount);
    			}
    		} else {
    			normalList.add(recordData);
    			recordData.put("PROBLEM_TYPE", "");
    			if (amount != null) {
    				normalTotalAmt =   normalTotalAmt.add(amount);
    			}
    		}
        }
       

        public void processRecords(List recordList) throws Exception {
            genericJdbcDao.batchAdd(tableName, recordList);
            count += recordList.size();
        }

        public int getCount() {
            return count;
        }

		public boolean isBatchHeaderExists() {
			return batchHeaderExists;
		}

		public boolean isFileHeaderExists() {
			return fileHeaderExists;
		}

		public boolean isTrailerExists() {
			return trailerExists;
		}
    }
	
	private void addPayrollByJdbc(String tableName, Map dateMap) throws Exception {
		genericJdbcDao.add(tableName, dateMap);
    }
	
	private String getFileName() {
			Date d = new Date();
			return "BOBUBND.O" + DateTime.formatDate(d, "ddHHmmss");
		   }
	
	public void approveBankDraft(FileRequest fileRequest, CorpUser corpUser, BigDecimal equivalentMOP) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
        TransferLimitService transferLimitService = (TransferLimitService)appContext.getBean("TransferLimitService");
        UtilService utilService = (UtilService)appContext.getBean("UtilService");
        fileRequest.setStatus(Constants.STATUS_NORMAL);
        fileRequest.setBatchResult("P");
        fileRequest.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        fileRequest.setExecuteTime(new Date());
		requestDao.update(fileRequest);
		
		String sql_approvel_reqBankDraft_record = 
			"update REQ_BANK_DRAFT set status = ?, DETAIL_RESULT = ?  where BATCH_ID = ? and status is not null";
		try {
			this.genericJdbcDao.update(sql_approvel_reqBankDraft_record,
					new Object[]{Constants.STATUS_NORMAL, "P", fileRequest.getBatchId()});
		} catch (Exception e) {
			throw new NTBException("err.bat.ApproveReqBankDraftError");
		}
	    transferLimitService.addUsedLimitQuota(fileRequest.getFromAccount(),
	    		corpUser.getCorpId(), Constants.TXN_TYPE_BANK_DRAFT,
                 fileRequest.getFromAmount().
                 doubleValue(),
                 equivalentMOP.doubleValue());
        //TODO write report
		utilService.uploadFileToHost("XC05",new File(saveFilePath + fileRequest.getFileName()));
		
	}

	public void clearUnavailableDataByCorpId(String corpId) throws NTBException {
		
		String sql_file_request = "delete from FILE_REQUEST where status is null and CORP_ID = '" + corpId + "'";
    	String sql_bankdraft_record = "delete from REQ_BANK_DRAFT where status is null and CORP_ID = '" + corpId + "'";
    	
    	genericJdbcDao.getJdbcTemplate().execute(sql_file_request);
    	genericJdbcDao.getJdbcTemplate().execute(sql_bankdraft_record);
		
	}

	public void clearUnavailableDataByPaybatchId(String batchId) throws NTBException {
		
		String sql_fileRequest = "delete from FILE_REQUEST where status is null and BATCH_ID = '" + batchId + "'";
    	String sql_bankdraft_record = "delete from REQ_BANK_DRAFT where status is null and BATCH_ID = '" + batchId + "'";
    	genericJdbcDao.getJdbcTemplate().execute(sql_fileRequest);
    	genericJdbcDao.getJdbcTemplate().execute(sql_bankdraft_record);
		
	}

	public void rejectFileRequest(FileRequest fileRequest) throws NTBException {
		fileRequest.setStatus(Constants.STATUS_REMOVED);
		fileRequest.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
		fileRequest.setExecuteTime(new Date());
		requestDao.update(fileRequest);
		
		String sql_reject_transfer_record = 
			"update REQ_BANK_DRAFT set status = ? where BATCH_ID = ? and status is not null";
		try {
			this.genericJdbcDao.update(sql_reject_transfer_record,
					new Object[]{Constants.STATUS_REMOVED, fileRequest.getBatchId()});
		} catch (Exception e) {
			throw new NTBException("err.bat.RejectReqBankDraftrror");
		}
		
		//delete file
		File file = new File(saveFilePath + fileRequest.getFileName());
		if(file.exists()){
			file.delete();
		}
	}

	public void updateStatus(String batchId) throws NTBException {
		String SQL_UPDATE_FILE_REQUEST = 
			"update FILE_REQUEST set status = ? where BATCH_ID = ? and STATUS is null";
		String SQL_UPDATE_REQ_BANK_DRAFT = 
			"update REQ_BANK_DRAFT set status = ? where BATCH_ID = ? and STATUS is null and PROBLEM_TYPE = ''";
		try {
			genericJdbcDao.update(SQL_UPDATE_FILE_REQUEST, 
					new Object[]{Constants.STATUS_PENDING_APPROVAL, batchId});
			
			genericJdbcDao.update(SQL_UPDATE_REQ_BANK_DRAFT, 
					new Object[]{Constants.STATUS_PENDING_APPROVAL, batchId});
		} catch (Exception e) {
			throw new NTBException("err.bat.UpdateReqBankDraftError");
		}		
	}

	public FileRequest viewAvaliableFileRequest(String batchId) throws NTBException {
		String hql = "from FileRequest as pr where pr.batchId = ? and pr.status is not null";
		List list = this.requestDao.list(hql, new Object[]{batchId});
		return (FileRequest) list.get(0);
	}
}
