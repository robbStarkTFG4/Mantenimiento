package register_activity_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mantenimiento.mim.com.mantenimiento.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import server.RegisterAPI;
import util.navigation.Navigator;


public class ProduceCodeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Navigator navigator;

    final Handler h = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            showCode();
            return false;
        }
    });
    private ProgressDialog dialog;
    private TextView caja;
    private BarcodeConsumer consumer;

    public ProduceCodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProduceCodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProduceCodeFragment newInstance(String param1, String param2) {
        ProduceCodeFragment fragment = new ProduceCodeFragment();
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
        View view = inflater.inflate(R.layout.fragment_produce_code, container, false);

        caja = (TextView) view.findViewById(R.id.show_code);
        Button btn = (Button) view.findViewById(R.id.proceed);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProduceCodeFragment.this.navigator.navigate("lugar");
            }
        });

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Espera un momento...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        /*Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                    h.sendMessage(new Message());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();*/
      /*  RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(RegisterAPI.BASE_URL)
                .build();

        final RegisterAPI service =
                restAdapter.create(RegisterAPI.class);*/
        RegisterAPI service = RegisterAPI.Factory.getInstance();
        service.getBarCode(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                caja.setText(s);
                consumer.barCodeResult(s);
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                caja.setText("hubo algun error");
                caja.setTextColor(Color.RED);
            }
        });
        return view;
    }

    public void showCode() {
        dialog.dismiss();
        caja.setText("2131231231");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            navigator = (Navigator) context;
            consumer = (BarcodeConsumer) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Navigator");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigator = null;
        consumer = null;
    }

    public interface BarcodeConsumer {
        public void barCodeResult(String code);
    }

}
