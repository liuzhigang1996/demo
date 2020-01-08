package app.cib.service.sys;

import com.neturbo.set.exception.NTBException;
import java.util.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface MailService {
    public void toRequester_Approved(String txnType, String[] userList, Map dataMap);
    public void toRequester_Rejected(String txnType, String[] userList, Map dataMap);
    public void toApprover_Requested(String txnType, String[] userList, Map dataMap);
    public void toApprover_Seleted(String txnType, String[] userList, Map dataMap);
    public void toApprover_Seleted(String txnType, String[] userList,String corpId, Map dataMap);
    public void toApprover_Changed(String txnType, String[] userList, Map dataMap);
    public void toMember_GroupUpdated(String groupId, Map dataMap);
    public void toMember_GroupAssigned(String userId, String groupId, Map dataMap);
    public void toCorpUser_Operated(String operation, String userId, Map dataMap);
    public void toBankUser_Operated(String operation, String userId, Map dataMap);
    public void toLastApprover_Executor(String txnType, String userId, Map dataMap);
    public void toLastApprover_Executor(String txnType, String userId, String corpId, Map dataMap);

}
