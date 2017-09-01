package com.ggbook.utils;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/18.
 */
public class AddressToCoordinateUtil {
    /**
     * 正向地址匹配接口
     * 根据xy获取所在省市县
     * @param xy
     * @return
     */
    public static Map geodecode(String xy){
        String sUrl="http://api.map.baidu.com/geocoder/v2/?ak=8926feb9ef0a93c41c13b6103bf4dfa6&location="+xy + "&output=json&pois=0";
        String address="";
        StringBuffer str = new StringBuffer();
        Map resultMap=new HashMap();
        try {
            java.net.URL url = new java.net.URL(sUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                str.append(line);
            }
            in.close();
            if (str.equals("") || str == null) {
                System.err.println("百度服务无返回");
                return null;
            }
            System.out.println(str.toString());


            JSONObject jsonobj = null;
            try {
                jsonobj = JSONObject.fromObject(str.toString());
                JSONObject result = jsonobj.getJSONObject("result");// 获取对象
                JSONObject addressComponent = result
                        .getJSONObject("addressComponent");
                String province = addressComponent
                        .getString("province");
                resultMap.put("province",province);
                String city = addressComponent.getString("city");
                resultMap.put("city",city);
                String district = addressComponent
                        .getString("district");
                resultMap.put("district",district);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("此坐标获取不到省份："+xy);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 逆向匹配接口
     * 根据地址名称，匹配得到经纬度坐标
     * @param addr
     * @return
     */
    public static String geocode(String addr){
        String xyStr="";
        addr= URLEncoder.encode(addr);
        StringBuffer str = new StringBuffer();
        String lng="";
        String lat="";
        try {
            //System.out.println("http://api.map.baidu.com/geocoder?address="+addr+"&output=json");
            String sUrl="http://api.map.baidu.com/geocoder/v2/?address="+addr+"&output=json&ak=8926feb9ef0a93c41c13b6103bf4dfa6";
            java.net.URL url = new java.net.URL(sUrl);
            //java.net.URL url = new java.net.URL("http://api.map.baidu.com/?qt=gc&wd="+addr+"&cn=%E5%85%A8%E5%9B%BD&ie=utf-8&oue=0&res=api&callback=BMap._rd._cbk96117");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                str.append(line);
            }
            in.close();
            JSONObject dataJson=null;
            dataJson =JSONObject.fromObject(str.toString());
            JSONObject result=dataJson.getJSONObject("result");
            JSONObject location=result.getJSONObject("location");
            lng =location.getDouble("lng")+"";
            lat =location.getDouble("lat")+"";
            xyStr= lng+","+lat;
        } catch (Exception e) {
            lng="";
            lat="";
        }
        return xyStr;
    }

    public static void main(String [] args){
        String xy = AddressToCoordinateUtil.geocode("广州市");
        System.out.println(xy);

        Map address = AddressToCoordinateUtil.geodecode("40.033534,116.313289");
        System.out.println(address);

    }
}
