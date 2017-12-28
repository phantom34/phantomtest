package test.phantom.com.p90.ui.choice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import test.phantom.com.p90.R;
import test.phantom.com.p90.base.KBaseActivity;
import test.phantom.com.p90.entity.SearchBookBean;
import test.phantom.com.p90.ui.bookdetail.BookDetailActivity;
import test.phantom.com.p90.ui.bookdetail.BookDetailPersenter;
import test.phantom.com.p90.ui.searchbook.SearchBookAdapter;
import test.phantom.com.p90.until.NetworkUtil;
import test.phantom.com.p90.widget.irecyclerview.IRecyclerView;

/**
 * @author Administrator
 */
public class ChoiceBookActivity extends KBaseActivity<ChoiceBookPresenter> implements ChoiceBookContract.View {

    private ImageButton ivReturn;
    private TextView tvTitle;
    private IRecyclerView rfRvSearchBooks;
    private SearchBookAdapter searchBookAdapter;
    private List<SearchBookBean> listData = new ArrayList<>();

    public static void startChoiceBookActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, ChoiceBookActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }


    protected void initData() {

    }

    protected void bindView() {
        ivReturn = (ImageButton) findViewById(R.id.iv_return);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(mPresenter.getTitle());
        rfRvSearchBooks = (IRecyclerView) findViewById(R.id.rfRv_search_books);
        searchBookAdapter = new SearchBookAdapter(this, listData);
        rfRvSearchBooks.setAdapter(searchBookAdapter);

        rfRvSearchBooks.setLayoutManager(new LinearLayoutManager(this));

        View viewRefreshError = LayoutInflater.from(this).inflate(R.layout.view_searchbook_refresherror, null);
        viewRefreshError.findViewById(R.id.tv_refresh_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBookAdapter.replaceAll(null);
                //刷新失败 ，重试
                mPresenter.initPage();
                mPresenter.toSearchBooks(null);
                startRefreshAnim();
            }
        });
//        rfRvSearchBooks.setNoDataAndrRefreshErrorView(LayoutInflater.from(this).inflate(R.layout
//                        .view_searchbook_nodata, null),
//                viewRefreshError);
    }

    protected void bindEvent() {
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchBookAdapter.setItemClickListener(new SearchBookAdapter.OnItemClickListener() {
            @Override
            public void clickAddShelf(View clickView, SearchBookBean searchBookBean) {
                mPresenter.addBookToShelf(searchBookBean);

            }

            @Override
            public void clickItem(View animView, SearchBookBean searchBookBean) {
                Intent intent = new Intent(ChoiceBookActivity.this, BookDetailActivity.class);
                intent.putExtra("from", BookDetailPersenter.Companion.getFROM_SEARCH());
                intent.putExtra("data", searchBookBean);
                startActivityByAnim(intent, animView, "img_cover", android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

//        rfRvSearchBooks.setBaseRefreshListener(new BaseRefreshListener() {
//            @Override
//            public void startRefresh() {
//                mPresenter.initPage();
//                mPresenter.toSearchBooks(null);
//                startRefreshAnim();
//            }
//        });
//        rfRvSearchBooks.setLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void startLoadmore() {
//                mPresenter.toSearchBooks(null);
//            }
//
//            @Override
//            public void loadMoreErrorTryAgain() {
//                mPresenter.toSearchBooks(null);
//            }
//        });
    }

    @Override
    public void refreshSearchBook(List<SearchBookBean> books) {
        searchBookAdapter.replaceAll(books);
    }

    @Override
    public void loadMoreSearchBook(final List<SearchBookBean> books) {
        searchBookAdapter.addAll(books);
    }

    @Override
    public void searchBookError() {
        if (mPresenter.getPage() > 1) {
//            rfRvSearchBooks.loadMoreError();
        } else {
            //刷新失败
//            rfRvSearchBooks.refreshError();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void addBookShelfSuccess(List<SearchBookBean> datas) {
        searchBookAdapter.notifyDataSetChanged();
    }

    @Override
    public void addBookShelfFailed(int code) {
        Toast.makeText(this, NetworkUtil.getErrorTip(code), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateSearchItem(int index) {
        searchBookAdapter.notifyDataSetChanged();
    }

    @Override
    public void startRefreshAnim() {
        rfRvSearchBooks.setRefreshing(true);
    }

    @Override
    public String getUrl() {
        return getIntent().getStringExtra("url");
    }

    @Override
    public String getmTitle() {
        return getIntent().getStringExtra("title");
    }

    @Override
    public List<SearchBookBean> getSearchBookData() {
        return null;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bookchoice;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void firstRequest() {

    }


}