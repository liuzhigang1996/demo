package app.cib.core;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import app.cib.bo.sys.ApproveRule;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.sys.TxnSubtype;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.TxnSubtypeService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.sys.CorpUserService;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.RBFactory;

/**
 * @author panwen
 *
 */
public class AssignUserTag extends TagSupport {

	private static final String[] authorizeLevel = new String[] { "A", "B",
			"C", "D", "E", "F", "G" };

	/**
	 *
	 */
	private static final long serialVersionUID = 7249315724961074328L;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {

		try {
			// ������
			JspWriter out = pageContext.getOut();
			// ��session�����������
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
			String firstApproverLable = rbList.getString("first_approver");
			if (null != rbList) {
				confirmSelectApprover = rbList.getString(
						"confirm_select_approver", "Can\'t specify same person as the multiple layer approvers");
				confirmApproverExclusive = rbList.getString(
						"confirm_approver_exclusive",
						"Can\'t specify same person as the multiple layer approvers");
			} else {
				//mod by linrui for mul-language 20171123
				confirmSelectApprover = rbList.getString("must_specify_approver", "Can\'t specify same person as the multiple layer approvers");
				confirmApproverExclusive = rbList.getString("must_spe_app_exclusively","Can\'t specify same person as the multiple layer approvers");
			}
			//add by hjs 20071019
			notAFinancialController = rbList.getString("at_least_select_fir_approver","At least select 1 First Approver!");//end

			CorpUser loginedUser = (CorpUser) session
					.getAttribute("UserObject$Of$Neturbo");
			String corpType = loginedUser.getCorporation().getCorpType();
			//add by hjs 20071019
			String allowFinancialController = loginedUser.getCorporation().getAllowFinancialController();

			HashMap resultData = (HashMap) session
					.getAttribute("ResultData$Of$Neturbo");

			String currencyField = (String) resultData.get("currencyField");
			String currency = (String) resultData.get(currencyField);

			String amountField = (String) resultData.get("amountField");
			Object amount = resultData.get(amountField);

			String amountMopEqField = (String) resultData
					.get("amountMopEqField");
			Object amountMopEq = resultData.get(amountMopEqField);

			String txnTypeField = (String) resultData.get("txnTypeField");
			String txnType = (String) resultData.get(txnTypeField);

			if (null == currencyField || null == currency
					|| null == amountField || null == amount
					|| null == amountMopEqField || null == amountMopEq
					|| null == txnTypeField || null == txnType) {
				StringBuffer outputBuffer = new StringBuffer("");
				outputBuffer.append("<script language='javascript'>");
				outputBuffer.append("function checkAssignedUser(){");
				outputBuffer.append("return true;}");
				outputBuffer.append("</script>");
				out.write(outputBuffer.toString());
			} else {
				if ("N".equalsIgnoreCase(selectFlag)) {
					StringBuffer outputBuffer = new StringBuffer();

					if ("Y".equalsIgnoreCase(loginedUser.getCorporation()
							.getAllowApproverSelection())) {
						try {
							String[] assignedApprover = pageContext
									.getRequest().getParameterValues(
											"assignedApprover");

							String[] chkbxMail = pageContext.getRequest()
									.getParameterValues("chkbxMail");

							boolean hasOldAssigned = false;
							boolean hasOldMail = false;

							if (null != assignedApprover) {
								hasOldAssigned = true;
							}

							if (null != chkbxMail && chkbxMail.length > 0) {
								hasOldMail = true;
							}

							CorpUserService corpUserService = (CorpUserService) Config
									.getAppContext().getBean("corpUserService");

							ApproveRuleService approveRuleService = (ApproveRuleService) Config
									.getAppContext().getBean(
											"ApproveRuleService");

							TxnSubtypeService txnSubtypeService = (TxnSubtypeService) Config
									.getAppContext().getBean(
											"TxnSubtypeService");

							TxnSubtype txnSubtype = txnSubtypeService
									.load(txnType);
							if (null != txnSubtype
									&& null != txnSubtype.getTxnType()) {
								txnType = txnSubtype.getTxnType();
							}

							ApproveRule approveRule = approveRuleService
									.getApproveRule(txnType, loginedUser
											.getCorpId(), currency, Double
											.parseDouble(amount.toString()),
											Double.parseDouble(amountMopEq
													.toString()));

							if (null != approveRule) {
								List approvers = null;
								CorpUser approver = null;
								outputBuffer
										.append("<table border='0' cellspacing='0' cellpadding='3'>");
								outputBuffer
										.append("<tr class='groupconfirm'>");
								outputBuffer
										.append("<td>");//Specify Approver(s)</td>");//mod by linrui for mul-language 20171122 begin
								outputBuffer.append(rbList.getString("specify_approver"));						
								outputBuffer.append("</td>");		//end				
								outputBuffer.append("<td><table>");

								if (FlowEngineService.RULE_TYPE_SINGLE
										.equals(approveRule.getRuleType())) {
									outputBuffer.append("<tr><td> ");//Level mod by linrui for mul-language 20171122 
									outputBuffer.append(rbList.getString("level"));
									outputBuffer.append(approveRule
											.getSingleLevel());
									outputBuffer
											.append("&nbsp;&nbsp;<select name='assignedApprover' disabled='disabled'>");
									approvers = corpUserService
											.listUserByLevel(loginedUser
													.getCorpId(), approveRule
													.getSingleLevel());
									outputBuffer
									.append("<option value = ''>"+RBFactory.getInstance("app.cib.resource.txn.assign_user",locale+"").getString("select_approver")+"</option> ");
									if (null != approvers
											&& approvers.size() > 0) {
										for (int i = 0; i < approvers.size(); i++) {
											approver = (CorpUser) approvers
													.get(i);
											if (!loginedUser
													.getUserId()
													.equals(
															approver
																	.getUserId()) || (Constants.CORP_TYPE_MIDDLE.equals(corpType) || Constants.CORP_TYPE_SMALL.equals(corpType))) {
												outputBuffer
														.append("<option value='");
												outputBuffer.append(approver
														.getUserId());
												outputBuffer.append("'");

												if (hasOldAssigned) {
													if (approver
															.getUserId()
															.equals(
																	assignedApprover[0])) {
														outputBuffer
																.append(" selected");
													}
												}
												outputBuffer.append(">");
												outputBuffer.append(approver
														.getAuthLevel());
												outputBuffer.append(" - ");
												outputBuffer.append(approver
														.getUserName());
												//add by hjs 20071017
												if("Y".equals(allowFinancialController) &&
														"1".equals(approver.getFinancialControllerFlag())){
													outputBuffer.append(" - ");
													outputBuffer.append(rbList.getString("first_approver"));//mod by linrui for mul-language 20171122
												}
												outputBuffer
														.append("</option>");
											}
										}
									}

									outputBuffer.append("</select>&nbsp;&nbsp; ");//Notification Mail &nbsp; ");mod by linrui for mul-language 20171122 begin
									/*outputBuffer.append("( ");//add by linrui 20190515
									outputBuffer.append(rbList.getString("notification_mail"));
									outputBuffer.append("&nbsp; ");*/
									outputBuffer.append("<input type='checkbox' name='chkbxMail' value='0' style='display:none'");//disabled='disbled'
									if (hasOldMail || !hasOldAssigned) {
										outputBuffer
												.append(" checked ='checked'");
									}
									outputBuffer.append(">&nbsp;");
//									outputBuffer.append(")");//add by linrui 20190515
									outputBuffer.append("</td></tr>");

								} else if (FlowEngineService.RULE_TYPE_MULTI
										.equals(approveRule.getRuleType())) {
									Integer countInt = null;
									int count = 0;
									int mailIndx = 0;
									int total = 0;

									for (int levelIndex = 0; levelIndex < authorizeLevel.length; levelIndex++) {
										count = 0;

										switch (levelIndex) {
										case 0:
											countInt = approveRule.getLevelA();
											break;
										case 1:
											countInt = approveRule.getLevelB();
											break;
										case 2:
											countInt = approveRule.getLevelC();
											break;
										case 3:
											countInt = approveRule.getLevelD();
											break;
										case 4:
											countInt = approveRule.getLevelE();
											break;
										case 5:
											countInt = approveRule.getLevelF();
											break;
										case 6:
											countInt = approveRule.getLevelG();
											break;
										}

										if (null != countInt) {
											count = countInt.intValue();
										}

										if (count > 0) {
											for (int indx = 0; indx < count; indx++) {
												outputBuffer.append("<tr><td> ");//Level mod by linrui for mul-language 20171122 
												outputBuffer.append(rbList.getString("level"));
												outputBuffer
														.append(authorizeLevel[levelIndex]);
												outputBuffer
														.append("&nbsp;&nbsp;<select name='assignedApprover' disabled='disabled'>");
												approvers = corpUserService
														.listUserByLevel(
																loginedUser
																		.getCorpId(),
																authorizeLevel[levelIndex]);
												outputBuffer
												.append("<option value = ''>"+RBFactory.getInstance("app.cib.resource.txn.assign_user",locale+"").getString("select_approver")+"</option> ");
												if (null != approvers
														&& approvers.size() > 0) {
													for (int i = 0; i < approvers
															.size(); i++) {
														approver = (CorpUser) approvers
																.get(i);

														if (!loginedUser
																.getUserId()
																.equals(
																		approver
																				.getUserId()) || (Constants.CORP_TYPE_MIDDLE.equals(corpType) || Constants.CORP_TYPE_SMALL.equals(corpType))) {

															outputBuffer
																	.append("<option value='");
															outputBuffer
																	.append(approver
																			.getUserId());
															outputBuffer
																	.append("'");

															if (hasOldAssigned) {
																if (approver
																		.getUserId()
																		.equals(
																				assignedApprover[total])) {
																	outputBuffer
																			.append(" selected");
																}
															}
															outputBuffer
																	.append(">");
															outputBuffer
																	.append(approver
																			.getAuthLevel());
															outputBuffer
																	.append(" - ");
															outputBuffer
																	.append(approver
																			.getUserName());
															//add by hjs 20071017
															if("Y".equals(allowFinancialController) &&
																	"1".equals(approver.getFinancialControllerFlag())){
																outputBuffer.append(" - ");
																outputBuffer.append(rbList.getString("first_approver"));//mod by linrui for mul-language 20171122
															}
															outputBuffer
																	.append("</option>");
														}
													}
												}

												outputBuffer.append("</select>&nbsp;&nbsp;");//Notification Mail &nbsp; ");mod by linrui for mul-language 20171122 begin
												/*outputBuffer.append("( ");//mod by linrui 20190515
										outputBuffer.append(rbList.getString("notification_mail"));
										outputBuffer.append("&nbsp; ");//end
*/												outputBuffer
														.append("<input type='checkbox' name='chkbxMail' value='");
												outputBuffer.append(total);
												outputBuffer.append("'");
												if (hasOldMail) {
													while (mailIndx < chkbxMail.length
															&& Integer
																	.parseInt(chkbxMail[mailIndx]) < total) {
														mailIndx += 1;
													}

													if (Integer
															.parseInt(chkbxMail[mailIndx]) == total) {
														outputBuffer
																.append(" checked='checked'");
													}
												} else {
													//outputBuffer
													//		.append(" checked='checked'");
												}
												total += 1;
												outputBuffer
														.append(" style='display:none'");//disabled='disbled'
												outputBuffer.append(">&nbsp;");
//												outputBuffer.append(")");//add by linrui 20190515
												outputBuffer
														.append("</td></tr>");

											}

										}

									}// end for
								}

								outputBuffer.append("</table></td>");
								outputBuffer.append("</tr>");
								outputBuffer.append("</table>");
							}

							outputBuffer
									.append("<input type='hidden' name='assignedUser' value=''>");
							outputBuffer
									.append("<input type='hidden' name='mailUser' value=''>");
							outputBuffer
									.append("<script language='javascript'>");
							outputBuffer
									.append("function checkAssignedUser(){");
							outputBuffer
							.append("var assApprover =  document.getElementsByName('assignedApprover');for(var k = 0;k < assApprover.length;k++){if(assApprover[k].value == ''){alert('"+confirmSelectApprover+"');return false;}}");
							outputBuffer.append("var assignedUsers = '';");
							outputBuffer.append("var mailUsers = '';");
							outputBuffer.append("var sameFlag = false;");
							outputBuffer.append("var noApproverFlag = false;");

							outputBuffer
									.append("if(document.form1.chkbxMail){");
							outputBuffer
									.append("if(document.form1.chkbxMail.length){");
							outputBuffer.append("var i = 0;");
							outputBuffer.append("var j = 0;");

							outputBuffer
									.append("for(i= 0;i < document.form1.chkbxMail.length; i++){");

							outputBuffer
									.append("if(document.form1.assignedApprover[i].options.length < 1){");
							outputBuffer.append("noApproverFlag = true;");
							outputBuffer.append("break;");
							outputBuffer.append("}");

							outputBuffer
									.append("assignedUsers = assignedUsers + document.form1.assignedApprover[i].value + ';';");
							outputBuffer
									.append("if(document.form1.chkbxMail[i].checked){");
							outputBuffer
									.append("mailUsers = mailUsers + document.form1.assignedApprover[i].value + ';';");
							outputBuffer.append("}");
							outputBuffer.append("if(i > 0){");
							outputBuffer.append("for(j=0; j< i; j++){");
							outputBuffer
									.append("if(document.form1.assignedApprover[j].value == document.form1.assignedApprover[i].value){");
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

							outputBuffer
									.append("if(document.form1.assignedApprover.options.length < 1){");
							outputBuffer.append("noApproverFlag = true;");
							outputBuffer.append("}");

							outputBuffer
									.append("assignedUsers = document.form1.assignedApprover.value;");
							outputBuffer
									.append("if(document.form1.chkbxMail.checked){");
							outputBuffer.append("mailUsers = assignedUsers;");
							outputBuffer.append("}");
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
							outputBuffer.append("else{");
							outputBuffer
									.append("document.form1.assignedUser.value = assignedUsers;");
							outputBuffer
									.append("document.form1.mailUser.value = mailUsers;");
							outputBuffer.append("return true;");
							outputBuffer.append("}");
							outputBuffer.append("}");
							outputBuffer.append("}");
							outputBuffer.append("</script>");

						} catch (NTBException ntbe) {
							Log.error("Custom Tag Process error (AssignUser)",
									ntbe);
							outputBuffer = new StringBuffer("");
							outputBuffer
									.append("<script language='javascript'>");
							outputBuffer
									.append("function checkAssignedUser(){");
							outputBuffer.append("return true;}");
							outputBuffer.append("</script>");

						}
					} else {
						outputBuffer.append("<script language='javascript'>");
						outputBuffer.append("function checkAssignedUser(){");
						outputBuffer.append("return true;}");
						outputBuffer.append("</script>");
					}

					out.write(outputBuffer.toString());

				} else {
					String[] assignedApprover = pageContext.getRequest()
							.getParameterValues("assignedApprover");
					String[] chkbxMail = pageContext.getRequest()
							.getParameterValues("chkbxMail");

					boolean hasOldAssigned = false;
					boolean hasOldMail = false;

					if (null != assignedApprover) {
						hasOldAssigned = true;
					}

					if (null != chkbxMail && chkbxMail.length > 0) {
						hasOldMail = true;
					}

					StringBuffer outputBuffer = new StringBuffer();

					if ("Y".equalsIgnoreCase(loginedUser.getCorporation()
							.getAllowApproverSelection())) {
						try {

							CorpUserService corpUserService = (CorpUserService) Config
									.getAppContext().getBean("corpUserService");

							ApproveRuleService approveRuleService = (ApproveRuleService) Config
									.getAppContext().getBean(
											"ApproveRuleService");

							TxnSubtypeService txnSubtypeService = (TxnSubtypeService) Config
									.getAppContext().getBean(
											"TxnSubtypeService");

							TxnSubtype txnSubtype = txnSubtypeService
									.load(txnType);
							if (null != txnSubtype
									&& null != txnSubtype.getTxnType()) {
								txnType = txnSubtype.getTxnType();
							}

							ApproveRule approveRule = approveRuleService
									.getApproveRule(txnType, loginedUser
											.getCorpId(), currency, Double
											.parseDouble(amount.toString()),
											Double.parseDouble(amountMopEq
													.toString()));

							if (null != approveRule) {
								List approvers = null;
								CorpUser approver = null;
								outputBuffer
										.append("<table border='0' cellspacing='0' cellpadding='3'>");
								outputBuffer
										.append("<tr class='groupconfirm'>");
								outputBuffer
								.append("<td>");//Specify Approver(s)</td>");//mod by linrui for mul-language 20171122 begin
						outputBuffer.append(rbList.getString("specify_approver"));						
						outputBuffer.append("</td>");		//end
								outputBuffer.append("<td><table>");

								if (FlowEngineService.RULE_TYPE_SINGLE
										.equals(approveRule.getRuleType())) {
									outputBuffer.append("<tr><td> ");//Level mod by linrui for mul-language 20171122 
									outputBuffer.append(rbList.getString("level"));
									outputBuffer.append(approveRule
											.getSingleLevel());
									outputBuffer
											.append("&nbsp;&nbsp;<select name='assignedApprover'>");
									approvers = corpUserService
											.listUserByLevel(loginedUser
													.getCorpId(), approveRule
													.getSingleLevel());
									outputBuffer
									.append("<option value = ''>"+RBFactory.getInstance("app.cib.resource.txn.assign_user",locale+"").getString("select_approver")+"</option> ");
									if (null != approvers
											&& approvers.size() > 0) {
										for (int i = 0; i < approvers.size(); i++) {
											approver = (CorpUser) approvers
													.get(i);
											if (!loginedUser
													.getUserId()
													.equals(
															approver
																	.getUserId()) || (Constants.CORP_TYPE_MIDDLE.equals(corpType) || Constants.CORP_TYPE_SMALL.equals(corpType))) {
												outputBuffer
														.append("<option value='");
												outputBuffer.append(approver
														.getUserId());
												outputBuffer.append("'");

												if (hasOldAssigned) {
													if (approver
															.getUserId()
															.equals(
																	assignedApprover[0])) {
														outputBuffer
																.append(" selected");
													}
												} 
												outputBuffer.append(">");
												outputBuffer.append(approver
														.getAuthLevel());
												outputBuffer.append(" - ");
												outputBuffer.append(approver
														.getUserName());
												//add by hjs 20071017
												if("Y".equals(allowFinancialController) &&
														"1".equals(approver.getFinancialControllerFlag())){
													outputBuffer.append(" - ");
													outputBuffer.append(rbList.getString("first_approver"));//mod by linrui for mul-language 20171122
												}
												outputBuffer
														.append("</option>");
											}
										}
									}

									outputBuffer.append("</select>&nbsp;&nbsp;");//Notification Mail &nbsp; ");mod by linrui for mul-language 20171122 begin
									/*outputBuffer.append("( ");//mod by linrui 20190515
							outputBuffer.append(rbList.getString("notification_mail"));
							outputBuffer.append("&nbsp; ");*/
									outputBuffer
											.append("<input type='checkbox' name='chkbxMail' value='0' style='display:none' ");
									if (hasOldMail || !hasOldAssigned) {
										outputBuffer
												.append(" checked ='checked'");
									}
									outputBuffer.append(">&nbsp;");
//									outputBuffer.append(")");//add by linrui 20190515
									outputBuffer.append("</td></tr>");

								} else if (FlowEngineService.RULE_TYPE_MULTI
										.equals(approveRule.getRuleType())) {
									Integer countInt = null;
									int count = 0;
									int mailIndx = 0;
									int total = 0;

									for (int levelIndex = 0; levelIndex < authorizeLevel.length; levelIndex++) {
										count = 0;

										switch (levelIndex) {
										case 0:
											countInt = approveRule.getLevelA();
											break;
										case 1:
											countInt = approveRule.getLevelB();
											break;
										case 2:
											countInt = approveRule.getLevelC();
											break;
										case 3:
											countInt = approveRule.getLevelD();
											break;
										case 4:
											countInt = approveRule.getLevelE();
											break;
										case 5:
											countInt = approveRule.getLevelF();
											break;
										case 6:
											countInt = approveRule.getLevelG();
											break;
										}

										if (null != countInt) {
											count = countInt.intValue();
										}

										if (count > 0) {
											for (int indx = 0; indx < count; indx++) {
												outputBuffer.append("<tr><td> ");//Level mod by linrui for mul-language 20171122 
												outputBuffer.append(rbList.getString("level"));
												outputBuffer
														.append(authorizeLevel[levelIndex]);
												outputBuffer
														.append("&nbsp;&nbsp;<select name='assignedApprover'");
												outputBuffer.append(">");

												approvers = corpUserService
														.listUserByLevel(
																loginedUser
																		.getCorpId(),
																authorizeLevel[levelIndex]);
												outputBuffer
												.append("<option value = ''>"+RBFactory.getInstance("app.cib.resource.txn.assign_user",locale+"").getString("select_approver")+"</option> ");
												if (null != approvers
														&& approvers.size() > 0) {
													for (int i = 0; i < approvers
															.size(); i++) {
														approver = (CorpUser) approvers
																.get(i);
														if (!loginedUser
																.getUserId()
																.equals(
																		approver
																				.getUserId()) || (Constants.CORP_TYPE_MIDDLE.equals(corpType) || Constants.CORP_TYPE_SMALL.equals(corpType))) {
															outputBuffer
																	.append("<option value='");
															outputBuffer
																	.append(approver
																			.getUserId());
															outputBuffer
																	.append("'");

															if (hasOldAssigned) {
																if (approver
																		.getUserId()
																		.equals(
																				assignedApprover[total])) {
																	outputBuffer
																			.append(" selected");
																}
															} 
															outputBuffer
																	.append(">");
															outputBuffer
																	.append(approver
																			.getAuthLevel());
															outputBuffer
																	.append(" - ");
															outputBuffer
																	.append(approver
																			.getUserName());
															//add by hjs 20071017
															if("Y".equals(allowFinancialController) &&
																	"1".equals(approver.getFinancialControllerFlag())){
																outputBuffer.append(" - ");
																outputBuffer.append(rbList.getString("first_approver"));//mod by linrui for mul-language 20171122
															}
															outputBuffer
																	.append("</option>");
														}
													}

												}

												outputBuffer.append("</select>&nbsp;&nbsp;");//Notification Mail &nbsp; ");mod by linrui for mul-language 20171122 begin
												/*outputBuffer.append("( ");//mod by linrui 20190515
										outputBuffer.append(rbList.getString("notification_mail"));
										outputBuffer.append("&nbsp; ");//end*/
												outputBuffer
														.append("<input type='checkbox' name='chkbxMail' style='display:none' value='");
												outputBuffer.append(total);
												outputBuffer.append("'");
												if (hasOldMail) {
													while (mailIndx < chkbxMail.length
															&& Integer
																	.parseInt(chkbxMail[mailIndx]) < total) {
														mailIndx += 1;
													}

													if (Integer
															.parseInt(chkbxMail[mailIndx]) == total) {
														outputBuffer
																.append(" checked='checked'");
													}
												} else {
													outputBuffer
															.append(" checked='checked'");
												}

												total += 1;
												outputBuffer.append(">&nbsp;");
//												outputBuffer.append(")");//add by linrui 20190515
												outputBuffer
														.append("</td></tr>");
											}

										}
									}// end for
								}

								outputBuffer.append("</table></td>");
								outputBuffer.append("</tr>");
								outputBuffer.append("</table>");
							}

							outputBuffer.append("<input type='hidden' name='assignedUser' value=''>");
							outputBuffer.append("<input type='hidden' name='mailUser' value=''>");
							outputBuffer.append("<script language='javascript'>");
							outputBuffer.append("function checkAssignedUser(){");
							outputBuffer
							.append("var assApprover =  document.getElementsByName('assignedApprover');for(var k = 0;k < assApprover.length;k++){if(assApprover[k].value == ''){alert('"+confirmSelectApprover+"');return false;}}");
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
							outputBuffer.append("if(document.form1.chkbxMail[i].checked){");
							outputBuffer.append("mailUsers = mailUsers + document.form1.assignedApprover[i].value + ';';");
							outputBuffer.append("}");
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
							outputBuffer.append("if(allowFinancialController=='Y' && (appr2[appr2.selectedIndex].text.indexOf(' - First Approver')<0 && appr2[appr2.selectedIndex].text.indexOf('"+ firstApproverLable +"')<0)){");
							outputBuffer.append("alert('" + notAFinancialController + "');");
							outputBuffer.append("return false;");
							//modify by hjs 20071024
							outputBuffer.append("} else {");
							outputBuffer.append("noFinancialControllerFlag = false;");
							outputBuffer.append("}");
							
							outputBuffer.append("assignedUsers = document.form1.assignedApprover.value;");
							outputBuffer.append("if(document.form1.chkbxMail.checked){");
							outputBuffer.append("mailUsers = assignedUsers;");
							outputBuffer.append("}");
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
							outputBuffer.append("} else ");
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
							outputBuffer.append("return true;");
							outputBuffer.append("}");
							outputBuffer.append("}");
							outputBuffer.append("}");
							outputBuffer.append("</script>");
						} catch (NTBException ntbe) {
							Log.error("Custom Tag Process error (AssignUser)", ntbe);
							outputBuffer = new StringBuffer("");
							outputBuffer.append("<script language='javascript'>");
							outputBuffer.append("function checkAssignedUser(){");
							outputBuffer.append("return true;}");
							outputBuffer.append("</script>");

						}
					} else {
						outputBuffer.append("<script language='javascript'>");
						outputBuffer.append("function checkAssignedUser(){");
						outputBuffer.append("return true;}");
						outputBuffer.append("</script>");
					}

					out.write(outputBuffer.toString());

				}
			}

		} catch (Exception e) {
			Log.error("Custom Tag Process error (AssignUser)", e);
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
