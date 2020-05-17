package com.at.wechair.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/15
 * @Time: 17:23
 * @Description
 */
@RestController
@RequestMapping(value = "ChairsManagementController")
public class ChairsManagementController {
    @RequestMapping(value = "showChairsDistribution")
    public Map<String, Object> showChairsDistribution(@RequestParam(value = "open_id")String openId){
        return null;
    }
}
