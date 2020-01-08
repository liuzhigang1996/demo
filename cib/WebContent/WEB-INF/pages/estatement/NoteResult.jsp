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
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></script>
<style type="text/css">

.estatement_a_href:HOVER {
	text-decoration:underline;
}
.estatement_a_href:LINK {
	text-decoration:underline;
}
</style>
<script type="text/javascript">
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	var date_range = '<set:data name="date_range"/>';
  	changeRange(date_range);
  	var stmtType = document.getElementById("stmtType");
  	if(!"<set:data name='stmtType' />" == ""){
			for(var i=0;i<stmtType.options.length;i++){
	    		if(stmtType[i].value=='<set:data name='stmtType' />'){  
	        		stmtType[i].selected=true;   
	    		}  
			}  
	}
	
	var downloadHref = document.getElementsByName("downloadHref");
	for(var i = 0; i < downloadHref.length; i++){
		var dom = downloadHref[i];
		var targetDom = dom.parentNode.parentNode;
		var aHref = targetDom.getElementsByTagName("a");
		
		for(var j = 0; j < aHref.length; j++){
			if(aHref[j].name == "readHref"){
				downloadHref[i].onclick = aHref[j].onclick;
			}
		}
		
	}
	
}

function checkAllRead(flag,compressKey){
	var stmtType = document.getElementById("stmtType").value;
	var ActionMethod = document.getElementById("ActionMethod").value
	if(flag == "all"){
		document.getElementById("buttonReadAll").disabled = true;
		var url = "/cib/jsax?serviceName=ReadCheckService&readCheckAll=Y&stmtType=" + stmtType + "&ActionMethod=" + ActionMethod + "&operator=readAll";
	}else if(flag == "checkBox"){
		document.getElementById("buttonBatchDownload").disabled = true;
		var url = "/cib/jsax?serviceName=ReadCheckService&readCheckAll=Y&stmtType=" + stmtType + "&ActionMethod=" + ActionMethod + "&operator=batchDownload&" + compressKey;
	}
	getMsgToElement(url,'', '',function(){
		document.getElementById("buttonReadAll").disabled = false;
		document.getElementById("buttonBatchDownload").disabled = false;
		var retFlag = document.getElementById("readCheckFlag").value;
		var errorFlag = document.getElementById("errorFlag").value;
		var menuControlId = document.getElementById("menuControlId").value;
		if(errorFlag != "Y"){
			if(retFlag == "Y"){
				parent.frames[1].controlRead(menuControlId,retFlag);
			}
		}
		doSubmit();
	}, false,language) ;
}

function readCheck(internalId,reportDate,ReadCheckId,reportCode){
	var ActionMethod = document.getElementById("ActionMethod").value;
	var url = "/cib/jsax?serviceName=ReadCheckService&internalId=" + internalId + "&reportDate=" + reportDate + "&reportCode=" + reportCode + "&checkType=" + "read" + "&ActionMethod=" + ActionMethod;
	getMsgToElement(url,'', '', function(){
		var retFlag = document.getElementById("readCheckFlag").value;
		var errorFlag = document.getElementById("errorFlag").value;
		if(errorFlag != "Y"){
			var newSpan = document.getElementById(internalId + "_" + reportDate);
			if(newSpan != undefined){
				var dateDiv= document.getElementById(internalId + "_" + reportDate + "_hidden");
				if(dateDiv.childNodes.length == 1){
					document.getElementById(internalId + "_" + reportDate).style.display = "none";
					var spanDiv = document.createElement("span");
					spanDiv.setAttribute("style", "visibility: hidden;");
					spanDiv.innerHTML = "New&nbsp;";
					var dateDivValue = dateDiv.innerHTML;
					//if append spanDiv direct then the visibility span will  after the date which will be ineffective
					dateDiv.innerHTML = "";
					dateDiv.appendChild(spanDiv);
					dateDiv.innerHTML = dateDiv.innerHTML + dateDivValue;
				}
			}
		}
	},false,language) ;
	var readCheckFlag =  document.getElementById("readCheckFlag").value;
	var errorFlag = document.getElementById("errorFlag").value;
	if(errorFlag =="Y"){
		parent.frames[1].controlRead(ReadCheckId,"N");
	}else{
		parent.frames[1].controlRead(ReadCheckId,readCheckFlag);
	}
}



function checkAllBox(){
	var checkBoxs = document.getElementsByName("keybox");
	var flag = document.getElementById("checkAll").checked;
	if(flag){
		for(var i = 0;i < checkBoxs.length; i++){
			checkBoxs[i].checked = true;
		}
	}else{
		for(var i = 0;i < checkBoxs.length; i++){
			checkBoxs[i].checked = false;
		}
	}
}

