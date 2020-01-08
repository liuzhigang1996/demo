package com.neturbo.set.database;

import java.util.*;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.*;

/**
 * 用于统一产生不同的数字编号。
 */
public class IDGenerator {

    private static IDGenerator instance;

    //取编号
    private static String SELECT_ID =
            "SELECT Current_Id FROM ID_GENERATOR WHERE Id_Type = ? ";

    //更新数据库的最新编号
    private static String UPDATE_ID =
            "UPDATE ID_GENERATOR SET Current_Id=? WHERE Id_Type=? ";

    //插入新的编号类型
    private static String INSERT_ID =
            "INSERT INTO ID_GENERATOR (Id_Type,Current_Id) VALUES (?,?) ";
    /**
     * 构建器。
     */
    protected IDGenerator() {
        super();
    }

    public static IDGenerator getInstance(){
        if(instance == null){
            Log.debug("New IDGenerator");
            instance = new IDGenerator();
        }
        return instance;
    }

    /**
     * 获取单个编号。
     * @return int 取回的编号，取回的编号应该大于0，如果等于0则表明出错。<br>
     * @param idType java.lang.String 要返回编号类型。
     */
    public int getId(String idType) {
        return getIds(idType, 1)[0];
    }

    /**
     * 获取一组连续的编号。
     * @return int[] 取回的连续的编号，取回的编号应该大于0，如果等于0则表明出错。<br>
     * @param idType java.lang.String 要返回编号类型。
     * @param amount int 要返回编号数。
     */
    public synchronized int[] getIds(
            String idType,
            int number) {
        int[] ids = new int[number];

        Log.debug("START GET ID:" + new Date().toString());
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao)appContext.getBean("genericJdbcDao");

        for (int i = 0; i < ids.length; i++) {
            ids[i] = 0;
        }
        try {
            List idList = dao.query(SELECT_ID, new Object[] {idType});

            int currentNumber = 20000;
            if (idList == null || idList.size() == 0) {
                dao.getJdbcTemplate().update(
                        INSERT_ID,
                        new Object[] {
                        idType,
                        new Integer(currentNumber + number)});
            } else {

                Map firstRec = (Map)idList.get(0);

                    currentNumber =
                            Integer.parseInt(firstRec.get("CURRENT_ID").toString());

                dao.getJdbcTemplate().update(
                        UPDATE_ID,
                        new Object[] {
                        new Integer(currentNumber + number),
                        idType});
            }

            Log.debug("END GET ID:" + new Date().toString());

            for (int j = 0; j < ids.length; j++) {
                ids[j] = currentNumber++;
            }
        } catch (Exception e) {
            Log.error("Error generating unique Id for type " + idType, e);
        } finally {
            return ids;
        }
    }

}
