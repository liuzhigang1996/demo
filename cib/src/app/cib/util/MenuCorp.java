package app.cib.util;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.CorpUser;

import com.neturbo.set.core.NTBUser;
import com.neturbo.set.tags.MenuObject;
import com.neturbo.set.utils.DBRCFactory;
import com.neturbo.set.xml.XMLElement;

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
public class MenuCorp implements MenuObject {
    private NTBUser user;
    private XMLElement xmlElement;
    public MenuCorp() {
    }

    public void setUser(NTBUser user) {
        this.user = user;
    }

    public void setXmlElement(XMLElement xmlElement) {

        this.xmlElement = xmlElement;
    }

    public NTBUser getUser() {
        return user;
    }

    public XMLElement getXmlElement() {

        return xmlElement;
    }

    private final static String MENU_ID_CORPACCOUNT = "CorpAccount";
    private final static String MENU_ID_CORPTRANSFER = "CorpFundAllocation";
    private final static String MENU_ID_TIMEDEPOSIT = "TDTransaction";
    private final static String MENU_ID_TDENQUIRY = "TDEnquiry";
    private final static String MENU_ID_TDWITHDRAWAL = "TDWithdrawal";

    private final static String MENU_ID_ENQUIRY_CA = "CurrentAccount";
    private final static String MENU_ID_ENQUIRY_TD = "TimeDeposit";
    private final static String MENU_ID_ENQUIRY_OA = "OverdraftAccount";
    private final static String MENU_ID_ENQUIRY_LA = "LoanAccount";
    private final static String MENU_ID_ENQUIRY_SA = "SavingAccount";
    private final static String MENU_ID_ENQUIRY_CC = "CreditAccount";

    /*private final static String MENU_ID_CORP_ENQUIRY_CA = "CurrentAccount1";
    private final static String MENU_ID_CORP_ENQUIRY_TD = "TimeDeposit1";
    private final static String MENU_ID_CORP_ENQUIRY_OA = "OverdraftAccount1";
    private final static String MENU_ID_CORP_ENQUIRY_LA = "LoanAccount1";
    private final static String MENU_ID_CORP_ENQUIRY_SA = "SavingAccount1";
    private final static String MENU_ID_CORP_ENQUIRY_CC = "CreditAccount1";*/

    private final static String MENU_ID_INTEREST_RATE = "InterestRate";
    private final static String MENU_ID_EXCHANGE_RATE = "ExchangeRate";
    private final static String MENU_ID_TAX = "Tax";
    private final static String MENU_ID_WIRE_IN = "WireInOnTheWay";
    private final static String MENU_ID_CHANGE_CODE = "ChangeSecurityCode";
    
    //added by xyf 20090923
    private final static String MENU_ID_MERCHANT_ENQ = "MerchantEnquiry";
    private final static String MENU_ID_MERCHANT_ENQ_SUMMARY = "SummaryInformationEnquiry";
    private final static String MENU_ID_MERCHANT_ENQ_DETAIL = "DetailInformationEnquiry";
    
    
//    <!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB -->
    private final static String MENU_ID_CHANGE_SECURITY_CODE = "ChangeSecurityCode" ;
    //add by linrui for model1 cannot visit authorization 20190822
    private final static String MENU_ID_AUTHORIZATION = "Authorization" ;
    
    private final static String MENU_ID_ESTATEMENT = "estatement" ;

