package data_activity_fragments;


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
import util.navigation.modelos.Equipo;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.adapter.DataAdapter;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView nombreField;
    private TextView idField;
    private TextView lineaField;

    private List<HistorialDetalles> dataList;
    private Equipo equipo;
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
    public static EquipmentFragment newInstance(String param1, String param2, Navigator navigator) {
        EquipmentFragment fragment = new EquipmentFragment();
        fragment.setNavigator(navigator);
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

        equipo = new Equipo("Motor", "40", "abcd123");

        dataList = new ArrayList<>();
        dataList.add(new HistorialDetalles("Amperaje", "45A"));
        dataList.add(new HistorialDetalles("Voltaje", "240V/440V"));
        dataList.add(new HistorialDetalles("Nema", "VII"));
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);

        widgetSetUp(view);

        recyclerSetUp(view);
        return view;
    }

    private void widgetSetUp(View view) {
        floatButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "dasdasdas", Toast.LENGTH_LONG).show();
                navigator.navigate("orden");
            }
        });

        nombreField = (TextView) view.findViewById(R.id.nombreField);
        nombreField.setText(equipo.getNombre());
        idField = (TextView) view.findViewById(R.id.idField);
        idField.setText(equipo.getId());
        lineaField = (TextView) view.findViewById(R.id.lineaField);
        lineaField.setText(equipo.getLinea().getNombre());
    }

    private void recyclerSetUp(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.generales);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new DataAdapter(dataList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }
}
