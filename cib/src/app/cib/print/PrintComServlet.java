package app.cib.print;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationContext;
import app.cib.core.CibIdGenerator;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.utils.ZipUtils;
import com.neturbo.set.xml.*;

public class PrintComServlet extends HttpServlet {

	String PRINT_TEMPLATE_PATH = Config.getProperty("PrintTemplateDir");

	private static final int BUFFER_SIZE = 4 * 1024;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	public void validate(HttpSession session) {

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String actType = req.getParameter("Action");
		if (actType != null && !actType.equals("")) {
			Method theWillBeCalledMethod = null;
			try {
				theWillBeCalledMethod = PrintComServlet.class
						.getDeclaredMethod(actType, new Class[] {
								HttpServletRequest.class,
								HttpServletResponse.class });
				theWillBeCalledMethod.invoke(this, new Object[] { req, resp });
			} catch (NoSuchMethodException nsme) {
				nsme.printStackTrace(System.err);
			} catch (IllegalAccessException iae) {
				iae.printStackTrace(System.err);
			} catch (InvocationTargetException ite) {
				ite.printStackTrace(System.err);
			}

		}
	}

	/**
	 * 根据批次获取待打印信息.
	 * 
	 * @param HttpServletRequest
	 *            req 包含客户请求信息的映像集合
	 * @param HttpServletResponse
	 *            res 包含请求返回信息的映像集合
	 * @throws ServiceException
	 *             ,IOException 抛出处理中出现的异常
	 */
	private void getPrintData(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String printBatchId = req.getParameter("PrintId"); // 批次号
			String printUser = "";
			String ENCRYPT_KEY = "i2A364C5d6E7f89O";
			/*
			NTBUser user = ((NTBUser) session.getAttribute("UserObject$Of$Neturbo"));
			if(user == null){
				throw new NTBException("err.sys.NotLogined"); 	
			}else{
				printUser = user.getUserId();
			}
			*/
			ApplicationContext appContext = Config.getAppContext();
			GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
			String sql = "select t.print_id, t.cif_no, t.user_id, t.cif_name, t.login_id, to_char(t.create_time, 'yyyy-mm-dd hh24:mi:ss') as create_time, t.remark, t.operator_id from corp_print_info t where t.print_id=? and t.status = '0'";
			java.util.Map map = new java.util.HashMap();
			try {
				map = dao.querySingleRow(sql, new Object[] {printBatchId});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.error("listPrintPassword Error", e);
				throw new NTBException("System Error"); 
			}
			String cifName = Utils.null2EmptyWithTrim(map.get("CIF_NAME"));
			String cifNo = Utils.null2EmptyWithTrim(map.get("CIF_NO"));
			String loginId = Utils.null2EmptyWithTrim(map.get("LOGIN_ID"));
			String userId = Utils.null2EmptyWithTrim(map.get("USER_ID"));
			printUser = Utils.null2EmptyWithTrim(map.get("OPERATOR_ID"));
//			String loginPassword = genPassword();
			//mod by linrui 20190809
//			String loginPassword = genFixLenPassword();
			//mod by linrui 20190815
			String loginPassword = getRandomCode();
			
			String transPassword = "";
			boolean genTransPswd = checkIfGenTransPwsd(cifNo, userId);
			if(genTransPswd){
//			    transPassword = genPassword();
				//mod by linrui 20190809
//			    transPassword = genFixLenPassword();
				//mod by linrui 20190815
			    transPassword = getRandomCode();
			}
			
            String encryptedLoginPswd = Encryption.digest(userId +loginPassword, "MD5");
            
            String encryptedTransPswd = "";
            if(genTransPswd){
                encryptedTransPswd = Encryption.digest(userId +transPassword, "MD5");
            }
            
			String zipFlag = Utils.null2Empty(req.getParameter("ZipFlag"));
			Log.info("Print client calling -> getPrintData: PrintId="
					+ printBatchId + ", ZipFlag=" + zipFlag);

			XMLElement root = new XMLElement("print-data");
			root.setAttribute("print-type", "pinmailer");
			XMLWriter xMLWriter = XMLFactory.getWriter();
			List printData = new ArrayList();
			Map rec1 = new LinkedHashMap();
			
			String encryCifName =DesUtil.encrypt(cifName, ENCRYPT_KEY);
			rec1.put("var1", encryCifName);
			
			String printCifNo = "";
			if(!cifNo.equals("") && cifNo.length() > 1){
				printCifNo = cifNo.substring(1);
			}
			String encryPrintCifNo =DesUtil.encrypt(printCifNo, ENCRYPT_KEY);
			rec1.put("var2", encryPrintCifNo);

			
			String encryLoginLoginId =DesUtil.encrypt(loginId, ENCRYPT_KEY);
			rec1.put("var3", encryLoginLoginId);
			
			String encryLoginPassword =DesUtil.encrypt(loginPassword, ENCRYPT_KEY);
			rec1.put("var4", encryLoginPassword);
			
			if(genTransPswd){
			    String encryTransPassword =DesUtil.encrypt(transPassword, ENCRYPT_KEY);
			    rec1.put("var5", encryTransPassword);
			}
			
			printData.add(rec1);

			/* == 根据返回List生成控件所需XML== */
			XMLElement pages = new XMLElement("pages");
			XMLElement page = null;
			root.addChild(pages);
			page = new XMLElement("page");
			page.setAttribute("no", "1");
			pages.addChild(page);
			BigDecimal rank = null;
			for (int i = 0; i < printData.size(); i++) {
				Map data = (Map) printData.get(i);
				XMLElement rec = getRecXMLElement(data);
				page.addChild(rec);

			}

			xMLWriter.setRootElement(root);
			xMLWriter.setCharset("UTF-8");
			if (zipFlag.equals("Y")) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				xMLWriter.setOutput(bos);
				xMLWriter.Marshal();
				Log.info("Print data for " + printBatchId + ":\n");
						//+ new String(bos.toByteArray()));
				ZipUtils.ZipStream(bos, resp.getOutputStream(), printBatchId);
			} else {
				xMLWriter.setOutput(resp.getOutputStream());
				xMLWriter.Marshal();
			}
            updateCorpUserPswd(cifNo, userId, encryptedLoginPswd, encryptedTransPswd, printBatchId, printUser);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Error get PrintData", e);
		}
	}
	
	private boolean checkIfGenTransPwsd(String cifNo, String userId) throws Exception{
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
		String sql1 = "select user_id from corp_user where corp_id=? and user_id=? and role_id=?";
		List list1 = dao.query(sql1, new Object[]{cifNo, userId, Constants.ROLE_APPROVER});
		
		String sql2 = "select t.authentication_mode from corporation t where t.corp_id=? and t.authentication_mode=?";
		List list2 = dao.query(sql2, new Object[]{cifNo, Constants.AUTHENTICATION_SECURITY_CODE});
		
		if(list1!=null && list1.size()>0 && list2!=null && list2.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	private void updateCorpUserPswd(String corpId, String userId, String loginPassword, String transPassword, String printId, String printUser) throws Exception{
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");

		String sql1 = "update corp_print_info t set t.status = '1' where t.print_id = ?";
		dao.update(sql1, new Object[]{printId});
		
		String sql2 = "update corp_user t set t.user_password=?, t.security_code=?, t.last_update_time=sysdate where t.corp_id = ? and t.user_id = ?";		
		dao.update(sql2, new Object[]{loginPassword, transPassword, corpId, userId});
		
		String sql3 = "insert into corp_print_log(print_log_id,print_id,print_user,print_time) values(seq_print_log.nextval,?,?,sysdate)";
		dao.update(sql3, new Object[]{printId, printUser});
		
		String seqNo = CibIdGenerator.getIdForOperation("");
		java.util.Date curDate = new java.util.Date();
		String validityStr = Utils.null2EmptyWithTrim(Config.getProperty("pinMailer.active.period")).toUpperCase();
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		
		if(validityStr.indexOf("M") != -1){
    		validityStr = (validityStr.substring(0, validityStr.indexOf("M"))).trim();
    		cal.add(Calendar.MONTH, Integer.parseInt(validityStr));
    	}else if(validityStr.indexOf("D") != -1){
    		validityStr = (validityStr.substring(0, validityStr.indexOf("D"))).trim();
    		cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(validityStr));
    	}else if(validityStr.indexOf("H") != -1){
    		validityStr = (validityStr.substring(0, validityStr.indexOf("H"))).trim();
    		cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(validityStr));
    	}	
		cal.add(Calendar.DAY_OF_YEAR, 1);
        java.sql.Timestamp expiryDate = new java.sql.Timestamp(cal.getTimeInMillis());
        String sql4 = "insert into pin_mailer(seq_no,user_id,password,created,confirmed,expiry_date,corp_id) "
        	        + "values(?, ?, ?, sysdate, sysdate, ?, ?)";
        dao.update(sql4, new Object[]{seqNo, userId, loginPassword, expiryDate, corpId});
	}

	private void getImg(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			javax.servlet.ServletContext servletContext = this
					.getServletContext();

			String imgId = req.getParameter("Img");
			Log.info("Print client calling -> getImg: Img=" + imgId);
			/* 从服务器配置文件读取相关信息. */
			String filePath = "";
			File file = new File(filePath);
			resp.setContentType("application/octet-stream");
			BufferedInputStream bin = null;
			BufferedOutputStream bout = null;
			byte[] buffer = null;
			int readCount = -1;
			bin = new BufferedInputStream(new FileInputStream(filePath));
			bout = new BufferedOutputStream(resp.getOutputStream());
			buffer = new byte[BUFFER_SIZE];
			while ((readCount = bin.read(buffer)) > 0) {
				bout.write(buffer, 0, readCount);
			}
			buffer = null;
			bout.flush();
			bout.close();
			bout = null;
			bin.close();
			bin = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getRptTemplate(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String tempName = req.getParameter("Template");
			tempName = PRINT_TEMPLATE_PATH + "/" + tempName;
			String version = Utils.null2Empty(req.getParameter("Version"));
			Log.info("Print client calling -> getRptTemplate: Template="
					+ tempName + ", version=" + version);

			resp.setContentType("application/octet-stream");
			BufferedInputStream bin = null;
			BufferedOutputStream bout = null;
			byte[] buffer = null;
			int readCount = -1;
			bin = new BufferedInputStream(
					Config.getConfigStream(tempName));
			bout = new BufferedOutputStream(resp.getOutputStream());
			buffer = new byte[BUFFER_SIZE];
			while ((readCount = bin.read(buffer)) > 0) {
				bout.write(buffer, 0, readCount);
			}
			buffer = null;
			bout.flush();
			bout.close();
			bout = null;
			bin.close();
			bin = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setPrintResult(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String codeId = req.getParameter("ID");
	}

	private XMLElement getRecXMLElement(Map data) {
		XMLElement rec = new XMLElement("rec");

		Set set = data.entrySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> me = (Map.Entry<String, String>) it
					.next();
			XMLElement item = new XMLElement("i");
			item.setAttribute("name", me.getKey());
			item.setText(Utils.null2Empty(me.getValue()));
            
			item.setAttribute("encrypted", "Y");
			rec.addChild(item);

		}

		return rec;
	}
	
	 private static long seed = System.currentTimeMillis();
	 public String genPassword() {
			seed++;
	        Random random = new Random();
	        random.setSeed(seed);
	        StringBuffer userInfo = new StringBuffer("");
	        int length = random.nextInt(7) + 6;
	        for (int i = 0; i < length; i++) {
	            userInfo.append(random.nextInt(10));
	        }
	        return userInfo.toString();
	    }
	 //add by linrui 20190809
	 public String genFixLenPassword() {
			seed++;
	        Random random = new Random();
	        random.setSeed(seed);
	        StringBuffer password = new StringBuffer("");
	        for (int i = 0; i < 8; i++) {
	            char s = 0;
	            int j=random.nextInt(2);
	            switch (j) {
	            case 0:
	                //gen random number
	                s = (char) (random.nextInt(57) % (57 - 48 + 1) + 48);
	                break;
	            case 1:
	                //gen random capital letter
	                s = (char) (random.nextInt(90) % (90 - 65 + 1) + 65);            
	                break;
	            }
	            password.append(s);
	        }
	        return password.toString();
	 }
	 // upper lower number
	 public static final String PW_PATTERN = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";
		public static String getRandomCode() {
			int n = 8;
			String string = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
			String stringInt = "23456789";
			char[] ch = new char[n];
			for (int i = 0; i < n; i++) {
				Random random = new Random();
				if (n - 1 == i) {
	              String beforeOne = String.valueOf(ch);
	              if(!beforeOne.matches(PW_PATTERN)){
	            	  Random random2 = new Random();
	      			  int index2 = random.nextInt(stringInt.length());
	      			  ch[i] = stringInt.charAt(index2);
	              }else{
	            	  int index = random.nextInt(string.length());
	  				  ch[i] = string.charAt(index); 
	              }
				}else{
					int index = random.nextInt(string.length());
					ch[i] = string.charAt(index);
				}
			}
			String result = String.valueOf(ch);
			return result;
		}
	 

}
