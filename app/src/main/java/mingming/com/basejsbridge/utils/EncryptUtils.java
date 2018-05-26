package mingming.com.basejsbridge.utils;

import android.annotation.SuppressLint;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @see 3DES加密
 * @author lenvov
 */
public class EncryptUtils {

	// 加解密统一使用的编码方式
	private final static String encoding = "utf-8";

	/**
	 * 3DES加密
	 * @param plainText 待加密文本
	 * @param secretKey 约定的key,至少24位，例如：String secretKey = "zhuxiaojie123@lx100$#365";
	 *  iv向量 例如： String iv = "12345678";
	 * @return 密文
	 * @throws Exception: 如果secretKey小于24位，InvalidKeyException
	 */
	@SuppressLint("TrulyRandom")
	public static String encrypt(String plainText, String secretKey, String iv)
			throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
		return Base64.encode(encryptData);

	}

	/**
	 * 3DES解密
	 *  encryptText密文
	 * secretKey加密时的key
	 *  iv加密时使用的向量
	 * @return 返回解密后的明文
	 * @throws Exception
	 */
	public static String decrypt(String encryptText, String secretKey, String iv)
			throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

		return new String(decryptData, encoding);
	}
}
