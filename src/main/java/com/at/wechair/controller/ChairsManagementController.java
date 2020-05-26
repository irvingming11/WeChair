package com.at.wechair.controller;

import com.at.wechair.service.ChairsManagementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/15
 * @Time: 17:23
 * @Description
 */
@RestController
@RequestMapping(value = "ChairsManagementController")
public class ChairsManagementController {
    public HashMap<String,Object> map = new HashMap<>(1000);
    @Value("${chairs.selectChairsNumberSql}")
    private String countSql;
    @Resource
    private ChairsManagementService chairService;

    /**
     * 选座功能：
     *      剩余座位数量
     *      所有座位状态
     *
     *
     */
    @RequestMapping(value = "showChairsDistribution")
    public Map<String, Object> showChairsDistribution() {
        //统计剩余空座数量
        int count = chairService.getChairsCount(countSql,0);
        ArrayList<String> list = new ArrayList<>(1000);
        //获取所有座位状态
        list = chairService.getChairStatus(list);
        if(count <= 0){
            map.put("msg","数据库操作异常");
        }else{
            map.put("number", count);
            map.put("chair_list",list);
        }
        return map;
    }
    /**
     * 预约功能：
     *      判断登录状态（待完成）
     *      判断用户权限（已完成）
     *      获取用户正使用和已预约座位数（已完成）
     *      判断用户黑名单状态（部分完成）{查询预约记录表，发现一天内有三次记录的状态都是违约，用户当天无法预约}
     *          设置时间限制（24小时）
     *      更新座位状态（已完成）
     * 预约查看功能：
     *      更新预约查看记录状态（待完成）
     *      记录预约时间（待完成）
     *      预约倒计时（待完成）
     *      取消预约（待完成）：
     *          用户自动取消预约
     *          超出预约有效时间
     *      更新预约状态（待完成）
     *      更新取消座位状态（待完成）
     *      更新用户取消预约次数（待完成）
     *      更新用户黑名单状态（待完成
     */
    @RequestMapping(value = "seatReservation")
    public Map<String, Object> seatReservation(@RequestParam(value = "open_id") String openId,@RequestParam(value = "session_key") String sessionKey,@RequestParam(value = "number")int chairNumber){
        map.put("open_id", openId);
        map.put("chairNumber",chairNumber);
        map.put("session_key", sessionKey);
        //用户预约
        map = chairService.userReservation(map);
        return map;
    }
}
