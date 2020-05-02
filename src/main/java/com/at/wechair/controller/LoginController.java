package com.at.wechair.controller;

import com.at.wechair.entity.AesCbcUtil;
import com.at.wechair.entity.HttpRequest;
import com.mysql.cj.util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/LoginController")
public class LoginController {
    /**
     * 用户登录小程序通过微信接口获取用户信息
     */

    @RequestMapping(params = "decodeUserInfo")
    public Map<String, Object> decodeUserInfo(String encryptedData, String iv, String code) throws JSONException {
        Map<String, Object> map = new HashMap<>(1000);

        // 登录凭证不能为空
        if (StringUtils.isNullOrEmpty(code)) {
            map.put("status", 0);
            map.put("msg", "code 不能为空");
            return map;
        }

        // AppID(小程序ID)
        String appId = "wxc5e747c5efae300d";
        // AppSecret(小程序密钥)
        String appSecret = "9d3a5bf69172662d13d382b34e8b031e";
        // 授权（必填）
        String grantType = "authorization_code";

        // 向微信服务器 使用登录凭证 code 获取 session_key 和 openid

        // 请求参数
        String params = "appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type="
                + grantType;
        // 发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        // 解析相应内容（转换成json对象）
        JSONObject json = new JSONObject(sr);
        // 获取会话密钥（session_key）
        String sessionKey = json.get("session_key").toString();
        // 用户的唯一标识（openid）
        String openid = (String) json.get("openid");

        //对encryptedData加密数据进行AES解密
        try {
            String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
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
}
