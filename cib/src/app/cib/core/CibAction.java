package app.cib.core;

import com.neturbo.set.core.*;

import java.util.Locale;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;
import com.neturbo.set.exception.NTBException;
import com.neturbo.base.action.ActionMapping;
import com.neturbo.set.utils.*;
import app.cib.util.*;
import app.cib.util.otp.SMSOTPObject;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;
import app.cib.cert.server.CertVerification;
import com.neturbo.set.utils.RBFactory;
import app.cib.bo.sys.CorpUser;
import com.neturbo.set.exception.NTBLoginException;
import java.security.cert.X509Certificate;
import app.cib.bo.txn.TxnSignData;
import app.cib.service.sys.CorpUserService;
import app.cib.dao.txn.TxnSignDataDao;
import java.util.Date;

//import app.cib.cert.server.IaikCertVerification;
import app.cib.cert.server.BcCertVerification;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CibAction extends NTBAction {
    private static boolean checkCertFlag = false;
    public CibAction() {}

    public void execute() throws NTBException {
        //Log.warn("Method execute not defined");
    }

    public void handleException(NTBException e) {
        //approve transaction ��ʱ��,�����޶�Ҫд log ��,���ǳ��޻��׳� Exception,��˻ع�
        //Ŀǰ�Ľ���취��д log ��д��BufferList, Ȼ���ڴ�д�����
        if(ErrConstants.ERROR_EXCEED_DAILY_LIMIT.equals(e.getErrorCode())){
            UploadReporter.flushBuffer();
        }
    }

    public void writeSysLog(Map inputData) {

    }
    
    public void checkSMS() throws NTBException{
		String otp = Utils.null2Empty(this.getParameter("otp"));
		String smsFlowNo = Utils.null2Empty(this.getParameter("smsFlowNo"));
		String exceedResend = Utils.null2Empty(this.getParameter("exceedResend"));
		
		String lang = SMSOTPUtil.getLang(this.getUser().getLanguage());
		
		
		boolean isDoubleSubmit = false ;
		SMSOTPObject otpObject = SMSOTPUtil.getOtpObject(smsFlowNo) ;
		if(null==otpObject || "".equals(otpObject)){
			isDoubleSubmit = true ;
		}
		
		SMSReturnObject returnObject = new SMSReturnObject() ;
		
		if(!isDoubleSubmit){
			SMSOTPUtil.check(returnObject, smsFlowNo, otp, exceedResend, lang) ;
		}
		//update by liang_ly for CR204 2015-4-9
		if(!returnObject.isReturnFlag() && !isDoubleSubmit){
			Log.info("Check SMS: Correct, go on...");
		}else{
			Log.info("Check SMS: Error, return to previous page...");
			throw new NTBException("err.sms.OTPERROR");
		}
    }
    
    
    /**
     * @desc: get PIN/Security code policy check
     * @author: xyf 20081218
     */
    public Map getCheckRuleMap() {
    	Map checkMap = new HashMap();
    	int pinMin = Utils.nullEmpty2Zero(Config.getProperty("pin.length.min"));
    	int pinMax = Utils.nullEmpty2Zero(Config.getProperty("pin.length.max"));
    	int pinUpper = Utils.nullEmpty2Zero(Config.getProperty("pin.length.letter.upper"));
    	int pinLower = Utils.nullEmpty2Zero(Config.getProperty("pin.length.letter.lower"));
    	int pin2Min = Utils.nullEmpty2Zero(Config.getProperty("pin2.length.min"));
    	int pin2Max = Utils.nullEmpty2Zero(Config.getProperty("pin2.length.max"));
    	int pin2Upper = Utils.nullEmpty2Zero(Config.getProperty("pin2.length.letter.upper"));
    	int pin2Lower = Utils.nullEmpty2Zero(Config.getProperty("pin2.length.letter.lower"));
    	
    	checkMap.put("pinMin", String.valueOf(pinMin));
    	checkMap.put("pinMax", String.valueOf(pinMax));
    	checkMap.put("pinUpper", String.valueOf(pinUpper));
    	checkMap.put("pinLower", String.valueOf(pinLower));
    	checkMap.put("pin2Min", String.valueOf(pin2Min));
    	checkMap.put("pin2Max", String.valueOf(pin2Max));
    	checkMap.put("pin2Upper", String.valueOf(pin2Upper));
    	checkMap.put("pin2Lower", String.valueOf(pin2Lower));
    	
    	return checkMap;
    }

}