function quickQuery(value){
	//document.getElementById("quickQueryFlag").value = "Y";
	//document.getElementById("dateFrom").value = "";
	//document.getElementById("dateTo").value = "";
	setDateRange(value, document.getElementById('dateFrom'), document.getElementById('dateTo'));
	doSubmit();
}
function doSubmit()
{
	if(validate_estatementHistory(document.getElementById("form1"))){
		setFormDisabled('form1');
		document.getElementById("form1").submit();
	}
}
function batchDownload()
{
	var compressKey = '';
    var keyCounts = 0;
    var keys = document.getElementsByName("keybox");
    for (i = 0; i < keys.length; i++){
       if (keys[i].checked) {
          keyCounts++;
       }
    }
    if(keyCounts ==0){
    	alert("<set:label name = 'selectTips' />");
    	return;
    }
    var count = 0;
    for (i = 0; i < keys.length; i++) {
          if (keys[i].checked) {
          	count = count +1 ;
             compressKey += 'key=' + keys[i].value;
             if (count != keyCounts) {
             compressKey += '&';
             }
          }
    }
    if (compressKey == '') {
       return false; 
    }
   window.location.href='/cib/DownloadZip?' + compressKey;
  checkAllRead('checkBox',compressKey);
}

function changeRange(range){
	if(range == 'all'){
		document.getElementById("dateFrom").value='';
		document.getElementById("dateTo").value='';
		document.getElementById("dateFrom").disabled = true;
		document.getElementById("dateTo").disabled = true;
		document.getElementById("dateRange").value=0;
		document.getElementById("dateRange").disabled = true;
	}else{
		document.getElementById("dateFrom").disabled = false;
		document.getElementById("dateTo").disabled = false;
		document.getElementById("dateRange").disabled = false;
		document.getElementsByName("date_range")[1].checked = true;
		var dateRange = document.getElementById("dateRange");
		if(!"<set:data name = 'dateRange' />" == ""){
			for ( var i = 0; i < dateRange.options.length; i++) {
				if(dateRange[i].value == "<set:data name = 'dateRange' />"){
					dateRange[i].selected = true;
				}
			}
		}
	}
}
function viewPdf(fileName,internalId,reportDate,ReadCheckId,reportCode){
	readCheck(internalId,reportDate,ReadCheckId,reportCode);
    window.open("/cib/PDF_plug-in/web/viewer.html?file=/cib/PDFDownloadFileDir/"+fileName,"","left=0,top=0,width=" + (screen.availWidth-10) + ",height=" + (screen.availHeight-30) + ",menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars=no,resizable=yes");
}
</script>

