package test.phantom.com.p90.ui.bookdetail

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import test.phantom.com.p90.base.BasePresenter
import test.phantom.com.p90.base.SimpleObserver
import test.phantom.com.p90.dao.DbHelper
import test.phantom.com.p90.entity.BookShelfBean
import test.phantom.com.p90.entity.SearchBookBean
import java.util.*
import javax.inject.Inject
import android.widget.Toast
import test.phantom.com.p90.base.BaseActivity
import io.reactivex.ObservableEmitter
import test.phantom.com.p90.injector.BaseApplication


/**
 * Created by phantom on 2017/12/14.
 */
class BookDetailPersenter : BasePresenter {

    private lateinit var mView: BookDetailActivity

    private var bookShelfs = Collections.synchronizedList(ArrayList<BookShelfBean>())   //用来比对搜索的书籍是否已经添加进书架
    private var searchBook: SearchBookBean? = null
    private var inBookShelf: Boolean? = false
    private val bookShelf: BookShelfBean? = null

    @Inject
    fun BookDetailPersenter(activity: BookDetailActivity) {
        this.mView = activity
    }


    fun getBookShlf() {

        Observable.create(ObservableOnSubscribe<List<BookShelfBean>> { e ->
            var temp = DbHelper.getInstance().getmDaoSession().bookShelfBeanDao.queryBuilder().list()
            if (temp != null) {
                temp = ArrayList()
            }
            e.onNext(temp)
            e.onComplete()
        })
                .flatMap { bookShelfBean ->
                    bookShelfs!!.addAll(bookShelfBean)

                    val bookShelfResult = BookShelfBean()
                    bookShelfResult.noteUrl = searchBook!!.noteUrl
                    bookShelfResult.finalDate = System.currentTimeMillis()
                    bookShelfResult.durChapter = 0
                    bookShelfResult.durChapterPage = 0
                    bookShelfResult.tag = searchBook!!.tag
                    WebBookModelImpl.getInstance().getBookInfo(bookShelfResult)
                }
                .map { bookShelfBean ->
                    for (i in bookShelfs!!.indices) {
                        if (bookShelfs[i].noteUrl == bookShelfBean.noteUrl) {
                            inBookShelf = true
                            break
                        }
                    }
                    bookShelfBean
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :SimpleObserver<BookShelfBean>(){
                    override fun onNext(t: BookShelfBean?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onError(e: Throwable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }

    fun addBookShlf(){
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