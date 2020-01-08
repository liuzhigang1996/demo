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
	  重要聲明
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
  		重要聲明
  	</div>
  	<div class = "main">
	  	<div class = "title_left">
	  		<div style="height: 15%">&nbsp;</div>
	  		<ul style = "list-style:none;margin:0px 0px;">
	  			<li><div class = "title_left_1" id = "title_1"><a class = "link" href = "javascript:void(0);" onclick="goTop()">司法管轄區及限制</a></div></li>
	  			<li><div class = "title_left_2" id = "title_2"><a class = "link" href = "#Use_Information" onclick="changeStyle('title_2')">資訊使用</a></div></li>
	  			<li><div class = "title_left_2" id = "title_3"><a class = "link" href = "#System_Failure_Security" onclick="changeStyle('title_3')">系統故障及安全</a></div></li>
	  			<li><div class = "title_left_2" id = "title_4"><a class = "link" href = "#Internet_Communications" onclick="changeStyle('title_4')">互聯網通訊</a></div></li>
	  			<li><div class = "title_left_2" id = "title_5"><a class = "link" href = "#Linked_Web_Sites" onclick="changeStyle('title_5')">網址連結</a></div></li>
	  			<li><div class = "title_left_2" id = "title_6"><a class = "link" href = "#other_Linked_Web_Sites" onclick="changeStyle('title_6')">其他網站接入</a></div></li>
	  			<li><div class = "title_left_2" id = "title_7"><a class = "link" href = "#Use_Cookie" onclick="changeStyle('title_7')">Cookie使用</a></div></li>
	  			<li><div class = "title_left_2" id = "title_8"><a class = "link" href = "#Others" onclick="changeStyle('title_8')">其他</a></div></li>
	  		</ul>
	  	</div>
	  	<div class = "content">
	  		<div class = "content_main">
	  			<div style = "margin: 10px 40px;">任何使用者瀏覽澳門發展銀行股份有限公司（“本行”）網站首頁及其中之任何網頁或使用本行電子銀行服務時，即表明知悉及同意以下所列條款及條件（“本條款”）及不時做出的修訂，若閣下不接受本條款，請勿查閱本行網站或使用本行網上服務。</div>
	  		</div>
	  		<div id = "Jurisdiction_Restrictions">
	  			<div class = "content_title">一、司法管轄區及限制</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">1. 本行所有資訊、資料、意見及服務的對象僅限於居住於澳門特別行政區（"澳門"）的個人或營業地點設於澳門的公司，並不擬提供予其他司法管轄區之人士或法人使用。</div></li>
						<li><div class = "content_main_1">2. 在若干國家，政府、法例或監管機關的規定可能限制或禁止進入及／或使用本行提供的資訊、產品和服務。閣下需自行負責瞭解及遵循當中被限制之內容。</div></li>
						<li><div class = "content_main_1">3. 本條款是受澳門法律管轄，任何爭議須受澳門法院管轄。</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Use_Information">
	  			<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">二、資訊使用</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. 本行已盡力確保本行的網站首頁及其中之任何網頁所提供之資料的準確性，但並未就資料的準確性或完整性作出任何明確的或隱含的擔保、保證或聲明。</div></li>
	  					<li><div class = "content_main_1">2. 所有資料只供參考用途，並不應視為對個別情況做出的特定建議。本行建議使用者在有需要時，應徵詢適當的專業人士意見。</div></li>
	  					<li><div class = "content_main_1">3. 除另有說明者外，所有資料不得被視為要約。</div></li>
	  					<li><div class = "content_main_1">4. 除另有說明者外，所提供的任何價格、利率或其他報價僅供閣下作參考之用，並可於本行確認接受閣下的要約前無須給予通知而更改。除另有說明者外，閣下應付的價格並不包括閣下需要額外自行支付的適用稅項、稅費、交易徵費、合理費用及開支。</div></li>
	  					<li><div class = "content_main_1">5. 本行絕對禁止任何人士未經授權使用本行網站及應用程式，包括但不只限於未經授權進入本行系統、不當使用密碼或不當使用任何網站上的任何資料。</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px"/>
	  		<div id = "System_Failure_Security">
	  			<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">三、系統故障及安全</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. 閣下須自行負責防止、保障及確保其電腦、手提電話或任何其他電子設備(統稱“裝置”)不受電腦病毒或其他破壞性入侵。</div></li>
	  					<li><div class = "content_main_1">2. 本行已採取合理步驟確保本行網站及應用程式無故障、缺陷、病毒及錯誤，但本行並未就此作出任何擔保、保證或聲明。</div></li>
	  					<li><div class = "content_main_1">3. 本行在任何情況下，均不因閣下進入或使用本行網上服務涉及或導致的任何功能失效、系統、伺服器或連線失敗、錯誤、遺漏、中斷、保安問題、電腦病毒、惡意電腦程式、破壞、運作或傳送延誤、延誤錯誤或未能進入服務而負上任何責任，即使本行已獲通知可能會出現上述情況。</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Internet_Communications">
	  			<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">四、互聯網通訊</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. 閣下承認，通過互聯網之通訊並非完全可靠，並且可能出現傳送或運作延誤、數據丟失或被毀。</div></li>
	  					<li><div class = "content_main_1">2. 若因使用本行網站或應用程式所載之任何資訊，或者因向本行發送或自本行接收訊息，造成任何直接的、間接的、連帶性的或特殊的損失或損害，本行概不負責。</div></li>
					</ol>
		  		</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Linked_Web_Sites">
	  			<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">五、網址連結</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. 本網址提供超連結至其他網址，並不視為本行同意、推薦、認可、保證或推介任何第三方或在其網址所提供的服務／產品，亦不可視為本行與該等第三方及網址有任何形式的合作。</div></li>
	  					<li><div class = "content_main_1">2. 本行對與本網址連結的任何其他網址提供的內容或其設定不承擔任何責任。閣下進入或使用該等網址須自行承擔風險，並須遵守進入／使用該等網址的任何條款。</div></li>
	  					<li><div class = "content_main_1">3. 如閣下與本網址以外其他網址的供應者訂立任何合約安排，本行並非此等安排的其中一方，除非本行已明確表明或同意作為其中一方。</div></li>
					</ol>
		  		</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "other_Linked_Web_Sites">
	  			<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">六、其他網站接入</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. 其他網站建立超鏈接至本行網站，必須事先得到本行書面批准。本行有絕對酌情權隨時撤回批准，並要求清除任何連至本網站的鏈接。</div></li>
	  					<li><div class = "content_main_1">2. 建立超鏈接至本網站，並不代表本行認可或同意該等網站，亦不代表本行與第三者合作。對於該等鏈接產生或引致任何後果，本行一概不負責任。</div></li>
	  					<li><div class = "content_main_1">3. 任何連至本行網站的超鏈接，必須直接鏈接至本網站的主頁，及不能在本行網頁或內容「附加框架」或「直接鏈接內頁」。</div></li>
					</ol>
		  		</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Use_Cookie">
	  			<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">7、Cookie使用</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. 為了協助瞭解閣下對本行網站及應用程式的興趣及將用戶偏好加以儲存，透過cookies和類似的技術，本行只會收集有關閣下如何透過裝置上網瀏覽本行網站和應用程式之數據資料。</div></li>
	  					<li><div class = "content_main_1">2. Cookie不包含用戶的姓名、地址、電話號碼、電子郵件地址，或任何可識別用戶個人身份的資料。本行不會藉此收集或向任何其他第三方提供閣下的個人資料。</div></li>
	  					<li><div class = "content_main_1">3. 目前本行並不提供免除cookie追蹤的功能，原因是每當閣下刪除或清除cookies或者改用其他瀏覽器時，所有免除cookie追蹤的設定都會變為無效。為免混淆，如果閣下不希望本行從閣下的瀏覽器收集數據，本行建議閣下自行更改瀏覽器的隱私設定。但請注意，完全封鎖cookies可能會影響閣下使用本行的網站及應用程式。</div></li>
					</ol>
		  		</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Others">
	  			<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">八、其他</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">1. 本行有酌情權，可於任何時間並且沒有事先通知的情況下，將本行所提供之任何資料、產品及服務予以撤銷或修改。本行亦擁有最終決定權及絕對酌情權，決定任何人士是否合乎資格使用個別資料、產品及服務。</div></li>
	  					<li><div class = "content_main_1">2. 若本條款有所修訂，而閣下仍繼續使用本行網站或應用程式，即表示接納有關修訂。</div></li>
	  					<li><div class = "content_main_1">3. 本條款之中文版及英文版如有任何不符的地方，以中文版為準。</div></li>
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
