<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.enq.account_enquiry">
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
	//modified by lzg 20190621
	/*var daterange = '<set:data name="date_range"/>';
	if (daterange == 'date') {
		document.form1.date_range[0].checked = false;
		document.form1.date_range[1].checked = true;
		document.form1.dateFrom.disabled = false;
		document.form1.dateTo.disabled = false;
	} else {
		document.form1.date_range[0].checked = true;
		document.form1.date_range[1].checked = false;
		document.form1.dateFrom.disabled = true;
		document.form1.dateTo.disabled = true;
	}*/
	//modified by lzg end
	//add by linrui 20190916
	var sortOrder='<set:data name="sortOrder"/>';
	if(sortOrder == '2'){
	    document.getElementById("sortOrder")[1].selected = true;
	}else{
	    document.getElementById("sortOrder")[0].selected = true;
    }
    //end
}

function chanPage(flagPage,arg){
	if(validate_trans_history(document.getElementById("form1"))) {
			document.getElementById("flagPage").value=flagPage;
			if (arg == 'list') {
				document.form1.ActionMethod.value = 'listTransHistory';
			}
			document.getElementById("form1").submit();
			setFormDisabled("form1");
		}
}

function doSubmit(arg) {
		//add by linrui 20180420
		document.getElementById("prePage").value="";
    	document.getElementById("keyAll").value="";
    	document.getElementById("nextPage").value="";
    	document.getElementById("flagPage").value="";
    	//end
		if(validate_trans_history(document.getElementById("form1"))) {
			if (arg == 'list') {
				document.form1.ActionMethod.value = 'listTransHistorySa';
			}
			document.getElementById("form1").submit();
			setFormDisabled("form1");
			//setFieldEnabled("buttonReturn");
		}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/accEnquiryBank.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.account_enquiry" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_sa_detail_bank" defaultvalue="BANK Online Banking >Account Enquiry > Current Account"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_detail" defaultvalue="CURRENT ACCOUNT HISTORY"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/accEnquiryBank.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
		    <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><set:messages width="100%" cols="1" align="center"/>
                    <set:fieldcheck name="trans_history" form="form1" file="trans_history" />
                </td>
              </tr>
            </table>
		    <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1" colspan="4"><set:label name="Account_Detial" defaultvalue="Account Detial"/></td>
                </tr>
                <tr>
                  <td class="content1" width="5%">&nbsp;</td>
                  <td class="content1" width="30%"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                  <td class="content1"><set:data name="ACCOUNT_NO"/></td>
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
                <%--<tr class="greyline">
                  <td class="content1" width="5%">&nbsp;</td>
                  <td class="content1" width="30%"><set:label name="Currency" defaultvalue="Currency"/></td>
                  <td class="content1"><set:data name="CCY_CODE_OF_AC" db="rcCurrencyCBS"/></td>
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
                --%>
                <!-- add by lzg for GAPMC-EB-001-0058 -->
                <tr class="greyline">
                  <td class="content1" width="5%">&nbsp;</td>
                  <td class="content1" width="30%"><set:label name="Currency" defaultvalue="Currency"/></td>
                  <td class="content1"><set:data name="CCY_CODE_OF_AC" db="rcCurrencyCBS"/></td>
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
                <!-- add by lzg end -->
                <tr>
                  <td class="content1" width="5%">&nbsp;</td>
                  <td class="content1" width="30%"><set:label name="Ledger_Balance" defaultvalue="Ledger Balance : "/></td>
                  <td class="content1"><set:data name="CURRENT_BALANCE" format="amount"/></td>
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
                <tr class="greyline">
                  <td class="content1" width="5%">&nbsp;</td>
                  <td class="content1" width="30%"><set:label name="Available_Balance" defaultvalue="Available Balance : "/></td>
                  <td class="content1">
                  <set:data name="AVAILABLE_BALANCE" format="amount"/>
                  </td>
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
                <%--
                <tr>
                  <td class="content1" width="5%">&nbsp;</td>
                  <td class="content1" width="30%"><set:label name="Statement" defaultvalue="Statement : "/></td>
                  <td class="content1"><set:data name="ACCOUNT_NO" db="statementForDetail"/></td>
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
                --%>
                <!-- add by lzg 20190618 -->
               <tr>
              	 <td colspan="4" style = "text-align: center;"><input type = "button" class = "sectionbutton" value = "<set:label name='buttonReturn' defaultvalue='return'/>" onClick="postToMainFrame('/cib/accEnquiryBank.do?ActionMethod=listSavingAccount','')"/></td>
               </tr>
               <!-- add by lzg end -->
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="5" class="groupinput"><set:label name="Transaction_History" defaultvalue="Account Transaction History"/></td>
                </tr>
                <tr>
                  <td class="content1">&nbsp;</td>
                  <!-- modified by lzg 20190621 -->
                  <td class="content1"><div align="center">
                      <!--<input id="date_range" name="date_range" type="radio" value="range" checked onClick="this.form.dateFrom.value='';this.form.dateTo.value='';this.form.dateFrom.disabled = true;this.form.dateTo.disabled = true;form.range.disabled = false;">
                    -->
                    <input type = "hidden" name = "date_range" value = "range"/>
                    </div></td>
                    <!-- modified by lzg end -->
                  <td class="content1"><set:label name="Range" defaultvalue="Range : "/></td>
                  <td class="content1"><select id="range" name="range" onChange="">
                      <set:rblist file="app.cib.resource.enq.range"> <set:rboption name="range"/> </set:rblist>
                    </select>                  </td>
                </tr>
                <!-- add by linrui 20190918 -->
                <tr>
                <td class="content1">&nbsp;</td>
                  <td class="content1">                  
                    <div align="right">
                        </div></td>
                  <td width="16%" class="content1"><set:label name="Sort_Order" defaultvalue="Sort_Order"/></td>
                  <td width="74%" class="content1"><select id="sortOrder" name="sortOrder"  disabled="disabled">
                      <set:rblist file="app.cib.resource.common.sort_order"> <set:rboption/> </set:rblist>
                    </select>
                  </td>
                </tr>
                  <!-- end -->
                <!-- modified by lzg 20190621 -->
                <!--<tr class="greyline">
                  <td class="content1">&nbsp;</td>
                  <td class="content1"><div align="center">
                      <input id="date_range" name="date_range" type="radio" value="date" onClick="this.form.dateFrom.disabled = false;this.form.dateTo.disabled = false;form.range.disabled=true;">
                    </div></td>
                  <td class="content1"><set:label name='date_from' defaultvalue='From'/></td>
                  <td class="content1"><input name="dateFrom" type="text" id="dateFrom" size="12" maxlength="10" disabled value="<set:data name='dateFrom'/>">
                    <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" > &nbsp;&nbsp;&nbsp; <set:label name='date_to' defaultvalue='To'/> &nbsp;
                    <input name="dateTo" type="text" id="dateTo" size="12" maxlength="10" disabled value="<set:data name='dateTo'/>">
                    <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" > </td>
                </tr>
                --><!-- modified by lzgend -->
                <%--
                <tr>
                  <td class="content1">&nbsp;</td>
                  <td class="content1" >&nbsp;</td>
                  <td class="content1" ><set:label name="View_by" defaultvalue="View by"/></td>
                  <td class="content1"><select id="viewBy" name="viewBy" onChange="">
                      <option value="">All</option>
                      <set:rblist db="txnFilter"> <set:rboption name="viewBy"/> </set:rblist>
                    </select>                  </td>
                </tr>
                --%>
                <!--<tr class="greyline">
                  <td class="content1" height="10"></td>
                  <td height="10" class="content1">&nbsp;</td>
                  <td height="10" class="content1"><set:label name="Remark" defaultvalue="Remark"/></td>
                  <td height="10" colspan="2" class="content1"><input name="remark" type="text" id="remark" value="<set:data name='remark'/>"></td>
                </tr>
                -->
                <input name="remark" type="hidden" id="remark" value="">
                <tr>
                 	<td colspan="4" style = "text-align: center;"><input id="search" type="button" value="<set:label name='buttonSearch' defaultvalue='Search'/>" name="search" onClick="doSubmit('list')"></td>
                 </tr>
              </table>
              <input id="ActionMethod" name="ActionMethod" type="hidden" value="listTransHistory">
	          <input name="accountNo" type="hidden" id="accountNo" value="<set:data name='ACCOUNT_NO'/>">
			  <input name="fromPage" type="hidden" id="fromPage" value="<set:data name='fromPage'/>">
			  <input name="callMethod" type="hidden" id="callMethod" value="<set:data name='callMethod'/>">
			  <input type="hidden" name="prePageHis" id="prePage" value="<set:data name='prePageHis' />">
			  <input type="hidden" name="nextPageHis" id="nextPage" value="<set:data name='nextPageHis' />">
			  <input type="hidden" name="flagPageHis" id="flagPage" value="<set:data name='flagPageHis' />">
			  <input type="hidden" name="keyAllHis" id="keyAll" value="<set:data name='keyAllHis' />"> 
			  <input type="hidden" name="isFirst" id="isFirst" value="N">
			  <!-- add by lzg 20190621 -->
			  <input type="hidden" name="currentPage" id="currentPage" value="<set:data name = 'currentPage' />">
			  <!-- add by lzg end -->
              <!-- modified by lzg 20190621 -->
              <!--<table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="57%" height="40" class="sectionbutton"><div align="right">
                      <input id="search" type="button" value="&nbsp;<set:label name='buttonSearch' defaultvalue='Search'/>&nbsp;" name="search" onClick="doSubmit('list')">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="listTransHistory">
                      <input name="accountNo" type="hidden" id="accountNo" value="<set:data name='ACCOUNT_NO'/>">
                    </div>				  </td>
                  <td width="43%" height="40" align="right" valign="bottom" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                      <%--<tr>
                        <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=transHistoryView&fileName=saving_account_view&columnTitles=<set:label name='Transaction_Date'/>,<set:label name='Value_Date'/>,<set:label name='Description'/>,<set:label name='Debit'/>,<set:label name='Credit'/>,<set:label name='Balance'/>,<set:label name='Transaction_Remark'/>&columnNames=POST_DATE||format@date,EFFECTIVE_DATE||format@date,DESCRIPTION||specialChar@yes,POST_AMOUNT||format@amount,CREDIT||format@amount,BALANCE||format@amount,REMARK">Download</a></td>
                      </tr>
                    --%></table>				  </td>
                </tr>
              </table>
              --><!-- modified by lzg end -->
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1"><set:label name="Transaction_Date" defaultvalue="Transaction Date"/></td>
                  <td class="listheader1"><set:label name="Value_Date" defaultvalue="Value Date"/></td>
				  <td class="listheader1"><set:label name="Transaction_Time" defaultvalue="Transaction Time"/></td>
                  <td class="listheader1"><set:label name="Description" defaultvalue="Description"/></td>
                  <td class="listheader1"><set:label name="Debit" defaultvalue="Debit"/></td>
                  <td class="listheader1"><set:label name="Credit" defaultvalue="Credit"/></td>
                  <td class="listheader1"><set:label name="Balance" defaultvalue="Balance"/></td>
                  <!-- add by lzg 20191018 -->
                  <td class="listheader1"><set:label name="Transaction_Channel" defaultvalue="Transaction Channel"/></td>
                  <td class="listheader1"><set:label name="Serial_Number" defaultvalue="Serial Number"/></td>
                  <!-- add by lzg end -->
                  <td class="listheader1"><set:label name="Transaction_Remark" defaultvalue="Transaction Remark"/></td>
                </tr>
                <set:list name="transHistoryView" showNoRecord="YES" showPageRows="10">
                <tr class="<set:listclass class1='' class2='greyline' />">
                  <td class="listcontent1"><set:listdata name="POST_DATE" format="date"/></td>
                  <td class="listcontent1"><set:listdata name="EFFECTIVE_DATE" format="date"/></td>
                  <td class="listcontent1"><set:listdata name="DESCRIPTION" escapechar="YES"/></td>
                  <td class="listcontent1"><set:listdata name="POST_AMOUNT" format="amount"/></td>
                  <td class="listcontent1"><set:listdata name="CREDIT" format="amount"/></td>
                  <td class="listcontent1"><set:listdata name="BALANCE" format="amount"/></td>
                  <td class="listcontent1"><set:listdata name="QHIS_CHNL"/></td>
                  <td class="listcontent1"><set:listdata name="SERIAL_NO"/></td>
                  <td class="listcontent1"><set:listdata name="REMARK"/></td>
                </tr>
                </set:list>
              </table>
              <!-- InstanceEndEditable -->
              <!-- add by linrui for class style -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
				  	<tr>
				  	<td align="right" class="pageline">
					<set:if name='disableP' condition='EQUALS' value='button'>
                    <span class="pagelink">
					<a href="#" onClick="chanPage('P','list')">&lt;&lt;<set:label name='previous_page' rb='app.cib.resource.common.operation'/></a>
					</span> | 
					</set:if>
					<set:if name='disableP' condition='EQUALS' value='hidden'>
					<span class="pagedisabled">&lt;&lt;<set:label name='previous_page' rb='app.cib.resource.common.operation'/></span> | 
					</set:if>
					<set:if name='disableN' condition='EQUALS' value='button'>
					<span class="pagelink">
					<a href="#" onClick="chanPage('N','list')"><set:label name='next_page' rb='app.cib.resource.common.operation'/>&gt;&gt;</a>
					</span>
					</set:if>
					<set:if name='disableN' condition='EQUALS' value='hidden'>
				  	<span class="pagedisabled"><set:label name='next_page' rb='app.cib.resource.common.operation'/>&gt;&gt;</span>
					</set:if>
				  	</td>
				  	</tr>
              </table>
              <!-- add by linrui end -->
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
