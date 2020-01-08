/**
 *
 */
package app.cib.service.enq;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;


import app.cib.bo.enq.LceBean;
import app.cib.core.CibTransClient;
import app.cib.dao.enq.ExchangeRatesDao;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-07-20
 */
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

	/**
	 * @param args
	 */

	private ExchangeRatesDao exchangeRatesDao;
	
	//add by lzg for getExchangeRateFromHost
	public static final String RATE_TYPE_NO_RATE = "0";
	public static final String RATE_TYPE_ALL_RATE = "3";
	public static final String RATE_TYPE_BUY_RATE = "1";
	public static final String RATE_TYPE_SELL_RATE = "2";
	
	public static final String RATE_TYPE = "rateType";
	public static final String BUY_RATE = "buyRate";
	public static final String SELL_RATE = "sellRate";
	//add by lzg end
	public ExchangeRatesDao getExchangeRatesDao() {
		return exchangeRatesDao;
	}

	public void setExchangeRatesDao(ExchangeRatesDao exchangeRatesDao) {
		this.exchangeRatesDao = exchangeRatesDao;
	}


	/**
	 * list the exchange rate by Zone_Code and CorpID
	 * 根据地区代码和企业ID显示调整过后的货币汇率
	 * @param zoneCode:地区代码(ex:AP(亚太地区)), corpID:企业ID
	 * @return exchangeRatesList:货币汇率列表
	 */
	public List listERateByZoneCode(String zoneCode, String corpID)
			throws NTBException {
		
		List exchangeRateList = null;
		exchangeRateList = exchangeRatesDao.listERateByZoneCode(zoneCode, corpID);
		
		return exchangeRateList;
	}

	/**
	 * 通过汇率换算金额
	 * @param corpID:企业ID, fromCCY:转入货币类型, toCCY:转出货币类型, fromAmount:转入货币金额
	 * @return toAmount:转出货币金额
	 */
	public BigDecimal getEquivalent(String corpID, String fromCCY, String toCCY, BigDecimal fromAmount, BigDecimal toAmount, int scale) throws NTBException {
		
		// Jet modified 2009-01-05
		if (fromAmount == null){
			scale = exchangeRatesDao.getScale(fromCCY);
		}
		
		if (toAmount == null){
			scale = exchangeRatesDao.getScale(toCCY);
		}
		return exchangeRatesDao.getEquivalent(corpID, fromCCY, toCCY, fromAmount, toAmount, scale);
	}
	//add by lzg 20190507 for getExchangeRateFromHost
	public BigDecimal getEquivalentFromHostRate(String corpID, String fromCCY, String toCCY, BigDecimal fromAmount, BigDecimal toAmount, int scale,Map exchangeMap) throws NTBException {
		
		// Jet modified 2009-01-05
		if (fromAmount == null){
			scale = exchangeRatesDao.getScale(fromCCY);
		}
		
		if (toAmount == null){
			scale = exchangeRatesDao.getScale(toCCY);
		}
		return getEquivalentByRateFromHost(corpID, fromCCY, toCCY, fromAmount, toAmount, scale,exchangeMap);
	}
	//add by lzg end
	//add by lzg 20190507 for getExchangeRateFromHost
	private BigDecimal getEquivalentByRateFromHost(String corpID,
			String fromCCY, String toCCY, BigDecimal fromAmount,
			BigDecimal toAmount, int scale,Map exchangeMap) throws NTBException {
		corpID = Utils.null2EmptyWithTrim(corpID);
		fromCCY = Utils.null2EmptyWithTrim(fromCCY);
		toCCY = Utils.null2EmptyWithTrim(toCCY);

		if (scale < 0) {
			scale = 8;
		} else if (scale > 8) {
			scale = 8;
		}

		if ((!fromCCY.equals("")) && (!toCCY.equals(""))) {
			
			if ((fromAmount != null) || (toAmount != null)){

				Map exchRateMap = null;
				String rateType = "";
				BigDecimal amount = null;

				fromCCY = fromCCY.toUpperCase();
				toCCY = toCCY.toUpperCase();

				if (fromCCY.equals(toCCY)) {
					amount = (fromAmount!=null) ? fromAmount : toAmount;

				} else {
					exchRateMap = exchangeMap;
					rateType = exchRateMap.get(RATE_TYPE).toString();

					if (rateType.equals(RATE_TYPE_NO_RATE)) {
						amount = new BigDecimal("-1");
					} else if (rateType.equals(RATE_TYPE_BUY_RATE)) {
						if (fromAmount != null) {
							amount = fromAmount.multiply(new BigDecimal(exchRateMap.get(BUY_RATE).toString()));	
						} else if (toAmount != null) {
							amount = toAmount.divide(new BigDecimal(exchRateMap.get(BUY_RATE).toString()), scale, BigDecimal.ROUND_HALF_UP);						
						}
					} else if (rateType.equals(RATE_TYPE_SELL_RATE)) {
						if (fromAmount != null) {
							amount = fromAmount.divide(new BigDecimal(exchRateMap.get(SELL_RATE).toString()), scale, BigDecimal.ROUND_HALF_UP);
						} else if (toAmount != null) {
							amount = toAmount.multiply(new BigDecimal(exchRateMap.get(SELL_RATE).toString()));							
						}
					} else if (rateType.equals(RATE_TYPE_ALL_RATE)) {
						if (fromAmount != null) {
							amount = fromAmount.multiply(new BigDecimal(exchRateMap.get(BUY_RATE).toString()));
							amount = amount.divide(new BigDecimal(exchRateMap.get(SELL_RATE).toString()), scale, BigDecimal.ROUND_HALF_UP);
						} else if (toAmount != null) {
							amount = toAmount.multiply(new BigDecimal(exchRateMap.get(SELL_RATE).toString()));						
							amount = amount.divide(new BigDecimal(exchRateMap.get(BUY_RATE).toString()), scale, BigDecimal.ROUND_HALF_UP);
						}
						//amount = fromAmount.multiply(new BigDecimal(exchRateMap.get(BUY_RATE).toString()));
						//amount = amount.divide(new BigDecimal(exchRateMap.get(SELL_RATE).toString()), scale, BigDecimal.ROUND_HALF_UP);
					}

				}
				if (amount == null) {
					Log.warn("The equivalent is still null after calculated");
					return null;
				} else {
					return amount.setScale(scale, BigDecimal.ROUND_HALF_UP);
				}
				
			} else {
				Log.error("All the fromAmout and toAmount are null");
				return null;
			}
		} else {
			Log.error("fromCCY or toCCY is null or empty");
			return null;
		}
	}
	//add by lzg end
	/**
	 * @param corpID :企业ID
	 * @param fromCCY
	 * @param toCCY
	 * @param scale :小数点位数
	 * @return rateMap:key[0]:rateType(0:NoSuchRate,1:buyRate,2:sellRate,3,buyRate&sellRate); key[1]:buyRate; key[2]:sellRate
	 * @throws NTBException
	 */
	public Map getExchangeRate(String corpID, String fromCCY, String toCCY, int scale) throws NTBException {
		return exchangeRatesDao.getExchangeRate(corpID, fromCCY, toCCY, scale);
	}
	

	public Map getExchangeRateFromHost(String corpID, String fromCCY,
			String toCCY, int scale) throws NTBException {
		Map exchRateMap = null;
		Map rateMap = new HashMap();
		BigDecimal buyRate = null;
		BigDecimal sellRate = null;
		String rateType = RATE_TYPE_NO_RATE;
		corpID = Utils.null2EmptyWithTrim(corpID);
		fromCCY = Utils.null2EmptyWithTrim(fromCCY);
		toCCY = Utils.null2EmptyWithTrim(toCCY);

		if (scale < 0) {
			scale = 8;
		} else if (scale > 8) {
			scale = 8;
		}

		if ((!fromCCY.equals("")) && (!toCCY.equals(""))) {
			fromCCY = fromCCY.toUpperCase();
			toCCY = toCCY.toUpperCase();

			if (fromCCY.equals(toCCY)) {
				rateType = RATE_TYPE_ALL_RATE;

				buyRate = new BigDecimal("1");
				sellRate = new BigDecimal("1");

			} else if (toCCY.equals("MOP")) { //锟斤拷一锟組OP

				exchRateMap = getExcRateFromHost(fromCCY);
				if (exchRateMap != null) {
					buyRate = new BigDecimal(exchRateMap.get("BUY").toString());
					if (buyRate.doubleValue() > 0) {
						buyRate = buyRate.setScale(scale, BigDecimal.ROUND_HALF_UP);
						rateType = RATE_TYPE_BUY_RATE;
					} else {
						//rateType = RATE_TYPE_NO_RATE;
						throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{fromCCY});
					}
				} else {
					//rateType = RATE_TYPE_NO_RATE;
					throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{fromCCY});
				}

			} else if (fromCCY.equals("MOP")) { //MOP锟斤拷锟斤拷锟�

				exchRateMap = getExcRateFromHost(toCCY);
				if (exchRateMap != null) {
					sellRate = new BigDecimal(exchRateMap.get("SELL").toString());
					if (sellRate.doubleValue() > 0) {
						sellRate = sellRate.setScale(scale, BigDecimal.ROUND_HALF_UP);
						rateType = RATE_TYPE_SELL_RATE;
					} else {
						//rateType = RATE_TYPE_NO_RATE;
						throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{toCCY});
					}
				} else {
					//rateType = RATE_TYPE_NO_RATE;
					throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{toCCY});
				}

			} /* update by gan 20180112
			else if (toCCY.equals("HKD")) { //锟斤拷一锟紿KD

				exchRateMap = getExchangeRate(corpID, fromCCY);
				if (exchRateMap != null) {
					buyRate = new BigDecimal(exchRateMap.get("TT_HKD_BUY_RATE").toString());
					if (buyRate.doubleValue() > 0) {
						buyRate = buyRate.setScale(scale, BigDecimal.ROUND_HALF_UP);
						rateType = RATE_TYPE_BUY_RATE;
					} else {
						//rateType = RATE_TYPE_NO_RATE;
						throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{fromCCY});
					}
				} else {
					//rateType = RATE_TYPE_NO_RATE;
					throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{fromCCY});
				}

			} else if (fromCCY.equals("HKD")) { //HKD锟斤拷锟斤拷锟�

				exchRateMap = getExchangeRate(corpID, toCCY);
				if (exchRateMap != null) {
					sellRate = new BigDecimal(exchRateMap.get("TT_HKD_SELL_RATE").toString());
					if (sellRate.doubleValue() > 0) {
						sellRate = sellRate.setScale(scale, BigDecimal.ROUND_HALF_UP);
						rateType = RATE_TYPE_SELL_RATE;
					} else {
						//rateType = RATE_TYPE_NO_RATE;
						throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{toCCY});
					}
				} else {
					//rateType = RATE_TYPE_NO_RATE;
					throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{toCCY});
				}

			}*/ else { //锟斤拷一锟斤拷锟斤拷

				exchRateMap = getExcRateFromHost(fromCCY);
				if (exchRateMap != null) {
					buyRate = new BigDecimal(exchRateMap.get("BUY").toString());
					if (buyRate.doubleValue() > 0) {
						buyRate = buyRate.setScale(scale, BigDecimal.ROUND_HALF_UP);
						rateType = RATE_TYPE_BUY_RATE;

						exchRateMap = getExcRateFromHost(toCCY);
						if (exchRateMap != null) {
							sellRate = new BigDecimal(exchRateMap.get("SELL").toString());
							if (sellRate.doubleValue() > 0) {
								sellRate = sellRate.setScale(scale, BigDecimal.ROUND_HALF_UP);
								rateType = RATE_TYPE_ALL_RATE;
							} else {
								//rateType = RATE_TYPE_NO_RATE;
								throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{toCCY});
							}
						} else {
							//rateType = RATE_TYPE_NO_RATE;
							throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{toCCY});
						}
					} else {
						//rateType = RATE_TYPE_NO_RATE;
						throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{fromCCY});
					}
				} else {
					//rateType = RATE_TYPE_NO_RATE;
					throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{fromCCY});
				}
			}

			rateMap.put(RATE_TYPE, rateType);
			rateMap.put(BUY_RATE, buyRate);
			rateMap.put(SELL_RATE, sellRate);
			return rateMap;

		} else {
			return null;
		}
	}

	private Map getExcRateFromHost(String ccy1) throws NTBException{
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		
		CibTransClient testClient = new CibTransClient("CIB", "4317");
		String effectiveDate = CibTransClient.getCurrentDate();
		toHost.put("BASECCY", "MOP");
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			   throw new NTBHostException(testClient.getErrorArray());
		}
		Log.info(fromHost);
		ArrayList retList = (ArrayList) fromHost.get("DATA");
		int count = Integer.parseInt(fromHost.get("COUNT").toString());
		HashMap retMap = new HashMap();
		HashMap rateMap;
		for(int i = 0; i < count; i++){
			rateMap = (HashMap) retList.get(i);
			if(rateMap.get("CCY1").toString().equals(ccy1)){
				retMap.putAll(rateMap);
				break;
			}
		}
		Log.info(retMap);
		return retMap;
	}

	public BigDecimal getEquivalentInMop(String fromCCY, BigDecimal fromAmt, int scale) throws NTBException {
		return exchangeRatesDao.getEquivalentInMop(fromCCY, fromAmt, scale);
	}

	public BigDecimal getExchangeRateInMop(String fromCcy, int scale) throws NTBException {
		return exchangeRatesDao.getExchangeRateInMop(fromCcy, scale);
	}

	public LceBean getLceInfo (String fromCcy, String toCcy, BigDecimal fromAmt, BigDecimal toAmt) throws NTBException {
		LceBean lceInfo = null;

		fromCcy = Utils.null2EmptyWithTrim(fromCcy);
		toCcy = Utils.null2EmptyWithTrim(toCcy);

		if ((fromCcy.equals("")) || (toCcy.equals(""))) {

			throw new NTBException("err.enq.GetLceInfoException");

		} else {

			lceInfo = new LceBean();

			fromCcy = fromCcy.toUpperCase();
			toCcy = toCcy.toUpperCase();

			BigDecimal fromAmtLCE = null;
			BigDecimal fromRateLCE = null;

			BigDecimal toAmtLCE = null;
			BigDecimal toRateLCE = null;

			if (fromCcy.equals("MOP")) {
				fromAmtLCE = fromAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
				fromRateLCE = (new BigDecimal("1")).setScale(8, BigDecimal.ROUND_HALF_UP);

				toAmtLCE = fromAmtLCE;
				toRateLCE = toAmtLCE.divide(toAmt , 8, BigDecimal.ROUND_HALF_UP);
			} else if (toCcy.equals("MOP")) {
				toAmtLCE = toAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
				toRateLCE = (new BigDecimal("1")).setScale(8, BigDecimal.ROUND_HALF_UP);

				fromAmtLCE = toAmtLCE;
				fromRateLCE = fromAmtLCE.divide(fromAmt , 8, BigDecimal.ROUND_HALF_UP);
			} else {
				fromAmtLCE = this.getEquivalentInMop(fromCcy, fromAmt, 2);
				fromRateLCE = fromAmtLCE.divide(fromAmt, 8, BigDecimal.ROUND_HALF_UP);

				toAmtLCE = fromAmtLCE;
				toRateLCE = toAmtLCE.divide(toAmt , 8, BigDecimal.ROUND_HALF_UP);
			}
			lceInfo.setFromAmtLCE(fromAmtLCE);
			lceInfo.setFromRateLCE(fromRateLCE);
			lceInfo.setToAmtLCE(toAmtLCE);
			lceInfo.setToRateLCE(toRateLCE);
		}

		return lceInfo;
	}
	
	public static void main(String[] args) {
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		LceBean m = null;
		try {
			m = exRatesService.getLceInfo("MOP", "MOP", new BigDecimal("657691.79"), new BigDecimal("657691.79"));
		} catch (NTBException e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * Add by heyongjiang 20100827
	 */
	public BigDecimal getEquivalentForCheckPurpose(String corpId,
			String fromCcy, String toCcy, BigDecimal fromAmount,
			BigDecimal toAmount, int scale) throws NTBException {
		// TODO Auto-generated method stub
		// Jet modified 2009-01-05
		if (fromAmount == null){
			scale = exchangeRatesDao.getScale(fromCcy);
		}
		
		if (toAmount == null){
			scale = exchangeRatesDao.getScale(toCcy);
		}
		return exchangeRatesDao.getEquivalentForCheckPurpose(corpId, fromCcy, toCcy, fromAmount, toAmount, scale);
	}

}
