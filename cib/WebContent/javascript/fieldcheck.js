
/**************************************************************************************
Validation script use by the frame work, please do not modify this section
**************************************************************************************/

var isModified = false;
var validateAll = new Array("required", "maxlength", "minlength", "type", "depends", "rejects");
var validateAllExDepends = new Array("required", "maxlength", "minlength", "type");
var validatePartial = new Array("maxlength", "minlength", "type");
var requiredArray = new Array("required");
var maxlengthArray = new Array("maxlength");
var minlengthArray = new Array("minlength");
var typeArray = new Array("type");
var dependsArray = new Array("depends");
var rejectsArray = new Array("rejects");
var loupper = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
var lolower = 'abcdefghijklmnopqrstuvwxyz';
var lointeger = '0123456789';

var lospset0 = ' !@#$%&*()-=_+|;:,./<>"' + "'" + '?';
var lospset1 = ' &()-,./' + "'" + '"';
var lospset2 = ' !@#$%&*()-=_+|;:,./<>"' + "'" + '?';
var lospset3 = ' @#$&*()-=_+,./<>' + "'" + '?';
var lospset4 = '!@#$%&*()-=_+|;:,./<>"' + "'" + '?';
var lospset7 = ' .(+&$*)-/,' + "'" + '=';
var lospset8 = ' !@#$%&*()-=_+;:,./<>"' + "'" + '?';
var lospset9 = ' .(+)-/,' + "'" + '=';

var lospset10 = '{}[]\`~^';
var lospset11 = '.(+)-/,'+"':?";

//Original Expression = /^\d+$/;
var filterInt = /^[+-]?\d+$/;

function uncheckRadio(theField) {
	theField.checked = ! theField.checked;
	return true;
}
function trim(s) {
	return s.replace(/^\s*/, "").replace(/\s*$/, "");
}

function stripChar(str, filter) {
	if (filter.test(str)) {
		while (filter.test(str)) {
			str = str.replace(filter, '');
		}
	}
	return str;
}
function isValidChar(str, validstr) {
	for (var i = 0; i < str.length; i++) {
		if (validstr.indexOf(str.substr(i, 1)) == -1)
		return false;
	}
	return true;
}

function getFieldValue(field) {
	var result = new Array();
	var i = 0;
	if (field.type == 'text'
	|| field.type == 'textarea'
	|| field.type == 'file'
	|| field.type == 'hidden'
	|| field.type == 'select-one'
	|| field.type == 'password') {

		var value = '';
		// get field's value
		if (field.type == "select-one") {
			var si = field.selectedIndex;
			if (si >= 0) {
				value = field.options[si].value;
			}
		} else {
			value = field.value;
		}

		result[0] = trim(value);
	} else if (field.type == "select-multiple") {
		var numOptions = field.options.length;

		for (loop = numOptions - 1; loop >= 0; loop--) {
			if (field.options[loop].selected) {
				value = field.options[loop].value;
				result[i++] = value;
			}
		}
	} else if (
	(field.length > 0)
	&& (field[0].type == 'radio'|| field[0].type == 'checkbox')) {
		for (loop = 0; loop < field.length; loop++) {
			if (field[loop].checked) {
				value = field[loop].value;
				result[i++] = value;
			}
		}
	} else if (field.type == 'checkbox') {
		if (field.checked) {
			value = field.value;
			if (trim(value).length > 0) {
				result[0] = trim(value);
			}
		}
	} else if ( field.type == 'radio' ) {
		if (field.checked) {
			value = field.value;
			if (trim(value).length > 0) {
				result[0] = trim(value);
			}
		}
	}
	return result[0];
}
function getNumericValue(field) {
	filter = /,/;
	numValue = stripChar(field, filter);
	filter = /\//;
	numValue = stripChar(numValue, filter);
	filter = /:/;
	numValue = stripChar(numValue, filter);
	//return numValue;
	if ( isNaN(parseFloat(numValue)) )  {
		return numValue;
	} else {
		return parseFloat(numValue);
	}
}


