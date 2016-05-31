package fotographic_report_fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Modifier;
import util.navigation.Navigator;
import util.navigation.modelos.Lugar;
import util.navigation.modelos.Orden;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrabajoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrabajoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Navigator navigator;
    private EditText actividad;
    private EditText descripcion;
    private PhotographicConsumer consumer;
    private EditText encargado;


    public TrabajoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrabajoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrabajoFragment newInstance(String param1, String param2) {
        TrabajoFragment fragment = new TrabajoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trabajo, container, false);

        actividad = (EditText) view.findViewById(R.id.actividad_photographic);
        encargado = (EditText) view.findViewById(R.id.encargado_photographic);
        descripcion = (EditText) view.findViewById(R.id.descripcion_photographic);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            navigator = (Navigator) context;
            consumer = (PhotographicConsumer) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigator = null;
        consumer = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.photographic_menu, menu);
        Modifier.changeMenuItemColor(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fotos_photographic:
                navigator.navigate("fotillos");
                break;
            case R.id.archivar_photographic:
                AlertDialog alertArchivar = new AlertDialog.Builder(getContext())
                        .setMessage("estas seguro?").setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (validateForm()) {
                                    consumer.archivarPhotoGraphic(buildOrder());
                                } else {
                                    Toast.makeText(getContext(), "llena todos los datos", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).create();
                alertArchivar.show();
                break;
            case R.id.send_photographic:
                if (actividad.getText().length() > 0 && descripcion.getText().length() > 0) {
                    //createObjects and do stuff with them
                    AlertDialog alert = new AlertDialog.Builder(getContext())
                            .setMessage("estas seguro?")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (validateForm()) {
                                        consumer.consumePhotoGraphic(buildOrder());
                                    } else {
                                        Toast.makeText(getContext(), "llena todos los datos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("cancelar", null)
                            .create();
                    alert.show();
                    alert.setCanceledOnTouchOutside(false);
                } else {
                    Toast.makeText(getContext(), "llena todos los datos", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateForm() {
        return descripcion.getText().length() > 0 && actividad.getText().length() > 0 && encargado.getText().length() > 0;
    }


    @NonNull
    private Orden buildOrder() {
        Orden orden = new Orden();
        orden.setActividad(actividad.getText().toString());
        orden.setDescripcion(descripcion.getText().toString());
        orden.setEncargado(encargado.getText().toString());
        return orden;
    }

    public interface PhotographicConsumer {
        public void consumePhotoGraphic(Orden orden);

        public void archivarPhotoGraphic(Orden orden);
    }
}
