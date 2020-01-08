<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bnk.config_checking">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
  <title>Corporation Banking</title>
  <!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
  <script language=JavaScript>
  var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit(arg){
	if (arg == 'return') {
		window.location.href="/cib/corporation.do?ActionMethod=prefEntry&amp;prefType=checking";
	}
	setFormDisabled("form1");
}
</script>
  <!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corporation.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.config_checking" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
          <set:label name="navigationTitle_checking" defaultvalue="MDB Corporate Online Banking > Corporation Management > Corporation Information"/>
          <!-- InstanceEndEditable --></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#" onClick="printPage('pageheader');"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
	<!--
    <td class="buttonhelp"><a href="#"><img src="/cib/images/shim.gif" width="36" height="18" border="0"></a></td>
	-->
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="EDC64A">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="white">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="40">
  <tr>
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" -->
        <set:label name="functionTitle_checking" defaultvalue="Corporation information"/>
        <!-- InstanceEndEditable --></td>
    <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" bgcolor="white"><img src="/cib/images/shim.gif" width="1" height="1"></td>
        </tr>
      </table></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="19" class="topborderlong"><img src="/cib/images/table_top_long.gif" width="100%" height="19"></td>
  </tr>
  <tr>
    <td align="center" bgcolor="#999999"><table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
        <tr>
          <td width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
          <td>
		  <form action="/cib/corporation.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
                <set:messages width="100%" cols="1" />
				<table width="100%" border="0" cellspacing="0" cellpadding="3">
              		<tr>
                		<td colspan="2" class="groupinput"><set:label name="Corp_Info" rb="app.cib.resource.bnk.corporation"/></td>
              		</tr>
              		<tr>
                		<td width="28%" class="label1"><set:label name="Corp_Id" rb="app.cib.resource.bnk.corporation"/></td>
                		<td width="72%" class="content1"><set:data name='corpId'/></td>
              		</tr>
              		<tr class="greyline">
                		<td class="label1"><set:label name="Corp_Name" rb="app.cib.resource.bnk.corporation"/></td>
                		<td class="content1"><set:data name='corpName'/></td>
              		</tr>     
              		<tr>
                		<td class="label1"><set:label name="corp_type" rb="app.cib.resource.bnk.corporation"/></td>
                		<td class="content1"><set:data name='corpType' rb="app.cib.resource.common.corp_type"/></td>
              		</tr>
              		<tr class="greyline">
                		<td class="label1"><set:label name="ALLOW_FINANCIAL_CONTROLLER" rb="app.cib.resource.bnk.corporation"/></td>
                		<td class="content1"><set:data name='allowFinancialController' rb="app.cib.resource.common.yes_no"/></td>
              		</tr>
            	</table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td colspan="2" class="groupinput"><set:label name="corp_attr"/></td>
                  </tr>
                  <tr>
                    <td colspan="2" class=""><table width="100%" border="0" cellspacing="0" cellpadding="3">
                        <tr>
                          <td class="listheader1" align="left" width="250"><set:label name="check_point_desc"/></td>
                          <td align="left" class="listheader1" width="400"><set:label name="req_setting"/></td>
                          <td class="listheader1" align="left" width="200"><set:label name="curr_setting"/></td>
                          <td class="listheader1" align="left"><set:label name="result"/></td>
                        </tr>
                        <set:list name="userInfoList" showNoRecord="YES">
                          <tr class="<set:listclass class1='' class2='greyline'/>">
                            <td class="listcontent1" align="left"><b><set:label name="opt_users_no"/></b></td>
                            <td class="listcontent1" align="left">
								<set:listif name="corpType" condition="equals" value="1"><span style="width:60"><set:label name="at_least"/></span></set:listif>
								<set:listif name="corpType" condition="equals" value="2"><span style="width:60">&nbsp;</span></set:listif>
								<set:listif name="corpType" condition="equals" value="3"><span style="width:60">&nbsp;</span></set:listif>
								<set:listif name="corpType" condition="equals" value="4"><span style="width:60"><set:label name="at_least"/></span></set:listif>
								&nbsp;
								<set:listdata name="reqOprs"/>&nbsp;<set:label name="operators"/><br>
								<set:listif name="corpType" condition="equals" value="1"><span style="width:60"><set:label name="at_least"/></span></set:listif>
								<set:listif name="corpType" condition="equals" value="2"><span style="width:60"><set:label name="at_least"/></span></set:listif>
								<set:listif name="corpType" condition="equals" value="3"><span style="width:60"><set:label name="at_least"/></span></set:listif>
								<set:listif name="corpType" condition="equals" value="4"><span style="width:60"><set:label name="at_least"/></span></set:listif>
								&nbsp;
								<set:listdata name="reqApprs"/>&nbsp;<set:label name="approvers"/><br>
								<set:listif name="corpType" condition="equals" value="1">
									<span style="width:60">
									<set:listif name="allowExecutor" condition="equals" value="Y">
										<set:label name="at_least"/>
									</set:listif>
									<set:listif name="allowExecutor" condition="notequals" value="Y">&nbsp;</set:listif>
									</span>
								</set:listif>
								<set:listif name="corpType" condition="equals" value="2"><span style="width:60">&nbsp;</span></set:listif>
								<set:listif name="corpType" condition="equals" value="3"><span style="width:60">&nbsp;</span></set:listif>
								<set:listif name="corpType" condition="equals" value="4"><span style="width:60">&nbsp;</span></set:listif>
								&nbsp;
								<set:listdata name="reqExecs"/>&nbsp;<set:label name="executors"/><br>
								<set:listif name="corpType" condition="equals" value="1"><span style="width:60"><set:label name="at_least"/></span></set:listif>
								<set:listif name="corpType" condition="equals" value="2"><span style="width:60"><set:label name="at_least"/></span></set:listif>
								<set:listif name="corpType" condition="equals" value="3"><span style="width:60">&nbsp;</span></set:listif>
								<set:listif name="corpType" condition="equals" value="4"><span style="width:60">&nbsp;</span></set:listif>
								&nbsp;
								<set:listdata name="reqAdms"/>&nbsp;<set:label name="admins"/><br>
								<set:listif name="corpType" condition="notequals" value="">
								<set:if name="allowFinancialController" condition="equals" value="Y"><span style="width:60"><set:label name="at_least"/></span></set:if>
								<set:if name="allowFinancialController" condition="notequals" value="Y"><span style="width:60">&nbsp;</span></set:if>
								</set:listif>
								&nbsp;
								<set:listdata name="reqCtrs"/>&nbsp;<set:label name="financial_controller"/>
								</td>
                            <td class="listcontent1" align="left">
								<set:listdata name="currOprs"/>&nbsp;<set:label name="operators"/><br>
								<set:listdata name="currApprs"/>&nbsp;<set:label name="approvers"/><br>
								<set:listdata name="currExecs"/>&nbsp;<set:label name="executors"/><br>
								<set:listdata name="currAdms"/>&nbsp;<set:label name="admins"/><br>
								<set:listdata name="currCtrs"/>&nbsp;<set:label name="financial_controller"/>
								</td>
                            <td class="listcontent1" align="left"><set:listdata name="result" rb="app.cib.resource.bnk.config_checking_result"/></td>
                          </tr>
                        </set:list>
                      </table></td>
                  </tr>
                  <tr >
                    <td colspan="2" class="groupinput"><set:label name="trans_limits"/></td>
                  </tr>
                  <tr>
                    <td colspan="2" class=""><table width="100%" border="0" cellspacing="0" cellpadding="3">
                        <tr>
                          <td class="listheader1" align="left" width="250"><set:label name="check_point_desc"/></td>
                          <td align="left" class="listheader1" width="400"><set:label name="req_setting"/></td>
                          <td class="listheader1" align="left" width="200"><set:label name="curr_setting"/></td>
                          <td class="listheader1" align="left"><set:label name="result"/></td>
                        </tr>
                          <tr class="greyline">
                            <td class="greyline" align="left" colspan="4"><b><set:label name="integrality"/></b></td>
                          </tr>
                        <set:list name="accLimitInfoList" showNoRecord="YES">
                          <tr class="<set:listclass class1='' class2='greyline'/>">
                            <td class="listcontent1" align="left">&nbsp;</td>
                            <td class="listcontent1" align="left"><set:listdata name="accountNo"/></td>
                            <td class="listcontent1" align="left"><set:listdata name="accountLimitStatus" rb='app.cib.resource.sys.pref_status'/></td>
                            <td class="listcontent1" align="left"><set:listdata name="result" rb="app.cib.resource.bnk.config_checking_result"/></td>
                          </tr>
                        </set:list>
                      </table></td>
                  </tr>
                  <tr >
                    <td colspan="2" class="groupinput"><set:label name="auth_pref"/></td>
                  </tr>
                  <tr>
                    <td colspan="2" class=""><table width="100%" border="0" cellspacing="0" cellpadding="3">
                        <tr>
                          <td class="listheader1" align="left" width="250"><set:label name="check_point_desc"/></td>
                          <td align="left" class="listheader1" width="400"><set:label name="req_setting"/></td>
                          <td class="listheader1" align="left" width="200"><set:label name="curr_setting"/></td>
                          <td class="listheader1" align="left"><set:label name="result"/></td>
                        </tr>
                          <tr class="greyline">
                            <td class="greyline" align="left" colspan="4"><b><set:label name="integrality"/></b></td>
                          </tr>
                        <set:list name="noSettingRuleList" showNoRecord="YES">
                          <tr class="<set:listclass class1='' class2='greyline'/>">
                            <td class="listcontent1" align="left">&nbsp;</td>
                            <td class="listcontent1" align="left"><set:listdata name="REQUIRED_SETTING"/></td>
                            <td class="listcontent1" align="left"><set:label name="no_setting"/></td>
                            <td class="listcontent1" align="left"><set:label name="iffy"/></td>
                          </tr>
                        </set:list>
                      </table></td>
                  </tr>
                  <tr>
                    <td colspan="2" class=""><table width="100%" border="0" cellspacing="0" cellpadding="3">
                        <tr>
                          <td class="listheader1" align="left" width="250"><set:label name="check_point_desc"/></td>
                          <td align="left" class="listheader1" width="400"><set:label name="req_setting"/></td>
                          <td class="listheader1" align="left" width="200"><set:label name="curr_setting"/></td>
                          <td class="listheader1" align="left"><set:label name="result"/></td>
                        </tr>
                          <tr class="greyline">
                            <td class="greyline" align="left" colspan="4"><b><set:label name="normal_rule_list"/></b></td>
                          </tr>
                        <set:list name="normalRuleList" showNoRecord="YES">
                          <tr class="<set:listclass class1='' class2='greyline'/>">
                            <td class="listcontent1" align="left">&nbsp;</td>
                            <td class="listcontent1" align="left"><set:listdata name="requiredSetting"/></td>
                            <td class="listcontent1" align="left"><set:listdata name="currentSetting"/></td>
                            <td class="listcontent1" align="left"><set:listdata name="result" rb="app.cib.resource.bnk.config_checking_result"/></td>
                          </tr>
                        </set:list>
                      </table></td>
                  </tr>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td class="groupseperator">&nbsp;</td>
                  </tr>
                </table>
				<!--
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td height="40" colspan="2" class="sectionbutton"><set:if name="status" condition="equals" value="0"></set:if>
                      <set:if name="status" condition="equals" value="2"></set:if>
                      <input name="return" id="return" type="button" value="<set:label name='return' defaultvalue=' return '/>" onClick="doSubmit('return')">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="updateLoad">
                    </td>
                  </tr>
                </table>
				-->
                <!-- InstanceEndEditable -->
		  </form>
		  </td>
          <td width="1%"><img src="/cib/images/shim.gif" width="10" height="1"></td>
      </table></td>
    <td align="right" width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="999999">
    <td><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="white">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="2"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>
</body>
</set:loadrb>
<!-- InstanceEnd --></html>
