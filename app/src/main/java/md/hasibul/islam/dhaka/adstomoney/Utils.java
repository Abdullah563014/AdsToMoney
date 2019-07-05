package md.hasibul.islam.dhaka.adstomoney;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String todayDate(){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String formattedDate = df.format(date.getTime());
        return formattedDate;
    }
}
