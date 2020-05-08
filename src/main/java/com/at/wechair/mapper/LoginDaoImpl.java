package com.at.wechair.mapper;

import org.springframework.stereotype.Repository;

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

@Repository
public class LoginDaoImpl extends BaseDao implements LoginDao{
    private int operatedResult = 0;


    @Override
    public boolean dataOperation(String sql,Object[] params) {
        try{
            operatedResult  = super.executeUpdate(sql,params);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            super.closeResource();
        }
        return operatedResult > 0;
    }



    @Override
    public String getUserInfo(HashMap<String,Object> map) {
        String sql = "select * from Student where UserID = ?";
        Object[] params = {map.get("open_id")};
        ResultSet rs = super.executeQuery(sql,params);
        if(rs == null){
            return null;
        }else {
            String result = null;
            try {
                while(rs.next()) {
                    result = rs.getString("UserID");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                super.closeResource();
            }
            return result;
        }
    }

}
