package app.cib.service.enq;

import java.util.List;

import app.cib.bo.enq.InterestRateBean;
import app.cib.dao.enq.InterestRatesDao;
import app.cib.util.Constants;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;

public class InterestRateServiceImpl implements InterestRateService{
	
	private InterestRatesDao interestRatesDao;
    private GenericJdbcDao genericJdbcDao;

	
	public InterestRateBean listInterestRate(String currency, String corpId) throws NTBException {
		 InterestRateBean iRates = new InterestRateBean();
         iRates = interestRatesDao.getInterestRateInfo(corpId, currency);
				
		return iRates;
	}
	
    // Jet added for special interest rate 2008-06-04
	public List listSpecialInterestRate(String corpId) throws NTBException{
		String sql = "SELECT INTEREST_RATE FROM HS_SPECIAL_INTEREST_RATE WHERE CORP_ID=?";
		try{
			List subList = genericJdbcDao.query(sql, new Object[] {corpId});
			return subList;
		} catch (Exception e) {
			Log.error("InterestRateServiceImpl.listSpecialInterestRate ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public List listPeriod(String corpId) throws NTBException{
//		String sql = "SELECT DISTINCT FREQUENCY, UNIT FROM HS_SPECIAL_INTEREST_RATE WHERE CORP_ID=? order by UNIT, FREQUENCY";
		String sql = "SELECT DISTINCT FREQUENCY, UNIT, PERIOD_DESCRIPTION, " +
				"CASE UNIT " +
				"WHEN 'D' THEN 1 " +
				"WHEN 'W' THEN 2 " +
				"WHEN 'M' THEN 3 " +
				"WHEN 'Y' THEN 4 " +
				"END IDX " +
				"FROM HS_SPECIAL_INTEREST_RATE WHERE CORP_ID=? order by IDX, FREQUENCY";
		try{
			List subList = genericJdbcDao.query(sql, new Object[] {corpId});
			return subList;
		} catch (Exception e) {
			Log.error("InterestRateServiceImpl.listCurrency ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}		
	}

	public List listCurrency(String corpId) throws NTBException{
		String sql = "SELECT DISTINCT CURRENCY FROM HS_SPECIAL_INTEREST_RATE WHERE CORP_ID=? order by CURRENCY";
		try{
			List subList = genericJdbcDao.query(sql, new Object[] {corpId});
			return subList;
		} catch (Exception e) {
			Log.error("InterestRateServiceImpl.listCurrency ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}		
	}
	
	public List listRate(String corpId, String frequency, String unit) throws NTBException{
		 
		String sql = "SELECT CURRENCY, INTEREST_RATE FROM HS_SPECIAL_INTEREST_RATE WHERE CORP_ID=? and FREQUENCY=? and UNIT=? order by CURRENCY";
		try{
			List subList = genericJdbcDao.query(sql, new Object[] {corpId, frequency, unit});
			return subList;
		} catch (Exception e) {
			Log.error("InterestRateServiceImpl.listRate ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}				
	}
	
	public InterestRatesDao getInterestRatesDao() {
		return interestRatesDao;
	}

	public void setInterestRatesDao(InterestRatesDao interestRatesDao) {
		this.interestRatesDao = interestRatesDao;
	}
		
    public GenericJdbcDao getGenericJdbcDao() {
        return genericJdbcDao;
    }

    public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
        this.genericJdbcDao = genericJdbcDao;
    }
	
}
