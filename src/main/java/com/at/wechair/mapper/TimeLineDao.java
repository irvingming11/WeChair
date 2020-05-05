package com.at.wechair.mapper;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/1
 * @Time: 16:33
 * @Description
 */
public interface TimeLineDao {
    /**
     * 更新访问时间沙漏实时时间到数据库
     */
    void updateTimeLine();

    /**
     *  获取访问实时学习时长
     * @return Double
     */
    Double getStudyTime();

    /**
     * 生成（包括访问当天）每周学习时长的Map
     * @return Double
     */
    HashMap<String,Double> getTimeMap();
}
