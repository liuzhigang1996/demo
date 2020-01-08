<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.transfer_history">
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
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	changRoleIdbak('<set:data name="business_type"/>');
	var date_range = '<set:data name="date_range"/>';
  	changeRange(date_range);
  	var fromAccount = document.getElementById("fromAccount")
  	if(!"<set:data name='fromAccount' />" == ""){
		for(var i=0;i<fromAccount.options.length;i++){
    		if(fromAccount[i].value=='<set:data name='fromAccount' />'){  
        		fromAccount[i].selected=true;   
    		}  
		}  
	}
}
function doSubmit()
{
	if(validate_transferHistoryResult(document.getElementById("form1"))){
		setFormDisabled('form1');
		document.getElementById("form1").submit();
	}
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
  document.getElementsByName("date_range")[1].checked = true;
  var dateRange = document.getElementById("dateRange");
  if(!"<set:data name = 'dateRange' />" == ""){
   for ( var i = 0; i < dateRange.options.length; i++) {
    if(dateRange[i].value == "<set:data name = 'dateRange' />"){
     dateRange[i].selected = true;
    }
   }
  }
 }
}
function changRoleIdbak(obj){
	if(obj == '4'){
		document.getElementById("operator").style.display = "none";
		document.getElementById("approver").style.display = "";
		//setDivDisabled("approver");
		//setDivEnabled("operator");
	}else {
		document.getElementById("operator").style.display = "";
		document.getElementById("approver").style.display = "none";
		//setDivDisabled("operator");
		//setDivEnabled("approver");
	}
}
function changRoleId(obj){
	if(obj.options[obj.selectedIndex].value=='4'){
		document.getElementById("operator").style.display = "none";
		document.getElementById("approver").style.display = "";
		//setDivDisabled("approver");
		//setDivEnabled("operator");
	}else {
		document.getElementById("operator").style.display = "";
		document.getElementById("approver").style.display = "none";
		//setDivDisabled("operator");
		//setDivEnabled("approver");
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferHistory.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.transfer_history" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="BANK Online Banking >Transfer > Transfer History"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="TRANSFER HISTORY"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/transferHistory.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="transferHistoryResult" form="form1" file="transfer_history_result" />                        </td>
                      </tr>
            </table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator"><span class="content1">
                </span></td>
              </tr>
            </table>
			 <table width="100%" border="0" cellspacing="0" cellpadding="3">
			 <tr class="greyline">
                <td width="28%" class="label1"><set:label name="Business_type" defaultvalue="Business_type"/></td>
                <td colspan="2" class="content1" ><select id="business_type" name="business_type" nullable="0" onChange="changRoleId(this);">
				<option>-<set:label name="select_transfer_type" defaultvalue="----- Select a Transfer Type ------"/></option>
					<set:rblist file="app.cib.resource.txn.business_type">
                    <set:rboption name="business_type"/>
					</set:rblist>
                  </select>	
				</td>
              </tr>
			  <td width="28%" class="label1"><div title="operator"  id="operator" >
                        <set:label name="Account_Number" defaultvalue="Account Number"/>
                      </div>
                      <div title="approver"  id="approver" style="display:none">
                        <set:label name="To_Account_Number" defaultvalue="Account Number"/>
                      </div></td>
				  <td width="72%"  class="content1" >
				  	<select id="fromAccount" name="fromAccount" nullable="0">
                    	<option value="0" selected><set:label name="all" rb="app.cib.resource.txn.bill_payment"/></option>
						<!-- /* Modify by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */-->
						<!--
						<set:rblist db="caoasaAccountByUser">
                    		<set:rboption name="fromAccount"/>
						</set:rblist>
						-->
						<set:list name="transferuser"> <option  value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_NO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_NO' />
	                      </option>
	                    </set:list>
						<!-- /* Modify by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */-->
                  	</select>	
				</td> 
				</table>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr class="greyline">
                <td width="28%" height="39" class="label1"><set:label name="payment_period" defaultvalue="payment_period"/></td>
                <td width="72%" class="content1" colspan="3">
               	  <span class="content">
               	  <input id="date_range" name="date_range" type="radio" value="all" checked onClick="changeRange('all')"><set:label name="all" defaultvalue="all"/>
               	  </span><span class="sectionbutton">
               	  <input id="ActionMethod" name="ActionMethod" type="hidden" value="listHistory">
               	  </span></td>
              </tr>
              <tr>
                <td width="28%" class="label1">&nbsp;</td>
                <td width="72%" class="content1"><span class="content">
                	<input id="date_range" name="date_range" type="radio" value="range" onClick="changeRange('range')">
                	</span>
					<set:label name="date_from" defaultvalue="date from"/>
					<input id="dateFrom" name="dateFrom" type="text" value="<set:data name='dateFrom' format='date'/>" size="12" maxlength="10" disabled="disabled">
					<img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" >&nbsp;&nbsp;&nbsp;&nbsp;
                <set:label name="date_to" defaultvalue="date to"/>&nbsp;&nbsp;&nbsp;&nbsp;<input id="dateTo" name="dateTo" type="text" value="<set:data name='dateTo' format='date'/>" size="12" maxlength="10" disabled="disabled"> 
                <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" >
				<select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));">
                    <option value='0'><set:label name="select_date_short_cut" defaultvalue="----- Select a Date Short-cut ------"/></option>
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
              <tr align="center">
                <td width="55%" height="40" align="right"> <span class="sectionbutton">
                  <input id="buttonOk" name="buttonOk" type="button" value="<set:label name='search' defaultvalue='search'/>" onClick="doSubmit('ok')">
                  </span></td>
                <td align="right" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0"><%--
                  <tr>
                    <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=toList&fileName=transfer_history&columnTitles=<set:label name='REQUES_DATE'/>,<set:label name='FROM_ACCOUNT'/>,<set:label name='TO_ACCOUNT'/>,<set:label name='CURRENCY_TYPE'/>,<set:label name='AMOUNT'/>,<set:label name='STATE'/>&columnNames=requestTime||format@datetime1,fromAccount,toAccount,currency||db@currency,transferAmount||format@amount,status||rb@app.cib.resource.common.status"><set:label name="download" rb="app.cib.resource.common.operation"/></a> </td>                 
                  </tr>
                --%></table></td>
              </tr>
            </table> 
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
			   <tr >
                <td class="listheader1"><div align="left"><set:label name="REQUES_DATE" defaultvalue="REQUEST DATE"/></div></td>
                <td class="listheader1"><div align="left"><set:label name="FROM_ACCOUNT" defaultvalue="FROM ACCOUNT"/></div></td>
                <td class="listheader1"><div align="left"><set:label name="TO_ACCOUNT" defaultvalue="TO ACCOUNT"/></div></td>
				<td class="listheader1"><div align="center"><set:label name="CURRENCY_TYPE" defaultvalue="CURRENCY TYPE"/></div></td>
				<td class="listheader1"><div align="right"><set:label name="AMOUNT" defaultvalue="AMOUNT"/></div></td>
				<td class="listheader1"><div align="left"><set:label name="STATE" defaultvalue="STATE"/></div></td>
				<set:if name="uploadStatusFlag" condition="EQUALS" value="Y">
				<td class="listheader1"><div align="left"><set:label name="upload_status" defaultvalue="upload_status"/></div></td>
				</set:if>	
				<set:if name="approverFlag" condition="EQUALS" value="Y">
				<td class="listheader1"><div align="center"><set:label name="CHANGE_APPROVER" defaultvalue="CHANGE APPROVER"/></div></td>
				</set:if>
				<td class="listheader1"><div align="center"><set:label name="VIEW_DETAIL" defaultvalue="VIEW DETAIL"/></div></td>
              </tr>
			  <set:list name="toList" showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1"><div align="left"><set:listdata name="requestTime" format="datetime1"/></div></td>
                <td class="listcontent1"><div align="left"><set:listdata name="fromAccount"/></div></td>
			    <td class="listcontent1"><div align="left"><set:listdata name="toAccount"/></div></td>
			    <td class="listcontent1"><div align="center"><set:listif name="inputAmountFlag" condition="EQUALS" value="1"><set:listdata name="toCurrency" db="rcCurrencyCBS"/></set:listif><set:listif name="inputAmountFlag" condition="EQUALS" value="0"><set:listdata name="fromCurrency" db="rcCurrencyCBS"/></set:listif></div></td>
				<td class="listcontent1"><div align="right"><set:listif name="inputAmountFlag" condition="EQUALS" value="1"><set:listdata name="transferAmount" format="amount"/></set:listif><set:listif name="inputAmountFlag" condition="EQUALS" value="0"><set:listdata name="debitAmount" format="amount"/></set:listif></div></td>
				<td class="listcontent1"><div align="left"><set:listdata name="status" rb="app.cib.resource.common.status"/><a onClick="postToMainFrame('/cib/transferHistory.do?ActionMethod=viewInBANK',{transId:'<set:listdata name='transId'/>'})" href="#"></div></td>
				<set:if name="uploadStatusFlag" condition="EQUALS" value="Y">
				<td class="listcontent1"><div align="left"><set:listdata name="batchResult" rb="app.cib.resource.bat.batch_result"/></div></td>
				</set:if>	
				<set:if name="approverFlag" condition="EQUALS" value="Y">
				<td class="listcontent1"><div align="center"><set:listif name="status" condition="NOTEQUALS" value="1">--</set:listif><set:listif name="status" condition="EQUALS" value="1"><set:listif name="changeFlag" condition="EQUALS" value="Y"><a onClick="postToMainFrame('/cib/approve.do?ActionMethod=changeApproverLoad',{txnTypeToChange:'<set:data name='txnType'/>',transNoToChange:'<set:listdata name='transId'/>'})" href="#"><set:label name="Change" defaultvalue="Change"/></a></set:listif><set:listif name="changeFlag" condition="EQUALS" value="N">--</set:listif></set:listif></div></td>
				</set:if>
				<td class="listcontent1"><div align="center"><a onClick="postToMainFrame('/cib/transferHistory.do?ActionMethod=<set:data name='methodType'/>',{transId:'<set:listdata name='transId'/>'})" href="#"><set:label name="VIEW_DETAIL" defaultvalue="View Detail"/></div></td>
              </tr>
			  </set:list>
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
