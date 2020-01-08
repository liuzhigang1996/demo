/**
 * @author hjs
 * 2007-5-29
 */
package app.cib.service.sys;

import java.util.List;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Format;

/**
 * @author hjs
 * 2007-5-29
 */
public class TransAmountServiceImpl implements TransAmountService {
	
	private GenericJdbcDao genericJdbcDao;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}
	
	public void checkMinTransAmt(String ccyCode, double transAmt)
			throws NTBException {
		String sql = "select MIN_TRANS_AMT from HS_CURRENCY where CCY_CODE = ?";
		try {
			Object minAmount = genericJdbcDao.querySingleValue(sql, new Object[]{ccyCode});
			if(transAmt < Double.parseDouble(minAmount.toString())){
				throw new NTBException("err.sys.MinTransAmtError", new Object[]{ccyCode, Format.formatAmount(minAmount.toString())});
			}
		} catch (Exception e) {
			Log.error("DB error", e);
			if(e instanceof NTBException){
				throw (NTBException)e;
			} else {
				throw new NTBException("err.sys.DBError");
			}
		}

	}

	public void checkMinAmtOtherBanks(String ccyCode, double transAmt)
			throws NTBException {
		String sql = "select MIN_AMT_OTHER_BANKS from HS_CURRENCY where CCY_CODE = ?";
		try {
			Object minAmount = genericJdbcDao.querySingleValue(sql, new Object[]{ccyCode});
			if(transAmt < Double.parseDouble(minAmount.toString())){
				throw new NTBException("err.sys.MinAmtOtherBanksError", new Object[]{ccyCode, Format.formatAmount(minAmount.toString())});
			}
		} catch (Exception e) {
			Log.error("DB error", e);
			if(e instanceof NTBException){
				throw (NTBException)e;
			} else {
				throw new NTBException("err.sys.DBError");
			}
		}
	}

}