function stripChar2(str, filter) {
	if (filter.test(str)) {
		while (filter.test(str)) {
			str = str.replace(filter, '');
		}
	}
	return str;
}

function isDividedBy(field1, field2) {
	filter = /,/;
	field1 = field1.toString();
	field2 = field2.toString();
	v1 = stripChar2(field1, filter);
	v2 = stripChar2(field2, filter);
	if (isNaN(v1 % v2)) {
		return false;
	} else if ((v1 % v2) == 0) {
		return true;
	} else {
		return false;
	}
}

function contains(source, target) {
	var isContain = true;
	if (source.length == 0)
	return true;
	if (target.length == 0)
	return false;

	for (var i = 0; i < source.length; i++) {
		for (var j = 0; j < target.length; ++j) {
			if (trim(source[i]) == target[j]) {
				break;
			}
		}
		if (j == target.length)
		return false;
	}
	return true;
}

function changeAction(form, action) {
	var newAction = contextPath + action;
	form.action = newAction;
}

function isDisable(field) {
	if(field==null){
		return true;
	}
	if (field.type == 'text' || field.type == 'password' || field.type == 'textarea') {
		return (field.readOnly || field.disabled);
	} else if ( (field.length > 0) && (field[0].type == 'radio'|| field[0].type == 'checkbox')) {
		return (field[0].disabled);
	} else {
		return (field.disabled);
	}
}

/*和scriptlib.js衝突
function isChecked(field) {
	if (
	(field.length > 0)
	&& (field[0].type == 'radio'|| field[0].type == 'checkbox')) {
		for (loop = 0; loop < field.length; loop++) {
			if (field[loop].checked) {
				return true;
			}
		}
	} else if (field.type == 'radio'|| field.type == 'checkbox') {
		if (field.checked) {
			return true;
		}
	}
	return false;
}
*/

function isCharInString (c, str) {
	for (i = 0; i < str.length; i++) {
		if (str.charAt(i) == c)
		return true;
	}
	return false
}

function cancelRadio(field) {
	field.checked = ! field.checked;
	return true;
}

function isValidCharByByteType(str, validstr, byteType, isAllChinese) {
	if ((byteType == "double") || (byteType == "double-only")) {
		for (var i=0; i<str.length; i++) {
			if (str.charCodeAt(i) <= 255) {		// non-chinese chars.
				if (isAllChinese)
				return false;
				if (!isValidChar(str.substr(i,1), validstr)) {
					return false;
				}
			}
		}
	} else {
		for (var i=0; i<str.length; i++) {
			if (!isValidChar(str.substr(i,1), validstr)) {
				return false;
			}
		}
	}
	return true;
}

function enableAll(form) {
	for (i=0;i < form.elements.length;i++) {
		if (form.elements[i].type == "button" || form.elements[i].type == "submit")
		form.elements[i].disabled = true;
	else
		form.elements[i].disabled = false;
	}

	documentObj = eval('document');

	for (i=0;i < documentObj.links.length;i++) {
		documentObj.links[i].href = "#";
		documentObj.links[i].onclick = 'false';
	}
}

function isAccount(str) {
	var filter = /^[0-9]{3}-[0-9]{3}-[0-9]{1}-[0-9]{6}-[0-9]{1}$/;
	if (filter.test(str))
	return true;


	filter = /^[0-9]{3}-[0-9]{3}-[0-9]{2}-[0-9]{5}-[0-9]{1}$/;
	if (filter.test(str))
	return true;

	filter = /^[0-9]{14}$/;
	if (filter.test(str))
	return true;

	return false;
}

