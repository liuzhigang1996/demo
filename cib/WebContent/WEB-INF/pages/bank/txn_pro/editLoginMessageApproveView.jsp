<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.bnk.txn_pro">
<link rel="stylesheet" type="text/css" href="/cib/css/txnPro.css">
<SCRIPT language=JavaScript src="/cib/javascript/baidu_jquery.js"></SCRIPT>
<script>
function LoadMessage(){
		if("Y" == "<set:data name = 'clearflag' />"){
			document.getElementById("showEmessageTitle").parentNode.style.width = "90%";
			document.getElementById("showCmessageTitle").parentNode.style.width = "90%";
			return;
		}
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

$(function(){
        $.fn.autoHeight = function(){    
        function autoHeight(elem){
            elem.style.height = 'auto';
            elem.scrollTop = 0; //防抖动
            elem.style.height = elem.scrollHeight + 'px';
        }
        this.each(function(){
            autoHeight(this);
            $(this).on('keyup', function(){
                autoHeight(this);
            });
        });    
    }                
    $('textarea[autoHeight]').autoHeight();    
})
</script>
  <table width="100%" border="0" cellspacing="0" cellpadding="3" >
    <%-- <tr>
      <td colspan="2" class="groupinput"><set:label name="edit_description_info" /></td>
    </tr> --%>
    <tr>
    	<td colspan="2">
    		<div>
				<div >
					<div>
						<span class = "myTitle" ><set:label name = '2nd_menu'/></span>
						<span class = "menu_2nd" ><set:data name = "menu2nd" rb="app.cib.resource.bnk.2ndMenu"/></span>
					</div>
					<div>
						<span class = "myTitle" style = "float: left;"><set:label name = 'descriptionC'/></span>
					</div>
					<div class = "loginDesciption" >
							<div id = "showCmessageTitle">
							</div>
					</div>
				</div>
				<div>
					<div>
						<span class = "myTitle" style = "float: left;"><set:label name = 'descriptionE'/></span>
					</div>
					<div class = "loginDesciption" >
							<div id = "showEmessageTitle">
							</div>
					</div>
				</div>
				<div style = "clear: both;"></div>
			</div>
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
    	</td>
    </tr>
  </table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
