package com.ggbook.service.book;

import com.ggbook.model.book.Pages;
import com.jfinal.ext.kit.ModelKit;
import com.jfinal.kit.StrKit;

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
}
