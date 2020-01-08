<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<set:loadrb file="app.cib.resource.estatement.main">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
<title>Corporation Banking</title>
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js?v=20141217"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js?v=20141217"></SCRIPT>
</head>
	<frameset rows="10%,*" framespacing="0" frameborder="no" id = "e_top">
		<frame src="/cib/estatement.do?ActionMethod=showTitle" scrolling="no"  frameborder="0" >
		<frameset  cols="22%,*" framespacing="0" frameborder="no" id = "bottom">
			<frame src="/cib/estatement.do?ActionMethod=showMenu" scrolling="no" name="left" frameborder="0" >
			<frame src="/cib/estatement.do?ActionMethod=showCoNote" scrolling="yes" name="main" frameborder="0">
		</frameset>
	</frameset>
</set:loadrb>
</html>
