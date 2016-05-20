package util.navigation.modelos;

import java.util.List;

/**
 * Created by marcoisaac on 5/11/2016.
 */
public class Equipo {
    private String nombre;
    private String id;
    private List<HistorialDetalles> historialDetallesList;
    private Lugar linea;

    public Equipo(String nombre, String linea, String id) {
        this.nombre = nombre;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Lugar getLinea() {
        return linea;
    }


}
