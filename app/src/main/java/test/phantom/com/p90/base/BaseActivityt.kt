package test.phantom.com.p90.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import test.phantom.com.p90.R

/**
 * Created by phantom on 2017/12/7.
 */
abstract class BaseActivityt : AppCompatActivity() {
    /**
     * 获取状态栏的高度
     * @return
     */
    protected val statusBarHeight: Int
        get() {
            try {
                val c = Class.forName("com.android.internal.R\$dimen")
                val obj = c.newInstance()
                val field = c.getField("status_bar_height")
                val x = Integer.parseInt(field.get(obj).toString())
                return resources.getDimensionPixelSize(x)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return 0
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }

        setContentView(getLayoutId())
        init()


    }

    /*********************
     * 子类实现
     */
    //获取布局文件
    protected abstract fun getLayoutId(): Int

    //初始化view
    protected abstract fun init()

    protected fun openActivity(pClass: Class<*>) {
        val mIntent = Intent(this, pClass)
        this.startActivity(mIntent)
    }

    /**
     * 设置沉浸式状态栏
     */
//    protected fun setStatusBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val linear_bar = findViewById(R.id.bar_layout) as ViewGroup
//            val statusHeight = statusBarHeight
//            linear_bar.post {
//                val titleHeight = linear_bar.height
//                val params = linear_bar.layoutParams as android.widget.LinearLayout.LayoutParams
//                params.height = statusHeight + titleHeight
//                linear_bar.layoutParams = params
//            }
//        }
//    }


    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected fun SetStatusBarColor() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary))
    }

}