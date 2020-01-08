/**
 * @author hjs
 * 2006-8-3
 */
package app.cib.service.bnk;

import java.util.List;
import java.util.Map;

import com.neturbo.set.exception.NTBException;

import app.cib.bo.bnk.SetCurrency;
import app.cib.bo.bnk.SetCurrencyHis;
import app.cib.bo.bnk.TxnLimit;

/**
 * @author linrui
 * 2019-7-1
 */
public interface SetCurrencyService {
	
	public List listManageCcy() throws NTBException;
	public List listManageCcyPending() throws NTBException;
	public List listManageCcyNormal() throws NTBException;
	public void update(SetCurrency setCurrency) throws NTBException;
	public void updateHis(SetCurrencyHis setCurrency) throws NTBException;
	public void convertPojo2Map(Object pojo, Map valueMap) throws NTBException;
	public void approve() throws NTBException;
	public void reject() throws NTBException;
}
