package eu.allan.apps.manlyminder2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by allanfrederiksen on 09/09/2018.
 */

public class Database {

    private String TAG = "\t" + getClass().getSimpleName();
    private final SharedPreferences prefs;

    public Database(Context context) {

        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);

    }


    public String getSingleEntry(String key){
        String rv = "";
        try {
            rv = prefs.getString(key, "");
        }catch (Exception e){
            rv = "Exception";
        }
        return rv;
    }
    public void setSingleEntry(String key, String value){
        saveCleanEntry(key, value);
    }
    public void saveCleanEntry(String key, String value){
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        // Clean
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());

            if(entry.getKey().equals(key)){
                editor.remove(entry.getKey());
            }
        }
        // Add
        editor.putString(key, value);
        editor.commit();
    }
    public void clearAll(){
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        // Clean
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d(TAG, "remove: " + entry.getKey() + ": " + entry.getValue().toString());
            editor.remove(entry.getKey());

        }
        editor.commit();
    }

    public int insertInTable(String tableName, String[] data) {
        //String table = getSingleEntry(tableName);
        String auto_id = getSingleEntry(tableName + "_auto_id");
        int id = 0;
        if(TextUtils.isDigitsOnly(auto_id) && !auto_id.equals("") && auto_id.length() > 0){
            id = Integer.parseInt(auto_id);
            id++;
        }else{
            saveCleanEntry(tableName + "_table_length", data.length + "");
        }
        saveCleanEntry(tableName + "_auto_id", id + "");
        String theKey = "table_" + tableName + "_" + id;
        for(int i=0;i<data.length;i++){
            String theValues = theKey + "_" + i;
            saveCleanEntry(theValues, data[i]);
            //Log.i(TAG, "insertInTable: " + theValues + "/" + data[i]);
        }

        return id;
    }
    public String[][] searchTable(String tableName, String value, int colum){
        String[][] result = queryTableAll(tableName);
        String[][] finalResult = null;
        //Log.i(TAG, "searchTable, result.length: " + result.length);
        ArrayList<String[]> al = new ArrayList<>();
        if(result != null) {
            for (int i = 0; i < result.length; i++) {

                if (result[i][colum].equals(value)) {
                    al.add(result[i]);
                    //Log.i(TAG, "searchTable, found: " + result[i][colum]);
                }

            }
            if (result.length > 0) {
                if (result[0].length > 0) {
                    finalResult = new String[al.size()][];
                    for (int i = 0; i < al.size(); i++) {
                        finalResult[i] = al.get(i);
                    }
                    //Log.i(TAG, "finalResult.length: " + finalResult.length);
                } else {
                    finalResult = null;
                    //Log.i(TAG, "finalResult.length: null");
                }
            } else {
                finalResult = null;
                //Log.i(TAG, "finalResult.length: null");
            }
        }
        if(finalResult != null) {
            if (finalResult.length == 0) {
                return null;
            } else {
                return finalResult;
            }
        }else{
            return null;
        }
    }
    public String[][] queryTableAll(String tableName){
        //Log.i(TAG, "################## queryTableAll(" + tableName + ") ##################");
        String auto_id = getSingleEntry(tableName + "_auto_id");
        String table_length = getSingleEntry(tableName + "_table_length");
        String[][] retval = new String[0][0];
        ArrayList<String[]> als = new ArrayList<>();
        if ((auto_id.length() > 0) && table_length.length() > 0){
            if(TextUtils.isDigitsOnly(auto_id) && TextUtils.isDigitsOnly(table_length)) {
                int tableLength = Integer.parseInt(table_length);
                int autoId = Integer.parseInt(auto_id)+1;
                int ii = 0;
                for(int i=0;i<autoId;i++){
                    String[] item = new String[tableLength+1];
                    item[0] = i + "";
                    int empty = 0;
                    for(int n=0;n<tableLength;n++){
                        String query = "table_" + tableName + "_" + i + "_" + n;
                        String insertString = getSingleEntry(query);
                        item[n+1] = insertString;
                        if(insertString.length() == 0 || insertString == null || insertString.equals("")){
                            empty++;
                        }
                    }
                    if(empty < 1) { // Det var her du pillede sidst
                        als.add(item);
                    }
                }
                retval = new String[als.size()][tableLength];
                for(int i=0;i<als.size();i++){
                    retval[i] = als.get(i);
                }
            }
        }

        //Log.i(TAG, "LENGTH: " + retval.length);
        if(retval.length != 0) {
            //Log.i(TAG, "LENGTH LENGTH: " + retval[0].length);
        }else{
            retval = null;
        }
        return retval;
    }
    public ArrayList<String[]> queryToArrayList(String[][] res){
        ArrayList<String[]> r = null;
        if(res != null){
            r = new ArrayList<>();
            for(int i = 0; i < res.length;i++){
                r.add(res[i]);
            }
        }
        return r;
    }
    public void deleteTable(String tableName){
        //Log.i(TAG, "################## DELETE TABLE (" + tableName + ") ##################");
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(tableName + "_auto_id");
        editor.remove(tableName + "_table_length");
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            String[] splitKey = entry.getKey().split("_");
            if(splitKey.length >= 2){
                //Log.i(TAG, "splitKey.length: " + splitKey.length);
                //Log.i(TAG, "splitKey[0]: " + splitKey[0]);
                //Log.i(TAG, "splitKey[1]: " + splitKey[1]);
                if(splitKey[0].equals("table") && splitKey[1].equals(tableName)) {

                    //Log.i(TAG, "Removing: " + entry.getKey());
                    editor.remove(entry.getKey());
                }
            }
        }
        editor.commit();
    }
    public void deleteTableRow(String tableName, String id){
        //Log.i(TAG, "################## DELETE TABLE ROW (" + tableName + ") ##################");
        SharedPreferences.Editor editor = prefs.edit();
        String table_length = getSingleEntry(tableName + "_table_length");
        int rowLength = Integer.parseInt(table_length);
        for(int i=0;i<rowLength+1;i++) {
            String query = "table_" + tableName + "_" + id + "_" + i;
            editor.remove(query);
            Log.i(TAG, "Removing: " + query);
        }


        editor.commit();
    }

    // MÅSKE BUG
    public void updateTableRowByColum(String tableName, String searchValue, int searchColum, String insertValue, int insertColum){
        //Log.i(TAG, "################## updateTableRowByColum ##################");
        String[][] result = queryTableAll(tableName);


        for(int i=0;i<result.length;i++){
            if(result[i][searchColum].equals(searchValue)){
                String[] data = new String[result[i].length];
                //updateRow[0] = result[i][0];
                for(int n=0;n<result[i].length-1;n++) {

                    if (n == insertColum) {
                        data[n] = insertValue;
                    } else {
                        data[n] = result[i][n+1];
                    }

                }
                updateTableRowById(tableName, data, Integer.parseInt(result[i][0]));
            }
        }
    }



    public boolean updateTableRowById(String tableName, String[] data, int id) {
        //Log.i(TAG, "################## updateTableRowById ##################");
        String theKey = "table_" + tableName + "_" + id;
        for(int i=0;i<data.length;i++){
            String theValues = theKey + "_" + i;
            saveCleanEntry(theValues, data[i]);
        }

        return false;
    }
    public String[] getRowById(String tableName, int id){
        String[] returnResult = new String[0];
        String[][] result = queryTableAll(tableName);
        for(int i=0;i<result.length;i++){
            if(Integer.parseInt(result[i][0]) == id){
                returnResult = result[i];
            }
        }
        return returnResult;
    }
    public void deleteTableRowByColum(String tableName, String value, int colum){
        //Log.i(TAG, "################## deleteTableRowByColum ##################");
        String[][] result = queryTableAll(tableName);
        if(result != null) {
            ArrayList<Integer> al = new ArrayList<>();
            for (int i = 0; i < result.length; i++) {
                if (result[i][colum].equals(value)) {
                    al.add(Integer.parseInt(result[i][0]));
                }
            }
            for (int i = 0; i < al.size(); i++) {
                //Log.i(TAG, "DELETE: " + al.get(i));
                deleteTableRow(tableName, al.get(i) + "");
                //Log.i(TAG, "DELETE ID: " + al.get(i));
            }
        }
    }
    public int numRows(String tableName) {
        //Log.i(TAG, "################## NUMBER OF ROWS (" + tableName + ") ##################");
        int retval = 0;
        String[][] aQuery = queryTableAll(tableName);
        if(aQuery != null){
            //Log.i(TAG, "## " + tableName + " LENGTH: " + aQuery.length);
            retval = aQuery.length;
        }

        return retval;
    }
    public int lastAutoIdEntry(String tableName) {
        //Log.i(TAG, "################## LAST AUTO INCREMENT ID ##################");
        String auto_id = getSingleEntry(tableName + "_auto_id");
        return Integer.parseInt(auto_id);
    }
    public int getTableNumCols(String tableName) {
        //Log.i(TAG, "################## LAST AUTO INCREMENT ID ##################");
        String table_length = getSingleEntry(tableName + "_table_length");
        return Integer.parseInt(table_length);
    }

    public void listTable(String tableName) {
        Log.i(TAG, "################## listTable(" + tableName + ") ##################");
        String[][] aQuery = queryTableAll(tableName);
        //Log.i(TAG, "## TABLE LENGTH: " + aQuery.length);
        if(aQuery != null) {
            for (int i = 0; i < aQuery.length; i++) {
                //Log.i(TAG, "TABLE: " + tableName);
                String out = "";
                for (int n = 0; n < aQuery[i].length; n++) {

                    try{
                        long l = Long.parseLong(aQuery[i][n]);
                        if(l > 1000L) {
                            TimeAndDate td = new TimeAndDate();
                            out = out + "|[" + n + "] " + " <" + td.getNormalDateAndTimeFromUnixtime(l) + "> ";
                        }else{
                            out = out + "|[" + n + "] " + l + " NOT A DATE";
                        }
                    }catch (NumberFormatException e){
                        out = out + "|[" + n + "] " + aQuery[i][n]+ " NOT A NUMBER";
                    }

                    //Log.i(TAG, "[" + tableName + "]" + "[" + i + "]" + "[" + n + "] " + aQuery[i][n]);
                }
                Log.i(TAG, out);
            }
        }
    }
    public void logOut(String tableName) {
        //Log.i(TAG, "################## LOG OUT ##################");
        String[][] aQuery = queryTableAll(tableName);
        //Log.i(TAG, "## TABLE LENGTH: " + aQuery.length);
        if(aQuery != null) {
            for (int i = 0; i < aQuery.length; i++) {
                //Log.i(TAG, "TABLE: " + aQuery[i]);
                for (int n = 0; n < aQuery[i].length; n++) {
                    //Log.i(TAG, "[" + tableName + "]" + "[" + i + "]" + "[" + n + "] " + aQuery[i][n]);
                }
                //Log.i(TAG, "\n");
            }
        }
    }
    public void arrayOut(String[][] arr) {
        //Log.i(TAG, "################## ARRAY OUT ##################");
        //Log.i(TAG, "Array length: " + arr.length);
        for (int i = 0; i < arr.length; i++) {
            //Log.i(TAG, "TABLE: " + aQuery[i]);
            for (int n = 0; n < arr[i].length; n++) {
                //Log.i(TAG, "[VALUE]" + "[" + i + "] " + arr[i][n]);
            }
        }
    }
    public void createTest(){
        //Log.i(TAG, "################## CREATE TEST ##################");
        String[] str = new String[4];
        str[0] = "Value 0 1";
        str[1] = "Value 1 1";
        str[2] = "Value 2 1";
        str[3] = "Value 3 1";
        insertInTable("testtabel", str);
        str = new String[4];
        str[0] = "Value 0 2";
        str[1] = "Value 1 2";
        str[2] = "Value 2 2";
        str[3] = "Value 3 2";
        insertInTable("testtabel", str);
        str = new String[4];
        str[0] = "Value 0 3";
        str[1] = "Value 1 3";
        str[2] = "Value 2 3";
        str[3] = "Value 3 3";
        insertInTable("testtabel", str);
    }
    public void showAll(){
        //Log.i(TAG, "################## SHOW ALL ##################");
        //Log.i(TAG, "[key] Value\n");
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        // Clean
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.i(TAG, "[" + entry.getKey() + "] " + entry.getValue().toString());
        }
    }
    /*
    public ArrayList<String> getEntry(String key){
        ArrayList<String> retval = new ArrayList<String>();
        int size = prefs.getInt(key + "_size", 0);
        for(int i=0;i<size;i++){
            String s = key + "_" + i;
            retval.add(prefs.getString(s, "NOTHING FOUND"));
        }

        return retval;
    }
    public void removeEntry(String key){
        //Log.d(TAG, "removeEntry->key: " + key);
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        // Clean
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            String[] ss = entry.getKey().split("_");
            if(ss[0].equals(key) || entry.getKey().equals(key)){
                editor.remove(key);
                //Log.d(TAG, "removeEntry->Found: " + key);
            }
        }
        editor.commit();
    }
    public void removeAccountEntries(String key){
        //Log.d(TAG, "removeEntry->key: " + key);
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        // Clean
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            String[] ss = entry.getKey().split("_");
            if(ss.length > 1) {
                String sss = ss[0] + "_" + ss[1];
                if (sss.equals(key) || entry.getKey().equals(key)) {
                    editor.remove(entry.getKey());
                    //Log.i(TAG, "removeAccountEntries: " + entry.getKey());
                }
            }
        }
        editor.commit();
    }
    public void saveEntry(String key, String value){
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        // Clean
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            String[] ss = entry.getKey().split("_");
            if(ss[0].equals(key)){
                editor.remove(entry.getKey());
            }
        }
        // Add
        editor.putString(key + "_" + 0, value);
        editor.commit();
    }


    public void saveEntries(String key, ArrayList<String> value){
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        // Clean
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            String[] ss = entry.getKey().split("_");
            if(ss[0].equals(key)){
                editor.remove(entry.getKey());
            }
        }
        // Add
        int i = 0;
        for(String data: value){
            editor.putString(key + "_" + i, data);
            i++;
            //Log.d(TAG, "data: " + data);
        }
        editor.putInt(key + "_size", i);
        editor.commit();
    }
    public void saveMfiEntries(String key, ArrayList<String> value){
        SharedPreferences.Editor editor = prefs.edit();

        // Add
        int i = 0;
        for(String data: value){
            editor.putString(key + "_" + i, data);
            i++;
            //Log.d(TAG, "data: " + data);
        }
        editor.putInt(key + "_size", i);
        editor.commit();
    }

    public ArrayList<String> getAllKeys(String type){
        ArrayList<String> retval = new ArrayList<String>();
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String[] ss = entry.getKey().split("_");
            if(!retval.contains(ss[0])){
                if(ss[0].equals(type) || type == null) {
                    retval.add(ss[0]);
                }
            }
        }
        return retval;
    }
    public ArrayList<String> getAllKeys_v2(String type){
        ArrayList<String> retval = new ArrayList<String>();
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String[] ss = entry.getKey().split("_");
            if(ss.length >= 2) {
                if (!retval.contains(ss[0] + "_" + ss[1])) {
                    if (ss[0].equals(type) || type == null) {
                        retval.add(ss[0] + "_" + ss[1]);
                    }
                }
            }
        }
        return retval;
    }


    public void listAll(){
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d(TAG, entry.getKey() + ": " + entry.getValue().toString());

        }

    }

    public void listList(String name, ArrayList<String> entry) {
        for(String s: entry){
            //Log.d(TAG, name + ": " + s + "");
        }
    }
/*
    public void saveAllEvents(ArrayList<AnEvent> theEvents){
        deleteAllEvents();
        for(AnEvent ae: theEvents){
            if(ae.isValidEvent()) {
                String key = "calendarevent_" + ae.getCalendarId() + "_" + ae.getEventId();
                ArrayList<String> data = new ArrayList<>();
                data.add(ae.getCalendarName());
                data.add(ae.getEventName());
                data.add(ae.getCalendarId() + "");
                data.add(ae.getEventId() + "");
                data.add(ae.getStartTime() + "");
                data.add(ae.getEndTime() + "");
                saveEvent(key, data);
            }
        }
    }
    public void deleteAllEvents() {
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        // Clean
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            String[] ss = entry.getKey().split("_");
            if (ss[0].equals("calendarevent")) {
                editor.remove(entry.getKey());
            }
        }
        editor.commit();
    }

    public void saveEvent(String eventKey, ArrayList<String> eventData){
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        // Clean
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            String[] ss = entry.getKey().split("_");
            if(ss[0].equals(eventKey)){
                editor.remove(entry.getKey());
            }
        }
        editor.commit();
        // Add
        int i = 0;
        for(String data: eventData){
            editor.putString(eventKey + "_" + i, data);
            //Log.d(TAG, "\t\teventKey_" + i + ": " + eventKey + "_" + i + ": " + data);

            //Log.d(TAG, "data: " + data);

            //Log.d(TAG, "event_key_0: " + eventKey + "_" + i + ": " + prefs.getString(eventKey + "_" + i, null) + "\n");
            i++;
        }
        //Log.i(TAG, "Saving Event: " + eventData.get(1));
        editor.commit();

    }

    public ArrayList<AnEvent> loadAllEvents(){
        ArrayList<AnEvent> retval = new ArrayList<AnEvent>();
        ArrayList<String> foundKeys = new ArrayList<String>();
        SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String[] ss = entry.getKey().split("_");
            if(ss[0].equals("calendarevent")){
                if(ss.length >= 3){
                    String eventName = ss[0] + "_" + ss[1] + "_" + ss[2];
                    if(!foundKeys.contains(eventName)){
                        foundKeys.add(eventName);
                    }
                }

                AnEvent ae = loadAnEvent(entry.getKey());
                if(ae.isValidEvent()) {
                    retval.add(ae);

            }
        }
        for(String eventNames: foundKeys){
            AnEvent ae = loadAnEvent(eventNames);
            if(ae.isValidEvent()) {
                retval.add(ae);
            }
        }

        return retval;

    }
    public AnEvent loadAnEvent(String event_key){

        //Log.d(TAG, "event_key: " + event_key);
        //Log.d(TAG, event_key + "_0: " + prefs.getString(event_key + "_" + 0, null));

        AnEvent ae = new AnEvent();
        ae.setCalendarName(prefs.getString(event_key + "_" + 0, null));
        ae.setEventTitle(prefs.getString(event_key + "_" + 1, null));
        //Log.d(TAG, "prefs.getString(event_key + \"_\" + 2, mum): " + prefs.getString(event_key + "_" + 2, "mum"));
        //Log.d(TAG, "event_key: " + event_key + "_" + 2);
        ae.setCalendarId(Long.parseLong(prefs.getString(event_key + "_" + 2, "0")));
        ae.setEventId(Long.parseLong(prefs.getString(event_key + "_" + 3, "0")));
        ae.setStartTime(Long.parseLong(prefs.getString(event_key + "_" + 4, "0")));
        ae.setEndTime(Long.parseLong(prefs.getString(event_key + "_" + 5, "0")));

        return ae;
    }
*/

}
