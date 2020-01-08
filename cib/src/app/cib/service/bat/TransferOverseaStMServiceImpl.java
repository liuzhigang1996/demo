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
import java.math.BigInteger;
import java.net.InetAddress;
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

import app.cib.bo.bat.FileRequest;
import app.cib.bo.bat.FileRequestFileBean;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferOversea;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.dao.srv.RequestDao;
import app.cib.service.srv.RequestService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.txn.TransferService;
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
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.xml.XMLElement;

public class TransferOverseaStMServiceImpl implements TransferOverseaStMService{
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
    	isFileHeaderOk = false;
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
	        XMLElement payrollDoc = BatchXMLFactory.getBatchXML("TRANSFER_OVERSEA_STM");
	        
	        String tableHeader = payrollDoc.getAttribute("table_header");
	        String tableDetail = payrollDoc.getAttribute("table_detail");

	        FileParser parser = new FileParser(payrollDoc, inStreamForParsing);
	        parser.setGenericJdbcDao(this.genericJdbcDao);

	        FileRecordProcessor pro = new MyRecordProcessor(tableDetail);
	        parser.parseRecord(pro);
	        int totalAmt = ((MyRecordProcessor) pro).getCount();
	        // check consistency
	        
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
	        	BigDecimal totalAmtInFile = (BigDecimal) (fileRequestHeader.get("TO_AMOUNT"));
		        if ( totalAmtInFile.compareTo(normalTotalAmt.add(errTotalAmt)) !=0 ) {
	    			cancelUpload(BATCH_ID, outStream, inStreamForParsing, saveFile);
		        }

		        int totalNoInFile = Integer.parseInt(fileRequestHeader.get("TOTAL_NUMBER").toString());
		        if (totalNoInFile !=  normalList.size() + errList.size()) {
	    			cancelUpload(BATCH_ID, outStream, inStreamForParsing, saveFile);
		        }
	        }
	      //add by linrui 20180125
	        String fileName2 = this.getToHostFileName();
			File saveFile2 = new File(saveFilePath + fileName2);
			//end
	        // 写FILRE_QEQUEST锟斤拷
        	if (errArray.size() == 0) {
        		fileRequestHeader.put("BATCH_ID", BATCH_ID);
        		fileRequestHeader.put("FILE_NAME", fileName2);//mod by linrui 20180125
        		fileRequestHeader.put("BATCH_TYPE", FileRequest.TRANSFER_OVERSEA_STM);
        		fileRequestHeader.put("CORP_ID", corpUser.getCorpId());
        		fileRequestHeader.put("USER_ID", corpUser.getUserId());
        		fileRequestHeader.put("REMARK", "");
        		fileRequestHeader.put("REQUEST_TIME", new Date());
        		fileRequestHeader.put("EXECUTE_TIME", null);
        		fileRequestHeader.put("AUTH_STATUS", Constants.STATUS_PENDING_APPROVAL);
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
        		throw new NTBException(errArray);
        	}
	        
        	fileRequestFileBean.setNormalList(normalList);
        	fileRequestFileBean.setErrList(errList);
        	fileRequestFileBean.setNormalTotalAmt(normalTotalAmt.doubleValue());
        	fileRequestFileBean.setErrTotalAmt(errTotalAmt.doubleValue());
        	fileRequestFileBean.setAllCount(totalAmt);
        	fileRequestFileBean.setFileRequestHeader(fileRequestHeader);
        	//update by linrui 20180117
        	writeOverStmFile(fileRequestFileBean,  saveFile2);
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
        	CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
        	
    		boolean errFlag = false;
            //锟叫断革拷锟斤拷锟斤拷欠锟斤拷锟斤拷峤� add by mxl 1225
    		if ( requestService.listFileRequestBythreekeys(recordDetail.get("CORP_ID").toString(),recordDetail.get("ORDER_DATE").toString(),recordDetail.get("BATCH_REFERENCE").toString(),FileRequest.TRANSFER_OVERSEA_STM).size()>0) {
    			errArray.addError("err.bat.TheUploadBatchFileHasBeenSubmitted");
    		}      		
            Set keySet = recordDetail.keySet();
            
			//Jet added, check currency of from account must equal to transfer currency
