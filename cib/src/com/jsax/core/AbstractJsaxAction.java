/**
 * @author hjs
 * 2006-8-8
 */
package com.jsax.core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;


import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.xml.XMLElement;
import com.neturbo.set.xml.XMLFactory;
import com.neturbo.set.xml.XMLWriter;

/**
 * @author hjs
 * 2006-8-8
 */
public abstract class AbstractJsaxAction {
	
	protected static final String TARGET_TYPE_OBJECT = "object";
	protected static final String TARGET_TYPE_ELEMENT = "element";
	protected static final String TARGET_TYPE_FIELD = "field";
	static final String ERR_PROCESSOR = "errProcessor";
    
	private String debugMode = "0";
	
	private HttpServletRequest request;
	private HttpSession session;
	private NTBUser user;
	private String serviceName;
	
	private List responseList = new ArrayList();
	
	private BufferedReader targetXml;
	private String targetType;
	private String targetId;

	protected String getTargetId() {
		return targetId;
	}

	protected void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	protected String getTargetType() {
		return targetType;
	}

	protected void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	protected BufferedReader getTargetXml() {
		return targetXml;
	}

	protected void setTargetXml(BufferedReader targetXml) {
		this.targetXml = targetXml;
	}

	private HttpServletRequest getRequest() {
		return request;
	}

	private void setRequest(HttpServletRequest request) throws NTBException {
		this.request = request;
		if (request != null) {
			this.debugMode = getParameter("debugMode");
		}
	}

	public Map getResultData() {
		return (Map) session.getAttribute("ResultData$Of$Neturbo");
	}

	public void setResultData(Map resultData) {
        if (session != null) {
            session.setAttribute("ResultData$Of$Neturbo", resultData);
        }
	}
	//add by linrui for pwc check ccy
	public void setCurrencyResultData(String account, List resultData) {
		if (session != null) {
			session.setAttribute("ResultData$Of$"+account, resultData);
		}
	}
	public List getCurrencyResultData(String account) {
		return (List) session.getAttribute("ResultData$Of$"+account);
	}
	//end
	protected String getParameter(String arg0) throws NTBException{
		if (this.request == null) {
			throw new NTBException("err.jsax.RequestObjectIsNull");
		} else {
			return Utils.null2EmptyWithTrim(request.getParameter(arg0));
		}
	}
	
	protected String[] getParameterValues(String arg0) throws NTBException{
		if (this.request == null) {
			throw new NTBException("err.jsax.RequestObjectIsNull");
		} else {
			return this.request.getParameterValues(arg0);
		}
	}


	protected NTBUser getUser() {
		return user;
	}


	private void setUser(NTBUser user) {
		this.user = user;
	}

	protected String getServiceName() {
		return serviceName;
	}

	protected void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	protected void convertMap2Pojo(Map valueMap, Object pojo) throws NTBException {
        try {
            BeanUtils.populate(pojo, valueMap);
        } catch (Exception e) {
            Log.error("Error writing values to POJO", e);
        }
    }

    protected void convertPojo2Map(Object pojo, Map valueMap) throws NTBException {
        try {
            valueMap.putAll(BeanUtils.describe(pojo));
        } catch (Exception e) {
            Log.error("Error reading values from POJO", e);
        }
    }

    protected List convertPojoList2MapList(List pojoList) throws NTBException {
        try {
            List mapList = new ArrayList();
            for(int i=0; i<pojoList.size(); i++){
                Object pojo = pojoList.get(i);
                mapList.add(BeanUtils.describe(pojo));
            }
            return mapList;
        } catch (Exception e) {
            Log.error("Error reading values from POJO", e);
        }
        return null;
    }
    
    void clearResponseList() {
		responseList.clear();
    }
    
