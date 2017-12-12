package test.phantom.com.p90.ui.main

import android.view.KeyEvent
import android.widget.Toast
import org.jetbrains.anko.startActivity
import test.phantom.com.p90.R
import test.phantom.com.p90.base.BaseActivity
import test.phantom.com.p90.base.BaseActivityt
import test.phantom.com.p90.entity.BookShelfBean
import test.phantom.com.p90.ui.main.adapter.MainInfoAdapter


class MainActivity : BaseActivity() {

    companion object {
        fun startAction(activity: BaseActivityt) {
            activity.startActivity<MainActivity>()
        }
    }

    private var exitTime: Long = 0
    var mDatas: List<BookShelfBean> = ArrayList()

    lateinit var mAdapter: MainInfoAdapter


    override fun getLayoutId(): Int = R.layout.activity_main

    override fun init() {

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
}
