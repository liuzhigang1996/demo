<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.neturbo.set.core.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  <script language="javascript">
function setCookie() { 
	//alert(10) ;
	var currentDate= new Date() ;
	var tem = "" ;
	var year = currentDate.getFullYear()+""; 
	var month = currentDate.getMonth()+1;
	var date = currentDate.getDate(); 
	var hour = currentDate.getHours(); 
	var minute = currentDate.getMinutes();
	var second = currentDate.getSeconds();
	var milliSecond = currentDate.getMilliseconds(); 
	tem = "" + year + (month<10?("0"+month):month) + (date<10?("0"+date):date) + (hour<10?("0"+hour):hour) + (minute<10?("0"+minute):minute) + (second<10?("0"+second):second) + milliSecond ;
	//alert(tem) ;
   var name = "the_third_cookie_t_" + tem;
   //alert(name) ;
   var value= "the_third_cookie_t" ;
   document.cookie = name + "="+ escape(value) +";expires="; 

	var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)")); 
	var cookieFlag = "N" ;
	if(arr !=null) {
		cookieFlag = "Y" ;
		//ɾ��cookie
		var exp = new Date();  
		exp.setTime(exp.getTime() - 1); 
		document.cookie= name + "="+value+";expires="+exp.toGMTString(); 
	} 
	var bolUrl ="<%=Config.getProperty("app.stockTrading.bolUrl") %>";
	//alert(bolUrl) ;
	//window.location.href="http://192.168.1.105:8080/ebank/bank/cookieRes.jsp?cookieFlag=" + cookieFlag;
	window.location.href=bolUrl +"?cookieFlag=" + cookieFlag;
}

</script>
  <body onload="setCookie()">
    sfdsfdsf
  </body>
</html>
