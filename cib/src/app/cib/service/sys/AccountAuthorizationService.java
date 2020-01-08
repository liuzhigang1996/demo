package app.cib.service.sys;

import java.util.*;

import app.cib.bo.sys.*;

import com.neturbo.set.exception.*;
import com.neturbo.set.core.NTBAction;

public interface AccountAuthorizationService {
    public List listAll(String corpId) throws NTBException;
    public List listByAccount(String corpId, String account) throws NTBException;
    public List listByUser(String corpId, String authUser) throws NTBException;
    public List listByAccountUser(String corpId, String account, String authUser) throws NTBException;
    public List listByAccountUserHistory(String corpId, String account, String authUser) throws NTBException;
    public void addAccountAuthorizationHis(Map filedValueMapser) throws NTBException;
    public List loadHisBySeqNo(String id) throws NTBException;
    public void updateAccountAuthorizationHis(Map columnMap, Map conditionMap) throws NTBException;
    public void addAccountAuthorization(Map filedValueMapser) throws NTBException;
    public void updateAccountAuthorization(Map columnMap, Map conditionMap) throws NTBException;
    public void deleteAccountAuthorization(String recordId) throws NTBException;
}
