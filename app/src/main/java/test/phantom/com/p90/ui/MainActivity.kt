package test.phantom.com.p90.ui

import org.jetbrains.anko.startActivity
import test.phantom.com.p90.R
import test.phantom.com.p90.base.BaseActivity
import test.phantom.com.p90.base.BaseActivityt

class MainActivity : BaseActivity() {



    companion object {
        fun startAction(activity: BaseActivityt){
            activity.startActivity<MainActivity>()
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main


    override fun init() {

    }


}
