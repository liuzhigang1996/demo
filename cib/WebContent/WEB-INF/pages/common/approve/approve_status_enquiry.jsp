<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
<SCRIPT language=JavaScript src="/cib/javascript/common1.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->

<!-- modify by long_zg 2014-03-17 for IE11 begin-->
<!--<SCRIPT language=JavaScript src="/cib/javascript/jsax.js"></SCRIPT>-->
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<!-- modify by long_zg 2014-03-17 for IE11 end-->

<script language="JavaScript" src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	if('<set:data name="rangeType"/>' != 'range') {
		document.form1.rangeType[0].checked = true;
		document.form1.rangeType[0].onclick();
	} else {
		document.form1.rangeType[1].checked = true;
		document.form1.rangeType[1].onclick();
	}
	//add by linrui 20190916
	var sortOrder='<set:data name="sortOrder"/>';
	if(sortOrder == '2'){
	    document.getElementById("sortOrder")[1].selected = true;
	}else{
	    document.getElementById("sortOrder")[0].selected = true;
    }
    //end
    <set:if name="viewAllTransFlag" condition="equals" value="1">
		showUserList($('roleId'), $('userId'), 
			function(){
				if('<set:data name="userId"/>' != '')
					$('userId').value = '<set:data name="userId"/>';
			}
		);
    </set:if>
}
function doSubmit(arg){
	if(true){//(validate_payment_history(document.getElementById("form1"))){
		//document.getElementById("submit1").disabled=true;
		if (arg == 'list') {
			document.form1.ActionMethod.value = 'statusEnquiry';
		}
		document.getElementById("form1").submit();
		setFormDisabled("form1");
		//setFieldEnabled("add");
	}
}
function changeRange(range){
	if(range == 'all'){
		document.getElementById("dateFrom").value='';
		document.getElementById("dateTo").value='';
		document.getElementById("dateFrom").disabled = true;
		document.getElementById("dateTo").disabled = true;
		document.getElementById("dateRange").value=0;
		document.getElementById("dateRange").disabled = true;
	}else{
		document.getElementById("dateFrom").disabled = false;
		document.getElementById("dateTo").disabled = false;
		document.getElementById("dateRange").disabled = false;
	}
}
function showUserList(originSelect, targetSelect, callback){
	if(callback == 'undefined'){
		callback = null;
	}
	var params = getParams(originSelect, targetSelect);
	var url = '/cib/jsax?serviceName=UserListService&' + params;
	getMsgToSelect(url,'', callback,true,language);
}
function getParams(originSelect, targetSelect) {
	var targetType = 'targetType=object';
	var targetId = 'targetId=' + originSelect.id;
	var originValue = 'originValue=' + originSelect.value;
	var subListId = 'subListId=' + targetSelect.id;
	var params = '';
	params = params + targetType + '&' + targetId + '&' + originValue + '&' + subListId;
	return params;
}
function doView(wId){
  //setFormDisabled("form1");
  document.location.href = "/cib/approve.do?ActionMethod=view&workId="+wId;
}
function doCancelTransaction(wId){
  document.location.href = "/cib/approve.do?ActionMethod=cancelLoad&workId="+wId;
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/approve.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.flow.approve" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --> <set:label name="navigationTitle_status" defaultvalue="MDB Corporate Online Banking > Authorization > Authorization State List"/> <!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --> <set:label name="functionTitle_status" defaultvalue="AUTHORIZATION STATE LIST"/> <!-- InstanceEndEditable --></td>
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
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/></td>
                </tr>
              </table>
              <set:if name="viewAllTransFlag" condition="equals" value="1">
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table border=0 bgcolor=#808080 cellpadding=0 cellspacing=1 width=100%>
                <tr>
                  <td bgcolor=#FFFFFF class=content><table border=0 cellpadding=5 cellspacing=0 width=100% bgcolor=#FFFFFF>
                      <tr >
                        <td class=content valign=top width=140><set:label name="period" defaultvalue="period"/></td>
                        <td class=content colspan="4"><input id="rangeType" name="rangeType" type="radio" value="all" checked onClick="changeRange('all')">
&nbsp; <set:label name="all" defaultvalue="all"/> <br>
                          <input id="rangeType" name="rangeType" type="radio" value="range" onClick="changeRange('range')">
&nbsp; <set:label name='date_from' defaultvalue='date_from'/>&nbsp;
                          <input name="dateFrom" type="text" id="dateFrom" size="12" maxlength="10" disabled value="<set:data name='dateFrom'/>">
                          <span class="content1"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" ></span>&nbsp;&nbsp;&nbsp; <set:label name='date_to' defaultvalue='date_to'/>&nbsp;
                          <input name="dateTo" type="text" id="dateTo" size="12" maxlength="10" disabled value="<set:data name='dateTo'/>">
                          <span class="content1"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" ></span> 
						  <select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));">
				<option value='0'><set:label name="select_short_cut_date" defaultvalue="----- Select a Date Short-cut -----"/><!-- add by linrui for mul-language --></option>
                  <set:rblist file="app.cib.resource.common.date_selection">
                    <set:rboption/>
                  </set:rblist>
                </select></td>
                      </tr>
                  <!-- add by linrui 20190916 -->
                  <tr>
                  <td class=content valign=top width=140><set:label name="Sort_Order" defaultvalue="Sort_Order"/></td>
                    <td class=content colspan="4">
					<span class="content1">
                    <select id="sortOrder" name="sortOrder" >
                      <set:rblist file="app.cib.resource.common.sort_order"> <set:rboption/> </set:rblist>
                    </select>
                    </span></td>                    
                  </tr>
                  <!-- end -->
                      <tr class="greyline">
                        <td class=content valign=top><set:label name="role" defaultvalue="role"/></td>
                        <td class=content colspan="3">
						  <select name="roleId" id="roleId" onChange="showUserList(this, $('userId'));">
                            <option value="0" selected><set:label name="all"/></option>
					  		<set:if name="corpType" value="1" condition="equals">
                      			<set:rblist file="app.cib.resource.common.corp_role"> <set:rboption name="roleId"/> </set:rblist>
					  		</set:if>
					  		<set:if name="corpType" value="2" condition="equals">
                      			<set:rblist file="app.cib.resource.common.corp_role_opt2"> <set:rboption name="roleId"/> </set:rblist>
					  		</set:if>
					  		<set:if name="corpType" value="3" condition="equals">
                      			<set:rblist file="app.cib.resource.common.corp_role_opt3"> <set:rboption name="roleId"/> </set:rblist>
					  		</set:if>
					  		<set:if name="corpType" value="4" condition="equals">
                      			<set:rblist file="app.cib.resource.common.corp_role_opt4"> <set:rboption name="roleId"/> </set:rblist>
					  		</set:if>
                          </select>
                        </td>
                      </tr>
                      <tr class="">
                        <td class=content valign=top><set:label name="user_id" defaultvalue="user_id"/></td>
                        <td class=content colspan="2">
						  <select name="userId" id="userId" nullable="0" >
                            <option value="0" selected><set:label name="all"/></option>
                          </select>
						</td>
                      </tr>
                      <tr class="greyline">
                        <td class=content valign=top><set:label name="Approve_Status" defaultvalue="Approve_Status"/></td>
                        <td class=content colspan="3">
						  <select name="procStatus" id="procStatus">
                        	<set:rblist file="app.cib.resource.flow.proc_status_view_all_trans">
                          		<set:rboption name="procStatus"/>
                        	</set:rblist>
                          </select>
                        </td>
                      </tr>
                    </table></td>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="53%" height="40" class="sectionbutton"><div align="right">
                      <input id="search" type="button" value="&nbsp;<set:label name='search' defaultvalue='search'/>&nbsp;" name="search" onClick="doSubmit('list')">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="list">
                    </div></td>
                  <td width="47%" height="40" class="sectionbutton">&nbsp;</td>
                </tr>
              </table>
              </set:if>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="100%" class="content">&nbsp;</td>
                  <td height="40"  valign="top" class="content1" align="right" ><table width="100" border="0" cellspacing="0" cellpadding="0">
                      <%--<tr>
                       <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=statusWorkList&fileName=approve_status_enquiry&columnTitles=<set:label name='Date'/>,<set:label name='Txn_Type'/>,<set:label name='user_id'/>,<set:label name='Approve_Progress'/><set:if name= 'FinanceFlag' condition='equals' value = 'Y'>,<set:label name='Currency'/>,<set:label name='Amount'/></set:if>,<set:label name='Approve_Status'/>,<set:label name='Action'/>,<set:label name='Deal_Memo'/>,&columnNames=dealEndTime||format@datetime,txnType||rb@app.cib.resource.common.subtype,workDealer,progressList,currencyDownload,amountDownload||format@amount,procStatus||rb@app.cib.resource.flow.process_status,dealAction||rb@app.cib.resource.flow.approve_action,dealMemo"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>
                        
                      </tr>
                    --%></table></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td width="8%" class="listheader1"><set:label name="Deal_End_Time" defaultvalue="Date &amp; Time"/></td>
                  <td width="8%" class="listheader1"><set:label name="Txn_Type" defaultvalue="Operation"/></td>
                  <td width="8%" class="listheader1"><set:label name="user_id" defaultvalue="user_id"/></td>
                  <td width="8%" class="listheader1"><set:label name="Approve_Progress" defaultvalue="Progress"/></td>
                  <set:if name= "FinanceFlag" condition="equals" value = "Y">
                  <td width="8%" class="listheader1"><set:label name="Currency" defaultvalue="CCY"/></td>
                  <td width="8%" class="listheader1"><set:label name="Amount" defaultvalue="Amount"/></td>
                  </set:if>
                  <td width="8%" class="listheader1"><set:label name="Approve_Status" defaultvalue="Status"/></td>
                  <!-- td class="listheader1"><set:label name="Action" defaultvalue="Action"/></td -->
                  <td width="8%" class="listheader1"><set:label name="Deal_Memo" defaultvalue="Observations"/></td>
                </tr>
                <set:list name="statusWorkList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline' />">
                  <td class="listcontent1"><set:listdata name="procCreateTime" format="datetime"/></td>
                  <td class="listcontent1"><a onClick="postToMainFrame('/cib/approve.do?ActionMethod=statusEnquiryView',{workId:'<set:listdata name='workId'/>',returnPage:'outstanding'})" href="#"><set:listdata name="txnType" rb="app.cib.resource.common.subtype"/></a></td>
                  <td class="listcontent1"><set:listdata name="workDealer"/></td>
                  <td class="listcontent1"><set:list name="progressList"> <set:listif name="FinishFlag" condition="equals" value="1"> <span class="textnormal"><set:listdata name="Dealer"/></span><br>
                    </set:listif> <set:listif name="FinishFlag" condition="equals" value="0"> <span class="textgray">&gt; <set:listdata name="Dealer" rb="app.cib.resource.flow.dealer_type"/></span><br>
                    </set:listif> </set:list> </td>
                  <set:if name= "FinanceFlag" condition="equals" value = "Y">
                  <td class="listcontent1">
                  <!-- modify by Li_zd 2016-08-18 for issuse_20160815 bob batch end -->
	              <set:listif name="ruleFlag" condition="equals" value="0"><set:listdata name="currency" db="rcCurrencyCBS"/></set:listif>
			      <set:listif name="ruleFlag" condition="equals" value="1"><set:listdata name="toCurrency" db="rcCurrencyCBS"/></set:listif>
                  </td>
                  <td class="listcontent1">
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
                  <td align="left" class="listcontent1"><set:listdata name="procStatus" rb="app.cib.resource.flow.process_status_enquiry"/> &nbsp; 
				  <set:listif name="cancelFlag" condition="equals" value="Y"> <br>
                    <input id="cancelButton" type="button" name="cancelButton" value="<set:label name='buttonCancel' defaultvalue='Cancel' />" onClick="doCancelTransaction('<set:listdata name='workId'/>')">
                   </set:listif> </td>
                  <!-- td class="listcontent1"><set:listdata name="dealAction" rb="app.cib.resource.flow.approve_action"/></td -->
                  <td class="listcontent1"><set:listdata name="dealMemo" maxlen="40"/>&nbsp;</td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" class="sectionbutton">&nbsp;                  </td>
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
