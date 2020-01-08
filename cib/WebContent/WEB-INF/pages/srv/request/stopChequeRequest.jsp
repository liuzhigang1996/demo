<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.stop_check_request">
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
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	if(language == "en_US"){
		document.getElementById("showENG").style.display = "block";
	}else{
		document.getElementById("showCHN").style.display = "block";
	}
   	putFieldValues("reason", "<set:data name='reason' />");
	if('<set:data name='reason' />' == '1'){
		document.form1.stopReason.disabled = false;
		document.form1.stopOtherReason.disabled = true;
	} else if('<set:data name='reason' />' == '2') {
		document.form1.stopReason.disabled = true;
		document.form1.stopOtherReason.disabled = false;
	}else{
		document.form1.stopReason.disabled = false;
		document.form1.stopOtherReason.disabled = true;
	}
	//add by linrui 20190514 for bug UAT6-106
	if(!"<set:data name='currentAccount' />" == ""){
		//changeFromAcc("<set:data name='accountNo' />");
		var currentAccount = document.getElementById("currentAccount");
		var currentAccountCcy = document.getElementById("currentAccountCcy");
		for(var i=0;i<currentAccount.options.length;i++){
    		if(currentAccount[i].value=='<set:data name='currentAccount' />'){  
    			currentAccount[i].selected=true;   
    		}  
		}  
		showToCcyList(currentAccount, currentAccountCcy,'getToCcy', currentAccount.value);
		if(!"<set:data name='currentAccountCcy' />" == ""){
			for(var i=0;i<currentAccountCcy.options.length;i++){
	    		if(currentAccountCcy[i].value=='<set:data name='currentAccountCcy' />'){  
	    			currentAccountCcy[i].selected=true;   
	    		}  
			}  
		}
		changeToCcy(currentAccountCcy.value);
	}
}
function changeToCcy(toCcy){
	document.getElementById("currentAccountCcy").value= toCcy;
}
//add by linrui for mul-ccy select begin 20190426
function getParams(originSelect, targetSelect) {
	var targetType = 'targetType=object';
	var targetId = 'targetId=' + originSelect.id;
	var originValue = 'originValue=' + originSelect.value;
	var subListId = 'subListId=' + targetSelect.id;
	var params = '';
	params = params + targetType + '&' + targetId + '&' + originValue + '&' + subListId;
	return params;
}
function showToCcyList(originSelect, targetSelect,action, toAcct){
	//action check ccy
	document.getElementById("currentAccount").value= toAcct;
	if ((originSelect.value != null) && (originSelect.value != '')) {
		   var params = getParams(originSelect, targetSelect);
		   var url = '/cib/jsax?serviceName=AccInTxnService&action='+action+'&'+ params+ '&showToAcc=' + toAcct +'&language=' + language;
		   getMsgToSelect(url,'', null, false,language);		   
	}
	//add by lzg 20190612
	var currentAccountCcy = document.getElementById("currentAccountCcy");
	if(currentAccountCcy.options.length == 2){
		currentAccountCcy[1].selected = true;
		changeToCcy(currentAccountCcy[1].value);
	}
	//add by lzg end  	
}
function doSubmit()
{ 
	if(validate_stop_check(document.getElementById("form1"))){
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
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/stopChequeRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.stop_check_request" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitleStop" defaultvalue="BANK Online Banking >Cheque > Bank Draft Request"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitleStop" defaultvalue="BANK DRAFT REQUEST"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/stopChequeRequest.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <set:messages width="100%" cols="1" align="center"/>
            <set:fieldcheck name="stop_check" form="form1" file="check_request" />
			<table border="0" cellpadding="0" cellspacing="0">
			  <tr>
			    <td>
			      <table border="0" cellpadding="0" cellspacing="0">
			        <tr>
			          <td class="ltab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
          <td class="tab1_o">
            <set:label name="Cheque_single" defaultvalue=" Cheque Stop Request"/></td>
			     <td class="rtab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
		  </tr></table>		  </td>
			     <td>
			     <!-- mod by linrui for oracle -->
			     <%--
			       <table border="0" cellpadding="0" cellspacing="0">
			         <tr>
			           <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>         
          <td class="tab1_c">
            <a onClick="toTargetFormgoToTarget('/cib/stopChequeBatchRequest.do?ActionMethod=uploadFileLoad')" href="#"><set:label name="Cheque_batch" defaultvalue=" Cheque Stop Batch Request"/></a></td>			    
			     <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
		  </tr></table>			     --%>
		  <!-- end --> 
		        </td>
			     <td>
			       			      </td>
			     </tr>
			  </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="4" class="groupinput"><set:label name="Fill_Info" defaultvalue="Enter request information"/></td>
              </tr>
          <tr >
                <td class="label1"><span class = "xing">*</span><set:label name="Cheque_Number" defaultvalue="Cheque Number"/></td>
                <td class="content1" colspan="3"><input id="chequeNumber" name="chequeNumber" type="text" value="<set:data name='chequeNumber'/>" size="6" maxlength="6"></td>
              </tr>   
              <tr class="greyline">
                <td width="20%" valign="top" class="label1"><span class = "xing">*</span><set:label name="Account" defaultvalue="Account"/></td>
                <td class="content1" colspan="3" >
                <%--<select id="currentAccount" name="currentAccount" nullable="0" >
				<option selected value=" "><set:label name="Select_Account_Label" defaultvalue="----- Please Select an Account ------"/></option>
                  <set:rblist db="caoaAccountByUser">
                    <set:rboption name="currentAccount"/>
                  </set:rblist>
                </select>
                 --%>
                 
                 <%--mod by linrui for get mul Ccy 20190514--%>
                <select id="currentAccount" name="currentAccount" nullable="0" onChange="showToCcyList(this, $('currentAccountCcy'),'getToCcy', this.value);">
				 <option value="0" selected><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
				   <set:list name="transferuser"> <option  value="<set:listdata  name='ACCOUNT_NO' />" 
                           <set:listselected key='ACCOUNT_NO' equals='selectToAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_NO' />
                      </option>
                  </set:list>
				  </select>
                <%--mul ccy end --%>
                 
                 </td>
              </tr>
              <%--mul ccy for linrui 20190514 --%>
              <%--add by linrui for get Ccy from host 20190514--%>
              <tr>
                  <td class="label1"><span class = "xing">*</span><set:label name="From_ccy" defaultvalue="From Currency"/></td>
                  <td class="content1" colspan="3" ><table><tr><td><select name="currentAccountCcy" id="currentAccountCcy" nullable="0" onChange="changeToCcy(this.value);">
                      <option value="" selected><set:label name="select_ccy" defaultvalue="----Please select Currency----"/></option>
                    </select>  </td></tr></table>
                  </td>
              </tr>
              <%--add by linrui for get Ccy end--%>
              <%-- mul ccy end--%>
              
			   <tr class="greyline">
                <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Amount" defaultvalue="Amount"/></td>
                <td class="content1" colspan="3"><input id="amount" name="amount" type="text" value="<set:data name='amount' format="amount"/>" size="15" maxlength="20"></td>
              </tr>
			  <tr>
                <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Beneficiary_Name" defaultvalue="Beneficiary Name"/></td>
                <td class="content1" colspan="3"><input id="beneficiaryName" name="beneficiaryName" type="text" value="<set:data name='beneficiaryName'/>" size="40" maxlength="40"></td>
              </tr>
			  <tr class="greyline">
                <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Issue_Date" defaultvalue="Issue date"/></td>
                <td class="content1" colspan="3"><input id="issueDate" name="issueDate" type="text" value="<set:data name='issueDate'/>" size="10" maxlength="10"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "{scwShow(document.getElementById('issueDate'), this,language)};" > </td>
              </tr> 
			   <tr>
                <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Expiry_Date" defaultvalue="Expiry date"/></td>
                <td class="content1" colspan="3"><input id="expiryDate" name="expiryDate" type="text" value="<set:data name='expiryDate'/>" size="10" maxlength="10"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "{scwShow(document.getElementById('expiryDate'), this,language)};" > </td>
              </tr>         
			   <tr class="greyline">
                <td class="label1"  rowspan="2"><span class = "xing">*</span><set:label name="Reason_for_stop" defaultvalue="Reason for stop"/></td>
                <td class="label1" colspan="3"><input id="reason" type="radio" name="reason" checked value="1" onClick="this.form.stopOtherReason.disabled = true; this.form.stopReason.disabled = false;" >
				<set:label name="Specific" defaultvalue="Specific"/>&nbsp;&nbsp;&nbsp;&nbsp;
				      <%--<select id="stopReason" name="stopReason" nullable="0">
					    <option selected value=""><set:label name="Select_Reason_Label" defaultvalue="----- Please Select a Reason ------"/></option>
                        <set:rblist  db="stopReason">
                          <set:rboption name="stopReason"/>
                        </set:rblist>
                      </select> --%>  
                      <select id="stopReason" name="stopReason"  nullable="0">
                      	<set:rblist file="app.cib.resource.srv.stop_reason"> <set:rboption name="stopReason"/> </set:rblist>		 
                    </select>
                      
                              
				</td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><input id="reason" type="radio" name="reason" value="2" onClick="this.form.stopReason.disabled = true;this.form.stopOtherReason.disabled = false" >
                <set:label name="Other" defaultvalue="Other"/>&nbsp;&nbsp;&nbsp;&nbsp;<input id="stopOtherReason" name="stopOtherReason" type="text" value="<set:data name='stopOtherReason'/>" size="40" maxlength="40"></td>
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
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="stop">
				<!-- add by lzg 20190624 for pwc-->
					<set:singlesubmit/>
				  <!-- add by lzg end -->
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
<!-- add by lzg 20190612 -->
	<div style = "margin-left: 1%;"><%@ include file="/common/otherTips.jsp" %></div>
<!-- add by lzg end -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>
</body>
</set:loadrb>
<!-- InstanceEnd --></html>
