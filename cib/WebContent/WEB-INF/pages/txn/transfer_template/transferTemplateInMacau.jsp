<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.template_listMacau">
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
<SCRIPT language=JavaScript src="/cib/javascript/toTarget.js"></SCRIPT>
<!-- /* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */-->
<SCRIPT language=JavaScript src="/cib/javascript/common1.js"></SCRIPT>
<!-- /* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */-->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit(arg) {
	if (arg == 'add') {
		document.form1.ActionMethod.value = 'addTemplateLoad';
		document.form1.submit();
		setFormDisabled('form1');
	} else {
		if(validate_templateList(document.getElementById("form1"))){
			if (arg == 'execute') {
				document.form1.ActionMethod.value = 'transferTemplateLoad';
			} else if (arg == 'edit') {
				document.form1.ActionMethod.value = 'editTemplateLoad';
			} else if (arg == 'delete') {
				document.form1.ActionMethod.value = 'deleteTemplate';
			}
			document.form1.submit();
			setFormDisabled('form1');
		}
	}
}
//add by long_zg 2014-03-04 for toTarget 未定义

function toTarget(theTarget){
	document.form1.action = theTarget;
	document.form1.submit();
} 
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferTemplateInMacau.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.template_listMacau" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="BANK Online Banking >Transfer >Accounts in Banks Overseas"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="TRANSFER TEMPLATES"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/transferTemplateInMacau.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="templateList" form="form1" file="transfer_template_list" /> </td>
                </tr>
              </table>
              <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_c"><a onClick="toTarget('/cib/transferTemplate.do?ActionMethod=listTemplate')" href="#"><set:label name="Accounts_in_BANK" defaultvalue="Accounts in BANK"/></a></td>
                        <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                    </table></td>
                    <!-- /* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */-->
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_c"><a onClick="postToMainFrame('/cib/transferTemplate3rd.do?ActionMethod=listTemplate',{ownerAccFlag:'N'})" href="#"><set:label name="Accounts_in_BANK_3RD" defaultvalue="To MDB Account"/></a></td>
                        <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                    </table></td>
                  <!-- /* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */-->
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_o"><set:label name="Accounts_in_Macau" defaultvalue="Accounts in Other Banks in Macau"/></td>
                        <td class="rtab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                    </table></td>
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_c"><a onClick="toTarget('/cib/transferTemplateInOversea.do?ActionMethod=listTemplate')" href="#"> <set:label name="Accounts_in_Overseas" defaultvalue="Accounts in Banks Overseas"/> </a></td>
                        <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                    </table></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1"><div align="left"></div></td>
                  <td  class="listheader1"><div align="left"><set:label name="FROM_ACCOUNT" defaultvalue="Account Number"/></div></td>
                  <td  class="listheader1"><div align="left"><set:label name="Beneficiary_Name" defaultvalue="Beneficiary  Account Name"/></div></td>
                  <td  class="listheader1"><div align="left"><set:label name="Beneficiary_Account" defaultvalue="Beneficiary  Account"/></div></td>
                  <td  class="listheader1"><div align="center"><set:label name="Currency" defaultvalue="Currency"/></div></td>
                  <td  class="listheader1"><div align="left"><set:label name="Transfer_Description" defaultvalue="Transfer Description"/></div></td>
                </tr>
                <set:list name="templateList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1"><div align="center"><set:listradio name="transId" value="transId" text=""/></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="fromAccount"/></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="beneficiaryName1"/></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="toAccount"/></div></td>
                  <td class="listcontent1"><div align="center"><set:listdata name="toCurrency"/></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="remark"/></div></td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input id="execute" name="execute" type="button" value="<set:label name='execute' defaultvalue=' execute '/>" <set:selected key="recordFlag" equalsvalue="Y" output="disabled"/> onClick="doSubmit('execute')"> <input id="edit" name="edit" type="button" value="<set:label name='edit' defaultvalue=' edit '/>"  <set:selected key="recordFlag" equalsvalue="Y" output="disabled"/> onClick="doSubmit('edit')"> <input id="delete" name="delete" type="button" value="<set:label name='delete' defaultvalue=' delete '/>"  <set:selected key="recordFlag" equalsvalue="Y" output="disabled"/> onClick="doSubmit('delete')">
                    <input id="add" name="add" type="button" value="<set:label name='add' defaultvalue='add '/>"  onClick="doSubmit('add')">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="addTemplate">
                    <set:singlesubmit/>
                  </td>
                </tr>
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
