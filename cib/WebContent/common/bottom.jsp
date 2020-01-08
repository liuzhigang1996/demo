<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<set:loadrb file="app.cib.resource.sys.login">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="/cib/css/main.css" rel="stylesheet" type="text/css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js"></SCRIPT>
<title></title>
<script language="JavaScript">
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function popWindow(url){
		var popWin1= window.open(url,"temp1","toolbar=no,location=no,top=100,left=0,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1024,height=600");
		popWin1.focus();
}
</script>
</head>
<body bgcolor="#FFFFFF">
<table width="100%" border="0" cellspacing="0" cellpadding="0" style = "border-collapse:collapse;">
  <%-- <tr>
    <td background="/cib/images/bottom_bg1.gif" >
      <table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="left" width="24">&nbsp;</td>
          <td class="bottom1_c">
		<set:menu type="group" mid="bottom" level="level0">
		<set:menu level="level1" inlevel="level0">
	          <td class="bottom1_c"><a href="#" onClick="popWindow('<set:menuvalue property='path' inlevel='level1' />');" > <set:menuvalue property='label' inlevel='level1' /> </a> </td>
		  <td class="bottom1_c">|</td>
		</set:menu>
		</set:menu>
        </tr>
      </table>
    </td>
  </tr> --%>
<!-- include terms & agreement -->
  <tr>
  	<td height="17" align="left" background="/cib/images/bottom_bg2.gif" class="copyright">&nbsp;&nbsp;<set:label name="Hotline"/></td>
    <%-- <td height="17" align="right" background="/cib/images/bottom_bg2.gif" class="copyright"><set:label name="Copy_Right"/></td> --%>
    <set:if name="PageLanguage" value="en_US" condition="equals">
	  	<td height="17" align="right" background="/cib/images/bottom_bg2.gif" class="copyright"><set:label name="Copy_Right"/></td>
	  </set:if>  
	<set:if name="PageLanguage" value="zh_HK" condition="equals">
	  	<td height="17" align="right" style="padding-right: 10;" background="/cib/images/bottom_bg2.gif" class="copyright"><set:label name="Copy_Right"/></td>
	</set:if>  
    <set:if name="PageLanguage" value="" condition="equals">
	  	<td height="17" align="right" background="/cib/images/bottom_bg2.gif" class="copyright"><set:label name="Copy_Right"/></td>
	  </set:if>
  
  </tr>
</table>
</body>
</set:loadrb>
</html>
