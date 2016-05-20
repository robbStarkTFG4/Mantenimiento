package util.navigation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.modelos.HistorialDetalles;

/**
 * Created by marcoisaac on 5/11/2016.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<HistorialDetalles> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout parent;

        public ViewHolder(LinearLayout v) {
            super(v);
            parent = v;
        }
    }

    public DataAdapter(List<HistorialDetalles> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        int quantity = holder.parent.getChildCount();
        for (int i = 0; i < quantity; i++) {
            View v = holder.parent.getChildAt(i);
            switch (v.getId()) {
                case R.id.titulo:
                    ((TextView) v).setText(mDataset.get(position).getParametro());
                    break;
                case R.id.valor:
                    ((TextView) v).setText(mDataset.get(position).getValor());
                    break;
            }

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}