package data_activity_fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mantenimiento.mim.com.mantenimiento.R;
import menu_inicio.MainMenuFragment;
import util.navigation.Navigator;
import util.navigation.WorkServer;
import util.navigation.modelos.Orden;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrdenConsumer} interface
 * to handle interaction events.
 * Use the {@link OrdenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Navigator mListener;
    private OrdenConsumer orderConsumer;

    private String[] activitiesList = {"mantenimiento", "electrico", "electromecanico", "soldadura", "aislamiento", "construccion", "otro"};

    public OrdenFragment() {
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
    public static OrdenFragment newInstance(String param1, String param2) {
        OrdenFragment fragment = new OrdenFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orden, container, false);
        widgetSetUp(view);
        return view;
    }

    private void widgetSetUp(View view) {

        final EditText numeroOrden = (EditText) view.findViewById(R.id.numero_orden);
        if (mParam1 != null) {
            numeroOrden.setText(mParam1);
        }
        final EditText descripcion = (EditText) view.findViewById(R.id.descripcion_orden);
        final EditText encargado = (EditText) view.findViewById(R.id.encargado_orden);

        final Button actividad = (Button) view.findViewById(R.id.actividad_orden);

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

        final EditText prioridad = (EditText) view.findViewById(R.id.prioridad_orden);

        Button btn = (Button) view.findViewById(R.id.siguiente_orden);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroOrden.getText().length() > 0 && encargado.getText().length() > 0
                        && descripcion.getText().length() > 0 && !actividad.getText().equals("selecciona..")
                        && prioridad.getText().length() > 0) {
                    createOrden(numeroOrden.getText().toString(), descripcion.getText().toString(), encargado.getText().toString(), actividad.getText().toString(), prioridad.getText().toString());
                    mListener.navigate("servicio");
                } else {
                    Toast.makeText(getContext(), "llena todos los datos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createOrden(String numeroOrden, String descripcion, String encargado, String actividad, String prioridad) {
        Orden orden = new Orden();
        orden.setDescripcion(descripcion);
        orden.setEncargado(encargado);
        orden.setNumeroOrden(numeroOrden);
        orden.setActividad(actividad);
        orden.setPrioridad(prioridad);
        orderConsumer.consumeOrden(orden);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OrdenConsumer {
        // TODO: Update argument type and name
        public void consumeOrden(Orden orden);
    }
}
