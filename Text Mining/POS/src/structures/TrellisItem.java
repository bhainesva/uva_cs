package structures;

/**
 * Created by ben on 4/20/16.
 */
public class TrellisItem {
    public double value;
    public int pointer;

    public TrellisItem(double val) {
        value = val;
    }

    public TrellisItem(double val, int point) {
        value = val;
        pointer = point;
    }
}

