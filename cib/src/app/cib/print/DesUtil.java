package app.cib.print;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesUtil {


	private final static String DES = "DES";


	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {

		SecureRandom sr = new SecureRandom();

		DESKeySpec dks = new DESKeySpec(key);

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		Cipher cipher = Cipher.getInstance(DES);

		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(src);
	}


	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {

		SecureRandom sr = new SecureRandom();

		DESKeySpec dks = new DESKeySpec(key);

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		Cipher cipher = Cipher.getInstance(DES);

		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(src);
	}


	public final static String decrypt(String data, String ENCRYPT_KEY) {
		try {
			if (data == null || "".equals(data))
				return data;
			return new String(decrypt(hex2byte(data.getBytes()),
					ENCRYPT_KEY.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public final static String encrypt(String password, String ENCRYPT_KEY) {
		try {
			String strc = password;
			strc = new String(strc.getBytes());
			return byte2hex(encrypt(password.getBytes(), ENCRYPT_KEY.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("Hex String Error");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	public static void main(String[] args) {
		System.out.println(encrypt("228dAa_&9", "i2A364C5d6E7f89O"));
		System.out.println(decrypt("93D489AF4ABA14FB195C336F9F787AE5", "i2A364C5d6E7f89O"));
	}
}