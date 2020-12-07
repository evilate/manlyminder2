package eu.allan.apps.manlyminder2;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationTimer {

    private static final String TAG = "NotificationTimer";
    private final Database db;
    private final Context context;


    public NotificationTimer(Context context) {
        this.context = context;
        this.db = new Database(context);

    }
    public long getNextNotificationTime(){
        long retval = System.currentTimeMillis() + (1000*60*60*72);
        long now = System.currentTimeMillis();// + (1000*60*60*24);
        //Log.i("\t\t CHECK: ", "");
        String notificationId = "birthday";
        if(db.getSingleEntry(notificationId + "_service_active").equals("true")) {
            long next = Long.parseLong(db.getSingleEntry(notificationId + "_next_reminder"));
            //printDate("notificationId: " + notificationId, next);
            if (next > now && next < retval) {
                retval = next;

            }
        }
        notificationId = "valentinesday";
        if(db.getSingleEntry(notificationId + "_service_active").equals("true")) {
            long next = Long.parseLong(db.getSingleEntry(notificationId + "_next_reminder"));
            //printDate("notificationId: " + notificationId, next);
            if (next > now && next < retval) {
                retval = next;

            }
        }
        notificationId = "sendflowers";
        if(db.getSingleEntry(notificationId + "_service_active").equals("true")) {
            long next = Long.parseLong(db.getSingleEntry(notificationId + "_next_reminder"));
            //printDate("notificationId: " + notificationId, next);
            if (next > now && next < retval) {
                retval = next;

            }
        }
        notificationId = "pms";
        if(db.getSingleEntry(notificationId + "_service_active").equals("true")) {
            long next = Long.parseLong(db.getSingleEntry(notificationId + "_next_reminder"));
            //printDate("notificationId: " + notificationId, next);
            if (next > now && next < retval) {
                retval = next;

            }
        }
        //printDate("getNextNotificationTime", retval);
        return retval;
    }

    public void printDate(String out, long timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        Date c = cal.getTime();
        //System.out.println(out + " => " + c);
        Log.i(TAG, out + ": " + c);

    }


}
