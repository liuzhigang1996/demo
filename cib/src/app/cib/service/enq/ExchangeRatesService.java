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
	 * ���ݵ����������ҵID��ʾ��������Ļ��һ���
	 * @param zoneCode:��������(ex:AP(��̫����)), corpID:��ҵID
	 * @return exchangeRatesList:���һ����б�
	 */
	public List listERateByZoneCode(String zoneCode, String corpId) throws NTBException;
	
	
	/**
	 * ͨ�����ʻ�����
	 * @param corpID:��ҵID, fromCCY:ת����������, toCCY:ת���������, fromAmount:ת�����ҽ��, toAmount:ת����ҽ��
	 * @return amount
	 */
	public BigDecimal getEquivalent(String corpId, String fromCcy, String toCcy, BigDecimal fromAmount, BigDecimal toAmount, int scale) throws NTBException;
	
	/**
	 * ͨ�����ʻ�����,������ʵʱ�Ӻ��Ļ�ȡ
	 * @param corpID:��ҵID, fromCCY:ת����������, toCCY:ת���������, fromAmount:ת�����ҽ��, toAmount:ת����ҽ��
	 * @return amount
	 */
	//add by lzg 20190507
	public BigDecimal getEquivalentFromHostRate(String corpId, String fromCcy, String toCcy, BigDecimal fromAmount, BigDecimal toAmount, int scale,Map exchangeMap) throws NTBException;
	/**
	 * Add by heyongjiang 20100827
	 * ͨ�����ʻ�����
	 * @param corpID:��ҵID, fromCCY:ת����������, toCCY:ת���������, fromAmount:ת�����ҽ��, toAmount:ת����ҽ��
	 * @return amount
	 */
	public BigDecimal getEquivalentForCheckPurpose(String corpId, String fromCcy, String toCcy, BigDecimal fromAmount, BigDecimal toAmount, int scale) throws NTBException;
	
	/**
	 * @param corpID :��ҵID
	 * @param fromCCY
	 * @param toCCY
	 * @param scale :С����λ��
	 * @return rateMap:key[0]:rateType(0:NoSuchRate,1:buyRate,2:sellRate,3,buyRate&sellRate); key[1]:buyRate; key[2]:sellRate
	 * @throws NTBException
	 */
	public Map getExchangeRate(String corpId, String fromCcy, String toCcy, int scale) throws NTBException;
	
	/**
	 * @param corpID :��ҵID
	 * @param fromCCY
	 * @param toCCY
	 * @param scale :С����λ��
	 * @return rateMap:key[0]:rateType(0:NoSuchRate,1:buyRate,2:sellRate,3,buyRate&sellRate); key[1]:buyRate; key[2]:sellRate
	 * @throws NTBException
	 */
	//add by lzg 20190507
	public Map getExchangeRateFromHost(String corpId, String fromCcy, String toCcy, int scale) throws NTBException;
	
	
	/**
	 * �ȶ�MOP����
	 * @param fromCcy
	 * @param scale :С����λ��
	 * @return
	 * @throws NTBException
	 */
	public BigDecimal getExchangeRateInMop (String fromCcy, int scale) throws NTBException ;
	
	
	/**
	 * �ȶ�MOP���
	 * @param fromCCY
	 * @param fromAmt
	 * @param scale :С����λ��
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
