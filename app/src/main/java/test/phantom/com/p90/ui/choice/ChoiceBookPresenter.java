package test.phantom.com.p90.ui.choice;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import test.phantom.com.p90.base.SimpleObserver;
import test.phantom.com.p90.dao.DbHelper;
import test.phantom.com.p90.entity.BookShelfBean;
import test.phantom.com.p90.entity.RxBusTag;
import test.phantom.com.p90.entity.SearchBookBean;
import test.phantom.com.p90.model.WebBookModel;
import test.phantom.com.p90.ui.OnGetChapterListListener;
import test.phantom.com.p90.until.NetworkUtil;

/**
 * @author Administrator
 */
public class ChoiceBookPresenter implements ChoiceBookContract.Presenter {

    private String url = "";
    private String title;

    private int page = 1;
    private long startThisSearchTime;
    //用来比对搜索的书籍是否已经添加进书架
    private List<BookShelfBean> bookShelfs = new ArrayList<>();

    private ChoiceBookContract.View mView;

    @Inject
    public ChoiceBookPresenter(ChoiceBookContract.View view) {
        this.mView = view;
        RxBus.get().register(this);
        init();
    }

    private void init() {
        url = mView.getUrl();
        title = mView.getmTitle();
        Observable.create(new ObservableOnSubscribe<List<BookShelfBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookShelfBean>> e) throws Exception {
                List<BookShelfBean> temp = DbHelper.getInstance().getmDaoSession().getBookShelfBeanDao().queryBuilder
                        ().list();
                if (temp == null) {
                    temp = new ArrayList<BookShelfBean>();
                }
                e.onNext(temp);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<List<BookShelfBean>>() {
                    @Override
                    public void onNext(List<BookShelfBean> value) {
                        bookShelfs.addAll(value);
                        initPage();
                        toSearchBooks(null);
                        mView.startRefreshAnim();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void initPage() {
        this.page = 1;
        this.startThisSearchTime = System.currentTimeMillis();
    }

    @Override
    public void toSearchBooks(String key) {
        final long tempTime = startThisSearchTime;
        searchBook(tempTime);
    }


    private void searchBook(final long searchTime) {
        WebBookModel.Companion.getInstance().getKindBook(url, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<List<SearchBookBean>>() {
                    @Override
                    public void onNext(List<SearchBookBean> value) {
                        if (searchTime == startThisSearchTime) {
                            for (SearchBookBean temp : value) {
                                for (BookShelfBean bookShelfBean : bookShelfs) {
                                    if (temp.getNoteUrl().equals(bookShelfBean.getNoteUrl())) {
                                        temp.setAdd(true);
                                        break;
                                    }
                                }
                            }
                            if (page == 1) {
                                mView.refreshSearchBook(value);
//                                mView.refreshFinish(value.size() <= 0 ? true : false);
                            } else {
                                mView.loadMoreSearchBook(value);
//                                mView.loadMoreFinish(value.size() <= 0 ? true : false);
                            }
                            page++;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.searchBookError();
                    }
                });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void addBookToShelf(final SearchBookBean searchBookBean) {
        final BookShelfBean bookShelfResult = new BookShelfBean();
        bookShelfResult.setNoteUrl(searchBookBean.getNoteUrl());
        bookShelfResult.setFinalDate(0);
        bookShelfResult.setDurChapter(0);
        bookShelfResult.setDurChapterPage(0);
        bookShelfResult.setTag(searchBookBean.getTag());
        WebBookModel.Companion.getInstance().getBookInfo(bookShelfResult)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<BookShelfBean>() {
                    @Override
                    public void onNext(BookShelfBean value) {
                        WebBookModel.Companion.getInstance().getChapter(value, new OnGetChapterListListener() {
                            @Override
                            public void success(BookShelfBean bookShelfBean) {
                                saveBookToShelf(bookShelfBean);
                            }

                            @Override
                            public void error() {
                                mView.addBookShelfFailed(NetworkUtil.ERROR_CODE_OUTTIME);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.addBookShelfFailed(NetworkUtil.ERROR_CODE_OUTTIME);
                    }
                });
    }

    @Override
    public String getTitle() {
        return title;
    }

    private void saveBookToShelf(final BookShelfBean bookShelfBean) {
        Observable.create(new ObservableOnSubscribe<BookShelfBean>() {
            @Override
            public void subscribe(ObservableEmitter<BookShelfBean> e) throws Exception {
                DbHelper.getInstance().getmDaoSession().getChapterListBeanDao().insertOrReplaceInTx(bookShelfBean
                        .getBookInfoBean().getChapterlist());
                DbHelper.getInstance().getmDaoSession().getBookInfoBeanDao().insertOrReplace(bookShelfBean
                        .getBookInfoBean());
                //网络数据获取成功  存入BookShelf表数据库
                DbHelper.getInstance().getmDaoSession().getBookShelfBeanDao().insertOrReplace(bookShelfBean);
                e.onNext(bookShelfBean);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<BookShelfBean>() {
                    @Override
                    public void onNext(BookShelfBean value) {
                        //成功   //发送RxBus
                        RxBus.get().post(RxBusTag.HAD_ADD_BOOK, value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.addBookShelfFailed(NetworkUtil.ERROR_CODE_OUTTIME);
                    }
                });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public void detachView() {
        RxBus.get().unregister(this);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(RxBusTag.HAD_ADD_BOOK)
            }
    )
    public void hadAddBook(BookShelfBean bookShelfBean) {
        bookShelfs.add(bookShelfBean);
        List<SearchBookBean> datas = mView.getSearchBookData();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getNoteUrl().equals(bookShelfBean.getNoteUrl())) {
                datas.get(i).setAdd(true);
                mView.updateSearchItem(i);
                break;
            }
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(RxBusTag.HAD_REMOVE_BOOK)
            }
    )
    public void hadRemoveBook(BookShelfBean bookShelfBean) {
        if (bookShelfs != null) {
            for (int i = 0; i < bookShelfs.size(); i++) {
                if (bookShelfs.get(i).getNoteUrl().equals(bookShelfBean.getNoteUrl())) {
                    bookShelfs.remove(i);
                    break;
                }
            }
        }
        List<SearchBookBean> datas = mView.getSearchBookData();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getNoteUrl().equals(bookShelfBean.getNoteUrl())) {
                datas.get(i).setAdd(false);
                mView.updateSearchItem(i);
                break;
            }
        }
    }
}