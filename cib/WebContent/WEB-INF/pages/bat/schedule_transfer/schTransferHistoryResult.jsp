<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
<SCRIPT language=JavaScript src="/cib/javascript/common1.js?v=20180117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad() {
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
function doSubmit() {
	if(validate_schTransferHistory(document.getElementById("form1"))){
		setFormDisabled('form1');
		document.getElementById("form1").submit();
	}
}


</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/schTransferHistory.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bat.sch_transfer_history" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_sch_bat_his" defaultvalue="BANK Online Banking >Transfer > Transfer History"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_sch_bat_his" defaultvalue="TRANSFER HISTORY"/><!-- InstanceEndEditable --></td>
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
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="schTransferHistory" form="form1" file="sch_transfer_history" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td width="28%" class="label1"><set:label name="sch_business_type"/></td>
                  <td colspan="2" class="content1" ><select id="beneficiaryType" name="beneficiaryType" nullable="0" >
                    <option value="">----- <set:label name="select_info" defaultvalue="select_info"/> ------</option>
                    <set:rblist file="app.cib.resource.bat.sch_business_type"> <set:rboption name="beneficiaryType"/> </set:rblist>
                  </select>
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr class="greyline">
                  <td width="28%" height="39" class="label1"><set:label name="period" defaultvalue="period"/></td>
                  <td width="72%" class="content1" colspan="3"><span class="content">
                    <input id="date_range" name="date_range" type="radio" value="all" checked onClick="changeRange('all')">
                    <set:label name="all" defaultvalue="all"/> </span><span class="sectionbutton">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="listHistory">
                    </span></td>
                </tr>
                <tr>
                  <td width="28%" class="label1">&nbsp;</td>
                  <td width="72%" class="content1"><span class="content">
                    <input id="date_range" name="date_range" type="radio" value="range" onClick="changeRange('range')">
                    </span> <set:label name="Date_From"/>
                    <input id="dateFrom" name="dateFrom" type="text" value="<set:data name='dateFrom' format='date'/>
" size="12" maxlength="10" disabled="disabled"> <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" >&nbsp;&nbsp;&nbsp;&nbsp; <set:label name="Date_To"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="dateTo" name="dateTo" type="text" value="<set:data name='dateTo' format='date'/>
" size="12" maxlength="10" disabled="disabled"> <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" > <select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));">
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
                <tr align="center">
                  <td width="55%" height="40" align="right"><span class="sectionbutton">
                    <input id="search" name="search" type="button" value="&nbsp;&nbsp;<set:label name='search' defaultvalue=' search '/>&nbsp;&nbsp;" onClick="doSubmit('listHistory')">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="listHistory">
                    </span></td>
                  <td align="right" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                      <%--<tr>
                        <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=recList&columnTitles=<set:label name='user_id'/>,<set:label name='ben_type'/>,<set:label name='sch_name'/>,<set:label name='sch_type'/>,<set:label name='sch_date'/>,<set:label name='status'/>,<set:label name='upload_result'/>,<set:label name='host_err_msg'/>&columnNames=userId,beneficiaryType,scheduleName,frequnceType,scheduleDate||format@date,status,hostresponseCode"><set:label name="download" rb="app.cib.resource.common.operation"/></a> </td>
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
                  <td class="listheader1"><div align="left"><set:label name="sch_date"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="status"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="upload_result"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="host_err_msg"/></div></td>
                  <td class="listheader1"><div align="center"><set:label name="view_detail"/></div></td>
                </tr>
                <set:list name="recList"  showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td align="left" class="listcontent1"><set:listdata name="userId"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="beneficiaryType" rb="app.cib.resource.rpt.beneficiary_type"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="scheduleName"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="frequnceType" rb="app.cib.resource.bat.frequence_type" /></td>
                  <td align="left" class="listcontent1"><set:listdata name="scheduleDate" format="date"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="status" rb="app.cib.resource.rpt.status"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="hostresponseCode" rb="app.cib.resource.bat.sch_result"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="hosterrorCode" db="hostErrMsg"/></td>
                  <td align="center" class="listcontent1">
				  <%--<input type="hidden" id="url_<set:listdata name='transId'/>" value="/cib/schTransferHistory.do?ActionMethod=viewDetail&beneficiaryType=<set:data name='beneficiaryType'/>&beneficiaryTypeInRec=<set:listdata name='beneficiaryType'/>&userId=<set:listdata name='userId'/>&transId=<set:listdata name='transId'/>&frequnceType=<set:listdata name='frequnceType'/>&frequnceDays=<set:listdata name='frequnceDays'/>&date_range=<set:data name='date_range'/>&dateFrom=<set:data name='dateFrom'/>&dateTo=<set:data name='dateTo'/>&customReturn=4&scheduleName=<set:listdata name='scheduleName' escapechar='true'/>&scheduleDate=<set:listdata name='scheduleDate'/>&status=<set:listdata name='status'/>" >
				  	<a onClick="toTargetFormgoToTarget(document.getElementById('url_<set:listdata name='transId'/>').value)" href="#"><set:label name="view_detail"/></a>
				  --%>
				  <a onClick="postToMainFrame('/cib/schTransferHistory.do?ActionMethod=viewDetail',{beneficiaryType:'<set:data name='beneficiaryType'/>',beneficiaryTypeInRec:'<set:listdata name='beneficiaryType'/>',userId:'<set:listdata name='userId'/>',transId:'<set:listdata name='transId'/>',frequnceType:'<set:listdata name='frequnceType'/>',frequnceDays:'<set:listdata name='frequnceDays'/>',date_range:'<set:data name='date_range'/>',dateFrom:'<set:data name='dateFrom'/>',dateTo:'<set:data name='dateTo'/>',customReturn:4,scheduleName:'<set:listdata name='scheduleName' escapechar='true'/>',scheduleDate:'<set:listdata name='scheduleDate'/>',status:'<set:listdata name='status'/>'})" href="#"><set:label name="view_detail"/>
				  </a>
				  
				  </td>
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
