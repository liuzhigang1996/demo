<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<script type="text/javascript">
window.onbeforeunload = function (e) {
	//mainf.closeWin();
	closeWin();
};
var otpWIndows;

function setOtpWIndows(win)
{
	otpWIndows = win;
}
function closeWin(){
	if(otpWIndows){
		if(!otpWIndows.closed){
			otpWIndows.close();
		}
	}

}
//add by linrui for changeLang 20190805
function changeLanguage(language){
	if(window.parent.frames.length > 1){
		 window.parent.frames['topFrame'].document.getElementById('changeLangCode').value='Y';
		 window.parent.location='/cib/welcome.do?ActionMethod=changePage&PageLanguage=' + language;
	}
}
//end

</script>


<set:loadrb file="app.cib.resource.sys.login" alias="">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
<title><set:label name='documentTitle_main' /></title>
</head>
<frameset rows="135,*,17" cols="*" frameborder="NO" border="0" framespacing="0">
      <frame name="topFrame" src="/cib/common/top.jsp?TimeStamp=<set:data name='TimeStamp'/>" scrolling="NO" noresize marginheight="0" marginwidth="0">
      <frame name="mainFrame" src="/cib/welcome.do?ActionMethod=load&TimeStamp=<set:data name='TimeStamp'/>" scrolling="auto" noresize marginheight="0" marginwidth="0">
      <frame name="bottomFrame" src="/cib/common/bottom.jsp" scrolling="no" noresize marginheight="0" marginwidth="0">
</frameset><noframes></noframes>
</set:loadrb>
</html>
