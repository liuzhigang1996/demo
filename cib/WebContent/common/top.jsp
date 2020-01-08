<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set'%>
<html>
	<set:loadrb file="app.cib.resource.sys.login">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

			<META http-equiv="Expires" content="Mon, 26 Jul 1997 05:00:00 GMT">
			<!--// Date in the past-->
			<META http-equiv="Last-Modified"
				content="Sat, 10 Nov 1997 09:08:07 GMT">
			<!--// always modified-->
			<meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
			<meta http-equiv="Pragma" content="no-cache">

			<link href="/cib/css/main.css?v=123" rel="stylesheet" type="text/css">
			<SCRIPT language=JavaScript src="/cib/javascript/common.js"></SCRIPT>
			<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
			<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
			<title></title>
			<script language="JavaScript">
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function toTarget(theTarget){
	document.goToTarget.action = theTarget;
	document.goToTarget.submit();
}

//for firefox disable F5
var disableKey2=function(e){
   e=e||window.event;
   //alert(e.which||e.keyCode);
   if((e.which||e.keyCode)==116){
    if(e.preventDefault){
    e.preventDefault();
         alert('F5 disable 3');
		 }
    else{event.keyCode = 0;
     e.returnValue=false;} 
   } 
}
if(document.addEventListener){
   document.addEventListener("keydown",disableKey2,false);
}
else{
   document.attachEvent("onkeydown",disableKey2);
}

