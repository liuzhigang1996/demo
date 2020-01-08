<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.sys.auth_pref">
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
	var allTxnTypeStatus = '<set:data name="allTxnTypeStatus"/>';
	if (allTxnTypeStatus == '0' || allTxnTypeStatus == '1') {
		document.form1.allTxnType.checked = true;
	}
	document.getElementById("allTxnType").onclick();
}
function disLink(obj){
     if(obj.checked ){
	    document.getElementById("showTxnType").style.display = "none";
	    document.getElementById("showStatusAll").style.display = "block";		
	 } else {
	    document.getElementById("showTxnType").style.display = "block";
	    document.getElementById("showStatusAll").style.display = "none";		
	 }
}
//added by xyf 20090928
function doSetLoad(varCorpId, varCcy, varTxnType){
	var tempId = escape(varCorpId);
	///cib/authorizationPreference.do?ActionMethod=setLoad&corpId=<set:data name='corpId'/>&currency=MOP&txnType=ALL
	window.document.location.href="/cib/authorizationPreference.do?ActionMethod=setLoad&corpId=" + tempId + "&currency=" + varCcy + "&txnType=" + varTxnType;
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/authorizationPreference.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.auth_pref" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="MDB Corporate Online Banking > Corp Preferences > Set Authorization Preference"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="Set Authorization Preference"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/authorizationPreference.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <set:messages width="100%" cols="1" />
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Corp_Info" defaultvalue="Corporation Information"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/></td>
                <td width="72%" class="content1"><set:data name='corpId'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Corp_Name" defaultvalue="Company Name"/></td>
                <td class="content1"><set:data name='corpName'/></td>
              </tr>             
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Select_currency" defaultvalue="Select a currency type to set up authorization rules"/></td>
              </tr>
              <tr>
                <td class="label1">
                    <input name="allTxnType" type="checkbox" id="allTxnType" onClick="disLink(this)" value="checked">
                  	<set:label name="All_Txntype" defaultvalue="All Transaction Type"/>
				</td>
                <td class="content1">
				<!--
				<a onClick="toTargetFormgoToTarget('/cib/authorizationPreference.do?ActionMethod=setLoad&corpId=<set:data name='corpId'/>&currency=MOP&txnType=ALL')" href="#"><div name="showStatusAll" id="showStatusAll"><set:data name='allTxnTypeStatus' rb="app.cib.resource.sys.pref_status" /></div></a>
				-->
				 <a href="#" onClick="doSetLoad('<set:data name='corpId'/>', 'MOP', 'ALL');">
                  <div name="showStatusAll" id="showStatusAll"><set:data name='allTxnTypeStatus' rb="app.cib.resource.sys.pref_status" /></div></a>
				</td>
			  </tr>
            </table>
            <table id="showTxnType" width="100%" border="0" cellpadding="0" cellspacing="0">			  
              <tr >
                <td class="groupinput" width="95%" ><set:label name="Txn_Type" defaultvalue="Transfer Type"/></td>
			  <set:list name="List_TRANSFER_OWN_ACCOUNT">
                <td class="groupinput"></td>
			  </set:list>
              </tr>
              <tr>
                <td class="label1"><!-- /*Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin */ --><!--<set:label name="Accounts_BANK" defaultvalue="Transfer in BANK"/>--><set:label name="TO_MY_ACCOUNT" defaultvalue="To My Account"/><!-- /*Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end */ --></td>
			  <set:list name="List_TRANSFER_OWN_ACCOUNT">
                <td class="content1">
				<!--
				<a onClick="toTargetFormgoToTarget('/cib/authorizationPreference.do?ActionMethod=setLoad&corpId=<set:data name='corpId'/>&currency=<set:listdata name='cur'/>&txnType=TRANSFER_BANK')" href="#"><set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				-->
				<a href="#" onClick="doSetLoad('<set:data name='corpId'/>', '<set:listdata name='cur'/>', 'TRANSFER_OWN_ACCOUNT');">
                  <set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				</td>
			  </set:list>
              </tr>
             <!-- /* Add by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin */ -->
              <tr class="greyline">
                <td class="label1"><set:label name="TO_MDB_BANK_ACCOUNT" defaultvalue="To MDB Account"/></td>
			  <set:list name="List_TRANSFER_BANK">
                <td class="content1">

				<a href="#" onClick="doSetLoad('<set:data name='corpId'/>', '<set:listdata name='cur'/>', 'TRANSFER_BANK');">
				<set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				</td>
			  </set:list>
              </tr>
              <!-- /* Add by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end */ -->
              
              <tr>
                <td class="label1"><!-- /* Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin */ --><!--<set:label name="Accounts_Macau" defaultvalue="Transfer_in_Macau"/>--><set:label name="TO_LOCAL_BANK_ACCOUNT" defaultvalue="To Local Bank Account"/><!-- /* Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end */ --></td>
			  <set:list name="List_TRANSFER_MACAU">
                <td class="content1">
				<!--
				<a onClick="toTargetFormgoToTarget('/cib/authorizationPreference.do?ActionMethod=setLoad&corpId=<set:data name='corpId'/>&currency=<set:listdata name='cur'/>&txnType=TRANSFER_MACAU')" href="#"><set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				-->
				<a href="#" onClick="doSetLoad('<set:data name='corpId'/>', '<set:listdata name='cur'/>', 'TRANSFER_MACAU');">
                  <set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				</td>
			  </set:list>
              </tr>
              <tr class="greyline">
                <td class="label1"><!-- /* Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin */ --><!--<set:label name="Accounts_Overseas" defaultvalue="Transfer_Overseas"/>--><set:label name="TO_OVERSEAS_BANK_ACCOUNT" defaultvalue="To Overseas Bank Account"/><!-- /* Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end */ --></td>
			  <set:list name="List_TRANSFER_OVERSEAS">
                <td class="content1">
				<!--
				<a onClick="toTargetFormgoToTarget('/cib/authorizationPreference.do?ActionMethod=setLoad&corpId=<set:data name='corpId'/>&currency=<set:listdata name='cur'/>&txnType=TRANSFER_OVERSEAS')" href="#"><set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				-->
				<a href="#" onClick="doSetLoad('<set:data name='corpId'/>', '<set:listdata name='cur'/>', 'TRANSFER_OVERSEAS');">
                  <set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				</td>
			  </set:list>
              </tr>
              <!--<tr class="greyline">
                <td class="label1"><set:label name="Corp_Fund_Alloc" defaultvalue="Corporation Fund Allocation"/></td>
			  <set:list name="List_TRANSFER_CORP">
                <td class="content1">
				
				<a onClick="toTargetFormgoToTarget('/cib/authorizationPreference.do?ActionMethod=setLoad&corpId=<set:data name='corpId'/>&currency=<set:listdata name='cur'/>&txnType=TRANSFER_CORP')" href="#"><set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				
				<a href="#" onClick="doSetLoad('<set:data name='corpId'/>', '<set:listdata name='cur'/>', 'TRANSFER_CORP');">
                  <set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				</td>
			  </set:list>
              </tr>
              --><!--<tr>
                <td class="label1"><set:label name="Bill_Payment" defaultvalue="Pay_Bills"/></td>
			  <set:list name="List_PAY_BILLS">
                <td class="content1">
				--><!--
				<a onClick="toTargetFormgoToTarget('/cib/authorizationPreference.do?ActionMethod=setLoad&corpId=<set:data name='corpId'/>&currency=<set:listdata name='cur'/>&txnType=PAY_BILLS')" href="#"><set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				-->
				<!--<a href="#" onClick="doSetLoad('<set:data name='corpId'/>', '<set:listdata name='cur'/>', 'PAY_BILLS');">
                  <set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				</td>
			  </set:list>
              </tr>
              -->
              <tr>
                <td class="label1"><set:label name="Time_Deposit" defaultvalue="Time Deposit"/></td>
			  <set:list name="List_TIME_DEPOSIT">
                <td class="content1">
				<!--
				<a onClick="toTargetFormgoToTarget('/cib/authorizationPreference.do?ActionMethod=setLoad&corpId=<set:data name='corpId'/>&currency=<set:listdata name='cur'/>&txnType=TIME_DEPOSIT')" href="#"><set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				-->
				<a href="#" onClick="doSetLoad('<set:data name='corpId'/>', '<set:listdata name='cur'/>', 'TIME_DEPOSIT');">
                  <set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				</td>
			  </set:list>
              </tr>
              
              <!--<tr>
                <td class="label1"><set:label name="Payroll" defaultvalue="Payroll"/></td>
			  <set:list name="List_PAYROLL">
                <td class="content1">
				--><!--
				<a onClick="toTargetFormgoToTarget('/cib/authorizationPreference.do?ActionMethod=setLoad&corpId=<set:data name='corpId'/>&currency=<set:listdata name='cur'/>&txnType=PAYROLL')" href="#"><set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				-->
				<!--<a href="#" onClick="doSetLoad('<set:data name='corpId'/>', '<set:listdata name='cur'/>', 'PAYROLL');">
                  <set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				</td>
			  </set:list>
              </tr>
			  <tr class="greyline">
                <td class="label1"><set:label name="Scheduled_Txn" defaultvalue="Scheduled Transaction"/></td>
			  <set:list name="List_SCHEDULE_TXN">
                <td class="content1">
				<a href="#" onClick="doSetLoad('<set:data name='corpId'/>', '<set:listdata name='cur'/>', 'SCHEDULE_TXN');">
                  <set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				</td>
			  </set:list>
              </tr>			  
              <tr>
                <td class="label1"><set:label name="Bank_Draft" defaultvalue="Bank Draft"/></td>
			  <set:list name="List_BANK_DRAFT">
                <td class="content1">
				--><!--
				<a onClick="toTargetFormgoToTarget('/cib/authorizationPreference.do?ActionMethod=setLoad&corpId=<set:data name='corpId'/>&currency=<set:listdata name='cur'/>&txnType=BANK_DRAFT')" href="#"><set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				-->
				<!--<a href="#" onClick="doSetLoad('<set:data name='corpId'/>', '<set:listdata name='cur'/>', 'BANK_DRAFT');">
                  <set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				</td>
			  </set:list>
              </tr>
			  --><!--<tr class="greyline">
                <td class="label1"><set:label name="Cashier_Order" defaultvalue="Cashier Order"/></td>
			  <set:list name="List_CASHIER_ORDER">
                <td class="content1">
				--><!--
				<a onClick="toTargetFormgoToTarget('/cib/authorizationPreference.do?ActionMethod=setLoad&corpId=<set:data name='corpId'/>&currency=<set:listdata name='cur'/>&txnType=CASHIER_ORDER')" href="#"><set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				-->
				<!--<a href="#" onClick="doSetLoad('<set:data name='corpId'/>', '<set:listdata name='cur'/>', 'CASHIER_ORDER');">
                  <set:listdata name="status" rb="app.cib.resource.sys.pref_status" /></a>
				</td>
			  </set:list>
              </tr>	
            --></table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
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
