package mantenimiento.mim.com.mantenimiento;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import menu_inicio.MainMenuFragment;
import util.navigation.Navigator;
import util.navigation.modelos.ListaNombreEquipos;

public class Main extends AppCompatActivity implements Navigator {

    private View parent;

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
        menu.setNavigator(this);
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
