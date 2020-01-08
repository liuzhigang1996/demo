Prototype.Browser = (function(res) {
    var ua = navigator.userAgent;

    if(ua.include('Trident')){
        res.Gecko = false;
        res.IE = true;
    }
    return res;
})(Prototype.Browser);


function getElementNodeByTag( myNode,  tagName){
	
	if(myNode.hasChildNodes){
		
		var childNodes = myNode.childNodes;
	
		for ( var i = 0; i < childNodes.length; i++) {
			
			var myItem = childNodes[i];

			if(myItem.nodeType == 1 && myItem.nodeName == tagName) {
			
			 return myItem ;
			}
		}
	}
}

function getElementNodesByTag( myNode,  tagName){
	
	if(myNode.hasChildNodes){
		
		var childNodes = myNode.childNodes;
		var elementNodes = new Array();
		
		for ( var i = 0; i < childNodes.length; i++) {
			
			var myItem = childNodes[i];

			if(myItem.nodeType == 1 && myItem.nodeName == tagName) {
				elementNodes.push(myItem) ;	 
			}
		}
		return elementNodes ;
	}
}


function getMsgToElement(url,id, args, callBackFunction, async) {
	var async2 = true;
	if (async == null) {
		async2 = true;
	} else {
		async2 = async;
	}
	//add by linrui for split url
	var flagIndex = url.indexOf('?');
	var urlLength = url.length;
	args = url.substring(flagIndex+1,urlLength+1); 
	url = url.substring(0,flagIndex+1);
	//end
	var myAjax = new Ajax.Request(url, {
		method : 'post',
		parameters : args,
		//true---异步;false---同步.默认为true
		asynchronous : async2,
		onSuccess : function(ajaxRequest) {
		//alert(ajaxRequest.responseText);
		//alert(ajaxRequest.responseXML);
			//
		processMsgToElement(ajaxRequest);

		processError(ajaxRequest);
		//
		//showError();
		//
		if ((callBackFunction != null) && (callBackFunction != '')) {
			//alert(11);
			callBackFunction();
		}
	}
	});
}

function getMsgToElement(url,id, args, callBackFunction, async, language) {
	var async2 = true;
	if (async == null) {
		async2 = true;
	} else {
		async2 = async;
	}
  //add by linrui for split url
	var flagIndex = url.indexOf('?');
	var urlLength = url.length;
	args = url.substring(flagIndex+1,urlLength+1); 
	url = url.substring(0,flagIndex+1);
	//end
	var myAjax = new Ajax.Request(url, {
		method : 'post',
		parameters : args,
		//true---Òì²½;false---Í¬²½.Ä¬ÈÏÎªtrue
		asynchronous : async2,
		onSuccess : function(ajaxRequest) {
		//alert(ajaxRequest.responseText);
		//alert(ajaxRequest.responseXML);
		//
		processMsgToElement(ajaxRequest);
		
		processErrorByLanguage(ajaxRequest,language);
		//
		//showError();
		//
		if ((callBackFunction != null) && (callBackFunction != '')) {
			//alert(11);
			callBackFunction();
		}
	}
	});
}

//for CR204 add by chen_y
function getMsgAndErrorToElement(url,id, args, callBackFunction, async) {
	var async2 = true;
	if (async == null) {
		async2 = true;
	} else {
		async2 = async;
	}
	//add by linrui for split url
	var flagIndex = url.indexOf('?');
	var urlLength = url.length;
	args = url.substring(flagIndex+1,urlLength+1); 
	url = url.substring(0,flagIndex+1);
	//end
	var myAjax = new Ajax.Request(url, {
		method : 'post',
		parameters : args,
		//true---异步;false---同步.默认为true
		asynchronous : async2,
		onSuccess : function(ajaxRequest) {
		//alert(ajaxRequest.responseText);
		//alert(ajaxRequest.responseXML);
			//
		processMsgAndErrorToElement(ajaxRequest);

		//processError(ajaxRequest);
		//
		//showError();
		//
		if ((callBackFunction != null) && (callBackFunction != '')) {
			//alert(11);
			callBackFunction();
		}
	}
	});
}


