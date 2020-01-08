<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/upload.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
}
function doSubmit(arg) {
	if(true){	
		if (arg == 'ok') {
		if(validate_bank_draft_check(document.getElementById("form1"))){
			document.form1.ActionMethod.value = 'uploadFile';
			document.getElementById("form1").submit();
			setFormDisabled("form1");
		}
		}
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/stopChequeBatchRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.stop_check_request" --><!-- InstanceParam name="enctype" type="text" value="multipart/form-data" -->
</head>
<body class="body1" onLoad="pageLoad();">
<set:loadrb file="app.cib.resource.srv.stop_check_request">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitleBatch" defaultvalue="BANK Online Banking >Services > Bank Draft Batch Request "/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitleBatch" defaultvalue="BANK DRAFT BATCH REQUEST"/><!-- InstanceEndEditable --></td>
    <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" bgcolor="#FF0000"><img src="/cib/images/menu_image/spacer.gif" width="1" height="1"></td>
        </tr>
      </table></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="19"><img src="/cib/images/table_top_long.gif" width="100%" height="19"></td>
  </tr>
  <tr>
    <td align="center" bgcolor="#999999"><table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
        <tr>
          <td width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
          <td>
		  <form action="/cib/stopChequeBatchRequest.do" method="post" enctype="multipart/form-data" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="stop_cheque_check" form="form1" file="batch_request" /> </td>
                </tr>
              </table>
			  <table border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td><table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      <td class="tab1_c"><a href="stopChequeRequest.do?ActionMethod=stopLoad"><set:label name="Cheque_single" defaultvalue="Bank Draft Request"/></a></td>
                      <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                    </tr>
                </table></td>
                <td><table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td class="ltab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      <td class="tab1_o">
                        <set:label name="Cheque_batch" defaultvalue="Bank Draft Batch Request"/></td>
                      <td class="rtab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                    </tr>
                </table></td>
                <td></td>
              </tr>
            </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3" id="round">
                <tr>
                  <td colspan="3" class="groupinput">&nbsp;</td>
                </tr>
                <tr class="">
                  <td colspan="2" class="label1" width="92%"><set:label name="notice" defaultvalue="notice"/></td>
                  <td class="label1" width="8%"><table width="75" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td class="buttonhelp1"><script language="JavaScript">
function popWindow(url){
		var popWin1= window.open(url,"temp1","toolbar=no,location=no,top=100,left=0,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=600");
		popWin1.focus();
}
                  </script>
                        <a href="#" onClick="popWindow('<set:label name='STOP_CHEQUE_N' rb='app.cib.resource.common.upload_sample'/>')"><set:label name="help" rb="app.cib.resource.common.operation"/></a></td>
                    </tr>
                  </table></td>
                </tr>
                <tr class="greyline">
                  <td width="28%" class="label1"><set:label name="choose_file" defaultvalue="choose_file"/></td>
                  <td width="72%" colspan="2" class="content1"><input name="uploadFile" type="file" id="uploadFile" value="<set:data name='uploadFile'/>" onKeyDown="this.blur();" onpaste="return false;" size="70">                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton">
                      <input id="ok" name="ok" type="button" value="&nbsp;&nbsp;<set:label name='buttonOK' defaultvalue=' submit '/>&nbsp;&nbsp;" onClick="doSubmit('ok')">
<!--                      <input id="reset" name="reset" type="reset" value="&nbsp;&nbsp;<set:label name='buttonReset' defaultvalue=' reset '/>&nbsp;&nbsp;"> -->
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="uploadFile">
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
</set:loadrb>
</body>
<!-- InstanceEnd --></html>
