<%@ page import="app.cib.cription.*" %>
<%@ page import="sun.misc.BASE64Encoder" %>
<%@ page import="java.security.interfaces.RSAPublicKey" %>
<%@ page import="java.security.interfaces.RSAPrivateKey" %>


<%@ page contentType="text/html; charset=utf-8" %>
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta http-equiv="Cache-Control" content="no-cache">
  <meta http-equiv="Pragma" content="no-cache">
  <title>security test</title>
  </head>
  <body>
<%
	String publicKeyStr = "" ;
	String cryptResult = "" ;
	String decryptResult = "" ;
	
	try {
		RSAEncrypt enctrypt = new RSAEncrypt() ;
		
		RSAPublicKey publicKey = enctrypt.loadPublicKey() ;
			
		StringBuffer output = new StringBuffer();
	
		publicKeyStr = (new BASE64Encoder()).encode(publicKey.getEncoded());
	
		cryptResult = AESUtil.encryptJC(
					"c49756e8c15d54d064cd74b383dedfa65efc0e47d9fad516043a2676baa39447",
					"c49756e8c15d54d064cd74b383dedfa65efc0e47d9fad516043a2676baa39447");

		decryptResult = AESUtil.decryptJC(
					"U2FsdGVkX18xMjM0NTY3OP0fscxtdswaRRfp1e1SHvHJTKosRX56bnmlp4K49xbRLzBj49U9s/hyKjfxcRkqsrnW6zfrO5TVr6dCOMKor16y9JwSaqn8drA38pP0LHk6",
					"c49756e8c15d54d064cd74b383dedfa65efc0e47d9fad516043a2676baa39447");
		%>
        <font color="#FF0000">crypt success</font><br>
        public key = <%=publicKeyStr%><br>
        cryptResult = <%=cryptResult%><br>
        decryptResult = <%=decryptResult%>
        <%

		} catch (Exception e) {
%>
			<font color="#FF0000">crypt error</font>
<%
		}
	
%>

  </body>
</html>
