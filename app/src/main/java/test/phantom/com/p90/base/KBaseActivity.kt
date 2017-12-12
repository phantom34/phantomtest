package test.phantom.com.p90.base

import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import test.phantom.com.p90.injector.ActivityModule
import test.phantom.com.p90.injector.ApplicationComponent
import test.phantom.com.p90.injector.BaseApplication


abstract class KBaseActivity<T : BasePresenter> : CommonActivity() {

//    @Inject
    public lateinit var presenter: T

    private var disposables2Stop: CompositeDisposable? = null //管理stop取消订阅者
    private var disposables2Destroy: CompositeDisposable? = null//管理Destroy取消订阅者

    /**
     * 获取 ApplicationComponent
     *
     * @return ApplicationComponent
     */
    protected val appComponent: ApplicationComponent
        get() = BaseApplication.getAppComponent()

    /**
     * 获取 ActivityModule
     *
     * @return ActivityModule
     */
    protected val activityModule: ActivityModule
        get() = ActivityModule(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (disposables2Destroy != null) {
            throw IllegalStateException("onCreate called multiple times")
        }
        disposables2Destroy = CompositeDisposable()

        setContentView(getLayoutId())
        mContext = this
        //init()中只进行初始化动作
        init()

        initInjector()
        TAG = javaClass.simpleName
    }

    fun addRxStop(disposable: Disposable): Boolean {
        if (disposables2Stop == null) {
            throw IllegalStateException(
                    "addUtilStop should be called between onStart and onStop")
        }
        disposables2Stop!!.add(disposable)
        return true
    }

    fun addRxDestroy(disposable: Disposable): Boolean {
        if (disposables2Destroy == null) {
            throw IllegalStateException(
                    "addUtilDestroy should be called between onCreate and onDestroy")
        }
        disposables2Destroy!!.add(disposable)
        return true
    }

    fun remove(disposable: Disposable) {
        if (disposables2Stop == null && disposables2Destroy == null) {
            throw IllegalStateException("remove should not be called after onDestroy")
        }
        if (disposables2Stop != null) {
            disposables2Stop!!.remove(disposable)
        }
        if (disposables2Destroy != null) {
            disposables2Destroy!!.remove(disposable)
        }
    }


    override fun onStart() {
        super.onStart()
        if (disposables2Stop != null) {
            throw IllegalStateException("onStart called multiple times")
        }
        disposables2Stop = CompositeDisposable()
    }

    override fun onStop() {
        super.onStop()
        if (disposables2Stop == null) {
            throw IllegalStateException("onStop called multiple times or onStart not called")
        }
        disposables2Stop!!.dispose()
        disposables2Stop = null
    }

    /*********************
     * 子类实现
     */
    //获取布局文件
    protected abstract fun getLayoutId(): Int

    //初始化view
    protected abstract fun init()

    //Dagger 注入
    protected abstract fun initInjector()

    override fun onDestroy() {
        super.onDestroy()
        if (disposables2Destroy == null) {
            throw IllegalStateException(
                    "onDestroy called multiple times or onCreate not called")
        }
        disposables2Destroy!!.dispose()
        disposables2Destroy = null
    }
}
