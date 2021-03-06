<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.cash_order_request">
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
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit()
{
	if(validate_cash_order_history(document.getElementById("form1"))){
		setFormDisabled('form1');
		document.getElementById("form1").submit();
	}
}
function showTree() {
	var url="/cib/corporation.do?ActionMethod=listCorpForTree";
	var popWin1= window.open(url,"temp1","toolbar=no,location=no,top=50,left=50,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=530,height=400");
	popWin1.focus();
}
function changeCorp(arg) {
	putFieldValues("toCorp", arg);
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
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/cashOrderRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.cash_order_request" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitleList" defaultvalue="BANK Online Banking >Corp Fund Allocation > Corporation Transfer History"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitleList" defaultvalue="CORPORATION TRANSFER HISTORY"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/cashOrderRequest.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="cash_order_history" form="form1" file="cash_order" />                        </td>
                      </tr>
            </table>
			<table border="0" cellpadding="0" cellspacing="0">
               <tr>
               <td>
               <!-- mod by linrui for BANK without this function 20180314 -->
			   <%--<table border="0" cellpadding="0" cellspacing="0">
			     <tr>
			   <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
        <td class="tab1_c">
		<a onClick="toTargetFormgoToTarget('/cib/bankRequest.do?ActionMethod=listLoad')" href="#"><set:label name="Bank_Draft" defaultvalue="Bank Draft Request History"/></a></td>
			   <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
		</tr></table>
		--%></td>
			   <td>
			   <table border="0" cellpadding="0" cellspacing="0">
			     <tr>
			   <td class="ltab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
        <td class="tab1_o">
		<set:label name="Cashier_Order" defaultvalue="Cashier Order Request History"/></a></td>
			   <td class="rtab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
		</tr></table>
			   </td>
			   <td>
			   <table border="0" cellpadding="0" cellspacing="0">
			     <tr>
			   <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
        <td class="tab1_c">
		<a onClick="toTargetFormgoToTarget('/cib/stopChequeRequest.do?ActionMethod=listLoad')" href="#"><set:label name="Stop_Cheque" defaultvalue="Stop Cheque Request History"/></a></td>
			   <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
		</tr></table>
			    </td>
				<td>
				<!-- mod by linrui for BANK without this function 20180314 -->
			   <%--<table border="0" cellpadding="0" cellspacing="0">
			     <tr>
			   <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>	   
        <td class="tab1_c">
		<a onClick="toTargetFormgoToTarget('/cib/protectionChequeRequest.do?ActionMethod=listLoad')" href="#"><set:label name="Cheque_Protection" defaultvalue="Cheque Protection Request History"/></a></td>
		
			   <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
		</tr></table>
			    --%></td>
			   </tr>
            </table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="label1"><set:label name="From_Account" defaultvalue="Account Number"/></td>
                <td class="content1" ><select id="fromAccount" name="fromAccount" nullable="0">
				<option selected value=" "><set:label name="Select_Account_Label" defaultvalue="----- Select an Account ------"/></option>
					<set:rblist db="caoasaAccountByUser">
                    <set:rboption/>
					</set:rblist>
                  </select>	
				</td>
              </tr>
             <tr class="greyline">
                <td width="28%" height="39" class="label1"><set:label name="payment_period" defaultvalue="payment_period"/></td>
                <td width="72%" class="content1" colspan="2">
               	  <span class="content">
               	  <input id="date_range" name="date_range" type="radio" value="all" checked onClick="changeRange('all')">
               	  </span><set:label name="all" defaultvalue="all"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1">&nbsp;</td>
                <td width="72%" class="content1"><span class="content">
                	<input id="date_range" name="date_range" type="radio" value="range" onClick="changeRange('range')">
                	</span>
					<set:label name="date_from" defaultvalue="date from"/>
                 <input id="dateFrom" name="dateFrom" type="text" value="" size="12" maxlength="10" disabled="disabled"> 
                <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" >&nbsp;&nbsp;&nbsp;&nbsp;
                <set:label name="date_to" defaultvalue="date to"/>&nbsp;&nbsp;&nbsp;&nbsp;<input id="dateTo" name="dateTo" type="text" value="" size="12" maxlength="10" disabled="disabled"> 
                <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" >
				<select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));">
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
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
					<input id="ok" name="ok" type="button" value="&nbsp;&nbsp;<set:label name='search' defaultvalue=' search '/>&nbsp;&nbsp;" onClick="doSubmit('listHistory')">
				  	<input id="ActionMethod" name="ActionMethod" type="hidden" value="listHistory">
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
