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
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
		var parentDiv = document.getElementById("menu_content");
		if(language == "zh_HK"){
			var childrenDiv = parentDiv.children;
			for(var i = 0; i <childrenDiv.length; i++){
				var menuDiv = childrenDiv[i];
				if(i == 0){
					menuDiv.setAttribute("style", "width:auto;margin-top: 0;");
				}else if(i == childrenDiv.length - 1){
					menuDiv.setAttribute("style", "width:auto;margin-bottom: 0;");
				}else{
					menuDiv.setAttribute("style", "width:auto;");
				}
			}
		}
		parent.bottom.cols = parentDiv.offsetWidth +15  + ",*";
		changeStyle("menu_1");
		var menuReadFlag = document.getElementById("menuReadFlag").value;
		menuReadFlag = menuReadFlag.split(",");
		for(var i = 0; i < menuReadFlag.length; i++){
			var flag = menuReadFlag[i].split("_")[1];
			var controlId = menuReadFlag[i].split("_")[0];
			if(flag == "true"){
				document.getElementById(controlId).style.display = "none";
			}else{
				document.getElementById(controlId).style.display = "";
			}
		}
}
var preTitleId = "";
function changeStyle(id){
	var title = document.getElementById(id);
	if(preTitleId != ""){
		var preTitle = document.getElementById(preTitleId);
		preTitle.style.backgroundColor = "white";
		preTitle.getElementsByTagName("a")[0].style.color = "#2680EB";
	}
	title.style.backgroundColor = "#2680EB";
	title.getElementsByTagName("a")[0].style.color = "white";
	preTitleId = id;
}
	  	
function controlRead(id,flag){
	if(flag == "Y"){
		document.getElementById(id).style.display = "none";
	}else{
		document.getElementById(id).style.display = "";
	}
}
</script>

</head>

<body onLoad="pageLoad();">
	<div style = "height: 100%;">
	<div>
	  	<div id = "menu_content" style = "float:left;border-right: 3px solid #01A2E8;padding-right: 20px;">
		  		<div class = "menu" id = "menu_1" style = "margin-top: 0;">
		  			<div class = "menu_child">
			  			<a  href = "/cib/estatement.do?ActionMethod=showCoNote" target="main" style = "text-decoration: none;color: #2680EB;" onclick = "changeStyle('menu_1')">
			  				<set:if name="language" condition="equals" value="zh_HK" >
			  					&emsp;&emsp;&emsp;&emsp;
			  				</set:if>
			  				<set:label name = 'CoNote' />
			  				<set:if name="language" condition="equals" value="zh_HK" >
			  					&emsp;&emsp;&emsp;&emsp;
			  				</set:if>
			  			</a>
			  			<div class = "subscript" id = "CoNote"  style = "display:none;">New</div>
		  			</div>
		  		</div>
				<div class = "menu" id = "menu_2">
					<div class = "menu_child">
						<a  href = "/cib/estatement.do?ActionMethod=showPeNote" target="main" style = "text-decoration: none;color: #2680EB;" onclick = "changeStyle('menu_2')">
							<set:label name = 'PeNote' />
						</a>
			  			<div class = "subscript" id = "PeNote" style = "display:none;">New</div>
					</div>
				</div>
				<div class = "menu" id = "menu_3">
					<div class = "menu_child">
						<a href = "/cib/estatement.do?ActionMethod=showReNote" target="main" style = "text-decoration: none;color: #2680EB;" onclick = "changeStyle('menu_3')">
							<set:label name = 'ReNote' />
						</a>
			  			<div class = "subscript" id = "ReNote" style = "display:none;">New</div>
					</div>
				</div>
				<div class = "menu" id = "menu_4">
					<div class = "menu_child">
						<a href = "/cib/estatement.do?ActionMethod=showLoNote" target="main" style = "text-decoration: none;color: #2680EB;" onclick = "changeStyle('menu_4')">
							<set:label name = 'LoNote' />
						</a>
			  			<div class = "subscript" id = "LoNote" style = "display:none;">New</div>
					</div>
				</div>
				<div class = "menu" id = "menu_5">
					<div class = "menu_child">
						<a href = "/cib/estatement.do?ActionMethod=showCrNote" target="main" style = "text-decoration: none;color: #2680EB;" onclick = "changeStyle('menu_5')">
							<set:label name = 'CrNote' />
						</a>
			  			<div class = "subscript" id = "CrNote" style = "display:none;">New</div>
					</div>
				</div>
				<div class = "menu" id = "menu_6">
					<div class = "menu_child">
						<a href = "/cib/estatement.do?ActionMethod=showOvNote" target="main" style = "text-decoration: none;color: #2680EB;" onclick = "changeStyle('menu_6')">
							<set:label name = 'OvNote' />
						</a>
			  			<div class = "subscript" id = "OvNote" style = "display:none;">New</div>
					</div>
				</div>
				<div class = "menu" id = "menu_7" style = "margin-bottom:0;">
					<div class = "menu_child">
						<a href = "/cib/estatement.do?ActionMethod=showAsNote" target="main" style = "text-decoration: none;color: #2680EB;" onclick = "changeStyle('menu_7')">
							<set:label name = 'AsNote' />
						</a>
			  			<div class = "subscript" id = "AsNote" style = "display:none;">New</div>
					</div>
				</div>
	  	</div>
	  	<div id = "menu_border" class = "menu_border_style" ></div>
	  	</div>
  </div>
  <input name = "menuReadFlag" id = "menuReadFlag"  type = "hidden" value = "<set:data name = 'menuReadFlag' />"/>
</body>
</set:loadrb>
</html>
