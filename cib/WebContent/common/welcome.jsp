<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<title>Corporation Banking</title>
<link rel="stylesheet" type="text/css" href="/cib/css/main.css">
<link rel="stylesheet" type="text/css" href="/cib/css/txnPro.css">
<link rel="stylesheet" id='skin' type="text/css" href="/cib/jsprompt/skin/bluebar/ymPrompt.css" />
<script Language="Javascript1.2" Src="/cib/jsprompt/ymPrompt_source.js"></Script>
<SCRIPT language=JavaScript src="/cib/javascript/common.js">
</SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/common1.js?v=20130117"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function securityAlter(){
	var corpType = '<set:data name="corpType"/>';
	var alertMessageFlag = '<set:data name="alertMessageFlag"/>';
	if (alertMessageFlag=='Y' && corpType=="3") {
		ymPrompt.alert(
	  {width:500, height:225, message:'<set:label name="label_Message4" rb="app.cib.resource.sys.login"/>',title:'<set:label name="lable_OtpSecurityAlert_Title" rb="app.cib.resource.sys.login"/>',showMask:true,winPos:[200,100]}
		);
	} else {
	    var alertMessage = '<set:data name="alertMessage"/>';
		if(alertMessage!=''&&alertMessage!=null){
		     ymPrompt.alert(
		  {width:500, height:225, message:'<set:data name="alertMessage"/>',title:'<set:label name="label_SecurityAlert_Title" rb="app.cib.resource.sys.login"/>',showMask:true,winPos:[200,100]}
		      );
		}
	}
}
function pageLoad(){
   securityAlter();
   loadMessage();
   if(language == "en_US"){
      document.getElementById("showEmessageTitle").style.display = "";
      document.getElementById("showCmessageTitle").style.display = "none";
   }else{
   	  document.getElementById("showEmessageTitle").style.display = "none";
      document.getElementById("showCmessageTitle").style.display = "";
   }
}
function loadMessage(){
	var cMessageListDom = document.getElementById("storeCmessageValue");
		var cMessageList = cMessageListDom.childNodes;
		var cMessageCount = parseInt("<set:data name = 'cMessageCount' />");
		
		var cTitleArr = new Array();
		var cContentArr = new Array();
		var countTitle = 0;
		var countContent = 0;
		for(var i = 0; i <  cMessageList.length;i++){
			if(cMessageList[i].name == "CTitle"){
				cTitleArr[countTitle] = cMessageList[i].value;
				countTitle = countTitle+1;
			}else if(cMessageList[i].name == "CContent"){
				cContentArr[countContent] = cMessageList[i].value;
				countContent = countContent+1;
			}
		}
		
		for(var i = 0; i <cMessageCount;i++ ){
			addDiv("showCmessageTitle",cTitleArr[i],cContentArr[i]);
		}
		
		var eMessageListDom = document.getElementById("storeEmessageValue");
		var eMessageList = eMessageListDom.childNodes;
		var eMessageCount = parseInt("<set:data name = 'eMessageCount' />");
		
		var eTitleArr = new Array();
		var eContentArr = new Array();
		countTitle = 0;
		countContent = 0;
		for(var i = 0; i <  eMessageList.length;i++){
			if(eMessageList[i].name == "ETitle"){
				eTitleArr[countTitle] = eMessageList[i].value;
				countTitle = countTitle+1;
			}else if(eMessageList[i].name == "EContent"){
				eContentArr[countContent] = eMessageList[i].value;
				countContent = countContent+1;
			}
		}
		
		for(var i = 0; i <eMessageCount;i++ ){
			addDiv("showEmessageTitle",eTitleArr[i],eContentArr[i]);
		}
}

