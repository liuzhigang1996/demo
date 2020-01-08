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
	var daterange = '<set:data name="date_range"/>';
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
	}
}
function doSubmit(arg) {
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
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/accEnquiryBank.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.account_enquiry" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
	  <set:if name="fromPage" value="" condition="equals">
	  	<set:label name="navigationTitle_detail" defaultvalue="BANK Online Banking >Account Enquiry > Credit Card"/>
	  </set:if>
	  <set:if name="fromPage" value="sub" condition="equals">
	  	<set:label name="navigationTitle_detail" defaultvalue="BANK Online Banking >Account Enquiry > Credit Card" rb="app.cib.resource.enq.subsidiary_account_enquiry"/>
	  </set:if>
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
		  <form action="/cib/accEnquiryBank.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="trans_history" form="form1" file="trans_history" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                  <td class="listheader1" colspan="3"><set:label name="Card_Info" defaultvalue="Card Info"/></td>
                  <td ></td>
                  <td class="listheader1" colspan="3"><set:label name="As_Of_Last_Statement" defaultvalue="As Of Last Statement" /></td>
                </tr>
                <tr>
                  <td class="content1" width="2%"></td>
                  <td class="content1" width="16%"><set:label name="Credit_Card_Number" defaultvalue="Credit Card Number" />:</td>
                  <td class="content1" width="35%"><set:data name='CARD_NO' /> - <set:data name="ACCOUNT_TYPE" rb="app.cib.resource.common.account_type_desc"/></td>
                  <td class="content1" width="4%"></td>
                  <td class="content1" width="2%"></td>
                  <td class="content1" width="16%"><set:label name="Statement_Balance" defaultvalue="Statement Balance" />:</td>
                  <td class="content1" width="25%"><set:data name='STATEMENT_BALANCE' format='amountcc' /></td>
                </tr>
                 <tr class="greyline">
                  <td class="content1"></td>
                  <td class="content1"><set:label name="Currence" defaultvalue="Currence" />:</td>
                  <td class="content1"><set:data name="CARD_CURRENCY"/></td>
                  <td class="content1"></td>
                  <td class="content1"></td>
                  <td class="content1"><set:label name="Payment_Due_Date" defaultvalue="Payment Due Date" />:</td>
                  <td class="content1"><set:data name='PAYMENT_DUE_DATE' format='date' /></td>
                </tr>
                 <tr>
                  <td class="content1"></td>
                  <td class="content1"><set:label name="Available_Limit" defaultvalue="Available Limit" />:</td>
                  <td class="content1"><set:data name="AVAILABLE_CREDIT_AMOUNT" format="amount"/></td>
                  <td class="content1"></td>
                  <td class="content1"></td>
                  <td class="content1"><set:label name="Current_Due" defaultvalue="Current Due" />:</td>
                  <td class="content1"><set:data name="PAYMENT_DUE" format="amount"/></td>
                </tr>
                <tr class="greyline">
                  <td class="content1"></td>
                  <td class="content1"><set:label name="Credit_Limit" defaultvalue="Credit Limit" />:</td>
                  <td class="content1"><set:data name="CREDIT_LIMIT" format="amount"/></td>
                  <td class="content1"></td>
                  <td class="content1"></td>
                  <td class="content1"><set:label name="Past_Due" defaultvalue="Past Due" />:</td>
                  <td class="content1"><set:data name='PAST_DUE_AMOUNT' format='amount' /></td>
                </tr>
                <tr>
                  <td class="content1"></td>
                  <td class="content1"><set:label name="Last_Payment_Day" defaultvalue="Last Payment Day" />:#</td>
                  <td class="content1"><set:data name='LAST_PAYMENT_DATE'format='date' /></td>
                  <td class="content1"></td>
                  <td class="content1"></td>
                  <td class="content1"><set:label name="Minimum_Payment" defaultvalue="Minimum Payment" />:</td>
                  <td class="content1"><set:data name='MINIMUM_DUE' format='amount' /></td>
                </tr>
                <tr class="greyline">
                  <td class="content1"></td>
                  <td class="content1"><set:label name="Last_Payment_Amount" defaultvalue="Last Payment Amount" />:#</td>
                  <td class="content1"><set:data name='LAST_PAYMENT_AMOUNT' format='amount' /></td>
                  <td class="content1"></td>
                  <td class="content1"></td>
                  <td class="content1"><set:label name="Bonus_Point" defaultvalue="Bonus Point" />:</td>
                  <td class="content1"><set:data name='BONUS_POINT' format='amount' pattern='#,##0'/></td>
                </tr>
                <set:if name="showStatement" condition="equals" value="show">
                <tr>
                  <td class="content1"></td>
                  <td class="content1"><set:label name="Statement" defaultvalue="Statement" />:*</td>
                  <td class="content1" colspan="5"><set:data name="CARD_NO" db="statementForDetail"/></td>
                  </td>
                </tr>
                <tr class="greyline">
                   <td class="content1"></td>
                   <td class="content1" colspan="6"><b><set:label name="pdf_reader_tips" defaultvalue="#Up to last banking date" /></b>
                </tr>
                </set:if>
                <tr>
                  <td class="content1"></td>
                  <td class="content1" colspan="6"><set:label name="Up_To_Last_Banking_Date" defaultvalue="#Up to last banking date" /></td>
                </tr>                
              </table>
              
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="4" class="groupinput"><set:label name="Transaction_History" defaultvalue="Account Transaction History"/></td>
                </tr>
                <tr>
                  <td class="content1">                  
                    <div align="right">
                      <input id="date_range" name="date_range" type="radio" value="range" checked onClick="this.form.dateFrom.value='';this.form.dateTo.value='';this.form.dateFrom.disabled = true;this.form.dateTo.disabled = true;form.range.disabled = false;">
                        </div></td>
                  <td width="16%" class="content1"><set:label name="Range" defaultvalue="Range : "/></td>
                  <td width="76%" class="content1"><select id="range" name="range" onChange="">
                      <set:rblist file="app.cib.resource.enq.range"> <set:rboption name="range"/> </set:rblist>
                    </select>
                  </td>
                </tr>
                <tr class="greyline">
                  <td class="content1">                  
                    <div align="right">
                      <input id="date_range" name="date_range" type="radio" value="date" onClick="this.form.dateFrom.disabled = false;this.form.dateTo.disabled = false;form.range.disabled = true;">
                        </div></td>
                  <td class="content1"><set:label name='date_from' defaultvalue='From'/></td>
                  <td class="content1"><input name="dateFrom" type="text" id="dateFrom" size="12" maxlength="10" disabled value="<set:data name='dateFrom'/>">
                    <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" > &nbsp;&nbsp;&nbsp; <set:label name='date_to' defaultvalue='To'/> &nbsp;
                    <input name="dateTo" type="text" id="dateTo" size="12" maxlength="10" disabled value="<set:data name='dateTo'/>">
                    <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" > </td>
                </tr>
                <tr>
                  <td class="content1">&nbsp;</td>
                  <td class="content1" ><set:label name="View_by" defaultvalue="View by"/></td>
                  <td class="content1"><select id="viewBy" name="viewBy" onChange="">
                      <option value="">All</option>
                      <set:rblist db="txnFilterCC"> <set:rboption name="viewBy"/> </set:rblist>
                    </select>
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="54%" height="40" class="sectionbutton"><div align="right">
                      <input id="search" type="button" value="&nbsp;<set:label name='buttonSearch' defaultvalue='Search'/>&nbsp;" name="search" onClick="doSubmit('list')">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="listTransHistory">
                      <input name="accountNo" type="hidden" id="accountNo" value="<set:data name='CARD_NO'/>">
					  <input name="fromPage" type="hidden" id="fromPage" value="<set:data name='fromPage'/>">
					  <input name="callMethod" type="hidden" id="callMethod" value="<set:data name='callMethod'/>">
                    </div>
				  </td>
                  <td width="46%" height="40" align="right" valign="bottom" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                      <%--<tr>
                        <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=transHistoryView&logo=true&general=<set:label name="Credit_Card_Number" defaultvalue="Credit Card Number"/>,<set:data name="CARD_NO"/> - <set:data name="CARD_NO" db="accountDesc"/>&fileName=account_view&columnTitles=<set:label name='Transaction_Date'/>,<set:label name='Value_Date'/>,<set:label name='Description'/>,<set:label name='Transaction_Info'/>,<set:label name='Debit'/>,<set:label name='Credit'/>,<set:label name='Transaction_Nature'/>&columnNames=POST_DATE||format@date,EFFECTIVE_DATE||format@date,DESCRIPTION_EXCEL||specialChar@yes,TRANSACTION_INFO_EXCEL||specialChar@yes,POST_AMOUNT||format@amount,CREDIT||format@amount,TRANSACTION_NATURE||db@transactionNature">Download</a></td>
                      </tr>
                    --%></table>
				  </td>
                </tr>
              </table>
              
              
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1" align="left" valign="middle"><set:label name="Posting_Date" defaultvalue="Posting Date"/></td>
                  <td class="listheader1" align="left" valign="middle"><set:label name="Transation_Date" defaultvalue="Transation Date"/></td>
                  <td class="listheader1" align="left" valign="middle"><set:label name="Description" defaultvalue="Description"/></td>
                  <td class="listheader1" align="right" valign="middle"><set:label name="Debit" defaultvalue="Debit"/></td>
                  <td class="listheader1" align="right" valign="middle"><set:label name="Credit" defaultvalue="Credit"/></td>
                </tr>
                <set:list name="transHistoryView" showNoRecord="YES" showPageRows="10">
                <tr class="<set:listclass class1='' class2='greyline' />">
                  <td class="listcontent1" align="left"><set:listdata name="POST_DATE" format="date"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="EFFECTIVE_DATE" format="date"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="DESCRIPTION"/>
                  <br>
                  <set:listif name="ORIGINAL_CURRENCY" condition="notequals" value="MOP">
                  	<set:listif name="ORIGINAL_CURRENCY" condition="notequals" value="">
                      <set:label name="Original_Amount" defaultvalue="Original Amount"/>&nbsp;：&nbsp;<set:listdata name="ORIGINAL_CURRENCY"/>&nbsp; <set:listdata name="ORIGINAL_AMOUNT"/>
                    </set:listif>
                  </set:listif>
                  </td>
                  <set:listif name="DR_CR_CODE" condition="equals" value="D">
                  <td align="right" class="listcontent1"><set:listdata name="POST_AMOUNT" format="amount"/></td>
                  <td align="right" class="listcontent1">&nbsp;</td>
                  </set:listif>
                  <set:listif name="DR_CR_CODE" condition="equals" value="C">
                  <td align="right" class="listcontent1">&nbsp;</td>
                  <td align="right" class="listcontent1"><set:listdata name="POST_AMOUNT" format="amountcc"/></td>
                  </set:listif>
                </tr>
                </set:list>
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
