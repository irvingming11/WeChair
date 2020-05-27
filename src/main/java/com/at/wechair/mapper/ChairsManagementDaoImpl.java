package com.at.wechair.mapper;

import com.at.wechair.util.TransformChairNumber;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
    public Object selectData(String sql, Object[] params) {
        ResultSet rs = super.executeQuery(sql, params);
        Object result = null;
        try{
            while(rs.next()){
                result = rs.getObject(1);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public boolean updateData(String sql, Object[] params) {
        return super.executeUpdate(sql, params) != 0;
    }

    @Override
    public HashMap<String,Object> getMarks(HashMap<String,Object> map, String sql, Object[] params) {
        ResultSet rs = super.executeQuery(sql, params);
        try {
            while (rs.next()) {
                int tableId = rs.getInt("TableId");
                int seatId = rs.getInt("SeatId");
                String mark = rs.getString("Mark");
                map.put(TransformChairNumber.transform(tableId, seatId),mark);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}
