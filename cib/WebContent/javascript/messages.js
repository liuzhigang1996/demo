/**************************************************************************************
Validation script use by the frame work, please do not modify this section
**************************************************************************************/

//array of serverity gifs
var chkValidationSeverityGifs = new Array;
chkValidationSeverityGifs['info'] = '/cib/images/Info.gif';
chkValidationSeverityGifs['warn'] = '/cib/images/Warn.gif';
chkValidationSeverityGifs['critical'] = '/cib/images/Warn.gif';
var chkValidationSeverity = 'critical';

//div id that will be looked for and html will be placed between its tags.
var chkValidationErrorMessageMarker = 'errMsg';
//error table class
var chkValidationErrorTableClass = 'table_err';
//error table message class
var chkValidationErrorMessageTableClass = 'table_errmsg';
//error td class
var chkValidationErrorTdClass = 'td_errcell';

//message to display at top of error box
var chkValidationErrorMessageInstruction = 'REQUEST REJECTED';

//div id that will be looked for and html will be placed between its tags.
var chkValidationWarnMessageMarker = 'warnMsg';
//warning table class
var chkValidationWarnTableClass = 'table_warn';
//warning table message class
var chkValidationWarnMessageTableClass = 'table_warnmsg';
//warning td class
var chkValidationWarnTdClass = 'td_warncell';

//message to display at top of warning box
var chkValidationWarnMessageInstruction = 'REQUEST REJECTED';

//div id that will be looked for and html will be placed between its tags.
var chkValidationInfoMessageMarker = 'infoMsg';
//information table class
var chkValidationInfoTableClass = 'table_info';
//information table message class
var chkValidationInfoMessageTableClass = 'table_infomsg';
//information td class
var chkValidationInfoTdClass = 'td_infocell';

//message to display at top of information box
var chkValidationInfoMessageInstruction = 'INFORMATION';

//cols for message box
var chkValidationErrorCol = 2;
//max rows for message box
var chkValidationErrorRowMax = 20;
//label style
var chkValidationLabelStyle = 'clsLabel';
//input element style
var chkValidationInputStyle = '';
var reset = false;
var chkValidationSelectElement = true;
//store error message
var needToMoveLayer = false;

function chkDisplayErrorMessageWithMarker(formName) {

	ypos800 = 190;
	ypos1024 = 190;
	var html = '';
	if (errors.length > 0) {
		html = '&nbsp';
		if (!reset) {
			chkValidationSeverity = "critical";
			var severity = chkValidationSeverityGifs[chkValidationSeverity];
			var preHtml =
			"<table class='"+
			chkValidationErrorTableClass+
			"' width='100%'><tr><td valign='center' width='50'><center><img src='"+
			severity+
			"'/></center></td>"+
			"<td><table class='"+
			chkValidationErrorMessageTableClass+
			"'  width='100%'><tr><td colspan='3'>"+
			"<b>"+chkValidationErrorMessageInstruction+"</b>"+
			"</td></tr>";

			var errorHtml = '';
			var rowCount = 0;
			///var columns = csvGetDisplayErrorColumns();
			var columns = chkValidationErrorCol;
			if(errors.length < columns) columns = errors.length;
			var columnWidth = Math.round(1/columns*100);

			for (i=0; i<errors.length; i++) {

				//new row
				if (i%columns == 0) errorHtml += "<tr>";

				var iStr = "" + (i+1) + ". ";

				//cell
				errorHtml +=
				"<td width='"+
				columnWidth+
				"%' style='color:red' class='" +chkValidationErrorTdClass +"'>" + iStr;

				if(errorLabelList[i] !=null && errorLabelList[i] !="" ){
				errorHtml += "<a style = 'color:red' href='javascript:csvSetFocus("+formName+".elements[\""+
				errorNameList[i]+
				"\"], \""+
				errorLayerList[i]+
				"\"," +
				errorArrayIndex[i]+
				")'>"+
				errorLabelList[i]+
				"<a> - "
				}

				errorHtml += errors[i]+	"</td>";

				//close of row
				if (i%columns == columns-1) {
					errorHtml += "</tr>";
					rowCount++;
				}
				//check to see if we are at max.
				if (rowCount==chkValidationErrorRowMax) break;
			}

			var postHtml = "</table></td></tr></table>";
			html = preHtml+errorHtml+postHtml;
		}
	}
	if (needToMoveLayer) {
		if (errors.length > 0) {
			ypos1024 += line1024*Math.ceil(errors.length/3);
			ypos800 += line800*Math.ceil(errors.length/3);
		}
		moveLayer();
	}
	document.all[chkValidationInfoMessageMarker].innerHTML ="";
	document.all[chkValidationInfoMessageMarker].style.display = "none";
	document.all[chkValidationErrorMessageMarker].innerHTML =html;
	document.all[chkValidationErrorMessageMarker].style.display = "";
}

