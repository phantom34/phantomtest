package test.phantom.com.p90.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import test.phantom.com.p90.entity.BookContentBean;
import test.phantom.com.p90.entity.BookInfoBean;
import test.phantom.com.p90.entity.BookShelfBean;
import test.phantom.com.p90.entity.ChapterListBean;
import test.phantom.com.p90.entity.DownloadChapterBean;
import test.phantom.com.p90.entity.SearchHistoryBean;

import test.phantom.com.p90.dao.BookContentBeanDao;
import test.phantom.com.p90.dao.BookInfoBeanDao;
import test.phantom.com.p90.dao.BookShelfBeanDao;
import test.phantom.com.p90.dao.ChapterListBeanDao;
import test.phantom.com.p90.dao.DownloadChapterBeanDao;
import test.phantom.com.p90.dao.SearchHistoryBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bookContentBeanDaoConfig;
    private final DaoConfig bookInfoBeanDaoConfig;
    private final DaoConfig bookShelfBeanDaoConfig;
    private final DaoConfig chapterListBeanDaoConfig;
    private final DaoConfig downloadChapterBeanDaoConfig;
    private final DaoConfig searchHistoryBeanDaoConfig;

    private final BookContentBeanDao bookContentBeanDao;
    private final BookInfoBeanDao bookInfoBeanDao;
    private final BookShelfBeanDao bookShelfBeanDao;
    private final ChapterListBeanDao chapterListBeanDao;
    private final DownloadChapterBeanDao downloadChapterBeanDao;
    private final SearchHistoryBeanDao searchHistoryBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bookContentBeanDaoConfig = daoConfigMap.get(BookContentBeanDao.class).clone();
        bookContentBeanDaoConfig.initIdentityScope(type);

        bookInfoBeanDaoConfig = daoConfigMap.get(BookInfoBeanDao.class).clone();
        bookInfoBeanDaoConfig.initIdentityScope(type);

        bookShelfBeanDaoConfig = daoConfigMap.get(BookShelfBeanDao.class).clone();
        bookShelfBeanDaoConfig.initIdentityScope(type);

        chapterListBeanDaoConfig = daoConfigMap.get(ChapterListBeanDao.class).clone();
        chapterListBeanDaoConfig.initIdentityScope(type);

        downloadChapterBeanDaoConfig = daoConfigMap.get(DownloadChapterBeanDao.class).clone();
        downloadChapterBeanDaoConfig.initIdentityScope(type);

        searchHistoryBeanDaoConfig = daoConfigMap.get(SearchHistoryBeanDao.class).clone();
        searchHistoryBeanDaoConfig.initIdentityScope(type);

        bookContentBeanDao = new BookContentBeanDao(bookContentBeanDaoConfig, this);
        bookInfoBeanDao = new BookInfoBeanDao(bookInfoBeanDaoConfig, this);
        bookShelfBeanDao = new BookShelfBeanDao(bookShelfBeanDaoConfig, this);
        chapterListBeanDao = new ChapterListBeanDao(chapterListBeanDaoConfig, this);
        downloadChapterBeanDao = new DownloadChapterBeanDao(downloadChapterBeanDaoConfig, this);
        searchHistoryBeanDao = new SearchHistoryBeanDao(searchHistoryBeanDaoConfig, this);

        registerDao(BookContentBean.class, bookContentBeanDao);
        registerDao(BookInfoBean.class, bookInfoBeanDao);
        registerDao(BookShelfBean.class, bookShelfBeanDao);
        registerDao(ChapterListBean.class, chapterListBeanDao);
        registerDao(DownloadChapterBean.class, downloadChapterBeanDao);
        registerDao(SearchHistoryBean.class, searchHistoryBeanDao);
    }
    
    public void clear() {
        bookContentBeanDaoConfig.getIdentityScope().clear();
        bookInfoBeanDaoConfig.getIdentityScope().clear();
        bookShelfBeanDaoConfig.getIdentityScope().clear();
        chapterListBeanDaoConfig.getIdentityScope().clear();
        downloadChapterBeanDaoConfig.getIdentityScope().clear();
        searchHistoryBeanDaoConfig.getIdentityScope().clear();
    }

    public BookContentBeanDao getBookContentBeanDao() {
        return bookContentBeanDao;
    }

    public BookInfoBeanDao getBookInfoBeanDao() {
        return bookInfoBeanDao;
    }

    public BookShelfBeanDao getBookShelfBeanDao() {
        return bookShelfBeanDao;
    }

    public ChapterListBeanDao getChapterListBeanDao() {
        return chapterListBeanDao;
    }

    public DownloadChapterBeanDao getDownloadChapterBeanDao() {
        return downloadChapterBeanDao;
    }

    public SearchHistoryBeanDao getSearchHistoryBeanDao() {
        return searchHistoryBeanDao;
    }

}
