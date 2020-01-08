package com.neturbo.set.database;

import java.util.*;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.*;

/**
 * ����ͳһ������ͬ�����ֱ�š�
 */
public class IDGenerator {

    private static IDGenerator instance;

    //ȡ���
    private static String SELECT_ID =
            "SELECT Current_Id FROM ID_GENERATOR WHERE Id_Type = ? ";

    //�������ݿ�����±��
    private static String UPDATE_ID =
            "UPDATE ID_GENERATOR SET Current_Id=? WHERE Id_Type=? ";

    //�����µı������
    private static String INSERT_ID =
            "INSERT INTO ID_GENERATOR (Id_Type,Current_Id) VALUES (?,?) ";
    /**
     * ��������
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
     * ��ȡ������š�
     * @return int ȡ�صı�ţ�ȡ�صı��Ӧ�ô���0���������0���������<br>
     * @param idType java.lang.String Ҫ���ر�����͡�
     */
    public int getId(String idType) {
        return getIds(idType, 1)[0];
    }

    /**
     * ��ȡһ�������ı�š�
     * @return int[] ȡ�ص������ı�ţ�ȡ�صı��Ӧ�ô���0���������0���������<br>
     * @param idType java.lang.String Ҫ���ر�����͡�
     * @param amount int Ҫ���ر������
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
