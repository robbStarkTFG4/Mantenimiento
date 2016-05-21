package util.navigation.modelos;

import java.io.Serializable;
import java.util.List;

/**
 * Created by marcoisaac on 5/11/2016.
 */
public class Equipo implements Serializable {

    private Integer idequipo;
    private String numeroEquipo;
    private int listaNombreEquiposIdlistaNombre;
    private String codigoBarras;
    private Lugar lugarIdlugar;
    private List<Orden> ordenList;
    private List<InformacionFabricante> informacionFabricanteList;

    public Equipo(String numeroEquipo) {
        this.numeroEquipo = numeroEquipo;
    }

    public Equipo(String numeroEquipo, String codigoBarras, int listaNombreEquiposIdlistaNombre) {
        this.numeroEquipo = numeroEquipo;
        this.codigoBarras = codigoBarras;
        this.listaNombreEquiposIdlistaNombre = listaNombreEquiposIdlistaNombre;
    }

    public Equipo() {
    }

    public Integer getIdequipo() {
        return idequipo;
    }

    public void setIdequipo(Integer idequipo) {
        this.idequipo = idequipo;
    }

    public String getNumeroEquipo() {
        return numeroEquipo;
    }

    public void setNumeroEquipo(String numeroEquipo) {
        this.numeroEquipo = numeroEquipo;
    }

    public Lugar getLugarIdlugar() {
        return lugarIdlugar;
    }

    public void setLugarIdlugar(Lugar lugarIdlugar) {
        this.lugarIdlugar = lugarIdlugar;
    }

    public int getListaNombreEquiposIdlistaNombre() {
        return listaNombreEquiposIdlistaNombre;
    }

    public void setListaNombreEquiposIdlistaNombre(int listaNombreEquiposIdlistaNombre) {
        this.listaNombreEquiposIdlistaNombre = listaNombreEquiposIdlistaNombre;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public List<Orden> getOrdenList() {
        return ordenList;
    }

    public void setOrdenList(List<Orden> ordenList) {
        this.ordenList = ordenList;
    }

    public List<InformacionFabricante> getInformacionFabricanteList() {
        return informacionFabricanteList;
    }

    public void setInformacionFabricanteList(List<InformacionFabricante> informacionFabricanteList) {
        this.informacionFabricanteList = informacionFabricanteList;
    }
}
