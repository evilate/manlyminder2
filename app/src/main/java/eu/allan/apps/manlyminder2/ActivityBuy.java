package eu.allan.apps.manlyminder2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class ActivityBuy extends AppCompatActivity {
    private static final String TAG = "ActivityBuy";
    private TextView saveData;
    private TextView cancelBtn;
    private CheckBox serviceActive;
    private Database db;
    private TextView nextReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        this.db = new Database(this);

        Log.i(TAG, "Create");
    }


    @Override
    public void onBackPressed() {
        // Handle the back button event
        Log.i(TAG, "onBackPressed");
        finish();
    }
}