function validateAccount(field) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}
	var value = field.value;

	trim(value);
	if ( value.length > 0 ) {
		if ( !isAccount(value) ) {
			return false;
		}
		filter = /-/;
		value = stripChar(value, filter);
		var weight = new Array(14);
		var base = 10;
		var sum = 0;
		weight[0] = 3;
		weight[1] = 1;
		weight[2] = 8;
		weight[3] = 9;
		weight[4] = 6;
		weight[5] = 7;
		weight[6] = 3;
		weight[7] = 7;
		weight[8] = 1;
		weight[9] = 9;
		weight[10] = 7;
		weight[11] = 3;
		weight[12] = 7;
		weight[13] = 1;

		for (var i=0; i<14; i++)
		sum += ((value.substr(i,1)) * weight[i]);
		if ((sum % base) == 0)
		return true;
	else
		return false;
	}
	return true;
}

function validateRequired(field) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}
	var isValid = true;
	var fields = new Array();

	if (field.type == 'text' ||
	field.type == 'textarea' ||
	field.type == 'file' ||
	field.type == 'select-one' ||
	field.type == 'hidden'   ||
	field.type == 'password') {

		var value = '';
		// get field's value
		if (field.type == "select-one") {
			var si = field.selectedIndex;
			if (si >= 0) {
				value = field.options[si].value;
			}
		} else {
			value = field.value;
		}

		if (trim(value).length == 0) {
			isValid = false;
		}
	} else if (field.type == 'checkbox') {
		return field.checked;
	} else if (field.type == "select-multiple") {
		var numOptions = field.options.length;
		lastSelected=-1;
		for(loop=numOptions-1;loop>=0;loop--) {
			if(field.options[loop].selected) {
				lastSelected = loop;
				value = field.options[loop].value;
				break;
			}
		}
		if(lastSelected < 0 || trim(value).length == 0) {
			isValid=false;
		}
	} else if ((field.length > 0) && (field[0].type == 'radio' || field[0].type == 'checkbox')) {
		isChecked=-1;
		for (loop=0;loop < field.length;loop++) {
			if (field[loop].checked) {
				isChecked=loop;
				break; // only one needs to be checked
			}
		}
		if (isChecked < 0) {
			isValid=false;
		}
	} else if ( field.type =='radio' ) {
		return field.checked;
	}
	return isValid;
}

function validateMinLength(field, min) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}

	if (field.type == 'text' || field.type == 'password' ||
	field.type == 'textarea') {
		if ((trim(field.value).length > 0) && (field.value.length < min)) {
			return false;
		} else {
			return true;
		}
	}
	return true;
}

function validateFixLength(field, min) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}

	if (field.type == 'text' || field.type == 'password' ||
	field.type == 'textarea') {
		if (field.value.length != min) {
			return false;
		} else {
			return true;
		}
	}
	return true;
}

function validateMaxLength(field, max, byteType) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}
	if (field.type == 'text' || field.type == 'password' ||
	field.type == 'textarea') {
		if (field.value.length > max) {
			return false;
		} else if ((byteType == "double") || (byteType == "double-only")) {
			var hostLength = 0;
			for(var loi  = 0;  loi < field.value.length ; loi ++) {
				if (field.value.charCodeAt(loi) > 255) {	// chinese chars.
					hostLength += 3;
					loi ++;
					if(loi == field.value.length)
					hostLength += 1;

					while(loi< field.value.length) {
						if(field.value.charCodeAt(loi) > 255)
						hostLength += 2;
					else {
						hostLength += 2;
						break;
					}

					loi ++;
					if (loi == field.value.length)
					hostLength += 1;
				}
			}
		else
			hostLength += 1;

		}
		if (hostLength > max) {
			return false;
		}
	}
}
return true;
}

