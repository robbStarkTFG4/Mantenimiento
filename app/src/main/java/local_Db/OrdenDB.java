package local_Db;

import java.io.Serializable;
import java.util.List;
import local_Db.DaoSession;
import de.greenrobot.dao.DaoException;
import util.navigation.modelos.Equipo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ORDEN_DB.
 */
public class OrdenDB implements Serializable {

    private Long id;
    private Integer idOrden;
    private String descripcion;
    private Integer status;
    private String numeroOrden;
    private String prioridad;
    private String actividad;
    private String encargado;
    private Long equipoId;
    private Long ordenId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient OrdenDBDao myDao;

    private EquipoDB equipoDB;
    private Long equipoDB__resolvedKey;

    private List<HistorialDetallesDB> historialDetallesDBList;
    private List<FotoDB> fotoDBList;



    public OrdenDB() {
    }

    public OrdenDB(Long id) {
        this.id = id;
    }

    public OrdenDB(Long id, Integer idOrden, String descripcion, Integer status, String numeroOrden, String prioridad, String actividad, String encargado, Long equipoId, Long ordenId) {
        this.id = id;
        this.idOrden = idOrden;
        this.descripcion = descripcion;
        this.status = status;
        this.numeroOrden = numeroOrden;
        this.prioridad = prioridad;
        this.actividad = actividad;
        this.encargado = encargado;
        this.equipoId = equipoId;
        this.ordenId = ordenId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrdenDBDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Integer idOrden) {
        this.idOrden = idOrden;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public Long getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Long equipoId) {
        this.equipoId = equipoId;
    }

    public Long getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Long ordenId) {
        this.ordenId = ordenId;
    }

    /** To-one relationship, resolved on first access. */
    public EquipoDB getEquipoDB() {
        Long __key = this.equipoId;
        if (equipoDB__resolvedKey == null || !equipoDB__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EquipoDBDao targetDao = daoSession.getEquipoDBDao();
            EquipoDB equipoDBNew = targetDao.load(__key);
            synchronized (this) {
                equipoDB = equipoDBNew;
            	equipoDB__resolvedKey = __key;
            }
        }
        return equipoDB;
    }

    public void setEquipoDB(EquipoDB equipoDB) {
        synchronized (this) {
            this.equipoDB = equipoDB;
            equipoId = equipoDB == null ? null : equipoDB.getId();
            equipoDB__resolvedKey = equipoId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<HistorialDetallesDB> getHistorialDetallesDBList() {
        if (historialDetallesDBList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            HistorialDetallesDBDao targetDao = daoSession.getHistorialDetallesDBDao();
            List<HistorialDetallesDB> historialDetallesDBListNew = targetDao._queryOrdenDB_HistorialDetallesDBList(id);
            synchronized (this) {
                if(historialDetallesDBList == null) {
                    historialDetallesDBList = historialDetallesDBListNew;
                }
            }
        }
        return historialDetallesDBList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetHistorialDetallesDBList() {
        historialDetallesDBList = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<FotoDB> getFotoDBList() {
        if (fotoDBList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FotoDBDao targetDao = daoSession.getFotoDBDao();
            List<FotoDB> fotoDBListNew = targetDao._queryOrdenDB_FotoDBList(id);
            synchronized (this) {
                if(fotoDBList == null) {
                    fotoDBList = fotoDBListNew;
                }
            }
        }
        return fotoDBList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetFotoDBList() {
        fotoDBList = null;
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

    public EquipoDB getEquipoDB2() {
        return this.equipoDB;
    }

    public List<HistorialDetallesDB> getHistorialDetallesDBList2() {
        return historialDetallesDBList;
    }

    public void setHistoryDetallesDB(List<HistorialDetallesDB> historyDetallesDB) {
        this.historialDetallesDBList = historyDetallesDB;
    }
}