//add by linrui for mul-language
function getMsgAndErrorToElementByLanguage(url,id, args, callBackFunction, async, language) {
	var async2 = true;
	if (async == null) {
		async2 = true;
	} else {
		async2 = async;
	}
  //add by linrui for split url
	var flagIndex = url.indexOf('?');
	var urlLength = url.length;
	args = url.substring(flagIndex+1,urlLength+1); 
	url = url.substring(0,flagIndex+1);
	//end
	var myAjax = new Ajax.Request(url, {
		method : 'post',
		parameters : args,
		asynchronous : async2,
		onSuccess : function(ajaxRequest) {
		processMsgAndErrorToElementByLanguage(ajaxRequest,language);
		
		if ((callBackFunction != null) && (callBackFunction != '')) {
			
			callBackFunction();
		}
	}
	});
}

function getMsgToSelect(url, args, callBackFunction, async){
	
	var async2 = true;
	if (async == null) {
		async2 = true;
	} else {
		async2 = async;
	}
	//add by linrui for split url
	var flagIndex = url.indexOf('?');
	var urlLength = url.length;
	args = url.substring(flagIndex+1,urlLength+1); 
	url = url.substring(0,flagIndex+1);
	//end
	
	
	var myAjax = new Ajax.Request(url, {
		method : 'post',
		parameters : args,
		//true---异步;false---同步.默认为true
		asynchronous : async2,
		onSuccess : function(ajaxRequest) {
			//alert(ajaxRequest.responseText);
			//alert(ajaxRequest.responseXML);
			//
		
		processMsgToSelect(ajaxRequest) ;
		
		processError(ajaxRequest);
		//
		//showError();
		//
		if ((callBackFunction != null) && (callBackFunction != '')) {
			//alert(11);
			callBackFunction();
		}
	}
	});

	//doUpdateByObj(url, args, originSelect.id, sUpdater, async2);
}

function getMsgToSelect(url, args, callBackFunction, async,language){
	
	var async2 = true;
	if (async == null) {
		async2 = true;
	} else {
		async2 = async;
	}
	//add by linrui for split url
	var flagIndex = url.indexOf('?');
	var urlLength = url.length;
	args = url.substring(flagIndex+1,urlLength+1); 
	url = url.substring(0,flagIndex+1);
	//end
	
	
	var myAjax = new Ajax.Request(url, {
		method : 'post',
		parameters : args,
		//true---异步;false---同步.默认为true
		asynchronous : async2,
		onSuccess : function(ajaxRequest) {
			//alert(ajaxRequest.responseText);
			//alert(ajaxRequest.responseXML);
			//
		
		processMsgToSelect(ajaxRequest) ;
		
		processErrorByLanguage(ajaxRequest,language);
		//
		//showError();
		//
		if ((callBackFunction != null) && (callBackFunction != '')) {
			//alert(11);
			callBackFunction();
		}
	}
	});

	//doUpdateByObj(url, args, originSelect.id, sUpdater, async2);
}


function processMsgToElement(ajaxRequest) {

	var results = ajaxRequest.responseXML;

	var responseNode = getElementNodeByTag(results, "ajax-response");

	var items = responseNode.childNodes;


	for ( var i = 0; i < items.length; i++) {

		var item = items[i];

		//1=element, 2=attr, 3=text, 8=comment, 9=document
		if(item.nodeType != 1) continue;
		if(item.getAttribute('id') == 'errProcessor') continue;
		
		var elementId = item.getAttribute('id');
		var responseType = item.getAttribute("type");
		var myElement = $(elementId);
		//alert("item-" + responseType +":" + item.firstChild.nodeValue);
		if (myElement) {
			if (responseType == "object")
				//to-do
				myElement.innerHTML = item.firstChild.nodeValue;
			else if (responseType == "element")
				myElement.innerHTML = item.firstChild.nodeValue;
			else if (responseType == "field")
				myElement.value = item.firstChild.nodeValue;
			else
				alert('Unrecognized ajax response type : ' + responseType);
		}
	}
}

