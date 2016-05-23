package util.navigation.modelos;

import java.io.Serializable;

/**
 * Created by marcoisaac on 5/12/2016.
 */
public class Foto implements Serializable {
    private Integer idfotos;
    private String titulo;
    private String descripcion;
    private String archivo;
    private Orden ordenIdorden;;

    public Foto() {
    }

    public Foto(String archivo, String titulo, String descripcion) {
        this.archivo = archivo;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Integer getIdfotos() {
        return idfotos;
    }

    public void setIdfotos(Integer idfotos) {
        this.idfotos = idfotos;
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

    public Orden getOrdenIdorden() {
        return ordenIdorden;
    }

    public void setOrdenIdorden(Orden ordenIdorden) {
        this.ordenIdorden = ordenIdorden;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
}
