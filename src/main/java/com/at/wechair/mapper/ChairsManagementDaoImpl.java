package com.at.wechair.mapper;

import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    public ArrayList<String> getMarks(ArrayList<String> list, String sql, Object[] params) {
        ResultSet rs = super.executeQuery(sql, params);
        int i = 0;
        try {
            while (rs.next()) {
                list.add(rs.getString(++i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
