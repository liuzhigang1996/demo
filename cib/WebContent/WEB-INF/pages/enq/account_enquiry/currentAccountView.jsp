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

<SCRIPT language=JavaScript src="/cib/javascript/jsax.js"></SCRIPT>

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
		document.form1.range.disabled = true;
	} else {
		document.form1.date_range[0].checked = true;
		document.form1.date_range[1].checked = false;
		document.form1.dateFrom.disabled = true;
		document.form1.dateTo.disabled = true;
		document.form1.range.disabled = false;
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
function doSubmit(arg) {
	//add by linrui 20180420
	document.getElementById("prePage").value="";
    document.getElementById("keyAll").value="";
    document.getElementById("nextPage").value="";
    document.getElementById("flagPage").value="";
    //end
		if(validate_trans_history(document.getElementById("form1"))) {
			if (arg == 'list') {
				document.form1.ActionMethod.value = "listTransHistory";
				//document.form1.ActionMethod.value = document.getElementById("callMethod").value; //by kevin 20131120
			}
			document.getElementById("form1").submit();
			setFormDisabled("form1");
			//setFieldEnabled("buttonReturn");
		}
}
//<!-- modify by wcc 20180115 --> 
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
var popUpWin=0;
function popUpWindow(URLStr, width, height)
{
  if(popUpWin)
  {
    if(!popUpWin.closed) popUpWin.close();
  }
  var left = (screen.width-width)/2;
  var top = (screen.height-height)/2;
  popUpWin = open(URLStr, 'popUpWin', 'toolbar=no,location=no,top='+top+',left='+left+',directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,width='+width+',height='+height+'');
}
function setSelectUserNo(){  
      document.getElementById("prePage01").type='hidden';
      document.getElementById("nextPage01").type='hidden';
      document.getElementById("isFirst").value='Y';
 }  
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/accEnquiry.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.account_enquiry" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
	  <%-- <set:if name="fromPage" value="" condition="equals">
	  	<set:label name="navigationTitle_detail" defaultvalue="BANK Online Banking >Account Enquiry > Current Account"/>
	  </set:if>
	  <set:if name="fromPage" value="sub" condition="equals">
	  	<set:label name="navigationTitle_detail" defaultvalue="BANK Online Banking >Account Enquiry > Current Account" rb="app.cib.resource.enq.subsidiary_account_enquiry"/>
	  </set:if> --%>
	  <set:label name="navigationTitle_ca_detail" />
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
		  <form action="/cib/accEnquiry.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="trans_history" form="form1" file="trans_history" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1" colspan="4"><set:label name="Account_Detial" defaultvalue="Account Detial"/></td>
                </tr>
                <tr>
                  <td class="content1" width="8%">&nbsp;</td>
                  <td class="content1" width="16%"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                  <td width="71%" class="content1"><set:data name="ACCOUNT_NO"/></td><%--  <set:data name="ACCOUNT_TYPE" rb="app.cib.resource.common.account_type_desc"/>--%>
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
				<!--<tr class="greyline">
                  <td class="content1" width="8%">&nbsp;</td>
                  <td class="content1" width="16%"><set:label name="Currency" defaultvalue="Currency"/></td>
                  <td class="content1"><set:data name="CCY_CODE_OF_AC" db="rcCurrencyCBS"/></td>
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
                <tr>-->
				<!-- add for GAPMC-EB-001-0058 by lzg-->
                <tr class="greyline">
                  <td class="content1" width="8%">&nbsp;</td>
                  <td class="content1" width="16%"><set:label name="Currency" defaultvalue="Currency"/></td>
                  <td class="content1"><set:data name="CCY_CODE_OF_AC" db="rcCurrencyCBS"/></td>
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
				<!-- add by lzg end-->
                <tr>
                  <td class="content1" width="8%">&nbsp;</td>
                  <td class="content1" width="16%"><set:label name="Ledger_Balance" defaultvalue="Ledger Balance : "/></td>
                  <td class="content1"><set:data name="CURRENT_BALANCE" format="amount"/></td>
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
                <tr class="greyline">
                  <td class="content1" width="8%">&nbsp;</td>
                  <td class="content1" width="16%"><set:label name="Available_Balance" defaultvalue="Available Balance : "/></td>
                  <td class="content1">
                  <set:data name="AVAILABLE_BALANCE" format="amount"/>
                  </td>
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
                 <%--
                <tr>
                  <td class="content1" width="8%">&nbsp;</td>
                  <td class="content1" width="16%"><set:label name="Statement" defaultvalue="Statement : "/></td>
                  <td class="content1"><set:data name="ACCOUNT_NO" db="statementForDetail"/></td>                
                  <td class="content1" width="5%">&nbsp;</td>
                </tr>
                 --%>
                 <!-- add by lzg 20190618 -->
                 <tr>
                 	<td colspan="4" style = "text-align: center;"><input type = "button" class = "sectionbutton" value = "<set:label name='buttonReturn' defaultvalue='return'/>" onClick="postToMainFrame('/cib/accEnquiry.do?ActionMethod=listCurrentAccount','')"/></td>
                 </tr>
                 <!-- add by lzg end -->
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="4" class="groupinput"><set:label name="Transaction_History" defaultvalue="Account Transaction History"/></td>
                </tr>
                <tr>
                  <td class="content1">                  
                    <div align="right">
                      <!-- modified by lzg 20190621 -->
                      <!--<input id="date_range" name="date_range" type="radio" value="range" checked onClick="this.form.dateFrom.value='';this.form.dateTo.value='';this.form.dateFrom.disabled = true;this.form.dateTo.disabled = true;form.range.disabled = false;setSelectUserNo();">
                        -->
                        <input type = "hidden" name = "date_range" value = "range"/>
                        </div></td>
                        <!-- modified by lzg end -->
                  <td width="16%" class="content1"><set:label name="Range" defaultvalue="Range : "/></td>
                  <td width="74%" class="content1"><select id="range" name="range" onChange="">
                      <set:rblist file="app.cib.resource.enq.range"> <set:rboption name="range"/> </set:rblist>
                    </select>
                  </td>
                </tr>
                <!-- add by linrui 20190918 -->
                <tr>
                  <td class="content1">                  
                    <div align="right">
                        </div></td>
                  <td width="16%" class="content1"><set:label name="Sort_Order" defaultvalue="Sort_Order"/></td>
                  <td width="76%" class="content1"><select id="sortOrder" name="sortOrder"  disabled="disabled">
                      <set:rblist file="app.cib.resource.common.sort_order"> <set:rboption/> </set:rblist>
                    </select>
                  </td>
                </tr>
                  <!-- end -->
                <!-- modified by lzg 20190621 -->
                <!--<tr class="greyline">
                  <td class="content1">                  
                    <div align="right">
                      <input id="date_range" name="date_range" type="radio" value="date" onClick="this.form.dateFrom.disabled = false;this.form.dateTo.disabled = false;form.range.disabled = true;setSelectUserNo();">
                        </div></td>
                  <td class="content1"><set:label name='date_from' defaultvalue='From'/></td>
                  <td class="content1"><input name="dateFrom" type="text" id="dateFrom" size="12" maxlength="10" disabled value="<set:data name='dateFrom'/>">
                    <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" > &nbsp;&nbsp;&nbsp; <set:label name='date_to' defaultvalue='To'/> &nbsp;
                    <input name="dateTo" type="text" id="dateTo" size="12" maxlength="10" disabled value="<set:data name='dateTo'/>">
                    <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" > </td>
                </tr>
                -->
                <!-- modified by lzg end -->
                <%--
                <tr>
                  <td class="content1">&nbsp;</td>
                  <td class="content1" ><set:label name="View_by" defaultvalue="View by"/></td>
                  <td class="content1"><select id="viewBy" name="viewBy" onChange="">
                      <option value=""><set:label name='select_all' defaultvalue='ALL'/><!-- add by linrui for mul-language20171121 --></option>
                      <set:rblist db="txnFilter"> <set:rboption name="viewBy"/> </set:rblist>
                    </select>
                  </td>
                </tr>
                --%>
                <!--<tr class="greyline">
                  <td height="10" class="content1">&nbsp;</td>
                  <td height="10" class="content1"><set:label name="Remark" defaultvalue="Remark"/></td>
                  <td height="10" class="content1"><input name="remark" type="text" id="remark" value="<set:data name='remark'/>"></td>
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
                  <td width="54%" height="40" class="sectionbutton"><div align="right">
                      <input id="search" type="button" value="&nbsp;<set:label name='buttonSearch' defaultvalue='Search'/>&nbsp;" name="search" onClick="doSubmit('list')">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="listTransHistory">
                      <input name="accountNo" type="hidden" id="accountNo" value="<set:data name='ACCOUNT_NO'/>">
					  <input name="fromPage" type="hidden" id="fromPage" value="<set:data name='fromPage'/>">
					  <input name="callMethod" type="hidden" id="callMethod" value="<set:data name='callMethod'/>">
					   modify by wcc 20180115 翻页相关值  
					  <input type="hidden" name="prePageHis" id="prePage" value="<set:data name='prePageHis' />">
					  <input type="hidden" name="nextPageHis" id="nextPage" value="<set:data name='nextPageHis' />">
					  <input type="hidden" name="flagPageHis" id="flagPage" value="<set:data name='flagPageHis' />">
					  <input type="hidden" name="keyAllHis" id="keyAll" value="<set:data name='keyAllHis' />"> 
					   <input type="hidden" name="isFirst" id="isFirst" value="N"> 
                    </div>
				  </td>
                  <td width="46%" height="40" align="right" valign="bottom" class="content1">
                  <table width="100" border="0" cellspacing="0" cellpadding="0">
                  <%--
                      <tr>
                        <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=transHistoryView&logo=true&general=<set:label name="Account_Number" defaultvalue="Account Number"/>,<set:data name="ACCOUNT_NO"/> - <set:data name="ACCOUNT_NO" db="accountDesc"/>&fileName=account_view&columnTitles=<set:label name='Transaction_Date'/>,<set:label name='Value_Date'/>,<set:label name='Description'/>,<set:label name='Transaction_Info'/>,<set:label name='Debit'/>,<set:label name='Credit'/>,<set:label name='Balance'/>,<set:label name='Transaction_Nature'/>,<set:label name='Transaction_Remark'/>&columnNames=POST_DATE||format@date,EFFECTIVE_DATE||format@date,DESCRIPTION_EXCEL||specialChar@yes,TRANSACTION_INFO_EXCEL||specialChar@yes,POST_AMOUNT||format@amount,CREDIT||format@amount,BALANCE||format@amount,TRANSACTION_NATURE||db@transactionNature,REMARK"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>
                      </tr>
                    --%>
                    </table>
				  </td>
                </tr>
              </table>
              --><!-- modified by lzg end -->
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1" align="left" valign="middle"><set:label name="Transaction_Date2" defaultvalue="Transaction Date"/></td>
                  <td class="listheader1" align="left" valign="middle"><set:label name="Value_Date" defaultvalue="Value Date"/></td>
                  <td class="listheader1" align="left" valign="middle"><set:label name="Transaction_Time" defaultvalue="Transaction Time"/></td>
                  <td class="listheader1" align="left" valign="middle"><set:label name="Description" defaultvalue="Description"/></td>
                  <td class="listheader1" align="right" valign="middle"><set:label name="Debit" defaultvalue="Debit"/></td>
                  <td class="listheader1" align="right" valign="middle"><set:label name="Credit" defaultvalue="Credit"/></td>
                  <td class="listheader1" align="right" valign="middle"><set:label name="Balance" defaultvalue="Balance"/></td>
                  <td class="listheader1" align="left" valign="middle"><set:label name="Transaction_Channel" /></td>
                  <td class="listheader1" align="left" valign="middle"><set:label name="Serial_Number" /></td>
                  <td class="listheader1" align="left" valign="middle"><set:label name="ref" defaultvalue="ref"/></td>
                </tr>
                <set:list name="transHistoryView" showNoRecord="YES" showPageRows="15">
                <tr class="<set:listclass class1='' class2='greyline' />">
                  <td class="listcontent1" align="left"><set:listdata name="POST_DATE" format="date"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="EFFECTIVE_DATE" format="date"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="QHIS_TX_TIME" /></td>
                  <td class="listcontent1" align="left"><set:listdata name="DESCRIPTION"/></td>
                  <td align="right" class="listcontent1"><set:listdata name="POST_AMOUNT" format="amount"/></td>
                  <td align="right" class="listcontent1"><set:listdata name="CREDIT" format="amount"/></td>
                  <td align="right" class="listcontent1"><set:listdata name="BALANCE" format="amount"/></td>
                  <td class="listcontent1"align="left"><set:listdata name="QHIS_CHNL"/></td>
                  <td class="listcontent1"align="left"><set:listdata name="SERIAL_NO"/></td>
                  <td class="listcontent1"align="left"><set:listdata name="REMARK"/></td>
                </tr>
                </set:list>
              </table>
               <!-- modify by wcc 20180115 --> 
				  <%--<table width="100%" border="0" cellspacing="0" cellpadding="0">
				  	<tr>
				  	<td align="right">
				  	<input type="<set:data name='disableP'/>" id = "prePage01" onClick="chanPage('P','list')" name="prePage" value="<set:label name='previous_page' rb='app.cib.resource.common.operation'/>"/>
				  	<input type="<set:data name='disableN'/>" id = "nextPage01" onClick="chanPage('N','list')" name="nextPage" value="<set:label name='next_page' rb='app.cib.resource.common.operation'/>"/>
				  	</td>
				  	</tr>
				  </table>
              --%><!-- InstanceEndEditable -->
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
