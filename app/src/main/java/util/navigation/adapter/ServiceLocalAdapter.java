package util.navigation.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import local_Db.HistorialDetallesDB;
import local_db_activity_fragments.ValueDialogPortableFragment;
import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.PortableDialogItem;
import util.navigation.UpdateList;
import util.navigation.modelos.HistorialDetalles;

/**
 * Created by marcoisaac on 5/11/2016.
 */
public class ServiceLocalAdapter extends RecyclerView.Adapter<ServiceLocalAdapter.ViewHolder> {
    private final FragmentManager manager;
    private PortableDialogItem portable;
    private List<HistorialDetallesDB> mDataset;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private FragmentManager manager;
        private PortableDialogItem port;
        // each data item is just a string in this case
        public LinearLayout parent;
        public TextView label;
        public TextView value;
        private int pos;
        private UpdateList update;

        public ViewHolder(LinearLayout v, PortableDialogItem port, FragmentManager manager) {
            super(v);
            this.port = port;
            this.manager = manager;
            this.update = update;
            parent = v;
            label = (TextView) v.findViewById(R.id.service_title_local);
            value = (TextView) v.findViewById(R.id.service_value_local);
            parent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            pos = getLayoutPosition();
            Log.d("ADAPTER", String.valueOf(pos));
            port.itemPosition(getLayoutPosition());
            ValueDialogPortableFragment dialog = ValueDialogPortableFragment.newInstance(null, null);
            dialog.show(manager, "portableDialog");
        }
    }

    public ServiceLocalAdapter(List<HistorialDetallesDB> myDataset, PortableDialogItem portable, FragmentManager manager) {
        mDataset = myDataset;
        this.portable = portable;
        this.manager = manager;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ServiceLocalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_local_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v, portable, manager);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        holder.label.setText(mDataset.get(position).getParametro());
        if (mDataset.get(position).getValor() != null) {
            holder.value.setText(mDataset.get(position).getValor());
        } else {
            holder.value.setText("click para agregar");
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}