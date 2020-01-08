<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<title>Corporation Banking</title>
<link rel="stylesheet" type="text/css" href="/cib/css/main.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js">
</SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/common1.js">
</SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function toTarget(theTarget){
    document.goToTarget.action = theTarget;
    document.goToTarget.submit();
}

function pageLoad(){
}
</script>
</head>
<body onLoad="pageLoad();">
<set:loadrb file="app.cib.resource.sys.login">
    <table width="780" border="0" cellspacing="0" cellpadding="5">
      <tr>
        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td valign="top"><img src="/cib/images/arrow4.gif" width="36" height="36"></td>
              <td>&nbsp;</td>
              <td bgcolor="#666666"><img src="images/shim.gif" width="1" height="1"></td>
              <td>&nbsp;</td>
              <td><TABLE cellSpacing=0 cellPadding=0 width=689 border=0>
                  <TR>
                    <TD class="welcometitle" height=30> <set:data name="corpName" /> </TD>
                  </TR>
                </TABLE>
                  <TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
                    <TR>
                      <TD  class="welcometitle1" height=25> <set:data name="userName" />, <set:data name="greetingTime" rb="app.cib.resource.sys.login" /> , <set:label name="label_Welcome_title"/>  </TD>
                    </TR>
                  </TABLE>
                <TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
                    <TBODY>
                      <TR>
                        <TD  class="welcometitle2"  height=20>&nbsp; <set:label name="label_Prev_Login"/>: <b><set:data name='prevLoginTime' format="datetime" pattern="yyyy-MM-dd HH:mm:ss" /> <span class="welcometitle2"><set:label name="label_Macau_Time"/></span></b> - <set:label name="label_Login_Status"/>: <b><set:data name='loginStatus' rb="app.cib.resource.common.login_status"/></b></TD>
                      </TR>
                    </TBODY>
                </TABLE></td>
            </tr>
        </table></td>
      </tr>
    </table>
	<set:if name="listCount" condition="notequals" value="0">
	<table width="780" border="0" cellspacing="2" cellpadding="0">
        <tr>
          <td align="right"><TABLE cellSpacing=0 cellPadding=0 width="735" bgColor=#FF0000
        border=0>
              <TBODY>
                <TR>
                  <TD height=1><IMG height=1 src="../jsp/account_enquriy/image/spacer.gif"
          width=1></TD>
                </TR>
              </TBODY>
          </TABLE></td>
        </tr>
      </table>
      <table width="780" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td valign="top"><img src="/cib/images/shim.gif" width="36" height="36"></td>
                <td>&nbsp;</td>
                <td >
				<set:if name="roleId" condition="notequals" value="7">
                  <table width="98%" border="0" cellspacing="0" cellpadding="3">
                    <tr>
                      <td height="20" class="content"><b><b><img src="/cib/images/message1.gif" width="20" height="15" align="absmiddle"></b> <set:label name="label_Pending_Case1"/> <set:data name="workCount"/>
                        <a href="#" onClick="toTarget('/cib/approve.do?ActionMethod=list')"><set:label name="label_Pending_Case2"/></a> <set:label name="label_Pending_Case3"/></b></td>
                    </tr>
					<set:list name="workList">
                    <tr>
                      <td class="content">
                      <form action="/cib/approve.do" method="post" name="form1" id="form1">
						<input type = "hidden" name = "ActionMethod" />
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="/cib/images/icon_info.gif" width="12" height="12" border="0" align="absmiddle"> 
                      <a onClick = "postToMainFrame('/cib/approve.do?ActionMethod=approveLoad',{workId:'<set:listdata name='workId'/>'})" href="#"><set:listdata name="txnType" rb="app.cib.resource.common.subtype"/></a> 
                      - (<set:listdata name="procCreateTime" format="datetime"/>)
                      </form>
                      </td>
                    </tr>
					</set:list>
                    <tr>
                      <td class="content" align="right"><a href="#" onClick="toTarget('/cib/approve.do?ActionMethod=list')"><img src="/cib/images/arrow3.gif" width="8" height="9" border="0"> <set:label name="label_View_Complete"/></a></td>
                    </tr>
                  </table>
				  </set:if>
			    </td>
              </tr>
          </table></td>
        </tr>
      </table>
	  </set:if>
	 <!--
      <table width="780" border="0" cellspacing="2" cellpadding="0">
        <tr>
          <td align="right"><TABLE cellSpacing=0 cellPadding=0 width="735" bgColor=#FF0000
        border=0>
              <TBODY>
                <TR>
                  <TD height=1><IMG height=1 src="/cib/images/shim.gif"
          width=1></TD>
                </TR>
              </TBODY>
          </TABLE></td>
        </tr>
      </table>
	  -->
      <table width="780" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td valign="top"><img src="/cib/images/shim.gif" width="36" height="36"></td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
          </table>
<!--		  
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td valign="top"><img src="/cib/images/shim.gif" width="36" height="36"></td>
                <td>&nbsp;</td>
                <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td colspan="3" background="/cib/images/dash_line_top.gif"><img src="/cib/images/shim.gif" width="1" height="1"></td>
                    </tr>
                    <tr>
                      <td background="/cib/images/dash_line_left.gif"><img src="/cib/images/shim.gif" width="1" height="1"></td>
                      <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="140" height="50" align="center"><img src="/cib/images/aLock_Animation2_E.gif" width="120" height="55"></td>
                            <td><set:label name="label_Security_Advices"/></td>
                          </tr>
                      </table></td>
                      <td background="/cib/images/dash_line_left.gif"><img src="/cib/images/shim.gif" width="1" height="1"></td>
                    </tr>
                    <tr>
                      <td colspan="3" background="/cib/images/dash_line_top.gif"><img src="/cib/images/shim.gif" width="1" height="1"></td>
                    </tr>
                </table></td>
              </tr>
            </table>
-->
			</td>
        </tr>
      </table>
      <table width="780" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>
<form name="goToTarget" method="POST" target="mainFrame">
</form>

</set:loadrb>
</body>
</html>
