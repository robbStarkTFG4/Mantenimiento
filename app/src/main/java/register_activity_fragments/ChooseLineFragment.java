package register_activity_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mantenimiento.mim.com.mantenimiento.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import server.LugarAPI;
import util.navigation.Modifier;
import util.navigation.Navigator;
import util.navigation.OnclickLink;
import util.navigation.SerialListHolder;
import util.navigation.WorkServer;
import util.navigation.modelos.Lugar;

import org.apache.commons.io.*;

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
    private ProgressDialog pg;


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
        setHasOptionsMenu(true);
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

        File file = null;
        switch (WorkServer.POSICION) {
            case 0:
                file = new File(Environment.  //THIS WORKS
                        getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                        "cerveceria.txt");
                break;
            case 1:
                file = new File(Environment.  //THIS WORKS
                        getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                        "concretos.txt");
                break;
        }

        dataList = new ArrayList<>();
        readList(file);

    }

    private void readList(File file) {
        String content;
        if (file.exists()) {
            try {
                //FileUtils.writeStringToFile(file, "porque?", "UTF-8");
                content = FileUtils.readFileToString(file, "UTF-8");
                if (content.length() > 0) {
                    Gson gson = new Gson();


                    Type listType = new TypeToken<List<Lugar>>() {
                    }.getType();
                    List<Lugar> lugaresList = gson.fromJson(content, listType);
                    dataList.clear();
                    dataList.addAll(lugaresList);

                    //for (Lugar l : lugaresList) {
                    //    Toast.makeText(ChooseLineFragment.this.getContext(), l.getNombre(), Toast.LENGTH_SHORT).show();
                    //}
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
                syncList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.choose_line_menu, menu);
        Modifier.changeMenuItemColor(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_line_sync:
                Context context = ChooseLineFragment.this.getContext();
                if (context != null) {
                    pg = new ProgressDialog(context);
                    pg.setMessage("espera un momento...");
                    pg.setCanceledOnTouchOutside(false);
                    pg.show();
                    syncList();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void syncList() {
        LugarAPI.Factory.getInstance().getLugaresList().enqueue(new Callback<List<Lugar>>() {
            @Override
            public void onResponse(Call<List<Lugar>> call, Response<List<Lugar>> response) {
                List<Lugar> body = response.body();
                if (body != null) {

                    Context context = ChooseLineFragment.this.getContext();
                    if (context == null) {
                        return;
                    }
                    pg.hide();
                    Gson gson = new Gson();
                    String json = gson.toJson(body);
                    File file = null;
                    switch (WorkServer.POSICION) {
                        case 0:
                            file = new File(Environment.  //THIS WORKS
                                    getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                                    "cerveceria.txt");
                            break;
                        case 1:
                            file = new File(Environment.  //THIS WORKS
                                    getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                                    "concretos.txt");
                            break;
                    }


                    try {
                        FileUtils.writeStringToFile(file, json, "UTF-8");
                        //Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
                        dataList.clear();
                        dataList.addAll(body);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Lugar>> call, Throwable throwable) {
                Context context = ChooseLineFragment.this.getContext();
                if (context == null) {
                    return;
                }
                Toast.makeText(context, "hubo algun error", Toast.LENGTH_LONG).show();
                pg.hide();
            }
        });
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
