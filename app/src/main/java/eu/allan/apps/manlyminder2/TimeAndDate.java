package eu.allan.apps.manlyminder2;

import android.content.res.Resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeAndDate {
    private String TAG = getClass().getSimpleName();
    public int years;
    public int months;
    public int days;
    public int hours;
    public int minutes;
    public int weeks;
    public String yearsStr = "";
    public String monthsStr = "";
    public String daysStr = "";
    public String hoursStr = "";
    public String minutesStr = "";
    public String weeksStr = "";

    public String yearsStrShort = "";
    public String monthsStrShort = "";
    public String daysStrShort = "";
    public String hoursStrShort = "";
    public String minutesStrShort = "";
    public String weeksStrShort = "";
    public ArrayList<String> timeArr;
    public ArrayList<String> timeArrModifier;
    public ArrayList<String> timeArrModifierShort;

    public TimeAndDate() {
        timeArr = new ArrayList<String>();
        timeArrModifier = new ArrayList<String>();
        timeArrModifierShort = new ArrayList<String>();
    }

    public long getDateNextYear(long unix_birthdate){
        long retval = 0L;
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTimeInMillis(unix_birthdate);
        if (birthDate.after(today)) {

            //throw new IllegalArgumentException("You don't exist yet");
        }
        int todayYear = today.get(Calendar.YEAR);
        int birthDateYear = birthDate.get(Calendar.YEAR);
        int todayDayOfYear = today.get(Calendar.DAY_OF_YEAR);
        int birthDateDayOfYear = birthDate.get(Calendar.DAY_OF_YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int birthDateMonth = birthDate.get(Calendar.MONTH);
        int todayDayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        int birthDateDayOfMonth = birthDate.get(Calendar.DAY_OF_MONTH);
        int age = todayYear - birthDateYear;


        // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
        if ((birthDateDayOfYear - todayDayOfYear > 3) || (birthDateMonth > todayMonth)){
            age--;

            // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
        } else if ((birthDateMonth == todayMonth) && (birthDateDayOfMonth > todayDayOfMonth)){
            age--;
        }
        if(birthDate.after(today)) {
            birthDate.set(Calendar.YEAR, (todayYear + 1));
        }else{
            birthDate.set(Calendar.YEAR, (birthDateYear + age + 1));
        }
        birthDate.set(Calendar.HOUR_OF_DAY, 1);
        birthDate.set(Calendar.MINUTE, 0);
        //Log.i(TAG, "AGE: " + age);
        //Log.i(TAG, "Calendar.YEAR+age+1: " + Calendar.YEAR+age+1);
        //Log.i(TAG, "Calendar.YEAR: " + Calendar.YEAR);
        return birthDate.getTimeInMillis();
    }

    public String getNormalDateFromTime(long unixtime){
        return formatDate(unixtime);
    }
    public String getNormalDateDateOnlyFromTime(long unixtime){
        return formatDateDateOnly(unixtime);
    }

    public String getNormalDateAndTimeFromUnixtime(long milliseconds) /* This is your topStory.getTime()*1000 */ {
        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //"dd/MM/yyyy' 'HH:mm:ss"
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        return sdf.format(calendar.getTime());
    }
    public String getNormalTimeFromUnixtime(long milliseconds) /* This is your topStory.getTime()*1000 */ {
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss"); //"dd/MM/yyyy' 'HH:mm:ss"
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        return sdf.format(calendar.getTime());
    }

    private String formatDate(long milliseconds) /* This is your topStory.getTime()*1000 */ {
        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //"dd/MM/yyyy' 'HH:mm:ss"
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        return sdf.format(calendar.getTime());
    }
    private String formatDateDateOnly(long milliseconds) /* This is your topStory.getTime()*1000 */ {
        DateFormat sdf = new SimpleDateFormat("dd-MM"); //"dd/MM/yyyy' 'HH:mm:ss"
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        return sdf.format(calendar.getTime());
    }
    public void doCalc(long from, long to) {
        //TimeSpan v = new TimeSpan();
        //Log.i(TAG, "doCalc(" + formatDate(from) + ", " + formatDate(to) + ")");
        //Log.i(TAG, "DOCALC->to: " + formatDate(to));
        Date later = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(to);
        Date earlier = new Date(from);
        later.setTime(cal.getTimeInMillis());

        /* Add months until we go past the target, then go back one. */
        while (calculateOffset(earlier, this).compareTo(later) <= 0) {
            this.years++;
        }
        if(this.years > 0) {
            this.years--;
        }
        if(this.years == 1){
            this.yearsStr = Resources.getSystem().getString(R.string.a_year);
            this.yearsStrShort = Resources.getSystem().getString(R.string.a_year_short);
        }else{
            this.yearsStr = Resources.getSystem().getString(R.string.years);
            this.yearsStrShort = Resources.getSystem().getString(R.string.year_short);
        }
        /* Add months until we go past the target, then go back one. */
        while (calculateOffset(earlier, this).compareTo(later) <= 0) {
            this.months++;
        }
        if(this.months > 0) {
            this.months--;
        }
        if(this.months == 1){
            this.monthsStr = Resources.getSystem().getString(R.string.a_month);
            this.monthsStrShort = Resources.getSystem().getString(R.string.a_month_short);
        }else{
            this.monthsStr = Resources.getSystem().getString(R.string.months);
            this.monthsStrShort = Resources.getSystem().getString(R.string.months_short);
        }

        /* Add months until we go past the target, then go back one. */
        while (calculateOffset(earlier, this).compareTo(later) <= 0) {
            this.weeks++;
        }
        if(this.weeks > 0) {
            this.weeks--;
        }
        if(this.weeks == 1){
            this.weeksStr = Resources.getSystem().getString(R.string.a_week);
            this.weeksStrShort = Resources.getSystem().getString(R.string.a_week_short);
        }else{
            this.weeksStr = Resources.getSystem().getString(R.string.week);
            this.weeksStrShort = Resources.getSystem().getString(R.string.week_short);
        }
        /* Add days until we go past the target, then go back one. */
        while (calculateOffset(earlier, this).compareTo(later) <= 0) {
            this.days++;
        }
        if(this.days > 0) {
            this.days--;
        }
        if(this.days == 1){
            this.daysStr = Resources.getSystem().getString(R.string.a_day);
            this.daysStrShort = Resources.getSystem().getString(R.string.a_day_short);
        }else{
            this.daysStr = Resources.getSystem().getString(R.string.days);
            this.daysStrShort = Resources.getSystem().getString(R.string.days_short);
        }
        /* Add hours until we go past the target, then go back one. */
        while (calculateOffset(earlier, this).compareTo(later) <= 0) {
            this.hours++;
        }
        if(this.hours > 0) {
            this.hours--;
        }
        if(this.hours == 1){
            this.hoursStr = Resources.getSystem().getString(R.string.an_hour);
            this.hoursStrShort = Resources.getSystem().getString(R.string.an_hour_short);
        }else{
            this.hoursStr = Resources.getSystem().getString(R.string.hour);
            this.hoursStrShort = Resources.getSystem().getString(R.string.hour_short);
        }

        while (calculateOffset(earlier, this).compareTo(later) <= 0) {
            this.minutes++;
        }
        if(this.minutes > 0) {
            this.minutes--;
        }
        if(this.minutes == 1){
            this.minutesStr = Resources.getSystem().getString(R.string.a_minute);
            this.minutesStrShort = Resources.getSystem().getString(R.string.a_minute_short);
        }else{
            this.minutesStr = Resources.getSystem().getString(R.string.minutes);
            this.minutesStrShort = Resources.getSystem().getString(R.string.minutes_short);
        }
        doLoadLists();
    }
    private void doLoadLists() {
        int counter = 0;
        int countMax = 6;
        String timeStr = "";
        if(this.years != 0 && counter < countMax){
            timeArr.add(this.years + "");
            timeArrModifier.add(this.yearsStr);
            timeArrModifierShort.add(this.yearsStrShort);
            counter++;
        }
        if(this.months != 0 && counter < countMax){
            timeArr.add(this.months + "");
            timeArrModifier.add(this.monthsStr);
            timeArrModifierShort.add(this.monthsStrShort);
            counter++;
        }
        if(this.weeks != 0 && counter < countMax){
            timeArr.add(this.weeks + "");
            timeArrModifier.add(this.weeksStr);
            timeArrModifierShort.add(this.weeksStrShort);
            counter++;
        }
        if(this.days != 0 && counter < countMax){
            timeArr.add(this.days + "");
            timeArrModifier.add(this.daysStr);
            timeArrModifierShort.add(this.daysStrShort);
            counter++;
        }
        if(this.hours != 0 && counter < countMax){
            timeArr.add(this.hours + "");
            timeArrModifier.add(this.hoursStr);
            timeArrModifierShort.add(this.hoursStrShort);
            counter++;
        }
        if(this.minutes != 0 && counter < countMax){
            timeArr.add(this.minutes + "");
            timeArrModifier.add(this.minutesStr);
            timeArrModifierShort.add(this.minutesStrShort);
            counter++;
        }
        if(timeArr.size() == 0){
            timeArr.add(Resources.getSystem().getString(R.string.tine_now));
            timeArrModifier.add("");
            timeArrModifierShort.add("");

        }
        while(timeArr.size() <= 6){
            timeArr.add("");
            timeArrModifier.add("");
            timeArrModifierShort.add("");

        }
    }



    private Date calculateOffset(Date start, TimeAndDate offset) {
        Calendar c = new GregorianCalendar();

        c.setTime(start);
        c.add(Calendar.YEAR, offset.years);
        c.add(Calendar.MONTH, offset.months);
        c.add(Calendar.WEEK_OF_YEAR, offset.weeks);
        c.add(Calendar.DAY_OF_YEAR, offset.days);
        c.add(Calendar.HOUR, offset.hours);
        c.add(Calendar.MINUTE, offset.minutes);

        return c.getTime();
    }
    public String getLongString(String seperator, int countMax) {
        ArrayList<String> timeArr = new ArrayList<String>();
        ArrayList<String> timeArrModifier = new ArrayList<String>();
        ArrayList<String> timeArrModifierShort = new ArrayList<String>();
        int counter = 0;
        //int countMax = 3;
        String timeStr = "";
        if(this.years != 0 && counter < countMax){
            timeStr = this.years + " years" + seperator;
            timeArr.add(this.years + "");
            timeArrModifier.add(this.yearsStr);
            timeArrModifierShort.add(this.yearsStrShort);
            counter++;
        }
        if(this.months != 0 && counter < countMax){
            timeStr = timeStr + this.months + " months" + seperator;
            timeArr.add(this.months + "");
            timeArrModifier.add(this.monthsStr);
            timeArrModifierShort.add(this.monthsStrShort);
            //Log.i(TAG, "Month " + counter + ": ");
            counter++;
        }
        if(this.weeks != 0 && counter < countMax){
            timeStr = timeStr + this.weeks + " weeks" + seperator;
            timeArr.add(this.weeks + "");
            timeArrModifier.add(this.weeksStr);
            timeArrModifierShort.add(this.weeksStrShort);
            counter++;
        }
        if(this.days != 0 && counter < countMax){
            timeStr = timeStr + this.days + " days" + seperator;
            timeArr.add(this.days + "");
            timeArrModifier.add(this.daysStr);
            timeArrModifierShort.add(this.daysStrShort);
            counter++;
        }
        if(this.hours != 0 && counter < countMax){
            timeStr = timeStr + this.hours + " hours" + seperator;
            timeArr.add(this.hours + "");
            timeArrModifier.add(this.hoursStr);
            timeArrModifierShort.add(this.hoursStrShort);
            counter++;
        }
        if(this.minutes != 0 && counter < countMax){
            timeStr = timeStr + this.minutes + " minutes" + seperator;
            timeArr.add(this.minutes + "");
            timeArrModifier.add(this.minutesStr);
            timeArrModifierShort.add(this.minutesStrShort);
            counter++;
        }
        int removeLength = seperator.length();
        if(timeStr.length() < removeLength){
            timeStr = "now";
        }else{
            timeStr = timeStr.substring(0, timeStr.length() - removeLength);
        }



        return timeStr;
    }
}
