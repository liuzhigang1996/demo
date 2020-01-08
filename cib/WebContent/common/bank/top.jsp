<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set'%>
<html>
	<set:loadrb file="app.cib.resource.sys.login">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

			<META http-equiv="Expires" content="Mon, 26 Jul 1997 05:00:00 GMT">
			<META http-equiv="Last-Modified" content="Sat, 10 Nov 1997 09:08:07 GMT">
			<meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
			<meta http-equiv="Pragma" content="no-cache">
			<meta http-equiv="Expires" content="0">
            <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8;IE=7; IE=EDGE">

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
  var now = new Date();
  var year = now.getYear();
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
  var timeValue = "";
  timeValue += ((date < 10) ? "0" : "") + date + ".";
  timeValue += ((month < 10) ? "0" : "") + month + ".";
  timeValue += year + "  ";
  //timeValue += (Day[day]) + "  ";
  timeValue += ((hours < 10) ? "0" : "") + hours;
  timeValue += ((minutes < 10) ? ":0" : ":") + minutes;
  //timeValue += ((seconds < 10) ? ":0" : ":") + seconds;
  document.all("TimeTag").innerHTML = timeValue;
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
  stopClock();
  doTimer();
}
function confirmQuit() 
{ 
	if( document.getElementById("exitActionFlag").value == "exitByCloseWindow" ){
		event.returnValue = "<set:label name='Confirm_Close_Window'/>"; 
	}
} 
function closeWindow(){

	var url = "/cib/bankLogin.do?ActionMethod=logout&exitActionFlag=exitByCloseWindow";
	var myAjax = new Ajax.Request(url, {
		method : 'get',
		parameters : '',
		//true---异步;false---同步.默认为true
		asynchronous : false
	});
	
	/*
	if( document.getElementById("exitActionFlag").value == "exitByCloseWindow" ){
		win = window.open("/cib/common/logout.jsp" ,"","top=10000,left=10000,height=1,width=1"); 
	}
	*/
}
function logout(){
		document.getElementById("exitActionFlag").value = "exitByButton";
		toTarget('/cib/bankLogin.do?ActionMethod=logout&PageLanguage=<set:data name='PageLanguage'/>');
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
</script>
</head>
<body onLoad="" onBeforeUnload="confirmQuit();" onUnLoad ="closeWindow();">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="100%" align="right" background="/cib/images/logo-page.gif" style="background-repeat:no-repeat;-moz-background-size:100% 100%;background-size:auto 100%;">
    <table width="180" border="0" cellspacing="0" cellpadding="0" height="20">
      <tr>
        <td><img src="/cib/images/top-bar-start.png" width="20" height="20"></td>
        <td width="156" class="topmenu" bgcolor="#003366" nowrap align="center">&nbsp;&nbsp;|&nbsp;&nbsp; 
		<a onClick="toTarget('/cib/bankWelcome.do?ActionMethod=load')" href="#"><set:label name="Home" defaultvalue="Home"/></a>
		 &nbsp;&nbsp;|&nbsp;&nbsp; 
		 <a onClick="msgbox('Macao Development Bank','<set:label name='logoutMessage'/>','logout()',1,1)" target="topFrame" href="#"><set:label name="Logout" defaultvalue="Logout"/></a>
		 &nbsp;&nbsp;|&nbsp;&nbsp;</td>
        <td width="20"><img src="/cib/images/top-bar_end.png" width="20" height="20"></td>
      </tr>
    </table>
        <table width="80" border="0" cellpadding="0" cellspacing="4" bgcolor="#FFFFFF">
          <tr>
            <td><table width="" border="0" cellpadding="0" cellspacing="0">
                <tr>
				  <td><img src="/cib/images/userinfo_left.gif"></td>
                  <td bgcolor="E7E7E7"><table width="180" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="25%" class="userinfo_title"><set:label name="User" defaultvalue="USER"/></td>
                        <td class="userinfo"><set:data name="userId"/></td>
                      </tr>
                      <tr>
                        <td class="userinfo_title"><set:label name="Name" defaultvalue="NAME"/></td>
                        <td class="userinfo"><set:data name="userName" maxlen="14"/></td>
                      </tr>
                      <tr>
                        <td class="userinfo_title"><set:label name="Role" defaultvalue="ROLE"/></td>
                        <td class="userinfo"><set:data name="roleId" rb="app.cib.resource.common.bank_role1"/></td>
                      </tr>
                  </table>
				  </td>
				  <td><img src="/cib/images/userinfo_right.gif"></td>
                </tr>
            </table></td>
          </tr>
      </table></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="26" valign="top" background="/cib/images/menu_tag_bg.gif"><table border="0" cellspacing="0" cellpadding="0" height="22">
      <tr>
		<set:menu type="group" mid="main" level="level0">
		<set:menu level="level1" inlevel="level0">
        <td id="lmenu_<set:menuvalue property='mid' inlevel='level1' />" class="lmenu1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
        <td id="menu_<set:menuvalue property='mid' inlevel='level1' />" class="menu1_c"><a href="#" onClick="toggleMenu('<set:menuvalue property='mid' inlevel='level1' />');selDefualtMenu_<set:menuvalue property='mid' inlevel='level1'/>();"> <set:menuvalue property='label' inlevel='level1' /> </a> </td>
        <td id="rmenu_<set:menuvalue property='mid' inlevel='level1' />" class="rmenu1_c"><IMG height=22 src="/cib/images/shim.gif" width=9></td>
		</set:menu>
		</set:menu>
      </tr>
    </table></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="menu2_b">
<set:menu type="group" mid="main" level="level0">
<set:menu level="level1" inlevel="level0">
<div id="submenu_<set:menuvalue property='mid' inlevel='level1' />" style="display: none">
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="menu2_b">
              <table border="0" cellspacing="0" cellpadding="0" height="32">
                <tr>
                  <td class="menu2_c">&nbsp;</td>
		  <set:menu level="level2" inlevel="level1">
        <td id="lmenu2_<set:menuvalue property='mid' inlevel='level2' />" class="lmenu2_c"><img src="/cib/images/shim.gif" width="3" height="32"></td>
                  <td class="menu2_c" id="menu2_<set:menuvalue property='mid' inlevel='level2' />" onClick="toggleMenu2('<set:menuvalue property='mid' inlevel='level2' />')">
				  <set:menuif property="@COUNT" condition="equals" value="1" inlevel='level1'>
<script language="JavaScript">
function selDefualtMenu_<set:menuvalue property='mid' inlevel='level1'/>(){
	toggleMenu2('<set:menuvalue property='mid' inlevel='level2' />');
	window.top.mainFrame.location="<set:menuvalue property='path' inlevel='level2' />";
}
</script>				  
				  </set:menuif>
                    <a href="#" onClick="toTarget('<set:menuvalue property='path' inlevel='level2' />')">
<set:menuvalue property='label' inlevel='level2' /></a>
				  </td>
        <td id="rmenu2_<set:menuvalue property='mid' inlevel='level2' />" class="rmenu2_c"><IMG src="/cib/images/shim.gif" width="3" height="32"></td>
        <td class="menu2_d"><IMG src="/cib/images/shim.gif" width="8" height="32"></td>
</set:menu>
                </tr>
            </table></td>
          </tr>
  </table>
</div>  
</set:menu>
</set:menu>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr><td class="menu2_b" height="32">&nbsp;</td>
</tr>
</table>	
<!--
	</td>
    <td width="115" valign="top" class="menu2_clock"><div id="TimeTag" name="TimeTag"></div></td>
-->
  </tr>
</table>
<input id="exitActionFlag" name="exitActionFlag" type="hidden" value="exitByCloseWindow">
<form name="goToTarget" method="POST" target="mainFrame">
</form>

</body>
</set:loadrb>
</html>
