<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bat.schedule_transfer_list">
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
var m1 = null;
var b1 = null;
var u1 = null;
var r1 = null;
function pageLoad(){
	m1 = document.getElementById('m');
	b1 = document.getElementById('b');
	u1 = document.getElementById('u');
	r1 = document.getElementById('r');
	var transId = '<set:data name="transId"/>';
	if(transId != ''){
		document.getElementById(transId).checked = true;
		document.getElementById(transId).onclick();
	}
}

function viewDetail(transId,status) {
	try{
	document.getElementById(transId).checked=true;
	document.getElementById(transId).onclick();
	document.form1.status.value=status;
	
	document.form1.ActionMethod.value = 'viewDetail';
	document.form1.submit();
	setFormDisabled('form1');
	}catch(e){
		alert ("View Detail ERROR: "+e.message);
    }
}
function doSubmit(arg) {
	if(validate_transferList(document.getElementById("form1"))) {
			if (arg == 'block') {
				document.form1.ActionMethod.value = 'blockLoad';
			} else if (arg == 'modify') {
				document.form1.ActionMethod.value = 'editLoad';
			} else if (arg == 'remove') {
				document.form1.ActionMethod.value = 'deleteLoad';
			} else if (arg == 'view') {
			   document.form1.ActionMethod.value = 'viewDetail';
			} else if (arg == 'unblock') {
				document.form1.ActionMethod.value = 'unblockLoad';
			}
			document.form1.submit();
			setFormDisabled('form1');
		}
}

