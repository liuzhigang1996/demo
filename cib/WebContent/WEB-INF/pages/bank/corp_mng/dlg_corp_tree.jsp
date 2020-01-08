<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/dialog.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
<link rel="stylesheet" type="text/css" href="/cib/css/dtree.css">
<SCRIPT language=JavaScript src="/cib/javascript/dtree.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	if("" == "<set:data name='selectCorpId'/>"){
	}else{
		d.openTo("<set:data name='selectCorpId'/>", true);
	}
}
function selectCorp(){
	var sel = document.getElementById("selectedId").value;
	if(sel != ""){
   		window.opener.changeCorp(document.getElementById("selectedId").value);
	}
	window.close();
}
function selectNode(arg){
	document.getElementById("selectedId").value = arg;
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corporation.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.corp_tree" -->
</head>
<body onLoad="pageLoad();">
<set:loadrb file="app.cib.resource.sys.corp_tree">
  <table width="500" border="0" cellspacing="0" cellpadding="0" height="40">
    <tr>
      <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="Select a Corporation"/><!-- InstanceEndEditable --></td>
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
    <td><form action="/cib/corporation.do" method="post" name="form1" id="form1">
      <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="label1">
				<div class="dtree">
      <p>Corporation structure | <a href="javascript: d.openAll();">open all</a> | <a href="javascript: d.closeAll();">close all</a></p>
      <script type="text/javascript">
			d = new dTree('d');
			if("" == "<set:data name='selectCorpId'/>"){
				d.clearCookie();
			}
			<set:if name="showRoot" condition="NOTEQUALS" value="Y">
			d.add("<set:data name='CORP_ID'/>","-1","<set:data name='CORP_NAME'/>(<set:data name='CORP_ID'/>)");
			</set:if>
			<set:if name="showRoot" condition="EQUALS" value="Y">
			d.add("<set:data name='CORP_ID'/>","-1","<set:data name='CORP_NAME'/>(<set:data name='CORP_ID'/>)", "javascript:selectNode('<set:data name='CORP_ID'/>');");
			</set:if>
			<set:list name="corpList">
			d.add("<set:listdata name='CORP_ID'/>","<set:listdata name='PARENT_CORP'/>","<set:listdata name='CORP_NAME'/>(<set:listdata name='CORP_ID'/>)","javascript:selectNode('<set:listdata name='CORP_ID'/>');");
			</set:list>
			document.write(d);
			</script>
    </div>
				</td>
              </tr>
		  </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton"><input id="buttonOk2" name="buttonOk2" type="button" value="<set:label name='buttonOK' defaultvalue=' OK '/>" onClick="selectCorp()">
                  <input id="selectedId" name="selectedId" type="hidden" value="">
                </td>
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
</set:loadrb>
</body>
<!-- InstanceEnd --></html>
