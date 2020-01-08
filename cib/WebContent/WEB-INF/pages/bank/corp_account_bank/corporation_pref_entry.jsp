<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bnk.corporation">
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
	//add by hjs 20070321
	if(document.getElementById('toUpper')){
		document.getElementById('toUpper').innerHTML = document.getElementById('toUpper').innerHTML.toUpperCase();
	}
}
function doSubmit(arg,corpId,corpName) {
	if(validate_pref_entry(document.getElementById("form1"))){
		document.getElementById("form1").action = '/cib/accEnquiryBank.do';
		document.getElementById("ActionMethod").value = 'listAccountSummary';
	/*
	if (arg == 'summary') {
		document.getElementById("form1").action = '/cib/accEnquiryBank.do';
		document.getElementById("ActionMethod").value = 'listAccountSummary';
	} else if (arg == 'current') {
		document.getElementById("form1").action = '/cib/accEnquiryBank.do';
		document.getElementById("ActionMethod").value = 'listCurrentAccount';
	} else if (arg == 'saving') {
		document.getElementById("form1").action = '/cib/accEnquiryBank.do';
		document.getElementById("ActionMethod").value = 'listSavingAccount';
	} else if (arg == 'timedeposit') {
		document.getElementById("form1").action = '/cib/accEnquiryBank.do';
		document.getElementById("ActionMethod").value = 'listTimeDeposit';
	} else if (arg == 'overdraft') {
		document.getElementById("form1").action = '/cib/accEnquiryBank.do';
	    document.getElementById("ActionMethod").value = 'listOverdraftAccount';
	} else if (arg == 'loan') {
		document.getElementById("form1").action = '/cib/accEnquiryBank.do';
	    document.getElementById("ActionMethod").value = 'listLoanAccount';
	}else if (arg == 'outstanding') {
		document.getElementById("form1").action = '/cib/approve.do';
	    document.getElementById("ActionMethod").value = 'statusEnquiryBank';
	    document.getElementById("procStatus").value = 'P';		
	}
	*/
		document.getElementById("corpId").value = corpId;
		document.getElementById("corpName").value = corpName;	
		setFormDisabled("form1");
		document.getElementById("form1").submit();
	}
}
function doEnquiry()
{
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/accountEnqBank.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.corporation" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_bank" defaultvalue="MDB Corporate Online Banking > Company Accounts > "/><set:data name='prefType' rb='app.cib.resource.bnk.account_enq_bank'/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><span id="toUpper"><set:data name='prefType' rb='app.cib.resource.bnk.account_enq_bank'/></span><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/accountEnqBank.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="perf_entry" form="form1" file="corp_mng" /></td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/> <input id="queryCorpId" name="queryCorpId" type="text" value="<set:data name='queryCorpId'/>" size="20" maxlength="20">&nbsp;&nbsp;&nbsp;&nbsp;<set:label name="Corp_Name" defaultvalue="Company Name"/> <input id="queryCorpName" name="queryCorpName" type="text" value="<set:data name='queryCorpName'/>" size="20" maxlength="20">&nbsp;&nbsp;&nbsp;&nbsp;<input id="buttonOk" name="buttonOk" type="button" value="<set:label name='buttonEnquiry' defaultvalue=' Enquiry '/>" onClick="doEnquiry()">
                  <input id="ActionMethod" name="ActionMethod" type="hidden" value="prefEntry">
                  <input id="prefType" name="prefType" type="hidden" value="<set:data name='prefType' />">
                  <input id="corpId" name="corpId" type="hidden" value="0">
                  <input id="corpName" name="corpName" type="hidden" value="0">
                  <input id="procStatus" name="procStatus" type="hidden" value="P">
				  </td>
              </tr>
			  </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="listheader1"><set:label name="Corp_Id" defaultvalue="Corporations ID"/></td>
                <td class="listheader1"><set:label name="Corp_Name" defaultvalue="Corporations Name"/></td>
                <td class="listheader1"><set:label name="corp_type" defaultvalue="Corporations Type"/></td>
                <td class="listheader1"><set:label name="Status" defaultvalue="Status"/></td>
              </tr>
			  <set:list name="corpList" showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1"><set:listif name="status" value="1" condition="notequals"><a href="#" onClick="doSubmit('<set:data name='prefType'/>','<set:listdata name='corpId'/>','<set:listdata name='corpName'/>')"></set:listif><set:listdata name="corpId"/><set:listif name="status" value="1" condition="notequals"></a></set:listif></td>
                <td class="listcontent1"><set:listdata name="corpName"/></td>
                <td class="listcontent1"><set:listdata name="corpType" rb="app.cib.resource.common.corp_type"/></td>
                <td class="listcontent1"><set:listdata name="status" rb="app.cib.resource.common.status"/></td>
              </tr>
			  </set:list>
            </table>
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
