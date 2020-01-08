/**
 * @author hjs
 * 2006-9-11
 */
package app.cib.dao.enq;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.enq.InterestRateBean;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-9-11
 */
public class InterestRatesDao extends GenericJdbcDao {
	
	private static final StringBuffer INTEREST_RATE_SQL = 
		new StringBuffer()
		.append("select ")
		.append("substr(tt1.period,1,1) as sort1, substr(tt1.period,2,1) as sort2,")
		.append("tt1.corp_id, tt1.ccy_code, tt1.period, tt1.effective_date,")
		.append("tt1.DEPOSIT_RANGE1, tt1.DEPOSIT_RANGE2, ")
		.append("tt1.DEPOSIT_RANGE3, tt1.DEPOSIT_RANGE4, ")
		.append("tt1.DEPOSIT_RANGE5, tt1.DEPOSIT_RANGE6, ")
		.append("tt1.DEPOSIT_RANGE7, tt1.DEPOSIT_RANGE8, ")
		.append("")
		.append("case tt1.sign_rate1 ")
		.append("	when '+' then interest_rate1 + SPREAD_RATE1 ")
		.append("	when '-' then interest_rate1 - SPREAD_RATE1 ")
		.append("	else interest_rate1 ")
		.append("end as interest_rate1, ")
		.append("")
		.append("case tt1.sign_rate2 ")
		.append("	when '+' then interest_rate2 + SPREAD_RATE2 ")
		.append("	when '-' then interest_rate2 - SPREAD_RATE2 ")
		.append("	else interest_rate2 ")
		.append("end as interest_rate2, ")
		.append("")
		.append("case tt1.sign_rate3 ")
		.append("	when '+' then interest_rate3 + SPREAD_RATE3 ")
		.append("	when '-' then interest_rate3 - SPREAD_RATE3 ")
		.append("	else interest_rate3 ")
		.append("end as interest_rate3, ")
		.append("")
		.append("case tt1.sign_rate4 ")
		.append("	when '+' then interest_rate4 + SPREAD_RATE4 ")
		.append("	when '-' then interest_rate4 - SPREAD_RATE4 ")
		.append("	else interest_rate4 ")
		.append("end as interest_rate4, ")
		.append("")
		.append("case tt1.sign_rate5 ")
		.append("	when '+' then interest_rate5 + SPREAD_RATE5 ")
		.append("	when '-' then interest_rate5 - SPREAD_RATE5 ")
		.append("	else interest_rate5 ")
		.append("end as interest_rate5, ")
		.append("")
		.append("case tt1.sign_rate6 ")
		.append("	when '+' then interest_rate6 + SPREAD_RATE6 ")
		.append("	when '-' then interest_rate6 - SPREAD_RATE6 ")
		.append("	else interest_rate6 ")
		.append("end as interest_rate6, ")
		.append("")
		.append("case tt1.sign_rate7 ")
		.append("	when '+' then interest_rate7 + SPREAD_RATE7 ")
		.append("	when '-' then interest_rate7 - SPREAD_RATE7 ")
		.append("	else interest_rate7 ")
		.append("end as interest_rate7, ")
		.append("")
		.append("case tt1.sign_rate8 ")
		.append("	when '+' then interest_rate8 + SPREAD_RATE8 ")
		.append("	when '-' then interest_rate8 - SPREAD_RATE8 ")
		.append("	else interest_rate8 ")
		.append("end as interest_rate8 ")
		.append("")
		.append("from ")
		.append("(")
		.append("	select ")
		.append("	t1.*, ")
		.append("	t2.corp_id, ")
		.append("	t2.SIGN_RATE1, t2.SPREAD_RATE1, ")
		.append("	t2.SIGN_RATE2, t2.SPREAD_RATE2, t2.SIGN_RATE3, t2.SPREAD_RATE3, t2.SIGN_RATE4, ")
		.append("	t2.SPREAD_RATE4, t2.SIGN_RATE5, t2.SPREAD_RATE5, t2.SIGN_RATE6, t2.SPREAD_RATE6, ")
		.append("	t2.SIGN_RATE7, t2.SPREAD_RATE7, t2.SIGN_RATE8, t2.SPREAD_RATE8 ")
		.append("	from ")
		.append("	hs_interest_rate as t1 ")
		.append("	left join ")
		.append("	hs_interest_rate_variance t2 ")
		.append("	on t1.ccy_code = t2.ccy_code and t1.period = t2.period_code and t2.corp_id = ? ")
		.append("	where t1.ccy_code = ? ")
		.append(") ")
		.append("as ")
		.append("tt1 ")
		.append("order by sort2, sort1");
	
