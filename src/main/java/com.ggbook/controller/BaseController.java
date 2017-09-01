package com.ggbook.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ggbook.utils.ToolUtil;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.ggbook.constants.Constant;
import com.ggbook.constants.Constant.Storage;
import com.ggbook.utils.XmlKit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;

public abstract class BaseController extends Controller{
	
	/**默认消息状态：null*/
	protected static final String CODE = "0";
	/**默认消息：成功*/
	protected static final String MSG = "成功";
	
	/**
	 * 封装返回页面的数据
	 * @param obj 内容
	 * @param status 状态
	 * @param code 消息代码
	 * @param msg 消息
	 */
	protected void renderRb(Object obj, boolean status, String code, String msg){
		this.renderRb(obj, status, code, msg, null, null);
	}

	/**
	 * 封装返回页面的数据
	 * @param obj 内容
	 * @param status 状态
	 * @param code 消息代码
	 * @param msg 消息
	 * @param busNo 写报文
	 * @param filename 文件名
	 */
	protected void renderRb(Object obj, boolean status, String code, String msg, String busNo, String filename){
		JSONObject json = new JSONObject();
		json.put("data", obj instanceof Model ? JSONObject.parse(JsonKit.toJson(obj)) : obj);
		json.put("status", status);
		json.put("code", code);
		json.put("msg", msg);
		renderJson(json);
	}
	
	/**
	 * 封装返回页面的数据
	 * @param obj 内容
	 * @param status 状态
	 */
	protected void renderRb(Object obj, boolean status){
		this.renderRb(obj, status, CODE, MSG);
	}
	
	/**
	 * 封装返回页面的数据
	 * @param obj 内容
	 */
	protected void renderRb(Object obj){
		if(obj instanceof JSONObject){
			JSONObject result = (JSONObject)obj;
			if(result.containsKey("errcode")){
				//这部分是某些返回的兼容
				String code = result.getString("errcode");
				String msg = result.getString("errmsg");
				boolean status = "0".equals(code) || "SUCCESS".equals(code);
				Object res = result.get("data");
				this.renderRb(res, status, code, msg);
				return;
			}
		}
		this.renderRb(obj, true, CODE, MSG);
	}

	/**
	 * 封装返回页面的数据
	 * @param obj 内容
	 * @param busNo 写报文
	 * @param filename 文件名
	 */
	protected void renderRb(Object obj, String busNo, String filename){
		this.renderRb(obj, true, CODE, MSG, busNo, filename);
	}
	
	/**
	 * 封装返回页面的数据
	 */
	protected void renderSucc(){
		this.renderRb("", true, CODE, MSG);
	}
	
	/**
	 * 封装返回页面的数据
	 * @param code 消息代码
	 * @param msg 消息
	 */
	protected void renderErr(Object code, String msg){
		this.renderRb("", false, ""+code, msg);
	}

	/**
	 * 封装返回页面的数据
	 * @param code 消息代码
	 * @param msg 消息
	 * @param busNo 写报文
	 * @param filename 文件名
	 */
	protected void renderErr(Object code, String msg, String busNo, String filename){
		this.renderRb("", false, ""+code, msg, busNo, filename);
	}
	
	/**
	 * 封装返回页面的数据
	 * @param msg 消息
	 */
	protected void renderErr(String msg){
		this.renderRb("", false, CODE, msg);
	}
	
	/**
	 * 获取页面参数
	 * @return
	 */
	protected JSONObject getParams(){
		JSONObject params = new JSONObject();
		Enumeration<String> keys = getParaNames();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			params.put(key, getPara(key));
		}
		return params;
	}
	
	/**
	 * 获取页面参数（搜索）
	 * @return
	 */
	protected JSONObject getSearchParams(){
		JSONObject params = this.getParams();
		
		return params;
	}
	
	/**
	 * 获取页面参数（搜索，分页）
	 * @return
	 */
	protected JSONObject getPageParams(){
		JSONObject params = this.getSearchParams();
		if(!params.containsKey("page")){
			params.put("page", 1);
		}
		if(!params.containsKey("size")){
			params.put("size", Constant.PAGE_SIZE);
		}
		return params;
	}
	
	protected JSONObject getBodyParam(){
		JSONObject res = this.getParams();
		if(res!=null && !res.isEmpty()){
			return res;
		}

		return JSONObject.parseObject(this.getStringFromBody());
	}

	protected JSONArray getBodyParamToJSONArray() {
		return JSONArray.parseArray(this.getStringFromBody());
	}

	protected JSONObject getBodyXml(){
		JSONObject res = this.getParams();
		if(res!=null && !res.isEmpty()){
			return res;
		}

		Document document = XmlKit.parse(this.getStringFromBody());
		Element root = document.getDocumentElement();
		JSONObject params = new JSONObject();
		NodeList list = root.getChildNodes();
		for(int i = 0; i < list.getLength(); ++i) {
			Node node = list.item(i);
			params.put(node.getNodeName(), node.getTextContent());
		}

		params.remove("#text");
		return params;
	}

	protected String getStringFromBody(){
		StringBuilder string = new StringBuilder();
		try {
			BufferedReader reader = this.getRequest().getReader();
			String line = null;
			while((line = reader.readLine()) != null){
//				if(line.contains("=")){
//					continue;
//				}
				string.append(line);
			}
			reader.close();
		} catch (IOException e) {}
		return string.toString();
	}

	protected ExecutorService getGlobalThreadPool() {
		try {
			ExecutorService fixedThreadPool = (ExecutorService) getRequest().getServletContext().getAttribute("globalThreadPool");
			return fixedThreadPool;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void setClientUserId(long userId) {
		JFinal.me().getServletContext().setAttribute(Storage.CLIENT_USER_ID, userId);
	}

	/**
	 * 获取本机IP
	 * @return
	 */
	protected String getLocalIp() {
		Object obj = JFinal.me().getServletContext().getAttribute(Storage.LOCAL_IP);
		if (obj == null) {
			List<String> ipList = ToolUtil.getLocalIPList();
			for (String ip : ipList) {
				try {
					if ("127.0.0.1".equals(ip)) continue;
					int port = getRequest().getServerPort();
					String tmp = HttpKit.get("http://" + ip+":"+port+"/test/hi");
					if (StrKit.isBlank(tmp)) continue;
					JFinal.me().getServletContext().setAttribute(Constant.Storage.LOCAL_IP, ip);
					return ip;
				} catch (Exception e) {

				}
			}
		}
		return obj.toString();
	}

	/**
	 * 设置本机IP
	 * @return
	 */
	protected void setLocalIp(String ip) {
		JFinal.me().getServletContext().setAttribute(Constant.Storage.LOCAL_IP, ip);
	}


	/**
	 * 获取安装目录
	 * @return
	 */
	protected String getInstallDir() {
		String os = System.getProperty("os.name");
		if(os.toLowerCase().startsWith("win")){
			return PropKit.get("install.window.path", "D:/Program Files/talos/apache-tomcat/webapps/ROOT");
		}else{
			return PropKit.get("install.linux.path");
		}
	}

	/**
	 * 获取上传目录
	 * @return
	 */
	protected String getUploadDir() {
		String os = System.getProperty("os.name");
		if(os.toLowerCase().startsWith("win")){
			return PropKit.get("upload.window.path");
		}else{
			return PropKit.get("upload.linux.path");
		}
	}
}
