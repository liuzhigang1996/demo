<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.chequeBookRequest">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8;IE=7; IE=EDGE">
<!-- InstanceBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js?v=20141217"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js?v=20141217"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	if(language == "en_US"){
		document.getElementById("showENG").style.display = "block";
	}else{
		document.getElementById("showCHN").style.display = "block";
	}
	//modified by lzg 20190606
	/* Add by long_zg 2019-05-31 UAT6-464 COB：COB：申請支票報錯，交易記錄也不見了 begin */
//	var  Pickup_Type = document.getElementsByName("Pickup_Type");
	/* Add by long_zg 2019-05-31 UAT6-464 COB：COB：申請支票報錯，交易記錄也不見了 end */
	
//	if(!"<set:data name='Pickuptype' />" == ""){
		//add by linrui 20180324
//		var pickuptype = '<set:data name='Pickuptype' />';
//		if(pickuptype =='P'){
//			Pickup_Type[0].checked=true;
//		}else{
//				Pickup_Type[1].checked=true;
//		}
//		changeRange(pickuptype);
//	}else{
		//changeRange("P");
//		Pickup_Type[0].checked=true;
//		changeRange("P");
//	}
	changeRange('P');
	//add by linrui 20190514 for bug UAT6-106
	if(!"<set:data name='accountNo' />" == ""){
		//changeFromAcc("<set:data name='accountNo' />");
		var accountNo = document.getElementById("accountNo");
		var payCurrency = document.getElementById("payCurrency");
		for(var i=0;i<accountNo.options.length;i++){
    		if(accountNo[i].value=='<set:data name='accountNo' />'){  
        		accountNo[i].selected=true;   
    		}  
		}  
		showToCcyList(accountNo, payCurrency,'getToCcy', accountNo.value);
		if(!"<set:data name='payCurrency' />" == ""){
			for(var i=0;i<payCurrency.options.length;i++){
	    		if(payCurrency[i].value=='<set:data name='payCurrency' />'){  
	        		payCurrency[i].selected=true;   
	    		}  
			}  
		}
		changeToCcy(payCurrency.value);
	}
	
}
function changeToCcy(toCcy){
	document.getElementById("payCurrency").value= toCcy;
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
	document.getElementById("accountNo").value= toAcct;
	if ((originSelect.value != null) && (originSelect.value != '')) {
		   var params = getParams(originSelect, targetSelect);
		   var url = '/cib/jsax?serviceName=AccInTxnService&action='+action+'&'+ params+ '&showToAcc=' + toAcct +'&language=' + language;
		   getMsgToSelect(url,'', null, false,language);		   
	}
	//add by lzg 20190612
	var payCurrency = document.getElementById("payCurrency");
	if(payCurrency.options.length == 2){
		payCurrency[1].selected = true;
		changeToCcy(payCurrency[1].value);
	}
	//add by lzg end 	
}
//bug UAT6-106
function doSubmit(){
	if(validate_add(document.getElementById("form1"))){
		document.getElementById("form1").submit() ;
	}
}
//add by linrui 20180324
function changeRange(range){
	if(range == 'P'){
 		document.getElementById("mailName1").value='';
 		document.getElementById("mailName2").value='';
		document.getElementById("mailAdress1").value='';
		document.getElementById("mailAdress2").value='';
		//document.getElementById("mailAdress3").value='';
		//document.getElementById("mailAdress4").value='';
		//document.getElementById("mailAdress5").value='';
		//document.getElementById("mailAdress6").value='';
		document.getElementById("Pickuptype").value='P';
		document.getElementById("mailName1").disabled = true;
		document.getElementById("mailName2").disabled = true;
		document.getElementById("mailAdress1").disabled = true;
		document.getElementById("mailAdress2").disabled = true;
		document.getElementById("mailAdress3").disabled = true;
		//document.getElementById("mailAdress4").disabled = true;
		//document.getElementById("mailAdress5").disabled = true;
		//document.getElementById("mailAdress6").disabled = true;
		document.getElementById("mailName").style.display = 'none';
		document.getElementById("mailAdress").style.display = 'none';
	}else if(range == 'S'){
		//document.getElementById("mailName1").value='';
 		//document.getElementById("mailName2").value='';
		//document.getElementById("mailAdress1").value='';
		//document.getElementById("mailAdress2").value='';
		//document.getElementById("mailAdress3").value='';
		//document.getElementById("mailAdress4").value='';
		//document.getElementById("mailAdress5").value='';
		//document.getElementById("mailAdress6").value='';
		document.getElementById("Pickuptype").value='S';
		document.getElementById("mailName1").disabled = false;
		document.getElementById("mailName2").disabled = false;
		document.getElementById("mailAdress1").disabled = false;
		document.getElementById("mailAdress2").disabled = false;
		document.getElementById("mailAdress3").disabled = false;
		//document.getElementById("mailAdress4").disabled = false;
		//document.getElementById("mailAdress5").disabled = false;
		//document.getElementById("mailAdress6").disabled = false;
		document.getElementById("mailName").style.display = '';
		document.getElementById("mailAdress").style.display = '';
	}
}
//add by linrui 20190319
//function getCurrency(){
	//currentAccount
