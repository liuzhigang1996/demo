/**
 * @author hjs
 * 2006-9-25
 */
package com.jsax.utils;

import java.util.List;
import java.util.Map;

import com.jsax.core.SubElement;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-9-25
 */
public class TxnInOverseaUtils {
	
	private static final String labelClass = "label1"; 
	private static final String contentClass = "content1"; 
	
	// special for transfer in overseas (with dynamic <input>) 
	public SubElement convertMapList2TableElement(List mapList, String labelKey, String inputFieldName, String fieldSize, String fieldMaxLength) throws NTBException {
		SubElement tableEle = new SubElement("table");
		//border="0" cellspacing="0" cellpadding="3"
		tableEle.addAttribute("width", "100%");
		tableEle.addAttribute("border", "0");
		tableEle.addAttribute("cellspacing", "0");
		tableEle.addAttribute("cellpadding", "0");
		
		Map tmpMap = null;
		String label = "";
		fieldSize = Utils.null2EmptyWithTrim(fieldSize);
		inputFieldName = Utils.null2EmptyWithTrim(inputFieldName);
		
		if (mapList != null) {
			for (int i=0; i<mapList.size(); i++) {
				tmpMap = (Map) mapList.get(i);
				label = Utils.null2EmptyWithTrim(tmpMap.get(labelKey).toString());
				
				SubElement trEle = new SubElement("tr");
				
				SubElement lableTdEle = new SubElement("td");
				lableTdEle.setAttribute("width", "25%");
				lableTdEle.setAttribute("class", labelClass);
				lableTdEle.setText(label);
				trEle.addChild(lableTdEle);
				
				SubElement inputTdEle = new SubElement("td");
				inputTdEle.setAttribute("class", contentClass);
				SubElement inputEle = new SubElement("input");
				if (!inputFieldName.equals("")) {
					inputEle.setAttribute("name", inputFieldName);
				} else {
					inputEle.setAttribute("name", label);
				}
				inputEle.setAttribute("type", "text");
				inputEle.setAttribute("value", "");
				inputEle.setAttribute("size", fieldSize);
				inputEle.setAttribute("maxlength", fieldMaxLength);
				inputTdEle.addChild(inputEle);
				trEle.addChild(inputTdEle);
				
				tableEle.addChild(trEle);
			}
			return tableEle;
		} else {
			Log.error("convertMapList2TableElement error: mapList is null");
			return null;
		}
	}
	
	// special for transfer in overseas (with dynamic <input> and <radio>) 
	public SubElement convertMapList2TableElement(List mapList, String radioName, String radioValueKey, String labelKey, String fieldSize, String fieldMaxLength) throws NTBException {
		SubElement tableEle = new SubElement("table");
		
		Map tmpMap = null;
		radioName = Utils.null2EmptyWithTrim(radioName);
		String radioValue = "";
		String label = "";
		fieldSize = Utils.null2EmptyWithTrim(fieldSize);
		
		if (mapList != null) {
			for (int i=0; i<mapList.size(); i++) {
				tmpMap = (Map) mapList.get(i);
				radioValue = Utils.null2EmptyWithTrim(tmpMap.get(radioValueKey).toString());
				label = Utils.null2EmptyWithTrim(tmpMap.get(labelKey).toString());
				
				SubElement trEle = new SubElement("tr");
				
				SubElement radioTdEle = new SubElement("td");
				radioTdEle.setAttribute("class", labelClass);
				SubElement radioEle = new SubElement("input");
				radioEle.setAttribute("name", radioName);
				radioEle.setAttribute("type", "radio");
				radioEle.setAttribute("value", radioValue);
				radioTdEle.addChild(radioEle);
				trEle.addChild(radioTdEle);
				
				SubElement lableTdEle = new SubElement("td");
				lableTdEle.setAttribute("class", labelClass);
				lableTdEle.setText(label);
				trEle.addChild(lableTdEle);
				
				SubElement inputTdEle = new SubElement("td");
				inputTdEle.setAttribute("class", contentClass);
				SubElement inputEle = new SubElement("input");
				inputEle.setAttribute("name", label);
				inputEle.setAttribute("type", "text");
				inputEle.setAttribute("value", "");
				inputEle.setAttribute("size", fieldSize);
				inputEle.setAttribute("maxlength", fieldMaxLength);
				inputTdEle.addChild(inputEle);
				trEle.addChild(inputTdEle);
				
				tableEle.addChild(trEle);
			}
			return tableEle;
		} else {
			Log.error("convertMapList2TableElement error: mapList is null");
			return null;
		}
	}
}
