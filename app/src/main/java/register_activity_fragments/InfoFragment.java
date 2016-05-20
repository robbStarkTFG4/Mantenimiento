package register_activity_fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Navigator;
import util.navigation.OnclickLink;
import util.navigation.SerialListHolder;
import util.navigation.adapter.RegisterAdapter;
import util.navigation.modelos.Equipo;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.ListaNombreEquipos;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ListaNombreEquipos mParam1;
    private List<InformacionFabricante> dataList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText numeroField;


    private Equipo equipo = new Equipo();
    private Navigator navigator;
    private OnclickLink link;
    private RegisterConsumer regCon;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EquipmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(ListaNombreEquipos param1, SerialListHolder param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (ListaNombreEquipos) getArguments().getSerializable(ARG_PARAM1);
            dataList = ((SerialListHolder) getArguments().getSerializable(ARG_PARAM2)).getInformacionFabricantes();
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigator = (Navigator) context;
        link = (OnclickLink) context;
        regCon = (RegisterConsumer) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        List<InformacionFabricante> filtered = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            InformacionFabricante gen = dataList.get(i);
            // if (gen.isShow()) {
            filtered.add(gen);
            // }
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.register_equipment, container, false);

        widgetSetUp(view);

        recyclerSetUp(view, filtered);
        return view;
    }

    private void testData() {
        dataList = new ArrayList<>();
        dataList.add(new InformacionFabricante("Amperaje"));
        dataList.add(new InformacionFabricante("Voltaje"));
        dataList.add(new InformacionFabricante("Res"));
        dataList.add(new InformacionFabricante("Nema"));
        dataList.add(new InformacionFabricante("Nema"));
        dataList.add(new InformacionFabricante("Nema"));
        dataList.add(new InformacionFabricante("Nema"));
        dataList.add(new InformacionFabricante("Nema"));
    }

    private void widgetSetUp(View view) {
        numeroField = (EditText) view.findViewById(R.id.info_numero_equipo);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.register_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.enviar_register:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Warn");
                builder.setMessage("Registrar equipo?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (numeroField.getText().length() > 0) {
                            regCon.register(dataList, numeroField.getText().toString());
                        } else {
                            Toast.makeText(InfoFragment.this.getContext(), "escribe numero equipo", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void recyclerSetUp(View view, List<InformacionFabricante> filtered) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.generales_register);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RegisterAdapter(filtered, getFragmentManager(), link);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public void setValor(String valor, int current) {
        //para cerrar keyboard
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // end cerrar
        dataList.get(current).setValor(valor);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), valor, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface RegisterConsumer {
        public void register(List<InformacionFabricante> infoList, String numeroEquipo);
    }
}
