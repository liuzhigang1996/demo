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
<script language="JavaScript" src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
   var range = '<set:data name="date_range"/>';
  if(range != 'range') {
		document.form1.date_range[0].checked = true;
		document.form1.date_range[0].onclick();
	} else {
		document.form1.date_range[1].checked = true;
		document.form1.date_range[1].onclick();
	}
	//add by linrui 20190916
	var sortOrder='<set:data name="sortOrder"/>';
	if(sortOrder == '2'){
	    document.getElementById("sortOrder")[1].selected = true;
	}else{
	    document.getElementById("sortOrder")[0].selected = true;
    }
    //end
}
function doSubmit(arg) {
	if(validate_query(document.getElementById("form1"))){
		if (arg == 'list') {
			document.form1.ActionMethod.value = 'list';
		}
		document.getElementById("form1").submit();
		setFormDisabled("form1");
	}
}
function changeRange(range){
	if(range != 'range'){
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
//modified by lzg for pwc
//function doView(wId){
  //setFormDisabled("form1");
  //document.getElementById("ActionMethod").value = "view";
  //document.getElementById("workId").value = wId;
  //document.form1.submit();
//}
//modified by lzg end
function selectAllClick(){
  if(document.form1.checkAllBox.checked){
    if(document.form1.workIds){
	  if(document.form1.workIds.length){
	    for(var indx = 0;indx < document.form1.workIds.length; indx ++)
		{
		  document.form1.workIds[indx].checked = true;
		}
	  }
	  else{
	    document.form1.workIds.checked = true;
	  }
	}
  }
  else{
    if(document.form1.workIds){
	  if(document.form1.workIds.length){
	    for(var indx = 0;indx < document.form1.workIds.length; indx ++)
		{
		  document.form1.workIds[indx].checked = false;
		}
	  }
	  else{
	    document.form1.workIds.checked = false;
	  }
	}
  }
}
function doMultiApprove(){
    setFormDisabled("form1");
    document.form1.submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/approve.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.flow.approve" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_pending" defaultvalue="MDB Corporate Online Banking > Authorization > Pending Approvals List"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_pending" defaultvalue="PENDING APPROVALS LIST"/><!-- InstanceEndEditable --></td>
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
                  <td><set:messages width="100%" cols="1" align="center"/>
				  <set:fieldcheck name="approve_history" form="form1" file="approve_history" />
				  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="15%" class="label1"><set:label name="Period"/></td>
                  <td width="70%" class="content1">
                    <input id="date_range" name="date_range" type="radio" value="all" checked onClick="changeRange('all')">
&nbsp; <set:label name="all" defaultvalue="all" rb="app.cib.resource.txn.bill_payment"/> <br>
                    <input id="date_range" name="date_range" type="radio" value="range" onClick="changeRange('range')">
&nbsp; <set:label name='date_from' defaultvalue='date_from' rb="app.cib.resource.txn.bill_payment"/>&nbsp;
                    <input name="dateFrom" type="text" id="dateFrom" size="12" maxlength="10" disabled value="<set:data name='dateFrom'/>">
                    <span class="content1"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" ></span>&nbsp;&nbsp;&nbsp; <set:label name='date_to' defaultvalue='date_to' rb="app.cib.resource.txn.bill_payment"/>&nbsp;
                    <input name="dateTo" type="text" id="dateTo" size="12" maxlength="10" disabled value="<set:data name='dateTo'/>">
                    <span class="content1"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" ></span>
                    <select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));">
                      <option value='0'><set:label name="select_short_cut_date" defaultvalue="----- Select a Date Short-cut -----"/><!-- add by linrui for mul-language --></option>
                      <set:rblist file="app.cib.resource.common.date_selection"> <set:rboption/> </set:rblist>
                    </select>
                    </td>
<td width="70%" rowspan="2" class="label1">
  &nbsp;
</td>                
                </tr>
                <!-- add by linrui 20190916 -->
                  <tr>
                  <td width = "15%">&nbsp;</td>
                  <td class="label1"  ><set:label name="Sort_Order" defaultvalue="Sort_Order"/>
                  <span class="content1">
                    <select id="sortOrder" name="sortOrder">
                      <set:rblist file="app.cib.resource.common.sort_order"> <set:rboption/> </set:rblist>
                    </select>
                    </span>
                  </td>
                    <td class="content1" >
					&nbsp;</td>                    
                  </tr>
                  <!-- end -->
                <tr>
                  <td width="15%" class="label1"><set:label name="ref_no"/></td>
                  <td width="70%" class="content1">
                  <input id="transId" type="text" name="transId" value="<set:data name='transId'/>" size="25" maxlength="20">
                  <input id="search" type="button" value="&nbsp;<set:label name='search' defaultvalue='search' rb='app.cib.resource.txn.bill_payment'/>&nbsp;" name="search" onClick="doSubmit('list')">
                  </td>
                </tr>
              </table>              
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1"><set:label name="ref_no"/></td>
                  <td class="listheader1"><set:label name="Proc_Create_Time" defaultvalue="Date &amp; Time"/></td>
                  <td class="listheader1"><set:label name="Txn_Type" defaultvalue="Operation"/></td>
                  <td class="listheader1"><set:label name="Approve_Progress" defaultvalue="Progress"/></td>
                  <td class="listheader1"><set:label name="Work_Dealer_Name" defaultvalue="Current Holder"/></td>
                  <set:if name= "FinanceFlag" condition="equals" value = "Y">
                  <td align="center" class="listheader1"><set:label name="Currency" defaultvalue="Currency"/></td>
                  <td align="right" class="listheader1"><set:label name="Amount" defaultvalue="Amount"/></td>
                  </set:if>
                  <td align="center" class="listheader1"><set:label name="Select_All" defaultvalue="Select All"/>
                    <input id="checkAllBox" type="checkbox" name="checkAllBox" value="" onClick="selectAllClick()">                  </td>
                  <td width="200" align="center" class="listheader1"><set:label name="Action" defaultvalue="Action"/></td>
                </tr>
                <set:list name="workList" showPageRows="10" showNoRecord="YES" pageClass="approve" pageStyle="POST">
                <tr class="<set:listclass class1='' class2='greyline' />">
                  <td class="listcontent1"><set:listdata name="transNo"/></td>
                  <td class="listcontent1"><set:listdata name="procCreateTime" format='datetime'/>&nbsp;</td>
                  <td class="listcontent1">
                  	<set:listdata name="txnType" rb="app.cib.resource.common.subtype"/>&nbsp;
                  </td>
                  <td class="listcontent1"><set:list name="progressList"> <set:listif name="FinishFlag" condition="equals" value="1"> <span class="textnormal"><set:listdata name="Dealer"/></span><br>
                    </set:listif> <set:listif name="FinishFlag" condition="equals" value="0"> <span class="textgray">&gt; <set:listdata name="Dealer" rb="app.cib.resource.flow.dealer_type"/></span><br>
                    </set:listif> </set:list> </td>
                  <td class="listcontent1"><set:listdata name="workDealerName"/>&nbsp;</td>
                  <set:if name= "FinanceFlag" condition="equals" value = "Y">
                  <td align="center" class="listcontent1">
                  <!-- modify by Li_zd 2016-08-18 for issuse_20160815 bob batch end -->
                  <set:listif name="ruleFlag" condition="equals" value="0"><set:listdata name="currency" db="rcCurrencyCBS"/></set:listif>
		          <set:listif name="ruleFlag" condition="equals" value="1"><set:listdata name="toCurrency" db="rcCurrencyCBS"/></set:listif>
                  </td>
                  <td align="right" class="listcontent1">
                  <!-- modify by long_zg 2014-01-08 for CR192 bob batch begin -->
                  <!-- <set:listif name="ruleFlag" condition="equals" value="0"><set:listdata name="amount" format='amount'/></set:listif><set:listif name="ruleFlag" condition="equals" value="1"><set:listdata name="toAmount" format='amount'/></set:listif>-->
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
                <!-- modify by long_zg 2014-01-08 for CR192 bob batch end -->
                  </td>
                  </set:if>
                  <td align="center" class="listcontent1"><set:listcheckbox name="workIds" value="workId" text=""/>
                    <input id="listIds" name="listIds" type="hidden" value="<set:listdata name='workId'/>"/>
&nbsp;</td>
                  <td align="center" class="listcontent1"><input id="buttonView" name="buttonView" type="button" value="&nbsp;&nbsp;<set:label name='buttonView' defaultvalue='View'/>&nbsp;&nbsp;&nbsp;" 
                  onClick="postToMainFrame('/cib/approve.do?ActionMethod=view',{workId:'<set:listdata name='workId'/>'});">
                    <set:if name= "ExecuteFlag" condition="equals" value = "N">
                    <input id="buttonApprove" name="buttonApprove" type="button" value="<set:label name='buttonApprove' defaultvalue='Approve'/>" 
                    onClick="postToMainFrame('/cib/approve.do?ActionMethod=approveLoad',{workId:'<set:listdata name='workId'/>'})">
                    </set:if> <set:if name= "ExecuteFlag" condition="equals" value = "Y">
                    <input id="buttonExecute" name="buttonExecute" type="button" value="<set:label name='buttonExecute' defaultvalue='Execute'/>" 
                    onClick="postToMainFrame('/cib/approve.do?ActionMethod=approveLoad',{workId:'<set:listdata name='workId'/>'})">
                  </set:if></td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" class="sectionbutton"><set:if name= "ExecuteFlag" condition="equals" value = "N">
                    <input id="buttonMultiApprove" name="buttonMultiApprove" type="button" value="<set:label name='buttonMultiApprove' defaultvalue='Approve Selected Operations'/>" onClick="doMultiApprove()">
                    </set:if> <set:if name= "ExecuteFlag" condition="equals" value = "Y">
                    <input id="buttonMultiExecute" name="buttonMultiExecute" type="button" value="<set:label name='buttonMultiExecute' defaultvalue='Execute Selected Operations'/>" onClick="doMultiApprove()">
                    </set:if>
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="multiApproveLoad">
                    <!--<input id="workId" name="workId" type="hidden" value=""></td>
                --></tr>
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
