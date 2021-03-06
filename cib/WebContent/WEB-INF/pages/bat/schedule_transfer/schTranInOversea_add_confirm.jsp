<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bat.schedule_transfer_oversea">
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
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit(){
	if(checkAssignedUser()){
	setFormDisabled('form1');
		document.form1.submit();
	}
}
function doCancel(){
	setFormDisabled("form1");
	document.getElementById("ActionMethod").value="addCancel";
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/scheduleTransferOversea.do" --><!-- InstanceParam name="help_href" type="text" value="#" passthrough="true" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bat.schedule_transfer_oversea" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="BANK Online Banking >Transfer > Accounts in Banks Overseas"/><!-- InstanceEndEditable --></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#" onClick="printPage('pageheader');"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
	<!--
    <td class="buttonhelp"><a href="@@@(help_href)@@@"><img src="/cib/images/shim.gif" width="36" height="18" border="0"></a></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="TRANSFER TO ACCOUNTS IN BANKS OVERSEAS"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/scheduleTransferOversea.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<set:messages width="100%" cols="1" align="center"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
			 <tr >
                <td class="groupinput", colspan="2"><set:label name="Confirmation" defaultvalue="Confirmation"/></td>
              </tr>
			   <tr>
                <td class="label1" ><set:label name="Operation" defaultvalue="Operation"/></td>
				<td class="label1" ><set:label name="functionTitle" defaultvalue="Add a schedule (Accounts in BANK)"/></td>
              </tr>
			   <tr>
                <td colspan="2" class="groupinput"><set:label name="SchTransfer_Info" defaultvalue="Schedule Transfer Information"/></td>
              </tr>
			   <tr >
			    <td class="label1"><set:label name="Schedule_Name" defaultvalue="Schedule Name"/></td>
               <td class="content1"><set:data name='scheduleName' />
			    </td>
              </tr>
			  <tr class="greyline">
                <td class="label1"><set:label name="Frequence" defaultvalue="Frequence"/></td>
                <td class="content1" colspan="3" ><set:if name="frequenceType" condition="EQUALS" value="1"><set:label name="Daily" defaultvalue="Daily (Work Day Only)"/></set:if>
				<set:if name="frequenceType" condition="EQUALS" value="2"><set:label name="WeeklyInfo" defaultvalue="Weekly---Date"/>:<set:data name='frequenceDays' rb="app.cib.resource.bat.week_display"/></set:if>
				<set:if name="frequenceType" condition="EQUALS" value="3"><set:label name="Monthly" defaultvalue="Monthly"/>:<set:data name='frequenceDays'  rb="app.cib.resource.bat.schedule_month"/></set:if>
				<set:if name="frequenceType" condition="EQUALS" value="4"><set:label name="Days_per_month" defaultvalue="Days per month"/>:<set:data name='frequenceDays'/></set:if></td>
              </tr>
              <tr >
			    <td class="label1"><set:label name="Transfer_End_Date" defaultvalue="End Date"/></td>
               <td class="content1"><set:data name='endDate' format="date" />
			    </td>
              </tr>
			  <tr class="greyline">
                <td colspan="2"  class="label1"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
              </tr>
			  <tr>
			    <td class="label1"><set:label name="Transfer_Account" defaultvalue="Account Number"/></td>
               <td class="content1"><set:data name='fromAccount'/>
			    </td>
              </tr>
			   <tr class="greyline">
			    <td class="label1"><set:label name="Debit_Amount" defaultvalue="Amount to be debited"/></td>
               <td class="content1"><set:data name='fromCurrency' db='rcCurrencyCBS'/><set:data name='debitAmount' format="amount"/>
			    </td>
              </tr>
			  <tr >
				<td class="label1"  colspan="3"><b><set:label name="Transfer_To" defaultvalue="Transfer to"/></b></td>
              </tr>
			   <tr class="greyline">
			    <td class="label1"><set:label name="Beneficiary_Account_Name" defaultvalue="Beneficiary Account Name"/></td>
               <td class="content1"><set:data name='beneficiaryName1'/><br><set:data name='beneficiaryName2'/></td>
			   </tr>
			   <tr>
			    <td class="label1"><set:label name="Beneficiary_Account" defaultvalue="Account Number"/></td>
               <td class="content1"><set:data name='toAccount'/>
			    </td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Transfer_Amount" defaultvalue="Transfer Amount"/></td>
                <td class="content1" colspan="2" ><set:data name='toCurrency' db='rcCurrencyCBS'/><set:data name='transferAmount' format="amount"/>
				</td>
              </tr>
			   <set:if name="fromCurrency" condition="NOTEQUALS" field="toCurrency">
			   <tr >
			    <td class="label1"><set:label name="Exchange_Rate" defaultvalue="Exchange Rate"/></td>
               <td class="content1"><set:data name='exchangeRate' format="rate"/>&nbsp;&nbsp;<span style = "color:red;"><set:label name="Exchange_Rate_Tip" defaultvalue="The Rate is for reference only, the dealing rate will be quoted upon trading."/></span>
			    </td>
              </tr>
			  </set:if>
			   <!--------------added by xyf 20090721 begin--------------->
			  <tr class="greyline">
			    <td class="label1"><set:label name="Beneficiary_Address" defaultvalue="Beneficiary Address"/></td>
               <td class="content1"><set:data name='beneficiaryName3'/><br><set:data name='beneficiaryName4'/></td>
              </tr>
			  <!---------------added by xyf 20090721 end--------------->
			   <tr>
			    <td class="label1"><set:label name="Country_of_Beneficiary_Bank" defaultvalue="Country of Beneficiary Bank"/></td>
               <td class="content1"><set:data name='beneficiaryCountry' db="country"/>
			    </td>
              </tr>
			  <tr class="greyline">
			    <td class="label1"><set:label name="Beneficiary_City" defaultvalue="City of Beneficiary Bank"/></td>
               <td class="content1"><set:if name='otherCityFlag' condition='equals' value='N'><set:data name='beneficiaryCity' db="city"/></set:if>
			   <set:if name='otherCityFlag' condition='equals' value='Y'><set:data name='beneficiaryCity' /></set:if>
			    </td>
              </tr>
			  <tr>
			    <td class="label1"><set:label name="Beneficiary_Bank" defaultvalue="Name of Beneficiary Bank"/></td>
               <td class="content1"><set:if name='otherBankFlag' condition='equals' value='N'><set:data name='beneficiaryBank1' db="overseaBank"/></set:if>
			   <set:if name='otherBankFlag' condition='equals' value='Y'><set:data name='beneficiaryBank1'/></set:if>
			    </td>
              </tr>
			   <tr class="greyline">
			    <td class="label1"><set:label name="Beneficiary_Bank_Address" defaultvalue="Address of Beneficiary Bank"/></td>
               <td class="content1"><set:data name='beneficiaryBank2'/>
			   <br><set:data name='beneficiaryBank3'/>
			    </td>
              </tr>
			 <tr>
			    <td class="label1"><set:label name="SWIFT_Address" defaultvalue="SWIFT_Address"/></td>
               <td class="content1"><set:data name='swiftAddress'/>
			    </td>
              </tr>
			   <set:if name='spbankLabel' condition='notequals' value='#'>
			  <tr class="greyline">
			    <td class="label1"><set:data name='spbankLabel'/></td>
               <td class="content1"><set:data name='spbankCode'/></td>
              </tr>
			  </set:if>
                <!--<tr>
                  <td class="label1"><set:label name="Correspondent_Bank" defaultvalue="Correspondent Bank"/></td>
                  <td  class="content1"  colspan="3">
				  <set:data name='correspondentBankLine1'/><br>
				  <set:data name='correspondentBankLine2'/><br>
				  <set:data name='correspondentBankLine3'/><br>
				  <set:data name='correspondentBankLine4'/>			  
				  </td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Correspondent_Bank_Account" defaultvalue="Correspondent Bank Account"/></td>
                  <td  class="content1"  colspan="3"><set:data name='correspondentBankAccount'/>
				  </td>
                </tr>
			  --><tr >
			    <td class="label1"><set:label name="Message_Send" defaultvalue="Message to be sent"/></td>
               <td class="content1"><set:data name='messsage'/>
			   <br><set:data name='messsage2'/>
			    </td>
              </tr>
              <!-- modified by lzg add set if 20190601 -->
              <set:if name="chareBy" condition="NOTEQUALS" value="S">
			  <tr class="greyline">
			  <td class="label1"><set:label name="Deduct_Charge_from_Account" defaultvalue="Deduct Charge from  Account "/></td>
              <td class="content1" colspan="2" ><set:data name='chargeAccount'/>
				</td>
              </tr>
              </set:if>
              <!-- add by lzg end -->
			   <tr >
			    <td class="label1"><set:label name="Charge_Amount" defaultvalue="Charge Amount"/></td>
               <td class="content1"><set:data name='toCurrency' db='rcCurrencyCBS'/> <set:data name='chargeAmount' format="amount"/>
			    </td>
              </tr>
			   <tr  class="greyline" >
			    <td class="label1"><set:label name="Overseas_charges_to_be_paid_by" defaultvalue="Overseas charges to be paid by"/></td>
               <td class="content1"><set:data name='chareBy' rb="app.cib.resource.txn.charge_name"/>
			    </td>
              </tr>
			  <!-- add by lw 2011-01-18 -->
			  <!-- modified by lzg 20190602 -->
			   <!--<set:if name="showPurpose" condition="EQUALS" value="true">
			  <tr>
               	<td class="label1"><set:label name="Purpose" defaultvalue="Purpose"/></td>
               <td class="content1"><set:data name='purpose'/></td>
             </tr >	
			 </set:if> 
			 -->
			 <tr>
               	<td class="label1"><set:label name="Purpose" defaultvalue="Purpose"/></td>
               <td class="content1"><set:data name='purposeCode' rb = "app.cib.resource.txn.purposecode"/></td>
             </tr >
			 <!-- modified by lzg end -->
			 <!-- add by lw end -->	
              <tr class="greyline">
                <td class="label1"><set:label name="Remark" defaultvalue="Remark"/></td>
                <td class="content1"><set:data name='remark'/> 
              </tr>
            </table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
			  <tr>
			  <td>
			  <set:assignuser selectFlag="Y" /> 
			  </td>
			  </tr>
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
				  <input id="buttonOk" name="buttonOk" type="button" value="<set:label name='buttonConfirm' defaultvalue='Confirm'/>" onClick="doSubmit();">
				  <input id="return" name="return" type="button" value="<set:label name='buttonReturn'/>" onClick="doCancel();">
				  <input id="ActionMethod" name="ActionMethod" type="hidden" value="addConfirm">
				  <set:singlesubmit/>
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
