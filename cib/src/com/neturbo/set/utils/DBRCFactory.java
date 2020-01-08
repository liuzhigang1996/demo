package com.neturbo.set.utils;

import java.util.*;

import com.neturbo.set.core.*;
import app.cib.util.CachedDBRCFactory;

public class DBRCFactory {

    private String key = "";
    private NTBResources resourceObj = null;

    //����ResourceBundle
    private static HashMap instances = new HashMap(20);
    private static DBRCEventHandler eventHandler = null;

    /**
     *    ˽�еĹ����ӱ�֤����޷�ֱ�ӽ�����ʵ����
     */
    protected DBRCFactory(String key, String table, String keyField,
                          String valueField, String condiction, boolean isMul) {

        try {
            this.key = key;
            resourceObj = new NTBDbResources(table, keyField, valueField,
                                             condiction, isMul);
            instances.put(key, this);
        } catch (Exception e) {
            Log.error("Error loading resource from DB(" + key + ")", e);
        }
    }

    /**
     *    ˽�еĹ����ӱ�֤����޷�ֱ�ӽ�����ʵ����
     */
    protected DBRCFactory() {
    }

    /**
     *    ˽�еĹ����ӱ�֤����޷�ֱ�ӽ�����ʵ����
     */
    protected DBRCFactory(String key, NTBResources rc) {
        resourceObj = rc;
        this.key = key;
        instances.put(key, this);
    }

    public void populate() {
        resourceObj.populate();
    }


    public static void setEventHandler(DBRCEventHandler eventHandler1) {
        eventHandler = eventHandler1;
    }


    public static void populateInstance(String rcName) {
        DBRCFactory rc = (DBRCFactory) instances.get(rcName);
        if (rc != null) {
            rc.populate();
        }
    }

    /**
     *    ��������������һ������ָ�����ڲ�״̬��ʵ��
     */
    public synchronized static DBRCFactory getInstance(String key) {
        key = key.trim();
        if (instances.containsKey(key)) {
            if (eventHandler != null) {
                eventHandler.onGetInstance(key);
            }
            return (DBRCFactory) instances.get(key);
        } else {
            Log.error("DB Resources not exist(" + key + ")");
            return null;
        }
    }

    /**
     *    ��������������һ������ָ�����ڲ�״̬��ʵ��
     */
    public synchronized static String[] getInstanceNames() {
        Set keySet = instances.keySet();
        String[] retVal = new String[instances.size()];
        int index = 0;

        for (Iterator it = keySet.iterator(); it.hasNext(); ) {
            String fieldName = (String) it.next();
            retVal[index] = fieldName;
            index++;
        }
        return retVal;
    }

    //���ָ��ֵ
    public String getString(String code) {
        String retStr = "";
        if (resourceObj != null) {
            retStr = resourceObj.getProperty(code);
        } else {
            Log.warn(
                    "Error loading DB resource(" + key + ")");
            retStr = code;
        }
        if (retStr == null) {
            retStr = "";
        }
        return retStr;
    }

    //���ָ��ֵ
    public String getString(String code, String defaultValue) {
        String retStr = defaultValue;
        if (resourceObj != null) {
            retStr = resourceObj.getProperty(code);
        } else {
            Log.warn(
                    "Error loading DB resource(" + key + ")");
            retStr = defaultValue;
        }
        return retStr;
    }


    //�������ֵ
    public HashMap getAllString() {

        String keyField;
        String valueField;

        HashMap allString = new HashMap();
        try {
            List keys = resourceObj.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                keyField = (String) keys.get(i);
                valueField = resourceObj.getProperty(keyField);
                allString.put(keyField, valueField);
            }
        } catch (Exception e) {
            Log.warn("No keyField in DB resource(" + key + ")", e);
        }

        return allString;
    }

    //�������ֵ
    public ArrayList getStringArray() {

        String keyField;
        String valueField;

        ArrayList stringArray = new ArrayList();
        try {
            List keys = resourceObj.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                keyField = (String) keys.get(i);
                valueField = resourceObj.getProperty(keyField);
                stringArray.add(new String[] {keyField, valueField});
            }
        } catch (Exception e) {
            Log.warn("No keyField in DB resource(" + key + ")", e);
        }

        return stringArray;
    }

    public void setArgs(Object obj) {
        resourceObj.setArgs(obj);
    }

}
