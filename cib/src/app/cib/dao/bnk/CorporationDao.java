package app.cib.dao.bnk;

import java.util.List;

import app.cib.bo.bnk.Corporation;

import com.neturbo.set.database.*;

public class CorporationDao extends GenericHibernateDao {

    public List listAll() {
        String hql = "from Corporation as corp where corp.status <> '9' ";
        return this.list(hql, new Object[] {});
    }

    public List list(Corporation corpObj){
        String hql = "from Corporation as corp where corp.status <> '9' ";

        String corpId = corpObj.getCorpId();
        String corpName = corpObj.getCorpName();
        //modified by hjs 20080425 for case insensitive
        if (corpId != null && !corpId.trim().equals("")) {
            hql = hql + "and  upper(corp.corpId) like '%" + corpId.toUpperCase() + "%'";
        }
        if (corpName != null && !corpName.trim().equals("")) {
            hql = hql + " and upper(corp.corpName) like '%" + corpName.toUpperCase() + "%'";
        }

        List list = getHibernateTemplate().find(hql);
        return list;
    }

}
