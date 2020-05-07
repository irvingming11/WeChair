package com.at.wechair.service;

import com.at.wechair.entity.Account;
import com.at.wechair.entity.OrdinaryUser;

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
     * @param account  账户类
     * @return boolean
     */
    String getUserAuthorities(Account account);

    /**
     * 存储用户信息
     * @param account   账户
     * @param user      用户
     * @return boolean
     */
    boolean storageUserInfo(Account account, OrdinaryUser user);

    /**
     * 获取已经存在数据库中的用户信息，并更新session_key和用户名
     * @param openId       用户的open_id
     * @param sessionKey    用户的session_key
     * @param userName      用户名
     * @return HashMap
     */
    boolean updateUserInfo(String openId, String sessionKey,String userName);

}
