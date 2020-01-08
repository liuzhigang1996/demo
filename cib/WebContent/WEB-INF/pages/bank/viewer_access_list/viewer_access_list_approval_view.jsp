<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.bnk.viewer_access_list">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="label1"><set:label name="Viewer" defaultvalue="Viewer"/></td>
	<td class="content1"><set:data name="viewerName"/></td>
  </tr>
  <tr class="greyline">
    <td valign="top" class="label1"><set:label name="selected_corp" defaultvalue="Selected Corporation(s)"/></td>
    <td width="82%" class="label1"><span class="content1">
      <select name="associatedList" size="15" multiple="multiple" id="select" style="width:300">
        <set:list name="selectedCorpList">
          <set:listoption name="associatedList" value="CORP_ID" text="CORP_NAME"/>
        </set:list>
      </select>
      </span></td>
  </tr>
</table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