var menuNow1 = "";
var menuNow2 = "";
function toggleMenu(ID){
	if(menuNow1 != ""){
		menuObj = document.getElementById("menu_" + menuNow1);
		menuObj.className="menu1_c";
		lmenuObj = document.getElementById("lmenu_" + menuNow1);
		lmenuObj.className="lmenu1_c";
		rmenuObj = document.getElementById("rmenu_" + menuNow1);
		rmenuObj.className="rmenu1_c";
		submenubar = document.getElementById("submenu_" + menuNow1);
	    submenubar.style.display = "none";
	}
	menuObj = document.getElementById("menu_" + ID);
	menuObj.className="menu1_o";
	lmenuObj = document.getElementById("lmenu_" + ID);
	lmenuObj.className="lmenu1_o";
	rmenuObj = document.getElementById("rmenu_" + ID);
	rmenuObj.className="rmenu1_o";
	submenubar = document.getElementById("submenu_" + ID);
	submenubar.style.display="block";
	menuNow1 = ID;
}
function toggleMenu2(ID){
	if(menuNow2 != ""){
		menuObj = document.getElementById("menu2_" + menuNow2);
		menuObj.className="menu2_c";
		lmenuObj = document.getElementById("lmenu2_" + menuNow2);
		lmenuObj.className="lmenu2_c";
		rmenuObj = document.getElementById("rmenu2_" + menuNow2);
		rmenuObj.className="rmenu2_c";
	}
	menuObj = document.getElementById("menu2_" + ID);
	menuObj.className="menu2_o";
	lmenuObj = document.getElementById("lmenu2_" + ID);
	lmenuObj.className="lmenu2_o";
	rmenuObj = document.getElementById("rmenu2_" + ID);
	rmenuObj.className="rmenu2_o";
	menuNow2 = ID;
}
var timerID = null;
var timerRunning = false;
var timeDiff = 0;
function MakeArray(size)
{
  this.length = size;
  for(var i = 1; i <= size; i++)
  {
  this[i] = "";
  }
  return this;
}
function showTime () {
  var now = new Date(new Date() -(-timeDiff));
  var year = now.getFullYear();
  var month = now.getMonth() + 1;
  var date = now.getDate();
  var hours = now.getHours();
  var minutes = now.getMinutes();
  var seconds = now.getSeconds();
  var day = now.getDay();
  Day = new MakeArray(7);
  Day[0]="Sun";
  Day[1]="Mon";
  Day[2]="Tue";
  Day[3]="Wed";
  Day[4]="Thu";
  Day[5]="Fri";
  Day[6]="Sat";
  Month = new MakeArray(12);
  Month[0]="Jan";
  Month[1]="Feb";
  Month[2]="Mar";
  Month[3]="Apr";
  Month[4]="May";
  Month[5]="Jun";
  Month[6]="Jul";
  Month[7]="Aug";
  Month[8]="Sep";
  Month[9]="Oct";
  Month[10]="Nov";
  Month[11]="Dec";
  //CN
  Day_hk = new MakeArray(7);
  Day_hk[0]="星期日";
  Day_hk[1]="星期一";
  Day_hk[2]="星期二";
  Day_hk[3]="星期三";
  Day_hk[4]="星期四";
  Day_hk[5]="星期五";
  Day_hk[6]="星期六";
  Month_hk = new MakeArray(12);
  Month_hk[0]="一月";
  Month_hk[1]="二月";
  Month_hk[2]="三月";
  Month_hk[3]="四月";
  Month_hk[4]="五月";
  Month_hk[5]="六月";
  Month_hk[6]="七月";
  Month_hk[7]="八月";
  Month_hk[8]="九月";
  Month_hk[9]="十月";
  Month_hk[10]="十一月";
  Month_hk[11]="十二月";
  //cn end
  if(language=="en_US"){
     var timeValue = "&nbsp;";
     timeValue += (Day[day]) + "  ";
     timeValue += ((date < 10) ? "0" : "") + date + "-";
     timeValue += (Month[month-1]) + "-";
     timeValue += year + "  ";
     timeValue += ((hours < 10) ? "0" : "") + hours;
     timeValue += ((minutes < 10) ? ":0" : ":") + minutes;
     //timeValue += ((seconds < 10) ? ":0" : ":") + seconds;
     document.all("TimeTag1").innerHTML = timeValue;
     if("<set:data name="timeZone"/>" != ""){
     var timeZone = "<set:data name='timeZone'/>";
     var nowForeign =new Date(now - (-3600000)*(parseInt(timeZone)-8));
     year = nowForeign.getFullYear();
     month = nowForeign.getMonth() + 1;
     date = nowForeign.getDate();
     hours = nowForeign.getHours();
     minutes = nowForeign.getMinutes();
     seconds = nowForeign.getSeconds();
     day = nowForeign.getDay();
     timeValue = "&nbsp;";
     timeValue += (Day[day]) + "  ";
     timeValue += ((date < 10) ? "0" : "") + date + "-";
     timeValue += (Month[month-1]) + "-";
     timeValue += year + "  ";
     timeValue += ((hours < 10) ? "0" : "") + hours;
     timeValue += ((minutes < 10) ? ":0" : ":") + minutes;
     //timeValue += ((seconds < 10) ? ":0" : ":") + seconds;
     document.getElementById("TimeTag2").innerHTML = timeValue;
     }
  }else{
     var timeValue = "&nbsp;";
     timeValue += (Day_hk[day]) + "  ";
     timeValue +=  year + "-";
     timeValue +=  month + "-";
     timeValue += ((date < 10) ? "0" : "") + date + " ";
     timeValue += ((hours < 10) ? "0" : "") + hours;
     timeValue += ((minutes < 10) ? ":0" : ":") + minutes;
     document.all("TimeTag1").innerHTML = timeValue;
     if("<set:data name="timeZone"/>" != ""){
     var timeZone = "<set:data name='timeZone'/>";
     var nowForeign =new Date(now - (-3600000)*(parseInt(timeZone)-8));
     year = nowForeign.getFullYear();
     month = nowForeign.getMonth() + 1;
     date = nowForeign.getDate();
     hours = nowForeign.getHours();
     minutes = nowForeign.getMinutes();
     seconds = nowForeign.getSeconds();
     day = nowForeign.getDay();
     timeValue = "&nbsp;";
 	 timeValue += (Day_hk[day]) + "  ";
 	 timeValue += year + "-";
 	 timeValue += month + "-";
 	 timeValue += ((date < 10) ? "0" : "") + date + " ";
 	 timeValue += ((hours < 10) ? "0" : "") + hours;
 	 timeValue += ((minutes < 10) ? ":0" : ":") + minutes;
     document.getElementById("TimeTag2").innerHTML = timeValue;
    }
  }
}
function doTimer()
{
	showTime ();
    timerID = setTimeout("doTimer()", 1000);
	timerRunning = true;
}
function stopClock (){
  if(timerRunning){
  	clearTimeout(timerID);
  }
  timerRunning = false;
}
function startClock () {
  var now = new Date();
  var timeMacau = <set:data name='timeMacau'/>;
  timeDiff = timeMacau - now;
  stopClock();
  doTimer();
  //add by linrui for change language 20190805
  if(language == "en_US"){
	  document.getElementById("showHK").style.display = "block"; 
	  document.getElementById("showUS").style.display = "none";
	}else{
	  document.getElementById("showUS").style.display = "block"; 
	  document.getElementById("showHK").style.display = "none"; 
	}
	//end 
}
function confirmQuit() 
{ 
	if( document.getElementById("exitActionFlag").value == "exitByCloseWindow" ){
		if(document.getElementById("changeLangCode").value == "Y"){//add by linrui 20190815
		event.returnValue = "<set:label name='Change_lang_message'/>"; 
		}else{
		event.returnValue = "<set:label name='Confirm_Close_Window'/>";
		document.getElementById("changeLangCode").value == "";
		}
	}
} 
function closeWindow(){
	
    //add by linrui for change Language
    if(document.getElementById("changeLangCode").value == "Y"){
        //alert(document.getElementById("changeLangCode").value);
        document.getElementById("changeLangCode").value = "";
      return;
    }else{
       var url = "/cib/login.do?ActionMethod=logout&exitActionFlag=exitByCloseWindow&PageLanguage="+language;
	   var myAjax = new Ajax.Request(url, {
		   method : 'get',
		   parameters : '',
		   //true---异步;false---同步.默认为true
		   asynchronous : false
	    });
    }
	/*
	if( document.getElementById("exitActionFlag").value == "exitByCloseWindow" ){
		win = window.open("/cib/common/logout.jsp" ,"","top=10000,left=10000,height=1,width=1"); 
	}
	*/
}
function logout(){
	    //add by linrui 20190710
	    parent.closeWin();
	    //add by linrui 20190710 end
		document.getElementById("exitActionFlag").value = "exitByButton";
		toTarget("/cib/login.do?ActionMethod=logout&PageLanguage="+ language);	
}

