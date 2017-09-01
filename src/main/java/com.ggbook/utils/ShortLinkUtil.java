package com.ggbook.utils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import org.junit.Test;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 短链接工具类
 */
public class ShortLinkUtil {

    @Test
    public void T() {
        String json = baidu("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx14e0d38b43374f1e&redirect_uri=http%3a%2f%2fapi.wybashi.com%2ftransit.jsp%3ftarget%3dhttp%3a%2f%2fh5.wybashi.com%2fwebapp%2fpage%2fwangyue%2fcommuterorder.html%2526no%3dBAD250&response_type=code&scope=snsapi_base&state=wx14e0d38b43374f1e#wechat_redirect");
        System.out.println(json);
    }

    /**
     * 调用百度短链接api
     * @param url
     * @return
     */
    public static String baidu(String url) {
        try {
            url = URLEncoder.encode(url);
            Map<String, String> queryParas = new HashMap<>();
            queryParas.put("url", url);
            String data = "url=" + url;

            String json = HttpKit.post("http://dwz.cn/create.php", queryParas, data);

            return JSONObject.parseObject(json).getString("tinyurl");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
