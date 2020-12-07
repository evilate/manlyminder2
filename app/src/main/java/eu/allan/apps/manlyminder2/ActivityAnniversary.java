package eu.allan.apps.manlyminder2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class ActivityAnniversary extends AppCompatActivity {
    private static final String TAG = "ActivityAnniversary";
    private TextView saveData;
    private TextView cancelBtn;
    private CheckBox serviceActive;
    private Database db;
    Button btDev;
    private String activityReturnValue;
    private String activityReturnValueText;
    private ArrayList<String> deletePmsIds;
    private ArrayList<String> onCancelDeletePmsIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        db = new Database(this);
        saveData = (TextView)findViewById(R.id.tv_btn_save);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("DAK", "DAKKE DAK");
                ActivityAnniversary.this.save();
            }
        });
        cancelBtn = (TextView)findViewById(R.id.tv_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(String s: onCancelDeletePmsIds){
                    db.deleteTableRow("anniversaryreminders", s);
                }
                ActivityAnniversary.this.finish();
            }
        });

        this.deletePmsIds = new ArrayList<String>();
        this.onCancelDeletePmsIds = new ArrayList<String>();

        this.serviceActive = (CheckBox)findViewById(R.id.checkBox);
        String sa = db.getSingleEntry("anniversary_service_active");
        Log.i(TAG, "anniversary_service_active: " + sa);
        if(sa.equals("true")){
            serviceActive.setChecked(true);
        }
        activityReturnValue = "0";
        activityReturnValueText = "";

        btDev = (Button)findViewById(R.id.button_add);
        btDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("DAK", "DAKKE DAK");
                Intent intent = new Intent(ActivityAnniversary.this, ActivityAnniversaryAdd.class);
                //intent.putExtra("datepickerresult", )
                startActivityForResult(intent,1);

                //new _NotificationManager(ActivityMain.this);
            }
        });
        String[][] table = db.queryTableAll("anniversaryreminders");
        if(table != null){
            for(String[] s: table){
                //for(int i = 0;i<s.length;i++){
                Log.i(TAG, "TAAAG: " + s[0]);
                Calendar cldr = Calendar.getInstance();

                if(!s[1].equals("")) {
                    cldr.setTimeInMillis(Long.parseLong(s[1]));
                    Date c = cldr.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);

                    addItem(formattedDate, s[2], s[0]);
                    Log.i(TAG, "Added item: " + formattedDate + "/" + s[1]);
                    //db.deleteTableRow("pmslog", s[0]);
                }else{
                    db.deleteTableRow("anniversaryreminders", s[0]);
                }
                //}
            }
        }
    }
    private void addItem(String text, String date, String id) {

        final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.activity_anniversary_item, null);
        TextView tv = view.findViewById(R.id.tv_text);
        tv.setText(text);
        TextView tvt = view.findViewById(R.id.tv_date);
        tvt.setText(date);

        final TextView tvId = view.findViewById(R.id.tv_id);
        tvId.setText(id);

        LinearLayout container = (LinearLayout) findViewById(R.id.ll_anniversary);
        Button delBtn = (Button)view.findViewById(R.id.btn_log);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityAnniversary.this.deletePmsIds.add(tvId.getText().toString());
                ((ViewGroup) view.getParent()).removeView(view);
                Log.i(TAG, "ID: " + tvId.getText().toString());
            }
        });
        container.addView(view);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Retrieve data in the intent
        this.activityReturnValue = data.getStringExtra("valueId");
        this.activityReturnValueText = data.getStringExtra("valueText");
        Log.i(TAG, "this.activityReturnValueText: " + this.activityReturnValueText);
        String[] s = new String[3];
        s[0] = activityReturnValue;
        s[1] = activityReturnValueText;
        s[2] = "false";
        int id = db.insertInTable("anniversaryreminders", s);
        ActivityAnniversary.this.onCancelDeletePmsIds.add(id + "");

        Calendar cldr = Calendar.getInstance();
        cldr.setTimeInMillis(Long.parseLong(this.activityReturnValue));
        Date c = cldr.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        ActivityAnniversary.this.addItem(formattedDate, s[2], id + "");
        /*
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

         */
        //Log.i(TAG, activityReturnValue + " / " + formattedDate);
    }
    private void save() {
        if(serviceActive.isChecked()){
            db.setSingleEntry("anniversary_service_active", "true");
        }else{
            db.setSingleEntry("anniversary_service_active", "false");
        }
        for(String s: deletePmsIds){
            db.deleteTableRow("anniversaryreminders", s);
        }
        String[][] table = db.queryTableAll("anniversaryreminders");
        if(table != null){
            for(String[] s: table){
                Calendar cldr = Calendar.getInstance();
                if(!s[1].equals("")) {
                    String[] update = new String[3];
                    update[0] = s[1];
                    update[1] = s[2];
                    update[2] = "true";
                    db.updateTableRowById("anniversaryreminders", update, Integer.parseInt(s[0]));

                }
            }
        }
        Log.i(TAG, "LIST TABLE");
        db.listTable("anniversaryreminders");
        finish();
    }
    @Override
    public void onBackPressed() {
        // Handle the back button event
        Log.i(TAG, "onBackPressed");
        finish();
    }
}
