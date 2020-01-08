package app.cib.dao.sys;

import java.util.Date;
import java.util.List;

import app.cib.bo.sys.PinMailer;
import app.cib.util.Constants;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;


public class PinMailerDao extends GenericHibernateDao {
	
    public PinMailerDao() {
    }

    
    /**
     * add by xyf 20081222, get period for PIN mailer time
     */
    public Date getPinMailerDate(String userId) throws NTBException {
    	String hql = "from PinMailer p where p.userId=? order by p.created desc";
    	try{
    		List list = this.list(hql, new Object[]{userId});
    		if(list != null && list.size()>0){
    			PinMailer pinm = (PinMailer)list.get(0);
    			return pinm.getConfirmed();
    		}
    	}catch(Exception e){
    		Log.error("getPinMailerDate error", e);
    		throw new NTBException("err.sys.DBError");
    	}
    	return null;
}
    

}
