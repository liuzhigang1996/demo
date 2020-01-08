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
function pageLoad(){
		$('textarea[autoHeight]').autoHeight(); 
}

function doSubmit(operator)
{
	document.getElementById("ActionMethod").value = operator;
	
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}

function FormatDescription(description){
	description = description.replace(/\n/g, '<br>'); //<br> replace
	description = description.replace(/\s/g, ' '); //空格 replace
	return description;
}

//textarea height auto
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
//end

</script>
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><set:label name="navigationTitleEditConfirm" /></td>
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
    <td class="title1" nowrap><set:label name="functionTitleEditConfirm" /></td>
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
					<div>
						<span class = "myTitle" style = "float: left;"><set:label name = 'descriptionC'/></span>
						<div class = "desciption"><textarea readonly="readonly"   autoHeight = "true"  id = "descriptionC" name = "descriptionC" class = "myTextarea" ><set:data name = 'descriptionC' /></textarea></div>
					</div>
				</div>
				<div>
					<div>
						<span class = "myTitle" style = "float: left;"><set:label name = 'descriptionE'/></span>
						<div class = "desciption"><textarea readonly="readonly"   autoHeight = "true"  id = "descriptionE" name = "descriptionE" class = "myTextarea" ><set:data name = 'descriptionE' /></textarea></div>
					</div>
				</div>
				<div style = "clear: both;"></div>
				<div style = "width: 100%;text-align: center;">
					<input class = "myButton"  type = "button" value = "<set:label name = 'confirmButton'/>"  onclick="doSubmit('editConfirm')"/>
					<input class = "myButton"  type = "button" value = "<set:label name = 'cancelButton'/>" onclick="doSubmit('cancel')">
				</div>
			</div>
			
			<input id="ActionMethod" name="ActionMethod" type="hidden" value="edit">
			<input id="menu2nd" name="menu2nd" type="hidden" value= "<set:data name = 'menu2nd'/>">
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
