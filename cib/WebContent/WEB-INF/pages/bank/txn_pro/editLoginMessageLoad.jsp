<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<set:loadrb file="app.cib.resource.bnk.txn_pro">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<title>Corporation Banking</title>
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<link rel="stylesheet" type="text/css" href="/cib/css/txnPro.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/baidu_jquery.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
var count = 0;
function pageLoad(){
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
		addDiv("showCmessageTitle","addAreaC",cTitleArr[i],cContentArr[i],"C");
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
		addDiv("showEmessageTitle","addAreaE",eTitleArr[i],eContentArr[i],"E");
	}
}

function addDiv(targetId,targetId2,titleValue,contentValue,lang){
	addContent(targetId2,targetId,lang);
	addCompleteForPageLoad(targetId,"Message"+count,titleValue,contentValue,targetId2,lang);
	
}

function addCompleteForPageLoad(showArea,addArea,titleValue,contentValue,editArea,lang){
	if(lang == "C"){
		var addButton = document.getElementById("addCmessageButton");
	}else{
		var addButton = document.getElementById("addEmessageButton");
	}
	addButton.parentNode.removeChild(addButton);
	
	var showDom = document.getElementById(showArea);
	
	var toAddDiv = document.createElement("div");
	toAddDiv.setAttribute("style", "margin-bottom:10px;")
	toAddDiv.setAttribute("id", "showTitle" + count);
	
	var deleteButton = document.createElement("input");
	deleteButton.setAttribute("type", "button");
	deleteButton.setAttribute("value", "<set:label name = 'deleteLabel' />");
	deleteButton.setAttribute("class", "myButton2");
	deleteButton.setAttribute("style", "background-color: red;");
	deleteButton.setAttribute("onclick", "deleteMessage(this,'"+addArea+"')");
	
	var contentTextArea = "Content"+count;
	var titleTextArea = "Title"+count;
	titleDom = document.getElementById(titleTextArea);
	titleDom.value = titleValue;
	var titleSpan = document.createElement("div");
	var uDom =  document.createElement("u");
	uDom.innerHTML = titleDom.value;
	uDom.setAttribute("onclick", "editMessage('"+addArea+"','"+showArea+"','showTitle"+count+"','"+titleTextArea+"','"+contentTextArea+"')");
	uDom.setAttribute("style", "margin-right:5px;");
	titleSpan.appendChild(uDom);
	titleSpan.setAttribute("style", "cursor: pointer;font-weight:bold;display:inline-block;padding-left:5px;width:80%;word-break: break-all;word-wrap: break-word;");
	
	var foldButton = document.createElement("img");
	foldButton.setAttribute("src", "/cib/images/plus.png");
	foldButton.setAttribute("width", "12");
	foldButton.setAttribute("height", "12");
	foldButton.setAttribute("style", "cursor: pointer;");
	foldButton.setAttribute("onclick", "foldMethod(this,'foldArea"+count+"')");
	
	var contentSpan = document.createElement("textarea");
	var contentDom = document.getElementById(contentTextArea);
	contentDom.value = contentValue;
	contentSpan.innerHTML = contentDom.value;
	contentSpan.setAttribute("id", "foldArea" + count);
	contentSpan.setAttribute("class", "myTextarea");
	contentSpan.setAttribute("class", "myTextarea");
	contentSpan.setAttribute("style", "margin-left:0px;display:none;border:none");
	contentSpan.setAttribute("readOnly", "readOnly");
	
	toAddDiv.appendChild(deleteButton);
	titleSpan.appendChild(foldButton);
	toAddDiv.appendChild(titleSpan);
	toAddDiv.appendChild(contentSpan);
	
	showDom.appendChild(toAddDiv);
	showDom.appendChild(addButton);
	
	showDom.style.display="";
	document.getElementById(addArea).style.display="none";
	count = count + 1;
}

