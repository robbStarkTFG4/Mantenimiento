package util.navigation;

import java.io.Serializable;
import java.util.List;

import local_Db.HistorialDetallesDB;
import local_Db.OrdenDB;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.Lugar;

/**
 * Created by marcoisaac on 5/18/2016.
 */
public class SerialListHolder implements Serializable {
    private List<Lugar> list;
    private List<InformacionFabricante> informacionFabricantes;
    private List<HistorialDetalles> historyList;
    private List<OrdenDB> localOrderDB;
    private List<HistorialDetallesDB> historyListDB;

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

    public List<HistorialDetalles> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<HistorialDetalles> historyList) {
        this.historyList = historyList;
    }

    public void setLocalOrderDB(List<OrdenDB> localOrderDB) {
        this.localOrderDB = localOrderDB;
    }

    public List<OrdenDB> getLocalOrderDB() {
        return localOrderDB;
    }

    public List<HistorialDetallesDB> getHistoryListDB() {
        return historyListDB;
    }

    public void setHistoryListDB(List<HistorialDetallesDB> historyListDB) {
        this.historyListDB = historyListDB;
    }
}
