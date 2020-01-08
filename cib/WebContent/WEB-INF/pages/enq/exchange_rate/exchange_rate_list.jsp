<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.enq.exchange_rate">
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
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/exchangeRates.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.exchange_rate" -->
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="FREQUENT PAYMENT LIST"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/exchangeRates.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="11">
				  	<input id="zoneCode" name="zoneCode" type="radio" value="AP" checked onClick="doSubmit();">
                    <set:label name='asia_pacific' defaultvalue='asia_pacific'/>&nbsp;&nbsp;&nbsp;
                    <input id="zoneCode" type="radio" name="zoneCode" value="EU" onClick="doSubmit();">
                    <set:label name='european' defaultvalue='european'/>&nbsp;&nbsp;&nbsp;
                    <input id="zoneCode" name="zoneCode" type="radio" value="NA" onClick="doSubmit();">
                    <set:label name='north_american' defaultvalue='north_american'/>
				  </td>
                </tr>
                <tr>
                  <td rowspan="3" align="center" class="listheader1"><set:label name="code" defaultvalue="code"/></td>
                  <td colspan="2" rowspan="3" align="center" class="listheader1"><set:label name="currency" defaultvalue="currency"/></td>
                  <td colspan="4" align="center" class="listheader1"><set:label name="tt" defaultvalue="tt"/></td>
                  <td colspan="4" align="center" class="listheader1"><set:label name="banknotes" defaultvalue="banknotes"/></td>
                </tr>
                <tr>
                  <td colspan="2" align="center" class="listheader1"><set:label name="against_hk" defaultvalue="against_hk"/></td>
                  <td colspan="2" align="center" class="listheader1"><set:label name="against" defaultvalue="against"/></td>
                  <td colspan="2" align="center" class="listheader1"><set:label name="against_hk" defaultvalue="against_hk"/></td>
                  <td colspan="2" align="center" class="listheader1"><set:label name="against" defaultvalue="against"/></td>
                </tr>
                <tr>
                  <td class="listheader1" align="center"><set:label name="buy" defaultvalue="buy"/></td>
                  <td align="center" class="listheader1"><set:label name="sell" defaultvalue="sell"/></td>
                  <td align="center" class="listheader1"><set:label name="buy" defaultvalue="buy"/></td>
                  <td align="center" class="listheader1"><set:label name="sell" defaultvalue="sell"/></td>
                  <td align="center" class="listheader1"><set:label name="buy" defaultvalue="buy"/></td>
                  <td align="center" class="listheader1"><set:label name="sell" defaultvalue="sell"/></td>
                  <td align="center" class="listheader1"><set:label name="buy" defaultvalue="buy"/></td>
                  <td align="center" class="listheader1"><set:label name="sell" defaultvalue="sell"/></td>
                </tr>
                <set:list name="eRatesList" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1" align="center"><set:listdata name="CCY_CODE"/>
                  <td class="listcontent1" align="center"><set:listdata name='UNIT'/></td>
                  <td class="listcontent1" align="left"><set:listdata name='CCY_LONG_NAME'/></td>
                  <td class="listcontent1" align="center"><set:listdata name="TT_HKD_BUY_RATE" format="rate"/></td>
                  <td class="listcontent1" align="center"><set:listdata name="TT_HKD_SELL_RATE" format="rate"/></td>
                  <td class="listcontent1" align="center"><set:listdata name="TT_MOP_BUY_RATE" format="rate"/></td>
                  <td class="listcontent1" align="center"><set:listdata name='TT_MOP_SELL_RATE' format="rate"/></td>
                  <td class="listcontent1" align="center"><set:listdata name="NOTE_HKD_BUY_RATE" format="rate"/></td>
                  <td class="listcontent1" align="center"><set:listdata name="NOTE_HKD_SELL_RATE" format="rate"/></td>
                  <td class="listcontent1" align="center"><set:listdata name="NOTE_MOP_BUY_RATE" format="rate"/></td>
                  <td class="listcontent1" align="center"><set:listdata name="NOTE_MOP_SELL_RATE" format="rate"/></td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator" align="right"><a href="#" onClick="window.close();"><font color="#FFFFFF"><set:label name="close" defaultvalue="close"/></font></a></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="content1"><set:label name="notice" defaultvalue="notice"/></td>
                </tr>
                <tr>
                  <td class="content1"><set:label name="copyright" defaultvalue="copyright"/></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input name="ActionMethod" id="ActionMethod" type="hidden" value="listExchangeRates">
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
