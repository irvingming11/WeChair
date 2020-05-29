package com.at.wechair.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/27
 * @Time: 15:37
 * @Description
 */
public class TimeOuter {



//    public  TimeOuter(Long time, String s) {
//        System.out.println("执行");
//        midTime = stampToDate(time);
//        while (midTime > 0) {
//            midTime--;
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        if(midTime == 0){
//
//        }
//    }


    /**
     * 获取剩余时间戳
     *
     * @return Long
     */
    public static String[] stampToDate(Long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(time));
        return simpleDateFormat.format(time).split(" ");
    }


    /**
     * 将字符串转换成Date类型
     *
     * @param date 字符串日期
     * @return Date
     */
    public static Date stringToDate(String date) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return ft.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串转化成time类型
     *
     * @param time 时间
     * @return Time
     */
    public static Date stringToTime(String time) {
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
        try {
            return ft.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取剩余时间保存在数组中返回
     *
     * @return int[]
     */
    public static int[] time(String endTime) {
        Date date = new Date();
        String[] times = TimeOuter.stampToDate(date.getTime());
        String nowTime = times[1];
        int[] nowTimes = stringToInteger(nowTime.split(":"));
        int[] endTimes = stringToInteger(endTime.split(":"));
        for (int i = nowTimes.length - 1; i >= 1; i--) {
            if (endTimes[i] < nowTimes[i]) {
                endTimes[i - 1] -= 1;
                endTimes[i] += 60;

            }
            nowTimes[i] = endTimes[i] - nowTimes[i];
        }
        return nowTimes;
    }

    private static int[] stringToInteger(String[] param) {
        int[] result = new int[3];
        for (int i = 0; i < param.length; i++) {
            result[i] = Integer.parseInt(param[i]);
        }
        return result;
    }


}