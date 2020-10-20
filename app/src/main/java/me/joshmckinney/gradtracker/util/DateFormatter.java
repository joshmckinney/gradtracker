package me.joshmckinney.gradtracker.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public static String toDate(Date date) {
        String dateFormat = "MM/dd/yy";
        String output = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        output = sdf.format(date.getTime());
        return output;
    };
}
