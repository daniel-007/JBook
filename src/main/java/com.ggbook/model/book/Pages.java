package com.ggbook.model.book;

import com.ggbook.model.BaseModel;
import com.ggbook.utils.RedisKit;

/**
 * 书籍章节内容
 */
public class Pages extends BaseModel<Pages> {
    public static final Pages me = new Pages();
    public static final String TABLE = "t_pages";

    /**
     * 从redis获取
     * @return
     */
    public String getValue() {
        String value = null;
        try {
            value = getValue(getStr("bookCode"), getStr("code"));
            put("value", value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 从redis获取
     * @return
     */
    public String getValue(String bookCode, String code) {
        String value = null;
        try {
            String key = "ext_" + bookCode + "_" + code;
            value = RedisKit.get(key);
            put("value", value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 保存到Redis
     * @param value
     */
    public boolean setValue(String value) {
        try {
            return setValue(getStr("bookCode"), getStr("code"), value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存到Redis
     * @param value
     */
    public boolean setValue(String bookCode, String code, String value) {
        try {
            String key = "ext_" + bookCode + "_" + code;
            RedisKit.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
