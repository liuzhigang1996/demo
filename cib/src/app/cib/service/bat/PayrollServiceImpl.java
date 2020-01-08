/**
 * @author hjs
 * 2006-10-11
 */
package app.cib.service.bat;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bat.Payroll;
import app.cib.bo.bat.PayrollFileBean;
import app.cib.bo.bat.PayrollRec;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpPermission;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.dao.bat.PayrollDao;
import app.cib.dao.bat.PayrollRecDao;
import app.cib.dao.sys.CorpAccountDao;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.utl.UtilService;
import app.cib.util.Constants;
import app.cib.util.ErrConstants;

import com.jsax.service.util.FtpUtil;
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

/**
 * @author hjs
 * 2006-10-11
 */
public class PayrollServiceImpl implements PayrollService {
	
	private static final String saveFilePath = Config.getProperty("BatchFileUploadDir") + "/";
	private Map payrollHeader = new HashMap();
	private String payrollId = "";
	private int lineNo = 0;
	private NTBErrorArray errArray = new NTBErrorArray();
	private List errParameters = new ArrayList();

	private List normalList = new ArrayList();
	private List errList = new ArrayList();
	private BigDecimal normalTotalAmt = new BigDecimal("0");
	private BigDecimal errTotalAmt = new BigDecimal("0");
	private Set accountSet = new HashSet();

	private CorpUser corpUser;
	
	private PayrollDao payrollDao;
	private PayrollRecDao payrollRecDao;
	private CorpAccountDao corpAccountDao;
	private GenericJdbcDao genericJdbcDao;
	private String uploadFileName;
	private static String UPDATE_PAYROLL_STATUS = "update payroll set BATCH_RESULT = ? where PAYROLL_ID = ? ";
	private static String UPDATE_PAYROLLREC_STATUS = "update payroll_rec set DETAIL_RESULT = ? where TRANS_ID = ? ";


	public PayrollDao getPayrollDao() {
		return payrollDao;
	}

	public void setPayrollDao(PayrollDao payrollDao) {
		this.payrollDao = payrollDao;
	}

	public PayrollRecDao getPayrollRecDao() {
		return payrollRecDao;
	}

	public void setPayrollRecDao(PayrollRecDao payrollRecDao) {
		this.payrollRecDao = payrollRecDao;
	}

	public CorpAccountDao getCorpAccountDao() {
		return corpAccountDao;
	}

	public void setCorpAccountDao(CorpAccountDao corpAccountDao) {
		this.corpAccountDao = corpAccountDao;
	}

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public void addPayroll(Payroll payroll) throws NTBException {
		payrollDao.add(payroll);
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public PayrollFileBean parseFile(CorpUser corpUser, InputStream inStream) throws NTBException {
		PayrollFileBean payrollFileInfo = new PayrollFileBean();
    	payrollId = CibIdGenerator.getRefNoForTransaction();
    	String fileName = this.getFileName();
        File saveFile = new File(saveFilePath + fileName);
        //check file exist
        if (saveFile.exists()) {
        	throw new NTBException("err.bat.FileExistError");
        }
    	
    	this.corpUser = corpUser;
        
    	FileOutputStream outStream = null;
    	InputStream inStreamForParsing = null;
    	
    	// initialize
    	payrollHeader = new HashMap();
    	lineNo = 0;
    	errArray = new NTBErrorArray();
    	errParameters = new ArrayList();
    	normalList = new ArrayList();
    	errList = new ArrayList();
    	normalTotalAmt = new BigDecimal("0");
    	errTotalAmt = new BigDecimal("0");
    	accountSet = new HashSet();
    	
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
	        XMLElement payrollDoc = BatchXMLFactory.getBatchXML("BAT_PAYROLL");
	        
	        String tableHeader = payrollDoc.getAttribute("table_header");
	        String tableDetail = payrollDoc.getAttribute("table_detail");

	        FileParser parser = new FileParser(payrollDoc, inStreamForParsing);
	        parser.setGenericJdbcDao(this.genericJdbcDao);

	        FileRecordProcessor pro = new MyRecordProcessor(tableDetail);
	        parser.parseRecord(pro);
	        int totalAmt = ((MyRecordProcessor) pro).getCount();
	        
            //no file header exception 20110623 by wen
	        if(!pro.isFileHeaderExists()){
	        	errArray.addError("err.bat.NoFileHeaderLine");
	        }
            //no batch header exception 20110623 by wen
	        if(!pro.isBatchHeaderExists()){
	        	errArray.addError("err.bat.NoBatchHeaderLine");
	        }
            //no trailer exception 20110623 by wen
	        if(!pro.isTrailerExists()){
	        	errArray.addError("err.bat.NoTrailerLine");
	        }
	        
	        //operation when file header & batch header exists 20110624 by wen
	        if(pro.isFileHeaderExists()&&pro.isBatchHeaderExists()){
	        	 // check consistency
		        BigDecimal totalAmtInFile = new BigDecimal(payrollHeader.get("TOTAL_AMOUNT").toString());
		        if (totalAmtInFile.compareTo(normalTotalAmt.add(errTotalAmt)) != 0) {
	    			errArray.addError("err.bat.TotalAmtConsistencyError");
		        }
		        int totalNoInFile = Integer.parseInt(payrollHeader.get("TOTAL_NUMBER").toString());
		        if (totalNoInFile !=  normalList.size() + errList.size()) {
	    			errArray.addError("err.bat.TotalNoConsistencyError");
		        }
	        }

	        //add by gzy 20180118
	        String fileName2 = this.getFileName();
			File saveFile2 = new File(saveFilePath + fileName2);
	        
	        // дPAYROLL��
        	if (errArray.size() == 0) {
        		payrollHeader.put("PAYROLL_ID", payrollId);
        		payrollHeader.put("FILE_NAME", fileName2);//mod by linrui 20180306
        		payrollHeader.put("CORP_ID", corpUser.getCorpId());
        		payrollHeader.put("USER_ID", corpUser.getUserId());
        		payrollHeader.put("REMARK", "");
        		payrollHeader.put("REQUEST_TIME", new Date());
        		payrollHeader.put("EXECUTE_TIME", null);
        		payrollHeader.put("AUTH_STATUS", Constants.AUTH_STATUS_SUBMITED);
        		payrollHeader.put("OPERATION", Constants.OPERATION_NEW);
        		payrollHeader.put("TOTAL_NUMBER", new Integer(payrollHeader.get("TOTAL_NUMBER").toString()));
        		payrollHeader.put("BATCH_RESULT", "");
        		
        		payrollHeader.remove("RECORD_ID");
        		payrollHeader.remove("FH");
        		payrollHeader.remove("BH");
                
        		// �޴����¼��дPAYROLL��
        		if (errList.size() == 0) {
            		addPayrollByJdbc(tableHeader, payrollHeader);
        		} else {
        			cancelUpload(payrollId, outStream, inStreamForParsing, saveFile);
        		}
        	} else  {
    			Log.error("parse head file error");
    			if(errParameters.size() > 0){
    				throw new NTBException(errArray, errParameters.toArray());
    			} else {
            		throw new NTBException(errArray);
    			}
        	}
	        
	        payrollFileInfo.setNormalList(normalList);
	        payrollFileInfo.setErrList(errList);
	        payrollFileInfo.setNormalTotalAmt(normalTotalAmt);
	        payrollFileInfo.setErrTotalAmt(errTotalAmt);
	        payrollFileInfo.setAllCount(totalAmt);
	        payrollFileInfo.setPayrollHeader(payrollHeader);
            //add by gzy 20180118
			writePayRollFile(payrollFileInfo,  saveFile2);
	        
	        return payrollFileInfo;
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			cancelUpload(payrollId, outStream, inStreamForParsing, saveFile);
			if (e instanceof NTBException) {
				throw (NTBException)e;
			} else {
				throw new NTBException(ErrConstants.GENERAL_ERROR);
			}
		}
	}
	

