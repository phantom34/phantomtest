package test.phantom.com.p90.ui.importbook;

import java.io.File;
import java.util.List;

import test.phantom.com.p90.base.BasePresenter;

/**
 * Created by phantom on 2017/12/28.
 */

public interface ImportBookContract {

    interface View {

        /**
         * 新增书籍
         * @param newFile
         */
        void addNewBook(File newFile);

        /**
         * 书籍搜索完成
         */
        void searchFinish();

        /**
         * 添加成功
         */
        void addSuccess();

        /**
         * 添加失败
         */
        void addError();
    }

    interface Presenter extends BasePresenter {
        void searchLocationBook();

        void importBooks(List<File> books);

    }

}
