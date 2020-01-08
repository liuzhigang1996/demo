<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
  <set:list name="inputList">
	putFieldValues("beneficiaryName_<set:listdata name='index'/>", "<set:listdata name='beneficiaryName'/>");
	putFieldValues("beneficiaryName2_<set:listdata name='index'/>", "<set:listdata name='beneficiaryName2'/>");
	putFieldValues("draftAmount_<set:listdata name='index'/>", "<set:listdata name='draftAmount' format='amount'/>");
	// add by lw 20101029
	changePurpose("<set:listdata name='purposeString'/>","<set:listdata name='index'/>", "<set:listdata name='showPurpose'/>","<set:listdata name='showProof'/>");
	// add by lw end
  </set:list>
}
function doSubmit() {
	//add by lw 20100902
	<set:list name="inputList">
	if(!PurposeCheck('<set:listdata name='index'/>')){
	  return false;
	}
	</set:list>
	//add by lw end	
	if(Beneficiary_Name()){
		if(fieldCheck()){
			setFormDisabled('form1');
			document.getElementById("form1").submit();
		} else {
	   		alert('<set:label name="Amount_Is_Error" defaultvalue="The amount that you input is error"/>');
		}
	} else {
			alert('<set:label name="Beneficiary_Name_Is_Error" defaultvalue="The amount that you input is error"/>');
	}
}
function fieldCheck() {
	var form = document.form1;
	for (i=0; i<form.elements.length; i++){
		if (form.elements[i].type == 'text') {
			if (form.elements[i].name.substring(0,12) == 'draftAmount_') {
				if (form.elements[i].value=='' || !validateAmountByLang(form.elements[i], 15, 2,language) || form.elements[i].value.substring(0,1) == 0) {
					form.elements[i].focus();
					form.elements[i].select();
					return false;
				}
			//alert('<set:data name='toCurrency'/>');
			if ('<set:data name='toCurrency'/>'=='JPY' || '<set:data name='toCurrency'/>'=='KRW') {	
				if (!checkInteger(form.elements[i].value)) {
				alert('<set:label name="err.txn.DecimalNotAllowed" rb="app.cib.resource.common.errmsg"/>');
				form.elements[i].value.select();
				return false;
			} 
			}
			}
		}
	}
	return true;
}
function checkInteger(amt) {
	if (amt.indexOf('.') > 0) {
		return false;
	} else {
		return true;
	}
}
function Beneficiary_Name() {
    var form = document.form1;
	for (i=0; i<form.elements.length; i++){
		if (form.elements[i].type == 'text') {
			if (form.elements[i].name.substring(0,16) == 'beneficiaryName_') {
				//alert(form.elements[i].name + ',' + form.elements[i].value);
				if (form.elements[i].value=='') {
					return false;
				}
			}
		}
	}
	return true;
}
/*add by lw 20101029*/
function changePurpose(purposeString, index, showPurpose,showProof){
    var purpose = document.getElementById("purpose_" + index);
	if(showPurpose == "true"){
	    document.getElementById("purposeArea_" + index).style.display = "block";
		purpose.value = purposeString;
	}
	if(showPurpose == "false"){
		document.getElementById("purposeArea_" + index).style.display = "none";
	}
	if(showProof == "true"){
		document.getElementById("proofDiv").style.display = "block";
		document.form1.submit1.disabled = true;
	}
	if(showProof == "false"){
		document.getElementById("proofDiv").style.display = "none";
	}
}
/*add by lw end*/
/* Add by lw 20100901 */
function PurposeCheck(index){
	var form = document.form1;
  	var purpose = document.getElementById("purpose_" + index);
	if(document.getElementById("purposeArea_" + index).style.display == "block"){
		if(trim(purpose.value) == ''){
			alert('<set:label name="OtherPurpose_Is_Blank" defaultvalue="Please enter purpose"/>');
			purpose.focus();
			return false;			
		}	
	}
	return true;
}
// add by lw 20101026
function changeProof(){
	if(document.getElementById('proofOfPurpose').checked == true){
		document.form1.submit1.disabled = false;
	}
	else{
		document.form1.submit1.disabled = true;
	}
}
// add by lw end
function checkIfShowPurpose(value,index){
	var draftAmount = document.getElementById("draftAmount_" + index).value;
	var toCurrency = document.getElementById("toCurrency").value;
	var fromAccount = document.getElementById("fromAccount").value;
	var url = '/cib/jsax?serviceName=CheckNeedPurposeService&transferAmount=' +draftAmount+ '&debitAmount=' +""+ '&fromAccount='+fromAccount+ '&toCurrency=' +toCurrency+ '&toCountryCode=MO';
	//registerElement('purposeVisible');
	// add by lw 20101026
	//registerElement('proofVisible'); 
	// add by lw end 
	getMsgToElement(url, draftAmount, '', null,false,language);
	var vi = document.getElementById("purposeVisible").innerHTML;
	// add by lw 20101026
	var pi = document.getElementById("proofVisible").innerHTML;
	// add by lw end
	if(vi == "visible"){
	    document.getElementById("purposeArea_" + index).style.display = "block";
		// add by lw 20101026
		if(pi == "needproof"){
			document.getElementById("proofDiv").style.display = "block";
			document.getElementById('proofOfPurpose').checked = false;
			document.form1.submit1.disabled = true;
		}
		else{
			document.getElementById("proofDiv").style.display = "none";
			document.form1.submit1.disabled = false;
		}
		// add by lw end
	}
	else{
		document.getElementById("proofVisible").value = "";
	    document.getElementById("purposeArea_" + index).style.display = "none";
		document.getElementById("purpose_" + index).value = "";
		// add by lw 20101026
		if(index == 1){
			document.getElementById("proofDiv").style.display = "none";
			document.form1.submit1.disabled = false;
		}
		// add by lw end
	}
}
/* Add by lw end */
</script>
<style type="text/css">
<!--
.STYLE1 {font-size: 60pt}
-->
</style>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/bankRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.bank_draft_request" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="BANK Online Banking >Services > Bank Draft Request"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitleInput" defaultvalue="BANK DRAFT REQUEST"/><!-- InstanceEndEditable --></td>
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
		    <!-- InstanceBeginEditable name="sectioncontent" --> <set:messages width="100%" cols="1" align="center"/>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr class="greyline">
                  <td colspan="4" class="label1"><set:label name="Fill_Info1" defaultvalue="Enter details(a maximum of 10 each page can be input)"/></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <!--<tr>
                  <td class="label1"><set:label name="Total_Amount" defaultvalue="The Total Amount"/></td>
                  <td align="center" class="content1" colspan="3"><set:data name='toCurrency'/> <set:data name='toAmount' format="amount"/></td>
                </tr>
				-->
                <tr >
                  <td width="12%" class="listheader1"><set:label name="Beneficiary_Name" defaultvalue="Beneficiary Name"/></td>
                  <td width="12%" class="listheader1"><set:label name="Amount" defaultvalue="Amount"/></td>
				  <!-- Add by lw 20100901 -->
				  <td class="listheader1"><set:label name="Purpose" defaultvalue="Purpose"/></td>
				  <!-- Add by lw end -->
                </tr>
                <set:list name="inputList" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td align="center" class="listcontent1">
                    <div align="left">
                      <input id="beneficiaryName_<set:listdata name='index'/>" name="beneficiaryName_<set:listdata name='index'/>" type="text" value="" size="45" maxlength="35"><br>
                      <input id="beneficiaryName2_<set:listdata name='index'/>" name="beneficiaryName2_<set:listdata name='index'/>" type="text" value="" size="45" maxlength="35">
                    </div></td>
					<!-- add by lw 20100901 -->
					<td align="center" class="listcontent1"><div align="left">
  <input name="draftAmount_<set:listdata name='index'/>" id="draftAmount_<set:listdata name='index'/>" onBlur="checkIfShowPurpose(this.value,<set:listdata name='index'/>)" type="text" value="<set:data name='draftAmount' format="amount"/>" size="20" maxlength="20">
  					<input id="index" name="index" type="hidden" value="<set:listdata name='index'/>">
  					</div></td>
				  <!-- add by lw 20101026 -->
				  <!-- modify by lw for CR145 2011-08-24 -->
				  <div id="purposeVisible" name="purposeVisible" style="display:none"></div>				  
				  <div id="proofVisible" name="proofVisible" style="display:none"></div>
				  <td colspan="4">
				  <div id="purposeArea_<set:listdata name='index'/>" name="purposeArea_<set:listdata name='index'/>" style="display:none">
					<table width="100%">
					<tr>
	     				<td><input name="purpose_<set:listdata name='index'/>" id="purpose_<set:listdata name='index'/>" value="<set:data name="purpose"/>" type="text" size="35" maxlength="35"></td>
						<td><input id="toCurrency" name="toCurrency" type="hidden" value="<set:data name='toCurrency'/>"></td>
						<td><input id="fromAccount" name="fromAccount" type="hidden" value="<set:data name='fromAccount'/>"></td>
					</tr>
					</table>
	     		</div>
				  <!-- add by lw end -->
                  </td>                   
                </tr>
                </set:list>
				<!-- add by lw 20101026 -->
				<!-- modify by lw for CR145 2011-08-24 -->
				<tr>
				<td colspan="4">
				<div name="proofDiv" id="proofDiv" style="display:none">
				<table width="100%">
     			<tr>
					<td class="label1" width="24%" align="right"><input name="proofCheckBox" type="checkbox" id="proofCheckBox" onClick="changeProof()"></td>
					<td class="content1" colspan="3" width="76%"><font color="#FF0000"><set:label name="Need_Proof_Message" defaultvalue="* Need to provide Proof Document."/></font></td>
				</tr>
				</table>
				</div>
				</td>
				</tr>
				<!-- add by lw end -->
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td colspan="2" class="sectionbutton"><input id="submit1" name="submit1" type="button" value="<set:label name='Next' defaultvalue='Next' />" tabindex="3"  onClick="doSubmit();">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="add">
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
