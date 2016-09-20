package server;

import java.util.List;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import util.navigation.WorkServer;
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
    //public static final String BASE_URL = "http://mantenimiento-contactres.rhcloud.com/MantenimientoRest/webresources/";
    // public static final String BASE_URL = "http://env-5002349.jl.serv.net.mx/rest/webresources/";
    //public static final String BASE_URL = "http://cemex-5266592.jl.serv.net.mx/rest/webresources/";

    public class Factory {
        private static PhotoReportAPI service;

        public static PhotoReportAPI getInstance() {
            //if (service == null) {


            Retrofit retrofit = new Retrofit.Builder()
                    //.baseUrl(instance.getBASE_URL())
                    .baseUrl(WorkServer.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(PhotoReportAPI.class);
            return service;
            // } else {
            //     return service;
            // }

        }
    }

    // DATA PERSIST

    @POST("com.mim.entities.orden/reportphoto/{lugar}")
    public Call<Orden> persistOrder(@Path("lugar") String lugar, @Body Orden orden);

    @POST("com.mim.entities.orden/orderMode/{lugar}")
    public Call<Orden> persistOrderMode(@Path("lugar") String lugar, @Body Orden orden);

    @POST("com.mim.entities.fotos/objetos/{id}")
    public Call<Foto> persistPhotoObjects(@Path("id") int id, @Body List<Foto> list);

    //upload picture


    @POST("com.mim.entities.fotos/prime")
    public Call<ResponseBody> uploadImage2(@Part RequestBody description, @Part MultipartBody.Part imagen);

}