function validateDate(field, datePattern) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}

	var filter = /\//;
	if ( datePattern.length == 0 ) {
		if( field.value.length == 8){
			datePattern = 'YYYYMMDD';
		}else{
			datePattern = 'DD/MM/YYYY';
		}
	} else {
		datePattern = stripChar(datePattern, filter);
	}

	var value = field.value;
	//value = stripChar(value, filter);
	if ((field.type == 'text' ||
	field.type == 'textarea') &&
	(value.length > 0) &&
	(datePattern.length > 0)) {
		var MONTH = "MM";
		var DAY = "DD";
		var YEAR = "YYYY";
		var orderMonth = datePattern.indexOf(MONTH);
		var orderDay = datePattern.indexOf(DAY);
		var orderYear = datePattern.indexOf(YEAR);
		if (orderDay == -1) {
			var iDelim1 = orderYear + YEAR.length;
			var iDelim2 = orderMonth + MONTH.length;
			var delim1 = datePattern.substring(iDelim1, iDelim1 + 1);
			var delim2 = datePattern.substring(iDelim2, iDelim2 + 1);
			dateRegexp = new RegExp("^(\\d{4})(\\d{2})$");
			var matched = dateRegexp.exec(value);
			if (matched != null) {
				if (!isValidDate(-1, matched[2], matched[1])) {
					return false;
				}
			} else {
				return false;
			}
		} else if ((orderDay < orderYear && orderDay > orderMonth)) {
			var iDelim1 = orderMonth + MONTH.length;
			var iDelim2 = orderDay + DAY.length;
			var delim1 = datePattern.substring(iDelim1, iDelim1 + 1);
			var delim2 = datePattern.substring(iDelim2, iDelim2 + 1);
			if (iDelim1 == orderDay && iDelim2 == orderYear) {
				dateRegexp = new RegExp("^(\\d{2})(\\d{2})(\\d{4})$");
			} else if (iDelim1 == orderDay) {
				dateRegexp = new RegExp("^(\\d{2})(\\d{2})[" + delim2 + "](\\d{4})$");
			} else if (iDelim2 == orderYear) {
				dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})(\\d{4})$");
			} else {
				dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})[" + delim2 + "](\\d{4})$");
			}
			var matched = dateRegexp.exec(value);
			if (matched != null) {
				if (!isValidDate(matched[2], matched[1], matched[3])) {
					return false;
				}
			} else {
				return false;
			}
		} else if ((orderMonth < orderYear && orderMonth > orderDay)) {
			var iDelim1 = orderDay + DAY.length;
			var iDelim2 = orderMonth + MONTH.length;
			var delim1 = datePattern.substring(iDelim1, iDelim1 + 1);
			var delim2 = datePattern.substring(iDelim2, iDelim2 + 1);
			if (iDelim1 == orderMonth && iDelim2 == orderYear) {
				dateRegexp = new RegExp("^(\\d{2})(\\d{2})(\\d{4})$");
			} else if (iDelim1 == orderMonth) {
				dateRegexp = new RegExp("^(\\d{2})(\\d{2})[" + delim2 + "](\\d{4})$");
			} else if (iDelim2 == orderYear) {
				dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})(\\d{4})$");
			} else {
				dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})[" + delim2 + "](\\d{4})$");
			}
			var matched = dateRegexp.exec(value);
			if (matched != null) {
				if (!isValidDate(matched[1], matched[2], matched[3])) {
					return false;
				}
			} else {
				return false;
			}
		} else if ((orderMonth > orderYear && orderMonth < orderDay)) {
			var iDelim1 = orderYear + YEAR.length;
			var iDelim2 = orderMonth + MONTH.length;
			var delim1 = datePattern.substring(iDelim1, iDelim1 + 1);
			var delim2 = datePattern.substring(iDelim2, iDelim2 + 1);
			if (iDelim1 == orderMonth && iDelim2 == orderDay) {
				dateRegexp = new RegExp("^(\\d{4})(\\d{2})(\\d{2})$");
			} else if (iDelim1 == orderMonth) {
				dateRegexp = new RegExp("^(\\d{4})(\\d{2})[" + delim2 + "](\\d{2})$");
			} else if (iDelim2 == orderDay) {
				dateRegexp = new RegExp("^(\\d{4})[" + delim1 + "](\\d{2})(\\d{2})$");
			} else {
				dateRegexp = new RegExp("^(\\d{4})[" + delim1 + "](\\d{2})[" + delim2 + "](\\d{2})$");
			}
			var matched = dateRegexp.exec(value);
			if (matched != null) {
				if (!isValidDate(matched[3], matched[2], matched[1])) {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	return true;
}

function isValidDate(day, month, year) {
	if ( month != -1 ) {
		if (month < 1 || month > 12) {
			return false;
		}
	}
	if ( day != -1 ) {
		if (day < 1 || day > 31) {
			return false;
		}
	}
	if ((month == 4 || month == 6 || month == 9 || month == 11) &&
	(day == 31)) {
		return false;
	}
	if (month == 2) {
		var leap = (year % 4 == 0 &&
		(year % 100 != 0 || year % 400 == 0));
		if (day>29 || (day == 29 && !leap)) {
			return false;
		}
	}
	return true;
}

function getDateValue(value1) {

		if( value1.length == 8){
			datePattern = 'YYYYMMDD';
		}else{
			datePattern = 'DD/MM/YYYY';
		}

		var MONTH = "MM";
		var DAY = "DD";
		var YEAR = "YYYY";
		var orderMonth = datePattern.indexOf(MONTH);
		var orderDay = datePattern.indexOf(DAY);
		var orderYear = datePattern.indexOf(YEAR);
		if ((orderDay < orderYear && orderDay > orderMonth)) {
			var iDelim1 = orderMonth + MONTH.length;
			var iDelim2 = orderDay + DAY.length;
			var delim1 = datePattern.substring(iDelim1, iDelim1 + 1);
			var delim2 = datePattern.substring(iDelim2, iDelim2 + 1);
			if (iDelim1 == orderDay && iDelim2 == orderYear) {
				dateRegexp = new RegExp("^(\\d{2})(\\d{2})(\\d{4})$");
			} else if (iDelim1 == orderDay) {
				dateRegexp = new RegExp("^(\\d{2})(\\d{2})[" + delim2 + "](\\d{4})$");
			} else if (iDelim2 == orderYear) {
				dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})(\\d{4})$");
			} else {
				dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})[" + delim2 + "](\\d{4})$");
			}
			var matched = dateRegexp.exec(value1);
			if (matched != null) {
			return ("" + matched[3]+ matched[1]+ matched[2]);
			}
		} else if ((orderMonth < orderYear && orderMonth > orderDay)) {
			var iDelim1 = orderDay + DAY.length;
			var iDelim2 = orderMonth + MONTH.length;
			var delim1 = datePattern.substring(iDelim1, iDelim1 + 1);
			var delim2 = datePattern.substring(iDelim2, iDelim2 + 1);
			if (iDelim1 == orderMonth && iDelim2 == orderYear) {
				dateRegexp = new RegExp("^(\\d{2})(\\d{2})(\\d{4})$");
			} else if (iDelim1 == orderMonth) {
				dateRegexp = new RegExp("^(\\d{2})(\\d{2})[" + delim2 + "](\\d{4})$");
			} else if (iDelim2 == orderYear) {
				dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})(\\d{4})$");
			} else {
				dateRegexp = new RegExp("^(\\d{2})[" + delim1 + "](\\d{2})[" + delim2 + "](\\d{4})$");
			}
			var matched = dateRegexp.exec(value1);
			if (matched != null) {
			return ("" + matched[3]+ matched[2]+ matched[1]);
			}
		} else if ((orderMonth > orderYear && orderMonth < orderDay)) {
			var iDelim1 = orderYear + YEAR.length;
			var iDelim2 = orderMonth + MONTH.length;
			var delim1 = datePattern.substring(iDelim1, iDelim1 + 1);
			var delim2 = datePattern.substring(iDelim2, iDelim2 + 1);
			if (iDelim1 == orderMonth && iDelim2 == orderDay) {
				dateRegexp = new RegExp("^(\\d{4})(\\d{2})(\\d{2})$");
			} else if (iDelim1 == orderMonth) {
				dateRegexp = new RegExp("^(\\d{4})(\\d{2})[" + delim2 + "](\\d{2})$");
			} else if (iDelim2 == orderDay) {
				dateRegexp = new RegExp("^(\\d{4})[" + delim1 + "](\\d{2})(\\d{2})$");
			} else {
				dateRegexp = new RegExp("^(\\d{4})[" + delim1 + "](\\d{2})[" + delim2 + "](\\d{2})$");
			}
			var matched = dateRegexp.exec(value1);
			if (matched != null) {
			return ("" + matched[1]+ matched[2]+ matched[3]);
			}
		}
		return value1;

}

