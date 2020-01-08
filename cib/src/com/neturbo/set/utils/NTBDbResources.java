package com.neturbo.set.utils;

import java.io.*;
import java.util.*;
import com.neturbo.set.database.GenericJdbcDao;
import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.*;

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
public class NTBDbResources implements NTBResources {
    private List keys;
    private List values;
    private boolean isMul;
    private String sql;
    private String keyName;
    private String valueName;
    private NTBUser user;

    public NTBDbResources() {
    }

    public NTBDbResources(String name, String key, String value, boolean isMulFlag) throws
            Exception {
        this(name, key, value, "", isMulFlag);
    }

    public NTBDbResources(String name, String key, String value,
                          String condiction, boolean isMulFlag) throws Exception {
    	isMul = isMulFlag;
        keys = new ArrayList();
        values = new ArrayList();
        sql = "SELECT " + key + " , " + value;
        if (isMul) {
            sql += " , LOCAL_CODE ";
        }
        sql += " FROM " + name;
        if (condiction != null && !condiction.equals("")) {
            sql += " WHERE 0=0 " + condiction;
        }
        this.keyName = key;
        this.valueName = value;

        if (name == null) {
            throw new Exception("Null name for resource loading");
        }
        load();
    }


    public List getKeys() {
    	if (isMul) {
        	List keyList = new ArrayList();
            for (int i = 0; i < keys.size(); i++) {
            	String key = (String) keys.get(i);
            	if (key != null && key.length() > 0) {
            		key = key.substring(0, key.length()-1);
            	}
                if (!keyList.contains(key)) {
                	keyList.add(key);
                }
            }
            return keyList;
    	} else {
    		return keys;
    	}
    }

    public String getProperty(String key) {
    	int index;
    	if (isMul) {
			String lang = user.getLangCode();
	        index = keys.indexOf(key + lang);
    	} else {
    		index = keys.indexOf(key);
    	}

        if (index != -1) {
            return (String) values.get(index);
        } else {
            return null;
        }
    }

    /**
     */
    public String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val == null) ? defaultValue : val;
    }

    /**
     */
    private void load() throws Exception {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean(
                "genericJdbcDao");
        List res = dao.query(sql, new Object[] {});
        for (int i = 0; i < res.size(); i++) {
            Map rec = (Map) res.get(i);
            String key = ((String) rec.get(keyName)).trim();
            String value = ((String) rec.get(valueName)).trim();
        	if (isMul) {
	            String langCode = ((String) rec.get("LOCAL_CODE"));
	            keys.add(key + langCode);
        	} else {
                keys.add(key);
        	}
            values.add(value);
        }
    }

    public void populate(){    	
    	// Add by Jet 2008-06-25
    	try {
    		keys.clear();
    		values.clear();
			load();
		} catch (Exception e) {			
			e.printStackTrace();
		}
    }

    public void setArgs(Object ojb) {
		if (ojb instanceof NTBUser) {
			user = (NTBUser) ojb;
		}
    }

//    public static void main(String[] args) {
//        try {
//            NTBDbResources rc = new NTBDbResources("HS_MERCHANT_NAME", "MER_CODE",
//                    "MER_SHORT_NAME", true);
//            List keys = rc.getKeys();
//            for (int i = 0; i < keys.size(); i++) {
//                System.out.println(
//                        keys.get(i)
//                        + "="
//                        + rc.getProperty((String) keys.get(i)));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
