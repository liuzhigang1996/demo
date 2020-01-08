<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bnk.viewer_access_list">
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
	if(arg == 'list'){
		setFormDisabled('form1');
		document.getElementById('ActionMethod').value = 'listCorp';
		document.getElementById("form1").submit();
	} else if (arg == 'update'){
		selectAddAssociated();
		setFormDisabled('form1');
		document.getElementById('ActionMethod').value = 'updateAccessList';
		document.getElementById("form1").submit();
	}
}
function selectAddAssociated(){
	var associatedList = document.getElementById('associatedList');
	if(associatedList.options.length>0){
		for(var i=0; i<associatedList.options.length; i++){
			associatedList.options[i].selected = true;
		}
	}
}
function moveSelected(oSourceSel,oTargetSel) {
	var arrSelValue = new Array();
    var arrSelText = new Array();
    var arrValueTextRelation = new Array();
	var index = 0;
    for(var i=0; i<oSourceSel.options.length; i++) {
		if(oSourceSel.options[i].selected) {
        	arrSelValue[index] = oSourceSel.options[i].value;
        	arrSelText[index] = oSourceSel.options[i].text;
        	arrValueTextRelation[arrSelValue[index]] = oSourceSel.options[i];
        	index ++;
        }
     }
     for(var i=0; i<arrSelText.length; i++) {
         var oOption = document.createElement("option");
         oOption.text = arrSelText[i];
         oOption.value = arrSelValue[i];
         oTargetSel.add(oOption);
         oSourceSel.removeChild(arrValueTextRelation[arrSelValue[i]]);
     }
}
function associate(){
	var unassociatedList = document.getElementById('unassociatedList');
	var associatedList = document.getElementById('associatedList');
	moveSelected(unassociatedList, associatedList);
}
function unassociate(){
	var unassociatedList = document.getElementById('unassociatedList');
	var associatedList = document.getElementById('associatedList');
	moveSelected(associatedList, unassociatedList);
}
function associateAll(){
	var unassociatedList = document.getElementById('unassociatedList');
	var associatedList = document.getElementById('associatedList');
	if(unassociatedList.options.length>0){
		for(var i=0; i<unassociatedList.options.length; i++){
			unassociatedList.options[i].selected = true;
		}
	}
	moveSelected(unassociatedList, associatedList);
}
function unassociateAll(){
	var unassociatedList = document.getElementById('unassociatedList');
	var associatedList = document.getElementById('associatedList');
	if(associatedList.options.length>0){
		for(var i=0; i<associatedList.options.length; i++){
			associatedList.options[i].selected = true;
		}
	}
	moveSelected(associatedList, unassociatedList);
}
</script>
  <!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/viewerAccessList.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.viewer_access_list" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
          <set:label name="navigationTitle_set" defaultvalue="MDB Corporate Online Banking > Viewer Access List > Viewer Access List"/>
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
        <set:label name="functionTitle_set" defaultvalue="VIEWER ACCESS LIST"/>
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
		  <form action="/cib/viewerAccessList.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><set:messages width="100%" cols="1" align="center"/>
                    </td>
                  </tr>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td class="groupseperator" colspan="3">&nbsp;</td>
                  </tr>
                  <tr>
                    <td colspan="2" class="label1"><set:label name="Viewer" defaultvalue="Viewer"/></td>
                    <td width="89%" class="label1"><select name="viewerId" id="viewerId">
                        <set:list name="viewerList">
                          <set:listoption name="viewerId" value="USER_ID" text="USER_NAME"/>
                        </set:list>
                      </select></td>
                  </tr>
                  <tr>
                    <td class="groupseperator" colspan="3">&nbsp;</td>
                  </tr>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td height="40" colspan="2" class="sectionbutton"><input id="submit1" name="submit1" type="button" value="<set:label name='buttonListCorp' defaultvalue='List Corporation'/>" tabindex="3"  onClick="doSubmit('list');">
                      <!--                  <input id="bottonReset" name="bottonReset" type="reset" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> --></td>
                  </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3" id="corpList">
                  <tr class="greyline">
                    <td width="32%" class="listheader1"><set:label name="available_corp" defaultvalue="Available Corporation(s)"/></td>
                    <td class="listheader1">&nbsp;</td>
                    <td width="60%" class="listheader1"><set:label name="selected_corp" defaultvalue="Selected Corporation(s)"/></td>
                  </tr>
                  <tr class="">
                    <td align="left" class="listcontent1"><select name="unassociatedList" size="15" multiple id="unassociatedList" style="width:300">
                      <set:list name="candidateCorpList">
                        <set:listoption name="unassociatedList" value="CORP_ID" text="CORP_NAME"/>
                      </set:list>
                    </select></td>
                    <td align="center" class="listcontent1"><p>
                        <input id="associated" type="button" name="associated" value=" &gt;&gt; " onClick="associate();">
                        <br>
                        <input id="unassociated" type="button" name="unassociated" value=" &lt;&lt; " onClick="unassociate();" size="20">
                      </p>
                      <p>
                        <input id="associated2" type="button" name="associated2" value="<set:label name='buttonall' defaultvalue='ALL'/>  &gt;&gt; " onClick="associateAll();">
                        <input id="unassociated2" type="button" name="unassociated2" value=" &lt;&lt;  <set:label name='buttonall' defaultvalue='ALL'/>" onClick="unassociateAll();" size="20">
                        <br>
                      </p></td>
                    <td align="left" class="listcontent1">
                      <select name="associatedList" size="15" multiple id="associatedList" style="width:300">
                        <set:list name="selectedCorpList">
                          <set:listoption name="associatedList" value="CORP_ID" text="CORP_NAME"/>
                        </set:list>
                      </select>                    </td>
                  </tr>
                  <tr>
                    <td class="groupseperator" colspan="3">&nbsp;</td>
                  </tr>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" id="updateButton">
                  <tr>
                    <td height="40" colspan="2" class="sectionbutton"><input id="submit1" name="submit1" type="button" value="<set:label name='buttonUpdate' defaultvalue='Update'/>" tabindex="3"  onClick="doSubmit('update');">
                      <!--                  <input id="bottonReset" name="bottonReset" type="reset" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
                      <input name="ActionMethod" type="hidden" id="ActionMethod" value="list"></td>
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
