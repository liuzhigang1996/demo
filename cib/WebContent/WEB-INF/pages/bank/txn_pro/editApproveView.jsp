<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.bnk.txn_pro">
<link rel="stylesheet" type="text/css" href="/cib/css/txnPro.css">
<SCRIPT language=JavaScript src="/cib/javascript/baidu_jquery.js"></SCRIPT>
<script>
function Load(){
		$('textarea[autoHeight]').autoHeight(); 
}

$(function(){
        $.fn.autoHeight = function(){    
        function autoHeight(elem){
            elem.style.height = 'auto';
            elem.scrollTop = 0; //防抖动
            elem.style.height = elem.scrollHeight + 'px';
        }
        this.each(function(){
            autoHeight(this);
            $(this).on('keyup', function(){
                autoHeight(this);
            });
        });    
    }                
    $('textarea[autoHeight]').autoHeight();    
})
</script>
  <table width="100%" border="0" cellspacing="0" cellpadding="3">
    <%-- <tr>
      <td colspan="2" class="groupinput"><set:label name="edit_description_info" /></td>
    </tr> --%>
    <tr>
    	<td colspan="2">
    		<div>
				<div >
					<div>
						<span class = "myTitle" ><set:label name = '2nd_menu'/></span>
						<span class = "menu_2nd" ><set:data name = "menu2nd" rb="app.cib.resource.bnk.2ndMenu"/></span>
					</div>
					<div>
						<span class = "myTitle" style = "float: left;"><set:label name = 'descriptionC'/></span>
						<div class = "desciption"><textarea readonly="readonly"  autoHeight = "true"  id = "descriptionC" name = "descriptionC" class = "myTextarea" ><set:data name = 'descriptionC' /></textarea></div>
					</div>
				</div>
				<div>
					<div>
						<span class = "myTitle" style = "float: left;"><set:label name = 'descriptionE'/></span>
						<div class = "desciption"><textarea readonly="readonly"  autoHeight = "true"  id = "descriptionE" name = "descriptionE" class = "myTextarea" ><set:data name = 'descriptionE' /></textarea></div>
					</div>
				</div>
				<div style = "clear: both;"></div>
			</div>
    	</td>
    </tr>
  </table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
