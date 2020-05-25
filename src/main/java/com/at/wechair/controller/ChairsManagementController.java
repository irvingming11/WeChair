package com.at.wechair.controller;

import com.at.wechair.service.ChairsManagementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
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
@Component
@PropertySource(value = "classpath:/sql.properties")
@ConfigurationProperties(prefix = "chairs")
@RestController
@RequestMapping(value = "ChairsManagementController")
public class ChairsManagementController {
    public HashMap<String,Object> map = new HashMap<>(1000);
    @Value("${chairs.selectChairsNumberSql}")
    private String countSql;
    @Value("${chairs.selectOwnChairSql}")
    private String ownChairSql;
    @Value("${chairs.selectReservationChairSql}")
    private String reservationSql;
    @Resource
    private ChairsManagementService chairService;
    @RequestMapping(value = "showChairsDistribution")
    public Map<String, Object> showChairsDistribution() {
        int count = chairService.getChairsCount(countSql,0);
        ArrayList<Integer> list = new ArrayList<>(1000);
        list = chairService.getChairStatus(list);
        if(count <= 0){
            map.put("msg","数据库操作异常");
        }else{
            map.put("number",count);
            map.put("chair_list",list);
        }
        return map;
    }
    @RequestMapping(value = "seatReservation")
    public Map<String, Object> seatReservation(@RequestParam(value = "open_id") String openId,@RequestParam(value = "number")int chairNumber){
        map.put("open_id", openId);
        boolean result = chairService.judgeUserAuthority(map);
        if(result){
            int ownNumber = chairService.getChairsCount(ownChairSql,openId);
            int reservedNumber = chairService.getChairsCount(reservationSql, openId);
            if(ownNumber == 0 && reservedNumber == 0){

            }else{

            }
        }else{
            map.put("authority","未授权");
        }
        map.put("chairNumber",chairNumber);
        return map;
    }
}
