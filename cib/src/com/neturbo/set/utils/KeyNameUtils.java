package com.neturbo.set.utils;

import java.util.*;

import org.apache.commons.beanutils.BeanUtils;

import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class KeyNameUtils {
	public KeyNameUtils() {
	}

	public static String dash2CaseDiff(String key) {
		if (key == null) {
			return null;
		}
		String[] words = Utils.splitStr(key, "_");
		key = words[0].toLowerCase();
		for (int i = 1; i < words.length; i++) {
			words[i] = words[i].substring(0, 1).toUpperCase()
					+ words[i].substring(1).toLowerCase();
			key += words[i];
		}
		return key;
	}

	public static String caseDiff2Dash(String key) {
		if (key == null) {
			return null;
		}

		byte[] chars = key.getBytes();
		key = new String(chars, 0, 1).toUpperCase();
		for (int i = 1; i < chars.length; i++) {
			String char1 = new String(chars, i, 1);
			if (char1.compareTo("A") >= 0 && char1.compareTo("Z") <= 0) {
				key += "_" + char1;
			} else {
				key += char1.toUpperCase();
			}
		}
		return key;
	}

	public static Map mapCaseDiff2Dash(Map key) {
		if (key == null) {
			return null;
		}

		Set keySet = key.keySet();
		List keyList = new ArrayList();
		for (Iterator it = keySet.iterator(); it.hasNext();) {
			keyList.add(it.next());
		}
		for (int i = 0; i < keyList.size(); i++) {
			String fieldName = (String) keyList.get(i);
			String newFieldName = caseDiff2Dash(fieldName);
			Object value = key.get(fieldName);
			key.remove(fieldName);
			if (Map.class.isInstance(value)) {
				Map valueMap = (Map) value;
				key.put(newFieldName, mapCaseDiff2Dash(valueMap));
			} else if (List.class.isInstance(value)) {
				List valueList = (List) value;
				key.put(newFieldName, listCaseDiff2Dash(valueList));
			} else {
				key.put(newFieldName, value);
			}
		}
		return key;
	}

	public static List listCaseDiff2Dash(List key) {
		if (key == null) {
			return null;
		}
		List newKey = new ArrayList();
		for (int i = 0; i < key.size(); i++) {
			Object value = key.get(i);
			if (Map.class.isInstance(value)) {
				Map valueMap = (Map) value;
				newKey.add(mapCaseDiff2Dash(valueMap));
			} else if (List.class.isInstance(value)) {
				List valueList = (List) value;
				newKey.add(listCaseDiff2Dash(valueList));
			} else {
				newKey.add(value);
			}
		}
		return key;
	}

	public static Map mapDash2CaseDiff(Map key) {
		if (key == null) {
			return null;
		}

		Set keySet = key.keySet();
		List keyList = new ArrayList();
		for (Iterator it = keySet.iterator(); it.hasNext();) {
			keyList.add(it.next());
		}
		for (int i = 0; i < keyList.size(); i++) {
			String fieldName = (String) keyList.get(i);
			Object value = key.get(fieldName);
			String newFieldName = dash2CaseDiff(fieldName);
			key.remove(fieldName);
			if (Map.class.isInstance(value)) {
				Map valueMap = (Map) value;
				key.put(newFieldName, mapDash2CaseDiff(valueMap));
			}
			if (List.class.isInstance(value)) {
				List valueList = (List) value;
				key.put(newFieldName, listDash2CaseDiff(valueList));
			} else {
				key.put(newFieldName, value);
			}
		}
		return key;
	}

	public static List listDash2CaseDiff(List key) {
		if (key == null) {
			return null;
		}
		List newKey = new ArrayList();
		for (int i = 0; i < key.size(); i++) {
			Object value = key.get(i);
			if (Map.class.isInstance(value)) {
				Map valueMap = (Map) value;
				newKey.add(mapDash2CaseDiff(valueMap));
			} else if (List.class.isInstance(value)) {
				List valueList = (List) value;
				newKey.add(listDash2CaseDiff(valueList));
			} else {
				newKey.add(value);
			}
		}
		return newKey;
	}

	/**
	 * 
	 * @param pojoList
	 * @return
	 * @throws NTBException
	 */
	public static List convertPojoList2MapList(List pojoList)
			throws NTBException {
		try {
			List mapList = new ArrayList();
			for (int i = 0; i < pojoList.size(); i++) {
				Object pojo = pojoList.get(i);
				mapList.add(BeanUtils.describe(pojo));
			}
			return mapList;
		} catch (Exception e) {
			Log.warn("Error reading values from POJO", e);
		}
		return null;
	}

	/**
	 * 
	 * @param valueMap
	 * @param pojo
	 * @throws NTBException
	 */
	public static void convertMap2Pojo(Map valueMap, Object pojo)
			throws NTBException {
		try {
			BeanUtils.populate(pojo, valueMap);
		} catch (Exception e) {
			Log.warn("Error writing values to POJO", e);
		}
	}

	public static void convertPojo2Map(Object pojo, Map valueMap)
			throws NTBException {
		try {
			valueMap.putAll(BeanUtils.describe(pojo));
		} catch (Exception e) {
			Log.warn("Error reading values from POJO", e);
		}
	}

	public static String fillWith(String str,String fillWith,int length, boolean isLeft) {
		str = null2Empty(str) ;
		if(isLeft){
			while(str.length()<length){
				str = fillWith + str ;
			}
			return str ;
		}
		while(str.length()<length){
			str += fillWith ;
		}
		return str ;
	}
	
	public static String null2Empty(String str) {
		if(null==str){
			return "" ;
		}
		return str.trim() ;
	}
	
	
	public static void main(String args[]) {
		List list1 = new ArrayList();
		Map map1 = new HashMap();
		map1.put("keyWordTest", "1111111");
		map1.put("keyWord123", "1111112");
		list1.add(map1);
		Map map2 = new HashMap();
		map2.put("keyMain2", "1111113");
		map2.put("keyMainGo", "1111114");
		list1.add(map2);

		Map mapMain = new HashMap();
		mapMain.put("run", list1);
		mapMain.put("nokey", "dfhoiuyiu34");

		System.out.println(KeyNameUtils.fillWith("eeeeffff", "0", 10, false)) ;
		System.out.println(mapCaseDiff2Dash(mapMain));
	}

}
