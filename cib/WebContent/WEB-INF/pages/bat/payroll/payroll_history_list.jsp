<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bat.payroll">
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
<script language="JavaScript" src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	if('<set:data name="date_range"/>' != 'range') {
		document.form1.date_range[0].checked = true;
		document.form1.date_range[0].onclick();
	} else {
		document.form1.date_range[1].checked = true;
		document.form1.date_range[1].onclick();
	}
}
function changeRange(range){
if(range == 'all'){
document.getElementById("dateFrom").value='';
document.getElementById("dateTo").value='';
document.getElementById("dateFrom").disabled = true;
document.getElementById("dateTo").disabled = true;
document.getElementById("dateRange").value=0;
document.getElementById("dateRange").disabled = true;
}else{
document.getElementById("dateFrom").disabled = false;
document.getElementById("dateTo").disabled = false;
document.getElementById("dateRange").disabled = false;
}
}
function doSubmit(arg) {
	if(validate_query(document.getElementById("form1"))){
		//document.getElementById("submit1").disabled=true;
		if (arg == 'list') {
			document.form1.ActionMethod.value = 'listHistory';
		}
		document.getElementById("form1").submit();
		setFormDisabled("form1");
		//setFieldEnabled("buttonReturn");
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/payroll.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bat.payroll" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_history" defaultvalue="MDB Corporate Online Banking > Online Report >  Bill Payment Report"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_history" defaultvalue="BILL PAYMENT REPORT"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/payroll.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="user_actitity_report" form="form1" file="approve_history" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="2" class="groupinput">&nbsp;</td>
                </tr>
                <tr>
                  <td width="20%" class="label1"><set:label name="period" defaultvalue="period"/></td>
                  <td class="content1"><span class="content">
                    <input id="date_range" name="date_range" type="radio" value="all" checked onClick="changeRange('all')">
                    <set:label name="all" defaultvalue="all"/> </span></td>
                </tr>
                <tr class="greyline">
                  <td width="20%" class="label1">&nbsp;</td>
                  <td class="content1"><span class="content">
                    <input id="date_range" name="date_range" type="radio" value="range" onClick="changeRange('range')">
                    </span> <set:label name='Date_From'/>&nbsp;
                    <input name="dateFrom" type="text" id="dateFrom" size="12" maxlength="10" disabled value="<set:data name='dateFrom'/>">
                    <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" >&nbsp;&nbsp;&nbsp; <set:label name='Date_To'/>&nbsp;
                    <input name="dateTo" type="text" id="dateTo" size="12" maxlength="10" disabled value="<set:data name='dateTo'/>">
                    <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" > <select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));">
                      <option value='0'><set:label name="Select_Date_Short_Cut" defaultvalue="----- Select a Date Short-cut ------"/></option>
                      <set:rblist file="app.cib.resource.common.date_selection">
                        <set:rboption/>
                      </set:rblist>
                    </select></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input id="search" name="search" type="button" value="&nbsp;&nbsp;<set:label name='search' defaultvalue=' search '/>&nbsp;&nbsp;" onClick="doSubmit('list')">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="ok">
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
               <tr>
			   <td height="40" class="content1"  colspan="7" align="right"><table width="100" border="0" cellspacing="0" cellpadding="0">
                  <%--<tr>
                    <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=historyList&columnTitles=<set:label name='upload_date'/>,<set:label name='total_amt'/>,<set:label name='no_of_record'/>,<set:label name='status'/>,<set:label name='upload_status'/>&columnNames=requestTime||format@datetime,totalAmount||format@amount,totalNumber,status"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>
                  </tr>
                --%></table></td>
			   </tr>
                <tr>
                  <td class="listheader1"><set:label name="upload_date"/></td>
                  <td align="right" class="listheader1"><set:label name="total_amt"/></td>
                  <td class="listheader1"><set:label name="no_of_record"/></td>
                  <td class="listheader1"><set:label name="status"/></td>
				  <set:if name="approverFlag" condition="EQUALS" value="Y">
				  <td class="listheader1"><div align="center"><set:label name="CHANGE_APPROVER" defaultvalue="CHANGE APPROVER"/></div></td>
				  </set:if>
                  <td class="listheader1"><set:label name="upload_status"/></td>
                  <td align="center" class="listheader1"><set:label name="view_detail"/></td>
                </tr>
                <set:list name="historyList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1" align="left"><set:listdata name="requestTime" format="datetime"/></td>
                  <td class="listcontent1" align="right"><set:listdata name="totalAmount" format="amount"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="totalNumber"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="status" rb="app.cib.resource.common.status"/></td>
				  <set:if name="approverFlag" condition="EQUALS" value="Y">
				  <td class="listcontent1"><div align="center"><set:listif name="status" condition="NOTEQUALS" value="1">--</set:listif><set:listif name="status" condition="EQUALS" value="1"><set:listif name="changeFlag" condition="EQUALS" value="Y"><a onClick="toTargetFormgoToTarget('/cib/approve.do?ActionMethod=changeApproverLoad&txnTypeToChange=<set:data name="txnType"/>&transNoToChange=<set:listdata name="payrollId"/>')" href="#"><set:label name="Change" defaultvalue="Change"/></a></set:listif><set:listif name="changeFlag" condition="EQUALS" value="N">--</set:listif></set:listif></div></td>
				  </set:if>
                  <td class="listcontent1" align="left"><set:listdata name="batchResult" rb="app.cib.resource.bat.payroll_result"/></td>
                  <td class="listcontent1" align="center"><a onClick="toTargetFormgoToTarget('/cib/payroll.do?ActionMethod=viewHistoryDetail&payrollId=<set:listdata name='payrollId'/>&date_range=<set:data name='date_range'/>&dateFrom=<set:data name='dateFrom'/>&dateTo=<set:data name='dateTo'/>')" href="#"><set:label name="view_detail" defaultvalue="view_detail"/></a></td>
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
