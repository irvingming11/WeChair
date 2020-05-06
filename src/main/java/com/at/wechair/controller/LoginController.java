package com.at.wechair.controller;

import com.at.wechair.util.AesCbcUtil;
import com.at.wechair.util.HttpRequest;

import com.mysql.cj.util.StringUtils;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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

    private static final String APP_SECRET = "9d3a5bf69172662d13d382b34e8b031e";
    // AppID(小程序ID)

    private static final String APP_ID = "wxc5e747c5efae300d";
    private static final String GRANT_TYPE = "authorization_code";


    /**
     * 用户登录小程序通过微信接口获取用户信息
     *
     * 解密用户敏感数据
     *
     * @param encryptedData  加密数据
     * @param iv            加密算法的初始向量
     * @param code          登录凭证
     */
    @RequestMapping(value = "decodeUserInfo", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Object> decodeUserInfo(@RequestParam(value = "encryptedData") String encryptedData,
                                              @RequestParam(value = "iv") String iv,
                                              @RequestParam(value = "code") String code) throws Exception {


        Map<String, Object> map = new HashMap<>(1000);
        // 登录凭证不能为空
        System.out.println();
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
        map.put("open_id", openid);
        map.put("session_key", sessionKey);
        if (json.has("unionid")) {
            map.put("unionid", json.get("unionid"));
        } else {
            map.put("unionid", "");
        }
        //对encryptedData加密数据进行AES解密
        String string = AesCbcUtil.decrypt(encryptedData, sessionKey,iv,"UTF-8");
        JSONObject result = new JSONObject(string);
        if (string != null && string.length() > 0) {
            map.put("status", 1);
            map.put("msg", "解密成功");
            map.put("nickName", result.get("nickName"));
            map.put("gender", result.get("gender"));
            map.put("city", result.get("city"));
            map.put("province", result.get("province"));
            map.put("country", result.get("country"));
            map.put("avatarUrl", result.get("avatarUrl"));
        } else {
            map.put("status", 0);
            map.put("msg", "解密失败");
        }
        return map;
    }
}




