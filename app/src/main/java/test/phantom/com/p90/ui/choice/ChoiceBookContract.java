package test.phantom.com.p90.ui.choice;

import java.util.List;

import test.phantom.com.p90.base.BasePresenter;
import test.phantom.com.p90.entity.SearchBookBean;

/**
 * Created by phantom on 2017/12/28.
 */


public interface ChoiceBookContract {


    interface View {

        void refreshSearchBook(List<SearchBookBean> books);

        void loadMoreSearchBook(List<SearchBookBean> books);

        void searchBookError();

        void addBookShelfSuccess(List<SearchBookBean> searchBooks);

        void addBookShelfFailed(int code);

        void updateSearchItem(int index);

        void startRefreshAnim();

        String getUrl();

        String getmTitle();

        List<SearchBookBean> getSearchBookData();
    }

    interface Presenter extends BasePresenter {
        int getPage();

        void initPage();

        void toSearchBooks(String key);

        void addBookToShelf(final SearchBookBean searchBookBean);

        String getTitle();
    }
}
