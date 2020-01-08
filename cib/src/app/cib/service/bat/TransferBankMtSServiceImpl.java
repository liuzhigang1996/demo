package app.cib.service.bat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
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
import app.cib.bo.bat.Payroll;
import app.cib.bo.bat.TransferBankStm;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferBank;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.dao.srv.RequestDao;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.srv.RequestService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.utl.UtilService;
import app.cib.util.Constants;
import app.cib.util.ErrConstants;
import app.cib.util.UploadReporter;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPMessageCollector;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBErrorArray;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.xml.XMLElement;

public class TransferBankMtSServiceImpl implements TransferBankMtSService{
	private static String SELECT_TRANS_ID = "Select TRANS_ID from TRANSFER_BNU_STM where BATCH_ID=?";
	private static final String saveFilePath = Config.getProperty("BatchFileUploadDir") + "/";
	private Map fileRequestHeader = new HashMap();
	private String BATCH_ID = "";
	private int LINE_NO = 0;
	private NTBErrorArray errArray = new NTBErrorArray();
	private boolean isFileError = false;
	private boolean isBatchHeaderOk = false;
	private Set referenceSet = new HashSet();
	
	private List normalList = new ArrayList();
	private List errList = new ArrayList();
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
	
	
	public FileRequestFileBean parseFile(CorpUser corpUser, InputStream inStream) throws NTBException {
		FileRequestFileBean fileRequestFileBean = new FileRequestFileBean();
		BATCH_ID = CibIdGenerator.getRefNoForTransaction();
    	String fileName = this.getFileName();
        File saveFile = new File(saveFilePath + fileName);
        
        this.corpUser = corpUser;
    	FileOutputStream outStream = null;
    	InputStream inStreamForParsing = null;
    	
        //initial
    	fileRequestHeader = new HashMap();
    	LINE_NO = 0;
    	errArray = new NTBErrorArray();
    	isFileError = false;
    	isBatchHeaderOk = false;
    	normalList = new ArrayList();
    	errList = new ArrayList();
    	normalTotalAmt = new BigDecimal("0");
    	errTotalAmt = new BigDecimal("0");
    	referenceSet = new HashSet();
    	
    	try {
			// 锟斤拷锟侥诧拷写锟斤拷锟斤拷锟斤拷锟�
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
	        XMLElement payrollDoc = BatchXMLFactory.getBatchXML("TRANSFER_BANK_MTS");
	        
	        String tableHeader = payrollDoc.getAttribute("table_header");
	        String tableDetail = payrollDoc.getAttribute("table_detail");

	        FileParser parser = new FileParser(payrollDoc, inStreamForParsing);
	        parser.setGenericJdbcDao(this.genericJdbcDao);

	        FileRecordProcessor pro = new MyRecordProcessor(tableDetail);
	        parser.parseRecord(pro);
	       
	        int totalAmt = ((MyRecordProcessor) pro).getCount();
	        
            //no header exception 20110623 by wen
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
		        BigDecimal totalAmtInFile = (BigDecimal) (fileRequestHeader.get("TO_AMOUNT"));
		        if (totalAmtInFile.compareTo(normalTotalAmt.add(errTotalAmt)) !=0) {
	    			cancelUpload(BATCH_ID, outStream, inStreamForParsing, saveFile);
		        } 
		 
		        int totalNoInFile = Integer.parseInt(fileRequestHeader.get("TOTAL_NUMBER").toString());
		        if (totalNoInFile !=  normalList.size() + errList.size()) {
	    			cancelUpload(BATCH_ID, outStream, inStreamForParsing, saveFile);
		        }
	        }
	         //add by linrui 20180119
	          String fileName2 = this.getFileName();
			  File saveFile2 = new File(saveFilePath + fileName2);
			 //end
	        
	        // 写FILRE_QEQUEST锟斤拷
        	if (errArray.size() == 0) {
        		fileRequestHeader.put("BATCH_ID", BATCH_ID);
        		fileRequestHeader.put("FILE_NAME", fileName2);
        		fileRequestHeader.put("BATCH_TYPE", FileRequest.TRANSFER_BANK_MTS);
        		fileRequestHeader.put("CORP_ID", corpUser.getCorpId());
        		fileRequestHeader.put("USER_ID", corpUser.getUserId());
        		fileRequestHeader.put("REMARK", "");
        		fileRequestHeader.put("REQUEST_TIME", new Date());
        		fileRequestHeader.put("EXECUTE_TIME", null);
        		fileRequestHeader.put("AUTH_STATUS", Constants.AUTH_STATUS_SUBMITED);
        		fileRequestHeader.put("OPERATION", Constants.OPERATION_NEW);
        		fileRequestHeader.put("BATCH_RESULT", "P");
        		fileRequestHeader.remove("RECORD_ID");
        		fileRequestHeader.remove("BH");
        		// 锟睫达拷锟斤拷锟铰硷拷锟叫碏ILRE_QEQUEST锟斤拷
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
        	fileRequestFileBean.setNormalTotalAmt(normalTotalAmt.doubleValue());
        	fileRequestFileBean.setErrTotalAmt(errTotalAmt.doubleValue());
        	fileRequestFileBean.setAllCount(totalAmt);
        	fileRequestFileBean.setFileRequestHeader(fileRequestHeader);
        	//add by linrui 20180119
			writeBankMtsFile(fileRequestFileBean,  saveFile2);
			//
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
            	// validate file header
            	validateFH(fileHeader);
                return FileRecordProcessor.FILE_RECORD_NOPROCESS;
                
            } else if (FileRecordProcessor.RECORD_BATCH_HEADER_BANK_MTS.equals(recordId)) {
            	
            	batchHeaderExists = true; // 20110623 by wen
            	
            	fileHeader = (Map) recordData.get("BH");
            	// validate file header
            	validateFH(fileHeader);
                return FileRecordProcessor.FILE_RECORD_NOPROCESS;
                
            }else if (FileRecordProcessor.RECORD_FILE_END2.equals(recordId)) {
            	
            	trailerExists = true; // 20110623 by wen
            	
                return FileRecordProcessor.FILE_RECORD_STOP;
                
            } else if (FileRecordProcessor.RECORD_BATCH_HEADER_BANK_STM.equals(recordId)) {
            	
            	throw new NTBException("err.bat.TheUploadFileIsNotMatchTransferBankMtS");
            	
            }
            
            if(batchHeaderExists){ // check batch detail when batch header exists 20110624 by wen
            	if (FileRecordProcessor.RECORD_BATCH_DETAIL.equals(recordId)) {
                    Map recordDetail = (Map) recordData.get("BD");
                    //validate batch detail
                    validateBD(recordData, recordDetail);
                    return FileRecordProcessor.FILE_RECORD_BATCH;
                    
                } 
            }
            
            return FileRecordProcessor.FILE_RECORD_NOPROCESS;

        }
        
