package test.phantom.com.p90.model

import io.reactivex.Observable
import test.phantom.com.p90.entity.BookContentBean
import test.phantom.com.p90.entity.BookShelfBean
import test.phantom.com.p90.entity.SearchBookBean
import test.phantom.com.p90.ui.OnGetChapterListListener

/**
 * Created by phantom on 2017/12/14.
 */
class WebBookModel private constructor() {

    private object Holder {
        val INSTANCE = WebBookModel()
    }

    companion object {
        val instance: WebBookModel by lazy { Holder.INSTANCE }
    }

    /**
     * 网络请求并解析书籍信息
     * return BookShelfBean
     */
    fun getBookInfo(book: BookShelfBean): Observable<BookShelfBean>? {
        when (book.tag) {
            GxwztvBookModel.TAG -> {
                return GxwztvBookModel.getInstance().getBookInfo(book)
            }
            else -> return null
        }
    }

    /**
     * 网络解析图书目录
     * return BookShelfBean
     */
    fun getChapter(book: BookShelfBean, listener: OnGetChapterListListener) {
        when (book.tag) {
            GxwztvBookModel.TAG -> GxwztvBookModel.getInstance().getBookInfo(book)
        }

    }

    /**
     * 章节缓存
     */
    fun getBookContent(durChapterUrl: String, durChapterIndex: Int, tag: String): Observable<BookContentBean> =
            when (tag) {
                GxwztvBookModel.TAG -> {
                    GxwztvBookModel.getInstance().getBookContent(durChapterUrl, durChapterIndex)
                }
                else -> {
                    GxwztvBookModel.getInstance().getBookContent(durChapterUrl, durChapterIndex)
                }
            }

    /**
     * 其他站点集合搜索
     */
    fun searchOtherBook(content: String, page: Int, tag: String): Observable<List<SearchBookBean>>? =
            when (tag) {
                GxwztvBookModel.TAG -> {
                    GxwztvBookModel.getInstance().searchBook(content, page)
                }
                else -> {
                    null
                }
            }

    /**
     * 获取分类书籍
     */
    fun getKindBook(url: String, page: Int) =
            GxwztvBookModel.getInstance().getKindBook(url, page)


}