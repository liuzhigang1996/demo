/**
 * 
 */
package app.cib.service.enq;

import java.math.BigDecimal;
import java.util.*;

import app.cib.bo.enq.LceBean;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-07-20
 */
public interface ExchangeRatesService {
	
	/**
	 * list the exchange rate by Zone_Code and CorpID
	 * 根据地区代码和企业ID显示调整过后的货币汇率
	 * @param zoneCode:地区代码(ex:AP(亚太地区)), corpID:企业ID
	 * @return exchangeRatesList:货币汇率列表
	 */
	public List listERateByZoneCode(String zoneCode, String corpId) throws NTBException;
	
	
	/**
	 * 通过汇率换算金额
	 * @param corpID:企业ID, fromCCY:转出货币类型, toCCY:转入货币类型, fromAmount:转出货币金额, toAmount:转入货币金额
	 * @return amount
	 */
	public BigDecimal getEquivalent(String corpId, String fromCcy, String toCcy, BigDecimal fromAmount, BigDecimal toAmount, int scale) throws NTBException;
	
	/**
	 * 通过汇率换算金额,汇率是实时从核心获取
	 * @param corpID:企业ID, fromCCY:转出货币类型, toCCY:转入货币类型, fromAmount:转出货币金额, toAmount:转入货币金额
	 * @return amount
	 */
	//add by lzg 20190507
	public BigDecimal getEquivalentFromHostRate(String corpId, String fromCcy, String toCcy, BigDecimal fromAmount, BigDecimal toAmount, int scale,Map exchangeMap) throws NTBException;
	/**
	 * Add by heyongjiang 20100827
	 * 通过汇率换算金额
	 * @param corpID:企业ID, fromCCY:转出货币类型, toCCY:转入货币类型, fromAmount:转出货币金额, toAmount:转入货币金额
	 * @return amount
	 */
	public BigDecimal getEquivalentForCheckPurpose(String corpId, String fromCcy, String toCcy, BigDecimal fromAmount, BigDecimal toAmount, int scale) throws NTBException;
	
	/**
	 * @param corpID :企业ID
	 * @param fromCCY
	 * @param toCCY
	 * @param scale :小数点位数
	 * @return rateMap:key[0]:rateType(0:NoSuchRate,1:buyRate,2:sellRate,3,buyRate&sellRate); key[1]:buyRate; key[2]:sellRate
	 * @throws NTBException
	 */
	public Map getExchangeRate(String corpId, String fromCcy, String toCcy, int scale) throws NTBException;
	
	/**
	 * @param corpID :企业ID
	 * @param fromCCY
	 * @param toCCY
	 * @param scale :小数点位数
	 * @return rateMap:key[0]:rateType(0:NoSuchRate,1:buyRate,2:sellRate,3,buyRate&sellRate); key[1]:buyRate; key[2]:sellRate
	 * @throws NTBException
	 */
	//add by lzg 20190507
	public Map getExchangeRateFromHost(String corpId, String fromCcy, String toCcy, int scale) throws NTBException;
	
	
	/**
	 * 等额MOP汇率
	 * @param fromCcy
	 * @param scale :小数点位数
	 * @return
	 * @throws NTBException
	 */
	public BigDecimal getExchangeRateInMop (String fromCcy, int scale) throws NTBException ;
	
	
	/**
	 * 等额MOP金额
	 * @param fromCCY
	 * @param fromAmt
	 * @param scale :小数点位数
	 * @return
	 * @throws NTBException
	 */
	public BigDecimal getEquivalentInMop(String fromCcy, BigDecimal fromAmt, int scale) throws NTBException ;
	
	
	/**
	 * 
	 * @param fromCcy
	 * @param fromAmt
	 * @param toCcy
	 * @param toAmt
	 * @return view bean : LceInfo
	 * @throws NTBException
	 */
	public LceBean getLceInfo (String fromCcy, String toCcy, BigDecimal fromAmt, BigDecimal toAmt) throws NTBException;
}
