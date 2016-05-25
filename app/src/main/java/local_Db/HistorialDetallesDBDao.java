package local_Db;

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

import local_Db.HistorialDetallesDB;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table HISTORIAL_DETALLES_DB.
*/
public class HistorialDetallesDBDao extends AbstractDao<HistorialDetallesDB, Long> {

    public static final String TABLENAME = "HISTORIAL_DETALLES_DB";

    /**
     * Properties of entity HistorialDetallesDB.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Idhistorial = new Property(1, Integer.class, "idhistorial", false, "IDHISTORIAL");
        public final static Property Parametro = new Property(2, String.class, "parametro", false, "PARAMETRO");
        public final static Property Valor = new Property(3, String.class, "valor", false, "VALOR");
        public final static Property OrdenId = new Property(4, Long.class, "ordenId", false, "ORDEN_ID");
        public final static Property HitorialId = new Property(5, Long.class, "hitorialId", false, "HITORIAL_ID");
    };

    private DaoSession daoSession;

    private Query<HistorialDetallesDB> ordenDB_HistorialDetallesDBListQuery;

    public HistorialDetallesDBDao(DaoConfig config) {
        super(config);
    }
    
    public HistorialDetallesDBDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'HISTORIAL_DETALLES_DB' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'IDHISTORIAL' INTEGER," + // 1: idhistorial
                "'PARAMETRO' TEXT," + // 2: parametro
                "'VALOR' TEXT," + // 3: valor
                "'ORDEN_ID' INTEGER," + // 4: ordenId
                "'HITORIAL_ID' INTEGER);"); // 5: hitorialId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'HISTORIAL_DETALLES_DB'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, HistorialDetallesDB entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer idhistorial = entity.getIdhistorial();
        if (idhistorial != null) {
            stmt.bindLong(2, idhistorial);
        }
 
        String parametro = entity.getParametro();
        if (parametro != null) {
            stmt.bindString(3, parametro);
        }
 
        String valor = entity.getValor();
        if (valor != null) {
            stmt.bindString(4, valor);
        }
 
        Long ordenId = entity.getOrdenId();
        if (ordenId != null) {
            stmt.bindLong(5, ordenId);
        }
 
        Long hitorialId = entity.getHitorialId();
        if (hitorialId != null) {
            stmt.bindLong(6, hitorialId);
        }
    }

    @Override
    protected void attachEntity(HistorialDetallesDB entity) {
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
    public HistorialDetallesDB readEntity(Cursor cursor, int offset) {
        HistorialDetallesDB entity = new HistorialDetallesDB( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // idhistorial
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // parametro
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // valor
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // ordenId
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // hitorialId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, HistorialDetallesDB entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIdhistorial(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setParametro(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setValor(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setOrdenId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setHitorialId(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(HistorialDetallesDB entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(HistorialDetallesDB entity) {
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
    
    /** Internal query to resolve the "historialDetallesDBList" to-many relationship of OrdenDB. */
    public List<HistorialDetallesDB> _queryOrdenDB_HistorialDetallesDBList(Long hitorialId) {
        synchronized (this) {
            if (ordenDB_HistorialDetallesDBListQuery == null) {
                QueryBuilder<HistorialDetallesDB> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.HitorialId.eq(null));
                ordenDB_HistorialDetallesDBListQuery = queryBuilder.build();
            }
        }
        Query<HistorialDetallesDB> query = ordenDB_HistorialDetallesDBListQuery.forCurrentThread();
        query.setParameter(0, hitorialId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getOrdenDBDao().getAllColumns());
            builder.append(" FROM HISTORIAL_DETALLES_DB T");
            builder.append(" LEFT JOIN ORDEN_DB T0 ON T.'ORDEN_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected HistorialDetallesDB loadCurrentDeep(Cursor cursor, boolean lock) {
        HistorialDetallesDB entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        OrdenDB ordenDB = loadCurrentOther(daoSession.getOrdenDBDao(), cursor, offset);
        entity.setOrdenDB(ordenDB);

        return entity;    
    }

    public HistorialDetallesDB loadDeep(Long key) {
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
    public List<HistorialDetallesDB> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<HistorialDetallesDB> list = new ArrayList<HistorialDetallesDB>(count);
        
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
    
    protected List<HistorialDetallesDB> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<HistorialDetallesDB> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}