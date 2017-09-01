package com.ggbook.model.book;

import com.alibaba.fastjson.JSONObject;
import com.ggbook.model.BaseModel;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User extends BaseModel<User> {
    public static final User me = new User();
    public static final String TABLE = "t_user";

    /**
     * 保存或修改
     * @param User
     * @return
     */
    public boolean save(User User){
        if(User == null){
            return false;
        }

        long id = User.get("id", 0l);
        if(id>0){
            User dbUser = me.findById(id);
            /*
            correct 这个方法是用于当页面没有传某些值上来而进行保存对象的情况
            例如对象如下：id,name,creater,createDt
            页面传递的值只有id,name
            那么要从数据中获取creater,createDt，并设置到页面传上来的对象中，否则该字段会被覆盖成空值
             */
            User = super.correct(dbUser, User);
        }

        if(StrKit.isBlank(User.getStr("name"))){
            return false;
        }
        if(id>0){
            return User.update();
        }else{
            return User.save();
        }
    }

    /**
     * 页面获取列表
     * @param params 页面的参数(包含page，size等)
     * @return
     */
    public Page<User> list(JSONObject params){
        List<Object> paras = new ArrayList<Object>();
        StringBuffer sqls = new StringBuffer();
        sqls.append("FROM " + TABLE +" WHERE 1=1 ");

        /*
        setSql 这个方法用于设置常用的搜索条件
        String.class：数据库正则
        Number.class：数值相等
        * */
        Map<String, Object> keys  = new JSONObject();
        keys.put("name", String.class);
        keys.put("type", Number.class);
        keys.put("status", Number.class);
        super.setSql(params, paras, sqls, keys);

        int page = params.getIntValue("page");
        int size = params.getIntValue("size");
        String select = "SELECT * ";
        return me.paginate(page, size, select, sqls.toString(), paras.toArray());
    }

    /**
     * 修改状态
     * @param params
     * @return
     */
    public int status(JSONObject params){
        Object[] paras = {params.get("status"), params.get("id")};
        return Db.update("UPDATE "+TABLE+" SET `status`=? WHERE `id`=? ", paras);
    }

    /**
     * 修改类型
     * @param params
     * @return
     */
    public int type(JSONObject params){
        Object[] paras = {params.get("type"), params.get("id")};
        return Db.update("UPDATE "+TABLE+" SET `type`=? WHERE `id`=? ", paras);
    }

}