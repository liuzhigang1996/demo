<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	if("<set:data name='hasNext'/>"!="YES"){
		setFieldDisabled("buttonNext");
	}
	if("<set:data name = 'loadFlag' />" == "editLoginMessage"){
    	LoadMessage();
    }
}
function doReturn(){
  setFormDisabled("form1");
  document.getElementById("ActionMethod").value = "returnList";
  document.getElementById("selectedWorkId").value = "<set:data name='workId'/>";
  document.form1.submit();
}
function doNext(){
  setFormDisabled("form1");
  document.getElementById("ActionMethod").value = "view";
  document.getElementById("workId").value = "<set:data name='nextWorkId'/>";
  document.getElementById("selectedWorkId").value = "<set:data name='workId'/>";
  document.form1.submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/approve.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.flow.approve" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_view" defaultvalue="MDB Corporate Online Banking > Authorization > Authorization Details"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_view" defaultvalue="AUTHORIZATION DETAILS"/><!-- InstanceEndEditable --></td>
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
                        <td><set:messages width="100%" cols="1" align="center"/></td>
                      </tr>
                </table>
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td width="28%" class="label1"><b>
                  <set:label name="Txn_Type_Cap" defaultvalue="OPERATION"/>
                </b></td>
                <td width="72%" class="content1"><b>
                  <set:data name="txnType" rb="app.cib.resource.common.subtype"/>
                </b></td>
              </tr>
              <tr>
                <td class="groupconfirm" colspan="2"><set:label name="Confirmation_Details" defaultvalue="DETAILS"/></td>
              </tr>
            </table>
			<%
			java.util.HashMap resultData = (java.util.HashMap) session
				.getAttribute("ResultData$Of$Neturbo");
		String url = (String) resultData.get("detailPageUrl");
			%>
			<jsp:include page="<%=url%>" flush="false" />
<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupconfirm"><set:label name="Confirmation_Status" defaultvalue="AUTHORIZATION STATUS"/></td>
              </tr>
			  <tr>
			    <td width="28%" class="listcontent1"><set:data name="procStatus" rb="app.cib.resource.flow.process_status"/></td>
                <td width="72%" class="listcontent1">
				<set:list name="progressList">
				<set:listif name="FinishFlag" condition="equals" value="1">
				<span class="textnormal"><set:listdata name="Dealer"/></span><br>
				</set:listif>
				<set:listif name="FinishFlag" condition="equals" value="0">
				<span class="textgray">&gt; <set:listdata name="Dealer" rb="app.cib.resource.flow.dealer_type"/></span><br>
				</set:listif>
				</set:list>				</td>
			  </tr>
			  </table>
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
               <tr>
                <td width="28%" class="listheader1"><set:label name="Work_Dealer" defaultvalue="Operator"/></td>
				<td width="12%" class="listheader1"><set:label name="Work_Deal_Time" defaultvalue="Time"/></td>
				<td width="12%" class="listheader1"><set:label name="Work_Action" defaultvalue="Operation"/></td>
				<set:if name= "FinanceFlag" condition="equals" value = "Y">
				<td width="7%" align="center" valign="middle" class="listheader1"><set:label name="Work_Level" defaultvalue="Level"/></td>
				</set:if>
				<td class="listheader1"><set:label name="Work_Memo" defaultvalue="Opinion"/></td>
</tr>
			  <set:list name="procWorkList" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline' />">
                <td class="listcontent1"><set:listdata name="workDealerName"/></td>
				<td class="listcontent1"><set:listdata name="dealEndTime" format="datetime"/></td>
				<td class="listcontent1"><set:listdata name="dealAction" rb="app.cib.resource.flow.approve_action"/></td>
				<set:if name= "FinanceFlag" condition="equals" value = "Y">
				<td align="center" class="listcontent1"><set:listdata name="approveLevel"/>&nbsp;</td>
				</set:if>
				<td class="listcontent1"><set:listdata name="dealMemo" maxlen="40"/>&nbsp;</td>
</tr>
</set:list>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" class="sectionbutton">
                  <input id="addToMulti" type="checkbox" name="addToMulti" value="YES" <set:selected key='addToMulti' equalsvalue='YES'/>><set:label name='Select_For_Multi'/> &nbsp;
                  <input id="buttonNext" name="buttonNext" type="button" value="<set:label name='buttonNext' defaultvalue='Next'/>" onClick="doNext()">
                  <input id="buttonReturn" name="buttonReturn" type="button" value="<set:label name='buttonReturn' defaultvalue='Return'/>" onClick="doReturn()">
                  <input id="ActionMethod" name="ActionMethod" type="hidden" value="list">
				  <input id="workId" name="workId" type="hidden" value="">	
				  <input id="selectedWorkId" name="selectedWorkId" type="hidden" value="">			  </td>
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
