package jp.ne.biglobe.biglobeapp.api;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.ne.biglobe.biglobeapp.R;

/**
 * Created by taipa on 7/14/15.
 */
public class BaseballMasterLoader {
    private static final String TAG = BaseballMasterLoader.class.getSimpleName();
    private Context mContext;

    public BaseballMasterLoader(Context context) {
        mContext = context;
    }

    public void updateBaseballMaster() {
        // If today is checked, don't download json again

        // Start download json


    }

    public void run(Response.Listener<JSONObject> responseOK, Response.ErrorListener responseNG) {
        String url = mContext.getString(R.string.baseball_master_url);

        Log.d(TAG, url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, "", responseOK, responseNG) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createBasicAuthHeader();
            }
        };

        Volley.newRequestQueue(mContext).add(jsObjRequest);
    }

    private Map<String, String> createBasicAuthHeader() {
        Map<String, String> headerMap = new HashMap<String, String>();

        String credentials = "btpush" + ":" + "btpush_dev";
        String base64EncodedCredentials =
                Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerMap.put("Authorization", "Basic " + base64EncodedCredentials);
        return headerMap;
    }
}