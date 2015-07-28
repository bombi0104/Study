package jp.ne.biglobe.biglobeapp.api;

import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import jp.ne.biglobe.biglobeapp.R;

/**
 * Created by taipa on 7/15/15.
 */
public class BLStringRequest extends StringRequest {
    public BLStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
//        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("Content-Type", "application/json; charset=utf-8");

        return createBasicAuthHeader();
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        return super.getRetryPolicy();
    }

    /**
     *
     * @return
     */
    private Map<String, String> createBasicAuthHeader() {
        Map<String, String> headerMap = new HashMap<String, String>();

        String credentials = "btpush" + ":" + "btpush_dev";
        String base64EncodedCredentials =
                Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerMap.put("Authorization", "Basic " + base64EncodedCredentials);
        return headerMap;
    }

}
