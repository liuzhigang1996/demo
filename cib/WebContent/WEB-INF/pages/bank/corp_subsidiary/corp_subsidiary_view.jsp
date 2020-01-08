<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.bnk.corp_subsidiary">
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td colspan="2" class="groupinput"><set:label name="Corp_Info" defaultvalue="Corporation Information"/></td>
                  </tr>
                  <tr>
                    <td width="28%" class="label1"><set:label name="Corp_Id" defaultvalue="corp_Id"/></td>
                    <td width="72%" class="content1"><set:data name='corpId'/></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Corp_Name" defaultvalue="corp_name"/></td>
                    <td class="content1"><set:data name='corpName'/></td>
                  </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td colspan="3" class="groupinput"><set:label name="Subsidiaries" defaultvalue="Subsidiaries"/>
					</td>
                  </tr>
                  <tr>
                    <td class="listheader1"><set:label name="Corp_Id" defaultvalue="Corporations ID"/></td>
                    <td class="listheader1"><set:label name="Corp_Name" defaultvalue="Corporations Name"/></td>
                    <td class="listheader1"><set:label name="Status" defaultvalue="Status"/></td>
                  </tr>
                  <set:list name="subsidiaryList" showNoRecord="YES">
                    <tr>
                      <td class="listcontent1"><set:listdata name="corpId"/></td>
                      <td class="listcontent1"><set:listdata name="corpName"/></td>
                      <td class="listcontent1"><set:listdata name="status"  rb="app.cib.resource.common.status"/></td>
                    </tr>
                  </set:list>
                </table>
				</set:loadrb>
                <!-- InstanceEndEditable --><!-- InstanceEnd -->
