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

public class Chair {

    /**
     * condition    座位状态
     * location     座位定位
     */
    private int condition;
    public String chairNumber;
    private String location;
    // 取消预约（预约超过三十分钟未扫码上座）

    public void cancelBook() {

    }
    //更新座位状态

    public void updateCondition() {

    }
    //识别落座用户类别(预约者or未预约直接扫码者)

    public void discernUser() {
    }
}
