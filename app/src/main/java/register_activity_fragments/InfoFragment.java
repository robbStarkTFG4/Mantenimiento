package register_activity_fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    private String mParam2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView nombreField;
    private TextView idField;
    private TextView lineaField;

    private List<InformacionFabricante> dataList;
    private Equipo equipo;
    private Navigator navigator;
    private OnclickLink link;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        equipo = new Equipo("Motor", "40", "abcd123");


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
                Toast.makeText(getContext(), "registrar equipo", Toast.LENGTH_LONG).show();
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
        dataList.get(current).setValor(valor);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), valor, Toast.LENGTH_LONG).show();
    }
}
