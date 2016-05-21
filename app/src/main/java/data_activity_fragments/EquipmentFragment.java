package data_activity_fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Navigator;
import util.navigation.SerialListHolder;
import util.navigation.modelos.Equipo;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.adapter.DataAdapter;
import util.navigation.modelos.InformacionFabricante;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EquipmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EquipmentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private Equipo equipo;
    private String mParam2;
    private List<InformacionFabricante> infList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView nombreField;
    private TextView idField;
    private TextView lineaField;

    private FloatingActionButton floatButton;
    private Navigator navigator;

    public EquipmentFragment() {
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
    public static EquipmentFragment newInstance(Equipo param1, String param2, SerialListHolder holder) {
        EquipmentFragment fragment = new EquipmentFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putSerializable(ARG_PARAM3, holder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            equipo = (Equipo) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            infList = ((SerialListHolder) getArguments().getSerializable(ARG_PARAM3)).getInformacionFabricantes();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);

        widgetSetUp(view);

        recyclerSetUp(view);
        return view;
    }

    private void testData() {
        equipo = new Equipo("Motor", "40", 2);

        infList = new ArrayList<>();
        infList.add(new InformacionFabricante("Amperaje", "45A"));
        infList.add(new InformacionFabricante("Voltaje", "240V/440V"));
        infList.add(new InformacionFabricante("Nema", "VII"));
    }

    private void widgetSetUp(View view) {
        floatButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.navigate("orden");
            }
        });

        nombreField = (TextView) view.findViewById(R.id.nombreField);
        nombreField.setText(mParam2);
        idField = (TextView) view.findViewById(R.id.idField);
        idField.setText(equipo.getNumeroEquipo());
        lineaField = (TextView) view.findViewById(R.id.lineaField);
        lineaField.setText(equipo.getLugarIdlugar().getNombre());
    }

    private void recyclerSetUp(View view) {

        //Toast.makeText(getContext(), infList.get(0).getParametro() + " " + infList.get(0).getValor(), Toast.LENGTH_LONG).show();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.generales);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new DataAdapter(infList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
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
