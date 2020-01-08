package app.cib.core;

import iaik.x509.ocsp.Request;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import app.cib.bo.flow.FlowProcess;
import app.cib.bo.sys.CorpUser;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.CorpUserService;
import app.cib.util.otp.SMSOTPUtil;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.StrTokenizer;
import com.neturbo.set.utils.TemplateFactory;
import com.neturbo.set.utils.Utils;

public class SMSOTPTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8172558948591543135L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		/*
		 * 2006-09-20 modified, remove mail notification checkbox
		 */

		try {
			// ������
			JspWriter out = pageContext.getOut();
			// ��session�����������
			HttpSession session = pageContext.getSession();
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			Locale locale = (Locale) session.getAttribute("Locale$Of$Neturbo");
			CorpUser user = ((CorpUser) session
					.getAttribute("UserObject$Of$Neturbo"));
			if (locale == null) {
				if (user != null) {
					locale = user.getLanguage();
				}
			}
			if (locale == null) {
				locale = Config.getDefaultLocale();
			}
			//add by linrui for mul-language 20171124
			user.setLanguage(locale);
			RBFactory rbList = RBFactory.getInstance(
					"app.cib.resource.common.otp", locale.toString());
//			String lang = SMSOTPUtil.getLang(user.getLanguage());
			//�ֻ�����ȡ������
			StringBuffer mobileNo = new StringBuffer();
			// If country code and the area code are both blank, please use the mobile number directly to send the SMS.
			String areaCode = user.getMobileAreaCode();
			String countryCode = user.getMobileCountryCode();
			//
			if("0".equals(areaCode)|| "00".equals(areaCode) || "000".equals(areaCode)){
				areaCode ="";
			}
			//
			if("0".equals(countryCode)|| "00".equals(countryCode) || "000".equals(countryCode)){
				countryCode ="";
			}
			
			if(areaCode==null && countryCode==null )//countryCode areaCode is blank
				
			{
				mobileNo.append(user.getMobile());
			}else{
				if(!"".equals(countryCode) || !"".equals(areaCode)){
					//mobileNo.append("+");	
					//mobileNo.append(countryCode);
					//mobileNo.append(areaCode);
					
				}
				mobileNo.append(user.getMobile());
			}
			
			
			String sessionId = session.getId();
			String remoteIP =request.getRemoteAddr();
			
	        Map resultData =
                ((Map) session.getAttribute("ResultData$Of$Neturbo"));
	        String transAmt = (String)resultData.get(amtField);
	        String accNo = (String)resultData.get(accField);
	        funcName = (String)resultData.get("funcName");
	        String cifNo = user.getCorpId();
	        String userId = user.getUserId();
	        
			/*String smsFlowNo = SMSOTPUtil.sendOtp(lang,mobileNo.toString(), sessionId, 
					funcName, transAmt, (new Date()).toString(), accNo, cifNo, 
					funcName, remoteIP, userId, txnCode) ;*/
			String smsFlowNo = SMSOTPUtil.sendOtpSMS(locale.toString(),user.getMobile()/*mobileNo.toString()*/, sessionId, 
					funcName, transAmt, (new Date()).toString(), accNo, cifNo, 
					funcName, remoteIP, userId, txnCode) ;

			//����ģ��
			StringBuffer outputBuffer = new StringBuffer();
			TemplateFactory template = TemplateFactory.getInstance("Otp.jsp");
			
			//�������
			Map parameters = new HashMap();
			parameters.put("smsFlowNo", smsFlowNo);
			parameters.put("validityPeriod", Config.getProperty("app.sms.otp.validity.period"));
			parameters.put("disableTime", Config.getProperty("app.sms.otp.disable.time"));
			//add by linrui for mul-language 20171124put some title or message to otp.jsp begin
			parameters.put("otpInfo", rbList.getString("otp_info"));
			parameters.put("otpInfo1", rbList.getString("otp_info_1"));
			parameters.put("buttonOk", rbList.getString("button_ok"));
			parameters.put("buttonCancel", rbList.getString("button_cancel"));
			parameters.put("getNewOtp", rbList.getString("get_new_otp"));
			parameters.put("resendOtp", rbList.getString("resend_otp"));
			//end
			//Ϊ����Ӧ��ͬҳ��
			/*colspan= colspan + ",";
			String[] colspanArray = Utils.splitStr(colspan, ",");
			parameters.put("colLeft", colspanArray[0]);
			parameters.put("colRight", colspanArray[1]);*/
			String jspContent = template.getContent(parameters);
			
			//�������
			outputBuffer.append(jspContent);
			out.write(outputBuffer.toString());
		} catch (Exception e) {
			Log.error("Custom Tag Process error (ReassignUser)", e);
			JspWriter out = pageContext.getOut();
			StringBuffer outputBuffer = new StringBuffer();
			outputBuffer.append("Sms connection timeout") ;
			outputBuffer.append(e.getMessage());
			try {
				out.write(outputBuffer.toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return (SKIP_BODY);
	}

	private String amtField;
	private String accField;
	private String funcName;
	private String txnCode;
	private String colspan="2,1";

	public String getAmtField() {
		return amtField;
	}
	public void setAmtField(String amtField) {
		this.amtField = amtField;
	}
	public String getAccField() {
		return accField;
	}
	public void setAccField(String accField) {
		this.accField = accField;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getTxnCode() {
		return txnCode;
	}
	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}
	public String getColspan() {
		return colspan;
	}
	public void setColspan(String colspan) {
		this.colspan = colspan;
	}
	

}
