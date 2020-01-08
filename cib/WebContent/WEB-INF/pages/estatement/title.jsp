<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<set:loadrb file="app.cib.resource.estatement.main">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<title>Corporation Banking</title>
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<link rel="stylesheet" type="text/css" href="/cib/css/estatement.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js?v=20141217"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js?v=20141217"></SCRIPT>
<script type="text/javascript">
function pageLoad(){
	//parent.top.rows = parentDiv.offsetWidth +30  + ",*";
	//alert(parent.e_top.rows);
	var maniDiv = document.getElementById("mainDiv");
	parent.e_top.rows = mainDiv.offsetHeight +10+ ",*";
}
</script>
</head>

<body class="body1" onLoad="pageLoad();">
<div id = "mainDiv">
	<div name="pageheader" id="pageheader">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
  			<tr>
    			<td width="100%" height="18" class="navigationbar">
    				<set:label name="navigationTitle" />
    			</td>
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
	<div class = "functionTitle">
		<set:label name = 'functionTitle'/>
	</div>
</div>
</body>
</set:loadrb>
</html>
