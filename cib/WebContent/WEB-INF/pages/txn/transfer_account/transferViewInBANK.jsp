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
<SCRIPT language=JavaScript src="/cib/javascript/common1.js?v=20190530"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
  <script language=JavaScript>
  var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
var popUpWin=0;
function popUpWindow(URLStr, left, top, width, height)
{
  if(popUpWin)
  {
    if(!popUpWin.closed) popUpWin.close();
  }
  popUpWin = open(URLStr, 'popUpWin', 'toolbar=no,location=no,directories=no,status=no,menub ar=no,scrollbar=no,resizable=no,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
}
function doReturn()
{
	document.getElementById("form1").submit();
}
</script>
  <!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferHistory.do" --><!-- InstanceParam name="help_href" type="text" value="#" passthrough="true" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.transfer_bank" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
    	<!-- /* Modify by long_zg 2015-05-30 UAT6-482 COB 转账历史记录查询，筛选第三者转账，点击查看详情，显示的是TO MY ACCOUNT begin */  -->
          <!--<set:label name="navigationTitle" defaultvalue="BANK Online Banking >Transfer > Accounts in BANK"/>-->
          <set:if name='ownerAccFlag' condition='NOTEQUALS' value='N' ><set:label name="navigationTitle" defaultvalue="BANK Online Banking >Transfer > Accounts in BANK"/></set:if>
          <set:if name='ownerAccFlag' condition='EQUALS' value='N' ><set:label name="navigationTitle3rd" defaultvalue="BANK Online Banking >Transfer > Accounts in BANK"/></set:if>
          <!-- /* Modify by long_zg 2015-05-30 UAT6-482 COB 转账历史记录查询，筛选第三者转账，点击查看详情，显示的是TO MY ACCOUNT end */  -->
          <!-- InstanceEndEditable --></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#" onClick="printPage('pageheader');"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
	<!--
    <td class="buttonhelp"><a href="@@@(help_href)@@@"><img src="/cib/images/shim.gif" width="36" height="18" border="0"></a></td>
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
        <set:label name="functionTitleDetail" defaultvalue="TRANSFER IN BANK DETAIL"/>
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
		  <form action="/cib/transferHistory.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td class="groupseperator">&nbsp;</td>
                  </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td class="label1" colspan="2"><b>
                      <set:label name="Transfer_From" defaultvalue="Transfer From"/>
                      </b></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1" width="28%"><set:label name="From_Account" defaultvalue="Account Number"/></td>
                    <td class="content1" width="72%"><set:data name='fromAccount'/>
                    </td>
                  </tr>
                  <tr >
                    <td class="label1"><set:label name="Amount_to_be_debited" defaultvalue="Amount to be debited"/></td>
                    <td class="content1"><set:data name='fromCurrency' db="rcCurrencyCBS"/>
                      <set:data name='debitAmount' format="amount"/>
                    </td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1" colspan="2"><b>
                      <set:label name="Transfer_To" defaultvalue="Transfer To"/>
                      </b></td>
                  </tr>
                  <tr >
                    <td class="label1"><set:label name="To_Account" defaultvalue="Account Number"/></td>
                    <td class="content1" colspan="3" ><set:data name='toAccount'/>
                    </td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Transfer_Amount" defaultvalue="TransferAmount"/></td>
                    <td class="content1"><set:data name='toCurrency' db="rcCurrencyCBS"/>
                      <set:data name='transferAmount' format="amount"/>
                    </td>
                  </tr>
                  <set:if name="fromCurrency" condition="NOTEQUALS" field="toCurrency">
                    <tr>
                      <td class="label1"><set:label name="Exchange_Rate" defaultvalue=" Exchange Rate"/></td>
                      <td class="content1"><set:data name='exchangeRate' format="rate"/>&nbsp;&nbsp;<span style = "color:red;"><set:label name="Exchange_Rate_Tip" defaultvalue="The Rate is for reference only, the dealing rate will be quoted upon trading."/></span></td>
                    </tr>
                  </set:if>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Remark" defaultvalue="Remark"/></td>
                    <td class="content1"><set:data name='remark'/>
                  </tr>
                  <!-- ADD BY LINRUI 20190911 -->
                  <tr class="label1">
                    <td class="label1"><set:label name="Remark_Host" defaultvalue="Remark"/></td>
                    <td class="content1" colspan="2"> <set:data name='remarkHost'/></td>
                  </tr>
                  <!-- end -->
                  <!-- add by lzg 20190613 -->
             <set:if name="serialNumber" condition="NOTEQUALS" value="">
             <tr class="greyline">
				<td class="label1"><set:label name="Serial_Number" defaultvalue="Serial Number"/></td>
               <td class="content1"><set:data name='serialNumber'/></td>
             </tr >
             </set:if>
             <tr >
				<td class="label1"><set:label name="Transaction_Time" /></td>
               <td class="content1"><set:data name='executeTime' /></td>
             </tr >
             <!-- add by lzg end -->
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td class="groupseperator">&nbsp;</td>
                  </tr>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <%-- <td height="40" class="sectionbutton"><input id="submit1" name="submit1" type="button" value="<set:label name='Return' defaultvalue=' Return ' />" tabindex="3"  onClick="document.forms[0].submit();"> --%>
                    <td height="40" class="sectionbutton"><input id="submit1" name="submit1" type="button" value="<set:label name='Return' defaultvalue=' Return ' />" tabindex="3"  onClick="doReturn();">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="listHistory">
                      <input id="business_type" name="business_type" type="hidden" value="<set:data name='business_type'/>">
                      <input id="dateFrom" name="dateFrom" type="hidden" value="<set:data name='dateFrom'/>">
                      <input id="dateTo" name="dateTo" type="hidden" value="<set:data name='dateTo'/>">
                      <input id="date_range" name="date_range" type="hidden" value="<set:data name='date_range'/>">
                      <input id="isBack" name="isBack" type="hidden" value="Y">
                      <input id="methodType" name="methodType" type="hidden" value="viewInBANK">
                      <input id="fromAccount" name="fromAccount" type="hidden" value="<set:data name='fromAccount'/>">
                      
                      <!-- add by lzg 20190624 for pwc-->
						<set:singlesubmit/>
				  	  <!-- add by lzg end -->
                    </td>
                    <td width="200" ><table width="200" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td class="buttonsavetemp">
                          <!-- /* Modify by long_zg 2015-05-30 UAT6-484 COB：第三者交易歷史顯示保存模板後，點擊close沒關閉彈窗，顯示未知內容 begin */  -->
                            <!-- <a href="#" onClick="popUpWindow('/cib/transferInBANK.do?ActionMethod=saveTemplate&transId=<set:data name='transId'/>', 249, 300, 500, 200)">
                            <set:label name="Save_As_Transfer_Template" defaultvalue="Save As Transfer Template"/>
                            </a> -->
                            <a  onClick="postToMainFrame('/cib/transferHistory.do?ActionMethod=saveTemplate',{transId:'<set:data name='transId'/>',tmp:'BAN'})"><set:label name="Save_As_Transfer_Template" defaultvalue="Save As Transfer Template"/></a>
                            <!-- /* Modify by long_zg 2015-05-30 UAT6-484 COB：第三者交易歷史顯示保存模板後，點擊close沒關閉彈窗，顯示未知內容 end */  -->
                           </td>
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