//???¡®¡¤?¡¤¡§:add by liqun 20171124 for multi-language
function chkDisplayErrorMessageWithMarkerByLanguage(formName,language) {
	
	ypos800 = 190;
	ypos1024 = 190;
	var html = '';
	if (errors.length > 0) {
		html = '&nbsp';
		if (!reset) {
			chkValidationSeverity = "critical";
			var severity = chkValidationSeverityGifs[chkValidationSeverity];
			var preHtml =
			"<table class='"+
			chkValidationErrorTableClass+
			"' width='100%'><tr><td valign='center' width='50'><center><img src='"+
			severity+
			"'/></center></td>"+
			"<td><table class='"+
			chkValidationErrorMessageTableClass+
			"'  width='100%'><tr><td colspan='3'>"+
			"<b>"+chkValidationErrorMessageInstructionByLanguage(language)+"</b>"+
			"</td></tr>";

			var errorHtml = '';
			var rowCount = 0;
			///var columns = csvGetDisplayErrorColumns();
			var columns = chkValidationErrorCol;
			if(errors.length < columns) columns = errors.length;
			var columnWidth = Math.round(1/columns*100);

			for (i=0; i<errors.length; i++) {

				//new row
				if (i%columns == 0) errorHtml += "<tr>";

				var iStr = "" + (i+1) + ". ";

				//cell
				errorHtml +=
				"<td width='"+
				columnWidth+
				"%' class='" +chkValidationErrorTdClass +"'>" + iStr;

				if(errorLabelList[i] !=null && errorLabelList[i] !="" ){
				errorHtml += "<a href='javascript:csvSetFocus("+formName+".elements[\""+
				errorNameList[i]+
				"\"], \""+
				errorLayerList[i]+
				"\"," +
				errorArrayIndex[i]+
				")'>"+
				errorLabelList[i]+
				"<a> - "
				}

				errorHtml += errors[i]+	"</td>";

				//close of row
				if (i%columns == columns-1) {
					errorHtml += "</tr>";
					rowCount++;
				}
				//check to see if we are at max.
				if (rowCount==chkValidationErrorRowMax) break;
			}

			var postHtml = "</table></td></tr></table>";
			html = preHtml+errorHtml+postHtml;
		}
	}
	if (needToMoveLayer) {
		if (errors.length > 0) {
			ypos1024 += line1024*Math.ceil(errors.length/3);
			ypos800 += line800*Math.ceil(errors.length/3);
		}
		moveLayer();
	}
	document.all[chkValidationInfoMessageMarker].innerHTML ="";
	document.all[chkValidationInfoMessageMarker].style.display = "none";
	document.all[chkValidationErrorMessageMarker].innerHTML =html;
	document.all[chkValidationErrorMessageMarker].style.display = "";
}
//add by liqun 20171124 for multi-language
function chkValidationErrorMessageInstructionByLanguage(language){
	if(language == "zh_CN"){
		return chkValidationErrorMessageInstruction = '\u8bf7\u6c42\u88ab\u62d2\u7edd';
	}else if(language == "zh_TW" || language == "zh_HK"){
		return chkValidationErrorMessageInstruction = '\u8acb\u6c42\u88ab\u62d2\u7d55';
	}else if(language == "en_US"){
		return chkValidationErrorMessageInstruction = 'REQUEST REJECTED';
	}else if(language == "pt_PT"){
		return chkValidationErrorMessageInstruction = 'Pedido Rejeitado';
	}
}
//add by linrui 20181025 for multi-language
function chkValidationWarnMessageInstructionByLanguage(language){
	if(language == "zh_CN"){
		return chkValidationWarnMessageInstruction = '\u8bf7\u6c42\u88ab\u62d2\u7edd';
	}else if(language == "zh_TW" || language == "zh_HK"){
		return chkValidationWarnMessageInstruction = '\u8acb\u6c42\u88ab\u62d2\u7d55';
	}else if(language == "en_US"){
		return chkValidationWarnMessageInstruction = 'REQUEST REJECTED';
	}else if(language == "pt_PT"){
		return chkValidationWarnMessageInstruction = 'Pedido Rejeitado';
	}
}

