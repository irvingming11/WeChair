package com.at.wechair.service;

import com.at.wechair.entity.Account;
import com.at.wechair.entity.OrdinaryUser;

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

}
