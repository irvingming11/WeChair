package com.at.wechair.controller;

import com.at.wechair.entity.OrdinaryUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/2
 * @Time: 10:53
 * @Description
 */
@RestController
public class MessageNotifyingController {
    /**
     * @return String
     * @RequestMapping 注解的参数：
     * value: 指定请求的实际地址
     * method： 指定请求的method类型， GET、POST、PUT、DELETE等
     * consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
     * produces: 指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回
     * params： 指定request中必须包含某些参数值是，才让该方法处理
     * headers： 指定request中必须包含某些指定的header值，才能让该方法处理请求
     */
    // 进入消息通知页面
    @RequestMapping(value = "/messages")
    public String messages() {
        OrdinaryUser user = new OrdinaryUser();
        if (user.showCertificationStatus()) {
            return "message.jsp";
        } else {
            return "未进行学生认证，请进行学生认证";
        }
    }


}
