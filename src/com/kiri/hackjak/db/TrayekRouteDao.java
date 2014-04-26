package com.kiri.hackjak.db;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.kiri.hackjak.db.TrayekRoute;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TRAYEK_ROUTE.
*/
public class TrayekRouteDao extends AbstractDao<TrayekRoute, Long> {

    public static final String TABLENAME = "TRAYEK_ROUTE";

    /**
     * Properties of entity TrayekRoute.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property NamaRute = new Property(1, String.class, "namaRute", false, "NAMA_RUTE");
        public final static Property IdTrayek = new Property(2, long.class, "idTrayek", false, "ID_TRAYEK");
    };

    private DaoSession daoSession;

    private Query<TrayekRoute> trayek_RuteQuery;

    public TrayekRouteDao(DaoConfig config) {
        super(config);
    }
    
    public TrayekRouteDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TRAYEK_ROUTE' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'NAMA_RUTE' TEXT NOT NULL ," + // 1: namaRute
                "'ID_TRAYEK' INTEGER NOT NULL );"); // 2: idTrayek
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TRAYEK_ROUTE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TrayekRoute entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getNamaRute());
        stmt.bindLong(3, entity.getIdTrayek());
    }

    @Override
    protected void attachEntity(TrayekRoute entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public TrayekRoute readEntity(Cursor cursor, int offset) {
        TrayekRoute entity = new TrayekRoute( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // namaRute
            cursor.getLong(offset + 2) // idTrayek
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TrayekRoute entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNamaRute(cursor.getString(offset + 1));
        entity.setIdTrayek(cursor.getLong(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TrayekRoute entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TrayekRoute entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "rute" to-many relationship of Trayek. */
    public List<TrayekRoute> _queryTrayek_Rute(long idTrayek) {
        synchronized (this) {
            if (trayek_RuteQuery == null) {
                QueryBuilder<TrayekRoute> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.IdTrayek.eq(null));
                queryBuilder.orderRaw("NAMA_RUTE ASC");
                trayek_RuteQuery = queryBuilder.build();
            }
        }
        Query<TrayekRoute> query = trayek_RuteQuery.forCurrentThread();
        query.setParameter(0, idTrayek);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getTrayekDao().getAllColumns());
            builder.append(" FROM TRAYEK_ROUTE T");
            builder.append(" LEFT JOIN TRAYEK T0 ON T.'ID_TRAYEK'=T0.'_ID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected TrayekRoute loadCurrentDeep(Cursor cursor, boolean lock) {
        TrayekRoute entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Trayek trayek = loadCurrentOther(daoSession.getTrayekDao(), cursor, offset);
         if(trayek != null) {
            entity.setTrayek(trayek);
        }

        return entity;    
    }

    public TrayekRoute loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<TrayekRoute> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<TrayekRoute> list = new ArrayList<TrayekRoute>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<TrayekRoute> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<TrayekRoute> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
