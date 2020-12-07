package eu.allan.apps.manlyminder2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class MyDatePicker extends AppCompatActivity {

    EditText eText;
    Button btnGet;
    TextView tvw;
    DatePickerDialog picker;
    private static final String TAG = "MyDatePicker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.date_picker);
        tvw = (TextView) findViewById(R.id.textView1);
        eText = (EditText) findViewById(R.id.editText1);
        eText.setInputType(InputType.TYPE_NULL);

         */
        Database db = new Database(this);
        String retval = db.getSingleEntry("birthday_settings_birthday");
        long now = System.currentTimeMillis();
        if(retval.length() > 2){
            now = Long.parseLong(retval);
        }
        //Calendar c = Calendar.getInstance();

        final Calendar cldr = Calendar.getInstance();
        cldr.setTimeInMillis(now);
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(MyDatePicker.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        cldr.set(year, monthOfYear, dayOfMonth);
                        //String str = "onDateSet" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        String str = "" + cldr.getTimeInMillis();
                        Log.i(TAG, str);
                        Intent intent = new Intent();
                        intent.putExtra("valueId", str); //value should be your string from the edittext
                        MyDatePicker.this.setResult(1, intent); //The data you want to send back
                        MyDatePicker.this.finish(); //That's w

                    }
                }, year, month, day);
        picker.show();
    }
    @Override
    public void onBackPressed() {
        // Handle the back button event
        Log.i(TAG, "onBackPressed");
        finish();
    }
}