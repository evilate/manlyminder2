package eu.allan.apps.manlyminder2;

import android.app.DatePickerDialog;
import android.content.Context;
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

public class ActivityPms extends AppCompatActivity {
    private static final String TAG = "ActivityPms";
    private CheckBox serviceActive;
    private CheckBox servicePeriod;
    private CheckBox serviceOvul;
    private CheckBox servicePms;
    private Database db;
    private TextView nextReminder;
    private TextView saveData;
    private TextView cancelBtn;
    private DatePickerDialog picker;
    private Button timePickerButton;
    private ArrayList<String> deletePmsIds;
    private ArrayList<String> onCancelDeletePmsIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pms);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        db = new Database(this);
        this.deletePmsIds = new ArrayList<String>();
        this.onCancelDeletePmsIds = new ArrayList<String>();
        setup();
    }

    private void setup() {
        timePickerButton = (Button) findViewById(R.id.btn_date_picker);
        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ActivityPms.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                Log.i(TAG, dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                Calendar cldr = Calendar.getInstance();

                                cldr.set(year, monthOfYear, dayOfMonth);

                                cldr.set(Calendar.HOUR_OF_DAY, 0);
                                cldr.set(Calendar.MINUTE, 0);
                                cldr.set(Calendar.SECOND, 0);
/*
                                Date c = cldr.getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);

                                btDev.setText(formattedDate + " 📆");
                                Log.i(TAG, activityReturnValue + " / " + formattedDate);
 */
                                String[] s = new String[1];
                                s[0] = cldr.getTimeInMillis() + "";
                                int id = db.insertInTable("pmslog", s);
                                ActivityPms.this.onCancelDeletePmsIds.add(id + "");
                                Date c = cldr.getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);

                                ActivityPms.this.addItem(formattedDate, id + "");
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        /*
        btnGet=(Button)findViewById(R.id.button1);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvw.setText("Selected Date: "+ eText.getText());
            }


        });
        */
        saveData = (TextView)findViewById(R.id.tv_btn_save);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("DAK", "DAKKE DAK");
                ActivityPms.this.save();
            }
        });
        cancelBtn = (TextView)findViewById(R.id.tv_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(String s: onCancelDeletePmsIds){
                    db.deleteTableRow("pmslog", s);
                }
                ActivityPms.this.finish();
            }
        });
        this.serviceActive = (CheckBox)findViewById(R.id.checkBox);
        String sa = db.getSingleEntry("pms_service_active");
        Log.i(TAG, "pms_service_active: " + sa);
        if(sa.equals("true")){
            serviceActive.setChecked(true);
        }
        this.servicePeriod = (CheckBox)findViewById(R.id.checkBox_period);
        sa = db.getSingleEntry("pms_service_period");
        Log.i(TAG, "pms_service_period: " + sa);
        if(sa.equals("true")){
            servicePeriod.setChecked(true);
        }
        this.serviceOvul = (CheckBox)findViewById(R.id.checkBox_ovul);
        sa = db.getSingleEntry("pms_service_ovul");
        Log.i(TAG, "pms_service_ovul: " + sa);
        if(sa.equals("true")){
            serviceOvul.setChecked(true);
        }
        this.servicePms = (CheckBox)findViewById(R.id.checkBox_pms);
        sa = db.getSingleEntry("pms_service_pms");
        Log.i(TAG, "pms_service_pms: " + sa);
        if(sa.equals("true")){
            servicePms.setChecked(true);
        }

        String[][] table = db.queryTableAll("pmslog");
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

                        addItem(formattedDate, s[0]);
                        Log.i(TAG, "Added item: " + formattedDate + "/" + s[1]);
                        //db.deleteTableRow("pmslog", s[0]);
                    }else{
                        db.deleteTableRow("pmslog", s[0]);
                    }
                //}
            }
        }
    }

    private void addItem(String text, String id) {

        final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.activity_pms_log_item, null);
        TextView tv = view.findViewById(R.id.tv_log);
        tv.setText(text);
        final TextView tvId = view.findViewById(R.id.tv_id);
        tvId.setText(id);

        LinearLayout container = (LinearLayout) findViewById(R.id.ll_logs);
        Button delBtn = (Button)view.findViewById(R.id.btn_log);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityPms.this.deletePmsIds.add(tvId.getText().toString());
                ((ViewGroup) view.getParent()).removeView(view);
                Log.i(TAG, "ID: " + tvId.getText().toString());
            }
        });
        container.addView(view);
    }

    private void save() {
        if(serviceActive.isChecked()){
            db.setSingleEntry("pms_service_active", "true");
        }else{
            db.setSingleEntry("pms_service_active", "false");
        }
        if(servicePeriod.isChecked()){
            db.setSingleEntry("pms_service_period", "true");
        }else{
            db.setSingleEntry("pms_service_period", "false");
        }
        if(serviceOvul.isChecked()){
            db.setSingleEntry("pms_service_ovul", "true");
        }else{
            db.setSingleEntry("pms_service_ovul", "false");
        }
        if(servicePms.isChecked()){
            db.setSingleEntry("pms_service_pms", "true");
        }else{
            db.setSingleEntry("pms_service_pms", "false");
        }
        for(String s: deletePmsIds){
            db.deleteTableRow("pmslog", s);
        }

        PmsController pc = new PmsController(this, "pms");
        db.setSingleEntry("pms_next_reminder", pc.getNextReminder() + "");

        //db.setSingleEntry("pms_next_reminder", "");
        finish();
    }
    @Override
    public void onBackPressed() {
        // Handle the back button event
        Log.i(TAG, "onBackPressed");
        finish();
    }
}
