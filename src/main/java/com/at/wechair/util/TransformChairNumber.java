package com.at.wechair.util;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/27
 * @Time: 10:39
 * @Description
 */

public class TransformChairNumber {

    private static String [] number = {"one","two","three","four","five","six","seven","eight","nine","ten"};

    /**
     * 转换英文的座位号为桌号和座位号
     * @param chairNumber   英文座位号
     * @return int[]
     */
    public static int[] transform(String chairNumber){
        String [] str = chairNumber.split("_");
        int [] result = new int[2];
        for(int i = 0;i < str.length;i++) {
            for (int j = 0; j < number.length; j++) {
                if (str[i].equals(number[j])) {
                    result[i] = j + 1;
                }
            }
        }
        return result;
    }
    /**
     * 转换数字桌号和座位号为英文座位号
     * @param tableId   桌号
     * @param seatId    座位号
     * @return String
     */
    public static String transform(int tableId,int seatId){
        return number[tableId-1] + "_" + number[seatId-1];
    }
}


