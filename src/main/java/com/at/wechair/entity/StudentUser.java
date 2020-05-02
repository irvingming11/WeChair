package com.at.wechair.entity;

import lombok.*;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/1
 * @Time: 15:22
 * @Description
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class StudentUser {
    private String name;
    private String sex;
    private String studentNumber;
    private String dept;
    private String specialty;
    private Date outTime;
    private Double studyTime;
    //抢座的等待时长

    private Date waitTime;
    //学时排行状态（打开或者关闭）

    private boolean timeRankStatus;
    // 无效预约次数

    private int invalidAppointmentTime;
    // 学生认证

    public void studentCertificate(String name, String sex, String studentNumber, String dept, String specialty) {

        /*
           判断学生信息是否属实,属实则修改数据库中的认证状态
          if(){
              alterCertificationStatus();

          }

         */

    }
    // 修改学时排行状态

    public void alterStudyTimeRankedStatus() {

    }
    // 预约座位

    public void bookingChair() {

    }
    // 查看预约

    public void viewBooking() {

    }
    // 取消预约

    public void cancelBooking() {

    }
    // 更新预约剩余超时时间

    public void updateTime() {

    }
    //  选择是否加入排行

    public void chooseRank() {

    }

}
