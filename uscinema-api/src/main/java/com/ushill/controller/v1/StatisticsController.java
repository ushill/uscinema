package com.ushill.controller.v1;

import com.ushill.DTO.UserStatDTO;
import com.ushill.VO.UnifyResponse;
import com.ushill.VO.user.UserVO;
import com.ushill.interceptor.LocalUser;
import com.ushill.interceptor.ScopeLevel;
import com.ushill.utils.stat.CachedStatMap;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/2 下午8:10
 */
@RestController
@RequestMapping("/v1/statistics")
@Validated
public class StatisticsController {

    private final CachedStatMap cachedStatMap;

    public StatisticsController(CachedStatMap cachedStatMap) {
        this.cachedStatMap = cachedStatMap;
    }

    @GetMapping("/get/cached/{key}")
    @ScopeLevel(4)
    public Map<String, Number> getCachedStats(@PathVariable @Length(min = 1, max = 64) String key){
        return cachedStatMap.getStats(key);
    }

    @GetMapping("/get/cached/all")
    @ScopeLevel(4)
    public Map<String, Map<String, Number>> getCachedStats(){
        return cachedStatMap.getStats();
    }

    @GetMapping("/clear/cached/{key}")
    @ScopeLevel(4)
    public UnifyResponse clearCachedStats(@PathVariable @Length(min = 1, max = 64) String key){

        cachedStatMap.clearStats(key);
        return new UnifyResponse(0, "已清除", "GET " + "/v1/statistics/clear/cached/" + key);
    }

    @GetMapping("/clear/cached/all")
    @ScopeLevel(4)
    public UnifyResponse clearCachedStats(){

        cachedStatMap.clearStats();
        return new UnifyResponse(0, "已清除", "GET " + "/v1/statistics/clear/cached/alll");
    }
}
