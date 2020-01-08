<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.chequeBookRequest">
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
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></script>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	var date_range = '<set:data name="date_range"/>';
  	changeRange(date_range);
}
function doSubmit()
{
	if(validate_history(document.getElementById("form1"))){
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
}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/stopChequeRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.stop_check_request" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><set:label name="navigationTitleList" defaultvalue="BANK Corporation Banking >Cheque > Cheque Book Request"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/chequeBookRequest.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="history" form="form1" file="stop_cheque_history" />                        </td>
                      </tr>
            </table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr class="greyline">
                <td width="28%" height="39" class="label1"><set:label name="payment_period" defaultvalue="payment_period"/></td>
                <td width="72%" class="content1" colspan="3">
               	  <span class="content">
               	  <input id="date_range" name="date_range" type="radio" value="all" checked onClick="changeRange('all')"><set:label name="all" defaultvalue="all"/>
               	  </span><span class="sectionbutton">
               	  <input id="ActionMethod" name="ActionMethod" type="hidden" value="listHistory">
               	  <!-- add by lzg 20190624 for pwc-->
					<set:singlesubmit/>
				  <!-- add by lzg end -->
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
                <select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));" >
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
                  <input id="buttonOk" name="buttonOk" type="button" value="<set:label name='search' defaultvalue=' search '/>" onClick="doSubmit('ok')">
                  </span></td>
                <td align="right" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                 </table></td>
              </tr>
            </table> 
			  <table width="100%" border="0" cellspacing="0" cellpadding="3">
			   <tr class="greyline">
			    <td class="listheader1"><div align="left"><set:label name="REQUES_DATE" defaultvalue="REQUEST DATE"/></div></td>
                <td class="listheader1"><div align="left"><set:label name="ACCOUNT_NO" defaultvalue="ACCOUNT NO"/></div></td>
				<td class="listheader1"><div align="left"><set:label name="Currency" defaultvalue="Currency"/></div></td>
				<td class="listheader1"><div align="left"><set:label name="No_Of_Books"  defaultvalue="No. of Books"/></div></td>
				<td class="listheader1"><div align="left"><set:label name="Pickup_Type" defaultvalue="PICK UP TYPE"/></div></td>
               	<td class="listheader1"><div align="left"><set:label name="ack_status" defaultvalue="STATUS"/></div></td>
				</tr>
			  <set:list name="chequeBookRequestList"  showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
			    <td class="listcontent1"><div align="left"><set:listdata name="requestTime" format="datetime1"/></div></td>
                <td class="listcontent1"><div align="left"><set:listdata name="accountNo" /></div></td>
				<td class="listcontent1"><div align="left"><set:listdata name="payCurrency" /></div></td>
				<td class="listcontent1"><div align="left"><set:listdata name="noOfBook" /></div></td>
				<td class="listcontent1"><div align="left"><set:listdata name="pickupType" rb = "app.cib.resource.srv.pick_up_type"/></div></td>
               	<td class="listcontent1"><div align="left"><set:listdata name="status" rb="app.cib.resource.common.status"/></div></td>
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
