<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.chequeBookRequest">
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
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	var mailName = '<set:data name='Pickuptype'/>';
	if("P"==mailName){
		document.getElementById("mailName").style.display = 'none';
		document.getElementById("mailAdress").style.display = 'none';
		}
}
function doSubmit(arg){
	document.getElementById("ActionMethod").value="addLoad" ;
		document.getElementById("form1").submit() ;
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/chequeBookRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.chequeBookRequest" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
    <set:label name="navigationTitleRequest" defaultvalue="BANK Online Banking >Cheque > Cheque Book Request"/>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" -->
        <set:label name="chequeBookRequestTitle" defaultvalue="CHEQUE BOOK REQUEST"/>
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
		  <form action="/cib/chequeBookRequest.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            
			<set:messages width="100%" cols="1" align="center"/>
            <set:fieldcheck name="stop_check" form="form1" file="check_request" />
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Acknowledgement" defaultvalue="Acknowledgement"/></td>
              </tr>
              <!-- modified by lzg 20190820 -->
              <set:if name = "corpType" condition="notequals" value = "3" >
             <tr>
               <td width="28%" class="label1"><set:label name="ack_status" defaultvalue="ack_status"/></td>
               <td width="72%" class="content1"><set:label name='ackStatusAccepted' /></td>
             </tr>
             </set:if>
             <tr>
               <td width="28%" class="label1"><set:label name="reference_No" defaultvalue="reference_No"/></td>
               <td width="72%" class="content1"><set:data name='transNo'/></td>
             </tr>
           </table>
           <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
          		<tr>
                <td class="label1" width="28%"><set:label name="Current_Account" defaultvalue="Current Account"/></td>
                <td class="content1" width="72%">
                <set:data name='payCurrency' db="rcCurrencyCBS"/> - <set:data name='accountNo' />
                </td>
              </tr>   
			   <tr>
                <td class="label1"><set:label name="No_Of_Books" defaultvalue="No. Of Books"/></td>
                <td class="content1"><set:data name='noOfBook' format='amount' pattern='#' /></td>
              </tr><%--
			  <tr>
                <td class="label1"><set:label name="Pickup_Branch" defaultvalue="Pickup Branch"/></td>
                <td class="content1">
                <set:data name='pickupBranchCode' db='branch' />
                </td>
              </tr>
            --%>
            <!-- add by linrui 20180326 begin-->
            
            <!-- add by lzg 20190606 -->
             <tr>
                <td class="label1" ><set:label name="Pickup_Type_Label" defaultvalue="Pickup Type"/></td>
                <td class="content1"><set:data  name='Pickuptype' rb = "app.cib.resource.srv.pick_up_type" /></td>
              </tr>
             <!-- add by lzg end -->
            <tr id="mailName">
                <td class="label1"><set:label name="Local_Mail_Name" defaultvalue="Name"/></td>
                <td class="content1">
                    
                    <set:data name='mailName1'/>
                    
                    <set:data name='mailName2'/>
                    
                </td>
            </tr>
            <tr id="mailAdress">
                <td class="label1"><set:label name="Local_Mail_Adress" defaultvalue="Adress"/></td>
                <td class="content1">
                   
                    <set:data name='mailAdress1'/>
                                   
                    <set:data name='mailAdress2'/>
                   
                    <set:data name='mailAdress3'/>
                   
                </td>
            </tr>
            <!-- end -->
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
			  <td colspan="2" class="sectionbutton">
                <input id="submit1" name="submit1" type="button" value="<set:label name='Do_it_again' defaultvalue=' Do it again ' />" tabindex="3"  onClick="doSubmit('addLoad');">
                <input id="ActionMethod" name="ActionMethod" type="hidden" value="addLoad">
                <!-- add by lzg 20190624 for pwc-->
					<set:singlesubmit/>
				  <!-- add by lzg end -->
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