//add by lzg 20190506
	function msgbox(title,content,func,cancel,focus){
        //create_mask();
        var temp="<div style=\"width:300px;border: 2px solid #002D72;background-color: #fff; font-weight: bold;font-size: 12px;\" >"
                +"<div style=\"line-height:25px; padding:0px 5px; color:white;    background-color: #002D72;\">"+title+"</div>"
                +"<table  cellspacing=\"0\" border=\"0\" width = \"100%\"><tr>"
                +"<td><center><div style=\"background-color: #fff; font-weight: bold;font-size: 12px;padding:20px 0px ;\"><center>"+content
                +"</center></div></center></td></tr></table>"
                +"<div style=\"text-align:center; padding:0px 0px 20px;background-color: #fff;\"><center><input type='button'  style=\"border:1px solid #CCC; background-color:#CCC; width:55px; height:25px;\" value='<set:label name='OkValue'/>'id=\"msgconfirmb\"   onclick=\""+func+";\">";
        if(null!=cancel){temp+="   <input type='button' style=\"border:1px solid #CCC; background-color:#CCC; width:55px; height:25px;\" value='<set:label name='CancelValue'/>'  id=\"msgcancelb\"   onClick='removeMsgBox()'>";}
        temp+="</center></div></div>";
        create_msgbox(400,130,temp);
	}

	function get_width(){
   		return (document.body.clientWidth+document.body.scrollLeft);
	}
    function get_height(){
        return (document.body.clientHeight+document.body.scrollTop);
    }
    function get_left(w){
        var bw=document.body.clientWidth;
        var bh=document.body.clientHeight;
        w=parseFloat(w);
        return (bw/2-w/2+document.body.scrollLeft);
    }
    function get_top(h){
        var bw=document.body.clientWidth;
        var bh=document.body.clientHeight;
        h=parseFloat(h);
        return (bh/2-h/2+document.body.scrollTop);
    }
    function create_mask(){//A function that creates a mask layer
        var mask=document.createElement("div");
        mask.id="mask";
        mask.style.position="absolute";
        mask.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=4,opacity=25)";
        mask.style.opacity=0.4;//Mozilla's Opaque setting
        mask.style.background="black";
        mask.style.top="0px";
        mask.style.left="0px";
        mask.style.width=get_width();
        mask.style.height=get_height();
        mask.style.zIndex=1000;
        document.body.appendChild(mask);
    }
    function create_msgbox(w,h,t){//The function that creates the pop-up dialog box
        var box=document.createElement("div")    ;
        box.id="msgbox";
        box.style.position="absolute";
        box.style.width=w;
        box.style.height=h;
        box.style.overflow="visible";
        box.innerHTML=t;
        box.style.zIndex=1001;
        document.body.appendChild(box);
        //window.open(re_pos());
        re_pos();
    }
    
    function re_mask(){
        /*
        Change the size of the mask layer to ensure that everything 
        is also overwritten when scrolling and window sizes change
        */
        var mask=document.getElementById("mask")    ;
        if(null==mask)return;
        mask.style.width=get_width()+"px";
        mask.style.height=get_height()+"px";
    }
    function re_pos(){
        /*
        Change the location of the popup dialog layer to ensure that it 
        remains in the middle of the page as it scrolls and the window 
        size changes
        */
        var box=document.getElementById("msgbox");
        if(null!=box){
            var w=box.style.width;
            var h=box.style.height;
            box.style.left=get_left(w)+"px";
            box.style.top=get_top(h)+"px";
        }
    }
    function removeMsgBox(){
        /*
        Clear mask layer and popup dialog box
        */
        //var mask=document.getElementById("mask");
        var msgbox=document.getElementById("msgbox");
        if(null==msgbox)return;
       // document.body.removeChild(mask);
        document.body.removeChild(msgbox);
    }
    
    function re_show(){
        /*
        Redisplay the mask layer and pop-up window elements
        */
        re_pos();
        re_mask();    
    }
    function load_func(){
        /*
        loading function,cover window's onresize and onscroll's function
        */
        window.onresize=re_show;
        window.onscroll=re_show;    
    }
    //add by lzg end
    //add by linrui 20190805 for changelanguage
    function changeLanguage(language){
	    parent.changeLanguage(language);
}
</script>
		</head>
		<body onLoad="startClock();" onBeforeUnload="confirmQuit();"
			onUnLoad="closeWindow();">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td height="100%" align="right" background="/cib/images/logo-page.gif" style="background-repeat:no-repeat;-moz-background-size:100% 100%;background-size:auto 100%;">
						<table width="220" border="0" cellspacing="0" cellpadding="0"
							height="20">
							<tr>
								<td width="60"  class="topmenu" bgcolor="#002D72">
									<%--<img src="/cib/images/top-bar_end.png" width="60" height="20">--%>
                                    <a id="showHK" style="padding-left:15%;" onClick="changeLanguage('zh_HK')" href="#" ><set:label name='LANG_HK' /></a>
                                    <a id="showUS" style="padding-left:30%;" onClick="changeLanguage('en_US')" href="#" ><set:label name='LANG_ENG' /></a>
								</td>
								<!--<td class="topmenu_clock" bgcolor="#003366" nowrap><div id="TimeTag" name="TimeTag"></td>-->
								<!-- modified by lzg 20190526 -->
								<td width="140" class="topmenu" bgcolor="#002D72" nowrap
									align="center">
									<!-- modified by lzg end -->
									&nbsp;&nbsp;|&nbsp;&nbsp;
									<a onClick="toTarget('/cib/welcome.do?ActionMethod=load')"
										href="#"><set:label name="Home" defaultvalue="Home" /> </a>
									&nbsp;&nbsp;|&nbsp;&nbsp;
									<a
										onClick="msgbox('<set:label name='LogoutHeader'/>','<set:label name='logoutMessage'/>','logout()',1,1)"
										target="topFrame" href="#"><set:label name="Logout"
											defaultvalue="Logout" /> </a> &nbsp;&nbsp;|&nbsp;&nbsp;
								</td>
								<td width="20">
									<img src="/cib/images/top-bar_end.png" width="20" height="20">
								</td>
							</tr>
						</table>
						<table border="0" cellpadding="0" cellspacing="4"
							bgcolor="#FFFFFF">
							<tr>
								<td>
									<table width="" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td>
												<img src="/cib/images/shim.gif" width="5" height="48">
											</td>
											<td align="center">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td class="userinfo_title">
															<set:label name="Corp" />
															&nbsp;
														</td>
														<td class="userinfo">
															<set:data name="corpName" maxlen="16" />
															&nbsp;
														</td>
														<td class="userinfo_title">
															<set:label name="Macau_Time" />
															&nbsp;
														</td>
														<td class="userinfo">
															<div id="TimeTag1" name="TimeTag">
														</td>
													</tr>
													<tr>
														<td class="userinfo_title">
															<set:label name="User" defaultvalue="USER" />
															&nbsp;
														</td>
														<td class="userinfo">
															<set:data name="userName" maxlen="16" />
															&nbsp;
														</td>
														<td class="userinfo_title">
															<set:if name="timeZone" condition="NOTEQUALS" value="">
																<set:data name="foreignCity" />
																<set:label name="Time" />
															</set:if>
															<set:if name="timeZoneName" condition="NOTEQUALS"
																value=""> (<set:data name="timeZoneName" />) </set:if>
															&nbsp;
														</td>
														<td class="userinfo">
															<div id="TimeTag2" name="TimeTag">
														</td>
													</tr>
													<!--
                      <tr>
                        <td class="userinfo_title"><set:label name="Role" defaultvalue="ROLE"/></td>
                        <td class="userinfo"><set:data name="roleId" rb="app.cib.resource.common.corp_role"/></td>
                      </tr>
					  -->
												</table>
											</td>
											<td>
												<img src="/cib/images/shim.gif" width="5" height="48">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td height="34" valign="top"
						background="/cib/images/menu_tag_bg.gif">
						<table border="0" cellspacing="0" cellpadding="0" height="34">
							<tr>
								<set:menu type="group" mid="main" level="level0">
									<set:menu level="level1" inlevel="level0">
										<td
											id="lmenu_<set:menuvalue property='mid' inlevel='level1' />"
											class="lmenu1_c">
											<img src="/cib/images/shim.gif" width="8" height="26">
										</td>
										<td
											id="menu_<set:menuvalue property='mid' inlevel='level1' />"
											class="menu1_c">
											<a href="#"
												onClick="toggleMenu('<set:menuvalue property='mid' inlevel='level1' />');selDefualtMenu_<set:menuvalue property='mid' inlevel='level1'/>();">
												<set:menuvalue property='label' inlevel='level1' /> </a>
										</td>
										<td
											id="rmenu_<set:menuvalue property='mid' inlevel='level1' />"
											class="rmenu1_c">
											<IMG height=22 src="/cib/images/shim.gif" width=9>
										</td>
									</set:menu>
								</set:menu>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="menu2_b">
						<set:menu type="group" mid="main" level="level0">
							<set:menu level="level1" inlevel="level0">
								<div
									id="submenu_<set:menuvalue property='mid' inlevel='level1' />"
									style="display: none">
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td class="menu2_b">
												<table border="0" cellspacing="0" cellpadding="0"
													height="32">
													<tr>
														<td class="menu2_c">
															&nbsp;
														</td>
														<set:menu level="level2" inlevel="level1">
															<td
																id="lmenu2_<set:menuvalue property='mid' inlevel='level2' />"
																class="lmenu2_c">
																<img src="/cib/images/shim.gif" width="3" height="32">
															</td>
															<td class="menu2_c"
																id="menu2_<set:menuvalue property='mid' inlevel='level2' />"
																onClick="toggleMenu2('<set:menuvalue property='mid' inlevel='level2' />')">
																<set:menuif property="@COUNT" condition="equals"
																	value="1" inlevel='level1'>
																	<script language="JavaScript">
