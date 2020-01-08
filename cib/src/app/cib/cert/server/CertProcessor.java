package app.cib.cert.server;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TxnSignData;
import app.cib.core.CibIdGenerator;
import app.cib.dao.txn.TxnSignDataDao;
import app.cib.util.UploadReporter;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBAction;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBLoginException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

public class CertProcessor {
	private static boolean checkCertFlag = false;
	static {
		String checkCertStr = Utils.null2Empty(Config.getProperty("checkCert"))
				.toUpperCase();
		checkCertFlag = !checkCertStr.equals("NO");
	}

	public CertProcessor() {
	}

	public static boolean getCheckCertFlag() {
		return checkCertFlag;
	}

	//<!-- modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin -->
	/*public static int checkUserCert(CorpUser user, NTBAction action)
			throws NTBException {*/
	public static int checkUserCert(CorpUser user, String userCertStr)
			throws NTBException {
	//<!-- modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB end -->
		
		// 如果是 Approver 则检查证书
		BcCertVerification certVerifiaction = new BcCertVerification();
		if (user.getRoleId().equals(CorpUser.ROLE_CORP_APPROVER)) {
			RBFactory rb = RBFactory
					.getInstance("app.cib.resource.common.cert_error");

			try {
				// 获得证书的String
				//<!-- delete by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB end -->
				/*String userCertStr = action.getParameter("UserCert");*/
				
				Log.info("TEST---UserCert="+userCertStr);
				if (Utils.null2EmptyWithTrim(userCertStr).equals("")) {
					if (checkCertFlag) {
						throw new NTBLoginException("-1010");
					}
				} else {
					// 解析证书内容
					int retVal = certVerifiaction.decodeUserCert(userCertStr);
					if (retVal != 0) {
						if (checkCertFlag) {
							throw new NTBException(String.valueOf(retVal));
						}
					}
					X509Certificate userCert = certVerifiaction.getUserCert();
					// 如果证书为空
					if (userCert == null) {
						if (checkCertFlag) {
							throw new NTBLoginException("-1010");
						}
					} else {
						// 进行证书校验（包括三步：1、校验时间2、校验证书链3、校验CRL）
						int retVal1 = certVerifiaction.doVerifyCert(userCert);
						Log.info("Certificate Verify Result: "
								+ rb.getString(String.valueOf(retVal)));
						if (retVal1 != 0) {//For 测试，跳过这个报错。Sam 2012-6-8 原版代码
//						if (retVal1 != 0 && retVal1 != -1011) {
							//For 测试，跳过这个报错。Sam 2012-6-8
							if (checkCertFlag) {
								throw new NTBException(String.valueOf(retVal1));
							}
						}
						// 获得证书DN
						String userDN = null;
						String userAlterName = null;
						userDN = certVerifiaction.getCertSubjectDN();
						// 获得证书的 AlterName，并对照用户表的数据
						try {
							userAlterName = certVerifiaction
									.getSubjectAlteNameHexCode();
						} catch (Exception e) {
							Log
									.error(
											"Error reading alternative name of certificate",
											e);
							if (checkCertFlag) {
								throw new NTBLoginException("-1030");
							}
						}
						if (!Utils.null2EmptyWithTrim(user.getCertInfo1())
								.equals(userAlterName)) {
							if (checkCertFlag) {
								throw new NTBLoginException("-1030");
							}
						}
						// 验证通过后存入user session
						user.setUserCert(userCert);
					}
				}
			} catch (NTBException e) {
				String errCode = e.getErrorCode();
				if (!"-1010".equals(errCode)) {
					Map reportMap1 = new HashMap();
					reportMap1.put("LOGIN_DATE", DateTime.formatDate(user
							.getCurrLoginTime(), "yyyyMMdd"));
					reportMap1.put("LOGIN_TIME", DateTime.formatDate(user
							.getCurrLoginTime(), "HHmmss"));
					reportMap1.put("USER_ID", user.getUserId());
					String certOwner = certVerifiaction.getCertInfo(0);
					if(certOwner.length() > 50){
						certOwner = certOwner.substring(0,50);
					}
					String certIssuer = certVerifiaction.getCertInfo(9);
					if(certIssuer.length() > 50){
						certIssuer = certIssuer.substring(0,50);
					}
					reportMap1.put("CERT_OWNER", certOwner);
					reportMap1.put("CERT_ISSUER", certIssuer);
					reportMap1.put("CORP_ID", user.getCorpId());
					reportMap1.put("STATUS", errCode);
					UploadReporter.write("RP_CERTEXP", reportMap1);
				}

				/*
				 * -1001=Unknown error when initialize certificate setting
				 * -1002=Certificate setting file invalid -1003=Certificate file
				 * not found -1004=I/O error when reading certificate file
				 * -1005=Fail to read certificate chain -1006=Fail to read CRL
				 * -1010=Missing user's certificate -1011=Certificate is expired
				 * (Verification of Validity Period) -1012=Certificate is not
				 * yet valid (Verification of Validity Period) -1014=Certificate
				 * is revoked (CRL Verification) -1017=No such Algorithm
				 * (Certificate Chain Verification) -1018=Public key invalid
				 * (Certificate Chain Verification) -1019=No such Provider
				 * (Certificate Chain Verification) -1020=Certificate chain not
				 * match (Certificate Chain Verification) -1021=Signature not
				 * match -1022=Certificate I/O error -1023=Fail to read user's
				 * certificate -1030=Certificate not match
				 */
				if ("-1010".equals(errCode)) {
					throw new NTBLoginException("err.cert.NoCertForUser");
				} else if ("-1030".equals(errCode)) {
					throw new NTBLoginException("err.cert.userCertNotMatch");
				} 
				else if ("-1011".equals(errCode)) {
					throw new NTBLoginException("err.cert.userCertExpired");//For 测试，跳过这个报错。Sam 2012-6-8
				}
				else if ("-1012".equals(errCode)) {
					throw new NTBLoginException("err.cert.userCertNotYetValid");
				} else if ("-1014".equals(errCode)) {
					throw new NTBLoginException("err.cert.userCertRevoked");
				} else if ("-1020".equals(errCode)) {
					throw new NTBLoginException("err.cert.CertChainNotMatch");
				} else {
					throw new NTBLoginException(
							"err.cert.CertVerificationError",
							new Object[] { errCode });
				}
			}
		}
		return 0;
	}