function setParams(p1, p2, p3, p4) {
	document.getElementById('scheduleId').value=p1;
	document.getElementById('frequenceType').value=p2;
	document.getElementById('frequenceDays').value=p3;
	document.getElementById('scheduleName').value=p4;
}
//check for disabling the button add by hjs
function checkStatus(status) {
	if (status == '0') { //normal
		m1.disabled = false;
		b1.disabled = false;
		u1.disabled = true;
		r1.disabled = false;
	} else if (status == '2') { //block
		m1.disabled = true;
		b1.disabled = true;
		u1.disabled = false;
		r1.disabled = true;
	} else { //remove && pending
		m1.disabled = true;
		b1.disabled = true;
		u1.disabled = true;
		r1.disabled = true;
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/scheduleTransferBANK.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bat.schedule_transfer_list" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="990" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="BANK Online Banking >Transfer >Accounts in Banks Overseas"/><!-- InstanceEndEditable --></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#" onClick="printPage('pageheader');"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
	<!--
    <td class="buttonhelp"><a href="#"><img src="/cib/images/shim.gif" width="36" height="18" border="0"></a></td>
	-->
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="003399">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="FF0000">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="40">
  <tr>
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="TRANSFER TEMPLATE"/><!-- InstanceEndEditable --></td>
    <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" bgcolor="#FF0000"><img src="/cib/images/shim.gif" width="1" height="1"></td>
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
		  <form action="/cib/scheduleTransferBANK3rd.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="transferList" form="form1" file="schedule_transfer" /> </td>
                </tr>
              </table>
              <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_c"><!-- /* Modify by long_zg 2019-05-22 UAT6-246 COB：新增的第三者轉賬未完全改好  begin */ --><!-- <set:label name="Accounts_in_BANK" defaultvalue="Accounts in BANK"/> --> <a onClick="toTargetFormgoToTarget('/cib/scheduleTransferBANK.do?ActionMethod=list')" href="#"><set:label name="TO_MY_ACCOUNT" defaultvalue="To My Account"/></a><!-- /* Modify by long_zg 2019-05-22 UAT6-246 COB：新增的第三者轉賬未完全改好  end */ --></td>
                        <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                  </table></td>
                  <!-- /* Add by long_zg 2019-05-22 UAT6-246 COB：新增的第三者轉賬未完全改好  begin */ -->
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_o"><set:label name="TO_MDB_BANK_ACCOUNT" defaultvalue="To MDB Account"/></td>
                        <td class="rtab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                  </table></td>
                  <!-- /* Add by long_zg 2019-05-22 UAT6-246 COB：新增的第三者轉賬未完全改好  end */ -->
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_c"><a onClick="toTargetFormgoToTarget('/cib/scheduleTransferMacau.do?ActionMethod=list')" href="#"><!-- /* Modify by long_zg 2019-05-22 UAT6-246 COB：新增的第三者轉賬未完全改好  begin */ --><!-- <set:label name="Accounts_in_Macau" defaultvalue="Accounts in Other Banks in Macau"/> --> <set:label name="TO_LOCAL_BANK_ACCOUNT" defaultvalue="To Local Bank Account"/> <!-- /* Modify by long_zg 2019-05-22 UAT6-246 COB：新增的第三者轉賬未完全改好  end */ --></a></td>
                        <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                  </table></td>
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_c"><a onClick="toTargetFormgoToTarget('/cib/scheduleTransferOversea.do?ActionMethod=list')" href="#"><!-- /* Modify by long_zg 2019-05-22 UAT6-246 COB：新增的第三者轉賬未完全改好  begin */ --><!-- <set:label name="Accounts_in_Overseas" defaultvalue="Accounts in Banks Overseas"/> --> <set:label name="TO_OVERSEAS_BANK_ACCOUNT" defaultvalue="To Overseas Bank Account"/> <!-- /* Modify by long_zg 2019-05-22 UAT6-246 COB：新增的第三者轉賬未完全改好  end */ --></a></td>
                        <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                  </table></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td width="30" class="listheader1"></td>
                  <td class="listheader1"><set:label name="FROM_ACCOUNT"/></td>
                  <td class="listheader1"><set:label name="BENEFICIARY_ACCOUNT"/></td>
                  <td class="listheader1"><set:label name="SCHEDULE"/></td>
                  <td class="listheader1"><set:label name="STATE"/></td>
                  <td align="center" class="listheader1"><set:label name="view_detail" rb="app.cib.resource.bat.sch_transfer_history"/></td>
                </tr>
                <set:list name="toList"  showPageRows="10" showNoRecord="YES">
                <!-- add by lzg 20190605 -->
                <set:listif name="status" condition="NOTEQUALS" value="9">
                <!-- add by lzg end -->
				  <label for="transId_<set:listdata name='transId'/>">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td align="center" class="listcontent1">
               	  <input type="radio" name="transId" id="<set:listdata name='transId'/>" onClick="checkStatus('<set:listdata name='status'/>');setParams(&quot;<set:listdata name='scheduleId'/>&quot;, &quot;<set:listdata name='frequnceType'/>&quot;, &quot;<set:listdata name='frequnceDays'/>&quot;, &quot;<set:listdata name='scheduleName'/>&quot;);" value="<set:listdata name='transId'/>">
				  
				  </td>
                  <td class="listcontent1"><set:listdata  name="fromAccount"  /></td>
                  <td class="listcontent1"><set:listdata name="toAccount"/></td>
                  <td class="listcontent1"><set:listdata name="frequnceType" rb="app.cib.resource.bat.frequence_type"/></td>
                  <td class="listcontent1"><set:listdata name="status" rb="app.cib.resource.common.status"/> 
				  <!--
                  <td align="center" class="listcontent1"><a href="#" onClick="document.getElementById('<set:listdata name='transId'/>').checked=true;document.getElementById('<set:listdata name='transId'/>').onclick();document.form1.status.value='<set:listdata name='status'/>';doSubmit('view');"><set:label name="view_detail" rb="app.cib.resource.bat.sch_transfer_history"/></a></td>
				  -->
                  <td align="center" class="listcontent1"><a href="#" onClick="viewDetail('<set:listdata name='transId'/>','<set:listdata name='status'/>')"><set:label name="view_detail" rb="app.cib.resource.bat.sch_transfer_history"/></a></td>
                </tr>
					</label>
					</set:listif>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton">
				  	<input id="m" name="modify" type="button" value="<set:label name='Modify' defaultvalue=' Modify '/>" disabled onClick="doSubmit('modify')">
                    <input id="b" name="block" type="button" value="<set:label name='Block' defaultvalue=' Block '/>"   disabled onClick="doSubmit('block')">
					<input id="u" name="unblock" type="button" value="<set:label name='Unblock' defaultvalue=' Unblock '/>"   disabled onClick="doSubmit('unblock')">
                    <input id="r" name="remove" type="button" value="<set:label name='Remove' defaultvalue=' Remove '/>"  disabled onClick="doSubmit('remove')">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="add">
                    <!-- add by lzg 20190624 for pwc-->
					<set:singlesubmit/>
				  <!-- add by lzg end -->
                    <span class="listcontent1">
                    <input name="scheduleId" type="hidden" id="scheduleId">
                    <input name="frequenceType" type="hidden" id="frequenceType">
                    <input name="frequenceDays" type="hidden" id="frequenceDays">
                    <input name="scheduleName" type="hidden" id="scheduleName">
                    <input name="status" type="hidden" id="status">
                  </span> </td>
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
  <tr bgcolor="FF0000">
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