	/**
	 * @param corpId
	 * @param currency
	 * @return	List: Period:key[PERIOD], Label1:key[INTEREST_RATE1], Label2:key[INTEREST_RATE1], Label3:key[INTEREST_RATE2], Label4:key[INTEREST_RATE3], Label5:key[INTEREST_RATE4]
	 * @throws NTBException
	 */
	public InterestRateBean getInterestRateInfo(String corpId, String currency) throws NTBException{
		List list = null;
		InterestRateBean iRates = new InterestRateBean();
		
		corpId = Utils.null2EmptyWithTrim(corpId);
		currency = Utils.null2EmptyWithTrim(currency);
		if(corpId.equals("")) {
			throw new NTBException("err.enq.getInterestRateInfoException");
		} if(currency.equals("")) {
			throw new NTBException("err.enq.getInterestRateInfoException");
		} else {
			currency = currency.toUpperCase();
			try {
				list = this.query(INTEREST_RATE_SQL.toString(), new Object[] {corpId, currency});
				
				//set savings rate
				Map tmpMap = null;
				for (int i=0; i<list.size(); i++) {
					tmpMap = (Map) list.get(i);
					if ("SV".equalsIgnoreCase(tmpMap.get("PERIOD").toString())) {
						iRates.setSavingsRate(tmpMap.get("INTEREST_RATE1").toString().trim() + "%");
						list.remove(tmpMap);
						break;
					}
				}
				
				//set period label1, label3, label4, label5
				tmpMap = null;
				tmpMap = (Map) list.get(1);
				iRates.setPeriodLabel1(tmpMap.get("DEPOSIT_RANGE1").toString().trim());
				iRates.setPeriodLabel3(tmpMap.get("DEPOSIT_RANGE2").toString().trim());
				iRates.setPeriodLabel4(tmpMap.get("DEPOSIT_RANGE3").toString().trim());
				iRates.setPeriodLabel5(tmpMap.get("DEPOSIT_RANGE4").toString().trim());
				//set period label2
				tmpMap = (Map) list.get(0);
				iRates.setPeriodLabel2(tmpMap.get("DEPOSIT_RANGE1").toString().trim());
				//set savings rate
				iRates.setEffectiveDate(tmpMap.get("EFFECTIVE_DATE").toString().trim());
				//set list
				iRates.setIRateList(list);
				
				return iRates;
			} catch (Exception e) {
				throw new NTBException("err.enq.GetInterestRateInfoException");
			}
			
		}
	}
	
	
	public static void main(String[] args){
        ApplicationContext appContext = Config.getAppContext();
        InterestRatesDao dao = (InterestRatesDao)appContext.getBean("InterestRatesDao");
		try {
			//System.err.println(dao.getInterestRateInfo("10001", "MOP"));
			InterestRateBean bean =  dao.getInterestRateInfo("10001", "MOP");
			List list = bean.getIRateList();
			/*
			Collections.sort(
					list,
					new Comparator() {
						public int compare(Object o1, Object o2) {
								return o1.hashCode()-o2.hashCode();
						}
					}
			) ;
			*/
			for(int i=0; i<list.size(); i++){
				System.err.println(list.get(i));
			}
			System.err.println(list);
		} catch (NTBException e) {
			e.printStackTrace();
		}
	}
	/*
	private class ccc implements Comparator{

		public int compare(Object o1, Object o2) {
			return o1.hashCode()-o2.hashCode();
		}
		
	}
	*/
	
	
	// Jet added for special interest rate
	
	
	
	
	
}