	// for targetType is "element"
	protected SubElement mapList2SelectElement(List mapList, String selectName, String selectId, String textKey, String valueKey) throws NTBException {
		SubElement selectEle = new SubElement("select");
		selectEle.setAttribute("name", Utils.null2EmptyWithTrim(selectName));
		selectEle.setAttribute("id", Utils.null2EmptyWithTrim(selectId));
		
		Map tmpMap = null;
		String text = "";
		String value = "";
		
		if (mapList != null) {
			for (int i=0; i<mapList.size(); i++) {
				tmpMap = (Map) mapList.get(i);
				text = Utils.null2EmptyWithTrim(tmpMap.get(textKey).toString());
				value = Utils.null2EmptyWithTrim(tmpMap.get(valueKey).toString());
				SubElement optionEle = new SubElement("option");
				optionEle.setAttribute("value", value);
				optionEle.setText(text);
				selectEle.addChild(optionEle);
			}
			return selectEle;
		} else {
			Log.error("[JSAX]Service[" + this.serviceName+ "].mapList2SelectElement error: mapList is null");
			return null;
		}
	}
	
	//for targetType is "object", by Map List
	protected SubElement mapList2Selector(List mapList, String subListId, String textKey, String valueKey) throws NTBException {
		SubElement selectEle = new SubElement("select");
		selectEle.setAttribute("id", Utils.null2EmptyWithTrim(subListId));
		
		Map tmpMap = null;
		String text = "";
		String value = "";
		
		if (mapList != null) {
			for (int i=0; i<mapList.size(); i++) {
				tmpMap = (Map) mapList.get(i);
				text = Utils.null2EmptyWithTrim(tmpMap.get(textKey).toString());
//				value = Utils.null2EmptyWithTrim(tmpMap.get(valueKey).toString());
				if (valueKey.indexOf("&") != -1) {// add by linrui 20190325
					String valueK[] = valueKey.split("&");
					if (tmpMap.get(valueK[0]).toString() != "0") {
						value = Utils.null2EmptyWithTrim(tmpMap.get(valueK[0]).toString())+ "&";
						value = value + Utils.null2EmptyWithTrim(tmpMap.get(valueK[1]).toString());
					} else {
						value = Utils.null2EmptyWithTrim(tmpMap.get(valueK[0]).toString());
					}
				} else {
					value = Utils.null2EmptyWithTrim(tmpMap.get(valueKey).toString());
				}
				//end
				SubElement optionEle = new SubElement("option");
				optionEle.setAttribute("value", value);
				optionEle.setAttribute("text", text);
				selectEle.addChild(optionEle);
			}
			return selectEle;
		} else {
			Log.error("[JSAX]Service[" + this.serviceName+ "].mapList2Selector error: mapList is null");
			return null;
		}
	}
	
	//for targetType is "object", by Array List
	protected SubElement arrayList2Selector(List arrayList, String subListId, int textIndex, int valueIndex) throws NTBException {
		SubElement selectEle = new SubElement("select");
		selectEle.setAttribute("id", Utils.null2EmptyWithTrim(subListId));
		
		Object[] tmpArray = null;
		String text = "";
		String value = "";
		
		if (arrayList != null) {
			for (int i=0; i<arrayList.size(); i++) {
				tmpArray = (String[]) arrayList.get(i);
				text = Utils.null2EmptyWithTrim(tmpArray[textIndex].toString());
				value = Utils.null2EmptyWithTrim(tmpArray[valueIndex].toString());
				SubElement optionEle = new SubElement("option");
				optionEle.setAttribute("value", value);
				optionEle.setAttribute("text", text);
				selectEle.addChild(optionEle);
			}
			return selectEle;
		} else {
			Log.error("[JSAX]Service[" + this.serviceName+ "].arrayList2Selector error: arrayList is null");
			return null;
		}
	}
	
