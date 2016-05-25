package util.navigation.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import local_Db.FotoDB;
import local_db_activity_fragments.CameraLocalFragment;
import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.OnclickLink;
import util.navigation.modelos.Foto;

/**
 * Created by marcoisaac on 5/11/2016.
 */
public class FotosLocalAdapter extends RecyclerView.Adapter<FotosLocalAdapter.ViewHolder> implements OnclickLink {
    private final DisplayMetrics metrics;
    private List<FotoDB> mDataset;
    private Context context;
    private PositionConsumer positon;


    public interface PositionConsumer {
        public void position(int position);
    }

    @Override
    public void position(int pos) {
        positon.position(pos);
    }

    @Override
    public void positionDialog(int pos) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;
        private final TextView descripcion;
        private final ImageView image;
        private Context context;
        private OnclickLink link;

        public ViewHolder(CardView v, Context context, OnclickLink link) {
            super(v);
            v.setOnClickListener(this);
            this.context = context;
            this.link = link;
            title = (TextView) v.findViewById(R.id.description_corta);
            descripcion = (TextView) v.findViewById(R.id.description_larga);
            image = (ImageView) v.findViewById(R.id.imagen_cartita);
        }

        @Override
        public void onClick(View v) {
            link.position(getLayoutPosition());
        }
    }

    public FotosLocalAdapter(List<FotoDB> myDataset, Context context, PositionConsumer pos) {
        mDataset = myDataset;
        this.context = context;
        positon = pos;
        metrics = new DisplayMetrics();

        final WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getMetrics(metrics);
    }


    @Override
    public FotosLocalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cartas, parent, false);
        ViewHolder vh = new ViewHolder(v, context, this);
        return vh;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FotoDB foto = mDataset.get(position);

        holder.title.setText(foto.getTitulo());
        holder.descripcion.setText(foto.getDescripcion());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //holder.image.setImageDrawable(context.getDrawable(camarita));
        }

        File fil = new File(foto.getArchivo());//
        if (fil.exists()) {
            //Toast.makeText(context, "si existe el archivo", Toast.LENGTH_LONG).show();
            Picasso.with(context).load(fil).
                    resize((int) (metrics.widthPixels)// fil as parameter
                            , (int) (150)) // instead of Uri was file path in ExpandableCustomAdp
                    .into(holder.image);
        } else {
            Toast.makeText(context, "No existe: -> " + fil.getPath(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        if (mDataset != null) {
            return mDataset.size();
        } else {
            return 0;
        }
    }

}