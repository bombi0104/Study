package jp.ne.biglobe.biglobeapp.views;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import de.greenrobot.event.EventBus;
import jp.ne.biglobe.biglobeapp.BLApplication;
import jp.ne.biglobe.biglobeapp.R;
import jp.ne.biglobe.biglobeapp.api.NukeSSLCerts;
import jp.ne.biglobe.biglobeapp.gcm.RegistrationIntentService;

import jp.ne.biglobe.biglobeapp.models.CommonProcess;

import jp.ne.biglobe.biglobeapp.utils.Enums;

import jp.ne.biglobe.biglobeapp.utils.BLEvent;
import jp.ne.biglobe.biglobeapp.utils.SharedPrefs;
import jp.ne.biglobe.biglobeapp.views.TopFragment.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements
        OnFragmentInteractionListener, PushSettingFragment.OnFragmentInteractionListener,
        TutorialPage1Fragment.OnFragmentInteractionListener {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private CommonProcess commonProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NukeSSLCerts.nuke();

        // Register to event bus
        EventBus.getDefault().register(this);

        // Show tutorial if need.
        showTutorial();

        // Init process
        commonProcess = new CommonProcess(this);
        commonProcess.initApp();

        //GsonExample example = new GsonExample();
        //example.example01();

        // Load TOP Setting screen.
        loadTopFragment();

        BLApplication app = (BLApplication) getApplication();
        app.sendGA("PushNotification", "Received", "http://google.com");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Register GCM Token
     */
    private void registerGCMToken() {
        // Just call register when have no data in PREF_GCM_TOKEN
        if (TextUtils.isEmpty(SharedPrefs.getString(Enums.PREF_GCM_TOKEN))) {
            if (checkPlayServices()) {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        } else {
            Log.d(TAG, "GCM Token: " + SharedPrefs.getString(Enums.PREF_GCM_TOKEN));
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Show tutorial if need
     */
    private void showTutorial() {
        // If turotial is not show, show it first
        if (!SharedPrefs.getBool(Enums.PREF_SHOWED_TUTORIAL)) {

        }
    }

    private void loadTopFragment() {
//        TopFragmentOld fragment = new TopFragmentOld();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.add(R.id.rootView, fragment);
//        ft.commit();
        TopFragment fragment = TopFragment.newInstance("aaa", "bbb");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.rootView, fragment);
        ft.commit();

    }


    private void loadSettingFragment() {
        Log.d(TAG, "loadSettingFragment START");
        PushSettingFragment fragment = new PushSettingFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.rootView, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void loadTutorial1Fragment() {
        Log.d(TAG, "loadSettingFragment START");
        TutorialPage1Fragment fragment = new TutorialPage1Fragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.rootView, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

//
//    @Override
//    public void onFragmentInteraction(String id) {
//        Log.d(TAG, "onFragmentInteraction : " + id);
//        if ("2".equals(id)) {
//            loadSettingFragment();
//        } else if (id == "4") {
//            ShortcutHelper helper = new ShortcutHelper(this);
//            helper.deleteSystemShortcut();
//            helper.createShortcut();
//        }
//    }


    public void onEvent(BLEvent event) {
        Log.d(TAG, "Get event : " + event.type);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Touch on item of Top screen
     *
     * @param v
     */
    public void onTextViewClick(View v) {
        switch (v.getId()) {
            case R.id.tvOpenSetting:
                loadSettingFragment();
                break;
            case R.id.tvCreateShortcut:
                loadTutorial1Fragment();
                break;
            default:
                break;

        }
    }
}
