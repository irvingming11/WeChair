package com.at.wechair.service;

import com.at.wechair.entity.Account;
import org.springframework.stereotype.Service;

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
    boolean getUserAuthorities(Account account);

}
