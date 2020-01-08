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
	$.ajax({

		 type: 'post',
	
		 url: url ,
	
		data: args ,
	
		dataType:'xml',
		
		success: function(ajaxRequest){
			
			
			processMsgToElement(ajaxRequest);

			processError(ajaxRequest);
			
			if ((callBackFunction != null) && (callBackFunction != '')) {
			//alert(11);
				callBackFunction();
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			//alert(111) ;
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
	$.ajax({
		
		type: 'post',
		
		url: url ,
		
		data: args ,
		
		dataType:'xml',
		
		async : async2,
		
		success: function(ajaxRequest){
		
		
		processMsgToElement(ajaxRequest);
		
		processErrorByLanguage(ajaxRequest ,language);
		
		if ((callBackFunction != null) && (callBackFunction != '')) {
			//alert(11);
			callBackFunction();
		}
	},
	error:function(XMLHttpRequest, textStatus, errorThrown){
		//alert(111) ;
	}
	
	});
}


function processMsgToElement(ajaxRequest) {
	
	$(ajaxRequest).find('response').each(function(){
		//alert(msg.errorMsg) ;
		//alert($(this).attr('id'));
		
		var item = $(this);

		//1=element, 2=attr, 3=text, 8=comment, 9=document
		//alert(item.attr('type')) ;
		if(item.attr('type') != 'element') return true;
		if(item.attr('id') == 'errProcessor') return true;
		
		var elementId = item.attr('id');
		//alert(elementId) ;
		var responseType = item.attr("type");
		var myElement = $(elementId);
		//alert("item-" + responseType +":" + item.firstChild.nodeValue);
		if (myElement) {
			myElement.innerHTML = item.text();
		}

	}) ;
}

function processError(ajaxRequest){
	errors = new Array();
	errorNameList = new Array();
	errorLabelList = new Array();
	errorLayerList = new Array();
	errorArrayIndex = new Array();
	errorIndex = 0;
			
	$(ajaxRequest).find('errMsg').each(function(){
		//alert(msg.errorMsg) ;
		errors[errorIndex] = $(this).children("error").text();

	}) ;

	chkDisplayErrorMessageWithMarker('null');
	window.location.hash = 'errMsg';
}

function processErrorByLanguage(ajaxRequest,language){
	errors = new Array();
	errorNameList = new Array();
	errorLabelList = new Array();
	errorLayerList = new Array();
	errorArrayIndex = new Array();
	errorIndex = 0;
	
	$(ajaxRequest).find('errMsg').each(function(){
		//alert(msg.errorMsg) ;
		errors[errorIndex] = $(this).children("error").text();
		
	}) ;
	
	if(errors.length==0){
		return ;
	}
	
//	chkDisplayErrorMessageWithMarker('null');
	chkDisplayErrorMessageWithMarkerByLanguage('null',language);
	window.location.hash = 'errMsg';
}