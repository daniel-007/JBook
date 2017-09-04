package com.ggbook.service.book;

import com.ggbook.model.book.Book;
import com.jfinal.ext.kit.ModelKit;
import com.jfinal.kit.StrKit;

import java.util.List;

/**
 * 2017/8/30.
 */
public class BookService {
    public static final BookService me = new BookService();

    /**
     * 批处理保存拉取数据
     * @param list
     */
    public void saveBatch(List<Book> list) {
        ModelKit.batchSave(list);
    }

    /**
     * 根据code集合查询
     * @param codes
     * @return
     */
    public List<Book> getByCodes(String[] codes){
        String[] strArr = new String[codes.length];
        for(int i=0; i<codes.length; i++) {
            strArr[i] = "?";
        }
        StringBuffer sqls = new StringBuffer();
        sqls.append("SELECT * ")
                .append("FROM t_book ")
                .append("WHERE code IN (" + StrKit.join(strArr, ",") + ")");
        return Book.me.find(sqls.toString(), codes);
    }

    public List<Book> list() {
        return Book.me.find("SELECT * FROM t_book ");
    }
}
