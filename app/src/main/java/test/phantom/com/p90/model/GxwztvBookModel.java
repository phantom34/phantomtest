package test.phantom.com.p90.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import test.phantom.com.p90.api.IGxwztvApi;
import test.phantom.com.p90.entity.BookContentBean;
import test.phantom.com.p90.entity.BookInfoBean;
import test.phantom.com.p90.entity.BookShelfBean;
import test.phantom.com.p90.entity.SearchBookBean;
import test.phantom.com.p90.until.ErrorAnalyContentManager;

/**
 * @author Administrator
 */

// TODO: 2017/12/14  need rewrite
public class GxwztvBookModel extends BaseModel {

    public static final String TAG = "http://www.gxwztv.com";

    public static GxwztvBookModel getInstance() {
        return new GxwztvBookModel();
    }

    public Observable<BookShelfBean> getBookInfo(final BookShelfBean bookShelfBean) {
        return getRetrofitObject(TAG).create(IGxwztvApi.class).getBookInfo(bookShelfBean.getNoteUrl().replace(TAG,
                "")).flatMap(new Function<String, ObservableSource<BookShelfBean>>() {
            @Override
            public ObservableSource<BookShelfBean> apply(String s) throws Exception {
                return analyBookInfo(s, bookShelfBean);
            }
        });
    }

    private Observable<BookShelfBean> analyBookInfo(final String s, final BookShelfBean bookShelfBean) {
        return Observable.create(new ObservableOnSubscribe<BookShelfBean>() {
            @Override
            public void subscribe(ObservableEmitter<BookShelfBean> e) throws Exception {
                bookShelfBean.setTag(TAG);
                bookShelfBean.setBookInfoBean(analyBookinfo(s, bookShelfBean.getNoteUrl()));
                e.onNext(bookShelfBean);
                e.onComplete();
            }
        });
    }

