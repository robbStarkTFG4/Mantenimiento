package menu_inicio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mantenimiento.mim.com.mantenimiento.Main;
import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Navigator;

/**
 * Created by marcoisaac on 5/10/2016.
 */
public class MainMenuFragment extends Fragment {

    private TextView leerCodigoText;
    private TextView registrar;
    private Navigator navigator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);
        setUpControls(view);
        return view;
    }

    private void setUpControls(View view) {
        leerCodigoText = (TextView) view.findViewById(R.id.leer_codigo);
        leerCodigoText.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.navigate("leerCodigo");
            }
        });

        registrar = (TextView) view.findViewById(R.id.registrar);
        registrar.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.navigate("registrar");
            }
        });
    }

    public void setNavigator(Main navigator) {
        this.navigator = navigator;
    }
}
