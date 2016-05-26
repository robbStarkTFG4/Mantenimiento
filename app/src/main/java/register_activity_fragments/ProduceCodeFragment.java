package register_activity_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
    private EditText caja;
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produce_code, container, false);

        caja = (EditText) view.findViewById(R.id.show_code);
        Button btn = (Button) view.findViewById(R.id.proceed);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCode(caja.getText().toString());
            }
        });

        Button btnScan = (Button) view.findViewById(R.id.scan_code);
        btnScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                readCode();
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
        generateCodeFromDB();
        return view;
    }

    private void readCode() {
        //launch camera
        IntentIntegrator integrator = new IntentIntegrator((Activity) getContext());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.forSupportFragment(this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String toast;
        if (result != null) {
            if (result.getContents() == null) {
                toast = "Cancelado";
            } else {
                String res = result.getContents();
                caja.setText(res);
            }
        }
    }

    private void validateCode(String code) {
        final ProgressDialog dialog2 = new ProgressDialog(this.getContext());
        dialog2.setMessage("validando codigo....");
        dialog2.show();
        RegisterAPI service = RegisterAPI.Factory.getInstance();
        service.validateCode(code, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                dialog2.dismiss();
                if (s.equals("valido")) {
                    consumer.barCodeResult(caja.getText().toString());
                    ProduceCodeFragment.this.navigator.navigate("lugar");
                } else {
                    Toast.makeText(ProduceCodeFragment.this.getContext(), "codigo invalido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dialog2.dismiss();
                Toast.makeText(ProduceCodeFragment.this.getContext(), "hubo un error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateCodeFromDB() {
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
