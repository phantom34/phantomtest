//Copyright (c) 2017. 章钦豪. All rights reserved.
package test.phantom.com.p90.view.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import test.phantom.com.p90.R;
import test.phantom.com.p90.widget.ReadBookControl;

public class FontPop extends PopupWindow{
    private Context mContext;
    private View view;
    private FrameLayout flSmaller;
    private FrameLayout flBigger;
    private TextView tvTextSizedDefault;
    private TextView tvTextSize;
    private ImageView civBgWhite;
    private ImageView civBgYellow;
    private ImageView civBgGreen;
    private ImageView civBgBlack;

    private ReadBookControl readBookControl;

    public interface OnChangeProListener{
        public void textChange(int index);

        public void bgChange(int index);
    }
    private OnChangeProListener changeProListener;

    public FontPop(Context context,@NonNull OnChangeProListener changeProListener){
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mContext = context;
        this.changeProListener = changeProListener;

        view = LayoutInflater.from(mContext).inflate(R.layout.view_pop_font, null);
        this.setContentView(view);
        initData();
        bindView();
        bindEvent();

        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_pop_checkaddshelf_bg));
        setFocusable(true);
        setTouchable(true);
        setAnimationStyle(R.style.anim_pop_windowlight);
    }

    private void bindEvent() {
        flSmaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText(readBookControl.getTextKindIndex()-1);
                changeProListener.textChange(readBookControl.getTextKindIndex());
            }
        });
        flBigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText(readBookControl.getTextKindIndex()+1);
                changeProListener.textChange(readBookControl.getTextKindIndex());
            }
        });
        tvTextSizedDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText(ReadBookControl.DEFAULT_TEXT);
                changeProListener.textChange(readBookControl.getTextKindIndex());
            }
        });

        civBgWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBg(0);
                changeProListener.bgChange(readBookControl.getTextDrawableIndex());
            }
        });
        civBgYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBg(1);
                changeProListener.bgChange(readBookControl.getTextDrawableIndex());
            }
        });
        civBgGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBg(2);
                changeProListener.bgChange(readBookControl.getTextDrawableIndex());
            }
        });
        civBgBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBg(3);
                changeProListener.bgChange(readBookControl.getTextDrawableIndex());
            }
        });
    }

    private void bindView() {
        flSmaller = (FrameLayout) view.findViewById(R.id.fl_smaller);
        flBigger = (FrameLayout) view.findViewById(R.id.fl_bigger);
        tvTextSizedDefault = (TextView) view.findViewById(R.id.tv_textsize_default);
        tvTextSize = (TextView) view.findViewById(R.id.tv_dur_textsize);
        updateText(readBookControl.getTextKindIndex());

        civBgWhite = (ImageView) view.findViewById(R.id.civ_bg_white);
        civBgYellow = (ImageView) view.findViewById(R.id.civ_bg_yellow);
        civBgGreen = (ImageView) view.findViewById(R.id.civ_bg_green);
        civBgBlack = (ImageView) view.findViewById(R.id.civ_bg_black);
        updateBg(readBookControl.getTextDrawableIndex());
    }

    private void updateText(int textKindIndex) {
        if(textKindIndex==0){
            flSmaller.setEnabled(false);
            flBigger.setEnabled(true);
        }else if(textKindIndex == readBookControl.getTextKind().size()-1){
            flSmaller.setEnabled(true);
            flBigger.setEnabled(false);
        }else{flSmaller.setEnabled(true);
            flBigger.setEnabled(true);

        }
        if(textKindIndex == ReadBookControl.DEFAULT_TEXT){
            tvTextSizedDefault.setEnabled(false);
        }else{
            tvTextSizedDefault.setEnabled(true);
        }
        tvTextSize.setText(String.valueOf(readBookControl.getTextKind().get(textKindIndex).get("textSize")));
        readBookControl.setTextKindIndex(textKindIndex);
    }

    private void updateBg(int index) {
        civBgWhite.setColorFilter(Color.parseColor("#00000000"));
        civBgYellow.setColorFilter(Color.parseColor("#00000000"));
        civBgGreen.setColorFilter(Color.parseColor("#00000000"));
        civBgBlack.setColorFilter(Color.parseColor("#00000000"));
        switch (index){
            case 0:
                civBgWhite.setColorFilter(Color.parseColor("#F3B63F"));
                break;
            case 1:
                civBgYellow.setColorFilter(Color.parseColor("#F3B63F"));
                break;
            case 2:
                civBgGreen.setColorFilter(Color.parseColor("#F3B63F"));
                break;
            default:
                civBgBlack.setColorFilter(Color.parseColor("#F3B63F"));
                break;
        }
        readBookControl.setTextDrawableIndex(index);
    }

    private void initData() {
        readBookControl = ReadBookControl.getInstance();
    }
}