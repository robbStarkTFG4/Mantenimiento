package util.navigation;

import util.navigation.modelos.ListaNombreEquipos;

/**
 * Created by marcoisaac on 5/10/2016.
 */
public interface Navigator {
    public void navigate(String addres);

    public void tipoEquipo(ListaNombreEquipos tip);
}
