package test.phantom.com.p90.ui.readbook;

import android.app.Activity;
import android.graphics.Paint;

import test.phantom.com.p90.base.BasePresenter;
import test.phantom.com.p90.entity.BookShelfBean;
import test.phantom.com.p90.widget.BookContentView;

/**
 * Created by phantom on 2017/12/28.
 */

public interface ReadBookContract {

    interface View {
        /**
         * 获取当前阅读界面UI画笔
         * @return
         */
        Paint getPaint();

        /**
         * 获取当前小说内容可绘制宽度
         * @return
         */
        int getContentWidth();

        /**
         * 小说数据初始化成功
         * @param durChapterIndex
         * @param chapterAll
         * @param durPageIndex
         */
        void initContentSuccess(int durChapterIndex, int chapterAll, int durPageIndex);

        /**
         * 开始加载
         */
        void startLoadingBook();

        void setHpbReadProgressMax(int count);

        void initPop();

        void showLoadBook();

        void dimissLoadBook();

        void loadLocationBookError();

        void showDownloadMenu();
    }


    interface Presenter extends BasePresenter {
        int getOpen_from();

        BookShelfBean getBookShelf();

        void initContent();

        void loadContent(BookContentView bookContentView, long bookTag, final int chapterIndex, final int page);

        void updateProgress(int chapterIndex, int pageIndex);

        void saveProgress();

        String getChapterTitle(int chapterIndex);

        void setPageLineCount(int pageLineCount);

        void addToShelf(final ReadBookPresenter.OnAddListner addListner);

        Boolean getAdd();

        void initData(Activity activity);

        void openBookFromOther(Activity activity);
    }

}
