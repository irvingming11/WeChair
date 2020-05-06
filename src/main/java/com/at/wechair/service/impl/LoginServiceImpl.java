package com.at.wechair.service.impl;

import com.at.wechair.entity.Account;
import com.at.wechair.mapper.LoginDao;
import com.at.wechair.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/5
 * @Time: 10:42
 * @Description
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public boolean getUserAuthorities(Account account) {

        return false;
    }
}
