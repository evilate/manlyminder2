package eu.allan.apps.manlyminder2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class ActivityBirthday extends AppCompatActivity {
    private static final String TAG = "ActivityBirthday";
    Button btDev;

    private String activityReturnValue;
    private CheckBox serviceActive;
    private Database db;
    private TextView nextReminder;
    private TextView saveData;
    private TextView cancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        db = new Database(this);



        activityReturnValue = "0";

        btDev = (Button)findViewById(R.id.btn_pick_birthday);
        btDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("DAK", "DAKKE DAK");
                Intent intent = new Intent(ActivityBirthday.this, MyDatePicker.class);
                //intent.putExtra("datepickerresult", )
                startActivityForResult(intent,1);

                //new _NotificationManager(ActivityMain.this);
            }
        });

        String entry = db.getSingleEntry("birthday_settings_date");
        Calendar cldr = Calendar.getInstance();
        if(!entry.equals("0")){
            cldr.setTimeInMillis(Long.parseLong(entry));
        }else{
            cldr.setTimeInMillis(System.currentTimeMillis());
        }
        Date c = cldr.getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        btDev.setText(formattedDate + " 📆");


        saveData = (TextView)findViewById(R.id.tv_btn_save);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("DAK", "DAKKE DAK");
                ActivityBirthday.this.save();
            }
        });
        cancelBtn = (TextView)findViewById(R.id.tv_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityBirthday.this.finish();
            }
        });

        this.serviceActive = (CheckBox)findViewById(R.id.checkBox);
        String sa = db.getSingleEntry("birthday_service_active");
        Log.i(TAG, "birthday_service_active: " + sa);
        if(sa.equals("true")){
            serviceActive.setChecked(true);
        }
        if(serviceActive.isChecked()){
            db.setSingleEntry("birthday_service_active", "true");
        }else{
            db.setSingleEntry("birthday_service_active", "false");
        }
        String nr = db.getSingleEntry("birthday_next_reminder");
        this.nextReminder = (TextView)findViewById(R.id.textView_next_reminder);
        if(!nr.equals("")){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(nr));
            c = cal.getTime();
            df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            formattedDate = df.format(c);
            nextReminder.setText("Next reminder: " + formattedDate + "");
        }
        Log.i(TAG, "Create");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Retrieve data in the intent
        this.activityReturnValue = data.getStringExtra("valueId");
        Calendar cldr = Calendar.getInstance();
        cldr.setTimeInMillis(Long.parseLong(activityReturnValue));
        cldr.set(Calendar.HOUR_OF_DAY, 0);
        cldr.set(Calendar.MINUTE, 0);
        cldr.set(Calendar.SECOND, 0);
        activityReturnValue = cldr.getTimeInMillis() + "";
        Date c = cldr.getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        btDev.setText(formattedDate + " 📆");
        NotificationController nc = new NotificationController(this, "birthday");
        //nc.setNextReminderTime();
        long nr = nc.getNextReminderTimeForTemp("" + activityReturnValue);
        nc.printDate("ActivityBirthday", nr);
        if(nr != 0L){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(nr);
            c = cal.getTime();
            df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            formattedDate = df.format(c);
            nextReminder.setText("Next reminder:: " + formattedDate + "");
        }

        Log.i(TAG, activityReturnValue + " / " + formattedDate);
    }
    private void save() {
        String checkForSave = "0";
        if(activityReturnValue.equals("0")){
            String retval = db.getSingleEntry("birthday_settings_date");
            if(!retval.equals("0") || !retval.equals("")){
                activityReturnValue = retval;
            }
        }
        if (!activityReturnValue.equals("0")) {
            db.setSingleEntry("birthday_settings_date", activityReturnValue);
            Calendar cldr = Calendar.getInstance();
            cldr.setTimeInMillis(Long.parseLong(activityReturnValue));
            cldr.set(Calendar.HOUR_OF_DAY, 0);
            cldr.set(Calendar.MINUTE, 0);
            cldr.set(Calendar.SECOND, 0);

            Date c = cldr.getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH:mm", Locale.getDefault());
            String formattedDate = df.format(c);
            db.setSingleEntry("birthday_settings_date", activityReturnValue);
            db.setSingleEntry("birthday_next_reminder", "0");
//            db.setSingleEntry("birthday_last_reminder_sent", System.currentTimeMillis() + "");
            db.setSingleEntry("birthday_last_reminder_sent", System.currentTimeMillis() + "");
            db.setSingleEntry("birthday_gift_bought", "false");

            Log.i(TAG, "SAVED: " + activityReturnValue + " / " + formattedDate);
        }
        if(serviceActive.isChecked()){
            db.setSingleEntry("birthday_service_active", "true");
        }else{
            db.setSingleEntry("birthday_service_active", "false");
        }
        db.setSingleEntry("birthday_next_reminder", "0");
        NotificationController nc = new NotificationController(this, "birthday");
        nc.setNextReminderTime();
        finish();
    }
    @Override
    public void onBackPressed() {
        // Handle the back button event
        Log.i(TAG, "onBackPressed");
        finish();
    }
}
