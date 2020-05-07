package com.at.wechair.mapper;

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
public interface LoginDao {
    /**
     * 添加操作
     * @param account 账户
     * @param user  用户
     * @return  boolean
     */
    boolean add(Account account, OrdinaryUser user);

    /**
     * 获取数据操作
     * @param map json数据
     */
    void getList(HashMap<Object,Object> map);
}
