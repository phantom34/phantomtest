package test.phantom.com.p90.ui.bookdetail

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.act_book_detail.*
import test.phantom.com.p90.R
import test.phantom.com.p90.base.KBaseActivity


/**
 * Created by phantom on 2017/12/14.
 */
class BookDetailActivity : KBaseActivity<BookDetailPersenter>() {

    override fun firstRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private lateinit var animHideLoading: Animation
    private lateinit var animShowInfo: Animation

    override fun getLayoutId(): Int = R.layout.act_book_detail

    override fun init() {
        animShowInfo = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        animHideLoading = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        animHideLoading.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                tv_loading.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}