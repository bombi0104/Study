package vn.bombi.study.Helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import vn.bombi.study.Activity.MainActivity;
import vn.bombi.study.R;

/**
 * Created by anhtai on 6/12/15.
 */
public class NotificationHelper {

    Context mContext;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyMgr;
    int numMessages = 0;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    public void createSimple() {
        mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setAutoCancel(true);


        Intent resultIntent;
        resultIntent = new Intent(mContext, MainActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        mNotifyMgr = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(1, mBuilder.build());
    }

    public void updateSimple() {
        if (mBuilder != null) {
            mBuilder.setContentText("Sendcond text")
                    .setNumber(numMessages++);
            // Because the ID remains unchanged, the existing notification is
            // updated.
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }

    public void removeSimple(int id) {

    }

    public void addBigView(String msg) {
        mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My notification - BigView")
                .setContentText("Hello World!")
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg));


        Intent resultIntent;
        resultIntent = new Intent(mContext, MainActivity.class);

//        PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        mContext,
//                        0,
//                        resultIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);

        // Sets up the Snooze and Dismiss action buttons that will appear in the
        // big view of the notification.
        Intent dismissIntent = new Intent(mContext, MainActivity.class);
        PendingIntent piDismiss = PendingIntent.getService(mContext, 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(mContext, MainActivity.class);
        PendingIntent piSnooze = PendingIntent.getService(mContext, 0, snoozeIntent, 0);

        mBuilder.addAction(R.mipmap.ic_launcher, "AAA", piDismiss);
        mBuilder.addAction(R.mipmap.ic_launcher, "BBB", piSnooze);

        // Gets an instance of the NotificationManager service
        mNotifyMgr = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(2, mBuilder.build());
    }
}
