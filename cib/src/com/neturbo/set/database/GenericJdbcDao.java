package com.neturbo.set.database;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import app.cib.bo.sys.CorpUser;

import com.neturbo.set.core.*;
import com.neturbo.set.exception.NTBException;

import java.io.*;
import java.math.BigDecimal;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author hjs
 * 2005-12-17
 */
public class GenericJdbcDao extends JdbcDaoSupport {

    private Map updateMap;
    private List batchUpdateList;
    private String[] keyArray;
    private List batchQueryList;
	
    private JdbcTemplate jdbcTemplate;
   
    //private static Connection con;

    public GenericJdbcDao() {
    }

    public void add(String tableName, Map filedValueMap) throws Exception {
        Set keySet = filedValueMap.keySet();

        StringBuffer fieldsBuffer = new StringBuffer();
        StringBuffer valuesBuffer = new StringBuffer();

        for (Iterator it = keySet.iterator(); it.hasNext(); ) {
            String fieldName = (String) it.next();
            Object value = filedValueMap.get(fieldName);
            if (value != null) {
                fieldsBuffer.append(fieldName + ",");
                valuesBuffer.append("?,");
            }
        }
        String fields = fieldsBuffer.substring(0, fieldsBuffer.length() - 1);
        String values = valuesBuffer.substring(0, valuesBuffer.length() - 1);

        StringBuffer mySQL = new StringBuffer(
                "INSERT INTO "
                + tableName
                + " ("
                + fields
                + ") VALUES ("
                + values
                + ")");

        String sql = mySQL.toString();
        updateMap = filedValueMap;
        PreparedStatementSetter setter = null;
        setter = new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws
                    SQLException {
                Map columnMap = updateMap;
                Set keySet = columnMap.keySet();
                int i = 1;
                for (Iterator it = keySet.iterator(); it.hasNext(); ) {
                    String fieldName = (String) it.next();
                    Object value = columnMap.get(fieldName);
                    if (value != null) {
                        if (value instanceof java.util.Date) {
                            ps.setTimestamp(
                            		i,
                                    new java.sql.Timestamp(((java.util.Date)value).getTime())
                            		);
                            i++;
                        } else if (value instanceof BigDecimal) {
                            ps.setDouble(i, ((BigDecimal) value).doubleValue());
                            i++;
                        } else if (value instanceof Long) {
                            ps.setInt(i, ((Long) value).intValue());
                            i++;
                        } else if (value instanceof Integer) {
                            ps.setInt(i, ((Integer) value).intValue());
                            i++;
                        } else if (value instanceof Blob) { // for oracle to db2 app
                            ps.setBlob(i, (Blob) value);
                            i++;
                        } else if (value instanceof Clob) { // for oracle to db2 app
                            ps.setClob(i, (Clob) value);
                            i++;
                        } else {
                            String valueStr = value.toString();
                            try {
                                valueStr = new String(valueStr.getBytes(),
                                        Config.getProperty("DBCharset"));
                            } catch (Exception e) {
                            }
                            ps.setString(i, value.toString());
                            i++;
                        }
                    }

                }
            }
        };

