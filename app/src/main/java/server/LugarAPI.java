package server;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import util.navigation.WorkServer;
import util.navigation.modelos.Lugar;

/**
 * Created by marcoisaac on 9/21/2016.
 */

public interface LugarAPI {
    public class Factory {
        private static LugarAPI service;

        public static LugarAPI getInstance() {
            //if (service == null) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();


            Retrofit retrofit = new Retrofit.Builder()
                    //.baseUrl(instance.getBASE_URL())
                    .baseUrl(WorkServer.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(LugarAPI.class);
            return service;
            // } else {
            // return service;
            //}
        }
    }

    @GET("com.mim.entities.lugar")
    public Call<List<Lugar>> getLugaresList();
}
