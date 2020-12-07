package eu.allan.apps.manlyminder2;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class ActivitySettings extends AppCompatActivity {

    private static final String TAG = "ActivitySettings";
    private TextView saveData;
    private TextView cancelBtn;
    private TextView name;
    private TextView nickname;
    private Spinner mySpinner;
    private Switch incognito;
    private Database db;
    private CheckBox reminder_schedule_1;
    private CheckBox reminder_schedule_2;
    private CheckBox reminder_schedule_3;
    private CheckBox reminder_schedule_4;
    private CheckBox reminder_schedule_5;
    private CheckBox reminder_schedule_6;
    private CheckBox reminder_schedule_7;
    private Button timeButton;
    private long timeButtonLong;

    TimePicker picker;
    Button btnGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        db = new Database(this);
        saveData = (TextView)findViewById(R.id.tv_btn_save);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("DAK", "DAKKE DAK");
                ActivitySettings.this.save();
            }
        });
        cancelBtn = (TextView)findViewById(R.id.tv_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivitySettings.this.finish();
            }
        });
        this.mySpinner = (Spinner) findViewById(R.id.main_settings_status_spinner);
        incognito = (Switch)findViewById(R.id.main_settings_incognito);
        name = (EditText)findViewById(R.id.main_settings_name);
        nickname = (EditText)findViewById(R.id.main_settings_nickname);

        this.reminder_schedule_1 = (CheckBox)findViewById(R.id.reminder_schedule_checkbox_1);
        this.reminder_schedule_2 = (CheckBox)findViewById(R.id.reminder_schedule_checkbox_2);
        this.reminder_schedule_3 = (CheckBox)findViewById(R.id.reminder_schedule_checkbox_3);
        this.reminder_schedule_4 = (CheckBox)findViewById(R.id.reminder_schedule_checkbox_4);
        this.reminder_schedule_5 = (CheckBox)findViewById(R.id.reminder_schedule_checkbox_5);
        this.reminder_schedule_6 = (CheckBox)findViewById(R.id.reminder_schedule_checkbox_6);
        this.reminder_schedule_7 = (CheckBox)findViewById(R.id.reminder_schedule_checkbox_7);
        timeButton = (Button) findViewById(R.id.btn_pick_birthday);
/*
        picker=(TimePicker)findViewById(R.id.timePicker1);
        picker.setIs24HourView(true);
        btnGet=(Button)findViewById(R.id.button1);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = picker.getHour();
                    minute = picker.getMinute();
                }
                else{
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }
                if(hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                }
                else
                {
                    am_pm="AM";
                }
                tvw.setText("Selected Date: "+ hour +":"+ minute+" "+am_pm);
            }
        });


 */
        Log.i(TAG, "Create");
        load();
    }
    private void load(){
        name.setText(db.getSingleEntry("main_settings_name"));
        nickname.setText(db.getSingleEntry("main_settings_nickname"));
        setSpinText(mySpinner, db.getSingleEntry("main_settings_status"));
        String incognito = db.getSingleEntry("main_settings_incognito");
        if(incognito.equals("on")){
            this.incognito.setChecked(true);
        }else{
            this.incognito.setChecked(false);
        }
        timeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivitySettings.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String h = selectedHour + "";
                        String m = selectedMinute + "";
                        if(selectedHour < 10){
                            h = "0" + selectedHour;
                        }
                        if(selectedMinute < 10){
                            m = "0" + selectedMinute;
                        }

                        timeButton.setText( h + ":" + m);
                        timeButtonLong = (selectedHour*60*60*1000)+(selectedMinute*60*1000);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        Calendar cal = Calendar.getInstance();
        //cal.setTimeInMillis(Long.parseLong(db.getSingleEntry("main_settings_timeofday")));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        timeButtonLong = Long.parseLong(db.getSingleEntry("main_settings_timeofday"));
        cal.setTimeInMillis(cal.getTimeInMillis()+timeButtonLong);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        //Calendar.HOUR_OF_DAY gives you the 24-hour time.
        //Calendar.HOUR gives you the 12-hour time.
        String h = hour + "";
        String m = minute + "";
        if(hour < 10){
            h = "0" + hour;
        }
        if(minute < 10){
            m = "0" + minute;
        }

        timeButton.setText( h + ":" + m);
    }
    public void setSpinText(Spinner spin, String text) {
        for(int i= 0; i < spin.getAdapter().getCount(); i++) {
            if(spin.getAdapter().getItem(i).toString().contains(text)) {
                spin.setSelection(i);
            }
        }

    }
    private void save(){


        String name_text = "Woman";
        if(!name.getText().toString().equals("")){
            name_text = name.getText().toString();
        }
        db.setSingleEntry("main_settings_name", name_text);

        String nickname_text = "Woman";
        if(!nickname.getText().toString().equals("")){
            nickname_text = nickname.getText().toString();
        }
        db.setSingleEntry("main_settings_nickname", nickname_text);

        String spinner_text = "Woman";
        if(!mySpinner.getSelectedItem().toString().equals("")){
            spinner_text = mySpinner.getSelectedItem().toString();
        }
        db.setSingleEntry("main_settings_status", spinner_text);

        String incognito_text = "off";
        if(incognito.isChecked()){
            incognito_text = "on";
        }
        db.setSingleEntry("main_settings_incognito", incognito_text);

        if(reminder_schedule_1.isChecked()){
            db.setSingleEntry("reminder_schedule_1", "true");
        }else{
            db.setSingleEntry("reminder_schedule_1", "false");
        }

        if(reminder_schedule_2.isChecked()){
            db.setSingleEntry("reminder_schedule_2", "true");
        }else{
            db.setSingleEntry("reminder_schedule_2", "false");
        }

        if(reminder_schedule_3.isChecked()){
            db.setSingleEntry("reminder_schedule_3", "true");
        }else{
            db.setSingleEntry("reminder_schedule_3", "false");
        }

        if(reminder_schedule_4.isChecked()){
            db.setSingleEntry("reminder_schedule_4", "true");
        }else{
            db.setSingleEntry("reminder_schedule_4", "false");
        }

        if(reminder_schedule_5.isChecked()){
            db.setSingleEntry("reminder_schedule_5", "true");
        }else{
            db.setSingleEntry("reminder_schedule_5", "false");
        }

        if(reminder_schedule_6.isChecked()){
            db.setSingleEntry("reminder_schedule_6", "true");
        }else{
            db.setSingleEntry("reminder_schedule_6", "false");
        }

        if(reminder_schedule_7.isChecked()){
            db.setSingleEntry("reminder_schedule_7", "true");
        }else{
            db.setSingleEntry("reminder_schedule_7", "false");
        }
        timeButton.getText();

        db.setSingleEntry("main_settings_timeofday", timeButtonLong + "");
        finish();
    }

    @Override
    public void onBackPressed() {
        // Handle the back button event
        Log.i(TAG, "onBackPressed");
        finish();
    }
}
