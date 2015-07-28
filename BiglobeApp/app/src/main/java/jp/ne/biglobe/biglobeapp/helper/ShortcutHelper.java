package jp.ne.biglobe.biglobeapp.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.net.URISyntaxException;

import jp.ne.biglobe.biglobeapp.R;
import jp.ne.biglobe.biglobeapp.views.MainActivity;

/**
 * Created by taipa on 7/2/15.
 */
public class ShortcutHelper {
    private static final String TAG = ShortcutHelper.class.getSimpleName();
    private Context mContext;

    public ShortcutHelper(Context context) {
        mContext = context;
    }

    public void deleteSystemShortcut() {
        Log.d(TAG, "deleteSystemShortcut START");
        Intent intent = new Intent();
        String uri1 = "#Intent;action=android.intent.action.MAIN;category=android.intent.category.LAUNCHER;launchFlags=0x10200000;component=com.cyanogenmod.filemanager/.activities.NavigationActivity;end";
        String uri2 = "#Intent;action=android.intent.action.MAIN;category=android.intent.category.LAUNCHER;launchFlags=0x10200000;component=com.android.camera/.Camera;end";
        String uri3 = "#Intent;action=android.intent.action.MAIN;category=android.intent.category.LAUNCHER;launchFlags=0x10200000;component=jp.ne.biglobe.biglobeapp/.Views.MainActivity;end";
        try {
            Intent altShortcutIntent = Intent.parseUri(uri3, 0);
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, altShortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, mContext.getString(R.string.app_name));
        } catch (URISyntaxException e) {
            Log.d(TAG, e.getMessage());
        }
        intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        mContext.sendBroadcast(intent);
    }

    public void createShortcut() {
        Intent shortcutIntent = new Intent(mContext, MainActivity.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shortcutIntent.putExtra("duplicate", false);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, mContext.getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(mContext, R.mipmap.ic_launcher));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        mContext.sendBroadcast(addIntent);
    }
}
