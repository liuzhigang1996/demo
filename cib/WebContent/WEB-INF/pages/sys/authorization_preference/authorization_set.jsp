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
	changeRuleType("<set:data name='ruleType' />");
	if("1" == "<set:data name='prefStatus' />"){
		setFormAllDisabled("form1");
		setFieldEnabled("buttonReturn");
	}
}
function doSubmit(arg1,arg2) {
	if (arg1 == 'addList') {
		document.getElementById("ActionMethod").value = 'addList';
		setFormDisabled("form1");
		document.getElementById("form1").submit();
	}
	if (arg1 == 'deleteList') {
		document.getElementById("ActionMethod").value = 'deleteList';
		document.getElementById("seqno").value = arg2;
		setFormDisabled("form1");
		document.getElementById("form1").submit();
	}
	if (arg1 == 'return') {
		document.getElementById("ActionMethod").value = 'query';
		setFormDisabled("form1");
		document.getElementById("form1").submit();
	}
	if (arg1 == 'set') {
		if(validate_setRules(document.getElementById("form1"))){
		  ruleType=getFieldValue(document.getElementById("form1").elements["ruleType"]);
		  if(ruleType=='1'&&validate_multiRules(document.getElementById("form1"))){
			document.getElementById("ActionMethod").value = 'set';
			setFormDisabled("form1");
			document.getElementById("form1").submit();
		  }
		  if(ruleType=='0'){
			document.getElementById("ActionMethod").value = 'set';
			setFormDisabled("form1");
			document.getElementById("form1").submit();
		  }
		}
	}
}
function changeRuleType(ruleType){
	var ruleTypeElement = null;
	divSigleClass = document.getElementById('sigleClass');
	divMultiClass = document.getElementById('multiClass');
	if(ruleType == '0'){
		divSigleClass.style.display="block";
		divMultiClass.style.display="none";
	} else if(ruleType == '1'){
		divSigleClass.style.display="none";
		divMultiClass.style.display="block";
	} else{
		divSigleClass.style.display="none";
		divMultiClass.style.display="none";
	}
}
function chgFromAmount(seqNo){
	if(seqNo>=0){
		currentFromAmount = document.getElementById('fromAmount' + seqNo);
		prevSeq = seqNo - 1;
		prevToAmount = document.getElementById('toAmount' + prevSeq);
		if(prevToAmount){
			prevToAmount.value = currentFromAmount.value;
		}
	}
}
function chgToAmount(seqNo){
	if(seqNo>=0){
		currentToAmount = document.getElementById('toAmount' + seqNo);
		nextSeq = seqNo + 1;
		nextFromAmount = document.getElementById('fromAmount' + nextSeq);
		if(nextFromAmount){
			nextFromAmount.value = currentToAmount.value;
		}
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/authorizationPreference.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.auth_pref" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="MDB Corporate Online Banking > Corp Preferences > Set Authorization Preferences"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="Set Authorization Preferences"/><!-- InstanceEndEditable --></td>
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
            <set:fieldcheck name="setRules" form="form1" file="auth_pref" />
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
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Txn_Type_Currency" defaultvalue="Transaction Type and Currency"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="Txn_Type" defaultvalue="Transaction Type"/></td>
                <td width="72%" class="content1"><set:data name='txnType' rb="app.cib.resource.common.txn_type"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Currency" defaultvalue="Currency Type"/></td>
                <td class="content1"><set:data name='currency' db="rcCurrencyCBS"/></td>
              </tr>             
            </table>
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="8" class="groupinput"><set:label name="Select_Auth_Type" defaultvalue="Select Authorization Type"/></td>
              </tr>
              <tr>
                <td colspan="2" class="content1"><input id="ruleType" onClick="changeRuleType('0')" name="ruleType" type="radio" value="0" <set:selected key='ruleType' equalsvalue='0' output='checked'/>><set:label name="Single_Class" defaultvalue="Single Class Authorization"/></td>
                <td class="content1" colspan="6" ><div name="sigleClass" id ="sigleClass">
                  <select name="singleLevel" id="singleLevel">
                    <option value="">
                      <set:label name="Select_Level" defaultvalue="Select a Level"/>
                      </option>
					  <set:rblist file="app.cib.resource.sys.auth_level">
                    <set:rboption name="singleLevel" />
					  </set:rblist>
                  </select>
                </div></td>
              </tr>
              <tr class="greyline">
                <td colspan="8" class="content1"><input id="ruleType" onClick="changeRuleType('1')" name="ruleType" type="radio" value="1" <set:selected key="ruleType" equalsvalue="1" output="checked"/>><set:label name="Multi_Class" defaultvalue="Multi Class Authorization"/>				</td>
              </tr>
			  </table>
			  <div name="multiClass" id="multiClass">
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="2" class="listheader1"><set:label name="Amount_Range" defaultvalue="Amount Range"/>(<set:data name='currency' db="rcCurrencyCBS"/>)</td>
                <td colspan="5" class="listheader1"><set:label name="Number_Approver" defaultvalue="Number of Approver(s)"/></td>
                <td class="listheader1">
                  <input id="addMatrix" name="addMatrix" type="button" value="<set:label name='buttonAddNewRule' defaultvalue=' Add a New Rule'/>" onClick="doSubmit('addList')">
                </td>
              </tr>
              <tr>
                <td class="listheader1"><set:label name="From" defaultvalue="From"/></td>
                <td class="listheader1"><set:label name="To" defaultvalue="To"/></td>
                <td class="listheader1"><set:label name="Level_A" defaultvalue="Level A"/></td>
                <td class="listheader1"><set:label name="Level_B" defaultvalue="Level B"/></td>
                <td class="listheader1"><set:label name="Level_C" defaultvalue="Level C"/></td>
                <td class="listheader1"><set:label name="Level_D" defaultvalue="Level D"/></td>
                <td class="listheader1"><set:label name="Level_E" defaultvalue="Level E"/></td>
                <td class="listheader1"><set:label name="Remove_from_Rules" defaultvalue="Remove"/></td>
              </tr>
			  <tr>
				<input id="seqno" name="seqno" type="hidden" value="">
			  </tr>
			  <set:list name="ruleList" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1"><input id="fromAmount<set:listdata name='seq'/>" name="fromAmount<set:listdata name='seq'/>" type="text" onChange="chgFromAmount(<set:listdata name='seq'/>);" value="<set:listdata name='fromAmount' format='amount'/>" size="12"></td>
                <td class="listcontent1"><input id="toAmount<set:listdata name='seq'/>" name="toAmount<set:listdata name='seq'/>" type="text" onChange="chgToAmount(<set:listdata name='seq'/>);" value="<set:listdata name='toAmount' format='amount'/>" size="12"></td>
                <td class="listcontent1"><input id="levelA<set:listdata name='seq'/>" name="levelA<set:listdata name='seq'/>" type="text" value="<set:listdata name='levelA'/>" size="3"></td>
                <td class="listcontent1"><input id="levelB<set:listdata name='seq'/>" name="levelB<set:listdata name='seq'/>" type="text" value="<set:listdata name='levelB'/>" size="3"></td>
                <td class="listcontent1"><input id="levelC<set:listdata name='seq'/>" name="levelC<set:listdata name='seq'/>" type="text" value="<set:listdata name='levelC'/>" size="3"></td>
                <td class="listcontent1"><input id="levelD<set:listdata name='seq'/>" name="levelD<set:listdata name='seq'/>" type="text" value="<set:listdata name='levelD'/>" size="3"></td>
                <td class="listcontent1"><input id="levelE<set:listdata name='seq'/>" name="levelE<set:listdata name='seq'/>" type="text" value="<set:listdata name='levelE'/>" size="3"></td>
                <td align="center" class="listcontent1">
				<input id="ruleType1" name="deleteMatrix" type="button" value="<set:label name='buttonRemove' defaultvalue=' Remove from Rules '/>" onClick="doSubmit('deleteList','<set:listdata name='seq'/>')"></td>
              </tr>
			  </set:list>
            </table>
			</div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" class="sectionbutton"><input id="applyChange" name="applyChange" type="button" value="<set:label name='buttonOK' defaultvalue='Save Settings'/>" onClick="doSubmit('set')">
                      <input name="buttonReturn" id="buttonReturn" type="button" value="<set:label name='buttonReturn' defaultvalue=' Return '/>" onClick="doSubmit('return')">  			  	  <input id="ActionMethod" name="ActionMethod" type="hidden" value="addList">				  <input id="corpId" name="corpId" type="hidden" value="<set:data name='corpId' />"></td>
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
