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
	  資料保護聲明
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
  		資料保護聲明
  	</div>
  	<div class = "main">
	  	<div class = "title_left">
	  		<div style="height: 15%">&nbsp;</div>
	  		<ul style = "list-style:none;margin:0px 0px;">
	  			<li><div class = "title_left_1" id = "title_1"><a class = "link" href = "javascript:void(0);" onclick="goTop()">法律依據</a></div></li>
	  			<li><div class = "title_left_2" id = "title_2"><a class = "link" href = "#Data_Collection_Purposes" onclick="changeStyle('title_2')">資料收集及處理目的</a></div></li>
	  			<li><div class = "title_left_2" id = "title_3"><a class = "link" href = "#Data_Processing_Transferring" onclick="changeStyle('title_3')">資料處理及轉移</a></div></li>
	  			<li><div class = "title_left_2" id = "title_4"><a class = "link" href = "#Data_Subject_rights" onclick="changeStyle('title_4')">資料當事人的權利</a></div></li>
	  			<li><div class = "title_left_2" id = "title_5"><a class = "link" href = "#Security_Protection" onclick="changeStyle('title_5')">資料安全保護</a></div></li>
	  			<li><div class = "title_left_2" id = "title_6"><a class = "link" href = "#Others" onclick="changeStyle('title_6')">其他</a></div></li>
	  		</ul>
	  	</div>
	  	<div class = "content">
	  		<div id = "Legal_Basis">
	  			<div class = "content_title">一、法律依據</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">澳門發展銀行股份有限公司（“本行”）嚴格遵守澳門特別行政區第32/93/M號法令《金融體系法律制度》、第8/2005號法律《個人資料保護法》及其他相關法律法規所規定的職業保密及個人資料保護的義務，嚴格保護資料當事人之資料。</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Data_Collection_Purposes">
	  			<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">二、資料收集及處理目的</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1"><b>(一)本行客戶資料</b></div></li>
	  				</ol>
	  				<ol style = "list-style-type: none;">
	  					<li>
	  						<div class = "content_main_1"><b>本行收集客戶資料的類別包括：</b></div>
	  					</li>
	  					<li>
	  						<ol style = "list-style-type: none;">
	  							<li><div class = "content_main_1">(1) 身份證明或公司註冊文件；</div></li>
	  							<li><div class = "content_main_1">(2) 住址證明文件；</div></li>
	  							<li><div class = "content_main_1">(3) 各類財務資料；</div></li>
	  							<li><div class = "content_main_1">(4) 用戶註冊信息、身份認證信息；</div></li>
	  							<li><div class = "content_main_1">(5) 物業、交通工具等資產文件；</div></li>
	  							<li><div class = "content_main_1">(6) 交易記錄、登錄日誌；</div></li>
	  							<li><div class = "content_main_1">(7) 載有客戶個人信息的其他文檔、圖片、音視頻、數字證書、計算機程序等文件；</div></li>
	  							<li><div class = "content_main_1">(8) 根據法律規定或客戶同意收集的其他資料；</div></li>
	  						</ol>
	  					</li>
	  					<li>
	  						<div class = "content_main_1"><b>本行對客戶資料處理目的和用途包括：</b></div>
	  						<ol style = "list-style-type: none;">
	  							<li><div class = "content_main_1">(1) 為建立、維持、評估、管理、改善及執行本行向客戶提供的金融服務或信貸便利。</div></li>
	  							<li><div class = "content_main_1">(2) 確保客戶維持可靠信用及在適當時作信貸檢查。</div></li>
	  							<li><div class = "content_main_1">(3) 向客戶發送本行產品、服務等營銷資訊。</div></li>
	  							<li><div class = "content_main_1">(4) 使用必要之資料，以對根據《金融體系法律制度》所賦予之權限而作出且成為行政上訴及/或相關訴訟標的之行為進行辯護。</div></li>
	  							<li><div class = "content_main_1">(5) 本行或本行受託人為對拖欠之客戶採用必要之方法，以獲得賠償之方式實現本行債權而使用持有之資料。</div></li>
	  							<li><div class = "content_main_1">(6) 本行讓與債權或將有關徵收交託資料予亦須遵守保密義務之第三人。</div></li>
	  							<li><div class = "content_main_1">(7) 本行為取得技術性之意見，對所需資訊之謹慎使用。</div></li>
	  							<li><div class = "content_main_1">(8) 本行就遵從洗錢、恐怖份子資金籌集或其他非法活動之制裁或防止或偵測而作出資料披露。</div></li>
	  							<li><div class = "content_main_1">(9) 為履行法律制度規定或監管要求而作出之任何其他披露。</div></li>
	  							<li><div class = "content_main_1">(10) 與上述有聯繫或有關的用途。</div></li>
	  						</ol>
	  					</li>
	  				</ol>
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1"><b>(二)求職者資料</b></div></li>
	  					<li>
	  						<div class = "content_main_1">本行為人員招聘的目的，向求職者收集下述資料：</div>
	  						<ol style = "list-style-type: none;">
	  							<li><div class = "content_main_1">(1) 身份認別資料：姓名、出生日期、出生地、性別、國籍、地址、電話號碼、電郵地址、學歷、常用語言、身份證明文件種類、編號及副本等；</div></li>
	  							<li><div class = "content_main_1">(2) 履歷資料;</div></li>
	  							<li><div class = "content_main_1">(3) 根據法律規定或求職者同意收集的其他資料。</div></li>
	  						</ol>
	  					</li>
	  				</ol>
	  			</div>
  			</div>
  			<hr style = "margin: 0px 40px" />
  			<div id = "Data_Processing_Transferring">
  				<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">三、資料處理及轉移</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">資料當事人知悉並同意，本行可依據澳門特別行政區第32/93/M號法令《金融體系法律制度》、第8/2005號法律《個人資料保護法》及相關法例之規定，並以謹慎及合理措施保障資料當事人資料保密及安全情況下，在澳門或以外的地方使用、處理及存儲資料當事人資料，及/或轉移及披露予在澳門或以外地方的其他機構或人員（包括但不限於監管機構、司法機構、公共實體；其他信用機構、交易所、結算所；合同相對方；有保密責任約束的人士；外部核數師、法律顧問、服務供應商；出票人及承讓人等）用於適當之用途。</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Data_Subject_rights">
  				<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">四、資料當事人的權利</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">(1) 知悉其資料是否被本行保存，以及倘有之資料種類及來源；</div></li>
						<li><div class = "content_main_1">(2) 查詢被本行處理之資料、要求本行對不完整或不準確的資料進行修改及刪除；</div></li>
						<li><div class = "content_main_1">(3) 知悉其何等資料會被披露予其他第三者或機構（諸如信貸資料機構或債務追收代理機構）；</div></li>
						<li><div class = "content_main_1">(4) 通過本行向上述第三者或機構轉達查詢、修改或刪除其資料的要求；</div></li>
						<li><div class = "content_main_1">(5) 要求本行提供曾接受本行披露的個人資料的上述第三者或機構的聯絡詳情；</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Security_Protection">
  				<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">五、資料安全保護</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">本行會對資料當事人資料保存直至該等資料無須再用於收集時所作之用途或法定的保存期限。本行使用安全的技術、隱私保護的管理措施，以及限制只有被授權的僱員才能接觸、查閱或處理資料當事人的個人資料，以保護資料當事人的個人隱私。</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Others">
  				<div style="height: 5%">&nbsp;</div>
	  			<div class = "content_title">六、其他</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">此聲明之中文版及英文版如有任何不符的地方，以中文版為準。</div></li>
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