function validateEmail(field) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}
	if (field.type == 'text' ||
	field.type == 'textarea' ||
	field.type == 'select-one' ||
	field.type == 'radio') {

		var value = '';
		if (field.type == "select-one") {
			var si = field.selectedIndex;
			if (si >= 0) {
				value = field.options[si].value;
			}
		} else {
			value = field.value;
		}

		if (value.length > 0) {
			//modified by hjs 20081110
			//var filter=/^([\w]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,10}(?:\.[a-z]{2})?)$/i
			var filter=/^(\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*)$/i
			return (filter.test(value));
		}
	}
	return true;
}
function validateInteger(field, maxlength) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}

	if (field.type == 'text' ||
	field.type == 'textarea' ||
	field.type == 'select-one' ||
	field.type == 'radio') {

		var value = '';
		if (field.type == "select-one") {
			var si = field.selectedIndex;
			if (si >= 0) {
				value = field.options[si].value;
			}
		} else {
			value = field.value;
		}

		if (value.length > 0) {
			var filter = /,/;
			value = stripChar(value, filter);
			if ( value.length > maxlength ) {
				return false;
			}
			if (filterInt.test(value))
			return true;
		else
			return false;
		}
	}
	return true;
}

function validateAmountByLang(field, prefix, suffix,lang) { //20171115 apply language to check amount format

    var tempValue;
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}
	if (field.type == 'text' ||
	field.type == 'textarea' ||
	field.type == 'select-one' ||
	field.type == 'radio') {

		var value = '';
		if (field.type == "select-one") {
			var si = field.selectedIndex;
			if (si >= 0) {
				value = field.options[si].value;
			}
		} else {
			value = field.value;
		}
		
		tempValue = value;

		if (value.length > 0) {
			
			if("pt_PT"==lang){
				if(value.indexOf('.')!=-1){
				   return false;
				}else{
					value=value.replace(",",".");
				}

		    }

			var filter = /,/;
			value = stripChar(value, filter);
			var filter = new RegExp("^(\\d{1,"+prefix+"}\\.\\d{1,"+suffix+"}|\\d{1,"+prefix+"}\\.|\\d{1,"+prefix+"})?$");

			if (filter.test(value)){
				field.value= value;
				if("pt_PT"==lang){
					field.value= tempValue;
				}
				return true;
			}else
			  return false;
		}
	}
	return true;
}

