package jp.ne.biglobe.biglobeapp;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import jp.ne.biglobe.biglobeapp.models.SettingModel;
import jp.ne.biglobe.biglobeapp.utils.SharedPrefs;

/**
 * Created by anhtai on 7/5/15.
 */
public class BLApplication extends android.app.Application {
    private static final String TAG = BLApplication.class.getSimpleName();

    private static Context mContext;

    public SettingModel getSetting() {
        return mSetting;
    }

    public void setSetting(SettingModel mSetting) {
        this.mSetting = mSetting;
    }

    private SettingModel mSetting;

    // Google GoogleAnalytics
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        // Init SharedPreference
        SharedPrefs.init(this);

        mContext = this.getApplicationContext();
        mSetting = SettingModel.load();
    }

    /**
     *
     * @return
     */
    public static Context getAppContext() {
        return mContext;
    }

    /**
     * Gets the default {@link Tracker} for this {@link BLApplication}.
     * @return tracker
     */
    synchronized public Tracker getTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.ga_analytics);
        }
        return mTracker;
    }

    /**
     * Send normal GA
     * @param category
     * @param action
     * @param label
     */
    public void sendGA(String category, String action, String label) {
        this.getTracker().send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }

}
