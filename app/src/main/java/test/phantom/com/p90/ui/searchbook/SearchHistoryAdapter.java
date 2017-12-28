//Copyright (c) 2017. 章钦豪. All rights reserved.
package test.phantom.com.p90.ui.searchbook;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import test.phantom.com.p90.R;
import test.phantom.com.p90.entity.SearchHistoryBean;
import test.phantom.com.p90.widget.flowlayout.FlowLayout;
import test.phantom.com.p90.widget.flowlayout.TagAdapter;

public class SearchHistoryAdapter extends TagAdapter<SearchHistoryBean> {
    public SearchHistoryAdapter() {
        super(new ArrayList<SearchHistoryBean>());
    }

    public interface OnItemClickListener{
        void itemClick(SearchHistoryBean searchHistoryBean);
    }
    private SearchHistoryAdapter.OnItemClickListener onItemClickListener;

    public OnItemClickListener getListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public View getView(FlowLayout parent, int position, final SearchHistoryBean searchHistoryBean) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_searchhistory_item,
                parent, false);
        tv.setText(searchHistoryBean.getContent());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onItemClickListener){
                    onItemClickListener.itemClick(searchHistoryBean);
                }
            }
        });
        return tv;
    }

    public SearchHistoryBean getItemData(int position){
        return mTagDatas.get(position);
    }

    public int getDataSize(){
        return mTagDatas.size();
    }
}
