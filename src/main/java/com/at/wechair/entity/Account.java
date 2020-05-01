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
     */
    private boolean certificationStatus;
    private boolean blackListStatus;
    public void showUserRule(boolean certificationStatus){
    }
    public void showUserRule(){

    }

}
