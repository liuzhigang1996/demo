package app.cib.service.srv;

import app.cib.bo.srv.BatchFile;

import com.neturbo.set.exception.NTBException;

public interface UploadFileService {
	
	public void add(BatchFile batchFile) throws NTBException;
}
