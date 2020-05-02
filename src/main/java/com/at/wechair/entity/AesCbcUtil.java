package com.at.wechair.entity;

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


/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/2
 * @Time: 17:22
 * @Description
 */
public class AesCbcUtil {
    /*
     *
     * AES-128-CBC 加密方式
     * AES-128-CBC可以自己定义“密钥”和“偏移量“。
     * AES-128是jdk自动生成的“密钥”。
     *
     * */
    static {
        //  BouncyCastleProvider添加maven依赖
        Security.addProvider(new BouncyCastleProvider());

        /*
         * AES解密
         * @param data 密文，被加密的数据
         * @param key 秘钥
         * @param iv 偏移量
         * @param encodingFormat 解密后的结果需要进行的编码
         * */
    }

    public static String decrypt(String data, String key, String iv, String encodingFormat) {
        initialize();
        // 被加密的数据
        byte[] dataByte = Base64.decodeBase64(data);
        // 加密秘钥
        byte[] keyByte = Base64.decodeBase64(key);
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            //         初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                return new String(resultByte, encodingFormat);
            }
            return null;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidParameterSpecException
                | InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException
                | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void initialize() {
    }
}
