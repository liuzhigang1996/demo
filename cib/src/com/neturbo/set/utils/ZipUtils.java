/**
 * @author hjs
 * 2007-4-23
 */
package com.neturbo.set.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author hjs
 * 2007-4-23
 */
public class ZipUtils {
	
	public static OutputStream files2Zip(String[] files) throws Exception {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		// create the ZIP file
		OutputStream byteOutStream = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(byteOutStream);

		// compress the files
		for (int i = 0; i < files.length; i++) {
			FileInputStream in = new FileInputStream(files[i]);
			
			File f = new File(files[i]);
			// add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry(f.getName()));

			// transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			// complete the entry
			out.closeEntry();
			in.close();
		}

		// complete the ZIP file
		out.close();
		
		return byteOutStream;
	}

	public static void ZipStream(ByteArrayOutputStream byteArrayOs,
			OutputStream os, String fileName) throws Exception {
		byte[] b = new byte[4096];
		int len = 0;
		ZipOutputStream zipOut = new ZipOutputStream(os);
		zipOut.putNextEntry(new ZipEntry(fileName)); // Zip 内文件名
		ByteArrayInputStream in = new ByteArrayInputStream(
				byteArrayOs.toByteArray());
		while ((len = in.read(b)) != -1) {
			zipOut.write(b, 0, len);
		}
		zipOut.close();
		os.close();
	}

	public static void main(String[] args) {
	}

}
