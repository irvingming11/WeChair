package com.at.wechair.controller;

import com.at.wechair.entity.Account;
import com.at.wechair.entity.OrdinaryUser;
import com.at.wechair.service.LoginService;
import com.at.wechair.util.AesCbcUtil;
import com.at.wechair.util.HttpRequest;


import com.mysql.jdbc.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
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
    private static final String MAN = "1";
    private static final String WOMAN = "0";
    @Resource
    private LoginService loginService;


    /**
     * 用户登录小程序通过微信接口获取用户信息
     * <p>
     * 解密用户敏感数据
     *
     * @param encryptedData 加密数据
     * @param iv            加密算法的初始向量
     * @param code          登录凭证
     */
    @RequestMapping(value = "decodeUserInfo", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Object> decodeUserInfo(@RequestParam(value = "encryptedData") String encryptedData,
                                              @RequestParam(value = "iv") String iv,
                                              @RequestParam(value = "code") String code) throws Exception {


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
//        获取sessionKey和openId
        map = getMap(params, map);
        String sessionKey = (String) map.get("session_key");
        //对encryptedData加密数据进行AES解密
        String string = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
        JSONObject result = new JSONObject(string);
        map = getMap(result, map);
        dealWithInfo(map);
        return map;
    }

    /**
     * 获取openid和session_key
     *
     * @param params 数据库操作值
     * @param map    解密得到的数据
     * @return map
     * @throws JSONException JSON对象异常
     */
    public Map<String, Object> getMap(String params, Map<String, Object> map) throws JSONException {
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
        return map;
    }

    /**
     * 存储解密数据
     *
     * @param result 解密结果
     * @param map    存储解密数据对象
     * @return map
     * @throws JSONException JSON对象异常
     */
    public Map<String, Object> getMap(JSONObject result, Map<String, Object> map) throws JSONException {
        if (result != null && result.length() > 0) {
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

    public void dealWithInfo(Map<String, Object> map) {
        Account account = new Account();
        OrdinaryUser user = new OrdinaryUser();
        account.setOpenId((String) map.get("open_id"));
        account.setSessionKey((String) map.get("session_key"));
        user.setUserName((String) map.get("nickName"));
        user.setWeChatName((String) map.get("nickName"));
        String gender = map.get("gender").toString();
        if (MAN.equals(gender)) {
            user.setSex("男");
        } else if (WOMAN.equals(gender)) {
            user.setSex("女");
        } else {

            System.out.println("性别信息获取异常");
        }
        boolean result = loginService.storageUserInfo(account, user);
        if (result) {
            System.out.println("数据存储成功");
        } else {
            System.out.println("数据存储失败");
        }
    }
}




