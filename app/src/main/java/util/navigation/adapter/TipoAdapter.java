package util.navigation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.OnclickLink;
import util.navigation.modelos.ListaNombreEquipos;

/**
 * Created by marcoisaac on 5/17/2016.
 */
public class TipoAdapter extends RecyclerView.Adapter<TipoAdapter.ViewHolder> {

    private Context context;
    private List<ListaNombreEquipos> dataList;
    private OnclickLink link;

    public TipoAdapter(List<ListaNombreEquipos> list, Context context, OnclickLink link) {
        this.dataList = list;
        this.context = context;
        this.link = link;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView text;
        private OnclickLink link;

        public ViewHolder(View v, OnclickLink link) {
            super(v);
            this.link = link;
            v.setOnClickListener(this);
            text = (TextView) v.findViewById(R.id.tipo_element);
        }

        @Override
        public void onClick(View v) {
            link.position(getLayoutPosition());
        }
    }

    @Override
    public TipoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tipo_cajita, parent, false);
        ViewHolder vh = new ViewHolder(v, link);
        return vh;
    }

    @Override
    public void onBindViewHolder(TipoAdapter.ViewHolder holder, int position) {
        if (dataList != null && dataList.size() > 0) {
            holder.text.setText(dataList.get(position).getNombre());
        }

    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }
}
