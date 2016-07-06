package mantenimiento.mim.com.mantenimiento;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import data_activity_fragments.CameraFragment;
import data_activity_fragments.FotoDialogFragment;
import data_activity_fragments.OrdenFragment;
import data_activity_fragments.ServicioFragment;
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
import local_db_activity_fragments.ValueDialogPortableFragment;
import orden_mode_fragments.ProduceCodeORModeFragment;
import register_activity_fragments.ChooseLineFragment;
import register_activity_fragments.TypeFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.OrdenAPI;
import server.PhotoReportAPI;
import server.RegisterAPI;
import util.navigation.CompresConsumer;
import util.navigation.Modifier;
import util.navigation.Navigator;
import util.navigation.OnclickLink;
import util.navigation.PortableDialogItem;
import util.navigation.SerialListHolder;
import util.navigation.async_tasks.CompresImages;
import util.navigation.async_tasks.Uploader;
import util.navigation.modelos.Foto;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.ListaNombreEquipos;
import util.navigation.modelos.Orden;

public class OrderModeActivity extends AppCompatActivity implements Navigator,
        ProduceCodeORModeFragment.BarcodeOrderModeConsumer, ChooseLineFragment.LineConsumer, OnclickLink,
        OrdenFragment.OrdenConsumer, ServicioFragment.HistoryConsumer, PortableDialogItem, CameraFragment.PhotosConsumer
        , FotoDialogFragment.DialogConsumer, CompresConsumer, Uploader.UploaderConsumer, ValueDialogPortableFragment.PortableDialogConsumer {

    private String currentLine;
    private Orden ordenData;
    private FragmentManager manager;
    private ListaNombreEquipos nombreEquipo;
    private List<Foto> fotos;
    private List<HistorialDetalles> historyList;
    private ProgressDialog pg;
    private int idCurrentOrden;

    // database objects
    private SQLiteDatabase db;
    private DaoMaster master;
    public DaoSession session;
    private int currentPortablePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_mode);
        launchScanner();
        setUpDB();
        pg=new ProgressDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void setUpDB() {
        try {
            DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(this, "mimDb14", null);
            db = openHelper.getWritableDatabase();
            master = new DaoMaster(db);
            session = master.newSession();

        } catch (Exception e) {
            Log.d("d", e.getMessage());
        }
    }

    private void launchScanner() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_order_mode, new ProduceCodeORModeFragment()).commit();
    }


    @Override
    public void barCodeResult(String code) {

    }

    @Override
    public void navigate(String addres) {
        if (manager == null) {
            manager = getSupportFragmentManager();
        }

        switch (addres) {
            case "lugar":
                manager.beginTransaction().replace(R.id.content_order_mode, new ChooseLineFragment(), "place").addToBackStack(null).commit();
                break;
            case "tipo":
                manager.beginTransaction().replace(R.id.content_order_mode, OrdenFragment.newInstance(currentLine,null)).addToBackStack(null).commit();
                break;
            case "servicio":
                manager.beginTransaction().replace(R.id.content_order_mode, new TypeFragment()).addToBackStack(null).commit();
                break;
            case "info":
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("cargando parametros");
                dialog.show();
                RegisterAPI service = RegisterAPI.Factory.getInstance();
                service.registerFactoryParams(nombreEquipo.getIdlistaNombre()).enqueue(new Callback<List<InformacionFabricante>>() {
                    @Override
                    public void onResponse(Call<List<InformacionFabricante>> call, Response<List<InformacionFabricante>> response) {
                        if (response.body() != null) {
                            dialog.dismiss();
                            SerialListHolder holder = new SerialListHolder();
                            List<HistorialDetalles> data = new ArrayList<HistorialDetalles>();
                            Modifier.convertToHistory(response.body(), data);
                            holder.setHistoryList(data);
                            manager.beginTransaction().replace(R.id.content_order_mode, ServicioFragment.newInstance(holder, null),"serviNet").addToBackStack(null).commit();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(OrderModeActivity.this, "hubo algun error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<InformacionFabricante>> call, Throwable throwable) {
                        if (this != null) {
                            dialog.dismiss();
                            Toast.makeText(OrderModeActivity.this, "hubo algun error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "fotos":
                manager.beginTransaction().replace(R.id.content_order_mode, CameraFragment.newInstance(null, null), "kami").addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void tipoEquipo(ListaNombreEquipos tip) {
        this.nombreEquipo = tip;
    }

    @Override
    public void consumeLine(String name) {
        this.currentLine = name;
    }

    @Override
    public void position(int pos) {
    }

    @Override
    public void positionDialog(int pos) {
        ChooseLineFragment frag = (ChooseLineFragment) getSupportFragmentManager().findFragmentByTag("place");
        frag.setSelectedLine(pos);
    }

    @Override
    public void consumeOrden(Orden orden) {
        this.ordenData = orden;
    }

    @Override
    public void consume(List<HistorialDetalles> list) {
        this.historyList = list;
    }

    @Override
    public void upload() {


        pg.setMessage("espera un momento...");
        pg.setCanceledOnTouchOutside(false);
        pg.show();
        PhotoReportAPI service = PhotoReportAPI.Factory.getInstance();
        service.persistOrderMode(currentLine, ordenData).enqueue(new Callback<Orden>() {
            @Override
            public void onResponse(Call<Orden> call, Response<Orden> response) {
                if (OrderModeActivity.this != null) {
                    if (response.body() != null) {
                        upLoadHistoryDetails(response.body().getIdorden(), pg);
                    } else {
                        if (OrderModeActivity.this != null) {
                            Toast.makeText(OrderModeActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Orden> call, Throwable throwable) {
                if (OrderModeActivity.this != null) {
                    pg.dismiss();
                    Toast.makeText(OrderModeActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void upLoadHistoryDetails(final Integer idorden, final ProgressDialog pg) {
        Log.d("ID_ORDEN", String.valueOf(idorden));
        OrdenAPI service = OrdenAPI.Factory.getInstance();
        service.persistHistoryList(idorden, historyList).enqueue(new Callback<HistorialDetalles>() {
            @Override
            public void onResponse(Call<HistorialDetalles> call, Response<HistorialDetalles> response) {
                upLoadPicturesObjects(idorden, pg);
            }

            @Override
            public void onFailure(Call<HistorialDetalles> call, Throwable throwable) {
                if (OrderModeActivity.this != null) {
                    pg.dismiss();
                    Toast.makeText(OrderModeActivity.this, "hubo algun error history", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void upLoadPicturesObjects(final Integer idorden, final ProgressDialog pg) {
        if (fotos != null) {
            OrdenAPI service = OrdenAPI.Factory.getInstance();
            service.persistPhotoObjects(idorden, fotos).enqueue(new Callback<Foto>() {
                @Override
                public void onResponse(Call<Foto> call, Response<Foto> response) {
                    pg.setMessage("comprimiendo imagenes.....");
                    OrderModeActivity.this.pg = pg;
                    fileUpload(idorden);
                }

                @Override
                public void onFailure(Call<Foto> call, Throwable throwable) {
                    if (OrderModeActivity.this != null) {
                        Toast.makeText(OrderModeActivity.this, "hubo algun error fotos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {

            OrdenAPI service = OrdenAPI.Factory.getInstance();
            service.markOrder(idorden).enqueue(new Callback<Orden>() {
                @Override
                public void onResponse(Call<Orden> call, Response<Orden> response) {
                    pg.dismiss();
                    Toast.makeText(OrderModeActivity.this, "Reporte subido exitosamente", Toast.LENGTH_SHORT).show();
                    closeService();
                }

                @Override
                public void onFailure(Call<Orden> call, Throwable throwable) {
                    if (OrderModeActivity.this != null) {
                        Toast.makeText(OrderModeActivity.this, "hubo algun error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void fileUpload(int idOrden) {
        this.idCurrentOrden = idOrden;
        if (fotos != null) {
            if (fotos.size() > 0) {
                CompresImages task = new CompresImages(this, 0);
                Foto[] array = new Foto[fotos.size()];
                for (int i = 0; i < fotos.size(); i++) {
                    array[i] = fotos.get(i);
                }
                task.execute(array);
            }
        }
    }

    private void closeService() {
        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void archiveReport() {

        //orden.setEquipoIdequipo(current);
        //orden.setHistorialDetallesList(historyList);
        // 1-lugar  2 - equipo 3 - orden  4 - historyList 5-fotos
        final LugarDB lu = new LugarDB();
        lu.setNombre(currentLine);

        final EquipoDB equi = new EquipoDB();
        equi.setNumeroEquipo("n/a");
        equi.setCodigoBarras("n/a");
        equi.setListaNombreEquipoIdListaNombre(7);

        final List<HistorialDetallesDB> listHis = new ArrayList<>();

        for (int i = 0; i < historyList.size(); i++) {
            HistorialDetalles his = historyList.get(i);

            HistorialDetallesDB temp = new HistorialDetallesDB();
            temp.setValor(his.getValor());
            temp.setParametro(his.getParametro());
            listHis.add(temp);
        }

        final List<FotoDB> fotoList = new ArrayList<>();

        if (fotos != null) {
            for (int i = 0; i < fotos.size(); i++) {
                Foto foto = fotos.get(i);

                FotoDB fotoDB = new FotoDB();
                fotoDB.setArchivo(foto.getArchivo());
                fotoDB.setDescripcion(foto.getDescripcion());
                fotoDB.setTitulo(foto.getTitulo());
                fotoList.add(fotoDB);
            }
        }

        //List<FotoDB> listDB;
        final OrdenDB ordenDB = ordenData.transform();

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


                HistorialDetallesDBDao hisDao = session.getHistorialDetallesDBDao();
                for (int i = 0; i < listHis.size(); i++) {
                    HistorialDetallesDB his = listHis.get(i);
                    his.setOrdenDB(ordenDB);
                    hisDao.insert(his);
                    Log.d("HISTORIAL ID: ", String.valueOf(his.getId()) + " PARAMETRO: " + his.getParametro());
                }

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

    @Override
    public void itemPosition(int pos) {
        currentPortablePos=pos;
    }

    @Override
    public void setPhotosList(List<Foto> list) {
        this.fotos = list;
    }

    @Override
    public List<Foto> getPhotosList() {
        return this.fotos;
    }

    @Override
    public void consumeDialog(String title, String descripcion) {
        CameraFragment cameraFragment = (CameraFragment) getSupportFragmentManager().findFragmentByTag("kami");
        cameraFragment.setPhotoInfo(title, descripcion);
    }

    @Override
    public void updateModel(String title, String descripcion, int posicion) {
    }

    @Override
    public void compresResult(boolean res, int codigo) {
        if (res) {
            sendToServer(idCurrentOrden);
        } else {
            Toast.makeText(this, "Fallo en compresion", Toast.LENGTH_SHORT).show();
            Log.d("RESULTADO_COMPRESION: ", "fallo");
        }
    }

    private void sendToServer(int idCurrentOrden) {

        if (fotos != null) {
            if (fotos.size() > 0) {
                pg.setMessage("Subiendo imagenes...");
                Uploader task = new Uploader(this, 0);
                Foto[] array = new Foto[fotos.size()];
                for (int i = 0; i < fotos.size(); i++) {
                    array[i] = fotos.get(i);
                }
                task.execute(array);
            }
        }
    }

    @Override
    public void consumeUpload(boolean res, int codigo) {
        if (res) {
            OrdenAPI service = OrdenAPI.Factory.getInstance();
            service.markOrder(idCurrentOrden).enqueue(new Callback<Orden>() {
                @Override
                public void onResponse(Call<Orden> call, Response<Orden> response) {
                    pg.dismiss();
                    Toast.makeText(OrderModeActivity.this, "Reporte procesado", Toast.LENGTH_SHORT).show();
                    Log.d("RESULTADO_COMPRESION: ", "funciono");
                    closeService();
                    // Toast.makeText(DataActivity.this, "Subiendo imagenes...", Toast.LENGTH_SHORT).show();
                    //sendToServer(DataActivity.this.idCurrentOrden);
                }

                @Override
                public void onFailure(Call<Orden> call, Throwable throwable) {
                    if (OrderModeActivity.this != null) {
                        Toast.makeText(OrderModeActivity.this, "hubo algun error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            pg.dismiss();
            Toast.makeText(OrderModeActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void consumeValue(String valor) {
        FragmentManager manager = getSupportFragmentManager();
        ServicioFragment serv = (ServicioFragment) manager.findFragmentByTag("serviNet");
        serv.setValue(valor, currentPortablePos);
    }
}