//			String Currency_acount = null;
//			String Currency_transfer = null;

            for (Iterator it = keySet.iterator(); it.hasNext();) {
				String keyName = (String) it.next();
				Object keyObj = recordDetail.get(keyName);
				if (keyObj instanceof String) {
					keyObj = new String(((String) keyObj).getBytes(), Config
							.getProperty("DBCharset")).trim();
				}
				recordDetail.put(keyName, keyObj);
				// check validate
				if (keyName.equals("FROM_ACCOUNT")) {
					fromAccount = keyObj.toString().trim();
					if (keyObj.toString().trim().equals("")) {
						errFlag = true;
						break;
					} else {
    					//modify by hjs 20070813
    					boolean flag = corpAccountService.checkAccountByUser(corpUser, fromAccount);
    					if (!flag) {
    						Log.error("FROM_ACCOUNT is not the privilege");
    						errArray.addError("err.bat.FromAccountBatchIsNotPrivilage");
    						continue;
    					}
						//Jet added, check currency of from account must equal to transfer currency
//						Currency_acount = corpAccountService.getCurrency(keyObj.toString());
					}

				} else if (keyName.equals("TO_AMOUNT")) {
					if (keyObj.toString().trim().equals("")) {
						Log.error("TO_AMOUNT is empty");
						errArray.addError("err.bat.ToAmountIsEmpty");
						continue;
					}
				} else if (keyName.equals("TOTAL_NUMBER")) {
					if (keyObj.toString().trim().equals("")) {
						Log.error("TOTAL_NUMBER is empty");
						errArray.addError("err.bat.TotalNumberIsEmpty");
						continue;
					}
				} else if (keyName.equals("TO_CURRENCY")) {
					if (keyObj.toString().trim().equals("")) {
						Log.error("To_CURRENCY is empty");
						errArray.addError("err.bat.ToCurrencyIsEmpty");
						continue;
//					} else {
						// Jet added, check currency of from account must equal to transfer currency
//						Currency_transfer = keyObj.toString();
					}
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
				} else if (keyName.equals("ORDER_DATE")) {
					if (keyObj.toString().trim().equals("")) {
						Log.error("ORDER_DATE is empty");
						errArray.addError("err.bat.OrderDateIsEmpty");
						continue;
					}
					try {
						// 锟角凤拷锟斤拷锟节革拷式
						(new SimpleDateFormat("yyyyMMdd")).parse(keyObj
								.toString().trim());
					} catch (Exception e) {
						Log.error("ORDER_DATE format error");
						errArray.addError("err.bat.OrderDateFormatError");
						continue;
					}

				} else if (keyName.equals("BATCH_STATUS")) {
					/*
					 * if (keyObj.toString().trim().equals("")) {
					 * Log.error("BATCH_STATUS is empty");
					 * errArray.addError("err.bat.BatchStatusIsEmpty");
					 * continue; }
					 */

				}
			} //end for
            
			// Jet added, check currency of from account must equal to transfer currency
