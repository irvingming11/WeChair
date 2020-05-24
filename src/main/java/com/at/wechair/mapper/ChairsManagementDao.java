package com.at.wechair.mapper;

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
     * 统计剩余座位数量
     * @param sql   查询语句
     * @param params    对象集合
     * @return int
     */
    int chairsCounters(String sql,Object[] params);
}