function selDefualtMenu_<set:menuvalue property='mid' inlevel='level1'/>(){
	toggleMenu2('<set:menuvalue property='mid' inlevel='level2' />');
	toTarget("<set:menuvalue property='path' inlevel='level2' />");
}
</script>
																</set:menuif>
																<a href="#"
																	onClick="toTarget('<set:menuvalue property='path' inlevel='level2' />')"><set:menuvalue
																		property='label' inlevel='level2' /> </a>
															</td>
															<td
																id="rmenu2_<set:menuvalue property='mid' inlevel='level2' />"
																class="rmenu2_c">
																<IMG src="/cib/images/shim.gif" width="3" height="32">
															</td>
															<td class="menu2_d">
																<IMG src="/cib/images/shim.gif" width="8" height="32">
															</td>
														</set:menu>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</div>
							</set:menu>
						</set:menu>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td class="menu2_b" height="32">
									&nbsp;
								</td>
							</tr>
						</table>
						<!--
	</td>
    <td width="115" valign="top" class="menu2_clock"><div id="TimeTag" name="TimeTag"></div></td>
-->
				</tr>
			</table>
			<input id="exitActionFlag" name="exitActionFlag" type="hidden"
				value="exitByCloseWindow">
			<input id="changeLangCode" name="changeLangCode" type="hidden" value="">
			<form name="goToTarget" method="POST" target="mainFrame">
			</form>
		</body>
	</set:loadrb>
</html>
