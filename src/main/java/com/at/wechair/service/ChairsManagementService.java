package com.at.wechair.service;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/16
 * @Time: 17:23
 * @Description
 */
public interface ChairsManagementService {
    /**
     * 统计剩余座位数量或用户已使用和已预约的座位数
     * @param params 查询的参数
     * @param sql sql
     * @return int
     */
    int getChairsCount(String sql,Object[] params);

    /**
     * 获取座位状态
     * @param map 存储状态信息的集合
     * @return HashMap
     */
    HashMap<String,Object> getChairStatus(HashMap<String,Object> map);

    /**
     * 用户预约座位
     * @param map 存储信息的集合
     * @return HashMap
     */
    HashMap<String,Object> userReservation(HashMap<String,Object> map);

    /**
     * 查看用户预约记录
     * @param map   存储信息的集合
     * @return HashMap
     */
    HashMap<String,Object> reservationList(HashMap<String, Object> map) throws SQLException;

    /**
     * 取消用户预约记录
     * @param map map
     * @return HashMap
     */
    HashMap<String,Object> updateReservationList(HashMap<String, Object> map);

    /**
     * 使用记录
     * @param map   map
     * @return HashMap
     */
    HashMap<String, Object> usingList(HashMap<String, Object> map);

    /**
     * 更新离座状态
     * @param map map
     * @return HashMap
     */
    HashMap<String,Object> updateLeavingStatus(HashMap<String, Object> map);

    /**
     * 扫码用座
     * @param map map
     * @return HashMap
     */
    HashMap<String, Object> scan(HashMap<String, Object> map);

    /**
     * 确认使用
     * @param map map
     * @return HashMap
     */
    HashMap<String, Object> usingChair(HashMap<String, Object> map);
}
