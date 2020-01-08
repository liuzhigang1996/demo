package app.cib.core;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import app.cib.bo.flow.FlowProcess;
import app.cib.bo.sys.CorpUser;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.CorpUserService;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.StrTokenizer;

public class ReassignUserTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8172558948591543135L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		/*
		 * 2006-09-20 modified, remove mail notification checkbox
		 */

		try {
			// 获得输出
			JspWriter out = pageContext.getOut();
			// 从session获得语言种类
			HttpSession session = pageContext.getSession();
			Locale locale = (Locale) session.getAttribute("Locale$Of$Neturbo");
			NTBUser user = ((NTBUser) session
					.getAttribute("UserObject$Of$Neturbo"));
			if (locale == null) {
				if (user != null) {
					locale = user.getLanguage();
				}
			}
			if (locale == null) {
				locale = Config.getDefaultLocale();
			}

			String confirmSelectApprover = null;
			String confirmApproverExclusive = null;
			//add by hjs 20071019
			String notAFinancialController = null;

			RBFactory rbList = RBFactory.getInstance(
					"app.cib.resource.txn.assign_user", locale.toString());

			if (null != rbList) {
				confirmSelectApprover = rbList.getString(
						"confirm_select_approver", "Can't specify same person as the multiple layer approvers");
				confirmApproverExclusive = rbList.getString(
						"confirm_approver_exclusive",
						"Can't specify same person as the multiple layer approvers");
			} else {
				//mod by linrui for mul-language 20171123
				confirmSelectApprover = rbList.getString("must_specify_approver", "Can't specify same person as the multiple layer approvers");
				confirmApproverExclusive = rbList.getString("must_spe_app_exclusively","Can't specify same person as the multiple layer approvers");
			}
			//add by hjs 20071019
			notAFinancialController = rbList.getString("at_least_select_fir_approver","At least select 1 First Approver!");//end

			CorpUser loginedUser = (CorpUser) session
					.getAttribute("UserObject$Of$Neturbo");
			//add by hjs 20071019
			String allowFinancialController = loginedUser.getCorporation().getAllowFinancialController();

			String transNoToChange = pageContext.getRequest().getParameter(
					"transNoToChange");
			String txnTypeToChange = pageContext.getRequest().getParameter(
					"txnTypeToChange");

			StringBuffer outputBuffer = new StringBuffer();

			if (null == transNoToChange || null == txnTypeToChange) {
				outputBuffer = new StringBuffer("");
				outputBuffer.append("<script language='javascript'>");
				outputBuffer.append("function checkAssignedUser(){");
				outputBuffer.append("return true;}");
				outputBuffer.append("</script>");

			} else {
				FlowEngineService flowEngineService = (FlowEngineService) Config
						.getAppContext().getBean("FlowEngineService");
				FlowProcess flowProcess = flowEngineService.viewFlowProcess(
						txnTypeToChange, transNoToChange);

				if (null == flowProcess
						|| null == flowProcess.getProcId()
						|| FlowEngineService.APPROVE_TYPE_UNASSIGNED
								.equals(flowProcess.getApproveType())) {
					outputBuffer = new StringBuffer("");
					outputBuffer.append("<script language='javascript'>");
					outputBuffer.append("function checkAssignedUser(){");
					outputBuffer.append("return true;}");
					outputBuffer.append("</script>");
				} else {
					String approveRule = flowProcess.getApproveRule();
					String processStatus = flowEngineService
							.getProcessStatus(flowProcess.getProcId());
					String flowApprovers = flowProcess.getApprovers();

					String[] assignedApprover = pageContext.getRequest()
							.getParameterValues("assignedApprover");

					// String[] chkbxMail =
					// pageContext.getRequest().getParameterValues("chkbxMail");

					boolean hasOldAssigned = false;
					// boolean hasOldMail = false;

					if (null != assignedApprover && assignedApprover.length > 0) {
						hasOldAssigned = true;
					}

					// if (null != chkbxMail && chkbxMail.length > 0) {
					// hasOldMail = true;
					// }

					List ruleItems = flowEngineService
							.extractRuleStr(approveRule);
					HashMap ruleItem = null;
					StrTokenizer approverTokens = new StrTokenizer(
							flowApprovers, ";");
					StrTokenizer statusTokens = new StrTokenizer(processStatus,
							";");

					int total = 0;
					// int mailIndx = 0;
					int ruleCount = 0;
					String curLevel = null;
					String curFlowApprover = null;
					String curProcessStatus = null;
					boolean lockFlag = false;

					outputBuffer
							.append("<table border='0' cellspacing='0' cellpadding='3'>");
					outputBuffer.append("<tr class='groupconfirm'>");
					outputBuffer
					.append("<td>");//Specify Approver(s)</td>");//mod by linrui for mul-language 20171122 begin
			outputBuffer.append(rbList.getString("specify_approver"));						
			outputBuffer.append("</td>");		//end				
					outputBuffer.append("<td><table>");

					CorpUserService corpUserService = (CorpUserService) Config
							.getAppContext().getBean("corpUserService");
					List approvers = null;
					CorpUser approver = null;

					for (int i = 0; i < ruleItems.size(); i++) {
						ruleItem = (HashMap) ruleItems.get(i);
						curLevel = (String) ruleItem.get("Level");
						ruleCount = Integer.parseInt((String) ruleItem
								.get("Count"));

						for (int j = 0; j < ruleCount; j++) {
							curFlowApprover = approverTokens.nextToken();
							curProcessStatus = statusTokens.nextToken();

							if (FlowEngineService.WORK_STATUS_FINISH
									.equals(curProcessStatus)
									|| FlowEngineService.WORK_STATUS_CANCEL
											.equals(curProcessStatus)) {
								lockFlag = true;
							} else {
								lockFlag = false;
							}

							outputBuffer.append("<tr><td> ");//Level mod by linrui for mul-language 20171122 
							outputBuffer.append(rbList.getString("level"));
							outputBuffer.append(curLevel);
							outputBuffer
									.append("<select name='assignedApprover'");
							if ("N".equals(selectFlag) || lockFlag) {
								outputBuffer.append(" disabled='disabled'");
							}

							outputBuffer.append(">");
							approvers = corpUserService.listUserByLevel(
									loginedUser.getCorpId(), curLevel);

							if (null != approvers && approvers.size() > 0) {
								for (int k = 0; k < approvers.size(); k++) {
									approver = (CorpUser) approvers.get(k);
									outputBuffer.append("<option value='");
									outputBuffer.append(approver.getUserId());
									outputBuffer.append("'");

									if (hasOldAssigned) {
										if (approver.getUserId().equals(
												assignedApprover[total])) {
											outputBuffer.append(" selected");
										}
									} else {
										if (approver.getUserId().equals(
												curFlowApprover)) {
											outputBuffer.append(" selected");
										}
									}
									outputBuffer.append(">");
									outputBuffer
											.append(approver.getAuthLevel());
									outputBuffer.append(" - ");
									outputBuffer.append(approver.getUserName());
									//add by hjs 20071017
									if("Y".equals(allowFinancialController) &&
											"1".equals(approver.getFinancialControllerFlag())){
										outputBuffer.append(" - ");
										outputBuffer.append(rbList.getString("first_approver"));//mod by linrui for mul-language 20171122
									}
									outputBuffer.append("</option>");
								}
							}

							/*
							 * outputBuffer.append("</select>(Notification
							 * Mail"); outputBuffer .append("<input
							 * type='checkbox' name='chkbxMail' value='");
							 * outputBuffer.append(total);
							 * outputBuffer.append("'"); if (hasOldMail) { while
							 * (mailIndx < chkbxMail.length && Integer
							 * .parseInt(chkbxMail[mailIndx]) < total) {
							 * mailIndx += 1; }
							 * 
							 * if (Integer.parseInt(chkbxMail[mailIndx]) ==
							 * total) { outputBuffer.append("
							 * checked='checked'"); } } else {
							 * outputBuffer.append(" checked='checked'"); }
							 * 
							 * 
							 * if ("N".equals(selectFlag)) {
							 * outputBuffer.append(" disabled='disabled'"); }
							 * outputBuffer.append(">)");
							 */

							outputBuffer.append("</select>");
							outputBuffer
									.append("<input type='hidden' name='chkbxMail' value='");
							outputBuffer.append(total);
							outputBuffer.append("'>");
							outputBuffer.append("</td></tr>");

							total += 1;
						}

					}

					outputBuffer.append("</table></td>");
					outputBuffer.append("</tr>");
					outputBuffer.append("</table>");

					outputBuffer.append("<input type='hidden' name='assignedUser' value=''>");
					outputBuffer.append("<input type='hidden' name='mailUser' value=''>");
					outputBuffer.append("<script language='javascript'>");
					outputBuffer.append("function checkAssignedUser(){");
					outputBuffer.append("var assignedUsers = '';");
					outputBuffer.append("var mailUsers = '';");
					outputBuffer.append("var sameFlag = false;");
					outputBuffer.append("var noApproverFlag = false;");
					//add by hjs 20071019
					outputBuffer.append("var noFinancialControllerFlag = true;");
					outputBuffer.append("var allowFinancialController = '"+ allowFinancialController +"';");

					outputBuffer.append("if(document.form1.chkbxMail){");
					outputBuffer.append("if(document.form1.chkbxMail.length){");
					outputBuffer.append("var i = 0;");
					outputBuffer.append("var j = 0;");

					outputBuffer.append("for(i= 0;i < document.form1.chkbxMail.length; i++){");

					outputBuffer.append("if(document.form1.assignedApprover[i].options.length < 1){");
					outputBuffer.append("noApproverFlag = true;");
					outputBuffer.append("break;");
					outputBuffer.append("}");
					
					//modify by hjs 20071024
					outputBuffer.append("var appr1 = document.form1.assignedApprover[i];");
					outputBuffer.append("if(noFinancialControllerFlag && appr1[appr1.selectedIndex].text.indexOf(' - First Approver')>-1){");
					outputBuffer.append("noFinancialControllerFlag = false;");
					outputBuffer.append("}");

					outputBuffer.append("assignedUsers = assignedUsers + document.form1.assignedApprover[i].value + ';';");
					// outputBuffer.append("if(document.form1.chkbxMail[i].checked){");
					// outputBuffer.append("mailUsers = mailUsers +
					// document.form1.assignedApprover[i].value + ';';");
					// outputBuffer.append("}");
					outputBuffer.append("if(i > 0){");
					outputBuffer.append("for(j=0; j< i; j++){");
					outputBuffer.append("if(document.form1.assignedApprover[j].value == document.form1.assignedApprover[i].value){");
					outputBuffer.append("sameFlag = true;");
					outputBuffer.append("break;");
					outputBuffer.append("}");
					outputBuffer.append("}");

					outputBuffer.append("if(sameFlag){");
					outputBuffer.append("break;");
					outputBuffer.append("}");
					outputBuffer.append("}");
					outputBuffer.append("}");
					outputBuffer.append("}");
					outputBuffer.append("else{");

					outputBuffer.append("if(document.form1.assignedApprover.options.length < 1){");
					outputBuffer.append("noApproverFlag = true;");
					outputBuffer.append("}");
					
					//add by hjs 20071019
					outputBuffer.append("var appr2 = document.form1.assignedApprover;");
					outputBuffer.append("if(allowFinancialController=='Y' && appr2[appr2.selectedIndex].text.indexOf(' - First Approver')<0){");
					outputBuffer.append("alert('" + notAFinancialController + "');");
					outputBuffer.append("return false;");
					//modify by hjs 20071024
					outputBuffer.append("} else {");
					outputBuffer.append("noFinancialControllerFlag = false;");
					outputBuffer.append("}");

					outputBuffer.append("assignedUsers = document.form1.assignedApprover.value;");
					// outputBuffer.append("if(document.form1.chkbxMail.checked){");
					// outputBuffer.append("mailUsers = assignedUsers;");
					// outputBuffer.append("}");
					outputBuffer.append("}");

					outputBuffer.append("if(noApproverFlag){");
					outputBuffer.append("alert('");
					outputBuffer.append(confirmSelectApprover);
					outputBuffer.append("');");
					outputBuffer.append("return false;");
					outputBuffer.append("} else ");

					outputBuffer.append("if(sameFlag){");
					outputBuffer.append("alert('");
					outputBuffer.append(confirmApproverExclusive);
					outputBuffer.append("');");
					outputBuffer.append("return false;");
					outputBuffer.append("}");
					//add by hjs 20071024
					outputBuffer.append("if(allowFinancialController=='Y' && noFinancialControllerFlag){");
					outputBuffer.append("alert('");
					outputBuffer.append(notAFinancialController);
					outputBuffer.append("');");
					outputBuffer.append("return false;");
					outputBuffer.append("}");
					//
					outputBuffer.append("else{");
					outputBuffer.append("document.form1.assignedUser.value = assignedUsers;");
					outputBuffer.append("document.form1.mailUser.value = mailUsers;");
					if ("Y".equals(selectFlag)) {

						outputBuffer.append("if(document.form1.chkbxMail){");
						outputBuffer.append("var mailIndx = 0;");
						outputBuffer
								.append("if(document.form1.chkbxMail.length){");
						outputBuffer
								.append("for( mailIndx= 0; mailIndx< document.form1.chkbxMail.length; mailIndx++){");
						outputBuffer
								.append("document.form1.assignedApprover[mailIndx].disabled = false;");
						outputBuffer.append("}");
						outputBuffer.append("}");
						outputBuffer.append("else{");
						outputBuffer
								.append("document.form1.assignedApprover.disabled = false;");
						outputBuffer.append("}");
						outputBuffer.append("}");

					}

					outputBuffer.append("return true;");
					outputBuffer.append("}");
					outputBuffer.append("}");
					outputBuffer.append("}");
					outputBuffer.append("</script>");
				}
			}

			out.write(outputBuffer.toString());
		} catch (Exception e) {
			Log.error("Custom Tag Process error (ReassignUser)", e);
		}

		return (SKIP_BODY);
	}

	private String selectFlag = "N";

	public String getSelectFlag() {
		return selectFlag;
	}

	public void setSelectFlag(String selectFlag) {
		this.selectFlag = selectFlag;
	}

}
