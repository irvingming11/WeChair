package com.at.wechair.mapper;

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
     * @return boolean
     */
    boolean add();

    /**
     * 获取数据操作
     * @param map json数据
     */
    void getList(HashMap<Object,Object> map);
}
