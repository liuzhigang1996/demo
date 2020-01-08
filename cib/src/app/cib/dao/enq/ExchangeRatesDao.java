/**
 *
 */
package app.cib.dao.enq;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs 2006-07-20
 */
public class ExchangeRatesDao extends GenericJdbcDao {

	public static final String RATE_TYPE = "rateType";
	public static final String BUY_RATE = "buyRate";
	public static final String SELL_RATE = "sellRate";

	public static final String RATE_TYPE_NO_RATE = "0";
	public static final String RATE_TYPE_BUY_RATE = "1";
	public static final String RATE_TYPE_SELL_RATE = "2";
	public static final String RATE_TYPE_ALL_RATE = "3";

	private static final StringBuffer EXCHANGE_RATE_BY_ZONE_CODE_SQL =
		new StringBuffer()
		.append("select ")
		.append("ttt1.ccy_code, ttt1.unit, ttt1.ccy_long_name, ")
		.append("")
		.append("case when ")
		.append("	case ttt2.sign_trans_buy_rate ")
		.append("		when '+' then tt_buy_rate_hkd + spread_trans_buy_rate ")
		.append("		when '-' then tt_buy_rate_hkd - spread_trans_buy_rate ")
		.append("		else tt_buy_rate_hkd ")
		.append("	end <= 0 ")
		.append("	then '-' ")
		.append("	else char( ")
		.append("		case ttt2.sign_trans_buy_rate ")
		.append("			when '+' then tt_buy_rate_hkd + spread_trans_buy_rate ")
		.append("			when '-' then tt_buy_rate_hkd - spread_trans_buy_rate ")
		.append("			else tt_buy_rate_hkd ")
		.append("		end ")
		.append("	) ")
		.append("end as tt_hkd_buy_rate, ")
		.append("")
		.append("case when ")
		.append("	case ttt2.sign_trans_sell_rate ")
		.append("		when '+' then tt_sell_rate_hkd + spread_trans_sell_rate ")
		.append("		when '-' then tt_sell_rate_hkd - spread_trans_sell_rate ")
		.append("		else tt_sell_rate_hkd ")
		.append("	end <= 0 ")
		.append("	then '-' ")
		.append("	else char( ")
		.append("		case ttt2.sign_trans_sell_rate ")
		.append("			when '+' then tt_sell_rate_hkd + spread_trans_sell_rate ")
		.append("			when '-' then tt_sell_rate_hkd - spread_trans_sell_rate ")
		.append("			else tt_sell_rate_hkd ")
		.append("		end ")
		.append("	) ")
		.append("end as tt_hkd_sell_rate, ")
		.append("")
		.append("case when ")
		.append("	case ttt2.sign_note_buy_rate ")
		.append("		when '+' then tt_buy_rate_mop + spread_note_buy_rate ")
		.append("		when '-' then tt_buy_rate_mop - spread_note_buy_rate ")
		.append("		else tt_buy_rate_mop ")
		.append("	end <= 0 ")
		.append("	then '-' ")
		.append("	else char( ")
		.append("		case ttt2.sign_note_buy_rate ")
		.append("			when '+' then tt_buy_rate_mop + spread_note_buy_rate ")
		.append("			when '-' then tt_buy_rate_mop - spread_note_buy_rate ")
		.append("			else tt_buy_rate_mop ")
		.append("		end ")
		.append("	) ")
		.append("end as tt_mop_buy_rate, ")
		.append("")
		.append("case when ")
		.append("	case ttt2.sign_note_sell_rate ")
		.append("		when '+' then tt_sell_rate_mop + spread_note_sell_rate ")
		.append("		when '-' then tt_sell_rate_mop - spread_note_sell_rate ")
		.append("		else tt_sell_rate_mop ")
		.append("	end <= 0 ")
		.append("	then '-' ")
		.append("	else char( ")
		.append("		case ttt2.sign_note_sell_rate ")
		.append("			when '+' then tt_sell_rate_mop + spread_note_sell_rate ")
		.append("			when '-' then tt_sell_rate_mop - spread_note_sell_rate ")
		.append("			else tt_sell_rate_mop ")
		.append("		end ")
		.append("	) ")
		.append("end as tt_mop_sell_rate, ")
		.append("")
		.append("case when ")
		.append("	note_buy_rate_hkd <= 0 ")
		.append("	then '-' ")
		.append("	else char( ")
		.append("		note_buy_rate_hkd ")
		.append("	)")
		.append("end as note_hkd_buy_rate, ")
		.append("")
		.append("case when ")
		.append("	note_sell_rate_hkd <= 0 ")
		.append("	then '-' ")
		.append("	else char( ")
		.append("		note_sell_rate_hkd ")
		.append("	) ")
		.append("end as note_hkd_sell_rate, ")
		.append("")
		.append("case when ")
		.append("	note_buy_rate_mop <= 0 ")
		.append("	then '-' ")
		.append("	else char( ")
		.append("		note_buy_rate_mop ")
		.append("	) ")
		.append("end as note_mop_buy_rate, ")
		.append("")
		.append("case when ")
		.append("	note_sell_rate_mop <= 0 ")
		.append("	then '-' ")
		.append("	else char( ")
		.append("		note_sell_rate_mop ")
		.append("	) ")
		.append("end as note_mop_sell_rate ")
		.append("")
		.append("from ")
		.append("( ")
		.append("  select  ")
		.append("		tt1.seqno_in_zone, tt1.ccy_code, tt1.unit, tt1.ccy_long_name, ")
		.append("		tt2.tt_buy_rate_hkd, tt2.tt_sell_rate_hkd, tt2.tt_buy_rate_mop, tt2.tt_sell_rate_mop, tt2.note_buy_rate_hkd, tt2.note_sell_rate_hkd, tt2.note_buy_rate_mop, tt2.note_sell_rate_mop ")
		.append("  from ")
		.append("  ( ")
		.append("    select t1.ccy_code, t1.unit, t2.ccy_long_name, t1.seqno_in_zone ")
		.append("    from HS_CURRENCY t1, HS_CURRENCY_NAME t2 ")
		.append("    where t1.ccy_zone_no = ? and t1.ccy_code = t2.ccy_code ")
		.append( "  ) as tt1 ")
		.append("  left join HS_EXCHANGE_RATE as tt2 ")
		.append("  on tt1.ccy_code = tt2.ccy_code ")
		.append(") ")
		.append("as ttt1 ")
		.append("left join ")
		.append("  ( ")
		.append("   select t1.* ")
		.append("   from HS_EXCHANGE_RATE_VARIANCE t1, HS_CURRENCY t2 ")
		.append("   where t1.ccy_code = t2.ccy_code and t2.ccy_zone_no = ? and t1.corp_id = ? ")
		.append("  ) ")
		.append("as ttt2 ")
		.append("on ttt1.ccy_code = ttt2.ccy_code ")
		.append("order by seqno_in_zone");

