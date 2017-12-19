package test.phantom.com.p90.ui.readbook

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.act_readbook.*
import test.phantom.com.p90.R
import test.phantom.com.p90.base.KBaseActivity
import test.phantom.com.p90.until.PremissionCheck
import test.phantom.com.p90.view.popupwindow.CheckAddShelfPop
import test.phantom.com.p90.view.popupwindow.FontPop
import test.phantom.com.p90.view.popupwindow.MoreSettingPop
import test.phantom.com.p90.view.popupwindow.ReadBookMenuMorePop
import test.phantom.com.p90.widget.ChapterListView
import test.phantom.com.p90.widget.MoProgressHUD


/**
 * Created by phantom on 2017/12/18.
 */
class ReadBookActivity : KBaseActivity<ReadBookPresenter>() {

    //主菜单
    private var flMenu: FrameLayout? = null
    private var vMenuBg: View? = null
    //主菜单动画
    private var menuTopIn: Animation? = null
    private var menuTopOut: Animation? = null
    private var menuBottomIn: Animation? = null
    private var menuBottomOut: Animation? = null

    private var checkAddShelfPop: CheckAddShelfPop? = null
    private var chapterListView: ChapterListView? = null
    private var readBookMenuMorePop: ReadBookMenuMorePop? = null
    private var fontPop: FontPop? = null
    private var moreSettingPop: MoreSettingPop? = null

    private var moProgressHUD: MoProgressHUD? = null
    private var showCheckPremission: Boolean = false


    override fun getLayoutId(): Int = R.layout.act_readbook

    override fun init() {
        moProgressHUD = MoProgressHUD(this)

        menuTopIn = AnimationUtils.loadAnimation(this, R.anim.anim_readbook_top_in)
        menuTopIn!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                vMenuBg!!.setOnClickListener({
                    ll_menu_top.startAnimation(menuTopOut)
                    ll_menu_bottom.startAnimation(menuBottomOut)
                })
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        menuBottomIn = AnimationUtils.loadAnimation(this, R.anim.anim_readbook_bottom_in)

        menuTopOut = AnimationUtils.loadAnimation(this, R.anim.anim_readbook_top_out)
        menuTopOut!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                vMenuBg!!.setOnClickListener(null)
            }

            override fun onAnimationEnd(animation: Animation) {
                flMenu!!.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        menuBottomOut = AnimationUtils.loadAnimation(this, R.anim.anim_readbook_bottom_out)
    }

    override fun initInjector() {
    }

    private fun initCsvBook() {
        csv_book.bookReadInit { presenter.initData(this@ReadBookActivity) }
    }

    @SuppressLint("NewApi")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0x11) {
            if (grantResults != null && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && PremissionCheck.checkPremission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                presenter.openBookFromOther(this@ReadBookActivity)
            } else {
                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showCheckPremission = true
                    moProgressHUD!!.showTwoButton("去系统设置打开SD卡读写权限？", "取消", View.OnClickListener { finish() }, "设置", View.OnClickListener { PremissionCheck.requestPermissionSetting(this@ReadBookActivity) })
                } else {
                    Toast.makeText(this, "未获取SD卡读取权限", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    protected override fun onResume() {
        super.onResume()
        if (showCheckPremission && presenter.getOpen_from() === presenter.OPEN_FROM_OTHER && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !PremissionCheck.checkPremission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            showCheckPremission = true
            presenter.openBookFromOther(this)
        }
    }

    override fun finish() {
//        if (!AppManager.getAppManager().(this, false)) {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
        super.finish()
    }


}