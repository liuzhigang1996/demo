package com.jsax.service.util;

import app.cib.util.otp.SMSOTPUtil;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.SubElement;
import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.core.Log;

public class OtpService extends JsaxAction implements JsaxService{

	@Override
	public void doTransaction() throws Exception {
		this.setTargetType(TARGET_TYPE_ELEMENT);
		
		String smsFlowNo = this.getParameter("smsFlowNo") ;
		if(null==smsFlowNo || "".equals(smsFlowNo)){
			Log.error("smsFlowNo is null");
			throw new Exception("smsFlowNo is null");
		}
		
		String exceedResendFlag = "N" ;
		int check = SMSOTPUtil.resendCheck(smsFlowNo) ;
		
		if(-1==check){
			exceedResendFlag = "Y" ;
		}
		
//		smsFlowNo = SMSOTPUtil.resend(smsFlowNo) ;
		smsFlowNo = SMSOTPUtil.resendSMS(smsFlowNo) ;
//		this.addSubResponseListByDefaultType("exceedResendFlag", exceedResendFlag);
//		this.addSubResponseListByDefaultType("smsFlowNo", smsFlowNo);
		this.addSubResponseList(TARGET_TYPE_FIELD, "exceedResendFlag", exceedResendFlag) ;
		this.addSubResponseList(TARGET_TYPE_FIELD, "smsFlowNo", smsFlowNo) ;
	}

}
