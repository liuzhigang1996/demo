<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<title>Corporation Banking</title>
</head>
<frameset rows="135,*,17" cols="*" frameborder="NO" border="0" framespacing="0">
      <frame name="topFrame" src="/cib/common/bank/top.jsp?TimeStamp=<set:data name='TimeStamp'/>" scrolling="NO" noresize marginheight="0" marginwidth="0">
      <frame name="mainFrame" src="/cib/bankWelcome.do?ActionMethod=load&TimeStamp=<set:data name='TimeStamp'/>" scrolling="auto" noresize marginheight="0" marginwidth="0">
      <frame name="bottomFrame" src="/cib/common/bank/bottom.jsp" scrolling="no" noresize marginheight="0" marginwidth="0">
</frameset><noframes></noframes>
<body>
<input id="exitActionFlag" name="exitActionFlag" type="hidden" value="exitByCloseWindow">
</body>
</html>
