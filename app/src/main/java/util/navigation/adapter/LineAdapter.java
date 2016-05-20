package util.navigation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.OnclickLink;
import util.navigation.modelos.Lugar;

/**
 * Created by marcoisaac on 5/11/2016.
 */
public class LineAdapter extends RecyclerView.Adapter<LineAdapter.ViewHolder> {

    private List<Lugar> mDataset;
    private OnclickLink link;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public LinearLayout parent;
        public TextView text;
        private OnclickLink link;

        public ViewHolder(LinearLayout v, OnclickLink link) {
            super(v);
            this.link = link;
            v.setOnClickListener(this);
            parent = v;
            text = (TextView) v.findViewById(R.id.line_recycler_register);
        }


        @Override
        public void onClick(View v) {
            link.positionDialog(getLayoutPosition());
        }
    }

    public LineAdapter(List<Lugar> myDataset, OnclickLink link) {
        mDataset = myDataset;
        this.link = link;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.line_layout_register, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v, link);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        holder.text.setText(mDataset.get(position).getNombre());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}