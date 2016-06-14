package mantenimiento.mim.com.mantenimiento;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import data_activity_fragments.CameraFragment;
import data_activity_fragments.FotoDialogFragment;
import fotographic_report_fragments.TrabajoFragment;
import local_Db.DaoMaster;
import local_Db.DaoSession;
import local_Db.EquipoDB;
import local_Db.EquipoDBDao;
import local_Db.FotoDB;
import local_Db.FotoDBDao;
import local_Db.HistorialDetallesDB;
import local_Db.HistorialDetallesDBDao;
import local_Db.LugarDB;
import local_Db.LugarDBDao;
import local_Db.OrdenDB;
import local_Db.OrdenDBDao;
import local_db_activity_fragments.CameraLocalFragment;
import register_activity_fragments.ChooseLineFragment;
import register_activity_fragments.LineDialogFragment;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import server.OrdenAPI;
import server.PhotoReportAPI;
import util.navigation.Navigator;
import util.navigation.OnclickLink;
import util.navigation.async_tasks.CompresImages;
import util.navigation.modelos.Equipo;
import util.navigation.modelos.Foto;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.ListaNombreEquipos;
import util.navigation.modelos.Lugar;
import util.navigation.modelos.Orden;

public class FotoGraphActivity extends AppCompatActivity implements Navigator, ChooseLineFragment.LineConsumer
        , OnclickLink, CameraFragment.PhotosConsumer, FotoDialogFragment.DialogConsumer, TrabajoFragment.PhotographicConsumer, CompresImages.CompresConsumer {

    private String lugar;
    private List<Foto> photoList;


    // database objects
    private SQLiteDatabase db;
    private DaoMaster master;
    public DaoSession session;
    private ProgressDialog pg;
    //End database objects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFragment();
        initDB();
    }

    private void initDB() {
        try {
            DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(this, "mimDb14", null);
            db = openHelper.getWritableDatabase();
            master = new DaoMaster(db);
            session = master.newSession();

        } catch (Exception e) {
            Log.d("d", e.getMessage());
        }
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main_foto_rep, ChooseLineFragment.newInstance(null, null), "fotoPlace").commit();
    }

    @Override
    public void navigate(String addres) {
        FragmentManager manager = getSupportFragmentManager();
        switch (addres) {
            case "tipo":
                manager.beginTransaction().replace(R.id.content_main_foto_rep, TrabajoFragment.newInstance(null, null)).addToBackStack(null).commit();
                break;
            case "fotillos":
                manager.beginTransaction().replace(R.id.content_main_foto_rep, CameraFragment.newInstance(null, null), "foro").addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CameraFragment.REQUEST_CAMERA_RESULT:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Cannot run application because camera service permission have not been granted", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void tipoEquipo(ListaNombreEquipos tip) {
    }

    @Override
    public void consumeLine(String name) {
        this.lugar = name;
    }

    @Override
    public void position(int pos) {
    }

    @Override
    public void positionDialog(int pos) {
        ((LineDialogFragment) getSupportFragmentManager().findFragmentByTag("fotoRep")).dismiss();
        ((ChooseLineFragment) getSupportFragmentManager().findFragmentByTag("fotoPlace")).setSelectedLine(pos);
    }

    @Override
    public void setPhotosList(List<Foto> list) {
        this.photoList = list;
    }

    @Override
    public List<Foto> getPhotosList() {
        return this.photoList;
    }

    @Override
    public void consumeDialog(String title, String descripcion) {
        CameraFragment cameraFragment = (CameraFragment) getSupportFragmentManager().findFragmentByTag("foro");
        cameraFragment.setPhotoInfo(title, descripcion);
    }

    @Override
    public void updateModel(String title, String descripcion, int posicion) {
    }

    @Override
    public void consumePhotoGraphic(Orden orden) {

        pg = new ProgressDialog(this);
        pg.setCanceledOnTouchOutside(false);
        pg.setMessage("espera un momento....");
        pg.show();

        PhotoReportAPI service = PhotoReportAPI.Factory.getInstance();
        service.persistOrder(lugar, orden, new Callback<Orden>() {
            @Override
            public void success(Orden orden, Response response) {
                //Toast.makeText(FotoGraphActivity.this, "exito: " + orden.getIdorden(), Toast.LENGTH_LONG).show();
                if (FotoGraphActivity.this != null) {
                    uploadPictures(orden.getIdorden(), pg);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (FotoGraphActivity.this != null) {
                    pg.dismiss();
                    Toast.makeText(FotoGraphActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void archivarPhotoGraphic(Orden orden) {

        //orden.setEquipoIdequipo(current);
        //orden.setHistorialDetallesList(historyList);
        // 1-lugar  2 - equipo 3 - orden  4 - historyList 5-fotos
        final LugarDB lu = new LugarDB();
        lu.setNombre(this.lugar);

        final EquipoDB equi = new EquipoDB();
        equi.setNumeroEquipo("n/a");
        equi.setCodigoBarras("n/a");
        equi.setListaNombreEquipoIdListaNombre(7);


        final List<FotoDB> fotoList = new ArrayList<>();

        if (photoList != null) {
            for (int i = 0; i < photoList.size(); i++) {
                Foto foto = photoList.get(i);

                FotoDB fotoDB = new FotoDB();
                fotoDB.setArchivo(foto.getArchivo());
                fotoDB.setDescripcion(foto.getDescripcion());
                fotoDB.setTitulo(foto.getTitulo());
                fotoList.add(fotoDB);
            }
        }

        //List<FotoDB> listDB;
        final OrdenDB ordenDB = orden.transform();

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                LugarDBDao lugarDao = session.getLugarDBDao();
                lugarDao.insert(lu);
                Log.d("LUGAR ID: ", String.valueOf(lu.getId()));

                EquipoDBDao equiDao = session.getEquipoDBDao();
                equi.setLugarDB(lu);
                equiDao.insert(equi);
                Log.d("EQUIPO ID: ", String.valueOf(equi.getId()));

                OrdenDBDao orDao = session.getOrdenDBDao();
                ordenDB.setEquipoDB(equi);
                orDao.insert(ordenDB);
                Log.d("ORDEN ID: ", String.valueOf(ordenDB.getId()));

                if (fotoList.size() > 0) {
                    FotoDBDao photoDao = session.getFotoDBDao();
                    for (int i = 0; i < fotoList.size(); i++) {
                        FotoDB fo = fotoList.get(i);
                        fo.setOrdenDB(ordenDB);
                        photoDao.insert(fo);
                    }

                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                closeService();
            }
        }.execute();
    }

    private void uploadPictures(Integer idorden, final ProgressDialog pg) {
        if (photoList != null) {
            OrdenAPI service = OrdenAPI.Factory.getInstance();
            service.persistPhotoObjects(idorden, photoList, new Callback<Foto>() {
                @Override
                public void success(Foto foto, Response response) {
                    pg.setMessage("subiendo imagenes.....");

                    fileUpload();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(FotoGraphActivity.this, "hubo algun error fotos", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            pg.dismiss();
            Toast.makeText(this, "Reporte subido exitosamente", Toast.LENGTH_SHORT).show();
            closeService();
        }
    }

    private void fileUpload() {
        if (photoList != null) {
            if (photoList.size() > 0) {
                CompresImages task = new CompresImages(this, 0);
                Foto[] array = new Foto[photoList.size()];
                for (int i = 0; i < photoList.size(); i++) {
                    array[i] = photoList.get(i);
                }
                task.execute(array);
            }
        }
    }

    @Override
    public void compresResult(boolean res, int codigo) {
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