function chkDisplayWarnMessageWithMarker(formName) {

	ypos800 = 190;
	ypos1024 = 190;
	var html = '';
	if (warns.length > 0) {
		html = '&nbsp';
		if (!reset) {
			chkValidationSeverity = "warn";
			var severity = chkValidationSeverityGifs[chkValidationSeverity];
			var preHtml =
			"<table class='"+
			chkValidationWarnTableClass+
			"' width='100%'><tr><td valign='center' width='50'><center><img src='"+
			severity+
			"'/></center></td>"+
			"<td><table class='"+
			chkValidationWarnMessageTableClass+
			"'  width='100%'><tr><td colspan='3'>"+
			"<b>"+chkValidationWarnMessageInstruction+"</b>"+
			"</td></tr>";

			var warnHtml = '';
			var rowCount = 0;
			///var columns = csvGetDisplayErrorColumns();
			var columns = chkValidationErrorCol;
			if(warns.length < columns) columns = warns.length;
			var columnWidth = Math.round(1/columns*100);

			for (i=0; i<warns.length; i++) {

				//new row
				if (i%columns == 0) warnHtml += "<tr>";

				var iStr = "" + (i+1) + ". ";

				//cell
				warnHtml +=
				"<td width='"+
				columnWidth+
				"%' class='" +chkValidationWarnTdClass +"'>" + iStr;

				if(warnLabelList[i] !=null && warnLabelList[i] !="" ){
				warnHtml += "<a href='javascript:csvSetFocus("+formName+".elements[\""+
				warnNameList[i]+
				"\"], \""+
				warnLayerList[i]+
				"\"," +
				warnArrayIndex[i]+
				")'>"+
				warnLabelList[i]+
				"<a> - "
				}

				warnHtml += warns[i]+	"</td>";

				//close of row
				if (i%columns == columns-1) {
					warnHtml += "</tr>";
					rowCount++;
				}
				//check to see if we are at max.
				if (rowCount==chkValidationErrorRowMax) break;
			}

			var postHtml = "</table></td></tr></table>";
			html = preHtml+warnHtml+postHtml;
		}
	}
	if (needToMoveLayer) {
		if (errors.length > 0) {
			ypos1024 += line1024*Math.ceil(errors.length/3);
			ypos800 += line800*Math.ceil(errors.length/3);
		}
		moveLayer();
	}
	document.all[chkValidationInfoMessageMarker].innerHTML ="";
	document.all[chkValidationInfoMessageMarker].style.display = "none";
	document.all[chkValidationErrorMessageMarker].innerHTML =html;
	document.all[chkValidationErrorMessageMarker].style.display = "";
}

