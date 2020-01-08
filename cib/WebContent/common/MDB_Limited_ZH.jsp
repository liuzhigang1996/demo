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
	  澳門發展銀行股份有限公司 電子銀行服務專用條款
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
  		電子銀行服務專用條款
  	</div>
  	<div class = "main">
  	<div class = "title_left">
  		<div style="height: 15%">&nbsp;</div>
  		<ul style = "list-style:none;margin:0px 0px;">
  			<li><div class = "title_left_1" id = "title_1"><a class = "link" href = "javascript:void(0);" onclick="goTop()">目的及範圍</a></div></li>
  			<li><div class = "title_left_2" id = "title_2"><a class = "link" href = "#Definitions" onclick="changeStyle('title_2')">定義</a></div></li>
  			<li><div class = "title_left_2" id = "title_3"><a class = "link" href = "#Account_Management" onclick="changeStyle('title_3')">賬戶管理</a></div></li>
  			<li><div class = "title_left_2" id = "title_4"><a class = "link" href = "#Client_Instructions" onclick="changeStyle('title_4')">客戶指示</a></div></li>
  			<li><div class = "title_left_2" id = "title_5"><a class = "link" href = "#Login" onclick="changeStyle('title_5')">登入</a></div></li>
  			<li><div class = "title_left_2" id = "title_6"><a class = "link" href = "#Biometric_Authentication" onclick="changeStyle('title_6')">生物識別認證</a></div></li>
  			<li><div class = "title_left_2" id = "title_7"><a class = "link" href = "#Security_Measures" onclick="changeStyle('title_7')">保安措施</a></div></li>
  			<li><div class = "title_left_2" id = "title_8"><a class = "link" href = "#Client_Responsibilities" onclick="changeStyle('title_8')">客戶責任</a></div></li>
  			<li><div class = "title_left_2" id = "title_9"><a class = "link" href = "#Encryption_Virus" onclick="changeStyle('title_9')">加密及病毒</a></div></li>
  			<li><div class = "title_left_2" id = "title_10"><a class = "link" href = "#service_interruptions" onclick="changeStyle('title_10')">服務中斷</a></div></li>
  		</ul>
  	</div>
  	<div class = "content">
  		<div id = "Purpose_Scope">
  			<div class = "content_title">第1條 目的及範圍</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">1.1.電子銀行服務專用條款（以下簡稱“本條款”）適用於澳門發展銀行股份有限公司（以下簡稱“本行”）內使用由本行提供的電子銀行服務的賬戶持有人（以下簡稱“客戶”）。</div></li>
  					<li><div class = "content_main_1">1.2.本條款的適用，不妨礙本行所訂立的《賬戶及有關服務一般條款及條件》（以下簡稱“《一般條款》”）同時適用於客戶，倘若本條款與《一般條款》有抵觸，本條款的效力優先於《一般條款》。</div></li>
  					<li><div class = "content_main_1">1.3.本條款的英文版僅供參考，若中、英文版有任何岐異，當以中文版為準。</div></li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px" />
  		<div id = "Definitions">
  			<div style="height: 5%">&nbsp;</div>
  			<div class = "content_title">第2條 定義</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">2.1.除非另有明文規定，本條款內下列文字及詞語應具有下列含義：</div></li>
  					<li>
  						<ul style = "list-style-type: none;">
		  					<li><div class = "content_main_1">(a) “個人客戶”指在本行開立個人賬戶並且申請使用由本行提供電子銀行服務的自然人。</div></li>
		  					<li><div class = "content_main_1">(b) “商業客戶”指在本行開立商業賬戶並且申請使用由本行提供電子銀行服務的商業機構，包括但不限於個人企業主、公司及其他法人機構。</div></li>
		  					<li><div class = "content_main_1">(c) “申請人”指其姓名及資料填寫在本行電子銀行服務申請表格上的人士。</div></li>
		  					<li><div class = "content_main_1">(d) “電子銀行服務”指本行為客戶提供以利用互聯網設備，透過本行網上銀行或手機銀行查詢賬戶或進行銀行交易的服務。</div></li>
		  					<li><div class = "content_main_1">(e) “網上銀行”指本行以互聯網站為商業客戶持有人提供查詢賬戶或進行銀行交易的網上銀行服務，該服務暫不向個人賬戶持有人提供。</div></li>
		  					<li><div class = "content_main_1">(f) “手機銀行”指本行透過移動應用程式為客戶提供查詢賬戶或進行交易的服務。該移動應用程式由本行製作、管理、維護並不時更新、透過主流平台或本行網站發佈，適用於移動互聯網設備。</div></li>
		  					<li><div class = "content_main_1">(g) “本行網站” 指由本行負責建設、管理、維護並不時更新，用於本行發佈信息、提供網上銀行服務的網站，網站域名為：www.mdb.com.mo。</div></li>
		  					<li><div class = "content_main_1">(h) “客戶號碼”指銀行為客戶申請使用網上銀行而發予的客戶識別號碼。</div></li>
		  					<li><div class = "content_main_1">(i) “登入ID”指申請人在本行開通電子銀行時自行設定的登入電子銀行的用戶名稱。</div></li>
		  					<li><div class = "content_main_1">(j) “密碼”指當登入本行電子銀行時，申請人用作核實其客戶號碼所需的識別碼。</div></li>
		  					<li><div class = "content_main_1">(k) “一次性密碼（OTP）”指為了確認相關金融交易，由本行系統生成並透過短訊方式傳送給申請人已在本行登記的有效手機號碼的一次性獨特數字密碼。</div></li>
		  					<li><div class = "content_main_1">(l) “使用者”指代表客戶負責管理和控制網上銀行服務的人士，包括一位或以上的系統管理員、操作員、審批員、執行員或本行訂定其他名稱的人士，使用者在網上銀行內以客戶號碼、登入ID和密碼作識別認證，各類使用者的角色和權限由本行依據客戶之指示而設定。</div></li>
		  					<li><div class = "content_main_1">(m) “生物認證”指用數理統計方法透過人體生物特徵來區分生物個體的認證技術，包括但不限於面部識別及指紋識別等。</div></li>
	  					</ul>
	  				</li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Account_Management">
  			<div style="height: 5%">&nbsp;</div>
  			<div class = "content_title">第3條 賬戶管理</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">3.1.個人客戶申请電子銀行服務时，應由賬戶持有人向本行提出申請，並由其進行賬戶查詢及交易。</div></li>
  					<li><div class = "content_main_1">3.2.本行不接受賬戶持有人將第3.1條款所述之權利授予任何第三人。</div></li>
  					<li><div class = "content_main_1">3.3.網上銀行服務應由具權限人士代表簽署本行申請文件、協議及條款，以作出(i)設定、增加或刪除使用者；(ii)設定及更改交易限額；(iii)刪除或增加服務或功能等。</div></li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Client_Instructions">
  			<div style="height: 5%">&nbsp;</div>
  			<div class = "content_title">第4條 客戶指示</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">4.1.客戶一經申請使用本行的電子銀行服務，即視客戶已確認具適當設備及設施，並同意接收本行以電子訊息取代紙張或其他通訊方式所發出的資訊。</div></li>
  					<li><div class = "content_main_1">4.2.電子訊息被視為經訊息發送人簽署的書面文件，客戶不得對以電子訊息訂立的合約的有效性以其訂立方式提出異議。</div></li>
  					<li><div class = "content_main_1">4.3.客戶可於本行營業時間以外使用本行提供的電子銀行服務，但本行不向客戶保證於收到客戶指示後即時進行有關交易，有可能於正常營業時間內方對客戶指示進行處理。</div></li>
  					<li><div class = "content_main_1">4.4.客戶與本行可透過電子郵件、應用程式、手機短訊或任何其他方式進行通訊，客戶確認及接受透過上述方式進行的通訊存有被第三人截取、監察、修改或干擾之風險，倘出現上述情況而引致客戶所招致或蒙受的任何損失，本行無須對客戶負上任何因此而生的責任。</div></li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Login">
  			<div style="height: 5%">&nbsp;</div>
  			<div class = "content_title">第5條 登入</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">5.1.手機銀行申請人獲本行批准並取得由本行給予的臨時登入ID和臨時密碼後，可用作登入並使用電子銀行服務。首次登入時，應自行訂定其登入ID和密碼，一旦由申請人自行訂定其登入ID和密碼且被本行電腦系統接納，臨時登入ID和臨時密碼即告失效。</div></li>
  					<li><div class = "content_main_1">5.2.手機銀行申請人自行訂定其登入ID和密碼後，可自行啟動手機中所具備的生物認證以用作設定認證程序。</div></li>
  					<li><div class = "content_main_1">5.3.網上銀行的登入ID需於申請時填報，經本行確認並提供臨時密碼後，可用作登入並使用電子銀行服務。一旦由使用者自行訂定其密碼且被本行電腦系統接納，臨時密碼即告失效。</div></li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Biometric_Authentication">
  			<div style="height: 5%">&nbsp;</div>
  			<div class = "content_title">第6條 生物識別認證</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">6.1.生物識別認證登錄服務僅可適用於本行提供的手機銀行服務。</div></li>
  					<li>
	  					<div class = "content_main_1">
	  						6.2.僅符合以下條件的客戶可使用本行手機銀行生物識別認證登錄服務：
	  					</div>
	  					<ul style = "list-style-type: none;">
	  						<li><div class = "content_main_1">(a) 客戶所使用的移動設備為該登錄服務所支援（本行可自行確定並不時調整所支援的移動設備）；</div></li>
	  						<li><div class = "content_main_1">(b) 客戶是本行手機銀行註冊用戶且客戶已在其移動設備上下載本行手機銀行應用程式；</div></li>
	  						<li><div class = "content_main_1">(c) 客戶已在其移動設備上啟動了指紋、面部特徵或其他生物特徵識別功能並為此存儲指紋、面部特徵或其他生物特徵；</div></li>
	  					</ul>
  					</li>
  					<li><div class = "content_main_1">6.3.客戶應採取一切合理措施，妥善保管客戶的移動設備以及所存儲的指紋、面部特徵或其他生物特徵，避免客戶的移動設備被其他第三人使用。</div></li>
  					<li><div class = "content_main_1">6.4.客戶明白當其使用生物識別認證登錄服務，任何存儲在客戶移動設備上的指紋、面部特徵或其他生物特徵都可以用於登入客戶的手機銀行服務，客戶應確保其移動設備上不應存儲任何第三人的人體生物特徵，減低第三人登入客戶的手機銀行服務的風險，同時客戶應當自行承擔上述所涉及的一切風險。</div></li>
  					<li><div class = "content_main_1">6.5.客戶明白於本行手機銀行登錄時所使用的生物識別認證是透過客戶的移動設備上所具備的生物識別認證功能完成，而並非由本行提供，因此本行不對客戶的移動設備上的指紋、面部識別或其他生物識別認證功能的安全性作任何陳述、保證或擔保。</div></li>
  					<li><div class = "content_main_1">6.6.本行有權隨時暫停、取消或客戶的生物識別認證登錄服務，而無須事先通知，其中包括但不限於在知悉或懷疑客戶的移動設備、生物特徵或其他密碼存在安全隱患的情況。</div></li>
  					<li><div class = "content_main_1">6.7.生物認證的可使用範圍由本行全權決定，客戶明白倘若生物認證無法使用，應使用其他認證方式登錄服務，生物認證並非手機銀行的唯一驗證方式，就客戶未按本條款內的規定所使用或無法使用生物認證而令客戶招致或蒙受的任何損失，本行無須對客戶負上任何因此而生的責任。</div></li>
  				</ul>
	  		</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Security_Measures">
  			<div style="height: 5%">&nbsp;</div>
  			<div class = "content_title">第7條 保安措施</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">7.1.客戶應採取一切合理的預防措施以保障其透過互聯網設備使用服務時的安全並防止資訊被盜取。</div></li>
  					<li><div class = "content_main_1">7.2.本行保留權利在任何時候指定何種銀行交易要求申請人使用一次性密碼作雙重驗證，以便有關交易能被本行的電腦系統確認及接納。</div></li>
  					<li><div class = "content_main_1">7.3.客戶應對其登入ID、臨時登入ID、客戶號碼、臨時密碼和密碼的安全及保密負全部責任，申請人應以適當的方式確保不讓第三人知悉。申請人應緊記客戶號碼、登入ID及密碼，不應以書面形式保存。</div></li>
  					<li><div class = "content_main_1">7.4.因安全理由，客戶應定期更改電子銀行服務所使用的密碼。</div></li>
  					<li><div class = "content_main_1">7.5.倘若客戶知道或懷疑其登入ID、臨時登入ID、臨時密碼、密碼已遺失或被盜用又或以任何方式被第三人知悉時，應立即通知本行。</div></li>
  					<li><div class = "content_main_1">7.6.客戶同意對使用一次性密碼的安全性負全部責任，應以適當的方式確保不讓第三人知悉。倘若一次性密碼或用於接收一次性密碼的裝置實際上或懷疑被誤用，客戶應立即通知本行。</div></li>
  					<li><div class = "content_main_1">7.7.凡經使用申請人的登入ID、臨時登入ID、客戶號碼、臨時密碼、密碼、面容識別、指紋掃瞄、一次性密碼所發出的任何指示均被視為有效，並對客戶具約束力。任何已收指示或已完成交易的確認都被視為申請人於傳送後立即收到，一經發出並經本行接納，不得取消或更改。</div></li>
  					<li><div class = "content_main_1">7.8.客戶知悉並同意由於可能出現不可預測的網絡不通暢、其開放性及公開性或本行不能控制的其他情況，有可能會導致錯誤、延遲、中斷、受非法干預、網絡攻擊而導致不完整的傳送，因此本行所存有的賬簿和記錄視為任何特定交易的最終決定性證據。</div></li>
  					<li><div class = "content_main_1">7.9.本行保留隨時建立或更改網上銀行交易、轉賬或付款限額的權利。客戶可登入本行電子銀行了解詳情。</div></li>
  					<li><div class = "content_main_1">7.10.客戶如更換或棄置曾使用本行電子銀行服務的互聯網設備時，應確保已清除所有相關應用程式、存儲數據和交易記錄。</div></li>
  					
  				</ul>
	  		</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Client_Responsibilities">
  			<div style="height: 5%">&nbsp;</div>
  			<div class = "content_title">第8條 客戶責任</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">8.1.客戶應確保其賬戶有足夠的資金或信貸額，以便本行能執行客戶指示。倘若因客戶賬戶無足夠資金或信貸額而引致本行沒有或延遲執行客戶指示，本行概不就因此而生的任何損失負責。</div></li>
  					<li><div class = "content_main_1">8.2.客戶應確保及經常檢查其賬戶中的賬項記錄的準確性、完整性及正確性，並在知悉任何錯誤或遺漏時立即通知本行，其中包括但不限於任何未經要求或不正確的交易賬項。</div></li>
  					<li><div class = "content_main_1">8.3.客戶應確保其向本行電子銀行提供的一切資料均準確、完整及正確。本行無須對客戶使用服務時所造成的任何錯誤或遺漏負責。</div></li>
  					<li><div class = "content_main_1">8.4.客戶應向本行提供一個於澳門本地電信服務商登記的有效手機號碼，以便其能通過短信接收密碼進行銀行交易。倘若隨後需增加、修改或取消手機號碼，客戶應以書面形式通知本行。客戶須對本行收到書面通知前發生的所有損失承擔全部責任。</div></li>
  					<li><div class = "content_main_1">8.5.倘若客戶使用海外流動服務網絡，而相關的電信服務商可能不允許客戶透過短信接收密碼，又或可能接收延遲及進行收費等，本行對在此情況下客戶所產生的任何費用不承擔任何責任。</div></li>
  					<li>
	  					<div class = "content_main_1">
	  						8.6.倘若客戶作出以下任意一種行為，應承擔全部風險和損失，並向本行承擔可能因此而產生之賠償：
	  					</div>
	  					<ul style = "list-style-type: none;">
	  						<li><div class = "content_main_1">(a) 擅自修改、翻譯、改編、出租、轉許可、傳播或轉讓本行提供的應用程式或服務，或試圖通過逆向工程、反編譯或其他方式獲取本行的原始程式碼；</div></li>
	  						<li><div class = "content_main_1">(b) 破壞或試圖破壞電子銀行服務安全的任何行為（其中包括但不限於釣魚，駭客，網絡詐騙，散播病毒、木馬、惡意程式碼，進行掃描、嗅探、ARP欺騙、DDOS等攻擊行為）；</div></li>
	  						<li><div class = "content_main_1">(c) 在互聯網裝置或操作系統供應商支援或保修的配置範圍之外，或在經修改或破解的任何裝置或操作系統上使用本行電子銀行，例如經越獄的iOS設備、經root的Android設備等；</div></li>
	  						<li><div class = "content_main_1">(d) 未從本行官方網站上接入本行網上銀行服務，或未從本行官方網站或本行指定之主流應用程式商店上下載手機銀行及其更新版本；</div></li>
	  						<li><div class = "content_main_1">(e) 以其他任何違反法律法規、行業慣例和社會公共道德，及影響、損害本行利益的方式或目的使用本行提供的電子銀行服務。</div></li>
	  					</ul>
  					</li>
  					<li><div class = "content_main_1">8.7.本行提示客戶，欺騙者可能盜用本行之名義通過欺詐網站、應用程式或者內置假網站鏈接的電子郵件或短信息，以騙取接收者透露敏感資訊如電子銀行登入ID和密碼。客戶如果發現與本行相類似的欺詐網頁或應用程式，應當盡快通知本行。本行可因安全理由自行根據情況增設附加監控措施，或要求以書面或其他方式確認各項交易。</div></li>
  				</ul>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "Encryption_Virus">
  			<div style="height: 5%">&nbsp;</div>
  			<div class = "content_title">第9條 加密及病毒</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">9.1.由於電子銀行需透過互聯網使用，而互聯網是本行控制範圍外的公共系統，故此客戶有責任確保其用作使用電子銀行的任何裝置獲得充分保障以免感染電腦病毒及受到其他破壞或干擾。</div></li>
  					<li><div class = "content_main_1">9.2.本行對客戶因使用電子銀行而引致資料、軟件、電腦、電腦網絡、移動裝置或其他設備的任何損失或損害概不負責，除該等損失或損害乃直接及純粹因本行疏忽或故意所引起。</div></li>
  				</ul>
	  		</div>
  		</div>
  		<hr style = "margin: 0px 40px"/>
  		<div id = "service_interruptions">
  			<div style="height: 5%">&nbsp;</div>
  			<div class = "content_title">第10條 服務中斷</div>
  			<div class = "content_main">
  				<ul style = "list-style-type: none;">
  					<li><div class = "content_main_1">10.1.本行如進行例行檢查、升級系統等可預測之操作而無法提供全部或部份電子銀行服務，將合理地儘快以適當方式通知客戶。</div></li>
  					<li><div class = "content_main_1">10.2.在本行認為有需要或適宜的情況下，包括但不限於懷疑保安系統受到破壞、進行緊急維修等，可不需通知客戶的情況下暫停全部或部份電子銀行服務。</div></li>
  					<li><div class = "content_main_1">10.3.倘若本行已就由本行所提供的某項服務向客戶徵收費用，而本行因服務中斷並未能提供，本行將向客戶退還此等款項。除退還上述款項外，本行概不承擔其他責任。</div></li>
  				</ul>
  			</div>
  		</div>
  		<div id = "service_interruptions">
  			<div class = "content_main">
  				<div style = "text-align: right;">（2019/07版本v1.0）<span style = "padding-right: 25px;">&nbsp;</span></div>
  			</div>
  		</div>
  		<hr style = "margin: 0px 40px" />
	  	<div style = "height: 360px">&nbsp;</div>
  	</div>
  	</div>
  </body>
  </set:loadrb>
</html>
