<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.flow.approve">
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
<script language="JavaScript" src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
 function pageLoad(){
 var form1=document.form1;
var date_range='<set:data name="date_range"/>';
	if (date_range == 'all'){
		form1.date_range[0].checked = true;
		form1.dateFrom.disabled = true;
		form1.dateTo.disabled = true;
	} else if (date_range == 'range'){
		form1.date_range[0].checked = true;
		form1.dateFrom.disabled = false;
		form1.dateTo.disabled = false;
		form1.dateRange.disabled = false;
	} else{
		form1.date_range[0].checked = true;
		form1.dateFrom.disabled = true;
		form1.dateTo.disabled = true;
	}
	//add by linrui 20190916
	var sortOrder='<set:data name="sortOrder"/>';
	if(sortOrder == '2'){
	    document.getElementById("sortOrder")[1].selected = true;
	}else{
	    document.getElementById("sortOrder")[0].selected = true;
    }
    //end
	
}
function doEnquiry(){
  if(validate_query(document.getElementById("form1"))){
    setFormDisabled("form1");
    document.form1.submit();
  }
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
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/approve.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.flow.approve" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
        <set:label name="navigationTitle_history" defaultvalue="MDB Corporate Online Banking > Authorization > Authorization History List"/>
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
        <set:label name="functionTitle_history" defaultvalue="AUTHORIZATION HISTORY LIST"/>
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
		  <form action="/cib/approve.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                         <set:fieldcheck name="approve_history" form="form1" file="approve_history"/></td>
                      </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
				<table border="0" cellspacing="0" cellpadding="3" width="100%">
				<tr>
				<td width="20%" class="label1"><set:label name="period" defaultvalue="period" rb="app.cib.resource.bat.payroll"/></td>
				
                  <td class="content1"><span class="content">
                    <input id="date_range" name="date_range" type="radio" value="all" onClick="changeRange('all')" checked="checked">
                    <set:label name="all" defaultvalue="all"/> </span></td>
				</tr>
                  <tr>
                  <td class="label1" width="28%">&nbsp;</td>
				  
                    <td class="content1" width="72%">
					
					<input id="date_range" name="date_range" type="radio" value="range" checked="checked" onClick="changeRange('range')">
					
                      <set:label name="Query_From" defaultvalue="From"/>
                      <input type="text" name="dateFrom" id ="dateFrom" value="<set:data name='dateFrom'/>" size="10" maxlength="10">
                      <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" >&nbsp;&nbsp;<set:label name="Query_To" defaultvalue="To"/>&nbsp;&nbsp;
                      <input type="text" name="dateTo" id ="dateTo" value="<set:data name='dateTo'/>" size="10" maxlength="10" >
                    <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" ><span class="content1">
                    <select id="dateRange" name="dateRange"  onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));">
                      <option value='0'><set:label name="select_short_cut_date" defaultvalue="----- Select a Date Short-cut -----"/></option>
                      <set:rblist file="app.cib.resource.common.date_selection"> <set:rboption/> </set:rblist>
                    </select>
                    </span></td>                    
                  </tr>
                  
                  <!-- add by linrui 20190916 -->
                  <tr>
                  <td class="label1" width="28%"><set:label name="Sort_Order" defaultvalue="Sort_Order"/></td>
                    <td class="content1" width="72%">
					<span class="content1">
                    <select id="sortOrder" name="sortOrder" >
                      <set:rblist file="app.cib.resource.common.sort_order"> <set:rboption/> </set:rblist>
                    </select>
                    </span></td>                    
                  </tr>
                  <!-- end -->
                  
                </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" class="sectionbutton">
                    <div align="center"><span class="tablecontent">
                      <input id="buttonEnquiry" name="buttonEnquiry" type="button" value="<set:label name='buttonEnquiry' defaultvalue='Enquiry'/>" onClick="doEnquiry()">
                      </span>                      
                      </div></td>
                </tr>
              </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td height="40" class="sectionbutton"><input id="ActionMethod" name="ActionMethod" type="hidden" value="historyEnquiry">
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
