<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/dialog.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bnk.corp_subsidiary">
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
		if (arg == 'enquiry') {
			document.getElementById("ActionMethod").value = 'listCorpForAdd';
			setFormDisabled("form1");
			document.getElementById("form1").submit();
		}else if(arg == 'add'){
			//add by linrui 20190327
			var corpSelect = true;
			var cor = document.getElementsByName("corpIds");
			var cor_len = cor.length;
			for (var i = 0; i< cor_len ;i++){
               if(cor[i].checked){
            	   corpSelect = false;
            	   break;
               }
			}
			if(corpSelect){
				alert("<set:label name='CorpId_Request'/>");
				return false;
			}
			//end
	if(validate_add(document.getElementById("form1"))){
			var fieldObject = document.getElementsByName("corpIds");
			var selectedCorpIds = "";
			if(isArray(fieldObject)){
				for (i = 0; i < fieldObject.length; i++) {
					if(fieldObject[i].checked==true){
						selectedCorpIds = selectedCorpIds + fieldObject[i].value + ";";
					}
				}
			}else{
				selectedCorpIds = fieldObject.value;
			}
   	  	   	window.opener.addSubList(selectedCorpIds);
			window.close();
	}
		}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpSubsidiary.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.corp_subsidiary" -->
</head>
<body onLoad="pageLoad();">

  <table width="500" border="0" cellspacing="0" cellpadding="0" height="40">
    <tr>
      <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" -->
        <set:label name="functionTitle" defaultvalue="Select Subsidiaries"/>
        <!-- InstanceEndEditable --></td>
      <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="1" bgcolor="#FF0000"><img src="/cib/images/menu_image/spacer.gif" width="1" height="1"></td>
          </tr>
      </table></td>
    </tr>
  </table>
  <table width="500" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
  <tr>
    <td width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
    <td><form action="/cib/corpSubsidiary.do" method="post" name="form1" id="form1">
      <!-- InstanceBeginEditable name="sectioncontent" -->
	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/> 
                            <set:fieldcheck name="sub_select" form="form1" file="corp_subsidiary" />                        </td>
                      </tr>
                </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="3">
            <tr>
              <td class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/></td>
              <td class="label1"><input id="corpId" name="corpId" type="text" value="<set:data name='corpId'/>" size="20" maxlength="20"></td>
            </tr>
            <tr class="greyline">
              <td class="label1"><set:label name="Corp_Name" defaultvalue="Company Name"/></td>
              <td class="label1"><input id="corpName" name="corpName" type="text" value="<set:data name='corpName'/>" size="20" maxlength="20"></td>
            </tr>
            <tr>
              <td height="40" colspan="2" align="center" class="label1">
			  <input id="buttonEnquiry" name="buttonEnquiry" type="button" value="<set:label name='buttonEnquiry' defaultvalue='Enquiry'/>" onClick="doSubmit('enquiry');">
                <input id="buttonAdd" name="buttonAdd" type="button" value="<set:label name='buttonAddSub' defaultvalue=' Add Subsidiary '/>" onClick="doSubmit('add')">
                <span class="sectionbutton">
                <input id="ActionMethod" name="ActionMethod" type="hidden" value="add">
                </span></td>
            </tr>
          </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="3">
            <tr>
              <td class="listheader1">&nbsp;</td>
              <td class="listheader1"><set:label name="Corp_Id" defaultvalue="Corporations ID"/></td>
              <td class="listheader1"><set:label name="Corp_Name" defaultvalue="Corporations Name"/></td>
              <td class="listheader1"><set:label name="Status" defaultvalue="Status"/></td>
            </tr>
            <set:list name="corpList" showNoRecord="YES">
              <tr>
                <td class="listcontent1"><set:listcheckbox name="corpIds" value="corpId" text=""/></td>
                <td class="listcontent1"><set:listdata name="corpId"/></td>
                <td class="listcontent1"><set:listdata name="corpName"/></td>
                <td class="listcontent1"><set:listdata name="status" rb="app.cib.resource.common.status"/></td>
              </tr>
            </set:list>
          </table>
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td class="groupseperator">&nbsp;</td>
            </tr>
          </table>
          <!-- InstanceEndEditable -->
    </form></td>
</table>
<table width="500" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>

</body>
</set:loadrb>
<!-- InstanceEnd --></html>
