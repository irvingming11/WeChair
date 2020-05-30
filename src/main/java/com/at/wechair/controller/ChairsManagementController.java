package com.at.wechair.controller;

import com.at.wechair.service.ChairsManagementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.SQLException;
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
//        int count = chairService.getChairsCount(countSql,"green");
        HashMap<String,Object> chairStatus = new HashMap<>(1000);
        //获取所有座位状态
        chairStatus = chairService.getChairStatus(chairStatus);
//        if(count < 0){
//            map.put("msg","数据库操作异常");
//        }else{
//            map.put("number", count);
//            map.put("chairs_status",chairStatus);
//
//        }
        return chairStatus;
    }
    /**
     * 预约功能：
     *      判断登录状态（已完成）
     *      判断用户权限（已完成）
     *      获取用户正使用和已预约座位数（已完成）
     *      判断用户黑名单状态（部分完成）{查询预约记录表，发现一天内有三次记录的状态都是违约，用户当天无法预约}
     *          设置时间限制（24小时）
     *      更新座位状态（已完成）
     *
     */
    @RequestMapping(value = "seatReservation")
    public Map<String, Object> seatReservation(@RequestParam(value = "open_id") String openId,
                                               @RequestParam(value = "session_key") String sessionKey,
                                               @RequestParam(value = "seat_num")String chairNumber){
        map.put("open_id", openId);
        map.put("chairNumber",chairNumber);
        map.put("session_key", sessionKey);
        //用户预约
        map = chairService.userReservation(map);
        return map;
    }

    /**
     * 预约查看功能：
     *        更新预约查看记录状态（已完成）
     *              查看历史预约(待完成)
     *        记录预约时间（完成）
     *        预约倒计时（待完成）
     *        取消预约（待完成）：
     *              用户自动取消预约(已完成)
     *              超出预约有效时间
     *        更新预约状态（已完成）
     *        更新取消座位状态（待完成）
     *        更新用户取消预约次数（待完成）
     *        更新用户黑名单状态（待完成）
     */
    @RequestMapping(value = "showReservation")
    public Map<String, Object> showReservation(@RequestParam(value = "open_id") String openId,@RequestParam(value = "session_key")String sessionKey) throws SQLException {
        map.put("open_id", openId);
        map.put("session_key", sessionKey);
        map = chairService.reservationList(map);
        return map;
    }
    @RequestMapping(value = "cancelReservation")
    public Map<String, Object> cancelReservation(@RequestParam(value = "open_id")String openId,
                                                 @RequestParam(value = "session_key")String sessionKey){
        map.put("open_id", openId);
        map.put("session_key", sessionKey);
        map = chairService.updateReservationList(map);
        return map;
    }
    @RequestMapping(value = "showUsing")
    public Map<String, Object> showUsing(@RequestParam(value = "open_id") String openId,
                                         @RequestParam(value = "session_key")String sessionKey){
        map.put("open_id", openId);
        map.put("session_key", sessionKey);
        map = chairService.usingList(map);
        return map;
    }
    @RequestMapping(value = "leaveChair")
    public Map<String, Object> leaveChair(@RequestParam(value = "open_id") String openId,
                                          @RequestParam(value = "session_key")String sessionKey){
        map.put("open_id", openId);
        map.put("session_key", sessionKey);
        map = chairService.updateLeavingStatus(map);
        return map;

    }
}
