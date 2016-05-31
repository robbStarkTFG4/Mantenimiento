package local_db_activity_fragments;


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
import android.widget.EditText;
import android.widget.Toast;

import local_Db.OrdenDB;
import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Modifier;
import util.navigation.Navigator;
import util.navigation.modelos.Orden;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrabajoLocalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrabajoLocalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private OrdenDB mParam1;
    private Long mParam2;
    private Navigator navigator;
    private EditText actividad;
    private EditText descripcion;
    private PhotographicLocalConsumer consumer;
    private EditText encargado;


    public TrabajoLocalFragment() {
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
    public static TrabajoLocalFragment newInstance(OrdenDB param1, Long param2) {
        TrabajoLocalFragment fragment = new TrabajoLocalFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        //args.putLong(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (OrdenDB) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getLong(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trabajo_local, container, false);


        actividad = (EditText) view.findViewById(R.id.actividad_photographic_local);
        encargado = (EditText) view.findViewById(R.id.encargado_photographic_local);
        descripcion = (EditText) view.findViewById(R.id.descripcion_photographic_local);

        if (mParam1 != null) {
            actividad.setText(mParam1.getActividad());
            encargado.setText(mParam1.getEncargado());
            descripcion.setText(mParam1.getDescripcion());
        }


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            navigator = (Navigator) context;
            consumer = (PhotographicLocalConsumer) context;
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
                navigator.navigate("fotos");
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
    private OrdenDB buildOrder() {

        if (mParam1 == null) {
            mParam1 = new OrdenDB();
        }

        mParam1.setActividad(actividad.getText().toString());
        mParam1.setDescripcion(descripcion.getText().toString());
        mParam1.setEncargado(encargado.getText().toString());
        return mParam1;
    }

    public interface PhotographicLocalConsumer {
        /**
         * for server upload
         *
         * @param orden
         */
        public void consumePhotoGraphic(OrdenDB orden);

        /**
         * for local storage
         *
         * @param orden
         */
        public void archivarPhotoGraphic(OrdenDB orden);
    }
}
