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
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/16
 * @Time: 17:23
 * @Description
 */

@Service
public class ChairsManagementServiceImpl implements ChairsManagementService {

    @Value("${chairs.selectChairsStatusSql}")
    private String statusSql;
    @Value("${chairs.updateReservationSql}")
    private String reservationSql;
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
    public ArrayList<Integer> getChairStatus(ArrayList<Integer> list){
        Object[] params = {};
        list = chairsDao.getMarks(list,statusSql,params);
        return list;
    }
    @Override
    public Map<String, Object> getUserInfo(String openId) {
        Object[] params = {openId};
        return null;
    }
    @Override
    public boolean judgeUserAuthority(HashMap<String, Object> map){
        int authority = (int) loginService.getUserAuthorities(map.get("open_id").toString(),map).get("ownAuthority");
        return authority != OU_AUTHORITY;
    }
    @Override
    public boolean updateStatus(int seatId){
        Object[] params = {2,seatId};
        return chairsDao.updateSeatStatus(reservationSql,params);

    }
}
