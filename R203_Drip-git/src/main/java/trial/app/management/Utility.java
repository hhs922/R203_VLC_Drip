package trial.app.management;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
    protected static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    protected static void debug(String msg) {
        System.out.println(msg);
    }

    protected static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    protected static String timeCode(Date date) {
        return date == null ? "null" : format.format(date);
    }
    protected static String currentTimeCode() {
        return timeCode(new Date());
    }
}
