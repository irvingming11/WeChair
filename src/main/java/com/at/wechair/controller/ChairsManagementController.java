package com.at.wechair.controller;

import com.at.wechair.service.ChairsManagementService;
import com.at.wechair.service.LoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
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
    public HashMap<String,Object> map = new HashMap<>(1000);
    @Resource
    private ChairsManagementService chairsService;
    @RequestMapping(value = "showChairsDistribution")
    public Map<String, Object> showChairsDistribution() {
        int count = chairsService.getChairsCount(0);
        if(count <= 0){
            map.put("msg","数据库操作异常");
        }else{
            map.put("number",count);
        }
        return map;
    }
    @RequestMapping(value = "seatReservation")
    public Map<String, Object> seatReservation(@RequestParam(value = "open_id") String openId,int chairNumber){
        map.put("open_id", openId);
        map.put("chairNumber",chairNumber);
        return map;
    }
}
