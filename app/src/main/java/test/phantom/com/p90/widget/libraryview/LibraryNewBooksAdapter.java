package test.phantom.com.p90.widget.libraryview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import test.phantom.com.p90.R;
import test.phantom.com.p90.entity.LibraryNewBookBean;
import test.phantom.com.p90.widget.flowlayout.FlowLayout;
import test.phantom.com.p90.widget.flowlayout.TagAdapter;


public class LibraryNewBooksAdapter extends TagAdapter<LibraryNewBookBean> {
    private LibraryNewBooksView.OnClickAuthorListener clickNewBookListener;

    public LibraryNewBooksAdapter() {
        super(new ArrayList<LibraryNewBookBean>());
    }

    @Override
    public View getView(FlowLayout parent, int position, final LibraryNewBookBean libraryNewBookBean) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_library_hotauthor_item,
                parent, false);
        tv.setText(libraryNewBookBean.getName());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != clickNewBookListener){
                    clickNewBookListener.clickNewBook(libraryNewBookBean);
                }
            }
        });
        return tv;
    }

    public LibraryNewBookBean getItemData(int position){
        return mTagDatas.get(position);
    }

    public int getDataSize(){
        return mTagDatas.size();
    }

    public void setClickNewBookListener(LibraryNewBooksView.OnClickAuthorListener clickNewBookListener) {
        this.clickNewBookListener = clickNewBookListener;
    }
}
