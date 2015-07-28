package jp.ne.biglobe.biglobeapp.models;

/**
 * Created by anhtai on 7/5/15.
 */
public class PushInfo {
    private int act;
    private String title;
    private String msg;
    private String u1;
    private String u2;
    private String g1;
    private String g2;

    public PushInfo() {

    }

    public PushInfo(int act, String msg, String u1, String u2, String g1, String g2) {
        this.act = act;
        this.msg = msg;
        this.u1 = u1;
        this.u2 = u2;
        this.g1 = g1;
        this.g2 = g2;
    }

}
