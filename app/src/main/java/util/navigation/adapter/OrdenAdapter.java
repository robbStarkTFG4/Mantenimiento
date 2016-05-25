package util.navigation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import local_Db.OrdenDB;
import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.OnclickLink;

/**
 * Created by marcoisaac on 5/17/2016.
 */
public class OrdenAdapter extends RecyclerView.Adapter<OrdenAdapter.ViewHolder> {

    private Context context;
    private List<OrdenDB> dataList;
    private OnclickLink link;

    public OrdenAdapter(List<OrdenDB> list, Context context, OnclickLink link) {
        this.dataList = list;
        this.context = context;
        this.link = link;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView orden;
        public TextView prioridad;
        public TextView codigo;
        public TextView equipo;
        private OnclickLink link;

        public ViewHolder(View v, OnclickLink link) {
            super(v);
            this.link = link;
            v.setOnClickListener(this);
            orden = (TextView) v.findViewById(R.id.numero_orden_local);
            prioridad = (TextView) v.findViewById(R.id.prioridad_orden_local);
            codigo = (TextView) v.findViewById(R.id.codigo_barras_local);
            equipo = (TextView) v.findViewById(R.id.numero_equipo_local);
        }

        @Override
        public void onClick(View v) {
            link.position(getLayoutPosition());
        }
    }

    @Override
    public OrdenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_archive, parent, false);
        ViewHolder vh = new ViewHolder(v, link);
        return vh;
    }

    @Override
    public void onBindViewHolder(OrdenAdapter.ViewHolder holder, int position) {
        OrdenDB or = dataList.get(position);

        holder.orden.setText(or.getNumeroOrden());
        holder.prioridad.setText(or.getPrioridad());
        holder.codigo.setText(or.getEquipoDB2().getCodigoBarras());
        holder.equipo.setText(or.getEquipoDB2().getNumeroEquipo());

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
