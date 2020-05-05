package com.at.wechair.controller;

import com.at.wechair.entity.HttpRequest;
import com.at.wechair.service.LoginService;
import com.mysql.cj.util.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/2
 * @Time: 16:18
 * @Description
 */

@RestController
@RequestMapping(value = "LoginController")
public class LoginController {
    // AppSecret(小程序密钥)

    private static final String APP_SECRET="9d3a5bf69172662d13d382b34e8b031e";
    // AppID(小程序ID)

    private static final String APP_ID = "wxc5e747c5efae300d";
    private static final String GRANT_TYPE="authorization_code";

    @Resource
    private LoginService loginService;
    /**
     * 用户登录小程序通过微信接口获取用户信息
     *
     * 解密用户敏感数据
     * @param encryptedData 明文
     * @param iv            加密算法的初始向量
     * @param code          登录凭证
     * @return Map
     */
    @RequestMapping(value = "decodeUserInfo", method = RequestMethod.GET, produces = "application/json")
    public Map<String,Object> decodeUserInfo(@RequestParam(value = "encryptedData")String encryptedData,
                                             @RequestParam(value = "iv")String iv,
                                             @RequestParam(value = "code")String code) throws JSONException {

        Map<String, Object> map = new HashMap<>(1000);
        // 登录凭证不能为空
        if (StringUtils.isNullOrEmpty(code)) {
            map.put("status", 0);
            map.put("msg", "code 不能为空");
            return map;
        }
        // 向微信服务器 使用登录凭证 code 获取 session_key 和 openid

        // 请求参数
        String params = "appid=" + APP_ID + "&secret=" + APP_SECRET + "&js_code=" + code + "&grant_type="
                + GRANT_TYPE;
        // 发送请求
        String str = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        // 解析相应内容（转换成json对象）
        JSONObject json = new JSONObject(str);
        // 获取会话密钥（session_key）
        String sessionKey = json.get("session_key").toString();
        String openid = json.get("openid").toString();
        map.put("openid", openid);
        map.put("session_key", sessionKey);
        if (json.has("unionid")) {
            map.put("unionid", json.get("unionid"));
        }else{
            map.put("unionid", "");
        }


        //对encryptedData加密数据进行AES解密
        try {
            String result = getUserInfo(encryptedData, sessionKey, iv);
            if (null != result && result.length() > 0) {
                map.put("status", 1);
                map.put("msg", "解密成功");
                JSONObject userInfoJSON = new JSONObject(result);
                Map<String, Object> userInfo = new HashMap<>(100);
                userInfo.put("openId", userInfoJSON.get("openId"));
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                // 解密unionId & openId;
                if (!userInfoJSON.isNull("unionId")) {
                    userInfo.put("unionId", userInfoJSON.get("unionId"));
                }
                map.put("userInfo", userInfo);
            } else {
                map.put("status", 0);
                map.put("msg", "解密失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    public String getUserInfo(String encryptedData,String sessionkey,String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionkey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 密钥不足16位补足
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + 1;
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            // 初始化

            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                return new String(resultByte, StandardCharsets.UTF_8);
            }
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidParameterSpecException
                | IllegalBlockSizeException
                | BadPaddingException
                | InvalidKeyException
                | InvalidAlgorithmParameterException
                | NoSuchProviderException e
        ) {
            e.printStackTrace();
        }
        return null;

    }
}