	// depend on this.targetType and this.targetId
	protected void addSubResponseListByDefaultType(List elementList) throws NTBException {
		if (elementList == null) {
			Log.error("[JSAX]Service[" + this.serviceName+ "].addSubResponseListByDefaultType error: arguments[elementList] is null");
		} else {
			this.addSubResponseList(this.targetType, this.targetId, elementList);
		}
	}
	//--
	//depend on this.targetType
	protected void addSubResponseListByDefaultType(String id, String text) throws NTBException {
		id = Utils.null2EmptyWithTrim(id);
		text = Utils.null2EmptyWithTrim(text);
		
		if (this.targetType.equals("")) {
			Log.error("[JSAX]Service[" + this.serviceName+ "].addSubResponseListByDefaultType error: arguments[this.targetType] is null or empty");
		} else if (id.equals("")) {
			Log.error("[JSAX]Service[" + this.serviceName+ "].addSubResponseListByDefaultType error: arguments[id] is null or empty");			
		} else {
			this.addSubResponseList(this.targetType, id, text);
		}
	}
	//--
	protected void addSubResponseList(String targetType, String id, SubElement element) throws NTBException {
		String type = targetType;
		type = Utils.null2EmptyWithTrim(type);
		id = Utils.null2EmptyWithTrim(id);
		
		if (element == null) {
			Log.error("[JSAX]Service" + this.serviceName+ "].addSubResponseList error: argument[element] is null");
		} else if (type.equals("")) {
			Log.error("[JSAX]Service" + this.serviceName+ "].addSubResponseList error: argument[targetType] is null or empty");			
		} else if (id.equals("")) {
			Log.error("[JSAX]Service[" + this.serviceName+ "].addSubResponseList error: argument[id] is null or empty");			
		} else {
			SubElement subResponse = getSubResponse(type, id);
			subResponse.addChild(element);
			
			responseList.add(subResponse);
		}
	}
	//-
	// depend on this.targetType and this.targetId
	protected void addSubResponseList(String targetType, String id, List elementList) throws NTBException {
		String type = targetType;
		if (elementList == null) {
			Log.warn("[JSAX]Service[" + this.serviceName+ "].addSubResponseList error: arguments[elementList] is null");
		} else {
			SubElement subResponse = getSubResponse(type, id);
			for (int i=0; i<elementList.size(); i++) {
				if (elementList.get(i) == null) {
					Log.error("[JSAX]Service[" + this.serviceName+ "].addSubResponseList error: argument[element] is null");
				} else if (type.equals("")) {
					Log.error("[JSAX]Service[" + this.serviceName+ "].addSubResponseList error: argument[targetType] is null or empty");			
				} else if (id.equals("")) {
					Log.error("[JSAX]Service[" + this.serviceName+ "].addSubResponseList error: argument[id] is null or empty");
				} else {
					if(elementList.get(i) instanceof SubElement){
						subResponse.addChild((XMLElement) elementList.get(i));
					}
				}
			}
			responseList.add(subResponse);
		}
	}
	//--
	protected void addSubResponseList(String targetType, String id, String text) throws NTBException {
		String type = targetType;
		type = Utils.null2EmptyWithTrim(type);
		id = Utils.null2EmptyWithTrim(id);
		text = Utils.null2EmptyWithTrim(text);
		
		if (type.equals("")) {
			Log.error("[JSAX]Service[" + this.serviceName+ "].addSubResponseList error: argument[targetType] is null or empty");
		} else if (id.equals("")) {
			Log.error("[JSAX]Service[" + this.serviceName+ "].addSubResponseList error: argument[id] is null or empty");			
		} else {
			SubElement subResponse = getSubResponse(type, id);
			subResponse.setText(text);
			
			responseList.add(subResponse);
		}
	}
	
	String backToPage() throws NTBException {
		SubElement responseRoot = getResponseRoot();
		
		//SubElement subResponse = null;
		for(int i=0; i<responseList.size(); i++){
			SubElement subResponse = (SubElement) responseList.get(i);
			responseRoot.addChild(subResponse);
		}
		//���ResponseList
		clearResponseList();
		
		XMLWriter responseXml = getResponseWriter();
		responseXml.setRootElement(responseRoot);
		
		return response2String(responseXml);
	}
	
	private XMLWriter getResponseWriter() throws NTBException {
		return XMLFactory.getWriter();
	}
	
	private SubElement getResponseRoot() throws NTBException{
    	SubElement root = new SubElement("ajax-response");
    	root.setAttribute("requestId", this.getParameter("requestId"));
		return root;
    }
	
	private SubElement getSubResponse(String type, String id) throws NTBException{
    	SubElement subResponse = new SubElement("response");
		subResponse.setAttribute("type", type);
		subResponse.setAttribute("id", id);
		return subResponse;
    }
	
	private SubElement getErrMsgElement() throws NTBException{
    	SubElement errMsg = new SubElement("errMsg");
		return errMsg;
	}
	