	private static final StringBuffer EXCHANGE_RATE_BY_CCY_CODE_SQL =
		new StringBuffer()
//		.append("select ")
//		.append("ttt1.ccy_code, ")
//		.append("case ttt2.sign_trans_buy_rate ")
//		.append("	when '+' then tt_buy_rate_hkd + spread_trans_buy_rate ")
//		.append("	when '-' then tt_buy_rate_hkd - spread_trans_buy_rate ")
//		.append("	else tt_buy_rate_hkd ")
//		.append("end as tt_hkd_buy_rate, ")
//		.append("")
//		.append("case ttt2.sign_trans_sell_rate ")
//		.append("	when '+' then tt_sell_rate_hkd + spread_trans_sell_rate ")
//		.append("	when '-' then tt_sell_rate_hkd - spread_trans_sell_rate ")
//		.append("	else tt_sell_rate_hkd ")
//		.append("end as tt_hkd_sell_rate, ")
//		.append("")
//		.append("case ttt2.sign_note_buy_rate ")
//		.append("	when '+' then tt_buy_rate_mop + spread_note_buy_rate ")
//		.append("	when '-' then tt_buy_rate_mop - spread_note_buy_rate ")
//		.append("	else tt_buy_rate_mop ")
//		.append("end as tt_mop_buy_rate, ")
//		.append("")
//		.append("case ttt2.sign_note_sell_rate ")
//		.append("	when '+' then tt_sell_rate_mop + spread_note_sell_rate ")
//		.append("	when '-' then tt_sell_rate_mop - spread_note_sell_rate ")
//		.append("	else tt_sell_rate_mop ")
//		.append("end as tt_mop_sell_rate ")
//		.append("")
//		.append("from ")
//		.append("( ")
//		.append("	select * from hs_exchange_rate as rate ")
//		.append("	where rate.ccy_code = ? ")
//		.append(") ")
//		.append("as ttt1 ")
//		.append("left join ")
//		.append("( ")
//		.append("	select ")
//		.append("		CORP_ID, CCY_CODE, SIGN_TRANS_BUY_RATE, SPREAD_TRANS_BUY_RATE, ")
//		.append("		SIGN_TRANS_SELL_RATE, SPREAD_TRANS_SELL_RATE, SIGN_NOTE_BUY_RATE, ")
//		.append("		SPREAD_NOTE_BUY_RATE, SIGN_NOTE_SELL_RATE, SPREAD_NOTE_SELL_RATE ")
//		.append("	from hs_exchange_rate_variance t1 ")
//		.append("	where t1.corp_id = ? and t1.ccy_code= ? ")
//		.append(") ")
//		.append("as ttt2 ")
//		.append("on ttt1.ccy_code = ttt2.ccy_code");
	//mod by linrui for oracle
	/*.append("select ")
	.append("ttt1.ccy_code, ")
	.append("case ttt2.sign_trans_buy_rate ")
	.append("	when '+' then tt_buy_rate_hkd + spread_trans_buy_rate ")
	.append("	when '-' then tt_buy_rate_hkd - spread_trans_buy_rate ")
	.append("	else tt_buy_rate_hkd ")
	.append("end as tt_hkd_buy_rate, ")
	.append("")
	.append("case ttt2.sign_trans_sell_rate ")
	.append("	when '+' then tt_sell_rate_hkd + spread_trans_sell_rate ")
	.append("	when '-' then tt_sell_rate_hkd - spread_trans_sell_rate ")
	.append("	else tt_sell_rate_hkd ")
	.append("end as tt_hkd_sell_rate, ")
	.append("")
	.append("case ttt2.sign_note_buy_rate ")
	.append("	when '+' then tt_buy_rate_mop + spread_note_buy_rate ")
	.append("	when '-' then tt_buy_rate_mop - spread_note_buy_rate ")
	.append("	else tt_buy_rate_mop ")
	.append("end as tt_mop_buy_rate, ")
	.append("")
	.append("case ttt2.sign_note_sell_rate ")
	.append("	when '+' then tt_sell_rate_mop + spread_note_sell_rate ")
	.append("	when '-' then tt_sell_rate_mop - spread_note_sell_rate ")
	.append("	else tt_sell_rate_mop ")
	.append("end as tt_mop_sell_rate ")
	.append("")
	.append("from ")
	.append("( ")
	.append("	select * from hs_exchange_rate  ")//MOD
	.append("	where ccy_code = ? ")
	.append(") ")
	.append(" ttt1 ")//mod
	.append("left join ")
	.append("( ")
	.append("	select ")
	.append("		CORP_ID, CCY_CODE, SIGN_TRANS_BUY_RATE, SPREAD_TRANS_BUY_RATE, ")
	.append("		SIGN_TRANS_SELL_RATE, SPREAD_TRANS_SELL_RATE, SIGN_NOTE_BUY_RATE, ")
	.append("		SPREAD_NOTE_BUY_RATE, SIGN_NOTE_SELL_RATE, SPREAD_NOTE_SELL_RATE ")
	.append("	from hs_exchange_rate_variance t1 ")
	.append("	where t1.corp_id = ? and t1.ccy_code= ? ")
	.append(") ")
	.append(" ttt2 ")//mod
	.append("on ttt1.ccy_code = ttt2.ccy_code");*/
	//end
    //update by linrui for no VARIANCE 20180205
	.append("select tt_buy_rate_mop as tt_mop_buy_rate,tt_sell_rate_mop as tt_mop_sell_rate" +
			" from hs_exchange_rate ")//MOD
	.append("where ccy_code = ? ");
	/*
	 * list the exchange rate by Zone_Code and CorpID
	 * ��ݵ���������ҵID��ʾ������Ļ��һ���
	 */
	public List listERateByZoneCode(String zoneCode, String corpID) throws NTBException {
		//System.out.println(sql);

		try {
			List exchangeRateList = query(EXCHANGE_RATE_BY_ZONE_CODE_SQL.toString(),
					new Object[] { zoneCode, zoneCode, corpID });

			return exchangeRateList;
		} catch (Exception e) {
			throw new NTBException("err.enq.ListExchangeRateByZoneCodeException");
		}
	}

