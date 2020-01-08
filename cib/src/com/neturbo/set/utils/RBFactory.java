package com.neturbo.set.utils;

import java.util.*;
import com.neturbo.set.core.*;

public class RBFactory {

    private String fileName = "";
    //缺省地区代码为中国
    private String localeCode = "zh_CN";
    private Locale locale = null;
    private NTBProperties resourceBundle = null;
    //储存ResourceBundle
    private static HashMap instances = new HashMap(20);
    /**
     *    私有的构造子保证外界无法直接将此类实例化
     */
    private RBFactory(String fileName, String localCode) {

        try {
            this.localeCode = localCode;
            this.fileName = fileName;
            //如果
            if (localeCode != null) {
                String language = localCode.substring(0, 2);
                String region = localCode.substring(3, 5);
                locale = new Locale(language, region);
                resourceBundle = new NTBProperties(fileName, locale);
                instances.put(fileName + "_" + localeCode, this);
            } else {
                resourceBundle = new NTBProperties(fileName);
                instances.put(fileName, this);
            }
        } catch (Exception e) {
            Log.error("Resource not exist (" + fileName + ")", e);
        }
    }

    /**
     *    私有的构造子保证外界无法直接将此类实例化
     */
    private RBFactory() {
    }

    /**
     *    工厂方法，返还一个具有指定的内部状态的实例
     */
    public synchronized static RBFactory getInstance(
            String fileName,
            String localeCode) {
        fileName = fileName.trim();
        localeCode = localeCode.trim();
        if (instances.containsKey(makeKey(fileName, localeCode))) {
            return (RBFactory) instances.get(makeKey(fileName, localeCode));
        } else {
            return new RBFactory(fileName, localeCode);
        }
    }

    /**
     *    工厂方法，返还一个具有指定的内部状态的实例
     */
    public synchronized static RBFactory getInstance(String fileName) {
        fileName = fileName.trim();
        if (instances.containsKey(fileName)) {
            return (RBFactory) instances.get(fileName);
        } else {
            return new RBFactory(fileName, null);
        }
    }

    //获得指定值
    public String getString(String code) {
        String retStr = "";
        if (resourceBundle != null) {
                retStr = resourceBundle.getProperty(code);
                retStr = (retStr == null) ? code : retStr;
        } else {
            Log.warn(
                    "Error loading resource(" + fileName + ")");
            retStr = code;
        }
        return retStr;
    }

    //获得指定值
    public String getString(String code, String defaultValue) {
        String retStr = "";
        if (resourceBundle != null) {
            retStr = resourceBundle.getProperty(code);
            retStr = (retStr == null) ? defaultValue : retStr;
        } else {
            Log.warn(
                    "Error loading resource(" + fileName + ")");
            retStr = defaultValue;
        }
        return retStr;
    }

    //获得所有值
    public HashMap getAllString() {

        String key;
        String value;

        HashMap allString = new HashMap();
        try {
            List keys = resourceBundle.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                key = (String) keys.get(i);
                value = resourceBundle.getProperty(key);
                allString.put(key, value);
            }
        } catch (Exception e) {
            Log.warn("No key in properties(" + fileName + ")", e);
        }

        return allString;
    }

    //获得所有值
    public ArrayList getStringArray() {

        String key;
        String value;

        ArrayList stringArray = new ArrayList();
        try {
            List keys = resourceBundle.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                key = (String) keys.get(i);
                value = resourceBundle.getProperty(key);
                stringArray.add(new String[] {key, value});
            }
        } catch (Exception e) {
            Log.warn("No key in properties(" + fileName + ")", e);
        }

        return stringArray;
    }

    //构造搜索关键字
    private static String makeKey(String fileName, String localeCode) {
        return fileName + "_" + localeCode;
    }
}
