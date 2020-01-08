package app.cib.service.enq;

import java.util.List;

import com.neturbo.set.exception.NTBException;

public interface MerchantEnquiryService {
	
	public String getMerchantGroupByUser(String userId) throws NTBException;
	
	public String getMerchantTypeByUser(String userId) throws NTBException;
	
	public List listMerchantIdByGroup(String merchantGroup) throws NTBException;
	
	public List listSummaryFilter(String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException;
	
	public List listDetailFilter(String dateType, String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException;	
	
	public String getMerNameForDet(String dateType, String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException;
	
}

