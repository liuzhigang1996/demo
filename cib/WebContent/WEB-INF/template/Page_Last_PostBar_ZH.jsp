<tr><td colspan="20" class="pageline">
<SCRIPT language=JavaScript>
function doPrevPage() {
document.getElementById("ActionMethod").value="execute";
document.getElementById("PAGE_ACTION_TYPE").value="PREV";
document.form1.action="/cib/PageProcess1.do";
document.form1.submit();
}
function doNextPage() {
document.getElementById("ActionMethod").value="execute";
document.getElementById("PAGE_ACTION_TYPE").value="NEXT";
document.form1.action="/cib/PageProcess1.do";
document.form1.submit();
}
</SCRIPT>
<div align="right"> 
<span class="pagetext"><%total%> <%TOTAL_RECORD_NO_SHOW%> <%records%></span> |
<span class="pagetext"><%the%> <%CURRENT_PAGE_NO_SHOW%> <%pageOf%>  <%all%> <%TOTAL_PAGE_NO_SHOW%> <%pageOfEnd%></span> |
<!-- add by linrui 20190912 -->
<span class="pagelink">
<a href="/cib/PageProcess1.do?PAGE_ACTION_TYPE=MOVETO&MOVE_TO_PAGE_NO=1&CURRENT_PAGE_NO=<%CURRENT_PAGE_NO%>&PAGE_JSP=<%PAGE_JSP%>"> &lt;&lt;<%firstPage%></a>
</span>
<!-- end --> 
<span class="pagelink">
<a href="#" onclick="doPrevPage()"> &lt;<%previous_page%></a></span> | 
<span class="pagedisabled"> <%next_page%>&gt; </span> | 
<!-- add by linrui 20190912 -->
<span class="pagedisabled">
 <%lastPage%>&gt;&gt;
</span>
<!-- end -->
<input id="PAGE_ACTION_TYPE" name="PAGE_ACTION_TYPE" type="hidden" value ="" />
<input id="CURRENT_PAGE_NO" name="CURRENT_PAGE_NO" type="hidden" value ="<%CURRENT_PAGE_NO%>" />
<input id="PAGE_JSP" name="PAGE_JSP" type="hidden" value ="<%PAGE_JSP%>" />
<input id="PAGE_CLASS" name="PAGE_CLASS" type="hidden" value ="<%PAGE_CLASS%>" />
</div>
</td></tr>
