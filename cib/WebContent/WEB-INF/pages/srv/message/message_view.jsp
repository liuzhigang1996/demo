<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td width="28%" class="label1"><set:label rb="app.cib.resource.srv.message" name="Message_Title" defaultvalue="Message Title"/></td>
    <td width="72%" class="content1"><set:data name='msgTitle'/></td>
  </tr>
  <tr class="greyline">
    <td width="28%" class="label1"><set:label rb="app.cib.resource.srv.message" name="From_Date" defaultvalue="From Date"/></td>
    <td width="72%" class="content1"><set:data name='fromDate' format='date'/></td>
  </tr>
  <tr>
    <td width="28%" class="label1"><set:label rb="app.cib.resource.srv.message" name="To_Date" defaultvalue="To Date"/></td>
    <td width="72%" class="content1"><set:data name='toDate' format='date'/></td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label rb="app.cib.resource.srv.message" name="Message_Content" defaultvalue="Message Content"/></td>
    <td class="content1"><textarea name="msgContent" cols="40" rows="5" readonly="readonly"><set:data name='msgContent'/>
</textarea></td>
  </tr>
</table>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
