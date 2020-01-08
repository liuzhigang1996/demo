package app.cib.cription;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;

import com.neturbo.set.core.Config;

public class RSAEncrypt {
	
	public static final String DEFAULT_PUBLIC_KEY= 
		"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChDzcjw/rWgFwnxunbKp7/4e8w" + "\r" +
		"/UmXx2jk6qEEn69t6N2R1i/LmcyDT1xr/T2AHGOiXNQ5V8W4iCaaeNawi7aJaRht" + "\r" +
		"Vx1uOH/2U378fscEESEG8XDqll0GCfB1/TjKI2aitVSzXOtRs8kYgGU78f7VmDNg" + "\r" +
		"XIlk3gdhnzh+uoEQywIDAQAB" + "\r";
	
	public static final String DEFAULT_PRIVATE_KEY=
		"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKEPNyPD+taAXCfG" + "\r" +
		"6dsqnv/h7zD9SZfHaOTqoQSfr23o3ZHWL8uZzINPXGv9PYAcY6Jc1DlXxbiIJpp4" + "\r" +
		"1rCLtolpGG1XHW44f/ZTfvx+xwQRIQbxcOqWXQYJ8HX9OMojZqK1VLNc61GzyRiA" + "\r" +
		"ZTvx/tWYM2BciWTeB2GfOH66gRDLAgMBAAECgYBp4qTvoJKynuT3SbDJY/XwaEtm" + "\r" +
		"u768SF9P0GlXrtwYuDWjAVue0VhBI9WxMWZTaVafkcP8hxX4QZqPh84td0zjcq3j" + "\r" +
		"DLOegAFJkIorGzq5FyK7ydBoU1TLjFV459c8dTZMTu+LgsOTD11/V/Jr4NJxIudo" + "\r" +
		"MBQ3c4cHmOoYv4uzkQJBANR+7Fc3e6oZgqTOesqPSPqljbsdF9E4x4eDFuOecCkJ" + "\r" +
		"DvVLOOoAzvtHfAiUp+H3fk4hXRpALiNBEHiIdhIuX2UCQQDCCHiPHFd4gC58yyCM" + "\r" +
		"6Leqkmoa+6YpfRb3oxykLBXcWx7DtbX+ayKy5OQmnkEG+MW8XB8wAdiUl0/tb6cQ" + "\r" +
		"FaRvAkBhvP94Hk0DMDinFVHlWYJ3xy4pongSA8vCyMj+aSGtvjzjFnZXK4gIjBjA" + "\r" +
		"2Z9ekDfIOBBawqp2DLdGuX2VXz8BAkByMuIh+KBSv76cnEDwLhfLQJlKgEnvqTvX" + "\r" +
		"TB0TUw8avlaBAXW34/5sI+NUB1hmbgyTK/T/IFcEPXpBWLGO+e3pAkAGWLpnH0Zh" + "\r" +
		"Fae7oAqkMAd3xCNY6ec180tAe57hZ6kS+SYLKwb4gGzYaCxc22vMtYksXHtUeamo" + "\r" +
		"1NMLzI2ZfUoX" + "\r";

	/**
	 * ˽Կ
	 */
	private RSAPrivateKey privateKey;

	/**
	 * ��Կ
	 */
	private RSAPublicKey publicKey;
	
	/**
	 * �ֽ�����ת�ַ���ר�ü���
	 */
	private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	

	/**
	 * ��ȡ˽Կ
	 * @return ��ǰ��˽Կ����
	 */
	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * ��ȡ��Կ
	 * @return ��ǰ�Ĺ�Կ����
	 */
	public RSAPublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * ���������Կ��
	 */
	public void genKeyPair(){
		KeyPairGenerator keyPairGen= null;
		try {
			keyPairGen= KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair= keyPairGen.generateKeyPair();
		this.privateKey= (RSAPrivateKey) keyPair.getPrivate();
		this.publicKey= (RSAPublicKey) keyPair.getPublic();
	}

	/**
	 * ���ļ����������м��ع�Կ
	 * @param in ��Կ������
	 * @throws Exception ���ع�Կʱ�������쳣
	 */
	public RSAPublicKey loadPublicKey(InputStream in) throws Exception{
		try {
			BufferedReader br= new BufferedReader(new InputStreamReader(in));
			String readLine= null;
			StringBuilder sb= new StringBuilder();
			while((readLine= br.readLine())!=null){
				if(readLine.charAt(0)=='-'){
					continue;
				}else{
					sb.append(readLine);
					sb.append('\r');
				}
			}
			return loadPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("��Կ��������ȡ����");
		} catch (NullPointerException e) {
			throw new Exception("��Կ������Ϊ��");
		}
	}


	/**
	 * ���ַ����м��ع�Կ
	 * @param publicKeyStr ��Կ�����ַ���
	 * @throws Exception ���ع�Կʱ�������쳣
	 */
	public RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception{
		try {
			BASE64Decoder base64Decoder= new BASE64Decoder();
			byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);
			KeyFactory keyFactory= KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("�޴��㷨");
		} catch (InvalidKeySpecException e) {
			throw new Exception("��Կ�Ƿ�");
		} catch (IOException e) {
			throw new Exception("��Կ�������ݶ�ȡ����");
		} catch (NullPointerException e) {
			throw new Exception("��Կ����Ϊ��");
		}
	}

	/**
	 * ���ļ��м���˽Կ
	 * @param keyFileName ˽Կ�ļ���
	 * @return �Ƿ�ɹ�
	 * @throws Exception 
	 */
	public RSAPrivateKey loadPrivateKey(InputStream in) throws Exception{
		try {
			BufferedReader br= new BufferedReader(new InputStreamReader(in));
			String readLine= null;
			StringBuilder sb= new StringBuilder();
			while((readLine= br.readLine())!=null){
				if(readLine.charAt(0)=='-'){
					continue;
				}else{
					sb.append(readLine);
					sb.append('\r');
				}
			}
			return loadPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("˽Կ���ݶ�ȡ����");
		} catch (NullPointerException e) {
			throw new Exception("˽Կ������Ϊ��");
		}
	}

