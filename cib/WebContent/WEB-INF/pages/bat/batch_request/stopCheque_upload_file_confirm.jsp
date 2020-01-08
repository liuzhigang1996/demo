<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.stop_check_request">
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
function doSubmit(arg) {
	if (arg == 'confirm') {
		document.form1.ActionMethod.value = 'uploadFileConfirm';
		document.getElementById("form1").submit();
		setFormDisabled("form1");
	} else if (arg == 'return'){
		document.form1.ActionMethod.value = 'uploadFileCancel';
		document.getElementById("form1").submit();
		setFormDisabled("form1");
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/stopChequeBatchRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.stop_check_request" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitleBatch" defaultvalue="MDB Corporate Online Banking > Payroll > Upload a Payroll File"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitleBatch" defaultvalue="UPLOAD A PAYROLL FILE"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/stopChequeBatchRequest.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="general_payment_check" form="form1" file="payroll" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3" id="round">
                <tr>
                  <td colspan="2" class="groupinput"><set:label name="Confirmation" defaultvalue="Confirmation"/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="no_of_record_file" defaultvalue="no_of_record_file"/></td>
                  <td class="content1"><set:data name="TOTAL_NUMBER" /></td>
                </tr>
                <tr >
                  <td  width="28%" class="label1"><set:label name="no_of_record" defaultvalue="no_of_record"/></td>
                  <td  width="72%" class="content1"><set:data name="allCount" /></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="right_record_no" defaultvalue="right_record_no"/></td>
                  <td class="content1"><set:data name="rightRecordNo" /></td>
                </tr>
                <tr >
                  <td class="label1"><set:label name="err_record_no" defaultvalue="err_record_no"/></td>
                  <td class="content1"><set:data name="errRecordNo" /></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupinput">
				  	<set:if name="errRecordNo" value="0" condition="notequals">
						<set:label name="err_record_below" defaultvalue="err_record_below"/> 
					</set:if> 
					<set:if name="rightRecordNo" value="0" condition="notequals"> 
						<set:label name="nor_record_below" defaultvalue="nor_record_below"/> 
					</set:if> :
				  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1"><set:label name="line_in_file" defaultvalue="line_in_file"/></td>
				  <td align="right" class="listheader1"><set:label name="amt" defaultvalue="amt"/></td>
                  <td class="listheader1"><set:label name="payee_acc" defaultvalue="CURRENT ACCOUNT"/></td>
				  <td class="listheader1"><set:label name="Cheque_Number" defaultvalue="Cheque Number"/></td>
                  <set:if name="errRecordNo" value="0" condition="notequals">
				  	<td class="listheader1"><set:label name="problem_desc" defaultvalue="problem_desc"/></td>
				  </set:if>
                </tr>
                <set:list name="recList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                   <td class="listcontent1"><set:listdata name="LINE_NO" /></td>
				  <td align="right" class="listcontent1"><set:listdata name="AMOUNT" format="amount"/></td>
                  <td class="listcontent1"><set:listdata name="CURRENT_ACCOUNT" /></td>
				  <td class="listcontent1"><set:listdata name="CHEQUE_NUMBER" /></td>
                  <set:if name="errRecordNo" value="0" condition="notequals">  
				  	<td class="listcontent1"><set:listdata name="PROBLEM_TYPE" rbs="app.cib.resource.bat.file_err_type"/></td>
				  </set:if>
                </tr>
                </set:list>
              </table>
			  <set:assignuser selectFlag='Y'/>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><div align="center">
				  		<set:if name="errFlag" condition="equals" value="N">
                      <input id="confirm" name="confirm" type="button" value="&nbsp;&nbsp;<set:label name='Confirm' defaultvalue=' Confirm '/>&nbsp;&nbsp;" onClick="doSubmit('confirm')">
                       </set:if>
					  <input id="return" name="return" type="button" value="&nbsp;&nbsp;<set:label name='Return' defaultvalue=' Return '/>&nbsp;&nbsp;" onClick="doSubmit('return')">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="uploadFile">
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
