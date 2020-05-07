package com.at.wechair.entity;

import lombok.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/1
 * @Time: 15:24
 * @Description
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class Account {
    /**
     * certificationStatus     学生认证状态
     * blackListStatus         账户黑名单状态
     * openId                   用户的openid
     * sessionKey               用户的session_key
     * ownAuthority            拥有的权限
     */
    private boolean certificationStatus = false;
    private boolean blackListStatus = false;
    private String openId;
    private String sessionKey;
    private int ownAuthority = 400;
    public void showUserRule(boolean certificationStatus){

    }
    public void showUserRule(){

    }

}
