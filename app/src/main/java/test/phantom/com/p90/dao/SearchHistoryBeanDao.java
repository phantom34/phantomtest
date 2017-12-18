package test.phantom.com.p90.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import test.phantom.com.p90.entity.SearchHistoryBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SEARCH_HISTORY_BEAN".
*/
public class SearchHistoryBeanDao extends AbstractDao<SearchHistoryBean, Long> {

    public static final String TABLENAME = "SEARCH_HISTORY_BEAN";

    /**
     * Properties of entity SearchHistoryBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Type = new Property(1, int.class, "type", false, "TYPE");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property Date = new Property(3, long.class, "date", false, "DATE");
    };


    public SearchHistoryBeanDao(DaoConfig config) {
        super(config);
    }
    
    public SearchHistoryBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SEARCH_HISTORY_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TYPE\" INTEGER NOT NULL ," + // 1: type
                "\"CONTENT\" TEXT," + // 2: content
                "\"DATE\" INTEGER NOT NULL );"); // 3: date
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SEARCH_HISTORY_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SearchHistoryBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getType());
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
        stmt.bindLong(4, entity.getDate());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SearchHistoryBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getType());
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
        stmt.bindLong(4, entity.getDate());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SearchHistoryBean readEntity(Cursor cursor, int offset) {
        SearchHistoryBean entity = new SearchHistoryBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // type
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // content
            cursor.getLong(offset + 3) // date
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SearchHistoryBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setType(cursor.getInt(offset + 1));
        entity.setContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDate(cursor.getLong(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SearchHistoryBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SearchHistoryBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
