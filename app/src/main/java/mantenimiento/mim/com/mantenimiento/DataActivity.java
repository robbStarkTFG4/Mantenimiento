package mantenimiento.mim.com.mantenimiento;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import data_activity_fragments.BarcodeReaderFragment;
import data_activity_fragments.CameraFragment;
import data_activity_fragments.EquipmentFragment;
import data_activity_fragments.FotoDialogFragment;
import data_activity_fragments.ImageViewFragment;
import data_activity_fragments.OrdenFragment;
import data_activity_fragments.ServicioFragment;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import server.OrdenAPI;
import util.navigation.Navigator;
import util.navigation.SerialListHolder;
import util.navigation.async_tasks.CompresImages;
import util.navigation.modelos.Equipo;
import util.navigation.modelos.Foto;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.ListaNombreEquipos;
import util.navigation.modelos.Orden;

public class DataActivity extends AppCompatActivity implements Navigator, FotoDialogFragment.DialogConsumer
        , CameraFragment.PhotosConsumer, ImageViewFragment.OnFragmentInteractionListener, BarcodeReaderFragment.EquipmentConsumer
        , OrdenFragment.OrdenConsumer, ServicioFragment.HistoryConsumer, CompresImages.CompresConsumer {

    private FragmentManager manager;
    private List<Foto> list;

    private Equipo current;
    private List<InformacionFabricante> factoryList;
    private List<HistorialDetalles> historyList;
    private String nombreEquipo;
    private Orden orden;
    private ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        launchReader();
    }

    private void launchReader() {
        if (manager == null) {
            manager = getSupportFragmentManager();
        }
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content, BarcodeReaderFragment.newInstance("das", "dad", this)).commit();
    }

    @Override
    public void navigate(String addres) {
        if (manager == null) {
            manager = getSupportFragmentManager();
        }
        SerialListHolder holder = new SerialListHolder();
        switch (addres) {
            case "equipo":

                holder.setInformacionFabricantes(factoryList);
                manager.beginTransaction().replace(R.id.content, EquipmentFragment.newInstance(current, nombreEquipo, holder)).commit();
                break;
            case "orden":
                manager.beginTransaction().replace(R.id.content, OrdenFragment.newInstance("das", "dad")).addToBackStack(null).commit();
                break;
            case "servicio":
                holder.setHistoryList(historyList);
                manager.beginTransaction().replace(R.id.content, ServicioFragment.newInstance(holder, "dad")).addToBackStack(null).commit();
                break;
            case "fotos":
                manager.beginTransaction().replace(R.id.content, CameraFragment.newInstance("dadas", "dasda", this), "fot").addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void tipoEquipo(ListaNombreEquipos tip) {
    }

    @Override
    public void consumeDialog(String title, String descripcion) {
        CameraFragment cameraFragment = (CameraFragment) getSupportFragmentManager().findFragmentByTag("fot");
        cameraFragment.setPhotoInfo(title, descripcion);
    }

    @Override
    public void setPhotosList(List<Foto> list) {
        this.list = list;
    }

    @Override
    public List<Foto> getPhotosList() {
        return list;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void consumeEquipment(Equipo equipo) {
        this.current = equipo;
    }

    @Override
    public void consumeFactoryList(List<InformacionFabricante> list) {
        this.factoryList = list;
    }

    @Override
    public void consumeHistoryList(List<HistorialDetalles> list) {
        this.historyList = list;
    }

    @Override
    public void consumeNombreEquipo(String nombre) {
        this.nombreEquipo = nombre;
    }

    @Override
    public void consumeOrden(Orden orden) {
        this.orden = orden;
    }

    @Override
    public void consume(List<HistorialDetalles> list) {
        //this.historyList.clear();
        this.historyList = list;
        //Toast.makeText(this,historyList.get(0).getValor(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void upload() {
        orden.setEquipoIdequipo(current);
        final ProgressDialog pg = new ProgressDialog(this);
        pg.setMessage("espera un momento...");
        pg.setCanceledOnTouchOutside(false);
        pg.show();
        OrdenAPI service = OrdenAPI.Factory.getInstance();
        service.persistOrder(orden, new Callback<Orden>() {
            @Override
            public void success(Orden orden, Response response) {
                //pg.setMessage(String.valueOf(orden.getIdorden()));//aqui esta el id de la orden
                upLoadHistoryDetails(orden.getIdorden(), pg);
            }

            @Override
            public void failure(RetrofitError error) {
                pg.dismiss();
                Toast.makeText(DataActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void upLoadHistoryDetails(final int idorden, final ProgressDialog pg) {
        Log.d("ID_ORDEN", String.valueOf(idorden));
        OrdenAPI service = OrdenAPI.Factory.getInstance();
        service.persistHistoryList(idorden, historyList, new Callback<HistorialDetalles>() {
            @Override
            public void success(HistorialDetalles o, Response response) {
                upLoadPicturesObjects(idorden, pg);
            }

            @Override
            public void failure(RetrofitError error) {
                pg.dismiss();
                Toast.makeText(DataActivity.this, "hubo algun error history", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void upLoadPicturesObjects(int idorden, final ProgressDialog pg) {
        if (list != null) {
            OrdenAPI service = OrdenAPI.Factory.getInstance();
            service.persistPhotoObjects(idorden, list, new Callback<Foto>() {
                @Override
                public void success(Foto foto, Response response) {
                    pg.setMessage("subiendo imagenes.....");
                    DataActivity.this.pg = pg;
                    fileUpload();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(DataActivity.this, "hubo algun error fotos", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            pg.dismiss();
            Toast.makeText(this, "Reporte subido exitosamente", Toast.LENGTH_SHORT).show();
            closeService();
        }

    }

    private void fileUpload() {
        if (list != null) {
            if (list.size() > 0) {
                CompresImages task = new CompresImages(this);
                Foto[] array = new Foto[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = list.get(i);
                }
                task.execute(array);
            }
        }
    }

    @Override
    public void compresResult(boolean res) {
        if (res) {
            pg.dismiss();
            Toast.makeText(this, "Reporte subido exitosamente", Toast.LENGTH_SHORT).show();
            Log.d("RESULTADO_COMPRESION: ", "funciono");
            closeService();
        } else {
            Toast.makeText(this, "Fallo en subida de imagenes", Toast.LENGTH_SHORT).show();
            Log.d("RESULTADO_COMPRESION: ", "fallo");
        }
    }

    private void closeService() {
        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
