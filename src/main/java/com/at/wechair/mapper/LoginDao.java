package com.at.wechair.mapper;

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
     * 数据增删改操作
     * @param sql 操作的sql语句
     * @param params 操作的对象
     * @return  boolean
     */
    boolean dataOperation(String sql,Object[] params);




    /**
     * 获取数据操作
     * @param map json数据
     * @return OrdinaryUser
     */
    OrdinaryUser getUserInfo(HashMap<String,Object> map);
}
