package mantenimiento.mim.com.mantenimiento;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import data_activity_fragments.CameraFragment;
import data_activity_fragments.FotoDialogFragment;
import de.greenrobot.dao.query.WhereCondition;
import local_Db.DaoMaster;
import local_Db.DaoSession;
import local_Db.EquipoDB;
import local_Db.FotoDB;
import local_Db.FotoDBDao;
import local_Db.HistorialDetallesDB;
import local_Db.HistorialDetallesDBDao;
import local_Db.OrdenDB;
import local_Db.OrdenDBDao;
import local_db_activity_fragments.CameraLocalFragment;
import local_db_activity_fragments.LocalOrdesFragment;
import local_db_activity_fragments.OrdenInfoFragment;
import local_db_activity_fragments.ServicioLocalFragment;
import local_db_activity_fragments.ValueDialogPortableFragment;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import server.OrdenAPI;
import util.navigation.Navigator;
import util.navigation.OnclickLink;
import util.navigation.PortableDialogItem;
import util.navigation.SerialListHolder;
import util.navigation.async_tasks.CompresImages;
import util.navigation.modelos.Foto;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.ListaNombreEquipos;
import util.navigation.modelos.Orden;

public class LocalDBActivity extends AppCompatActivity implements Navigator, OnclickLink
        , OrdenInfoFragment.OrdenConsumer, ServicioLocalFragment.HistoryConsumerLocal
        , CameraLocalFragment.PhotosConsumer, ValueDialogPortableFragment.PortableDialogConsumer
        , PortableDialogItem, FotoDialogFragment.DialogConsumer, CompresImages.CompresConsumer {

    private List<OrdenDB> dataList;
    private OrdenDB current;
    private List<HistorialDetallesDB> historial;

    // database objects
    private SQLiteDatabase db;
    private DaoMaster master;
    public DaoSession session;
    private List<FotoDB> fotoList;
    private int currentItem;
    private ProgressDialog pg;
    private List<Foto> list;
    //End database objects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_db);
        setUpDB();
        //testData();
        loadDataFromDB();
        startFragment();
    }

    private void setUpDB() {
        try {
            DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(this, "mimDb13", null);
            db = openHelper.getWritableDatabase();
            master = new DaoMaster(db);
            session = master.newSession();

        } catch (Exception e) {
            Log.d("d", e.getMessage());
        }
    }

    private void testData() {
        dataList = new ArrayList<>();
        OrdenDB ordenDB = new OrdenDB();
        ordenDB.setDescripcion("dasdsadasdsadas");
        ordenDB.setActividad("mecanica");
        ordenDB.setEncargado("dasdsa dsa dsaasdsadas asdas");
        ordenDB.setPrioridad("2-alta");
        ordenDB.setNumeroOrden("12345678912");

        EquipoDB equipo = new EquipoDB();
        equipo.setCodigoBarras("12345678");
        equipo.setNumeroEquipo("asdfbq12345678");

        ordenDB.setEquipoDB(equipo);

        List<HistorialDetallesDB> historyList = new ArrayList<>();
        HistorialDetallesDB history = new HistorialDetallesDB();
        history.setValor("dasdasdas");
        history.setParametro("dasdasdas");
        historyList.add(history);

        ordenDB.setHistoryDetallesDB(historyList);


        dataList.add(ordenDB);
    }

    private void loadDataFromDB() {
        OrdenDBDao dao = session.getOrdenDBDao();
        dataList = dao.loadAll();
        if (dataList != null) {
            if (dataList.size() > 0) {
                for (int i = 0; i < dataList.size(); i++) {
                    OrdenDB or = dataList.get(i);
                    or.getEquipoDB().getLugarDB();
                    //or.getFotoDBList();
                    //or.getHistorialDetallesDBList();
                }
            }
        }
    }

    private void startFragment() {
        SerialListHolder holder = new SerialListHolder();
        holder.setLocalOrderDB(dataList);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_local, LocalOrdesFragment.newInstance(holder, null)).commit();
    }

    @Override
    public void navigate(String addres) {
        FragmentManager manager = getSupportFragmentManager();
        switch (addres) {
            case "info_orden":
                manager.beginTransaction().replace(R.id.content_local, OrdenInfoFragment.newInstance(current, null)).addToBackStack(null).commit();
                break;
            case "servicio":
                SerialListHolder holder = new SerialListHolder();
                holder.setHistoryListDB(historial);
                manager.beginTransaction().replace(R.id.content_local, ServicioLocalFragment.newInstance(holder, null), "localito").addToBackStack(null).commit();
                break;
            case "fotos":
                manager.beginTransaction().replace(R.id.content_local, CameraLocalFragment.newInstance("dadas", "dasda", this), "fotLol").addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void tipoEquipo(ListaNombreEquipos tip) {
    }

    @Override
    public void position(int pos) {
        //Toast.makeText(this, dataList.get(pos).getHistorialDetallesDBList2().get(0).getParametro(), Toast.LENGTH_LONG).show();
        current = dataList.get(pos);
        current.getHistorialDetallesDBList();
        historial = session.getHistorialDetallesDBDao().queryBuilder().where(HistorialDetallesDBDao.Properties.OrdenId.eq(current.getId())).list();
        fotoList = session.getFotoDBDao().queryBuilder().where(FotoDBDao.Properties.IdOrden.eq(current.getId())).list();
        for (int i = 0; i < fotoList.size(); i++) {
            FotoDB temp = fotoList.get(i);
            temp.getOrdenDB();
        }
        //EquipoDB equi = current.getEquipoDB();
        //Log.d("EQUIPO_CODIGO", equi.getCodigoBarras());
        //current.setEquipoDB(equi);
        navigate("info_orden");
    }

    @Override
    public void positionDialog(int pos) {

    }

    @Override
    public void consumeOrden(OrdenDB orden) {
        this.current = orden;
    }

    @Override
    public void consume(List<HistorialDetallesDB> list) {
        this.historial = list;
    }

    @Override//convertir :  1 - orden
    public void upload() {
        Log.d("CODIGO_BARRAS", current.getEquipoDB().getCodigoBarras());
        Orden orden = new Orden(current);
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
                Toast.makeText(LocalDBActivity.this, "hubo algun error 1", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void upLoadHistoryDetails(final Integer idorden, final ProgressDialog pg) {
        List<HistorialDetalles> detalles = new ArrayList<>();
        for (int i = 0; i < historial.size(); i++) {
            HistorialDetallesDB his = historial.get(i);
            detalles.add(new HistorialDetalles(his.getParametro(), his.getValor()));
        }
        OrdenAPI service = OrdenAPI.Factory.getInstance();
        service.persistHistoryList(idorden, detalles, new Callback<HistorialDetalles>() {
            @Override
            public void success(HistorialDetalles o, Response response) {
                upLoadPicturesObjects(idorden, pg);
            }

            @Override
            public void failure(RetrofitError error) {
                pg.dismiss();
                Toast.makeText(LocalDBActivity.this, "hubo algun error 2", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void upLoadPicturesObjects(int idorden, final ProgressDialog pg) {
        list = new ArrayList<>();
        for (int i = 0; i < fotoList.size(); i++) {
            FotoDB temp = fotoList.get(i);
            list.add(new Foto(temp.getArchivo(), temp.getTitulo(), temp.getDescripcion()));
        }
        if (list != null) {
            OrdenAPI service = OrdenAPI.Factory.getInstance();
            service.persistPhotoObjects(idorden, list, new Callback<Foto>() {
                @Override
                public void success(Foto foto, Response response) {
                    pg.setMessage("subiendo imagenes.....");
                    LocalDBActivity.this.pg = pg;
                    fileUpload();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LocalDBActivity.this, "hubo algun error 3", Toast.LENGTH_SHORT).show();
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

    @Override
    public void update() {
        session.getOrdenDBDao().update(current);
        HistorialDetallesDBDao hisDao = session.getHistorialDetallesDBDao();
        for (int i = 0; i < historial.size(); i++) {
            HistorialDetallesDB temp = historial.get(i);
            hisDao.update(temp);
        }

        if (fotoList != null) {
            if (fotoList.size() > 0) {
                for (int i = 0; i < fotoList.size(); i++) {
                    FotoDB foto = fotoList.get(i);

                    if (foto.getId() != null) {
                        session.getFotoDBDao().update(foto);
                    } else {
                        foto.setOrdenDB(current);
                        session.getFotoDBDao().insert(foto);
                    }
                }
            }
        }
        closeService();
    }


    @Override
    public void setPhotosList(List<FotoDB> list) {
        this.fotoList = list;
    }

    @Override
    public List<FotoDB> getPhotosList() {
        return fotoList;
    }

    private void closeService() {
        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void consumeValue(String valor) {
        //Toast.makeText(this, "VALOR: " + valor + " POSICION: " + currentItem, Toast.LENGTH_SHORT).show();
        FragmentManager manager = getSupportFragmentManager();
        ServicioLocalFragment frag = (ServicioLocalFragment) manager.findFragmentByTag("localito");
        frag.setValue(valor, currentItem);
    }

    @Override
    public void itemPosition(int pos) {
        this.currentItem = pos;
        Log.d("LA POSICION ES:  ", String.valueOf(pos));
    }

    @Override
    public void consumeDialog(String title, String descripcion) {
        CameraLocalFragment cameraFragment = (CameraLocalFragment) getSupportFragmentManager().findFragmentByTag("fotLol");
        cameraFragment.setPhotoInfo(title, descripcion);
    }
}
