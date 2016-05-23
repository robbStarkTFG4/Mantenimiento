package util.navigation.async_tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import server.OrdenAPI;
import util.navigation.modelos.Foto;

/**
 * Created by marcoisaac on 5/20/2016.
 */
public class CompresImages extends AsyncTask<Foto, Void, Boolean> {


    public interface CompresConsumer {
        public void compresResult(boolean res);
    }

    private CompresConsumer consumer;

    public CompresImages(CompresConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    protected Boolean doInBackground(Foto... params) {
        OrdenAPI service = OrdenAPI.Factory.getInstance();
        for (Foto foto : params) {

            String ruta = foto.getArchivo();
            Bitmap bit = BitmapFactory.decodeFile(ruta);
            if (bit != null) {
                try {
                    File rep = new File(ruta);
                    OutputStream stream = new FileOutputStream(rep);
                    bit.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    stream.flush();
                    stream.close();
                    Log.d("ASYNC_TASK", rep.getName());
                    Response res = service.uploadImage2(new TypedString(rep.getName()), new TypedFile("image/jpeg", rep));
                    if (res != null) {
                        if (!(res.getStatus() == 204)) {
                            return false;
                        }
                    }
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        consumer.compresResult(aBoolean);
    }
}
