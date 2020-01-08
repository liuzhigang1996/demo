<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.flow.approve">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsProgressBarHandler.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}

var checkoutWorksSize = parseInt('<set:data name='checkoutWorksSize'/>'); //works sum
var doneCount = 0;  //works count for which has been processed.
var startTime = new Date().getTime();  //

		// <![CDATA[

			if (!JS_BRAMUS) { var JS_BRAMUS = new Object(); }

			JS_BRAMUS.jsProgressBarAjaxHandler = Class.create();

			JS_BRAMUS.jsProgressBarAjaxHandler.prototype = {

				activeRequestCount			: parseInt(checkoutWorksSize),//0
				totalRequestCount			: parseInt(checkoutWorksSize),//0

				initialize					: function() {

					// Register Ajax Responders
						Ajax.Responders.register({
							onCreate: function() {
							    overTimeCheck();
							    //this.activeRequestCount--;
								//alert( this.activeRequestCount);
								//this.activeRequestCount++;
								//this.totalRequestCount++;
							}.bind(this),
							onComplete: function() {
								/*this.activeRequestCount--;
								myJsProgressBarHandler.setPercentage(
									'progress',
									parseInt((this.totalRequestCount - this.activeRequestCount) / this.totalRequestCount * 100)
								);
								if (this.activeRequestCount == 0) { alert("All Done!");} */
							}.bind(this)
						});
						
						//Init myJsProgressBarHandler here
						myJsProgressBarHandler = new JS_BRAMUS.jsProgressBarHandler();
						
						//Begin....			
						populateElement();

				}
			}

			function initProgressBarAjaxHandler() { myJsProgressBarAjaxHandler = new JS_BRAMUS.jsProgressBarAjaxHandler(); }
			Event.observe(window, 'load', initProgressBarAjaxHandler, false);

		// ]]>
		
		function doSubmit(){
           setFormDisabled("form1");
           document.form1.submit();
        }
		
		function populateElement(){
		
		   ajaxGo("/cib/jsax?serviceName=MultiApprovePbarService" + '&language=' + language,false);
		   
		   var parPercent = parseInt($("parPercent").innerHTML);
		   
		   //alert($("parPercent").innerHTML);
		   setTimeout(function(){ setPercentage(parPercent); }, 100);

		   if(parPercent==100){
		       //all works finished, submit to next page
			   setTimeout(function (){ doSubmit(); },1000);    
		   }else{
		       setTimeout(function (){ populateElement(); },2000); 
		   }
		   
        }

        function setPercentage(parPercent) {// add by li_zd at 20170728
        	myJsProgressBarHandler.setPercentage(
					'progress',
					parPercent
			)
        }
		
       function overTimeCheck()  {  
          var endTime = new Date().getTime();  
           if(endTime - startTime >600000) {   //600000
		       //when process time over 10 mins, submit to next page
               setTimeout(function (){ doSubmit(); },500);
           }   
       } 


</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/approve.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.flow.approve" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
        <set:label name="navigationTitle_multilist" defaultvalue="MDB Corporate Online Banking > Authorization > Multi Approval List"/>
        <!-- InstanceEndEditable --></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#" onClick="printPage('pageheader');"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
	<!--
    <td class="buttonhelp"><a href="#"><img src="/cib/images/shim.gif" width="36" height="18" border="0"></a></td>
	-->
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="EDC64A">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="white">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="40">
  <tr>
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" -->
        <set:label name="functionTitle_multilist" defaultvalue="MULTI APPROVE LIST"/>
        <!-- InstanceEndEditable --></td>
    <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" bgcolor="white"><img src="/cib/images/shim.gif" width="1" height="1"></td>
        </tr>
      </table></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="19" class="topborderlong"><img src="/cib/images/table_top_long.gif" width="100%" height="19"></td>
  </tr>
  <tr>
    <td align="center" bgcolor="#999999"><table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
        <tr>
          <td width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
          <td>
		  <form action="/cib/approve.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->

			</div>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td>
						<set:messages width="100%" cols="1" align="center"/>
						</td>
                      </tr>
                </table>
				<table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td colspan="5" class=""><p><set:data name='progressNote'/>&nbsp;&nbsp;</p><p><span class="progressBar percentImage1" id="progress">0%</span></p></td>
                  </tr>
                  <tr>
                    <td colspan="5" class="">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="listheader1"><set:label name="Txn_Type" defaultvalue="Type"/></td>
                    <td class="listheader1"><set:label name="Trans_Desc" defaultvalue="Description"/></td>
                    <set:if name= "FinanceFlag" condition="equals" value = "Y">
					<td align="center" class="listheader1"><set:label name="Currency" defaultvalue="CCY"/></td>
                    <td align="right" class="listheader1"><set:label name="Amount" defaultvalue="Amount"/></td>
                    </set:if>
					<td align="right" class="listheader1">&nbsp;</td>
				  </tr>
                  <set:list name="checkoutWorks">
                    <tr class="<set:listclass class1='' class2='greyline' />">
                      <td class="listcontent1"><set:listdata name="txnType" rb="app.cib.resource.common.subtype"/>                  </td>
                      <td class="listcontent1"><set:listdata name="transDesc"/></td>
					  <set:if name= "FinanceFlag" condition="equals" value = "Y">
                      <td align="center" class="listcontent1">
					  <set:listif name="ruleFlag" condition="equals" value="0">	  
					  <set:listdata name="currency" db="rcCurrencyCBS"/>
				</set:listif>
