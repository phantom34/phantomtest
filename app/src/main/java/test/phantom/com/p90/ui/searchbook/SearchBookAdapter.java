//Copyright (c) 2017. 章钦豪. All rights reserved.
package test.phantom.com.p90.ui.searchbook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import test.phantom.com.p90.R;
import test.phantom.com.p90.base.BaseRecyclerAdapter;
import test.phantom.com.p90.base.IViewHolder;
import test.phantom.com.p90.entity.SearchBookBean;

/**
 * @author Administrator
 */
public class SearchBookAdapter extends BaseRecyclerAdapter {

    private List<SearchBookBean> searchBooks;

    public interface OnItemClickListener {
        void clickAddShelf(View clickView, SearchBookBean searchBookBean);

        void clickItem(View animView, SearchBookBean searchBookBean);
    }

    private OnItemClickListener itemClickListener;

    public SearchBookAdapter(Context context, List<SearchBookBean> list) {
        super(context, list);
        searchBooks = new ArrayList<>();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void addAll(List<SearchBookBean> newDatas) {
        if (newDatas != null && newDatas.size() > 0) {
            int oldCount = getItemCount();
            searchBooks.addAll(newDatas);
            notifyItemRangeInserted(oldCount, newDatas.size());
        }
    }

    @Override
    public IViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        IViewHolder holder = super.onCreateViewHolder(parent, viewType);
        if (holder != null) {
            return holder;
        }
        View view = mLayoutInflater.inflate(R.layout.adapter_searchbook_item, parent, false);

        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public class ViewHolder extends IViewHolder<SearchBookBean> {

        FrameLayout flContent;
        ImageView ivCover;
        TextView tvName;
        TextView tvAuthor;
        TextView tvState;
        TextView tvWords;
        TextView tvKind;
        TextView tvLastest;
        TextView tvAddShelf;
        TextView tvOrigin;


        public ViewHolder(View itemView) {
            super(itemView);
            flContent = (FrameLayout) itemView.findViewById(R.id.fl_content);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            tvWords = (TextView) itemView.findViewById(R.id.tv_words);
            tvLastest = (TextView) itemView.findViewById(R.id.tv_lastest);
            tvAddShelf = (TextView) itemView.findViewById(R.id.tv_addshelf);
            tvKind = (TextView) itemView.findViewById(R.id.tv_kind);
            tvOrigin = (TextView) itemView.findViewById(R.id.tv_origin);
        }

        @Override
        public void setData(final SearchBookBean data) {
            super.setData(data);

            Glide.with(ivCover.getContext())
                    .load(data.getCoverUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.img_cover_default)
                    .into(ivCover);
            tvName.setText(data.getName());
            tvAuthor.setText(data.getAuthor());
            String state = data.getState();
            if (state == null || state.length() == 0) {
                tvState.setVisibility(View.GONE);
            } else {
                tvState.setVisibility(View.VISIBLE);
                tvState.setText(state);
            }
            long words = data.getWords();
            if (words <= 0) {
                tvWords.setVisibility(View.GONE);
            } else {
                String wordsS = Long.toString(words) + "字";
                if (words > 10000) {
                    DecimalFormat df = new DecimalFormat("#.#");
                    wordsS = df.format(words * 1.0f / 10000f) + "万字";
                }
                tvWords.setVisibility(View.VISIBLE);
                tvWords.setText(wordsS);
            }
            String kind = data.getKind();
            if (kind == null || kind.length() <= 0) {
                tvKind.setVisibility(View.GONE);
            } else {
                tvKind.setVisibility(View.VISIBLE);
                tvKind.setText(kind);
            }
            if (data.getLastChapter() != null && data.getLastChapter().length() > 0) {
                tvLastest.setText(data.getLastChapter());
            } else if (data.getDesc() != null && data.getDesc().length() > 0) {
                tvLastest.setText(data.getDesc());
            } else {
                tvLastest.setText("");
            }
            if (data.getOrigin() != null && data.getOrigin().length() > 0) {
                tvOrigin.setVisibility(View.VISIBLE);
                tvOrigin.setText("来源:" + data.getOrigin());
            } else {
                tvOrigin.setVisibility(View.GONE);
            }
            if (data.getAdd()) {
                tvAddShelf.setText("已添加");
                tvAddShelf.setEnabled(false);
            } else {
                tvAddShelf.setText("+添加");
                tvAddShelf.setEnabled(true);
            }

            flContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.clickItem(ivCover, data);
                    }
                }
            });
            tvAddShelf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.clickAddShelf(tvAddShelf, data);
                    }
                }
            });
        }
    }

}