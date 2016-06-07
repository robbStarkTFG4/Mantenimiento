package util.navigation.async_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import local_Db.EquipoDB;
import local_Db.FotoDB;
import local_Db.HistorialDetallesDB;
import local_Db.LugarDB;
import local_Db.OrdenDB;
import report_generator.ReporteEnvasado;

/**
 * Created by marcoisaac on 6/7/2016.
 */
public class ReportBuilder extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private OrdenDB orden;
    private List<HistorialDetallesDB> observaciones;
    private List<FotoDB> fotos;
    private EquipoDB equipo;
    private LugarDB lugar;
    private byte[] imageBytes;

    public ReportBuilder(Context context, OrdenDB orden, List<HistorialDetallesDB> observaciones, List<FotoDB> fotos, EquipoDB equipo, LugarDB lugar, byte[] imageBytes) throws IOException, FileNotFoundException, DocumentException {
        this.orden = orden;
        this.equipo = equipo;
        this.lugar = lugar;
        this.fotos = fotos;
        this.imageBytes = imageBytes;
        this.observaciones = observaciones;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            new ReporteEnvasado(orden, observaciones, fotos, equipo, lugar, imageBytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            Toast.makeText(context, "reporte creado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "hubo algun error", Toast.LENGTH_LONG).show();
        }
    }
}
