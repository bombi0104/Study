package jp.ne.biglobe.biglobeapp.utils;

/**
 * Created by taipa on 7/21/15.
 */
public class BLEvent {
    public final TYPES type;

    public BLEvent(TYPES type) {
        this.type = type;
    }

    public enum TYPES {
        INIT_PROCESS_DONE,
        UPDATE_BASEBALL_MASTER_DONE

    }
}
