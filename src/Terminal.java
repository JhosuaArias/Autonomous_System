import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Terminal {

    private As as;

    private final String ADDRESS_FORMAT = "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";

    public Terminal() {
        As as = new As();
    }

    public void consoleTokenizer(String command) {
        StringTokenizer stringTokenizer = new StringTokenizer(command);
        if (command.compareTo("start") == 0) {
            as.start();
        } else if (command.compareTo("stop") == 0) {
            as.stop();
        } else if (command.compareTo("show routes") == 0) {
            as.showRoutes();
        } else if (command.compareTo("help") == 0) {
            as.help();
        } else if (stringTokenizer.nextToken().compareTo("add") == 0) {
            String address = stringTokenizer.nextToken();
            Pattern pattern = Pattern.compile(ADDRESS_FORMAT);
            Matcher matcher = pattern.matcher(address);
            if (matcher.matches()) {
                as.addSubNetwork(address);
            } else {
                System.out.println("The given address is not valid");
            }
        } else {
            System.out.println("Unknown command");
        }
    }
}