	public RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception{
		try {
//			BASE64Decoder base64Decoder= new BASE64Decoder();
//			byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);
//			RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(buffer));  
//			RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());  
//			KeyFactory keyFactory= KeyFactory.getInstance("RSA");  
//			PrivateKey priKey= keyFactory.generatePrivate(rsaPrivKeySpec); 
			
			BASE64Decoder base64Decoder= new BASE64Decoder();  
            byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);  
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);  
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");  
            PrivateKey priKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec); 
            
			return (RSAPrivateKey) priKey;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("�޴��㷨");
		} catch (InvalidKeySpecException e) {
			throw new Exception("˽Կ�Ƿ�");
		} catch (IOException e) {
			throw new Exception("˽Կ�������ݶ�ȡ����");
		} catch (NullPointerException e) {
			throw new Exception("˽Կ����Ϊ��");
		}
	}

	/**
	 * ���ܹ���
	 * @param publicKey ��Կ
	 * @param plainTextData ��������
	 * @return
	 * @throws Exception ���ܹ����е��쳣��Ϣ
	 */
	public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception{
		if(publicKey== null){
			throw new Exception("���ܹ�ԿΪ��, ������");
		}
		Cipher cipher= null;
		try {
			cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output= cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("�޴˼����㷨");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}catch (InvalidKeyException e) {
			throw new Exception("���ܹ�Կ�Ƿ�,����");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("���ĳ��ȷǷ�");
		} catch (BadPaddingException e) {
			throw new Exception("������������");
		}
	}

	/**
	 * ���ܹ���
	 * @param privateKey ˽Կ
	 * @param cipherData ��������
	 * @return ����
	 * @throws Exception ���ܹ����е��쳣��Ϣ
	 */
	public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception{
		if (privateKey== null){
			throw new Exception("����˽ԿΪ��, ������");
		}
		Cipher cipher= null;
		try {
			cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output= cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("�޴˽����㷨");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}catch (InvalidKeyException e) {
			throw new Exception("����˽Կ�Ƿ�,����");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("���ĳ��ȷǷ�");
		} catch (BadPaddingException e) {
			throw new Exception("������������");
		}		
	}

	
	/**
	 * �ֽ�����תʮ�������ַ���
	 * @param data ��������
	 * @return ʮ����������
	 */
	public static String byteArrayToString(byte[] data){
		StringBuilder stringBuilder= new StringBuilder();
		for (int i=0; i<data.length; i++){
			//ȡ���ֽڵĸ���λ ��Ϊ�����õ���Ӧ��ʮ�����Ʊ�ʶ�� ע���޷�������
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);
			//ȡ���ֽڵĵ���λ ��Ϊ�����õ���Ӧ��ʮ�����Ʊ�ʶ��
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
			if (i<data.length-1){
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}


	public static void main(String[] args){
		RSAEncrypt rsaEncrypt= new RSAEncrypt();
		//rsaEncrypt.genKeyPair();

		//���ع�Կ
		try {
			rsaEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY);
			System.out.println("���ع�Կ�ɹ�");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("���ع�Կʧ��");
		}

		//����˽Կ
		try {
			rsaEncrypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY);
			System.out.println("����˽Կ�ɹ�");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("����˽Կʧ��");
		}

		//�����ַ���
		String encryptStr= "Test String chaijunkun";

		try {
			//����
			byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), encryptStr.getBytes());
			//����
			byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), cipher);
			System.out.println("���ĳ���:"+ cipher.length);
			System.out.println(RSAEncrypt.byteArrayToString(cipher));
			System.out.println("���ĳ���:"+ plainText.length);
			System.out.println(RSAEncrypt.byteArrayToString(plainText));
			System.out.println(new String(plainText));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public String loadKeyContent(String path) throws Exception{
		try {
			File file = new File(path) ;
			InputStream in = new FileInputStream(file) ;
			BufferedReader br= new BufferedReader(new InputStreamReader(in));
			String readLine= null;
			StringBuilder sb= new StringBuilder();
			while((readLine= br.readLine())!=null){
				if(readLine.charAt(0)=='-'){
					continue;
				}else{
					sb.append(readLine);
					sb.append('\r');
				}
			}
			return sb.toString();
		} catch (IOException e) {
			throw new Exception("���ݶ�ȡ����");
		} catch (NullPointerException e) {
			throw new Exception("������Ϊ��");
		}
	}
	
	public RSAPublicKey loadPublicKey() throws Exception{
		String path = Config.getProperty("app.public_key.path").trim() ;
		if(null!=path && !"".equals(path)){
			File file = new File(path) ;
			InputStream in = new FileInputStream(file) ;
			return this.loadPublicKey(in) ;
		}
		return this.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY) ;
	}
	
	public RSAPrivateKey loadPrivateKey() throws Exception{
		String path = Config.getProperty("app.private_key.path").trim() ;
		if(null!=path && !"".equals(path)){
			File file = new File(path) ;
			InputStream in = new FileInputStream(file) ;
			return this.loadPrivateKey(in) ;
		}
		return this.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY) ;
	}
	
	public String decryption(String key) throws Exception{
		RSAPrivateKey rsaPrivateKey = this.loadPrivateKey() ;
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
		byte[] bytes = Base64.decodeBase64(key);
		bytes = cipher.doFinal(bytes);
		String decryptKey = new String(bytes);
		return decryptKey ;
	}
}