//			if ((Currency_acount != null && Currency_transfer != null)
//					&& !Currency_acount.equals(Currency_transfer)) {
//				Log.error("Currency not equal error");
//				errArray.addError("err.bat.DebitCurrencyEqualError");
//			}

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
        	CorpAccountService corpAccountService = (CorpAccountService)appContext.getBean("CorpAccountService");
        	
    		boolean errFlag = false;
    		String errType = "";
    		BigDecimal amount = null;
    		String chargeAccout = null;
    		String beneficiaryCountry = null;
    		String toAccount = null;
    		String accountType = null;
    		
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
            		if (keyObj == null || keyObj.toString().trim().equals("")) {
            			errType += "to_acc_empty,";
            			errFlag = true;
            			continue;
            		} else {
            			toAccount = keyObj.toString().trim();
            		}        			
        		}  else if (keyName.equals("TRANSFER_AMOUNT")) {
        			//modified by xyf 20090817
        			//if (keyObj == null) {
        			if (keyObj ==null || "".equals(keyObj)){
            			errType += "transfer_amt_empty,";
            			errFlag = true;
            			continue;
            		} else {
            			amount = (BigDecimal) keyObj;
            		}
        		}   else if (keyName.equals("CHARGE_BY")) {
        			if (keyObj == null) {
        				errType += "charge__by_empty,";
            			errFlag = true;
            			continue;
            		}
        			if ( !((keyObj.toString().trim().equals("1") || (keyObj.toString().trim().equals("2")) ))) {
            			errType += "charge__by_err,";
            			errFlag = true;
            			continue;
            		}
        		} else if (keyName.equals("NETWORK_TYPE")) {
        			if (keyObj == null) {
        				errType += "network__type_empty,";
            			errFlag = true;
            			continue;
            		}
        			if ( !((keyObj.toString().trim().equals("1") || (keyObj.toString().trim().equals("2")) ))) {
            			errType += "network__type_err,";
            			errFlag = true;
            			continue;
            		}
        		} else if (keyName.equals("BENBANK_SWIAB")) {
        			//modified by xyf 20090817
        			//if (keyObj == null) {
        			if (keyObj ==null || "".equals(keyObj)){
        				errType += "beneficiary__bank_swift_empty,";
            			errFlag = true;
            			continue;
            		}
        		} else if (keyName.equals("BENEFICIARY_NAME")) {
        			//modified by xyf 20090817
        			//if (keyObj == null) {
        			if (keyObj ==null || "".equals(keyObj)){
        				errType += "beneficiary__name_empty,";
            			errFlag = true;
            			continue;
            		}
        		} else if (keyName.equals("BENEFICIARY_COUNTRY")) {
        			//modified by xyf 20090817
        			//if (keyObj == null) {
        			if (keyObj ==null || "".equals(keyObj)){
        				errType += "bene__country_empty,";
            			errFlag = true;
            			continue;
            		} else {
            			beneficiaryCountry = keyObj.toString().trim();        			            			
            		}
        			if (keyObj.toString().trim().equals("MO")) {
        				throw new NTBException("err.bat.TheUploadFileIsNotMatchTransferOverseaStM");
            		}
        		}  else if (keyName.equals("BENEFICIARY_BANK")) {
        			//modified by xyf 20090817
        			//if (keyObj == null) {
        			if (keyObj ==null || "".equals(keyObj)){
        				errType += "beneficiary_bank_empty,";
            			errFlag = true;
            			continue;
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
        		}  else if (keyName.equals("CHARGE_ACCOUNT")) {
        			//modified by xyf 20090817
        			//if (keyObj.toString().trim().equals("")) {
        			if (keyObj==null || keyObj.toString().trim().equals("")) {
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
        		} else if (keyName.equals("RECORD_TYPE")) {
        			
        			//--------modified by xyf 20090817-----------
        			//RECORD_TYPE it can be blank
           			//if (keyObj == null) {
        			/*if (keyObj ==null || "".equals(keyObj)){
        				errType += "account_type_empty,";
            			errFlag = true;
            			continue;
            		}*/
        			if (keyObj == null) {
        				keyObj = "";
        			}
        			//-------------------end---------------------
        			
        			if (!keyObj.toString().trim().equals("") && !keyObj.toString().trim().equals("I")) {
            			errType += "account_type_err,";
            			errFlag = true;
            			continue;
            		}
        			accountType = keyObj.toString().trim();
        		}
                //--------------added by wen_chy 20090723 begin---------------//
        		else if (keyName.equals("SPBANK_CODE")) {
        			Map bdMap = (HashMap)recordData.get("BD");
        			if(Utils.null2EmptyWithTrim(bdMap.get("BENEFICIARY_COUNTRY")).equals("AU")){
        				if (Utils.null2EmptyWithTrim(keyObj).equals("")) {
            				errType += "bsb_number_empty,";
                			errFlag = true;
                			continue;
                		}
        			}
        			
        		}
                //--------------added by wen_chy 20090723 end---------------//
                
                
                //--------------added by xyf 20090723 begin---------------//
        		else if (keyName.equals("BENEFICIARY_NAME3")) {
        			
        			if (Utils.null2EmptyWithTrim(keyObj).equals("")) {
            			//modified by xyf 20090814
            			//if BENEFICIARY_NAME3 is "", then check BENEFICIARY_NAME4,
        				//if BENEFICIARY_NAME4 is "", then check COUNTRY from DB
            			Map bdMap = (HashMap)recordData.get("BD");
            			String countryCode = Utils.null2EmptyWithTrim(bdMap.get("BENEFICIARY_COUNTRY"));
            			String beneficiaryName4 = Utils.null2EmptyWithTrim(bdMap.get("BENEFICIARY_NAME4"));
            			
            			if(beneficiaryName4.equals("")){
            				TransferService transferService = (TransferService) appContext.getBean("TransferService");
            				String checkFlag = transferService.getAddrMandatoryFlagOversea(countryCode, "E");
            				if("Y".equalsIgnoreCase(checkFlag)){
            					errType += "beneficiary_address_empty,";
                    			errFlag = true;
                    			continue;
            				}
            			}
        				/*errType += "beneficiary_address_empty,";
            			errFlag = true;
            			continue;*/
            		}
        		}
                //--------------added by xyf 20090723 end----------------//
//        		else {
//        			errType += "unknown,";
//        			errFlag = true;
//        			continue;
//        		}        		
            }
           
            recordData.put("TRANS_ID", CibIdGenerator.getIdForBatchRecord("TRANSFER_OVERSEA_STM"));
            recordData.put("BATCH_ID", BATCH_ID);
            recordData.put("CORP_ID", corpUser.getCorpId());
            recordData.put("USER_ID", corpUser.getUserId());
            //recordData.put("REMARK", "");
            recordData.put("EXECUTE_TIME", new Date());
            recordData.put("REQUEST_TIME", new Date());
            recordData.put("LINE_NO", new Integer(++LINE_NO));
            recordData.remove("RECORD_ID");
            recordData.remove("BD");
            
            // Jet added for IBAN account checking 
			if (accountType != null && accountType.equals("I")){
				GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");

				String SELECT_IBAN = "Select IBAN_LENGTH,IBAN_PREFIX from HS_COUNTRY_SPECIFIC_ACCOUNT_LABEL where COUNTRY_CODE=?";
				String length = null;
				List IBANList = null;
				
				// Get the checking standard
				try {
					IBANList = dao.query(SELECT_IBAN, new Object[] { beneficiaryCountry });
					if (null != IBANList && IBANList.size() > 0) {
						Map cdtMap1 = (Map) IBANList.get(0);
						length = (String) cdtMap1.get("IBAN_LENGTH");
					}

				} catch (Exception e) {
					throw new NTBException("err.txn.GetIBANException");
				}

				if (IBANList == null || IBANList.size() <= 0){
					errType += "Not_Iban_Country_err,";
					errFlag = true;
				} else {
					// 1. Validate the length of IBAN. Every country has a fixed IBAN length.
					if (!length.equals("00")) {
						String accountLength = (new Integer(toAccount.length())).toString();
						if (!accountLength.equals(length)) {
							errType += "Iban_Account_Length_err,";
							errFlag = true;
						}
					}
				
					// 2. check the first 2 characters must be a country code which is equal to the country of the beneficiary bank.
					String accountPrefix = toAccount.substring(0,2).toUpperCase();			
					String toAccount1 = accountPrefix + toAccount.substring(2);
					toAccount = toAccount1;

					if (!(accountPrefix.compareTo(beneficiaryCountry) == 0)) {
						errType += "Iban_Account_Prefix_err,";
						errFlag = true;
					}

		            // 3. Checksum validation
					if (!length.equals("00") && new Integer(length).intValue() > 4){
//						String after4Bytes = toAccount.substring(4);			
//						String newIBANString = after4Bytes
//							+ prefixConversion(toAccount.substring(0, 1).toUpperCase())
//							+ prefixConversion(toAccount.substring(1, 2).toUpperCase())
//							+ toAccount.substring(2, 4); 

						String tempIBANString = toAccount.substring(4) + toAccount.substring(0, 4);
						String newIBANString = "";
						for(int i = 0; i < tempIBANString.length(); i ++){
							newIBANString = newIBANString + prefixConversion(tempIBANString.substring(i, i+1).toUpperCase());
						}
						
						BigInteger newIBAN = new BigInteger(newIBANString);
						if(newIBAN.remainder(new BigInteger("97")).intValue() != 1 ){
							errType += "Iban_Account_Checksum_err,";
							errFlag = true;
						}			
					}
				}		
			}        			            
            
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
    	return "BOBUORF.O" + DateTime.formatDate(d, "ddHHmmss") + ".txt";
    }
	public void approveTransferOverseaStM(FileRequest fileRequest, CorpUser corpUser, BigDecimal equivalentMOP) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
        TransferLimitService transferLimitService = (TransferLimitService)appContext.getBean("TransferLimitService");
        UtilService utilService = (UtilService)appContext.getBean("UtilService");
        fileRequest.setStatus(Constants.STATUS_NORMAL);
        fileRequest.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        fileRequest.setBatchResult("P");
        fileRequest.setExecuteTime(new Date());
		requestDao.update(fileRequest);
		
		String sql_approvel_transfer_record = 
			"update TRANSFER_OVERSEA_STM set status = ?, DETAIL_RESULT = ? where BATCH_ID = ? and status is not null";
		try {
			this.genericJdbcDao.update(sql_approvel_transfer_record,
					new Object[]{Constants.STATUS_NORMAL, "P", fileRequest.getBatchId()});
		} catch (Exception e) {
			throw new NTBException("err.bat.ApproveTransferOverseaSTMError");
		}
		
		// add Limit
		transferLimitService.addUsedLimitQuota(fileRequest.getFromAccount(),
				corpUser.getCorpId(), 
				Constants.TXN_TYPE_TRANSFER_OVERSEAS,
				fileRequest.getFromAmount().doubleValue(), equivalentMOP.doubleValue());
		
		// TODO write report
