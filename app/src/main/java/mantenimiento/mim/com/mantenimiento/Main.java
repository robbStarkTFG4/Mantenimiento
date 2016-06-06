package mantenimiento.mim.com.mantenimiento;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import menu_inicio.MainMenuFragment;
import util.navigation.Navigator;
import util.navigation.modelos.ListaNombreEquipos;

public class Main extends AppCompatActivity implements Navigator {

    private View parent;
    private String codigo;

    @Override
    protected void onStart() {
        super.onStart();
        /*if (getIntent().getStringExtra("codigo") != null) {
            this.codigo = getIntent().getStringExtra("codigo");
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("continuar con reporte?").setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Main.this, codigo, Toast.LENGTH_SHORT).show();
                            getIntent().removeExtra("codigo");
                            Intent intent = new Intent(Main.this, DataActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("codigo", codigo);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }).create();
            dialog.show();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        parent = findViewById(R.id.main_content);
        launchMainMenu();
    }

    private void launchMainMenu() {
        FragmentManager manager = getSupportFragmentManager();
        MainMenuFragment menu = new MainMenuFragment();
        manager.beginTransaction().replace(R.id.main_content, menu, "menu").commit();
    }


    @Override
    public void navigate(String addres) {
        Intent intent;
        switch (addres) {
            case "registrar":
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case "leerCodigo":
                intent = new Intent(this, DataActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void tipoEquipo(ListaNombreEquipos tip) {
    }
}
