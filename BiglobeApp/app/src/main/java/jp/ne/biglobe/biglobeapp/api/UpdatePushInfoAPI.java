package jp.ne.biglobe.biglobeapp.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import jp.ne.biglobe.biglobeapp.R;
import jp.ne.biglobe.biglobeapp.utils.Enums;
import jp.ne.biglobe.biglobeapp.utils.SharedPrefs;

/**
 * Created by taipa on 7/14/15.
 */
public class UpdatePushInfoAPI {
    private static final String TAG = UpdatePushInfoAPI.class.getSimpleName();
    private Context mContext;
    private String url;

    public UpdatePushInfoAPI(Context context) {
        mContext = context;
        generateURL();
    }

    public void run(Response.Listener<String> responseOK, Response.ErrorListener responseNG) {
        Log.d(TAG, "Call API : " + url);
        BLStringRequest request = new BLStringRequest(Request.Method.GET, url, responseOK, responseNG);
        Volley.newRequestQueue(mContext).add(request);
    }

    /**
     * @return
     */
    private void generateURL() {
        url = mContext.getString(R.string.base_api_url) + "updatePushInfo.jsp?";
        url = url + "tid=" + SharedPrefs.getString(Enums.PREF_TOKENID);
        url = url + "&aid=" + mContext.getString(R.string.appid);
        url = url + "&dev=a";
        url = url + "&test=" + mContext.getString(R.string.test_flag);
    }

    /**
     * Add param to updatePushInfo
     * @param item
     * @param value
     * @param baseball_team_id
     */
    public void addParam(Enums.UPDATE_ITEMS item, boolean value, int baseball_team_id) {
        switch (item) {
            case NEWS_MORNING:
                url = url + "&d1=" + (value ? "1" : "0");
                break;
            case NEWS_NOON:
                url = url + "&d2=" + (value ? "1" : "0");
                break;
            case NEWS_NIGHT:
                url = url + "&d3=" + (value ? "1" : "0");
                break;
            case BASEBALL_SCHEDULE:
                url = url + "&bs=" + (value ? "1" : "0");
                break;
            case BASEBALL_BATTLE_START:
                url = url + "&ts=" + baseball_team_id + "_" + (value ? "1" : "0");
                break;
            case BASEBALL_BATTLE_END:
                url = url + "&te=" + baseball_team_id + "_"  + (value ? "1" : "0");
                break;
            case OSUSUME_CONTENT:
                url = url + "&rc=" + (value ? "1" : "0");
                break;
            default:
                break;
        }
    }
}
