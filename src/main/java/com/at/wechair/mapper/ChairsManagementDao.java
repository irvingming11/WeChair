package com.at.wechair.mapper;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/16
 * @Time: 17:24
 * @Description
 */
public interface ChairsManagementDao {



    /**
     * 查询数据库中的数据
     * @param sql sql
     * @param params 保存查询对象的集合
     * @return Object
     */
    Object selectData(String sql, Object[] params);

    /**
     * 更新数据库中的数据
     * @param sql   sql
     * @param params    保存查询对象的集合
     * @return boolean
     */
    boolean updateData(String sql, Object[] params);
    /**
     * 从数据库获取每个座位的状态
     * @param list 存储座位状态的集合
     * @param sql   查询的sql语句
     * @param params   查询的对象集合
     * @return ArrayList
     */
    ArrayList<String> getMarks(ArrayList<String> list, String sql, Object[] params);



}
