package jp.ne.biglobe.biglobeapp.models;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;
import jp.ne.biglobe.biglobeapp.BLApplication;
import jp.ne.biglobe.biglobeapp.api.BaseballMasterLoader;
import jp.ne.biglobe.biglobeapp.api.RegTokenAPI;
import jp.ne.biglobe.biglobeapp.gcm.RegistrationIntentService;
import jp.ne.biglobe.biglobeapp.utils.Enums;
import jp.ne.biglobe.biglobeapp.utils.MessageEvent;
import jp.ne.biglobe.biglobeapp.utils.SharedPrefs;
import jp.ne.biglobe.biglobeapp.utils.Utils;

/**
 * Created by taipa on 7/2/15.
 */
public class CommonProcess {
    private static final String TAG = "CommonProcess";
    private Context mContext;

    public CommonProcess(Context context) {
        mContext = context;
    }

    /**
     *
     */
    public void initApp() {
        // Check if default setting value is first generated ?
        if (!SharedPrefs.getBool(Enums.PREF_GENERATED_DEFAULT_SETTING_VALUES)) {
            // TODO: General default setting values.
            SettingModel setting = new SettingModel();
            setting.generateDefault(mContext);
            setting.save();
            SharedPrefs.saveBool(Enums.PREF_GENERATED_DEFAULT_SETTING_VALUES, true);

            // Save create setting data to BLApplication class
            BLApplication blapp = (BLApplication) mContext.getApplicationContext();
            blapp.setSetting(setting);

            // Print json to get log debug
            Gson gSon = new Gson();
            String value = gSon.toJson(setting, SettingModel.class);
            Log.d(TAG, "SettingJSON : " + value);
        } else {
            SettingModel setting = SettingModel.load();
            BLApplication blapp = (BLApplication) mContext.getApplicationContext();
            blapp.setSetting(setting);
        }

        // Register GCM token if not exist.
        if (TextUtils.isEmpty(SharedPrefs.getString(Enums.PREF_GCM_TOKEN))) {
            Intent intent = new Intent(mContext, RegistrationIntentService.class);
            mContext.startService(intent);
        } else {
            // If tokenID is not exist, call regtoken API
            if (TextUtils.isEmpty(SharedPrefs.getString(Enums.PREF_TOKENID))) {
                // TODO: Call API regToken
                callRegTokenAPI();
            } else {
                updatePushInfoFirstTime();
            }
        }
    }