//        utilService.uploadFileToHost("XJ55",new File(saveFilePath + fileRequest.getFileName()));
		//mod by linrui 20180309
        utilService.uploadFileToHost("XJ55",new File(saveFilePath + fileRequest.getFileName()),FileRequest.TRANSFER_OVERSEA_STM);
				
	}

	public void clearUnavailableDataByCorpId(String corpId) throws NTBException {
		String sql_file_request = "delete from FILE_REQUEST where status is null and CORP_ID = '" + corpId + "'";
    	String sql_transfer_oversea_stm = "delete from TRANSFER_OVERSEA_STM where status is null and CORP_ID = '" + corpId + "'";
    	
    	genericJdbcDao.getJdbcTemplate().execute(sql_file_request);
    	genericJdbcDao.getJdbcTemplate().execute(sql_transfer_oversea_stm);
	}

	public void clearUnavailableDataByPaybatchId(String batchId) throws NTBException {
		String sql_file_request = "delete from FILE_REQUEST where status is null and BATCH_ID = '" + batchId + "'";
    	String sql_transfer_oversea_stm = "delete from TRANSFER_OVERSEA_STM where status is null and BATCH_ID = '" + batchId + "'";
    	
    	genericJdbcDao.getJdbcTemplate().execute(sql_file_request);
    	genericJdbcDao.getJdbcTemplate().execute(sql_transfer_oversea_stm);
		
	}

	public void rejectFileRequest(FileRequest fileRequest) throws NTBException {
		fileRequest.setStatus(Constants.STATUS_REMOVED);
		fileRequest.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
		fileRequest.setExecuteTime(new Date());
		requestDao.update(fileRequest);
		
		String sql_reject_transfer_record = 
			"update TRANSFER_OVERSEA_STM set status = ? where BATCH_ID = ? and status is not null";
		try {
			this.genericJdbcDao.update(sql_reject_transfer_record,
					new Object[]{Constants.STATUS_REMOVED, fileRequest.getBatchId()});
		} catch (Exception e) {
			throw new NTBException("err.bat.RejectTransferOverseaSTMError");
		}
		
		//delete file
		File file = new File(saveFilePath + fileRequest.getFileName());
		if(file.exists()){
			file.delete();
		}
	}

	public void updateStatus(String batchId) throws NTBException {
		String SQL_UPDATE_FILEREQUST = 
			"update FILE_REQUEST set status = ? where BATCH_ID = ? and STATUS is null";
		String SQL_UPDATE_TRANSFER_OVERSEA = 
			"update TRANSFER_OVERSEA_STM set status = ? where BATCH_ID = ? and STATUS is null and PROBLEM_TYPE = ''";
		
		try {
			genericJdbcDao.update(SQL_UPDATE_FILEREQUST, 
					new Object[]{Constants.STATUS_PENDING_APPROVAL, batchId});
			
			genericJdbcDao.update(SQL_UPDATE_TRANSFER_OVERSEA, 
					new Object[]{Constants.STATUS_PENDING_APPROVAL, batchId});
		} catch (Exception e) {
			throw new NTBException("err.bat.UpdateTransferOverseaSTMError");
		}
	}

	public FileRequest viewAvaliableFileRequest(String batchId) throws NTBException {
		String hql = "from FileRequest as pr where pr.batchId = ? and pr.status is not null";
		List list = this.requestDao.list(hql, new Object[]{batchId});
		return (FileRequest) list.get(0);
	}



