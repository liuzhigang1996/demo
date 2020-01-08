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
 }else{
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
function doView(wId){
  document.location.href = "/cib/approve.do?ActionMethod=view&workId="+wId;
}
function doEnquiry(){
  if(validate_query(document.getElementById("form1"))){
    setFormDisabled("form1");
    document.form1.submit();
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
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/approve.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.flow.approve" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --> <set:label name="navigationTitle_history" defaultvalue="MDB Corporate Online Banking > Authorization > Authorization History List"/> <!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --> <set:label name="functionTitle_history" defaultvalue="AUTHORIZATION HISTORY LIST"/> <!-- InstanceEndEditable --></td>
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
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="approve_history" form="form1" file="approve_history"/> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table border="0" cellspacing="0" cellpadding="3" width="100%">
                <tr><td width="20%" class="label1">
                <set:label name="period" defaultvalue="period" rb="app.cib.resource.bat.payroll"/></td>
				
                  <td class="content1"><span class="content">
                    <input id="date_range" name="date_range" type="radio" value="all" onClick="changeRange('all')" checked="checked">
                    <set:label name="all" defaultvalue="all" rb="app.cib.resource.bat.payroll"/>
                  </span></td>
                </tr>
				<tr>
                  <td class="label1" width="28%"></td>
				  
                  <td class="content1" width="72%">
				  <input id="date_range" name="date_range" type="radio" value="range" onClick="changeRange('range')">
				  
                    <set:label name="Query_From" defaultvalue="From"/> 
                    <input type="text" name="dateFrom" id ="dateFrom" value="<set:data name='dateFrom'/>" size="10" maxlength="10" >
                    <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" > &nbsp;&nbsp;<set:label name="Query_To" defaultvalue="To"/>&nbsp;&nbsp;
                    <input type="text" name="dateTo" id ="dateTo" value="<set:data name='dateTo'/>" size="10" maxlength="10" >
                    <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" ><span class="content1">
                    <select id="dateRange" name="dateRange"  onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));">
                      <option value='0'><set:label name="select_short_cut_date" defaultvalue="----- Select a Date Short-cut -----"/><!-- add by linrui for mul-language --></option>
                      <set:rblist file="app.cib.resource.common.date_selection"> <set:rboption/> </set:rblist>
                    </select>
                    </span></td>
                </tr>
                <!-- add by linrui 20190916 -->
                  <tr>
                  <td class="label1" width="28%"><set:label name="Sort_Order" defaultvalue="Sort_Order"/></td>
                    <td class="content1" width="72%">
					<span class="content1">
                    <select id="sortOrder" name="sortOrder" >
                      <set:rblist file="app.cib.resource.common.sort_order"> <set:rboption/> </set:rblist>
                    </select>
                    </span></td>                    
                  </tr>
                  <!-- end -->
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" class="sectionbutton"><div align="center"><span class="tablecontent">
                      <input id="buttonEnquiry" name="buttonEnquiry" type="button" value="<set:label name='buttonEnquiry' defaultvalue='Enquiry'/>" onClick="doEnquiry()">
                      </span> </div></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td height="40" class="content1" align="right" ><table width="100" border="0" cellspacing="0" cellpadding="0">
                      <%--<tr>
                        <td class="buttonexcel"><set:if name= "FinanceFlag" condition="equals" value = "Y"><a href="/cib/DownloadCVS?listName=historyWorkList&columnTitles=Deal_End_Time,Txn_Type,Approve_Progress,Currency,Amount,Approve_Status,Action,Deal_Memo&columnNames=dealEndTime||format@datetime,txnType,Dealer,toCurrency,toAmount,procStatus,dealAction,dealMemo"><set:label name="download" rb="app.cib.resource.common.operation"/></a></set:if>
						<set:if name= "FinanceFlag" condition="notequals" value = "Y"><a href="/cib/DownloadCVS?listName=historyWorkList&columnTitles=Deal_End_Time,Txn_Type,Approve_Progress,Approve_Status,Action,Deal_Memo&columnNames=dealEndTime||format@datetime,txnType,Dealer,procStatus,dealAction,dealMemo"><set:label name="download" rb="app.cib.resource.common.operation"/></a></set:if>			   </td>
                      </tr>
                    --%></table></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1"><set:label name="Deal_End_Time"/></td>
                  <td class="listheader1"><set:label name="Txn_Type"/></td>
                  <td class="listheader1"><set:label name="Approve_Progress"/></td>
                  <set:if name= "FinanceFlag" condition="equals" value = "Y">
                  <td class="listheader1"><set:label name="Currency"/></td>
                  <td align="right" class="listheader1"><set:label name="Amount"/></td>
                  </set:if>
                  <td class="listheader1"><set:label name="Approve_Status"/></td>
                  <td class="listheader1"><set:label name="Action"/></td>
                  <td class="listheader1"><set:label name="Deal_Memo"/></td>
                </tr>
                <set:list name="historyWorkList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline' />">
                  <td class="listcontent1"><set:listdata name="procCreateTime" format='datetime'/></td>
                  <td class="listcontent1"><a onClick="postToMainFrame('/cib/approve.do?ActionMethod=historyEnquiryView',{workId:'<set:listdata name='workId'/>',dateFrom:'<set:data name='dateFrom'/>',dateTo:'<set:data name='dateTo'/>'})" href="#"><set:listdata name="txnType" rb="app.cib.resource.common.subtype"/></a></td>
                  <td class="listcontent1"><set:list name="progressList"> <set:listif name="FinishFlag" condition="equals" value="1"> <span class="textnormal"><set:listdata name="Dealer"/></span><br>
                    </set:listif> <set:listif name="FinishFlag" condition="equals" value="0"> <span class="textgray">&gt; <set:listdata name="Dealer" rb="app.cib.resource.flow.dealer_type"/></span><br>
                    </set:listif> </set:list> </td>
                  <set:if name= "FinanceFlag" condition="equals" value = "Y">
                  <td class="listcontent1" align="center">
                  <!-- modify by Li_zd 2016-08-18 for issuse_20160815 bob batch end -->
	                  <set:listif name="txnType" condition="equals" value="AUTOPAY_ADD">MOP</set:listif>
	                  <set:listif name="txnType" condition="equals" value="AUTOPAY_EDIT">MOP</set:listif>
	                  <set:listif name="txnType" condition="equals" value="AUTOPAY_DELETE">MOP</set:listif>
	                  <set:listif name="txnType" condition="notequals" value="AUTOPAY_ADD">
		                <set:listif name="txnType" condition="notequals" value="AUTOPAY_EDIT">
			                <set:listif name="txnType" condition="notequals" value="AUTOPAY_DELETE">
			                  <set:listif name="ruleFlag" condition="equals" value="0"><set:listdata name="currency" db="rcCurrencyCBS"/></set:listif>
			                  <set:listif name="ruleFlag" condition="equals" value="1"><set:listdata name="toCurrency" db="rcCurrencyCBS"/></set:listif>
			                </set:listif>
			            </set:listif>
			          </set:listif>
                  </td>
                  <td class="listcontent1" align="right">
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
                  <td class="listcontent1"><set:listdata name="procStatus" rb="app.cib.resource.flow.process_status_enquiry"/> &nbsp;</td>
                  <td align="left" class="listcontent1"><set:listdata name="dealAction" rb="app.cib.resource.flow.approve_action"/></td>
                  <td class="listcontent1"><set:listdata name="dealMemo" maxlen="40"/>&nbsp;</td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" class="sectionbutton"><input id="ActionMethod" name="ActionMethod" type="hidden" value="historyEnquiry">
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
