package app.cib.service.enq;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;

public class SwiftCodeEnqureServiceImpl implements SwiftCodeEnqureService {
	private GenericJdbcDao genericJdbcDao;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	@Override
	public List getCorrespondentBankBySwiftCode(String correspondentBankCode,String firstRecord,String endRecord) throws NTBException {
		String sql = "";
		sql = "select BICD_SWF_CD,BICD_CH_NAME,BICD_SWF_NAME1,BICD_SWF_NAME2,BICD_SWF_NAME3 from (select ROWNUM as rown,BICD_SWF_CD,BICD_CH_NAME,BICD_SWF_NAME1,BICD_SWF_NAME2,BICD_SWF_NAME3 FROM PGTBICD WHERE "
				+ "BICD_SWF_CD not like ('____MO%') "
				+ "AND BICD_SWF_CD like ('%" + correspondentBankCode + "%') "
				+ "AND ROWNUM <= " + endRecord  + ") table_alias  where table_alias.rown > " + firstRecord;
		try {
			return genericJdbcDao.query(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NTBException("err.sys.DBError");
		}
	}

	@Override
	public int getCorrespondentBankBySwiftCodeCount(String correspondentBankCode) throws NTBException {
		String sql = "";
		sql = "select count(1) recordCount FROM PGTBICD WHERE "
				+ "BICD_SWF_CD not like ('____MO%') "
				+ "AND BICD_SWF_CD like ('%" + correspondentBankCode + "%') ";
		try {
			List retList = genericJdbcDao.query(sql, null);
			HashMap retMap = (HashMap) retList.get(0);
			BigDecimal count = (BigDecimal) retMap.get("RECORDCOUNT");
			return count.intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw new NTBException("err.sys.DBError");
		}
	}

	@Override
	public int getCorrespondentBankByBankNameCount(String correspondentBankName) throws NTBException {
		String sql = "";
		sql = "select count(1) recordCount FROM PGTBICD WHERE "
				+ "BICD_SWF_CD not like ('____MO%') "
				+ "AND (TRIM(BICD_SWF_NAME1)||TRIM(BICD_SWF_NAME2)||TRIM(BICD_SWF_NAME3) like ('%" + correspondentBankName + "%') or BICD_CH_NAME like ('%"+correspondentBankName+"%')) ";
		try {
			List retList = genericJdbcDao.query(sql, null);
			HashMap retMap = (HashMap) retList.get(0);
			BigDecimal count = (BigDecimal) retMap.get("RECORDCOUNT");
			return count.intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw new NTBException("err.sys.DBError");
		}
	}

	@Override
	public List getCorrespondentBankByBankName(String correspondentBankName, String firstRecord, String endRecord)
			throws NTBException {
		String sql = "";
		sql = "select BICD_SWF_CD,BICD_CH_NAME,BICD_SWF_NAME1,BICD_SWF_NAME2,BICD_SWF_NAME3 from (select ROWNUM as rown,BICD_SWF_CD,BICD_CH_NAME,BICD_SWF_NAME1,BICD_SWF_NAME2,BICD_SWF_NAME3 FROM PGTBICD WHERE "
				+ "BICD_SWF_CD not like ('____MO%') "
				+ "AND (TRIM(BICD_SWF_NAME1)||TRIM(BICD_SWF_NAME2)||TRIM(BICD_SWF_NAME3) like ('%" + correspondentBankName + "%') or BICD_CH_NAME like ('%"+correspondentBankName+"%')) "
				+ "AND ROWNUM <= " + endRecord  + ") table_alias  where table_alias.rown > " + firstRecord;
		try {
			return genericJdbcDao.query(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NTBException("err.sys.DBError");
		}
	}
	
}