</head>
<body  onLoad="pageLoad();">
<div >
	<form action="/cib/estatement.do" method="post" name="form1" id="form1" target="main">
	<set:messages width="100%" cols="1" align="center"/>
	<set:fieldcheck name="history" form="form1" file="estatement_history" />
	<div class="groupseperator_e">&nbsp;</div>
	<div style = "width:100%;">
		<span class ="label_e"><set:label name="statement_type" /></span>
		<span class = "content_e">
			<select id = "stmtType" name = "stmtType">
				<set:if name = "estType" condition="equals" value = "CO">
					<set:rblist file="app.cib.resource.estatement.Co_statement_type">
	                  	<set:rboption/>
	                </set:rblist>
                </set:if>
				<set:if name = "estType" condition="equals" value = "PE">
					<set:rblist file="app.cib.resource.estatement.Pe_statement_type">
	                  	<set:rboption/>
	                </set:rblist>
                </set:if>
				<set:if name = "estType" condition="equals" value = "RE">
					<set:rblist file="app.cib.resource.estatement.Re_statement_type">
	                  	<set:rboption/>
	                </set:rblist>
                </set:if>
				<set:if name = "estType" condition="equals" value = "LO">
					<set:rblist file="app.cib.resource.estatement.Lo_statement_type">
	                  	<set:rboption/>
	                </set:rblist>
                </set:if>
				<set:if name = "estType" condition="equals" value = "CR">
					<set:rblist file="app.cib.resource.estatement.Cr_statement_type">
	                  	<set:rboption/>
	                </set:rblist>
                </set:if>
				<set:if name = "estType" condition="equals" value = "OV">
					<set:rblist file="app.cib.resource.estatement.Ov_statement_type">
	                  	<set:rboption/>
	                </set:rblist>
                </set:if>
				<set:if name = "estType" condition="equals" value = "AS">
					<set:rblist file="app.cib.resource.estatement.As_statement_type">
	                  	<set:rboption/>
	                </set:rblist>
                </set:if>
			</select>
		</span>
	</div>
	<div>
		<span class ="label_e">
			<set:label name="date_range" />
		</span>
		<span class = "content_e">
            <input id="date_range" name="date_range" type="radio" value="all" checked onClick="changeRange('all')">
        </span>
        <span class = "content_e"><set:label name="all" defaultvalue="all"/></span>
        <br>
        <span class ="MyHidden">
        	<set:label name="date_range" />
        </span>
        <span class="content_e">
           <input id="date_range" name="date_range" type="radio" value="range" onClick="changeRange('range')">
        </span>
			<span class="content_e"><set:label name="date_from" defaultvalue="date from"/></span>
           	<input id="dateFrom" name="dateFrom" type="text" value="<set:data name='dateFrom' format='date'/>" size="12" maxlength="10" disabled="disabled"> 
            <img src="/cib/images/datetime.gif" width="16" height="16"  style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" >&nbsp;&nbsp;&nbsp;&nbsp;
            <span class="content_e">
                <set:label name="date_to"/>
            </span>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input id="dateTo" name="dateTo" type="text" value="<set:data name='dateTo' format='date'/>" size="12" maxlength="10" disabled="disabled"> 
            <img src="/cib/images/datetime.gif" width="16" height="16" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" >
			<select id="dateRange" name="dateRange" disabled="disabled" onChange="quickQuery(this);">
				<option value='0'><set:label name="Select_Date_Short_Cut" defaultvalue="----- Select a Date Short-cut ------"/></option>
                <set:rblist file="app.cib.resource.common.date_selection">
                	<set:rboption/>
                </set:rblist>
            </select>
            <span class = "MyButton"><input id="buttonSearch" name="buttonSearch" type="button" value="&nbsp;&nbsp;<set:label name='buttonSearch' />&nbsp;&nbsp;" onClick="doSubmit()"/></span>
	</div>
    <table width="95%" border="0" cellspacing="0" cellpadding="3">
		<tr class="greyline">
			<td class="listheader1"><div align="left"><input type="checkbox"  id = "checkAll"  onclick = "checkAllBox()"><set:label name="statement_name" /></div></td>
            <td class="listheader1"><div align="left"><set:label name="statement_date" /></div></td>
			<td class="listheader1"><div align="left"><set:label name="operation" /></div></td>
		</tr>
		<set:list name="docList"  showPageRows="10" showNoRecord="YES">
	        <tr class="<set:listclass class1='' class2='greyline'/>">
		    	<td class="listcontent1"><div align="left"><input type="checkbox"  name="keybox" value="<set:listdata name='KEY' />"   /><set:listdata name="REPORT_CODE" rb="app.cib.resource.estatement.estatementName"  /></div></td>
	            <td class="listcontent1">
		            <div align="left">
		            	<div style = "position: relative;overflow:hidden;height: 30px;">
		            	<set:listif name = "MARK_READ" condition="notequals" value = "N">
		            		<span  style = "visibility: hidden;">New&nbsp;</span>
		            	</set:listif>
		            	<set:listif name = "MARK_READ" condition="equals" value = "N">
		            	<span id = "<set:listdata name='INTERNAL_ID' />_<set:listdata name='REPORT_DATE' />">
		            	<div class = "subscript2"  >New</div>
		            	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		            	</span>
		            	</set:listif>
		            	<span id = "<set:listdata name='INTERNAL_ID' />_<set:listdata name='REPORT_DATE' />_hidden" style = "position: absolute;top:7px;"><set:listdata name="REPORT_DATE" format="date" pattern="yyyy-MM-dd"/></span>
		            	</div>
		            </div>
	            </td>
				<td class="listcontent1">
					<div align="left">
						<span><a class = "estatement_a_href" style = "cursor: pointer;padding-right: 20px;" href="#" onclick="viewPdf('<set:listdata name='PDF_FILE_NAME' />','<set:listdata name='INTERNAL_ID' />','<set:listdata name='REPORT_DATE' />','<set:listdata name='REPORT_CODE'  rb='app.cib.resource.estatement.estatementReadId' />','<set:listdata name='REPORT_CODE'  />');"><set:label name = "buttonView" /></a></span>
						<span><set:listdata name="FILE_LINK" /><%--<set:label name = "buttonDownload" /> --%></span>
						<span><a name = "readHref" class = "estatement_a_href"  style = "cursor: pointer;padding-right: 20px;" href="#" onclick = "readCheck('<set:listdata name='INTERNAL_ID' />','<set:listdata name='REPORT_DATE' />','<set:listdata name='REPORT_CODE'  rb='app.cib.resource.estatement.estatementReadId' />','<set:listdata name='REPORT_CODE'  />')"><set:label name = "buttonRead" /></a></span>
					</div>
				</td>
		    </tr>
		</set:list>
	</table>
     <div class = "MyButton">
     	<input style = "cursor: pointer;" id="buttonBatchDownload" name="buttonBatchDownload" type="button" onclick = "batchDownload()" value="&nbsp;&nbsp;<set:label name='buttonBatchDownload' />&nbsp;&nbsp;" />
     	<input style = "cursor: pointer;" id="buttonReadAll" name="buttonReadAll" onclick="checkAllRead('all','')" type="button" value="&nbsp;&nbsp;<set:label name='buttonReadAll' />&nbsp;&nbsp;" />
     </div>
     <set:singlesubmit/>
    <input id="ActionMethod" name="ActionMethod" type="hidden" value="<set:data name = 'ActionMethod' />">
    <input id="quickQueryFlag" name="quickQueryFlag" type="hidden" value="N">
    <input id="readCheckFlag" name="readCheckFlag" type="hidden" />
    <input id="errorFlag" name="errorFlag" type="hidden" />
    <!-- the id of menu that should be inhide  -->
    <input id="menuControlId" name="menuControlId" type="hidden" />
    </form>
</div>
</body>
</set:loadrb>
</html>
