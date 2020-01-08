package app.cib.action.bnk;

/**loadPendingHis
 * @author nabai
 *
 * 锟矫戯拷锟斤拷锟�锟斤拷锟斤拷锟斤拷锟睫改★拷删锟斤拷锟窖拷锟斤拷i锟斤拷锟斤拷锟斤拷锟絠锟斤拷锟斤拷锟斤拷锟杰碼
 */
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.bnk.BankUserHis;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.cription.AESUtil;
import app.cib.service.bnk.BankUserService;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Sorting;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.core.Log;

public class BankUserAction extends CibAction implements Approvable {
    BankUserService bankUserService;

    public void addLoad() throws NTBException {
        // 锟斤拷锟矫空碉拷 ResultData 锟斤拷锟斤拷锟绞撅拷锟斤拷
		//add by xyf 20081218, get PIN/Security code policy check
    	Map resultData = new HashMap();
		resultData.putAll(this.getCheckRuleMap());
		
        setResultData(resultData);
    }

    public void add() throws NTBException {
        BankUserHis userHisObj = new BankUserHis();
        this.convertMap2Pojo(this.getParameters(), userHisObj);

        BankUserService bankUserService = (BankUserService) Config
                                          .getAppContext().getBean(
                                                  "bankUserService");
        BankUser bankUser = bankUserService.load(userHisObj.getUserId());
        if (bankUser != null) {
            throw new NTBException("err.bnk.bankUserExist");
        }
        if (bankUserService.loadPendingHis(userHisObj.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        // 锟斤拷示锟斤拷锟�
        Map resultData = new HashMap();
        
		//add by xyf 20081218, get PIN/Security code policy check
		resultData.putAll(this.getCheckRuleMap());
		
        this.setResultData(resultData);
        this.convertPojo2Map(userHisObj, resultData);
        // 锟斤拷锟矫伙拷锟斤拷锟叫达拷锟絪ession锟斤拷锟皆憋拷confirm锟斤拷写锟斤拷锟斤拷菘锟�
        this.setUsrSessionDataValue("userHisObj", userHisObj);
    }

    public void addCancel() throws NTBException {
        // 锟秸猴拷锟斤拷乇嗉筹拷锟�
    }
    
    public void addConfirm() throws NTBException {
        // 锟斤拷锟揭伙拷锟斤拷锟叫达拷锟斤拷锟捷匡拷
        BankUserHis userHisObj = (BankUserHis)this
                                 .getUsrSessionDataValue("userHisObj");
        ApplicationContext appContext = Config.getAppContext();
        BankUserService bankUserService = (BankUserService) appContext.getBean(
                                                  "bankUserService");

        BankUser bankUser = bankUserService.load(userHisObj.getUserId());
        if (bankUser != null) {
            throw new NTBException("err.bnk.bankUserExist");
        }
        if (bankUserService.loadPendingHis(userHisObj.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        //使锟斤拷 BANK_USER_HIS 锟斤拷seqNo锟斤拷为锟斤拷锟斤拷锟斤拷锟斤拷ID
        String seqNo = CibIdGenerator.getIdForOperation("BANK_USER_HIS");

        /*
                String processId = flowEngineService.startProcess(
         Constants.TXN_SUBTYPE_BANK_USER_ADD, "0", BankUserAction.class,
                        null, 0, null, 0, 0, seqNo, null, getUser(),
                        null, null);

                try {
         */
        userHisObj.setSeqNo(seqNo);
        //Jet modified, not PIN mailer required any more
        userHisObj.setUserPassword(Encryption.digest(userHisObj.getUserId()+ userHisObj.getUserPassword(), "MD5"));
//        userHisObj.setUserPassword(Constants.OPERATION_RESET_PASSWORD);
        userHisObj.setOperation(Constants.OPERATION_NEW);
        //userHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
        //userHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
        userHisObj.setStatus(Constants.STATUS_NORMAL);
        userHisObj.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        userHisObj.setLastUpdateTime(new Date());
        userHisObj.setRequester(this.getUser().getUserId());
        bankUserService.addBankUserHis(userHisObj);

        // 锟睫诧拷锟斤拷锟斤拷只锟斤拷示锟斤拷锟斤拷锟斤拷
        BankUser userObj = new BankUser(userHisObj.getUserId());
        try {
            BeanUtils.copyProperties(userObj, userHisObj);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        // Todo: Change this after PIN Mailer function done
        /*
        userObj.setUserPassword(Encryption.digest(userObj.getUserId()
                                                  + "111111", "MD5"));
        */
        bankUserService.add(userObj);

        /*
                } catch (Exception e) {
                    flowEngineService.cancelProcess(processId, getUser());
                    Log.error("Error adding new bank user", e);
                    throw new NTBException("err.bnk.AddBankUserFailure");
                }
         */

        Map resultData = this.getResultData();
        resultData.put("seqNo", seqNo);

        //锟斤拷锟绞硷拷
        //Jet marked 2008-1-16
//        MailService mailService = (MailService) appContext.getBean("MailService");
//        Map mailData = new HashMap();
//        this.convertPojo2Map(userObj, mailData);
//        mailService.toBankUser_Operated(userObj.getOperation(), userObj.getUserId(), mailData);
    }

    public void updateLoad() throws NTBException {

        String userID = getParameter("userId");
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext
                          .getBean("bankUserService");

        // 锟斤拷锟斤拷没锟斤拷薷牡锟斤拷锟斤拷约锟�
        if (this.getUser().getUserId().equals(userID)) {
            throw new NTBException("err.bnk.CanNotChangeOwnId");
        }
        // 锟斤拷锟斤拷没锟斤拷锟阶刺拷锟斤拷锟斤拷锟饺�
        if (bankUserService.loadPendingHis(userID) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        // 锟斤拷询锟矫伙拷锟斤拷锟斤拷锟斤拷示
        Map resultData = new HashMap();
        BankUser bankUser = bankUserService.load(userID);

        this.convertPojo2Map(bankUser, resultData);
        setResultData(resultData);
        // 锟斤拷锟矫伙拷锟斤拷锟叫达拷锟絪ession锟斤拷锟皆憋拷confirm锟斤拷写锟斤拷锟斤拷菘锟�
        this.setUsrSessionDataValue("bankUser", bankUser);
    }

    public void update() throws NTBException {
        BankUser bankUser = (BankUser)this.getUsrSessionDataValue("bankUser");
        this.convertMap2Pojo(this.getParameters(), bankUser);
        Map resultData = new HashMap();
        this.setResultData(resultData);
        this.convertPojo2Map(bankUser, resultData);
        // 锟斤拷锟矫伙拷锟斤拷锟叫达拷锟絪ession锟斤拷锟皆憋拷confirm锟斤拷写锟斤拷锟斤拷菘锟�
        this.setUsrSessionDataValue("bankkUser", bankUser);
    }

    public void updateCancel() throws NTBException {
        // 锟秸猴拷锟斤拷乇嗉筹拷锟�
    }
    
    public void updateConfirm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext
                          .getBean("bankUserService");
        BankUser bankUser = (BankUser)this.getUsrSessionDataValue("bankUser");

        if (bankUserService.loadPendingHis(bankUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        String[] seqNo = CibIdGenerator.getIdsForOperation("BANK_USER_HIS", 2);

        //hjs before
        BankUser bankUserBefore = bankUserService.load(bankUser.getUserId());
        BankUserHis bankUserHisBefore = new BankUserHis();
        try {
            BeanUtils.copyProperties(bankUserHisBefore, bankUserBefore);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        bankUserHisBefore.setSeqNo(seqNo[0]);
        bankUserHisBefore.setOperation(Constants.OPERATION_UPDATE_BEFORE);
        bankUserHisBefore.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        bankUserHisBefore.setLastUpdateTime(new Date());
        bankUserHisBefore.setAfterModifyId(seqNo[1]);
        bankUserService.addBankUserHis(bankUserHisBefore);

        //after
        BankUserHis bankUserHis = new BankUserHis();
        try {
            BeanUtils.copyProperties(bankUserHis, bankUser);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }

        bankUserHis.setSeqNo(seqNo[1]);
        bankUserHis.setOperation(Constants.OPERATION_UPDATE);
        //bankUserHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
        //bankUserHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
        bankUserHis.setStatus(Constants.STATUS_NORMAL);
        bankUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        bankUserHis.setRequester(this.getUser().getUserId());
        bankUserHis.setLastUpdateTime(new Date());

        bankUserService.addBankUserHis(bankUserHis);

        //锟斤拷锟斤拷要锟斤拷权锟斤拷通锟斤拷锟斤拷锟�approve 锟斤拷锟�
        //this.approve(Constants.TXN_SUBTYPE_BANK_USER_EDIT, seqNo, this);

        /*
                } catch (Exception e) {
                    flowEngineService.cancelProcess(processId, getUser());
                    Log.error("Error editing bank user", e);
                    throw new NTBException("err.bnk.EditBankUserFailure");
                }
         */
        // 锟斤拷锟斤拷权直锟斤拷写锟斤拷
        BankUser userObj = bankUserService.load(bankUserHis.getUserId());
        try {
            BeanUtils.copyProperties(userObj, bankUserHis);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        bankUserService.update(userObj);

        Map resultData = new HashMap();
        setResultData(resultData);
        this.convertPojo2Map(bankUser, resultData);

        //锟斤拷锟绞硷拷
        //Jet marked 2008-1-16
//        MailService mailService = (MailService) appContext.getBean("MailService");
//        mailService.toBankUser_Operated(bankUser.getOperation(), bankUser.getUserId(), resultData);

    }

    public void setPasswordLoad() throws NTBException {
        // 锟斤拷锟矫空碉拷 ResultData 锟斤拷锟斤拷锟绞撅拷锟斤拷
		//add by xyf 20081218, get PIN/Security code policy check
    	Map resultData = new HashMap();
		resultData.putAll(this.getCheckRuleMap());
		
        setResultData(resultData);
    }

    public void setPassword() throws NTBException {
    	//add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin
    	String jCryption = this.getParameter("jCryption") ;
		String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
		//this.newSession();
		String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
		
		Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;
//		Map paraMap = this.getParameters();//mod by linrui for linux
		
		String oldPassword = (String)paraMap.get("oldPassword");
        String newPassword = (String)paraMap.get("newPassword");
      //add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end
        
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext
                          .getBean("bankUserService");
        

        BankUser userObj = (BankUser)this.getUser();
        String userId = userObj.getUserId();
        String encryptedPass = Encryption.digest(userId + oldPassword,
                                                 "MD5");
        String savedPass = userObj.getUserPassword();

        if (!savedPass.equals(encryptedPass)) {
            throw new NTBException("err.sys.PasswordError.oldPassword");
        }
        String encryptedNewPass = Encryption
                                  .digest(userId + newPassword, "MD5");
      //add by linrui 20190708
        if (encryptedNewPass.equals(savedPass)) {
            throw new NTBException("err.sys.PasswordError.duplicate");
        }
        //end
        userObj.setUserPassword(encryptedNewPass);
        bankUserService.update(userObj);
        // add by mxl 1003 写锟斤拷锟斤拷锟�
        bankUserService.loadUploadPin(userObj);

    }

    public void resetPassword() throws NTBException {

        String userID = getParameter("userId");
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext
                          .getBean("bankUserService");

        // 锟斤拷锟斤拷没锟斤拷薷牡锟斤拷锟斤拷约锟�
        if (this.getUser().getUserId().equals(userID)) {
            throw new NTBException("err.bnk.CanNotChangeOwnId");
        }
        // 锟斤拷锟斤拷没锟斤拷锟阶刺拷锟斤拷锟斤拷锟饺�
        if (bankUserService.loadPendingHis(userID) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        // 锟斤拷询锟矫伙拷锟斤拷锟斤拷锟斤拷示
        Map resultData = new HashMap();
        BankUser bankUser = bankUserService.load(userID);

        this.convertPojo2Map(bankUser, resultData);
        
        //Jet modified, no PIN mailer required any more
        resultData.put("newUserPassword", "");
        setResultData(resultData);
        // 锟斤拷锟矫伙拷锟斤拷锟叫达拷锟絪ession锟斤拷锟皆憋拷confirm锟斤拷写锟斤拷锟斤拷菘锟�
        this.setUsrSessionDataValue("bankUser", bankUser);
    }

    public void resetPasswordCancel() throws NTBException {
        // 锟秸猴拷锟斤拷乇嗉筹拷锟�
    	getResultData().put("fromPage", "1");
    }
    
    public void resetPasswordConfirm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext
                          .getBean("bankUserService");
        //String userID = getParameter("userId");
        BankUser bankUser = (BankUser) getUsrSessionDataValue("bankUser");

        if (bankUserService.loadPendingHis(bankUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        String seqNo = CibIdGenerator.getIdForOperation("BANK_USER_HIS");

        /*
                 String processId = flowEngineService.startProcess(
                Constants.TXN_SUBTYPE_BANK_USER_RESETPWD, "0",
                BankUserAction.class, null, 0, null, 0, 0,
                seqNo, null, getUser(), null, null);
                 try {
         */
        BankUserHis bankUserHis = new BankUserHis();
        try {
            BeanUtils.copyProperties(bankUserHis, bankUser);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }

        bankUserHis.setSeqNo(seqNo);
        //Jet modified, no PIN mailer required any more
        String newUserPassword = this.getParameter("newUserPassword");
        bankUserHis.setUserPassword(Encryption.digest(bankUserHis.getUserId() + newUserPassword, "MD5"));
//        bankUserHis.setUserPassword(Constants.OPERATION_RESET_PASSWORD);
        bankUserHis.setOperation(Constants.OPERATION_RESET_PASSWORD);
        bankUserHis.setStatus(Constants.STATUS_NORMAL);
        bankUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        bankUserHis.setRequester(this.getUser().getUserId());
        bankUserHis.setLastUpdateTime(new Date());

        bankUserService.addBankUserHis(bankUserHis);

       /*
                 } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error resetting bank user's password", e);
            throw new NTBException("err.bnk.ResetBankUserFailure");
                 }
         */

        // 锟斤拷锟斤拷权直锟斤拷写锟斤拷
        BankUser userObj = bankUserService.load(bankUserHis.getUserId());
        try {
            BeanUtils.copyProperties(userObj, bankUserHis);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        // Todo: Change this after PIN Mailer function done
        /*
        userObj.setUserPassword(Encryption.digest(userObj.getUserId()
                                                  + "111111", "MD5"));
*/
        bankUserService.update(userObj);

        Map resultData = new HashMap();
        //Jet modified, no PIN mailer required any more
        resultData.put("newUserPassword", newUserPassword);
        setResultData(resultData);
        this.convertPojo2Map(bankUser, resultData);

        //锟斤拷锟绞硷拷
        //Jet marked 2008-1-16
//        MailService mailService = (MailService) appContext.getBean("MailService");
//        mailService.toBankUser_Operated(bankUser.getOperation(), bankUser.getUserId(), resultData);
    }

    public void delete() throws NTBException {

        String userID = getParameter("userId");
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext
                          .getBean("bankUserService");

        // 锟斤拷锟斤拷没锟斤拷薷牡锟斤拷锟斤拷约锟�
        if (this.getUser().getUserId().equals(userID)) {
            throw new NTBException("err.bnk.CanNotChangeOwnId");
        }
        // 锟斤拷锟斤拷没锟斤拷锟阶刺拷锟斤拷锟斤拷锟饺�
        if (bankUserService.loadPendingHis(userID) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        // 锟斤拷询锟矫伙拷锟斤拷锟斤拷锟斤拷示
        Map resultData = new HashMap();
        BankUser bankUser = bankUserService.load(userID);

        this.convertPojo2Map(bankUser, resultData);
        setResultData(resultData);
        // 锟斤拷锟矫伙拷锟斤拷锟叫达拷锟絪ession锟斤拷锟皆憋拷confirm锟斤拷写锟斤拷锟斤拷菘锟�
        this.setUsrSessionDataValue("bankUser", bankUser);

    }

    public void deleteCancel() throws NTBException {
        // 锟秸猴拷锟斤拷乇嗉筹拷锟�
    	getResultData().put("fromPage", "1");
    }
    
    public void deleteConfirm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext
                          .getBean("bankUserService");
        BankUser bankUser = (BankUser)this.getUsrSessionDataValue(
                "bankUser");

        if (bankUserService.loadPendingHis(bankUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        String seqNo = CibIdGenerator.getIdForOperation("BANK_USER_HIS");

        /*
                 String processId = flowEngineService.startProcess(
                Constants.TXN_SUBTYPE_BANK_USER_DELETE, "0",
                BankUserAction.class, null, 0, null, 0, 0,
                seqNo, null, getUser(), null, null);
                 try {
         */
        BankUserHis bankUserHis = new BankUserHis();
        try {
            BeanUtils.copyProperties(bankUserHis, bankUser);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }

        bankUserHis.setSeqNo(seqNo);
        bankUserHis.setOperation(Constants.OPERATION_REMOVE);
        //bankUserHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
        //bankUserHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
        bankUserHis.setStatus(Constants.STATUS_REMOVED);
        bankUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        bankUserHis.setRequester(this.getUser().getUserId());
        bankUserHis.setLastUpdateTime(new Date());

        bankUserService.addBankUserHis(bankUserHis);

        //锟斤拷锟斤拷要锟斤拷权锟斤拷通锟斤拷锟斤拷锟�approve 锟斤拷锟�
        //this.approve(Constants.TXN_SUBTYPE_BANK_USER_DELETE, seqNo, this);

        /*
                 } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error deleting bank user", e);
            throw new NTBException("err.bnk.DeleteBankUserFailure");
                 }
         */
        // 锟斤拷锟斤拷权直锟斤拷写锟斤拷
        BankUser userObj = bankUserService.load(bankUserHis.getUserId());
        try {
            BeanUtils.copyProperties(userObj, bankUserHis);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        bankUserService.update(userObj);

        Map resultData = new HashMap();
        setResultData(resultData);
        this.convertPojo2Map(bankUser, resultData);

        //锟斤拷锟绞硷拷
        //Jet marked 2008-1-16
//        MailService mailService = (MailService) appContext.getBean("MailService");
//        mailService.toBankUser_Operated(bankUser.getOperation(), bankUser.getUserId(), resultData);
    }

    public void block() throws NTBException {
        String userID = getParameter("userId");
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext
                          .getBean("bankUserService");

        // 锟斤拷锟斤拷没锟斤拷薷牡锟斤拷锟斤拷约锟�
        if (this.getUser().getUserId().equals(userID)) {
            throw new NTBException("err.bnk.CanNotChangeOwnId");
        }
        // 锟斤拷锟斤拷没锟斤拷锟阶刺拷锟斤拷锟斤拷锟饺�
        if (bankUserService.loadPendingHis(userID) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        // 锟斤拷询锟矫伙拷锟斤拷锟斤拷锟斤拷示
        Map resultData = new HashMap();
        BankUser bankUser = bankUserService.load(userID);

        this.convertPojo2Map(bankUser, resultData);
        setResultData(resultData);
        // 锟斤拷锟矫伙拷锟斤拷锟叫达拷锟絪ession锟斤拷锟皆憋拷confirm锟斤拷写锟斤拷锟斤拷菘锟�
        this.setUsrSessionDataValue("bankUser", bankUser);
    }

    public void blockCancel() throws NTBException {
        // 锟秸猴拷锟斤拷乇嗉筹拷锟�
    	getResultData().put("fromPage", "1");
    }
    
    public void blockConfirm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext
                          .getBean("bankUserService");
        BankUser bankUser = (BankUser)this.getUsrSessionDataValue(
                "bankUser");

        if (bankUserService.loadPendingHis(bankUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        String seqNo = CibIdGenerator.getIdForOperation("BANK_USER_HIS");

        /*
                 String processId = flowEngineService.startProcess(
                Constants.TXN_SUBTYPE_BANK_USER_BLOCK, "0",
                BankUserAction.class, null, 0, null, 0, 0,
                seqNo, null, getUser(), null, null);
                 try {
         */
        BankUserHis bankUserHis = new BankUserHis();
        try {
            BeanUtils.copyProperties(bankUserHis, bankUser);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }

        bankUserHis.setSeqNo(seqNo);
        bankUserHis.setOperation(Constants.OPERATION_BLOCK);
        bankUserHis.setBlockReason(Constants.BLOCK_REASON_BY_ADMIN);
        //bankUserHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
        //bankUserHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
        bankUserHis.setStatus(Constants.STATUS_BLOCKED);
        bankUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        bankUserHis.setRequester(this.getUser().getUserId());
        bankUserHis.setLastUpdateTime(new Date());

        bankUserService.addBankUserHis(bankUserHis);

        /*
                 } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error blocking bank user", e);
            throw new NTBException("err.bnk.BlockBankUserFailure");
                 }
         */
        // 锟斤拷锟斤拷权直锟斤拷写锟斤拷
        BankUser userObj = bankUserService.load(bankUserHis.getUserId());
        try {
            BeanUtils.copyProperties(userObj, bankUserHis);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        bankUserService.update(userObj);

        Map resultData = new HashMap();
        this.convertPojo2Map(bankUser, resultData);
        setResultData(resultData);

        //锟斤拷锟绞硷拷
        //Jet marked 2008-1-16
//        MailService mailService = (MailService) appContext.getBean("MailService");
//        mailService.toBankUser_Operated(bankUser.getOperation(), bankUser.getUserId(), resultData);
    }

    public void unblock() throws NTBException {

        String userID = getParameter("userId");
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext
                          .getBean("bankUserService");

        // 锟斤拷锟斤拷没锟斤拷薷牡锟斤拷锟斤拷约锟�
        if (this.getUser().getUserId().equals(userID)) {
            throw new NTBException("err.bnk.CanNotChangeOwnId");
        }
        // 锟斤拷锟斤拷没锟斤拷锟阶刺拷锟斤拷锟斤拷锟饺�
        if (bankUserService.loadPendingHis(userID) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        // 锟斤拷询锟矫伙拷锟斤拷锟斤拷锟斤拷示
        Map resultData = new HashMap();
        BankUser bankUser = bankUserService.load(userID);

        this.convertPojo2Map(bankUser, resultData);
        setResultData(resultData);
        // 锟斤拷锟矫伙拷锟斤拷锟叫达拷锟絪ession锟斤拷锟皆憋拷confirm锟斤拷写锟斤拷锟斤拷菘锟�
        this.setUsrSessionDataValue("bankUser", bankUser);
    }

    public void unblockCancel() throws NTBException {
        // 锟秸猴拷锟斤拷乇嗉筹拷锟�
    	getResultData().put("fromPage", "1");
    }
    
    public void unblockConfirm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext
                          .getBean("bankUserService");
        BankUser bankUser = (BankUser)this.getUsrSessionDataValue(
                "bankUser");

        if (bankUserService.loadPendingHis(bankUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        String seqNo = CibIdGenerator.getIdForOperation("BANK_USER_HIS");

        /*
                 String processId = flowEngineService.startProcess(
                Constants.TXN_SUBTYPE_BANK_USER_UNBLOCK, "0",
                BankUserAction.class, null, 0, null, 0, 0,
                seqNo, null, getUser(), null, null);
                 try {
         */

        BankUserHis bankUserHis = new BankUserHis();
        try {
            BeanUtils.copyProperties(bankUserHis, bankUser);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }

        bankUserHis.setSeqNo(seqNo);
        bankUserHis.setOperation(Constants.OPERATION_UNBLOCK);
        //bankUserHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
        //bankUserHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
        bankUserHis.setStatus(Constants.STATUS_NORMAL);
        bankUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        bankUserHis.setRequester(this.getUser().getUserId());
        bankUserHis.setLastUpdateTime(new Date());

        bankUserService.addBankUserHis(bankUserHis);

        /*
                 } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error unblocking bank user", e);
            throw new NTBException("err.bnk.UnblockBankUserFailure");
                 }
         */
        // 锟斤拷锟斤拷权直锟斤拷写锟斤拷
        BankUser userObj = bankUserService.load(bankUserHis.getUserId());
        try {
            BeanUtils.copyProperties(userObj, bankUserHis);
            userObj.setLoginFailTimes(new Integer(0));
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        bankUserService.update(userObj);

        Map resultData = new HashMap();
        setResultData(resultData);
        this.convertPojo2Map(bankUser, resultData);

        //锟斤拷锟绞硷拷
        //Jet marked 2008-1-16
//        MailService mailService = (MailService) appContext.getBean("MailService");
//        mailService.toBankUser_Operated(bankUser.getOperation(), bankUser.getUserId(), resultData);

    }

    public void view() throws NTBException {
        String userId = this.getParameter("userId");

        BankUserService bankUserService = (BankUserService) Config
                                          .getAppContext().getBean(
                                                  "bankUserService");
        BankUser user = bankUserService.load(userId);
        Map resultData = new HashMap();
        this.convertPojo2Map(user, resultData);
        resultData.put("fromPage", Utils.null2EmptyWithTrim(this.getParameter("fromPage")));
        setResultData(resultData);
    }

    public void list() throws NTBException {
        BankUserService bankUserService = (BankUserService) Config
                                          .getAppContext().getBean(
                                                  "bankUserService");
        List userList = bankUserService.list();
        userList = this.convertPojoList2MapList(userList);
        
        //add by hjs 20070320
        if(userList.size()>0){
    		Locale locale = (this.getUser().getLanguage()==null) ? Config.getDefaultLocale() : this.getUser().getLanguage();
    		RBFactory rbFactory = RBFactory.getInstance("app.cib.resource.common.bank_role1", locale.toString());
            Map row = null;
            String roleId = "";
            for(int i=0; i<userList.size(); i++){
            	row = (Map) userList.get(i);
            	roleId = Utils.null2EmptyWithTrim(row.get("roleId"));
            	String roleName = rbFactory.getString(roleId);
            	row.put("roleName", roleName);
            }
            Sorting.sortMapList(userList, new String[]{"roleName", "userName"}, Sorting.SORT_TYPE_ASC);
        }
        
        Map resultData = new HashMap();
        resultData.put("userList", userList);
        setResultData(resultData);

    }

    public String viewDetail(String txnType, String id, CibAction bean) throws
            NTBException {

        BankUserService bankUserService = (BankUserService) Config
                                          .getAppContext().getBean(
                                                  "bankUserService");
        BankUserHis bankUserHis = bankUserService.loadHisBySeqNo(id);
        Map resultData = bean.getResultData();
        convertPojo2Map(bankUserHis, resultData);
        bean.setResultData(resultData);
        return "/WEB-INF/pages/bank/bank_user/bankUser_view_detail.jsp";
    }

    public boolean approve(String txnType, String id, CibAction bean) throws
            NTBException {
        BankUserService bankUserService = (BankUserService) Config
                                          .getAppContext().getBean(
                                                  "bankUserService");
        BankUserHis bankUserHis = bankUserService.loadHisBySeqNo(id);
        BankUser bankUser = new BankUser();
        if (txnType.equals(Constants.TXN_SUBTYPE_BANK_USER_ADD)) {
            bankUserHis.setStatus(Constants.STATUS_NORMAL);
            bankUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            bankUserHis.setLastUpdateTime(new Date());

            bankUser = bankUserService.load(bankUserHis.getUserId());
            bankUser.setStatus(Constants.STATUS_NORMAL);
            bankUser.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            bankUserService.update(bankUser);
        }else {
            if (txnType.equals(Constants.TXN_SUBTYPE_BANK_USER_DELETE)) {
                bankUserHis.setStatus(Constants.STATUS_REMOVED);
                bankUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            } else if (txnType.equals(Constants.TXN_SUBTYPE_BANK_USER_EDIT)) {
                bankUserHis.setStatus(Constants.STATUS_NORMAL);
                bankUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            } else if (txnType.equals(Constants.TXN_SUBTYPE_BANK_USER_BLOCK)) {
                bankUserHis.setStatus(Constants.STATUS_BLOCKED);
                bankUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            } else if (txnType.equals(Constants.TXN_SUBTYPE_BANK_USER_UNBLOCK)) {
                bankUserHis.setStatus(Constants.STATUS_NORMAL);
                bankUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
                bankUserHis.setLoginFailTimes(new Integer(0));
            } else if (txnType.equals(Constants.TXN_SUBTYPE_BANK_USER_RESETPWD)) {
                bankUserHis.setStatus(Constants.STATUS_NORMAL);
                bankUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            }
            bankUserHis.setLastUpdateTime(new Date());
            bankUserService.updateBankUserHis(bankUserHis);

            bankUser = bankUserService.load(bankUserHis.getUserId());
            try {
                BeanUtils.copyProperties(bankUser, bankUserHis);
                if (txnType.equals(Constants.TXN_SUBTYPE_BANK_USER_UNBLOCK)){
                	bankUser.setLoginFailTimes(new Integer(0));
                }
            } catch (Exception e) {
                Log.error("Error copy properties", e);
                throw new NTBException("err.sys.GeneralError");
            }
            bankUserService.update(bankUser);
        }
        return true;

    }

    public boolean reject(String txnType, String id, CibAction bean) throws
            NTBException {
        BankUserService bankUserService = (BankUserService) Config
                                          .getAppContext().getBean(
                                                  "bankUserService");
        BankUserHis bankUserHis = bankUserService.loadHisBySeqNo(id);
        bankUserHis.setStatus(Constants.STATUS_REMOVED);
        bankUserHis.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
        bankUserService.updateBankUserHis(bankUserHis);
        BankUser bankUser = new BankUser();
        if (txnType.equals(Constants.TXN_SUBTYPE_BANK_USER_ADD)) {
            try {
                BeanUtils.copyProperties(bankUser, bankUserHis);
            } catch (Exception e) {
                Log.error("Error copy properties", e);
                throw new NTBException("err.sys.GeneralError");
            }
            bankUserService.remove(bankUser);
        }
        return true;
    }

    public boolean cancel(String txnType, String id, CibAction bean) throws
            NTBException {
        return reject(txnType, id, bean);
    }

	public void listOperationHistoryLoad() throws NTBException {
		this.setResultData(new HashMap(1));
	}

	public void listOperationHistory() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BankUserService bankUserService = (BankUserService) appContext.getBean("bankUserService");

		String dateFrom = "";
		String dateTo = "";
		String requester = Utils.null2EmptyWithTrim(this.getParameter("requester"));
		String date_range = Utils.null2EmptyWithTrim(getParameter("date_range"));
		if (date_range.equals("range")) {
			if (!Utils.null2EmptyWithTrim(getParameter("dateFrom")).equals("")) {
				dateFrom = Utils.null2EmptyWithTrim(getParameter("dateFrom"));
			}
			if (!Utils.null2EmptyWithTrim(getParameter("dateTo")).equals("")) {
				dateTo = Utils.null2EmptyWithTrim(getParameter("dateTo"));
			}
		}

		List hisList = bankUserService.listOperationHistory(requester, dateFrom, dateTo);
		hisList = this.convertPojoList2MapList(hisList);

		Map resultData = new HashMap();
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("requester", requester);
		resultData.put("hisList", hisList);
		setResultData(resultData);
	}
}
