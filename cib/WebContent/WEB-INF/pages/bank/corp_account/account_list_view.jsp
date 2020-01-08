<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td colspan="2" class="groupinput"><set:label name="confirmation" defaultvalue="confirmation" rb="app.cib.resource.bnk.corp_account"/></td>
  </tr>
  <tr>
    <td width="28%" class="label1"><set:label name="corp_Id" defaultvalue="corp_Id" rb="app.cib.resource.bnk.corp_account"/></td>
    <td width="72%" class="content1"><set:data name='corpId'/> </td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="corp_name" defaultvalue="corp_name" rb="app.cib.resource.bnk.corp_account"/></td>
    <td class="content1"><set:data name='corpName'/> </td>
  </tr>
  <tr class="">
    <td class="label1" colspan="2">&nbsp;</td>
  </tr>
  <tr class="">
    <td class="label1" colspan="2"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="49%" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="4" class="listheader1"><div align="center"><set:label name="acc_in_bank_online" defaultvalue="acc_in_bank_online" rb="app.cib.resource.bnk.corp_account"/></div></td>
              </tr>
              <tr>
                <td class="listheader1" align="center"><set:label name="account_no"/>&nbsp;</td>
                <td class="listheader1" colspan="2" align="center"><set:label name="acc_name"/>&nbsp;</td>
                <td class="listheader1" align="center"><set:label name="currency"/>&nbsp;</td>
              </tr>
              <set:list name="onlineAccList">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="content1"><set:listdata name="accountNo"/>&nbsp;</td>
                <td class="listcontent1"><set:listdata name="accountNo" db='accountName'/>&nbsp;</td>
                <td class="listcontent1"><set:listdata name="accountDesc"/>&nbsp;</td>
                <td class="listcontent1"><set:listdata name="currency" db="rcCurrencyCBS"/>&nbsp;</td>
              </tr>
              </set:list>
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td colspan="4"><div align="center"><span class="sectionbutton"> </span></div></td>
              </tr>
            </table></td>
          <td width="3%">&nbsp;</td>
          <td width="48%" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="4" class="listheader1"><div align="center"><set:label name="acc_in_bank_offline" defaultvalue="acc_in_bank_offline" rb="app.cib.resource.bnk.corp_account"/></div></td>
              </tr>
              <tr>
                <td class="listheader1" align="center"><set:label name="account_no"/>&nbsp;</td>
                <td class="listheader1" colspan="2" align="center"><set:label name="acc_name"/>&nbsp;</td>
                <td class="listheader1" align="center"><set:label name="currency"/>&nbsp;</td>
              </tr>
              <set:list name="offlineAccList">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="content1"><set:listdata name="accountNo"/>&nbsp;</td>
                <td class="listcontent1"><set:listdata name="accountNo" db='accountName'/>&nbsp;</td>
                <td class="listcontent1"><set:listdata name="accountDesc"/>&nbsp;</td>
                <td class="listcontent1"><set:listdata name="currency" db="rcCurrencyCBS"/>&nbsp;</td>
              </tr>
              </set:list>
              <tr>
                <td colspan="4"><div align="center"><span class="sectionbutton"> </span></div></td>
              </tr>
            </table></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
      </table></td>
  </tr>
</table>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
