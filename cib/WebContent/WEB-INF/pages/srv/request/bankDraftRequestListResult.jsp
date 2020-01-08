<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.bank_draft_request">
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
}
function doSubmit()
{
	if(validate_bank_draft_history(document.getElementById("form1"))){
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
}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/bankRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.bank_draft_request" -->
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
		  <form action="/cib/bankRequest.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="bank_draft_history" form="form1" file="bank_draft" />                        </td>
                      </tr>
            </table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="label1"><set:label name="From_Account" defaultvalue="Account Number"/></td>
                <td class="content1" ><select id="fromAccount" name="fromAccount" nullable="0">
					<set:rblist db="caoasaAccountByUser">
                    <set:rboption name="fromAccount"/>
					</set:rblist>
                  </select>	
				</td>
              </tr>
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
                 <input id="dateFrom" name="dateFrom" type="text" value="<set:data name='dateFrom' format="date"/>" size="12" maxlength="10" disabled="disabled"> 
                <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" >&nbsp;&nbsp;&nbsp;&nbsp;
                <set:label name="date_to" defaultvalue="date to"/>&nbsp;&nbsp;&nbsp;&nbsp;<input id="dateTo" name="dateTo" type="text" value="<set:data name='dateTo' format="date"/>" size="12" maxlength="10" disabled="disabled"> 
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
              <tr align="center">
                <td width="55%" height="40" align="right"> <span class="sectionbutton">
                  <input id="buttonOk" name="buttonOk" type="button" value="<set:label name='Enquiry' defaultvalue=' Enquiry '/>" onClick="doSubmit('ok')">
                  </span></td>
                <td align="right" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                  <%--<tr>
                     <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=toList&fileName=bankDraft_history&columnTitles=<set:label name='Request_Type'/>,<set:label name='REQUES_DATE'/>,<set:label name='FROM_ACCOUNT'/>,<set:label name='CURRENCY'/>,<set:label name='DRAFT_NUMBER'/>,<set:label name='TOTAL_AMOUNT'/>,<set:label name='STATUS'/>,<set:label name='upload_status'/>&columnNames=requestType,requestTime||format@datetime1,fromAccount,toCurrency||db@currency,totalNumber,toAmount||format@amount,status||rb@app.cib.resource.common.status,batchResult||rb@app.cib.resource.bat.batch_result"><set:label name="download" rb="app.cib.resource.common.operation"/></a> </td>
                     
                  </tr>
                --%></table></td>
              </tr>
            </table> 
			  <table width="100%" border="0" cellspacing="0" cellpadding="3">
			   <tr class="greyline">
			    <td class="listheader1"><div align="left"><set:label name="Request_Type" defaultvalue="Request Type"/></div></td>
			    <td class="listheader1"><div align="left"><set:label name="REQUES_DATE" defaultvalue="REQUEST DATE"/></div></td>
			    <td  class="listheader1"><div align="left"><set:label name="FROM_ACCOUNT" defaultvalue="FROM ACCOUNT"/></div></td>
                <td  class="listheader1"><div align="center"><set:label name="CURRENCY" defaultvalue="CURRENCY"/></div></td>
                <td  class="listheader1"><div align="center"><set:label name="DRAFT_NUMBER" defaultvalue="NUMBER OF BANK DRAFT"/></div></td>
                <td  class="listheader1"><div align="right"><set:label name="TOTAL_AMOUNT" defaultvalue="TOTAL AMOUNT"/></div></td>
				<td  class="listheader1"><div align="left"><set:label name="STATUS" defaultvalue="STATUS"/></div></td>
				<td class="listheader1"><div align="left"><set:label name="upload_status" defaultvalue="upload_status"/></div></td>	
				<set:if name="approverFlag" condition="EQUALS" value="Y">
				<td class="listheader1"><div align="center"><set:label name="CHANGE_APPROVER" defaultvalue="CHANGE APPROVER"/></div></td>
				</set:if>
				<td  class="listheader1"><div align="center"><set:label name="VIEW_DETAIL" defaultvalue="VIEW DETAIL"/></div></td>		
	           </tr>
			  <set:list name="toList"  showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
			    <td class="listcontent1"><div align="left"><set:listdata name="requestType" /></div></td>
			    <td class="listcontent1"><div align="left"><set:listdata name="requestTime" format="datetime1"/></div></td>
			    <td class="listcontent1"><div align="left"><set:listdata name="fromAccount" /></div></td>
                <td class="listcontent1"><div align="center"><set:listdata name="toCurrency" db="rcCurrencyCBS"/></div></td>
				<td class="listcontent1"><div align="center"><set:listdata name="totalNumber" /></div></td>
				<td class="listcontent1"><div align="right"><set:listdata name="toAmount" format="amount"/></div></td>
				<td class="listcontent1"><div align="left"><set:listdata name="status" rb="app.cib.resource.common.status"/></div></td>
				<td class="listcontent1"><div align="left"><set:listdata name="batchResult" rb="app.cib.resource.bat.batch_result"/></div></td>
				<set:if name="approverFlag" condition="EQUALS" value="Y">
				<td class="listcontent1"><div align="center"><set:listif name="status" condition="NOTEQUALS" value="1">--</set:listif><set:listif name="status" condition="EQUALS" value="1"><set:listif name="changeFlag" condition="EQUALS" value="Y"><a onClick="postToMainFrame('/cib/approve.do?ActionMethod=changeApproverLoad',{txnTypeToChange:'<set:data name="txnType"/>',transNoToChange:'<set:listdata name="batchId"/>'})" href="#"><set:label name="Change" defaultvalue="Change"/></a></set:listif><set:listif name="changeFlag" condition="EQUALS" value="N">--</set:listif></set:listif></div></td>
				</set:if>
				<td class="listcontent1"><div align="center"><a onClick="postToMainFrame('/cib/bankRequest.do?ActionMethod=viewDetail',{batchId:'<set:listdata name="batchId"/>'})" href="#"><set:label name="VIEW_DETAIL" defaultvalue="View Detail"/></div></td>
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
