package app.cib.service.bat;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Map;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.bat.FileRequestFileBean;
import app.cib.bo.sys.CorpUser;

import com.neturbo.set.exception.NTBException;

public interface TransferBankStMService {
	public FileRequestFileBean parseFile(CorpUser corpUser, InputStream inStream) throws NTBException;
	public void approveTransferBankStM(FileRequest fileRequest, CorpUser corpUser, BigDecimal equivalentMOP) throws NTBException;
	public void clearUnavailableDataByCorpId(String corpId) throws NTBException;
	public void clearUnavailableDataByPaybatchId(String batchId) throws NTBException;
	public void updateStatus(String batchId) throws NTBException;
	public FileRequest viewAvaliableFileRequest(String batchId) throws NTBException;
	public void rejectFileRequest(FileRequest fileRequest) throws NTBException ;
	public void cancelUpload(String batchId, OutputStream outStream,InputStream inStreamForParsing, File saveFile);
	//add by linrui 20180308
	public Map toHostTransferSTM(FileRequest pojoFileRequest,
			CorpUser corpUser, String txnType) throws NTBException;
}
