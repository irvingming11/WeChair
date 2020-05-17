package com.at.wechair.service;

import com.at.wechair.entity.OrdinaryUser;
import org.json.JSONException;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/3
 * @Time: 8:43
 * @Description
 */



public interface LoginService {
    /**
     * 判断用户是否是首次登陆，即查询用户是否在数据库中
     * @param map  用户信息
     * @return  boolean
     */
    boolean findOneUser(HashMap<String,Object> map);
    /**
     * 获取用户权限
     * @param openId  用户的openId
     * @param map   存储用户信息的集合
     * @return HashMap
     */
    HashMap<String,Object> getUserAuthorities(String openId,HashMap<String,Object> map);

    /**
     * 解密用户信息
     * @param encryptedData     用户的加密数据
     * @param sessionKey        用户的session_key
     * @param iv                用户的密钥
     * @param map               存储信息的map容器
     * @return  HashMap
     * @throws JSONException    json数据异常
     */
    HashMap<String,Object> decryptUserInfo(String encryptedData,String sessionKey,String iv,HashMap<String,Object> map) throws JSONException;

    /**
     * 存储用户信息
     * @param user      用户
     * @return boolean
     */
    boolean storageUserInfo(OrdinaryUser user);

    /**
     * 更新用户信息
     * @param params    用户更新信息集合
     * @return  boolean
     */
    boolean updateUserInfo(Object[] params);

}
