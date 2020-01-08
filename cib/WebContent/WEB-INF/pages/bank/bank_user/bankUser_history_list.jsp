<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bnk.bank_user">
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
	var form1 = document.form1;
	var date_range = '<set:data name="date_range"/>';
	if (date_range == 'all'){
		form1.date_range[0].checked = true;
		form1.dateFrom.disabled = true;
		form1.dateTo.disabled = true;
	} else if (date_range == 'range'){
		form1.date_range[1].checked = true;
		form1.dateFrom.disabled = false;
		form1.dateTo.disabled = false;
		form1.dateRange.disabled = false;
	}
}
function doSubmit(arg) {
	if(validate_payment_history(document.getElementById("form1"))){
		//document.getElementById("submit1").disabled=true;
		if (arg == 'list') {
			document.form1.ActionMethod.value = 'listOperationHistory';
		}
		document.getElementById("form1").submit();
		setFormDisabled("form1");
		//setFieldEnabled("buttonReturn");
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
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/bankUser.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.bank_user" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_OprHis" defaultvalue="MDB Corporate Online Banking > Online Report >  Transaction Report"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_OprHis" defaultvalue="TRANSACTION REPORT"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/bankUser.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="payment_history" form="form1" file="bill_payment" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="3" class="groupinput">&nbsp;</td>
                </tr>
                <tr>
                  <td width="20%" class="label1"><set:label name="period" defaultvalue="period"/></td>
                  <td width="80%" colspan="2" class="content1"><span class="content">
                    <input id="date_range" name="date_range" type="radio" value="all" checked onClick="changeRange('all')">
&nbsp; <set:label name="all" defaultvalue="all"/> <br>
<input id="date_range" name="date_range" type="radio" value="range" onClick="changeRange('range')">
&nbsp; <set:label name='date_from' defaultvalue='date_from'/>&nbsp;
<input name="dateFrom" type="text" id="dateFrom" size="12" maxlength="10" disabled value="<set:data name='dateFrom'/>">
<img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" >&nbsp;&nbsp;&nbsp; <set:label name='date_to' defaultvalue='date_to'/>&nbsp;
<input name="dateTo" type="text" id="dateTo" size="12" maxlength="10" disabled value="<set:data name='dateTo'/>">
<img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" >
<select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));">
  <option value='0'>----- Select a Date Short-cut ------</option>
  <set:rblist file="app.cib.resource.common.date_selection"> <set:rboption/> </set:rblist>
</select>
                  </span></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="requester" defaultvalue="requester"/></td>
                  <td colspan="2" class="content1"><input name="requester" type="text" id="requester" maxlength="20" value="<set:data name='requester'/>"></td>
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
                        <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=hisList&fileName=user_operation_history&columnTitles=<set:label name='requester'/>,<set:label name='update_time'/>,<set:label name='User_Name'/>,<set:label name='User_Id'/>,<set:label name='Role'/>,<set:label name='operation'/>,<set:label name='Status'/>&columnNames=requester,lastUpdateTime||format@datetime,userName,userId,roleId||rb@app.cib.resource.common.bank_role1,operation||rb@app.cib.resource.common.operation,status||rb@app.cib.resource.common.status">Download</a></td>
                      </tr>
                    --%></table></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1"><div align="left"><set:label name="requester" defaultvalue="requester"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="update_time" defaultvalue="update_time"/></div></td>
                  <td class="listheader1"><set:label name="User_Name" defaultvalue="User_Name"/></td>
				  <td class="listheader1"><set:label name="User_Id" defaultvalue="User ID"/></td>
                  <td class="listheader1"><set:label name="Role" defaultvalue="Role"/></td>
                  <td class="listheader1"><div align="left"><set:label name="operation" defaultvalue="operation"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="Status" defaultvalue="Status"/></div></td>
                  <td class="listheader1"><div align="center"><set:label name="View_Detail" defaultvalue="View_Detail"/></div></td>
                </tr>
                <set:list name="hisList"  showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td align="left" class="listcontent1"><set:listdata name="requester"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="lastUpdateTime" format="datetime" /></td>
                  <td align="left" class="listcontent1"><set:listdata name="userName"/></td>
				  <td align="left" class="listcontent1"><set:listdata name="userId"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="roleId" rb="app.cib.resource.common.bank_role1"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="operation" rb="app.cib.resource.common.operation"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="status" rb="app.cib.resource.common.status"/></td>
                  <td align="center" class="listcontent1"><a onClick="postToMainFrame('/cib/bankUser.do?ActionMethod=view',{userId:'<set:listdata name='userId'/>',fromPage:'2'})" href="#"><set:label name="View_Detail"/></a> </td>
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
