package util.navigation;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import util.navigation.modelos.HistorialDetalles;
import util.navigation.modelos.InformacionFabricante;

/**
 * Created by marcoisaac on 5/23/2016.
 */
public class Modifier {
    public static void changeMenuItemColor(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getIcon() != null) {
                Drawable draw = item.getIcon();
                Drawable dr = DrawableCompat.wrap(draw);
                if (dr != null) {
                    DrawableCompat.setTint(dr, Color.WHITE);
                }
            }
        }
    }

    public static void convertToHistory(List<InformacionFabricante> body, List<HistorialDetalles> dataList) {
        for (int i = 0; i < body.size(); i++) {
            InformacionFabricante inf = body.get(i);
            dataList.add(new HistorialDetalles(inf.getParametro(), inf.getValor()));
        }
    }
}
