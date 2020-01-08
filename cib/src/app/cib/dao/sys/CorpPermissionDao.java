package app.cib.dao.sys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.bouncycastle.jce.provider.JDKKeyFactory.RSA;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;
import app.cib.bo.sys.CorpPermission;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibIdGenerator;
import app.cib.util.*;

import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.exception.*;

public class CorpPermissionDao extends GenericHibernateDao {
	//add by linrui 
	private DataSource dataSource;
	private static Connection con;
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	//end
	
    public List loadFuncPermByGroup(String roleId, String corpId,
                                    String groupId) {
    	//add by linrui 20171128
//    	ApplicationContext appContext = Config.getAppContext();
//    	GenericJdbcDao dao = (GenericJdbcDao) appContext
//		.getBean("genericJdbcDao");
        	List permList = null;       
        if (groupId.equals("0")) {
            permList = this.getHibernateTemplate().find(
                    "from CorpPermission where Permission_Type='0' and Role_Id=? and Group_Id ='0'  and Status='0'",
                    new Object[] {roleId}); 
        	//add by linrui 20171128
//             try{
//             permList = getPermList();
//             }catch (Exception e) {			
//			}
        	//end
        } else {
        	if(Constants.ROLE_OPERATOR_APPROVER.equals(roleId) 
        			|| Constants.ROLE_OPERATOR_APPROVER_ADMIN.equals(roleId)
        			|| Constants.ROLE_APPROVER_ADMIN.equals(roleId)){
        		//roleId = Constants.ROLE_APPROVER;
                permList = this.getHibernateTemplate().find(
                        "from CorpPermission where Permission_Type='0'  and ((Corp_Id=? and Group_Id =?) or (Corp_Id='BASIC' and Group_Id ='0' and Role_Id=?))  and Status='0'",
                        new Object[] {corpId, groupId, roleId});
        	}else{
                permList = this.getHibernateTemplate().find(
                        "from CorpPermission where Permission_Type='0' and Role_Id=? and ((Corp_Id=? and Group_Id =?) or (Corp_Id='BASIC' and Group_Id ='0'))  and Status='0'",
                        new Object[] {roleId, corpId, groupId});
            }
        }
        return permList;             
    }

    public List loadAccPermByGroup(String corpId, String groupId) {
        List permList = null;
        if (groupId.equals("0")) {
            permList = this.getHibernateTemplate().find(
                    "from CorpPermission where Permission_Type='1' and Corp_Id=? and Group_Id='0' and Status='0'",
                    new Object[] {corpId});
        } else {
            permList = this.getHibernateTemplate().find(
                    "from CorpPermission where Permission_Type='1' and Corp_Id=? and Group_Id=? and Status='0'",
                    new Object[] {corpId, groupId});
        }
        return permList;
    }

    public void addAccForDefaultGrp(String corpId, String accNo) {
        String permissionId = CibIdGenerator
                              .getIdForOperation("CORP_PERMISSION");
        CorpPermission corpPermission = new CorpPermission();
        corpPermission.setCorpId(corpId);
        corpPermission.setGroupId("0");
        corpPermission.setPermissionId(permissionId);
        corpPermission.setPermissionResource(accNo);
        corpPermission.setPermissionType(CorpPermission.
                                         PERMISSION_TYPE_ACCOUNT);
        corpPermission.setRoleId("0");
        corpPermission.setStatus(Constants.STATUS_NORMAL);
        corpPermission.setVersion(new Integer(1));
        add(corpPermission);
    }

    public void delAccForDefaultGrp(String corpId, String accNo) {
        getHibernateTemplate().delete(
                "from CorpPermission where Corp_Id=? and Permission_Type=? and Permission_Resource=?",
                new Object[] {corpId, CorpPermission.PERMISSION_TYPE_ACCOUNT,
                accNo}, new Type[] {Hibernate.STRING,Hibernate.STRING,
                Hibernate.STRING});
    }

    public void deleteByGroupVersion(String groupId, Integer ver) throws NTBException  {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao)appContext.getBean("genericJdbcDao");
        try{
            dao.update(
                    "update Corp_Permission set status=? where Group_Id=? and Version = ?",
                    new Object[] {Constants.STATUS_REMOVED,groupId, ver});
        }catch(Exception e){
            Log.error("Error update group's permission", e);
            throw new NTBException("err.sys.DBError");
        }
    }

    public void deleteByGroupStatus(String groupId, String  status) throws NTBException  {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao)appContext.getBean("genericJdbcDao");
        try{
            dao.update(
                    "update Corp_Permission set status=? where Group_Id=? and Status = ?",
                    new Object[] {Constants.STATUS_REMOVED,groupId, status});
        }catch(Exception e){
            Log.error("Error update group's permission", e);
            throw new NTBException("err.sys.DBError");
        }
    }

    public void updateByGroupVersion(String groupId, Integer ver) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao)appContext.getBean("genericJdbcDao");
        try{
            dao.update(
                    "update Corp_Permission set status=? where Group_Id=? and Version = ?",
                    new Object[] {Constants.STATUS_NORMAL,groupId, ver});
        }catch(Exception e){
            Log.error("Error update group's permission", e);
            throw new NTBException("err.sys.DBError");
        }
    }
//    public List getPermList() throws SQLException{
//    	String driver = "oracle.jdbc.driver.OracleDriver";
//    	String url = "jdbc:oracle:thin:@localhost:1521:bobtemplate";
//    	String user = "lam";
//    	String password = "123456";
////    	  con = (Connection) DriverManager.getConnection(url, user, password);
//    	List permList=null;
//    	int i =0;
//    	try{
//    	if (con == null || con.isClosed()) {
//			con = (Connection) DriverManager.getConnection(url, user, password);
//		}
//    	PreparedStatement stat = con.prepareStatement("select * from CORPORATION where crop_id = 'BOBSITTST2'");//select * from Corp_Permission where Permission_Type='0' and Role_Id='1' and Group_Id ='0'  and Status='0'
//    	ResultSet rs = stat.executeQuery();
//    	while(rs.next()){
//    		permList.add(rs.getObject(i));
//    		i++;
//    		}
//    	}catch (Exception e) {   		
//				if (con == null || con.isClosed()) {
//				} else {
//				con.close();}				
//    	}finally{
//			con.close();
//		    }
//    	return permList;
//    }
}
