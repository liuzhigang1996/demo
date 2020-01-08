package app.cib.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import app.cib.bo.sys.CorpUser;
import app.cib.core.CibTransClient;
import app.cib.service.srv.EStatementService;

import com.neturbo.base.action.ActionForwards;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DBRCFactory;

public class DownloadStatementServlet extends HttpServlet {

	private CorpUser user;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3239314174067012716L;

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//add by linrui 20190907 for security
		HttpSession session = req.getSession();
		NTBUser user = ((NTBUser) session.getAttribute("UserObject$Of$Neturbo"));
		if(user == null){
			Log.error("User is not avaliable");
			session.setAttribute("ErrorData$Of$Neturbo", new NTBException("err.sys.ZipFileError"));
	        ActionForwards forwards = Config.getGlobalForwards();
	        resp.sendRedirect(Config.getProperty("AppContextRoot") + forwards.findForward("ErrorReporter").getPath());
		}
		EStatementService eStatementService = (EStatementService) Config.getAppContext().getBean("EStatementService");
		String corpId =  ((CorpUser)user).getCorpId();
		//end
		String accNo = req.getParameter("accNo");
		String key = req.getParameter("key");
		
		String fileName = StatementToken.checkToken(key.substring(2));
		//add by linrui 20190907 for security
		if(!eStatementService.isBelongCorpId(corpId.substring(1), fileName)){
			Log.error("Illegal request estatement");
			session.setAttribute("ErrorData$Of$Neturbo", new NTBException("err.sys.StatementFileNameError"));
            ActionForwards forwards = Config.getGlobalForwards();
            resp.sendRedirect(Config.getProperty("AppContextRoot") + forwards.findForward("ErrorReporter").getPath());
            return;
		}
		//end
		String filePath = "";
		String type = key.split("-")[0];
		if (type.equals("S")) {
			filePath = Config.getProperty("StatementDownDir")+"/";
			 
		} else if (type.equals("O")) {
			filePath = Config.getProperty("OutwardRemAdviceDir")+"/";
			
		} else if (type.equals("P")) {
			filePath = Config.getProperty("PDFDownloadDir")+"/";
			
		} else  {
			session.setAttribute("ErrorData$Of$Neturbo", new NTBException("err.sys.StatementFileNameError"));
	            ActionForwards forwards = Config.getGlobalForwards();
	            resp.sendRedirect(Config.getProperty("AppContextRoot") + forwards.findForward("ErrorReporter").getPath());
	            return;
		}
		/* ********************* */
		
		File pdfFile = new File(filePath+fileName);
		if (fileName != null&&pdfFile.exists()) {
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-Disposition", "attachment;" + " filename="
					+ fileName);
			BufferedOutputStream bout = null;
			BufferedInputStream bin = null;
			byte[] buffer = new byte[4096];
			int readCount = -1;
			bin = new BufferedInputStream(new FileInputStream(pdfFile));
			bout = new BufferedOutputStream(resp.getOutputStream());
			while ((readCount = bin.read(buffer)) > 0) {
				bout.write(buffer, 0, readCount);
			}
			buffer = null;

			bout.flush();

			bout.close();
			bout = null;

			bin.close();
			bin = null;
			
			// Jet added for e-advice statistics report
			if (type.equals("O")){
//		        user = ((CorpUser) req.getSession().getAttribute("UserObject$Of$Neturbo"));

				Map uploadMap = new HashMap();
				uploadMap.put("SEQ_NO", req.getSession().getId());
		        uploadMap.put("ACCESS_DATE", CibTransClient.getCurrentDate());
		        uploadMap.put("ACCESS_TIME ", CibTransClient.getCurrentTime());
		        uploadMap.put("USER_ID", user.getUserId());
		        uploadMap.put("CORPORATION_ID", ((CorpUser)user).getCorpId());	        
		        UploadReporter.write("RP_EADVICE", uploadMap);				
			}
		}else{
			Log.error("Illegal request estatement");
			session.setAttribute("ErrorData$Of$Neturbo", new NTBException("err.sys.ZipFileError"));
	        ActionForwards forwards = Config.getGlobalForwards();
	        resp.sendRedirect(Config.getProperty("AppContextRoot") + forwards.findForward("ErrorReporter").getPath());
	        return;
		}
	}
}
