package test.phantom.com.p90.ui.searchbook;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import test.phantom.com.p90.R;
import test.phantom.com.p90.base.KBaseActivity;
import test.phantom.com.p90.entity.SearchBookBean;
import test.phantom.com.p90.entity.SearchHistoryBean;
import test.phantom.com.p90.ui.bookdetail.BookDetailActivity;
import test.phantom.com.p90.ui.bookdetail.BookDetailPersenter;
import test.phantom.com.p90.until.NetworkUtil;
import test.phantom.com.p90.widget.flowlayout.TagFlowLayout;
import test.phantom.com.p90.widget.irecyclerview.IRecyclerView;
import tyrantgit.explosionfield.ExplosionField;

/**
 * @author Administrator
 */

public class SearchActivity extends KBaseActivity<SearchContarct.Presenter> implements SearchContarct.View {
    private FrameLayout flSearchContent;
    private EditText edtContent;
    private TextView tvTosearch;

    private LinearLayout llSearchHistory;
    private TextView tvSearchHistoryClean;
    private TagFlowLayout tflSearchHistory;
    private SearchHistoryAdapter searchHistoryAdapter;
    private Animation animHistory;
    private Animator animHistory5;
    private ExplosionField explosionField;

    private IRecyclerView rfRvSearchBooks;
    private SearchBookAdapter searchBookAdapter;

    private List<SearchBookBean> listData = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void init() {
        initView();
        initEvent();
    }

    @Override
    protected void initInjector() {

    }

    protected void initView() {

        explosionField = ExplosionField.attach2Window(this);
        searchHistoryAdapter = new SearchHistoryAdapter();
        searchBookAdapter = new SearchBookAdapter(this, listData);
        flSearchContent = (FrameLayout) findViewById(R.id.fl_search_content);
        edtContent = (EditText) findViewById(R.id.edt_content);
        tvTosearch = (TextView) findViewById(R.id.tv_tosearch);

        llSearchHistory = (LinearLayout) findViewById(R.id.ll_search_history);
        tvSearchHistoryClean = (TextView) findViewById(R.id.tv_search_history_clean);
        tflSearchHistory = (TagFlowLayout) findViewById(R.id.tfl_search_history);
        tflSearchHistory.setAdapter(searchHistoryAdapter);

        rfRvSearchBooks = (IRecyclerView) findViewById(R.id.rfRv_search_books);
        rfRvSearchBooks.setAdapter(searchBookAdapter);
        rfRvSearchBooks.setLayoutManager(new LinearLayoutManager(this));

        View viewRefreshError = LayoutInflater.from(this).inflate(R.layout.view_searchbook_refresherror, null);
        viewRefreshError.findViewById(R.id.tv_refresh_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //刷新失败 ，重试
                mPresenter.initPage();
                mPresenter.toSearchBooks(null, true);
                rfRvSearchBooks.setRefreshing(true);
            }
        });

        searchBookAdapter.setItemClickListener(new SearchBookAdapter.OnItemClickListener() {
            @Override
            public void clickAddShelf(View clickView, SearchBookBean searchBookBean) {
                mPresenter.addBookToShelf(searchBookBean);
            }

            @Override
            public void clickItem(View animView, SearchBookBean searchBookBean) {
                Intent intent = new Intent(SearchActivity.this, BookDetailActivity.class);
                intent.putExtra("from", BookDetailPersenter.Companion.getFROM_SEARCH());
                intent.putExtra("data", searchBookBean);
                startActivityByAnim(intent, animView, "img_cover", android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    protected void initEvent() {

        tvSearchHistoryClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < tflSearchHistory.getChildCount(); i++) {
                    explosionField.explode(tflSearchHistory.getChildAt(i));
                }
                mPresenter.cleanSearchHistory();
            }
        });
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtContent.setSelection(edtContent.length());
                checkTvToSearch();
                mPresenter.querySearchHistory();
            }
        });
        edtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent
                        .KEYCODE_ENTER)) {
                    toSearch();
                    return true;
                } else {
                    return false;
                }
            }
        });
        tvTosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPresenter.getInput()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    } else {
                        finish();
                    }
                } else {
                    //搜索
                    toSearch();
                }
            }
        });

        searchHistoryAdapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void itemClick(SearchHistoryBean searchHistoryBean) {
                edtContent.setText(searchHistoryBean.getContent());
                toSearch();
            }
        });

        bindKeyBoardEvent();

    }


    /**
     * 开始搜索
     **/
    public void toSearch() {

        if (edtContent.getText().toString().trim().length() > 0) {
            final String key = edtContent.getText().toString().trim();
            mPresenter.setHasSearch(true);
            mPresenter.insertSearchHistory();
            closeKeyBoard();

            mPresenter.initPage();
            mPresenter.toSearchBooks(key, false);
        } else {
            YoYo.with(Techniques.Shake).playOn(flSearchContent);
        }
    }

    private void bindKeyBoardEvent() {
        llSearchHistory.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                llSearchHistory.getWindowVisibleDisplayFrame(r);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) llSearchHistory.getLayoutParams();

                int height = llSearchHistory.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

