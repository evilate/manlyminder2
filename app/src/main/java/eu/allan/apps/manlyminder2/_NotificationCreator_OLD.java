package eu.allan.apps.manlyminder2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.app.RemoteInput;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by allanfrederiksen on 10/10/2018.
 */

public class _NotificationCreator_OLD {

    private String TAG = getClass().getSimpleName();
    private Context context;
    public static int NOTIFICATION_ID = 1;
    public static final String KEY_NOTIFICATION_REPLY = "KEY_NOTIFICATION_REPLY";
    private PendingIntent intent;
    private NotificationManager notifManager;


    public static final int REPLY_INTENT_ID = 0;
    public static final int ARCHIVE_INTENT_ID = 1;

    public static final int REMOTE_INPUT_ID = 1247;

    public static final String LABEL_BTN_1 = "Bought it";
    public static final String LABEL_BTN_2 = "Buy ot now";
    public static final String LABEL_BTN_3 = "Remind me later";
    public static final String LABEL_ACTIVITY = "Archive";
    //public static final String REPLY_ACTION = "com.hitherejoe.notifi.util.ACTION_MESSAGE_REPLY";
    public static final String REPLY_ACTION = "eu.allan.ACTION_MESSAGE_REPLY";
    public static final String KEY_PRESSED_ACTION = "eu.allan.KEY_PRESSED_ACTION";
    public static final String KEY_TEXT_REPLY = "eu.allan.KEY_TEXT_REPLY";
    private static final String KEY_NOTIFICATION_GROUP = "eu.allan.KEY_NOTIFICATION_GROUP";

    private static String DEFAULT_CHANNEL_ID = "default_channel";
    private static String DEFAULT_CHANNEL_NAME = "Default";

