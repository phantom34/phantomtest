//Copyright (c) 2017. 章钦豪. All rights reserved.
package test.phantom.com.p90.ui.library;

import android.os.Handler;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import test.phantom.com.p90.base.SimpleObserver;
import test.phantom.com.p90.cache.ACache;
import test.phantom.com.p90.entity.LibraryBean;
import test.phantom.com.p90.injector.BaseApplication;
import test.phantom.com.p90.model.GxwztvBookModel;

public class LibraryPresenter implements LibraryContract.Presenter {
    public final static String LIBRARY_CACHE_KEY = "cache_library";
    private ACache mCache;
    private Boolean isFirst = true;

    private final LinkedHashMap<String, String> kinds = new LinkedHashMap<>();

    private LibraryContract.View mView;

    @Inject
    public LibraryPresenter(LibraryContract.View view) {
        this.mView = view;
        init();
    }

    private void init() {
        kinds.put("东方玄幻", "http://www.gxwztv.com/xuanhuanxiaoshuo/");
        kinds.put("西方奇幻", "http://www.gxwztv.com/qihuanxiaoshuo/");
        kinds.put("热血修真", "http://www.gxwztv.com/xiuzhenxiaoshuo/");
        kinds.put("武侠仙侠", "http://www.gxwztv.com/wuxiaxiaoshuo/");
        kinds.put("都市爽文", "http://www.gxwztv.com/dushixiaoshuo/");
        kinds.put("言情暧昧", "http://www.gxwztv.com/yanqingxiaoshuo/");
        kinds.put("灵异悬疑", "http://www.gxwztv.com/lingyixiaoshuo/");
        kinds.put("运动竞技", "http://www.gxwztv.com/jingjixiaoshuo/");
        kinds.put("历史架空", "http://www.gxwztv.com/lishixiaoshuo/");
        kinds.put("审美", "http://www.gxwztv.com/danmeixiaoshuo/");
        kinds.put("科幻迷航", "http://www.gxwztv.com/kehuanxiaoshuo/");
        kinds.put("游戏人生", "http://www.gxwztv.com/youxixiaoshuo/");
        kinds.put("军事斗争", "http://www.gxwztv.com/junshixiaoshuo/");
        kinds.put("商战人生", "http://www.gxwztv.com/shangzhanxiaoshuo/");
        kinds.put("校园爱情", "http://www.gxwztv.com/xiaoyuanxiaoshuo/");
        kinds.put("官场仕途", "http://www.gxwztv.com/guanchangxiaoshuo/");
        kinds.put("娱乐明星", "http://www.gxwztv.com/zhichangxiaoshuo/");
        kinds.put("其他", "http://www.gxwztv.com/qitaxiaoshuo/");

        mCache = ACache.get(BaseApplication.getInstance());
    }

    public void detachView() {

    }

    @Override
    public void getLibraryData() {
        if (isFirst) {
            isFirst = false;
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> e) throws Exception {
                    String cache = mCache.getAsString(LIBRARY_CACHE_KEY);
                    e.onNext(cache);
                    e.onComplete();
                }
            }).flatMap(new Function<String, ObservableSource<LibraryBean>>() {
                @Override
                public ObservableSource<LibraryBean> apply(String s) throws Exception {
                    return GxwztvBookModel.getInstance().analyLibraryData(s);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SimpleObserver<LibraryBean>() {
                        @Override
                        public void onNext(LibraryBean value) {
                            //执行刷新界面
                            mView.updateUI(value);
                            getLibraryNewData();
                        }

                        @Override
                        public void onError(Throwable e) {
                            getLibraryNewData();
                        }
                    });
        } else {
            getLibraryNewData();
        }
    }

    private void getLibraryNewData() {
        GxwztvBookModel.getInstance().getLibraryData(mCache).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<LibraryBean>() {
                    @Override
                    public void onNext(final LibraryBean value) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mView.updateUI(value);
                                mView.finishRefresh();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.finishRefresh();
                    }
                });
    }

    @Override
    public LinkedHashMap<String, String> getKinds() {
        return kinds;
    }
}