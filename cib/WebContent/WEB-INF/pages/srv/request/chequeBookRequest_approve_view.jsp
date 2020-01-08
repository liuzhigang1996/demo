<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="payCurrency|accountNo|noOfBook|pickupBranchCode">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
    <tr>
    <td class="label1" width="28%"><set:label name="Current_Account" defaultvalue="Current Account" rb='app.cib.resource.srv.chequeBookRequest'/></td>
    <td class="content1" width="72%">
    <set:data name='payCurrency' db="rcCurrencyCBS"/> - <set:data name='accountNo' />
    </td>
  </tr>   
   <tr>
    <td class="label1"><set:label name="No_Of_Books" defaultvalue="No. Of Books" rb='app.cib.resource.srv.chequeBookRequest' /></td>
    <td class="content1"><set:data name='noOfBook' format='amount' pattern='#' /></td>
  </tr>
  <!-- add by lzg 20190606 -->
   <tr>
      <td class="label1" ><set:label name="Pickup_Type_Label" defaultvalue="Pickup Type" rb = "app.cib.resource.srv.chequeBookRequest"/></td>
      <td class="content1"><set:data  name='pickupType' rb = "app.cib.resource.srv.pick_up_type" /></td>
    </tr>
   <!-- add by lzg end -->
   <!-- add by lzg 20190624 for pwc-->
	<set:singlesubmit/>
  <!-- add by lzg end -->
  <set:if name="pickupType" condition="equals" value="S">
  <tr>
    <td class="label1"><set:label name="Local_Mail_Name" defaultvalue="Name" rb="app.cib.resource.srv.chequeBookRequest"/></td>
    <td class="content1">
                    
                    <set:data name='packageName'/>
                    
                    
    </td>
  </tr>
  <tr>
    <td class="label1"><set:label name="Local_Mail_Adress" defaultvalue="Adress" rb="app.cib.resource.srv.chequeBookRequest"/></td>
    <td class="content1">
                   
                    <set:data name='packageAdress'/>
                   
                   
    </td>
   </tr>
   </set:if>
  
  <%--
  <tr>
    <td class="label1"><set:label name="Pickup_Branch" defaultvalue="Pickup Branch" rb='app.cib.resource.srv.chequeBookRequest' /></td>
    <td class="content1">
    <set:data name='pickupBranchCode' db='branch' />
    </td>
  </tr>
--%></table>



<!-- InstanceEndEditable -->
<!-- InstanceEnd -->