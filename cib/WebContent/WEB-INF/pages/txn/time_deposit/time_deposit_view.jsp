<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.time_deposit">
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
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit(arg) {
	if (arg == 'ok') {
		document.form1.ActionMethod.value = 'withdrawTimeDeposit';
	} 
	document.form1.submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/timeDeposit.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.time_deposit" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_view" defaultvalue="MDB Corporate Online Banking > Time Deposit > Time Deposit Detial"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_view" defaultvalue="TIME DEPOSIT DETIAL"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/timeDeposit.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="tdAccount_detial" defaultvalue="tdAccount_detial"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name='account_no' defaultvalue="account_no"/></td>
                <td width="72%" class="content1"><set:data name="ACCOUNT_NO" db='tdAccountByUser'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="currency" defaultvalue="currency"/></td>
                <td class="content1"><set:data name="CUR" db="rcCurrencyCBS"/></td>
              </tr>
              <tr>
                <td class="label1"><set:label name="principal" defaultvalue="principal"/></td>
                <td class="content1"><%--<set:data name="CUR_BAL" format="amount"/>--%>
                	<set:data name="CUR"/><set:data name="ISSUE_AMOUNT" format="amount"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="period" defaultvalue="period"/></td>
                <td class="content1"><set:data name="period"/></td>
              </tr>
              <tr>
                <td class="label1"><set:label name="interest_rate" defaultvalue="interest_rate"/></td>
                <td class="content1"><set:data name='INT_RATE' format='percent'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="interest_accrued" defaultvalue="interest_accrued"/></td>
                <td class="content1"><set:data name="CUR"/><set:data name='interestAccured' format="amount"/></td>
              </tr>
              <tr >
                <td class="label1"><set:label name="value_date" defaultvalue="value_date"/></td>
                <td class="content1">
					<set:if name="REN_DATE" condition="equals" value="00000000">
						<set:data name="OPEN_DATE" format='date'/>
					</set:if>
					<set:if name="REN_DATE" condition="notequals" value="00000000">
						<set:data name="OPEN_DATE" format='date'/>
					</set:if>
				</td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="maturity_date" defaultvalue="maturity_date"/></td>
                <td class="content1"><set:data name='NEXT_MAT_DATE' format='date'/></td>
              </tr>
               <!-- add by lzg 20191018 -->
			 <tr >
			   <td class="label1"><set:label name="Expected_Interest" /></td>
			   <td class="content1"><set:data name='expectedInterest' format='amount'/></td>
			 </tr>
			 <!-- add by lzg end -->
              <tr class="greyline">
                <td class="label1"><set:label name="principal" defaultvalue="principal"/>&nbsp;+&nbsp;<set:label name="interest" defaultvalue="interest"/></td>
                <td class="content1"><set:data name="CUR"/><set:data name='netCreditAmt' format="amount"/></td>
              </tr>
              <!-- add by linrui for add Maturity Instruction 20190424 -->
              <tr >
                <td class="label1"><set:label name="Maturity_Instruction" defaultvalue="Maturity Instruction" rb="app.cib.resource.txn.time_deposit"/></td>
                <td class="content1"><set:data name='instCd' rb='app.cib.resource.txn.timedeposit_inst_cd'/></td>
              </tr>
              <!-- end -->
             <%-- <tr>
                <td class="label1"><set:label name="branch" defaultvalue="branch"/></td>
                <td class="content1"><set:data name='BRANCH' db='branch'/></td>
              </tr>--%>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
              	<!-- modified by lzg 20190621 -->
                <td height="40" colspan="4" class="sectionbutton" style = "text-align: center;">
	                <input type = "button" class = "sectionbutton" value = "<set:label name='return' defaultvalue='return'/>" onClick="postToMainFrame('/cib/accEnquiry.do?ActionMethod=listTimeDeposit','')"/>
					<input id="ActionMethod" name="ActionMethod" type="hidden" value="ok">
				</td>
				<!-- modified by lzg end -->
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
