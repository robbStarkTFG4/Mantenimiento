package server;

import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
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
    public static final String BASE_URL = "http://mantenimiento-contactres.rhcloud.com/MantenimientoRest/webresources/";
     //public static final String BASE_URL = "http://env-5002349.jl.serv.net.mx/rest/webresources/";

    public class Factory {
        private static OrdenAPI service;

        public static OrdenAPI getInstance() {
            if (service == null) {

                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(OrdenAPI.BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                service = retrofit.create(OrdenAPI.class);
                return service;
            } else {
                return service;
            }
        }
    }

    // FOR RETRIEVE DATA
    @GET("com.mim.entities.equipo/{code}")
    public Call<Equipo> getEquipmentByCodeBar(@Path("code") String codigo);

    @GET("com.mim.entities.informacionfabricante/factoryList/{id}")
    public Call<List<InformacionFabricante>> getFactoryList(@Path("id") int equipoId);

    @GET("com.mim.entities.historialdetalles/history/{id}")
    public Call<List<HistorialDetalles>> getHistorialDetalles(@Path("id") int idEquipo);

    @GET("com.mim.entities.listanombreequipos/nombre/{id}")
    public Call<ListaNombreEquipos> getNombreEquipo(@Path("id") int id);

    // DATA PERSIST

    @POST("com.mim.entities.orden/sube")
    public Call<Orden> persistOrder(@Body Orden orden);

    @GET("com.mim.entities.orden/mark/{numero}")
    public Call<Orden> markOrder(@Path("numero") int id);

    @POST("com.mim.entities.historialdetalles/lista/{orden}")
    public Call<HistorialDetalles> persistHistoryList(@Path("orden") int orden, @Body List<HistorialDetalles> historyList);

    @POST("com.mim.entities.fotos/objetos/{id}")
    public Call<Foto> persistPhotoObjects(@Path("id") int id, @Body List<Foto> list);

    //upload picture
    @Multipart
    @POST("com.mim.entities.fotos/prime")
    public Call<ResponseBody> uploadImage2(@Part("id") String id, @Part("file") RequestBody  imagen);
}