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

/**
 * Service used to register an equipment into the DB
 * Created by marcoisaac on 5/19/2016.
 */
public interface RegisterAPI {
    //public static final String BASE_URL = "http://mantenimiento-contactres.rhcloud.com/MantenimientoRest/webresources";
    public static final String BASE_URL = "http://env-5002349.jl.serv.net.mx/rest/webresources";

    public class Factory {
        private static RegisterAPI service;

        public static RegisterAPI getInstance() {
            if (service == null) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(RegisterAPI.BASE_URL)
                        .build();

                final RegisterAPI service =
                        restAdapter.create(RegisterAPI.class);
                return service;
            } else {
                return service;
            }
        }
    }


    @GET("/generic")
    public void getBarCode(Callback<String> cb);

    @GET("/generic/valide/{codigo}")
    public void validateCode(@Path("codigo") String codigo, Callback<String> cb);

    @GET("/com.mim.entities.listanombreequipos")
    public void getListNombreEquipos(Callback<List<ListaNombreEquipos>> cb);

    @GET("/com.mim.entities.listanombreequipos/params/{id}")
    public void registerFactoryParams(@Path("id") int idlistaNombre, Callback<List<InformacionFabricante>> cb);

    @POST("/com.mim.entities.equipo/{nombre}")
    public void registerEquipment(@Path("nombre") String nombre, @Body Equipo equipo, Callback<Equipo> cb);

    @POST("/com.mim.entities.informacionfabricante/{equipo}")
    public void registerFactoryParams(@Path("equipo") int idEquipo, @Body List<InformacionFabricante> inf, Callback<InformacionFabricante> cb);
}