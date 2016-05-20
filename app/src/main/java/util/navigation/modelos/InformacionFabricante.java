package util.navigation.modelos;

import java.io.Serializable;

/**
 * Created by marcoisaac on 5/18/2016.
 */
public class InformacionFabricante implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idinformacionFabricante;
    private String parametro;
    private String valor;
    private Equipo equipoIdequipo;

    public InformacionFabricante(String parametro) {
        this.parametro = parametro;
    }

    public Integer getIdinformacionFabricante() {
        return idinformacionFabricante;
    }

    public void setIdinformacionFabricante(Integer idinformacionFabricante) {
        this.idinformacionFabricante = idinformacionFabricante;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Equipo getEquipoIdequipo() {
        return equipoIdequipo;
    }

    public void setEquipoIdequipo(Equipo equipoIdequipo) {
        this.equipoIdequipo = equipoIdequipo;
    }
}
