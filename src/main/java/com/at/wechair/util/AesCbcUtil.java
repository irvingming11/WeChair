package com.at.wechair.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;


/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/2
 * @Time: 17:22
 * @Description
 */


public class AesCbcUtil {

    /**
     *  AES解密
     * @param encryptedData 密文，被加密的数据
     * @param iv 偏移量
     * @param encodingFormat 解密后的结果需要进行的编码
     * @return   String
     * */
    public static String decrypt(String encryptedData, String sessionKey, String iv, String encodingFormat) {
        // 加密数据
        byte[] dataByte = Base64.decodeBase64(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decodeBase64(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);
        try {
            // 密钥不足16位补足
            int base = 16;
            if(keyByte.length % base != 0){
                int groups = keyByte.length / base + 1;
                byte[] temp = new byte[groups * base];

                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
//             初始化，java不支持PKCS7Padding解码，需要另外导入库
            Security.addProvider(new BouncyCastleProvider());
            byte[] resultByte = decode(dataByte,keyByte,ivByte);


            if (resultByte != null && resultByte.length > 0) {
                return new String(resultByte, encodingFormat);
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static byte[] decode(byte[] encryptedData, byte[] sessionKey, byte[] iv){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            Key keySpec = new SecretKeySpec(sessionKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE,keySpec,generateIv(iv));
            return cipher.doFinal(encryptedData);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchProviderException e) {
            return null;
        }
    }
    private static AlgorithmParameters generateIv(byte[] iv){
        try {
            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(iv));
            return params;
        } catch (NoSuchAlgorithmException | InvalidParameterSpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
