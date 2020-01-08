<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bat.sch_transfer_history">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/common1.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language="JavaScript" src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit(arg) {
 if(validate_transfer_history_op(document.getElementById("form1"))){
		//document.getElementById("submit1").disabled=true;
		if (arg == 'list') {
			document.form1.ActionMethod.value = 'listOperationHistory';
		}
		document.getElementById("form1").submit();
		setFormDisabled("form1");
		//setFieldEnabled("buttonReturn");
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/schTransferHistory.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bat.sch_transfer_history" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_sch_opt_his" defaultvalue="MDB Corporate Online Banking > Online Report >  Transaction Report"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_sch_opt_his" defaultvalue="TRANSACTION REPORT"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/schTransferHistory.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="transfer_history_op" form="form1" file="transfer_history_op" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="3" class="groupinput">&nbsp;</td>
                </tr>
                <tr>
                  <td width="20%" class="label1"><set:label name="sch_business_type" defaultvalue="sch_business_type"/></td>
                  <td width="80%" colspan="2" class="content1"><select id="beneficiaryType" name="beneficiaryType" nullable="0" >
                      <option value="">----- <set:label name="select_info" defaultvalue="select_info"/> ------</option>
                      <set:rblist file="app.cib.resource.bat.sch_business_type"> <set:rboption name="beneficiaryType"/> </set:rblist>
                    </select></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="user_id" defaultvalue="user_id"/></td>
                  <td colspan="2" class="content1"><input name="userId" type="text" id="userId" maxlength="20" value="<set:data name='userId'/>"></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><div align="right">
                      <input id="search" name="search" type="button" value="&nbsp;&nbsp;<set:label name='search' defaultvalue=' search '/>&nbsp;&nbsp;" onClick="doSubmit('list')">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="ok">
                  </div></td>
                  <td width="44%" height="40"  colspan="7" align="right" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                      <%--<tr>
                        <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=hisList&&columnTitles=<set:label name='user_id'/>,<set:label name='ben_type'/>,<set:label name='sch_name'/>,<set:label name='sch_type'/>,<set:label name='operation'/>,<set:label name='status'/>&columnNames=userId,beneficiaryType,scheduleName,frequnceType||rb@app.cib.resource.bat.frequence_type,operation,status"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>
                      </tr>
                    --%></table></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1"><div align="left"><set:label name="user_id"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="ben_type"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="sch_name"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="sch_type"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="operation"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="status"/></div></td>
                  <td class="listheader1"><div align="center"><set:label name="view_detail"/></div></td>
                </tr>
                <set:list name="hisList"  showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td align="left" class="listcontent1"><set:listdata name="userId"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="beneficiaryType" rb="app.cib.resource.rpt.beneficiary_type"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="scheduleName"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="frequnceType" rb="app.cib.resource.bat.frequence_type" /></td>
                  <td align="left" class="listcontent1"><set:listdata name="operation" rb="app.cib.resource.common.operation"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="status" rb="app.cib.resource.common.status"/></td>
                  <td align="center" class="listcontent1"><a onClick="postToMainFrame('/cib/schTransferHistory.do?ActionMethod=viewDetail',{beneficiaryType:'<set:data name='beneficiaryType'/>',beneficiaryTypeInRec:'<set:listdata name='beneficiaryType'/>',userId:'<set:data name='userId'/>',transId:'<set:listdata name='transId'/>',frequnceType:'<set:listdata name='frequnceType'/>',frequnceDays:'<set:listdata name='frequnceDays'/>',customReturn:3,scheduleName:'<set:listdata name='scheduleName'/>',status:'<set:listdata name='status'/>'})" href="#"><set:label name="view_detail"/></a> </td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"></td>
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
