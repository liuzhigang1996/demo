/**
 * @author js
 * 2007-8-6
 */
package app.cib.service.bnk;

import java.util.*;

import app.cib.bo.bnk.Corporation;

import com.neturbo.set.core.NTBAction;
import com.neturbo.set.exception.NTBException;

/**
 * @author js
 * 2007-8-6
 */
public interface ConfigCheckingService {
	public static String Result_Pass = "1";
	public static String Result_Iffy = "2";
	
	public List checkUsersInfo(Corporation corp) throws NTBException;
	public List checkAccLimitsInfo(String corpId, NTBAction action) throws NTBException;
	public List checkNoSettingRules(Corporation corp) throws NTBException;
	public List checkNormalRules(Corporation corp, NTBAction action) throws NTBException;

}
