<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.protection_check_request">
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
<SCRIPT language=JavaScript src="/cib/javascript/jsax.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
//alert("1");
//alert("<set:data name='chequeStyle' />");
//putFieldValues("chequeStyle", "<set:data name='chequeStyle' />");
}
function doSubmit()
{
	if(validate_protection_check(document.getElementById("form1"))){
			setFormDisabled('form1');
			document.getElementById("form1").submit();
	}
}
</script>
<style type="text/css">
<!--
.STYLE1 {font-size: 60pt}
-->
</style>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/protectionChequeRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.protection_check_request" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitleProtection" defaultvalue="BANK Online Banking >Services > Cheque Protection"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitleProtection" defaultvalue="CHEQUE PROTECTION"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/protectionChequeRequest.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <set:messages width="100%" cols="1" align="center"/>
            <set:fieldcheck name="protection_check" form="form1" file="check_request" />
			<table border="0" cellpadding="0" cellspacing="0">
			  <tr>
			    <td>
			      <table border="0" cellpadding="0" cellspacing="0">
			        <tr>
			          <td class="ltab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
          <td class="tab1_o">
            <set:label name="Cheque_single" defaultvalue="Cheque Protection Request"/></td>
			     <td class="rtab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
		  </tr></table>		  </td>
			     <td>
			       <table border="0" cellpadding="0" cellspacing="0">
			         <tr>
			           <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
          <td class="tab1_c">
            <a onClick="toTargetFormgoToTarget('/cib/protectionChequeBatchRequest.do?ActionMethod=uploadFileLoad')" href="#"><set:label name="Cheque_batch" defaultvalue=" Cheque Protection Batch Request"/></a></td>
			     <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
		  </tr></table>			     </td>
			     <td>
			       			      </td>
			     </tr>
			  </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="4" class="groupinput"><set:label name="Fill_Info" defaultvalue="Enter request information"/></td>
              </tr>
			   <tr>
                <td width="20%" valign="top" class="label1"><set:label name="Account" defaultvalue="Account"/></td>
                <td class="content1" colspan="3" ><select id="account" name="account" nullable="0" >
				<option selected value=" "><set:label name="Select_Account_Label" defaultvalue="----- Please Select an Account ------"/></option>
                  <set:rblist db="caoaAccountByUser">
                    <set:rboption name="account"/>
                  </set:rblist>
                </select>
                 </td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><set:label name="Cheque_Style" defaultvalue="Cheque Style"/></td>
                <td class="content1" colspan="3" ><select id="chequeStyle" name="chequeStyle" nullable="0" >
				<option selected value=" "><set:label name="Select_Cheque_Style" defaultvalue="----- Please Select an Cheque Style ------"/></option>
                  <set:rblist db="chequeStyle">
                    <set:rboption name="chequeStyle"/>
                  </set:rblist>
                </select>
                 </td>
              </tr> 
          <tr >
                <td class="label1"><set:label name="Cheque_Number" defaultvalue="Cheque Number"/></td>
                <td class="content1" colspan="3"><input id="chequeNumber" name="chequeNumber" type="text" value="<set:data name='chequeNumber'/>" size="6" maxlength="6"></td>
              </tr>   
			   <tr class="greyline">
                <td class="label1"><set:label name="Amount" defaultvalue="Amount"/></td>
                <td class="content1" colspan="3"><input id="amount" name="amount" type="text" value="<set:data name='amount' format="amount"/>" size="15" maxlength="20"></td>
              </tr>
			  <tr>
                <td class="label1"><set:label name="Beneficiary_Name" defaultvalue="Beneficiary Name"/></td>
                <td class="content1" colspan="3"><input id="beneficiaryName" name="beneficiaryName" type="text" value="<set:data name='beneficiaryName'/>" size="40" maxlength="40"></td>
              </tr>
			  <tr class="greyline" >
                <td class="label1"><set:label name="Issue_date" defaultvalue="Issue date"/></td>
                <td class="content1" colspan="3">
				<input name="issueDate" type="text" id="issueDate" size="10" maxlength="10" value="<set:data name='issueDate' format='date'/>">
	<img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "scwShow(document.getElementById('issueDate'), this,language);" >
	</td>
              </tr>       
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
			  <td colspan="2" class="sectionbutton">
                <input id="submit1" name="submit1" type="button" value="<set:label name='buttonOK' defaultvalue=' OK ' />" tabindex="3"  onClick="doSubmit();">
<!--                <input id="bottonReset" name="bottonReset" type="reset" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="protection">				  </td>
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