var count = 0;
function addDiv(targetId,titleValue,contentValue){
	var toAddArea = document.getElementById(targetId);
	var toAddDiv = document.createElement("div");
	toAddDiv.setAttribute("style", "margin-bottom:10px;");
	
	var titleSpan = document.createElement("div");
	titleValue = titleValue.replace(/\r\n/g,"<br>");
	titleSpan.innerHTML = titleValue;
	titleSpan.setAttribute("style", "cursor: pointer;font-weight:bold;display:inline-block;padding-left:5px;width:80%;word-break: break-all;word-wrap: break-word;");
	
	var foldButton = document.createElement("img");
	foldButton.setAttribute("src", "/cib/images/plus.png");
	foldButton.setAttribute("width", "12");
	foldButton.setAttribute("height", "12");
	foldButton.setAttribute("style", "cursor: pointer;");
	foldButton.setAttribute("onclick", "foldMethod(this,'foldArea"+count+"')");
	
	var contentSpan = document.createElement("textarea");
	contentSpan.value = contentValue;
	contentSpan.setAttribute("id", "foldArea" + count);
	contentSpan.setAttribute("class", "myTextarea");
	contentSpan.setAttribute("style", "margin-left:0px;display:none;border:none");
	contentSpan.setAttribute("readOnly", "readOnly");
	
	titleSpan.appendChild(foldButton);
	toAddDiv.appendChild(titleSpan);
	toAddDiv.appendChild(contentSpan);
	
	toAddArea.appendChild(toAddDiv);
	count = count+1;
}

function foldMethod(foldButton,controlArea){
	if(foldButton.src.indexOf("plus.png") != -1){
		foldButton.src = "/cib/images/minus.png";
	}else{
		foldButton.src = "/cib/images/plus.png";
	}
	var controlDom = document.getElementById(controlArea);
	if(controlDom.style.display == "none"){
		controlDom.style.display = "";
		controlDom.style.marginLeft = "2px";
		//auto Height
		 controlDom.style.height = 'auto';
         controlDom.scrollTop = 0; 
         controlDom.style.height = controlDom.scrollHeight + 'px';
	}else{
		controlDom.style.display = "none";
	}
}

</script>
</head>
<body onLoad="pageLoad();">
<div id = "storeCmessageValue">
	<set:list name = "cMessageList">
		<input name = "CTitle" type = "hidden" value = "<set:listdata name = 'CTitle'/>">
		<input name = "CContent" type = "hidden" value = "<set:listdata name = 'CContent'/>">
	</set:list>
</div>
<div id = "storeEmessageValue">
	<set:list name = "eMessageList">
		<input name = "ETitle" type = "hidden" value = "<set:listdata name = 'ETitle'/>">
		<input name = "EContent" type = "hidden" value = "<set:listdata name = 'EContent'/>">
	</set:list>
