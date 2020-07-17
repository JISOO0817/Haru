package com.jisooZz.haru.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    /**
     * 입력한 Date Format에 따라 날짜 반환
     * */
    public static String getDateTime(String type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(type, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
