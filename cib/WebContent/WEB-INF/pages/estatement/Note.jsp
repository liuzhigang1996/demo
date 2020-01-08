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

<script type="text/javascript">
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	doSubmit();
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

</head>
<body  onLoad="pageLoad();">
<div >
	<form action="/cib/estatement.do" method="post" name="form1" id="form1" target="main">
	<set:messages width="100%" cols="1" align="center"/>
	<set:fieldcheck name="history" form="form1" file="estatement_history" />
	<div class="groupseperator_e" >&nbsp;</div>
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
		<span class="content_e">
			<set:label name="date_from" defaultvalue="date from"/>
		</span>
        <input id="dateFrom" name="dateFrom" type="text" value="" size="12" maxlength="10" disabled="disabled"> 
        <img src="/cib/images/datetime.gif" width="16" height="16"  style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" >&nbsp;&nbsp;&nbsp;&nbsp;
        <span class="content_e">
            <set:label name="date_to"/>
        </span>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input id="dateTo" name="dateTo" type="text" value="" size="12" maxlength="10" disabled="disabled"> 
        <img src="/cib/images/datetime.gif" width="16" height="16" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" >
		<select id="dateRange" name="dateRange" disabled="disabled" onchange="quickQuery(this);">
			<option value='0'><set:label name="Select_Date_Short_Cut" defaultvalue="----- Select a Date Short-cut ------"/></option>
            <set:rblist file="app.cib.resource.common.date_selection">
            	<set:rboption/>
            </set:rblist>
        </select>
        <span class = "MyButton"><input id="buttonSearch" name="buttonSearch" type="button" value="&nbsp;&nbsp;<set:label name='buttonSearch' />&nbsp;&nbsp;" onClick="doSubmit()"/></span>
	</div>
    	<input id="ActionMethod" name="ActionMethod" type="hidden" value="<set:data name = 'ActionMethod' />">
		<set:singlesubmit/>
    </form>
</div>
</body>
</set:loadrb>
</html>
