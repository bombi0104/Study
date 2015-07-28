package jp.ne.biglobe.biglobeapp.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by taipa on 7/21/15.
 */
public class Utils {

    /**
     * Compare 2 date.
     *
     * @param d1 Date1 YYYYMMDD
     * @param d2 Date2 YYYYMMDD
     * @return true : d1 > d2
     * false : others
     */
    public static boolean compareDate(String d1, String d2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date1 = dateFormat.parse(d1);
            Date date2 = dateFormat.parse(d2);

            if (date1.compareTo(date2) > 0) {
                return true;
            }
            return false;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
