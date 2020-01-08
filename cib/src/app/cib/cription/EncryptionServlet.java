package app.cib.cription;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPublicKey;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Encoder;

import com.neturbo.set.core.Log;
import com.neturbo.set.utils.Utils;

/**
 * add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB
 * @author long_zg
 *
 */
public class EncryptionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Default constructor.
	 */

	public EncryptionServlet() {

		// TODO Auto-generated constructor stub

	}

	/**
	 * 
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void service(HttpServletRequest request,

	HttpServletResponse response) throws ServletException, IOException {

		String getPublicKey = Utils.null2Empty(request
				.getParameter("getPublicKey"));
		String handshake = Utils.null2Empty(request.getParameter("handshake"));
		if (!"".equals(getPublicKey)) {
			try {
				this.getPublicKey(request, response);
			} catch (Exception e) {
				Log.error(e) ;
				throw new ServletException(e.getMessage()) ;
			}
		}
		if (!"".equals(handshake)) {
			try {
				handshake(request, response);
			} catch (Exception e) {
				Log.error(e) ;
				throw new ServletException(e.getMessage()) ;
			}
		}
	}

	public void getPublicKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		RSAEncrypt enctrypt = new RSAEncrypt() ;
		
//		RSAPublicKey publicKey = enctrypt.loadPublicKey(in) ;
		
		/*RSAPublicKey publicKey = enctrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY) ;
		
		String publicKeyStr = (new BASE64Encoder()).encode(publicKey.getEncoded());*/

		RSAPublicKey publicKey = enctrypt.loadPublicKey() ;
		
		StringBuffer output = new StringBuffer();

		String publicKeyStr = (new BASE64Encoder()).encode(publicKey.getEncoded());
		
		output.append("{\"publickey\":\"" + publicKeyStr + "\"}") ;

		response.getOutputStream().print(
				output.toString().replaceAll("\r", "").replaceAll("\n", "")
						.trim());

	}

	public void handshake(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String key = request.getParameter("key");
		
		RSAEncrypt rsaEnctypt = new RSAEncrypt();
		
		/*RSAPrivateKey rsaPrivateKey = rsaEnctypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY) ;
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
		byte[] bytes = Base64.decodeBase64(key);
		bytes = cipher.doFinal(bytes);
		String decryptKey = new String(bytes);*/
		
		
//		String ssss = AES.Encrypt(decryptKey,decryptKey) ;
		
		String decryptKey = rsaEnctypt.decryption(key) ;
		
		String encryptContent = AESUtil.encryptJC(decryptKey, decryptKey) ;
		
		request.getSession().setAttribute("jCryptionKey", decryptKey) ;
		
		StringBuffer output = new StringBuffer();
		output.append("{\"challenge\":\"" + encryptContent +"\"}");
//		response.getOutputStream().print(output.toString());
		response.getOutputStream().print(
				output.toString().replaceAll("\r", "").replaceAll("\n", "")
						.trim());
	}

}
