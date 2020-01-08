<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
}
function doSubmit(arg) {
	if(validate_previous_file(document.getElementById("form1"))){
		if (arg == 'return') {
			document.form1.ActionMethod.value = 'listHistory';
			document.getElementById("form1").submit();
			setFormDisabled("form1");
			//setFieldEnabled("buttonReturn");
		}
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/payroll.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bat.payroll" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_history" defaultvalue="MDB Corporate Online Banking > Payroll > Upload a Payroll File"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_history_detail" defaultvalue="UPLOAD A PAYROLL FILE"/><!-- InstanceEndEditable --></td>
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
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="previous_file" form="form1" file="payroll" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3" id="round">
                <tr>
                  <td colspan="2" class="groupinput"><set:label name="detail" defaultvalue="detail"/></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="total_amt_file" defaultvalue="total_amt_file"/></td>
                  <td class="content1"><set:data name="currency"/>&nbsp;<set:data name="totalAmount" format="amount"/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="no_of_record_file" defaultvalue="no_of_record_file"/></td>
                  <td class="content1"><set:data name="totalNumber" /></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="total_amt_paid" defaultvalue="total_amt_paid"/></td>
                  <td class="content1"><set:data name="currency"/>&nbsp;<set:data name="totalAmountAccepted" format="amount"/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="no_of_record_accept" defaultvalue="no_of_record_accept"/></td>
                  <td class="content1"><set:data name="totalNumberAccepted" /></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="total_amt_reject" defaultvalue="total_amt_reject"/></td>
                  <td class="content1"><set:data name="currency"/>&nbsp;<set:data name="totalAmountRejected" format="amount"/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="no_of_record_reject" defaultvalue="no_of_record_reject"/></td>
                  <td class="content1"><set:data name="totalNumberRejected" /></td>
                </tr>
                <tr class="">
                  <td width="28%" class="label1"><set:label name="pay_day" defaultvalue="pay_day"/></td>
                  <td width="72%" class="content1"><set:data name="startValueDate" format="date"/></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="">&nbsp;</td>
                </tr>
                <tr>
                  <td class="groupinput"><set:label name="reject_trans" defaultvalue="reject_trans"/> :</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td align="left" class="listheader1"><set:label name="ref_no" defaultvalue="ref_no"/></td>
                  <td align="left" class="listheader1"><set:label name="payee_acc" defaultvalue="payee_acc"/></td>
                  <td align="center" class="listheader1"><set:label name="ccy" defaultvalue="ccy"/></td>
                  <td align="right" class="listheader1"><set:label name="amt" defaultvalue="amt"/></td>
                  <td align="left" class="listheader1"><set:label name="upload_status" defaultvalue="upload_status"/></td>
			    </tr>
                <set:list name="payrollRecList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td align="left" class="listcontent1"><set:listdata name="referenceNo"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="toAccount" /></td>
                  <td align="center" class="listcontent1"><set:data name="currency" /></td>
                  <td align="right" class="listcontent1"><set:listdata name="creditAmount" format="amount"/></td>
                  <td align="left" class="listcontent1"><set:listdata name="detailResult" rb="app.cib.resource.bat.payroll_result"/></td>
			    </tr>
                </set:list>
				<!--
                <tr>
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1"><strong><div align="center"><set:label name="total_amt_reject"/></div></strong></td>
                  <td class="listcontent1"><strong><div align="center"><set:data name="totalAmount" format="amount"/></div></strong></td>
                  <td colspan="2" class="listcontent1">&nbsp;</td>
			    </tr>
				-->
              </table>
			  <set:assignuser selectFlag='Y'/>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><div align="center">
                      <input id="return" name="return" type="button" value="&nbsp;&nbsp;<set:label name='return' defaultvalue=' return '/>&nbsp;&nbsp;" onClick="doSubmit('return');">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="uploadFile">
                      <input name="date_range" type="hidden" id="date_range" value="<set:data name='date_range'/>">
                      <input name="dateFrom" type="hidden" id="dateFrom" value="<set:data name='dateFrom'/>">
                      <input name="dateTo" type="hidden" id="dateTo" value="<set:data name='dateTo'/>">
                  </div>
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
