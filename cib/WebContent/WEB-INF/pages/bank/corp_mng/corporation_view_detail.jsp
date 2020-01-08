<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.bnk.corporation">
  <table width="100%" border="0" cellspacing="0" cellpadding="3">
    <tr>
      <td colspan="2" class="groupinput"><set:label name="Corp_Info" defaultvalue="Corporation Information" rb="app.cib.resource.bnk.corporation"/></td>
    </tr>
    <tr>
      <td width="28%" class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/></td>
      <td width="72%" class="content1"><set:data name="corpId" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Corp_Name" defaultvalue="Company Name"/></td>
      <td class="content1"><set:data name="corpName" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Full_Name" defaultvalue="Full Name"/></td>
      <td class="content1"><set:data name="fullName" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="corp_type"/></td>
      <td class="content1"><set:data name="corpType" rb="app.cib.resource.common.corp_type"/></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Commercial_Registry_No" defaultvalue="Commercial Registry No"/></td>
      <td class="content1"><set:data name="commercialRegistryNo" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Country_of_Registry" defaultvalue="Country of Registry"/></td>
      <td class="content1"><set:data name="registryCountry" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Address" defaultvalue="Address"/></td>
      <td class="content1"><set:data name="address1" />
        <br>
        <set:data name="address2" />
        <br>
        <set:data name="address3" />
        <br>
        <set:data name="address4" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
      <td class="content1"><set:data name="telephone" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Fax_no" defaultvalue="Fax_no"/></td>
      <td class="content1"><set:data name="faxNo" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Email" defaultvalue="E-mail"/></td>
      <td class="content1"><set:data name="email" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Foreign_City" defaultvalue="Foreign City"/></td>
      <td class="content1"><set:data name="foreignCity" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Time_Zone" defaultvalue="Time Zone"/></td>
      <td class="content1"><set:data name="timeZone" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="AUTHENTICATION_MODE" /></td>
      <td class="content1"><set:data name="authenticationMode" rb="app.cib.resource.bnk.authentication_mode" /></td>
    </tr>
     <!-- add by long_zg 2019-05-16 for otp login begin -->
     <tr class="greyline">
       <td class="label1"><set:label name="OTP_LOGIN" /></td>
       <td class="content1"><set:data name="otpLogin" rb="app.cib.resource.common.yes_no" /></td>
     </tr>
     <!-- add by long_zg 2019-05-16 for otp login end -->
     
    <tr class="">
      <td class="label1"><set:label name="ALLOW_TD" defaultvalue="Allow Transaction Executer"/></td>
      <td class="content1"><set:data name="allowTd" rb="app.cib.resource.common.yes_no" />
      </td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="ALLOW_EXECUTOR" defaultvalue="Allow Time Deposit Operation"/></td>
      <td class="content1"><set:data name="allowExecutor" rb="app.cib.resource.common.yes_no" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="ALLOW_APPROVER_SELECTION" defaultvalue="Allow Approver Selection"/></td>
      <td class="content1"><set:data name="allowApproverSelection" rb="app.cib.resource.common.yes_no" /></td>
    </tr>
    <!--<tr class="">
      <td class="label1"><set:label name="ALLOW_TAX_PAYMENT" defaultvalue="Allow Tax Payment"/></td>
      <td class="content1"><set:data name="allowTaxPayment" rb="app.cib.resource.common.yes_no" /></td>
    </tr>
    --><!--<tr class="greyline">
      <td class="label1"><set:label name="ALLOW_DISPLAY_BOTTOM" defaultvalue="Allow Display Bottom Menu"/></td>
      <td class="content1"><set:data name="allowDisplayBottom" rb="app.cib.resource.common.yes_no" /></td>
    </tr>
    --><tr class="">
      <td class="label1"><set:label name="ALLOW_FINANCIAL_CONTROLLER" /></td>
      <td class="content1"><set:data name="allowFinancialController" rb="app.cib.resource.common.yes_no" /></td>
    </tr>
    <!--<tr class="greyline">
      <td class="label1"><set:label name="Authorization_Cur" defaultvalue="Authorization Preference Currency"/></td>
      <td class="content1"><input name="authCurMop" type="checkbox" value="Y" disabled="disabled" 
        <set:selected key="authCurMop" equalsvalue="Y" output="checked"/>
        >MOP<br>
        <input name="authCurHkd" type="checkbox" value="Y" disabled="disabled" 
        <set:selected key="authCurHkd" equalsvalue="Y" output="checked"/>
        >HKD<br>
        <input name="authCurUsd" type="checkbox" value="Y" disabled="disabled" 
        <set:selected key="authCurUsd" equalsvalue="Y" output="checked"/>
        >USD<br>
        <input name="authCurEur" type="checkbox" value="Y" disabled="disabled" 
        <set:selected key="authCurEur" equalsvalue="Y" output="checked"/>
        >EUR </td>
    </tr>
	--><tr class="greyline">
      <td class="label1"><set:label name="merchant_group" /></td>
      <td class="content1"><set:data name="merchantGroup"/></td>
    </tr>
    <tr >
      <td colspan="2" class="groupinput"><set:label name="Representative1_Information" defaultvalue="Representative1 Information"/></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Full_Name" defaultvalue="Full Name"/></td>
      <td class="content1"><set:data name="rep1FullName" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Short_Name" defaultvalue="Short Name"/></td>
      <td class="content1"><set:data name="rep1ShortName" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Title" defaultvalue="Title"/></td>
      <td class="content1"><set:data name="rep1Title"   /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="ID_Type" defaultvalue="ID Type"/></td>
      <td class="content1"><set:data name="rep1IdType"   rb="app.cib.resource.common.id_type"/></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="ID_NO" defaultvalue="ID No."/></td>
      <td class="content1"><set:data name="rep1IdNo" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="ID_Issue_date" defaultvalue="ID Issue date"/></td>
      <td class="content1"><set:data name="rep1IdIssueDate" format="date"/></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="ID_Issuer" defaultvalue="ID Issuer"/></td>
      <td class="content1"><set:data name="rep1IdIssuer" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
      <td class="content1"><set:data name="rep1Telephone" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Mobile_Telephone" defaultvalue="Mobile Telephone"/></td>
      <td class="content1"><set:data name="rep1Mobile" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Email" defaultvalue="Email"/></td>
      <td class="content1"><set:data name="rep1Email" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Fax_no" defaultvalue="Fax No."/></td>
      <td class="content1"><set:data name="rep1FaxNo" /></td>
    </tr>
    <tr >
      <td colspan="2" class="groupinput"><set:label name="Representative2_Information" defaultvalue="Representative2 Information"/></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Full_Name" defaultvalue="Full Name"/></td>
      <td class="content1"><set:data name="rep2FullName" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Short_Name" defaultvalue="Short Name"/></td>
      <td class="content1"><set:data name="rep2ShortName" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Title" defaultvalue="Title"/></td>
      <td class="content1"><set:data name="rep2Title"   /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="ID_Type" defaultvalue="ID Type"/></td>
      <td class="content1"><set:data name="rep2IdType"   rb="app.cib.resource.common.id_type"/></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="ID_NO" defaultvalue="ID No."/></td>
      <td class="content1"><set:data name="rep2IdNo" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="ID_Issue_date" defaultvalue="ID Issue date"/></td>
      <td class="content1"><set:data name="rep2IdIssueDate" format="date"/></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="ID_Issuer" defaultvalue="ID Issuer"/></td>
      <td class="content1"><set:data name="rep2IdIssuer" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
      <td class="content1"><set:data name="rep2Telephone" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Mobile_Telephone" defaultvalue="Mobile Telephone"/></td>
      <td class="content1"><set:data name="rep2Mobile" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Email" defaultvalue="Email"/></td>
      <td class="content1"><set:data name="rep2Email" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Fax_no" defaultvalue="Fax No."/></td>
      <td class="content1"><set:data name="rep2FaxNo" /></td>
    </tr>
  </table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
