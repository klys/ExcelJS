import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {

    private static Calendar c;
    public static void out(String message) {
        try {
            c = Calendar.getInstance();
            SimpleDateFormat formater = new SimpleDateFormat(/*Setting.logTimeFormat*/"yyyy-MM-dd hh:mm:ss");
            message = "["+formater.format(c.getTime())+"] "+ message + System.lineSeparator();
            System.out.println(message);
            Files.write(Paths.get(/*Setting.AppDir+"/"+Setting.logFileName*/"log.txt"), message.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void in(String message) {
        Log.out(message);
    }
}
