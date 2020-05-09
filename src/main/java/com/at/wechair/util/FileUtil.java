package com.at.wechair.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/8
 * @Time: 16:30
 * @Description
 */
public class FileUtil {
    public static String uploadImage(MultipartFile file, String path, String fileName){
        String newFileName = getNewFileName(fileName);
        String realPath = path + "/" + newFileName;
        File img = new File(realPath);
        // 判断父级目录是否存在
        if(!img.getParentFile().exists()){
            img.getParentFile().mkdir();
        }
        try{
            file.transferTo(img);
            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  获取文件后缀名
     * @param fileOriginName   源文件名
     * @return  String
     */
    public static String getNewFileName(String fileOriginName) {
        return System.currentTimeMillis() + UUID.randomUUID().toString() + getSuffix(fileOriginName);
    }

    /**
     * 获取文件后缀名
     * @param fileName  文件名
     * @return String
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
