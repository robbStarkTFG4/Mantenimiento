package register_activity_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.OnclickLink;
import util.navigation.SerialListHolder;
import util.navigation.adapter.LineAdapter;
import util.navigation.modelos.Lugar;


public class LineDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private List<Lugar> dataList;
    private String mParam2;

    private OnclickLink link;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public LineDialogFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LineDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LineDialogFragment newInstance(SerialListHolder param1, String param2) {
        LineDialogFragment fragment = new LineDialogFragment();
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
            dataList = ((SerialListHolder) getArguments().getSerializable(ARG_PARAM1)).getList();
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_line_dialog, container, false);
        recyclerSetUp(view);
        return view;
    }


    private void recyclerSetUp(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.register_line_recycler);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new LineAdapter(dataList, link);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnclickLink) {
            //mListener = (LineConsumer) context;
            link = (OnclickLink) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Navigator");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        link = null;
    }

}