        getJdbcTemplate().update(sql, setter);
    }

    public void batchAdd(String tableName, List recordList) throws Exception {

        Map columnMap = new HashMap();
        if (recordList == null || recordList.size() == 0) {
            return;
        } else {
            columnMap = (Map) recordList.get(0);
        }
        Set keySet = columnMap.keySet();

        StringBuffer fieldsBuffer = new StringBuffer();
        StringBuffer valuesBuffer = new StringBuffer();
        for (Iterator it = keySet.iterator(); it.hasNext(); ) {
            String fieldName = (String) it.next();
            Object value = columnMap.get(fieldName);
            if (value != null) {
                fieldsBuffer.append(fieldName + ",");
                valuesBuffer.append("?,");
            }
        }
        String fields = fieldsBuffer.substring(0, fieldsBuffer.length() - 1);
        String values = valuesBuffer.substring(0, valuesBuffer.length() - 1);

        StringBuffer mySQL = new StringBuffer(
                "INSERT INTO "
                + tableName
                + " ("
                + fields
                + ") VALUES ("
                + values
                + ")"
                             );

        String sql = mySQL.toString();
        batchUpdateList = recordList;
        BatchPreparedStatementSetter setter = null;
        setter = new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return batchUpdateList.size();
            }

            public void setValues(PreparedStatement ps, int index) throws
                    SQLException {
                Map columnMap = (Map) batchUpdateList.get(index);
                Set keySet = columnMap.keySet();
                int i = 1;
                for (Iterator it = keySet.iterator(); it.hasNext(); ) {
                    String fieldName = (String) it.next();
                    Object value = columnMap.get(fieldName);
                    if (value != null) {
                        if (value instanceof java.util.Date) {
                            ps.setTimestamp(
                            		i,
                            		new java.sql.Timestamp(((java.util.Date) value).getTime())
                            		);
                            i++;
                        } else if (value instanceof BigDecimal) {
                            ps.setDouble(i, ((BigDecimal) value).doubleValue());
                            i++;
                        } else if (value instanceof Long) {
                            ps.setInt(i, ((Long) value).intValue());
                            i++;
                        } else if (value instanceof Integer) {
                            ps.setInt(i, ((Integer) value).intValue());
                            i++;
                        } else {
                            String valueStr = value.toString();
                            try {
                                valueStr = new String(valueStr.getBytes(),
                                        Config.getProperty("DBCharset")).trim();
                            } catch (Exception e) {
                            }
                            ps.setString(i, value.toString());
                            i++;
                        }
                    }

                }
            }
        };

        getJdbcTemplate().batchUpdate(sql, setter);
    }


    public List batchQuery(String sql, List recordList, String[] recordKeys) throws
            Exception {

        batchQueryList = recordList;
        keyArray = recordKeys;

        //
        BatchPreparedStatementSetter setter = null;
        setter = new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return batchQueryList.size();
            }

            public void setValues(PreparedStatement ps, int index) throws
                    SQLException {
                Map columnMap = (Map) batchQueryList.get(index);
                for (int i = 0; i < keyArray.length; i++) {
                    String fieldName = keyArray[i];
                    Object value = columnMap.get(fieldName);
                    if (value != null) {
                        if (value instanceof java.util.Date) {
                            ps.setTimestamp(
                            		i + 1,
                            		new java.sql.Timestamp(((java.util.Date)value). getTime())
                            		);
                        } else if (value instanceof BigDecimal) {
                            ps.setDouble(i+1, ((BigDecimal) value).doubleValue());
                        } else if (value instanceof Long) {
                            ps.setInt(i+1, ((Long) value).intValue());
                        } else if (value instanceof Integer) {
                            ps.setInt(i+1, ((Integer) value).intValue());
                        } else {
                            String valueStr = value.toString();
                            try {
                                valueStr = new String(valueStr.getBytes(),
                                        Config.getProperty("DBCharset"));
                            } catch (Exception e) {
                            }
                            ps.setString(i+1, value.toString());
                        }
                    }

                }
            }
        };

        ResultSetExtractor extractor = null;
        extractor = new ResultSetExtractor() {
            public Object extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
                if (rs.next()) {
                    HashMap res = new HashMap();
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        String name = metaData.getColumnName(i);
                        res.put(name, rs.getObject(i));
                    }
                    return res;
                }
                return null;
            }
        };

        Object[] retArray = batchQuery(sql, setter, extractor);
        List retList = new ArrayList();
        for (int i = 0; i < retArray.length; i++) {
            retList.add(retArray[i]);
        }
        return retList;
    }

    public Object[] batchQuery(String sql,
                               final BatchPreparedStatementSetter pss,
                               final ResultSetExtractor rse) throws
            DataAccessException {

        if (logger.isDebugEnabled()) {
            logger.debug("Executing SQL batch query [" + sql + "]");
        }

        final Object[] returnValues = new Object[pss.getBatchSize()];

        jdbcTemplate = this.getJdbcTemplate();
        jdbcTemplate.execute(sql, new PreparedStatementCallback() {
            public Object doInPreparedStatement(PreparedStatement ps) throws
                    SQLException {
                ResultSet rs = null;
                try {
                    int batchSize = pss.getBatchSize();
                    for (int i = 0; i < batchSize; i++) {
                        if (pss != null) {
                            pss.setValues(ps, i);
                        }
                        rs = ps.executeQuery();
                        ResultSet rsToUse = rs;
                        NativeJdbcExtractor ne = jdbcTemplate.
                                                 getNativeJdbcExtractor();
                        if (ne != null) {
                            rsToUse = ne.getNativeResultSet(rs);
                        }
                        returnValues[i] = rse.extractData(rsToUse);
                        JdbcUtils.closeResultSet(rs);
                        if (pss instanceof ParameterDisposer) {
                            ((ParameterDisposer) pss).cleanupParameters();
                        }
                    }
                    return returnValues;
                } finally {
                    JdbcUtils.closeResultSet(rs);
                    if (pss instanceof ParameterDisposer) {
                        ((ParameterDisposer) pss).cleanupParameters();
                    }
                }
            }
        });
        return returnValues;
    }


    public void update(String tableName, Map columnMap, Map conditionMap) throws
            Exception {

        Set keySet = columnMap.keySet();
        Set keySet2 = conditionMap.keySet();
        StringBuffer fieldsBuffer = new StringBuffer();
        StringBuffer keyWordsBuffer = new StringBuffer();
        ArrayList valueList = new ArrayList();

        for (Iterator it = keySet.iterator(); it.hasNext(); ) {
            String fieldName = (String) it.next();
            fieldsBuffer.append(fieldName + "=?");
            if (it.hasNext()) {
                fieldsBuffer.append(",");
            }
            Object value = columnMap.get(fieldName);
            if(value != null){
                if (value instanceof java.util.Date) {
                    valueList.add(new java.sql.Timestamp(((java.util.Date) value).
                                                getTime()));
                } else {
                    valueList.add(value.toString());
                }
            }else{
            	valueList.add(null);
            }
        }
        String fields = fieldsBuffer.toString();

        for (Iterator it = keySet2.iterator(); it.hasNext(); ) {
            String keyWord = (String) it.next();
            keyWordsBuffer.append(" and " + keyWord + "=?");
            Object value = conditionMap.get(keyWord);
            if (value instanceof java.util.Date) {
                valueList.add(new java.sql.Timestamp(((java.util.Date) value).
                                                getTime()));
            } else {
                valueList.add(value.toString());
            }
        }
        String keyWords = keyWordsBuffer.toString();
        Object[] valueObjArray = valueList.toArray();

        StringBuffer mySQL = new StringBuffer(
                "UPDATE "
                + tableName
                + " SET "
                + fields
                + " WHERE 0=0 "
                + keyWords
                             );
        getJdbcTemplate().update(mySQL.toString(), valueObjArray);
    }

    public void update(String sql, Object[] parameters) throws Exception {

        getJdbcTemplate().update(sql, parameters);
    }
    public void execute(String sql) throws Exception {
    	
    	getJdbcTemplate().execute(sql);
    }

    public List query(String tableName, List columnList, Map conditionMap) throws
            Exception {
        StringBuffer fieldsBuffer = new StringBuffer();

        for (int i = 0; i < columnList.size(); i++) {
            if (i > 0) {
                fieldsBuffer.append(",");
            }
            fieldsBuffer.append(columnList.get(i));
        }
        String fields = fieldsBuffer.toString();

        Set conditionKeySet = conditionMap.keySet();
        StringBuffer keyWordsBuffer = new StringBuffer();
        ArrayList valueList = new ArrayList();
        for (Iterator it = conditionKeySet.iterator(); it.hasNext(); ) {
            String keyWord = (String) it.next();
            keyWordsBuffer.append(keyWord);
            if (it.hasNext()) {
                keyWordsBuffer.append("=? and ");
            }
            Object value = conditionMap.get(keyWord);
            if (value instanceof java.util.Date) {
                valueList.add(new java.sql.Timestamp(((java.util.Date) value).
                                                getTime()));
            } else {
                valueList.add(value.toString());
            }
        }
        String keyWords = keyWordsBuffer.toString();
        Object[] valueObjArray = valueList.toArray();

        String mySQL = "SELECT  "
                       + fields
                       + " FROM "
                       + tableName
                       + " WHERE "
                       + keyWords;

        return getJdbcTemplate().query(mySQL, valueObjArray,
                                       new ResultRowMapper());
    }

    public List query(String sql, Object[] valueObjArray) throws Exception {
        List list = getJdbcTemplate().query(sql, valueObjArray,
                                            new ResultRowMapper());
        return list;
    }

    public Map querySingleRow(String tableName, List columnList,
                              Map conditionMap) throws Exception {
        List list = query(tableName, columnList, conditionMap);
        if (list.size() > 0) {
            return (Map) list.get(0);
        }
        return null;
    }

    public Map querySingleRow(String sql, Object[] valueObjArray) throws
            Exception {
        List list = getJdbcTemplate().query(sql, valueObjArray,
                                            new ResultRowMapper());
        if (list.size() > 0) {
            return (Map) list.get(0);
        }
        return null;
    }

    public Map querySingleValue(String tableName, String fieldName,
                                Map conditionMap) throws Exception {
        Set conditionKeySet = conditionMap.keySet();
        StringBuffer keyWordsBuffer = new StringBuffer();
        ArrayList valueList = new ArrayList();
        for (Iterator it = conditionKeySet.iterator(); it.hasNext(); ) {
            String keyWord = (String) it.next();
            keyWordsBuffer.append(keyWord);
            if (it.hasNext()) {
                keyWordsBuffer.append("=? and ");
            }
            Object value = conditionMap.get(keyWord);
            if (value instanceof java.util.Date) {
                valueList.add(new java.sql.Timestamp(((java.util.Date) value).
                                                getTime()));
            } else {
                valueList.add(value.toString());
            }
        }
        String keyWords = keyWordsBuffer.toString();
        Object[] valueObjArray = valueList.toArray();

        String sql = "SELECT  "
                     + fieldName
                     + " FROM "
                     + tableName
                     + " WHERE "
                     + keyWords;

        List list = getJdbcTemplate().query(sql, valueObjArray,
                                            new ResultRowMapper());
        if (list.size() > 0) {
            return (Map) list.get(0);
        }
        return null;
    }

    public Object querySingleValue(String sql, Object[] valueObjArray) throws
            Exception {
        List list = getJdbcTemplate().query(sql, valueObjArray,
                                            new ResultSingleValueMapper());
        if (list.size() > 0) {
            return (Object) list.get(0);
        }
        return null;
    }

    public void removeAll(String tableName) throws Exception {
        this.getJdbcTemplate().execute("delete from " + tableName);
    }

	public List<HashMap<String, String>> findBankByName(String countryCode, String bankName) {
		String sql = "select BANK_CODE,BANK_NAME,SWIFT_CODE from HS_OVERSEAS_BANK where  COUNTRY_CODE = ?  AND BANK_NAME LIKE ?";
		bankName = bankName.toUpperCase();
		bankName = "%" + bankName + "%";
		Object[] args = new Object[]{countryCode,bankName};
		return getJdbcTemplate().query(sql, args,new ResultRowMapper());
		
	}
	
	//add by lzg for GAPMC-EB-001-0040
	/*
	public List load(Class clazz,Object[] objs,String sql) throws NTBException{
		try {
			if(con == null || con.isClosed()){
				con = getConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NTBException();
		} 
		ResultSet rs = null;
	    PreparedStatement psmt = null;
	    ArrayList retList = new ArrayList();
        try {
            psmt = con.prepareStatement(sql);
            for(int i = 0; i < objs.length; i++){
            	psmt.setObject(i+1, objs[i]);
            }
            rs = psmt.executeQuery();
            List list = populate(rs, clazz);
            return list;
        }catch (Exception e) {
        	e.printStackTrace();
        	return null;
		}finally{
			if(psmt !=null){
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new NTBException();
				}
			}
		}
	}
	
	//add by lzg for GAPMC-EB-001-0040
	private List populate(ResultSet rs, Class clazz) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount = rsmd.getColumnCount();
		List list = new ArrayList();
		List<Field> allFields = getAllFileds(clazz);
		while(rs.next()){
			Object obj = clazz.newInstance();
			for(int i = 1;i<=colCount;i++){
				 Object value = rs.getObject(i);
				 for(int j=0;j<allFields.size();j++){
					 Field f = allFields.get(j);
					 String columnName =  rsmd.getColumnName(i);
					 columnName = columnName.replace("_", "");
					 if(f.getName().equalsIgnoreCase(columnName)){
                         boolean flag = f.isAccessible();
                         try{
	                         f.setAccessible(true);
	                         f.set(obj, value);
	                         f.setAccessible(flag);
                         }catch (Exception e) {
							if(value instanceof java.math.BigDecimal){
								if(value != null){
									f.setAccessible(true);
									try{
										f.set(obj, ((BigDecimal)value).doubleValue());
									}catch (Exception e1) {
										f.set(obj, ((BigDecimal)value).intValue());
									}
									f.setAccessible(flag);
								}else{
									Log.error("parse " + f.getName() + "error!");
								}
							}
						}
                     }
				 }
			}
			list.add(obj);
		}
		return list;
	}
	//add by lzg end
	
	//add by lzg for GAPMC-EB-001-0040
	private List<Field> getAllFileds(Class clazz) {
		Class currentClass = clazz;
		List<Field> allFields = new ArrayList<Field>();
		while(currentClass.getSuperclass() != null){
			Field[] declaredFields = currentClass.getDeclaredFields();
			for (Field field : declaredFields) {
              allFields.add(field);
			}
			currentClass = currentClass.getSuperclass();
		}
		return allFields;
	}
	//add by lzg end
	
	//add by lzg for GAPMC-EB-001-0040
	public void updateByObject(String tableName, Object Obj,Map conditionMap) throws NTBException{
		try {
			if(con == null || con.isClosed()){
				con = getConnection();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new NTBException();
		}
		ResultSet rs = null;
	    PreparedStatement psmt = null;
	    String condtionSql = "";
	    try{
	    	String selectSql = "select * from " + tableName;
	    	if(conditionMap != null){
	    		condtionSql = " where 0 = 0";
	    		for(Iterator iter = conditionMap.keySet().iterator(); iter.hasNext();){
	    			String key = (String)iter.next();
	    			condtionSql = condtionSql + " and " + key + " = " + "'"
	    			+ conditionMap.get(key) + "'";
	    		}
	    	}
	    	selectSql = selectSql + condtionSql;
	    	Map retMap = querySingleRow(selectSql, null);
	    	List<Field> allFileds = getAllFileds(Obj.getClass());
	    	StringBuffer sql = new StringBuffer();
			sql.append("update " + tableName + " set ");
	    	for(Iterator iter = retMap.keySet().iterator(); iter.hasNext();){
	    		String key = (String)iter.next();
	    		if(!key.equals("SEQ_NO")){
	    		for(int i = 0; i < allFileds.size(); i++){
	    		    String fieldName = allFileds.get(i).getName();
	    			if(key.replace("_", "").equalsIgnoreCase(fieldName)){
	    				Field f = allFileds.get(i);
	    				String filedTypeName = f.getType().getName();
	    				boolean flag = f.isAccessible();
	    				f.setAccessible(true);
	    				Object value =  getValue(f,Obj);
	    				if(iter.hasNext()){
	    					if(value == null){
		    					sql.append(key + " = " + value + ",");
		    				}else if(filedTypeName.equals("java.lang.Double")){
	    						BigDecimal bigDecimal = new BigDecimal((Double)value);
	    						value = bigDecimal.toString();
	    						sql.append(key + " = " + value + ",");
		    				}else if(filedTypeName.equals("java.lang.Integer")){
		    					BigDecimal bigDecimal = new BigDecimal((Integer)value);
	    						value = bigDecimal.toString();
	    						sql.append(key + " = " + value + ",");
		    				}else{
		    					if(filedTypeName.equals("java.util.Date")){
		    						//String date = getDate(value);
		    						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSS",Locale.ENGLISH);
		    						String date = sdf.format(value);
		    						sql.append(key + " = '" + date + "',");
		    					}else{
		    						sql.append(key + " = '" + value + "',");
		    					}
		    				}
	    				}else{
	    					if(value == null){
		    					sql.append(key + " = " + value);
		    				}else if(filedTypeName.equals("java.lang.Double")){
	    						BigDecimal bigDecimal = new BigDecimal((Double)value);
	    						value = bigDecimal.toString();
		    					sql.append(key + " = " + value);
		    				}else if(filedTypeName.equals("java.lang.Integer")){
		    					BigDecimal bigDecimal = new BigDecimal((Integer)value);
	    						value = bigDecimal.toString();
		    					sql.append(key + " = " + value);
		    				}else{
		    					if(filedTypeName.equals("java.util.Date")){
		    						//String date = getDate(value);
		    						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSS",Locale.ENGLISH);
		    						String date = sdf.format(value);
		    						sql.append(key + " = '" + date + "'");
		    					}else{
		    						sql.append(key + " = '" + value + "'");
		    					}
		    				}
	    				}
	    				f.setAccessible(flag);
	    			 }
	    		}
	    		}
	    	}
	    	
	    	sql.append(condtionSql);
    		Log.info(sql);
    		psmt = con.prepareStatement(sql.toString());
    		psmt.execute();
	    }catch (Exception e) {
	    	e.printStackTrace();
	    	throw new NTBException();
		}finally{
			if(psmt !=null){
				try {
					psmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					throw new NTBException();
				}
			}
		}
		
	}
	private Object getValue(Field f,Object obj) throws NTBException{
		Object value = null;
		try{
			Class clazz = obj.getClass();
			String firstLetter = f.getName().substring(0,1).toUpperCase(); 
			String getMethodName = "get"+firstLetter+f.getName().substring(1);
			Method getMethod = clazz.getMethod(getMethodName, new Class[] {}); 
			value = getMethod.invoke(obj, new Object[] {});
		}catch (Exception e) {
			e.printStackTrace();
			throw new NTBException();
		}
		return value;
	}

	//add by lzg end
	//add by lzg for GAPMC-EB-001-0040
	/*
	public void delete(String tableName, Map conditionMapForDel) throws NTBException{
		try {
			if (con == null || con.isClosed()) {
				con = getConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NTBException();
		}
		ResultSet rs = null;
	    PreparedStatement psmt = null;
	    String sql = "delete from " + tableName;
	    if(conditionMapForDel != null){
	    	sql = sql + " where 0 = 0";
	    	for(Iterator iter = conditionMapForDel.keySet().iterator(); iter.hasNext();){
	    		String key = (String)iter.next();
	    		sql = sql + " and " + key + " = '" + conditionMapForDel.get(key) + "'";
	    	}
	    }
        try {
            psmt = con.prepareStatement(sql);
            psmt.execute();
        }catch (Exception e) {
        	throw new NTBException();
		}finally{
			if(psmt !=null){
				try {
					psmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					throw new NTBException();
				}
			}
		}
	
	}
	*/
	//add by lzg end
	//add by lzg for GAPMC-EB-001-0040
	public void updateByObject(String tableName, Object Obj,Map conditionMap) throws NTBException{
	    String condtionSql = "";
	    	String selectSql = "select * from " + tableName;
	    	if(conditionMap != null){
	    		condtionSql = " where 0 = 0";
	    		for(Iterator iter = conditionMap.keySet().iterator(); iter.hasNext();){
	    			String key = (String)iter.next();
	    			condtionSql = condtionSql + " and " + key + " = " + "'"
	    			+ conditionMap.get(key) + "'";
	    		}
	    	}
	    	selectSql = selectSql + condtionSql;
	    	Map retMap = new HashMap();
			try {
				retMap = querySingleRow(selectSql, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	List<Field> allFileds = getAllFileds(Obj.getClass());
	    	StringBuffer sql = new StringBuffer();
			sql.append("update " + tableName + " set ");
	    	for(Iterator iter = retMap.keySet().iterator(); iter.hasNext();){
	    		String key = (String)iter.next();
	    		if(!key.equals("SEQ_NO")){
	    		for(int i = 0; i < allFileds.size(); i++){
	    		    String fieldName = allFileds.get(i).getName();
	    			if(key.replace("_", "").equalsIgnoreCase(fieldName)){
	    				Field f = allFileds.get(i);
	    				String filedTypeName = f.getType().getName();
	    				boolean flag = f.isAccessible();
	    				f.setAccessible(true);
	    				Object value =  getValue(f,Obj);
	    				if(iter.hasNext()){
	    					if(value == null){
		    					sql.append(key + " = " + value + ",");
		    				}else if(filedTypeName.equals("java.lang.Double")){
	    						BigDecimal bigDecimal = new BigDecimal((Double)value);
	    						value = bigDecimal.toString();
	    						sql.append(key + " = " + value + ",");
		    				}else if(filedTypeName.equals("java.lang.Integer")){
		    					BigDecimal bigDecimal = new BigDecimal((Integer)value);
	    						value = bigDecimal.toString();
	    						sql.append(key + " = " + value + ",");
		    				}else{
		    					if(filedTypeName.equals("java.util.Date")){
		    						//String date = getDate(value);
		    						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSS",Locale.ENGLISH);
		    						String date = sdf.format(value);
		    						sql.append(key + " = '" + date + "',");
		    					}else{
		    						value = value.toString().replace("'", "''");
		    						sql.append(key + " = '" + value + "',");
		    					}
		    				}
	    				}else{
	    					if(value == null){
		    					sql.append(key + " = " + value);
		    				}else if(filedTypeName.equals("java.lang.Double")){
	    						BigDecimal bigDecimal = new BigDecimal((Double)value);
	    						value = bigDecimal.toString();
		    					sql.append(key + " = " + value);
		    				}else if(filedTypeName.equals("java.lang.Integer")){
		    					BigDecimal bigDecimal = new BigDecimal((Integer)value);
	    						value = bigDecimal.toString();
		    					sql.append(key + " = " + value);
		    				}else{
		    					if(filedTypeName.equals("java.util.Date")){
		    						//String date = getDate(value);
		    						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSS",Locale.ENGLISH);
		    						String date = sdf.format(value);
		    						sql.append(key + " = '" + date + "'");
		    					}else{
		    						sql.append(key + " = '" + value + "'");
		    					}
		    				}
	    				}
	    				f.setAccessible(flag);
	    			 }
	    		}
	    		}
	    	}
	    	
	    	sql.append(condtionSql);
    		Log.info(sql);
    		getJdbcTemplate().update(sql.toString());
	}
	
	private Object getValue(Field f,Object obj) throws NTBException{
		Object value = null;
		try{
			Class clazz = obj.getClass();
			String firstLetter = f.getName().substring(0,1).toUpperCase(); 
			String getMethodName = "get"+firstLetter+f.getName().substring(1);
			Method getMethod = clazz.getMethod(getMethodName, new Class[] {}); 
			value = getMethod.invoke(obj, new Object[] {});
		}catch (Exception e) {
			e.printStackTrace();
			throw new NTBException();
		}
		return value;
	}
	
	private List<Field> getAllFileds(Class clazz) {
		Class currentClass = clazz;
		List<Field> allFields = new ArrayList<Field>();
		while(currentClass.getSuperclass() != null){
			Field[] declaredFields = currentClass.getDeclaredFields();
			for (Field field : declaredFields) {
              allFields.add(field);
			}
			currentClass = currentClass.getSuperclass();
		}
		return allFields;
	}

	public void removeTransfer(String tableName, String transId) {
		String sql = "delete from " + tableName + " where TRANS_ID = '" + transId + "'";
		getJdbcTemplate().execute(sql);
		
	}

	public void removeCheque(String tableName, String transNo) {
		String sql = "delete from " + tableName + " where TRANS_NO = '" + transNo + "'";
		getJdbcTemplate().execute(sql);
		
	}
}
