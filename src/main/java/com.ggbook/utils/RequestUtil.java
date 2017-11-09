package com.ggbook.utils;

import com.ggbook.constants.Constant;
import com.jfinal.kit.StrKit;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wujie on 2017/10/25.
 */
public class RequestUtil {

    static Log log = LogFactory.getLog("RequestUtil.class");

    /**
     * 发送http post请求
     * @param REQUEST_URL 请求地址
     * @param map 请求参数
     * @return
     */
    @SuppressWarnings({ "rawtypes", "static-access" })
    public static String sendPostRequest(HttpServletRequest request, HttpServletResponse response, String REQUEST_URL, Map map) {

        StringBuffer sb = new StringBuffer("");
        int responseCode = 0;
        try {
            //创建连接
            URL url = new URL(REQUEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Accept",
                    "application/json");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.connect();
            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            JSONObject obj = new JSONObject().fromObject(map);

            out.write(obj.toString().getBytes("utf-8"));
            out.flush();
            out.close();
            // 获取响应状态码
            responseCode = connection.getResponseCode();
            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes());
                sb.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            return "URLException";
        } catch (UnsupportedEncodingException e) {
            return "EncodingException";
        } catch (Exception e) {
            if(responseCode == 401) {
                // 判断请求类型
                if(request.getMethod().equals("GET")) {
                    return "-1";
                }
                return "401";
            } else if(responseCode == 400) {
                return "400";
            }
            e.printStackTrace();
            return "TimeOut";
        }
        return sb.toString();
    }

    /**
     * 发送http post请求
     * @param REQUEST_URL 请求地址
     * @param data json数据
     * @return
     * @throws Exception
     */
    public static String sendPostJSONString(HttpServletRequest request, HttpServletResponse response, String REQUEST_URL, String data) throws Exception {

        StringBuffer sb = new StringBuffer("");
        int responseCode = 0;
        try {
            //创建连接
            URL url = new URL(REQUEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Accept",
                    "application/json");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.connect();
            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            out.write(data.getBytes("utf-8"));
            out.flush();
            out.close();
            // 获取响应状态码
            responseCode = connection.getResponseCode();
            //读取响应 f
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes());
                sb.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            return "URLException";
        } catch (UnsupportedEncodingException e) {
            return "EncodingException";
        } catch (IOException e) {
            if(responseCode == 401) {
                return "401";
            } else if(responseCode == 400) {

                return "400";
            }
            e.printStackTrace();
            return "TimeOut";
        }
        return sb.toString();
    }

    /**
     * 向指定URL发送GET方法的请求
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, Map<String, String> param) {
        StringBuffer result = new StringBuffer("");
        BufferedReader in = null;
        try {
            String urlStr = url;
            if(null != param) {
                urlStr += "?";
                for (String key : param.keySet()) {
                    urlStr += (key+"="+param.get(key)+"&");
                }
                urlStr.substring(0, urlStr.length()-1);
            }
            URL realUrl = new URL(urlStr);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }


    @SuppressWarnings("static-access")
    public static void main(String[] args) {

        String url = "http://m.xs.la/";
        System.out.println(new RequestUtil().sendGet(url, null));
    }
}
