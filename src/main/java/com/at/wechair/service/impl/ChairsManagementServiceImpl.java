package com.at.wechair.service.impl;

import com.at.wechair.mapper.ChairsManagementDao;
import com.at.wechair.service.ChairsManagementService;
import com.at.wechair.service.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    @Resource
    private ChairsManagementDao chairsDao;
    @Resource
    private LoginService loginService;
    /**
     * 普通用户权限
     */
    private static final int OU_AUTHORITY = 400;

    @Override
    public int getChairsCount(String sql,Object param){
        Object[] params = {param};
        return chairsDao.chairsCounters(sql,params);
    }

    @Override
    public ArrayList<String> getChairStatus(ArrayList<String> list){
        Object[] params = {};
        list = chairsDao.getMarks(list,statusSql,params);
        return list;
    }

    /**
     * 判断登录状态
     */
    public boolean judgeLoginStatus(){
        return true;
    }
    /**
     * 判断用户权限
     * @param map   存储信息的集合
     * @return boolean
     */
    public boolean judgeUserAuthority(HashMap<String, Object> map){
        int authority = (int) loginService.getUserAuthorities(map.get("open_id").toString(),map).get("ownAuthority");
        return authority != OU_AUTHORITY;
    }

    /**
     * 更新座位状态
     * @param seatId 座位号
     * @return boolean
     */
    public boolean updateStatus(int seatId){
        Object[] params = {2,seatId};
        return chairsDao.updateSeatStatus(reservationSql,params);

    }
    /**
     * 判断用户违规状态
     * @param openId  用户的open_id
     * @return boolean
     */
    public boolean judgeUserViolationStatus(String openId){
        Object[] params = {openId, 3};
        int number = chairsDao.getViolationNumber(violationSql, params);
        return number == 3;
    }
    @Override
    public HashMap<String, Object> userReservation(HashMap<String, Object> map){
        //判断用户权限
        boolean result = judgeUserAuthority(map);
        String openId = map.get("open_id").toString();
        int chairNumber = (int) map.get("chairNumber");
        if(result){
            //获取用户正使用的座位数量
            int ownNumber = getChairsCount(ownChairSql,openId);
            //获取用户已预约的座位数量
            int reservedNumber = getChairsCount(reservationChairSql, openId);
            boolean violationStatus = judgeUserViolationStatus(openId);
            if(ownNumber == 0 && reservedNumber == 0 && !violationStatus){
                //更新数据库座位状态
                if(updateStatus(chairNumber)){
                    map.put("reservation", "预约成功");
                }else{
                    map.put("reservation", "数据库操作失败");
                }
            }else{
                map.put("reservation","有已预约和正使用的座位，预约失败");
            }
        }else{
            map.put("authority","未授权");
        }
        return map;
    }
}