	/*
	 * ���˴�д������
	 */
	public void cancelUpload(String payrollId, OutputStream outStream,InputStream inStreamForParsing, File saveFile) {
		try {
			if(!payrollId.equals(""))
    			clearUnavailableDataByPayrollId(payrollId);
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
        Map recordDetail = null;
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
            	
            	fileHeaderExists = true; // 20110623 by wen
            	
            	fileHeader = (Map) recordData.get("FH");
            	// validate file header
            	validateFH(fileHeader);
                return FileRecordProcessor.FILE_RECORD_NOPROCESS;
                
            } else if (FileRecordProcessor.RECORD_BATCH_HEADER.equals(recordId)) {
            	
            	batchHeaderExists = true; // 20110623 by wen
            	
                batchHeader = (Map) recordData.get("BH");
                // validate batch header
                validateBH(batchHeader);
                return FileRecordProcessor.FILE_RECORD_NOPROCESS;
                
            } else if (FileRecordProcessor.RECORD_FILE_END.equals(recordId)) {
            	
            	trailerExists = true; // 20110623 by wen
            	
                return FileRecordProcessor.FILE_RECORD_STOP;
                
            }
            
            if(fileHeaderExists&&batchHeaderExists){ // check batch detail when file header & batch header eixts 20110624 by wen
            	
            	if (FileRecordProcessor.RECORD_BATCH_DETAIL.equals(recordId)) {
                    recordDetail = (Map) recordData.get("BD");
                    //validate batch detail
                    validateBD(recordData, recordDetail);
                    recordData.put("DETAIL_RESULT", "");
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
    		String groupId = "";
    		String startDate = "";
    		String endDate = "";
            
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
        		if (keyName.equals("ORIGINATOR_AC_NO")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("ORIGINATOR_AC_NO is empty");
            			errArray.addError("err.bat.DebitAccountIsEmpty");
            			continue;
            		}
            		keyObj = Utils.removePrefixZero(keyObj.toString().trim());
                    recordDetail.put(keyName, keyObj);
                	fromAcc = keyObj.toString();
        			// �ʺ��Ƿ����
        			if (corpAccountDao.load(CorpAccount.class, fromAcc) == null) {
            			Log.error("ORIGINATOR_AC_NO is not exist");
            			errArray.addError("err.bat.DebitAccountNotExist");
            			continue;
        			} else if (!isBelongToAccList(fromAcc)) {
            			Log.error("ORIGINATOR_AC_NO must belong to the account list");
            			errArray.addError("err.bat.DebitAccountNotBelongTo");
            			continue;
        			} else if (fromAcc.length() > 12) {//mod by linrui 20180201
            			Log.error("ORIGINATOR_AC_NO length error");
        				//���Ȳ��ܳ���10λ
            			errArray.addError("err.bat.DebitAccountLengthError");
            			continue;
        			}
        			
        		} else if (keyName.equals("GROUP_ID")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("GROUP_ID is empty");
            			errArray.addError("err.bat.GroupIdIsEmpty");
            			continue;
            		}
            		groupId = keyObj.toString().trim();
        		} else if (keyName.equals("START_VALUE_DATE")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("START_VALUE_DATE is empty");
            			errArray.addError("err.bat.StartValueDateIsEmpty");
            			continue;
            		}
        			startDate = keyObj.toString().trim();
        			Date _startDate = null;
                    // START VALUE DATE �Ƿ�Ϊ}�����պ�
                    // modified by hjs 20090511
                    int workingDays = 0;
                    try {
                    	_startDate = DateTime.getDateFromStr(startDate, "yyMMdd");
                    	if(fromAcc == null || "".equals(fromAcc)){
                    		fromAcc = Utils.removePrefixZero(Utils.null2EmptyWithTrim(recordDetail.get("ORIGINATOR_AC_NO")));
                    	}
                    	if(groupId == null || "".equals(groupId)){
                    		groupId = Utils.removePrefixZero(Utils.null2EmptyWithTrim(recordDetail.get("GROUP_ID")));
                    	}
        				workingDays = checkStartValueDate(corpUser.getCorpId(), fromAcc, groupId, _startDate); 
        				if (workingDays == 1) {
        					Log.error("START_VALUE_DATE must be later than the day after 1 working day");
        					errArray.addError("err.bat.StartValueDateError1");
//        					errParameters.add(new Integer(workingDays));
        					
        				} else if (workingDays == 2) {
        					Log.error("START_VALUE_DATE must be later than or equal to the day after 2 working days");
        					errArray.addError("err.bat.StartValueDateError2");
//        					errParameters.add(new Integer(workingDays));
        					
        				} else if (workingDays == -1){
        					throw new NTBException(ErrConstants.GENERAL_ERROR);
        					
        				} else if (workingDays == -2){
            				Log.error("START_VALUE_DATE should not be a holiday");
            				errArray.addError("err.bat.StartValueDateShouldNotBeHoliday");
        				}
        				
        			} catch (Exception e) {
        				if(workingDays == -1){
        					throw e;
        				}
        				Log.error("START_VALUE_DATE format error");
        				errArray.addError("err.bat.StartValueDateFormatError");
        			}
        			
        		} else if (keyName.equals("END_VALUE_DATE")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("END_VALUE_DATE is empty");
            			errArray.addError("err.bat.EndValueDateIsEmpty");
            			continue;
            		}
        			endDate = keyObj.toString().trim();
        			try {
        				//�Ƿ����ڸ�ʽ
        	            (new SimpleDateFormat("yyMMdd")).parse(keyObj.toString().trim());
        			} catch (Exception e) {
            			Log.error("END_VALUE_DATE format error");
            			errArray.addError("err.bat.EndValueDateFormatError");
            			continue;
        			}
        		} else if (keyName.equals("VOLUME_NO")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("VOLUME_NO is empty");
            			errArray.addError("err.bat.VolumeNoIsEmpty");
            			continue;
            		} 
        		}