function chkDisplayWarnMessageWithMarkerByLanguage(formName, language) {
	
	ypos800 = 190;
	ypos1024 = 190;
	var html = '';
	if (warns.length > 0) {
		html = '&nbsp';
		if (!reset) {
			chkValidationSeverity = "warn";
			var severity = chkValidationSeverityGifs[chkValidationSeverity];
			var preHtml =
				"<table class='"+
				chkValidationWarnTableClass+
				"' width='100%'><tr><td valign='center' width='50'><center><img src='"+
				severity+
				"'/></center></td>"+
				"<td><table class='"+
				chkValidationWarnMessageTableClass+
				"'  width='100%'><tr><td colspan='3'>"+
				"<b>"+chkValidationWarnMessageInstructionByLanguage(language)+"</b>"+
				"</td></tr>";
			
			var warnHtml = '';
			var rowCount = 0;
			///var columns = csvGetDisplayErrorColumns();
			var columns = chkValidationErrorCol;
			if(warns.length < columns) columns = warns.length;
			var columnWidth = Math.round(1/columns*100);
			
			for (i=0; i<warns.length; i++) {
				
				//new row
				if (i%columns == 0) warnHtml += "<tr>";
				
				var iStr = "" + (i+1) + ". ";
				
				//cell
				warnHtml +=
					"<td width='"+
					columnWidth+
					"%' class='" +chkValidationWarnTdClass +"'>" + iStr;
				
				if(warnLabelList[i] !=null && warnLabelList[i] !="" ){
					warnHtml += "<a href='javascript:csvSetFocus("+formName+".elements[\""+
					warnNameList[i]+
					"\"], \""+
					warnLayerList[i]+
					"\"," +
					warnArrayIndex[i]+
					")'>"+
					warnLabelList[i]+
					"<a> - "
				}
				
				warnHtml += warns[i]+	"</td>";
				
				//close of row
				if (i%columns == columns-1) {
					warnHtml += "</tr>";
					rowCount++;
				}
				//check to see if we are at max.
				if (rowCount==chkValidationErrorRowMax) break;
			}
			
			var postHtml = "</table></td></tr></table>";
			html = preHtml+warnHtml+postHtml;
		}
	}
	if (needToMoveLayer) {
		if (errors.length > 0) {
			ypos1024 += line1024*Math.ceil(errors.length/3);
			ypos800 += line800*Math.ceil(errors.length/3);
		}
		moveLayer();
	}
	document.all[chkValidationInfoMessageMarker].innerHTML ="";
	document.all[chkValidationInfoMessageMarker].style.display = "none";
	document.all[chkValidationErrorMessageMarker].innerHTML =html;
	document.all[chkValidationErrorMessageMarker].style.display = "";
}


