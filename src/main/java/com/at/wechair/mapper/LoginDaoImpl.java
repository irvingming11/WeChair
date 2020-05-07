package com.at.wechair.mapper;

import com.at.wechair.entity.Account;
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
    @Override
    public boolean add(Account account, OrdinaryUser user) {
        int i = 0;
        try{
            String sql = "insert into Student(UserID,Gender,Nickname,Password,warrant) values(?,?,?,?,?)";
            Object[] params = {account.getOpenId(),user.getSex(),user.getWeChatName(),account.getSessionKey(),account.getOwnAuthority()};
            i  = super.executeUpdate(sql,params);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            super.closeResource();
        }
        return i > 0;
    }

    @Override
    public void getList(HashMap<Object,Object> map) {
        String sql = "select * from Student where UserID = ?";
        Object[] params = {map.get("open_id")};
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
