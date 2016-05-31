package util.navigation.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import local_Db.EquipoDB;
import local_Db.HistorialDetallesDB;
import local_Db.LugarDB;
import local_Db.OrdenDB;

/**
 * Created by marcoisaac on 5/18/2016.
 */
public class Orden implements Serializable {
    private Integer idorden;
    private String descripcion;
    private Integer estatus;
    private String numeroOrden;
    private String encargado;
    private Equipo equipoIdequipo;
    private String prioridad;
    private String actividad;
    private List<HistorialDetalles> historialDetallesList;

    public Orden(OrdenDB current) {

        this.setDescripcion(current.getDescripcion());
        this.setEstatus(current.getStatus());
        this.setNumeroOrden(current.getNumeroOrden());
        this.setEncargado(current.getEncargado());
        this.setPrioridad(current.getPrioridad());
        this.setActividad(current.getActividad());

        EquipoDB equipoDB = current.getEquipoDB();

        Equipo equipo = new Equipo();
        equipo.setNumeroEquipo(equipoDB.getNumeroEquipo());
        equipo.setCodigoBarras(equipoDB.getCodigoBarras());
        equipo.setIdequipo(equipoDB.getIdEquipo());
        equipo.setListaNombreEquiposIdlistaNombre(equipoDB.getListaNombreEquipoIdListaNombre());


        LugarDB lugarDB = equipoDB.getLugarDB();

        Lugar lugar = new Lugar(lugarDB.getNombre());
        equipo.setLugarIdlugar(lugar);

        this.setEquipoIdequipo(equipo);
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

    public Orden(Integer idorden) {
        this.idorden = idorden;
    }

    public Orden() {
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public Equipo getEquipoIdequipo() {
        return equipoIdequipo;
    }

    public void setEquipoIdequipo(Equipo equipoIdequipo) {
        this.equipoIdequipo = equipoIdequipo;
    }

    public List<HistorialDetalles> getHistorialDetallesList() {
        return historialDetallesList;
    }

    public void setHistorialDetallesList(List<HistorialDetalles> historialDetallesList) {
        this.historialDetallesList = historialDetallesList;
    }

    public Integer getIdorden() {
        return idorden;
    }

    public void setIdorden(Integer idorden) {
        this.idorden = idorden;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public OrdenDB transform() {
        OrdenDB orden = new OrdenDB();
        orden.setEncargado(this.getEncargado());
        if (this.getPrioridad() != null) {
            orden.setPrioridad(this.getPrioridad());
        }
        orden.setActividad(this.getActividad());
        orden.setDescripcion(this.getDescripcion());
        if (this.getEstatus() != null) {
            orden.setStatus(this.getEstatus());
        }
        orden.setMostrar(false);
        if (this.getNumeroOrden() != null) {
            orden.setNumeroOrden(this.getNumeroOrden());
        }
        return orden;
    }
}
