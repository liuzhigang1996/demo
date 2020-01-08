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
	  知識產權聲明
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
  		知識產權聲明
  	</div>
  	<div class = "main">
	  	<!--<div class = "title_left">
	  		<div style="height: 15%">&nbsp;</div>
	  		<ul style = "list-style:none;margin:0px 0px;">
	  			<li><div class = "title_left_1" id = "title_1"><a class = "link" href = "javascript:void(0);" onclick="goTop()">copyright and the source</a></div></li>
	  			<li><div class = "title_left_2" id = "title_2"><a class = "link" href = "#Logo" onclick="changeStyle('title_2')">Logo</a></div></li>
	  			<li><div class = "title_left_2" id = "title_3"><a class = "link" href = "#Notes" onclick="changeStyle('title_3')">Notes</a></div></li>
	  		</ul>
	  	</div>
	  	-->
	  	<!--<div class = "content">
	  		<div class = "content_main">
	  			<div style = "margin: 10px 40px;">Any user who browses the homepage of the website of Macao Development Bank Limited ("the Bank") and any of the pages thereof and/or use E-Banking services shall be aware of and agree to the following terms. If you do not accept the following terms, do not access the Bank’s website or use the online service of the Bank.</div>
	  		</div>
	  		<div id = "copyright_source">
	  			<div class = "content_title">1. The copyright and the source of the content of this website</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
						<li><div class = "content_main_1">All contents of the Bank’s Website and applications including, but not limited to the text, graphics, sounds and videos are the copyright and any other intellectual right of the Bank and should not be copied, downloaded, translated, distributed or published in any way without the prior written consent of the Bank.</div></li>
					</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div id = "Logo">
	  			<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">2. Logo</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">“澳門發展銀行”, “MACAO DEVELOPMENT BANK”, “MDB” and logo of the Bank are ongoing registered trademarks applied and owned by the Bank and no such trademarks should be used in any way without the prior written consent of the Bank.</div></li>
	  				</ol>
	  			</div>
  			</div>
  			<hr style = "margin: 0px 40px" />
  			<div id = "Notes">
  				<div style="height: 15%">&nbsp;</div>
	  			<div class = "content_title">3. Notes</div>
	  			<div class = "content_main">
	  				<ol style = "list-style-type: none;">
	  					<li><div class = "content_main_1">In the event of any inconsistency between the Chinese version and English version of this Statement, the Chinese version shall prevail.</div></li>
	  				</ol>
	  			</div>
	  		</div>
	  		<hr style = "margin: 0px 40px" />
	  		<div style = "height: 450px">&nbsp;</div>
  		</div>
	  	-->
	  	<div >
	  		<div class = "content_main">
	  			<div style="height: 15%">&nbsp;</div>
	  			<div>任何使用者瀏覽澳門發展銀行股份有限公司（“本行”）網站首頁及其中之任何網頁或使用本行電子銀行服務時，即表明知悉及同意下列條款。若閣下不接受下列條款，請勿查閱此網站或使用本行網上服務</div>
	  		</div>
	  		<div id = "copyright_source">
	  			<div class = "content_main">
					<div class = "content_main_1">1. 本行網站及應用程式內的一切內容，包括但不限於內文、圖形、聲音及視頻，其版權及其他知識產權均屬本行所有，未經本行事先書面同意，不得以任何方式複製、下載、翻譯、分發或出版。</div>
	  			</div>
	  		</div>
	  		<div id = "Logo">
	  			<div class = "content_main">
	  				<div class = "content_main_1">2. <span style = "font-family: PMingLiU;">“澳門發展銀行”</span>、 “MACAO DEVELOPMENT BANK”、“MDB”及本行logo均為本行所申請及擁有的註冊商標，未經本行的事先書面同意，嚴禁以任何方式使用上述任何商標。</div>
	  			</div>
  			</div>
  			<div id = "Notes">
	  			<div class = "content_main">
	  				<div class = "content_main_1">3. 此聲明之中文版及英文版如有任何不符的地方，以中文版為準。</div>
	  			</div>
	  		</div>
  		</div>
  	</div>
  </body>
  </set:loadrb>
</html>
