<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.enq.account_enquiry">
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
<link rel="stylesheet" type="text/css" href="/cib/css/dtree.css">
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
  <SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/dtree.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	document.getElementById("showEqCcy").innerHTML = "<set:label name='Equivalent_In' defaultvalue='Equivalent in '/>&nbsp;" + toCcyDisplay("<set:data name='eqCcy' defaultvalue='MOP'/>");
	// add by hjs
	var listsize = 0;
	<set:list name="currentAccountList">
		listsize ++ ;
	</set:list>
	$('eqCcy').value = 'MOP';
	if (listsize > 0) {
		$('eqCcy').onchange();
	} else {
		$('eqCcy').disabled = true;
	}
}
function selectNode(arg){
	document.getElementById("corpId").value = arg;
	document.getElementById("form1").submit();
}
// add by hjs
function showEquivalent(toCcy){
	if ((toCcy != null) && (toCcy != 'none')) {
		var params = getParams(toCcy);
		var url = '/cib/jsax?serviceName=EquivalentService&' + params + '&language=' + language;
		getMsgToElement(url, toCcy, '', null,true,language);
		document.getElementById("showEqCcy").innerHTML = "<set:label name='Equivalent_In' defaultvalue='Equivalent in '/>&nbsp;" + toCcyDisplay(toCcy);
		//alert('send ok!');
	}
}
function toCcyDisplay(toCcy){
  if(toCcy =='MOP')
  return "<set:label name='MOP_LABEL' rb='app.cib.resource.bnk.corporation'/>";
  if(toCcy =='USD')
  return "<set:label name='USD_LABEL' rb='app.cib.resource.bnk.corporation'/>";
  if(toCcy =='HKD')
  return "<set:label name='HKD_LABEL' rb='app.cib.resource.bnk.corporation'/>";
  if(toCcy =='EUR')
  return "<set:label name='EUR_LABEL' rb='app.cib.resource.bnk.corporation'/>";  
}
function getParams(toCcy) {
	var div_element = null;
	var fromCcy_element = null;
	var fromAmt_element = null;
	var targetType = 'element';
	var targetId = '';
	var sFromCcy = '';
	var sFromAmt = '';
	var div_elements = document.getElementsByTagName('DIV');
	for(i=0; i<div_elements.length; i++){
		div_element = div_elements[i];
		if (div_element.id.substring(0,3) == 'equ') {
			fromCcy_element = $('fromCcy_' + div_element.id.substring(4));
			fromAmt_element = $('fromAmt_' + div_element.id.substring(4));
   			//registerElement(div_element.id);
			targetId += '&targetId=' + div_element.id;
			sFromCcy += '&fromCcy=' + $F(fromCcy_element);
			sFromAmt += '&fromAmt=' + $F(fromAmt_element);
			div_element.innerHTML = 'Loading...';
		}
	}
	targetType = 'targetType=' + targetType;
	toCcy = 'toCcy=' + toCcy;
	return targetType + '&' + toCcy + targetId + sFromCcy + sFromAmt;
}
//<!-- modify by wcc 20180209 --> 
function chanPage(flagPage,arg){
	document.getElementById("flagPage").value=flagPage;
	if (arg == 'list') {
		document.form1.ActionMethod.value = 'listCurrentAccount';
	}
	document.getElementById("form1").submit();
	setFormDisabled("form1");
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/subsidiaryAcc.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.account_enquiry" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
        <set:label name="navigationTitle_ca" defaultvalue="BANK Online Banking > Account Enquiry > Current Account" rb="app.cib.resource.enq.subsidiary_account_enquiry"/>
        <!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" -->
        <set:label name="functionTitle_ca" defaultvalue="CURRENT ACCOUNT(S) ENQUIRY"/>
        <!-- InstanceEndEditable --></td>
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
		  <form action="/cib/subsidiaryAcc.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
                <set:messages width="100%" cols="1"/>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td colspan="2" class="label1"><div class="dtree">
                        <p><set:label name="corporation_structure" defaultvalue="corporation structure"/><a href="javascript: d.openAll();"><set:label name="view_all" defaultvalue="view all"/></a><a href="javascript: d.closeAll();"><set:label name="close_all" defaultvalue="close all"/></a></p>
                        <script type="text/javascript">
			d = new dTree('d');
			if("Y" == "<set:data name='flag'/>"){
				d.clearCookie();
			}
			d.add("<set:data name='CORP_ID'/>","-1","<set:data name='CORP_NAME'/>(<set:data name='CORP_ID'/>)");
			<set:list name="corpList">
			d.add("<set:listdata name='CORP_ID'/>","<set:listdata name='PARENT_CORP'/>","<set:listdata name='CORP_NAME'/>(<set:listdata name='CORP_ID'/>)","javascript:selectNode('<set:listdata name='CORP_ID'/>');");
			</set:list>
			document.write(d);
			</script>
                      </div></td>
                  </tr>
                </table>
				<set:if name="flag" condition="notequals" value="Y">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td height="40" valign="top" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                        <%--<tr>
                          <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=currentAccountList&fileName=corp_current_account&columnTitles=<set:label name='Account_Number'/>,<set:label name='Description_Remark'/>,<set:label name='Original_Currency'/>,<set:label name='Balance'/>&columnNames=ACCOUNT_NO,ACCOUNT_NO||db@accountDesc||specialChar@yes,CURRENCY_CODE||db@currencyCBS,CURRENT_BALANCE||format@amount"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>
                        </tr>
                      --%></table></td>
                    <td align="right" valign="top" class="content1"><b>
                      <set:label name="Equivalent_In" defaultvalue="Equivalent amount in: "/>
                      </b>
                      <select name="eqCcy" id="eqCcy" onChange="showEquivalent(this.value);">
                      <set:rblist file="app.cib.resource.enq.equivalent_ccy">
                        <set:rboption name="eqCcy"/>
                      </set:rblist>
                  </select></td>
                  </tr>
                </table>
				 </set:if>
				<set:if name="flag" condition="notequals" value="Y">
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
				  <tr>
                    <td class="listheader1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                    <td class="listheader1"><set:label name="Description_Remark" defaultvalue="Description/Personal Remark"/></td>
                    <%--
                    <td align="center" class="listheader1"><set:label name="Online_Statement" defaultvalue="Online Statement"/></td>
                    --%>
                    <td align="center" class="listheader1"><set:label name="Original_Currency" defaultvalue="Original Currency"/></td>
                    <td align="right" class="listheader1"><set:label name="Balance" defaultvalue="Balance"/></td>
                    <td align="right" class="listheader1"><div name="showEqCcy" id="showEqCcy"><set:label name="Equivalent_In" defaultvalue="Equivalent in "/>&nbsp;<set:data name="eqCcy" defaultvalue="MOP"/></div></td>
                  </tr>
                  <set:list name="currentAccountList" showNoRecord="YES">
                  <!-- modify by wcc 20180209 -->
			  		<!--<set:listif name='ACCOUNT_STATUS' condition='notequals' value='4'>-->
			  		<tr class="<set:listclass class1='' class2='greyline'/>">
                      <td align="left" class="listcontent1"><a onClick="toTargetFormgoToTarget('/cib/accEnquiry.do?ActionMethod=viewCurrentAccount&range=1&accountNo=<set:listdata name='ACCOUNT_NO'/>&fromPage=sub&from_ccy=<set:listdata name='CURRENCY_CODE' db='currencyCBS'/>')" href="#">
                        <set:listif name='SPECIAL_ACC_FLAG' condition='equals' value='Y'>*</set:listif>
                        <set:listdata name='ACCOUNT_NO'/>
                        </a></td>
                      <td align="left" class="listcontent1"><set:listdata name="ACCOUNT_NO" db="accountDesc"/></td>
                      <%--
                      <td align="center" class="listcontent1"><set:listdata name="ACCOUNT_NO" db="statementForList"/></td>
                      --%>
                      <td align="center" class="listcontent1"><set:listdata name="CURRENCY_CODE" db="rcCurrencyCBS"/>
                        <input name="fromCcy_<set:listdata name='ACCOUNT_NO'/>_<set:listdata name="CURRENCY_CODE" db="currencyCBS"/>" type="hidden" id="fromCcy_<set:listdata name='ACCOUNT_NO'/>_<set:listdata name="CURRENCY_CODE" db="currencyCBS"/>" value="<set:listdata name='CURRENCY_CODE' db='currencyCBS'/>"></td>
                      <td  align="right" class="listcontent1"><set:listdata name="CURRENT_BALANCE" format="amount"/>
                        <input name="fromAmt_<set:listdata name='ACCOUNT_NO'/>_<set:listdata name="CURRENCY_CODE" db="currencyCBS"/>" type="hidden" id="fromAmt_<set:listdata name='ACCOUNT_NO'/>_<set:listdata name="CURRENCY_CODE" db="currencyCBS"/>" value="<set:listdata name='CURRENT_BALANCE'/>"></td>
                      <td align="right" class="listcontent1"><div id="equ_<set:listdata name='ACCOUNT_NO'/>_<set:listdata name="CURRENCY_CODE" db="currencyCBS"/>">&nbsp;</div></td>
                      <!--modify by wcc 20180115 翻页相关值  -->
					  <input type="hidden" name="prePage" id="prePage" value="<set:data name='prePage' />">
					  <input type="hidden" name="nextPage" id="nextPage" value="<set:data name='nextPage' />">
					  <input type="hidden" name="flagPage" id="flagPage" value="<set:data name='flagPage' />">
					  <input type="hidden" name="keyAll" id="keyAll" value="<set:data name='keyAll' />"> 
                    </tr>
			  		<!--</set:listif>-->
                  </set:list>
              </table>
			 </set:if>
			 <set:if name="flag" condition="notequals" value="Y">
                <%--<table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td class="groupseperator">&nbsp;</td>
                  </tr>
                </table>
				 --%>
			</set:if>
               <table width="100%" border="0" cellpadding="0" cellspacing="0">
			<set:if name='SPECIAL_ACC_FLAG' condition='equals' value='Y'>
              <tr>
                <td class="label1"><set:label name="*_account" defaultvalue="* means special account"/></td>
			  </tr>
			</set:if>
            </table>
             <!-- modify by wcc 20180115 --> 
				  <%--<table width="100%" border="0" cellspacing="0" cellpadding="0">
				  	<tr>
				  	<td align="right">
				  	<input type="<set:data name='disableP'/>"  onClick="chanPage('P','list')" name="prePage" value="<set:label name='previous_page' rb='app.cib.resource.common.operation'/>"/>
				  	<input type="<set:data name='disableN'/>"  onClick="chanPage('N','list')" name="nextPage" value="<set:label name='next_page' rb='app.cib.resource.common.operation'/>"/>
				  	</td>
				  	</tr>
				  </table>
                --%>
                <!-- add by linrui for class style -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
				  	<tr>
				  	<td align="right" class="pageline">
					<set:if name='disableP' condition='EQUALS' value='button'>
                    <span class="pagelink">
					<a href="#" onClick="chanPage('P','list')">&lt;&lt;<set:label name='previous_page' rb='app.cib.resource.common.operation'/></a>
					</span> | 
					</set:if>
					<set:if name='disableP' condition='EQUALS' value='hidden'>
					<span class="pagedisabled">&lt;&lt;<set:label name='previous_page' rb='app.cib.resource.common.operation'/></span> | 
					</set:if>
					<set:if name='disableN' condition='EQUALS' value='button'>
					<span class="pagelink">
					<a href="#" onClick="chanPage('N','list')"><set:label name='next_page' rb='app.cib.resource.common.operation'/>&gt;&gt;</a>
					</span>
					</set:if>
					<set:if name='disableN' condition='EQUALS' value='hidden'>
				  	<span class="pagedisabled"><set:label name='next_page' rb='app.cib.resource.common.operation'/>&gt;&gt;</span>
					</set:if>
				  	</td>
				  	</tr>
              </table>
              <!-- add by linrui end -->
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td height="40" class="sectionbutton"><input id="ActionMethod" name="ActionMethod" type="hidden" value="listCurrentAccount"></td>
                    <td height="40" class="sectionbutton"><input id="eqCcy" name="eqCcy" type="hidden" value="<set:data name='eqCcy'/>">
                      <input id="corpId" name="corpId" type="hidden" ></td>
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