function chkDisplayWarnNoCatchMessageWithMarkerByLanguage(formName, language) {
	
	ypos800 = 190;
	ypos1024 = 190;
	var html = '';
	if (warns.length > 0) {
		html = '&nbsp';
		if (!reset) {
			chkValidationSeverity = "info";
			var severity = chkValidationSeverityGifs[chkValidationSeverity];
			var preHtml =
				"<table class='"+
				chkValidationWarnTableClass+
				"' width='100%'><tr><td valign='center' width='50'><center><img src='"+
				severity+
				"'/></center></td>"+
				"<td><table class='"+
				chkValidationWarnMessageTableClass+
				"'  width='100%'><tr><td colspan='3'>"+
				"<b>"+chkValidationInfoMessageInstructionByLanguage(language)+"</b>"+
				"</td></tr>";
			
			var warnHtml = '';
			var rowCount = 0;
			///var columns = csvGetDisplayErrorColumns();
			var columns = chkValidationErrorCol;
			if(warns.length < columns) columns = warns.length;
			var columnWidth = Math.round(1/columns*100);
			
			for (i=0; i<warns.length; i++) {
				
				//new row
				if (i%columns == 0) warnHtml += "<tr>";
				
				var iStr = "" + (i+1) + ". ";
				
				//cell
				warnHtml +=
					"<td width='"+
					columnWidth+
					"%' class='" +chkValidationWarnTdClass +"'>" + iStr;
				
				if(warnLabelList[i] !=null && warnLabelList[i] !="" ){
					warnHtml += "<a href='javascript:csvSetFocus("+formName+".elements[\""+
					warnNameList[i]+
					"\"], \""+
					warnLayerList[i]+
					"\"," +
					warnArrayIndex[i]+
					")'>"+
					warnLabelList[i]+
					"<a> - "
				}
				
				warnHtml += warns[i]+	"</td>";
				
				//close of row
				if (i%columns == columns-1) {
					warnHtml += "</tr>";
					rowCount++;
				}
				//check to see if we are at max.
				if (rowCount==chkValidationErrorRowMax) break;
			}
			
			var postHtml = "</table></td></tr></table>";
			html = preHtml+warnHtml+postHtml;
		}
	}
	if (needToMoveLayer) {
		if (errors.length > 0) {
			ypos1024 += line1024*Math.ceil(errors.length/3);
			ypos800 += line800*Math.ceil(errors.length/3);
		}
		moveLayer();
	}
	document.all[chkValidationInfoMessageMarker].innerHTML ="";
	document.all[chkValidationInfoMessageMarker].style.display = "none";
	document.all[chkValidationErrorMessageMarker].innerHTML =html;
	document.all[chkValidationErrorMessageMarker].style.display = "";
}


