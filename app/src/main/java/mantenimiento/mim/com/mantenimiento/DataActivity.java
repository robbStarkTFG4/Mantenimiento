package mantenimiento.mim.com.mantenimiento;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import data_activity_fragments.BarcodeReaderFragment;
import data_activity_fragments.CameraFragment;
import data_activity_fragments.EquipmentFragment;
import data_activity_fragments.FotoDialogFragment;
import data_activity_fragments.ImageViewFragment;
import data_activity_fragments.OrdenFragment;
import data_activity_fragments.ServicioFragment;
import util.navigation.Navigator;
import util.navigation.modelos.Foto;
import util.navigation.modelos.ListaNombreEquipos;

public class DataActivity extends AppCompatActivity implements Navigator, FotoDialogFragment.DialogConsumer
        , CameraFragment.PhotosConsumer, ImageViewFragment.OnFragmentInteractionListener {

    private FragmentManager manager;
    private List<Foto> list;

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
        manager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                //manager.getFragment(null,"fot").r
            }
        });
        switch (addres) {
            case "equipo":
                manager.beginTransaction().replace(R.id.content, EquipmentFragment.newInstance("das", "dad", this)).commit();
                break;
            case "orden":
                manager.beginTransaction().replace(R.id.content, OrdenFragment.newInstance("das", "dad")).addToBackStack(null).commit();
                break;
            case "servicio":
                manager.beginTransaction().replace(R.id.content, ServicioFragment.newInstance("das", "dad", this)).addToBackStack(null).commit();
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
}