// Jet added 2008-1-16
/*
 *  the precedure is:
	1. Validate the length of IBAN. Every country has a fixed IBAN length.
	2. The first 2 characters must be a country code which is equal to the country of the beneficiary bank.
	3. Checksum validation: 
	    a. Check that the total IBAN length is correct as per the country. If not, the IBAN is invalid.
        b. Move the four initial characters to the end of the string.
        c. Replace the letters in the string with digits, expanding the string as necessary, such that A=10, B=11 ... Z=35.
        d. Convert the string to an integer and mod-97 the entire number.
        
        For example, PT50 0002 0123 1234 5678 9015 4  :
        Step 1: Check the IBAN length.
            The IBAN length is 25. Match with country Portugal (PT).            
        Step 2: Move the four initial characters to the end of the string.
            PT50 0002 0123 1234 5678 9015 4  ---->  0002 0123 1234 5678 9015 4 PT50
        Step 3: Replace the letters with digits.
            0002 0123 1234 5678 9015 4 PT50  -----> 0002 0123 1234 5678 9015 4 25 29 50
        Step 4: Convert the string to an integer and mod-97 
            000201231234567890154252950 mod 97 = 1     The remainder is 1. Valid IBAN number.
*/
//	private  void checkIBANAccount(String beneficiaryCountry,  String toAccount, String errType, boolean errFlag)throws NTBException {
//		ApplicationContext appContext = Config.getAppContext();
//		GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
//
//		String SELECT_IBAN = "Select IBAN_LENGTH,IBAN_PREFIX from HS_COUNTRY_SPECIFIC_ACCOUNT_LABEL where COUNTRY_CODE=?";
//		String length = null;
//		List IBANList = null;
//		
////		String prefix = null;
//
//		// Get the checking standard
//		try {
//			IBANList = dao.query(SELECT_IBAN, new Object[] { beneficiaryCountry });
//			if (null != IBANList && IBANList.size() > 0) {
//				Map cdtMap1 = (Map) IBANList.get(0);
//				length = (String) cdtMap1.get("IBAN_LENGTH");
////				prefix = (String) cdtMap1.get("IBAN_PREFIX");
//			}
//
//		} catch (Exception e) {
//			throw new NTBException("err.txn.GetIBANException");
//		}
//
//		if (IBANList == null || IBANList.size() <= 0){
//			errType += "Not_Iban_Country_err,";
//			errFlag = true;
//		} else {
//			// 1. Validate the length of IBAN. Every country has a fixed IBAN length.
//			if (!length.equals("00")) {
//				String accountLength = (new Integer(toAccount.length())).toString();
//				if (!accountLength.equals(length)) {
//					errType += "Iban_Account_Length_err,";
//					errFlag = true;
////				throw new NTBException("err.txn.IbanAccountLengthError");
//				}
//			}
//		
//			// 2. check the first 2 characters must be a country code which is equal to the country of the beneficiary bank.
////		if (!"".equals(prefix.trim()) && prefix.length() >2 ) {
////			prefix = prefix.substring(0,2);			
//			String accountPrefix = toAccount.substring(0,2).toUpperCase();			
//			String toAccount1 = accountPrefix + toAccount.substring(2);
//			toAccount = toAccount1;
//
//			if (!(accountPrefix.compareTo(beneficiaryCountry) == 0)) {
//				errType += "Iban_Account_Prefix_err,";
//				errFlag = true;
////			throw new NTBException("err.txn.IbanAccountPrefixError");
//			}
////		}
//
//            // 3. Checksum validation
//			if (!length.equals("00") && new Integer(length).intValue() > 4){
//				String after4Bytes = toAccount.substring(4);			
//				String newIBANString = after4Bytes
//					+ prefixConversion(toAccount.substring(0, 1).toUpperCase())
//					+ prefixConversion(toAccount.substring(1, 2).toUpperCase())
//					+ toAccount.substring(2, 4); 
//				BigInteger newIBAN = new BigInteger(newIBANString);
//				if(newIBAN.remainder(new BigInteger("97")).intValue() != 1 ){
//					errType += "Iban_Account_Checksum_err,";
//					errFlag = true;
////				throw new NTBException("err.txn.IbanAccountChecksumError");
//				}			
//			}
//		}		
//	}

	private String prefixConversion(String letter)throws NTBException {
		String digitString = null;
		if ("A".equals(letter)){
			digitString = "10";
		} else if ("B".equals(letter)) {
			digitString = "11";			
		} else if ("C".equals(letter)) {
			digitString = "12";			
		} else if ("D".equals(letter)) {
			digitString = "13";			
		} else if ("E".equals(letter)) {
			digitString = "14";			
		} else if ("F".equals(letter)) {
			digitString = "15";			
		} else if ("G".equals(letter)) {
			digitString = "16";			
		} else if ("H".equals(letter)) {
			digitString = "17";			
		} else if ("I".equals(letter)) {
			digitString = "18";			
		} else if ("J".equals(letter)) {
			digitString = "19";			
		} else if ("K".equals(letter)) {
			digitString = "20";			
		} else if ("L".equals(letter)) {
			digitString = "21";			
		} else if ("M".equals(letter)) {
			digitString = "22";			
		} else if ("N".equals(letter)) {
			digitString = "23";			
		} else if ("O".equals(letter)) {
			digitString = "24";			
		} else if ("P".equals(letter)) {
			digitString = "25";			
		} else if ("Q".equals(letter)) {
			digitString = "26";			
		} else if ("R".equals(letter)) {
			digitString = "27";			
		} else if ("S".equals(letter)) {
			digitString = "28";			
		} else if ("T".equals(letter)) {
			digitString = "29";			
		} else if ("U".equals(letter)) {
			digitString = "30";			
		} else if ("V".equals(letter)) {
			digitString = "31";			
		} else if ("W".equals(letter)) {
			digitString = "32";			
		} else if ("X".equals(letter)) {
			digitString = "33";			
		} else if ("Y".equals(letter)) {
			digitString = "34";			
		} else if ("Z".equals(letter)) {
			digitString = "35";			
		} else {
			digitString = letter;
		}
		return digitString;
	}	
	private void writeOverStmFile(FileRequestFileBean fileRequestFileBean, File saveFile) throws Exception {
		List normalList = fileRequestFileBean.getNormalList();
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(saveFile));		
			// File Header
			String bene_bank, bene_ac, bene_name, bene_add1, bene_add2, bene_add3, 
			amt, remi_pur, rece_bank, remark1, remark2, remark3, remark4, long4Str, total_num, filer, filer2;
			// data from header						
			Map row = null;
			for (int i = 0; i < normalList.size(); i++) {
				// data from detail
				row = (Map) normalList.get(i);
				bene_bank = Utils.appendSpace(row.get("BENBANK_SWIAB").toString(), 11);
				bene_ac   = Utils.appendSpace(row.get("TO_ACCOUNT").toString(), 32);
				bene_name = Utils.appendSpace(row.get("BENEFICIARY_NAME").toString(), 60);
				bene_add1 = Utils.appendSpace(row.get("BENEFICIARY_BANK_ADD1").toString(), 60);
				bene_add2 = Utils.appendSpace(row.get("BENEFICIARY_BANK_ADD2").toString(), 60);
				bene_add3 = Utils.appendSpace(row.get("BENEFICIARY_BANK_ADD3").toString(), 60);
				amt       = Format.formatDecimal(row.get("TRANSFER_AMOUNT").toString(),2);
				amt       = Utils.prefixZero(Utils.replaceStr(Utils.replaceStr(amt, ",", ""), ".", ""), 16);
				remi_pur  = Utils.appendSpace("", 5);
				rece_bank = row.get("INTE_BANK_SWIFT").toString();
				rece_bank = (null==rece_bank||"".equals(rece_bank))?bene_bank:rece_bank;
				rece_bank = Utils.appendSpace(rece_bank, 11);
				remark1   = Utils.appendSpace(row.get("MESSAGE1").toString(), 35);
				remark2   = Utils.appendSpace(row.get("MESSAGE2").toString(), 35);
				remark3   = Utils.appendSpace(row.get("MESSAGE3").toString(), 35);
				remark4   = Utils.appendSpace(row.get("MESSAGE4").toString(), 35);
				long4Str  = Utils.appendSpace("", 50);
				total_num = Utils.prefixZero("",5);
				filer     = Utils.appendSpace("", 3);
				filer2     = Utils.appendSpace("", 1);
				
				writer.write(bene_bank + bene_ac + bene_name + bene_add1 +  bene_add2 +  bene_add3 + amt + 
						remi_pur + rece_bank + remark1 + remark2 + remark3 + remark4 + long4Str + total_num + filer + filer2); 
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
	//search ccy from acct linrui 20180122
	public String getCcyByAcct(String acct) throws NTBException {
     String sql = "select * from Corp_Account ca where ca.ACCOUNT_NO = ?";
     try{
     Map map = genericJdbcDao.querySingleRow(sql, new Object[] { acct });
     return  map.get("CURRENCY").toString();
     } catch (Exception e) {
			throw new NTBException("err.bat.RejectTransferBankSTMError");
		}
    }
	//search cif_no from acct linrui 20180122
	public String getCifByAcct(String acct) throws NTBException {
     String sql = "select * from Corp_Account ca where ca.ACCOUNT_NO = ?";
     try{
     Map map = genericJdbcDao.querySingleRow(sql, new Object[] { acct });
     return  map.get("CIF_NO").toString();
     } catch (Exception e) {
			throw new NTBException("err.bat.RejectTransferBankSTMError");
		}
    }
	//search cif_name from cif_no
	public String getCiNameByACifNo(String cifNo) throws NTBException {
	     String sql = "select CUSTOMER_SHORT_NAME from HS_CORPORATE_INFO where cif_no = ?";
	     try{
	     Map map = genericJdbcDao.querySingleRow(sql, new Object[] { cifNo });
	     return  map.get("CUSTOMER_SHORT_NAME").toString();
	     } catch (Exception e) {
				throw new NTBException("err.bat.RejectTransferBankSTMError");
			}
	    }
	private String getToHostFileName() {
		Date d = new Date();
		return CibIdGenerator.getFileNameSequence()+ DateTime.formatDate(d, "MMdd") + ".TXT";//add by linrui 20180117
	}
//add by linrui for toHOst MACAU STM 20180122
	public Map toHostTransferSTM(FileRequest pojoFileRequest,
			CorpUser corpUser, String txnType) throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
			
		ApplicationContext appContext = Config.getAppContext();
		CibTransClient testClient = new CibTransClient("CIB", "ZJ56");
		String ip = null;
		String fromAcct = pojoFileRequest.getFromAccount();
		String amt = Format.formatDecimal(pojoFileRequest.getToAmount(),2);
		Date date = new Date();
		try{
		ip = InetAddress.getLocalHost().getHostAddress();
		}catch (Exception e) {
			throw new NTBException("err.sys.GetIPError");
		}
		toHost.put("currDate", DateTime.formatDate(date, "yyyyMMdd"));
		toHost.put("uploadType","75");
		toHost.put("currTime", DateTime.formatDate(date, "HHmmss"));
		toHost.put("teller_id_1", "HS24");
		toHost.put("type1", "75");
		toHost.put("teller_id_2", "HS24################");
		toHost.put("fileName", pojoFileRequest.getFileName().substring(0,8));
		toHost.put("ip_addr", ip);
		toHost.put("fil_PATH", "");
		toHost.put("teller_id_3", "HS24################");
		toHost.put("br_branch", "80101002310");
		toHost.put("CTRT_NO", "0000001");
		toHost.put("CNTR_NO", "");
		toHost.put("DD_AC", fromAcct);
		toHost.put("CI_NO", getCifByAcct(fromAcct));
		toHost.put("CI_ENM", getCiNameByACifNo(getCifByAcct(fromAcct)));
		toHost.put("ITM", "0000000000");
		toHost.put("AC_SEQ", "00000");
		toHost.put("TOT_CNT", Utils.prefixZero(pojoFileRequest.getTotalNumber().toString(), 5));
		toHost.put("TOT_AMT", Utils.prefixZero(Utils.replaceStr(Utils.replaceStr(amt, ",", ""), ".", ""), 16));
		toHost.put("X01", "A");
		toHost.put("BRANCH_BR", "80101002310");
		toHost.put("DRCR_FLG", "EBK");
				
		testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_TRANSFER,
				CibTransClient.ACCTYPE_3RD_PARTY, pojoFileRequest.getBatchId());
		fromHost = testClient.doTransaction(toHost);
		// 如果交易不成功则报出主机错误 0828
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		return fromHost;
//		return null;
	}




}
