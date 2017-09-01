package com.ggbook.model.book;

import com.ggbook.model.BaseModel;

/**
 * 书籍
 */
public class Book extends BaseModel<Book> {
    public static final Book me = new Book();
    public static final String TABLE = "t_book";

}