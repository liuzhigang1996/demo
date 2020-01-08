package app.cib.dao.sys;

import java.util.*;

import com.neturbo.set.database.*;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.sys.UserGroup;

public class UserGroupDao extends GenericHibernateDao{
    public UserGroupDao() {
    }
    
    public List loadUserGroup(String corpId) {
    	List permList = null;
    	permList = this.getHibernateTemplate().find(
    			"from UserGroup where   (Corp_Id=? or Corp_Id is null) ",
    			new Object[] { corpId});
    	return permList;
    }

}
