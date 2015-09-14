package com.flatstack.flatstackvk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd, hh:mm");

    public static String getFormattedTime(long time) {
        Date date = new Date((long)time * 1000);
        return dateTimeFormat.format(date);
    }
}
