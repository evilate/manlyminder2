package eu.allan.apps.manlyminder2;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;


/**
 * Created by allanfrederiksen on 06/03/2018.
 */

public class _MainService extends Service {

    private String TAG = getClass().getSimpleName();
    private SharedPreferences myPrefs;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private _MainService context;
    private Database db;


    @Override
    public void onCreate() {
        Log.i(TAG, "###onCreate");
        db = new Database(this);
        //final _MailAccountHandler mah = new _MailAccountHandler(this);
        final Context ctx = getApplicationContext();
        this.myPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        context = this;
        //final Context context = this;
        //_ReminderUtil.scheduleJob(this);
        /*
        if(mah.hasAccounts()){
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    if(key.equals("mail_status")){
                        if(!isNotification(1)){
                            new _Autorun(context);
                        }
                    }
                }
            };
            myPrefs.registerOnSharedPreferenceChangeListener(listener);
        }
        */
        //new _Autorun(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            try {
                if (intent != null) {
                    if (intent.getAction().equals(_Constants.ACTION.STARTFOREGROUND_ACTION)) {

                    } else if (intent.getAction().equals(_Constants.ACTION.CHECK_UNREAD_MAIL)) {

                        //Log.i(TAG, "onStartCommand");
                    } else if (intent.getAction().equals(_Constants.ACTION.STARTBACKGROUND_ACTION)) {

                    } else if (intent.getAction().equals(_Constants.ACTION.OK_ACTION)) {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancel(1);
                        String notificationId = intent.getStringExtra("notification_key");
                        if(notificationId.equals("birthday")){
                            db.setSingleEntry("birthday_gift_bought", "true");
                        }else if(notificationId.equals("valentinesday")){
                            db.setSingleEntry("valentinesday_gift_bought", "true");
                        }else if(notificationId.equals("sendflowers")){
                            db.setSingleEntry("sendflowers_gift_bought", "true");
                        }
                        Log.i(TAG, "Yeepee");
                        db.setSingleEntry("birthday_gift_bought", "true");
                    } else if (intent.getAction().equals(_Constants.ACTION.CLUTTER_ACTION)) {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancel(1);
                        String[][] notificationDb = db.queryTableAll("notification");
                        String[] notification = notificationDb[0];
                        String account_from_email = notification[2];
                        String account_from_name = notification[3];
                    } else if (intent.getAction().equals(_Constants.ACTION.CLUTTER_ACTION_NOTIFY)) {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancel(1);
                        String[][] notificationDb = db.queryTableAll("notification");
                        String[] notification = notificationDb[0];
                        String account_from_email = notification[2];
                        String account_from_name = notification[3];
                    }
                }
            } catch (Exception e) {
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
            }

        return START_STICKY;
    }

    public void sendNotification() {
        //_NotificationManager nc = new _NotificationManager(this);
        //NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //notificationManager.notify(1, nc.getNotification());
    }


    public boolean isNotification(int nid){
        boolean isNotification = false;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
            for (StatusBarNotification activeNotification : activeNotifications) {
                if (activeNotification.getId() == nid) {
                    isNotification = true;
                }
            }
        }
        return isNotification;
    }



    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {

    }
/*
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

 */
}