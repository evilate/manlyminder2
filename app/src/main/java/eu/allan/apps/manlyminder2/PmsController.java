package eu.allan.apps.manlyminder2;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PmsController {

    private static final String TAG = "NotificationController";
    private final Database db;
    private final String notificationId;

    private Context context;
    private long timeOfDayToSend;
    private String title;
    private String text;

    private String buttonText_1;

    private long lastPeriod;

    public PmsController(Context context, String notificationId) {
        this.context = context;
        this.notificationId = notificationId;
        this.db = new Database(context);

        timeOfDayToSend = Long.parseLong(db.getSingleEntry("main_settings_timeofday"));

        //printDate("PmsController pms_next_reminder", db.getSingleEntry("pms_next_reminder"));


    }
    public boolean isReady(){
        boolean rv = false;
        String[][] table = db.queryTableAll("pmslog");
        if(table != null){
            String sa = db.getSingleEntry("pms_service_active");
            if(sa.equals("true")) {
                rv = true;
            }
        }

        return rv;
    }
    public void sendReminder(){
        String[][] row = db.queryTableAll("pmslog");
        if(row != null) {
            long now = System.currentTimeMillis();
            String nr = db.getSingleEntry("pms_next_reminder");
            long next = 0;
            //Log.i(TAG, "nr: " + nr);
            if(!nr.equals("")){
                next = Long.parseLong(nr);
            }

            if (now > next) {
                // Send reminder
                Log.i(TAG, "now / next: " + now + "/" + next);
                setText();
                new _NotificationManagerPms(context, "pms", this.title, this.text, this.buttonText_1);
                // Re set reminder
                db.setSingleEntry("pms_next_reminder", getNextReminder() + "");
            }
        }
    }
    private void setText(){
        long retval = 0l;
        long day = (1000*60*60*24l);
        //long now = System.currentTimeMillis();
        Calendar calNow = Calendar.getInstance();

        calNow.set(Calendar.HOUR_OF_DAY, 0);
        calNow.set(Calendar.MINUTE, 0);
        calNow.set(Calendar.SECOND, 0);
        long now = calNow.getTimeInMillis();
        int id = findLastestPeriod();
        if(id != -1){
            String[] row = db.getRowById("pmslog", id);
            long lastPeriod = Long.parseLong(row[1]);
            Calendar calPeriod = Calendar.getInstance();
            calPeriod.setTimeInMillis(lastPeriod);
            calPeriod.set(Calendar.HOUR_OF_DAY, 0);
            calPeriod.set(Calendar.MINUTE, 0);
            calPeriod.set(Calendar.SECOND, 0);
            long period = calPeriod.getTimeInMillis();
            if(now > period && now < period + (day*7l)){
                // Period ended - energy
                retval = (period + (day*7l));
                setReminderText(1);
            }else if(now > period + (day*7l) && now < period + (day*14l)){
                // Low energy
                retval = (period + (day*14l));
                setReminderText(2);
            }else if(now > period + (day*14l) && now < period + (day*21l)){
                // PMS
                retval = (period + (day*21l));
                setReminderText(3);
            }else if(now > period + (day*21l) && now < period + (day*28l)) {
                // Period
                retval = (period + (day*28l));
                setReminderText(0);
            }

        }
    }
    public long getNextReminder(){
        long retval = 0l;
        long day = (1000*60*60*24l);
        //long now = System.currentTimeMillis();
        Calendar calNow = Calendar.getInstance();

        calNow.set(Calendar.HOUR_OF_DAY, 0);
        calNow.set(Calendar.MINUTE, 0);
        calNow.set(Calendar.SECOND, 0);
        long now = calNow.getTimeInMillis();
        int id = findLastestPeriod();
        if(id != -1) {
            String[] row = db.getRowById("pmslog", id);
            long lastPeriod = Long.parseLong(row[1]);
            Calendar calPeriod = Calendar.getInstance();
            calPeriod.setTimeInMillis(lastPeriod);
            calPeriod.set(Calendar.HOUR_OF_DAY, 0);
            calPeriod.set(Calendar.MINUTE, 0);
            calPeriod.set(Calendar.SECOND, 0);
            long period = calPeriod.getTimeInMillis();

            if (now > period && now < period + (day * 7l)) {
                // Period ended - energy
                retval = (period + (day * 7l));
                setReminderText(1);
            } else if (now > period + (day * 7l) && now < period + (day * 14l)) {
                // Low energy
                retval = (period + (day * 14l));
                setReminderText(2);
            } else if (now > period + (day * 14l) && now < period + (day * 21l)) {
                // PMS
                retval = (period + (day * 21l));
                setReminderText(3);
            } else if (now > period + (day * 21l) && now < period + (day * 28l)) {
                // Period
                retval = (period + (day * 28l));
                setReminderText(0);
            } else if (now > period + (day * 28l)) {
                // Period
                long overdue = now - period;
                retval = (period + (overdue) + (day));
                setReminderText(4);
                /*
                String nr = db.getSingleEntry("pms_next_reminder");
                long next = (period + (day * 28l));
                if(!nr.equals("")){
                    next = Long.parseLong(nr);
                }
                retval = next + (day);

                 */
            }
        }
        //Log.i(TAG, "getNextReminder retval: " + retval);
        printDate("getNextReminder retval", retval);
        return retval + this.timeOfDayToSend;
    }

    private void setReminderText(int textId){
        if(textId == 0){
            this.title = "Period beginning";
            this.text = "Time for her period";
            this.buttonText_1 = "Log first day";
        }else if(textId == 1){
            this.title = "Period ended";
            this.text = "High energy";
            this.buttonText_1 = "";
        }else if(textId == 2){
            this.title = "Ovulation time";
            this.text = "High propabillity for pregnacy";
            this.buttonText_1 = "";
        }else if(textId == 3){
            this.title = "PMS";
            this.text = "Be nice(er)";
            this.buttonText_1 = "";
        }else if(textId == 4){
            this.title = "Her period will begin soon";
            this.text = "Please log for precision notification";
            this.buttonText_1 = "Log first day";
        }
    }


    private int findLastestPeriod(){
        String[][] table = db.queryTableAll("pmslog");
        int id = -1;
        long time = 0l;
        if(table != null){
            for(String[] s: table){
                if(!s[1].equals("")) {
                    long newTime = Long.parseLong(s[1]);
                    printDate("\tLatest period: ", newTime);
                    if(newTime > time){
                        time = newTime;
                        id = Integer.parseInt(s[0]);

                    }
                }
            }
        }
        Log.i(TAG, "findLastestPeriod return: " + id);
        return id;
    }
    private void printDate(String out, long timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        Date c = cal.getTime();
        //System.out.println(out + " => " + c);
        Log.i(TAG, out + ": " + c);

    }
    private void printDate(String out, String timestamp){
        long ts = 0l;
        if(!timestamp.equals("") && !timestamp.equals("0")){
            ts = Long.parseLong(timestamp);
        }
        printDate(out, ts);
    }


}