//            	else {
//        			Log.error("Unknown filed error");
//        			errArray.addError("err.bat.UnknownFiledError");
//        			continue;
//        		}
            }
            
            //�ж��Ƿ����ύ��ý���
            isDoable(corpUser.getCorpId(), fromAcc, groupId, startDate);
            
            // �Ƿ�END VALUE DATE����START VALUE DATE
            if (!startDate.equals(endDate)) {
    			Log.error("START_VALUE_DATE is different from END_VALUE_DATE");
    			errArray.addError("err.bat.StartValueDateDifferentFromEndValueDate");
            }
            
            // Jet marked 2009-02-11
            //check unique
//            if (!checkUnique(fromAcc, groupId, startDate)) {
//    			Log.error("Check payroll record unique failed");
//    			errArray.addError("err.bat.CheckUniqueFailed");
//            }
            
            // �Ƿ�Ϊ�ѵǼ�payroll���˻�mod by linrui 20180201
            /*if (!checkValidation(corpUser.getCorpId(), fromAcc, groupId)) {
    			Log.error("Check payroll validation failed");
    			errArray.addError("err.bat.CheckValidationFailed");
            }*/
        	
        	payrollHeader.putAll(recordDetail);
        }
        
        /**
         * BH : Batch Header
         * @param recordDetail
         * @throws Exception
         */
        private void validateBH(Map recordDetail) throws Exception {
    		
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
        		if (keyName.equals("BATCH_NO")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("BATCH_NO is empty");
            			errArray.addError("err.bat.BatchNoIsEmpty");
            			continue;
            		} 
        		} else if (keyName.equals("BATCH_DATE")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("BATCH_DATE is empty");
            			errArray.addError("err.bat.BatchDateIsEmpty");
            			continue;
            		}
        			try {
        				//�Ƿ����ڸ�ʽ
        	            (new SimpleDateFormat("yyMMdd")).parse(keyObj.toString().trim());
        			} catch (Exception e) {
            			Log.error("BATCH_DATE format error");
            			errArray.addError("err.bat.BatchDateFormatError");
            			continue;
        			}
        		} else if (keyName.equals("TOTAL_NUMBER")) {
            		if (keyObj.toString().trim().equals("")) {
            			Log.error("TOTAL_NUMBER is empty");
            			errArray.addError("err.bat.TotalNoIsEmpty");
            			continue;
            		}
        			try {
        				//�Ƿ�Ϊ����
        				Integer.parseInt(keyObj.toString().trim());
        			} catch (Exception e) {
            			Log.error("TOTAL_NUMBER format error");
            			errArray.addError("err.bat.TotalNoFormatError");
            			continue;
        			}
        		} else if (keyName.equals("TOTAL_AMOUNT")) {
            		if (keyObj == null) {
            			Log.error("TOTAL_AMOUNT is empty");
            			errArray.addError("err.bat.TotalAmountIsEmpty");
            			continue;
            		}
        			try {
        				//�Ƿ�Ϊ���
        				new BigDecimal(keyObj.toString().trim());
        			} catch (Exception e) {
            			Log.error("TOTAL_NUMBER format error");
            			errArray.addError("err.bat.TotalAmountFormatError");
            			continue;
        			}
        		} 
