package test.phantom.com.p90.ui.main

import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import test.phantom.com.p90.R
import test.phantom.com.p90.base.BaseActivityt
import test.phantom.com.p90.base.KBaseActivity
import test.phantom.com.p90.entity.BookShelfBean
import test.phantom.com.p90.injector.component.DaggerMainComponent
import test.phantom.com.p90.injector.module.MainModule
import test.phantom.com.p90.ui.main.adapter.MainInfoAdapter


class MainActivity : KBaseActivity<MainPersenter>(), OnItemClickListener {
    override fun firstRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var exitTime: Long = 0
    var mDatas: List<BookShelfBean> = ArrayList()

    lateinit var mAdapter: MainInfoAdapter


    override fun getLayoutId(): Int = R.layout.activity_main

    override fun init() {

        mAdapter = MainInfoAdapter(this, mDatas)
        rf_rv_shelf.iAdapter = mAdapter
        rf_rv_shelf.layoutManager = LinearLayoutManager(this)
        mAdapter.setItemListenter(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(applicationContext, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
            System.exit(0)
        }
    }

    fun setData(data: List<BookShelfBean>) {
        mAdapter.replaceAll(data)
    }

    override fun initInjector() {
        DaggerMainComponent.builder()
                .mainModule(MainModule(this))
                .build()
                .inject(this)
    }

    override fun toSearch() {
        //点击去选书
//        startActivityByAnim(Intent(this@MainActivity, LibraryActivity::class.java), 0, 0)
    }

    override fun onClick(bookShelfBean: BookShelfBean, index: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLongClick(view: View, bookShelfBean: BookShelfBean, index: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun startAction(activity: BaseActivityt) {
            activity.startActivity<MainActivity>()
        }
    }

}