	public static String checkSignData(NTBUser user, NTBAction action,
			String act) throws NTBException {
		// 如果是 Approver 则检查数字签名
		if (user instanceof CorpUser) {
			CorpUser corpUser = (CorpUser) user;
			if (corpUser.getRoleId().equals(CorpUser.ROLE_CORP_APPROVER)) {
				RBFactory rb = RBFactory
						.getInstance("app.cib.resource.common.cert_error");
				CertVerification certVerifiaction = new CertVerification();
				boolean checkCertFlag = certVerifiaction.getCheckCertFlag();

				// 获得证书的String
				String fieldsToBeSigned = action
						.getParameter("_fieldsToBeSigned");
				String dataToBeSigned = action.getParameter("dataToBeSigned");
				String signatureValue = action.getParameter("signatureValue");
				String txnType = action.getParameter("txnType");
				String workId = action.getParameter("workId");
				String userCertStr = action.getParameter("userCert");
				if(fieldsToBeSigned==null){
					fieldsToBeSigned=(String) action.getResultData().get("_fieldsToBeSigned");
					dataToBeSigned = (String) action.getResultData().get("dataToBeSigned");
					signatureValue =(String) action.getResultData().get("signatureValue");
					txnType = (String) action.getResultData().get("txnType");
					workId = (String) action.getResultData().get("workId");
					userCertStr = (String) action.getResultData().get("userCert");
				}
				X509Certificate userCert = corpUser.getUserCert();

				if (Utils.null2EmptyWithTrim(fieldsToBeSigned).equals("")) {
					return null;
				}

				// 如果证书为空
				if (userCert == null) {
					if (checkCertFlag) {
						throw new NTBLoginException("err.cert.NoCertForUser");
					}
				} else {
					// 进行数字签名校验
					int retVal = certVerifiaction.doVerifySignedData(userCert,
							dataToBeSigned, signatureValue);
					Log.info("Certificate Verify Result(Sign): "
							+ rb.getString(String.valueOf(retVal)));
					if (retVal != 0) {
						if (checkCertFlag) {
							throw new NTBException(rb.getString(String
									.valueOf(retVal)));
						}
					}

					TxnSignDataDao txnSignDataDao = (TxnSignDataDao) Config
							.getAppContext().getBean("txnSignDataDao");
					String seqNo = CibIdGenerator
							.getIdForOperation("TXN_SIGN_DATA");
					TxnSignData signData = new TxnSignData(seqNo);
					signData.setAction(act);
					signData.setUserId(corpUser.getUserId());
					signData.setCorpId(corpUser.getCorpId());
					signData.setCertAltName(corpUser.getCertInfo1());
					signData.setSignFields(fieldsToBeSigned);
					signData.setSignData(dataToBeSigned);
					signData.setSignResult(signatureValue);
					signData.setTxnType(txnType);
					signData.setWorkId(workId);
					signData.setActionTime(new Date());
					
					Log.info("seqNo="+seqNo);
					Log.info("Action="+act);
					Log.info("UserId="+corpUser.getUserId());
					Log.info("CorpId="+corpUser.getCorpId());
					Log.info("CertAltName="+corpUser.getCertInfo1());
					Log.info("SignFields="+fieldsToBeSigned);
					Log.info("SignData="+dataToBeSigned);
					Log.info("SignResult="+signatureValue);
					Log.info("txnType="+txnType);
					Log.info("workId="+workId);
					Log.info("ActionTime="+signData.getActionTime());
					
					txnSignDataDao.add(signData);

					return seqNo;
				}
			}
		}
		return null;
	}