	private String response2String(XMLWriter doc) throws NTBException{
		if (doc != null) {			
		    ByteArrayOutputStream retOS = new ByteArrayOutputStream();
			doc.setOutput(retOS);		    
			doc.Marshal();			
			String retStr;
			try {
				retStr = retOS.toString("UTF-8");
				debugOut(retStr);
				
				return retStr;
			} catch (UnsupportedEncodingException e) {
				throw new NTBException(e.getMessage());
			}
		} else {
			Log.error("[JSAX]Response xml document is Null");
			return null;
		}
	}
	
	private void debugOut(String response)  throws NTBException{
		
		if (debugMode.equals("1")) {
			Map reqMap = request.getParameterMap();
			Iterator reqIt = reqMap.keySet().iterator();
			String reqKey = "";
			Object[] reqObj = null;
			int i = 1;
			String valueInfo;
			while (reqIt.hasNext()) {
				valueInfo = "";
				reqKey = (String) reqIt.next();
				reqObj = (Object[]) reqMap.get(reqKey);
				for (int j=0; j<reqObj.length; j++) {
					valueInfo += reqObj[j] + ", ";
				}
				if (!valueInfo.equals("")) {
					valueInfo = valueInfo.substring(0, valueInfo.length()-2);
				}
				Log.info("[JSAX]Service[" + this.serviceName+ "] Request param" + i + ": " + reqKey + "[Value(" + String.valueOf(reqObj.length) + "): " + valueInfo + "]");
				i++;
			}
			
			Log.info("[JSAX]Service[" + this.serviceName+ "] Response infomation:\n" + response);
		}
		
	}
	
	public String performService(HttpServletRequest request, NTBUser user) {
		try {
			this.setRequest(request);
			session = request.getSession(true);
			this.setUser(user);
			this.setServiceName(this.getRequest().getParameter("serviceName"));
			doTransaction();
			Log.debug("[JSAX]Service[" + this.getServiceName() + "] doTransaction successfully");
			return this.backToPage();
		} catch (Exception e) {
			Log.error("[JSAX]Service[" + this.getServiceName() + "] doTransaction failed", e);
			return this.processException(request, e);
		}
	}
	
	public abstract void doTransaction() throws Exception;
	
	abstract String processException (HttpServletRequest request, Exception e);
	
	class ErrMsgHandler {
		private String error;
		private String errorName;
		private String errorLabel;
		private String errorLayer;
		private String errorIndex;
		
		public ErrMsgHandler(String error, String errorName,
				String errorLabel, String errorLayer, String errorIndex) {
			this.setError(error);
			this.setErrorName(errorName);
			this.setErrorLabel(errorLabel);
			this.setErrorLayer(errorLayer);
			this.setErrorIndex(errorIndex);
			
		}
		
		public ErrMsgHandler(){
			
		}
		
		public SubElement getSubErrMsgElement() throws NTBException{
            
            SubElement errMsgElement = getErrMsgElement();
            
            SubElement error = new SubElement("error");
            error.setText(this.getError());
            errMsgElement.addChild(error);
            
            SubElement errorNameList = new SubElement("errorName");
            errorNameList.setText(this.getErrorName());
            errMsgElement.addChild(errorNameList);
            
            SubElement errorLabelList = new SubElement("errorLabel");
            errorLabelList.setText(this.getErrorLabel());
            errMsgElement.addChild(errorLabelList);
            
            SubElement errorLayerList = new SubElement("errorLayer");
            errorLayerList.setText(this.getErrorLayer());
            errMsgElement.addChild(errorLayerList);
            
            SubElement errorIndex = new SubElement("errorIndex");
            errorIndex.setText(this.getErrorIndex());
            errMsgElement.addChild(errorIndex);
            
			return errMsgElement;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getErrorIndex() {
			return errorIndex;
		}

		public void setErrorIndex(String errorIndex) {
			this.errorIndex = errorIndex;
		}

		public String getErrorLabel() {
			return errorLabel;
		}

		public void setErrorLabel(String errorLabel) {
			this.errorLabel = errorLabel;
		}

		public String getErrorLayer() {
			return errorLayer;
		}

		public void setErrorLayer(String errorLayer) {
			this.errorLayer = errorLayer;
		}

		public String getErrorName() {
			return errorName;
		}

		public void setErrorName(String errorName) {
			this.errorName = errorName;
		}
	}
}
