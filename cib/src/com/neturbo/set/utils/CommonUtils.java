/*
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.neturbo.set.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * @author panwen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CommonUtils {
	private CommonUtils() {
	}

	/**
	 * 從流中讀取全部字節
	 * @param res
	 * @param dFile
	 * @param dName
	 */
	public static byte[] getAllBytesFromStream(InputStream is)
		throws IOException {
		byte[] bytes = null;
		byte[] tmpBytes = null;
		int totalRead = 0;
		int readBytes = 0;

		bytes = new byte[BUFFER_SIZE];

		while ((readBytes =
			is.read(bytes, totalRead, bytes.length - totalRead))
			!= -1) {

			totalRead += readBytes;

			if (totalRead == bytes.length) {
				tmpBytes = new byte[Math.round(bytes.length * RATE)];
				System.arraycopy(bytes, 0, tmpBytes, 0, totalRead);
				bytes = tmpBytes;
			}
		}

		if (totalRead < bytes.length) {
			tmpBytes = new byte[totalRead];
			System.arraycopy(bytes, 0, tmpBytes, 0, totalRead);
			bytes = tmpBytes;
		}

		return bytes;
	}

	/** 下载文件
	 * @param res avax.servlet.http.HttpServletResponse
	 * @param dFile java.io.File 要下载的文件
	 */
	public static void downloadFile(HttpServletResponse res, File dFile) {
		downloadFile(res, dFile, null);
	}

	/** 下载文件
	 * @param res avax.servlet.http.HttpServletResponse
	 * @param dFile java.io.File 要下载的文件
	 * @param dName java.lang.String 下载缺省的文件名
	 */
	public static void downloadFile(
		HttpServletResponse res,
		File dFile,
		String dName) {

		if (dFile == null || !dFile.exists() || !dFile.isFile()) {
			throw new IllegalStateException("File not found");
		}

		if (dName == null) {
			dName = dFile.getName();
		}

		int totalLength = (int) dFile.length();

		res.setContentType("application/x-msdownload");
		res.setContentLength(totalLength);
		res.setHeader(
			"Content-Disposition",
			new StringBuffer("attachment; filename=").append(dName).toString());

		byte[] buffer = new byte[BUFFER_SIZE];

		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;

		try {
			bin = new BufferedInputStream(new FileInputStream(dFile));
			bout = new BufferedOutputStream(res.getOutputStream());

			int totalRead = 0;
			int readBytes = 0;

			while (totalRead < totalLength) {
				readBytes = bin.read(buffer, 0, BUFFER_SIZE);
				totalRead += readBytes;
				bout.write(buffer, 0, readBytes);
			}

			bout.flush();
			bout.close();
			bout = null;

			bin.close();
			bin = null;

		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {

			if (bout != null) {
				try {
					bout.flush();
					bout.close();
					bout = null;
				} catch (Exception e) {
				}
			}

			if (bin != null) {
				try {
					bin.close();
					bin = null;
				} catch (Exception e) {
				}
			}

		}

	}

	private static final int BUFFER_SIZE = 32 * 1024;

	private static final float RATE = 1.5f;

}
