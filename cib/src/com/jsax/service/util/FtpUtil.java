package com.jsax.service.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FTPMessageCollector;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.net.ftp.WriteMode;
import com.enterprisedt.net.*;


/**
 * Ftp鏂囦欢浼犺緭宸ュ叿绫�?
 * 
 * @author Cheng
 * 
 */
public class FtpUtil {

	private static FileTransferClient client = new FileTransferClient();

	private FtpUtil() {
	}

	public static void main(String[] args) throws Exception {
		String userName1 = "ftpuser";
		String userName2 = "mdbsit";
		String password = "123456";
		String remoteHost = "192.168.234.220";
		int remotePort = 21;
		int timeout = 10000;
		//----------------------------------------------------------------//
/*		FtpUtil.connectServer(userName1, password, remoteHost, remotePort, timeout);
		Calendar calendar = Calendar.getInstance();
		calendar.set(2016, 6, 22);
		Date time = calendar.getTime();
		//ftpDownloadFilesByDate("E:/test", "/home/ftpuser/expfile", new Date());
		ftpDownloadFilesByDate("E:/test", "/home/ftpuser/expfile", time);*/
		//-----------------------------------------------------------------//
		FtpUtil.connectServer(userName1, password, remoteHost, remotePort, timeout);
//		ftpDownloadFolder("C:/Users/admin/Desktop/�ķ�sit/payrollfiledownload", "/home/ftpuser/expfile");
//		//upload by ftp
		FtpUtil.uploadByFtp(new File("D:/batchupload/" + "BOBUORF.O13144410"));
		//download by ftp
		FtpUtil.DownloadFilesByName("D:/batchupload", "/home/ftpuser/expfile", "BSPCM01-200103-000000117-0000");
	}

	/**
	 * download Specify date file to local path by date
	 */
	public static void ftpDownloadFilesByDate(String localPath,
			String remotePath, Date date) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(date);
		File localDir = new File(localPath);
		if (!localDir.isDirectory()) {
			localDir.mkdirs();
		}
		List fileNameList = new ArrayList();
		FTPFile[] list = client.directoryList(remotePath);
		for (int i = 0; i < list.length; i++) {
			if (!list[i].isDir()) {
				String fileName = list[i].getName();
				String fileDate = fileName.substring(fileName.length() - 8);
				if (dateStr.equals(fileDate) && fileName.contains("NET")) {
					fileNameList.add(list[i].getName());
				}
			}
		}
		for (int i = 0; i < fileNameList.size(); i++) {
			String fileName = fileNameList.get(i).toString();
			String localFileName = fileName.substring(0,
					fileName.lastIndexOf("_"));
			ftpDownload(new File(localDir, localFileName).toString(),
					remotePath + "/" + (String) fileNameList.get(i));
//			Log.info((String)fileNameList.get(i) + "download finish");
			System.out.println((String) fileNameList.get(i) + "download");
		}
//		Log.info( "download down");
		System.out.println("download down");
	}

	/**
	 * download ftp file to local filepath run sql
	 */
	public static void ftpDownloadFolder(String localPath, String remotePath)
			throws Exception {
		File localDir = new File(localPath);
		if (!localDir.isDirectory()) {
			localDir.mkdirs();
		}
		List fileNameList = new ArrayList();
		FTPFile[] list = client.directoryList(remotePath);
		/*
		 * for (FTPFile ftpFile : list) { if (!ftpFile.isDir()) {
		 * fileNameList.add(ftpFile.getName()); } }
		 */
		for (int i = 0; i < list.length; i++) {
			if (!list[i].isDir()) {
				fileNameList.add(list[i].getName());
			}
		}

		/*
		 * for (String fileName : fileNameList) { ftpDownload(new File(localDir,
		 * fileName).toString(), remotePath + "/" + fileName); Log.info(fileName
		 * + "涓嬭浇�?屾垚"); }
		 */
		for (int i = 0; i < fileNameList.size(); i++) {
			ftpDownload(
					new File(localDir, (String) fileNameList.get(i)).toString(),
					remotePath + "/" + (String) fileNameList.get(i));
			// Log.info((String)fileNameList.get(i) + "download");
			Thread.sleep(1000);
			System.out.println((String) fileNameList.get(i) + "download finish");
		}
//		Log.info( "download down");

		System.out.println("download down");
	}

	/**
	 * 
	 * 
	 */
	public static void connectServer(String userName, String password,
			String remoteHost, int remotePort, int timeout) throws Exception {
		if (client.isConnected()) {
			
			client.disconnect();
		}
		client.setUserName(userName);
		client.setPassword(password);
		client.setRemoteHost(remoteHost);
		client.setRemotePort(remotePort);
		client.setTimeout(timeout);
		client.connect();
		System.out.println("connect sucessful");
//		Log.info("杩炴帴鎴愬姛");
	}

	/**
	 * 
	 * 
	 * @param localFileName
	 *            
	 * @param remoteFileName
	 *            
	 * @throws Exception
	 */
	public static void ftpDownload(String localFileName, String remoteFileName)
			throws Exception {
		client.downloadFile(localFileName, remoteFileName, WriteMode.OVERWRITE);
	}
	public static void ftpDelete(String fileName)//add by linrui for delete file after download
	        throws Exception {
		client.deleteFile(fileName);
	}
	private static void uploadByFtp(File uploadFile)  {
		// update by ftp
		if (uploadFile != null && uploadFile.exists()) {
			String host = "192.168.234.220";
			String user ="ftpuser";
			String password = "123456";
			String path = "/home/ftpuser/impfile";
			
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
//				Log.info("File '" + uploadFile + "' uploaded to remote host");

			} catch (Exception e) {
    			System.out.println("error");
			}
            if(ftp != null){
				try {
					ftp.quit();
				} catch (Exception e) {
					System.out.println("error2");
				}
            }
		} else {
			System.out.println("error3");
		}
		
	}
	//add by linrui for download by name
	public static void DownloadFilesByName(String localPath,
			String remotePath, String searchFileName) throws Exception {
		File localDir = new File(localPath);
		if (!localDir.isDirectory()) {
			localDir.mkdirs();
		}
		ftpDownload(new File(localDir, searchFileName).toString(),
				remotePath + "/" + searchFileName);
//		Log.info((String)fileNameList.get(i) + "download finish");
		System.out.println(searchFileName + "download");
	}

}
