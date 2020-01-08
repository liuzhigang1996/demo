<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.enq.merchant_enquiry">
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
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	//add by wen_chy 20090923
}
function doEnquiry()
{
    if(validate_merchant_summary(document.getElementById("form1"))){
	  document.getElementById("queryFlag").value = 'true';
	  setFormDisabled("form1");
	  document.getElementById("form1").submit();
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/merchantEnquiry.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.merchant_enquiry" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_sum" defaultvalue="MDB Corporate Online Banking > Merchant Enquiry > Summary Information Enquiry"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_sum" defaultvalue="SUMMARY INFORMATION ENQUIRY"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/merchantEnquiry.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="merchant_summary" form="form1" file="merchant_summary_detail" /></td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="label1"><set:label name="Posting_Date" defaultvalue="Settlement Date"/> <input id="postingDate" name="postingDate" type="text" value="<set:data name='postingDate'/>" size="12" maxlength="10" >&nbsp;<img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('postingDate').disabled){scwShow(document.getElementById('postingDate'), this,language)};" >&nbsp;&nbsp;&nbsp;&nbsp;
				<set:label name="Merchant_Id" defaultvalue="Merchant ID"/> 
				<select name="merchantId" id="merchantId" onChange="">
					<option value="all"><set:label name="select_all"/></option>
					<set:list name='merchantIdList'>
					<option value="<set:listdata  name='MERCHANT_ID' />" <set:listselected key='MERCHANT_ID' equals='merchantId' output='selected'/>>
					<set:listdata  name='MERCHANT_ID' />
					</option>
					</set:list>
					</select>
					&nbsp;&nbsp;&nbsp;&nbsp;<input id="buttonOk" name="buttonOk" type="button" value="<set:label name='buttonEnquiry' defaultvalue=' Enquiry '/>" onClick="doEnquiry()">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<set:if name="downloadFlag" value="true" condition="EQUALS">
					<%--<img src="/cib/images/excel1.gif" width="35" height="36" align="absmiddle"><a href="/cib/DownloadCVS?listName=summaryDownloadList&fileName=merchant_summary_info&columnNames=POSTING_DATE,CARD_TYPE,SALES_COUNT||format@amount||decimal@false,SALES_AMOUNT||format@amount,RETURN_COUNT||format@amount||decimal@false,RETURN_AMOUNT||format@amount,TOTAL_COMMISSION||format@amount,NET_AMOUNT||format@amount&columnTitles=      ,      ,      ,      ,      ,      ,      ,      ,">Download</a>--%>
					</set:if>
                  <input id="ActionMethod" name="ActionMethod" type="hidden" value="listSummary">
                  <input id="queryFlag" name="queryFlag" type="hidden" value="<set:data name='queryFlag' />">
				  </td>
              </tr>
			  </table>
			  <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
			  <set:list name="summaryFilterList" level="1" showNoRecord="YES">
			  <tr><td colspan="8"><hr></td></tr>
			  <tr>
                <td class=""><strong><set:label name="Merchant_Id" defaultvalue="Merchant ID"/></strong></td>
                <td class=""><set:listdata name="MERID" inlevel="1"/></td>
				<td class=""><strong><set:label name="Merchant_Name" defaultvalue="Merchant Name"/></strong></td>
                <td class="" colspan="5"><set:listdata name="MERNAME" inlevel="1"/></td>
              </tr>
              <tr>
                <td class="listheader1"><set:label name="Posting_Date" defaultvalue="Settlement Date"/></td>
                <td class="listheader1"><set:label name="Card_Type" defaultvalue="Card Type"/></td>
                <td class="listheader1"><set:label name="Sales_Count" defaultvalue="Sales Count"/></td>
                <td class="listheader1"><set:label name="Sales_Amount" defaultvalue="Sales Amount"/></td>
				<td class="listheader1"><set:label name="Return_Count" defaultvalue="Refund Count"/></td>
				<td class="listheader1"><set:label name="Return_Amount" defaultvalue="Refund Amount"/></td>
				<td class="listheader1"><set:label name="Total_Commission" defaultvalue="Commission"/></td>
				<td class="listheader1"><set:label name="Net_Amount" defaultvalue="Net Amount"/></td>
              </tr>
			  <set:list name="summaryList" inlevel="1" level="2">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1"><set:listdata name="POSTING_DATE" inlevel="2"/></td>
                <td class="listcontent1"><set:listdata name="CARD_TYPE" inlevel="2"/></td>
                <td class="listcontent1"><set:listdata name="SALES_COUNT" inlevel="2"/></td>
                <td class="listcontent1"><set:listdata name="SALES_AMOUNT" inlevel="2"/></td>
				<td class="listcontent1"><set:listdata name="RETURN_COUNT" inlevel="2"/></td>
                <td class="listcontent1"><set:listdata name="RETURN_AMOUNT" inlevel="2"/></td>
                <td class="listcontent1"><set:listdata name="TOTAL_COMMISSION" inlevel="2"/></td>
				<td class="listcontent1"><set:listdata name="NET_AMOUNT" inlevel="2"/></td>
              </tr>
			  </set:list>
			  <tr>
                <td class="listcontent1" colspan="2"><set:label name="Sub_Total" defaultvalue="SUB-TOTAL"/></td>
                <td class="listcontent1"><set:listdata name="SCSUM" inlevel="1"/></td>
                <td class="listcontent1"><set:listdata name="SASUM" inlevel="1"/></td>
                <td class="listcontent1"><set:listdata name="RCSUM" inlevel="1"/></td>
				<td class="listcontent1"><set:listdata name="RASUM" inlevel="1"/></td>
				<td class="listcontent1"><set:listdata name="TCSUM" inlevel="1"/></td>
				<td class="listcontent1"><set:listdata name="NASUM" inlevel="1"/></td>
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