</div>
<set:loadrb file="app.cib.resource.sys.login">
    <table width="780" border="0" cellspacing="0" cellpadding="5">
      <tr>
        <td><br><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td valign="top"><img src="/cib/images/arrow4.gif" width="36" height="36"></td>
              <td>&nbsp;</td>
              <td bgcolor="#666666"><img src="/cib/images/shim.gif" width="1" height="1"></td>
              <td>&nbsp;</td>
              <td><TABLE cellSpacing=0 cellPadding=0 width=689 border=0>
                  <TR>
                    <TD class="welcometitle" height=30> <set:data name="corpName" /> </TD>
                  </TR>
                </TABLE>
                  <TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
                    <TR>
                      <TD  class="welcometitle1" height=25> <set:data name="title" /> <set:data name="userName" />, <set:data name="greetingTime" rb="app.cib.resource.sys.login"/> , <set:label name="label_Welcome_title"/> </TD>
                    </TR>
                  </TABLE>
                <TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
                    <TBODY>
                      <TR>
                        <TD class="welcometitle2" height=20>&nbsp; <set:label name="label_Prev_Login"/>: <b><set:data name='prevLoginTime' format="datetime" pattern="yyyy-MM-dd HH:mm:ss" /> <set:label name="label_Macau_Time"/></b> - <set:label name="label_Login_Status"/>: <b><set:data name='loginStatus' rb="app.cib.resource.common.login_status"/></b></TD>
                      </TR>
                      <tr>
			        	<td>
			        		<div style = "height: auto;">
							<div id = "showEmessageTitle" style ="display: none;margin-top: 20px;margin-left:20px;">
							</div>
							<div id = "showCmessageTitle" style ="display: none;margin-top: 20px;margin-left:20px;">
							</div>
						</div>
			        	</td>
			        </tr>
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
                  <TD height=1><IMG height=1 src="/cib/images/shim.gif"
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
                <td valign="top" width="36"><img src="/cib/images/shim.gif" width="36" height="36"></td>
                <td width="10">&nbsp;</td>
                <td >
				<set:if name="workCount" condition="notequals" value="0">
                  <table width="98%" border="0" cellspacing="0" cellpadding="3">
                    <tr>
                      <td height="20" class="content"><b><b><img src="/cib/images/message1.gif" width="20" height="15" align="absmiddle"></b> <set:label name="label_Pending_Case1"/> <set:data name="workCount"/>
                        <a href="/cib/approve.do?ActionMethod=list"><set:label name="label_Pending_Case2"/></a> <set:label name="label_Pending_Case3"/></b></td>
                    </tr>
					<set:list name="workList">
                    <tr>
                      <!-- modified by lzg for pwc -->
                      <td class="content">
                      <form action="/cib/approve.do" method="post" name="form1" id="form1">
						<input type = "hidden" name = "ActionMethod" />
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="/cib/images/icon_info.gif" width="12" height="12" border="0" align="absmiddle"> 
                      <a onClick = "postToMainFrame('/cib/approve.do?ActionMethod=approveLoad',{workId:'<set:listdata name='workId'/>'})" href="#"><set:listdata name="txnType" rb="app.cib.resource.common.subtype"/>
                      <!-- modified by lzg end -->
                      </a> - ( 
                      <set:listdata name="procCreateTime" format="datetime"/>)
                      </form>
                      </td>
                    </tr>
					</set:list>
                    <tr>
                      <td class="content" align="right"><a href="/cib/approve.do?ActionMethod=list"><img src="/cib/images/arrow3.gif" width="8" height="9" border="0"> <b><set:label name="label_View_Complete"/></b></a></td>
                    </tr>
                  </table>
				</set:if>
				<set:if name="messageCount" condition="notequals" value="0">
				  <table width="98%" border="0" cellspacing="0" cellpadding="3">
                    <tr>
                      <td height="20" class="content"><b><img src="/cib/images/message.gif" width="20" height="15" align="absmiddle"> <set:label name="label_Message1"/>
                        <set:data name="messageCount"/>
                        <span class="style2"><a href="/cib/message.do?ActionMethod=list"><set:label name="label_Message2"/></a> </span><set:label name="label_Message3"/></b></td>
                    </tr>
                    <set:list name="messageList">
                      <tr>
                        <td height="18" class="content">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="/cib/images/icon_info.gif" width="12" height="12" border="0" align="absmiddle"> <a href="/cib/message.do?ActionMethod=view&muId=<set:listdata name='muId'/>">
                          <set:listdata name="msgTitle"/>
                          </a> - (
                          <set:listdata name="submitTime" format="datetime"/>
                          )</td>
                      </tr>
                    </set:list>
                    <tr>
                      <td class="content" align="right"><a href="/cib/message.do?ActionMethod=list"><img src="/cib/images/arrow3.gif" width="8" height="9" border="0"> <b><set:label name="label_View_Complete"/></b></td>
                    </tr>
                  </table>
				</set:if>			    </td>
              </tr>
          </table></td>
        </tr>
      </table>
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
	</set:if>
      <table width="100%"  border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td valign="top" width="36"><img src="/cib/images/shim.gif" width="36" height="36"></td>
                <td width="3">&nbsp;</td>
                <%--<td>
				<img src="/cib/images/<set:label name='welcome_ad' rb='app.cib.resource.common.advertisement'/>" >
				<iframe id ="promotionFrame" name="promotionFrame" frameborder="0" width="100%" height="300"  src="/cib/common/promotion.html"/>
				<!-- set the height 18pix bigger than the contents in the promotion.html .-->
				</td>--%>
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
          <p>&nbsp;</p></td>
        </tr>
      </table>
      <table width="780
	  " border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>
</set:loadrb>
</body>
</html>
