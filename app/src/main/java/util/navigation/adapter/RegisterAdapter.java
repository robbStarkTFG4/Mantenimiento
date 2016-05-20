package util.navigation.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mantenimiento.mim.com.mantenimiento.R;
import register_activity_fragments.ValueDialogFragment;
import util.navigation.OnclickLink;
import util.navigation.modelos.InformacionFabricante;

/**
 * Created by marcoisaac on 5/11/2016.
 */
public class RegisterAdapter extends RecyclerView.Adapter<RegisterAdapter.ViewHolder> {

    private final FragmentManager manager;
    private List<InformacionFabricante> mDataset;
    private OnclickLink link;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        private FragmentManager manager;
        public LinearLayout parent;
        private OnclickLink link;

        public ViewHolder(LinearLayout v, FragmentManager manage, OnclickLink link) {
            super(v);
            parent = v;
            v.setOnClickListener(this);

            this.manager = manage;
            this.link = link;
        }

        @Override
        public void onClick(View v) {
            link.position(getLayoutPosition());
            ValueDialogFragment dialog = new ValueDialogFragment();
            dialog.show(manager, "val_res");
        }
    }

    public RegisterAdapter(List<InformacionFabricante> myDataset, FragmentManager manager, OnclickLink link) {
        mDataset = myDataset;
        this.manager = manager;
        this.link = link;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RegisterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_layout_register, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v, manager, link);
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
                case R.id.titulo_register:
                    ((TextView) v).setText(mDataset.get(position).getParametro());
                    break;
                case R.id.valor_register:
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