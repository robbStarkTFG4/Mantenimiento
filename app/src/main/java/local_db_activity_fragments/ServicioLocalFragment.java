package local_db_activity_fragments;


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
import android.widget.Toast;

import com.itextpdf.text.BadElementException;

import java.io.IOException;
import java.util.List;

import local_Db.HistorialDetallesDB;
import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Modifier;
import util.navigation.Navigator;
import util.navigation.PortableDialogItem;
import util.navigation.SerialListHolder;
import util.navigation.adapter.ServiceAdapter;
import util.navigation.adapter.ServiceLocalAdapter;
import util.navigation.modelos.HistorialDetalles;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicioLocalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicioLocalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam2;
    private Navigator navigator;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<HistorialDetallesDB> dataList;
    private HistoryConsumerLocal consumer;

    public ServicioLocalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServicioLocalFragment newInstance(SerialListHolder param1, String param2) {
        ServicioLocalFragment fragment = new ServicioLocalFragment();
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
            dataList = ((SerialListHolder) getArguments().getSerializable(ARG_PARAM1)).getHistoryListDB();
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.servicio_local_fragment, container, false);
        recyclerSetUp(view);
        return view;
    }


    private void recyclerSetUp(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.parametros_servicio_local);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ServiceLocalAdapter(dataList, (PortableDialogItem) getContext(), getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.servicio_local_menu, menu);
        Modifier.changeMenuItemColor(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.fotos_local:
                navigator.navigate("fotos");
                break;
            case R.id.enviar_local:
                AlertDialog alert = new AlertDialog.Builder(getContext())
                        .setMessage("estas seguro?").setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                consumer.consume(dataList);
                                consumer.upload();
                            }
                        }).create();
                alert.show();
                break;
            case R.id.archiva_service_local:
                AlertDialog alert2 = new AlertDialog.Builder(getContext())
                        .setMessage("estas seguro?").setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                consumer.consume(dataList);
                                consumer.update();
                            }
                        }).create();
                alert2.show();
                break;
            case R.id.build_service_local:
                consumer.consume(dataList);
                try {
                    consumer.buildReportLocal();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BadElementException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            navigator = (Navigator) context;
            consumer = (HistoryConsumerLocal) context;
        }
    }

    public void setValue(String valor, int currentItem) {
        dataList.get(currentItem).setValor(valor);
        mAdapter.notifyDataSetChanged();
    }

    public interface HistoryConsumerLocal {
        public void consume(List<HistorialDetallesDB> list);

        public void upload();

        public void update();

        public void buildReportLocal() throws IOException, BadElementException;
    }
}
