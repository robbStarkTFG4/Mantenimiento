package util.navigation.custom.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by marcoisaac on 5/13/2016.
 */
public class RecyclerViewEmpty extends RecyclerView {
    private View emptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {


        @Override
        public void onChanged() {
            Adapter<?> adapter =  getAdapter();
            if(adapter != null && emptyView != null) {
                if(adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmpty.this.setVisibility(View.GONE);
                }
                else {
                    emptyView.setVisibility(View.GONE);
                    RecyclerViewEmpty.this.setVisibility(View.VISIBLE);
                }
            }

        }
    };

    public RecyclerViewEmpty(Context context) {
        super(context);
    }

    public RecyclerViewEmpty(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmpty(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);

        if(adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}

