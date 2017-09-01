package com.ggbook.utils;

import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * cookie小工具
 * @author lanyongmao
 * @data  2015-1-17
 * @time 下午3:59:19
 *
 */
public final class CookieUtil {

    /**
     * IP地址正则表达式
     */
    public static final String IP_REGEXP = "^([1-9]{0,1}\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.([1-9]{0,1}\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
    /**
     * cookie加密串
     */
    public static final String MD5_KEY = "This is a Md5KEY #$$FFJGVVbk*(&^&*((^";

    /**
     * 添加cookie，内容不再加密
     */
    public static void addCookie(String name, String value, HttpServletRequest request, HttpServletResponse response, Integer maxAge) throws Exception {
    	if(StringUtils.isBlank(name) || StringUtils.isBlank(value)){
    		return;
    	}
    	
    	//value = Md5Util.encString(value, MD5_KEY);
    	//value = URLEncoder.encode(value, "UTF-8");
    	
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");

        maxAge = maxAge == null ? -1 : maxAge;//如果设置为负值的话，则为浏览器进程Cookie(内存中保存)，关闭浏览器就失效。
        cookie.setMaxAge(maxAge);

        String domain = CookieUtil.getDomain(request);
        if (StringUtils.isNotBlank(domain)) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }
    
    /**
     * 获得解密后的cookie
     */
    public static String getDecodeCookie(String name, HttpServletRequest request) throws Exception {
        String value = getValue(request, name);
        if (StringUtils.isBlank(value)) {
            return null;
        }

        value = URLDecoder.decode(value, "UTF-8");
        value = Md5Util.decString(value, MD5_KEY);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value;
    }

    /**
     * 根据名字注销cookie
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String... names) throws Exception {
    	if(names == null || names.length == 0){
    		return;
    	}
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
        	return;
        }
        for(String name : names){
        	for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setMaxAge(0);
                    cookie.setValue("");
                    cookie.setPath("/");

                    String domain = getDomain(request);
                    if (StringUtils.isNotBlank(domain)) {
                        cookie.setDomain(domain);
                    }
                    response.addCookie(cookie);
                }
            }
        }
    }
    
    /**
     * 获取domain，比如：.baidu.com
     */
    private static String getDomain(HttpServletRequest request) {
        String serverName = request.getServerName();
        boolean b = Pattern.matches(IP_REGEXP, serverName);
        String domain = "";
        boolean bb = serverName.split("\\.").length == 2;//加入域名只有一个点，则忽略截取最低一级域名
        if(bb){
        	domain = "." + serverName;
        }else if (!b) {
            int endIndex = serverName.indexOf(".");
            if(endIndex != -1){            	
            	domain = serverName.substring(endIndex);
            }
        }
        return domain;
    }

	/**
	 * 获取cookie的value
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getValue(HttpServletRequest request, String name){
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0){
			for (Cookie cookie : cookies){
				if (cookie.getName().equals(name)){
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
}
