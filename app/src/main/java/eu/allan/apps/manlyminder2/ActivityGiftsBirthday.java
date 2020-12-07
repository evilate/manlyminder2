package eu.allan.apps.manlyminder2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class ActivityGiftsBirthday extends AppCompatActivity {
    private static final String TAG = "ActivityBirthday";
    DatePicker picker;
    Button btnGet;
    TextView tvw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_gifts);

    }
    @Override
    public void onBackPressed() {
        // Handle the back button event
        Log.i(TAG, "onBackPressed");
        finish();
    }
}
