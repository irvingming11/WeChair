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
     * @param sql   sql
     * @param params      用户
     * @return boolean
     */
    boolean storageUserInfo(String sql,Object[] params);

//    /**
//     * 更新用户信息
//     * @param params 集合
//     * @return  boolean
//     */
//    boolean updateUserInfo(Object[] params);

    /**
     * 更新上传的图片名到数据库
     * @param fileName   文件名
     * @param openId 用户open_id
     * @return boolean
     */
    boolean updateUserImage(String fileName,String openId);

}
