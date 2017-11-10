package IO;

import sun.rmi.runtime.Log;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogWriter {

    private PrintWriter ofile;

    public LogWriter(int id) throws FileNotFoundException {
        this.ofile = new PrintWriter("LogAS" + id + ".txt");

    }

    public void writeIntoLog(String message) {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS").format(Calendar.getInstance().getTime());
        ofile.println("[" + timeStamp + "]: " + message);
    }

    public void close() {
        ofile.close();
    }

}
