package com.at.wechair.entity;

import lombok.*;


/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/1
 * @Time: 15:21
 * @Description
 */



/*
 * @Data                    Lombok注解自动生成Getter 和 Setter方法
 * @NoArgsConstructor       无参构造器
 * @AllArgsConstructor      全参构造器
 * @EqualsAndHashCode       equals 和 hashcode方法
 * @ToString                生成toString方法
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class OrdinaryUser{
    public String  userName;
    public String weChatName;
    private String sex;
    private String phoneNumber;
    //无效离座次数

    private int invalidLeaveChairTime;
    private boolean certificationStatus = false;

    //更换头像

    public void alterUserImage(String weChatName,String phoneNumber){

    }

    //更改用户名

    public void alterUserName(String weChatName,String phoneNumber){

    }

    // 提出用户反馈

    public void sendSuggestions(){

    }

    //查看座位分布

    public void viewChairLocation(){

    }

    //查看座位状态

    public void viewChairCondition(){

    }

    //扫码上座

    public void  scan(){

    }

    //查看座位使用记录（包括正在使用、已使用）

    public void viewUsing(){

    }

    //申请离座

    public void applyLeaveChair(){

    }
    public boolean showCertificationStatus(){
        return this.certificationStatus;
    }

}
