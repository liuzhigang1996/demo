<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/dialogwithlogo.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.enq.interest_rate">
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

<!-- modify by long_zg 2014-03-17 for IE11 begin-->
<!--<SCRIPT language=JavaScript src="/cib/javascript/jsax.js"></SCRIPT>-->
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<!-- modify by long_zg 2014-03-17 for IE11 end-->

<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	var zoneCodeFromAction = '<set:data name="zoneCode"/>';
	if ((zoneCodeFromAction != null) && (zoneCodeFromAction != '')) {
		var zcArray = document.form1.zoneCode;
		if ((zoneCodeFromAction == 'AP') || (zoneCodeFromAction == '')) {
			zcArray[0].checked = true;
		} else if (zoneCodeFromAction == 'EU') {
			zcArray[1].checked = true;
		} else if (zoneCodeFromAction == 'NA') {
			zcArray[2].checked = true;
		} 
	}
}
function doSubmit() {
	$('form1').submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/interestRates.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.interest_rate" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue=""/><!-- InstanceEndEditable --></td>
    <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" bgcolor=""><img src="/cib/images/shim.gif" width="1" height="1"></td>
        </tr>
      </table></td>
	  <td width="60"><img src="/cib/images/logo2.gif"></td>
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
		  <form action="/cib/interestRates.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> </td>
                </tr>
              </table>
			   <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
			  <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="label1">
					<strong><set:label name="currency" defaultvalue=""/>:</strong>
					<br>&nbsp; 
				</td>
                <td class="label1">
					<select id="currency" name="currency" nullable="0" onChange="form1.submit();">
						<option>----- Select a currency ------</option>
						<set:rblist db="currencyComboBox">
                    		<set:rboption name="currency"/>
						</set:rblist>
                  	</select>
					&nbsp;&nbsp;&nbsp;&nbsp; 
					(<set:label name="effective_time" defaultvalue="effective time"/>&nbsp;&nbsp;<set:data name='effectiveDate' format="date"/>)
					<br>&nbsp; 
				</td>
              </tr>
              <tr class="greyline" >
                <td width="17%" class="label1"><strong><set:label name="currency" defaultvalue=""/></strong></td>
                <td width="83%" class="content1" >&nbsp;&nbsp;<set:data name='currency' db="currencyComboBox"/>
              </tr>
			   <tr>
                <td class="label1"><strong><set:label name="Savings_Interest_Rate" defaultvalue="Savings Interest Rate"/></strong></td>
                <td class="content1" >&nbsp;&nbsp;<set:data name='interestRate'/>
              </tr>
			   <tr class="greyline">
                <td colspan="2"  class="label1"><b><set:label name="Time_Deposit_Rate" defaultvalue="Time Deposit Rate"/></b></td>
              </tr> 
            </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1" align="left"><set:label name="Deposit_Period" defaultvalue="Deposit Period"/></td>
                  <td align="center" class="listheader1"><set:data name="label1" format="amount"/>&nbsp;<set:label name="to" defaultvalue="to"/>&nbsp;<set:data name="label1a" format="amount"/></td>
                  <td align="center" class="listheader1"><set:data name="label1" format="amount"/>&nbsp;<set:label name="to" defaultvalue="to"/>&nbsp;<set:data name="label2a" format="amount"/></td>
                  <td align="center" class="listheader1"><set:data name="label3" format="amount"/>&nbsp;<set:label name="to" defaultvalue="to"/>&nbsp;<set:data name="label3a" format="amount"/></td>
                  <td align="center" class="listheader1"><set:data name="label4" format="amount"/>&nbsp;<set:label name="to" defaultvalue="to"/>&nbsp;<set:data name="label4a" format="amount"/></td>
                  <td align="center" class="listheader1"><set:data name="label5" format="amount"/>&nbsp;<set:label name="above" defaultvalue="or above"/></td>
                </tr>
                <set:list name="interestRatesList" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1" align="left"><set:listdata name="PERIOD" db="period"/>
                  <td class="listcontent1" align="center"><set:listif name='PERIOD' condition='equals' value='7D'>-</set:listif><set:listif name='PERIOD' condition='notequals' value='7D'><set:listdata name='INTEREST_RATE1'/>%</set:listif></td>
                  <td class="listcontent1" align="center"><set:listdata name='INTEREST_RATE1'/>%</td>
                  <td class="listcontent1" align="center"><set:listdata name="INTEREST_RATE2"/>%</td>
                  <td class="listcontent1" align="center"><set:listdata name="INTEREST_RATE3"/>%</td>
                  <td class="listcontent1" align="center"><set:listdata name="INTEREST_RATE4"/>%</td>
                </tr>
                </set:list>
                <tr>
                  <td class="content1" colspan="6"><set:label name="Interest_Info" defaultvalue="Interest_Info"/></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator" align="right"><a href="#" onClick="window.close();"><font color="#FFFFFF"><set:label name="close" defaultvalue="close" rb="app.cib.resource.enq.exchange_rate"/></font></a></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="content1"><set:label name="copyright" defaultvalue="copyright" rb="app.cib.resource.enq.exchange_rate"/></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input name="ActionMethod" id="ActionMethod" type="hidden" value="listInterestRates">
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
