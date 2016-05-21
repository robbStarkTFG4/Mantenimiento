package util.navigation.adapter;

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
import android.widget.Toast;

import java.util.List;

import data_activity_fragments.ServicioFragment;
import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.UpdateList;
import util.navigation.modelos.HistorialDetalles;

/**
 * Created by marcoisaac on 5/11/2016.
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> implements UpdateList {
    private List<HistorialDetalles> mDataset;

    @Override
    public void updateModel(String val, int position) {
        Log.d("NUEVO_VALOR", val);
        mDataset.get(position).setValor(val);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public LinearLayout parent;
        public TextView label;
        public EditText value;
        private int pos;
        private UpdateList update;

        public ViewHolder(LinearLayout v, final UpdateList update) {
            super(v);
            this.update = update;
            parent = v;
            label = (TextView) v.findViewById(R.id.service_title);
            value = (EditText) v.findViewById(R.id.service_value);
            value.setOnClickListener(this);
            value.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    ViewHolder.this.update.updateModel(s.toString(), pos);
                }
            });
        }

        @Override
        public void onClick(View v) {
            pos = getLayoutPosition();
            Log.d("ADAPTER", String.valueOf(pos));
        }
    }

    public ServiceAdapter(List<HistorialDetalles> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v, this);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        holder.label.setText(mDataset.get(position).getParametro());
        holder.value.setText(mDataset.get(position).getValor());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}