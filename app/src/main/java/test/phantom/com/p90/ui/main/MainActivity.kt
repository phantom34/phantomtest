package test.phantom.com.p90.ui.main

import android.content.Intent
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
import test.phantom.com.p90.ui.bookdetail.BookDetailActivity
import test.phantom.com.p90.ui.bookdetail.BookDetailPersenter
import test.phantom.com.p90.ui.importbook.ImportBookActivity
import test.phantom.com.p90.ui.library.LibraryActivity
import test.phantom.com.p90.ui.main.adapter.MainInfoAdapter
import test.phantom.com.p90.ui.readbook.ReadBookActivity
import test.phantom.com.p90.ui.readbook.ReadBookPresenter
import test.phantom.com.p90.until.BitIntentDataManager


class MainActivity : KBaseActivity<MainPresenter>(), OnItemClickListener {


    override fun firstRequest() {
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
        ib_library.setOnClickListener({ startActivityByAnim(Intent(this@MainActivity, LibraryActivity::class.java), 0, 0) })
        ib_add.setOnClickListener({ startActivityByAnim(Intent(this@MainActivity, ImportBookActivity::class.java), 0, 0) })
        mPresenter.queryBookShelf(false)
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
        startActivityByAnim(Intent(this@MainActivity, LibraryActivity::class.java), 0, 0)
    }

    override fun onClick(bookShelfBean: BookShelfBean, index: Int) {
        val intent = Intent(this@MainActivity, ReadBookActivity::class.java)
        intent.putExtra("from", ReadBookPresenter.OPEN_FROM_APP)
        val key = System.currentTimeMillis().toString()
        intent.putExtra("data_key", key)
        try {
            BitIntentDataManager.getInstance().putData(key, bookShelfBean.clone())
        } catch (e: CloneNotSupportedException) {
            BitIntentDataManager.getInstance().putData(key, bookShelfBean)
            e.printStackTrace()
        }

        startActivityByAnim(intent, android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onLongClick(view: View, bookShelfBean: BookShelfBean, index: Int) {
        val intent = Intent(this@MainActivity, BookDetailActivity::class.java)
        intent.putExtra("from", BookDetailPersenter.Companion.FROM_BOOKSHELF)
        val key = System.currentTimeMillis().toString()
        intent.putExtra("data_key", key)
        BitIntentDataManager.getInstance().putData(key, bookShelfBean)
        startActivityByAnim(intent, view, "img_cover", android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {
        fun startAction(activity: BaseActivityt) {
            activity.startActivity<MainActivity>()
        }
    }

}
