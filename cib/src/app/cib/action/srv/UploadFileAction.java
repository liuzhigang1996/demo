package app.cib.action.srv;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import app.cib.bo.srv.BatchFile;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.srv.UploadFileService;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;

public class UploadFileAction extends CibAction {

	public void load() throws NTBException {

	}

	public void upload() throws NTBException {
		// �z���Ƿ���ݔ���ļ�
		if (getUploadFileName().equals("")) {
			throw new NTBException("err.txn.fileNotExist");
		}
		// �z���ļ���С
		// int fileSize = getUploadFileSize();
		String transId = CibIdGenerator.getRefNoForTransaction();
		String savePath = Config.getProperty("UploadDir") + "/";
		String doc_name = getUploadFileName();
		String fileType = getParameter("fileType");
		RBFactory rbFactory = RBFactory.getInstance("app.cib.resource.srv.upload_file_name");
		String fileName = rbFactory.getString(fileType)+DateTime.formatDate(new Date(), "ddHHmmss");
		//HashMap resultData = new HashMap();
		CorpUser user = (CorpUser) getUser();
		try {

			InputStream fileStream = getUploadFileInputStream();
			FileOutputStream outStream = new FileOutputStream(savePath
					+ fileName);
			// ���Ĳ�д�������
			byte[] buffer = new byte[4096];
			int readCount = -1;
			while ((readCount = fileStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, readCount);
			}	
			outStream.close();
			fileStream.close();
			
			BatchFile batchFile = new BatchFile();
			batchFile.setTransId(transId);
			batchFile.setRequestTime(new Date());
			batchFile.setCorpId(user.getCorpId());
			batchFile.setFileName(fileName);
			batchFile.setFileType(fileType);
			batchFile.setStatus(Constants.STATUS_NORMAL);
			batchFile.setUserId(user.getUserId());
			
			UploadFileService uploadFileService = (UploadFileService) Config.getAppContext()
			.getBean("UploadFileService");
			uploadFileService.add(batchFile);
		} catch (Exception e) {
			throw new NTBException("err.txn.fileUploadFault");
		}
	}

}
