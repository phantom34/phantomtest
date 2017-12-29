package test.phantom.com.p90.ui.main.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import test.phantom.com.p90.R
import test.phantom.com.p90.base.BaseRecyclerAdapter
import test.phantom.com.p90.base.IViewHolder
import test.phantom.com.p90.entity.BookShelfBean
import test.phantom.com.p90.ui.main.OnItemClickListener

/**
 * Created by phantom on 2017/12/12.
 */
class MainInfoAdapter(context: Context, list: List<BookShelfBean>) : BaseRecyclerAdapter<BookShelfBean>(context, list) {

    val TYPE_LASTEST = 1
    val TYPE_OTHER = 2
    lateinit var mOnItemListener: OnItemClickListener

    override fun onBindViewHolder(holder: IViewHolder<*>?, position: Int, payloads: MutableList<Any>?) {
        super.onBindViewHolder(holder, position, payloads)
        if (list.size <= 0) {
            return
        }
    }


    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_LASTEST
        } else {
            return TYPE_OTHER
        }

    }

    /**
     * 当已观看书籍个数为0时 显示 初始化 最后观看页面
     * 所有观看过书籍每行三本
     */
    override fun getItemCount(): Int {
        if (list.size == 0) {
            return 1
        } else {
            if (list.size % 3 == 0) {
                return list.size / 3 + 1
            } else {
                return list.size / 3 + 2
            }
        }
    }


    /**
     * 分为最后一次观看的书籍  和 所有已观看过的 书籍列表
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): IViewHolder<*>? {
        val holder = super.onCreateViewHolder(parent, viewType)
        if (holder != null) {
            return holder
        }
        val view: View = if (viewType == TYPE_LASTEST) {
            mLayoutInflater.inflate(R.layout.adapter_bookshelf_lastest, parent, false)
        } else {
            mLayoutInflater.inflate(R.layout.adapter_bookshelf_other, parent, false)
        }
        view.setOnClickListener(this)
        return if (viewType == TYPE_LASTEST) {
            LastVideoHolder(view)
        } else {
            OtherVideoHolder(view)
        }
    }

    /**
     * 最后观看书籍
     */
    internal inner class LastVideoHolder(itemView: View) : IViewHolder<BookShelfBean>(itemView) {

        override fun setData(data: BookShelfBean) {
            super.setData(data)
            if (list.size == 0)
                return
            this.setImageLoder(R.id.iv_cover, data.bookInfoBean.coverUrl, context, R.drawable.img_cover_default)
                    .setText(R.id.tv_name, String.format(context.resources.getString(R.string.tv_book_name), data.bookInfoBean.name))
                    .setText(R.id.tv_durprogress, String.format(context.resources.getString(R.string.tv_read_durprogress)
                            , data.bookInfoBean.chapterlist[data.durChapter].durChapterName))
                    .setOnClickListener(R.id.tv_watch, {
                        if (mOnItemListener != null) {
                            mOnItemListener.toSearch()
                        }
                    })
        }
    }

    /**
     * 所有书籍列表
     */
    internal inner class OtherVideoHolder(itemView: View) : IViewHolder<BookShelfBean>(itemView) {

        override fun setData(data: BookShelfBean?) {
            super.setData(data)
            val book1 = list[position * 3]
            if (position * 3 + 1 < list.size) {
                val book2 = list[position * 3 + 1]
                this.setVisible(R.id.fl_content_2, true)
                        .setImageLoder(R.id.fl_content_2, book2.bookInfoBean.coverUrl, context, R.drawable.img_cover_default)
                        .setText(R.id.tv_name_2, book2.bookInfoBean.name)
                        .setOnClickListener(R.id.fl_content_2, {
                            if (mOnItemListener != null) {
                                mOnItemListener.onClick(book2, position * 3 + 1)
                            }
                        })
                        .setOnLongClickListener(R.id.fl_content_2, {
                            if (mOnItemListener != null) {
                                mOnItemListener.onLongClick(getView(R.id.fl_content_2), book2, position * 3 + 1)
                            }
                            return@setOnLongClickListener true
                        })
            }
            if (position * 3 + 2 < list.size) {
                val book3 = list[position * 3 + 2]
                this.setVisible(R.id.fl_content_2, true)
                        .setImageLoder(R.id.fl_content_3, book3.bookInfoBean.coverUrl, context, R.drawable.img_cover_default)
                        .setText(R.id.tv_name_3, book3.bookInfoBean.name)
                        .setOnClickListener(R.id.fl_content_3, {
                            if (mOnItemListener != null) {
                                mOnItemListener.onClick(book1, position * 3 + 2)
                            }
                        })
                        .setOnLongClickListener(R.id.fl_content_3, {
                            if (mOnItemListener != null) {
                                mOnItemListener.onLongClick(getView(R.id.fl_content_3), book3, position * 3 + 2)
                            }
                            return@setOnLongClickListener true
                        })
            }
            this.setImageLoder(R.id.fl_content_1, book1.bookInfoBean.coverUrl, context, R.drawable.img_cover_default)
                    .setText(R.id.tv_name_1, book1.bookInfoBean.name)
                    .setOnClickListener(R.id.fl_content_1, {
                        if (mOnItemListener != null) {
                            mOnItemListener.onClick(book1, position * 3)
                        }
                    })
                    .setOnLongClickListener(R.id.fl_content_1, {
                        if (mOnItemListener != null) {
                            mOnItemListener.onLongClick(getView(R.id.fl_content_1), book1, position * 3)
                        }
                        return@setOnLongClickListener true
                    })
        }
    }


    fun setItemListenter(listener: OnItemClickListener) {
        this.mOnItemListener = listener
    }
}