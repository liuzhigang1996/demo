package app.cib.service.sys;

import java.util.*;

import app.cib.bo.sys.*;
import com.neturbo.set.exception.*;

public interface ApproveRuleService {
	ApproveRule getApproveRule(String txnType, String corpId, String currency,
			double amount, double amountMopEq) throws NTBException;

	boolean checkApproveRule(String corpId, Map data) throws NTBException;

	public List list(ApproveRule approveRuleObj) throws NTBException;

	public List listAllTxnType(ApproveRule approveRuleObj) throws NTBException;
	
	public List listAll(ApproveRule approveRuleObj) throws NTBException;

    public List listAllPending(String corpId) throws NTBException;
	
	public List listByPref(String prefId) throws NTBException;

    public List listAllByPref(String prefId) throws NTBException;

    public String getCorpId(String prefId) throws NTBException;

	public Map mapRuleStatus(String corpId) throws NTBException;

	public void setRules(CorpPreference corpPref, String corpId, List oldRuleList, List ruleList)
			throws NTBException;

	public void updateForApprove(String prefId) throws NTBException;

	public void updateForReject(String prefId) throws NTBException;
    
    public void checkUsability(String corpId, Object[] ccyArray) throws NTBException;
    
    public void removeApproveRule(String corpId, Object[] ccyArray) throws NTBException;
    // add by hjs 20071024
    public void initApproveRule(String requester, String corpId, Object[] ccyArray) throws NTBException;
}
