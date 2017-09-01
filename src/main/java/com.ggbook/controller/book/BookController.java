package com.ggbook.controller.book;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ggbook.controller.BaseController;
import com.ggbook.model.book.Book;
import com.ggbook.service.book.BookService;
import com.jfinal.kit.JsonKit;

import java.util.ArrayList;
import java.util.List;

/**
 * 书籍Controller
 * 2017-8-30 23:34:46
 */
public class BookController extends BaseController {

    /**
     * 保存书籍
     */
    public void save() {
        JSONObject params = super.getBodyParam();
        JSONArray books = params.getJSONArray("books");
        List<Book> list = new ArrayList();
        for(Object obj : books) {
            JSONObject jObj = (JSONObject) obj;
            Book book = new Book();
            book.set("code", jObj.getString("code"));
            book.set("name", jObj.getString("name"));
            book.set("author", jObj.getString("author"));
            book.set("brief", jObj.getString("brief"));
            book.set("url", jObj.getString("url"));
            book.set("cateId", jObj.getLongValue("cateId"));
            list.add(book);
        }
        try {
            BookService.me.saveBatch(list);
            renderSucc();
        } catch(Exception e) {
            e.printStackTrace();
            renderErr("保存失败");
        }
    }

    public void getByCodes(){
        JSONObject params = super.getBodyParam();
        String[] codes = params.getString("codes").split(",");
        List<Book> list = BookService.me.getByCodes(codes);
        JSONObject json = new JSONObject();
        List<String> codeArr = new ArrayList();
        for(Book book : list) {
            codeArr.add(book.get("code"));
        }
        json.put("rCodeArr", JsonKit.toJson(codeArr));
        json.put("count", list.size());
        renderRb(json);
    }
}
