package util.navigation.async_tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import server.OrdenAPI;
import util.navigation.CompresConsumer;
import util.navigation.modelos.Foto;

/**
 * Created by marcoisaac on 5/20/2016.
 */
public class CompresImagesLocal extends AsyncTask<Foto, Void, Boolean> {


    private int codigo;



    private CompresConsumer consumer;

    public CompresImagesLocal(CompresConsumer consumer, int codigo) {
        this.consumer = consumer;
        this.codigo = codigo;
    }

    @Override
    protected Boolean doInBackground(Foto... params) {
        OrdenAPI service = OrdenAPI.Factory.getInstance();
        for (Foto foto : params) {

            String ruta = foto.getArchivo();
            if (getFileSizeInMB(foto.getArchivo()) > 1) {
                Bitmap bit = BitmapFactory.decodeFile(ruta);
                if (bit != null) {
                    try {
                        File rep = new File(ruta);
                        OutputStream stream = new FileOutputStream(rep);
                        bit.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                        stream.flush();
                        stream.close();
                        Log.d("ASYNC_TASK", rep.getName());
                    } catch (IOException e) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (consumer != null) {
            consumer.compresResult(aBoolean, codigo);
        }
    }

    public float getFileSizeInMB(String fileName) {
        float ret = getFileSizeInBytes(fileName);
        ret = ret / (float) (1024 * 1024);
        return ret;
    }

    public long getFileSizeInBytes(String fileName) {
        long ret = 0;
        File f = new File(fileName);
        if (f.isFile()) {
            return f.length();
        } else if (f.isDirectory()) {
            File[] contents = f.listFiles();
            for (int i = 0; i < contents.length; i++) {
                if (contents[i].isFile()) {
                    ret += contents[i].length();
                } else if (contents[i].isDirectory()) {
                    ret += getFileSizeInBytes(contents[i].getPath());
                }
            }
        }
        return ret;
    }
}
