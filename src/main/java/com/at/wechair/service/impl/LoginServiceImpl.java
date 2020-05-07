package com.at.wechair.service.impl;

import com.at.wechair.entity.Account;
import com.at.wechair.entity.OrdinaryUser;
import com.at.wechair.mapper.LoginDao;
import com.at.wechair.service.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;



/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/5
 * @Time: 10:42
 * @Description
 */
@Component
@PropertySource(value = "classpath:/sql.properties")
@ConfigurationProperties(prefix = "login")
@Service
public class LoginServiceImpl implements LoginService {
    private static final String OPEN_ID = "open_id";
    @Resource
    private LoginDao loginDao;

    @Value("${login.addSql}")
    private String addSql;
    @Value("${login.updateSql}")
    private String updateSql;
    @Override
    public boolean findOneUser(HashMap<String,Object> map) {
        String userId = loginDao.getUserInfo(map);
        return userId.equals(map.get(OPEN_ID));
    }

    @Override
    public String getUserAuthorities(Account account) {
        return null;
    }

    @Override
    public boolean storageUserInfo(Account account, OrdinaryUser user) {
        Object[] params = {account.getOpenId(),user.getSex(),user.getWeChatName(),account.getSessionKey(),account.getOwnAuthority()};
        return loginDao.dataOperation(addSql,params);
    }

    @Override
    public boolean updateUserInfo(String openId, String sessionKey,String userName) {
        Object[] params = {openId,sessionKey,userName};
        return loginDao.dataOperation(updateSql,params);

    }


}

