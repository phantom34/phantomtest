package test.phantom.com.p90.until;

import android.content.Context;
import android.widget.Toast;

import test.phantom.com.p90.base.CommonApplication;


/**
 * Toast 工具类
 * 解决Toast连续出现要等前一个时间到
 * 再显示下一个的延迟现象
 *
 * @author DB
 */
public class ToastUtil {


    private static Toast mToast;
    private static Toast mImgToast;

    /**
     * @param message
     * @param duration
     * @return
     */
    private static Toast initToast(CharSequence message, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(CommonApplication.getAppContext(), message, duration);
        } else {
            mToast.setText(message);
            mToast.setDuration(duration);
        }
        return mToast;
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        initToast(message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 短时间显示Toast
     *
     * @param strResId
     */
    public static void showShort(int strResId) {
//		Toast.makeText(context, strResId, Toast.LENGTH_SHORT).show();
        initToast(CommonApplication.getAppContext().getResources().getText(strResId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        initToast(message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param strResId
     */
    public static void showLong(int strResId) {
        initToast(CommonApplication.getAppContext().getResources().getText(strResId), Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        initToast(message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param strResId
     * @param duration
     */
    public static void show(Context context, int strResId, int duration) {
        initToast(context.getResources().getText(strResId), duration).show();
    }


}
