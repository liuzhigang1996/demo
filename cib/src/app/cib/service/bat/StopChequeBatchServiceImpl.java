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
import app.cib.service.txn.TransferLimitService;
import app.cib.service.utl.UtilService;
import app.cib.util.Constants;
import app.cib.util.ErrConstants;

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
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.xml.XMLElement;

public class StopChequeBatchServiceImpl implements StopChequeBatchService{
	private static final String saveFilePath = Config.getProperty("BatchFileUploadDir") + "/";
	private Map fileRequestHeader = new HashMap();
	private String BATCH_ID = "";
	private int LINE_NO = 0;
	private NTBErrorArray errArray = new NTBErrorArray();
	private boolean isFileHeaderOk = false;
	private boolean isBatchHeaderOk = false;
	private Set referenceSet = new HashSet();
	
	private List normalList = new ArrayList();
	private List errList = new ArrayList();
	
	private BigDecimal normalTotalAmt =  new BigDecimal("0");
	private BigDecimal errTotalAmt =  new BigDecimal("0");
	
	private CorpUser corpUser;
	private GenericJdbcDao genericJdbcDao;
	private RequestDao requestDao;
	
	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public RequestDao getRequestDao() {
		return requestDao;
	}

	public void setRequestDao(RequestDao requestDao) {
		this.requestDao = requestDao;
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
    	isFileHeaderOk = false;
    	isBatchHeaderOk = false;
    	normalList = new ArrayList();
    	errList = new ArrayList();
    	normalTotalAmt = new BigDecimal("0");
    	errTotalAmt = new BigDecimal("0");
    	referenceSet = new HashSet();
    	try {
			// ���Ĳ�д�������
			outStream = new FileOutputStream(saveFile);
			byte[] buffer = new byte[4096];
			int readCount = 1;
			while ((readCount = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, readCount);
			}
			outStream.close();
			inStream.close();
			
			// �����ļ�
			inStreamForParsing = new FileInputStream(saveFile);
	        XMLElement payrollDoc = BatchXMLFactory.getBatchXML("STOP_CHEQUE");
	        
	        String tableHeader = payrollDoc.getAttribute("table_header");
	        String tableDetail = payrollDoc.getAttribute("table_detail");

	        FileParser parser = new FileParser(payrollDoc, inStreamForParsing);
	        parser.setGenericJdbcDao(this.genericJdbcDao);

	        FileRecordProcessor pro = new MyRecordProcessor(tableDetail);
	        parser.parseRecord(pro);
	        int totalAmt = ((MyRecordProcessor) pro).getCount();
	        System.out.println("fileRequestHeader="+fileRequestHeader);
	        
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
		        int totalNoInFile = Integer.parseInt(fileRequestHeader.get("TOTAL_NUMBER").toString());
		        if (totalNoInFile !=  normalList.size() + errList.size()) {
	    			cancelUpload(BATCH_ID, outStream, inStreamForParsing, saveFile);
		        }
	        }
	       
	       
	        // дFILRE_QEQUEST��
        	if (errArray.size() == 0) {
        		fileRequestHeader.put("BATCH_ID", BATCH_ID);
        		fileRequestHeader.put("FILE_NAME", fileName);
        		fileRequestHeader.put("BATCH_TYPE", FileRequest.STOP_CHEQUE_BATCH_TYPE);
        		fileRequestHeader.put("CORP_ID", corpUser.getCorpId());
        		fileRequestHeader.put("USER_ID", corpUser.getUserId());
        		fileRequestHeader.put("REMARK", "");
        		fileRequestHeader.put("REQUEST_TIME", new Date());
        		fileRequestHeader.put("EXECUTE_TIME", null);
        		fileRequestHeader.put("AUTH_STATUS", Constants.STATUS_PENDING_APPROVAL);
        		fileRequestHeader.put("BATCH_RESULT", "P");
        		fileRequestHeader.remove("RECORD_ID");
        		fileRequestHeader.remove("BH");
        		// �޴����¼��дFILRE_QEQUEST��
        		if (errList.size() == 0) {
            		addPayrollByJdbc(tableHeader, fileRequestHeader);
        		} else {
        			//�������˴�д������
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
	                
	            } else if (FileRecordProcessor.RECORD_BATCH_HEADER.equals(recordId)) {
	            	
	            	batchHeaderExists = true; // 20110623 by wen
	            	
	            	fileHeader = (Map) recordData.get("BH");
	            	// validate file header
	            	validateFH(fileHeader);
	                return FileRecordProcessor.FILE_RECORD_NOPROCESS;
	                
	            } else if (FileRecordProcessor.RECORD_FILE_END2.equals(recordId)) {
	            	
	            	trailerExists = true; // 20110623 by wen
	            	
	                return FileRecordProcessor.FILE_RECORD_STOP;
	                
	            }
	            
	            if(batchHeaderExists){ //check batch detail when batch header exists 20110624 by wen
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
	        	RequestService requestService = (RequestService) appContext.getBean("RequestService");

	        	//�жϸ�����Ƿ����ύ add by mxl 1225
	    		if ( requestService.listFileRequestBythreekeys(recordDetail.get("CORP_ID").toString(),recordDetail.get("ORDER_DATE").toString(),recordDetail.get("BATCH_REFERENCE").toString(),FileRequest.STOP_CHEQUE_BATCH_TYPE).size()>0) {
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
	           		if (keyName.equals("ORDER_DATE")) {
	            		if (keyObj.toString().trim().equals("")) {
	            			Log.error("ORDER_DATE is empty");
	             			errArray.addError("err.bat.OrderDateIsEmpty");
	             			continue;
	            		} 
	        			
	        		} else if (keyName.equals("TOTAL_NUMBER")) {
	            		if (keyObj.toString().trim().equals("")) {
	            			Log.error("FROM_ACCOUNT is empty");
	             			errArray.addError("err.bat.FromAccountIsEmpty");
	             			continue;
	            		} 
	        		} else if (keyName.equals("CORP_ID")) {
	            		if (keyObj.toString().trim().equals("")) {
	            			Log.error("CORP_ID is empty");
	             			errArray.addError("err.bat.CorpIdIsEmpty");
	             			continue;
	            		} 
	            		/*
	            		String corpId = corpUser.getCorpId();
	            		if (!keyObj.toString().trim().equals(corpId)) {
	            			Log.error("CORP_ID is error");
	            			errArray.addError("err.bat.CorpIdIsError");
	            			continue;
	            		}
	            		*/
	        		} 
	                
	            }

	                fileRequestHeader.putAll(recordDetail);
	        	
	        }

	        /**
	         * BD : Batch Detail
	         * @param recordData
	         * @param recordDetail
	         * @throws Exception
	         */
	        private void validateBD(Map recordData, Map recordDetail) throws Exception {
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
	                if (keyName.equals("CURRENT_ACCOUNT")) {
	            		if (keyObj.toString().trim().equals("")) {
	            			errType += "acc_empty,";
	            			errFlag = true;
	            			continue;
	            		} 
	        			//if (!keyObj.toString().trim().equals("")) {
	        				 // �жϸñ���ϸ�Ƿ��ѱ��ύ add by mxl 1226
	    	                String currentAccount = recordDetail.get("CURRENT_ACCOUNT").toString().trim();
	    	                String chequeNumber = recordDetail.get("CHEQUE_NUMBER").toString().trim();
	    	                String key = currentAccount + chequeNumber;
	    	                Log.info("key="+key.trim());
	    	                System.out.println("key="+key.trim());
	    	                if (referenceSet.contains(key.trim())) {
	                			errType += "stopCheque_key_duplicated,";
	                			errFlag = true;
	                			continue;
	            			} else {
	            				referenceSet.add(key.trim());
	            			}
	        			//}
	        		} else if (keyName.equals("AMOUNT")) {
	            		if (keyObj.toString().trim().equals("")) {
	            			errType += "cheque_amt_empty,";
	            			errFlag = true;
	            			continue;
	            		} else if(Double.parseDouble(keyObj.toString().trim().toString())==0){
	            			errType += "cheque_amt_err,";
	            			errFlag = true;
	            			continue;
	            		} else {
	            			amount = (BigDecimal) keyObj;
	            		}
	        		}  else if (keyName.equals("CHEQUE_NUMBER")) {
	        			if (keyObj.toString().trim().equals("")) {
	            			errType += "cheque_number_empty,";
	            			errFlag = true;
	            			continue;
	            		} else if(Integer.parseInt(keyObj.toString().trim().toString())==0){
	            			errType += "cheque_number_err,";
	            			errFlag = true;
	            			continue;
	            		}
	        			
	        		}  else if (keyName.equals("EXPIRY_DATE")) {
	        			/*
	        			if (keyObj.toString().trim().equals("")) {
	            			errType += "expiry_date_err,";
	            			errFlag = true;
	            			continue;
	            		}
	            		*/
	        		} else if (keyName.equals("ISSUE_DATE")) {
	        			if (keyObj.toString().trim().equals("")) {
	            			errType += "issue_date_empty,";
	            			errFlag = true;
	            			continue;
	            		}
	        		} else if (keyName.equals("BENEFICIARY_NAME")) {
	        			/*
	        			if (keyObj.toString().trim().equals("")) {
	            			errType += "name_err,";
	            			errFlag = true;
	            			continue;
	            		}
	            		*/
	        		} /*else {
	        			errType += "unknown,";
	        			errFlag = true;
	        			continue;
	        		}*/
	        		
	            }
	            recordData.put("TRANS_ID", CibIdGenerator.getIdForBatchRecord("STOP_CHEQUE"));
	            recordData.put("BATCH_ID", BATCH_ID);
	            recordData.put("CORP_ID", corpUser.getCorpId());
	            recordData.put("USER_ID", corpUser.getUserId());
	            recordData.put("REMARK", "");
	            recordData.put("EXECUTE_TIME", new Date());
	            recordData.put("REQUEST_TIME", new Date());
	            recordData.put("DETAIL_RESULT", "P");
	            recordData.put("LINE_NO", new Integer(++LINE_NO));
	            recordData.remove("RECORD_ID");
	            recordData.remove("BD");
	            
	    		// if error
	    		if (errFlag) {
	    			errList.add(recordData);
	    			recordData.put("PROBLEM_TYPE", errType);
	    			if (amount != null) {
	    				errTotalAmt = errTotalAmt.add(amount);;
	    			}
	    		} else {
	    			normalList.add(recordData);
	    			recordData.put("PROBLEM_TYPE", "");
	    			if (amount != null) {
	    				normalTotalAmt = normalTotalAmt.add(amount);
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
		return "BOBUSCF.O" + DateTime.formatDate(d, "ddHHmmss");
	}
	public void approveStopCheque(FileRequest fileRequest, CorpUser corpUser) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		UtilService utilService = (UtilService)appContext.getBean("UtilService");
        fileRequest.setStatus(Constants.STATUS_NORMAL);
        fileRequest.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        fileRequest.setBatchResult("P");
        fileRequest.setExecuteTime(new Date());
		requestDao.update(fileRequest);
		
		String sql_approvel_reqStopCheque_record = 
			"update REQ_STOP_CHEQUE set status = ?, DETAIL_RESULT = ?  where BATCH_ID = ? and status is not null";
		try {
			this.genericJdbcDao.update(sql_approvel_reqStopCheque_record,
					new Object[]{Constants.STATUS_NORMAL, "P", fileRequest.getBatchId()});
		} catch (Exception e) {
			throw new NTBException("err.bat.ApproveReqStopChequeError");
		}
		
        //TODO write report
		Map reportMap = new HashMap();
		utilService.uploadFileToHost("XC08",new File(saveFilePath + fileRequest.getFileName()));
		
	}

	public void clearUnavailableDataByCorpId(String corpId) throws NTBException {
		String sql_file_request = "delete from FILE_REQUEST where status is null and CORP_ID = '" + corpId + "'";
    	String sql_stopCheuque_record = "delete from REQ_STOP_CHEQUE where status is null and CORP_ID = '" + corpId + "'";
    	
    	genericJdbcDao.getJdbcTemplate().execute(sql_file_request);
    	genericJdbcDao.getJdbcTemplate().execute(sql_stopCheuque_record);
		
	}

	public void clearUnavailableDataByPaybatchId(String batchId) throws NTBException {
		String sql_fileRequest = "delete from FILE_REQUEST where status is null and BATCH_ID = '" + batchId + "'";
    	String sql_stopCheuque_record = "delete from REQ_STOP_CHEQUE where status is null and BATCH_ID = '" + batchId + "'";
    	genericJdbcDao.getJdbcTemplate().execute(sql_fileRequest);
    	genericJdbcDao.getJdbcTemplate().execute(sql_stopCheuque_record);
		
	}

	public void rejectFileRequest(FileRequest fileRequest) throws NTBException {
		fileRequest.setStatus(Constants.STATUS_REMOVED);
		fileRequest.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
		fileRequest.setExecuteTime(new Date());
		requestDao.update(fileRequest);
		
		String sql_reject_stopCheuque_record = 
			"update REQ_STOP_CHEQUE set status = ? where BATCH_ID = ? and status is not null";
		try {
			this.genericJdbcDao.update(sql_reject_stopCheuque_record,
					new Object[]{Constants.STATUS_REMOVED, fileRequest.getBatchId()});
		} catch (Exception e) {
			throw new NTBException("err.bat.RejectReqStopChequeError");
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
		String SQL_UPDATE_REQ_STOP_CHEQUE = 
			"update REQ_STOP_CHEQUE set status = ? where BATCH_ID = ? and STATUS is null and PROBLEM_TYPE = ''";

		try {
			genericJdbcDao.update(SQL_UPDATE_FILE_REQUEST, 
					new Object[]{Constants.STATUS_PENDING_APPROVAL, batchId});
			
			genericJdbcDao.update(SQL_UPDATE_REQ_STOP_CHEQUE, 
					new Object[]{Constants.STATUS_PENDING_APPROVAL, batchId});

		} catch (Exception e) {
			throw new NTBException("err.bat.UpdateReqStopChequeError");
		}
		
		
	}

	public FileRequest viewAvaliableFileRequest(String batchId) throws NTBException {
		String hql = "from FileRequest as pr where pr.batchId = ? and pr.status is not null";
		List list = this.requestDao.list(hql, new Object[]{batchId});
		return (FileRequest) list.get(0);
	}
}
