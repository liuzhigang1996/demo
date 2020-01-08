<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.autopay_instruction">
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
	selectAcct() ;
}
function selectAcct() {
	var card_merchant = "<set:data name='card_merchant' />" ;
	var apsCode = "<set:data name='apsCode' />" ;
	if(card_merchant==apsCode){
		document.getElementById("labelContact").innerHTML =  "<set:label name='CREDIT_CARD_NO' defaultvalue='CREDIT_CARD_NO'/>";
		showDiv("cardPayDiv", true);
		showDiv("generalPayDiv", false);
	} else {
		document.getElementById("labelContact").innerHTML =  "<set:label name='CONTRACT_NUMBER' defaultvalue='CONTRACT_NUMBER'/>";
		showDiv("cardPayDiv", false);
		showDiv("generalPayDiv", true);
	}
}
function showDiv(divId, showFlag){
	if(showFlag){
		document.getElementById(divId).style.display = "" ;
	} else {
		document.getElementById(divId).style.display = "none" ;
	}
	
}
function enablePayLimit(){
	document.getElementById("paymentLimit").disabled = false ;
}
function disablePayLimit(){
	document.getElementById("paymentLimit").disabled = true ;
	// document.getElementById("paymentLimit").value = "" ;
}
function doSubmit(arg){
	if("cancel"==arg){
		document.form1.ActionMethod.value = "listAutopay" ;
		document.getElementById("form1").submit();
		setFormDisabled("form1");
	} else {
		if(validate_add(document.getElementById("form1"))){
			document.form1.ActionMethod.value = "edit" ;
			document.getElementById("form1").submit();
			setFormDisabled("form1");
		}
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/autopayAuthorization.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.autopay_instruction" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_autopay_list" defaultvalue="navigationTitle_autopay_list=MDB Corporate Online Banking > Pay Bills > Autopay Instruction List "/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" -->
    
    <set:if name="title" condition="equals" value="delete">
    	<set:label name="functionTitle_autopay_delete" defaultvalue="AUTOPAY INSTRUCTION - DELETE"/>
     </set:if>
    <set:if name="title" condition="equals" value="add">
    	<set:label name="functionTitle_autopay_add" defaultvalue="AUTOPAY INSTRUCTION - ADDITION"/>
     </set:if>
     <set:if name="title" condition="equals" value="edit">
    	<set:label name="functionTitle_autopay_edit" defaultvalue="AUTOPAY INSTRUCTION - EDIT"/>
     </set:if>
    <!-- InstanceEndEditable --></td>
    
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
		  <form action="/cib/autopayAuthorization.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="add" form="form1" file="autopay" />
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator"><set:label name="FILL_IN_INFORMATION" defaultvalue="FILL_IN_INFORMATION"/></td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td width="28%" class="label1"><set:label name="PAYMENT_TYPE" defaultvalue="PAYMENT_TYPE"/></td>
                <td width="72%" class="content1">
                <set:data name='apsCode' db='autopay' />
                <input type="hidden" name="apsCode" value="<set:data name='apsCode' />" >
				</td>
              </tr>
              <tr>
                <td width="28%" class="label1" valign="top"><div id="labelContact"><set:label name="CONTRACT_NUMBER" defaultvalue="CONTRACT_NUMBER"/></div></td>
                <td width="72%" class="content1">
                <set:data name='contractNo' />
                <input type="hidden" name="contractNo" id="contractNo" value="<set:data name='contractNo' />">
				</td>
              </tr>
              <tr class="">
                <td width="28%" class="label1" valign="top"><set:label name="PAYMENT_LIMINT" defaultvalue="PAYMENT_LIMINT"/></td>
                <td width="72%" class="content1">
                <input type="radio" name="payOption" value="F" onClick="disablePayLimit()" <set:if name='payOption' condition='equals' value='F'>checked</set:if>> <set:label name="FULL_PAYMENT" defaultvalue="Full Payment"/>
				</td>
              </tr>
              <tr class="" id="cardPayDiv">
              <td width="28%" class="label1" valign="top">&nbsp;</td>
                <td width="72%" class="content1"><input type="radio" name="payOption" value="M" onClick="disablePayLimit()" <set:if name='payOption' condition='equals' value='M'>checked</set:if>> <set:label name="Minimum_Payment" defaultvalue="Minimum Payment"/>
				</td>
              </tr>
              <tr class="" id="generalPayDiv">
              <td width="28%" class="label1" valign="top">&nbsp;</td>
                <td width="72%" class="content1"><input type="radio" name="payOption" value="P" onClick="enablePayLimit()" <set:if name='payOption' condition='equals' value='P'>checked</set:if>> <set:label name="Input_Amout" defaultvalue="Input Amout"/>&nbsp;&nbsp;&nbsp;<input type="text" name="paymentLimit" id="paymentLimit" 
                 value="<set:data name='paymentLimit' format='amount' pattern='#' />" <set:if name="paymentLimit" condition="equals" value="">disabled</set:if>>
				</td>
              </tr>
              <tr class="">
                <td width="28%" class="label1"><set:label name="PAYMENT_ACCOUNT" defaultvalue="PAYMENT_ACCOUNT"/></td>
                <td width="72%" class="content1">
					<select name="payAcct" id="payAcct">
						<option value="" selected><set:label name="Please_Select"/></option>
                  		<set:rblist db="caoasaAccountByUser">
				  			<set:rboption name="payAcct"/>
				  		</set:rblist>
               	  	</select>
				</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <set:assignuser selectFlag='N'/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
					<input id="ok" name="ok" type="button" value="&nbsp;&nbsp;<set:label name='OK' defaultvalue=' OK '/>&nbsp;&nbsp;" onClick="doSubmit('ok')">
                    <input id="cancel" name="cancel" type="button" value="&nbsp;&nbsp;<set:label name='CANCEL' defaultvalue=' CANCEL '/>&nbsp;&nbsp;" onClick="doSubmit('cancel')">
				  	<input id="ActionMethod" name="ActionMethod" type="hidden" value="ok">
                    <input id="transNo" name="transNo" type="hidden" value="<set:data name='transNo' />">
                    <input id="mode" name="mode" type="hidden" value="<set:data name='mode' />">
                     <input id="card_merchant" name="card_merchant" type="hidden" value="<set:data name='card_merchant' />">
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
