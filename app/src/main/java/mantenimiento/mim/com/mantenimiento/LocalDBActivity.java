package mantenimiento.mim.com.mantenimiento;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import data_activity_fragments.FotoDialogFragment;
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
import local_db_activity_fragments.TrabajoLocalFragment;
import local_db_activity_fragments.ValueDialogPortableFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.OrdenAPI;
import server.PhotoReportAPI;
import util.navigation.CompresConsumer;
import util.navigation.Navigator;
import util.navigation.OnclickLink;
import util.navigation.PortableDialogItem;
import util.navigation.SerialListHolder;
import util.navigation.async_tasks.CompresImages;
import util.navigation.async_tasks.CompresImagesLocal;
import util.navigation.async_tasks.ReportBuilder;
import util.navigation.async_tasks.Uploader;
import util.navigation.modelos.Foto;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.ListaNombreEquipos;
import util.navigation.modelos.Orden;

public class LocalDBActivity extends AppCompatActivity implements Navigator, OnclickLink
        , OrdenInfoFragment.OrdenConsumer, ServicioLocalFragment.HistoryConsumerLocal
        , CameraLocalFragment.PhotosConsumer, ValueDialogPortableFragment.PortableDialogConsumer
        , PortableDialogItem, FotoDialogFragment.DialogConsumer, CompresConsumer
        , TrabajoLocalFragment.PhotographicLocalConsumer, Uploader.UploaderConsumer {

    private Boolean action_mode = false;

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
    private List<FotoDB> blackList;
    private int idOrden;
    //End database objects

    /**
     * used in photographic
     */


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
            DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(this, "mimDb14", null);
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
        dataList = dao.queryBuilder().where(OrdenDBDao.Properties.Mostrar.eq(false)).list();
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
            case "trabajo":
                manager.beginTransaction().replace(R.id.content_local, TrabajoLocalFragment.newInstance(current, null)).addToBackStack(null).commit();
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
        if ((current.getNumeroOrden() != null) && (current.getPrioridad() != null)) {
            if (current.getPrioridad().equals("")) {
                fotoList = session.getFotoDBDao().queryBuilder().where(FotoDBDao.Properties.IdOrden.eq(current.getId())).list();
                navigate("trabajo");
            } else {
                current.getHistorialDetallesDBList();
                historial = session.getHistorialDetallesDBDao().queryBuilder().where(HistorialDetallesDBDao.Properties.OrdenId.eq(current.getId())).list();
                fotoList = session.getFotoDBDao().queryBuilder().where(FotoDBDao.Properties.IdOrden.eq(current.getId())).list();
                for (int i = 0; i < fotoList.size(); i++) {
                    FotoDB temp = fotoList.get(i);
                    temp.getOrdenDB();
                }
                navigate("info_orden");
            }
            //EquipoDB equi = current.getEquipoDB();
            //Log.d("EQUIPO_CODIGO", equi.getCodigoBarras());
            //current.setEquipoDB(equi);
        } else {
            fotoList = session.getFotoDBDao().queryBuilder().where(FotoDBDao.Properties.IdOrden.eq(current.getId())).list();
            navigate("trabajo");
        }
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

        Orden orden = new Orden(current);
        final ProgressDialog pg = new ProgressDialog(this);
        pg.setMessage("espera un momento...");
        pg.setCanceledOnTouchOutside(false);
        pg.show();
        if (!orden.getEquipoIdequipo().getNumeroEquipo().equals("n/a")) {
            OrdenAPI service = OrdenAPI.Factory.getInstance();
            service.persistOrder(orden).enqueue(new Callback<Orden>() {
                @Override
                public void onResponse(Call<Orden> call, Response<Orden> response) {
                    if (response.body() != null) {
                        upLoadHistoryDetails(response.body().getIdorden(), pg);
                    } else {
                        if (LocalDBActivity.this != null) {
                            pg.dismiss();
                            Toast.makeText(LocalDBActivity.this, "hubo algun error 1", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Orden> call, Throwable throwable) {
                    if (LocalDBActivity.this != null) {
                        pg.dismiss();
                        Toast.makeText(LocalDBActivity.this, "hubo algun error 1", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            PhotoReportAPI service = PhotoReportAPI.Factory.getInstance();
            service.persistOrderMode(orden.getEquipoIdequipo().getLugarIdlugar().getNombre(), orden).enqueue(new Callback<Orden>() {
                @Override
                public void onResponse(Call<Orden> call, Response<Orden> response) {
                    if (LocalDBActivity.this != null) {
                        if (response.body() != null) {
                            upLoadHistoryDetails(response.body().getIdorden(), pg);
                        } else {
                            if (LocalDBActivity.this != null) {
                                Toast.makeText(LocalDBActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Orden> call, Throwable throwable) {
                    if (LocalDBActivity.this != null) {
                        pg.dismiss();
                        Toast.makeText(LocalDBActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void upLoadHistoryDetails(final Integer idorden, final ProgressDialog pg) {
        List<HistorialDetalles> detalles = new ArrayList<>();
        for (int i = 0; i < historial.size(); i++) {
            HistorialDetallesDB his = historial.get(i);
            detalles.add(new HistorialDetalles(his.getParametro(), his.getValor()));
        }
        OrdenAPI service = OrdenAPI.Factory.getInstance();
        service.persistHistoryList(idorden, detalles).enqueue(new Callback<HistorialDetalles>() {
            @Override
            public void onResponse(Call<HistorialDetalles> call, Response<HistorialDetalles> response) {
                upLoadPicturesObjects(idorden, pg);
            }

            @Override
            public void onFailure(Call<HistorialDetalles> call, Throwable throwable) {
                if (LocalDBActivity.this != null) {
                    pg.dismiss();
                    Toast.makeText(LocalDBActivity.this, "hubo algun error 2", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void upLoadPicturesObjects(final int idorden, final ProgressDialog pg) {
        list = new ArrayList<>();
        for (int i = 0; i < fotoList.size(); i++) {
            FotoDB temp = fotoList.get(i);
            list.add(new Foto(temp.getArchivo(), temp.getTitulo(), temp.getDescripcion()));
        }
        if (list.size() > 0) {
            OrdenAPI service = OrdenAPI.Factory.getInstance();
            service.persistPhotoObjects(idorden, list).enqueue(new Callback<Foto>() {
                @Override
                public void onResponse(Call<Foto> call, Response<Foto> response) {
                    if (LocalDBActivity.this != null) {
                        pg.setMessage("comprimiendo imagenes.....");
                    }
                    LocalDBActivity.this.pg = pg;
                    fileUpload(idorden);
                }

                @Override
                public void onFailure(Call<Foto> call, Throwable throwable) {
                    if (LocalDBActivity.this != null) {
                        Toast.makeText(LocalDBActivity.this, "hubo algun error 3", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            //current.
            OrdenAPI service = OrdenAPI.Factory.getInstance();
            service.markOrder(idorden).enqueue(new Callback<Orden>() {
                @Override
                public void onResponse(Call<Orden> call, Response<Orden> response) {
                    current.setMostrar(true);
                    session.getOrdenDBDao().update(current);
                    pg.dismiss();
                    if (LocalDBActivity.this != null) {
                        Toast.makeText(LocalDBActivity.this, "Reporte subido exitosamente", Toast.LENGTH_SHORT).show();
                    }
                    closeService();
                }

                @Override
                public void onFailure(Call<Orden> call, Throwable throwable) {
                    if (LocalDBActivity.this != null) {
                        Toast.makeText(LocalDBActivity.this, "hubo algun error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void fileUpload(int idOrden) {
        this.idOrden = idOrden;
        if (list != null) {
            if (list.size() > 0) {
                CompresImages task = new CompresImages(this, 0);
                Foto[] array = new Foto[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = list.get(i);
                }
                task.execute(array);
            }
        }
    }

    @Override
    public void compresResult(boolean res, int codigo) {
        Log.d("comprees", "result: " + res + "  codigo: " + codigo);
        if (codigo == 0) {
            if (res) {
                sendToServer(idOrden);
            } else {
                Toast.makeText(this, "Fallo en compresion de imagenes", Toast.LENGTH_SHORT).show();
                Log.d("RESULTADO_COMPRESION: ", "fallo");
            }
        } else {
            if (res) {
                pg.dismiss();
                reportGen();
            } else {
                if (this != null) {
                    Toast.makeText(this, "hubo algun error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void sendToServer(int idOrden) {
        if (list != null) {
            if (list.size() > 0) {
                pg.setMessage("Subiendo imagenes...");
                Uploader task = new Uploader(this, 0);
                Foto[] array = new Foto[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = list.get(i);
                }
                task.execute(array);
            }
        }

    }

    @Override
    public void consumeUpload(boolean res, int codigo) {
        if (res) {
            OrdenAPI service = OrdenAPI.Factory.getInstance();
            service.markOrder(idOrden).enqueue(new Callback<Orden>() {
                @Override
                public void onResponse(Call<Orden> call, Response<Orden> response) {
                    current.setMostrar(true);
                    session.getOrdenDBDao().update(current);
                    pg.dismiss();
                    Toast.makeText(LocalDBActivity.this, "Reporte subido exitosamente", Toast.LENGTH_SHORT).show();
                    Log.d("RESULTADO_COMPRESION: ", "funciono");
                    closeService();
                }

                @Override
                public void onFailure(Call<Orden> call, Throwable throwable) {
                    if (LocalDBActivity.this != null) {
                        Toast.makeText(LocalDBActivity.this, "hubo algun error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            pg.dismiss();
            Toast.makeText(LocalDBActivity.this, "hubo algun error", Toast.LENGTH_SHORT).show();
        }
    }

    private void reportGen() {
        try {
            InputStream ims = getAssets().open("logo.png");
            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            ReportBuilder builder = new ReportBuilder(this, current, historial, fotoList, current.getEquipoDB(), current.getEquipoDB().getLugarDB(), stream.toByteArray());
            builder.execute();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

        if (blackList != null) {
            Log.d("HAY ELEMENTOS", "ELEMENTOS");
            if (blackList.size() > 0) {
                for (FotoDB ft : blackList) {
                    session.getFotoDBDao().delete(ft);
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
    public void setBlackList(List<FotoDB> blackList) {
        this.blackList = blackList;
    }

    @Override
    public List<FotoDB> getPhotosList() {
        return fotoList;
    }

    @Override
    public void setActionMode(boolean mode) {
        action_mode = mode;
    }

    @Override
    public boolean isActionMode() {
        return action_mode;
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

    @Override
    public void updateModel(String title, String descripcion, int posicion) {
        CameraLocalFragment frag = (CameraLocalFragment) getSupportFragmentManager().findFragmentByTag("fotLol");
        frag.editModel(title, descripcion, posicion);
    }

    @Override
    public void consumePhotoGraphic(OrdenDB orden) {
        Log.d("CODIGO_BARRAS", current.getEquipoDB().getCodigoBarras());
        Orden res = new Orden();
        res.setDescripcion(orden.getDescripcion());
        res.setEncargado(orden.getEncargado());
        res.setActividad(orden.getActividad());

        final ProgressDialog pg = new ProgressDialog(this);
        pg.setMessage("espera un momento...");
        pg.setCanceledOnTouchOutside(false);
        pg.show();

        PhotoReportAPI service = PhotoReportAPI.Factory.getInstance();
        service.persistOrder(orden.getEquipoDB().getLugarDB().getNombre(), res).enqueue(new Callback<Orden>() {
            @Override
            public void onResponse(Call<Orden> call, Response<Orden> response) {
                if (response.body() != null) {
                    upLoadPicturesObjects(response.body().getIdorden(), pg);
                } else {
                    if (LocalDBActivity.this != null) {
                        Toast.makeText(LocalDBActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Orden> call, Throwable throwable) {
                if (LocalDBActivity.this != null) {
                    pg.dismiss();
                    Toast.makeText(LocalDBActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void archivarPhotoGraphic(OrdenDB orden) {
        session.getOrdenDBDao().update(current);

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

        if (blackList != null) {
            Log.d("HAY ELEMENTOS", "ELEMENTOS");
            if (blackList.size() > 0) {
                for (FotoDB ft : blackList) {
                    session.getFotoDBDao().delete(ft);
                }
            }
        }
        closeService();
    }

    public void buildReportLocal() throws IOException {
        Log.d("init", "building....");

        if (fotoList != null) {
            Log.d("point", "list is not null....");
            if (fotoList.size() > 0) {
                pg = new ProgressDialog(this);
                pg.setMessage("comprimiendo imagenes....");
                pg.setCanceledOnTouchOutside(false);
                pg.show();
                Log.d("point", "launch task....");
                List<Foto> list = new ArrayList<>();
                for (int i = 0; i < fotoList.size(); i++) {
                    FotoDB temp = fotoList.get(i);
                    list.add(new Foto(temp.getArchivo(), temp.getTitulo(), temp.getDescripcion()));
                }
                CompresImagesLocal task = new CompresImagesLocal(this, 2);
                Foto[] array = new Foto[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = list.get(i);
                }
                task.execute(array);
            } else {
                reportGen();
            }
        } else {
            Log.d("point", "null list ....");
            reportGen();
        }
    }

    @Override
    public void onBackPressed() {
        if (action_mode) {
            action_mode = false;
            Log.d("MODO: ", "ACTION_MODE_ON");
            CameraLocalFragment frag = (CameraLocalFragment) getSupportFragmentManager().findFragmentByTag("fotLol");
            if (frag != null) {
                frag.closeActionMode();
            }
        } else {
            super.onBackPressed();
        }
    }


}


