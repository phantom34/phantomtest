package test.phantom.com.p90.ui.searchbook;

import android.widget.EditText;

import java.util.List;

import test.phantom.com.p90.base.BasePresenter;
import test.phantom.com.p90.entity.SearchBookBean;
import test.phantom.com.p90.entity.SearchHistoryBean;

/**
 * Created by phantom on 2017/12/28.
 */

public interface SearchContarct {

    interface View {
        /**
         * 成功 新增查询记录
         */
        void insertSearchHistorySuccess(SearchHistoryBean searchHistoryBean);

        /**
         * 成功搜索 搜索记录
         */
        void querySearchHistorySuccess(List<SearchHistoryBean> datas);

//        /**
//         * 首次查询成功 更新UI
//         */
//        void refreshSearchBook(List<SearchBookBean> books);
//
        /**
         * 加载更多书籍成功 更新UI
         */
        void loadMoreSearchBook(List<SearchBookBean> books);

//        /**
//         * 搜索失败
//         */
//        void searchBookError(Boolean isRefresh);

        /**
         * 获取搜索内容EditText
         */
        EditText getEdtContent();

        /**
         * 添加书籍失败
         */
        void addBookShelfFailed(int code);

        SearchBookAdapter getSearchBookAdapter();

        void updateSearchItem(int index);

        /**
         * 判断书籍是否已经在书架上
         */
        Boolean checkIsExist(SearchBookBean searchBookBean);

        List<SearchBookBean> getListData();
    }

    interface Presenter extends BasePresenter {

        Boolean getHasSearch();

        void setHasSearch(Boolean hasSearch);

        void insertSearchHistory();

        void querySearchHistory();

        void cleanSearchHistory();

        int getPage();

        void initPage();

        void toSearchBooks(String key, Boolean fromError);

        void addBookToShelf(final SearchBookBean searchBookBean);

        Boolean getInput();

        void setInput(Boolean input);
    }
}