	public static String[] checkMultiSignData(NTBUser user, NTBAction action,
			String act) throws NTBException {
		// 如果是 Approver 则检查数字签名
		if (user instanceof CorpUser) {
			CorpUser corpUser = (CorpUser) user;
			if (corpUser.getRoleId().equals(CorpUser.ROLE_CORP_APPROVER)) {
				RBFactory rb = RBFactory
						.getInstance("app.cib.resource.common.cert_error");
				CertVerification certVerifiaction = new CertVerification();
				boolean checkCertFlag = certVerifiaction.getCheckCertFlag();

				// 获得证书的String
				String[] fieldsToBeSignedArray = action
						.getParameterValues("_fieldsToBeSigned");
				String[] dataToBeSignedArray = action
						.getParameterValues("dataToBeSigned");
				String[] signatureValueArray = action
						.getParameterValues("signatureValue");
				String[] txnTypeArray = action.getParameterValues("txnType");
				String[] workIdArray = action.getParameterValues("workId");
				String userCertStr = action.getParameter("userCert");
				if(fieldsToBeSignedArray==null){
					fieldsToBeSignedArray=(String[]) action.getResultData().get("_fieldsToBeSigned");
					dataToBeSignedArray = (String[]) action.getResultData().get("dataToBeSigned");
					signatureValueArray =(String[]) action.getResultData().get("signatureValue");
					txnTypeArray = (String[]) action.getResultData().get("txnType");
					workIdArray = (String[]) action.getResultData().get("workId");
					userCertStr = (String) action.getResultData().get("userCert");
				}
				X509Certificate userCert = corpUser.getUserCert();

				String[] seqNoArray = new String[fieldsToBeSignedArray.length];
				for (int i = 0; i < fieldsToBeSignedArray.length; i++) {
					String fieldsToBeSigned = fieldsToBeSignedArray[i];
					String dataToBeSigned = dataToBeSignedArray[i];
					String signatureValue = signatureValueArray[i];
					String txnType = txnTypeArray[i];
					String workId = workIdArray[i];

					if (Utils.null2EmptyWithTrim(fieldsToBeSigned).equals("")) {
						return null;
					}

					// 如果证书为空
					if (userCert == null) {
						if (checkCertFlag) {
							throw new NTBLoginException(
									"err.cert.NoCertForUser");
						}
					} else {
						// 进行数字签名校验
						int retVal = certVerifiaction.doVerifySignedData(
								userCert, dataToBeSigned, signatureValue);
						Log.info("Certificate Verify Result(MultiSign): "
								+ rb.getString(String.valueOf(retVal)));
						if (retVal != 0) {
							if (checkCertFlag) {
								throw new NTBException(rb.getString(String
										.valueOf(retVal)));
							}
						}

						TxnSignDataDao txnSignDataDao = (TxnSignDataDao) Config
								.getAppContext().getBean("txnSignDataDao");
						String seqNo = CibIdGenerator
								.getIdForOperation("TXN_SIGN_DATA");
						TxnSignData signData = new TxnSignData(seqNo);
						signData.setAction(act);
						signData.setUserId(corpUser.getUserId());
						signData.setCorpId(corpUser.getCorpId());
						signData.setCertAltName(corpUser.getCertInfo1());
						signData.setSignFields(fieldsToBeSigned);
						signData.setSignData(dataToBeSigned);
						signData.setSignResult(signatureValue);
						signData.setTxnType(txnType);
						signData.setWorkId(workId);
						signData.setActionTime(new Date());
						txnSignDataDao.add(signData);

						seqNoArray[i] = seqNo;
					}
				}
				return seqNoArray;
			}
		}
		return null;
	}

	public static void updateSignData(String key, Map parameters)
			throws NTBException {

		TxnSignDataDao txnSignDataDao = (TxnSignDataDao) Config.getAppContext()
				.getBean("txnSignDataDao");
		TxnSignData signData = (TxnSignData) txnSignDataDao.load(
				TxnSignData.class, key);
		signData.setTransId((String) parameters.get("transId"));
		signData.setProcId((String) parameters.get("procId"));
		signData.setActionResult((String) parameters.get("processResult"));
		txnSignDataDao.update(signData);

	}

}
