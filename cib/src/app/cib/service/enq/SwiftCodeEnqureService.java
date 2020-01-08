package app.cib.service.enq;

import java.util.ArrayList;
import java.util.List;

import com.neturbo.set.exception.NTBException;

public interface SwiftCodeEnqureService{

	public List getCorrespondentBankBySwiftCode(String correspondentBankCode,String firstRecord,String endRecord) throws NTBException;

	public int getCorrespondentBankBySwiftCodeCount(String correspondentBankCode) throws NTBException;

	public int getCorrespondentBankByBankNameCount(String correspondentBankName) throws NTBException;

	public List getCorrespondentBankByBankName(String correspondentBankName, String firstRecord, String endRecord) throws NTBException;
	
}

