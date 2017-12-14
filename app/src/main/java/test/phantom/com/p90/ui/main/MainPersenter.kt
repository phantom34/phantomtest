package test.phantom.com.p90.ui.main

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import test.phantom.com.p90.base.BasePresenter
import test.phantom.com.p90.base.SimpleObserver
import test.phantom.com.p90.dao.BookInfoBeanDao
import test.phantom.com.p90.dao.BookShelfBeanDao
import test.phantom.com.p90.dao.ChapterListBeanDao
import test.phantom.com.p90.dao.DbHelper
import test.phantom.com.p90.entity.BookShelfBean
import javax.inject.Inject


/**
 * Created by phantom on 2017/12/12.
 */
class MainPersenter : BasePresenter {

    private var mView: MainActivity? = null

    @Inject
    fun MainPersenter(mainActivity: MainActivity) {
        this.mView = mainActivity
    }


    fun queryBookShelf() {

        Observable.create(ObservableOnSubscribe<List<BookShelfBean>> { e ->
            val bookShelfes = DbHelper.getInstance().getmDaoSession().bookShelfBeanDao.queryBuilder()
                    .orderDesc(BookShelfBeanDao.Properties.FinalDate).list()
            var i = 0
            while (i < bookShelfes!!.size) {
                val temp = DbHelper.getInstance().getmDaoSession().bookInfoBeanDao.queryBuilder()
                        .where(BookInfoBeanDao.Properties.NoteUrl.eq(bookShelfes!![i].noteUrl)).limit(1).build().list()
                if (temp != null && temp.size > 0) {
                    val bookInfoBean = temp[0]
                    bookInfoBean.chapterlist = DbHelper.getInstance().getmDaoSession().chapterListBeanDao.queryBuilder()
                            .where(ChapterListBeanDao.Properties.NoteUrl.eq(bookShelfes[i].noteUrl))
                            .orderAsc(ChapterListBeanDao.Properties.DurChapterIndex).build().list()
                    bookShelfes[i].bookInfoBean = bookInfoBean
                } else {
                    DbHelper.getInstance().getmDaoSession().bookShelfBeanDao.delete(bookShelfes[i])
                    bookShelfes.removeAt(i)
                    i--
                }
                i++
            }
            e.onNext(bookShelfes)

        })
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SimpleObserver<List<BookShelfBean>>() {
                    override fun onNext(t: List<BookShelfBean>?) {
                        if (t != null) {
                            mView!!.setData(t)
                        }
                    }

                    override fun onError(e: Throwable?) {

                    }


                })

    }

    /**
     * 判断书籍数据 是否更新
     */
    fun refreshBookShelf(list: List<BookShelfBean>, index: Int) {
        if (index < list.size - 1) {

        }

    }


    fun saveBookShelf(list: List<BookShelfBean>, index: Int) {
        Observable.create(ObservableOnSubscribe<BookShelfBean> { e ->
            DbHelper.getInstance().getmDaoSession().chapterListBeanDao.insertOrReplaceInTx(list[index].bookInfoBean.chapterlist)
            e.onNext(list[index])
            e.onComplete()
        })
                .subscribeOn(Schedulers.io())
                .subscribe(object : SimpleObserver<BookShelfBean>() {
                    override fun onNext(t: BookShelfBean?) {
                        refreshBookShelf(list, index + 1)
                    }

                    override fun onError(e: Throwable?) {
                    }
                })


    }


}