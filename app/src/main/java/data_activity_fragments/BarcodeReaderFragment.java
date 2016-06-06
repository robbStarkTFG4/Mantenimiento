package data_activity_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import mantenimiento.mim.com.mantenimiento.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import server.OrdenAPI;
import util.navigation.Navigator;
import util.navigation.modelos.Equipo;
import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.InformacionFabricante;
import util.navigation.modelos.ListaNombreEquipos;


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
    private EquipmentConsumer consumer;
    private TextView manual;
    private EditText manualField;

    private int idTipo;

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
                barcodeReader();
                //equipmentForm();
            }
        });
        manualField = (EditText) view.findViewById(R.id.manual_field);
        manual = (TextView) view.findViewById(R.id.manual);
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = manualField.getText().toString();
                if (res.length() > 0) {
                    searchEquipment(res);
                } else {
                    Toast.makeText(getContext(), "escribe codigo", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (mParam1 == null) {
            barcodeReader();
        } else {
            searchEquipment(mParam1);
        }

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
                String res = result.getContents();
                searchEquipment(res);
            }

            // At this point we may or may not have a reference to the activity
            //displayToast(toast);
        }
    }

    private void searchEquipment(String res) {
        dialog = new ProgressDialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Loading");
        dialog.setMessage("Espera un momento");
        dialog.show();
        OrdenAPI service = OrdenAPI.Factory.getInstance();
        service.getEquipmentByCodeBar(res, new Callback<Equipo>() {


            @Override
            public void success(Equipo equipo, Response response) {
                if (consumer != null) {
                    consumer.consumeEquipment(equipo);
                    BarcodeReaderFragment.this.idTipo = equipo.getListaNombreEquiposIdlistaNombre();
                    //Toast.makeText(getContext(), "numero: " + BarcodeReaderFragment.this.idTipo, Toast.LENGTH_SHORT).show();
                    getFactoryList(dialog, equipo.getIdequipo());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (consumer != null) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "hubo algun error ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getFactoryList(final ProgressDialog dialog, final Integer idequipo) {
        OrdenAPI service = OrdenAPI.Factory.getInstance();
        service.getFactoryList(idequipo, new Callback<List<InformacionFabricante>>() {
            @Override
            public void success(List<InformacionFabricante> informacionFabricantes, Response response) {
                //dialog.dismiss();
                if (consumer != null) {
                    getHistorialDetalles(dialog, idequipo);
                    consumer.consumeFactoryList(informacionFabricantes);
                }
                //Toast.makeText(getContext(), "exito " + informacionFabricantes.get(0).getParametro(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                if (consumer != null) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "hubo algun error ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getHistorialDetalles(final ProgressDialog dialog, final Integer idequipo) {
        OrdenAPI service = OrdenAPI.Factory.getInstance();
        service.getHistorialDetalles(idequipo, new Callback<List<HistorialDetalles>>() {
            @Override
            public void success(List<HistorialDetalles> historialDetalles, Response response) {
                if (consumer != null) {
                    consumer.consumeHistoryList(historialDetalles);
                    retrieveNombre(dialog, idTipo);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (consumer != null) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "hubo algun error ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void retrieveNombre(final ProgressDialog dialog, Integer idTipo) {
        OrdenAPI service = OrdenAPI.Factory.getInstance();
        service.getNombreEquipo(idTipo, new Callback<ListaNombreEquipos>() {
            @Override
            public void success(ListaNombreEquipos s, Response response) {
                if (consumer != null) {
                    dialog.dismiss();
                    consumer.consumeNombreEquipo(s.getNombre());
                    navigator.navigate("equipo");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (consumer != null) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "hubo algun error nombre tipo ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void equipmentForm() {
        if (navigator != null) {
            Toast.makeText(getContext(), "Action", Toast.LENGTH_LONG).show();
            if (dialog != null) {
                dialog.dismiss();
            }
            navigator.navigate("equipo");
        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            navigator = (Navigator) context;
            consumer = (EquipmentConsumer) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        consumer = null;
        navigator = null;
    }

    public interface EquipmentConsumer {
        public void consumeEquipment(Equipo equipo);

        public void consumeFactoryList(List<InformacionFabricante> list);

        public void consumeHistoryList(List<HistorialDetalles> list);

        public void consumeNombreEquipo(String nombre);
    }
}


