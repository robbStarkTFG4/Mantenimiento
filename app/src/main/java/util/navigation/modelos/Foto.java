package util.navigation.modelos;

/**
 * Created by marcoisaac on 5/12/2016.
 */
public class Foto {
    private int id;
    private String path;
    private String titulo;
    private String descripcion;

    public Foto() {
    }

    public Foto(String path, String titulo, String descripcion) {
        this.path = path;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
}
