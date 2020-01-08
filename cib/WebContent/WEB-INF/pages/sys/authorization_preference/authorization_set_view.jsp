<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.sys.auth_pref">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Corp_Info" defaultvalue="Confirmation"/></td>
              </tr>
              <tr >
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
                <td width="28%" class="label1"><set:label name="Txn_Type" defaultvalue="Transaction Type"/></td>
                <td width="72%" class="content1"><set:data name='txnType1' rb="app.cib.resource.common.txn_type"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Currency" defaultvalue="Currency"/></td>
                <td class="content1"><set:data name='currency' db="rcCurrencyCBS"/></td>
              </tr>             
            </table>
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="8" class="groupinput"><set:label name="Auth_Type" defaultvalue="Authorization Type"/></td>
              </tr>
              <tr class="greyline">
                <td class="lable1"><set:label name="Auth_Type" defaultvalue="Authorization Type"/></td>
				<td  colspan="7" class="content1"><set:data name='ruleType' rb="app.cib.resource.sys.auth_pref"/> <set:if name="ruleType" condition="equals" value="0">&nbsp;&nbsp; - &nbsp;&nbsp;<set:data name='singleLevel'/></set:if></td>				
              </tr>
			  <set:if name="ruleType" condition="equals" value="1">
              <tr>
                <td colspan="7" class="lable1"><set:label name="Multi_Class_Matrix" defaultvalue="Authorization Matrix(for Multi Class Authorization):"/>				</td>
              </tr>
              <tr>
                <td class="listheader1"><set:label name="Amount_Range" defaultvalue="Amount Range(MOP)"/></td>
                <td colspan="6" class="listheader1"><set:label name="Number_Approver" defaultvalue="Number of Approver(s)"/></td>
              </tr>
              <tr>
                <td class="listheader1"><set:label name="From" defaultvalue="From"/></td>
                <td class="listheader1"><set:label name="To" defaultvalue="To"/></td>
                <td class="listheader1"><set:label name="Level_A" defaultvalue="Level A"/></td>
                <td class="listheader1"><set:label name="Level_B" defaultvalue="Level B"/></td>
                <td class="listheader1"><set:label name="Level_C" defaultvalue="Level C"/></td>
                <td class="listheader1"><set:label name="Level_D" defaultvalue="Level D"/></td>
                <td class="listheader1"><set:label name="Level_E" defaultvalue="Level E"/></td>
              </tr>
			  <set:list name="ruleList">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td align="right" class="listcontent1"><set:listdata name='fromAmount' format='amount'/></td>
                <td align="right" class="listcontent1"><set:listdata name='toAmount' format='amount'/></td>
                <td align="right" class="listcontent1"><set:listdata name='levelA'/></td>
                <td align="right" class="listcontent1"><set:listdata name='levelB'/></td>
                <td align="right" class="listcontent1"><set:listdata name='levelC'/></td>
                <td align="right" class="listcontent1"><set:listdata name='levelD'/></td>
                <td align="right" class="listcontent1"><set:listdata name='levelE'/></td>
              </tr>
			  </set:list>
			  </set:if>
  </table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