//	var debitAccount = document.getElementById("accountNo");
//	var accountCcy = debitAccount.options[debitAccount.selectedIndex].innerHTML;
//	var arr = accountCcy.split("-");
//	accountCcy = arr[1].substring(1,4);
//	document.getElementById("payCurrency").value = accountCcy;
//}
//end
//end
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/chequeBookRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.chequeBookRequest" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
    <set:label name="navigationTitleRequest" defaultvalue="BANK Online Banking >Cheque > Cheque Book Request"/>
	<!-- InstanceEndEditable --></td>
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
        <set:label name="chequeBookRequestTitle" defaultvalue="CHEQUE BOOK REQUEST"/>
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
		  <form action="/cib/chequeBookRequest.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            
			<set:messages width="100%" cols="1" align="center"/>
            <set:fieldcheck name="add" form="form1" file="cheque_book_request" />
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Fill_Info" defaultvalue="Enter request information"/></td>
              </tr>
          		<tr>
                <td class="label1" width="28%"><span class = "xing">*</span><set:label name="Current_Account" defaultvalue="Current Account"/></td>
                <td class="content1" width="72%">
                <%--<select id="accountNo" name="accountNo">
				<option selected value=" "><set:label name="Please_Select" defaultvalue="----- Please Select an Account ------"/></option>
                  <set:rblist db="caoaAccountByUser">
                    <set:rboption name="currentAccount"/>
                  </set:rblist>
                </select>
                --%>
                <%--<select name="accountNo" id="accountNo" nullable="0" onChange="getCurrency()">
					<option value="0" selected><set:label name="Please_Select"/></option>
                    <set:list name="transferuser"> <option value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_INFO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_INFO' />
                      </option>
                  </set:list>
                </select>
                --%>
                <%--mod by linrui for get mul Ccy 20190514--%>
                <select id="accountNo" name="accountNo" nullable="0" onChange="showToCcyList(this, $('payCurrency'),'getToCcy', this.value);">
				 <option value="0" selected><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
				   <set:list name="transferuser"> <option  value="<set:listdata  name='ACCOUNT_NO' />" 
                           <set:listselected key='ACCOUNT_NO' equals='selectToAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_NO' />
                      </option>
                  </set:list>
				  </select>
                <%--mul ccy end --%>
                </td>
              </tr>
              
              <%--add by linrui for get Ccy from host 20190514--%>
              <tr class="greyline">
                  <td class="label1"><span class = "xing">*</span><set:label name="From_ccy" defaultvalue="From Currency"/></td>
                  <td class="content1" colspan="3" ><table><tr><td><select name="payCurrency" id="payCurrency" nullable="0" onChange="changeToCcy(this.value);">
                      <option value="" selected><set:label name="select_ccy" defaultvalue="----Please select Currency----"/></option>
                    </select>  </td></tr></table>
                  </td>
              </tr>
              <%--add by linrui for get Ccy end--%>
                 
			   <%-- zhushi default 1
			   <tr>
                <td class="label1"><span class = "xing">*</span><set:label name="No_Of_Books" defaultvalue="No. Of Books"/></td>
                <td class="content1"><input id="noOfBook" name="noOfBook" type="text" maxlength="2" value="<set:data name='noOfBook' format='amount' pattern='#'  />"> (<set:label name="Maximum" defaultvalue="Maximum: 10"/>)</td>
              </tr>
			  --%>
			  
			  <%--<tr>
                <td class="label1"><set:label name="Pickup_Branch" defaultvalue="Pickup Branch"/></td>
                <td class="content1">
                <select id="pickupBranchCode" name="pickupBranchCode">
				<option selected value=" "><set:label name="Please_Select" defaultvalue="----- Please Select an Account ------"/></option>
                  <set:rblist db="branch">
                    <set:rboption name="pickupBranchCode"/>
                  </set:rblist>
                </select>
                </td>
              </tr>
            --%>
            <!-- modified by lzg 20190606 -->
            <!--<tr  class="greyline">
               
                <td class="label1" ><set:label name="Pickup_Type_Label" defaultvalue="Pickup Type"/></td>
                <td>
                    <table>
                    <td class="content1" >
                	<input type="radio" name="Pickup_Type" id="Pickup_Type" value="0" onClick="changeRange('P')"><set:label name="Pickup_Type_1" defaultvalue="PICK UP"/>
                	</td>  
                	<td class="content1"  >
                	<input type="radio" name="Pickup_Type"  id="Pickup_Type" value="0" onClick="changeRange('S')"><set:label name="Pickup_Type_2" defaultvalue="LOCAL MAIL"/>
                	</td>               	 
                	</table>
                </td>
                           
            </tr>
            --><!-- modified by lzg 20190606 -->
             <tr id="mailName">
                <td class="label1"><set:label name="Local_Mail_Name" defaultvalue="Name"/></td>                  
                    <td class="content1">
                    <input id="mailName1" name="mailName1" type="text" value="<set:data name='mailName1'/>" size="50" maxlength="35">
                    <input id="mailName2" name="mailName2" type="text" value="<set:data name='mailName2'/>" size="50" maxlength="35">
                    </td>                                 
             </tr>
             <tr  class="greyline" id="mailAdress">
                <td class="label1"><set:label name="Local_Mail_Adress" defaultvalue="Adress"/></td>
                    <td class="content1">
                    <input id="mailAdress1" name="mailAdress1" type="text" value="<set:data name='mailAdress1'/>" size="60" maxlength="50">
                    <input id="mailAdress2" name="mailAdress2" type="text" value="<set:data name='mailAdress2'/>" size="60" maxlength="50">
                    
                    <input id="mailAdress3" name="mailAdress3" type="text" value="<set:data name='mailAdress3'/>" size="60" maxlength="50">
                    <%--
                    <input id="mailAdress4" name="mailAdress4" type="text" value="<set:data name='mailAdress4'/>" size="50" maxlength="35">
                    <input id="mailAdress5" name="mailAdress5" type="text" value="<set:data name='mailAdress5'/>" size="50" maxlength="35">                    
                    <input id="mailAdress6" name="mailAdress6" type="text" value="<set:data name='mailAdress6'/>" size="50" maxlength="35">
                    --%>
                    </td>  
             </tr>
            <!-- end -->
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator"></td>
              </tr>
            </table>
            
             <%--<set:assignuser selectFlag='N'/>--%>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
			  <td colspan="2" class="sectionbutton">
                <input id="submit1" name="submit1" type="button" value="<set:label name='buttonOK' defaultvalue=' OK ' />" tabindex="3"  onClick="doSubmit();">
<!--            <input id="bottonReset" name="bottonReset" type="reset" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="add">
				<!-- add by linrui for change pickup tpye 20180324 -->
                <input id="Pickuptype" name="Pickuptype" type="hidden" value="<set:data name='Pickuptype'/>">
                <input id="payCurrency" name="AccountCcy" type="hidden" value="">
                <input id="noOfBook" name="noOfBook"type="hidden" value="1"><%--zanshi 20190717 --%>
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