function chkDisplayInfoMessageWithMarker(formName) {

	ypos800 = 190;
	ypos1024 = 190;
	var html = '';
	if (infos.length > 0) {
		html = '&nbsp';
		if (!reset) {
			chkValidationSeverity = "info";
			var severity = chkValidationSeverityGifs[chkValidationSeverity];
			var preHtml =
			"<table class='"+
			chkValidationInfoTableClass+
			"' width='100%'><tr><td valign='center' width='50'><center><img src='"+
			severity+
			"'/></center></td>"+
			"<td><table class='"+
			chkValidationInfoMessageTableClass+
			"' width='100%'><tr><td colspan='3'>"+
			"<b>"+chkValidationInfoMessageInstruction+"</b>"+
			"</td></tr>";

			var infoHtml = '';
			var rowCount = 0;
			///var columns = csvGetDisplayInfoColumns();
			var columns = chkValidationErrorCol;
			if(infos.length < columns) columns = infos.length;
			var columnWidth = Math.round(1/columns*100);

			for (i=0; i<infos.length; i++) {

				//new row
				if (i%columns == 0) infoHtml += "<tr>";

				//cell
				infoHtml +=
				"<td width='"+
				columnWidth+
				"%' class='" +chkValidationInfoTdClass +"'>";

					infoHtml +="<a href='javascript:csvSetFocus("+formName+".elements[\""+
					infoNameList[i]+
					"\"], \""+
					infoLayerList[i]+
					"\"," +
					infoArrayIndex[i]+
					")'>"+
					infoLabelList[i]+
					"<a> - ";

				infoHtml += infos[i]+ "</td>";

				//close of row
				if (i%columns == columns-1) {
					infoHtml += "</tr>";
					rowCount++;
				}
				//check to see if we are at max.
				if (rowCount==chkValidationErrorRowMax) break;
			}

			var postHtml = "</table></td></tr></table>";
			html = preHtml+infoHtml+postHtml;
		}
	}
	if (needToMoveLayer) {
		if (infos.length > 0) {
			ypos1024 += line1024*Math.ceil(infos.length/3);
			ypos800 += line800*Math.ceil(infos.length/3);
		}
		moveLayer();
	}
	document.all[chkValidationErrorMessageMarker].innerHTML ="";
	document.all[chkValidationErrorMessageMarker].style.display = "none";
	document.all[chkValidationInfoMessageMarker].innerHTML =html;
	document.all[chkValidationInfoMessageMarker].style.display = "";
}
//add by liqun 20171124 for muti-language
function chkDisplayInfoMessageWithMarkerByLanguage(formName,language) {

	ypos800 = 190;
	ypos1024 = 190;
	var html = '';
	if (infos.length > 0) {
		html = '&nbsp';
		if (!reset) {
			chkValidationSeverity = "info";
			var severity = chkValidationSeverityGifs[chkValidationSeverity];
			var preHtml =
			"<table class='"+
			chkValidationInfoTableClass+
			"' width='100%'><tr><td valign='center' width='50'><center><img src='"+
			severity+
			"'/></center></td>"+
			"<td><table class='"+
			chkValidationInfoMessageTableClass+
			"' width='100%'><tr><td colspan='3'>"+
			"<b>"+chkValidationInfoMessageInstructionByLanguage(language)+"</b>"+
			"</td></tr>";

			var infoHtml = '';
			var rowCount = 0;
			///var columns = csvGetDisplayInfoColumns();
			var columns = chkValidationErrorCol;
			if(infos.length < columns) columns = infos.length;
			var columnWidth = Math.round(1/columns*100);

			for (i=0; i<infos.length; i++) {

				//new row
				if (i%columns == 0) infoHtml += "<tr>";

				//cell
				infoHtml +=
				"<td width='"+
				columnWidth+
				"%' class='" +chkValidationInfoTdClass +"'>";

					infoHtml +="<a href='javascript:csvSetFocus("+formName+".elements[\""+
					infoNameList[i]+
					"\"], \""+
					infoLayerList[i]+
					"\"," +
					infoArrayIndex[i]+
					")'>"+
					infoLabelList[i]+
					"<a> - ";

				infoHtml += infos[i]+ "</td>";

				//close of row
				if (i%columns == columns-1) {
					infoHtml += "</tr>";
					rowCount++;
				}
				//check to see if we are at max.
				if (rowCount==chkValidationErrorRowMax) break;
			}

			var postHtml = "</table></td></tr></table>";
			html = preHtml+infoHtml+postHtml;
		}
	}
	if (needToMoveLayer) {
		if (infos.length > 0) {
			ypos1024 += line1024*Math.ceil(infos.length/3);
			ypos800 += line800*Math.ceil(infos.length/3);
		}
		moveLayer();
	}
	document.all[chkValidationErrorMessageMarker].innerHTML ="";
	document.all[chkValidationErrorMessageMarker].style.display = "none";
	document.all[chkValidationInfoMessageMarker].innerHTML =html;
	document.all[chkValidationInfoMessageMarker].style.display = "";
}
//add by liqun 20171124 for multi-language
function chkValidationInfoMessageInstructionByLanguage(language){
	if(language == "zh_CN"){
		return chkValidationInfoMessageInstruction = '\u4fe1\u606f';
	}else if(language == "zh_TW" || language == "zh_HK"){
		return chkValidationInfoMessageInstruction = '\u4fe1\u606f';
	}else if(language == "en_US" || language == "pt_PT"){
		return chkValidationInfoMessageInstruction;
	}
}

/*
* set focus in id member
*
*/
function csvSetFocus(el, toLayer, index) {
	//var el = cbeGetElementById(elId);
	if (el) {
		if (toLayer != '-1' && needToMoveLayer == true)
		selectTab(toLayer);
		if (el.type == 'text' ||
		el.type == 'textarea' ||
		el.type == 'file' ||
		el.type == 'select-one' ||
		el.type == 'password' ||
		el.type == 'checkbox' ||
		el.type == 'radio' ||
		el.type == "select-multiple") {
			el.focus();
		} else if ((el.length > 0) && (el[0].type == 'radio' || el[0].type == 'checkbox')) {
			el[index].focus();
		}
		if (chkValidationSelectElement && (el.type == "text" || el.type == "password" || el.type == "textarea"))
		el.select();
	}
}

