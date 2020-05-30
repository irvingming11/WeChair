package com.at.wechair.service.impl;

import com.at.wechair.mapper.ChairsManagementDao;
import com.at.wechair.service.ChairsManagementService;
import com.at.wechair.service.LoginService;
import com.at.wechair.util.TimeOuter;
import com.at.wechair.util.TransformChairNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/16
 * @Time: 17:23
 * @Description
 */
@Component
@PropertySource(value = "classpath:/sql.properties")
@ConfigurationProperties(prefix = "chairs")
@Transactional(rollbackFor = Exception.class)
@Service
public class ChairsManagementServiceImpl implements ChairsManagementService {
    @Value("${chairs.selectOwnChairSql}")
    private String ownChairSql;
    @Value("${chairs.selectReservationChairSql}")
    private String reservationChairSql;
    @Value("${chairs.selectChairsStatusSql}")
    private String statusSql;
    @Value("${chairs.updateReservationSql}")
    private String reservationSql;
    @Value("${chairs.selectViolationNumberSql}")
    private String violationSql;
    @Value("${chairs.selectLoginStatusSql}")
    private String loginStatusSql;
    @Value("${chairs.insertReservationListSql}")
    private String insertReservationSql;
    @Value("${chairs.selectNowReservationListSql}")
    private String selectNowReservationListSql;
    @Value("${chairs.selectReservationTimeSql}")
    private String reservationTimeSql;
    @Value("${chairs.selectOldReservationListSql}")
    private String selectOldReservationListSql;
    @Value("${chairs.cancelReservationListSql}")
    private String cancelReservationSql;
    @Value("${chairs.selectTableIdSql}")
    private String tableIdSql;
    @Value("${chairs.selectSeatIdSql}")
    private String seatIdSql;
    @Value("${chairs.updateCancelSeatSql}")
    private String updateSeatSql;
    @Resource
    private ChairsManagementDao chairsDao;
    @Resource
    private LoginService loginService;
    /**
     * 普通用户权限
     */
    private static final int OU_AUTHORITY = 400;

    @Override
    public int getChairsCount(String sql, Object[] params) {
        Object result = chairsDao.selectData(sql, params);
        if (result == null) {
            return 0;
        } else {
            return Integer.parseInt(result.toString());
        }
    }

    @Override
    public HashMap<String, Object> getChairStatus(HashMap<String, Object> map) {
        map = chairsDao.getMarks(map, statusSql, new Object[]{});
        return map;
    }

    /**
     * 判断登录状态
     *
     * @param openId 用户的open_id
     * @return boolean
     */
    public boolean judgeLoginStatus(String openId, String sessionKey) {
        Object[] params = {openId};
        String oldSessionKey = (String) chairsDao.selectData(loginStatusSql, params);
        return sessionKey.equals(oldSessionKey);
    }

    /**
     * 判断用户权限
     *
     * @param map 存储信息的集合
     * @return boolean
     */
    public boolean judgeUserAuthority(HashMap<String, Object> map) {
        int authority = (int) loginService.getUserAuthorities(map.get("open_id").toString(), map).get("ownAuthority");
        return authority != OU_AUTHORITY;
    }

    /**
     * 更新座位状态
     *
     * @param seatId 座位号
     * @return boolean
     */
    public boolean updateStatus(String openId, int tableId, int seatId) {
        Object[] params = {"orange", openId, tableId, seatId};
        return chairsDao.updateData(reservationSql, params);

    }

    /**
     * 判断用户违规状态
     *
     * @param openId 用户的open_id
     * @return boolean
     */
    public boolean judgeUserViolationStatus(String openId) {
        Date date = new Date();
        Long time = date.getTime();
        Object[] params = {openId,TimeOuter.stampToDate(time)[0]};
        Object result = chairsDao.selectData(violationSql, params);
        if (result == null) {
            return false;
        } else {
            return Integer.parseInt(result.toString()) >= 3;
        }
    }


    @Override
    public HashMap<String, Object> userReservation(HashMap<String, Object> map) {
        String openId = map.get("open_id").toString();
        String sessionKey = map.get("session_key").toString();
        String chairNumber = map.get("chairNumber").toString();
        //转换英文座位号为桌号和座位号
        int[] chairs = TransformChairNumber.transform(chairNumber);
        int tableId = chairs[0];
        int seatId = chairs[1];
        //判断登录态是否有效
        if (judgeLoginStatus(openId, sessionKey)) {
            //判断用户权限
            if (judgeUserAuthority(map)) {
                //获取用户正使用的座位数量
                int ownNumber = getChairsCount(ownChairSql, new Object[]{openId});
                //获取用户已预约的座位数量
                int reservedNumber = getChairsCount(reservationChairSql, new Object[]{openId,0});
                boolean violationStatus = judgeUserViolationStatus(openId);
                if (ownNumber == 0 && reservedNumber == 0 && !violationStatus) {
                    //更新数据库座位状态
                    if (updateStatus(openId, tableId, seatId)) {
                        //更新预约表预约记录
                        Date date = new Date();
                        Long startTime = date.getTime();
                        String[] times = TimeOuter.stampToDate(startTime);
                        HashMap<String, Object> reservationDate = new HashMap<>(100);
                        reservationDate.put("date", TimeOuter.stringToDate(times[0]));
                        reservationDate.put("time", TimeOuter.stringToTime(times[1]));
                        Object[] params = new Object[]{openId, reservationDate.get("date"), reservationDate.get("time"), 0, tableId, seatId};
                        chairsDao.updateData(insertReservationSql, params);
                        map.put("result", 1);
                    } else {
                        map.put("result", 10);
                    }
                } else {
                    map.put("result", -1);
                }
            } else {
                map.put("result", -2);
                map.put("authority", "未授权");
            }
        } else {
            map.put("result", 0);
        }
        return map;
    }
    @Override
    public HashMap<String, Object> reservationList(HashMap<String, Object> map) {
        String openId = map.get("open_id").toString();
        String sessionKey = map.get("session_key").toString();
        if (judgeLoginStatus(openId, sessionKey)) {
            //获取当前预约记录
            Object obj = chairsDao.selectData(reservationTimeSql, new Object[]{openId});
            Object[] data;
            if(obj != null) {
                Long reservationTime = TimeOuter.stampToDate(obj.toString());
                data = chairsDao.getReservationList(selectNowReservationListSql, new Object[]{openId}, reservationTime);
            }else{
                data = new Object[]{};
            }
            map.put("appointing", data);
            //获取历史预约记录
            map = chairsDao.getOldReservationList(selectOldReservationListSql, new Object[]{openId},map);
        } else {
            map.put("result", 0);
        }
        return map;
    }

    @Override
    public HashMap<String, Object> updateReservationList(HashMap<String, Object> map) {
        String openId = map.get("open_id").toString();
        String sessionKey = map.get("session_key").toString();
        if (judgeLoginStatus(openId, sessionKey)) {
            chairsDao.updateData(cancelReservationSql, new Object[]{2, openId});
            int tableId = (int) chairsDao.selectData(tableIdSql, new Object[]{openId});
            int seatId = (int) chairsDao.selectData(seatIdSql, new Object[]{openId});
            chairsDao.updateData(updateSeatSql, new Object[]{"green","",tableId,seatId});
            map.put("result",1);
        } else {
            System.out.println("执行");
            map.put("result", 0);
        }
        return map;
    }

}
