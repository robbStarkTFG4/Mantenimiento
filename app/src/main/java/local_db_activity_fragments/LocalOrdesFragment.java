package local_db_activity_fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import local_Db.EquipoDB;
import local_Db.OrdenDB;
import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Navigator;
import util.navigation.OnclickLink;
import util.navigation.SerialListHolder;
import util.navigation.adapter.OrdenAdapter;
import util.navigation.custom.recycler.RecyclerViewEmpty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocalOrdesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalOrdesFragment extends Fragment {
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

    private List<OrdenDB> ordenDBList;
    private OnclickLink link;

    public LocalOrdesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocalOrdesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalOrdesFragment newInstance(SerialListHolder param1, String param2) {
        LocalOrdesFragment fragment = new LocalOrdesFragment();
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
            ordenDBList = ((SerialListHolder) getArguments().getSerializable(ARG_PARAM1)).getLocalOrderDB();
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_ordes, container, false);
        recyclerSetUp(view);
        return view;
    }


    private void recyclerSetUp(View view) {
        mRecyclerView = (RecyclerViewEmpty) view.findViewById(R.id.recycler_local);
        mRecyclerView.setEmptyView((TextView) view.findViewById(R.id.empty_view_local));
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new OrdenAdapter(ordenDBList, getActivity(), link);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            navigator = (Navigator) context;
            link = (OnclickLink) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigator = null;
        link = null;
    }
}
