package com.ggbook.controller.book;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ggbook.controller.BaseController;
import com.ggbook.model.book.Book;
import com.ggbook.model.book.ErrBookLog;
import com.ggbook.model.book.ErrPagesLog;
import com.ggbook.model.book.Pages;
import com.ggbook.service.book.BookService;
import com.ggbook.service.book.PagesService;
import com.jfinal.kit.JsonKit;

import java.util.ArrayList;
import java.util.List;

/**
 * 抓取书籍Controller
 */
public class GrabController extends BaseController {

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

    /**
     * 根据code查询Book表
     */
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

    /**
     * 查询所有书籍 asc
     */
    public void list() {
        renderRb(BookService.me.list());
    }

    /**
     * 查询所有书籍 desc
     */
    public void listDesc() {
        renderRb(BookService.me.listDesc());
    }

    /**
     * 保存书本章节
     */
    public void savePage() {
        JSONObject params = super.getBodyParam();
        JSONArray pageArr = params.getJSONArray("pageArr");
        List<Pages> list = new ArrayList();
        for(Object obj : pageArr) {
            JSONObject jObj = (JSONObject) obj;
            Pages page = new Pages();
            page.set("bookCode", jObj.getString("bookCode"));
            page.set("name", jObj.getString("name"));
            page.set("code", jObj.getString("code"));
            list.add(page);
        }
        try {
            PagesService.me.saveBatch(list);
            renderSucc();
        } catch(Exception e) {
            e.printStackTrace();
            renderErr("保存失败");
        }
    }

    /**
     * 根据code查询Page表 判断重复
     */
    public void getPageByCodes(){
        JSONObject params = super.getBodyParam();
        String[] codes = params.getString("codes").split(",");
        String bookCode = params.getString("bookCode");
        List<Pages> list = PagesService.me.getByCodes(codes, bookCode);
        JSONObject json = new JSONObject();
        List<String> codeArr = new ArrayList();
        for(Pages page : list) {
            codeArr.add(page.get("code"));
        }
        json.put("rCodeArr", JsonKit.toJson(codeArr));
        json.put("count", list.size());
        renderRb(json);
    }

    /**
     * 保存错误book日志
     */
    public void saveBookErrLog() {
        JSONObject params = super.getBodyParam();
        String code = params.getString("code");
        int type = params.getIntValue("type");

        ErrBookLog log = new ErrBookLog();
        log.set("type", type);
        log.set("code", code);
        if(log.save()) {
            renderSucc();
            return ;
        }
        renderErr("保存失败");
    }

    /**
     * 保存错误pages日志
     */
    public void savePagesErrLog() {
        JSONObject params = super.getBodyParam();
        String code = params.getString("code");
        String bookCode = params.getString("bookCode");
        int type = params.getIntValue("type");

        ErrPagesLog log = new ErrPagesLog();
        log.set("type", type);
        log.set("code", code);
        log.set("bookCode", bookCode);
        if(log.save()) {
            renderSucc();
            return ;
        }
        renderErr("保存失败");
    }

    public void updatePagesContent() {
        JSONObject params = super.getBodyParam();
        String code = params.getString("code");
        String bookCode = params.getString("bookCode");
        String content = params.getString("content");

        if(PagesService.me.update(code, bookCode, content) > 0) {
            renderSucc();
            return ;
        }
        renderErr("更新失败");
    }

    /**
     * 根据书籍code查询 内容为空的章节
     */
    public void getNullPagesByCode() {
        JSONObject params = super.getBodyParam();
        String bookCode = params.getString("bookCode");
        renderRb(PagesService.me.getNullPagesByCode(bookCode));
    }
}
