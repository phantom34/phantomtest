package test.phantom.com.p90.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.annotation.Nullable
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import test.phantom.com.p90.R
import test.phantom.com.p90.base.SimpleObserver
import test.phantom.com.p90.dao.BookShelfBeanDao
import test.phantom.com.p90.dao.DbHelper
import test.phantom.com.p90.dao.DownloadChapterBeanDao
import test.phantom.com.p90.entity.BookShelfBean
import test.phantom.com.p90.entity.DownloadChapterBean
import test.phantom.com.p90.entity.DownloadChapterListBean
import test.phantom.com.p90.entity.RxBusTag
import test.phantom.com.p90.ui.main.MainActivity


/**
 * Created by phantom on 2017/12/14.
 */
class DownloadService : Service() {

    private lateinit var notifyManager: NotificationManager
    private var isInit = false
    private val notifiId = 19931118
    private var isStartDownload = false

    private var isDownloading = false
    val reTryTimes = 1

    @Nullable
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        RxBus.get().unregister(this)
        isInit = true
    }


    /**
     * 开启Service 判断有没有初始化过
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isInit) {
            isInit = true
            notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 添加 新的章节
     */
    private fun addNewTask(newData: List<DownloadChapterBean>) {
        isStartDownload = true
        Observable.create(ObservableOnSubscribe<Boolean> { e ->
            DbHelper.getInstance().getmDaoSession()
                    .downloadChapterBeanDao.insertOrReplaceInTx(newData)
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SimpleObserver<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        if (!isStartDownload) {
                            toDownLoad()
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
    }

    private fun toDownLoad() {
        isDownloading = true
        if (isStartDownload) {
            Observable.create(ObservableOnSubscribe<DownloadChapterBean> { e ->
                var bookshlflist = DbHelper.getInstance().getmDaoSession().bookShelfBeanDao
                        .queryBuilder().orderDesc(BookShelfBeanDao.Properties.FinalDate)
                        .list()
                if (bookshlflist != null && bookshlflist.size > 0) {
                    bookshlflist.forEach {
                        if (!it.tag.equals(BookShelfBean.LOCAL_TAG)) {
                            var downloadChapterList = DbHelper.getInstance().getmDaoSession()
                                    .downloadChapterBeanDao.queryBuilder()
                                    .where(DownloadChapterBeanDao.Properties.NoteUrl.eq(it.noteUrl))
                                    .orderAsc(DownloadChapterBeanDao.Properties.DurChapterIndex)
                                    .limit(1).list()
                            if (downloadChapterList != null && downloadChapterList.size > 0) {
                                e.onNext(downloadChapterList[0])
                                e.onComplete()
                                return@ObservableOnSubscribe
                            }
                        }
                    }
                    DbHelper.getInstance().getmDaoSession().downloadChapterBeanDao.deleteAll()
                    e.onNext(DownloadChapterBean())
                } else {
                    DbHelper.getInstance().getmDaoSession().downloadChapterBeanDao.deleteAll()
                    e.onNext(DownloadChapterBean())
                }
            })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .subscribe(object : SimpleObserver<DownloadChapterBean>() {
                        override fun onNext(t: DownloadChapterBean) {
                            if (t.noteUrl != null && t.noteUrl.isNotEmpty()) {
                                downLoading()
                            } else {
                                DbHelper.getInstance().getmDaoSession().downloadChapterBeanDao.deleteAll()
                                isDownloading = false
                                finishDownload()
                            }
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            isDownloading = false
                        }
                    })
        } else {
            isPause()
        }
    }

    private fun isPause() {
        isDownloading = false
        Observable.create(ObservableOnSubscribe<DownloadChapterBean> { e ->
            var bookShelfBeanList = DbHelper.getInstance().getmDaoSession().bookShelfBeanDao.queryBuilder()
                    .orderDesc(BookShelfBeanDao.Properties.FinalDate).list()
            if (bookShelfBeanList != null && bookShelfBeanList.size > 0) {
                bookShelfBeanList.forEach {
                    if (it.tag != BookShelfBean.LOCAL_TAG) {
                        var downloadChapterList = DbHelper.getInstance().getmDaoSession().downloadChapterBeanDao
                                .queryBuilder().where(DownloadChapterBeanDao.Properties.NoteUrl.eq(it.noteUrl))
                                .orderAsc(DownloadChapterBeanDao.Properties.DurChapterIndex).limit(1).list()
                        if (downloadChapterList != null && downloadChapterList.size > 0) {
                            e.onNext(downloadChapterList[0])
                            e.onComplete()
                            return@ObservableOnSubscribe
                        }
                    }
                }
                DbHelper.getInstance().getmDaoSession().downloadChapterBeanDao.deleteAll()
                e.onNext(DownloadChapterBean())
            } else {
                DbHelper.getInstance().getmDaoSession().downloadChapterBeanDao.deleteAll()
                e.onNext(DownloadChapterBean())
            }
        })
    }

    private fun downLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun startDownload() {
        isStartDownload = true
        toDownLoad()
    }

    private fun isProgress(downloadChapterBean: DownloadChapterBean) {
        RxBus.get().post(RxBusTag.PROGRESS_DOWNLOAD_LISTENER, downloadChapterBean)

        val mainIntent = Intent(this, MainActivity::class.java)
        val mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //创建 Notification.Builder 对象
        val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentTitle("正在下载：" + downloadChapterBean.bookName)
                .setContentText(if (downloadChapterBean.durChapterName == null) "  " else downloadChapterBean.durChapterName)
                .setContentIntent(mainPendingIntent)
        //发送通知
        notifyManager.notify(notifiId, builder.build())
    }

    private fun finishDownload() {
        RxBus.get().post(RxBusTag.FINISH_DOWNLOAD_LISTENER, Any())
        notifyManager.cancelAll()
        Handler(Looper.getMainLooper()).post(Runnable { Toast.makeText(applicationContext, "全部离线章节下载完成", Toast.LENGTH_SHORT).show() })
    }

    fun pauseDownload() {
        isStartDownload = false
        notifyManager.cancelAll()
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = arrayOf(Tag(RxBusTag.PAUSE_DOWNLOAD))
    )
    fun pauseTask(o: Object) {
        pauseDownload()
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = arrayOf(Tag(RxBusTag.START_DOWNLOAD))
    )
    fun startTask(o: Object) {
        startDownload()
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = arrayOf(Tag(RxBusTag.CANCEL_DOWNLOAD))
    )
    fun cancelTask(o: Object) {
        cancelDownload()
    }

    private fun cancelDownload() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = arrayOf(Tag(RxBusTag.ADD_DOWNLOAD_TASK))
    )
    fun addTask(newData: DownloadChapterListBean) {
        addNewTask(newData.data)
    }

}