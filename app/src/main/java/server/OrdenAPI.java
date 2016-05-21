package server;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import util.navigation.modelos.Equipo;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.ListaNombreEquipos;

/**
 * Service used to register an equipment into the DB
 * Created by marcoisaac on 5/19/2016.
 */
public interface OrdenAPI {
    public static final String BASE_URL = "http://mantenimiento-contactres.rhcloud.com/MantenimientoRest/webresources";

    public class Factory {
        private static OrdenAPI service;

        public static OrdenAPI getInstance() {
            if (service == null) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(OrdenAPI.BASE_URL)
                        .build();

                final OrdenAPI service =
                        restAdapter.create(OrdenAPI.class);
                return service;
            } else {
                return service;
            }
        }
    }

    @GET("/com.mim.entities.equipo/{code}")
    public void getEquipmentByCodeBar(@Path("code") String codigo, Callback<Equipo> cb);

    @GET("/com.mim.entities.informacionfabricante/factoryList/{id}")
    public void getFactoryList(@Path("id") int equipoId, Callback<List<InformacionFabricante>> cb);

    @GET("/com.mim.entities.historialdetalles/history/{id}")
    public void getHistorialDetalles(@Path("id") int idEquipo, Callback<List<HistorialDetalles>> cb);

    @GET("/com.mim.entities.listanombreequipos/nombre/{id}")
    public void getNombreEquipo(@Path("id") int id, Callback<ListaNombreEquipos> cb);
}