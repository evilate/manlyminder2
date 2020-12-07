package eu.allan.apps.manlyminder2;

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

public class ActivityFlowers extends AppCompatActivity {
    private static final String TAG = "ActivityValentines";
    private TextView saveData;
    private TextView cancelBtn;
    private CheckBox serviceActive;
    private Database db;
    private TextView nextReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowers);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        this.serviceActive = (CheckBox)findViewById(R.id.checkBox);

        this.db = new Database(this);
        String sa = db.getSingleEntry("sendflowers_service_active");
        Log.i(TAG, "sendflowers_service_active: " + sa);
        if(sa.equals("true")){
            serviceActive.setChecked(true);
        }
        if(serviceActive.isChecked()){
            db.setSingleEntry("sendflowers_service_active", "true");
        }else{
            db.setSingleEntry("sendflowers_service_active", "false");
        }
        saveData = (TextView)findViewById(R.id.tv_btn_save);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("DAK", "DAKKE DAK");
                ActivityFlowers.this.save();
            }
        });
        cancelBtn = (TextView)findViewById(R.id.tv_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityFlowers.this.finish();
            }
        });
        String nr = db.getSingleEntry("sendflowers_next_reminder");
        this.nextReminder = (TextView)findViewById(R.id.textView_next_reminder);
        if(!nr.equals("") && !nr.equals("0")){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(nr));
            Date c = cal.getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            nextReminder.setText("Next reminder: " + formattedDate + "");
        }
        Log.i(TAG, "Create");
    }

    private void save() {
        //String activityReturnValue = (System.currentTimeMillis() - (1000*60*60*24*7*6)) + "";
        String activityReturnValue = (System.currentTimeMillis()+ (1000*60*60*24*28*2L)) + "";
        if (!activityReturnValue.equals("0")) {
            db.setSingleEntry("sendflowers_settings_date", activityReturnValue);
            Calendar cldr = Calendar.getInstance();
            cldr.setTimeInMillis(Long.parseLong(activityReturnValue));
            cldr.set(Calendar.HOUR_OF_DAY, 0);
            cldr.set(Calendar.MINUTE, 0);
            cldr.set(Calendar.SECOND, 0);

            Date c = cldr.getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH:mm", Locale.getDefault());
            String formattedDate = df.format(c);
            db.setSingleEntry("sendflowers_settings_date", activityReturnValue);
//            db.setSingleEntry("birthday_last_reminder_sent", System.currentTimeMillis() + "");
            db.setSingleEntry("sendflowers_last_reminder_sent", System.currentTimeMillis() + "");
            db.setSingleEntry("sendflowers_gift_bought", "false");
            db.setSingleEntry("sendflowers_next_reminder", (System.currentTimeMillis() + (1000*60*60*24*28L)) + "");
            Log.i(TAG, "SAVED: " + activityReturnValue + " / " + formattedDate);
        }
        if(serviceActive.isChecked()){
            db.setSingleEntry("sendflowers_service_active", "true");
        }else{
            db.setSingleEntry("sendflowers_service_active", "false");
            db.setSingleEntry("sendflowers_next_reminder", "0");
        }
        finish();
    }
    @Override
    public void onBackPressed() {
        // Handle the back button event
        Log.i(TAG, "onBackPressed");
        finish();
    }
}
