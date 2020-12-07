package eu.allan.apps.manlyminder2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class _NotificationManager {

    private final Database db;
    private final String notificationId;
    private final String title;
    private final String text;
    private final String buttonText_1;
    private final String buttonText_2;
    private final String buttonText_3;
    private Context context;
    private static String DEFAULT_CHANNEL_ID = "default_channel_dc_1";
    private static String DEFAULT_CHANNEL_NAME = "Default_dc_1";
    private String TAG = getClass().getSimpleName();


    public _NotificationManager(Context context, String notificationId, String title, String text, String buttonText_1, String buttonText_2, String buttonText_3) {

        this.context = context;
        db = new Database(context);
        this.notificationId = notificationId;
        this.title = title;
        this.text = text;
        this.buttonText_1 = buttonText_1;
        this.buttonText_2 = buttonText_2;
        this.buttonText_3 = buttonText_3;
        if(notificationId.equals("birthday")){
            db.setSingleEntry("birthday_last_reminder_sent", System.currentTimeMillis() + "");
        }else if(notificationId.equals("valentinesday")){
            db.setSingleEntry("valentinesday_last_reminder_sent", System.currentTimeMillis() + "");
        }else if(notificationId.equals("sendflowers")){
            db.setSingleEntry("sendflowers_last_reminder_sent", System.currentTimeMillis() + "");

        }
        sendNotification();

    }
    public void sendNotification() {
        int nid = 1;
        if(notificationId.equals("birthday")){
            nid = 1;
        }else if(notificationId.equals("valentinesday")){
            nid = 2;
        }else if(notificationId.equals("sendflowers")){
            nid = 3;
        }else if(notificationId.equals("pms")){
            nid = 4;

        }else{

        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(nid, this.getNotification());
    }

    public Notification getNotification() {
        Notification n = null;
        /*
        String[][] notificationDb = db.queryTableAll("notification");

        String[] notification = notificationDb[0];
        String account_id = notification[1];
        String account_from_email = notification[2];
        String account_from_name = notification[3];
        String account_subject = notification[4];
 */
        String account_id = notificationId;
        Intent intent = new Intent(context, ActivityMain.class);
        intent.putExtra("notification_key", account_id);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Ok
        Intent previousIntent = new Intent(context, _MainService.class);
        previousIntent.setAction(_Constants.ACTION.OK_ACTION);
        previousIntent.putExtra("key", account_id);
        previousIntent.putExtra("notification_key", account_id);
        PendingIntent ppreviousIntent = PendingIntent.getService(context, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        // Clutter
        Intent playIntent = new Intent(context, _MainService.class);
        playIntent.setAction(_Constants.ACTION.CLUTTER_ACTION);
        playIntent.putExtra("key", account_id);
        playIntent.putExtra("notification_key", account_id);
        PendingIntent pplayIntent = PendingIntent.getService(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Later
        Intent nextIntent = new Intent(context, _MainService.class);
        nextIntent.setAction(_Constants.ACTION.CLUTTER_ACTION_NOTIFY);
        nextIntent.putExtra("key", account_id);
        nextIntent.putExtra("notification_key", account_id);

        PendingIntent pnextIntent = PendingIntent.getService(context, 0, nextIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // build _notification
        // the addAction re-use the same intent to keep the example short

        n = new NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                .setWhen(0)
                .setContentTitle(title)
                .setSubText(account_id)
                .setShowWhen(true)
                .setContentText(text)
                .setSmallIcon(R.drawable.notification_image_1)
                .addAction(R.drawable.ic_menu_gallery, buttonText_1, ppreviousIntent)
                .addAction(android.R.drawable.ic_media_play, buttonText_2, pplayIntent)
                .addAction(android.R.drawable.ic_media_next, buttonText_3, pnextIntent)

/*
                .addAction(R.drawable.ic_menu_gallery, "Bought it", ppreviousIntent)
                .addAction(android.R.drawable.ic_media_play, "Buy it now", pplayIntent)
                .addAction(android.R.drawable.ic_media_next, "Later", pnextIntent)

 */
                .setDeleteIntent(getIntent(account_id))
                .setContentIntent(pIntent)
                .setAutoCancel(false).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager, DEFAULT_CHANNEL_ID);
            if (notificationManager.getNotificationChannel(DEFAULT_CHANNEL_ID) != null) {
                NotificationChannel nc = notificationManager.getNotificationChannel(DEFAULT_CHANNEL_ID);
                nc.setVibrationPattern(new long[]{0L});
                nc.enableVibration(true);
            }
        }

        return n;
    }

    public static void createNotificationChannel(NotificationManager notificationManager, String id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(DEFAULT_CHANNEL_ID) == null){
                notificationManager.createNotificationChannel(new NotificationChannel(
                        id, DEFAULT_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW
                ));
            }
        }
    }
    public PendingIntent getIntent(String account_key) {
        Intent intent = new Intent("eu.allan.manlyminder.birthday");
        intent.putExtra("account_key", account_key);
        PendingIntent detailsPendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

}
