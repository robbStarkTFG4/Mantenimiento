package register_activity_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mantenimiento.mim.com.mantenimiento.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import server.RegisterAPI;
import util.navigation.Navigator;
import util.navigation.OnclickLink;
import util.navigation.adapter.TipoAdapter;
import util.navigation.custom.recycler.RecyclerViewEmpty;
import util.navigation.modelos.ListaNombreEquipos;


public class TypeFragment extends Fragment implements OnclickLink {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Navigator navigator;

    private RecyclerViewEmpty mRecyclerView;
    private RecyclerViewEmpty.Adapter mAdapter;
    private RecyclerViewEmpty.LayoutManager mLayoutManager;

    private List<ListaNombreEquipos> list = new ArrayList<>();
    ;

    final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            dataSetUp();
            dialog.dismiss();
            return false;
        }
    });
    private ProgressDialog dialog;

    public TypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TypeFragment newInstance(String param1, String param2) {
        TypeFragment fragment = new TypeFragment();
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
        View view = inflater.inflate(R.layout.fragment_type, container, false);
        //dataSetUp();
        loadData();
        recyclerSetUp(view);
        return view;
    }

    private void loadData() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Cargando categorias...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

       /* Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                    handler.sendMessage(new Message());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();*/
        RegisterAPI service = RegisterAPI.Factory.getInstance();
        service.getListNombreEquipos(new Callback<List<ListaNombreEquipos>>() {
            @Override
            public void success(List<ListaNombreEquipos> listaNombreEquiposes, Response response) {
                if (list.size() > 0) {
                    list.clear();
                }

                for(int i=0;i<listaNombreEquiposes.size();i++){
                    list.add(listaNombreEquiposes.get(i));
                }
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
               Toast.makeText(getContext(),"hubo algun error",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void dataSetUp() {

        if (list.size() > 0) {
            list.clear();
        }

        list.add(new ListaNombreEquipos("motor"));
        list.add(new ListaNombreEquipos("cadena"));
        list.add(new ListaNombreEquipos("sprocket"));
        mAdapter.notifyDataSetChanged();
    }

    private void recyclerSetUp(View view) {
        mRecyclerView = (RecyclerViewEmpty) view.findViewById(R.id.tipos_list);
        mRecyclerView.setEmptyView((TextView) view.findViewById(R.id.empty_view_tipos));
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TipoAdapter(list, getContext(), this);

        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            navigator = (Navigator) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ValueDialogConsumer");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigator = null;
    }


    @Override
    public void position(int pos) {
        navigator.tipoEquipo(list.get(pos));
        navigator.navigate("info");
    }

    @Override
    public void positionDialog(int pos) {
    }


}
