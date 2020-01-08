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
<link rel="stylesheet" type="text/css" href="/cib/css/dtree.css">
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/dtree.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	
}
function selectNode(arg){
	document.getElementById("corpId").value = arg;
	document.getElementById("form1").submit();
}

</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/accEnquiry.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.account_enquiry" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_credit_account" defaultvalue="MDB Corporate Online Banking > Subsidiaries Accounts > Credit Card" rb="app.cib.resource.enq.subsidiary_account_enquiry"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_credit_account" defaultvalue="CREDIT ACCOUNTS"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/subsidiaryAcc.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" --> <set:messages width="100%" cols="1"/>
		    <set:messages width="100%" cols="1"/>
               <table width="100%" border="0" cellspacing="0" cellpadding="3">
                 <tr>
                   <td colspan="2" class="label1"><div class="dtree">
                       <p><set:label name="corporation_structure" defaultvalue="corporation structure"/><a href="javascript: d.openAll();"><set:label name="view_all" defaultvalue="view all"/></a><a href="javascript: d.closeAll();"><set:label name="close_all" defaultvalue="close all"/></a></p>
                       <script type="text/javascript">
						d = new dTree('d');
						if("Y" == "<set:data name='flag'/>"){
							d.clearCookie();
						}
						d.add("<set:data name='CORP_ID'/>","-1","<set:data name='CORP_NAME'/>(<set:data name='CORP_ID'/>)");
						<set:list name="corpList">
						d.add("<set:listdata name='CORP_ID'/>","<set:listdata name='PARENT_CORP'/>","<set:listdata name='CORP_NAME'/>(<set:listdata name='CORP_ID'/>)","javascript:selectNode('<set:listdata name='CORP_ID'/>');");
						</set:list>
						document.write(d);
						</script>
                     </div></td>
                 </tr>
               </table>
			<set:if name="flag" condition="notequals" value="Y">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td height="40" valign="top" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                      <%--<tr>
                        <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=creditAccountList&fileName=credit_account&columnTitles=<set:label name='Credit_Card_Number'/>,<set:label name='Description_Remark'/>,<set:label name='Original_Currency'/>,<set:label name='Last_Statement_Balance'/>,<set:label name='Available_Limit'/>,<set:label name='Due_Date'/>,<set:label name='Minimum_Payment_Due'/>&columnNames=CREDIT_CARD_NO,CREDIT_CARD_NO||db@accountDesc||specialChar@yes,CURRENCY_CODE||db@currencyCBS,CURRENT_BALANCE||format@amountcc,AVAILABLE_BALANCE||format@amount,PAYMENT_DUE_DATE||format@date,MIN_PAYMENT||format@amount"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>
                      </tr>
                    --%></table></td>
                </tr>
              </table>
             </set:if>
             <set:if name="flag" condition="notequals" value="Y">
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td align="left" class="listheader1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                  <td align="left" class="listheader1" style="width:25%"><set:label name="Description_Remark" defaultvalue="Description/Personal Remark"/></td>
                  <td align="center" class="listheader1"><set:label name="Online_Statement" defaultvalue="Online Statement"/></td>
                  
                  <td align="center" class="listheader1"><set:label name="Available_Limit" defaultvalue="Available Limit"/></td>
                  <td align="center" class="listheader1"><set:label name="Last_Statement_Balance" defaultvalue="Last Statement Balance"/></td>
                  <td align="center" class="listheader1"><set:label name="Due_Date" defaultvalue="Due Date"/></td>
                  <td align="center" class="listheader1"><set:label name="Minimum_Payment_Due" defaultvalue="Minimum Payment Due"/></td>
                  
                </tr>
                <set:list name="creditAccountList" showNoRecord="YES"> <set:listif name='ACCOUNT_STATUS' condition='notequals' value='4'>
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td align="left" class="listcontent1"><a onClick="toTargetFormgoToTarget('/cib/accEnquiry.do?ActionMethod=viewCreditAccount&range=1&accountNo=<set:listdata name='CREDIT_CARD_NO'/>&fromPage=sub')" href="#"> <set:listif name='SPECIAL_ACC_FLAG' condition='equals' value='Y'>*</set:listif> <set:listdata name='CREDIT_CARD_NO'/></a></td>
                  <td align="left" class="listcontent1"><set:listdata name="CREDIT_CARD_NO" db="accountDesc"/></td>
                  <td align="center" class="listcontent1"><set:listdata name="CREDIT_CARD_NO" db="statementForList"/></td>
                  <td align="center" class="listcontent1"><set:listdata name="AVAILABLE_BALANCE"/>
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
             </set:if>
             <set:if name="flag" condition="notequals" value="Y">
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              </set:if>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <set:if name='SPECIAL_ACC_FLAG' condition='equals' value='Y'>
                <tr>
                  <td class="label1"><set:label name="*_account" defaultvalue="* means special account"/></td>
                </tr>
                </set:if>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" class="sectionbutton"><input id="ActionMethod" name="ActionMethod" type="hidden" value="listCreditAccount"></td>
                  <td height="40" class="sectionbutton"><input id="equivalentCCY" name="equivalentCCY" type="hidden" value="<set:data name='equivalentCCY'/>">
                   <input id="corpId" name="corpId" type="hidden" ></td>
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