//        		else {
//        			Log.error("Unknown filed error");
//        			errArray.addError("err.bat.UnknownFiledError");
//        			continue;
//        		}
            }

            payrollHeader.putAll(recordDetail);
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
        		if (keyName.equals("TO_ACCOUNT")) {
        			keyObj = Utils.removePrefixZero(keyObj.toString().trim());
            		recordData.put("TO_ACCOUNT", keyObj);
            		
            		if (keyObj.toString().equals("")) {
            			errType += "to_acc_empty,";
            			errFlag = true;
            			continue;
            		}
        			if (keyObj.toString().length() > 12) {
            			errType += "to_acc_length_err,";
            			errFlag = true;
            			continue;
        			}
        		} else if (keyName.equals("REFERENCE_NO")) {
            		if (keyObj.toString().trim().equals("")) {
            			errType += "ref_no_empty,";
            			errFlag = true;
            			continue;
            		} 
        		} else if (keyName.equals("CREDIT_AMOUNT")) {
            		if (keyObj == null) {
            			errType += "to_amt_err,";
            			errFlag = true;
            			continue;
            		}
            		amount = (BigDecimal) keyObj;
        		} 
//        		else {
//        			errType += "unknown,";
//        			errFlag = true;
//        			continue;
//        		}
            }
            //modified by nabai, 2010-10-12, �޸��ʺ��ظ�����ΪTO_ACCOUNT + REFERENCE_NO��
            String uniqueKey = (String)recordData.get("TO_ACCOUNT") +  (String)recordData.get("REFERENCE_NO");
			if (accountSet.contains(uniqueKey)) {
    			errType += "to_acc_duplicated,";
    			errFlag = true;
			} else {
				accountSet.add(uniqueKey);
			}
			
            recordData.put("TRANS_ID", CibIdGenerator.getIdForBatchRecord("PAYROLL"));
            recordData.put("PAYROLL_ID", payrollId);
            recordData.put("CORP_ID", corpUser.getCorpId());
            recordData.put("FROM_ACCOUNT", this.fromAcc);
            recordData.put("REMARK", "");
            recordData.put("EXECUTE_TIME", new Date());
            recordData.put("LINE_NO", new Integer(++lineNo));
            
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
    
    public void clearUnavailableDataByCorpId(String corpId) throws NTBException {
    	String hql_payroll = "from Payroll as p where p.status is null and p.corpId = ?";
    	String hql_payroll_rec = "from PayrollRec as pr where pr.status is null and pr.corpId = ?";
    	
    	List list = this.payrollDao.list(hql_payroll, new Object[]{corpId});
    	Payroll payroll = null;
    	for(int i=0; i<list.size(); i++) {
    		payroll = (Payroll) list.get(i);
    		(new File(saveFilePath + payroll.getFileName())).delete();
    		this.payrollDao.delete(payroll);
    	}
		this.payrollRecDao.delete(hql_payroll_rec, new Object[]{corpId});
    }
    
    public void clearUnavailableDataByPayrollId(String payrollId) throws NTBException {
    	String hql_payroll = "from Payroll as p where p.status is null and p.payrollId = ?";
    	String hql_payroll_rec = "from PayrollRec as pr where pr.status is null and pr.payrollId = ?";
    	
		this.payrollRecDao.delete(hql_payroll, new Object[]{payrollId});
		this.payrollRecDao.delete(hql_payroll_rec, new Object[]{payrollId});
    }
	
	public Payroll viewAvaliablePayroll(String payrollId) throws NTBException {
		String hql = "from Payroll as pr where pr.payrollId = ? and pr.status is not null";
		List list = this.payrollDao.list(hql, new Object[]{payrollId});
		return (Payroll) list.get(0);
	}
	
	public List listAvaliablePayrollRec(String payrollId) throws NTBException {
		payrollId = Utils.null2EmptyWithTrim(payrollId);
		
		String sql = "select * from PAYROLL_REC where PAYROLL_ID = ? and STATUS is not null and PROBLEM_TYPE = ''";
		List list;
		try {
			list = this.genericJdbcDao.query(sql, new Object[]{payrollId});
			return list;
		} catch (Exception e) {
			Log.error("PayrollService.listAvaliablePayrollRec error", e);
			throw new NTBException("err.bat.ListPayrollRecordError");
		}
	}
	
	public List listUploadErrorDetailRec(String payrollId) throws NTBException {
		return this.payrollRecDao.listUploadErrorDetailRec(payrollId);
	}
	
	public Payroll getLatestPayroll(String corpId) throws NTBException  {
		String hql ="";
		List list = null;
		hql = "from Payroll as pr where pr.corpId = ? and pr.status = ? and pr.authStatus = ? order by pr.startValueDate desc";
		list = this.payrollDao.list(hql, 
				new Object[]{corpId, Constants.STATUS_NORMAL, Constants.AUTH_STATUS_COMPLETED});
		Payroll payroll = null;
		if (list.size() > 0) {
			payroll = (Payroll) list.get(0);
			return payroll;
		} else {
			return new Payroll();
		}
	}

	public void updateStatus(String payrollId) throws NTBException {
		String SQL_UPDATE_PAYROLL = 
			"update PAYROLL set status = ? where PAYROLL_ID = ? and STATUS is null";
		String SQL_UPDATE_PAYROLL_RECORD = 
//			"update PAYROLL_REC set status = ? where PAYROLL_ID = ? and STATUS is null and PROBLEM_TYPE = ''";
		"update PAYROLL_REC set status = ? where PAYROLL_ID = ? and STATUS is null and PROBLEM_TYPE is null";//update by linrui 20180319
		String SQL_UPDATE_PAYROLL_ERRPR_RECORD = 
//			"update PAYROLL_REC set status = ? where PAYROLL_ID = ? and STATUS is null and PROBLEM_TYPE <> ''";
		"update PAYROLL_REC set status = ? where PAYROLL_ID = ? and STATUS is null and length(PROBLEM_TYPE)>0 ";//update by linrui 20180319
		
		try {
			genericJdbcDao.update(SQL_UPDATE_PAYROLL, 
					new Object[]{Constants.STATUS_PENDING_APPROVAL, payrollId});
			
			genericJdbcDao.update(SQL_UPDATE_PAYROLL_RECORD, 
					new Object[]{Constants.STATUS_PENDING_APPROVAL, payrollId});
			
			genericJdbcDao.update(SQL_UPDATE_PAYROLL_ERRPR_RECORD, 
					new Object[]{Constants.STATUS_REMOVED, payrollId});
		} catch (Exception e) {
			throw new NTBException("err.bat.UpdatePayrollError");
		}
	}
	
	public void approvePayroll(Payroll payroll, CorpUser corpUser, BigDecimal equivalentMOP) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
        TransferLimitService transferLimitService = (TransferLimitService)appContext.getBean("TransferLimitService");
        UtilService utilService = (UtilService)appContext.getBean("UtilService");
        
		payroll.setStatus(Constants.STATUS_NORMAL);
		payroll.setBatchResult("P");
		payroll.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
		payroll.setExecuteTime(new Date());
		payrollDao.update(payroll);
		
		String sql_approvel_payroll_record = 
			"update PAYROLL_REC set STATUS = ?, DETAIL_RESULT = ? where PAYROLL_ID = ? and STATUS is not null";
		try {
			this.genericJdbcDao.update(sql_approvel_payroll_record,
					new Object[]{Constants.STATUS_NORMAL, "P", payroll.getPayrollId()});
		} catch (Exception e) {
			Log.error("Approve payroll error", e);
			throw new NTBException("err.bat.ApprovePayrollError");
		}
		
		// add Limit
		transferLimitService.addUsedLimitQuota(payroll.getOriginatorAcNo(),
				corpUser.getCorpId(), 
				Constants.TXN_TYPE_PAYROLL,
				payroll.getTotalAmount().doubleValue(), equivalentMOP.doubleValue());
		
		//upload file to host
		//update by linrui 20180201
		utilService.uploadFileToHost("PYRL", new File(saveFilePath + payroll.getFileName()));
		
	}
	
	public void rejectPayroll(Payroll payroll) throws NTBException {
		payroll.setStatus(Constants.STATUS_REMOVED);
		payroll.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
		payroll.setExecuteTime(new Date());
		payrollDao.update(payroll);
		
		String sql_reject_payroll_record = 
			"update PAYROLL_REC set status = ? where PAYROLL_ID = ? and status is not null";
		try {
			this.genericJdbcDao.update(sql_reject_payroll_record,
					new Object[]{Constants.STATUS_REMOVED, payroll.getPayrollId()});
		} catch (Exception e) {
			throw new NTBException("err.bat.RejectPayrollError");
		}
		
		//delete file
		File file = new File(saveFilePath + payroll.getFileName());
		if(file.exists()){
			file.delete();
		}
	}
	
	public OutputStream getOutputStreamByPreFile(Payroll payroll) throws NTBException {
		
		try {
			String fileName = this.getFileName();
			ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
			OutputStreamWriter osw = new OutputStreamWriter(baos);
			BufferedWriter bw = new BufferedWriter(osw);
			
			FileReader fr = new FileReader(saveFilePath + payroll.getFileName());
			BufferedReader br = new BufferedReader(fr);
			String lineStr = "";
			char headerChar;
			loop:
				do {
					lineStr = br.readLine();
					if (lineStr != null) {
						headerChar = lineStr.charAt(0);
						switch (headerChar) {
						case 'A': {
							lineStr = replaceFH(lineStr, payroll.getStartValueDate());
							bw.write(lineStr);
							bw.newLine();
							break;
						}
						case 'H': {
							lineStr = replaceBH(lineStr, payroll.getBatchDate());
							bw.write(lineStr);
							bw.newLine();
							break;
						}
						case 'D': {
							bw.write(lineStr);
							bw.newLine();
							break;
						}
						case 'Z': {
							bw.write(lineStr);
							break loop;
						}
						default: {
							throw new NTBException("err.bat.ReadPreviousFileError");
						}
						}
					}
				} while (lineStr != null);
			fr.close();
			bw.close();
			
			payroll.setFileName(fileName);
			return baos;
			
		} catch (FileNotFoundException e) {
			throw new NTBException("err.bat.GetPreviousFileError");
		} catch (IOException e) {
			throw new NTBException("err.bat.GetPreviousFileError");
		}
	}
	
	private String replaceFH(String lineStr, String payDate) throws NTBException {
		lineStr = lineStr.substring(0, 16) + payDate + payDate + lineStr.substring(28);
		return lineStr;
	}
	
	private String replaceBH(String lineStr, String batchDate) throws NTBException {
		lineStr = lineStr.substring(0, 4) + batchDate + lineStr.substring(10);
		return lineStr;
	}
	
	public void addPayrollByPreFile(Payroll payroll, List payrollRecList, OutputStream newFileOutStream) throws NTBException {
		
		File saveFile = new File(saveFilePath + payroll.getFileName());
		OutputStream outStream = null;
        try {
        	// add payroll
    		this.payrollDao.add(payroll);
    		
    		List tmpList = new ArrayList();
    		for (int i=0; i<payrollRecList.size(); i++) {
    			Map tmpMap = (Map) payrollRecList.get(i);
    			tmpMap.put("LINE_NO", new Integer(i+1));
    			tmpList.add(tmpMap);

                //��д��ݿ�
                if (tmpList.size() >= 200) {
    				genericJdbcDao.batchAdd("PAYROLL_REC", tmpList);
                    tmpList.clear();
                }
    		}
    		if (tmpList.size() > 0) {
        		genericJdbcDao.batchAdd("PAYROLL_REC", tmpList);
    		}

    		outStream = new FileOutputStream(saveFile);
    		((ByteArrayOutputStream) newFileOutStream).writeTo(outStream);
    		outStream.close();
    		
		} catch (Exception e) {
			this.cancelUpload(payroll.getPayrollId(), outStream, null, saveFile);
			Log.error("confirm the use pervious file transaction error", e);
			throw new NTBException("err.bat.AddPayrollByPreFileError");
		} finally {
			try {
				newFileOutStream.close();
			} catch (IOException e) {
				Log.warn("close new file error", e);
			}
		}
	}
	
	private String getFileName() {
		return Utils.replaceStr(uploadFileName, "[datetime]", DateTime.formatDate(new Date(), "ddHHmmss"))+".TXT";//mod by linrui 20180306
	}
	
	public void isDoable(String corpId, String fromAcc, String groupId, String startValueDate) throws NTBException {
		/*
		Date d = new Date();
		Timestamp dateStart = DateTime.getTimestampByStr(DateTime.formatDate(d, "yyyy-MM-dd"), true);
		Timestamp dateEnd = DateTime.getTimestampByStr(DateTime.formatDate(d, "yyyy-MM-dd"), false);
		*/
		String hql = "from Payroll as pr where pr.corpId = ? ";
//		hql += " and pr.originatorAcNo = ? and pr.groupId = ? and pr.startValueDate = ? and pr.status is not null";
		//Jet update for select STATUS_NORMAL and STATUS_PENDING_APPROVAL
		hql += " and pr.originatorAcNo = ? and pr.groupId = ? and pr.startValueDate = ? and (pr.status = '0' or pr.status = '1')";
		
		List list = this.payrollDao.list(hql, new Object[]{corpId, fromAcc, groupId, startValueDate});
		
		if (list.size() > 0) {
			throw new NTBException("err.bat.TransHadExecutedToday");
		}
	}

	public List listHistory(String corpId, String userId, String dateFrom, String dateTo) throws NTBException {
		List list = this.payrollDao.listHistory(corpId, userId, dateFrom, dateTo);
		return list;
	}

	public boolean checkValidation(String corpId, String accountNo, String groupId) throws NTBException {
		corpId = corpId.trim();
		accountNo = accountNo.trim();
		groupId = groupId.trim();
		
		String sql = "select * from HS_PAYROLL_INFO where CORP_ID = ? and DEBIT_ACCOUNT = ? and GROUP_ID = ?";
		
		try {
			List list = genericJdbcDao.query(sql, new Object[]{corpId, accountNo, groupId});
			return list==null ? false : list.size()>0;
		} catch (Exception e) {
			throw new NTBException("err.bat.CheckPayrollValidationError");
		}
	}

	/**
	 * @param corpId
	 * @param accountNo
	 * @param groupId
	 * @param startDate
	 * @return 0��ʾ���ͨ��; -1��ʾ���쳣; -2��ʾaccount flagΪN��payroll dateΪ��; ����0��ֵ��ʾpayroll dateδ����ָ���Ĺ�����
	 * @throws NTBException
	 */
	public int checkStartValueDate(String corpId, String accountNo, String groupId, Date startDate) throws NTBException {
		UtilService utilService = (UtilService) Config.getAppContext().getBean("UtilService");
		int workingDays = 1;//0 mod by linrui 20180201
		corpId = corpId.trim();
		accountNo = accountNo.trim();
		groupId = groupId.trim();
		String accountFlag = "";
		
		/*//mod by linrui 20180201
		 String sql = "select ACCOUNT_FLAG from HS_PAYROLL_INFO  where CORP_ID = ? and DEBIT_ACCOUNT = ? and GROUP_ID = ? ";
		try {			
		  accountFlag = (String) genericJdbcDao.querySingleValue(sql, new Object[]{corpId, accountNo, groupId});
		} catch (Exception e) {
			Log.error("Error getting ACCOUNT_FLAG from table 'HS_PAYROLL_INFO'", e);
			return -1; // exception
		}
		
		if(accountFlag == null){
			Log.error("AccountFlag not found with corpId=" + corpId + "; accountNo=" + accountNo + "; groupId=" + groupId + ";");
			return -1; // exception
		}
		
		if("Y".equalsIgnoreCase(accountFlag)){ //check at least 1 working day in between
			workingDays = 1;
		} else if("N".equalsIgnoreCase(accountFlag)){//check at least 2 working days in between
			workingDays = 2;
			if(utilService.isHoliday(startDate)){
				return -2; // payroll date should not be a holiday when the Account Flag is 'N'
			}
		} else {
			Log.error("Error format of ACCOUNT_FLAG=" + accountFlag);
			return -1; // exception
		}*/
		
		try {
			// modified by hjs 20090511
//    		Date nextWorkingDay = new Date();
//    		for(int i=0; i<workingDays; i++){
//				nextWorkingDay = utilService.getNextWorkingDate(nextWorkingDay);
//    		}
    		
			// add by hjs 20090610
			Date nextWorkingDay = utilService.getNextWorkingDate(new Date());
    		if(workingDays == 1){
    			Calendar cal = Calendar.getInstance();
    			cal.setTime(nextWorkingDay);
    			cal.add(Calendar.DATE, 1);
    			nextWorkingDay = cal.getTime();
    		} else if(workingDays == 2){
    			nextWorkingDay = utilService.getNextWorkingDate(nextWorkingDay);
    		}
			
			if(DateTime.compareDate(startDate, nextWorkingDay) >= 0){
				return 0; //passed
			}
			
			return workingDays; // failed
			
		} catch (Exception e) {
			throw new NTBException("err.bat.checkStartValueDateError");
		}
	}

