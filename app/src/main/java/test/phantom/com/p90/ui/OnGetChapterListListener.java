package test.phantom.com.p90.ui;

import test.phantom.com.p90.entity.BookShelfBean;

public interface OnGetChapterListListener {
    public void success(BookShelfBean bookShelfBean);
    public void error();
}
