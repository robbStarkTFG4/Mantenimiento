package server;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import util.navigation.modelos.Equipo;
import util.navigation.modelos.Foto;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.ListaNombreEquipos;
import util.navigation.modelos.Orden;

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

    // FOR RETRIEVE DATA
    @GET("/com.mim.entities.equipo/{code}")
    public void getEquipmentByCodeBar(@Path("code") String codigo, Callback<Equipo> cb);

    @GET("/com.mim.entities.informacionfabricante/factoryList/{id}")
    public void getFactoryList(@Path("id") int equipoId, Callback<List<InformacionFabricante>> cb);

    @GET("/com.mim.entities.historialdetalles/history/{id}")
    public void getHistorialDetalles(@Path("id") int idEquipo, Callback<List<HistorialDetalles>> cb);

    @GET("/com.mim.entities.listanombreequipos/nombre/{id}")
    public void getNombreEquipo(@Path("id") int id, Callback<ListaNombreEquipos> cb);

    // DATA PERSIST

    @POST("/com.mim.entities.orden/sube")
    public void persistOrder(@Body Orden orden, Callback<Orden> cb);

    @POST("/com.mim.entities.historialdetalles/lista/{orden}")
    public void persistHistoryList(@Path("orden") int orden, @Body List<HistorialDetalles> historyList, Callback<HistorialDetalles> cb);

    @POST("/com.mim.entities.fotos/objetos/{id}")
    public void persistPhotoObjects(@Path("id") int id, @Body List<Foto> list, Callback<Foto> cb);

    //upload picture
    @Multipart
    @POST("/com.mim.entities.fotos/prime")
    public Response uploadImage2(@Part("id") TypedString description, @Part("file") TypedFile imagen);
}