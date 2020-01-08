/**
 * @author hjs
 * 2007-4-23
 */
package app.cib.util;

import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import app.cib.bo.sys.CorpUser;
import app.cib.service.srv.EStatementService;

import com.neturbo.base.action.ActionForwards;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.utils.ZipUtils;

/**
 * @author hjs 2007-4-23
 */
public class DownloadZipServlet extends HttpServlet {
	private static final long serialVersionUID = -8532099249318116848L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			//add by linrui 20190907 for security
			HttpSession session = request.getSession();
			NTBUser user = ((NTBUser) session.getAttribute("UserObject$Of$Neturbo"));
			if(user == null){
				Log.error("User is not avaliable");
				session.setAttribute("ErrorData$Of$Neturbo", new NTBException("err.sys.ZipFileError"));
		        ActionForwards forwards = Config.getGlobalForwards();
		        response.sendRedirect(Config.getProperty("AppContextRoot") + forwards.findForward("ErrorReporter").getPath());
			}
			EStatementService eStatementService = (EStatementService) Config.getAppContext().getBean("EStatementService");
			String corpId =  ((CorpUser)user).getCorpId();
			//end
			String[] keys = request.getParameterValues("key");
			String[] files = null;;
			List fileList = new ArrayList();
			for(int i=0; i<keys.length; i++){
				String fileName = StatementToken.checkToken(keys[i].substring(2));
				if(Utils.null2EmptyWithTrim(fileName).equals("")){
					continue;
				}
				//add by linrui 20190907 for security
				if(!eStatementService.isBelongCorpId(corpId.substring(1), fileName)){
					Log.error("Illegal request estatement");
					session.setAttribute("ErrorData$Of$Neturbo", new NTBException("err.sys.ZipFileError"));
			        ActionForwards forwards = Config.getGlobalForwards();
			        response.sendRedirect(Config.getProperty("AppContextRoot") + forwards.findForward("ErrorReporter").getPath());
			        return;
				}
				//end
				String filePath = "";
				String type = keys[i].split("-")[0];
				if (type.equals("S")) {
					filePath = Config.getProperty("StatementDownDir")+"/";
					 
				} else if (type.equals("O")) {
					filePath = Config.getProperty("OutwardRemAdviceDir")+"/";
					
				} else if (type.equals("P")) {
					filePath = Config.getProperty("PDFDownloadDir")+"/";
					
				} else  {
					session.setAttribute("ErrorData$Of$Neturbo", new NTBException("err.sys.StatementFileNameError"));
			        ActionForwards forwards = Config.getGlobalForwards();
			        response.sendRedirect(Config.getProperty("AppContextRoot") + forwards.findForward("ErrorReporter").getPath());
			        return;
				}
				fileList.add(filePath + fileName);
			}
			
			if(fileList.size()==0){
				Log.warn("ZIP file must have at least one entry");
				session.setAttribute("WarningData$Of$Neturbo", new NTBException("err.sys.NoFileToZipError"));
		        ActionForwards forwards = Config.getGlobalForwards();
		        response.sendRedirect(Config.getProperty("AppContextRoot") + forwards.findForward("ErrorReporter").getPath());
		        return;
			}
			
			files = new String[fileList.size()];
			for(int i=0; i<fileList.size(); i++){
				files[i] = (String) fileList.get(i);
			}
			
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;" + " filename=" + getZipFileName());
			ByteArrayOutputStream out = (ByteArrayOutputStream) ZipUtils.files2Zip(files);
			out.writeTo(response.getOutputStream());
			
			out.flush();
			out.close();
			out = null;
			
		} catch (Exception e) {
			Log.error("Zip file error", e);
			HttpSession session = request.getSession();
			session.setAttribute("ErrorData$Of$Neturbo", new NTBException("err.sys.ZipFileError"));
	        ActionForwards forwards = Config.getGlobalForwards();
	        response.sendRedirect(Config.getProperty("AppContextRoot") + forwards.findForward("ErrorReporter").getPath());
	        return;
		}
		
	}
	
	private String getZipFileName() {
		return "DocumentArchive_" + DateTime.formatDate(new Date(), "yyyyMMdd") + ".zip";
	}

}
