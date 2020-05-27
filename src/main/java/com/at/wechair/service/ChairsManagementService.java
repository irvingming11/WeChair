package com.at.wechair.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
     * @param param 查询的参数
     * @param sql sql
     * @return int
     */
    int getChairsCount(String sql,String param);

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
}
