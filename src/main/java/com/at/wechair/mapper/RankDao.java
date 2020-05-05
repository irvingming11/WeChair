package com.at.wechair.mapper;

import com.at.wechair.entity.RankInfo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/1
 * @Time: 16:17
 * @Description
 */
public interface RankDao {
    /**
     * 用户点击学时排名时更新实时排名
     * @return boolean
     */
    boolean updateNewRank();

    /**
     * 获取数据库所有用户学习时长的集合
     * @return List
     */
    List<RankInfo> getRankList();

    /**
     * 对时长数据进行排名
     * @return List
     */
    List<RankInfo> timeRank();
}
