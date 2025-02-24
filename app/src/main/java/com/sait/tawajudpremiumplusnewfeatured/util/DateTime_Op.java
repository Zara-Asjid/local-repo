package com.sait.tawajudpremiumplusnewfeatured.util;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper;

import java.time.format.DateTimeFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
@SuppressLint("SimpleDateFormat")
public class DateTime_Op {


    public static String convertDate(String dateInMilliseconds,
                                     String dateFormat) {
        if (!(dateInMilliseconds instanceof String))
            dateInMilliseconds = "0";
        if (dateInMilliseconds.isEmpty())
            dateInMilliseconds = "0";
        return (new SimpleDateFormat(dateFormat).format(new Date(
                dateStringToLong(dateInMilliseconds))));
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean validateDates(String fromDate, String toDate) {
        try {
            LocalDate fromDateObj = LocalDate.parse(fromDate);
            LocalDate toDateObj = LocalDate.parse(toDate);
            return fromDateObj.isBefore(toDateObj) || fromDateObj.isEqual(toDateObj);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    public static String convertDate(String dateInMilliseconds,
                                     String dateFormat, String timeZone) {
        if (!(dateInMilliseconds instanceof String))
            dateInMilliseconds = "0";
        if (dateInMilliseconds.isEmpty())
            dateInMilliseconds = "0";
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
        String strDF = df
                .format(new Date(dateStringToLong(dateInMilliseconds)));
        return (strDF);
    }

    public static long dateStringToLong(String dateInMiliseconds) {
        if (!(dateInMiliseconds instanceof String))
            dateInMiliseconds = "0";
        if (dateInMiliseconds.isEmpty())
            dateInMiliseconds = "0";
        return (Long.parseLong(dateInMiliseconds.replaceAll("[^\\d.]", "")));
    }
    public static String removeApiSegment(String url) {
        // Check if the URL contains "/api" and remove it
        if (url.contains("/api")) {
            return url.replace("/api", "");
        }
        // If "/api" is not present, return the original URL
        return url;
    }

    public static String previousyearDate(String currentDate) {
        try {
            int previousyear = Integer.parseInt(currentDate.split("-")[1]) - 1;
            return currentDate.split("-")[0] + "-" + previousyear + "-" + currentDate.split("-")[2];
        } catch (Exception e) {

        }
        return currentDate;
    }

    public static String oneYearPervious(String currentDate) {
        try {
            int previousyear = Integer.parseInt(currentDate.split("-")[0]) - 1;
            return previousyear + "-" + currentDate.split("-")[1] + "-" + currentDate.split("-")[2];
        } catch (Exception e) {

        }
        return currentDate;
    }

    public static String oneMonthPervious(String currentDate) {
        try {
            int previousyear = Integer.parseInt(currentDate.split("-")[0]) /*- 1*/;
            int previousMonth = Integer.parseInt(currentDate.split("-")[1]) - 1;
            return previousyear + "-" + previousMonth + "-" + currentDate.split("-")[2];
        } catch (Exception e) {

        }
        return currentDate;
    }

    public static String oneYearNext(String currentDate) {
        try {
            int previousyear = Integer.parseInt(currentDate.split("-")[0]) ;
            return previousyear + "-" + currentDate.split("-")[1] + "-" + currentDate.split("-")[2];
        } catch (Exception e) {

        }
        return currentDate;
    }

    public static Calendar dateMilisecondsToCalendar(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar;
    }

    public static String oneFormatToAnother(String date, String oldFormat,
                                            String newFormat) {
        try {
            DateFormat originalFormat = new SimpleDateFormat(oldFormat,
                    Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat(newFormat);
            Date d = originalFormat.parse(date);
            String formattedDate = targetFormat.format(d);
            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCurrentDateTime(String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar instance = Calendar.getInstance();
            String currentDateandTime = LocaleHelper.INSTANCE.arabicToEnglish(sdf.format(instance.getTime()));
            return currentDateandTime;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getPreviousMonthDateTime(String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MONTH, -1);
            String currentDateandTime = sdf.format(instance.getTime());
            return currentDateandTime;
        } catch (Exception e) {
            return null;
        }
    }


    public static String getInitialDateMonth(String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.ENGLISH);
            Calendar instance = Calendar.getInstance();
//            instance.add(Calendar.MONTH, -1);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            String currentDateandTime = sdf.format(instance.getTime());
            return currentDateandTime;
        } catch (Exception e) {
            return null;
        }
    }


    public static String  getSimpleDateFormat(String format) {
        String datenewFormat = null;
        String inputDate = format; // Example input date
        String inputFormat = "yyyy-MM-dd'T'HH:mm:ss"; // Example input date format
        String outputFormat = "yyyy-MM-dd"; // Desired output date format
        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
        Date date;
        try {
            date = inputFormatter.parse(inputDate);
            SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);
            datenewFormat = outputFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datenewFormat;
    }




    public static String  getDateFormat(String format) {
        String datenewFormat = null;
        String inputDate = format; // Example input date
        String inputFormat = "yyyy-MM-dd'T'HH:mm:ss"; // Example input date format
        String outputFormat = "dd/MM/yy"; // Desired output date format
        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
        Date date;
        try {
            date = inputFormatter.parse(inputDate);
            SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);
            datenewFormat = outputFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datenewFormat;
    }




    public static String simpleTimeConversionNew(String time) {
        String timeString = time; // or "2024-05-08T12:30:45:678"

        // Define regular expressions for both formats
        String regex1 = "\\d{4}-\\d{2}-\\d{2}T(\\d{2}:\\d{2}):\\d{2}";
        String regex2 = "\\d{4}-\\d{2}-\\d{2}T(\\d{2}:\\d{2}:\\d{2}).\\d{3}";

        // Create Pattern objects
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);

        // Create Matcher objects
        Matcher matcher1 = pattern1.matcher(timeString);
        Matcher matcher2 = pattern2.matcher(timeString);

        // Check if the string matches either format and extract the time
        String times = null;
        if (matcher1.matches()) {
            times = matcher1.group(1);
        } else if (matcher2.matches()) {
            times = matcher2.group(1);

           // convertTimeToHourMinute(times);

        } else {
            System.out.println("Invalid time format");

        }
        return times;
    }

    public static String convertTimeToHourMinute(String time) {
        String[] parts = time.split(":");
        if (parts.length >= 2) {
            return parts[0] + ":" + parts[1]; // Hour:Minute
        }
        return time; // Return the original time if it cannot be split
    }



        public static String simpleTimeConversion(String time) {
            String inputDateTimeString = time;

            // Parse the input string to LocalTime
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            }
            LocalTime inputTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                inputTime = LocalTime.parse(inputDateTimeString, formatter);
            }

            // Format the LocalTime to the desired format
            String outputTimeString = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                outputTimeString = inputTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            }

