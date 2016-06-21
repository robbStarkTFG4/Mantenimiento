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
public interface PhotoReportAPI {
    //public static final String BASE_URL = "http://mantenimiento-contactres.rhcloud.com/MantenimientoRest/webresources";
    public static final String BASE_URL = "http://env-5002349.jl.serv.net.mx/rest/webresources";

    public class Factory {
        private static PhotoReportAPI service;

        public static PhotoReportAPI getInstance() {
            if (service == null) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(PhotoReportAPI.BASE_URL)
                        .build();

                final PhotoReportAPI service =
                        restAdapter.create(PhotoReportAPI.class);
                return service;
            } else {
                return service;
            }
        }
    }

    // DATA PERSIST

    @POST("/com.mim.entities.orden/reportphoto/{lugar}")
    public void persistOrder(@Path("lugar")String lugar,@Body Orden orden, Callback<Orden> cb);

    @POST("/com.mim.entities.fotos/objetos/{id}")
    public void persistPhotoObjects(@Path("id") int id, @Body List<Foto> list, Callback<Foto> cb);

    //upload picture
    @Multipart
    @POST("/com.mim.entities.fotos/prime")
    public Response uploadImage2(@Part("id") TypedString description, @Part("file") TypedFile imagen);
}