import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.script.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class Main {
    private static final String IN_FILE_NAME = "SDOA_FY23P10W5.xlsx";

    private static final String OUT_FILE_NAME = "SDOA_FY23P10W5_DEBUG.xlsx";
    private static final String JS_SCRIPT = "sdoa.js";

    public static void allowBlanksOnExcel(Workbook workbook, int wide) {
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();

        while (iterator.hasNext()) {

            Row currentRow = iterator.next();

            for (int r = 0; r < wide; r++) {
                currentRow.getCell(r, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            }

        }
    }

    public static void main(String[] args) {
        try {

            FileInputStream excelFile = new FileInputStream(new File(IN_FILE_NAME));
            Workbook workbook = new XSSFWorkbook(excelFile);

            allowBlanksOnExcel(workbook, 40);

            Path scriptPath = Paths.get(JS_SCRIPT);
            // start javascript engine on java
            ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");
            Bindings bindings = js.getBindings(ScriptContext.ENGINE_SCOPE);

            bindings.put("stdout", System.out);
            bindings.put("Log", new Log());
            bindings.put("workbook", workbook);
            bindings.put("http", new HttpHandler());


            //System.out.println(js.eval("'hello-js-in-java-'+thing"));
            Reader scriptReader = Files.newBufferedReader(scriptPath);
            workbook = (Workbook) js.eval(scriptReader);

            //workbook.getSheet().getRow().getCell().setAsActiveCell();

            FileOutputStream outputStream;

            outputStream = new FileOutputStream(/*file+"\\export.xlsx"*/OUT_FILE_NAME);

            workbook.write(outputStream);

            outputStream.close();

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            StackTraceElement[] StackTrace = e.getStackTrace();
            for (StackTraceElement item:StackTrace) {
                Log.out(item.toString());
            }
        }
    }
}