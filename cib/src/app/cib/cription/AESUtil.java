package app.cib.cription;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.EscapeChar;

public class AESUtil {
	/**
	 * 将16进制转换为二进制
	 * 
	 * 　　* @param hexStr
	 * 
	 * 　　* @return
	 * 
	 * 　　
	 */

	public static byte[] parseHexStr2Byte(String hexStr) {

		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}

		return result;

	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * 　　* @param buf
	 * 
	 * 　　* @return
	 * 
	 * 　　
	 */

	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	public static String getBASE64(byte[] b) {
		String s = null;
		if (b != null) {
			s = new sun.misc.BASE64Encoder().encode(b);
		}
		return s;
	}

	public static byte[] getFromBASE64(String s) {
		byte[] b = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				return b;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	public static String md5(String inStr) {
		String resultStr = null;
		try {
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(inStr.getBytes("ISO8859-1"));
			//System.out.println(parseByte2HexStr(inStr.getBytes("ISO8859-1")));
			byte resultBytes[] = messagedigest.digest();
			// resultStr = Utils.bytes2HexStr(resultBytes);
			resultStr = new String(resultBytes, "ISO8859-1");
		} catch (Exception exception) {
			Log.error("Exception in digest(String)", exception);
			resultStr = null;
		}
		return resultStr;
	}

	public static String encryptJC(String content, String password) throws NTBException{

		try {
			// Set a random salt
			// $salt = openssl_random_pseudo_bytes(8);
			String salt = getRandomString(8);

			String salted = "";
			String dx = "";
			// Salt the key(32) and iv(16) = 48
			while (salted.length() < 48) {
				dx = md5(dx + password + salt);
				salted += dx;
			}

			String key = salted.substring(0, 32);
			String iv = salted.substring(32, 48);

//			//System.out.println("salted="
//					+ parseByte2HexStr(salted.getBytes("ISO8859-1")));
//			//System.out.println(parseByte2HexStr(key.getBytes("ISO8859-1")));
//			//System.out.println(parseByte2HexStr(iv.getBytes("ISO8859-1")));

			// Security.addProvider(new BouncyCastleProvider());

			// KeyGenerator kgen = KeyGenerator.getInstance("AES", "BC");
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(256, new SecureRandom(key.getBytes("ISO8859-1")));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();

			// SecretKeySpec keyspec = new SecretKeySpec(enCodeFormat, "AES");
			SecretKeySpec keyspec = new SecretKeySpec(
					key.getBytes("ISO8859-1"), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv
					.getBytes("ISO8859-1"));

			// Cipher in = Cipher.getInstance("AES/CBC/PKCS7Padding");
			// Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding",
			// "BC");// 创建密码器
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器

			byte[] byteContent = content.getBytes("ISO8859-1");
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);// 初始化
			byte[] result = cipher.doFinal(byteContent);

			// $encrypted_data = openssl_encrypt($data, 'aes-256-cbc', $key,
			// true, $iv);
			//System.out.println("ct=" + parseByte2HexStr(result));
			String resultStr = "Salted__" + salt
					+ new String(result, "ISO8859-1");
			String resultBase64 = getBASE64(resultStr.getBytes("ISO8859-1"));

			return resultBase64; // 加密

		} catch (Exception e) {
			e.printStackTrace();
			throw new NTBException(e.getMessage()) ;
		}
//		return null;

	}

