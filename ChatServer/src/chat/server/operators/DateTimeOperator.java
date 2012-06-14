package chat.server.operators;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeOperator {

    private final static DateFormat dateFormat = new SimpleDateFormat(
	    "dd.MM.yyyy HH:mm:ss");

    public static String getCurrentDateTime() {
	Date date = new Date();
	return dateFormat.format(date);
    }
}
