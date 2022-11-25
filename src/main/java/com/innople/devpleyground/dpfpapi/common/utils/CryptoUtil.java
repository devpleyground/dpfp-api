package com.innople.devpleyground.dpfpapi.common.utils;

import com.innople.devpleyground.dpfpapi.common.constants.ErrorConstants.ErrorCode;
import com.innople.devpleyground.dpfpapi.common.constants.GlobalConstants;
import com.innople.devpleyground.dpfpapi.common.exceptions.DpfpCustomException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64.Decoder;

public class CryptoUtil {
    /* MD5 해시 암호화 함수 : 복호화 X */
    public static String getMD5HashString(String target) throws Exception{
        StringBuilder sb = new StringBuilder();

        MessageDigest mDigest = MessageDigest.getInstance("MD5");
        mDigest.update(target.getBytes());

        byte[] msgStr = mDigest.digest();

        for (byte b : msgStr) {
            String tmpEncTxt = Integer.toHexString((int) b & 0x00ff);
            sb.append(tmpEncTxt);
        }

        return sb.toString();
    }

    /* SHA256 해시 암호화 함수 : 복호화 X */
    public static String getSHA256HashString(String target) throws Exception {
        return getSHAHashString(target, "SHA-256");
    }

    /* SHA512 해시 암호화 함수 : 복호화 X */
    public static String getSHA512HashString(String target) throws Exception {
        return getSHAHashString(target, "SHA-512");
    }

    /* SHA 해시 암호화 함수 */
    public static String getSHAHashString(String target, String algorithm) throws Exception {
        StringBuffer hexString = new StringBuffer();

        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hash = digest.digest(target.getBytes("UTF-8"));

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /* FrontEnd에서 AES256으로 암호화된 값을 복호화하는 함수 - START */
    public static String getAES256DecodedString(String ciphertext) {
        return getAES256DecodedString(ciphertext, GlobalConstants.defaultAES256CryptoKey);
    }

    public static String getAES256DecodedString(String ciphertext, String passphrase) {
        try {
            final int keySize = 256;
            final int ivSize = 128;

            // 텍스트를 BASE64 형식으로 디코드 한다.
            byte[] ctBytes = Base64.decodeBase64(ciphertext.getBytes("UTF-8"));

            // 솔트를 구한다. (생략된 8비트는 Salted__ 시작되는 문자열이다.)
            byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);
            System.out.println( Hex.encodeHexString(saltBytes) );

            // 암호화된 텍스트를 구한다.( 솔트값 이후가 암호화된 텍스트 값이다.)
            byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);

            // 비밀번호와 솔트에서 키와 IV값을 가져온다.
            byte[] key = new byte[keySize / 8];
            byte[] iv = new byte[ivSize / 8];
            EvpKDF(passphrase.getBytes("UTF-8"), keySize, ivSize, saltBytes, key, iv);

            // 복호화
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);

            return new String(recoveredPlaintextBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /* FrontEnd에서 AES256으로 암호화된 값을 복호화하는 함수 - END  */

    /* Backend 에서 AES256 방식으로 함오화하는 함수 - SRART  */
    public static String encryptStringWithAES256(String text) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return encryptStringWithAES256(text, GlobalConstants.defaultAES256CryptoKey);
    }

    public static String encryptStringWithAES256(String text, String passphrase) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        final byte[] keyBytes = passphrase.getBytes();
        final String iv = passphrase.substring(0, 16); // 16byte

        SecretKey secureKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));

        return enStr;
    }
    /* Backend 에서 AES256 방식으로 함오화하는 함수 - END  */

    /* Backend 에서 AES256 방식으로 복오화하는 함수 - SRART  */
    public static String decryptStringWithAES256(String text) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException  {
        return decryptStringWithAES256(text, GlobalConstants.defaultAES256CryptoKey);
    }

    public static String decryptStringWithAES256(String text, String passphrase) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException  {
        final byte[] keyBytes = passphrase.getBytes();
        final String iv = passphrase.substring(0, 16); // 16byte

        SecretKey secureKey = new SecretKeySpec(keyBytes, "AES");

        Cipher chiper = Cipher.getInstance("AES/CBC/PKCS5Padding");
        chiper.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes("UTF-8")));

        byte[] byteStr = Base64.decodeBase64(text.getBytes());

        return new String(chiper.doFinal(byteStr), "UTF-8");
    }
    /* Backend 에서 AES256 방식으로 복오화하는 함수 - END */

    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        return EvpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
    }

    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        keySize = keySize / 32;
        ivSize = ivSize / 32;
        int targetKeySize = keySize + ivSize;
        byte[] derivedBytes = new byte[targetKeySize * 4];
        int numberOfDerivedWords = 0;
        byte[] block = null;
        MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
        while (numberOfDerivedWords < targetKeySize) {
            if (block != null) {
                hasher.update(block);
            }
            hasher.update(password);
            // Salting
            block = hasher.digest(salt);
            hasher.reset();
            // Iterations : 키 스트레칭(key stretching)
            for (int i = 1; i < iterations; i++) {
                block = hasher.digest(block);
                hasher.reset();
            }
            System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4, Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));
            numberOfDerivedWords += block.length / 4;
        }
        System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
        System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);
        return derivedBytes; // key + iv
    }

    public static boolean matchesMD5(CharSequence rawPassword, String encodedPassword) throws Exception{
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        return MessageDigest.isEqual(getMD5HashString(rawPassword.toString()).getBytes(StandardCharsets.UTF_8), encodedPassword.getBytes(StandardCharsets.UTF_8));
    }

    public static String getDecryptedSharedKey(String encodedSharedKey) throws Exception {
        try {
            Decoder decoder = java.util.Base64.getUrlDecoder();
            String decodedSharedKey = new String(decoder.decode(encodedSharedKey.getBytes("utf-8")));
            String decryptedSharedKey = CryptoUtil.decryptStringWithAES256(decodedSharedKey);
            if(decryptedSharedKey.indexOf("^") < 0 || decryptedSharedKey.split("\\^").length != 3)
                throw new DpfpCustomException(ErrorCode.Invalid_Parameter);

            return decryptedSharedKey;
        }
        catch(Exception e) {
            throw e;
        }
    }

    //AES 128 - CBC : 암호화
    public static String encryptStringWithAES128(String text, String keyP, String ivP) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

        final String key = keyP.substring(0, 16); // 16byte
        final String iv  = ivP.substring(0, 16);  // 16byte

        final byte[] keyBytes = key.getBytes();

        SecretKey secureKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));

        return enStr;
    }

    //AES 128 - CBC : 복호화
    public static String decryptStringWithAES128(String text, String keyP, String ivP) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException  {

        final String key = keyP.substring(0, 16); // 16byte
        final String iv  = ivP.substring(0, 16);  // 16byte

        final byte[] keyBytes = key.getBytes();

        SecretKey secureKey = new SecretKeySpec(keyBytes, "AES");

        Cipher chiper = Cipher.getInstance("AES/CBC/PKCS5Padding");
        chiper.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes("UTF-8")));

        byte[] byteStr = Base64.decodeBase64(text.getBytes());

        return new String(chiper.doFinal(byteStr), "UTF-8");
    }
}