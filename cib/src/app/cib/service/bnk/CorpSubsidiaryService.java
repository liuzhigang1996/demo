package app.cib.service.bnk;

import java.util.*;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.*;
import app.cib.bo.txn.TimeDeposit;

import com.neturbo.set.exception.*;

public interface CorpSubsidiaryService {
    public void updateSubsidiary(CorpPreference corpPref, String parentId, List oldSubsidiaryList, List subsidirayList)throws NTBException;
    public void updateForApprove(String prefId)throws NTBException;
    public void updateForReject(String prefId)throws NTBException;
    public List listSubsidiary(String corpId) throws NTBException ;
    public List listSubsidiaryDef(String corpId) throws NTBException ;
    public List listSubsidiaryDefInApproval(String corpId) throws NTBException ;
    public List listSubsidiaryByPrefId(String prefId) throws NTBException ;
    public List listCorpForAdd(String parentId, String corpId, String corpName) throws NTBException ;

}