function validateAmount(field, prefix, suffix) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}
	if (field.type == 'text' ||
	field.type == 'textarea' ||
	field.type == 'select-one' ||
	field.type == 'radio') {

		var value = '';
		if (field.type == "select-one") {
			var si = field.selectedIndex;
			if (si >= 0) {
				value = field.options[si].value;
			}
		} else {
			value = field.value;
		}

		if (value.length > 0) {
			var filter = /,/;
			value = stripChar(value, filter);
			var filter = new RegExp("^(\\d{1,"+prefix+"}\\.\\d{1,"+suffix+"}|\\d{1,"+prefix+"}\\.|\\d{1,"+prefix+"})?$");

			if (filter.test(value)){
				field.value= value;
				return true;
			}
		else
			return false;
		}
	}
	return true;
}

function validateFile(field, filelength) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return 0;
	}
	if (field.type == 'file') {

		var str = field.value;
		if (trim(str) == "")
		return 0;
		var pos=str.indexOf("\\");
		var lengthStr = str.length;
		while (pos >= 0) {
			str = str.substring(pos+1,lengthStr);
			pos=str.indexOf("\\");
			lengthStr = str.length;
		}
		if (!validateValueMaxLength(str,filelength,'double'))
		return 3;

		var validstr = loupper + lolower + lointeger+".";

		if (isValidCharByByteType(str, validstr, 'double', false)) {
			var index = str.lastIndexOf('.');
			if (index>0 && index<str.length) {

				var ext = str.substring(index+1);
				if (ext.toLowerCase() == "doc" || ext.toLowerCase() == "pdf" || ext.toLowerCase() == "xls" || ext.toLowerCase() == "txt" || ext.toLowerCase() == "ppt") {
					return 0;
				} else {
					return 2;
				}
			} else {
				return 2;
			}
		} else {
			return 1;
		}
	}
	return 0;
}

