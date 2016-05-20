package data_activity_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Navigator;


public class BarcodeReaderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressDialog dialog;

    //widgets
    private TextView reintentar;

    //util
    final Handler h = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            equipmentForm();
            return false;
        }
    });

    private Navigator navigator;

    public BarcodeReaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BarcodeReaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BarcodeReaderFragment newInstance(String param1, String param2, Navigator navigator) {
        BarcodeReaderFragment fragment = new BarcodeReaderFragment();
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

        View view = inflater.inflate(R.layout.fragment_barcode_reader, container, false);

        reintentar = (TextView) view.findViewById(R.id.reintentar);
        reintentar.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //barcodeReader(); descomentar esto despues
                equipmentForm();
            }
        });

        barcodeReader();

        return view;
    }

    private void barcodeReader() {
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
                toast = "Codigo: " + result.getContents();
                dialog = new ProgressDialog(getContext());
                dialog.setCanceledOnTouchOutside(false);
                dialog.setTitle("Loading");
                dialog.setMessage("Espera un momento");
                dialog.show();
                Thread thread = new Thread() {
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
                thread.start();

            }

            // At this point we may or may not have a reference to the activity
            displayToast(toast);
        }
    }

    private void equipmentForm() {
        if (navigator != null) {
            Toast.makeText(getContext(), "Action", Toast.LENGTH_LONG).show();
            if(dialog!=null) {
                dialog.dismiss();
            }
            navigator.navigate("equipo");
        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
        }

    }

    private void displayToast(String toast) {
        Toast.makeText(getContext(), toast, Toast.LENGTH_LONG).show();
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }
}