//	private boolean checkUnique(String fromAcc, String groupId, String startValueDate) throws NTBException {
//		String hql = "from Payroll as pr where pr.originatorAcNo = ? and pr.groupId = ? and pr.startValueDate = ?";
//		return payrollDao.list(hql, new Object[]{fromAcc.trim(), groupId.trim(), startValueDate.trim()}).size() <= 0 ;
//	}
	
	private boolean isBelongToAccList(String fromAcc) throws NTBException  {
		List accList = this.corpUser.getAccountList();
		CorpPermission obj = null;
		for(int i=0; i<accList.size();i++) {
			obj = (CorpPermission) accList.get(i);
			if(obj.getPermissionResource().trim().equals(fromAcc)) {
				return true;
			}
		}
		return false;
	}
	
	public Map calculate(Payroll payroll) throws NTBException {
		Map resultMap = new HashMap();
		if(null==payroll.getBatchResult()||payroll.getBatchResult().equals("P") || payroll.getBatchResult().equals("")){//mod by linrui
			resultMap.put(PayrollService.TOTAL_AMOUNT_ACCEPTED, "0");
			resultMap.put(PayrollService.TOTAL_NUMBER_ACCEPTED, "0");
			resultMap.put(PayrollService.TOTAL_AMOUNT_REJECTED, "0");
			resultMap.put(PayrollService.TOTAL_NUMBER_REJECTED, "0");
		} else {
			Object[] objs = (Object[]) this.payrollDao.calculate(payroll.getPayrollId()).get(0);
			
			BigDecimal totalAmountAccepted = objs[0]==null? new BigDecimal("0") : new BigDecimal(objs[0].toString());
			int totalNumberAccepted = (new Integer(objs[1].toString()).intValue());
			BigDecimal totalAmountRejected = new BigDecimal(payroll.getTotalAmount().toString()).subtract(totalAmountAccepted);
			int totalNumberRejected = payroll.getTotalNumber().intValue() - totalNumberAccepted;

			resultMap.put(PayrollService.TOTAL_AMOUNT_ACCEPTED, totalAmountAccepted);
			resultMap.put(PayrollService.TOTAL_NUMBER_ACCEPTED, String.valueOf(totalNumberAccepted));
			resultMap.put(PayrollService.TOTAL_AMOUNT_REJECTED, totalAmountRejected);
			resultMap.put(PayrollService.TOTAL_NUMBER_REJECTED, String.valueOf(totalNumberRejected));
		}
		return resultMap;
	}

