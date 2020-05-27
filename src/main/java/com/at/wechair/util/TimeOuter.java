package com.at.wechair.util;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/27
 * @Time: 15:37
 * @Description
 */
public class TimeOuter {
    public static int time = 30 * 60;
    public static void main(String[] args) {
        time1();
    }

    private static void time1() {
        while (time > 0) {
            time--;
            try {
                Thread.sleep(1000);
                int mm = time / 60 % 60;
                int ss = time % 60;
                System.out.println("还剩" +  mm + "分钟" + ss + "秒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}