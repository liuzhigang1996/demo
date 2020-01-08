<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bat.payroll">
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
<script language="JavaScript" src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit(arg) {
	if(validate_previous_file(document.getElementById("form1"))){
		if (arg == 'ok') {
			document.form1.ActionMethod.value = 'usePreviousFile';
			document.getElementById("form1").submit();
			setFormDisabled("form1");
			//setFieldEnabled("buttonReturn");
		}
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/payroll.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bat.payroll" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_previous" defaultvalue="MDB Corporate Online Banking > Payroll > Upload a Payroll File"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_previous" defaultvalue="UPLOAD A PAYROLL FILE"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/payroll.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="previous_file" form="form1" file="payroll" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3" id="round">
                <tr>
                  <td colspan="2" class="groupinput"><set:label name="confirmation" defaultvalue="confirmation"/></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="total_amt_file" defaultvalue="total_amt_file"/></td>
                  <td class="content1"><set:data name="currency"/>&nbsp;<set:data name="totalAmount" format="amount"/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="no_of_record_file" defaultvalue="no_of_record_file"/></td>
                  <td class="content1"><set:data name="totalNumber" /></td>
                </tr>
                <tr class="">
                  <td width="28%" class="label1"><set:label name="pay_day" defaultvalue="pay_day"/></td>
                  <td width="72%" class="content1"><input name="payDate" type="text" id="payDate" value="<set:data name='payDate'/>" size="10" maxlength="10">
                  <span class="content"><span class="content1"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "scwShow(document.getElementById('payDate'), this,language);" ></span></span></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1"><div align="center"><set:label name="ref_no" defaultvalue="ref_no"/></td>
                  <td class="listheader1"><div align="center"><set:label name="payee_acc" defaultvalue="payee_acc"/></div></td>
                  <td class="listheader1"><div align="center"><set:label name="amt" defaultvalue="amt"/></div></td>
                  <td class="listheader1"><div align="center"><set:label name="ccy" defaultvalue="ccy"/></div></td>
			    </tr>
                <set:list name="payrollRecList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1"><div align="center"><set:listdata name="REFERENCE_NO"/></td>
                  <td class="listcontent1"><div align="center"><set:listdata name="TO_ACCOUNT" /></div></td>
                  <td class="listcontent1"><div align="center"><set:listdata name="CREDIT_AMOUNT" format="amount"/></div></td>
                  <td class="listcontent1"><div align="center"><set:data name="currency" /></div></td>
			    </tr>
                </set:list>
                <tr>
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1"><strong><div align="center"><set:label name="total_amt"/></div></strong></td>
                  <td class="listcontent1"><strong><div align="center"><set:data name="totalAmount" format="amount"/></div></strong></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
              </table>
			  <set:assignuser selectFlag='Y'/>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><div align="center">
				  <set:if name="listSize" value="0" condition="notequals">
                      <input id="confirm" name="confirm" type="button" value="&nbsp;&nbsp;<set:label name='ok' defaultvalue=' ok '/>&nbsp;&nbsp;" onClick="doSubmit('ok')">
                  </set:if>    
					  <input id="ActionMethod" name="ActionMethod" type="hidden" value="uploadFile">
                    </div></td>
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
