package com.neturbo.set.database;

import com.neturbo.set.core.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.type.DateType;
import net.sf.hibernate.type.StringType;
import net.sf.hibernate.type.Type;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * @author hjs Created Date Dec 11, 2005 Modify 2006-07-24
 */
public class GenericHibernateDao extends HibernateDaoSupport {

    /**
     * ��ָ���ı����һ���µ����
     * @param myPOJO
     */
    public void add(Object myPOJO) {
        getHibernateTemplate().save(myPOJO);
    }

    /**
     * ��ѯһ��ָ�������POJO
     * @param myPOJO ������ѯ��pojo.Class
     * @param primaryKey : ����
     * @return
     */
    public Object load(Class myPOJO, Serializable primaryKey) {
        try {
            Object thePOJO = getHibernateTemplate().load(myPOJO, primaryKey);
            
            //2019-06-05 20:36:00
            getHibernateTemplate().evict(thePOJO);
            
            return thePOJO;
        } catch (Exception e) {
            Log.error("Error executing hibernate load", e);
            return null;
        }
    }
    
    /**
     * ��ѯһ��ָ�������POJO
     * @param myPOJO ������ѯ��pojo.Class
     * @param primaryKey : ����
     * @return
     */
    public Object get(Class myPOJO, Serializable primaryKey) {
        try {
            Object thePOJO = getHibernateTemplate().get(myPOJO, primaryKey);

            //2019-06-05 20:36:00
            getHibernateTemplate().evict(thePOJO);
            return thePOJO;
        } catch (Exception e) {
            Log.error("Error executing hibernate get", e);
            return null;
        }
    }

    /**
     * �г����з�ϲ�ѯ����conditionMap��POJO
     *
     * @param myPOJO ������ѯ��pojo.Class
     * @param conditionMap where�ֶμ���Ӧ��ֵ���粻��where��������conditionMap��Ϊnull
     * @return List list ���ز�ѯ����POJO����
     */
    public List list(Class myPOJO, Map conditionMap) {
        // return a POJO set
        try {
            StringBuffer myHQL = new StringBuffer("from " + myPOJO.getName()
                                                  + " as myPOJO");
            List parameters = new ArrayList();

            Iterator keyItr = conditionMap.keySet().iterator();
            boolean isWherePst = true;

            if ((conditionMap != null) && (conditionMap.size() > 0)) {
                while (keyItr.hasNext()) {
                    String fieldName = (String) keyItr.next();
                    Object obj = conditionMap.get(fieldName);
                    if(obj != null){
                        if (isWherePst) {
                            myHQL.append(" where");
                            isWherePst = false;
                        } else {
                            myHQL.append(" and");
                        }
                        myHQL.append(" myPOJO.").append(fieldName + " = ?");
                        parameters.add(obj);
                    }
                }
            }

            List qList = getHibernateTemplate().find(myHQL.toString(), parameters.toArray());
            if (qList.size() > 0) {
            	
            	//2019-06-05 20:36:00
            	for(int i=0;i<qList.size();i++){
            		Object thePOJO = qList.get(i) ;
                    getHibernateTemplate().evict(thePOJO);
            	}
            	
                return qList;
            } else {
                return new ArrayList();
            }
        } catch (Exception e) {
            Log.error("Error executing hibernate query", e);
            return null;
        }
    }

    /**
     * ���hql����ѯ��һ��pojo����
     * @param hql
     * @param valueObjArray ��object����
     * @return
     */
    public List list(String hql, Object[] valueObjArray) {
        try {
            List qList = getHibernateTemplate().find(hql, valueObjArray);

            if (qList.size() > 0) {
            	
            	//2019-06-05 20:36:00
            	for(int i=0;i<qList.size();i++){
            		Object thePOJO = qList.get(i) ;
                    getHibernateTemplate().evict(thePOJO);
            	}
            	
                return qList;
            } else {
                return new ArrayList();
            }
        } catch (Exception e){
            Log.error("Error executing hibernate query", e);
            return null;
        }

    }

    /**
     * ����һ��ָ����POJO
     * @param �����µ�pojo.Class
     * @return
     */
    public void update(Object myPOJO) {
        // update a POJO
        getHibernateTemplate().update(myPOJO);
    }

    /**
     * ɾ��һ��ָ����POJO
     * @param ��ɾ���pojo.Class
     * @return
     */
    public void delete(Object myPOJO) {
        // delete a POJO
        this.getHibernateTemplate().delete(myPOJO);
    }
    
    /**
     * ���hql�����ɾ�����
     * @param hql
     * @param preParamArray
     */
    public void delete(String hql, Object[] preParamArray) {
    	List preParamList = new ArrayList();
    	Type[] typeArray = new Type[preParamArray.length];
    	Object preParam = null;
    	for(int i=0; i<preParamArray.length; i++) {
    		preParam = preParamArray[i];
    		if(preParam instanceof java.util.Date) {
    			preParamList.add(preParam);
    			typeArray[i] = new DateType();
    		} else {
    			preParamList.add(preParam.toString());
    			typeArray[i] = new StringType();
    		}
    	}
    	this.getHibernateTemplate().delete(hql, preParamList.toArray(), typeArray);
    }

}
