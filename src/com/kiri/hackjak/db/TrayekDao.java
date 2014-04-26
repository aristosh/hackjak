package com.kiri.hackjak.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.kiri.hackjak.db.Trayek;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TRAYEK.
*/
public class TrayekDao extends AbstractDao<Trayek, Long> {

    public static final String TABLENAME = "TRAYEK";

    /**
     * Properties of entity Trayek.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_ID");
        public final static Property Id = new Property(1, String.class, "id", false, "ID");
        public final static Property JenisAngkutan = new Property(2, String.class, "jenisAngkutan", false, "JENIS_ANGKUTAN");
        public final static Property JenisTrayek = new Property(3, String.class, "jenisTrayek", false, "JENIS_TRAYEK");
        public final static Property NoTrayek = new Property(4, String.class, "noTrayek", false, "NO_TRAYEK");
        public final static Property NamaTrayek = new Property(5, String.class, "namaTrayek", false, "NAMA_TRAYEK");
        public final static Property Terminal = new Property(6, String.class, "terminal", false, "TERMINAL");
        public final static Property KodeWilayah = new Property(7, String.class, "kodeWilayah", false, "KODE_WILAYAH");
        public final static Property Wilayah = new Property(8, String.class, "wilayah", false, "WILAYAH");
        public final static Property SukuDinas = new Property(9, String.class, "sukuDinas", false, "SUKU_DINAS");
    };

    private DaoSession daoSession;


    public TrayekDao(DaoConfig config) {
        super(config);
    }
    
    public TrayekDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TRAYEK' (" + //
                "'_ID' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "'ID' TEXT UNIQUE ," + // 1: id
                "'JENIS_ANGKUTAN' TEXT NOT NULL ," + // 2: jenisAngkutan
                "'JENIS_TRAYEK' TEXT," + // 3: jenisTrayek
                "'NO_TRAYEK' TEXT," + // 4: noTrayek
                "'NAMA_TRAYEK' TEXT," + // 5: namaTrayek
                "'TERMINAL' TEXT," + // 6: terminal
                "'KODE_WILAYAH' TEXT," + // 7: kodeWilayah
                "'WILAYAH' TEXT," + // 8: wilayah
                "'SUKU_DINAS' TEXT);"); // 9: sukuDinas
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TRAYEK'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Trayek entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
        stmt.bindString(3, entity.getJenisAngkutan());
 
        String jenisTrayek = entity.getJenisTrayek();
        if (jenisTrayek != null) {
            stmt.bindString(4, jenisTrayek);
        }
 
        String noTrayek = entity.getNoTrayek();
        if (noTrayek != null) {
            stmt.bindString(5, noTrayek);
        }
 
        String namaTrayek = entity.getNamaTrayek();
        if (namaTrayek != null) {
            stmt.bindString(6, namaTrayek);
        }
 
        String terminal = entity.getTerminal();
        if (terminal != null) {
            stmt.bindString(7, terminal);
        }
 
        String kodeWilayah = entity.getKodeWilayah();
        if (kodeWilayah != null) {
            stmt.bindString(8, kodeWilayah);
        }
 
        String wilayah = entity.getWilayah();
        if (wilayah != null) {
            stmt.bindString(9, wilayah);
        }
 
        String sukuDinas = entity.getSukuDinas();
        if (sukuDinas != null) {
            stmt.bindString(10, sukuDinas);
        }
    }

    @Override
    protected void attachEntity(Trayek entity) {
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
    public Trayek readEntity(Cursor cursor, int offset) {
        Trayek entity = new Trayek( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // id
            cursor.getString(offset + 2), // jenisAngkutan
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // jenisTrayek
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // noTrayek
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // namaTrayek
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // terminal
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // kodeWilayah
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // wilayah
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // sukuDinas
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Trayek entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setJenisAngkutan(cursor.getString(offset + 2));
        entity.setJenisTrayek(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setNoTrayek(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNamaTrayek(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTerminal(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setKodeWilayah(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setWilayah(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSukuDinas(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Trayek entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Trayek entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
