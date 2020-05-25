package com.at.wechair.mapper;

import java.sql.ResultSet;
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
     * 统计剩余座位数量或统计用户正使用座位数和已预约座位数
     * @param sql   查询语句
     * @param params    对象集合
     * @return int
     */
    int chairsCounters(String sql,Object[] params);

    /**
     * 从数据库获取每个座位的状态
     * @param list 存储座位状态的集合
     * @param sql   查询的sql语句
     * @param params   查询的对象集合
     * @return ArrayList
     */
    ArrayList<Integer> getMarks(ArrayList<Integer> list, String sql, Object[] params);


}
