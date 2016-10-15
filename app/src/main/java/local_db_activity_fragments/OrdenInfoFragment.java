package local_db_activity_fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import local_Db.OrdenDB;
import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Navigator;
import util.navigation.modelos.Orden;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrdenConsumer} interface
 * to handle interaction events.
 * Use the {@link OrdenInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdenInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private OrdenDB mParam1;
    private String mParam2;

    private Navigator mListener;
    private OrdenConsumer orderConsumer;

    private String[] activitiesList = {"mantenimiento", "electrico", "electromecanico", "soldadura", "aislamiento", "construccion", "otro"};

    public OrdenInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdenInfoFragment newInstance(OrdenDB param1, String param2) {
        OrdenInfoFragment fragment = new OrdenInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (OrdenDB) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.info_orden_local, container, false);
        widgetSetUp(view);
        return view;
    }

    private void widgetSetUp(View view) {

        final EditText numeroOrden = (EditText) view.findViewById(R.id.numero_orden_loc);
        final EditText descripcion = (EditText) view.findViewById(R.id.descripcion_orden_local);
        final EditText encargado = (EditText) view.findViewById(R.id.encargado_orden_local);

        final Button actividad = (Button) view.findViewById(R.id.actividad_orden_local);
        actividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Escoge actividad")
                        .setItems(activitiesList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                actividad.setText(activitiesList[which]);
                            }
                        });
                builder.create().show();
            }
        });

        final EditText prioridad = (EditText) view.findViewById(R.id.prioridad_orden_loc);

        Button btn = (Button) view.findViewById(R.id.siguiente_orden_local);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroOrden.getText().length() > 0 && encargado.getText().length() > 0
                        && descripcion.getText().length() > 0 && actividad.getText().length() > 0
                        && prioridad.getText().length() > 0) {
                    createOrden(numeroOrden.getText().toString(), descripcion.getText().toString(), encargado.getText().toString(), actividad.getText().toString(), prioridad.getText().toString());
                    mListener.navigate("servicio");
                } else {
                    Toast.makeText(getContext(), "llena todos los datos", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (mParam1 != null) {
            numeroOrden.setText(mParam1.getNumeroOrden());
            descripcion.setText(mParam1.getDescripcion());
            encargado.setText(mParam1.getEncargado());
            actividad.setText(mParam1.getActividad());
            prioridad.setText(mParam1.getPrioridad());
        }
    }

    private void createOrden(String numeroOrden, String descripcion, String encargado, String actividad, String prioridad) {

        mParam1.setDescripcion(descripcion);
        mParam1.setEncargado(encargado);
        mParam1.setNumeroOrden(numeroOrden);
        mParam1.setActividad(actividad);
        mParam1.setPrioridad(prioridad);
        orderConsumer.consumeOrden(mParam1);
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            mListener = (Navigator) context;
            orderConsumer = (OrdenConsumer) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Navigator");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        orderConsumer = null;
    }

    public interface OrdenConsumer {
        // TODO: Update argument type and name
        public void consumeOrden(OrdenDB orden);
    }
}
