package test.phantom.com.p90.ui

import android.net.Uri
import kotlinx.android.synthetic.main.act_welcome.*
import test.phantom.com.p90.R
import test.phantom.com.p90.base.BaseActivityt

/**
 * Created by phantom on 2017/12/7.
 */
class WelcomeActivity : BaseActivityt() {
    override fun getLayoutId(): Int = R.layout.act_welcome

    override fun init() {
        cv_welcome.setVideoURI(Uri.parse("android.resource://${this.packageName}/${R.raw.kr36}"))
        cv_welcome.start()
        cv_welcome.setOnCompletionListener({ cv_welcome.start() })
        btn_next.setOnClickListener({
            cv_welcome.pause()
            MainActivity.startAction(this)
        })
    }
}