<tr><td colspan="20" class="pageline">
<div align="right"> 
<span class="pagetext"><%total%> <%TOTAL_RECORD_NO_SHOW%> <%records%></span> |
<span class="pagetext"><%the%> <%CURRENT_PAGE_NO_SHOW%> <%pageOf%>  <%all%> <%TOTAL_PAGE_NO_SHOW%> <%pageOfEnd%></span> | 
<!-- add by linrui 20190912 -->
<span class="pagelink">
<a href="/cib/PageProcess1.do?PAGE_ACTION_TYPE=MOVETO&MOVE_TO_PAGE_NO=1&CURRENT_PAGE_NO=<%CURRENT_PAGE_NO%>&PAGE_JSP=<%PAGE_JSP%>"> &lt;&lt;<%firstPage%></a>
</span>
<!-- end -->
<span class="pagelink">
<a href="/cib/PageProcess1.do?PAGE_ACTION_TYPE=PREV&CURRENT_PAGE_NO=<%CURRENT_PAGE_NO%>&PAGE_JSP=<%PAGE_JSP%>"> &lt;<%previous_page%></a></span> | 
<span class="pagedisabled"> <%next_page%>&gt; </span> | 
<!-- add by linrui 20190912 -->
<span class="pagedisabled">
 <%lastPage%>&gt;&gt;
</span>
<!-- end -->
</div>
</td></tr>
