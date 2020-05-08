package com.at.wechair.service.impl;

import com.at.wechair.entity.Account;
import com.at.wechair.entity.OrdinaryUser;
import com.at.wechair.mapper.LoginDao;
import com.at.wechair.service.LoginService;
import com.at.wechair.util.AesCbcUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/5
 * @Time: 10:42
 * @Description
 */
@Component
@PropertySource(value = "classpath:/sql.properties")
@ConfigurationProperties(prefix = "login")
@Transactional(rollbackFor = Exception.class)
@Service
public class LoginServiceImpl implements LoginService {
    private static final String OPEN_ID = "open_id";
    @Resource
    private LoginDao loginDao;
    @Value("${login.addSql}")
    private String addSql;
    @Value("${login.updateSql}")
    private String updateSql;
    private static final String MAN = "1";
    private static final String WOMAN = "0";
    @Override
    public boolean findOneUser(HashMap<String,Object> map) {
        String userId = loginDao.getUserInfo(map);
        if(userId == null){
            return false;
        }
        return userId.equals(map.get(OPEN_ID));
    }

    @Override
    public String getUserAuthorities(Account account) {
        return null;
    }
    @Override
    public HashMap<String, Object> decryptUserInfo(String encryptedData, String sessionKey,String iv,HashMap<String, Object> map) {

        try {
            String string = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            JSONObject result = new JSONObject(string);
            map = getMap(result, map);
            if (findOneUser(map)) {
                dealWithInfo(map.get("open_id"), sessionKey, map.get("nickName"));
            } else {

                //对encryptedData加密数据进行AES解密
                dealWithInfo(map);
            }
        }catch(Exception e){
            System.out.println("出现了点小问题，请重新登录");
        }
        return  map;
    }
    /**
     * 存储解密数据
     *
     * @param result 解密结果
     * @param map    存储解密数据对象
     * @return map
     * @throws JSONException JSON对象异常
     */
    public HashMap<String, Object> getMap(JSONObject result, HashMap<String, Object> map) throws JSONException {
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
    /**
     * 首次登陆用户解密的用户信息插入数据库
     * @param map   存储数据的map容器
     */
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
        boolean result = storageUserInfo(account, user);
        if (result) {
            System.out.println("数据存储成功");
        } else {
            System.out.println("数据存储失败");
        }
    }

    /**
     * 对数据库中存在的用户的session_key和用户名等信息进行更新
     * @param openId        用户的open_id
     * @param sessionKey    用户的session_key
     * @param userName      用户名
     */
    public void dealWithInfo(Object openId, String sessionKey, Object userName){
        if(updateUserInfo(openId.toString(), sessionKey,userName.toString())){
            System.out.println("数据库更新成功");
        }else{
            System.out.println("数据库更新失败");
        }
    }

    @Override
    public boolean storageUserInfo(Account account, OrdinaryUser user) {
        Object[] params = {account.getOpenId(),user.getSex(),user.getWeChatName(),account.getSessionKey(),account.getOwnAuthority()};
        return loginDao.dataOperation(addSql,params);
    }

    @Override
    public boolean updateUserInfo(String openId, String sessionKey,String userName) {
        Object[] params = {sessionKey,userName,openId};
        return loginDao.dataOperation(updateSql,params);

    }


}

