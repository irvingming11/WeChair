package com.at.wechair.mapper;

import com.at.wechair.util.TimeOuter;
import com.at.wechair.util.TransformChairNumber;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    private static String endTime;

    @Override
    public Object selectData(String sql, Object[] params) {
        ResultSet rs = super.executeQuery(sql, params);
        Object result = null;
        try {
            while (rs.next()) {
                result = rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean updateData(String sql, Object[] params) {
        return super.executeUpdate(sql, params) != 0;
    }

    @Override
    public HashMap<String, Object> getMarks(HashMap<String, Object> map, String sql, Object[] params) {
        ResultSet rs = super.executeQuery(sql, params);
        try {
            while (rs.next()) {
                int tableId = rs.getInt("TableId");
                int seatId = rs.getInt("SeatId");
                String mark = rs.getString("Mark");
                map.put(TransformChairNumber.transform(tableId, seatId), mark);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Object[] getReservationList(String sql, Object[] params, Long time) {
        Object[] data = new Object[6];
        ResultSet rs = super.executeQuery(sql, params);
        try {
            while (rs.next()) {
                data[0] = "北书库";
                int mark = rs.getInt("Mark");
                int tableId = rs.getInt("TableID");
                int seatId = rs.getInt("SeatID");
                String number = tableId + "桌" + seatId + "座";
                data[2] = number;
                if (mark == 0) {
                    data[1] = "待使用";
                } else if (mark == 2) {
                    data[1] = "已取消";
                }
                data[5] = rs.getObject("AimDay");
            }
            String[] end = TimeOuter.stampToDate(time + 1000 * 60 * 30);
            endTime = end[1];
            int[] times = TimeOuter.time(endTime);
            data[3] = times[1];
            data[4] = times[2];
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public HashMap<String, Object> getOldReservationList(String sql, Object[] params, HashMap<String, Object> map) {
        try {
            int number = 0;
            super.executeQuery("select Count(*) from Reservation where UserID = ? and Mark in (2,3) group by UserID", params);
            while (rs.next()) {
                number = rs.getInt(1);
            }

            String[] results = new String[]{"room_name", "usedAppointStatus", "usedAppointSeat", "usedAppointDate"};
            String result = "";
            for (int i = 0; i < results.length; i++) {
                if (number != 0) {
                    Object[] data = new Object[number];
                    int j = 0;
                    ResultSet rs = super.executeQuery(sql, params);
                    while (rs.next()) {
                        switch(i){
                            case 0:
                                result = rs.getObject((i + 1)).toString();
                                break;
                            case 1:
                                int res = rs.getInt(i+1);
                                switch (res) {
                                    case 1:
                                        result = "已上座";
                                        break;
                                    case 2:
                                        result = "已取消";
                                        break;
                                    case 3:
                                        result = "已超时";
                                        break;
                                    default:
                                        result = "异常";
                                }
                                break;
                            case 2:
                                String res1 = rs.getObject((i + 1)).toString();
                                String res2 = rs.getObject((i + 2)).toString();
                                result = res1 + "桌" + res2 + "座";
                                break;
                            case 3:
                                result = rs.getObject((i + 2)).toString();
                                break;
                            default:
                                System.out.println("异常");
                        }
                        data[j++] = result;
                    }
                    map.put(results[i], data);
                } else {
                    map.put(results[i], new Object[]{});
                }
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Object[] getUsingList(String sql, Object[] params) {
        Object[] data = new Object[4];
        ResultSet rs = super.executeQuery(sql, params);
        try {
            while (rs.next()) {
                data[0] = "北书库";
                data[1] = "正使用";
                int tableId = rs.getInt("TableID");
                int seatId = rs.getInt("SeatID");
                String number = tableId + "桌" + seatId + "座";
                data[2] = number;
                data[3] = rs.getObject("Day");
            }
            System.out.println("执行");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    @Override
    public HashMap<String, Object> getOldUsingList(String sql,Object[] params,HashMap<String, Object> map){
        try {
            int number = 0;
            super.executeQuery("select Count(*) from Record where UserID = ? group by UserID", params);
            while (rs.next()) {
                number = rs.getInt(1);
            }
            String[] results = new String[]{"room_name", "status", "seat", "date"};
            String result = "";
            for (int i = 0; i < results.length; i++) {
                if (number != 0) {
                    Object[] data = new Object[number];
                    int j = 0;
                    ResultSet rs = super.executeQuery(sql, params);
                    while (rs.next()) {
                        switch(i){
                            case 1:
                                result = "已使用";
                                break;
                            case 2:
                                String res1 = rs.getObject(i).toString();
                                String res2 = rs.getObject((i + 1)).toString();
                                result = res1 + "桌" + res2 + "座";
                                break;
                            default:
                                result = rs.getObject((i + 1)).toString();
                        }
                        data[j++] = result;
                    }
                    map.put(results[i], data);
                } else {
                    map.put(results[i], new Object[]{});
                }
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
