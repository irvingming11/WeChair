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
    @Value("${chairs.selectUsingNumberSql}")
    private String usingNumberSql;
    @Value("${chairs.selectUsingSql}")
    private String usingSql;
    @Value("${chairs.updateLeavingSql}")
    private String updateLeavingSql;
    @Value("${chairs.insertUsingListSql}")
    private String usingListSql;
    @Value("${chairs.updateReservationChairsLeavingSql}")
    private String reservationChairLeavingSql;
    @Value("${chairs.selectChairMarkSql}")
    private String chairMarkSql;
    @Value("${chairs.selectReservationChairTableSql}")
    private String reservationChairTableSql;
    @Value("${chairs.selectReservationChairSeatSql}")
    private String reservationChairSeatSql;
    @Value("${chairs.updateChairStatusSql}")
    private String updateChairStatusSql;
    @Value("${chairs.updateReservationChairStatusSql}")
    private String updateReservationChairSql;
    @Value("${chairs.insertUsingSql}")
    private String insertUsingSql;
    @Resource
    private ChairsManagementDao chairsDao;
    @Resource
    private LoginService loginService;
    /**
     * 普通用户权限
     */
    private static final int OU_AUTHORITY = 400;
    private static final String MARK = "green";

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
     * 更新预约座位状态
     *
     * @param openId  user's open_id
     * @param tableId 桌号
     * @param seatId  座位号
     * @return boolean
     */
    public boolean updateStatus(String openId, int tableId, int seatId) {
        Object[] params = {"orange", openId, tableId, seatId};
        return chairsDao.updateData(reservationSql, params);

    }

    /**
     * 更新离座状态
     *
     * @param openId 用户open_id
     * @return boolean
     */
    public boolean updateStatus(String openId,String mark, int tableId, int seatId) {
        Object[] params = {mark, openId,tableId,seatId};
        return chairsDao.updateData(updateLeavingSql, params);
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
        Object[] params = {openId, TimeOuter.stampToDate(time)[0]};
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
                int reservedNumber = getChairsCount(reservationChairSql, new Object[]{openId, 0});
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
                        Object[] params = new Object[]{openId, reservationDate.get("date"), reservationDate.get("time"), 0, "北书库", tableId, seatId};
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
            if (obj != null) {
                Long reservationTime = TimeOuter.stampToDate(obj.toString());
                data = chairsDao.getReservationList(selectNowReservationListSql, new Object[]{openId}, reservationTime);
            } else {
                data = new Object[]{};
            }
            map.put("appointing", data);
            //获取历史预约记录
            map = chairsDao.getOldReservationList(selectOldReservationListSql, new Object[]{openId}, map);
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
            int tableId = (int) chairsDao.selectData(tableIdSql, new Object[]{openId, 0});
            int seatId = (int) chairsDao.selectData(seatIdSql, new Object[]{openId,0});
            chairsDao.updateData(cancelReservationSql, new Object[]{2, openId});
            chairsDao.updateData(updateSeatSql, new Object[]{"green", "", tableId, seatId});
            map.put("result", 1);
        } else {
            map.put("result", 0);
        }
        return map;
    }

    @Override
    public HashMap<String, Object> usingList(HashMap<String, Object> map) {
        String openId = map.get("open_id").toString();
        String sessionKey = map.get("session_key").toString();
        if (judgeLoginStatus(openId, sessionKey)) {
            //获取当前使用记录
            Object obj = chairsDao.selectData(usingNumberSql, new Object[]{openId, "red"});
            Object[] data;
            if (obj != null) {
                data = chairsDao.getUsingList(usingSql, new Object[]{openId});
            } else {
                data = new Object[]{};
            }
            map = chairsDao.getOldUsingList(usingSql, new Object[]{openId}, map);
            map.put("using", data);
            //获取历史使用记录

        } else {
            map.put("result", 0);
        }
        return map;
    }

    @Override
    public HashMap<String, Object> updateLeavingStatus(HashMap<String, Object> map) {
        String openId = map.get("open_id").toString();
        int tableId = (int) chairsDao.selectData("select TableID from Seat where UserID = ?", new Object[]{openId});
        int seatId = (int) chairsDao.selectData("select SeatID from Seat where UserID = ?", new Object[]{openId});
        if (updateStatus("",MARK,tableId,seatId)) {
            chairsDao.updateData(usingListSql, new Object[]{openId, "北书库", seatId, tableId});
            chairsDao.updateData(reservationChairLeavingSql, new Object[]{4, openId, tableId, seatId});
            map.put("status", 1);
        } else {
            map.put("status", 0);
        }
        return map;
    }

    @Override
    public HashMap<String, Object> scan(HashMap<String, Object> map) {
        String openId = map.get("open_id").toString();
        String sessionKey = map.get("session_key").toString();
        int tableId = Integer.parseInt(map.get("tableId").toString());
        int seatId = Integer.parseInt(map.get("seatId").toString());
        if (judgeLoginStatus(openId, sessionKey)) {
            //获取用户正使用的座位数量
            int ownNumber = getChairsCount(ownChairSql, new Object[]{openId,"red"});
            //获取用户已预约的座位数量
            int reservedNumber = getChairsCount(reservationChairSql, new Object[]{openId, 0});
            if(ownNumber == 0){
                if(reservedNumber == 0){
                    //判断当前座位是否空闲
                    String mark = chairsDao.selectData(chairMarkSql,new Object[]{tableId,seatId}).toString();
                    System.out.println();
                    if(MARK.equals(mark)){
                        map.put("status",2);
                    }else{
                        map.put("status",3);
                    }
                }else if(reservedNumber == 1){
                    //判断当前座位是否为自己预约的座位
                    int reservedTableNumber = Integer.parseInt(chairsDao.selectData(reservationChairTableSql,new Object[]{openId}).toString());
                    int reservedSeatNumber = Integer.parseInt(chairsDao.selectData(reservationChairSeatSql,new Object[]{openId}).toString());
                    System.out.println(reservedSeatNumber);
                    System.out.println(seatId);
                    if(reservedTableNumber == tableId && reservedSeatNumber == seatId){
                        //更新座位状态、更新预约状态
                        chairsDao.updateData(updateChairStatusSql,new Object[]{"red",openId,tableId,seatId});
                        chairsDao.updateData(updateReservationChairSql,new Object[]{1,openId,tableId,seatId});
                        chairsDao.updateData(insertUsingSql,new Object[]{openId,"北书库", seatId, tableId});
                        map.put("status",1);
                    }else{
                        map.put("status",4);
                    }
                }else{
                    map.put("status",5);
                }
            }else{
                map.put("status",4);
            }
        } else {
            map.put("result", 0);
        }
        return map;
    }
    @Override
    public HashMap<String, Object> usingChair(HashMap<String, Object> map){
        String openId = map.get("open_id").toString();
        String sessionKey = map.get("session_key").toString();
        int tableId = Integer.parseInt(map.get("tableId").toString());
        int seatId = Integer.parseInt(map.get("seatId").toString());
        if(judgeLoginStatus(openId, sessionKey)) {
            //更新座位状态
            chairsDao.updateData(updateChairStatusSql, new Object[]{"red", openId, tableId, seatId});
            //更新使用表
            chairsDao.updateData(insertUsingSql, new Object[]{openId, "北书库", seatId, tableId});
            map.put("result", 1);
        }else{
            map.put("result", 0);
        }
        return map;
    }
}
