package com.ggbook.utils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2016/11/18.
 */
public class RedisKit {

    public static String set(String key, String value) {
        String rs = "";
        Cache bbsCache = Redis.use();
        Jedis jedis = bbsCache.getJedis();
        try {
            rs = jedis.set(key, value);
        } finally {
            jedis.close();
        }
        return rs;
    }

    public static String get(String key) {
        if (StrKit.isBlank(key)) return "";
        String rs = "";
        Cache bbsCache = Redis.use();
        Jedis jedis = bbsCache.getJedis();
        try {
            rs = jedis.get(key);
        } finally {
            jedis.close();
        }
        return rs;
    }

    public static long del(String key) {
        if (StrKit.isBlank(key)) return 0;
        long rs = 0;
        Cache bbsCache = Redis.use();
        Jedis jedis = bbsCache.getJedis();
        try {
            rs = jedis.del(key);
        } finally {
            jedis.close();
        }
        return rs;
    }



    /**
     * 获取redis，并转换成json
     * @param key
     * @return
     */
    public static JSONObject getRedis(String key){
        String str = RedisKit.get(key);
        JSONObject json = new JSONObject();
        if(StrKit.notBlank(str)){
            json = JSONObject.parseObject(str);
        }
        return json;
    }

}
