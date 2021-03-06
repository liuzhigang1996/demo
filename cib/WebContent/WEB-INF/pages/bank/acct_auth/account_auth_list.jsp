<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.sys.account_authorization">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
    document.getElementsByName("Select_By")[0].checked = ('<set:data name="Select_By"/>'=='0');
    document.getElementsByName("Select_By")[1].checked = ('<set:data name="Select_By"/>'=='1');
    document.getElementsByName("Select_By")[2].checked = ('<set:data name="Select_By"/>'=='2');
    changeRange('<set:data name="Select_By"/>');
}
function doSubmit(arg)
{
	if (arg == 'search') {
		if(validate_search(document.getElementById("form1"))){
			document.form1.ActionMethod.value = 'listAccountAuthorization';	
			setFormDisabled('form1');
			document.getElementById("form1").submit();
		}
	} else if (arg == 'edit') {
		if(validate_select(document.getElementById("form1"))){
			document.form1.ActionMethod.value = 'editLoad';
			setFormDisabled('form1');
			document.getElementById("form1").submit();
		}
	} else if (arg == 'delete') {
		if(validate_select(document.getElementById("form1"))){
			document.form1.ActionMethod.value = 'delete';
			setFormDisabled('form1');
			document.getElementById("form1").submit();
		}
	} else if (arg == 'add') {
	    document.form1.ActionMethod.value = 'addLoad';
		setFormDisabled('form1');
		document.getElementById("form1").submit();
	}
}
function changeRange(range){
	if(range == '0'){
 		document.getElementById("account").value='';
		document.getElementById("authUser").value='';
		document.getElementById("account").disabled = true;
		document.getElementById("authUser").disabled = true;
	}else if(range == '1'){
		document.getElementById("authUser").value='';
		document.getElementById("authUser").disabled = true;
		document.getElementById("account").disabled = false;
	}else if(range == '2'){
 		document.getElementById("account").value='';
		document.getElementById("account").disabled = true;
		document.getElementById("authUser").disabled = false;
	}
}
function setKeys(arg1, arg2, arg3){
	document.form1.selectAccount.value = arg1;
	document.form1.selectUser.value = arg2;
	document.form1.selectRecordId.value = arg3;
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/accountAuthorization.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.account_authorization" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="MDB Corporate Online Banking > Company Preferences > Account Authorization"/><!-- InstanceEndEditable --></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#" onClick="printPage('pageheader');"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
	<!--
    <td class="buttonhelp"><a href="#"><img src="/cib/images/shim.gif" width="36" height="18" border="0"></a></td>
	-->
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="ACCOUNT AUTHORIZATION LIST"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/accountAuthorization.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="search" form="form1" file="account_authorization" /></td>
                      </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Corp_Info" defaultvalue="Corporation Information"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/></td>
				<td width="72%" class="content1"><set:data name='corpId'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Corp_Name" defaultvalue="Company Name"/></td>
                <td class="content1"><set:data name='corpName'/></td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput">&nbsp;</td>
              </tr>
            </table>
			 <table width="100%" border="0" cellspacing="0" cellpadding="3">
			 <tr>
                <td class="label1" rowspan="4"><set:label name="Select_By" defaultvalue="Select By"/></td>
			</tr>
			<tr>
                <td class="content1" ><input type="radio" name="Select_By" id="Select_By" value="0" onClick="changeRange('0')"><set:label name="Select_All" defaultvalue="All"/></td>
				<td class="content1" >&nbsp; </td>
			</tr>
			<tr>
				<td class="content1" ><input type="radio" name="Select_By" id="Select_By" value="1" onClick="changeRange('1')" ><set:label name="Select_By_Account" defaultvalue="By Account"/></td>
				<td class="content1" colspan="2" ><input id="account" name="account" type="text" value="<set:data name='account'/>" size="25" maxlength="20"> </td>
			</tr>
			<tr>
				<td class="content1" ><input type="radio" name="Select_By" id="Select_By" value="2" onClick="changeRange('2')"><set:label name="Select_By_User" defaultvalue="By User"/></td>
				<td class="content1" colspan="2" ><input id="authUser" name="authUser" type="text" value="<set:data name='authUser'/>" size="50" maxlength="40"></td>
             </tr>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr align="center">
                <td width="55%" height="40" align="right"> 
				  <span class="sectionbutton"><input id="buttonOk" name="buttonOk" type="button" value="<set:label name='Search' defaultvalue='Search'/>" onClick="doSubmit('search')"></span>
               	  <input id="ActionMethod" name="ActionMethod" type="hidden" value="listAccountAuthorization">
				  <input id="corpId" name="corpId" type="hidden" value="<set:data name='corpId'/>">
				  <input id="corpName" name="corpName" type="hidden" value="<set:data name='corpName'/>">
				  </td>
                <td align="right" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                  <%--<tr>
                    <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=acct_auth_list&fileName=account_authorization&columnTitles=<set:label name='Account_No'/>,<set:label name='Authorization_User'/>&columnNames=ACCOUNT_NO,AUTH_USER"><set:label name="download" rb="app.cib.resource.common.operation"/></a> </td>
                  </tr>
                --%></table></td>
              </tr>
            </table> 
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
			   <tr>
                <td class="listheader1">&nbsp;</td>
                <td class="listheader1"><div align="left"><set:label name="Account_No" defaultvalue="ACCOUNT NO"/></div></td>
                <td class="listheader1"><div align="left"><set:label name="Authorization_User" defaultvalue="AUTHORIZATION USER"/></div></td>
               </tr>
			  <set:list name="acct_auth_list" showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1"><input type="radio" name="Account_User" id="Account_User" value="" onClick="setKeys('<set:listdata name='ACCOUNT_NO'/>','<set:listdata name='AUTH_USER'/>','<set:listdata name='RECORD_ID'/>')" ></td>
                <td class="listcontent1"><div align="left"><set:listdata name="ACCOUNT_NO"/></div></td>
			    <td class="listcontent1"><div align="left"><set:listdata name="AUTH_USER"/></div></td>
              </tr>
			  </set:list>
			  <input id="selectAccount" name="selectAccount" type="hidden" value="">
			  <input id="selectUser" name="selectUser" type="hidden" value="">
			  <input id="selectRecordId" name="selectRecordId" type="hidden" value="">			  
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton">
                    <input id="buttonAdd" name="buttonAdd" type="button" value="<set:label name='buttonAdd' defaultvalue='Add'/>" onClick="doSubmit('add');">
					<input id="buttonEdit" name="buttonEdit" type="button" value="<set:label name='buttonEdit' defaultvalue=' Edit '/>" onClick="doSubmit('edit')">
                    <input id="buttonDelete" name="buttonDelete" type="button" value="<set:label name='buttonDelete' defaultvalue=' Delete '/>" onClick="doSubmit('delete');">					
				</td>
                </tr>
              </table>
			<!-- InstanceEndEditable -->
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