//add by linrui 20180303
	private void writePayRollFile(PayrollFileBean payrollFileInfo,
			File saveFile2) throws Exception  {
		// TODO Auto-generated method stub
		Map fileRequest = payrollFileInfo.getPayrollHeader();
		List normalList = payrollFileInfo.getNormalList();
		//String fileName = this.getFileName();
		//File saveFile = new File(saveFilePath + fileName);

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(saveFile2));
		
			// File Header
			String valDate, ccy, amt, dr_Ac, dr_ccy, cr_Ac, cr_ccy, remark, narr, oth_Ac, oth_Nm, oth_Bk;
			String dacType = "D"; // D活期
			String cacType = "D";
			String dr_Br = Utils.prefixZero("",11);
			String cr_Br = Utils.prefixZero("",11);
			// data from header
			valDate = DateTime.formatDate(new Date(), "yyyyMMdd");
//			ccy = Utils.prefixSpace(payrollHeader.get("TO_CURRENCY").toString(),3); // transfer_ccy
//			dr_Ac = Utils.appendSpace(payrollHeader.get("FROM_ACCOUNT")
//					.toString(),32); // from_acct
//			dr_ccy = Utils.prefixSpace(fileRequest.get("TO_CURRENCY")
//					.toString(),3); // from_ccy
//			cr_ccy = Utils.prefixSpace(fileRequestHeader.get("TO_CURRENCY")
//					.toString(),3); // to_ccy
			//update by linrui 20180119
			// from_ccy
