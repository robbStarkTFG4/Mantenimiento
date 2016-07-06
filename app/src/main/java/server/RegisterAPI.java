package server;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import util.navigation.modelos.Equipo;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.ListaNombreEquipos;

/**
 * Service used to register an equipment into the DB
 * Created by marcoisaac on 5/19/2016.
 */
public interface RegisterAPI {
    //public static final String BASE_URL = "http://mantenimiento-contactres.rhcloud.com/MantenimientoRest/webresources/";
    //public static final String BASE_URL = "http://env-5002349.jl.serv.net.mx/rest/webresources/";
    public static final String BASE_URL = "http://cemex-5266592.jl.serv.net.mx/rest/webresources/";

    public class Factory {
        private static RegisterAPI service;

        public static RegisterAPI getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(RegisterAPI.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                service = retrofit.create(RegisterAPI.class);
                return service;
            } else {
                return service;
            }
        }
    }


    @GET("generic")
    public Call<String> getBarCode();

    @GET("generic/valide/{codigo}")
    public Call<String> validateCode(@Path("codigo") String codigo);

    @GET("com.mim.entities.listanombreequipos")
    public Call<List<ListaNombreEquipos>> getListNombreEquipos();

    @GET("com.mim.entities.listanombreequipos/params/{id}")
    public Call<List<InformacionFabricante>> registerFactoryParams(@Path("id") int idlistaNombre);

    @POST("com.mim.entities.equipo/{nombre}")
    public Call<Equipo> registerEquipment(@Path("nombre") String nombre, @Body Equipo equipo);

    @POST("com.mim.entities.informacionfabricante/{equipo}")
    public Call<InformacionFabricante> registerFactoryParams(@Path("equipo") int idEquipo, @Body List<InformacionFabricante> inf);
}