        /**
         * FH : File Header
         * @param recordDetail
         * @throws Exception
         */
        private void validateFH(Map recordDetail) throws Exception {
        	ApplicationContext appContext = Config.getAppContext();
        	RequestService requestService = (RequestService) appContext
					.getBean("RequestService");
			CorpAccountService corpAccountService = (CorpAccountService) appContext
					.getBean("CorpAccountService");

    		//锟叫断革拷锟斤拷锟斤拷欠锟斤拷锟斤拷峤�add by mxl 1225
    		if (requestService.listFileRequestBythreekeys(
					recordDetail.get("CORP_ID").toString(),
					recordDetail.get("ORDER_DATE").toString(),
					recordDetail.get("BATCH_REFERENCE").toString(),
					FileRequest.TRANSFER_BANK_MTS).size() > 0) {
				errArray.addError("err.bat.TheUploadBatchFileHasBeenSubmitted");
			}
            Set keySet = recordDetail.keySet();
            
			//Jet added, check currency of from account must equal to transfer currency
			//String Currency_acount = null;
			String Currency_transfer = null;

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
        		if (keyName.equals("TO_ACCOUNT")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("TO_ACCOUNT is empty");
            			errArray.addError("err.bat.toAccountIsEmpty");
            			continue;
            		} else {
						//Jet added, check currency of from account must equal to transfer currency
						//Currency_acount = corpAccountService.getCurrency(keyObj.toString(),true);
            		}
        			
        		} else if (keyName.equals("TO_AMOUNT")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("TO_AMOUNT is empty");
            			errArray.addError("err.bat.ToAmountIsEmpty");
            			continue;
            		} 
        		} 
        		//Add by heyongjiang 2010-04-21
        		else if (keyName.equals("TO_ACCOUNT_NAME")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("TO_ACCOUNT_NAME is empty");
            			errArray.addError("err.bat.ToAccountNameIsEmpty");
            			continue;
            		} 
        		}
        		
        		else if (keyName.equals("TOTAL_NUMBER")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("TOTAL_NUMBER is empty");
            			errArray.addError("err.bat.TotalNumberIsEmpty");
            			continue;
            		} 
        		} else if (keyName.equals("TO_CURRENCY")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("TO_CURRENCY is empty");
            			errArray.addError("err.bat.ToCurrencyIsEmpty");
            			continue;
            		} else {
						//Jet added, check currency of from account must equal to transfer currency
						Currency_transfer = keyObj.toString();
            		}
            	}  else if (keyName.equals("CORP_ID")) {
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
            	}  else if (keyName.equals("ORDER_DATE")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("ORDER_DATE is empty");
            			errArray.addError("err.bat.OrderDateIsEmpty");
            			continue;
            		}
            		try {
        	            (new SimpleDateFormat("yyyyMMdd")).parse(keyObj.toString().trim());
        			} catch (Exception e) {
            			Log.error("ORDER_DATE format error");
            			errArray.addError("err.bat.OrderDateFormatError");
            			continue;
        			}
            	} 
            }// end for
            
			//comment by linrui 20190603
			/*if ((Currency_acount != null && Currency_transfer != null ) && !Currency_acount.equals(Currency_transfer)){
				Log.error("Currency not equal error");
				errArray.addError("err.bat.CreditCurrencyEqualError");				
			}*/

           fileRequestHeader.putAll(recordDetail);        	   
        }
        
        /**
         * BD : Batch Detail
         * @param recordData
         * @param recordDetail
         * @throws Exception
         */
        private void validateBD(Map recordData, Map recordDetail) throws Exception {
        	ApplicationContext appContext = Config.getAppContext();
        	RequestService requestService = (RequestService) appContext.getBean("RequestService");
    		boolean errFlag = false;
    		String errType = "";
    		BigDecimal amount = null;
    		
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
                if (keyName.equals("FROM_ACCOUNT")) {
            		if (keyObj.toString().trim().equals("")) {
            			errType += "from_acc_empty,";
            			errFlag = true;
            			continue;
            		} else {
                        //add by mxl 锟叫断革拷锟绞伙拷锟角凤拷锟斤拷权锟斤拷转锟绞诧拷锟斤拷 1115
            			List accountList = new ArrayList();
            			boolean flag = true;
            		    accountList = requestService.accListCorpAccount(corpUser.getCorpId());
            			if (null != accountList && accountList.size() > 0) {
            			for (int i = 0; i < accountList.size(); i++) {
            				 Map accountMap = new HashMap();
            				 String accountNumber = null;
            				 accountMap = (Map)accountList.get(i);
            				 accountNumber = (String)accountMap.get("ACCOUNT_NO");
            				 if(keyObj.equals(accountNumber)) {
            					 flag = false;
            				 }
            			}
            			} 
            			if (flag) {
            				errType += "from_acc_err,";
                			errFlag = true;
                			continue;
            			}
            		}
        			
        		}  else if (keyName.equals("DEBIT_AMOUNT")) {
            		if (keyObj.toString().trim().equals("")) {
            			errType += "debit_amt_empty,";
            			errFlag = true;
            			continue;
            		} else {
            			amount = (BigDecimal) keyObj;
            		}
        		}   else if (keyName.equals("REMARK")) {
        			if (keyObj.toString().trim().equals("")) {
        				errType += "remark_empty,";
            			errFlag = true;
            			continue;
            		}
        			keyObj = Utils.removePrefixZero(keyObj.toString());
        			if (referenceSet.contains(keyObj.toString().trim())) {
            			errType += "reference_duplicated,";
            			errFlag = true;
            			continue;
        			} else {
        				referenceSet.add(keyObj.toString().trim());
        			}
        		} /*else {
        			errType += "unknown,";
        			errFlag = true;
        			continue;
        		}*/
        		
            }
            recordData.put("TRANS_ID", CibIdGenerator.getIdForBatchRecord("TRANSFER_BANK_MTS"));
            recordData.put("BATCH_ID", BATCH_ID);
            recordData.put("CORP_ID", corpUser.getCorpId());
            recordData.put("USER_ID", corpUser.getUserId());
            //recordData.put("REMARK", "");
            recordData.put("EXECUTE_TIME", new Date());
            recordData.put("REQUEST_TIME", new Date());
            recordData.put("LINE_NO", new Integer(++LINE_NO));
            recordData.remove("RECORD_ID");
            recordData.remove("BD");
            
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
	return "BOBUBTC.O" + DateTime.formatDate(d, "ddHHmmss")+".TXT";//mod by linrui +.txt 20180119
   }
	public void approveTransferBankMtS(FileRequest fileRequest, CorpUser corpUser) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
        TransferLimitService transferLimitService = (TransferLimitService)appContext.getBean("TransferLimitService");
        ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
        TransferBankMtSService transferBankMtSService = (TransferBankMtSService) appContext.getBean("TransferBankMtSService");
        CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
        UtilService utilService = (UtilService)appContext.getBean("UtilService");
        fileRequest.setStatus(Constants.STATUS_NORMAL);
        fileRequest.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        fileRequest.setBatchResult("P");
        fileRequest.setExecuteTime(new Date());
		requestDao.update(fileRequest);
		
		String sql_approvel_transferBankMtS_record = 
			"update TRANSFER_BNU_STM set status = ?, DETAIL_RESULT = ? where BATCH_ID = ? and status is not null";
		try {
			this.genericJdbcDao.update(sql_approvel_transferBankMtS_record,
					new Object[]{Constants.STATUS_NORMAL, "P", fileRequest.getBatchId()});
		} catch (Exception e) {
			throw new NTBException("err.bat.ApproveTransferBankMtSError");
		}
        // batchId TransferBankStM transfer transId 
		List transIdList = new ArrayList();
		transIdList = transferBankMtSService.transIdListTransferBankStm(fileRequest.getBatchId());
	    if (null != transIdList && transIdList.size() > 0) {
	     for (int i=0; i<transIdList.size();i++) {
	    	 Map transIdMap = new HashMap();
	    	 String transId = null;
	    	 transIdMap = (Map) transIdList.get(0);
	    	 transId = (String) transIdMap.get("TRANS_ID");
	    	 TransferBankStm transferBankStm = transferBankMtSService.viewInBANKStm(transId);
	    	 
	    	 CorpAccount corpAccount2 = corpAccountService.viewCorpAccount(transferBankStm.getFromAccount());
	 		 String  fromCurrency = corpAccount2.getCurrency();
	 		 transferBankStm.setFromCurrency(fromCurrency);
	 		 // fromCurrency fromAmount add by mxl 1117
	 		 BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), fileRequest.getToCurrency(), transferBankStm.getFromCurrency(),new BigDecimal(transferBankStm
					.getDebitAmount().toString()), null, 2);
	 		 // fromAmount 
	     	 BigDecimal fromEquivalentMOP = exRatesService.getEquivalent(corpUser
	 				.getCorpId(),transferBankStm.getFromCurrency(), "MOP",
	 				new BigDecimal(fromAmount.toString()),
	 				null, 2);
	    	 
	    	 // add Limit 
	 		 transferLimitService.addUsedLimitQuota(transferBankStm.getFromAccount(),
	 				corpUser.getCorpId(), 
	 				Constants.TXN_TYPE_TRANSFER_BANK,
	 				fromAmount.doubleValue(), fromEquivalentMOP.doubleValue());
	     }
        } 
		// TODO write report edit by mxl 1122
