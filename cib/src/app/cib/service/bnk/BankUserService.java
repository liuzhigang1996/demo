package app.cib.service.bnk;

import java.util.*;

import app.cib.bo.bnk.*;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferBank;

import com.neturbo.set.exception.*;
import com.neturbo.set.core.NTBAction;

public interface BankUserService {
    public List list()throws NTBException;
    public BankUser load(String userId)throws NTBException;
    public void add(BankUser userObj)throws NTBException;
    public void remove(BankUser userObj)throws NTBException;
    public void update(BankUser userObj)throws NTBException;
    public BankUserHis loadPendingHis(String userId) throws NTBException;
    public BankUserHis loadHisBySeqNo(String seqNo) throws NTBException;
    public void addBankUserHis(BankUserHis usrHis) throws NTBException;
    public void updateBankUserHis(BankUserHis usrHis) throws NTBException;

    //modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin -->
    /*public BankUser authenticate(String userId, String inputPassword, NTBAction action)throws NTBException;*/
    public BankUser authenticate(String userId, String inputPassword, NTBAction action, String capsLockOnFlag)throws NTBException;
    //modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB end -->
	
    public void logout(BankUser user, NTBAction action)throws NTBException;
    public void loadUploadPin(BankUser userObj) throws NTBException;
    
    public List listOperationHistory(String requester, String dateFrom, String dateTo) throws NTBException;
}

