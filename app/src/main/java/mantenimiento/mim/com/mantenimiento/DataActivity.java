package mantenimiento.mim.com.mantenimiento;

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
import util.navigation.Navigator;
import util.navigation.SerialListHolder;
import util.navigation.modelos.Equipo;
import util.navigation.modelos.Foto;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.ListaNombreEquipos;
import util.navigation.modelos.Orden;

public class DataActivity extends AppCompatActivity implements Navigator, FotoDialogFragment.DialogConsumer
        , CameraFragment.PhotosConsumer, ImageViewFragment.OnFragmentInteractionListener, BarcodeReaderFragment.EquipmentConsumer
        , OrdenFragment.OrdenConsumer, ServicioFragment.HistoryConsumer {

    private FragmentManager manager;
    private List<Foto> list;

    private Equipo current;
    private List<InformacionFabricante> factoryList;
    private List<HistorialDetalles> historyList;
    private String nombreEquipo;
    private Orden orden;

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
        this.historyList.clear();
        this.historyList = list;
    }

    @Override
    public void upload() {

    }
}
