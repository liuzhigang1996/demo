/**
 * @author hjs
 * 2007-4-26
 */
package com.jsax.service.approve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.cib.bo.sys.CorpUser;
import app.cib.util.Constants;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.SubElement;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.utils.RBFactory;

/**
 * @author hjs
 * 2007-4-26
 */
public class UserListService extends JsaxAction implements JsaxService {
	
	private GenericJdbcDao genericJdbcDao;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}
	
	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public void doTransaction() throws Exception {
		//
		this.setTargetType(getParameter("targetType"));
		
		setTargetId(getParameter("targetId"));
		String roleId = getParameter("originValue");
		String subListId = getParameter("subListId");
		
		//Jet modified for CR - bank side view outstanding authorizations
		NTBUser corpUser = this.getUser();
		String corpId = "";
		if(corpUser instanceof CorpUser){
			corpId = ((CorpUser)corpUser).getCorpId();
		} else {
			corpId = getParameter("corpId");
		}

//		CorpUser corpUser = (CorpUser) this.getUser();
		
		Locale locale = (corpUser.getLanguage()==null) ? Config.getDefaultLocale() : corpUser.getLanguage();
		RBFactory rb = RBFactory.getInstance("app.cib.resource.flow.approve", locale.toString());
		
		//����һ��ѡ��
		Map headerOption = new HashMap();
		headerOption.put("USER_NAME", rb.getString("all"));
		headerOption.put("USER_ID", "0");
		
		List subList = new ArrayList();
		//mod by linrui 20190816
		/*if(!roleId.equals("0")){
			subList.addAll(this.getSubList(corpId, roleId));
		} else {
			subList.add(0, headerOption);
		}*/
		subList.add(0, headerOption);
		if(!roleId.equals("0")){
			subList.addAll(this.getSubList(corpId, roleId));
		}
		//mod by linrui end 
		//subList = this.convertPojoList2MapList(subList);
		
		//put to select
		SubElement element =  mapList2Selector(subList, subListId, "USER_NAME", "USER_ID");
		
		//���뵽select���б�
		List elementList = new ArrayList();
		elementList.add(element);
		this.addSubResponseListByDefaultType(elementList);

	}
	
	//ҵ���߼����?��
	private List getSubList(String corpId, String roleId) throws Exception {
		String sql = "select USER_ID, concat(concat(USER_ID, ' - '), USER_NAME) as USER_NAME from CORP_USER ";
		sql += "where CORP_ID = ? and ROLE_ID=?";
		return genericJdbcDao.query(sql, new Object[]{corpId, roleId});
	}

}
