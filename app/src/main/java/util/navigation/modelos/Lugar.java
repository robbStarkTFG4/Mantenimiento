package util.navigation.modelos;

import java.io.Serializable;

/**
 * Created by marcoisaac on 5/18/2016.
 */
public class Lugar implements Serializable{
    private String nombre;

    public Lugar(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
