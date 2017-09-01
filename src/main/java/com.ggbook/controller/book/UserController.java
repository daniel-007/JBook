package com.ggbook.controller.book;

import com.alibaba.fastjson.JSONObject;
import com.ggbook.controller.BaseController;
import com.ggbook.model.book.User;

public class UserController extends BaseController {

    /**
     * 后台列表
     */
    public void list(){
        JSONObject params = super.getPageParams();
        renderRb(User.me.list(params));
    }
}
