package com.ggbook.controller.book;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ggbook.constants.Constant;
import com.ggbook.controller.BaseController;
import com.ggbook.model.book.Book;
import com.ggbook.service.book.BookService;
import com.ggbook.utils.RequestUtil;
import com.jfinal.kit.JsonKit;

import java.util.ArrayList;
import java.util.List;

/**
 * 书籍Controller
 * 2017-8-30 23:34:46
 */
public class BookController extends BaseController {

    public void index() {
        JSONObject jData = JSONObject.parseObject(RequestUtil.sendGet(Constant.URL_PREFIX+"book/index", null));
        if(!jData.getBoolean("status")) {
            renderErr(jData.getString("msg"));
            return;
        }
        renderRb(jData.getJSONObject("body"));
    }
}
