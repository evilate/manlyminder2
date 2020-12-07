package eu.allan.apps.manlyminder2;

import android.app.DatePickerDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class ActivityAnniversaryAdd extends AppCompatActivity {
    private static final String TAG = "ActivityAnniversary";
    private TextView saveData;
    private TextView cancelBtn;
    private Database db;
    private Button timePickerButton;
    private DatePickerDialog picker;
    private ArrayList<String> deletePmsIds;
    private ArrayList<String> onCancelDeletePmsIds;
    private EditText textArea;
    private String selectedMillis;
    private TextView dateDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary_add);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        db = new Database(this);
        selectedMillis = "0";
        saveData = (TextView)findViewById(R.id.tv_btn_save);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("DAK", "DAKKE DAK");
                ActivityAnniversaryAdd.this.save();
            }
        });
        cancelBtn = (TextView)findViewById(R.id.tv_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityAnniversaryAdd.this.finish();
            }
        });

        Log.i(TAG, "ActivityAnniversaryAdd: ");

        textArea = (EditText) findViewById(R.id.editTextTextPersonName);
        dateDisplay = (TextView) findViewById(R.id.tv_display_date);
        setup();
    }

    private void setup() {
        timePickerButton = (Button) findViewById(R.id.btn_pick_date);
        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ActivityAnniversaryAdd.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                Log.i(TAG, dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                Calendar cldr = Calendar.getInstance();
                                cldr.set(year, monthOfYear, dayOfMonth);
                                //String selectedMillis = "onDateSet" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                                cldr.set(Calendar.HOUR_OF_DAY, 0);
                                cldr.set(Calendar.MINUTE, 0);
                                cldr.set(Calendar.SECOND, 0);
                                selectedMillis = "" + cldr.getTimeInMillis();
                                Date c = cldr.getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);

                                dateDisplay.setText(formattedDate + " 📆");
/*
                                Date c = cldr.getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);

                                btDev.setText(formattedDate + " 📆");
                                Log.i(TAG, activityReturnValue + " / " + formattedDate);
 */
                                //String[] s = new String[1];
                                //s[0] = cldr.getTimeInMillis() + "";
                                //int id = db.insertInTable("anniversaryreminders", s);
                                //ActivityAnniversaryAdd.this.onCancelDeletePmsIds.add(id + "");
                                //Date c = cldr.getTime();
                                //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                //String formattedDate = df.format(c);


                                //ActivityAnniversaryAdd.this.addItem(formattedDate, id + "");
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        /*
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

                    addItem(formattedDate, s[0]);
                    Log.i(TAG, "Added item: " + formattedDate + "/" + s[1]);
                    //db.deleteTableRow("pmslog", s[0]);
                }else{
                    db.deleteTableRow("anniversaryreminders", s[0]);
                }
                //}
            }
        }

         */
    }

    private void save() {
        Intent intent = new Intent();
        intent.putExtra("valueId", selectedMillis); //value should be your string from the edittext
        intent.putExtra("valueText", textArea.getText().toString()); //value should be your string from the edittext
        ActivityAnniversaryAdd.this.setResult(1, intent); //The data you want to send back

        finish();
    }
    @Override
    public void onBackPressed() {
        // Handle the back button event
        Log.i(TAG, "onBackPressed");
        finish();
    }
}
