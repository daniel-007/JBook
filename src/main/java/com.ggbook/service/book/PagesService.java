package com.ggbook.service.book;

import com.alibaba.fastjson.JSONObject;
import com.ggbook.model.book.Pages;
import com.ggbook.utils.RedisKit;
import com.jfinal.ext.kit.ModelKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.mongodb.DBObject;

import java.util.List;

/**
 * 书籍章节内容Service
 */
public class PagesService {
    public static final PagesService me = new PagesService();

    /**
     * 批处理保存拉取数据
     * @param list
     */
    public void saveBatch(List<Pages> list) {
        ModelKit.batchSave(list);
    }

    /**
     * 根据code集合查询
     * @param codes
     * @return
     */
    public List<Pages> getByCodes(String[] codes, String bookCode){
        String[] strArr = new String[codes.length];
        for(int i=0; i<codes.length; i++) {
            strArr[i] = "?";
        }
        StringBuffer sqls = new StringBuffer();
        sqls.append("SELECT * ")
                .append("FROM t_pages ")
                .append("WHERE bookCode = '" + bookCode + "' AND code IN (" + StrKit.join(strArr, ",") + ")");
        return Pages.me.find(sqls.toString(), codes);
    }

    /**
     * 根据code和bookCode更新
     * @param bookCode
     * @param code
     * @param content
     * @return
     */
    public int update(String bookCode, String code, String content){
        if(!Pages.me.setValue(bookCode, code, content)) {
            return 0;
        }
        return Db.update("UPDATE t_pages SET `status` = 2 WHERE code = ? AND bookCode = ? ", code, bookCode);
    }

    /**
     * 根据书籍code查询 内容为空的章节
     * @param bookCode
     * @return
     */
    public List<Pages> getNullPagesByCode(String bookCode){
        return Pages.me.find("SELECT * FROM t_pages WHERE bookCode = ? AND status = 1 ", bookCode);
    }
}
