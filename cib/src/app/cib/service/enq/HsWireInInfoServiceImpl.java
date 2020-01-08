/**
 * @author hjs
 * 2006-9-6
 */
package app.cib.service.enq;

import java.util.List;

import app.cib.bo.enq.HsWireInInfo;
import app.cib.dao.enq.HsWireInInfoDao;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-9-6
 */
public class HsWireInInfoServiceImpl implements HsWireInInfoService {
	
	private HsWireInInfoDao hsWireInInfoDao;

	public HsWireInInfoDao getHsWireInInfoDao() {
		return hsWireInInfoDao;
	}

	public void setHsWireInInfoDao(HsWireInInfoDao hsWireInInfoDao) {
		this.hsWireInInfoDao = hsWireInInfoDao;
	}

	public List listWireInInfo(String corpId, String currency) throws NTBException {
		String hql = "from HsWireInInfo as w where w.corporationId=? ";
		List list = null;
		if(currency.equals("OTHERS")) {
			hql += "and w.currency<>? and w.currency<>? and w.currency<>? and w.currency<>? ";
			hql += "and w.currency<>? and w.currency<>? and w.currency<>? and w.currency<>? ";
			list = this.hsWireInInfoDao.list(hql, new Object[]{corpId, "USD","HKD","MOP","JPY","EUR","GBP","AUD","NZD"});
		} else {
			hql += "and w.currency=? ";
			list = this.hsWireInInfoDao.list(hql, new Object[]{corpId, currency});
		}
		return list;
	}

	public HsWireInInfo viewWireInInfo(String seqNo) throws NTBException {
		HsWireInInfo HsWireInInfo = (HsWireInInfo) hsWireInInfoDao.load(HsWireInInfo.class, seqNo);
		return HsWireInInfo;
	}

}
