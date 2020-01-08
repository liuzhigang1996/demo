/**
 * @author hjs
 * 2006-9-6
 */
package app.cib.service.enq;

import java.util.List;

import app.cib.bo.enq.HsWireInInfo;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-9-6
 */
public interface HsWireInInfoService {
	
	public List listWireInInfo(String corpId, String currency) throws NTBException;
	public HsWireInInfo viewWireInInfo(String seqNo) throws NTBException;

}
