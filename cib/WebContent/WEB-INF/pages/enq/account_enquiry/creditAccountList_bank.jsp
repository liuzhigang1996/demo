<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
  
}
function changeDoc(arg) {
  if (arg == 'sum') {
    window.location.href='/cib/accEnquiryBank.do?ActionMethod=listAccountSummary';
  } else if (arg == 'ca') {
    window.location.href='/cib/accEnquiryBank.do?ActionMethod=listCurrentAccount';
  } else if (arg == 'td') {
    window.location.href='/cib/accEnquiryBank.do?ActionMethod=listTimeDeposit';
  } else if (arg == 'oa') {
    window.location.href='/cib/accEnquiryBank.do?ActionMethod=listOverdraftAccount';
  } else if (arg == 'la') {
    window.location.href='/cib/accEnquiryBank.do?ActionMethod=listLoanAccount';
  } else if (arg == 'sa') {
    window.location.href='/cib/accEnquiryBank.do?ActionMethod=listSavingAccount';
  } else if (arg == 'out') {
    window.location.href='/cib/approve.do?ActionMethod=statusEnquiryBank&procStatus=P';
  } else if (arg == 'cc') {
	    window.location.href='/cib/accEnquiryBank.do?ActionMethod=listCreditAccount';
  }
  setFormDisabled("form1");
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/accEnquiryBank.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.account_enquiry" -->
</head>
<body class="body1" onLoad="pageLoad();">

<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
          <set:label name="navigationTitle_cc_bank" defaultvalue="BANK Online Banking > Account Enquiry > Credit Card"/>
          <!-- InstanceEndEditable --></td>
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
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="40">
  <tr>
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_credit_account" defaultvalue="CREDIT ACCOUNTS"/><!-- InstanceEndEditable --></td>
    <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" bgcolor="white"><img src="/cib/images/shim.gif" width="1" height="1"></td>
        </tr>
      </table></td>
  </tr>
</table>
</div>

 




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
        <!-- InstanceBeginEditable name="sectioncontent" --> <set:messages width="100%" cols="1"/>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td height="40" valign="top" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                      <%--<tr>
                        <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=creditAccountList&fileName=credit_account&columnTitles=<set:label name='Credit_Card_Number'/>,<set:label name='Description_Remark'/>,<set:label name='Original_Currency'/>,<set:label name='Last_Statement_Balance'/>,<set:label name='Available_Limit'/>,<set:label name='Due_Date'/>,<set:label name='Minimum_Payment_Due'/>&columnNames=CREDIT_CARD_NO,CREDIT_CARD_NO||db@accountDesc||specialChar@yes,CURRENCY_CODE||db@currencyCBS,CURRENT_BALANCE||format@amountcc,AVAILABLE_BALANCE||format@amount,PAYMENT_DUE_DATE||format@date,MIN_PAYMENT||format@amount"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>
                      </tr>
                    --%></table></td>
                    <!--
                  <td align="right" valign="top" class="content1"><b> <set:label name="Enq_Amount_In" defaultvalue="Equivalent amount in: "/></b>
                    <select name="eqCcy" id="eqCcy" onChange="showEquivalent(this.value);">
                      <set:rblist file="app.cib.resource.enq.equivalent_ccy"> <set:rboption name="eqCcy"/> </set:rblist>
                    </select></td>-->
                </tr>
              </table>
              
              
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			   <tr>
			     <td width="28%" class="label1"><set:label name="Corp_Id" rb="app.cib.resource.bnk.corporation"/></td>
			     <td width="72%" class="content1"><set:data name='corpId'/></td>
			   </tr>
			   <tr>
			     <td class="label1"><set:label name="Corp_Name" rb="app.cib.resource.bnk.corporation"/></td>
			     <td class="content1"><set:data name='corpName'/></td>
			   </tr>
			 </table>
              <table border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('sum');">
                            <set:label name="functionTitle_sum_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('ca');">
                            <set:label name="functionTitle_ca_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('sa');">
                            <set:label name="functionTitle_sa_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('td');">
                            <set:label name="functionTitle_td_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('oa');">
                            <set:label name="functionTitle_oa_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('la');">
                            <set:label name="functionTitle_la_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>

                      <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_o"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_o"><a href="#" onClick="changeDoc('cc');">
                            <set:label name="functionTitle_cc_CR"/>
                            </a></td>
                          <td class="rtab1_o"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>

                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('out');">
                            <set:label name="functionTitle_out_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                  </tr>
                </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td align="left" class="listheader1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                  <td align="left" class="listheader1"><set:label name="Description_Remark" defaultvalue="Description/Personal Remark"/></td>
                  <td align="center" class="listheader1"><set:label name="Online_Statement" defaultvalue="Online Statement"/></td>
                  
                  <td align="center" class="listheader1"><set:label name="Available_Limit" defaultvalue="Available Limit"/></td>
                  <td align="center" class="listheader1"><set:label name="Last_Statement_Balance" defaultvalue="Last Statement Balance"/></td>
                  <td align="center" class="listheader1"><set:label name="Due_Date" defaultvalue="Due Date"/></td>
                  <td align="center" class="listheader1"><set:label name="Minimum_Payment_Due" defaultvalue="Minimum Payment Due"/></td>
                  
                </tr>
                <set:list name="creditAccountList" showNoRecord="YES"> <set:listif name='ACCOUNT_STATUS' condition='notequals' value='4'>
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td align="left" class="listcontent1"><a onClick="toTargetFormgoToTarget('/cib/accEnquiryBank.do?ActionMethod=viewCreditAccount&range=1&accountNo=<set:listdata name='CREDIT_CARD_NO'/>')" href="#"> <set:listif name='SPECIAL_ACC_FLAG' condition='equals' value='Y'>*</set:listif> <set:listdata name='CREDIT_CARD_NO'/></a></td>
                  <td align="left" class="listcontent1"><set:listdata name="CREDIT_CARD_NO" db="accountDesc"/></td>
                  <td align="center" class="listcontent1"><set:listdata name="CREDIT_CARD_NO" db="statementForList"/></td>
                  <td align="center" class="listcontent1"><set:listdata name="AVAILABLE_BALANCE" format="amount"/>
                    </td>
                  <td align="right" class="listcontent1"><set:listdata name="CURRENT_BALANCE" format="amountcc"/>
                    </td>
                  <td align="right" class="listcontent1"><set:listif name="PAYMENT_DUE_DATE" condition="equals" value="00000000">-</set:listif>
                  <set:listif name="PAYMENT_DUE_DATE" condition="notequals" value="00000000">
                  <set:listdata name="PAYMENT_DUE_DATE" format="date"/></set:listif></div></td>
                  <td align="right" class="listcontent1"><set:listdata name="MIN_PAYMENT" format="amount"/></div></td>
                </tr>
                </set:listif> </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <set:if name='SPECIAL_ACC_FLAG' condition='equals' value='Y'>
                <tr>
                  <td class="label1"><set:label name="*_account" defaultvalue="* means special account"/></td>
                </tr>
                </set:if>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" class="sectionbutton"><input id="ActionMethod" name="ActionMethod" type="hidden" value="listCurrentAccount"></td>
                  <td height="40" class="sectionbutton"><input id="equivalentCCY" name="equivalentCCY" type="hidden" value="<set:data name='equivalentCCY'/>"></td>
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
