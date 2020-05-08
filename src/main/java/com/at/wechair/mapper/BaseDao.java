package com.at.wechair.mapper;

import java.sql.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/5
 * @Time: 11:45
 * @Description  提供所有表格都要执行的共同操作方法：增删改查
 */

public class BaseDao {
    Connection cnt=null;
    PreparedStatement ps=null;
    ResultSet rs=null;

    /**
     * 建立数据库连接
     * @return boolean
     */

    public boolean getConnection(){
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://175.24.82.144:3306/wechair?useUnicode=true&characterEncoding=utf-8";
        String username="jiecheng";
        String password="jiecheng1234";
        try {
            Class.forName(driver);
            cnt= DriverManager.getConnection(url,username,password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 关闭数据库资源
     *
     */
    public void closeResource(){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (cnt != null) {
            try {
                cnt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行数据库查询
     * @param sql      数据库查询语句
     * @param params   数据库查询判断条件
     * @return ResultSet
     */
    public ResultSet executeQuery(String sql,Object[] params){
        if(this.getConnection()){
            try {
                ps = cnt.prepareStatement(sql);
                // 给sql语句中查询条件占位符进行赋值
                for(int i=0;i<params.length;i++){
                    ps.setObject(i+1, params[i]);
                }
                rs = ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

    /**
     * 对数据库执行增删改操作
     * @param sql     数据库查询语句
     * @param params  数据库查询判断条件
     * @return int
     */
    public int executeUpdate(String sql ,Object [] params){
        int updateRows=0;
        if(this.getConnection()){
            try {
                ps=cnt.prepareStatement(sql);
                for(int i=0;i<params.length;i++){
                    ps.setObject(i+1,params[i] );
                }
                // 对数据库执行增删改操作返回影响的行数
                updateRows=ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return updateRows;

    }
}