	protected String getZodeCodeByCcy(String ccy) throws NTBException {
		Map rowMap = null;
		String sql = "";
		sql = "SELECT CCY_ZONE_NO from HS_CURRENCY WHERE CCY_CODE = ?";
		try {
			rowMap = querySingleRow(sql, new Object[] {ccy});
			return rowMap.get("CCY_ZONE_NO").toString();
		} catch (Exception e) {
			throw new NTBException("err.enq.GetZodeCodeByCurrencyException");
		}
	}

	public Map getExchangeRate(String corpId, String currency) throws NTBException {
		try {
			Map rowMap = this.querySingleRow(EXCHANGE_RATE_BY_CCY_CODE_SQL.toString(),
					new Object[] {currency});//mod by linrui 20180205 {currency, corpId, currency}

			return rowMap;

		} catch (Exception e) {
			throw new NTBException("err.enq.GetExchangeRateException");
		}
	}

	public Map getExchangeRate(String corpID, String fromCCY, String toCCY, int scale) throws NTBException {
		Map exchRateMap = null;
		Map rateMap = new HashMap();
		BigDecimal buyRate = null;
		BigDecimal sellRate = null;
		String rateType = RATE_TYPE_NO_RATE;

		corpID = Utils.null2EmptyWithTrim(corpID);
		fromCCY = Utils.null2EmptyWithTrim(fromCCY);
		toCCY = Utils.null2EmptyWithTrim(toCCY);

		if (scale < 0) {
			scale = 7;
		} else if (scale > 7) {
			scale = 7;
		}

		if ((!fromCCY.equals("")) && (!toCCY.equals(""))) {
			fromCCY = fromCCY.toUpperCase();
			toCCY = toCCY.toUpperCase();

			if (fromCCY.equals(toCCY)) {
				rateType = RATE_TYPE_ALL_RATE;

				buyRate = new BigDecimal("1");
				sellRate = new BigDecimal("1");

			} else if (toCCY.equals("MOP")) { //��һ�MOP

				exchRateMap = getExchangeRate(corpID, fromCCY);
				if (exchRateMap != null) {
					buyRate = new BigDecimal(exchRateMap.get("TT_MOP_BUY_RATE").toString());
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

			} else if (fromCCY.equals("MOP")) { //MOP�����

				exchRateMap = getExchangeRate(corpID, toCCY);
				if (exchRateMap != null) {
					sellRate = new BigDecimal(exchRateMap.get("TT_MOP_SELL_RATE").toString());
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
			else if (toCCY.equals("HKD")) { //��һ�HKD

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

			} else if (fromCCY.equals("HKD")) { //HKD�����

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

			}*/ else { //��һ����

				exchRateMap = getExchangeRate(corpID, fromCCY);
				if (exchRateMap != null) {
					buyRate = new BigDecimal(exchRateMap.get("TT_MOP_BUY_RATE").toString());
					if (buyRate.doubleValue() > 0) {
						buyRate = buyRate.setScale(scale, BigDecimal.ROUND_HALF_UP);
						rateType = RATE_TYPE_BUY_RATE;

						exchRateMap = getExchangeRate(corpID, toCCY);
						if (exchRateMap != null) {
							sellRate = new BigDecimal(exchRateMap.get("TT_MOP_SELL_RATE").toString());
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

	/*
	 * ͨ����ʻ�����
	 */
	public BigDecimal getEquivalent(String corpID, String fromCCY, String toCCY, BigDecimal fromAmount, BigDecimal toAmount, int scale) throws NTBException{

		corpID = Utils.null2EmptyWithTrim(corpID);
		fromCCY = Utils.null2EmptyWithTrim(fromCCY);
		toCCY = Utils.null2EmptyWithTrim(toCCY);

		if (scale < 0) {
			scale = 7;
		} else if (scale > 7) {
			scale = 7;
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
					exchRateMap = this.getExchangeRate(corpID, fromCCY, toCCY, 7);
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

	public BigDecimal getExchangeRateInMop (String fromCcy, int scale) throws NTBException {
		String sql = "select ccy_code, mean_rate_mop from hs_exchange_rate where ccy_code = ?";
		Map rowMap = null;
		BigDecimal rateInMop = null;
		try {
			rowMap = this.querySingleRow(sql, new Object[] {fromCcy});
			rateInMop = new BigDecimal(rowMap.get("MEAN_RATE_MOP").toString());
			rateInMop = rateInMop.setScale(scale, BigDecimal.ROUND_HALF_UP);
			return rateInMop;
		} catch (Exception e) {
			throw new NTBException("err.enq.GetExchangeRateInMopException");
		}
	}

	public BigDecimal getEquivalentInMop(String fromCCY, BigDecimal fromAmt, int scale) throws NTBException {
		BigDecimal rateInMop = this.getExchangeRateInMop(fromCCY, 7);
		BigDecimal equivalentInMop = fromAmt.multiply(rateInMop);
		equivalentInMop = equivalentInMop.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return equivalentInMop;
	}

	public int getScale(String ccy) throws NTBException {
		String sql = "select ROUNDING_EFFECTIVE_DECIMAL_PLA from HS_CURRENCY where CCY_CODE = ?";
		try {
			String strScale = (String) this.querySingleValue(sql, new Object[]{ccy});
			return Integer.parseInt(strScale);
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
	}
	
	public static void main(String[] args){
        ApplicationContext appContext = Config.getAppContext();
        ExchangeRatesDao dao = (ExchangeRatesDao)appContext.getBean("ExchangeRatesDao");

        /*
        try {
			System.err.println(dao.getExchangeRate("10001", "aud", "usd", 7));
			//System.err.println(dao.getExchangeRateByCcy("10001", "USD"));
		} catch (NTBException e) {
			e.printStackTrace();
		}
        */

        try {

			System.err.println("toAmount_from_HKD(1000)_to_MOP: "
					+ dao.getEquivalent("10002", "HKD", "MOP", new BigDecimal("1000"), null, 2).toString());
			System.err.println("fromAmount_from_MOP_to_HKD(1000): "
					+ dao.getEquivalent("10002", "MOP", "HKD", null, new BigDecimal("1000"), 2).toString());
			

			System.err.println("toAmount_from_USD(1000)_to_ERD: "
					+ dao.getEquivalent("10002", "USD", "EUR", new BigDecimal("1000"), null, 2).toString());
			System.err.println("fromAmount_from_EUR_to_USD(1000): "
					+ dao.getEquivalent("10002", "EUR", "USD", null, new BigDecimal("1000"), 2).toString());
			
			/*
			System.err.println("toAmount_from_HKD(10000)_to_MOP: "
					+ dao.getEquivalent("10002", "HKD", "MOP", new BigDecimal("10000"), 2).toString());

			System.err.println("toAmount_from_MOP(10000)_to_USD: "
					+ dao.getEquivalent("10002", "MOP", "USD", new BigDecimal("10000"), 2).toString());

			System.err.println("toAmount_from_HKD(10000)_to_USD: "
					+ dao.getEquivalent("10002", "HKD", "USD", new BigDecimal("10000"), 2).toString());

			System.err.println("toAmount_from_USD(10000)_to_MOP: "
					+ dao.getEquivalent("10002", "USD", "MOP", new BigDecimal("10000"), 2).toString());

			System.err.println("toAmount_from_USD(10000)_to_HKD: "
					+ dao.getEquivalent("10002", "USD", "HKD", new BigDecimal("10000"), 2).toString());

			System.err.println("toAmount_from_USD(10000)_to_TWD: "
					+ dao.getEquivalent("10002", "USD", "TWD", new BigDecimal("10000"), 2).toString());

			System.err.println("toAmount_from_USD(10000)_to_AAA: "
					+ dao.getEquivalent("10002", "USD", "AAA", new BigDecimal("10000"), 2).toString());

			System.err.println("toAmount_from_JPY(10000)_to_USD: "
					+ dao.getEquivalent("10002", "JPY", "USD", new BigDecimal("10000"), 2).toString());
			*/
        } catch (NTBException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add by heyongjiang 20100827
	 * @param corpID
	 * @param fromCCY
	 * @param toCCY
	 * @param fromAmount
	 * @param toAmount
	 * @param scale
	 * @return
	 * @throws NTBException
	 */
	public BigDecimal getEquivalentForCheckPurpose(String corpID, String fromCCY, String toCCY, BigDecimal fromAmount, BigDecimal toAmount, int scale) throws NTBException{

		corpID = Utils.null2EmptyWithTrim(corpID);
		fromCCY = Utils.null2EmptyWithTrim(fromCCY);
		toCCY = Utils.null2EmptyWithTrim(toCCY);

		if (scale < 0) {
			scale = 7;
		} else if (scale > 7) {
			scale = 7;
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
					exchRateMap = this.getExchangeRateForCheckPurpose(corpID, fromCCY, toCCY, 7);
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
							amount = fromAmount.multiply(new BigDecimal(exchRateMap.get(SELL_RATE).toString()));
							//amount = fromAmount.divide(new BigDecimal(exchRateMap.get(SELL_RATE).toString()), scale, BigDecimal.ROUND_HALF_UP);
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
	
	public Map getExchangeRateForCheckPurpose(String corpID, String fromCCY, String toCCY, int scale) throws NTBException {
		Map exchRateMap = null;
		Map rateMap = new HashMap();
		BigDecimal buyRate = null;
		BigDecimal sellRate = null;
		String rateType = RATE_TYPE_NO_RATE;

		corpID = Utils.null2EmptyWithTrim(corpID);
		fromCCY = Utils.null2EmptyWithTrim(fromCCY);
		toCCY = Utils.null2EmptyWithTrim(toCCY);

		if (scale < 0) {
			scale = 7;
		} else if (scale > 7) {
			scale = 7;
		}

		if ((!fromCCY.equals("")) && (!toCCY.equals(""))) {
			fromCCY = fromCCY.toUpperCase();
			toCCY = toCCY.toUpperCase();

			if (fromCCY.equals(toCCY)) {
				rateType = RATE_TYPE_ALL_RATE;

				buyRate = new BigDecimal("1");
				sellRate = new BigDecimal("1");

			} else if (toCCY.equals("MOP")) { //��һ�MOP

				exchRateMap = getExchangeRate(corpID, fromCCY);
				if (exchRateMap != null) {
					sellRate = new BigDecimal(exchRateMap.get("TT_MOP_SELL_RATE").toString());
					if (sellRate.doubleValue() > 0) {
						sellRate = sellRate.setScale(scale, BigDecimal.ROUND_HALF_UP);
						rateType = RATE_TYPE_SELL_RATE;
					} else {
						//rateType = RATE_TYPE_NO_RATE;
						throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{fromCCY});
					}
				} else {
					//rateType = RATE_TYPE_NO_RATE;
					throw new NTBException("err.enq.NoSuchExchangeRate", new Object[]{fromCCY});
				}

			} else if (fromCCY.equals("MOP")) { //MOP�����

				exchRateMap = getExchangeRate(corpID, toCCY);
				if (exchRateMap != null) {
					sellRate = new BigDecimal(exchRateMap.get("TT_MOP_SELL_RATE").toString());
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

			} else if (toCCY.equals("HKD")) { //��һ�HKD

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

			} else if (fromCCY.equals("HKD")) { //HKD�����

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

			} else { //��һ����

				exchRateMap = getExchangeRate(corpID, fromCCY);
				if (exchRateMap != null) {
					buyRate = new BigDecimal(exchRateMap.get("TT_MOP_BUY_RATE").toString());
					if (buyRate.doubleValue() > 0) {
						buyRate = buyRate.setScale(scale, BigDecimal.ROUND_HALF_UP);
						rateType = RATE_TYPE_BUY_RATE;

						exchRateMap = getExchangeRate(corpID, toCCY);
						if (exchRateMap != null) {
							sellRate = new BigDecimal(exchRateMap.get("TT_MOP_SELL_RATE").toString());
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
	//Add by heyongjiang end

}
