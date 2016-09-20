package register_activity_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Navigator;
import util.navigation.OnclickLink;
import util.navigation.SerialListHolder;
import util.navigation.WorkServer;
import util.navigation.modelos.Lugar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Navigator} interface
 * to handle interaction events.
 * Use the {@link ChooseLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseLineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnclickLink link;
    private Navigator mListener;
    private List<Lugar> dataList;

    private LineDialogFragment dialog;
    private Button btn;
    private LineConsumer lineConsumer;
    private String nombre = null;
    private boolean control = false;

    public ChooseLineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseLineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooseLineFragment newInstance(String param1, String param2) {
        ChooseLineFragment fragment = new ChooseLineFragment();
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
        dataSetUp();
        View view = inflater.inflate(R.layout.fragment_choose_line, container, false);
        widgetSetUp(view);

        return view;
    }

    private void widgetSetUp(View view) {
        btn = (Button) view.findViewById(R.id.choose_line_register);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = LineDialogFragment.newInstance(new SerialListHolder(dataList), null);
                dialog.show(ChooseLineFragment.this.getFragmentManager(), "fotoRep");
            }
        });

        Button next = (Button) view.findViewById(R.id.siguiente_register);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (control) {
                    mListener.navigate("tipo");
                }
            }
        });
    }

    private void dataSetUp() {
        dataList = new ArrayList<>();
        //cerve

        if (WorkServer.POSICION == 0) {
            dataList.add(new Lugar("linea 10"));
            dataList.add(new Lugar("linea 30"));
            dataList.add(new Lugar("linea 40"));
            dataList.add(new Lugar("linea 50"));
            dataList.add(new Lugar("linea 60"));
            dataList.add(new Lugar("refrigeracion"));
            dataList.add(new Lugar("cocimientos"));
            dataList.add(new Lugar("fermentacion"));
            dataList.add(new Lugar("reposo"));
            dataList.add(new Lugar("fuerza motriz"));
            dataList.add(new Lugar("calderas"));
            dataList.add(new Lugar("adjunto liquido"));
            dataList.add(new Lugar("otro"));
        }

        if (WorkServer.POSICION == 1) {
            //cemex
            dataList.add(new Lugar("PD0665"));
            dataList.add(new Lugar("PD0300"));
            dataList.add(new Lugar("PD057"));
            dataList.add(new Lugar("PD442"));
            dataList.add(new Lugar("PD477"));
            dataList.add(new Lugar("PD327"));
            dataList.add(new Lugar("PD183"));
            dataList.add(new Lugar("PD268"));
            dataList.add(new Lugar("PD402"));
            dataList.add(new Lugar("PD471"));
            dataList.add(new Lugar("PD403"));
            dataList.add(new Lugar("PD294"));
            dataList.add(new Lugar("PD304"));
            dataList.add(new Lugar("PD109"));
            //
            dataList.add(new Lugar("PD0056"));
            dataList.add(new Lugar("PD0055"));
            dataList.add(new Lugar("PD0057"));
            dataList.add(new Lugar("PD0304"));
            dataList.add(new Lugar("PD0665"));
            dataList.add(new Lugar("PD0311"));
            dataList.add(new Lugar("PD0442"));
            dataList.add(new Lugar("PD0410"));
            dataList.add(new Lugar("PD0300"));
            dataList.add(new Lugar("PD0109"));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            mListener = (Navigator) context;
            lineConsumer = (LineConsumer) context;
            link = (OnclickLink) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Navigator");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        lineConsumer = null;
        link = null;
    }

    public void setSelectedLine(int selectedLine) {
        control = true;
        dialog.dismiss();
        nombre = dataList.get(selectedLine).getNombre();
        btn.setText(nombre);
        lineConsumer.consumeLine(nombre);
    }

    public interface LineConsumer {
        public void consumeLine(String name);
    }
}