function validateString(field, validChar) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}

	if ( validChar == '' ) {
		return true;
	}

	if (field.type == 'text' ||
	field.type == 'textarea' ) {

		var value = '';
		value = field.value;

		if (value.length > 0) {
			return isValidChar(value, validChar)
		}
	}
	return true;
}

function validateAddress(field, byteType) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}
	if (field.type == 'text' || field.type == 'textarea') {
		var validstr = loupper + lolower + lointeger;
		validstr += lospset2;

		return isValidCharByByteType(field.value, validstr, byteType, false);
	}
	return true;
}

function validateName(field, byteType) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}
	if (field.type == 'text' || field.type == 'textarea') {
		var validstr = loupper + lolower + lointeger;
		validstr += lospset1;
		return isValidCharByByteType(field.value, validstr, byteType, true);
	}
	return true;
}

function isValidHKID(idnum) {
	var filter = /^([A-Z]{1}\d{6}(?:\d{1}|[A-C]{1}))?$/;
	if (!filter.test(idnum))
	return false;

	var base = 11;
	var sum = 0;
	var chkdg;
	var remain;

	for (var i=0; i<26; i++) {
		if(idnum.substr(0,1)==loupper.substr(i,1)) {
			sum = 8 * (i+1);
			break;
		}
	}

	for (var i=1; i <= 6; i++)
	sum = sum + ((idnum.substr(i,1)) * (8-i));

	chkdg = idnum.substr(7,1);
	remain = sum % base;

	if (remain == 0) {
		if (chkdg != '0')
		return false;
	} else if ((base - remain) < 10) {
		var tmp = base - remain;

		if (chkdg != tmp)
		return false;
	} else if ((base - remain) == 10) {
		if (chkdg != 'A')
		return false;
	} else if ((base - remain) == 11) {
		if (chkdg != 'B')
		return false;
	} else if ((base - remain) == 12) {
		if (chkdg != 'C')
		return false;
	}

	return true;

}

function validateAccDate(field, operation) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return 0;
	}

	if ( validateDate(field, 'YYYYMMDD' ) ) {
		var filter = /\//;
		var value = field.value;
		value = stripChar(value, filter);
		if ((field.type == 'text' ||
		field.type == 'textarea')) {
			if (value.length > 0) {
				if ( operation == 'eqGt' ) {
					if ( (value > ACCDATE) || (value == ACCDATE) ) {
						return 0;
					}
				} else if ( operation == 'eqLt' ) {
					if ( (value < ACCDATE) || (value == ACCDATE)) {
						return 0;
					}
				} else if ( operation == 'eq' ) {
					if ( value == ACCDATE ) {
						return 0;
					}
				} else if ( operation == 'gt' ) {
					if ( value > ACCDATE ) {
						return 0;
					}
				} else if ( operation == 'lt' ) {
					if ( value < ACCDATE ) {
						return 0;
					}
				}
				return -2;
			}
		}
	} else {
		return -1;
	}
	return 0;
}



function validateTime(field, timePattern)
{
	//ignore validation when field is disable.
	if ( isDisable(field) )
	{
		return true;
	}
	//For CWS the timePattern is fixed, there is no timePattern
	timePattern = 'HHMM';
	var value = field.value;
	var filter = /:/;
	value = stripChar(value, filter);
	if (
	(field.type == 'text' || field.type == 'textarea') && (value.length > 0)
	)
	{
		timeRegexp = new RegExp("^(\\d{2})(\\d{2})$");
		var matched = timeRegexp.exec(value);
		if (matched != null)
		{
			if (!isValidTime(matched[1], matched[2]))
			{
				return false;
			}
		}
	else
		{
			return false;
		}
	}
	return true;
}

