<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.sys.corp_user">
  <table width="100%" border="0" cellspacing="0" cellpadding="3">
    <tr>
      <td colspan="2" class="groupinput"><set:label name="Corp_User_Info" rb="app.cib.resource.sys.corp_user"/></td>
    </tr>
    <!-- add by lzg for GAPMC-EB-001-0063 -->
     <tr>
        <td width="28%" class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/></td>
        <td width="72%" class="content1"><set:data name='corpId'/></td>
     </tr>
     <tr class="greyline">
       <td class="label1"><set:label name="Corp_Name" defaultvalue="Company Name"/></td>
       <td class="content1"><set:data name='corpName'/></td>
     </tr>
    <!-- add by lzg end -->
    <tr>
      <td width="28%" class="label1"><set:label name="User_Id" defaultvalue="User Id" rb="app.cib.resource.sys.corp_user"/></td>
      <td width="72%" class="content1"><set:data name='userId'/></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Role" defaultvalue="Role" rb="app.cib.resource.sys.corp_user"/></td>
      <td class="content1"><set:data name="roleId" rb="app.cib.resource.common.corp_role" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="User_Name" defaultvalue="User Name" rb="app.cib.resource.sys.corp_user"/></td>
      <td class="content1"><set:data name='userName'/></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Gender" defaultvalue="Gender"/></td>
      <td class="content1"><set:data name='gender' rb="app.cib.resource.common.gender"/></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Title" defaultvalue="Title"/></td>
      <td class="content1"><set:data name='title' /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Full_Name" defaultvalue="Full Name"/></td>
      <td class="content1"><set:data name='fullName'/></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="ID_Type" defaultvalue="ID_Type"/></td>
      <td class="content1"><set:data name='idType' rb="app.cib.resource.common.id_type" /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="ID_NO" defaultvalue="ID NO."/></td>
      <td class="content1"><set:data name='idNo' format = "IDNO"/></td>
    </tr>
    <tr  class="">
      <td class="label1"><set:label name="ID_Issue_date" defaultvalue="ID Issue date"/></td>
      <td class="content1"><set:data name='idIssueDate' format="date"/></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="ID_Issuer" defaultvalue="ID Issuer"/></td>
      <td class="content1"><set:data name='idIssuer' /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
      <td class="content1"><set:data name='telephone' /></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Mobile_Telephone" defaultvalue="Mobile Telephone"/></td>
      <td class="content1">
                  <!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB begin -->
                  <set:data name='mobileCountryCode' /> <set:data name='mobileAreaCode' />
                  <!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB end --> 
                  <set:data name='mobile' /></td>
    </tr>
    <tr>
      <td class="label1"><set:label name="Email" defaultvalue="Email" rb="app.cib.resource.sys.corp_user"/></td>
      <td class="content1"><set:data name='email'/></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Fax_no" defaultvalue="Fax No."/></td>
      <td class="content1"><set:data name='faxNo'/></td>
    </tr>
    <tr  class="">
      <td class="label1"><set:label name="Cert_Info1" defaultvalue="User Certificate"/></td>
      <td class="content1"><set:data name='certInfo1'/></td>
    </tr>
    <tr class="greyline">
      <td class="label1"><set:label name="Authoriztion_Level" defaultvalue="Authoriztion Class" rb="app.cib.resource.sys.corp_user"/></td>
      <td class="content1"><set:data name="authLevel" rb="app.cib.resource.common.auth_level" /></td>
    </tr>
    <tr class="">
      <td class="label1"><set:label name="User_Group" defaultvalue="User Group" rb="app.cib.resource.sys.corp_user"/></td>
      <td class="content1"><set:data name="groupId" db="groupByCorp"/></td>
    </tr>
    <set:if name="roleId" value="4" condition="notequals">
      <set:if name="roleId" value="3" condition="notequals">
        <tr class="greyline">
          <td class="label1"><set:label name="view_all_trans" rb="app.cib.resource.sys.corp_user"/></td>
          <td class="content1"><set:data name="viewAllTransFlag" rb="app.cib.resource.sys.view_all_trans_flag"/></td>
        </tr>
      </set:if>
    </set:if>
	 <tr class="">
       <td class="label1"><set:label name="merchantType"/></td>
       <td class="content1">
	   <table id="mt">
				   <tr>
				   <td><set:label name="Cage"/></td>
				   <td><select id="cage" name="cage" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="cage"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="FB"/></td>
				   <td><select id="fb" name="fb" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="fb"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="Ticketing"/> </td>
				   <td><select id="ticketing" name="ticketing" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="ticketing"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="Hotel"/></td>
				   <td><select id="hotel" name="hotel" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="hotel"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="Retail"/></td>
				   <td><select id="retail" name="retail" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="retail"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="General"/></td>
				   <td><select id="general" name="general" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="general"/> </set:rblist>
                   </select></td>
				   </tr>
				   </table>
	   </td>
     </tr>
    <set:if name="allowFinancialController" value="Y" condition="equals">
      <set:if name="roleId" value="2" condition="equals">
        <tr class="">
          <td class="label1"><set:label name="financial_controller" rb="app.cib.resource.sys.corp_user"/></td>
          <td class="content1"><set:data name="financialControllerFlag" rb="app.cib.resource.sys.view_all_trans_flag"/></td>
        </tr>
      </set:if>
    </set:if>
    <set:if name="showOperator" condition="EQUALS" value="Y" >
      <tr>
        <td colspan="2" class="groupinput"><set:label name="Transaction_Limits" defaultvalue="User's Transfer Limits" rb="app.cib.resource.sys.corp_user" /></td>
      </tr>
      <!-- /* Modify by long_zg 2019-05-30 UAT6-475 COB 企業網銀顯示錯誤 begin */ -->
      <!-- <tr >
        <td class="label1"><set:label name="Own_Accounts" defaultvalue="Online Own Accounts" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1"><set:data name='limit1' format='amount'/></td>
      </tr>
      <tr class="greyline" >
        <td class="label1"><set:label name="Accounts_BANK" defaultvalue="Accounts in BANK" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1"><set:data name="limit2" format="amount"/></td>
      </tr>
      <tr >
        <td class="label1"><set:label name="Accounts_Macau" defaultvalue="Accounts in Other Banks in Macau" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1"><set:data name="limit3" format="amount"/></td>
      </tr>
      <tr class="greyline" >
        <td class="label1"><set:label name="Accounts_Overseas" defaultvalue="Accounts in Banks Overseas" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1"><set:data name="limit4" format="amount"/></td>
      </tr>
       -->
       <tr >
        <td class="label1"><set:label name="TO_MY_ACCOUNT" defaultvalue="To My Account" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1"><set:data name='limit1' format='amount'/></td>
      </tr>
      <tr class="greyline" >
        <td class="label1"><set:label name="TO_MDB_BANK_ACCOUNT" defaultvalue="To MDB Account" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1"><set:data name="limit2" format="amount"/></td>
      </tr>
      <tr >
        <td class="label1"><set:label name="TO_LOCAL_BANK_ACCOUNT" defaultvalue="To Local Bank Account" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1"><set:data name="limit3" format="amount"/></td>
      </tr>
      <tr class="greyline" >
        <td class="label1"><set:label name="TO_OVERSEAS_BANK_ACCOUNT" defaultvalue="To Overseas Bank Account" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1"><set:data name="limit4" format="amount"/></td>
      </tr>
      <!-- /* Modify by long_zg 2019-05-30 UAT6-475 COB 企業網銀顯示錯誤 end */ -->
      <!--<tr >
        <td class="label1"><set:label name="Corp_Fund_Alloc" defaultvalue="Corporation Fund Allocation" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1"><set:data name="limit5" format="amount"/></td>
      </tr>
      --><!--<tr class="greyline">
        <td class="label1"><set:label name="Bill_Payment" defaultvalue="Bill Payment" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1"><set:data name="limit6" format="amount"/></td>
      </tr>
      --><tr  >
        <td class="label1"><set:label name="Time_Deposit" defaultvalue="Time Deposit" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1"><set:data name="limit7" format="amount"/></td>
      </tr>
      <!--<tr class="greyline">
        <td class="label1" width="28%"><set:label name="Payroll" defaultvalue="Payroll" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1" width="72%"><set:data name='limit8' format="amount"/></td>
      </tr>
      <tr class="">
        <td class="label1" width="28%"><set:label name="Bank_Draft" defaultvalue="Bank Draft" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1" width="72%"><set:data name='limit9' format="amount"/></td>
      </tr>
      <tr class="greyline">
        <td class="label1" width="28%"><set:label name="Cashier_Order" defaultvalue="Cashier Order" rb="app.cib.resource.sys.corp_user"/></td>
        <td class="content1" width="72%"><set:data name='limit10' format="amount"/></td>
      </tr>
    --></set:if>
    <set:if name="showApprover" condition="EQUALS" value="Y" > </set:if>
  </table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
