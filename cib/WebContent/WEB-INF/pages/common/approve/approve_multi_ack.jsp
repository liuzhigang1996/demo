<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.flow.approve">
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
}
function doAckReturn(){
  setFormDisabled("form1");
  document.form1.submit();
}
//add by su_jj 20110308
function divMessage2Show(divId){
	var popUp = document.getElementById(divId);
	var pop_div = document.getElementById("pub_div");
	//不允许同时弹出两个div
	if(pop_div.value != ""){
		document.getElementById(pop_div.value).style.display = "none";
	}
    if(popUp.style.display=='none'){
	    popUp.style.display="block";
	    pop_div.value = divId;
		var x = event.clientX;
		var y = event.clientY;
		popUp.style.right = document.body.clientWidth - x + 39;
		popUp.style.top = y + 12; 
		//popUp.style.left = document.body.clientWidth/2 - popUp.offsetWidth/2;
		//popUp.style.top = ((document.body.clientHeight/2 - popUp.offsetHeight/2)<0)?0:(document.body.clientHeight/2-popUp.offsetHeight/2);   
	}else{
	    popUp.style.display="none";
	    pop_div.value = "";
	}
	document.getElementById("aLink").value = "a_" + divId;
}
document.onclick=check;
function check(e){
   var pub_value = document.getElementById("pub_div").value
   var aLink_value = document.getElementById("aLink").value
   if(pub_value != null && pub_value != ""){
	   var target = (e && e.target) || (event && event.srcElement);
	   var obj = document.getElementById(pub_value);
	   var buttonObj = document.getElementById("button_ok");
	   var aObj = document.getElementById(aLink_value);
	   if(target==buttonObj){
	   		obj.style.display='';
	   }
	   else if(target==aObj){
	   		obj.style.display='';
	   }
	   else if(target!=obj){
	   		obj.style.display='none';
	   }
   }
}
function hiddenDiv(){
	var pub_value = document.getElementById("pub_div").value;
	if(pub_value != null && pub_value != ""){
		document.getElementById("pub_div").value = "";
		document.getElementById(pub_value).style.display='none';
	}
}
//su_jj end#
</script>
<style type="text/css">
.popupcontent{ 
	position: absolute; 
	align: center;
	overflow: hidden;
	width:300px;
	height:auto;
	/*top:120px;
	left:370px;*/
	border:2px solid #EE0000; 
	background-color:#F4F4F4; 
	padding:15px;
} 
</style>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/approve.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.flow.approve" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --> <set:label name="navigationTitle_multiack" defaultvalue="MDB Corporate Online Banking > Authorization > Multi Approval"/> <!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --> <set:label name="functionTitle_multiack" defaultvalue="AUTHORIZE MULTI TRANSACTIONS"/> <!-- InstanceEndEditable --></td>
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
		  <form action="/cib/approve.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			  <input name="pub_div" type="hidden" id="pub_div" value="">
			  <input name="aLink" type="hidden" id="aLink" value="">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="2" class="groupack"><set:label name="Acknowledgement" defaultvalue="Acknowledgement"/></td>
                </tr>
				<!--
                <tr>
                  <td width="28%" class="label1"><set:label name="Ack_Status" defaultvalue="Status"/></td>
                  <td width="72%" class="content1"><set:label name='ackStatusAccepted' defaultvalue="Accepted"/></td>
                </tr>
				-->
                <tr>
                  <td width="28%" class="label1"><set:label name="Work_Action" defaultvalue="Operation"/></td>
                  <td width="72%" class="content1"><set:data name="act" rb="app.cib.resource.flow.approve_action"/></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1"><set:label name="Proc_Create_Time" defaultvalue="Request Time"/></td>
                  <td class="listheader1"><set:label name="Txn_Type" defaultvalue="Type"/></td>
                  <td class="listheader1"><set:label name="Proc_Creator_Name" defaultvalue="Requestor"/></td>
                  <td class="listheader1"><set:label name="Trans_Desc" defaultvalue="Description"/></td>
                  <set:if name= "FinanceFlag" condition="equals" value = "Y">
                  <td align="center" class="listheader1"><set:label name="Currency" defaultvalue="CCY"/></td>
                  <td align="right" class="listheader1"><set:label name="Amount" defaultvalue="Amount"/></td>
                  </set:if>
                  <td align="center" class="listheader1"><set:label name="Approve_Include" defaultvalue="Can Approve"/></td>
                  <td class="listheader1"><set:label name="Approve_Success" defaultvalue="Success"/></td>
                  <td class="listheader1"><set:label name="Approve_Status" defaultvalue="Transaction Status"/></td>
                </tr>
                <set:list name="selectedWorkList">
                <tr class="<set:listclass class1='' class2='greyline' />">
                  <td class="listcontent1"><set:listdata name="procCreateTime" format='datetime'/></td>
                  <td class="listcontent1"><set:listdata name="txnType" rb="app.cib.resource.common.subtype"/></td>
                  <td class="listcontent1"><set:listdata name="procCreatorName"/></td>
                  <td class="listcontent1"><set:listdata name="transDesc"/></td>
                  <set:if name= "FinanceFlag" condition="equals" value = "Y">
                  <td class="listcontent1" align="center"><set:listif name="ruleFlag" condition="equals" value="0"><set:listdata name="currency" db="rcCurrencyCBS"/></set:listif><set:listif name="ruleFlag" condition="equals" value="1"><set:listdata name="toCurrency" db="rcCurrencyCBS"/></set:listif></td>
                  <td class="listcontent1" align="right"><!--<set:listif name="ruleFlag" condition="equals" value="0"><set:listdata name="amount" format='amount'/></set:listif><set:listif name="ruleFlag" condition="equals" value="1"><set:listdata name="toAmount" format='amount'/></set:listif>-->
                  <!-- modify by chen_y 2016-10-13 for CR192 bob batch begin -->
                   <set:listif name="txnType" condition="equals" value="AUTOPAY_ADD">
                    <set:listif name='amount' condition='equals' value='9.999999999E9'>
                        <set:label name="FULL_PAYMENT" defaultvalue="Full Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                    </set:listif>
                    <set:listif name='amount' condition='notequals' value='9.999999999E9'>
                        <set:listif name='amount' condition='equals' value='0.0'>
                            <set:label name="Minimum_Payment" defaultvalue="Minimum Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                        </set:listif>
                        <set:listif name='amount' condition='notequals' value='0.0'>
                            <set:listdata name='amount' format='amount' />
                        </set:listif>
                    </set:listif>
                </set:listif>
                <set:listif name="txnType" condition="equals" value="AUTOPAY_EDIT">
                    <set:listif name='amount' condition='equals' value='9.999999999E9'>
                        <set:label name="FULL_PAYMENT" defaultvalue="Full Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                    </set:listif>
                    <set:listif name='amount' condition='notequals' value='9.999999999E9'>
                        <set:listif name='amount' condition='equals' value='0.0'>
                            <set:label name="Minimum_Payment" defaultvalue="Minimum Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                        </set:listif>
                        <set:listif name='amount' condition='notequals' value='0.0'>
                            <set:listdata name='amount' format='amount' />
                        </set:listif>
                    </set:listif>
                </set:listif>
                <set:listif name="txnType" condition="equals" value="AUTOPAY_DELETE">
                    <set:listif name='amount' condition='equals' value='9.999999999E9'>
                        <set:label name="FULL_PAYMENT" defaultvalue="Full Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                    </set:listif>
                    <set:listif name='amount' condition='notequals' value='9.999999999E9'>
                        <set:listif name='amount' condition='equals' value='0.0'>
                            <set:label name="Minimum_Payment" defaultvalue="Minimum Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                        </set:listif>
                        <set:listif name='amount' condition='notequals' value='0.0'>
                            <set:listdata name='amount' format='amount' />
                        </set:listif>
                    </set:listif>
                </set:listif>
                <set:listif name="txnType" condition="notequals" value="AUTOPAY_ADD">
	                <set:listif name="txnType" condition="notequals" value="AUTOPAY_EDIT">
		                <set:listif name="txnType" condition="notequals" value="AUTOPAY_DELETE">
			                <set:listif name="txnType" condition="equals" value="CHEQUE_BOOK_REQUEST"></set:listif>
			                <set:listif name="txnType" condition="notequals" value="CHEQUE_BOOK_REQUEST">
			                	<set:listif name="ruleFlag" condition="equals" value="0"><set:listdata name="amount" format='amount'/></set:listif>
			                	<set:listif name="ruleFlag" condition="equals" value="1"><set:listdata name="toAmount" format='amount'/></set:listif>
			                </set:listif>
		                </set:listif>
	                </set:listif>
                </set:listif>
                  </td>
                  </set:if>
                  <td align="center" class="listcontent1"><set:listdata name="checkout"/></td>
				  <set:listif name="successFlag" condition="equals" value="Y" >
				  <td class="listcontent1"><set:listdata name="successFlag" rb="app.cib.resource.flow.multi_approve_status"/></td>
				  </set:listif>
				  <set:listif name="successFlag" condition="equals" value="N" >
				  <td class="listcontent1"><a href="#" id="a_<set:listdata name='div_id'/>" onClick="divMessage2Show('<set:listdata name="div_id"/>');"><set:listdata name="successFlag" rb="app.cib.resource.flow.multi_approve_status"/></a></td>
				  <div class="popupcontent" style="display:none" id="<set:listdata name='div_id'/>">
				  <strong><set:label name='RejectedReason' defaultvalue='Rejected Reason'/></strong>
				  <br/>
				  <set:listdata name="errorMsg"/>
				  <br/>
				  <br/>
				  <center><input name="button_close" type="button" id="button_close" value="<set:label name='Close' defaultvalue='Close'/>" onClick="hiddenDiv()" ></center>
				  </div>
				  </set:listif>
                  <td class="listcontent1"><set:listdata name="procStatus" rb="app.cib.resource.flow.process_status_enquiry"/></td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input id="buttonOk" name="buttonOk" type="button" value="<set:label name='buttonOK' defaultvalue='OK'/>" onClick="doAckReturn()">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="list">
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
