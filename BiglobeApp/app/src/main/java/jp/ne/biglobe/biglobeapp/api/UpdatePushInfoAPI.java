package jp.ne.biglobe.biglobeapp.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import jp.ne.biglobe.biglobeapp.R;

/**
 * Created by taipa on 7/14/15.
 */
public class UpdatePushInfoAPI {
    private static final String TAG = UpdatePushInfoAPI.class.getSimpleName();
    private Context mContext;

    public UpdatePushInfoAPI(Context context) {
        mContext = context;
    }

    public void run(Response.Listener<String> responseOK, Response.ErrorListener responseNG) {
        BLStringRequest request = new BLStringRequest(Request.Method.GET, getURL(), responseOK, responseNG);
        Volley.newRequestQueue(mContext).add(request);
    }

    /**
     *
     * @return
     */
    private String getURL() {
        String url = mContext.getString(R.string.base_api_url) + "updatePushInfo.jsp?";
        url = url + "tid=" + "";
        url = url + "&aid=" + mContext.getString(R.string.appid);
        url = url + "&dev=a";
        url = url + "&test=" + mContext.getString(R.string.test_flag);

        return url;
    }
}