    public boolean isShowMenu() {
        boolean retValue = true;
        if (user instanceof BankUser) {
            return retValue;
        }
        CorpUser corpUser = (CorpUser) user;
        Corporation corp = corpUser.getCorporation();
        String menuId = xmlElement.getAttribute("mid");
        

        //Current Account
        /*if (MENU_ID_ESTATEMENT.equals(menuId)) {
        	if(corpUser.getCorporation().getCorpType().equals("1")){
        		if("0".equals(corpUser.getGroupId())){
        			return false;
        		}
        	}
        }*/
        
        //estatement
        if (MENU_ID_ENQUIRY_CA.equals(menuId)) {
            DBRCFactory rc = CachedDBRCFactory.getInstance("caAccountByUser");
            rc.setArgs(user);
            if (rc.getStringArray().size() == 0) {
                return false;
            }
        }

        //Time Deposit
        if (MENU_ID_ENQUIRY_TD.equals(menuId) ||
            MENU_ID_TDENQUIRY.equals(menuId) ||
            MENU_ID_TDWITHDRAWAL.equals(menuId)) {
            DBRCFactory rc = CachedDBRCFactory.getInstance("tdAccountByUser");
            rc.setArgs(user);
            if (rc.getStringArray().size() == 0) {
                return false;
            }
        }

        //Overdraft Account
        if (MENU_ID_ENQUIRY_OA.equals(menuId)) {
            DBRCFactory rc = CachedDBRCFactory.getInstance("oaAccountByUser");
            rc.setArgs(user);
            if (rc.getStringArray().size() == 0) {
                return false;
            }
        }

        //Loan Account
        if (MENU_ID_ENQUIRY_LA.equals(menuId)) {
            DBRCFactory rc = CachedDBRCFactory.getInstance("laAccountByUser");
            rc.setArgs(user);
            /*if (rc.getStringArray().size() == 0) {
                return false;
            }*/
        	return true;//mod by linrui 20190404
        }

      //Saving Account
        if (MENU_ID_ENQUIRY_SA.equals(menuId)) {
            DBRCFactory rc = CachedDBRCFactory.getInstance("saAccountByUser");
            rc.setArgs(user);
            if (rc.getStringArray().size() == 0) {
                return false;
            }
        }
        
      //Credit Card
        if (MENU_ID_ENQUIRY_CC.equals(menuId)) {
            DBRCFactory rc = CachedDBRCFactory.getInstance("ccAccountByUser");
            rc.setArgs(user);
            if (rc.getStringArray().size() == 0) {
                return false;
            }
        }

      //
        if (MENU_ID_CORPACCOUNT.equals(menuId) ||
            MENU_ID_CORPTRANSFER.equals(menuId)) {
            String corpId = corpUser.getCorpId();
            RcCorporation rc = new RcCorporation(RcCorporation.SHOW_CORP_WITHOUT_ROOT);
            if (!rc.isParentCorp(corpId)) {
                return false;
            }
        }

      //TIME DEPOSIT
        if (MENU_ID_TIMEDEPOSIT.equals(menuId)) {
            if (!"Y".equals(corp.getAllowTd())) {
                return false;
            }
        }

      //TIME DEPOSIT
        if (MENU_ID_TAX.equals(menuId)) {
            if (!"Y".equals(corp.getAllowTaxPayment())) {
                return false;
            }
        }

      //WIRE IN INFORMATION
        if (MENU_ID_INTEREST_RATE.equals(menuId) ||
            MENU_ID_EXCHANGE_RATE.equals(menuId)) {
            if (!"Y".equals(corp.getAllowDisplayBottom())) {
                return false;
            }
        }

      //CHANGE SECURITY CODE
        if (MENU_ID_WIRE_IN.equals(menuId)) {
            if (Constants.CORP_TYPE_SMALL.equals(corp.getCorpType())) {
                return false;
            }
        }

        //CHANGE SECURITY CODE
        if (MENU_ID_CHANGE_CODE.equals(menuId)) {
        	//add by linrui 20190520 uat6-92
        	if(Constants.AUTHENTICATION_SECURITY_CODE.equals(corp.getAuthenticationMode()) && Constants.CORP_TYPE_SMALL.equals(corp.getCorpType())){
        		return true;
        	}
        	//end
            if (Constants.AUTHENTICATION_CERTIFICATION.equals(corp.getAuthenticationMode()) || !Constants.ROLE_APPROVER.equals(corpUser.getRoleId())) {
                return false;
            }
        }
        
        //added by xyf 20090923, for MerchantEnquiry menu
        if (MENU_ID_MERCHANT_ENQ.equals(menuId) || 
        	MENU_ID_MERCHANT_ENQ_SUMMARY.equals(menuId) ||
        	MENU_ID_MERCHANT_ENQ_DETAIL.equals(menuId)) {
            if (null == corp.getMerchantGroup() || "".equals(corp.getMerchantGroup().trim())) {
                return false;
            }
        }

        if(MENU_ID_CHANGE_SECURITY_CODE.equals(menuId)) {
        	if (Constants.CORP_TYPE_SMALL.equals(corp.getCorpType())) {
                return false;
            }
        }
        //ADD BY LINRUI 20190822 
        if(MENU_ID_AUTHORIZATION.equals(menuId)) {
        	if (Constants.CORP_TYPE_SMALL.equals(corp.getCorpType())) {
                return false;
            }
        }
        
        
        return retValue;
    }
}
