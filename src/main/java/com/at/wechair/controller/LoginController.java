package com.at.wechair.controller;

import com.at.wechair.service.LoginService;
import com.at.wechair.util.FileUtil;
import com.at.wechair.util.HttpRequest;


import com.mysql.jdbc.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.net.URLEncoder;
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
    @Resource
    private LoginService loginService;
    public HashMap<String,Object> map = new HashMap<>(1000);


    /**
     * 用户登录小程序通过微信接口获取用户信息
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
        return loginService.decryptUserInfo(encryptedData, sessionKey, iv, map);
    }

    /**
     * 上传学生证进行实名认证
     * @param file  用户上传的学生证
     * @return map
     */
    @RequestMapping(value = "uploadImage" , produces = "application/json")
    public Map<String, Object> uploadImage(@RequestParam(value = "file") MultipartFile file) {
        String localPath = "/root/images";
        String fileName = file.getOriginalFilename();
        String newFileName = FileUtil.uploadImage(file, localPath, fileName);
        map.put("pic",newFileName);
        String openId = map.get("open_id").toString();
        if (newFileName != null && loginService.updateUserImage(newFileName,openId)) {
            map.put("status", 1);
            map.put("msg", "上传成功");
        } else {
            map.put("status", 0);
            map.put("msg", "上传失败");
        }
        return map;
    }

    @RequestMapping(value = "getMessages")
    public Map<String,Object> getMessages(@RequestParam(value = "open_id") String openId) {
        HashMap<String, Object> map = new HashMap<>(1000);
        return loginService.getUserAuthorities(openId, map);
    }

    @RequestMapping(value = "showUserRules")
    public Map<String, Object> showUserRules(@RequestParam(value = "open_id") String openId) {
        return null;
    }
    @RequestMapping(value = "sendSuggestions")
    public Map<String, Object> sendSuggestions(){
        return null;
    }


    /**
     * 获取openid和session_key
     *
     * @param params 数据库操作值
     * @param map    解密得到的数据
     * @return map
     * @throws JSONException JSON对象异常
     */
    public HashMap<String, Object>
    getMap(String params, HashMap<String, Object> map) throws JSONException {
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

}




