package eu.allan.apps.manlyminder2;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationSendflowers {

    private static final String TAG = "NotificationController";
    private final Database db;

    private Context context;
    private long timeOfDayToSend;
    private long nextReminderLong;
    private ArrayList<Long> reminders;

    private ArrayList<String> remindersText;

    private ArrayList<String> remindersButtonText_1;
    private ArrayList<String> remindersButtonText_2;
    private ArrayList<String> remindersButtonText_3;

    private int reminderSelector = 0;

    public NotificationSendflowers(Context context) {
        this.context = context;
        this.db = new Database(context);
        timeOfDayToSend = Long.parseLong(db.getSingleEntry("main_settings_timeofday"));

        checkAndSendNotification("sendflowers");
    }

    private void checkAndSendNotification(String notificationId){
        if(db.getSingleEntry(notificationId + "_service_active").equals("true")) {
            if(notificationId.equals("sendflowers")){
                long timeOfDay = this.timeOfDayToSend;//(1000*60*60*4);
                //long lastReminderSent = Long.parseLong(db.getSingleEntry(notificationId + "_last_reminder_sent"));
                long nextReminderSent = Long.parseLong(db.getSingleEntry(notificationId + "_next_reminder"));
                //printDate("lastReminderSent", lastReminderSent);
                printDate("Next Reminder: ", nextReminderSent);
                Calendar cldr = Calendar.getInstance();
                //cldr.setTimeInMillis(lastReminderSent + (1000*60*60*24*7*4l));
                cldr.set(Calendar.HOUR_OF_DAY, 0);
                cldr.set(Calendar.MINUTE, 0);
                cldr.set(Calendar.SECOND, 0);
                //printDate("Next Reminder: ", cldr.getTimeInMillis() + timeOfDay);
                if(System.currentTimeMillis() > Long.parseLong(db.getSingleEntry(notificationId + "_next_reminder"))){
                    //printDate("wtf: ", lastReminderSent + (1000*60*60*24*7*4l));
                    if(!db.getSingleEntry(notificationId + "_next_reminder").equals("0")) {
                        new _NotificationManager(context, notificationId, "Flowers", "Time for flowers", "Quick buy", "Remind later", "Nah");
                    }
                    //db.setSingleEntry(notificationId + "_last_reminder_sent", System.currentTimeMillis() + "");
                    db.setSingleEntry(notificationId + "_next_reminder", (System.currentTimeMillis() + (1000*60*60*24*7*4l) + timeOfDayToSend) + "");

                }
            }
        }

    }
    private void printDate(String out, long timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        Date c = cal.getTime();
        //System.out.println(out + " => " + c);
        Log.i(TAG, out + ": " + c);

    }
    /*
    public boolean isSendNotification(){
        boolean retval = false;

        if(db.getSingleEntry(notificationId + "_service_active").equals("true")) {

            System.out.println("\t<###\t" + notificationId + "\t###>");
            Log.i(TAG, notificationId + "_service_active: " + db.getSingleEntry(notificationId + "_service_active"));
            Log.i(TAG, notificationId + "_gift_bought: " + db.getSingleEntry(notificationId + "_gift_bought"));
            printDate(notificationId + "_next_reminder: ", Long.parseLong(db.getSingleEntry(notificationId + "_next_reminder")));
            printDate(notificationId + "_settings_date: ", Long.parseLong(db.getSingleEntry(notificationId + "_settings_date")));
            printDate(notificationId + ": getNextDate(): ", getNextDate());
            System.out.println("\t<######>");
            String date_str = db.getSingleEntry(notificationId + "_settings_date");
            if (!date_str.equals("0") && !date_str.equals("")) {
                if (System.currentTimeMillis() > Long.parseLong(db.getSingleEntry(notificationId + "_next_reminder"))) {
                    retval = true;
                }
                String last_reminder_sent = db.getSingleEntry(notificationId + "_last_reminder_sent");
                if (!last_reminder_sent.equals("0") && !last_reminder_sent.equals("")) {
                    long last_reminder_sent_long = Long.parseLong(last_reminder_sent);
                    if (last_reminder_sent_long + ((1000 * 60 * 60 * 24l)) > System.currentTimeMillis()) {
                        //Log.i(TAG, "Sigh: ");
                        retval = false;
                    }
                }
                if (db.getSingleEntry(notificationId + "_gift_bought").equals("true")) {
                    retval = false;
                }

            }
        }
        if(retval){
            new _NotificationManager(context, this.getNotificationId(), this.getTitle(), this.getText(), this.getButtonText_1(), this.getButtonText_2(), this.getButtonText_3());
            db.setSingleEntry(notificationId + "_last_reminder_sent", System.currentTimeMillis() + "");
        }
        return retval;
    }

    public long setNextReminderTime(){
        String nextReminder = "";
        if(notificationId.equals("birthday")){
            nextReminder = db.getSingleEntry("birthday_next_reminder");
        }else if(notificationId.equals("valentinesday")){
            nextReminder = db.getSingleEntry("valentinesday_next_reminder");
        }else if(notificationId.equals("sendflowers")){
            nextReminder = db.getSingleEntry("sendflowers_next_reminder");
        }
        long nextDate = getNextDate();
        //printDate("nextBirthDate", nextBirthDate);
        long now = System.currentTimeMillis();
        //Log.i(TAG, "\tnextReminder: " + nextReminder);
        //System.out.println("nextReminder: " + nextReminder);
        //if(!nextReminder.equals("")){
            reminders = getReminders();
        //Log.i(TAG, "\treminders: " + reminders.size());
            for(Long l: reminders){
                //System.out.println("l: " + l + "");

                if((nextDate - l) > now){
                    //Log.i(TAG, "\t(nextDate - l): " + (nextDate - l));
                    nextReminderLong = (nextDate - l) + timeOfDayToSend;
                    db.setSingleEntry(notificationId + "_next_reminder", nextReminderLong + "");
                    //Log.i(TAG, db.getSingleEntry("birthday_next_reminder"));
                    //printDate("SET birthday_next_reminder", nextReminderLong);
                    reminderSelector++;
                }
            }
        //reminderSelector = reminders.size()-1;
        //}


        return 0l;
    }

    private long getNextDate(){
        String birthdate_str = "0";
        if(notificationId.equals("birthday")){
            birthdate_str = db.getSingleEntry("birthday_settings_date");
        }else if(notificationId.equals("valentinesday")){
            birthdate_str = db.getSingleEntry("valentinesday_settings_date");
        }else if(notificationId.equals("sendflowers")){
            birthdate_str = db.getSingleEntry("sendflowers_settings_date");

        }

        long birthDate = Long.parseLong(birthdate_str);

        // Birthday
        Calendar calBirthday = Calendar.getInstance();
        calBirthday.setTimeInMillis(birthDate);

        int calBirthdayDay = calBirthday.get(Calendar.DATE);
        int calBirthdayMonth = calBirthday.get(Calendar.MONTH);

        // Now
        Calendar calNow = Calendar.getInstance();
        int calNowYear = calNow.get(Calendar.YEAR);
        calNow.set(Calendar.HOUR_OF_DAY, 0);
        calNow.set(Calendar.MINUTE, 0);
        calNow.set(Calendar.SECOND, 0);


        // Next birthday
        Calendar calNextBirthday = Calendar.getInstance();
        calNextBirthday.set(calNowYear, calBirthdayMonth, calBirthdayDay);
        calNextBirthday.set(Calendar.HOUR_OF_DAY, 0);
        calNextBirthday.set(Calendar.MINUTE, 0);
        calNextBirthday.set(Calendar.SECOND, 0);

        if(calNow.getTimeInMillis() > calNextBirthday.getTimeInMillis()){
            calNextBirthday.set(Calendar.YEAR, calNextBirthday.get(Calendar.YEAR)+1);
        }
        return calNextBirthday.getTimeInMillis();
    }

    private ArrayList<Long> getReminders(){

        reminders = new ArrayList<>();
        remindersText = new ArrayList<>();
        if(notificationId.equals("birthday")) {
            String nextReminder;
            nextReminder = db.getSingleEntry("reminder_schedule_1");
            if (nextReminder.equals("true")) {
                reminders.add((0l));
                remindersText.add("It's her birthday today!");
                remindersTitle.add("Birthday comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");
            }
            nextReminder = db.getSingleEntry("reminder_schedule_2");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 1000l));
                remindersText.add("It's her birthday tomorrow.");
                remindersTitle.add("Birthday comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_3");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 3 * 1000l));
                remindersText.add("It's her birthday in 3 days.");
                remindersTitle.add("Birthday comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_4");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 1000l));
                remindersText.add("It's her birthday in a week.");
                remindersTitle.add("Birthday comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_5");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 2 * 1000l));
                remindersText.add("It's her birthday in 2 weeks.");
                remindersTitle.add("Birthday comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_6");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 4 * 1000l));
                remindersText.add("It's her birthday in 4 weeks.");
                remindersTitle.add("Birthday comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_7");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 6 * 1000l));
                remindersText.add("It's her birthday in 6 weeks.");
                remindersTitle.add("Birthday comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }



        }else if(notificationId.equals("valentinesday")){
            String nextReminder;
            nextReminder = db.getSingleEntry("reminder_schedule_1");
            if (nextReminder.equals("true")) {
                reminders.add((0l));
                remindersText.add("It's Valentines today!");
                remindersTitle.add("Valentines comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");
            }
            nextReminder = db.getSingleEntry("reminder_schedule_2");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 1000l));
                remindersText.add("It's Valentines tomorrow.");
                remindersTitle.add("Valentines comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_3");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 3 * 1000l));
                remindersText.add("It's Valentines in 3 days.");
                remindersTitle.add("Valentines comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_4");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 1000l));
                remindersText.add("It's Valentines in a week.");
                remindersTitle.add("Valentines comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_5");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 2 * 1000l));
                remindersText.add("It's Valentines in 2 weeks.");
                remindersTitle.add("Valentines comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_6");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 4 * 1000l));
                remindersText.add("It's Valentines in 4 weeks.");
                remindersTitle.add("Valentines comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_7");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 6 * 1000l));
                remindersText.add("It's Valentines in 6 weeks.");
                remindersTitle.add("Valentines comming up");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }



        }else if(notificationId.equals("sendflowers")){
            String nextReminder;
            nextReminder = db.getSingleEntry("reminder_schedule_1");
            if (nextReminder.equals("true")) {
                reminders.add((0l));
                remindersText.add("It's been 6 weeks!");
                remindersTitle.add("Time for flowers");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");
            }
            nextReminder = db.getSingleEntry("reminder_schedule_2");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 1000l));
                remindersText.add("It's been almost 6 weeks!");
                remindersTitle.add("Time for flowers");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_3");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 3 * 1000l));
                remindersText.add("It's been almost 6 weeks!");
                remindersTitle.add("Time for flowers");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_4");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 1000l));
                remindersText.add("It's been 5 weeks!");
                remindersTitle.add("Time for flowers");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_5");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 2 * 1000l));
                remindersText.add("It's been 4 weeks!");
                remindersTitle.add("Time for flowers");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_6");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 4 * 1000l));
                remindersText.add("It's been 2 weeks!");
                remindersTitle.add("Time for flowers");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
            nextReminder = db.getSingleEntry("reminder_schedule_7");
            if (nextReminder.equals("true")) {
                reminders.add((60 * 60 * 24 * 7 * 6 * 1000l));
                remindersText.add("It's been 0 weeks!");
                remindersTitle.add("Time for flowers");
                remindersButtonText_1.add("Bought it");
                remindersButtonText_2.add("Buy it now");
                remindersButtonText_3.add("Later");

            }
        }
        return reminders;
    }



    public String getButtonText_1() {
        return remindersButtonText_1.get(reminderSelector);
    }
    public String getButtonText_2() {
        return remindersButtonText_2.get(reminderSelector);
    }
    public String getButtonText_3() {
        return remindersButtonText_3.get(reminderSelector);
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getTitle() {
        Log.i(TAG, "reminderSelector: " + reminderSelector);
        return remindersTitle.get(reminderSelector);
    }

    public String getText() {
        return remindersText.get(reminderSelector);
    }


*/

}