            return outputTimeString;

    }

    public static String simpleTimeOnlyConversion(String time) {
        String inputDateTimeString = time;

        // Parse the input string to LocalTime
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        }
        LocalTime inputTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            inputTime = LocalTime.parse(inputDateTimeString, formatter);
        }

        // Format the LocalTime to the desired format
        String outputTimeString = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            outputTimeString = inputTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
        }

        return outputTimeString;

    }

    public static String simpleTimeOnlyConversion24hoursFormat(String time) {
        String inputDateTimeString = time;

        // Parse the input string to LocalTime
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("HH:mm");
        }
        LocalTime inputTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            inputTime = LocalTime.parse(inputDateTimeString, formatter);
        }

        // Format the LocalTime to the desired format
        String outputTimeString = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            outputTimeString = inputTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }

        return outputTimeString;

    }



/*

        try {
            Date d = dateFormat.parse(format);
            System.out.println("DATE" + d);
            System.out.println("Formated" + dateFormat.format(d));
        } catch (Exception e) {
            //java.text.ParseException: Unparseable date: Geting error
            System.out.println("Excep" + e);
        }
*/


    public static String getFinalDateMonth(String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.ENGLISH);
            Calendar instance = Calendar.getInstance();
//            instance.add(Calendar.MONTH, -1);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            instance.set(Calendar.DATE, instance.getActualMaximum(Calendar.DATE));
            Date lastDayOfMonth = instance.getTime();

            String currentDateandTime = sdf.format(lastDayOfMonth);
            return currentDateandTime;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentDateWithAddedDays(String format) {
        try {
           /* SimpleDateFormat sdf = new SimpleDateFormat(format);
            String currentDateandTime = sdf.format(new Date());*/

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date()); // Now use today date.
            c.add(Calendar.DATE, 10);
            String output = sdf.format(c.getTime());
            System.out.println(output);

            return output;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentDateTimeInUTC(String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
            String currentDateandTime = sdf.format(new Date());
            return currentDateandTime;
        } catch (Exception e) {
            return null;
        }
    }

    public static Calendar getCalendarFromFormat(String date, String format) {
        Calendar cal = Calendar.getInstance();
        try {
            DateFormat originalFormat = new SimpleDateFormat(format,
                    Locale.ENGLISH);
            Date d = originalFormat.parse(date);
            cal.setTime(d);
            return cal;
        } catch (Exception e) {
            return null;
        }
    }


    public static boolean isFirstDateGreater(Date date1, Date date2) {

        long miliSeconds1 = date1.getTime();
        long miliSeconds2 = date2.getTime();

        return miliSeconds1 > miliSeconds2;
    }

    public static String getDayOfweek() {
//        String dayName = "";
        Calendar calendar = Calendar.getInstance();
//        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return new SimpleDateFormat("EEE").format(calendar.getTime());
//        switch (day) {
//            case Calendar.SUNDAY:
//                dayName = "Sunday";
//                break;
//            case Calendar.MONDAY:
//                dayName = "Monday";
//                break;
//            case Calendar.TUESDAY:
//                dayName = "Tuesday";
//                break;
//            case Calendar.WEDNESDAY:
//                dayName = "Wednesday";
//                break;
//            case Calendar.THURSDAY:
//                dayName = "Thursday";
//                break;
//            case Calendar.FRIDAY:
//                dayName = "Friday";
//                break;
//            case Calendar.SATURDAY:
//                dayName = "Saturday";
//                break;
//        }
//        return dayName;
    }
}