function addContent(addArea,titleArea,lang){
	
	var toAddDiv = document.getElementById(addArea);
	var showTitleArea = document.getElementById(titleArea);
	
	var messageDiv = document.createElement("div");
	messageDiv.setAttribute("id", "Message" + count);
	var titleDiv = document.createElement("div");
	var titleLabel = document.createElement("span");
	titleLabel.innerHTML = "<set:label name = 'titleLabel' />";
	var titleInput = document.createElement("textarea");
	titleInput.setAttribute("id", "Title" + count);
	titleInput.setAttribute("name", "Title"+count);
	titleInput.setAttribute("onkeyup", "autoHeight(this)");
	titleInput.setAttribute("class", "myTextarea");
	titleInput.setAttribute("style", "height:30px;");
	titleDiv.appendChild(titleLabel);
	titleDiv.appendChild(titleInput);
	
	var contentDiv = document.createElement("div");
	var contentLabel = document.createElement("span");
	contentLabel.innerHTML = "<set:label name = 'contentLabel' />";
	var contentInput = document.createElement("textarea");
	contentInput.setAttribute("id", "Content" + count);
	contentInput.setAttribute("name", "Content" + count);
	contentInput.setAttribute("onkeyup", "autoHeight(this)");
	contentInput.setAttribute("class", "myTextarea");
	contentDiv.appendChild(contentLabel);
	contentDiv.appendChild(contentInput);
	
	var buttonDiv = document.createElement("div");
	buttonDiv.setAttribute("style", "width:100%;text-align:center;");
	var okButton = document.createElement("input");
	okButton.setAttribute("type", "button");
	okButton.setAttribute("value", "<set:label name = 'okButton' />");
	okButton.setAttribute("onclick", "addComplete('"+titleArea+"','Message"+count+"','Title"+count+"','Content"+count+"','"+addArea+"','"+lang+"')");
	okButton.setAttribute("class", "myButton2");
	okButton.setAttribute("style", "margin-top:10px;");
	var cancleButton = document.createElement("input");
	cancleButton.setAttribute("type", "button");
	cancleButton.setAttribute("value", "<set:label name = 'cancelButton' />");
	cancleButton.setAttribute("onclick", "cancleAdd('"+titleArea+"','Message"+count+"')");
	cancleButton.setAttribute("class", "myButton2");
	cancleButton.setAttribute("style", "margin-top:10px;margin-left:5px;");
	buttonDiv.appendChild(okButton);
	buttonDiv.appendChild(cancleButton);
	
	messageDiv.appendChild(titleDiv);
	messageDiv.appendChild(contentDiv);
	messageDiv.appendChild(buttonDiv);
	toAddDiv.appendChild(messageDiv);
	
	changeShowArea(addArea,titleArea);
}

function cancleAdd(titleArea,editArea){
	document.getElementById(titleArea).style.display = "";
	var removeDom = document.getElementById(editArea);
	removeDom.parentNode.removeChild(removeDom);
}

function addComplete(showArea,addArea,titleTextArea,contentTextArea,editArea,lang){
	var titleDom = document.getElementById(titleTextArea);
	if(titleDom.value.trim() == ""){
		alert("<set:label name = 'titleTips' />");
		return;
	}
	if(lang == "C"){
		var addButton = document.getElementById("addCmessageButton");
	}else{
		var addButton = document.getElementById("addEmessageButton");
	}
	addButton.parentNode.removeChild(addButton);
	
	var showDom = document.getElementById(showArea);
	
	var toAddDiv = document.createElement("div");
	toAddDiv.setAttribute("style", "margin-bottom:10px;")
	toAddDiv.setAttribute("id", "showTitle" + count);
	
	var deleteButton = document.createElement("input");
	deleteButton.setAttribute("type", "button");
	deleteButton.setAttribute("value", "<set:label name = 'deleteLabel' />");
	deleteButton.setAttribute("class", "myButton2");
	deleteButton.setAttribute("style", "background-color: red;");
	deleteButton.setAttribute("onclick", "deleteMessage(this,'"+addArea+"')");
	
	var titleSpan = document.createElement("div");
	var uDom =  document.createElement("u");
	uDom.innerHTML = titleDom.value;
	uDom.setAttribute("onclick", "editMessage('"+addArea+"','"+showArea+"','showTitle"+count+"','"+titleTextArea+"','"+contentTextArea+"')");
	uDom.setAttribute("style", "margin-right:5px;");
	titleSpan.appendChild(uDom);
	titleSpan.setAttribute("style", "cursor: pointer;font-weight:bold;display:inline-block;padding-left:5px;width:80%;word-break: break-all;word-wrap: break-word;");
	
	var foldButton = document.createElement("img");
	foldButton.setAttribute("src", "/cib/images/plus.png");
	foldButton.setAttribute("width", "12");
	foldButton.setAttribute("height", "12");
	foldButton.setAttribute("style", "cursor: pointer;");
	foldButton.setAttribute("onclick", "foldMethod(this,'foldArea"+count+"')");
	
	var contentSpan = document.createElement("textarea");
	var contentDom = document.getElementById(contentTextArea);
	contentSpan.innerHTML = contentDom.value;
	contentSpan.setAttribute("id", "foldArea" + count);
	contentSpan.setAttribute("class", "myTextarea");
	contentSpan.setAttribute("class", "myTextarea");
	contentSpan.setAttribute("style", "margin-left:0px;display:none;border:none");
	contentSpan.setAttribute("readOnly", "readOnly");
	
	toAddDiv.appendChild(deleteButton);
	titleSpan.appendChild(foldButton);
	toAddDiv.appendChild(titleSpan);
	toAddDiv.appendChild(contentSpan);
	
	showDom.appendChild(toAddDiv);
	showDom.appendChild(addButton);
	
	showDom.style.display="";
	document.getElementById(addArea).style.display="none";
	count = count + 1;
}

