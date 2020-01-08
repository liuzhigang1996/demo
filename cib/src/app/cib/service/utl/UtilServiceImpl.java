/**
 * @author hjs
 * 2006-11-21
 */
package app.cib.service.utl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bat.FileRequest;
import app.cib.service.sys.CutOffTimeService;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPFileFactory;
import com.enterprisedt.net.ftp.FTPMessageCollector;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Encoding;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs 2006-11-21
 */
public class UtilServiceImpl implements UtilService {

	private GenericJdbcDao genericJdbcDao;
	private static ChannelSftp sftp;
	private static Session session;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public Date getNextSchDate() throws NTBException {
		return getNextWorkingDate(new Date());
		// Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.DATE, 1);
		// if(isHoliday(cal.getTime())){
		// cal.setTime(new Date());
		// cal.add(Calendar.DATE, 1);
		// Date holidayEndDate = getHolidayEndDate(cal.getTime());
		// cal.setTime(holidayEndDate);
		// cal.add(Calendar.DATE, 1);
		// } else {
		// cal.setTime(new Date());
		// cal.add(Calendar.DATE, 1);
		// }
		// return cal.getTime();
	}

	public Date getNextWorkingDate(Date today) throws NTBException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DATE, 1);
		if (isHoliday(cal.getTime())) {
			Date holidayEndDate = getHolidayEndDate(cal.getTime());
			cal.setTime(holidayEndDate);
			cal.add(Calendar.DATE, 1);
		}
		return cal.getTime();
	}

	private Date getHolidayEndDate(Date beginDate) throws NTBException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		Date d = null;
		while (true) {
			cal.add(Calendar.DATE, 1);
			d = cal.getTime();
			if (!isHoliday(d)) {
				break;
			}
		}
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public void uploadFileToHost(String transCode, File uploadFile) throws NTBException {
		if (uploadFile == null || !uploadFile.exists() || uploadFile.isDirectory()) {
			return;
		}
		ApplicationContext appContext = Config.getAppContext();
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");

		// checkTransAvailable
		if (!Utils.null2EmptyWithTrim(transCode).equals("")) {
			//modify by long_zg 2014-12-15 for CR192 batch bob
			/*cutOffTimeService.checkTransAvailable(transCode);*/
			cutOffTimeService.checkTransAvailable(transCode,false,false);
		}

		// update by ftp
		uploadByFtp(uploadFile);

		// update by ftp
//		uploadBySftp(uploadFile);

		// backup
		bachupUploadFile(uploadFile);
		// delete
		deleteUploadFile(uploadFile);
	}
	//add by linrui for send batchtype 20180309
	public void uploadFileToHost(String transCode, File uploadFile, String batchType) throws NTBException {
		if (uploadFile == null || !uploadFile.exists() || uploadFile.isDirectory()) {
			return;
		}
		ApplicationContext appContext = Config.getAppContext();
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");
		
		// checkTransAvailable
		if (!Utils.null2EmptyWithTrim(transCode).equals("")) {
			//modify by long_zg 2014-12-15 for CR192 batch bob
			/*cutOffTimeService.checkTransAvailable(transCode);*/
			cutOffTimeService.checkTransAvailable(transCode,false,false);
		}
		
		// update by ftp
		uploadByFtp(uploadFile, batchType);
		
		// update by ftp
//		uploadBySftp(uploadFile);
		
		// backup
		bachupUploadFile(uploadFile);
		// delete
		deleteUploadFile(uploadFile);
	}

	private void uploadByFtp(File uploadFile) throws NTBException {
		// update by ftp
		if (uploadFile != null && uploadFile.exists()) {
			String host = Config.getProperty("UploadServer");
			String user = Config.getProperty("UploadUser");
			String password = Config.getProperty("UploadPassword");
			String path = Config.getProperty("UploadPath");
			
        	FTPClient ftp = null;
			try {
				// set up client
				ftp = new FTPClient();
				ftp.setRemoteHost(host);
				FTPMessageCollector listener = new FTPMessageCollector();
				ftp.setMessageListener(listener);
				ftp.debugResponses(true);
				// connect
				ftp.connect();
				// login
				ftp.login(user, password);
				// set up passive ASCII transfers
				ftp.setConnectMode(FTPConnectMode.ACTIVE);
				ftp.setType(FTPTransferType.ASCII);
				ftp.chdir(path);
				ftp.put(uploadFile.getPath(), uploadFile.getName(), false);
				Log.info("File '" + uploadFile + "' uploaded to remote host");

			} catch (Exception e) {
    			Log.error("Error uploading file to host via FTP", e);
				throw new NTBException("err.utl.UploadToHostError");
			}
            if(ftp != null){
				try {
					ftp.quit();
				} catch (Exception e) {
	    			Log.error("Error closing FTP", e);
				}
            }
		} else {
			Log.info("File[" + uploadFile.getName() + "] does not exist in upload directory");
			throw new NTBException("err.utl.UploadFileNotExist", new Object[] { uploadFile.getName() });
		}
	}
	//add by linrui for diff upload path 20180309
	private void uploadByFtp(File uploadFile ,String batchType) throws NTBException {
		// update by ftp
		String host,user,password,path;
		if (uploadFile != null && uploadFile.exists()) {
			if(FileRequest.TRANSFER_BANK_STM.equals(batchType)||FileRequest.TRANSFER_BANK_MTS.equals(batchType)){
				host = Config.getProperty("MDBUploadServer");
				user = Config.getProperty("MDBUploadUser");
				password = Config.getProperty("MDBUploadPassword");
				path = Config.getProperty("MDBUploadPath");
			}else{
				host = Config.getProperty("MaOverUploadServer");
				user = Config.getProperty("MaOverUploadUser");
				password = Config.getProperty("MaOverUploadPassword");
				path = Config.getProperty("MaOverUploadPath");	
			}
				//FTPClient ftp = null;
				/*// set up client
				ftp = new FTPClient();
				ftp.setRemoteHost(host);
				FTPMessageCollector listener = new FTPMessageCollector();
				ftp.setMessageListener(listener);
				//add by linrui 20180327 keep unix
				//ftp.dirDetails(uploadFile.getName(), null);
                ftp.setFTPFileFactory(new FTPFileFactory("UNIX"));				
				//end
				ftp.debugResponses(true);
				// connect
				ftp.connect();
				// login
				ftp.login(user, password);
				// set up passive ASCII transfers
				ftp.setConnectMode(FTPConnectMode.ACTIVE);
				ftp.setType(FTPTransferType.BINARY);//.ASCII
				ftp.chdir(path);
				ftp.put(uploadFile.getPath(), uploadFile.getName(), false);
				Log.info("File '" + uploadFile + "' uploaded to remote host");*/
		     try {
				connectServer(user,password,host,22);
				uploadFile(path,uploadFile);
			} catch (Exception e1) {
				Log.info("File[" + uploadFile.getName() + "] does not exist in upload directory");
				throw new NTBException("err.utl.UploadFileNotExist", new Object[] { uploadFile.getName() });
			}finally{
				disconnect();
			}
				
		}
	}
	
	
	public static void connectServer(String userName, String password,
			String remoteHost, int remotePort) throws Exception {
		try{
			Log.info("username: " + userName);
			Log.info("password:" + password);
			Log.info("ip address :" + remoteHost);
			JSch jsch = new JSch();
			session = jsch.getSession(userName, remoteHost, remotePort);  
	         
	         if (password != null) {  
	             session.setPassword(password);    
	         }  
	         Properties config = new Properties();  
	         config.put("StrictHostKeyChecking", "no");  
	             
	         session.setConfig(config);
	         session.connect();  
	           
	         Channel channel = session.openChannel("sftp");  
	         channel.connect(); 
	         Log.info("connect sftp success!");
	 
	         sftp = (ChannelSftp) channel;  
     } catch (JSchException e) {  
         e.printStackTrace();
     }  
	}
	
	public boolean uploadFile(String remotePath, File uploadFile)
	  {
	    FileInputStream in = null;
	    try
	    { 
	      createDir(remotePath);
	      in = new FileInputStream(uploadFile);
	      sftp.put(in, uploadFile.getName());
	      return true;
	    }
	    catch (FileNotFoundException e)
	    {
	      e.printStackTrace();
	    }
	    catch (SftpException e)
	    {
	      e.printStackTrace();
	    }
	    finally
	    {
	      if (in != null)
	      {
	        try
	        {
	          in.close();
	        }
	        catch (IOException e)
	        {
	          e.printStackTrace();
	        }
	      }
	    }
	    return false;
	  }
	
	public static void disconnect()
	  {
	    if (sftp != null)
	    {
	      if (sftp.isConnected())
	      {
	        sftp.disconnect();
	      }
	    }
	    if (session != null)
	    {
	      if (session.isConnected())
	      {
	        session.disconnect();
	      }
	    }
	  }
	
	public boolean createDir(String createpath)
	  {
	    try
	    {
	      if (isDirExist(createpath))
	      {
	        this.sftp.cd(createpath);
	        return true;
	      }
	      String pathArry[] = createpath.split("/");
	      StringBuffer filePath = new StringBuffer("/");
	      for (String path : pathArry)
	      {
	        if (path.equals(""))
	        {
	          continue;
	        }
	        filePath.append(path + "/");
	        if (isDirExist(filePath.toString()))
	        {
	          sftp.cd(filePath.toString());
	        }
	        else
	        {
	          // 建立目录
	          sftp.mkdir(filePath.toString());
	          // 进入并设置为当前目录
	          sftp.cd(filePath.toString());
	        }
	 
	      }
	      this.sftp.cd(createpath);
	      return true;
	    }
	    catch (SftpException e)
	    {
	      e.printStackTrace();
	    }
	    return false;
	  }
	
	public boolean isDirExist(String directory)
	  {
	    boolean isDirExistFlag = false;
	    try
	    {
	      SftpATTRS sftpATTRS = sftp.lstat(directory);
	      isDirExistFlag = true;
	      return sftpATTRS.isDir();
	    }
	    catch (Exception e)
	    {
	      if (e.getMessage().toLowerCase().equals("no such file"))
	      {
	        isDirExistFlag = false;
	      }
	    }
	    return isDirExistFlag;
	  }

	/**
	 * hjs: ͨ��SFTP�����ļ���REMOTE HOST
	 * 
	 * @param uploadFile
	 * @throws NTBException
	 */
	private void uploadBySftp(File uploadFile) throws NTBException {
		String knownHosts = Config.getProperty("KnownHosts");
		String host = Config.getProperty("UploadServer");
		String user = Config.getProperty("UploadUser");
		String password = Config.getProperty("UploadPassword");
		String path = Config.getProperty("UploadPath");
		int port = 22;

		JSch jsch = new JSch();
		Session session = null;
		InputStream localFileInputStream = null;
		try {
			session = jsch.getSession(user, host, port);
			session.setPassword(password);
			if (knownHosts == null || "".equals(knownHosts)) {
				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
			} else {
				jsch.setKnownHosts(knownHosts);
			}
			session.connect();

			ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();

			// convert ASCII to EBCDIC
			localFileInputStream = convASCII2EBCDIC(new FileInputStream(uploadFile));

			String[] s = uploadFile.getName().split("\\.");
			String remoteFile = path + "/" + s[0] + ".FILE" + "/" + s[1] + ".MBR";

			channel.put(localFileInputStream, remoteFile, ChannelSftp.OVERWRITE);
			Log.info("File '" + uploadFile + "' was uploaded to remote host " + host);

		} catch (Exception e) {
			Log.error("Error uploading file to host via SFTP", e);
			throw new NTBException("err.utl.UploadToHostError");

		} finally {
			if (localFileInputStream != null) {
				try {
					localFileInputStream.close();
				} catch (IOException e) {
				}
			}
			if (session != null)
				session.disconnect();
		}
	}

	private void deleteUploadFile(File uploadFile) throws NTBException {
		uploadFile.delete();
	}

	private void bachupUploadFile(File uploadFile) throws NTBException {
		String backDirPath = uploadFile.getParent() + "/" + DateTime.formatDate(new Date(), "yyyyMMdd");
		File backDir = new File(backDirPath);

		if (!backDir.exists()) {
			backDir.mkdir();
		}
		uploadFile.renameTo(new File(backDirPath + "/" + uploadFile.getName()));
	}

	public boolean isHoliday(Date date) throws NTBException {
			String sql = "select * from HS_HOLIDAY where \"DATE\" = ?";//MOD BY LINRUI
			String strDate = DateTime.formatDate(date, "yyyyMMdd");

			try {
				List list = this.genericJdbcDao.query(sql, new Object[] { strDate });
				return list.size() > 0;
			} catch (Exception e) {
				Log.error(e.getMessage(), e);
				throw new NTBException("err.utl.CheckHolidayError");
			}
	}

	private boolean isWeekend(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return (dayOfWeek == 1 || dayOfWeek == 7);
	}

	/**
	 * hjs 20090422
	 * 
	 * @param in
	 * @return The stream which has been converted if it's a ascii file, or
	 *         returns the passed-in one.
	 * @throws IOException
	 */
	private InputStream convASCII2EBCDIC(InputStream in) throws IOException {
		byte[] buf = new byte[4096];
		int length = 0;
		byte[] varBuf = new byte[0];
		while ((length = in.read(buf)) > -1) {
			int realLength = 0;
			for (int i = 0; i < length; i++) {
				if (buf[i] == 0x00) { // not a ascii code, stop converting
					return in;
				}
//				if (buf[i] == 0x0D) { // 'Carriage Return' code
//					if (buf[i + 1] == 0x0A) { // DOS
//						continue; // skip CR
//					} else { // MAC OS
//						buf[i] = 0x0A; // LF replaces CR
//					}
//				}
				if (buf[i] == 0x0D || buf[i] == 0x0A) {
					continue; // skip LF and CR
				}
				buf[realLength] = Encoding.ASCII2EBCDIC(buf[i]);
				realLength++;
			}
			byte[] tmpBuf = new byte[varBuf.length + realLength];
			System.arraycopy(varBuf, 0, tmpBuf, 0, varBuf.length);
			System.arraycopy(buf, 0, tmpBuf, varBuf.length, realLength);
			varBuf = tmpBuf;
		}
		return new ByteArrayInputStream(varBuf);
	}
//add by linrui for stoprequest input acctno check ccy
	public String getCcyByAcct(String acctno)  {
		List ccyList = new ArrayList();
		String sql = "select currency from corp_account where account_no= ?";
		try {
			List list = this.genericJdbcDao.query(sql, new Object[] { acctno });
			if(list.size()>0){				
				return (list.get(0).toString()).substring(10,13);
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
}
