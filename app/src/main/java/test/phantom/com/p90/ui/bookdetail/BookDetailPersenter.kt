package test.phantom.com.p90.ui.bookdetail

import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import test.phantom.com.p90.base.BasePresenter
import test.phantom.com.p90.base.SimpleObserver
import test.phantom.com.p90.dao.DbHelper
import test.phantom.com.p90.entity.BookShelfBean
import test.phantom.com.p90.entity.SearchBookBean
import test.phantom.com.p90.injector.BaseApplication
import test.phantom.com.p90.model.WebBookModel
import test.phantom.com.p90.ui.OnGetChapterListListener
import java.util.*
import javax.inject.Inject


/**
 * Created by phantom on 2017/12/14.
 */
class BookDetailPersenter : BasePresenter {

    private lateinit var mView: BookDetailActivity

    private var bookShelfs = Collections.synchronizedList(ArrayList<BookShelfBean>())   //用来比对搜索的书籍是否已经添加进书架
    private var searchBook: SearchBookBean? = null
    private var inBookShelf: Boolean? = false
    private var bookShelf: BookShelfBean? = null
    private val openfrom: Int = 0

    @Inject
    fun BookDetailPersenter(activity: BookDetailActivity) {
        this.mView = activity
    }


    fun getInBookShelf(): Boolean? = inBookShelf

    fun setInBookShelf(inBookShelf: Boolean?) {
        this.inBookShelf = inBookShelf
    }

    fun getOpenfrom(): Int = openfrom

    fun getSearchBook(): SearchBookBean? = searchBook

    fun getBookShelf(): BookShelfBean? = bookShelf


    fun getBookShlfInfo() {

        Observable.create(ObservableOnSubscribe<List<BookShelfBean>> { e ->
            var temp = DbHelper.getInstance().getmDaoSession().bookShelfBeanDao.queryBuilder().list()
            if (temp == null) {
                temp = ArrayList()
            }
            e.onNext(temp)
            e.onComplete()
        })
                .flatMap { bookShelfBeen ->
                    bookShelfs!!.addAll(bookShelfBeen)

                    val bookShelfResult = BookShelfBean()
                    bookShelfResult.noteUrl = searchBook!!.noteUrl
                    bookShelfResult.finalDate = System.currentTimeMillis()
                    bookShelfResult.durChapter = 0
                    bookShelfResult.durChapterPage = 0
                    bookShelfResult.tag = searchBook!!.tag
                    WebBookModel.instance.getBookInfo(bookShelfResult) as ObservableSource<out BookShelfBean>
                }
                .map { it ->
                    for (i in bookShelfs!!.indices) {
                        if (bookShelfs[i].noteUrl == it.noteUrl) {
                            inBookShelf = true
                            break
                        }
                    }
                    it
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SimpleObserver<BookShelfBean>() {
                    override fun onNext(t: BookShelfBean) {
                        WebBookModel.instance.getChapter(t, object : OnGetChapterListListener {
                            override fun success(bookShelfBean: BookShelfBean) {
                                bookShelf = bookShelfBean
//                                mView.updateView()
                            }

                            override fun error() {
                                bookShelf = null
//                                mView.getBookShelfError()
                            }
                        })
                    }

                    override fun onError(e: Throwable?) {
                        bookShelf = null
                    }
                })
    }

    fun addBookShlf() {
        Observable.create(ObservableOnSubscribe<Boolean> { e ->
            DbHelper.getInstance().getmDaoSession().chapterListBeanDao.insertOrReplaceInTx(bookShelf!!.bookInfoBean.chapterlist)
            DbHelper.getInstance().getmDaoSession().bookInfoBeanDao.insertOrReplace(bookShelf!!.bookInfoBean)
            //网络数据获取成功  存入BookShelf表数据库
            DbHelper.getInstance().getmDaoSession().bookShelfBeanDao.insertOrReplace(bookShelf)
            e.onNext(true)
            e.onComplete()
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
//                .compose((mView as BaseActivity).bindUntilEvent<Boolean>(ActivityEvent.DESTROY))
                .subscribe(object : SimpleObserver<Boolean>() {
                    override fun onNext(value: Boolean?) {
                        if (value!!) {
//                            RxBus.get().post(RxBusTag.HAD_ADD_BOOK, bookShelf)
                        } else {
                            Toast.makeText(BaseApplication.getInstance(), "放入书架失败!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        Toast.makeText(BaseApplication.getInstance(), "放入书架失败!", Toast.LENGTH_SHORT).show()
                    }
                })
    }
}