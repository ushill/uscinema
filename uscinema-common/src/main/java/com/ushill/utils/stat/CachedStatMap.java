package com.ushill.utils.stat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/2 下午5:54
 */
@Component
public class CachedStatMap {
    private ConcurrentHashMap<String, CachedStatUnit> map;
    private int nodeId;
    private String cachedStatKey;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;
    private volatile boolean inited;

    private CachedStatMap(ObjectMapper objectMapper, StringRedisTemplate redisTemplate,
                          @Value("${uscinema.node-id}") int nodeId,
                          @Value("${uscinema.cached-key.v1.cached-stat}") String cachedStatKey) throws IOException {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        this.nodeId = nodeId;
        this.cachedStatKey = cachedStatKey;

        String cachedCachedStat = redisTemplate.opsForValue().get(cachedStatKey + ":" + nodeId);

        if(cachedCachedStat != null){
            this.map = objectMapper.readValue(cachedCachedStat, new TypeReference<ConcurrentHashMap<String, CachedStatUnit>>(){});
        }else {
            this.map = new ConcurrentHashMap<>();
        }
        this.inited = true;
    }

    public void hitIncrement(String key){
        if(key == null){
            return;
        }

        CachedStatUnit unit = map.get(key);
        if(unit != null){
            unit.hit.incrementAndGet();
            return;
        }

        unit = new CachedStatUnit();
        unit.hit.incrementAndGet();
        unit = map.putIfAbsent(key, unit);
        if(null != unit){
            // 其他线程抢先放入了unit
            unit.hit.incrementAndGet();
        }
    }

    public void missIncrement(String key){
        if(key == null){
            return;
        }

        CachedStatUnit unit = map.get(key);
        if(unit != null){
            unit.miss.incrementAndGet();
            return;
        }

        unit = new CachedStatUnit();
        unit.miss.incrementAndGet();
        unit = map.putIfAbsent(key, unit);
        if(null != unit){
            // 其他线程抢先放入了unit
            unit.hit.incrementAndGet();
        }
    }

    public Map<String, Map<String, Number>> getStats(){
        Map<String, Map<String, Number>> res = new HashMap<>();
        CachedStatUnit total = new CachedStatUnit();
        map.forEach((k, v)->{
            HashMap<String, Number> unit = new HashMap<>();
            putUnitStatToMap(v, unit);
            total.hit.getAndAdd(v.getHit());
            total.miss.getAndAdd(v.getMiss());
            res.put(k, unit);
        });
        HashMap<String, Number> totalUnit = new HashMap<>();
        putUnitStatToMap(total, totalUnit);
        res.put("TOTAL", totalUnit);
        return res;
    }

    public Map<String, Number> getStats(String key){
        Map<String, Number> res = new HashMap<>();
        CachedStatUnit unit = map.get(key);
        if(unit == null){
            res.put("hit", -1);
            res.put("miss", -1);
            res.put("ratio", -1);
        }else {
            putUnitStatToMap(unit, res);
        }
        return res;
    }

    private void putUnitStatToMap(CachedStatUnit u, Map<String, Number> m){
        int hit = u.getHit();
        int miss = u.getMiss();
        m.put("hit", hit);
        m.put("miss", miss);
        m.put("ratio", hit + miss > 0? (double) hit / (hit + miss): 0);
    }

    public void clearStats(String key){
        map.remove(key);
    }

    public void clearStats(){
        map = new ConcurrentHashMap<>();
    }

    public void uploadCachedStats() throws JsonProcessingException {
        if(inited) {
            redisTemplate.opsForValue().set(cachedStatKey  + ":" + nodeId, objectMapper.writeValueAsString(map));
        }
    }

    private static class CachedStatUnit{
        private AtomicInteger hit = new AtomicInteger(0);
        private AtomicInteger miss = new AtomicInteger(0);

        public Integer getHit() {
            return hit.intValue();
        }

        public Integer getMiss() {
            return miss.intValue();
        }
    }


}