//		utilService.uploadFileToHost("51XX",new File(saveFilePath + fileRequest.getFileName()));
		utilService.uploadFileToHost("51XX",new File(saveFilePath + fileRequest.getFileName()),FileRequest.TRANSFER_BANK_MTS);//mod by linrui 20180309
	}

	public void clearUnavailableDataByCorpId(String corpId) throws NTBException {
		String sql_file_request = "delete from FILE_REQUEST where status is null and CORP_ID = '" + corpId + "'";
    	String sql_transfer_bank_mts = "delete from TRANSFER_BNU_STM where status is null and CORP_ID = '" + corpId + "'";
    	
    	genericJdbcDao.getJdbcTemplate().execute(sql_file_request);
    	genericJdbcDao.getJdbcTemplate().execute(sql_transfer_bank_mts);
	}

	public void clearUnavailableDataByPaybatchId(String batchId) throws NTBException {
		String sql_file_request = "delete from FILE_REQUEST where status is null and BATCH_ID = '" + batchId + "'";
    	String sql_transfer_bank_mts = "delete from TRANSFER_BNU_STM where status is null and BATCH_ID = '" + batchId + "'";
    	
    	genericJdbcDao.getJdbcTemplate().execute(sql_file_request);
    	genericJdbcDao.getJdbcTemplate().execute(sql_transfer_bank_mts);
	}

	

	public void updateStatus(String batchId) throws NTBException {
		String SQL_UPDATE_FILE_REQUEST = 
			"update FILE_REQUEST set status = ? where BATCH_ID = ? and STATUS is null";
		String SQL_UPDATE_TRANSFER_BANK_MTS = 
//			"update TRANSFER_BANK_STM set status = ? where BATCH_ID = ? and STATUS is null and PROBLEM_TYPE = ''";
		    "update TRANSFER_BNU_STM set status = ? where BATCH_ID = ? and STATUS is null and PROBLEM_TYPE is null";//mod by linrui 20180323 

		try {
			genericJdbcDao.update(SQL_UPDATE_FILE_REQUEST, 
					new Object[]{Constants.STATUS_PENDING_APPROVAL, batchId});
			
			genericJdbcDao.update(SQL_UPDATE_TRANSFER_BANK_MTS, 
					new Object[]{Constants.STATUS_PENDING_APPROVAL, batchId});
		} catch (Exception e) {
			throw new NTBException("err.bat.UpdateTransferBankMtSError");
		}
		
	}

	public void rejectFileRequest(FileRequest fileRequest) throws NTBException {
		fileRequest.setStatus(Constants.STATUS_REMOVED);
		fileRequest.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
		fileRequest.setExecuteTime(new Date());
		requestDao.update(fileRequest);
		
		String sql_reject_transferBank_MtS_record = 
			"update TRANSFER_BNU_STM set status = ? where BATCH_ID = ? and status is not null";
		try {
			this.genericJdbcDao.update(sql_reject_transferBank_MtS_record,
					new Object[]{Constants.STATUS_REMOVED, fileRequest.getBatchId()});
		} catch (Exception e) {
			throw new NTBException("err.bat.RejectTransferBankMtSError");
		}
		
		//delete file
		File file = new File(saveFilePath + fileRequest.getFileName());
		if(file.exists()){
			file.delete();
		}
	}

	public FileRequest viewAvaliableFileRequest(String batchId) throws NTBException {
		String hql = "from FileRequest as pr where pr.batchId = ? and pr.status is not null";
		List list = this.requestDao.list(hql, new Object[]{batchId});
		return (FileRequest) list.get(0);
	}

	public List transIdListTransferBankStm(String batchId) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext
                             .getBean("genericJdbcDao");
        List transIdList = null;
        try {
        	transIdList = dao.query(SELECT_TRANS_ID, new Object[] {batchId});
        } catch (Exception e) {
            e.printStackTrace();
        }
		return transIdList;
	}

	public TransferBankStm viewInBANKStm(String transID) throws NTBException {
		TransferBankStm transferBankStm = null;
        if ((transID != null) && (!transID.equals(""))) {
            transferBankStm = (TransferBankStm) requestDao.load(TransferBankStm.class,
                    transID);
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }
        return transferBankStm;
	}
	//for write file 20180120
	private void writeBankMtsFile(FileRequestFileBean fileRequestFileBean, File saveFile) throws Exception {
		Map fileRequest = fileRequestFileBean.getFileRequestHeader();
		List normalList = fileRequestFileBean.getNormalList();

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(saveFile));		
			// File Header
			String valDate, ccy, amt, dr_Ac, dr_ccy, cr_Ac, cr_ccy, remark, narr, oth_Ac, oth_Nm, oth_Bk,ref
			,cr_name;
			String dacType = "D"; // D娲绘湡
			String cacType = "D";
			String dr_Br = Utils.prefixZero("",11);
			String cr_Br = Utils.prefixZero("",11);
			// data from header
			valDate = DateTime.formatDate(new Date(), "yyyyMMdd");
			ccy = Utils.prefixSpace(fileRequestHeader.get("TO_CURRENCY").toString(),3); // transfer_ccy
			// to_account
			cr_Ac = Utils.appendSpace(fileRequestHeader.get("TO_ACCOUNT")
					.toString(),32);
			// to_ccy	update by linrui 20180120
			cr_ccy =  Utils.prefixSpace(getCcyByAcct(fileRequestHeader.get("TO_ACCOUNT").toString()),3);						
			//end
			narr = Utils.appendSpace("", 72);
			oth_Ac = Utils.appendSpace("", 32);
			oth_Nm = Utils.appendSpace("", 122);
			oth_Bk = Utils.appendSpace("", 14);
			
			Map row = null;
			for (int i = 0; i < normalList.size(); i++) {
				// data from detail
				row = (Map) normalList.get(i);
				// from_acct
				dr_Ac = Utils.appendSpace(row.get("FROM_ACCOUNT").toString(),32); 
				// from_ccy
				dr_ccy = Utils.prefixSpace(getCcyByAcct(row.get("FROM_ACCOUNT").toString()),3);				
				amt = Format.formatDecimal(row.get("DEBIT_AMOUNT").toString(),2);
				amt = Utils.prefixZero(Utils.replaceStr(Utils.replaceStr(amt, ",", ""), ".", ""), 16); // transferAmt
				remark = Utils.prefixSpace(row.get("REMARK").toString(),120);// 
				ref = Utils.appendSpace(CibIdGenerator.getTDRef(), 64);//add by linrui 20190412
				//cr_name = Utils.prefixSpace(row.get("TO_ACCOUNT_NAME").toString(),120);//add by linrui 20190412
				cr_name = Utils.prefixSpace(fileRequest.get("TO_ACCOUNT_NAME").toString(),120);//add by linrui 20190412
				writer.write(ref + cr_ccy + amt + dacType +  dr_Ac  + dr_Br + 
						cacType + cr_Ac + cr_Br + remark + narr); //mod by linrui 20180308
				writer.write("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			// finish, close stream!
			if(writer != null){
				writer.close();
			}
		}
	}
	//search ccy from acct
	public String getCcyByAcct(String acct) throws NTBException {
     String sql = "select * from Corp_Account ca where ca.ACCOUNT_NO = ?";
     try{
     Map map = genericJdbcDao.querySingleRow(sql, new Object[] { acct });
     return  map.get("CURRENCY").toString();
     } catch (Exception e) {
			throw new NTBException("err.bat.RejectTransferBankSTMError");
		}
    }
	//add by linrui for toHOst tran MTS 20180308
	public Map toHostTransferSTM(FileRequest pojoFileRequest,
			CorpUser corpUser, String txnType) throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
			
		ApplicationContext appContext = Config.getAppContext();
		CibTransClient testClient = new CibTransClient("CIB", "ZJ57");
		toHost.put("FILE_NM", pojoFileRequest.getFileName());	
		toHost.put("SERV_CD", "BSPCM01");
		testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_TRANSFER,
				CibTransClient.ACCTYPE_3RD_PARTY, pojoFileRequest.getBatchId());
		fromHost = testClient.doTransaction(toHost);
		// 濡傛灉浜ゆ槗涓嶆垚鍔熷垯鎶ュ嚭涓绘満閿欒 0828
		if (!"3".equals(fromHost.get("RESPONSE_CODE"))) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		return fromHost;
//		return null;
	}
}