//			dr_ccy = Utils.prefixSpace(getCcyByAcct(payrollHeader.get("FROM_ACCOUNT").toString()),3);			
			//end
			narr = Utils.appendSpace("", 72);
			oth_Ac = Utils.appendSpace("", 32);
			oth_Nm = Utils.appendSpace("", 122);
			oth_Bk = Utils.appendSpace("", 14);
			
			Map row = null;
			for (int i = 0; i < normalList.size(); i++) {
				// data from detail
				row = (Map) normalList.get(i);
				ccy = Utils.prefixSpace(getCcyByAcct(row.get("TO_ACCOUNT").toString()),3); // transfer_ccy
				amt = Format.formatDecimal(row.get("CREDIT_AMOUNT").toString(),2); //update by gan 20180202
				amt = Utils.prefixZero(Utils.replaceStr(Utils.replaceStr(amt, ",", ""), ".", ""), 16); // 交易金额 trans_amt
				cr_Ac = Utils.appendSpace(row.get("TO_ACCOUNT")
						.toString(),32);// 贷方账号 toaccount
				// to_ccy	update by linrui 20180119
				cr_ccy =  Utils.prefixSpace(getCcyByAcct(row.get("TO_ACCOUNT").toString()),3);
				dr_Ac = Utils.appendSpace(row.get("FROM_ACCOUNT")
						.toString(),32);// 借方账号 fromaccount
				dr_ccy =  Utils.prefixSpace(getCcyByAcct(row.get("FROM_ACCOUNT").toString()),3);
				remark = Utils.prefixSpace(row.get("REMARK").toString(),120);// 				
				writer.write((row.get("TRANS_ID").toString()).substring(2) + dr_ccy + amt + dacType +  dr_Ac + dr_Br + cacType+     //CibIdGenerator.getTDRef()
						  cr_Ac +  cr_Br + remark + narr); //update by linrui 20180306
				writer.write("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NTBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			// finish, close stream!
			if(writer != null){
				writer.close();
			}
		}
	}