function processMsgAndErrorToElement(ajaxRequest) {

	var results = ajaxRequest.responseXML;

	var responseNode = getElementNodeByTag(results, "ajax-response");

	var items = responseNode.childNodes;


	for ( var i = 0; i < items.length; i++) {

		var item = items[i];

		//1=element, 2=attr, 3=text, 8=comment, 9=document
		if(item.nodeType != 1) continue;
		if(item.getAttribute('id') == 'errProcessor') {
			errors = new Array();
			errorNameList = new Array();
			errorLabelList = new Array();
			errorLayerList = new Array();
			errorArrayIndex = new Array();
			errorIndex = 0;
			//chkDisplayErrorMessageWithMarker('null');		
			var errProcessor = item;//	getElementNodeByTag(item, "response");
			//alert(errProcessor);
			if(errProcessor == null){
				return;
			}
			
			try{
				if(errProcessor.hasChildNodes){
					var errList = errProcessor.childNodes;
					var errNode = null;
				
					var errorNode = null;
					var errorNameNode = null;
					var errorLabelNode = null;
					var errorLayerNode = null;
					var errorIndexNode = null;
					
					
					for(i=0; i<errList.length; i++) {
						errNode = errList[i];
						if (errNode.hasChildNodes) {
							if (errNode.nodeType == 1) {
								errorNode = errNode.getElementsByTagName('error')[0];
								errorNameNode = errNode.getElementsByTagName('errorName')[0];
								errorLabelNode = errNode.getElementsByTagName('errorLabel')[0];
								errorLayerNode = errNode.getElementsByTagName('errorLayer')[0];
								errorIndexNode = errNode.getElementsByTagName('errorIndex')[0];
								
								errors[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorNode.text:errorNode.textContent;
								errorNameList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorNameNode.text:errorNameNode.textContent;
								errorLabelList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorLabelNode.text:errorLabelNode.textContent;
								errorLayerList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorLayerNode.text:errorLayerNode.textContent;
								errorArrayIndex[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorIndexNode.text:errorIndexNode.textContent;
								errorIndex++;
							}
						}
					}
				} else {
					errors[errorIndex] = 'System error, please contact BANK for technical support [0]';
					errorNameList[errorIndex] = '';
					errorLabelList[errorIndex] = '';
					errorLayerList[errorIndex] = '-1';
					errorArrayIndex[errorIndex] = '0';
					errorIndex++;
				}
			} catch (e) {
				errorIndex = 0;
				errors[errorIndex] = 'System or Browser compatibility error, ' + e;
				errorNameList[errorIndex] = '';
				errorLabelList[errorIndex] = '';
				errorLayerList[errorIndex] = '-1';
				errorArrayIndex[errorIndex] = '0';
				errorIndex++;
				log2JLog('error', 'err.js.ErrorProcessor[jsax] exception -> ' + e);
			}
	
			chkDisplayErrorMessageWithMarker('null');
			window.location.hash = 'errMsg';
			
			continue;
		};
		
		var elementId = item.getAttribute('id');
		var responseType = item.getAttribute("type");
		var myElement = $(elementId);
		//alert("item-" + responseType +":" + item.firstChild.nodeValue);
		if (myElement) {
			if (responseType == "object")
				//to-do
				myElement.innerHTML = item.firstChild.nodeValue;
			else if (responseType == "element")
				myElement.innerHTML = item.firstChild.nodeValue;
			else if (responseType == "field")
				myElement.value = item.firstChild.nodeValue;
			else
				alert('Unrecognized ajax response type : ' + responseType);
		}
	}
}
//add by linrui 20190515
function processMsgAndErrorToElementByLanguage(ajaxRequest) {
	
	var results = ajaxRequest.responseXML;
	
	var responseNode = getElementNodeByTag(results, "ajax-response");
	
	var items = responseNode.childNodes;
	
	
	for ( var i = 0; i < items.length; i++) {
		
		var item = items[i];
		
		//1=element, 2=attr, 3=text, 8=comment, 9=document
		if(item.nodeType != 1) continue;
		if(item.getAttribute('id') == 'errProcessor') {
			errors = new Array();
			errorNameList = new Array();
			errorLabelList = new Array();
			errorLayerList = new Array();
			errorArrayIndex = new Array();
			errorIndex = 0;
			//chkDisplayErrorMessageWithMarker('null');		
			var errProcessor = item;//	getElementNodeByTag(item, "response");
			//alert(errProcessor);
			if(errProcessor == null){
				return;
			}
			
			try{
				if(errProcessor.hasChildNodes){
					var errList = errProcessor.childNodes;
					var errNode = null;
					
					var errorNode = null;
					var errorNameNode = null;
					var errorLabelNode = null;
					var errorLayerNode = null;
					var errorIndexNode = null;
					
					
					for(i=0; i<errList.length; i++) {
						errNode = errList[i];
						if (errNode.hasChildNodes) {
							if (errNode.nodeType == 1) {
								errorNode = errNode.getElementsByTagName('error')[0];
								errorNameNode = errNode.getElementsByTagName('errorName')[0];
								errorLabelNode = errNode.getElementsByTagName('errorLabel')[0];
								errorLayerNode = errNode.getElementsByTagName('errorLayer')[0];
								errorIndexNode = errNode.getElementsByTagName('errorIndex')[0];
								
								errors[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorNode.text:errorNode.textContent;
								errorNameList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorNameNode.text:errorNameNode.textContent;
								errorLabelList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorLabelNode.text:errorLabelNode.textContent;
								errorLayerList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorLayerNode.text:errorLayerNode.textContent;
								errorArrayIndex[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorIndexNode.text:errorIndexNode.textContent;
								errorIndex++;
							}
						}
					}
				} else {
					errors[errorIndex] = 'System error, please contact BNU for technical support [0]';
					errorNameList[errorIndex] = '';
					errorLabelList[errorIndex] = '';
					errorLayerList[errorIndex] = '-1';
					errorArrayIndex[errorIndex] = '0';
					errorIndex++;
				}
			} catch (e) {
				errorIndex = 0;
				errors[errorIndex] = 'System or Browser compatibility error, ' + e;
				errorNameList[errorIndex] = '';
				errorLabelList[errorIndex] = '';
				errorLayerList[errorIndex] = '-1';
				errorArrayIndex[errorIndex] = '0';
				errorIndex++;
				log2JLog('error', 'err.js.ErrorProcessor[jsax] exception -> ' + e);
			}
			
			chkDisplayErrorMessageWithMarkerByLanguage('null',language);
			window.location.hash = 'errMsg';
			
			continue;
		};
		
		var elementId = item.getAttribute('id');
		var responseType = item.getAttribute("type");
		var myElement = $(elementId);
		//alert("item-" + responseType +":" + item.firstChild.nodeValue);
		if (myElement) {
			if (responseType == "object")
				//to-do
				myElement.innerHTML = item.firstChild.nodeValue;
			else if (responseType == "element")
				myElement.innerHTML = item.firstChild.nodeValue;
			else if (responseType == "field")
				myElement.value = item.firstChild.nodeValue;
			else
				alert('Unrecognized ajax response type : ' + responseType);
		}
	}
}

function processMsgToSelect(ajaxRequest) {
	//var listNodes = ajaxResponse.childNodes;
	var results = ajaxRequest.responseXML;
	//var responseNode = results.getElementsByTagName("ajax-response");
	//var selectNodes = responseNode[0].getElementsByTagName("response");
	//var listNodes = responseNode[0].getElementsByTagName("select");
	
	var responseNode = getElementNodeByTag(results, "ajax-response");
	//alert(responseNode.nodeName) ;

	var selProcessor = getElementNodesByTag(responseNode, "response");
	//alert(selProcessor.nodeName) ;
	
	//1=element, 2=attr, 3=text, 8=comment, 9=document
	if(selProcessor[0].getAttribute('id') == 'errProcessor') return;
	for(var j=0;j<selProcessor.length;j++){
		var type = selProcessor[j].getAttribute('type') ;
		//alert(type + j) ;
		if(type=="element"){
			var noId = selProcessor[j].getAttribute('id') ;
			// mod by chen_y 20151110 for       Special account label and specific bank code label cannot be shown for user input.
			var myElement = document.getElementById(noId);// $(noId);
			//alert(noId) ;
			var returnStr ='';
			if(selProcessor[j].hasChildNodes()){
				//alert(selProcessor[j].childNodes.length) ;
				for(var m=0;m<selProcessor[j].childNodes.length;m++){
					if(selProcessor[j].childNodes[m].hasChildNodes()){
						returnStr = XMLtoString(selProcessor[j].childNodes[m]) ;
						break ;
					}
				}
				
			}
			//alert(returnStr) ;
			myElement.innerHTML = returnStr ;
		} else if(type=="object"){
			var listNodes = selProcessor[j].childNodes;
	
			//alert(listNodes.length) ;
			var listNode = null;
			var listId = null;
			var subList = null;

			for(i=0; i<listNodes.length; i++){
				listNode = listNodes[i];
				if (listNode.nodeType == 1) {
					
					listId = listNode.getAttribute('id');
					subList = document.getElementById(listId);//$(listId);
					subList.options.length = 0;
					var optNodes = listNode.childNodes;
					for(k=0; k<optNodes.length; k++){
						var optNode = optNodes[k];
						if (optNode.nodeType == 1) {
							var optText = optNode.getAttribute('text');
							var optValue = optNode.getAttribute('value');
							var optElement = document.createElement('option');
							optElement.setAttribute('value', optValue);
							optElement.innerHTML = optText;
							subList.appendChild(optElement);
						}
					}
				}
			}
		}
	}
}

function processError(ajaxRequest){
		errors = new Array();
		errorNameList = new Array();
		errorLabelList = new Array();
		errorLayerList = new Array();
		errorArrayIndex = new Array();
		errorIndex = 0;
		//chkDisplayErrorMessageWithMarker('null');
		var results = ajaxRequest.responseXML;
		//var responseNode = results.firstChild;
		var responseNode = getElementNodeByTag(results, "ajax-response");
		
		var errProcessor = 	getElementNodeByTag(responseNode, "response");
		if(errProcessor == null){
			return;
		}
		if(!( errProcessor.getAttribute('id') == 'errProcessor')){
			return;
		}
		
		try{
			if(errProcessor.hasChildNodes){
				var errList = errProcessor.childNodes;
				var errNode = null;
			
				var errorNode = null;
				var errorNameNode = null;
				var errorLabelNode = null;
				var errorLayerNode = null;
				var errorIndexNode = null;
				
				
				for(i=0; i<errList.length; i++) {
					errNode = errList[i];
					if (errNode.hasChildNodes) {
						if (errNode.nodeType == 1) {
							errorNode = errNode.getElementsByTagName('error')[0];
							errorNameNode = errNode.getElementsByTagName('errorName')[0];
							errorLabelNode = errNode.getElementsByTagName('errorLabel')[0];
							errorLayerNode = errNode.getElementsByTagName('errorLayer')[0];
							errorIndexNode = errNode.getElementsByTagName('errorIndex')[0];
							
							errors[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorNode.text:errorNode.textContent;
							errorNameList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorNameNode.text:errorNameNode.textContent;
							errorLabelList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorLabelNode.text:errorLabelNode.textContent;
							errorLayerList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorLayerNode.text:errorLayerNode.textContent;
							errorArrayIndex[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorIndexNode.text:errorIndexNode.textContent;
							errorIndex++;
						}
					}
				}
			} else {
				errors[errorIndex] = 'System error, please contact BANK for technical support [0]';
				errorNameList[errorIndex] = '';
				errorLabelList[errorIndex] = '';
				errorLayerList[errorIndex] = '-1';
				errorArrayIndex[errorIndex] = '0';
				errorIndex++;
			}
		} catch (e) {
			errorIndex = 0;
			errors[errorIndex] = 'System or Browser compatibility error, ' + e;
			errorNameList[errorIndex] = '';
			errorLabelList[errorIndex] = '';
			errorLayerList[errorIndex] = '-1';
			errorArrayIndex[errorIndex] = '0';
			errorIndex++;
			log2JLog('error', 'err.js.ErrorProcessor[jsax] exception -> ' + e);
		}

		chkDisplayErrorMessageWithMarker('null');
		window.location.hash = 'errMsg';
	}
//add by linrui for mul-language 20190219
function processErrorByLanguage(ajaxRequest, language){
	errors = new Array();
	errorNameList = new Array();
	errorLabelList = new Array();
	errorLayerList = new Array();
	errorArrayIndex = new Array();
	errorIndex = 0;
	//chkDisplayErrorMessageWithMarker('null');
	var results = ajaxRequest.responseXML;
	//var responseNode = results.firstChild;
	var responseNode = getElementNodeByTag(results, "ajax-response");
	
	//add by lzg 20190831
	var errProcessor = 	getElementNodesByTag(responseNode, "response");
	var flag = false;
	if(errProcessor == null){
		return;
	}
	for ( var i = 0; i < errProcessor.length; i++) {
		var currentProcessor = errProcessor[i];
		if(currentProcessor.getAttribute('id') == 'errProcessor'){
			flag = true;
			errProcessor = errProcessor[i];
			break;
		}
	}
	if(flag == false){
		return;
	}
	//add by lzg end
	/*var errProcessor = 	getElementNodeByTag(responseNode, "response");
	if(errProcessor == null){
		return;
	}
	if(!( errProcessor.getAttribute('id') == 'errProcessor')){
		return;
	}*/
	
	try{
		if(errProcessor.hasChildNodes){
			var errList = errProcessor.childNodes;
			var errNode = null;
			
			var errorNode = null;
			var errorNameNode = null;
			var errorLabelNode = null;
			var errorLayerNode = null;
			var errorIndexNode = null;
			
			
			for(i=0; i<errList.length; i++) {
				errNode = errList[i];
				if (errNode.hasChildNodes) {
					if (errNode.nodeType == 1) {
						errorNode = errNode.getElementsByTagName('error')[0];
						errorNameNode = errNode.getElementsByTagName('errorName')[0];
						errorLabelNode = errNode.getElementsByTagName('errorLabel')[0];
						errorLayerNode = errNode.getElementsByTagName('errorLayer')[0];
						errorIndexNode = errNode.getElementsByTagName('errorIndex')[0];
						
						errors[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorNode.text:errorNode.textContent;
						errorNameList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorNameNode.text:errorNameNode.textContent;
						errorLabelList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorLabelNode.text:errorLabelNode.textContent;
						errorLayerList[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorLayerNode.text:errorLayerNode.textContent;
						errorArrayIndex[errorIndex] = navigator.userAgent.indexOf("MSIE") != -1?errorIndexNode.text:errorIndexNode.textContent;
						errorIndex++;
					}
				}
			}
		} else {
			errors[errorIndex] = 'System error, please contact BNU for technical support [0]';
			errorNameList[errorIndex] = '';
			errorLabelList[errorIndex] = '';
			errorLayerList[errorIndex] = '-1';
			errorArrayIndex[errorIndex] = '0';
			errorIndex++;
		}
	} catch (e) {
		errorIndex = 0;
		errors[errorIndex] = 'System or Browser compatibility error, ' + e;
		errorNameList[errorIndex] = '';
		errorLabelList[errorIndex] = '';
		errorLayerList[errorIndex] = '-1';
		errorArrayIndex[errorIndex] = '0';
		errorIndex++;
		log2JLog('error', 'err.js.ErrorProcessor[jsax] exception -> ' + e);
	}
	
	chkDisplayErrorMessageWithMarkerByLanguage('null', language);
	window.location.hash = 'errMsg';
}

function XMLtoString(elem){
	
	var serialized;
	
	try {
		// XMLSerializer exists in current Mozilla browsers
		serializer = new XMLSerializer();
		serialized = serializer.serializeToString(elem);
	} 
	catch (e) {
		// Internet Explorer has a different approach to serializing XML
		serialized = elem.xml;
	}
	
	return serialized;
}
