package mantenimiento.mim.com.mantenimiento;

import android.app.ProgressDialog;
import android.content.Intent;
//import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import register_activity_fragments.ChooseLineFragment;
import register_activity_fragments.InfoFragment;
import register_activity_fragments.ProduceCodeFragment;
import register_activity_fragments.TypeFragment;
import register_activity_fragments.ValueDialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.RegisterAPI;
import util.navigation.Navigator;
import util.navigation.OnclickLink;
import util.navigation.SerialListHolder;
import util.navigation.modelos.Equipo;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.ListaNombreEquipos;

public class RegisterActivity extends AppCompatActivity implements Navigator, ValueDialogFragment.ValueDialogConsumer
        , OnclickLink, ProduceCodeFragment.BarcodeConsumer
        , ChooseLineFragment.LineConsumer, InfoFragment.RegisterConsumer {

    private ListaNombreEquipos nombreEquipo;
    private String barCode;
    private String lineName;

    private SerialListHolder holder;
    private int current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MultiDex.install(this);
        setContentView(R.layout.activity_register);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.register_content, ProduceCodeFragment.newInstance(null, null)).commit();
    }

    @Override
    public void navigate(String addres) {
        FragmentManager manager = getSupportFragmentManager();
        switch (addres) {
            case "lugar":
                manager.beginTransaction().replace(R.id.register_content, ChooseLineFragment.newInstance(null, null), "line")
                        .addToBackStack(null).commit();
                break;
            case "tipo":
                manager.beginTransaction().replace(R.id.register_content, TypeFragment.newInstance(null, null), "tip")
                        .addToBackStack(null).commit();
                break;
            case "info":
                //navigateToInf(manager);
                loadFactoryParams();
                break;
        }
    }

    private void navigateToInf(FragmentManager manager) {
        manager.beginTransaction().replace(R.id.register_content, InfoFragment.newInstance(nombreEquipo, holder), "infi")
                .addToBackStack(null).commit();
    }

    private void loadFactoryParams() {
        final ProgressDialog prog = new ProgressDialog(this);
        prog.setMessage("cargando...");
        prog.setCanceledOnTouchOutside(false);
        prog.show();
        RegisterAPI service = RegisterAPI.Factory.getInstance();
        service.registerFactoryParams(this.nombreEquipo.getIdlistaNombre()).enqueue(new Callback<List<InformacionFabricante>>() {
            @Override
            public void onResponse(Call<List<InformacionFabricante>> call, Response<List<InformacionFabricante>> response) {
               if(RegisterActivity.this!=null) {
                   prog.dismiss();
                   if (response.body() != null) {
                       holder = new SerialListHolder();
                       holder.setInformacionFabricantes(response.body());
                       navigateToInf(RegisterActivity.this.getSupportFragmentManager());
                   } else {
                       if (RegisterActivity.this != null) {
                           Toast.makeText(RegisterActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
                       }
                   }
               }
            }

            @Override
            public void onFailure(Call<List<InformacionFabricante>> call, Throwable throwable) {
                if (RegisterActivity.this != null) {
                    Toast.makeText(RegisterActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(String valor) {
        FragmentManager manager = getSupportFragmentManager();
        InfoFragment frag = (InfoFragment) manager.findFragmentByTag("infi");
        frag.setValor(valor, current);
    }

    @Override
    public void position(int pos) {
        this.current = pos;
    }

    @Override
    public void positionDialog(int pos) {
        FragmentManager manager = getSupportFragmentManager();
        ChooseLineFragment line = (ChooseLineFragment) manager.findFragmentByTag("line");
        line.setSelectedLine(pos);
    }

    /**
     * Tipo escogido
     *
     * @param tip
     */
    @Override
    public void tipoEquipo(ListaNombreEquipos tip) {
        this.nombreEquipo = tip;
    }

    /**
     * Codigo de barras obtenido desde el fragment "ProduceCodeFragment"
     *
     * @param code
     */
    @Override
    public void barCodeResult(String code) {
        this.barCode = code;
    }

    /**
     * nombre de la linea escogida
     *
     * @param name
     */
    @Override
    public void consumeLine(String name) {
        this.lineName = name;
    }

    @Override
    public void register(final List<InformacionFabricante> infoList, String numeroEquipo) {

        Equipo equipo = new Equipo();
        equipo.setNumeroEquipo(numeroEquipo);
        equipo.setCodigoBarras(barCode);
        equipo.setListaNombreEquiposIdlistaNombre(nombreEquipo.getIdlistaNombre());


        final ProgressDialog pg = new ProgressDialog(this);
        pg.setMessage("registrando espere....");
        pg.setCanceledOnTouchOutside(false);
        pg.show();

        RegisterAPI service = RegisterAPI.Factory.getInstance();
        service.registerEquipment(lineName,equipo).enqueue(new Callback<Equipo>() {
            @Override
            public void onResponse(Call<Equipo> call, Response<Equipo> response) {
                if(response.body()!=null) {
                    registratInfoFabricante(response.body().getIdequipo(), infoList, pg);
                }else{
                    if(RegisterActivity.this!=null) {
                        pg.dismiss();
                        Toast.makeText(RegisterActivity.this, "falla al registrar", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Equipo> call, Throwable throwable) {
                if(RegisterActivity.this!=null) {
                    pg.dismiss();
                    Toast.makeText(RegisterActivity.this, "falla al registrar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registratInfoFabricante(Integer idequipo, final List<InformacionFabricante> infoList, final ProgressDialog pg) {
        RegisterAPI service = RegisterAPI.Factory.getInstance();
        service.registerFactoryParams(idequipo,infoList).enqueue(new Callback<InformacionFabricante>() {
            @Override
            public void onResponse(Call<InformacionFabricante> call, Response<InformacionFabricante> response) {
                pg.dismiss();
                Toast.makeText(RegisterActivity.this, "registrado exitosamente: ", Toast.LENGTH_LONG).show();
                closeService();
            }

            @Override
            public void onFailure(Call<InformacionFabricante> call, Throwable throwable) {
                if(RegisterActivity.this!=null) {
                    pg.dismiss();
                    Toast.makeText(RegisterActivity.this, "falla al registrar inf", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void closeService() {
        Intent intent = new Intent(RegisterActivity.this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("codigo", barCode);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