//add by linrui 20180310
	//search ccy from acct
	public String getCcyByAcct(String acct) throws NTBException {
     String sql = "select * from Corp_Account ca where ca.ACCOUNT_NO = ?";
     Map map = new HashMap();
     try{
     map = genericJdbcDao.querySingleRow(sql, new Object[] { acct });
     } catch (Exception e) {
			throw new NTBException("err.bat.RejectTransferBankSTMError");
		}
     return  map.get("CURRENCY").toString();
    }
//add by linrui 20180317
	public void downloadHisToRec(Payroll payroll)throws NTBException{
		Map toHost = new HashMap();
        Map fromHost = new HashMap();
        toHost.put("SERV_CD", "BSPCM01");
        toHost.put("AC_DATE", payroll.getAcDate());
        toHost.put("JRNNO", payroll.getJrnNo());
        CibTransClient testClient = new CibTransClient("CIB", "ZJ58");;
        fromHost = testClient.doTransaction(toHost);
        if(!testClient.isSucceed()){
            throw new NTBHostException(testClient.getErrorArray());
        }else{
        String fileName = fromHost.get("FILE_NAME").toString();
        /*String status = "S".equalsIgnoreCase(fromHost.get("BSP_STATUS").toString())?"S":"X";
        //step 1 update status to payroll table
        updatePayrollStatus(status, payroll.getPayrollId());
       
        //step 1 end*/
        //step 1 download fileName from host
          try {
        	FtpUtil.connectServer(Config.getProperty("MDBUploadUser"), Config.getProperty("MDBUploadPassword"),
        			Config.getProperty("MDBUploadServer"), 21, 10000);
        	Log.info("download payroll result file: "+fileName+" beginning");
			FtpUtil.DownloadFilesByName(Config.getProperty("BatchFileUploadDir"),
					Config.getProperty("MDBDownloadRemotePath"), fileName.trim());
			Log.info("download payroll result file: "+fileName+" finish");
		  }catch (Exception e) {
			e.printStackTrace();
		  }
        //step 1 end
		//step 2 update haddownload status
		  payroll.setHaddownHis("Y");
		  payrollDao.update(payroll);
		//step 2 end
        //step 2 parse and load data to payroll_rec
          parseRecFile(new File(saveFilePath + fileName.trim()));
        //step 2 end		
       }  
	}
//add by linrui 20180320
	public void updatePayrollStatus(String status, String payrollId) throws NTBException {
		try {
			this.genericJdbcDao.update(UPDATE_PAYROLL_STATUS, new Object[]{status, payrollId});
		} catch (Exception e) {
			Log.error("update payroll status error", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	//add by linrui 20180320
public void updatePayrollRecStatus(String status, String payrollId) throws NTBException {
		try {
			this.genericJdbcDao.update(UPDATE_PAYROLLREC_STATUS, new Object[]{status, payrollId});
		} catch (Exception e) {
			Log.error("update payroll_rec status error", e);
			throw new NTBException("err.sys.DBError");
		}
	}
//add by linrui 20180320
	public void parseRecFile(File file) throws NTBException {
        //check file exist
        if (!file.exists()) {
        	throw new NTBException("err.bat.FileExistError");
        } 	
    	this.corpUser = corpUser;     
    	FileOutputStream outStream = null;
    	InputStream inStreamForParsing = null;  	
    	// initialize    	
    	List payRollRecList = new ArrayList();   	
		try {
			inStreamForParsing = new FileInputStream(file);
	        XMLElement payrollDoc = BatchXMLFactory.getBatchXML("PAYROLL_REC");	        
//	        XMLElement payrollDoc = BatchXMLFactory.getBatchXML("D:/mdb_eb/cib/WebContent/WEB-INF/batchfilexml/PAYROLL_REC.xml");	        
	        String tableDetail = payrollDoc.getAttribute("table");
	        FileParser parser = new FileParser(payrollDoc, inStreamForParsing);
	        parser.setGenericJdbcDao(this.genericJdbcDao);

	        FileRecordProcessor pro = new MyRecordProcessor(tableDetail);
	        payRollRecList = parser.parseRecRecord(pro);
	        for(int i =0 ; i< payRollRecList.size() ;i++){
	        	Map payRollRecMap = new HashMap();
	        	payRollRecMap = (Map)payRollRecList.get(i);
	        	String tranId = "BT"+payRollRecMap.get("TRANS_ID").toString();
	        	updatePayrollRecStatus("S".equalsIgnoreCase(payRollRecMap.get("STATUS").toString())?"0"
	        			:payRollRecMap.get("STATUS").toString()
	        			,tranId);
	        }
		} catch (Exception e) {			
			if (e instanceof NTBException) {
				throw (NTBException)e;
			} else {
				throw new NTBException(ErrConstants.GENERAL_ERROR);
			}
		}
	}
	public static void main(String[] args) {
		File file = new File("D:/batchupload/BSPCM01-200108-000002002-0000");
		PayrollServiceImpl aImpl = new PayrollServiceImpl();
		try {
			aImpl.parseRecFile(file);
		} catch (NTBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