    public Observable<List<SearchBookBean>> analySearchBook(final String s) {
        return Observable.create(new ObservableOnSubscribe<List<SearchBookBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SearchBookBean>> e) throws Exception {
                try {
                    Document doc = Jsoup.parse(s);
                    Elements booksE = doc.getElementById("novel-list").getElementsByClass("list-group-item clearfix");
                    if (null != booksE && booksE.size() >= 2) {
                        List<SearchBookBean> books = new ArrayList<SearchBookBean>();
                        for (int i = 1; i < booksE.size(); i++) {
                            SearchBookBean item = new SearchBookBean();
                            item.setTag(TAG);
                            item.setAuthor(booksE.get(i).getElementsByClass("col-xs-2").get(0).text());
                            item.setKind(booksE.get(i).getElementsByClass("col-xs-1").get(0).text());
                            item.setLastChapter(booksE.get(i).getElementsByClass("col-xs-4").get(0).getElementsByTag
                                    ("a").get(0).text());
                            item.setOrigin("gxwztv.com");
                            item.setName(booksE.get(i).getElementsByClass("col-xs-3").get(0).getElementsByTag("a")
                                    .get(0).text());
                            item.setNoteUrl(TAG + booksE.get(i).getElementsByClass("col-xs-3").get(0)
                                    .getElementsByTag("a").get(0).attr("href"));
                            item.setCoverUrl("noimage");
                            books.add(item);
                        }
                        e.onNext(books);
                    } else {
                        e.onNext(new ArrayList<SearchBookBean>());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    e.onNext(new ArrayList<SearchBookBean>());
                }
                e.onComplete();
            }
        });
    }


    private BookInfoBean analyBookinfo(String s, String novelUrl) {
        BookInfoBean bookInfoBean = new BookInfoBean();
        bookInfoBean.setNoteUrl(novelUrl);   //id
        bookInfoBean.setTag(TAG);
        Document doc = Jsoup.parse(s);
        Element resultE = doc.getElementsByClass("panel panel-warning").get(0);
        bookInfoBean.setCoverUrl(resultE.getElementsByClass("panel-body").get(0).getElementsByClass("img-thumbnail")
                .get(0).attr("src"));
        bookInfoBean.setName(resultE.getElementsByClass("active").get(0).text());
        bookInfoBean.setAuthor(resultE.getElementsByClass("col-xs-12 list-group-item no-border").get(0)
                .getElementsByTag("small").get(0).text());
        Element introduceE = resultE.getElementsByClass("panel panel-default mt20").get(0);
        String introduce = "";
        if (introduceE.getElementById("all") != null) {
            introduce = introduceE.getElementById("all").text().replace("[收起]", "");
        } else {
            introduce = introduceE.getElementById("shot").text();
        }
        bookInfoBean.setIntroduce("\u3000\u3000" + introduce);
        bookInfoBean.setChapterUrl(TAG + resultE.getElementsByClass("list-group-item tac").get(0).getElementsByTag
                ("a").get(0).attr("href"));
        bookInfoBean.setOrigin("gxwztv.com");
        return bookInfoBean;
    }


    public Observable<BookContentBean> getBookContent(final String durChapterUrl, final int durChapterIndex) {
        return getRetrofitObject(TAG).create(IGxwztvApi.class).getBookContent(durChapterUrl.replace(TAG, "")).flatMap
                (new Function<String, ObservableSource<BookContentBean>>() {
                    @Override
                    public ObservableSource<BookContentBean> apply(String s) throws Exception {
                        return analyBookContent(s, durChapterUrl, durChapterIndex);
                    }
                });
    }

    private Observable<BookContentBean> analyBookContent(final String s, final String durChapterUrl, final int
            durChapterIndex) {
        return Observable.create(new ObservableOnSubscribe<BookContentBean>() {
            @Override
            public void subscribe(ObservableEmitter<BookContentBean> e) throws Exception {
                BookContentBean bookContentBean = new BookContentBean();
                bookContentBean.setDurChapterIndex(durChapterIndex);
                bookContentBean.setDurChapterUrl(durChapterUrl);
                bookContentBean.setTag(TAG);
                try {
                    Document doc = Jsoup.parse(s);
                    List<TextNode> contentEs = doc.getElementById("txtContent").textNodes();
                    StringBuilder content = new StringBuilder();
                    for (int i = 0; i < contentEs.size(); i++) {
                        String temp = contentEs.get(i).text().trim();
                        temp = temp.replaceAll(" ", "").replaceAll(" ", "");
                        if (temp.length() > 0) {
                            content.append("\u3000\u3000" + temp);
                            if (i < contentEs.size() - 1) {
                                content.append("\r\n");
                            }
                        }
                    }
                    bookContentBean.setDurCapterContent(content.toString());
                    bookContentBean.setRight(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ErrorAnalyContentManager.getInstance().writeNewErrorUrl(durChapterUrl);
                    bookContentBean.setDurCapterContent(durChapterUrl.substring(0, durChapterUrl.indexOf('/', 8)) +
                            "站点暂时不支持解析，请反馈给Monke QQ:1105075896,半小时内解决，超级效率的程序员");
                    bookContentBean.setRight(false);
                }
                e.onNext(bookContentBean);
                e.onComplete();
            }
        });
    }


    public Observable<List<SearchBookBean>> searchBook(String content, int page) {
        return getRetrofitObject(TAG).create(IGxwztvApi.class).searchBook(content, page).flatMap(new Function<String,
                ObservableSource<List<SearchBookBean>>>() {
            @Override
            public ObservableSource<List<SearchBookBean>> apply(String s) throws Exception {
                return analySearchBook(s);
            }
        });
    }

    /**
     * 获取分类书籍
     */
    public Observable<List<SearchBookBean>> getKindBook(String url, int page) {
        url = url + page + ".htm";
        return getRetrofitObject(GxwztvBookModel.TAG).create(IGxwztvApi.class).getKindBooks(url.replace
                (GxwztvBookModel.TAG, "")).flatMap(new Function<String, ObservableSource<List<SearchBookBean>>>() {
            @Override
            public ObservableSource<List<SearchBookBean>> apply(String s) throws Exception {
                return analySearchBook(s);
            }
        });
    }
}