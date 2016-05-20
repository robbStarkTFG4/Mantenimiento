package util.navigation;

import java.io.Serializable;
import java.util.List;

import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.Lugar;

/**
 * Created by marcoisaac on 5/18/2016.
 */
public class SerialListHolder implements Serializable {
    private List<Lugar> list;
    private List<InformacionFabricante> informacionFabricantes;

    public SerialListHolder(List<Lugar> list) {
        this.list = list;
    }

    public SerialListHolder() {
    }

    public List<Lugar> getList() {
        return list;
    }

    public void setList(List<Lugar> list) {
        this.list = list;
    }

    public List<InformacionFabricante> getInformacionFabricantes() {
        return informacionFabricantes;
    }

    public void setInformacionFabricantes(List<InformacionFabricante> informacionFabricantes) {
        this.informacionFabricantes = informacionFabricantes;
    }
}
