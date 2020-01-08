<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.rpt.sch_report_list">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/common1.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	var size = 0;
	<set:list name="reportList">
		size++;
	</set:list>
	if(size == 0) {
		document.getElementById('cancel').style.display = 'none';
	}
}
function doSubmit(arg) {
	if(validate_sch_report(document.getElementById("form1"))){
		if (arg == 'cancel') {
			document.form1.ActionMethod.value = 'cancel';
			document.getElementById("form1").submit();
			setFormDisabled("form1");
			//setFieldEnabled("buttonReturn");
		}
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/schTxnReport.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.rpt.sch_report_list" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_sch" defaultvalue="BANK Online Banking > Online Report > Tomorrow Transaction Report"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_sch" defaultvalue="SCHEDULED TRANSFER REPORT"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/schTxnReport.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td>
				  	<set:messages width="100%" cols="1" align="center"/>
				  	<set:fieldcheck name="sch_report" form="form1" file="report" />
				  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
			   <tr>
			   <td height="40" class="content1"  colspan="10"align="right"><table width="100" border="0" cellspacing="0" cellpadding="0">
                  <%--<tr>
                    <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=reportList&columnTitles=<set:label name='user_id'/>,<set:label name='ben_type'/>,<set:label name='sch_name'/>,<set:label name='sch_type'/>,<set:label name='sch_date'/>,<set:label name='status'/>&columnNames=userId,beneficiaryType,scheduleName,frequnceType||rb@app.cib.resource.bat.frequence_type,scheduleDate||format@date,'status||rb@app.cib.resource.rpt.status"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>
                  </tr>
                --%></table></td>
			   </tr>
                <tr>
                  <td colspan="8" class=""><b>*<set:label name="notice" defaultvalue="notice"/>&nbsp;(<set:data name="nextBatchDay" format="date"/>)</b></td>
                </tr>
                <tr>
                  <td class="listheader1"><div align="center"></div></td>
                  <td class="listheader1"><div align="left"><set:label name="user_id"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="ben_type"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="sch_name"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="sch_type"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="sch_date"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="status"/></div></td>
                  <td class="listheader1"><div align="center"><set:label name="view_detail"/></div></td>
                </tr>
                <set:list name="reportList"  showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td align="center" class="listcontent1"><set:listradio name="batchId" value="batchId" text=""/></td>
                  <td align="left" class="listcontent1"><set:listdata name="userId"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="beneficiaryType" rb="app.cib.resource.rpt.beneficiary_type"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="scheduleName"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="frequnceType" rb="app.cib.resource.bat.frequence_type" /></td>
                  <td align="left" class="listcontent1"><set:listdata name="scheduleDate" format="date"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="status" rb="app.cib.resource.rpt.status"/></td>
                  <td align="center" class="listcontent1">
				  	<a onClick="postToMainFrame('/cib/schTxnReport.do?ActionMethod=viewDetail',{beneficiaryType:'<set:listdata name='beneficiaryType'/>',transId:'<set:listdata name='transId'/>',frequnceType:'<set:listdata name='frequnceType'/>',frequnceDays:'<set:listdata name='frequnceDays'/>',scheduleName:'<set:listdata name='scheduleName'/>',scheduleDate:'<set:listdata name='scheduleDate'/>'})" href="#"><set:label name="view_detail"/></a>
				  </td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><div align="center">
                      <input name="cancel" id="cancel" type="button" value="<set:label name='cancel' defaultvalue=' cancel '/>"  onClick="doSubmit('cancel')">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="cancel">
                    </div></td>
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
