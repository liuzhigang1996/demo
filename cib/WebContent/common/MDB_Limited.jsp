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
	  <set:label name="documentTitle_Limited"/>
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
  		TERMS FOR E-BANKING SERVICE
  	</div>
  	<div class = "main">
  	<div class = "title_left">
  		<div style="height: 15%">&nbsp;</div>
  		<ul style = "list-style:none;margin:0px 0px;">
  			<li><div class = "title_left_1" id = "title_1"><a class = "link" href = "javascript:void(0);" onclick="goTop()">Purpose and Scope</a></div></li>
  			<li><div class = "title_left_2" id = "title_2"><a class = "link" href = "#Definitions" onclick="changeStyle('title_2')">Definitions</a></div></li>
  			<li><div class = "title_left_2" id = "title_3"><a class = "link" href = "#Account_Management" onclick="changeStyle('title_3')">Account Management</a></div></li>
  			<li><div class = "title_left_2" id = "title_4"><a class = "link" href = "#Client_Instructions" onclick="changeStyle('title_4')">the Client's Instructions</a></div></li>
  			<li><div class = "title_left_2" id = "title_5"><a class = "link" href = "#Login" onclick="changeStyle('title_5')">Login</a></div></li>
  			<li><div class = "title_left_2" id = "title_6"><a class = "link" href = "#Biometric_Authentication" onclick="changeStyle('title_6')">Biometric Authentication</a></div></li>
  			<li><div class = "title_left_2" id = "title_7"><a class = "link" href = "#Security_Measures" onclick="changeStyle('title_7')">Security Measures</a></div></li>
  			<li><div class = "title_left_2" id = "title_8"><a class = "link" href = "#Client_Responsibilities" onclick="changeStyle('title_8')">Client Responsibilities</a></div></li>
  			<li><div class = "title_left_2" id = "title_9"><a class = "link" href = "#Encryption_Virus" onclick="changeStyle('title_9')">Encryption and Virus</a></div></li>
  			<li><div class = "title_left_2" id = "title_10"><a class = "link" href = "#service_interruptions" onclick="changeStyle('title_10')">Service Interruptions</a></div></li>
  		</ul>
  	</div>
  	<div class = "content">
  		<div id = "Purpose_Scope">
  			<div class = "content_title">1. Purpose and Scope</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">1.1 Special Terms for E-Banking Services (hereinafter referred to as "the Terms") apply to Macao Development Bank Limited (hereinafter referred to as "the Bank") account holders (hereinafter referred to as "Customer") who use E-Banking services provided by the Bank.</div></li>
  					<li><div class = "content_main_1">1.2 The application of the Terms, without prejudice to "General Terms and Conditions for Accounts and Related Service (the G-T&amp;C)” applies to the Customer. If there is any conflict between the Terms and the G-T&C, the Terms shall prevail.</div></li>
  					<li><div class = "content_main_1">1.3 The English version of the Terms is for reference only and if there is any conflict between the Chinese and English versions, the Chinese version shall prevail.</div></li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px" />
  		<div id = "Definitions">
  			<div style="height: 15%">&nbsp;</div>
  			<div class = "content_title">2. Definitions</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">2.1 Unless otherwise expressly stated, the following words and items in the Terms shall have the meaning as follows:</div></li>
  					<li>
  						<ul style = "list-style-type: none;">
		  					<li><div class = "content_main_1">(a) “Individual Customer” means a natural person who opens an individual account with the Bank and applies for the Bank's E-Banking Services.</div></li>
		  					<li><div class = "content_main_1">(b) “Commercial Customer” means a commercial entity that opens a commercial account with the Bank and applies for the use of E-banking Services provided by the Bank, including but not limited to individual business owners, companies and other legal entities.</div></li>
		  					<li><div class = "content_main_1">(c) “Applicant” means a person whose name and information are completed on the Bank's E-Banking application form.</div></li>
		  					<li><div class = "content_main_1">(d) "E-Banking Services" means that through the Bank's Internet Banking or Mobile Banking services, Customer could use internet devices to check their accounts or conduct banking transactions.</div></li>
		  					<li><div class = "content_main_1">(e) "Internet Banking" means that through the Bank's online banking services, Commercial Customer could use internet devices to check accounts or conduct banking transactions. This service is currently not available to Individual Customer.</div></li>
		  					<li><div class = "content_main_1">(f) "Mobile Banking" means that through the Bank's mobile banking application, Customers could use mobile devices to check accounts or conduct banking transactions. The mobile application is developed, managed and maintained by the Bank from time to time, published through the mainstream platform or the Bank's website, and adapt to mobile devices.</div></li>
		  					<li><div class = "content_main_1">(g) "The Bank's website" refers to the website of which the Bank is liable for the development, management and maintenance, updates from time to time, publishing information, providing online banking services. The website domain name is www.mdb.com.mo.</div></li>
		  					<li><div class = "content_main_1">(h) ” CIF No.” means the identification code or Customer number assigned by the bank to the Customer for accessing the Bank's Internet Banking.</div></li>
		  					<li><div class = "content_main_1">(i) ” Login ID” means the username set by the applicant for the purpose of E-Banking Services.</div></li>
		  					<li><div class = "content_main_1">(j) "Password" means the identifier required by the Applicant to verify his or her Login ID when logging on to the Bank's E-banking.</div></li>
		  					<li><div class = "content_main_1">(k) " One Time Password (OTP)" means the unique digital passwords which are generated by the Bank’s system and sent to the applicant via SMS to their valid mobile phone number registered in the Bank in order to confirm the relevant financial transactions.</div></li>
		  					<li><div class = "content_main_1">(l) "User" means the person(s) who is/ are liable for managing and controlling Internet Banking services on behalf of the Customer, including one or more than one system administrators, operators, approver, executor or other categories provided by the Bank. The CIF No., login ID and password of the User are used for identification and authentication in Internet Banking. The roles and permissions of the users are set by the Bank according to the instructions of Customer.</div></li>
		  					<li><div class = "content_main_1">(m) "Biometric Authentication" refers to authentication technique that uses unique biological characteristics to verify Customer’s identity, including but not limited to facial recognition and fingerprint recognition.</div></li>
	  					</ul>
	  				</li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Account_Management">
  			<div style="height: 15%">&nbsp;</div>
  			<div class = "content_title">3. Account Management</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">3.1. When Individual Customer applies for E-Banking Services, the application is only applicable to the account holder, and the account holder shall conduct account enquiry and transactions only by himself or herself.</div></li>
  					<li><div class = "content_main_1">3.2. The Bank does not accept the account holder grants the rights mentioned in Clause 3.1 to any third parties.</div></li>
  					<li><div class = "content_main_1">3.3 For Internet Banking, the authorized representatives of Commercial Customer should be entitled to, through signing the Bank's application documents, agreements and terms, (i) set, add or delete operators; (ii) set or modify transaction limits, and (iii) delete or add the services or functions.</div></li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Client_Instructions">
  			<div style="height: 15%">&nbsp;</div>
  			<div class = "content_title">4. Client's Instructions</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">4.1. Once apply for E-Banking Services, the Customer has confirmed that they have appropriate equipment and facilities, and agreed to receive electronic messages instead of paper or other means of communicating information.</div></li>
  					<li><div class = "content_main_1">4.2. Electronic messages are treated as written document signed by the person who sent the message, the Customer should not challenge the validity of contracts concluded by electronic messages because of the way it is set.</div></li>
  					<li><div class = "content_main_1">4.3. Customers may use E-Banking Services outside business hours, but the Bank does not guarantee the Customer for an immediate transaction, the request of customers may only be proceeded during normal business hours.</div></li>
  					<li><div class = "content_main_1">4.4. The Customer and the Bank may communicate via email, mobile application, SMS or any other methods. The Customer acknowledges and accepts that the communication conducted in the above manner is at risk of being intercepted, monitored, modified or interfered by third parties. The Bank will not be liable for any loss whatsoever incurred or suffered by the Customer.</div></li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Login">
  			<div style="height: 15%">&nbsp;</div>
  			<div class = "content_title">5. Login</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">5.1 The Customer of Mobile Banking would be granted a temporary login ID and password which could be used to log in to E-Banking Services. The Customer should set his/her own login ID and password when he/she login the Mobile Banking at the first time. Once the Customer's login ID and password are accepted by the Bank's computer system, the temporary login ID and temporary password will become invalid.</div></li>
  					<li><div class = "content_main_1">5.2 After the applicants of Mobile Banking has set their own login ID and password, they can activate their biometric authentications on their mobile devices.</div></li>
  					<li><div class = "content_main_1">5.3 Login ID of Internet Banking Users can be set at the time of applying the E-Banking Services. The Bank will confirm and provide a temporary password which can be used to login E-Banking Services. Once the password is set by the user and accepted by the Bank's computer system, the temporary password will become invalid.</div></li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Biometric_Authentication">
  			<div style="height: 15%">&nbsp;</div>
  			<div class = "content_title">6. Biometric Authentication</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">6.1. Biometric authentication login service is only applicable to the mobile banking service provided by the Bank.</div></li>
  					<li>
	  					<div class = "content_main_1">
	  						6.2. Customers who meet the following conditions can choose to use the mobile banking biometric authentication login service of the Bank:
	  					</div>
	  					<ul style = "list-style-type: none;">
	  						<li><div class = "content_main_1">(a) The mobile device used by the Customer supports the biometric authentication login service (the Bank may adjust the list of eligible mobile device from time to time.);</div></li>
	  						<li><div class = "content_main_1">(b) Customer has registered the Bank's mobile banking service and has downloaded the Bank's mobile banking application on their mobile device;</div></li>
	  						<li><div class = "content_main_1">(c) Customer has activated fingerprints, facial features or other biometric features on his/her mobile device and has stored fingerprints, facial features or other biometrics for this purpose.</div></li>
	  					</ul>
  					</li>
  					<li><div class = "content_main_1">6.3. Customer shall take all reasonable precautions to ensure the security of his/her mobile device and stored fingerprints, facial features or other biometric features, and to prevent the Customer's mobile device from being used by other third parties</div></li>
  					<li><div class = "content_main_1">6.4. Customer understands that when using biometric authentication login service, any fingerprints, facial features or other biometric stored on the customer's mobile device can be used to sign in to the customer's mobile banking service, Customer should make sure not to store biometric of other third parties on their mobile devices, while Customer should bear all the risks involved above by themselves.</div></li>
  					<li><div class = "content_main_1">6.5. Customer understands that biometric authentication is operated using the biometric authentication function on the Customer's mobile device, the bank does not make any comments, warranties or guarantees regarding the security of fingerprint, facial recognition or other biometric authentication functions.</div></li>
  					<li><div class = "content_main_1">6.6. The Bank reserves the right to suspend or cancel biometric authentication login service without prior notice, including but not limited to the knowledge or suspicion of security risk on the Customer's mobile device, biometric characteristic or other password related issues.</div></li>
  					<li><div class = "content_main_1">6.7. The scope of use of biometric authentication is determined by the Bank. Customer understands that if biometric authentication is not available, other authentication methods should be used to login the service as biometric authentication is not the only verification method for mobile banking. The Bank is not liable for any loss incurred or suffered by the Customer when he/she has not use biometric authentication in accordance with the provisions of the Terms or not able to use biometric authentication.</div></li>
  				</ul>
	  		</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Security_Measures">
  			<div style="height: 15%">&nbsp;</div>
  			<div class = "content_title">7. Security Measures</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">7.1. Customer should take all reasonable precautions to ensure cyber safety when using the service through the Internet and prevent information being stolen.</div></li>
  					<li><div class = "content_main_1">7.2. The Bank reserves the right to specify at any time which kind of banking transaction requires the applicant to use the OTP for two-factor verification so that the transaction can be confirmed and accepted by the Bank's computer system.</div></li>
  					<li><div class = "content_main_1">7.3. Customer shall be fully liable for the safety and confidentiality of their temporary login IDs, temporary passwords, CIF No., login IDs and passwords. Customer shall ensure that such information is not disclosed to third parties. Customer should save the CIF No., login ID and password but not in written form.</div></li>
  					<li><div class = "content_main_1">7.4. For security consideration, Customer should change the password used for E-Banking Services from time to time.</div></li>
  					<li><div class = "content_main_1">7.5. If Customer knows or suspects that the temporary login ID, temporary password, login IDs or passwords has been lost or stolen or known by any third parties by any means, the applicant must immediately notify the Bank.</div></li>
  					<li><div class = "content_main_1">7.6. Customer accepts full liability for the security of \OTP and agrees not to disclose the OTP to another person. If Customer suspects there is actual or suspected misconduct of the device used for receiving OTP and/or if the device has been lost or missing, he/she must immediately notify the Bank.</div></li>
  					<li><div class = "content_main_1">7.7. Any instructions given through the use of the Customer’s Temporary Login ID, Temporary Password, CIF No., Login ID, Password, Facial Recognition, Fingerprint Scanning, or OTP shall be valid and conclusively binding upon the Customer. Any confirmation by the Bank that an instruction has been received and/or a transaction has been performed is deemed to be received by the Customer immediately upon its transmission, and cannot be cancelled or changed once it has been issued and accepted by the Bank.</div></li>
  					<li><div class = "content_main_1">7.8.  By applying for the use and using the Services, the Customer is aware that due to unpredictable network congestion, as well as the openness and public nature of the Internet or other circumstances beyond the control of the Bank, delays, interception, attack, hacking by third parties or incomplete transmission may occur and shall acknowledges that, all books and records kept and maintained by the Bank shall provide, for all intents and purposes, final and conclusive evidence of any particular transaction.</div></li>
  					<li><div class = "content_main_1">7.9. The Bank reserves the rights to establish or changes the E-Banking transactions, transfer or payment limits. Customer can login the Bank's E-Banking Services for details.</div></li>
  					<li><div class = "content_main_1">7.10. Customer should ensure to clear all relevant applications, data storage and transaction records before replacing or disposing of equipment in which E-Banking Services have been performed.</div></li>
  				</ul>
	  		</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Client_Responsibilities">
  			<div style="height: 15%">&nbsp;</div>
  			<div class = "content_title">8. Customer Responsibilities</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">8.1. Customer shall ensure that there are sufficient funds or credit lines in the he/ her account(s), in order for the Bank to fulfil any orders and/or instructions through the Services. The Bank shall not be liable for any losses or damages resulting from or arising from any failure or delay in the fulfilment of such orders and/or instructions due to insufficient funds.</div></li>
  					<li><div class = "content_main_1">8.2. Customer must always verify the accurateness, completeness and correctness of all transactions made into his/ her accounts and shall immediately notify the Bank upon identifying any errors or omissions related to transactions conducted or instructions issued through E-Banking Services, including but not limited to any non-requested transaction or any incorrect transaction.</div></li>
  					<li><div class = "content_main_1">8.3. Customer shall ensure that all information provided to the E-Banking Services is accurate, complete and correct. The Bank shall not be liable for any errors or omissions caused in the use of the Service by the Customer.</div></li>
  					<li><div class = "content_main_1">8.4. Customer shall provide to the Bank a valid mobile phone number obtained from local telecommunications service providers in order to receive their password via SMS to conduct banking transactions. When the mobile number is changed or cancelled, Customer shall notify the Bank in written form. The Customer shall be liable for all losses incurred (if any) prior to the receipt of the written notice from the Customer.</div></li>
  					<li><div class = "content_main_1">8.5. If the Customer uses overseas mobile service network, which the telecommunications service providers may not allow Customer to receive password through messages, or the messages might be delayed or take charge to Customers and so on, in those cases, the Bank shall not be liable for all losses incurred.</div></li>
  					<li>
	  					<div class = "content_main_1">
	  						8.6 Customer shall bear all the risks and losses for any of the following acts, and indemnify the Bank for any losses incurred:
	  					</div>
	  					<ul style = "list-style-type: none;">
	  						<li><div class = "content_main_1">(a) arbitrarily modify, translate, adapt, rent, sublicense, transmit or transfer the application or service provided by the Bank, or attempt to obtain the original source code of the Bank by reverse engineering, decompiling or otherwise;</div></li>
	  						<li><div class = "content_main_1">(b) any act of sabotage or attempted to sabotage the security of E-Banking Services (including, but not limited to fishing, hackers, online fraud, spread viruses, Trojan horses, malicious code, scanning, sniffer, ARP spoofing, DDOS and other attacks);</div></li>
	  						<li><div class = "content_main_1">(c) use the E-Banking Services in mobile devices or operating system outside of the scope of the warranty vendor supported or arranged, or that cracked or modified, such as jailbroken iOS or rooted Android system;</div></li>
	  						<li><div class = "content_main_1">(d) not access our Internet banking services or download an updated version of mobile banking application from the Bank's official website or mainstream app stores designated by the Bank;</div></li>
	  						<li><div class = "content_main_1">(e) Any other use of E-Banking services in the form of violation of laws, regulations, industry practices and social morality, or with attempt to affect or damage the interests of the Bank on purposes.</div></li>
	  					</ul>
  					</li>
  					<li><div class = "content_main_1">8.7. The Bank from time to time might prompt Customers that fraudsters may defraud recipients of sensitive information such as E-Banking Services login IDs and passwords through emails or messages linked by fraudulent websites, applications or built-in fake websites in the name of the Bank. If the Customer found similar fraudulent web page or application, Customer should notify the Bank as soon as possible. The Bank may execute additional monitoring measures for security purpose or request for confirmation of transactions in writing or otherwise.</div></li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Encryption_Virus">
  			<div style="height: 15%">&nbsp;</div>
  			<div class = "content_title">9. Encryption and Virus</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">9.1. As access through the Internet which is a public system which the Bank has no control, it is the Customer's duty to make sure that any computer or other electronic device which used to access E-Banking is free from and adequately protected against computer viruses and other destructive or disruptive components.</div></li>
  					<li><div class = "content_main_1">9.2. The Bank will not be liable for any loss of or damage to the Customer or their data, software, computer, computer networks, mobile devices or other equipment caused by the Customer using E-Banking Services, unless such loss or damage is directly and solely caused by the Bank's negligence or deliberate default.</div></li>
  				</ul>
	  		</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "service_interruptions">
  			<div style="height: 15%">&nbsp;</div>
  			<div class = "content_title">10 Service Interruptions</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">10.1. The Bank will promptly notify Customer in a reasonably appropriate manner for routine inspections and upgrades when the Bank is unable to provide all or part of the E-Banking Services.</div></li>
  					<li><div class = "content_main_1">10.2. The Bank may not inform Customer and suspend all or part of E-Banking Services in special circumstances, including but not limited to the security system is suspected to be damaged, or the need of emergency repair.</div></li>
  					<li><div class = "content_main_1">10.3. In the event that the Bank has levied any charge from the Customer which is explicitly for a particular service which is not available, the Bank will reimburse the amount to the Customer. Other than reimbursing any sum as set out above, the Bank will have no further liability to the Customer.</div></li>
  				</ul>
  			</div>
  		</div>
  		<div id = "service_interruptions">
  			<div class = "content_main">
  				<div style = "text-align: right;">(2019/07 Version V1.0)<span style = "padding-right: 25px;">&nbsp;</span></div>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px" />
	  	<div style = "height: 360px">&nbsp;</div>
  	</div>
  	</div>
  </body>
  </set:loadrb>
</html>
