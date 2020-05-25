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
    int getChairsCount(String sql,Object param);

    /**
     * 获取座位状态
     * @param list 存储状态信息的集合
     * @return ArrayList
     */
    ArrayList<Integer> getChairStatus(ArrayList<Integer> list);


    /**
     * 获取用户信息，包括正使用的座位数和预约座位数
     * @param openId    用户的open_id
     * @return map
     */
    Map<String,Object> getUserInfo(String openId);

    /**
     * 判断用户权限
     * @param map   存储信息的集合
     * @return boolean
     */
    boolean judgeUserAuthority(HashMap<String,Object> map);

    /**
     * 更新座位状态
     * @param seatId 座位号
     * @return boolean
     */
    boolean updateStatus(int seatId);
}
