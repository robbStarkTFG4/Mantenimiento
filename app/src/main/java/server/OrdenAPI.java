package server;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import util.navigation.modelos.Equipo;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.ListaNombreEquipos;

/** Service used to register an equipment into the DB
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

}