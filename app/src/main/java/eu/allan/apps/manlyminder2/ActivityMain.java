package eu.allan.apps.manlyminder2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = "ActivityMain";
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);
        setContentView(R.layout.activity_main_v2);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        Log.d("ActivityMain", "WTF");
        this.db = new Database(this);
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        //db.setSingleEntry("appversion", "Build: " + versionCode + "/" + versionName);
        TextView versionInfo = (TextView)findViewById(R.id.textView_dev_version);
        versionInfo.setText("Build: " + versionCode + " / Version: " + versionName);
        //db.deleteTable("pmslog");
        //db.listTable("pmslog");
        Log.d(TAG, "#####");
        //db.showAll();
        Log.d(TAG, "#####");
        //db.setSingleEntry("main_settings_timeofday", (1000*60*60*8) + "");

        // TEST DB
        //databaseTest();
        boolean firstrun = true;
        String version = "1";
        if(db.getSingleEntry("firstrun").equals(version)) {
            firstrun = false;
        }
        if(firstrun) {
            Log.i(TAG, "FIRST RUN");
            preset();
            db.setSingleEntry("firstrun", version);
        }
        Intent intent = new Intent(ActivityMain.this, ActivityBuy.class);
        //intent.putExtra("datepickerresult", )
        //startActivityForResult(intent,1);
        //startActivity(intent);

        /*
        String[][] table = db.queryTableAll("pmslog");

        if(table != null){
            for(String[] s: table){
                //for(int i = 0;i<s.length;i++){
                if(s[1].equals("")) {
                    db.deleteTableRow("pmslog", s[0]);
                    Log.i(TAG, "Deleting item: " + s[0]);
                }
                //}
            }
        }

         */
        //db.deleteTable("pmslog");
        //db.deleteTableRow("pmslog", "1");
        //db.listTable("pmslog");

        //if(table != null){
            //Log.i(TAG, "Table length: " + table.length);
            //Log.i(TAG, "Single: " + db.getSingleEntry("table_pmslog_1_0"));
            //Log.i(TAG, "Single: " + db.getSingleEntry("table_pmslog_2_0"));

        //}
        //db.showAll();
        //db.listTable("anniversaryreminders");

        //db.showAll();
        //db.deleteTable("pmslog");
        _Util.scheduleJob(this);

        Log.i(TAG, "\tBuild: " + versionCode + "/" + versionName);
        //db.setSingleEntry("birthday_next_reminder", "0");
        //NotificationController nc = new NotificationController(this);

        Button btDev = (Button)findViewById(R.id.button_dev_1);
        btDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                db.setSingleEntry("birthday_next_reminder", (System.currentTimeMillis() + 60000) + "");
                db.setSingleEntry("valentinesday_next_reminder", (System.currentTimeMillis() + 120000) + "");
                db.setSingleEntry("sendflowers_next_reminder", (System.currentTimeMillis() + 180000) + "");

 */

                db.setSingleEntry("birthday_next_reminder", (System.currentTimeMillis()) + "");
                db.setSingleEntry("valentinesday_next_reminder", (System.currentTimeMillis()) + "");
                db.setSingleEntry("sendflowers_next_reminder", (System.currentTimeMillis()) + "");

                Log.i(TAG, "CLICKED");
                /*
                db.clearAll();
                preset();
                db.setSingleEntry("firstrun", "true");

                 */
                //Intent intent = new Intent(ActivityMain.this, MyDatePicker.class);
                //intent.putExtra("datepickerresult", )
                //startActivityForResult(intent,1);

                //ActivityMain.this.preset();
                //Log.i(TAG, "Next reminder: " + db.getSingleEntry("birthday_settings_birthday"));

                //new _NotificationManager(ActivityMain.this);
            }
        });
        Button btDev2 = (Button)findViewById(R.id.button_dev_2);
        btDev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.showAll();
                db.clearAll();
                Log.i(TAG, "####################### showAll #######################");
                db.showAll();
                db.setSingleEntry("firstrun", "true");
                //preset();
                ActivityMain.this.finish();
                //db.setSingleEntry("firstrun", "true");
                //db.setSingleEntry("birthday_next_reminder", (System.currentTimeMillis() + (1000*120l)) + "");
                //Intent intent = new Intent(ActivityMain.this, MyDatePicker.class);
                //intent.putExtra("datepickerresult", )
                //startActivityForResult(intent,1);

                //ActivityMain.this.preset();
                //Log.i(TAG, "Next reminder: " + db.getSingleEntry("birthday_settings_birthday"));

                //new _NotificationManager(ActivityMain.this);
            }
        });
        Button btDev3 = (Button)findViewById(R.id.button_dev_3);
        btDev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new _NotificationManager(ActivityMain.this);
            }
        });
        LinearLayout tvSettings = (LinearLayout)findViewById(R.id.act_settings);
        tvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("DAK", "DAKKE DAK");
                Intent intent = new Intent(ActivityMain.this, ActivitySettings.class);
                startActivity(intent);
            }
        });
        LinearLayout tvBirthday = (LinearLayout)findViewById(R.id.act_birthday);
        tvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityMain.this, ActivityBirthday.class);
                startActivity(intent);
            }
        });
        LinearLayout tvValentines = (LinearLayout)findViewById(R.id.act_valentines);
        tvValentines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityMain.this, ActivityValentines.class);
                startActivity(intent);
            }
        });
        LinearLayout tvAnniversery = (LinearLayout)findViewById(R.id.act_anniversary);
        tvAnniversery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityMain.this, ActivityAnniversary.class);
                startActivity(intent);
            }
        });
        LinearLayout tvFlowers = (LinearLayout)findViewById(R.id.act_flowers);
        tvFlowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityMain.this, ActivityFlowers.class);
                startActivity(intent);
            }
        });
        LinearLayout tvPms = (LinearLayout)findViewById(R.id.act_pms);
        tvPms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityMain.this, ActivityPms.class);
                startActivity(intent);
            }
        });
    }

    private void databaseTest() {
        String[] data = new String[3];
        data[0] = "one";
        data[1] = "two";
        data[2] = "three";
        db.insertInTable("testtable", data);
        data[0] = "four";
        data[1] = "five";
        data[2] = "six";
        db.updateTableRowById("testtable", data, 0);

        String[][] table = db.queryTableAll("testtable");
        for(String[] s: table){
            for(int i = 0;i<s.length;i++){
                Log.i(TAG, s[i]);
            }
        }

    }

    private void preset() {
        db.clearAll();
        db.setSingleEntry("birthday_settings_date", "0");
        db.setSingleEntry("birthday_next_reminder", "0");
        db.setSingleEntry("birthday_last_reminder_sent", System.currentTimeMillis() + "");
        db.setSingleEntry("birthday_gift_bought", "false");
        db.setSingleEntry("birthday_next_reminder", "0");
        db.setSingleEntry("birthday_service_active", "false"); // Ret

        db.setSingleEntry("valentinesday_settings_date", "824252400043");
        db.setSingleEntry("valentinesday_next_reminder", "0");
        db.setSingleEntry("valentinesday_last_reminder_sent", System.currentTimeMillis() + "");
        db.setSingleEntry("valentinesday_gift_bought", "false");
        db.setSingleEntry("valentinesday_next_reminder", "0");
        db.setSingleEntry("valentinesday_service_active", "false"); // Ret

        long newDate = (System.currentTimeMillis() - ((1000*60*60*24*7*4l)+(1000*60*60*24)));
        Log.i(TAG, newDate + "");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(newDate);
        Date c = cal.getTime();
        Log.i(TAG, "flowerDay: " + c);
        db.setSingleEntry("sendflowers_settings_date", newDate + ""); //(System.currentTimeMillis() - (1000*60*60*24*7*62)
        db.setSingleEntry("sendflowers_next_reminder", "0");
        db.setSingleEntry("sendflowers_last_reminder_sent", System.currentTimeMillis() + "");
        db.setSingleEntry("sendflowers_gift_bought", "false");
        db.setSingleEntry("sendflowers_next_reminder", "0");
        db.setSingleEntry("sendflowers_service_active", "false"); // Ret

        db.setSingleEntry("pms_service_active", "false");
        db.setSingleEntry("pms_service_period", "false");
        db.setSingleEntry("pms_service_ovul", "false");
        db.setSingleEntry("pms_service_pms", "false");



        db.setSingleEntry("main_settings_timeofday", (1000*60*60*8) + "");
        db.setSingleEntry("reminder_schedule_1", "true");
        db.setSingleEntry("reminder_schedule_2", "true");
        db.setSingleEntry("reminder_schedule_3", "true");
        db.setSingleEntry("reminder_schedule_4", "true");
        db.setSingleEntry("reminder_schedule_5", "true");
        db.setSingleEntry("reminder_schedule_6", "true");
        db.setSingleEntry("reminder_schedule_7", "true");


        Log.i(TAG, "Database cleared, presets loaded.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Retrieve data in the intent
//        String editTextValue = data.getStringExtra("valueId");
        //Log.i(TAG, editTextValue + " WOOHOO");
    }
    @Override
    public void onBackPressed() {
        // Handle the back button event
        Log.i(TAG, "onBackPressed");
        finish();
    }
}
