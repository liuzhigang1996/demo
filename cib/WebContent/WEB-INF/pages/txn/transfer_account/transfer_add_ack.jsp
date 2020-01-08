<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.transfer_bank">
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
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit()
{
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}
var popUpWin=0;
<%--function popUpWindow(URLStr, left, top, width, height)
{
  if(popUpWin)
  {
    if(!popUpWin.closed) popUpWin.close();
  }
  popUpWin = open(URLStr, 'popUpWin', 'toolbar=no,location=no,directories=no,status=no,menub ar=no,scrollbar=no,resizable=no,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
}
--%>
function popUpWindow(URLStr, Parameter, left, top, width, height)
{
  if(popUpWin)
  {
    if(!popUpWin.closed) popUpWin.close();
  }
  popUpWin = openPostWindow(URLStr, Parameter, 'popUpWin', 'toolbar=no,location=no,directories=no,status=no,menub ar=no,scrollbar=no,resizable=no,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
}
//mod by linrui 20190512 for PWC

</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferInBANK.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.transfer_bank" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="BANK Online Banking >Transfer > Accounts in BANK"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="TRANSFER TO ACCOUNTS IN BANK"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/transferInBANK.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<set:messages width="100%" cols="1" align="center"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr class="greyline">
                <td class="groupinput"  colspan="3" > <set:label name="Acknowledgement" defaultvalue="Acknowledgement"/> </td>
              </tr>
              <!-- modified by lzg 20190820 -->
              <set:if name = "corpType" condition="notequals" value = "3" >
              <tr>
                <td class="label1" width="28%" ><set:label name="Status" defaultvalue="Status"/></td>
				<td class="content1" width="72%" ><set:label name='ackStatusAccepted' /></td>
              </tr>
              </set:if>
              <!-- modified by lzg 20190820 -->
			  <tr class="greyline">
                <td class="label1"><set:label name="Referenc_No" defaultvalue="Reference No"/></td>
                <td class="content1"><set:data name='transId'/></td>
              </tr>
			   <tr>
                 <td class="label1" colspan="2"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
              </tr>
			  <tr class="greyline">
                <td class="label1"><set:label name="From_Account" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="2" ><set:data name='fromAccount' />
				</td>
              </tr>
			  <tr>
                <td class="label1"><set:label name="Amount_to_be_debited" defaultvalue="Amount to be debited"/></td>
                <td class="content1" colspan="2"><set:data name='fromCurrency' db="rcCurrencyCBS"/><set:data name='debitAmount' format="amount"/>
				</td>
              </tr>
			  <tr class="greyline">
                <td colspan="2"  class="label1"><b><set:label name="Transfer_To" defaultvalue="Transfer To"/></b></td>
              </tr>
			  <tr>
                <td class="label1"><set:label name="To_Account" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="2" ><set:data name='toAccount'/>
				</td>
              </tr>
              <!-- add by linrui 20181106 -->
              <tr class="greyline">
                <td class="label1"><set:label name="To_Name" defaultvalue="Beneficiary Name"/></td>
                <td class="content1" colspan="3" ><set:data name='toName' format='name'/>
				</td>
              </tr>
              <!-- end -->
			  <tr class="label1">
                <td class="label1"><set:label name="Transfer_Amount" defaultvalue="Transfer Amount"/></td>
                <td class="content1" colspan="2" ><set:data name='toCurrency' db="rcCurrencyCBS"/><set:data name='transferAmount' format="amount"/>
				</td>
              </tr>
			  <set:if name="fromCurrency" condition="NOTEQUALS" field="toCurrency">
			  <set:if name = "corpType" condition="notequals" value = "3" >
					  <tr>
		                <td class="label1"><set:label name="Exchange_Rate" defaultvalue="Exchange Rate"/></td>
		                <td class="content1" colspan="2"><set:data name='showExchangeRate' format="rate"/>&nbsp;&nbsp;<span style = "color:red;"><set:label name="Exchange_Rate_Tip" defaultvalue="The Rate is for reference only, the dealing rate will be quoted upon trading."/></span>
						</td>
		              </tr>
              	</set:if>
              	<set:if name = "corpType" condition="equals" value = "3" >
		              <tr>
		                <td class="label1"><set:label name="Exchange_Rate" defaultvalue="Exchange Rate"/></td>
		                <td class="content1" colspan="2"><set:data name='exchangeRate' format="rate"/>&nbsp;&nbsp;<span style = "color:red;"><set:label name="Exchange_Rate_Tip" defaultvalue="The Rate is for reference only, the dealing rate will be quoted upon trading."/></span>
						</td>
		              </tr>
              	</set:if>
			   </set:if>
              <tr class="greyline">
                <td class="label1"><set:label name="Remark" defaultvalue="Remark"/></td>
                <td class="content1" colspan="2"> <set:data name='remark'/></td>
              </tr>
              <!-- ADD BY LINRUI 20190911 -->
              <tr class="label1">
                <td class="label1"><set:label name="Remark_Host" defaultvalue="Remark"/></td>
                <td class="content1" colspan="2"> <set:data name='remarkHost'/></td>
              </tr>
              <!-- end -->
              <!-- add by lzg 20190820 -->
             <set:if name="serialNumber" condition="NOTEQUALS" value="">
             <tr class="greyline">
				<td class="label1"><set:label name="Serial_Number" defaultvalue="Serial Number"/></td>
               <td class="content1"><set:data name='serialNumber'/></td>
             </tr >
             </set:if>
             <!-- add by lzg end -->
			   </table>
			  <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <!-- add by lzg 20190820 -->
            <set:if name = "corpType" condition="notequals" value = "3" >
			 	<set:assignuser selectFlag="N" />
			 </set:if>
			 <!-- add by lzg end -->
			 <table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr>
                <td height="40" class="sectionbutton">
				<input id="submit1" name="submit1" type="button" value="<set:label name='submitl' defaultvalue=' submitl ' />" tabindex="3"  onClick="doSubmit();">
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="addLoad">
				<!-- add by lzg 20190624 for pwc-->
					<set:singlesubmit/>
				<!-- add by lzg end -->
								  </td>
				<td width="200" >
				  <table width="200" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <!-- <td class="buttonsavetemp"><a href="#" onClick="popUpWindow('/cib/transferInBANK.do?ActionMethod=saveTemplate&transId=<set:data name='transId'/>', 249, 300, 500, 200)"><set:label name="Save_As_Transfer_Template" defaultvalue="Save As Transfer Template"/></a></td> -->
                      <td class="buttonsavetemp"><a  onClick="postToMainFrame('/cib/transferInBANK.do?ActionMethod=saveTemplate',{transId:'<set:data name='transId'/>'})"><set:label name="Save_As_Transfer_Template" defaultvalue="Save As Transfer Template"/></a></td>
                    </tr>
                  </table></td>
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
