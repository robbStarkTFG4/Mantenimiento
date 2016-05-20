package util.navigation.modelos;

import java.io.Serializable;

/**
 * Created by marcoisaac on 5/17/2016.
 */
public class ListaNombreEquipos implements Serializable{
    private String nombre;
    private int idlistaNombre;

    public ListaNombreEquipos(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdlistaNombre() {
        return idlistaNombre;
    }

    public void setIdlistaNombre(int idlistaNombre) {
        this.idlistaNombre = idlistaNombre;
    }
}
