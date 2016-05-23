package util.navigation.modelos;

import java.io.Serializable;
import java.util.List;

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
}