    /**
     * Call regToken API
     */
    private void callRegTokenAPI() {
        RegTokenAPI api = new RegTokenAPI(mContext);
        api.run(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "RegTokenAPI : " + response);
                if (isReponseOK(response)) {
                    getTokenIdFromResponse(response);

                    // Check and update pushInfo first time
                    updatePushInfoFirstTime();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    /**
     * Call updatePushInfo API first time
     */
    private void updatePushInfoFirstTime() {
        if (!SharedPrefs.getBool(Enums.PREF_FIRST_CALL_UPDATE_PUSHINFO_SUCCESS)) {
            // TODO: Call API updatePushinfo first times
            EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
        }
    }

    /**
     * Get TokenID from response and save to sharedPreference PREF_TOKENID
     *
     * @param response
     */
    public static void getTokenIdFromResponse(String response) {
        String[] strings = response.split(":");
        if (strings.length > 1) {
            SharedPrefs.saveString(Enums.PREF_TOKENID, strings[1].trim());
            Log.d(TAG, "RegTokenAPI - Token : " + strings[1]);
        }
    }

    /**
     * Check if response is OK
     *
     * @param response
     * @return
     */
    public static boolean isReponseOK(String response) {
        if (!TextUtils.isEmpty(response)) {
            return response.startsWith("OK");
        }

        return false;
    }

    /**
     * Update Baseball master
     */
    public void updateBaseballMaster() {
        Log.d(TAG, "updateBaseballMaster START");
        if (!DateUtils.isToday(SharedPrefs.getLong(Enums.PREF_BASEBALL_MASTER_LAST_DOWNLOAD_TIME))) {
            Log.d(TAG, "Download baseballMaster.json");

            BaseballMasterLoader loader = new BaseballMasterLoader(mContext);
            loader.run(
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "Baseball Master : " + response.toString());
                            String updateDate = getUpdateDateFromBaseballMasterJson(response.toString());
                            Log.d(TAG, "Update-Date: " + updateDate);

                            BLApplication app = (BLApplication) mContext.getApplicationContext();
                            SettingModel settingModel = app.getSetting();

                            if (isNeedToUpdateBaseballMasterData(updateDate,
                                    settingModel.getBaseballMasterUpdateDate())) {
                                //Update json data to local PushSetting object
                                settingModel.updateBaseballTeam(response.toString());

                                //Update lastUpdate data = json.updateDate
                                settingModel.setBaseballMasterUpdateDate(updateDate);

                                // Send notify to update screen list.
                                EventBus.getDefault().post(new MessageEvent("MasterDataChanged"));
                            }

                            // Save downloaded timestamp to SharedPreference.
                            SharedPrefs.saveLong(Enums.PREF_BASEBALL_MASTER_LAST_DOWNLOAD_TIME, new Date().getTime());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.d(TAG, "updateBaseballMaster Download error");
                            error.printStackTrace();
                        }
                    }
            );

        } else {
            Log.d(TAG, "Don't download baseball master json");
        }
    }

    /**
     * Get updateDate value from json
     *
     * @param json Downloaded Baseball_master_json string
     * @return string : json.updatedate
     */
    private String getUpdateDateFromBaseballMasterJson(String json) {
        try {
            JSONObject masterData = new JSONObject(json);
            String updateDate = masterData.getString("updatedate");
            return updateDate;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Check if need to update local Baseball teams data.
     *
     * @param d1 yyyyMMdd : downloaded json.updatedate
     * @param d2 yyyyMMdd : last updated date
     * @return
     */
    private boolean isNeedToUpdateBaseballMasterData(String d1, String d2) {
        if (TextUtils.isEmpty(d2)) {
            return true;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date1 = dateFormat.parse(d1);
            Date date2 = dateFormat.parse(d2);

            if (date1.compareTo(date2) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Merge response data from updatePushInfo to local SettingModel object
     *
     * @param response
     * @param settingModel
     */
    public static void mergeUpdatePushInfoResponseToLocalObject(String response, SettingModel settingModel) {
        String[] responseArray = response.split("\n");
        for (int i = 0; i < responseArray.length; i++) {
            String[] item = responseArray[i].trim().split(":");
            if (item.length == 2) {
                switch (item[0]) {
                    case "d1":
                        settingModel.setNewsMorning("1".equals(item[1].trim()));
                        break;
                    case "d2":
                        settingModel.setNewsNoon("1".equals(item[1].trim()));
                        break;
                    case "d3":
                        settingModel.setNewsNight("1".equals(item[1].trim()));
                        break;
                    case "rc":
                        settingModel.setOsusume("1".equals(item[1].trim()));
                        break;
                    case "bs":
                        settingModel.setBaseballSchedule("1".equals(item[1].trim()));
                        break;
                    case "ts":
                        String[] ts = item[1].trim().split("_");
                        if (ts.length == 2) {
                            settingModel.updateBaseballTeamSettingValue(ts[0], "1".equals(ts[1].trim()), true);
                        }
                        break;
                    case "te":
                        //settingModel.setNewsMorning(item[1] == "1" ? true : false);
                        String[] te = item[1].trim().split("_");
                        if (te.length == 2) {
                            settingModel.updateBaseballTeamSettingValue(te[0], "1".equals(te[1].trim()), false);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        // Save changed data to SharedPreferences
        settingModel.save();
    }
}
