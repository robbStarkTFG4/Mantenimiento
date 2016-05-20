package server;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.ListaNombreEquipos;

/**
 * Created by marcoisaac on 5/19/2016.
 */
public interface MantenimientoAPI {
    public static final String BASE_URL = "http://mantenimiento-contactres.rhcloud.com/MantenimientoRest/webresources";

    public class Factory {
        private static MantenimientoAPI service;

        public static MantenimientoAPI getInstance() {
            if (service == null) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(MantenimientoAPI.BASE_URL)
                        .build();

                final MantenimientoAPI service =
                        restAdapter.create(MantenimientoAPI.class);
                return service;
            } else {
                return service;
            }
        }
    }


    @GET("/generic")
    public void getBarCode(Callback<String> cb);

    @GET("/com.mim.entities.listanombreequipos")
    public void getListNombreEquipos(Callback<List<ListaNombreEquipos>> cb);

    @GET("/com.mim.entities.listanombreequipos/params/{id}")
    public void registerFactoryParams(@Path("id")int idlistaNombre,Callback<List<InformacionFabricante>> cb);
}