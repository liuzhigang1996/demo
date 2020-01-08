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
	  STATEMENT ON DATA PROTECTION
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
  		STATEMENT ON DATA PROTECTION
  	</div>
  	<div class = "main">
	  	<div class = "title_left">
	  		<div style="height: 15%">&nbsp;</div>
	  		<ul style = "list-style:none;margin:0px 0px;">
	  			<li><div class = "title_left_1" id = "title_1"><a class = "link" href = "javascript:void(0);" onclick="goTop()">Legal Basis</a></div></li>
	  			<li><div class = "title_left_2" id = "title_2"><a class = "link" href = "#Data_Collection_Purposes" onclick="changeStyle('title_2')">Data Collection and Purposes</a></div></li>
	  			<li><div class = "title_left_2" id = "title_3"><a class = "link" href = "#Data_Processing_Transferring" onclick="changeStyle('title_3')">Data Processing and Transferring</a></div></li>
	  			<li><div class = "title_left_2" id = "title_4"><a class = "link" href = "#Data_Subject_rights" onclick="changeStyle('title_4')">The Data Subject's rights</a></div></li>
	  			<li><div class = "title_left_2" id = "title_5"><a class = "link" href = "#Security_Protection" onclick="changeStyle('title_5')">Security Protection of Data</a></div></li>
	  			<li><div class = "title_left_2" id = "title_6"><a class = "link" href = "#Others" onclick="changeStyle('title_6')">Others</a></div></li>
	  		</ul>
	  	</div>
	  	<div class = "content">
	  		<div id = "Legal_Basis">
	  			<div class = "content_title">1. Legal Basis</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">Macao Development Bank Limited (“the Bank”) strictly abides by the duty to maintain secrecy and obligations of personal data protection which are stipulated  in the Financial System Act (Decree No. 32/93/M) of the Macao SAR, the Law on Protection of Personal Data in No. 8/2005 and other relevant laws and regulations, and strictly protects the data of the Data Subjects.</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Data_Collection_Purposes">
	  			<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">2. Data Collection and Purposes of Processing</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1"><b>(A) Data of the Bank’s Clients</b></div></li>
	  				</ol>
	  				<ol style = "list-style-type: none;">
	  					<li>
	  						<div class = "content_main_1"><b>The Bank collects Client data as follows:</b></div>
	  					</li>
	  					<li>
	  						<ol style = "list-style-type: none;">
	  							<li><div class = "content_main_1">(1) Identification or company registration documents;</div></li>
	  							<li><div class = "content_main_1">(2) Proof of address;</div></li>
	  							<li><div class = "content_main_1">(3) Various types of financial data;</div></li>
	  							<li><div class = "content_main_1">(4) User registration data and identity authentication information;</div></li>
	  							<li><div class = "content_main_1">(5) Asset documents such as property and transportation;</div></li>
	  							<li><div class = "content_main_1">(6) Transaction records and login logs;</div></li>
	  							<li><div class = "content_main_1">(7) Documents, pictures, audio and video, digital certificates, computer programs and other documents containing the Client's personal data;</div></li>
	  							<li><div class = "content_main_1">(8) Other data collected in accordance with legal requirements or with the consent of the client;</div></li>
	  						</ol>
	  					</li>
	  					<li>
	  						<div class = "content_main_1"><b>The purpose and use of the Bank's processing of Client data includes:</b></div>
	  						<ol style = "list-style-type: none;">
	  							<li><div class = "content_main_1">(1) To establish, maintain, evaluate, manage, improve and execute the financial services or credit facilities provided by the Bank to Clients.</div></li>
	  							<li><div class = "content_main_1">(2) To ensure that the Client maintains a reliable credit and performs credit checks as appropriate.</div></li>
	  							<li><div class = "content_main_1">(3) To send promotional information such as products and services to Clients.</div></li>
	  							<li><div class = "content_main_1">(4) The use of the necessary information to defend the acts practiced under the terms of the powers conferred by the Financial System Act and which are the object of any administrative appeal and/or related proceedings.</div></li>
	  							<li><div class = "content_main_1">(5) The use of the information by the Bank or the attorneys of the Bank in order to perform any necessary measures to recover the overdue payment of the non-performing clients.</div></li>
	  							<li><div class = "content_main_1">(6) The possibility of the Bank assigning their credits or entrusting the respective collection to third parties who, in turn, are also subject to secrecy.</div></li>
	  							<li><div class = "content_main_1">(7) The prudent use of information required for technical advice.</div></li>
	  							<li><div class = "content_main_1">(8) The Bank makes disclosures in compliance with sanctions or prevention or detection of money laundering, terrorist fundraising or other illegal activities.</div></li>
	  							<li><div class = "content_main_1">(9) Any other disclosure in order to comply with legal requirements or regulatory.</div></li>
	  							<li><div class = "content_main_1">(10) Uses or related uses as described above.</div></li>
	  						</ol>
	  					</li>
	  				</ol>
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1"><b>(B) Data of Job Applicants</b></div></li>
	  					<li>
	  						<div class = "content_main_1">For the purpose of recruitment, the following data is collected from job applicants:</div>
	  						<ol style = "list-style-type: none;">
	  							<li><div class = "content_main_1">(1) Identity identification information: name, date of birth, place of birth, gender, nationality, address, telephone number, email address, education, common language, type of identification document, number and copy, etc.;</div></li>
	  							<li><div class = "content_main_1">(2) Resume information;</div></li>
	  							<li><div class = "content_main_1">(3) Other information collected in accordance with the law or the consent of the job applicants.</div></li>
	  						</ol>
	  					</li>
	  				</ol>
	  			</div>
  			</div>
  			<hr style = "margin: 0px 40px" />
  			<div id = "Data_Processing_Transferring">
  				<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">3. Data Processing and Transferring</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">The Data Subject acknowledges and agrees that the Bank may, for appropriate purpose, use, process and store Data Subject's data in or outside Macau and/or transfer or disclose such data to other institutions or individuals (including but not limited to regulatory agencies, judicial authorities, public entities; other credit institutions, exchanges, clearing houses; contract counterparts; persons with confidentiality obligations; external auditors, legal counsel, service providers; drawers and transferee, etc.) in or outside Macau, with cautious and reasonable measures to protect the confidentiality and security of the personal data of the Data Subject, in accordance with the Financial System Act of Macau (Decree No. 32/93/M), the Law on Protection of Personal Data in No. 8/2005 and other related legislations.</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Data_Subject_rights">
  				<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">4. The Data Subject's rights</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">(a) Knowing whether his/her information has been kept by the Bank and, if so, the type and source of the information;</div></li>
						<li><div class = "content_main_1">(b) Inquiring about the information processed by the Bank, requiring the Bank to modify and delete the incomplete or inaccurate information;</div></li>
						<li><div class = "content_main_1">(c) Knowing what information will be disclosed to other third parties or institutions (such as credit reference agencies or debt collection agencies);</div></li>
						<li><div class = "content_main_1">(d) Transmitting to the third party or institution through the Bank a request to inquire, modify or delete their information; and</div></li>
						<li><div class = "content_main_1">(e) Requiring the Bank to provide contact details of the above-mentioned third party or institution that has received personal data disclosed by the Bank.</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Security_Protection">
  				<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">5. Security Protection of Data</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">The Bank will retain Data Subject data until such data is no longer needed for those purposes when collected or exceeds the statutory retention period. The Bank uses secure technology, privacy protection management measures, and restricts that only authorized employees can access or process Data Subject data, only to protect the privacy of Data Subjects.</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Others">
  				<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">6. Others</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">In the event of any inconsistency between the Chinese version and English version of this Statement, the Chinese version shall prevail.</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div style = "height: 440px">&nbsp;</div>
  		</div>
  	</div>
  </body>
  </set:loadrb>
</html>
