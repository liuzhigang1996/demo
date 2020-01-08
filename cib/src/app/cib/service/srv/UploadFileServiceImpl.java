package app.cib.service.srv;

import app.cib.bo.srv.BatchFile;
import app.cib.dao.srv.BatchFileDao;

import com.neturbo.set.exception.NTBException;

public class UploadFileServiceImpl implements UploadFileService{

	private BatchFileDao batchFileDao = null;
	
	public void add(BatchFile batchFile) throws NTBException {
		batchFileDao.add(batchFile);	
	}


	public BatchFileDao getBatchFileDao() {
		return batchFileDao;
	}

	public void setBatchFileDao(BatchFileDao batchFileDao) {
		this.batchFileDao = batchFileDao;
	}

}