	public static String decryptJC(String content, String password) throws NTBException{

		try {
			// Set a random salt
			// $salt = openssl_random_pseudo_bytes(8);
			byte[] contentBytes = getFromBASE64(content);
			String contentStr = new String(contentBytes, "ISO8859-1");

			String salt = contentStr.substring(8, 16);
			String ct = contentStr.substring(16);

			int rounds = 3;
			String data00 = password + salt;
			String md5_hash = md5(data00);
			String salted = md5_hash;
			// Salt the key(32) and iv(16) = 48
			for (int i = 1; i < rounds; i++) {
				md5_hash = md5(md5_hash + data00);
				salted += md5_hash;
			}

			String key = salted.substring(0, 32);
			String iv = salted.substring(32, 48);

			//System.out.println("ct="
//					+ parseByte2HexStr(ct.getBytes("ISO8859-1")));
			//System.out.println("salted="
//					+ parseByte2HexStr(salted.getBytes("ISO8859-1")));
			//System.out.println(parseByte2HexStr(key.getBytes("ISO8859-1")));
			//System.out.println(parseByte2HexStr(iv.getBytes("ISO8859-1")));

			// KeyGenerator kgen = KeyGenerator.getInstance("AES", "BC");
//			KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			kgen.init(256, new SecureRandom(key.getBytes("ISO8859-1")));
//			SecretKey secretKey = kgen.generateKey();
//			byte[] enCodeFormat = secretKey.getEncoded();

			// SecretKeySpec keyspec = new SecretKeySpec(enCodeFormat, "AES");
			SecretKeySpec keyspec = new SecretKeySpec(
					key.getBytes("ISO8859-1"), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv
					.getBytes("ISO8859-1"));

			// Cipher in = Cipher.getInstance("AES/CBC/PKCS7Padding");
			// Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding",
			// "BC");// 创建密码器
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器

			byte[] byteContent = ct.getBytes("ISO8859-1");
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);// 初始化
			byte[] result = cipher.doFinal(byteContent);

			String resultStr = new String(result, "ISO8859-1");

			return resultStr; // 加密

		} catch (Exception e) {
			e.printStackTrace();
			throw new NTBException(e.getMessage()) ;
		}
//		return null;

	}

	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	
	
	/**
	 * <!-- add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB -->
	 * @param paramter
	 * @return
	 */
	public static Map getParamFromUrl(String paramter) {
		Map paraMap = new HashMap() ;
		String[] param = paramter.split("&") ;
		for(int i=0;i<param.length;i++){
			String[] item = param[i].split("=") ;
			 
			if(paraMap.get(item[0])!=null){//如果是数组的话
				List<String> array = new ArrayList<String>();
				if (paraMap.get(item[0]) instanceof String){
					array.add((String) paraMap.get(item[0]));
				}else{
					String[] s =(String[])paraMap.get(item[0]);
					for(int j=0;j<s.length; j++){
						array.add(s[j]);
					}
				}
				array.add(1==item.length?"": EscapeChar.fromURL(item[1]));
				String[] s = new String[array.size()];
				s = array.toArray(s);
				paraMap.put(item[0],s);
			}else{
				paraMap.put(item[0], 1==item.length?"": EscapeChar.fromURL(item[1])) ;
			}
		}
		return paraMap ;
	}
	
	public static void main(String[] args) {
		String content = "wangxz";

		String password = "12345678";

		// 加密

		/*
		 * //System.out.println("加密前：" + content);
		 * 
		 * byte[] encryptResult = encrypt(content, password);
		 * 
		 * String encryptResultStr = parseByte2HexStr(encryptResult);
		 * 
		 * //System.out.println("加密后：" + encryptResultStr);
		 */

		// 解密
		try {

			// byte[] decryptResult = decrypt(decryptFrom, new
			// String(parseHexStr2Byte("9b19d2a0f32e5284c3fb101271654f0c755b5c7f5d1090800e4c286c5dc9d888")));
			String cryptResult = encryptJC(
					"c49756e8c15d54d064cd74b383dedfa65efc0e47d9fad516043a2676baa39447",
					"c49756e8c15d54d064cd74b383dedfa65efc0e47d9fad516043a2676baa39447");
			// c49756e8c15d54d064cd74b383dedfa65efc0e47d9fad516043a2676baa39447

			//System.out.println("加密后：" + cryptResult);

			String decryptResult = decryptJC(
					"U2FsdGVkX18xMjM0NTY3OP0fscxtdswaRRfp1e1SHvHJTKosRX56bnmlp4K49xbRLzBj49U9s/hyKjfxcRkqsrnW6zfrO5TVr6dCOMKor16y9JwSaqn8drA38pP0LHk6",
					"c49756e8c15d54d064cd74b383dedfa65efc0e47d9fad516043a2676baa39447");

			//System.out.println("解密后：" + decryptResult);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}