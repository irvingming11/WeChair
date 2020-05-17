package com.at.wechair.mapper;

import com.at.wechair.entity.OrdinaryUser;
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
    public OrdinaryUser getUserInfo(HashMap<String,Object> map) {
        String sql = "select * from Student where UserID = ?";
        Object[] params = {map.get("open_id")};
        ResultSet rs = super.executeQuery(sql,params);
        OrdinaryUser user = null;
        if(rs == null){
            return null;
        }else {
            try {
                user = new OrdinaryUser();
                while(rs.next()) {
                    String openId = rs.getString("UserID");
                    String sessionKey = rs.getString("Password");
                    int ownAuthority = rs.getInt("warrant");
                    user.setOpenId(openId);
                    user.setSessionKey(sessionKey);
                    user.setOwnAuthority(ownAuthority);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                super.closeResource();
            }
            return user;
        }
    }

}
