package com.at.wechair.service;

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
     * 统计剩余座位数量
     * @param mark 座位状态
     * @return int
     */
    int getChairsCount(int mark);
    /**
     * 获取用户信息，包括正使用的座位数和预约座位数
     * @param openId    用户的open_id
     * @return map
     */
    Map<String,Object> getUserInfo(String openId);

    boolean judgeUserAuthority(HashMap<String,Object> map);
}
