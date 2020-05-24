package com.at.wechair.mapper;


import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/16
 * @Time: 17:25
 * @Description
 */
@Repository
public class ChairsManagementDaoImpl extends BaseDao implements ChairsManagementDao {
    @Override
    public int chairsCounters(String sql,Object[] params) {
        ResultSet rs =  super.executeQuery(sql,params);
        int counter = 0;
        if(rs == null){
            return -1;
        }else{
            while(true){
                try {
                    if (!rs.next()) {
                        break;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    counter = rs.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return counter;
    }
}