function editMessage(editArea,titleArea,showTitle,titleTextarea,contentArea){
	document.getElementById(titleArea).style.display = "none";
	var editDom = document.getElementById(editArea);
	editDom.style.display = "";
	
	document.getElementById("storeTitleTemp").value = document.getElementById(titleTextarea).value;
	document.getElementById("storeContentTemp").value = document.getElementById(contentArea).value;
	editDom.childNodes[2].childNodes[0].setAttribute("onclick", "editComplete('"+editArea+"','"+titleArea+"','"+showTitle+"','"+titleTextarea+"','"+contentArea+"')");
	editDom.childNodes[2].childNodes[1].setAttribute("onclick", "cancleEdit('"+editArea+"','"+titleArea+"','"+titleTextarea+"','"+contentArea+"')");
	changeAutoHeight(editDom.childNodes[0].childNodes[1]);
	changeAutoHeight(editDom.childNodes[1].childNodes[1]);

}

function cancleEdit(editArea,titleArea,titleTextarea,contentArea){
	var titleAreaDom = document.getElementById(titleArea);
	var editAreaDom = document.getElementById(editArea);
	titleAreaDom.style.display = "";
	editAreaDom.style.display = "none";
	document.getElementById(titleTextarea).value = document.getElementById("storeTitleTemp").value;
	document.getElementById(contentArea).value = document.getElementById("storeContentTemp").value;
}

function editComplete(editArea,titleArea,showTitle,titleTextarea,contentArea){
	var titleAreaDom = document.getElementById(titleArea);
	var editAreaDom = document.getElementById(editArea);
	titleAreaDom.style.display = "";
	editAreaDom.style.display = "none";
	
	document.getElementById(showTitle).childNodes[1].childNodes[0].innerHTML = document.getElementById(titleTextarea).value;
	document.getElementById(showTitle).childNodes[2].innerHTML = document.getElementById(contentArea).value;
	
}

function changeAutoHeight(targetDom){
		 targetDom.style.height = 'auto';
         targetDom.scrollTop = 0; 
         targetDom.style.height = targetDom.scrollHeight + 'px';
}

function deleteMessage(deleteButton,deleteId){
	deleteButton.parentNode.parentNode.removeChild(deleteButton.parentNode);
	var deleteDom = document.getElementById(deleteId);
	deleteDom.parentNode.removeChild(deleteDom);
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
		controlDom.style.marginLeft = "42px";
		//auto Height
		 controlDom.style.height = 'auto';
         controlDom.scrollTop = 0; 
         controlDom.style.height = controlDom.scrollHeight + 'px';
	}else{
		controlDom.style.display = "none";
	}
}

function changeShowArea(showArea,hiddenArea){
	document.getElementById(showArea).style.display = "";
	document.getElementById(hiddenArea).style.display = "none";
}

