package test.phantom.com.p90.ui.library;

import java.util.LinkedHashMap;

import test.phantom.com.p90.base.BasePresenter;
import test.phantom.com.p90.entity.LibraryBean;

/**
 * Created by phantom on 2017/12/28.
 */

public interface LibraryContract {


    interface View {
        /**
         * 书城书籍获取成功  更新UI
         */
        void updateUI(LibraryBean library);

        /**
         * 书城数据刷新成功 更新UI
         */
        void finishRefresh();
    }

    interface Presenter extends BasePresenter {

        LinkedHashMap<String, String> getKinds();

        void getLibraryData();
    }
}
