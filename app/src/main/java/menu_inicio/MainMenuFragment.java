package menu_inicio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import mantenimiento.mim.com.mantenimiento.FotoGraphActivity;
import mantenimiento.mim.com.mantenimiento.LocalDBActivity;
import mantenimiento.mim.com.mantenimiento.Main;
import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Modifier;
import util.navigation.Navigator;
import util.navigation.WorkServer;

/**
 * Created by marcoisaac on 5/10/2016.
 */
public class MainMenuFragment extends Fragment {

    private TextView leerCodigoText;
    private TextView registrar;
    private Navigator navigator;

    private String[] servers = {"cerveceria", "cemex"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);
        setUpControls(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        Modifier.changeMenuItemColor(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.archive:
                Intent intent = new Intent(getContext(), LocalDBActivity.class);
                startActivity(intent);
                break;
            case R.id.repFoto:
                startActivity(new Intent(getContext(), FotoGraphActivity.class));
                break;
            case R.id.create_order:
                navigator.navigate("newOrder");
                break;
            case R.id.choose_server:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Escoge servidor")
                        .setItems(servers, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainMenuFragment.this.getContext(), "posicion: " + which, Toast.LENGTH_SHORT).show();
                                WorkServer.POSICION = which;
                                WorkServer.BASE_URL = WorkServer.address[which];
                            }
                        });
                builder.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            navigator = (Navigator) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigator = null;
    }
}
