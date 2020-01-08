/**
 *
 */
package app.cib.service.sys;

import java.util.List;
import java.util.Map;

import app.cib.bo.sys.CorpAccount;

import com.neturbo.set.exception.NTBException;
import app.cib.bo.sys.CorpUser;

/**
 * @author hjs
 * 2006-07-25
 */
public interface CorpAccountService {

    public static final String OPERATION_TYPE_ADD_ACC = "1";
    public static final String OPERATION_TYPE_REMOVE_ACC = "2";

    public static final String ACC_LIST_TYPE_ONLINE = "onlineAccList";
    public static final String ACC_LIST_TYPE_OFFLINE = "offlineAccList";

	public String getAccountType(String account) throws NTBException;
	public String getAccountType(String account,String ccy) throws NTBException;//add by linrui for mul-ccy

	public String getCurrency(String account) throws NTBException;
	public String getCurrency(String account, boolean isMulCcy) throws NTBException;//ADD BY linrui for mul-ccy

	public String getAccountName(String account) throws NTBException;
	public String getAccountName(String account, String ccy) throws NTBException;//add by linrui for mul-ccy

    public CorpAccount viewCorpAccount(String accountNo) throws NTBException;
    public CorpAccount viewCorpAccount(String accountNo, String ccy) throws NTBException;//add by linrui for mul-ccy

    /*
     * 锟斤拷HOST锟斤拷取锟斤拷Online锟叫憋拷锟絆ffline锟叫�锟街憋拷锟斤拷锟揭伙拷锟絤ap锟斤拷锟�
     */
    public Map getHostAccListMap(String corpID, CorpUser corpUser) throws NTBException;

    /*
     * 锟斤拷Local DB锟斤拷取锟斤拷Online锟叫憋拷锟絆ffline锟叫�锟街憋拷锟斤拷锟揭伙拷锟絤ap锟斤拷锟�
     */
    public Map getLocalAccListMap(String corpID) throws NTBException;

    /*
     * 取锟矫撅拷锟斤拷锟矫伙拷锟斤拷锟斤拷锟斤拷Online锟叫憋拷锟絆ffline锟叫�锟街憋拷锟斤拷锟揭伙拷锟絤ap锟斤拷锟�
     */
    public Map getNewAccListMap(List currOnlineAccList, List currOfflineAccList, String[] varAccs, String oppType) throws NTBException;

    /*
     * 取锟矫凤拷锟阶刺拷锟斤拷锟斤拷谋锟斤拷锟揭碉拷锟斤拷锟斤拷撕锟斤拷斜锟�
     */
    public List listLocalCorpAccount(String corpID, String status) throws NTBException;
    public List listLocalCorpAccountMap(String corpID, String status) throws NTBException;
    public List listLocalCorpAccountClass(String corpID, String status) throws NTBException;
    /*
     * 锟斤拷锟斤拷confirm锟斤拷锟斤拷时锟皆憋拷锟斤拷业锟斤拷锟斤拷锟剿号的革拷锟斤拷
     */
    public void updateLocalAccount (String userId, String corpId, List onlineAccList, List offlineAccList, String oldPrefId, String newPrefId) throws NTBException;

    /*
     * 锟斤拷锟节对碉拷锟斤拷CorpAccount Pojo锟侥革拷锟斤拷
     */
    public void updateLocalAccount(CorpAccount pojoCorpAccount) throws NTBException;

    public List listPrivilegedAccount(CorpUser user, String accType) throws NTBException;

    public List listCorpAccountByAccType(String  corpId, String accType) throws NTBException;

	public void approve(String txnType, String prefId, CorpUser corpUser) throws NTBException;

	public void reject(String txnType, String prefId, CorpUser corpUser) throws NTBException;
	
	public boolean isSavingAccount(String accountNo) throws NTBException;
	public boolean isSavingAccount(String accountNo, String ccy) throws NTBException;//add by linrui for mul-ccy
	
	// add by hjs 20070813 锟斤拷锟侥筹拷没锟斤拷欠锟斤拷锟斤拷使锟斤拷某锟绞猴拷
	public boolean checkAccountByUser(CorpUser corpUser, String checkingAccount) throws NTBException;
	
	public List listAllPrivilegedAccount(CorpUser user, String accType) throws NTBException;
	public void updateAcctName(String accountNo, String accountName,String currency) throws NTBException;

}
