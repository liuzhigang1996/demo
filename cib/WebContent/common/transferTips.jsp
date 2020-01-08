<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="/cib/css/txnPro.css">
<script type="text/javascript">
</script>
</head>
<body>
<br/>
<div id = "showENG" style = "display:none">
	<div class= "myTextarea"><set:data name = 'descriptionE' /></div>
	<br/><br/><set:label name="mdb_limited"/><a href="javascript:void(0);" onclick="window.open('/cib/common/MDB_Limited.jsp');" target="_top"><set:label name="mdb_limited_url"/></a>
</div>

<div id = "showCHN" style = "display:none">
	<div class= "myTextarea"><set:data name = 'descriptionC' /></div>
	<br/><br/>如需查詢更多信息,<a href="javascript:void(0);" onclick="window.open('/cib/common/MDB_Limited_ZH.jsp');" target="_top">請參閱條款</a>
</div>
</body>
</html>
