<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.enq.account_enquiry">
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
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/accEnquiryBank.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.account_enquiry" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_detail_bank" defaultvalue="BANK Online Banking > Account Enquiry > Loan Account"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_detail" defaultvalue="LOAN ACCOUNT DETAIL"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/accEnquiryBank.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
					</td>
				</tr>
			</table>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="content1" width="30%"><set:label name="Corp_Id" rb="app.cib.resource.bnk.corporation"/></td>
                    <td class="content1"><set:data name='corpId'/></td>
                  </tr>
                  <tr>
                    <td class="content1"><set:label name="Corp_Name" rb="app.cib.resource.bnk.corporation"/></td>
                    <td class="content1"><set:data name='corpName'/></td>
                  </tr>
                </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="groupinput" colspan="2"><set:label name="Account_Detial" defaultvalue="Account Detial"/></td>
              </tr>
              <tr>
                <td class="content1" width="30%"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                <td class="content1"><set:data name="ACCOUNT_NO"/></td>
              </tr>
              <%--<tr class="greyline">
                <td class="content1" width="30%"><set:label name="Currency" defaultvalue="Currency"/></td>
                <td class="content1"><set:data name="CURRENCY_CODE" db="currencyCBS"/></td>
              </tr>
              --%>
              <!-- add by lzg for GAPMC-EB-001-0058 -->
              <tr class="greyline">
                <td class="content1" width="30%"><set:label name="Currency" defaultvalue="Currency"/></td>
                <td class="content1"><set:data name="CURRENCY_CODE" db="currencyCBS"/></td>
              </tr>
              <!-- add by lzg end -->
              <tr>
                <td class="content1" width="30%"><set:label name="Initial_Loan_Amount" defaultvalue="Initial Loan Amount"/></td>
                <td class="content1"><set:data name="LOAN_FACE_AMOUNT" format="amount"/></td>
              </tr>
              <tr class="greyline">
                <td class="content1" width="30%"><set:label name="Outstanding_Balance" defaultvalue="Outstanding Balance"/></td>
                <td class="content1"><set:data name="PRINCIPAL_BALANCE" format="amount"/></td>
              </tr>
              <tr>
                <td class="content1" width="30%"><set:label name="Start_Date" defaultvalue="Start Date"/></td>
                <td class="content1"><set:data name="LOAN_OPEN_DATE" format="date"/></td>
              </tr>
              <tr class="greyline">
                <td class="content1" width="30%"><set:label name="Maturity_Date" defaultvalue="Maturity Date"/></td>
                <td class="content1"><set:data name="NEXT_MATURITY_DATE" format="date"/></td>
              </tr>
              <tr>
                <td class="content1" width="30%"><set:label name="Payment_Account" defaultvalue="Payment Account"/></td>
                <td class="content1"><set:data name="PAYMENT_ACCOUNT"/></td>
              </tr>
              <tr class="greyline">
                <td class="content1" width="30%"><set:label name="Interest_Rate" defaultvalue="Interest Rate"/></td>
                <td class="content1"><set:data name="INTEREST_RATE"/>%</td>
              </tr>
              <tr>
                <td class="content1" width="30%"><set:label name="Next_Payment_Date" defaultvalue="Next Payment Date"/></td>
                <td class="content1"><set:data name="NEXT_PAYMENT_DUE_DATE" format="date"/></td>
              </tr>
              <!-- modify by wcc 20180320 -->
              <!--<tr class="greyline">
                <td class="content1" width="30%"><set:label name="Next_Payment_Amount" defaultvalue="Next Payment Amount : "/></td>
                <td class="content1"><set:data name="NEXT_PAYMENT_AMOUNT" format="amount"/></td>
              </tr>
              <tr>
                <td class="content1" width="30%"><set:label name="Past_Due_Amount" defaultvalue="Past Due Amount : "/></td>
                <td class="content1"><set:data name="TOTAL_PAYMENT_PAST_DUE" format="amount"/></td>
              </tr>
              <tr class="greyline">
                <td class="content1" width="30%"><set:label name="Last_Payment_Date" defaultvalue="Last Payment Date : "/></td>
                <td class="content1"><set:data name="LAST_PAYMENT_DATE" format="date"/></td>
              </tr>
              <tr>
                <td class="content1" width="30%"><set:label name="Last_Payment_Amount" defaultvalue="Last Payment Amount : "/></td>
                <td class="content1"><set:data name="LAST_PAYMENT_AMOUNT" format="amount"/></td>
              </tr>
            -->
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
              <!-- modified by lzg 20190621 -->
                <!--<td height="40" colspan="2" class="sectionbutton"><input id="ActionMethod" name="ActionMethod" type="hidden" value="loanAccountHistory"></td>
                -->
                <td height="40" colspan="4" class="sectionbutton" style = "text-align: center;">
                <input type = "button" class = "sectionbutton" value = "<set:label name='buttonReturn' defaultvalue='return'/>" onClick="postToMainFrame('/cib/accEnquiryBank.do?ActionMethod=listLoanAccount','')"/>
                <input id="ActionMethod" name="ActionMethod" type="hidden" value="loanAccountHistory">
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
