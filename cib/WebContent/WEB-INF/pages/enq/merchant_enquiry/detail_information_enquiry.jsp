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
	//add by wen_chy 20070321
}
function doEnquiry()
{
    if(validate_merchant_detail(document.getElementById("form1"))){	   
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
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_det" defaultvalue="MDB Corporate Online Banking > Merchant Enquiry > Detail Information Enquiry"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_det" defaultvalue="DETAIL INFORMATION ENQUIRY"/><!-- InstanceEndEditable --></td>
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
						<set:fieldcheck name="merchant_detail" form="form1" file="merchant_summary_detail" /></td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="label1">
				<select name="dateType">
                <set:rblist file="app.cib.resource.enq.mer_enq_date">
                  <option value="<set:rbkey />" <set:rbselected equals="dateType"/>>
                  <set:rbvalue />
                  </option>
                </set:rblist>
                </select>
					 <input id="date" name="date" type="text" value="<set:data name='date'/>" size="12" maxlength="10" >&nbsp;<img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('date').disabled){scwShow(document.getElementById('date'), this,language)};" >&nbsp;&nbsp;&nbsp;&nbsp;
				<set:label name="Merchant_Id" defaultvalue="Merchant ID"/>
				<select name="merchantId" id="merchantId" onChange="">
					<set:list name='merchantIdList'>
					<option value="<set:listdata  name='MERCHANT_ID' />" <set:listselected key='MERCHANT_ID' equals='merchantId' output='selected'/>>
					<set:listdata  name='MERCHANT_ID' />
					</option>
					</set:list>
					</select> 
				&nbsp;&nbsp;&nbsp;&nbsp;<input id="buttonOk" name="buttonOk" type="button" value="<set:label name='buttonEnquiry' defaultvalue=' Enquiry '/>" onClick="doEnquiry()">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<%--<set:if name="downloadFlag" value="true" condition="EQUALS">
					<img src="/cib/images/excel1.gif" width="35" height="36" align="absmiddle">
					  <set:if name="dateType" value="s" condition="EQUALS">
					  <a href="/cib/DownloadCVS?listName=detailDownloadList&fileName=merchant_detail_info&columnNames=SETTLEMENT_DATE,TRANSACTION_TYPE,AUTHORIZED_NUMBER,CARD_NUMBER,TRANSACTION_CURRENCY,TRANSACTION_AMOUNT||format@amount&columnTitles=         ,         ,         ,               ,         ,         ,">Download</a>                  </set:if>
					  <set:if name="dateType" value="t" condition="EQUALS">
				      <a href="/cib/DownloadCVS?listName=detailDownloadList&fileName=merchant_detail_info&columnNames=TRANSACTION_DATE,TRANSACTION_TYPE,AUTHORIZED_NUMBER,CARD_NUMBER,TRANSACTION_CURRENCY,TRANSACTION_AMOUNT||format@amount&columnTitles=         ,         ,         ,               ,         ,         ,">Download</a>
				      </set:if>
					</set:if>						
                  --%><input id="ActionMethod" name="ActionMethod" type="hidden" value="listDetail">
				  <input id="queryFlag" name="queryFlag" type="hidden" value="<set:data name='queryFlag' />">
				  </td>
              </tr>
			  </table>
			  <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
			  <set:if name="merchantName" value="" condition="NOTEQUALS">
			   <tr>
                <td class=""><strong><set:label name="Merchant_Name" defaultvalue="Merchant Name"/></strong></td>
                <td class="" colspan="5"><set:data name="merchantName"/></td>
              </tr>
			  </set:if>
			  <set:list name="detailFilterList" level="1" showNoRecord="YES">
			  <tr><td colspan="6"><hr></td></tr>
			  <tr>
                <td class=""><strong><set:label name="Terminal_ID" defaultvalue="Terminal ID"/></strong></td>
                <td class="" colspan="5"><set:listdata name="TERID" inlevel="1"/></td>
              </tr>
              <tr>
                <td class="listheader1">
				<set:if name="dateType" value="s" condition="EQUALS">
				<set:label name="Settlement_Date" defaultvalue="Settlement Date"/>
				</set:if>
				<set:if name="dateType" value="t" condition="EQUALS">
				<set:label name="Transaction_Date" defaultvalue="Transaction Date"/>
				</set:if>
				</td>
                <td class="listheader1"><set:label name="Transaction_Type" defaultvalue="Transaction Type"/></td>
                <td class="listheader1"><set:label name="Authorized_Number" defaultvalue="Authorization Code"/></td>
                <td class="listheader1"><set:label name="Card_Number" defaultvalue="Card Number"/></td>
				<td class="listheader1"><set:label name="Transaction_Currency" defaultvalue="CCY"/></td>
				<td class="listheader1"><set:label name="Transaction_Amount" defaultvalue="Transaction Amount"/></td>
              </tr>
			  <set:list name="detailList" inlevel="1" level="2">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1">
				<set:if name="dateType" value="s" condition="EQUALS">
				<set:listdata name="SETTLEMENT_DATE" inlevel="2"/>
				</set:if>
				<set:if name="dateType" value="t" condition="EQUALS">
				<set:listdata name="TRANSACTION_DATE" inlevel="2"/>
				</set:if>
				</td>
                <td class="listcontent1"><set:listdata name="TRANSACTION_TYPE" inlevel="2"/></td>
                <td class="listcontent1"><set:listdata name="AUTHORIZED_NUMBER" inlevel="2"/></td>
                <td class="listcontent1"><set:listdata name="CARD_NUMBER" inlevel="2"/></td>
				<td class="listcontent1"><set:listdata name="TRANSACTION_CURRENCY" inlevel="2"/></td>
                <td class="listcontent1"><set:listdata name="TRANSACTION_AMOUNT" inlevel="2"/></td>
              </tr>
			  </set:list>
			  <tr>
                <td class="listcontent1" colspan="1"><strong><set:label name="Total_Count" defaultvalue="Total Count"/></strong></td>
                <td class="listcontent1" colspan="5"><set:listdata name="ROWSUM" inlevel="1"/></td> 
              </tr>
			  <tr>
                <td class="listcontent1" colspan="1"><strong><set:label name="Total_Amount" defaultvalue="Settlement Amount"/></strong></td>
                <td class="listcontent1" colspan="5"><set:listdata name="TASUM" inlevel="1"/></td> 
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
