package app.cib.print;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;



public class ImageHostServlet extends HttpServlet {


	private static final long serialVersionUID = 8360809240666191049L;

	private static String UPLOAD_DIR =  "C:/ScanImageDir";

	private static final int BUFFER_SIZE = 4 * 1024;

	public static final String IMAGE_FILE_EXT = "sfi";

	public static final String PACK_SPLITER = "||";

	public static final String IMAGE_SPLITER = "::";

	public static final String ITEM_SPLITER = ",";

	public void init() throws ServletException {
		super.init();

		try {
			File aFile = new File(UPLOAD_DIR);
			if (!aFile.exists()) {
				aFile.mkdir();
			}

			System.out.println("ImageHostServlet init()");
		} catch (Exception e) {
			System.out.println("ImageHostServlet UPLOAD_DIR("+UPLOAD_DIR+") init ERROR ");
		}
	}
	

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String actType = req.getParameter("actType");

		if ("GetContent".equals(actType)) {
			doGetContent(req, resp);
		} else if ("GetPack".equals(actType)) {
			doGetPack(req, resp);
		} else 
		if ("GetPackId".equals(actType)) {
			doGetPackId(req, resp);
		} else 
			if ("UploadPack".equals(actType)) {
			doUploadPack(req, resp);
		}
	}
	
	private void doGetContent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			resp.setContentType("application/octet-stream");

			String imageId = req.getParameter("imageId");
			String imagePath = req.getParameter("imagePath");
			String imageType = req.getParameter("imageType");

			// query imagePath from DB
			String scanfineVersion = "";
			String packId = "";
			Map image = querySingleImageInfo(imageId);//锟斤拷锟斤拷菘锟斤拷取图锟斤拷锟斤拷息
			if (null != image) {
				imagePath = (String) image.get("IMAGE_PATH");
				scanfineVersion = (String) image.get("SCANFINE_VERSION");
				packId = (String) image.get("PACK_ID");
				System.out.println("[ImageHostServlet]-doGetContent ,  imageMap="
								+ image);
			}

			BufferedInputStream bin = null;
			BufferedOutputStream bout = null;
			StringBuffer fileName;
			// 
			if ("1".equals(scanfineVersion)) {
				// before image migration

				fileName = new StringBuffer(UPLOAD_DIR);
				if (fileName.length() > 0
						&& fileName.charAt(fileName.length() - 1) != File.separatorChar) {
					fileName.append(File.separator);
				}
				fileName.append(imagePath);

			} else if ("2".equals(scanfineVersion)) {
				// after image migration
				fileName = new StringBuffer(UPLOAD_DIR);

				if (fileName.length() > 0
						&& fileName.charAt(fileName.length() - 1) != File.separatorChar) {
					fileName.append(File.separator);
				}

				fileName.append(imagePath);
				if (fileName.length() > 0
						&& fileName.charAt(fileName.length() - 1) != File.separatorChar) {
					fileName.append(File.separator);
				}
				fileName.append(packId);
				if (fileName.length() > 0
						&& fileName.charAt(fileName.length() - 1) != File.separatorChar) {
					fileName.append(File.separator);
				}
				fileName.append(imageId);
				fileName.append("_");
				fileName.append(imageType);
				fileName.append(".");
				fileName.append(IMAGE_FILE_EXT);
			} else {
				fileName = new StringBuffer(UPLOAD_DIR);

				if (fileName.length() > 0
						&& fileName.charAt(fileName.length() - 1) != File.separatorChar) {
					fileName.append(File.separator);
				}

				fileName.append(imagePath);
				if (fileName.length() > 0
						&& fileName.charAt(fileName.length() - 1) != File.separatorChar) {
					fileName.append(File.separator);
				}
				fileName.append(packId);
				if (fileName.length() > 0
						&& fileName.charAt(fileName.length() - 1) != File.separatorChar) {
					fileName.append(File.separator);
				}
				fileName.append(imageId);
				fileName.append("_");
				fileName.append(imageType);
				fileName.append(".");
				fileName.append(IMAGE_FILE_EXT);
			}
			byte[] buffer = null;
			int readCount = -1;

			bin = new BufferedInputStream(new FileInputStream(fileName
					.toString()));
			bout = new BufferedOutputStream(resp.getOutputStream());

			buffer = new byte[BUFFER_SIZE];

			while ((readCount = bin.read(buffer)) > 0) {
				bout.write(buffer, 0, readCount);
			}
			buffer = null;

			if (bin != null) {
				try {
					bin.close();
					bin = null;
				} catch (IOException ioe) {
				}
			}

			if (bout != null) {
				try {
					bout.flush();
					bout.close();
					bout = null;
				} catch (IOException ioe) {
				}
			}
		} catch (Exception e) {
			System.out.println("[ImageHostServlet]-doGetContent ERROR "+e.toString());

		}
	}

	/**锟斤拷取图锟斤拷锟叫憋拷锟斤拷息
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doGetPack(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		StringBuffer buffer = new StringBuffer();

		System.out.println("[ImageHostServlet]-doGetPack begin" );
		try {
			buffer.append("000000");
			buffer.append(PACK_SPLITER);
			buffer.append("OK");
			buffer.append(PACK_SPLITER);

			String packId = req.getParameter("packId");


			Object[] parms = new Object[] { packId };

			Map packInfo = selectImagePack(packId);

			if (null != packInfo && packInfo.size() > 0) {
				buffer.append(packInfo.get("DOC_FORM_VER"));
				buffer.append(ITEM_SPLITER);

				buffer.append(packInfo.get("DOC_FORM_ID"));
				buffer.append(ITEM_SPLITER);

				buffer.append(packInfo.get("DOCUMENT_ID"));
				buffer.append(ITEM_SPLITER);

				buffer.append(packInfo.get("PACK_TITLE"));
				buffer.append(ITEM_SPLITER);

				buffer.append(packInfo.get("PACK_CATEGORY"));
				buffer.append(ITEM_SPLITER);

				buffer.append(packInfo.get("PACK_DESC"));
				buffer.append(ITEM_SPLITER);

				buffer.append(packInfo.get("DEAL_OPER_ID"));
				buffer.append(ITEM_SPLITER);

				buffer.append(new Date());
				buffer.append(PACK_SPLITER);

				parms = new Object[] { packId, "P" };
				// List imageList = genericJdbcDao.query(SELECT_LMS_IMAGE,
				// parms);
				List imageList = new ArrayList();
				imageList.add(querySingleImageInfo(""));
				if (null != imageList && imageList.size() > 0) {

					for (int i = 0; i < imageList.size(); i++) {
						if (i != 0) {
							buffer.append(IMAGE_SPLITER);
						}

						Map imageInfo = (Map) imageList.get(i);

						buffer.append(imageInfo.get("IMAGE_ID"));
						buffer.append(ITEM_SPLITER);

						buffer.append(imageInfo.get("IMAGE_PATH"));
						buffer.append(ITEM_SPLITER);

						buffer.append(imageInfo.get("IMAGE_MODE"));
						buffer.append(ITEM_SPLITER);

						buffer.append(imageInfo.get("IMAGE_SOURCE"));
						buffer.append(ITEM_SPLITER);

						buffer.append(imageInfo.get("IMAGE_WIDTH"));
						buffer.append(ITEM_SPLITER);

						buffer.append(imageInfo.get("IMAGE_HEIGHT"));
						buffer.append(ITEM_SPLITER);

						buffer.append(imageInfo.get("IMAGE_MEMO"));
						buffer.append(ITEM_SPLITER);

						buffer.append(imageInfo.get("IMAGE_STATUS"));
						buffer.append(ITEM_SPLITER);

						buffer.append(imageInfo.get("IMAGE_VERSION"));
						buffer.append(ITEM_SPLITER);

						buffer.append(imageInfo
												.get("IMAGE_TIMESTAMP"));

						// buffer.append(ITEM_SPLITER);
						// buffer.append(imageInfo.get("SCANFINE_VERSION"));

					}
				}
			} else {
				buffer.delete(0, buffer.length());
				buffer.append("111111");
				buffer.append(PACK_SPLITER);
				buffer.append("Pack not exists");
			}
			// System.out.println("[ImageHostServlet]-doGetPack
			// buffer="+buffer.toString());
		} catch (Exception e) {
			System.out.println("doGetPack ERROR "+e.toString());
			buffer.delete(0, buffer.length());
		}

		if (buffer.length() == 0) {
			buffer.append("999999");
			buffer.append(PACK_SPLITER);
			buffer.append("System ERROR");
		}

		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter pw = resp.getWriter();
		pw.print(buffer.toString());
		pw.flush();
		pw = null;

		System.out.println("[ImageHostServlet]-doGetPack  Output info:" + buffer);
	}



	/**锟斤拷取一锟斤拷锟斤拷图锟斤拷锟斤拷ID(packId)
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doGetPackId(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		PrintWriter pw = resp.getWriter();
		String packId = "";
		try {
			packId = "IMG_PACK_0000027721";//锟剿达拷锟斤拷锟揭伙拷锟斤拷锟絧ackId
		} catch (Exception ne) {
			System.out.println("ImageHostServlet getNewImagepackId ERROR ");
		}
		pw.print(packId);// IMAGE_PACK
		pw.flush();
		pw = null;
	}

	/**
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doUploadPack(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		StringBuffer buffer = new StringBuffer();

		DiskFileUpload fu = new DiskFileUpload();
		fu.setSizeMax(-1);
		fu.setSizeThreshold(4096);
		fu.setRepositoryPath(UPLOAD_DIR);

		try {
			List items = fu.parseRequest(req);
			Map fieldMap = new HashMap();
			List fileList = new ArrayList();
			
//			String imagePath = getCurrentDate();//锟斤拷取锟斤拷前锟斤拷锟节ｏ拷锟斤拷锟斤拷锟斤拷锟皆革拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷募锟斤拷校锟绞癸拷锟斤拷锟街硷拷锟斤拷锟斤拷锟斤拷锟街★拷
			String imagePath = "20120523";//锟斤拷锟斤拷锟斤拷使锟斤拷hardcode锟斤拷锟斤拷锟节ｏ拷20120523
			if (null != items && items.size() > 0) {
				FileItem item = null;
				for (int i = 0; i < items.size(); i++) {
					item = (FileItem) items.get(i);

					if (item.isFormField()) {
						fieldMap.put(item.getFieldName(), item.getString());
					} else {
						fileList.add(item);
					}
				}

				String uploadType = (String) fieldMap.get("uploadType");
				String userId = (String) fieldMap.get("userId");

				String packId = (String) fieldMap.get("packId");
				String packVer = (String) fieldMap.get("packVer");

				String packTitle = (String) fieldMap.get("packTitle");
				String packCategory = (String) fieldMap.get("packCategory");
				String packDesc = (String) fieldMap.get("packDesc");

				String documentId = (String) fieldMap.get("docFormId");
				String docFormType = (String) fieldMap.get("docFormType");

				String[] docFormTypeArray = docFormType.split("||");
				String loanLogId = "";
				String stepName = "";
				if (null != docFormTypeArray && docFormTypeArray.length > 0) {
					loanLogId = docFormTypeArray[0];
					if (docFormTypeArray.length > 1) {
						stepName = docFormTypeArray[1];
					}
				}
				String imagePackageId = (String) fieldMap.get("refNo");

				List imageList = new ArrayList();
				String imageInfo = (String) fieldMap.get("imageInfo");
//				Map userMap = (HashMap) queryUserInfo(userId);

//				String userName = "";
//				String unitId = "";
//				String unitName = "";
//				if (null != userMap) {
//					userName = (String) userMap.get("USER_NAME");
//					unitId = (String) userMap.get("UNIT_ID");
//					unitName = (String) userMap.get("UNIT_NAME");
//				}
				if (null != imageInfo) {
					imageInfo = imageInfo.trim();
				}


				if (fileList.size() > 0) {
					StringBuffer pathName = new StringBuffer(UPLOAD_DIR);

					if (pathName.length() > 0
							&& pathName.charAt(pathName.length() - 1) != File.separatorChar) {
						pathName.append(File.separator);
					}
					pathName.append(imagePath);
					pathName.append(File.separator);

					File aFile = new File(pathName.toString());

					if (!aFile.exists()) {
						aFile.mkdir();
					}

					aFile = null;
					// add by Peng Haisen 2010-11-1 begin
					pathName.append(packId);
					pathName.append(File.separator);
					// add by Peng Haisen 2010-11-1 end
					aFile = new File(pathName.toString());

					if (!aFile.exists()) {
						aFile.mkdir();
					}

					aFile = null;

					String fileName = null;
					for (int i = 0; i < fileList.size(); i++) {
						item = (FileItem) fileList.get(i);
						fileName = extractFileName(item.getName());
						aFile = new File(pathName.toString(), fileName);
						item.write(aFile);
						aFile = null;
					}
				}


				// 锟斤拷每丶锟斤拷锟斤拷偷锟揭筹拷锟斤拷斜锟�


			}

			buffer.append("000000");
			buffer.append(PACK_SPLITER);
			buffer.append("OK");
			buffer.append(PACK_SPLITER);
			buffer.append(imagePath);
			buffer.append(PACK_SPLITER);
//			buffer.append(DateTime.formatDate(imageTimestamp,
//					"yyyy-MM-dd HH:mm:ss"));
			buffer.append(new Date());
			
		} catch (Exception e) {
			System.out.println("doUploadPack ERROR");

			buffer.delete(0, buffer.length());

		}

		if (buffer.length() == 0) {
			buffer.append("999999");
			buffer.append(PACK_SPLITER);
			buffer.append("System ERROR");
		}

		resp.setContentType("text/plain");
		PrintWriter pw = resp.getWriter();
		pw.print(buffer.toString());
		pw.flush();
		pw = null;
	}

	  public static String extractFileName(String fullName) throws Exception {
		  String fileName = fullName;

		  if (fullName != null) {
		    int lastIndex = fullName.lastIndexOf(File.separator);

		    if (lastIndex > -1) {
		      fileName = fullName.substring(lastIndex + 1);
		    }
		  }

		  return fileName;
	}

	  //模锟斤拷锟斤拷锟捷库返锟截碉拷锟斤拷图锟斤拷锟斤拷息
		public Map querySingleImageInfo(String imageId) throws Exception {
			Map imageMap = new HashMap();
			try {

				imageMap.put("SEQ_NO", "100009");
				imageMap.put("IMAGE_ID", "744000001");
				imageMap.put("PACK_ID", "744");
				imageMap.put("IMAGE_TYPE", "I");
				imageMap.put("IMAGE_PATH", "20180423/744000001");
				imageMap.put("IMAGE_PAGENO", "14");
				imageMap.put("IMAGE_MODE", "tif");
				imageMap.put("IMAGE_SOURCE", "1");
				imageMap.put("IMAGE_WIDTH", "98");
				imageMap.put("IMAGE_MD5", "");
				imageMap.put("IMAGE_MEMO", "");
				imageMap.put("IMAGE_HEIGHT", "98");
				imageMap.put("IMAGE_STATUS", "0");
				imageMap.put("IMAGE_VERSION", "1");
				imageMap.put("IMAGE_TIMESTAMP", "2012-05-23 10:48:03");
				imageMap.put("DOC_FORM_ID", "");
				imageMap.put("SCANFINE_VERSION", "1");
				
			} catch (Exception e) {
				System.out.println("ERROR in querySingleImageInfo");
				throw new Exception(e.getMessage());
			}
			return imageMap;
		}
//模锟斤拷锟斤拷锟捷库返锟斤拷图锟斤拷锟斤拷锟较�
		public Map selectImagePack(String packId) throws Exception {
			Map packMap = new HashMap();

			packMap.put("PACK_ID", "744");
			packMap.put("DOC_FORM_ID", "");
			packMap.put("DOC_FORM_VER", "");
			packMap.put("PACK_TITLE", "");
			packMap.put("DEAL_TIME", "2012-05-23 10:48:03");
			packMap.put("DEAL_OPER_ID", "user01");
			packMap.put("DEAL_OPER_NAME", "User Name 01");
			packMap.put("DEAL_UNIT_ID", "744");
			packMap.put("DEAL_UNIT_NAME", "HQ");
//			
//			packMap.put("DOCUMENT_ID", "100009");
//			packMap.put("PACK_CATEGORY", "100009");
//			packMap.put("PACK_DESC", "100009");
//			packMap.put("STEP_NAME", "100009");
//			packMap.put("LOAN_LOG_ID", "100009");
//			packMap.put("STATUS", "100009");
			return packMap;
		}
		
	public String getCurrentDate() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String date = formatter.format(new Date());
        
        return date;
	}
}
