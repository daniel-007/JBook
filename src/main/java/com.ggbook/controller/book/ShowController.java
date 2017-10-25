package com.ggbook.controller.book;

import com.ggbook.controller.BaseController;

public class ShowController extends BaseController {

    public void category() {
        render("/page/book/category.html");
    }

    public void index() {
        render("/page/book/index.html");
    }

    public void details() {
        render("/page/book/details.html");
    }

    public void list() {
        render("/page/book/list.html");
    }

    public void section() {
        render("/page/book/section.html");
    }
}
