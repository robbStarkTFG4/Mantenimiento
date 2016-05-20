package util.navigation.modelos;

/**
 * Created by marcoisaac on 5/11/2016.
 */
public class HistorialDetalles {
    private Integer idhistorialDetalles;
    private String parametro;
    private String valor;
    private Orden ordenIdorden;

    public HistorialDetalles(String parametro, String valor) {
        this.parametro = parametro;
        this.valor = valor;
    }

    public HistorialDetalles(String parametro) {
        this.parametro = parametro;
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

}
