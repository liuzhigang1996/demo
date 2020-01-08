<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
  <set:loadrb file="app.cib.resource.sys.login" alias="">
  <head>
  		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  	  	<meta http-equiv="Cache-Control" content="no-cache">
  		<meta http-equiv="Pragma" content="no-cache">
  		<meta http-equiv="Expires" content="0">
  		<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8;IE=7; IE=EDGE">
	  <title>
	  IMPORTANT STATEMENT
	  </title>
  	  <script src="/cib/javascript/baidu_jquery.js"></script>
    <script>
    $(function(){
        $('a[href*=#],area[href*=#]').click(function() {
            console.log(this.pathname)
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
                var $target = $(this.hash);
                $target = $target.length && $target || $('[name=' + this.hash.slice(1) + ']');
                if ($target.length) {
                    var targetOffset = $target.position().top;
                    $('html,body').animate({
                                scrollTop: targetOffset
                            },
                            1000);
                    return false;
                }
            }
        });
    })
    </script>
	  
	  <script type="text/javascript">
	  	function goTop(){
	        var x=document.body.scrollTop||document.documentElement.scrollTop;
	        var timer=setInterval(function(){
	            x=x-100;
	            if(x<100)
	            {
	                x=0;
	                window.scrollTo(x,x);
	                clearInterval(timer);
	            }
	            window.scrollTo(x,x);
	        },"10");
	        changeStyle('title_1');
	  	}
	  	var currentDomId = "";
	  	function changeStyle(domId){
	  		if(currentDomId == ""){
	  			var targetDom = document.getElementById(domId);
	  			targetDom.style.borderRight = "5px solid #0052D9";
	  			targetDom.getElementsByTagName("a")[0].style.color = "#0052D9";
	  			currentDomId = domId;
	  		}else if(currentDomId != domId){
	  			var targetDom = document.getElementById(domId);
	  			var currentDom = document.getElementById(currentDomId);
	  			targetDom.style.borderRight = "5px solid #0052D9";
	  			targetDom.getElementsByTagName("a")[0].style.color = "#0052D9";
	  			currentDom.style.borderRight = "none";
	  			currentDom.getElementsByTagName("a")[0].style.color = "#666666";
	  			currentDomId = domId;
	  		} 
	  	}
	  </script>
  	 <link href="/cib/css/mdb_limited.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  
  	<div class = "headline">
  		IMPORTANT STATEMENT
  	</div>
  	<div class = "main">
	  	<div class = "title_left">
	  		<div style="height: 15%">&nbsp;</div>
	  		<ul style = "list-style:none;margin:0px 0px;">
	  			<li><div class = "title_left_1" id = "title_1"><a class = "link" href = "javascript:void(0);" onclick="goTop()">Jurisdiction and Restrictions</a></div></li>
	  			<li><div class = "title_left_2" id = "title_2"><a class = "link" href = "#Use_Information" onclick="changeStyle('title_2')">Use of Information</a></div></li>
	  			<li><div class = "title_left_2" id = "title_3"><a class = "link" href = "#System_Failure_Security" onclick="changeStyle('title_3')">System Failure and Security</a></div></li>
	  			<li><div class = "title_left_2" id = "title_4"><a class = "link" href = "#Internet_Communications" onclick="changeStyle('title_4')">Internet Communications</a></div></li>
	  			<li><div class = "title_left_2" id = "title_5"><a class = "link" href = "#Linked_Web_Sites" onclick="changeStyle('title_5')">Linked Web Sites</a></div></li>
	  			<li><div class = "title_left_2" id = "title_6"><a class = "link" href = "#Use_Cookie" onclick="changeStyle('title_6')">Use of Cookie</a></div></li>
	  			<li><div class = "title_left_2" id = "title_7"><a class = "link" href = "#Others" onclick="changeStyle('title_7')">Others</a></div></li>
	  		</ul>
	  	</div>
	  	<div class = "content">
	  		<div class = "content_main">
	  			<div style = "margin: 10px 40px;">Any user who browses the homepage of the website of Macao Development Bank Limited ("the Bank") and any of the pages thereof and/or uses E-Banking services of the Bank shall be aware of and agree to the following terms and conditions (“the Terms”) and modifications from time to time. If you do not accept the Terms, please do not access our website and do not use the E-Banking services of the Bank.</div>
	  		</div>
	  		<div id = "Jurisdiction_Restrictions">
	  			<div class = "content_title">1. Jurisdiction and Restrictions</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">1. All information, material, opinions and services provided by the Bank are directed at and restricted to persons resident in or entities having a place of business in the Macao Special Administrative Region ("Macao") and are not intended for access or use in other jurisdictions.</div></li>
						<li><div class = "content_main_1">2. In certain countries, governmental, legal or regulatory requirements may restrict or prohibit the accessing and /or use of the information, products and services provided by the Bank. It is your responsibility to find out what those restrictions are and observe them.</div></li>
						<li><div class = "content_main_1">3. The Terms are governed law of  Macau and any dispute shall be subject to the jurisdiction of the Macao Courts.</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Use_Information">
	  			<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">2. Use of Information</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. Whilst the Bank have attempted to ensure the accuracy of the information referred in the homepage of our website and any of the pages thereof, the Bank make no guarantee, warranty or representation either expressly or impliedly concerning the accuracy or completeness of the information contained herein. </div></li>
	  					<li><div class = "content_main_1">2. The information is provided for reference only. It should not be treated as a specific advice concerning individual situations. It is strongly recommended that appropriate professional advice should be sought where necessary.</div></li>
	  					<li><div class = "content_main_1">3. Unless otherwise stated, the information will not be deemed to be an offer.</div></li>
	  					<li><div class = "content_main_1">4. Unless otherwise stated, any price, interest rate or other quote is provided to you for reference purposes only, such information may be varied by the Bank without notice prior to the Bank's acceptance of the Client's offer. Unless otherwise stated, the price payable by you does not include the additional sum of applicable taxes, transaction levies, reasonable costs and expenses which should be borne by you.</div></li>
	  					<li><div class = "content_main_1">5. Unauthorized use of the web sites and applications of the Bank including but not limited to unauthorized entry into the Bank's systems, misuse of passwords, or misuse of any information posted on a site is strictly prohibited.</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px"/>
	  		<div id = "System_Failure_Security">
	  			<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">3. System Failure and Security</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. It is your sole responsibility to prevent, safeguard and ensure that no computer virus and destructive attack enters your computer, mobile phone or any other electronic device ("Devices").</div></li>
	  					<li><div class = "content_main_1">2. Whilst the Bank have taken reasonable steps to ensure that our site and applications are free from bugs, defects, viruses and errors, the Bank gives no guarantee and make no warranty or representation on such matters.</div></li>
	  					<li><div class = "content_main_1">3. Under no circumstances shall the Bank be liable for any failure of performance, system, server or connection failure, error, omission, interruption, breach of security, computer virus, malicious code, corruption, delay in operation or transmission, transmission error or unavailability of access in connection with your accessing the Bank’s online services even if the Bank had been advised as to the possibility.</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Internet_Communications">
	  			<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">4. Internet Communications</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. You acknowledge that communications over the internet are not completely reliable and may be subject to delays in transmission or operation, loss or corruption of data.</div></li>
	  					<li><div class = "content_main_1">2. The Bank shall take no responsibility for any direct, indirect, consequential or special loss or damages whatsoever arising in connection with using any information contained on Website and applications of the Bank or in connection with sending messages to or receiving messages from us.</div></li>
					</ol>
		  		</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Linked_Web_Sites">
	  			<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">5. Linked Web Sites</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. Any hyperlinks created and linked in any form to the Website of the Bank is subject to the prior written approval from the Bank. The Bank may, at its absolute discretion rescind any approval granted and require the removal of any link to our Website at any time.</div></li>
	  					<li><div class = "content_main_1">2. The setup of hyperlink does not represent any form of our approval or endorsement of those or cooperation with third parties. The Bank is not responsible for any consequences arising out of or in connection with such links.</div></li>
	  					<li><div class = "content_main_1">3. Any hyperlink to the Bank's Website must be made directly to the main page of our website while "framing" or "deep-linking" of the Bank's Website or content is prohibited.</div></li>
					</ol>
		  		</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Use_Cookie">
	  			<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">6. Use of Cookie</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. To assist in our understanding of your interest in our Website and applications, and to store and maintain user preferences, the Bank will collect information about how you use your Devices that takes you online to browse our Website and applications by using cookies and similar technologies.</div></li>
	  					<li><div class = "content_main_1">2. The cookie does not contain the user's name, address, phone number, email address, or anything that personally identifies the user. No personally identifiable information about you is collected or shared with any other third parties. </div></li>
	  					<li><div class = "content_main_1">3. Currently the Bank does not provide functions to opt out cookie tracking. The reason is all opt out settings for cookies tracking will become invalid if you delete or clear your cookies, or if you change the web browser you are using. To avoid confusion, if you do not want us to collect data from your browser, the Bank suggest you configure your privacy settings within the browser. Please note that if you block all cookies entirely, our Website and applications may not function properly.</div></li>
					</ol>
		  		</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Others">
	  			<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">7. Others</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. Any information, products or services supplied by us may be withdrawn or amended at any time without advance notice at the Bank's discretion. Anyone's eligibility for particular information, products and services is subject to the Bank's final determination and absolute discretion.</div></li>
	  					<li><div class = "content_main_1">2. Your continued use of our Website or applications following the posting of any changes to the Terms signifies your acceptance of those changes.</div></li>
	  					<li><div class = "content_main_1">3. In the event of any inconsistency between the Chinese version and English version of the Terms, the Chinese version shall prevail.</div></li>
					</ol>
		  		</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div style = "height: 300px">&nbsp;</div>
	  	</div>
	  </div>
  </body>
  </set:loadrb>
</html>
