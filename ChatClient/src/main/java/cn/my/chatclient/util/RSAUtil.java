package cn.my.chatclient.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

//import cn.my.chat.exception.ErrorCodes;
//import cn.my.chat.exception.Exceptions;

public class RSAUtil {

	private static String getKey(String filename) throws IOException {
		// Read key from file
		String strKeyPEM = "";
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = br.readLine()) != null) {
			strKeyPEM += line + "n";
		}
		br.close();
		return strKeyPEM;
	}

	/**
	 * Constructs a private key (RSA) from the given file
	 *
	 * @param filename
	 *            PEM Private Key
	 * @return RSA Private Key
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static RSAPrivateKey getPrivateKey(String filename) throws IOException, GeneralSecurityException {
		String privateKeyPEM = getKey(filename);
		return getPrivateKeyFromString(privateKeyPEM);
	}

	/**
	 * Constructs a private key (RSA) from the given string
	 *
	 * @param key
	 *            PEM Private Key
	 * @return RSA Private Key
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static RSAPrivateKey getPrivateKeyFromString(String key) throws IOException, GeneralSecurityException {
		String privateKeyPEM = key;

		// Remove the first and last lines
		privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----n", "");
		privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");

		// Base64 decode data
		byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

		KeyFactory kf = KeyFactory.getInstance("RSA");
		RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));
		return privKey;
	}

	/**
	 * Constructs a public key (RSA) from the given file
	 *
	 * @param filename
	 *            PEM Public Key
	 * @return RSA Public Key
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static RSAPublicKey getPublicKey(String filename) throws IOException, GeneralSecurityException {
		String publicKeyPEM = getKey(filename);
		return getPublicKeyFromString(publicKeyPEM);
	}

	/**
	 * Constructs a public key (RSA) from the given string
	 *
	 * @param key
	 *            PEM Public Key
	 * @return RSA Public Key
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static RSAPublicKey getPublicKeyFromString(String key) throws IOException, GeneralSecurityException {
		String publicKeyPEM = key;

		// Remove the first and last lines
		publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----n", "");
		publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

		// Base64 decode data
		byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

		KeyFactory kf = KeyFactory.getInstance("RSA");
		RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
		return pubKey;
	}

	/**
	 * @param privateKey
	 * @param message
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	public static String sign(PrivateKey privateKey, String message)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
		Signature sign = Signature.getInstance("SHA1withRSA");
		sign.initSign(privateKey);
		sign.update(message.getBytes("UTF-8"));
		return new String(Base64.getDecoder().decode(sign.sign()), "UTF-8");
	}

	/**
	 * @param publicKey
	 * @param message
	 * @param signature
	 * @return
	 * @throws SignatureException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public static boolean verify(PublicKey publicKey, String message, String signature)
			throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
		Signature sign = Signature.getInstance("SHA1withRSA");
		sign.initVerify(publicKey);
		sign.update(message.getBytes("UTF-8"));
		return sign.verify(Base64.getDecoder().decode(signature.getBytes("UTF-8")));
	}

	/**
	 * Encrypts the text with the public key (RSA)
	 *
	 * @param rawText
	 *            Text to be encrypted
	 * @param publicKey
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static String encrypt(String rawText, PublicKey publicKey) throws IOException, GeneralSecurityException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(rawText.getBytes("UTF-8")));
	}

	/**
	 * Decrypts the text with the private key (RSA)
	 *
	 * @param cipherText
	 *            Text to be decrypted
	 * @param privateKey
	 * @return Decrypted text (Base64 encoded)
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static String decrypt(String cipherText, PrivateKey privateKey)
			throws IOException, GeneralSecurityException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)), "UTF-8");
	}

	public static String decode(String source, String privateKey) {
		try {
			RSAPrivateKey key = getPrivateKeyFromString(privateKey);
			return decrypt(source, key);
		} catch (Exception e) {
//			throw Exceptions.fail(ErrorCodes.ILEGALDATA);
			e.printStackTrace();
			return null;
		}
	}

	public static String encode(String source, String publicKey) {
		try {
			RSAPublicKey key = getPublicKeyFromString(publicKey);
			return encrypt(source, key);
		} catch (Exception e) {
//			throw Exceptions.fail(ErrorCodes.ILEGALDATA);
			e.printStackTrace();
			return null;
		}
	}
}
