package com.ggbook.constants;

import java.util.Map;

/**
 * 错误消息业务类
 * 作用:将Servic的MSG及data扩展到Controller中
 */
public class MsgBean {

    private String msg;

    private Map data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
