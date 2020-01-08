package app.cib.service.sys;

import java.util.*;

import app.cib.bo.sys.*;

import com.neturbo.set.exception.*;
import com.neturbo.set.core.NTBAction;

public interface CorpUserService {
    public List listAllUser()throws NTBException;
    public List listUserByCorp(String corpId)throws NTBException;
    public List listUserHisByPerfId(String prefId)throws NTBException;
    public List listNormalByCorp(String corpId) throws NTBException;
    public List listUserByLevel(String corpId, String level)throws NTBException;
    public CorpUser load(String userId)throws NTBException;
    public void add(CorpUser userObj)throws NTBException;
    public void remove(CorpUser userObj)throws NTBException;
    public void update(CorpUser userObj)throws NTBException;
    public void addCorpUserHis(CorpUserHis corpUserHis) throws NTBException;
    //modified by lzg for GAPMC-EB-001-0040
    //public CorpUserHis loadPendingHis(String userId) throws NTBException;
    public CorpUserHis loadPendingHisWithCorpId(String userId,String corpId) throws NTBException;
    //modified by lzg end
    public CorpUserHis loadHisBySeqNo(String seqNo) throws NTBException;
    public CorpUser  loadBySeqNo(String seqNo) throws NTBException;
    public void updateCorpUserHis(CorpUserHis corpUserHis)throws NTBException;
    
    /* Modify by long_zg 2019-05-16 for otp login begin */
    //modified by lzg for GAPMC-EB-001-0040
    /*public CorpUser authenticate(String userId,String corpId, String inputPassword, NTBAction action,Map paraMap)throws NTBException;*/
    public CorpUser authenticate(String userId,String corpId, String inputPassword,String smsFlowNo,String otp, NTBAction action,Map paraMap, boolean isChgFirLoginPass)throws NTBException;
    //modified by lzg end
    /* Modify by long_zg 2019-05-16 for otp login begin */
    
    public void logout(CorpUser user, NTBAction action)throws NTBException;
    // add by mxl 0930 
    public void loadUploadPin(CorpUser userObj) throws NTBException;
    
    public List listUserHisByAfterId(String afterModifyId) throws NTBException;
    
    public List listByRoleId(String corpId, String roleId, String status) throws NTBException;
    
    //add by xyf 20081222, check period for PIN mailer time
    public boolean isInitPinActive(String userId, String corpId, Date loginTime) throws NTBException;
    
    /**
	 * by wen 20110803
	 * get security alert message
	 * @param userId
	 * @return
	 * @throws NTBException
	 */
	public String getSecurityAlertMsg(String userId, String corpType) throws NTBException;
	//add by lzg for GAPMC-EB-001-0040
	public CorpUser loadWithCorpId(String userId, String corpId) throws NTBException;
	//add by lzg end
	
	 public void updateAddPrintInfo(CorpUser userObj, String operatorId)throws NTBException;
	 
	 public void addPrintInfo(CorpUser userObj, String operatorId)throws NTBException;
	 //add by linrui 20190815 for CR0001-598
	 public boolean checkIdDuplicate(CorpUserHis corpUser)throws NTBException;
}