function doSubmit(operator)
{
	document.getElementById("ActionMethod").value = operator;
	var cMessage = document.getElementById("addAreaC");
	var eMessage = document.getElementById("addAreaE");
	
	var cMessageCount = filterMessage(cMessage,"C");
	var eMessageCount = filterMessage(eMessage,"E");
	document.getElementById("cMessageCount").value = cMessageCount;
	document.getElementById("eMessageCount").value = eMessageCount;
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}

function filterMessage(targetMessage,lang){
	var countMessage = 0;
	var messageChilds = targetMessage.childNodes;
	for(var i = 0; i < messageChilds.length; i++){
		if(messageChilds[i].tagName == "DIV"){
			var messageDoms = messageChilds[i].getElementsByTagName("textarea");
			for(var j = 0;j < messageDoms.length;j++){
				if(messageDoms[j].name.indexOf("Title") != -1){
					messageDoms[j].setAttribute("name", lang + "Title" + countMessage);
				}else if(messageDoms[j].name.indexOf("Content") != -1){
					messageDoms[j].setAttribute("name", lang + "Content" + countMessage);
				}
			}
			countMessage = countMessage+1;
		}
	}
	return countMessage;
}

function renameMessage(renameDoms,messageNo,lang){
}

function autoHeight(elem){
	 elem.style.height = 'auto';
     elem.scrollTop = 0; //防抖动
     elem.style.height = elem.scrollHeight + 'px';
}



</script>
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><set:label name="navigationTitleEdit" /></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#" onClick="printPage('pageheader');"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
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
    <td class="title1" nowrap><set:label name="functionTitleEdit" /></td>
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
		  <form action="/cib/transferPrompt.do" method="post" name="form1" id="form1">
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
					<set:messages width="100%" cols="1" align="center"/>
					</td>
				</tr>
			</table>
			<div>
				<div >
					<div>
						<span class = "myTitle" ><set:label name = '2nd_menu'/></span>
						<span class = "menu_2nd" ><set:data name = "menu2nd" rb="app.cib.resource.bnk.2ndMenu"/></span>
					</div>
					<div id ="Cmessage">
						<span class = "myTitle" style = "float: left;"><set:label name = 'descriptionC'/></span>
						<div class = "loginDesciption" >
							<div id = "showCmessageTitle">
								<input id = "addCmessageButton" class = "myButton2" type = "button" value = "<set:label name = 'addButton' />"  onclick = "addContent('addAreaC','showCmessageTitle','C')">
							</div>
							<div class = "loginDesciption" style = "border:none"  id = "addAreaC" style = "display: none;">
							</div>
						</div>
					</div>
				</div>
				<div>
					<div id ="Emessage">
						<span class = "myTitle" style = "float: left;"><set:label name = 'descriptionE'/></span>
						<div class = "loginDesciption" >
							<div id = "showEmessageTitle">
								<input id = "addEmessageButton" class = "myButton2" type = "button" value = "<set:label name = 'addButton' />"  onclick = "addContent('addAreaE','showEmessageTitle','E')">
							</div>
							<div class = "loginDesciption" style = "border:none"  id = "addAreaE" style = "display: none;">
							</div>
						</div>
					</div>
				</div>
				<div style = "clear: both;"></div>
				<div style = "width: 100%;text-align: center;">
					<input class = "myButton"  type = "button" value = "<set:label name = 'editButton2'/>"  onclick="doSubmit('editLoginMessage')"/>
					<input class = "myButton"  type = "button" value = "<set:label name = 'returnButton'/>" onclick="doSubmit('load')">
				</div>
			</div>
			
			<input id="ActionMethod" name="ActionMethod" type="hidden" value="edit">
			<input id="menu2nd" name="menu2nd" type="hidden" value= "<set:data name = 'menu2nd'/>">
			<input id="cMessageCount" name="cMessageCount" type="hidden" >
			<input id="eMessageCount" name="eMessageCount" type="hidden" >
			<input id="storeTitleTemp" name="storeTitleTemp" type="hidden" >
			<input id="storeContentTemp" name="storeContentTemp" type="hidden" >
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
