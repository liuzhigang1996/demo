package app.cib.service.enq;

import app.cib.bo.enq.InterestRateBean;

import com.neturbo.set.exception.NTBException;
import java.util.*;

/**
 * @author mxl
 * 2006-09-11
 */

public interface InterestRateService {
	
	public InterestRateBean listInterestRate(String currency, String corpId) throws NTBException;
	
	// Jet added for special insterest rate 2008-06-04
	public List listSpecialInterestRate(String corpId) throws NTBException;

	public List listPeriod(String corpId) throws NTBException;
	
	public List listCurrency(String corpId) throws NTBException;

	public List listRate(String corpId, String frequency, String unit) throws NTBException;
	
}