//                        && Math.abs(diff) != StatusBarUtils.getNavi_height()
                if (diff != 0) {
                    if (layoutParams.bottomMargin != diff) {
                        layoutParams.setMargins(0, 0, 0, Math.abs(diff));
                        llSearchHistory.setLayoutParams(layoutParams);
                        //打开输入
                        if (llSearchHistory.getVisibility() != View.VISIBLE) {
                            openOrCloseHistory(true);
                        }
                    }
                } else {
                    if (layoutParams.bottomMargin != 0) {
                        if (!mPresenter.getHasSearch()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                finishAfterTransition();
                            } else {
                                finish();
                            }
                        } else {
                            layoutParams.setMargins(0, 0, 0, 0);
                            llSearchHistory.setLayoutParams(layoutParams);
                            //关闭输入
                            if (llSearchHistory.getVisibility() == View.VISIBLE) {
                                openOrCloseHistory(false);
                            }
                        }
                    }
                }
            }
        });

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openKeyBoard();
                    }
                }, 100);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void checkTvToSearch() {
        if (llSearchHistory.getVisibility() == View.VISIBLE) {
            tvTosearch.setText("搜索");
            mPresenter.setInput(true);
        } else {
            tvTosearch.setText("返回");
            mPresenter.setInput(false);
        }
    }

    private void openOrCloseHistory(Boolean open) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (null != animHistory5) {
                animHistory5.cancel();
            }
            if (open) {
                animHistory5 = ViewAnimationUtils.createCircularReveal(
                        llSearchHistory,
                        0, 0, 0,
                        (float) Math.hypot(llSearchHistory.getWidth(), llSearchHistory.getHeight()));
                animHistory5.setInterpolator(new AccelerateDecelerateInterpolator());
                animHistory5.setDuration(700);
                animHistory5.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        llSearchHistory.setVisibility(View.VISIBLE);
                        edtContent.setCursorVisible(true);
                        checkTvToSearch();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (rfRvSearchBooks.getVisibility() != View.VISIBLE) {
                            rfRvSearchBooks.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animHistory5.start();
            } else {
                animHistory5 = ViewAnimationUtils.createCircularReveal(
                        llSearchHistory,
                        0, 0, (float) Math.hypot(llSearchHistory.getHeight(), llSearchHistory.getHeight()),
                        0);
                animHistory5.setInterpolator(new AccelerateDecelerateInterpolator());
                animHistory5.setDuration(300);
                animHistory5.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        llSearchHistory.setVisibility(View.GONE);
                        edtContent.setCursorVisible(false);
                        checkTvToSearch();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animHistory5.start();
            }
        } else {
            if (null != animHistory) {
                animHistory.cancel();
            }
            if (open) {
                animHistory = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
                animHistory.setInterpolator(new AccelerateDecelerateInterpolator());
                animHistory.setDuration(700);
                animHistory.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        llSearchHistory.setVisibility(View.VISIBLE);
                        edtContent.setCursorVisible(true);
                        checkTvToSearch();
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (rfRvSearchBooks.getVisibility() != View.VISIBLE) {
                            rfRvSearchBooks.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                llSearchHistory.startAnimation(animHistory);
            } else {
                animHistory = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
                animHistory.setInterpolator(new AccelerateDecelerateInterpolator());
                animHistory.setDuration(300);
                animHistory.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        llSearchHistory.setVisibility(View.GONE);
                        edtContent.setCursorVisible(false);
                        checkTvToSearch();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                llSearchHistory.startAnimation(animHistory);
            }
        }
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtContent.getWindowToken(), 0);
    }

    private void openKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        edtContent.requestFocus();
        imm.showSoftInput(edtContent, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    @Override
    public void insertSearchHistorySuccess(SearchHistoryBean searchHistoryBean) {
        //搜索历史插入或者修改成功
        mPresenter.querySearchHistory();
    }

    @Override
    public void querySearchHistorySuccess(List<SearchHistoryBean> datas) {
        searchHistoryAdapter.replaceAll(datas);
        if (searchHistoryAdapter.getDataSize() > 0) {
            tvSearchHistoryClean.setVisibility(View.VISIBLE);
        } else {
            tvSearchHistoryClean.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void loadMoreSearchBook(final List<SearchBookBean> books) {
        searchBookAdapter.addAll(books);
    }

    @Override
    public EditText getEdtContent() {
        return edtContent;
    }

    @Override
    public void addBookShelfFailed(int code) {
        Toast.makeText(this, NetworkUtil.getErrorTip(code), Toast.LENGTH_SHORT).show();
    }

    @Override
    public SearchBookAdapter getSearchBookAdapter() {
        return searchBookAdapter;
    }

    @Override
    public void updateSearchItem(int index) {
        searchBookAdapter.notifyDataSetChanged();
    }

    @Override
    public Boolean checkIsExist(SearchBookBean searchBookBean) {
        Boolean result = false;
        for (int i = 0; i < searchBookAdapter.getItemCount(); i++) {
            if (listData.get(i).getNoteUrl().equals(searchBookBean.getNoteUrl()) &&
                    listData.get(i).getTag().equals(searchBookBean.getTag())) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public List<SearchBookBean> getListData() {
        return listData;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        explosionField.clear();
    }

    @Override
    protected void firstRequest() {

    }
}