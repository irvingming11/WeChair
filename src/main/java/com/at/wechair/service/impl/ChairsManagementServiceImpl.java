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
@Component
@PropertySource(value = "classpath:/sql.properties")
@ConfigurationProperties(prefix = "chairs")
@Transactional(rollbackFor = Exception.class)
@Service
public class ChairsManagementServiceImpl implements ChairsManagementService {
    @Value("${chairs.selectChairsNumberSql}")
    private String countSql;
    @Resource
    private ChairsManagementDao chairsDao;
    @Resource
    private LoginService loginService;
    /**
     * 普通用户权限
     */
    private static final int OU_AUTHORITY = 400;
    @Override
    public int getChairsCount(int mark){
        Object[] params = {mark};
        return chairsDao.chairsCounters(countSql,params);
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
}
