package eu.allan.apps.manlyminder2;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class LogToDatabase {
    private final Database db;
    private String TAG = getClass().getSimpleName();

    public LogToDatabase(Context context) {
        this.db = new Database(context);
    }
    public void addLog(String item){
        String[] insert = new String[2];
        insert[0] = System.currentTimeMillis() + "";
        insert[1] = item;
        db.insertInTable("debuglog", insert);
    }
    public void listLog(){
        TimeAndDate td = new TimeAndDate();
        String[][] query = db.queryTableAll("debuglog");
        for(String[] s: query){
            long l = Long.parseLong(s[0]);
            String time = td.getNormalDateAndTimeFromUnixtime(l);
            Log.i(TAG, time + ": " + s[1]);
        }
    }
}
