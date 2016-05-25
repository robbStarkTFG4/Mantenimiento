package local_Db;

import java.io.Serializable;

import local_Db.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table FOTO_DB.
 */
public class FotoDB implements Serializable {

    private Long id;
    private String titulo;
    private String descripcion;
    private String archivo;
    private Long idOrden;
    private Long fotoId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient FotoDBDao myDao;

    private OrdenDB ordenDB;
    private Long ordenDB__resolvedKey;


    public FotoDB() {
    }

    public FotoDB(Long id) {
        this.id = id;
    }

    public FotoDB(Long id, String titulo, String descripcion, String archivo, Long idOrden, Long fotoId) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.archivo = archivo;
        this.idOrden = idOrden;
        this.fotoId = fotoId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFotoDBDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    public Long getFotoId() {
        return fotoId;
    }

    public void setFotoId(Long fotoId) {
        this.fotoId = fotoId;
    }

    /** To-one relationship, resolved on first access. */
    public OrdenDB getOrdenDB() {
        Long __key = this.idOrden;
        if (ordenDB__resolvedKey == null || !ordenDB__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrdenDBDao targetDao = daoSession.getOrdenDBDao();
            OrdenDB ordenDBNew = targetDao.load(__key);
            synchronized (this) {
                ordenDB = ordenDBNew;
            	ordenDB__resolvedKey = __key;
            }
        }
        return ordenDB;
    }

    public void setOrdenDB(OrdenDB ordenDB) {
        synchronized (this) {
            this.ordenDB = ordenDB;
            idOrden = ordenDB == null ? null : ordenDB.getId();
            ordenDB__resolvedKey = idOrden;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    public OrdenDB getOrdenDB2() {
        return ordenDB;
    }
}