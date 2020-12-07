package eu.allan.apps.manlyminder2;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class _Util {
    private static String TAG = "Util";
    // schedule the start of the service every 10 - 30 seconds
    public static void scheduleJob(Context context) {
        //Log.i(TAG, "Util->scheduleJob");
        String msg = "NOT ACTIVE";
        //SaveAndLoadNew sl = new SaveAndLoadNew(context);
        //String isChecked = sl.loadStringValue("is_active");
        boolean isTrue = true; //OLD
        long closestReminder = 0l;
        //if (isChecked.equals("true")) {

        if(isTrue){
            msg = "ACTIVE";
            Database db = new Database(context);
            // PMS reminder
            PmsController pc = new PmsController(context, "pms");
            if(pc.isReady()) {
                pc.sendReminder();
            }

            // Reminders
            ArrayList<String> reminders = new ArrayList();
            reminders.add("birthday");
            reminders.add("valentinesday");
            for(String notificationId: reminders) {
                NotificationController nc = new NotificationController(context, notificationId);
                boolean notificationSend = nc.isSendNotification();
                nc.printDate("Next " + notificationId.toUpperCase() + " Reminder", db.getSingleEntry(notificationId + "_next_reminder"));
                if(notificationSend){
                    if(!nc.isFirstRun()){ // hack
                        nc.sendNotification();
                        Log.i(TAG, notificationId.toUpperCase() + " notificationSend TRUE");
                    }
                    Log.i(TAG, notificationId.toUpperCase() + "notification SET NEW REMINDER");
                    nc.setNextReminderTime();
                }
            }


            ArrayList<String> remindersAnniversary = new ArrayList();
            // Anniversary
            String[][] table = db.queryTableAll("anniversaryreminders");
            if(table != null){
                for(String[] s: table){
                    Log.i(TAG, "anniversaryreminders: " + s[0]);
                    if(!s[1].equals("")) {
                        remindersAnniversary.add(s[0]);

                    }
                }
            }
            for(String notificationId: remindersAnniversary) {
                NotificationControllerAnniversary nca = new NotificationControllerAnniversary(context, notificationId);
                boolean notificationSend = nca.isSendNotification();
                nca.printDate("Next " + notificationId.toUpperCase() + " Reminder", db.getSingleEntry(notificationId + "_next_reminder"));
                if(notificationSend){
                    if(!nca.isFirstRun()){ // hack
                        nca.sendNotification();
                        Log.i(TAG, notificationId.toUpperCase() + " notificationSend TRUE");
                    }
                    Log.i(TAG, notificationId.toUpperCase() + "notification SET NEW REMINDER");
                    nca.setNextReminderTime();
                }
            }

            // Flowers
            NotificationSendflowers notifications_flowers = new NotificationSendflowers(context);
            ComponentName serviceComponent = new ComponentName(context, _TestJobService.class);
            JobInfo.Builder builder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                NotificationTimer nt = new NotificationTimer(context);
                long time = nt.getNextNotificationTime();

                long minLatency = 30 * 1000;
                long maxLatency = 60 * 1000;
                long timeTilNextNotification = time - System.currentTimeMillis();

                if(timeTilNextNotification < (1000*60*60*24l)) {
                    minLatency = timeTilNextNotification;
                    maxLatency = timeTilNextNotification + (1000 * 60 * 60l);

                }else{
                    minLatency = (1000*60*60*12l);
                    maxLatency = (1000*60*60*24l);

                }
                //nt.printDate("timeTilNextNotification", System.currentTimeMillis() + timeTilNextNotification);

                builder = new JobInfo.Builder(0, serviceComponent);

                builder.setMinimumLatency(30 * 1000); // wait at least
                builder.setOverrideDeadline(60 * 1000); // maximum delay
                builder.setPersisted(true);

                //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
                //builder.setRequiresDeviceIdle(true); // device should be idle
                //builder.setRequiresCharging(false); // we don't care if the device is charging or not
                JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
                jobScheduler.schedule(builder.build());
            }
        }
        //Log.i(TAG, msg);
    }

}