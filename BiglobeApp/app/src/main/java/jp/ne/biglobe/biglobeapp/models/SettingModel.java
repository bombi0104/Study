package jp.ne.biglobe.biglobeapp.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.ne.biglobe.biglobeapp.R;
import jp.ne.biglobe.biglobeapp.utils.Enums;
import jp.ne.biglobe.biglobeapp.utils.SharedPrefs;

/**
 * Created by taipa on 7/2/15.
 */
public class SettingModel {
    private static final String TAG = SettingModel.class.getSimpleName();

    private boolean all;
    private boolean newsMorning;
    private boolean newsNoon;
    private boolean newsNight;
    private boolean newsDialog;
    private boolean osusume;
    private boolean osusumeDialog;
    private boolean baseballSchedule;
    private ArrayList<BaseballTeam> baseballTeams;
    private String baseballMasterUpdateDate;


    /**
     *
     */
    public SettingModel() {

    }

    /**
     * Generate default value set
     *
     * @param context
     */
    public void generateDefault(Context context) {
        Log.d(TAG, "generateDefault");
        this.all = true;
        this.newsMorning = SharedPrefs.getBool2(Enums.OLD_APP_PREF_NEWS_MORNING);
        this.newsNoon = SharedPrefs.getBool2(Enums.OLD_APP_PREF_NEWS_NOON);
        this.newsNight = SharedPrefs.getBool2(Enums.OLD_APP_PREF_NEWS_NIGHT);
        this.newsDialog = SharedPrefs.getBool2(Enums.OLD_APP_PREF_DIALOG);

        this.osusume = true;
        this.osusumeDialog = true;
        this.baseballSchedule = true;

        // Generate baseball team data and all is false;
        this.baseballTeams = getTeamsFromLocalFile(context);
        this.baseballMasterUpdateDate = null;
    }

    /**
     *
     */
    public ArrayList<SettingContent.SettingItem> generateSettingItems(Context context) {
        ArrayList<SettingContent.SettingItem> items = new ArrayList<SettingContent.SettingItem>();
        items.add(new SettingContent.SettingItem("1", context.getString(R.string.setting_push), "", this.all));

        items.add(new SettingContent.SettingItem("2", context.getString(R.string.setting_news_moring), "", this.newsMorning));
        items.add(new SettingContent.SettingItem("3", context.getString(R.string.setting_news_noon), "", this.newsNoon));
        items.add(new SettingContent.SettingItem("4", context.getString(R.string.setting_news_night), "", this.newsNight));
        items.add(new SettingContent.SettingItem("5", context.getString(R.string.setting_news_dialog), "", this.newsDialog));
        items.add(new SettingContent.SettingItem("6", context.getString(R.string.setting_baseball_schedule), "", this.baseballSchedule));
        items.add(new SettingContent.SettingItem("7", context.getString(R.string.setting_header_baseball_battle_start), "", false));
        for (int i = 0; i < baseballTeams.size(); i++) {
            items.add(new SettingContent.SettingItem("7", baseballTeams.get(i).getName(), "", baseballTeams.get(i).getBattleStart()));
        }
        items.add(new SettingContent.SettingItem("7", context.getString(R.string.setting_header_baseball_battle_end), "", false));
        for (int i = 0; i < baseballTeams.size(); i++) {
            items.add(new SettingContent.SettingItem("7", baseballTeams.get(i).getName(), "", baseballTeams.get(i).getBattleEnd()));
        }

        items.add(new SettingContent.SettingItem("9", context.getString(R.string.setting_osusume_content), "", this.osusume));
        items.add(new SettingContent.SettingItem("10", context.getString(R.string.setting_osusume_content_dialog), "", this.osusumeDialog));

        Log.d(TAG, "generateSettingItems DONE " + items.size());
        return items;
    }

    /**
     * Save current Setting data to SharedPreferences
     */
    public void save() {
        Gson gSon = new Gson();
        String value = gSon.toJson(this, SettingModel.class);
        SharedPrefs.saveString(Enums.PREF_PUSH_SETTING_DATA, value);

//        Log.d(TAG, "SettingModel New Data:" + value);
    }


    /**
     * Load Setting object from SharedPreferences
     *
     * @return Setting Object
     */
    public static SettingModel load() {
        Gson gSon = new Gson();
        String jsonObj = SharedPrefs.getString(Enums.PREF_PUSH_SETTING_DATA);

        if (TextUtils.isEmpty(jsonObj)) {
            return null;
        }

        SettingModel value = gSon.fromJson(jsonObj, SettingModel.class);
        return value;
    }

    /**
     * @param jsonData
     */
    public void updateBaseballTeam(String jsonData) {
        ArrayList<BaseballTeam> teams = BaseballTeam.getTeamListFromJSON(jsonData);

        if (!teams.isEmpty()){
            // Update local setting of teams to new teams list
            for (BaseballTeam team : teams) {
                for (BaseballTeam tmp : baseballTeams) {
                    // Merge data by ID
                    if (team.getId() == tmp.getId()) {
                        team.setBattleStart(tmp.getBattleStart());
                        team.setBattleEnd(tmp.getBattleEnd());
                        break;
                    }
                }
            }

            this.baseballTeams = teams;
            this.save();
        }
    }

    /**
     * Get Baseball team list from Local File
     *
     * @return
     */
    private ArrayList<BaseballTeam> getTeamsFromLocalFile(Context context) {
        ArrayList<BaseballTeam> baseballteams = BaseballTeam.getTeamListFromJSON(loadJSONFromAsset(context));
        return baseballteams;
    }

    /**
     * @return
     */
    private String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("baseball_master.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public boolean isNewsMorning() {
        return newsMorning;
    }

    public void setNewsMorning(boolean newsMorning) {
        this.newsMorning = newsMorning;
    }

    public boolean isNewsNoon() {
        return newsNoon;
    }

    public void setNewsNoon(boolean newsNoon) {
        this.newsNoon = newsNoon;
    }

    public boolean isNewsNight() {
        return newsNight;
    }

    public void setNewsNight(boolean newsNight) {
        this.newsNight = newsNight;
    }

    public boolean isNewsDialog() {
        return newsDialog;
    }

    public void setNewsDialog(boolean newsDialog) {
        this.newsDialog = newsDialog;
    }

    public boolean isOsusume() {
        return osusume;
    }

    public void setOsusume(boolean osusume) {
        this.osusume = osusume;
    }

    public boolean isOsusumeDialog() {
        return osusumeDialog;
    }

    public void setOsusumeDialog(boolean osusumeDialog) {
        this.osusumeDialog = osusumeDialog;
    }

    public boolean isBaseballSchedule() {
        return baseballSchedule;
    }

    public void setBaseballSchedule(boolean baseballSchedule) {
        this.baseballSchedule = baseballSchedule;
    }

    public ArrayList<BaseballTeam> getBaseballTeams() {
        return baseballTeams;
    }

    public String getBaseballMasterUpdateDate() {
        return baseballMasterUpdateDate;
    }

    public void setBaseballMasterUpdateDate(String baseballMasterUpdateDate) {
        this.baseballMasterUpdateDate = baseballMasterUpdateDate;
    }

}