function isValidTime(hour, minute) {
	if (hour < 0 || hour > 23) {
		return false;
	}
	if (minute < 0 || minute > 59) {
		return false;
	}
	return true;
}

function validateCurrency(currency, exRate, amount) {
	if ( isDisable(currency) && isDisable(exRate) && isDisable(amount) ) {
		return true;
	}
	if ( validateRequired(amount) ) {
		if ( !validateRequired(currency) || !validateRequired(exRate) ) {
			return false;
		}
	}
	return true;
}

function validateENGNUM(field) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}

	var validChar = loupper + lolower + ' ' + lointeger;
	if (field.type == 'text' ||
	field.type == 'textarea' ) {

		var value = '';
		value = field.value;

		if (value.length > 0) {
			return isValidChar(value, validChar)
		}
	}
	return true;

}

function validateENGONLY(field) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}

	var validChar = loupper + lolower + ' ';
	if (field.type == 'text' ||
	field.type == 'textarea' ) {

		var value = '';
		value = field.value;

		if (value.length > 0) {
			return isValidChar(value, validChar)
		}
	}
	return true;

}

function validateNUMONLY(field) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}

	var validChar = lointeger;
	if (field.type == 'text' ||
	field.type == 'textarea' ) {

		var value = '';
		value = field.value;

		if (value.length > 0) {
			return isValidChar(value, validChar)
		}
	}
	return true;

}

function validateCHNONLY(field) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}

	if (field.type == 'text' ||
	field.type == 'textarea' ) {

		str = field.value;
		for (var i=0; i<str.length; i++) {
			if (str.charCodeAt(i) <= 255) {		// non-chinese chars.
				return false;
			}
		}
	}
	return true;

}


function validateSWIFT(field) {
	var validstr = loupper + lolower + ' ' + lointeger;
	validstr += "/-?:().,'+{}";
	var str = field.value;

	for (var i=0; i<str.length; i++) {
		if (str.charCodeAt(i) <= 255) {		// non-chinese chars.

			if (!isValidChar(str.substr(i,1), validstr)) {
				return false;
			}
		}
	}

	return true;
}

//add by wen_chy 20091209
function validateCommon(field, validChar) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}

	if ( validChar == '' ) {
		validChar = loupper + lolower + lointeger + ' ' +"/-+?:().,'";
	}

	if (field.type == 'text' ||
	field.type == 'textarea' ) {

		var value = '';
		value = field.value;

		if (value.length > 0) {
			return isValidChar(value, validChar)
		}
	}
	return true;
}
//add by linrui 20190704 must have english
function validateENGMUST(field) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}

	var validChar = loupper + lolower ;
	if (field.type == 'text' ||
	field.type == 'textarea' ) {

		var value = '';
		value = field.value;

		if (value.length > 0) {
			return isIncludeChar(value, validChar)
		}
	}
	return true;
}

//add by linrui 20190313
function validateRequestCommon(field, validChar) {
	//ignore validation when field is disable.
	if ( isDisable(field) ) {
		return true;
	}
	
	if ( validChar == '' ) {
		validChar = loupper + lolower + lointeger + ' ' ;
	}
	
	if (field.type == 'text' ||
			field.type == 'textarea' ) {
		
		var value = '';
		value = field.value;
		
		if (value.length > 0) {
			return isValidChar(value, validChar)
		}
	}
	return true;
}

function isIncludeChar(str, validstr) {
	for (var i = 0; i < str.length; i++) {
		if (validstr.indexOf(str.substr(i, 1)) >=0)
		return true;
	}
	return false;
}
//end
function eliminate(letter){
	letter = letter + "" ;
	var letter = letter.replace(/[^0-9]/ig,"");
	return letter ;
}
