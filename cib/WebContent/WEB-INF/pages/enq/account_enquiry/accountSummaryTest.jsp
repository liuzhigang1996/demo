<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.enq.account_enquiry">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>BANK Corporation i-Banking Service</title>
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
function showEquivalent(){
	document.getElementById("ActionMethod").value = "listAccountSummary";
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/accEnquiry.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.account_enquiry" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_sum" defaultvalue="BANK Online Banking > Account Enquiry > Current Account"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_sum" defaultvalue="CURRENT ACCOUNT(S) ENQUIRY"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/accEnquiry.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<set:messages width="100%" cols="1"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td height="40" valign="top" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                  <%--<tr>
                     <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=csvList&fileName=account_summary&columnTitles=<set:label name='Account_Type'/>,<set:label name='Original_Currency'/>,<set:label name='Amount'/>,,<set:label name='Equivalent_In'/>  <set:data name='eqCcy' defaultvalue='MOP'/>&columnNames=showAccType,CURRENCY_CODE||db@currencyCBS,TOTAL_BALANCE||format@amount,SUBTOTAL_TITLE,TOTAL_LCY_BALANCE||format@amount,SUBTOTAL||format@amount"><set:label name="Download" defaultvalue="Download" /><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>
                    
                  </tr>
                --%></table></td>
                <td align="right" valign="top" class="content1"><b>
                  <set:label name="Enq_Amount_In" defaultvalue="Equivalent amount in: "/></b>
                    <select name="eqCcy" id="eqCcy" onChange="showEquivalent();">
                      <set:rblist file="app.cib.resource.enq.equivalent_ccy">
                        <set:rboption name="eqCcy"/>
                      </set:rblist>
                  </select></td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="listheader1">&nbsp;</td>
                <td width="20%" class="listheader1"><set:label name="Account_Type" defaultvalue="Account Type"/></td>
                <td width="20%" align="right" class="listheader1"><set:label name="Original_Currency" defaultvalue="Original Currency"/></td>
                <td width="25%" align="right" class="listheader1"><set:label name="Amount" defaultvalue="Amount"/></td>
                <td width="10%" align="right" class="listheader1">&nbsp;</td>
                <td width="25%" align="right" class="listheader1"><set:label name="Equivalent_In" defaultvalue="Equivalent in "/>&nbsp;
                  <set:data name="eqCcy" defaultvalue="MOP"/></td>
                <td class="listheader1">&nbsp;</td>
              </tr>
			  <set:if name="showCurrentAccount" condition="equals" value="1">
			  <set:list name="currentAccountList">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1">&nbsp;</td>
                <td class="listcontent1"><set:listif name="@INDEX"  condition="equals" value="0"><a onClick="toTargetFormgoToTarget('/cib/accEnquiry.do?ActionMethod=listCurrentAccount')" href="#"><set:label name="Current_Account" defaultvalue="Current Account"/></a></set:listif></td>
                <td align="center" class="listcontent1"><set:listdata name="CURRENCY_CODE" db="currencyCBS"/></td>
                <td align="right" class="listcontent1"><set:listdata name="TOTAL_BALANCE" format="amount"/></td>
                <td align="right" class="listcontent1">&nbsp;</td>
                <td align="right" class="listcontent1"><set:listdata name="TOTAL_LCY_BALANCE" format="amount"/></td>
                <td class="listcontent1">&nbsp;</td>
              </tr>
			  </set:list>
              <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                <td class="listcontent1">&nbsp;</td>
                <td class="listcontent1" width="20%">&nbsp;</td>
                <td class="listcontent1" width="20%">&nbsp;</td>
                <td class="listcontent1" width="25%">&nbsp;</td>
                <td align="center"  class="listcontent1" width="10%"><strong><set:label name="Sub_Total" defaultvalue="Sub-Total"/></strong></td>
                <td width="25%" align="right" class="listcontent1"><set:data name="CA_SubTotal" format="amount"/></td>
                <td class="listcontent1">&nbsp;</td>
              </tr>
		    </set:if>
		    <set:if name="showNothing" condition="equals" value="1">
			  <set:list name="currentAccountList" showNoRecord="YES">
			  </set:list>
			</set:if>
            </table>
		  <set:if name="showTimeDeposit" condition="equals" value="1">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <set:list name="timeDepositList">
              <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                <td class="listcontent1">&nbsp;</td>
                <td class="listcontent1"><set:listif name="@INDEX"  condition="equals" value="0"><a onClick="toTargetFormgoToTarget('/cib/accEnquiry.do?ActionMethod=listTimeDeposit')" href="#"><set:label name="Time_Deposit" defaultvalue="Time Deposit"/></a></set:listif></td>
                <td align="center" class="listcontent1"><set:listdata name="CURRENCY_CODE" db="currencyCBS"/></td>
                <td align="right" class="listcontent1"><set:listdata name="TOTAL_BALANCE" format="amount"/></td>
                <td align="right" class="listcontent1">&nbsp;</td>
                <td align="right" class="listcontent1"><set:listdata name="TOTAL_LCY_BALANCE" format="amount"/></td>
                <td class="listcontent1">&nbsp;</td>
              </tr>
			  </set:list>
              <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                <td class="listcontent1">&nbsp;</td>
                <td class="listcontent1" width="20%">&nbsp;</td>
                <td class="listcontent1" width="20%">&nbsp;</td>
                <td class="listcontent1" width="25%">&nbsp;</td>
                <td align="center" class="listcontent1" width="10%"><strong><set:label name="Sub_Total" defaultvalue="Sub-Total"/></strong></td>
                <td width="25%" align="right" class="listcontent1"><set:data name="TD_SubTotal" format="amount"/></td>
                <td class="listcontent1">&nbsp;</td>
              </tr>
            </table>
		  </set:if>
		  <set:if name="showOverdraft" condition="equals" value="1">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <set:list name="overdraftAccountList">
              <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                <td class="listcontent1">&nbsp;</td>
                <td class="listcontent1"><set:listif name="@INDEX"  condition="equals" value="0"><a onClick="toTargetFormgoToTarget('/cib/accEnquiry.do?ActionMethod=listOverdraftAccount')" href="#"><set:label name="Overdraft_Account" defaultvalue="Overdraft Account"/></a></set:listif></td>
                <td align="center" class="listcontent1"><set:listdata name="CURRENCY_CODE" db="currencyCBS"/></td>
                <td align="right" class="listcontent1"><set:listdata name="TOTAL_BALANCE" format="amount"/></td>
                <td align="right" class="listcontent1">&nbsp;</td>
                <td align="right" class="listcontent1"><set:listdata name="TOTAL_LCY_BALANCE" format="amount"/></td>
                <td class="listcontent1">&nbsp;</td>
              </tr>
			  </set:list>
              <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                <td class="listcontent1">&nbsp;</td>
                <td class="listcontent1" width="20%">&nbsp;</td>
                <td class="listcontent1" width="20%">&nbsp;</td>
                <td class="listcontent1" width="25%">&nbsp;</td>
                <td align="center" class="listcontent1" width="10%"><strong><set:label name="Sub_Total" defaultvalue="Sub-Total"/></strong></td>
                <td width="25%" align="right" class="listcontent1"><set:data name="OA_SubTotal" format="amount"/></td>
                <td class="listcontent1">&nbsp;</td>
              </tr>
            </table>
		  </set:if>
		  <set:if name="showLoanAccount" condition="equals" value="1">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <set:list name="loanAccountList">
              <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                <td class="listcontent1">&nbsp;</td>
                <td class="listcontent1"><set:listif name="@INDEX"  condition="equals" value="0"><a onClick="toTargetFormgoToTarget('/cib/accEnquiry.do?ActionMethod=listLoanAccount')" href="#"><set:label name="Loan_Account" defaultvalue="Loan Account"/></a></set:listif></td>
                <td align="center" class="listcontent1"><set:listdata name="CURRENCY_CODE" db="currencyCBS"/></td>
                <td align="right" class="listcontent1"><set:listdata name="TOTAL_BALANCE" format="amount"/></td>
                <td align="right" class="listcontent1">&nbsp;</td>
                <td align="right" class="listcontent1"><set:listdata name="TOTAL_LCY_BALANCE" format="amount"/></td>
                <td class="listcontent1">&nbsp;</td>
              </tr>
			  </set:list>
              <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                <td class="listcontent1">&nbsp;</td>
                <td class="listcontent1" width="20%">&nbsp;</td>
                <td class="listcontent1" width="20%">&nbsp;</td>
                <td class="listcontent1" width="25%">&nbsp;</td>
                <td align="center" class="listcontent1" width="10%"><strong><set:label name="Sub_Total" defaultvalue="Sub-Total"/></strong></td>
                <td width="25%" align="right" class="listcontent1"><set:data name="LA_SubTotal" format="amount"/></td>
                <td class="listcontent1">&nbsp;</td>
              </tr>
            </table>
		  </set:if>
		   <set:if name="showSavingAccount" condition="equals" value="1">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <set:list name="savingAccountList">
              <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                <td class="listcontent1">&nbsp;</td>
                <td class="listcontent1"><set:listif name="@INDEX"  condition="equals" value="0"><a onClick="toTargetFormgoToTarget('/cib/accEnquiry.do?ActionMethod=listSavingAccount')" href="#"><set:label name="Saving_Account" defaultvalue="Saving Account"/></a></set:listif></td>
                <td align="center" class="listcontent1"><set:listdata name="CURRENCY_CODE" db="currencyCBS"/></td>
                <td align="right" class="listcontent1"><set:listdata name="TOTAL_BALANCE" format="amount"/></td>
                <td align="right" class="listcontent1">&nbsp;</td>
                <td align="right" class="listcontent1"><set:listdata name="TOTAL_LCY_BALANCE" format="amount"/></td>
                <td class="listcontent1">&nbsp;</td>
              </tr>
			  </set:list>
              <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                <td class="listcontent1">&nbsp;</td>
                <td class="listcontent1" width="20%">&nbsp;</td>
                <td class="listcontent1" width="20%">&nbsp;</td>
                <td class="listcontent1" width="25%">&nbsp;</td>
                <td align="center" class="listcontent1" width="10%"><strong><set:label name="Sub_Total" defaultvalue="Sub-Total"/></strong></td>
                <td width="25%" align="right" class="listcontent1"><set:data name="SA_SubTotal" format="amount"/></td>
                <td class="listcontent1">&nbsp;</td>
              </tr>
            </table>
		  </set:if>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="sectionbutton">
                  <input id="ActionMethod" name="ActionMethod" type="hidden" value="listAccountSummary"></td>
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
