package util.navigation;

/**
 * Created by marcoisaac on 6/8/2016.
 */
public interface BlackBasket {
    public void addElementToBlackList(int pos);

    public void removeFromBlackList(int pos);

    public void showCheckBox();

    public boolean getBoolean();

    public void showElementInfo(int layoutPosition);

}
