package test.phantom.com.p90.until;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;

import java.io.File;

/**
 * Description : 图片加载工具类 使用glide框架封装
 * <p>
 * throw new IllegalArgumentException("argument error");
 * 抛异常导致崩溃，改成 return
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder) {
        if (imageView == null) {
            return;
        }

        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext())
                    .load(url)
                    .placeholder(placeholder)
                    .error(placeholder)
                    .crossFade()
                    .into(imageView);
        }
    }

    public static void display(Context context, ImageView imageView, String url, int placeholder, int with, int
            height) {
        if (imageView == null) {
            return;
        }

        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(placeholder)
                    .error(placeholder)
                    .crossFade()
                    .override(with, height)
                    .into(imageView);
        }
    }

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            return;
        }

        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .placeholder(placeholder)
                    .error(error)
                    .crossFade()
                    .into(imageView);
        }
    }

    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }

        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
        }
    }

    public static void display(Context context, ImageView imageView, String url, RequestListener listener) {
        if (imageView == null) {
            return;
        }

        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .listener(listener)
                    .into(imageView);
        }
    }

    public static void display(Context context, ImageView imageView, @DrawableRes int url, RequestListener listener) {
        if (imageView == null) {
            return;
        }

        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .listener(listener)
                    .into(imageView);
        }
    }

    public static void displayByCompleteUrl(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }

        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
        }
    }


    public static void displayBlurAndRound(Context context, ImageView imageView, String url, int radius) {
        if (imageView == null) {
            return;
        }

        if (Util.isOnMainThread()) {
            Glide.with(context).load(url)

                    .into(imageView);
        }
    }

    public static void display(Context context, ImageView imageView, String url, Drawable placeholder) {
        if (imageView == null) {
            return;
        }
        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(placeholder)
                    .crossFade()
                    .into(imageView);
        }
    }

    public static void displayVideoBackgroud(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }
        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .error(new ColorDrawable(0x000000))
                    .placeholder(new ColorDrawable(0x000000))
                    .into(imageView);
        }
    }

    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }
        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext()).load(url).asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        }
    }


    public static void displayBigPhoto(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            return;
        }
        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext()).load(url).asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }

    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            return;
        }
        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext()).load(url)
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .centerCrop()
//                .placeholder(R.drawable.ic_image_loading)
//                .error(R.drawable.ic_empty_picture)
                    .into(imageView);
        }
    }

    public static void displayGif(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            return;
        }
        if (Util.isOnMainThread()) {
            Glide.with(context.getApplicationContext())
                    .load(url)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }
    }

    public static void getBitMap(Context context, final ImageView imageView, String url) {

        Bitmap myBitmap = null;
//        }
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                    }
                });
    }

    public static void fitImage(Activity activity, ImageView imageView, float picWidth, float picHeight) {
        WindowManager wm = activity.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        float height = (float) width / picWidth * picHeight;
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = (int) height;
        imageView.setLayoutParams(layoutParams);
    }
}