    public _NotificationCreator_OLD(Context context) {
        this.context = context;
        //new _NotificationManager(context, "");
    }
    /*
    public void sendNotification(String id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            //Log.i(TAG, "\nsendNotificationPostOreo(id);");
            //createNotificationChannel(notificationManager);
            sendNotificationPostOreo(id);
        }else{
            //Log.i(TAG, "\nsendNotificationPreOreo(id);");
            sendNotificationPreOreo(id);
        }
    }


    public void sendNotificationPostOreo(String id) {
        //InputParser ip = new InputParser(id, context, true);
        //SaveAndLoadNew sl = new SaveAndLoadNew(context);
        Database db = new Database(context);
        //Log.i(TAG, "sendNotificationPostOreo");
        // Create PendingIntent to take us to ActivityHelp
        // as a result of notification action
        Intent mainIntent = new Intent(context, ActivityMain.class);
        Intent detailsIntent = new Intent(context, ActivityHelp.class);
        Intent configIntent = new Intent(context, ActivityConfiguration.class);
        //detailsIntent.putExtra("EXTRA_DETAILS_ID", 42);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(
                context,
                0,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        PendingIntent detailsPendingIntent = PendingIntent.getActivity(
                context,
                0,
                detailsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        PendingIntent configPendingIntent = PendingIntent.getActivity(
                context,
                0,
                configIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Define PendingIntent for Reply action
        //PendingIntent replyPendingIntent = null;
        // Call Activity on platforms that don't support DirectReply natively
        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                new Intent(context, ReplyReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Create RemoteInput and attach it to Notification Action
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_NOTIFICATION_REPLY)
                .setLabel("Add event")
                .build();
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_menu_save, "REMIND", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();


        String title = sl.loadStringValue("editText_default_notification");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                .setSmallIcon(R.drawable.calendar_blue_240px)
                .setContentTitle(title)
                .setContentText(ip.getContentText())
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)
                .setWhen(0)
                .setDeleteIntent(getIntent())
                .addAction(android.R.drawable.ic_menu_compass, "CONFIG", configPendingIntent)
                .addAction(android.R.drawable.ic_menu_compass, "HELP", detailsPendingIntent)
                .addAction(replyAction)

                //.setVibrate(null)
                //.setDefaults(Notification.DEFAULT_VIBRATE)
                .setVibrate(new long[] { 0L })
                //.setChannelId(DEFAULT_CHANNEL_ID)

                //.addAction(android.R.drawable.ic_menu_directions, "Show Map", detailsPendingIntent)
                ;

        Notification n = mBuilder.build();
        //n.sound = null;
        n.vibrate = new long[] { 0L };
        //n.defaults &= Notification.DEFAULT_SOUND;
        n.defaults &= Notification.DEFAULT_VIBRATE;

        // Obtain NotificationManager system service in order to show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            createNotificationChannel(notificationManager);
            NotificationChannel nc = notificationManager.getNotificationChannel(DEFAULT_CHANNEL_ID);
            //nc.enableVibration(false);
            //nc.shouldVibrate();
            nc.setVibrationPattern(new long[]{ 0L });
            nc.enableVibration(true);

            //Log.i("DEFAULT_CHANNEL_ID: ", DEFAULT_CHANNEL_ID);
        }

        notificationManager.notify(NOTIFICATION_ID, n);

        //showRemoteInputNotification(context);
    }

    public static void createNotificationChannel(NotificationManager notificationManager) {
        //Log.i("NotificationCreator", "createNotificationChannel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create channel only if it is not already created
            if (notificationManager.getNotificationChannel(DEFAULT_CHANNEL_ID) == null) {
                notificationManager.createNotificationChannel(new NotificationChannel(
                        DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW
                ));
            }

            //Log.i("NotificationCreator", "createNotificationChannel->CREATED");
        }
    }

    public void sendNotificationPreOreo(String id) {
        InputParser ip = new InputParser(id, context, true);
        SaveAndLoadNew sl = new SaveAndLoadNew(context);

        // Create PendingIntent to take us to ActivityHelp
        // as a result of notification action
        Intent mainIntent = new Intent(context, MainActivity.class);
        Intent detailsIntent = new Intent(context, ActivityHelp.class);
        Intent configIntent = new Intent(context, ActivityConfiguration.class);
        //detailsIntent.putExtra("EXTRA_DETAILS_ID", 42);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(
                context,
                0,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        PendingIntent detailsPendingIntent = PendingIntent.getActivity(
                context,
                0,
                detailsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        PendingIntent configPendingIntent = PendingIntent.getActivity(
                context,
                0,
                configIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Define PendingIntent for Reply action
        PendingIntent replyPendingIntent = null;
        // Call Activity on platforms that don't support DirectReply natively
        if (Build.VERSION.SDK_INT < 24) {
            replyPendingIntent = detailsPendingIntent;
        } else { // Call BroadcastReceiver on platforms supporting DirectReply
            replyPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    new Intent(context, ReplyReceiver.class),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }

        // Create RemoteInput and attach it to Notification Action
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_NOTIFICATION_REPLY)
                .setLabel("Add event")
                .build();
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_menu_save, "REMIND", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();


        String title = sl.loadStringValue("editText_default_notification");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.calendar_blue_240px)
                .setContentTitle(title)
                .setContentText(ip.getContentText())
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                .setDeleteIntent(getIntent())
                .addAction(android.R.drawable.ic_menu_compass, "CONFIG", configPendingIntent)
                .addAction(android.R.drawable.ic_menu_compass, "HELP", detailsPendingIntent)
                .addAction(replyAction)
                //.addAction(android.R.drawable.ic_menu_directions, "Show Map", detailsPendingIntent)
                ;

        // Obtain NotificationManager system service in order to show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Log.i(TAG, "Build.VERSION_CODES.O");
            //createNotificationChannel(notificationManager);


        }

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        //showRemoteInputNotification(context);
    }
    public PendingIntent getIntent() {
        Intent intent = new Intent("eu.allan.apps.notifications.receive");
        PendingIntent detailsPendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        //intent.putExtra("extra", phoneNo); \\ phoneNo is the sent Number
        //sendBroadcast(intent);
        //return detailsPendingIntent;
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
*/

}
