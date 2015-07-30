package jp.ne.biglobe.biglobeapp.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import jp.ne.biglobe.biglobeapp.R;

/**
 * Created by taipa on 7/15/15.
 */
public class GetPushInfoAPI {
    private static final String TAG = GetPushInfoAPI.class.getSimpleName();
    private Context mContext;
    private String url;

    public GetPushInfoAPI(Context context) {
        mContext = context;
        generateURL();
    }

    public void run(Response.Listener<String> responseOK, Response.ErrorListener responseNG) {
        Log.d(TAG, "Call API : " + url);
        BLStringRequest request = new BLStringRequest(Request.Method.GET, url, responseOK, responseNG);
        Volley.newRequestQueue(mContext).add(request);
    }

    /**
     *
     * @return
     */
    private void generateURL() {
        url = mContext.getString(R.string.base_api_url) + "getPushInfo.jsp?";
        url = url + "tid=" + "";
        url = url + "&aid=" + mContext.getString(R.string.appid);
        url = url + "&dev=a";
        url = url + "&test=" + mContext.getString(R.string.test_flag);
    }
}
