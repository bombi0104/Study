package jp.ne.biglobe.biglobeapp.api;

import android.content.Context;
import android.text.TextUtils;
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
public class RegTokenAPI {
    private static final String TAG = RegTokenAPI.class.getSimpleName();
    private Context mContext;
    private String url;

    public RegTokenAPI(Context context) {
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
        url = mContext.getString(R.string.base_api_url) + "regToken.jsp?";
        url = url + "aid=" + mContext.getString(R.string.appid);
        url = url + "&dev=a";
        url = url + "&test=" + mContext.getString(R.string.test_flag);
        url = url + "&tkn=" + mContext.getString(R.string.test_flag);
        if (!TextUtils.isEmpty(SharedPrefs.getString(Enums.PREF_TOKENID))){
            url = url + "&tid=" + SharedPrefs.getString(Enums.PREF_TOKENID);
        }
    }
}
