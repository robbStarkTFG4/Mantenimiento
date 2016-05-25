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

import local_Db.FotoDB;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table FOTO_DB.
*/
public class FotoDBDao extends AbstractDao<FotoDB, Long> {

    public static final String TABLENAME = "FOTO_DB";

    /**
     * Properties of entity FotoDB.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Titulo = new Property(1, String.class, "titulo", false, "TITULO");
        public final static Property Descripcion = new Property(2, String.class, "descripcion", false, "DESCRIPCION");
        public final static Property Archivo = new Property(3, String.class, "archivo", false, "ARCHIVO");
        public final static Property IdOrden = new Property(4, Long.class, "idOrden", false, "ID_ORDEN");
        public final static Property FotoId = new Property(5, Long.class, "fotoId", false, "FOTO_ID");
    };

    private DaoSession daoSession;

    private Query<FotoDB> ordenDB_FotoDBListQuery;

    public FotoDBDao(DaoConfig config) {
        super(config);
    }
    
    public FotoDBDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'FOTO_DB' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'TITULO' TEXT," + // 1: titulo
                "'DESCRIPCION' TEXT," + // 2: descripcion
                "'ARCHIVO' TEXT," + // 3: archivo
                "'ID_ORDEN' INTEGER," + // 4: idOrden
                "'FOTO_ID' INTEGER);"); // 5: fotoId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'FOTO_DB'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, FotoDB entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String titulo = entity.getTitulo();
        if (titulo != null) {
            stmt.bindString(2, titulo);
        }
 
        String descripcion = entity.getDescripcion();
        if (descripcion != null) {
            stmt.bindString(3, descripcion);
        }
 
        String archivo = entity.getArchivo();
        if (archivo != null) {
            stmt.bindString(4, archivo);
        }
 
        Long idOrden = entity.getIdOrden();
        if (idOrden != null) {
            stmt.bindLong(5, idOrden);
        }
 
        Long fotoId = entity.getFotoId();
        if (fotoId != null) {
            stmt.bindLong(6, fotoId);
        }
    }

    @Override
    protected void attachEntity(FotoDB entity) {
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
    public FotoDB readEntity(Cursor cursor, int offset) {
        FotoDB entity = new FotoDB( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // titulo
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // descripcion
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // archivo
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // idOrden
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // fotoId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, FotoDB entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitulo(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDescripcion(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setArchivo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIdOrden(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setFotoId(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(FotoDB entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(FotoDB entity) {
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
    
    /** Internal query to resolve the "fotoDBList" to-many relationship of OrdenDB. */
    public List<FotoDB> _queryOrdenDB_FotoDBList(Long fotoId) {
        synchronized (this) {
            if (ordenDB_FotoDBListQuery == null) {
                QueryBuilder<FotoDB> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.FotoId.eq(null));
                ordenDB_FotoDBListQuery = queryBuilder.build();
            }
        }
        Query<FotoDB> query = ordenDB_FotoDBListQuery.forCurrentThread();
        query.setParameter(0, fotoId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getOrdenDBDao().getAllColumns());
            builder.append(" FROM FOTO_DB T");
            builder.append(" LEFT JOIN ORDEN_DB T0 ON T.'ID_ORDEN'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected FotoDB loadCurrentDeep(Cursor cursor, boolean lock) {
        FotoDB entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        OrdenDB ordenDB = loadCurrentOther(daoSession.getOrdenDBDao(), cursor, offset);
        entity.setOrdenDB(ordenDB);

        return entity;    
    }

    public FotoDB loadDeep(Long key) {
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
    public List<FotoDB> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<FotoDB> list = new ArrayList<FotoDB>(count);
        
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
    
    protected List<FotoDB> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<FotoDB> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}