package com.at.wechair.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/5
 * @Time: 15:45
 * @Description
 */
public class LoginDaoImpl extends BaseDao implements LoginDao{
    @Override
    public boolean add() {
        String sql = "";
        Object[] params = {};
        int i  = super.executeUpdate(sql,params);
        super.closeResource();
        return i > 0;
    }

    @Override
    public void getList(HashMap<Object,Object> map) {
        String sql = "";
        Object[] params = {map.get("")};
        ResultSet rs = super.executeQuery(sql,params);
        try {
            // 未写完整
            rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            super.closeResource();
        }
    }

}