<set:listif name="ruleFlag" condition="equals" value="1">
 <set:listdata name="toCurrency" db="rcCurrencyCBS"/>
 				</set:listif>
                        &nbsp;</td>
				<td align="right" class="listcontent1">
				
                <set:listif name="txnType" condition="equals" value="AUTOPAY_ADD">
                    <set:listif name='amount' condition='equals' value='9.999999999E9'>
                        <set:label name="FULL_PAYMENT" defaultvalue="Full Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                    </set:listif>
                    <set:listif name='amount' condition='notequals' value='9.999999999E9'>
                        <set:listif name='amount' condition='equals' value='0.0'>
                            <set:label name="Minimum_Payment" defaultvalue="Minimum Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                        </set:listif>
                        <set:listif name='amount' condition='notequals' value='0.0'>
                            <set:listdata name='amount' format='amount' />
                        </set:listif>
                    </set:listif>
                </set:listif>
                <set:listif name="txnType" condition="equals" value="AUTOPAY_EDIT">
                    <set:listif name='amount' condition='equals' value='9.999999999E9'>
                        <set:label name="FULL_PAYMENT" defaultvalue="Full Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                    </set:listif>
                    <set:listif name='amount' condition='notequals' value='9.999999999E9'>
                        <set:listif name='amount' condition='equals' value='0.0'>
                            <set:label name="Minimum_Payment" defaultvalue="Minimum Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                        </set:listif>
                        <set:listif name='amount' condition='notequals' value='0.0'>
                            <set:listdata name='amount' format='amount' />
                        </set:listif>
                    </set:listif>
                </set:listif>
                <set:listif name="txnType" condition="equals" value="AUTOPAY_DELETE">
                    <set:listif name='amount' condition='equals' value='9.999999999E9'>
                        <set:label name="FULL_PAYMENT" defaultvalue="Full Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                    </set:listif>
                    <set:listif name='amount' condition='notequals' value='9.999999999E9'>
                        <set:listif name='amount' condition='equals' value='0.0'>
                            <set:label name="Minimum_Payment" defaultvalue="Minimum Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                        </set:listif>
                        <set:listif name='amount' condition='notequals' value='0.0'>
                            <set:listdata name='amount' format='amount' />
                        </set:listif>
                    </set:listif>
                </set:listif>
                <set:listif name="txnType" condition="notequals" value="AUTOPAY_ADD">
	                <set:listif name="txnType" condition="notequals" value="AUTOPAY_EDIT">
		                <set:listif name="txnType" condition="notequals" value="AUTOPAY_DELETE">
			                <set:listif name="txnType" condition="equals" value="CHEQUE_BOOK_REQUEST"></set:listif>
			                <set:listif name="txnType" condition="notequals" value="CHEQUE_BOOK_REQUEST">
			                	<set:listif name="ruleFlag" condition="equals" value="0"><set:listdata name="amount" format='amount'/></set:listif>
			                	<set:listif name="ruleFlag" condition="equals" value="1"><set:listdata name="toAmount" format='amount'/></set:listif>
			                </set:listif>
		                </set:listif>
	                </set:listif>
                </set:listif>
                </td>
					 
					  </set:if>
					  
					   <td align="right" class="listcontent1"><p id="bp_<set:listdata name='workId'/>"> <set:data name='taskWaiting'/></p></td>
                    </tr>
                  </set:list>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td height="40" class="sectionbutton">
					<!--<input id="next" name="next" type="button" value="next" onClick="doSubmit()">-->
					
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="multiApproveShowResult">
					  <div id="parPercent" style="display:none">0</div>
					
                   </td>
                  </tr>
                </table>
                <!-- InstanceEndEditable -->
		  </form>
		  </td>
          <td width="1%"><img src="/cib/images/shim.gif" width="10" height="1"></td>
      </table></td>
    <td align="right" width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="999999">
    <td><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="white">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="2"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>
</body>
</set:loadrb>
<!-- InstanceEnd --></html>
