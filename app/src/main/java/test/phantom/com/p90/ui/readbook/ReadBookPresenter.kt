package test.phantom.com.p90.ui.readbook

import test.phantom.com.p90.base.BasePresenter
import javax.inject.Inject

/**
 * Created by phantom on 2017/12/19.
 */
class ReadBookPresenter : BasePresenter {


    val OPEN_FROM_OTHER = 0
    val OPEN_FROM_APP = 1


    private var mView: ReadBookActivity? = null

    @Inject
    fun ReadBookPresenter(activity: ReadBookActivity) {
        this.mView = activity
    }

    fun initData(readBookActivity: ReadBookActivity) {

    }

    fun openBookFromOther(readBookActivity: ReadBookActivity) {}
    fun getOpen_from(): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}