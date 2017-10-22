import java.net.Socket;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class As {


    private final String ADDRESS_FORMAT = "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";

    public As() {
        
    }


    public void start() {
        System.out.println("Start");
    }

    public void stop() {
        System.out.println("Stop");
    }

    public void showRoutes() {
        System.out.println("Show Routes");
    }

    public void addSubNetwork(String address) {
        System.out.println("Add "+ address);
    }

    public void help() {
        System.out.println("Help");
    }

    public void consoleTokenizer(String command) {
        StringTokenizer stringTokenizer = new StringTokenizer(command);
        if (command.compareTo("start") == 0) {
            this.start();
        } else if (command.compareTo("stop") == 0) {
            this.stop();
        } else if (command.compareTo("show routes") == 0) {
            this.showRoutes();
        } else if (command.compareTo("help") == 0) {
            this.help();
        } else if (stringTokenizer.nextToken().compareTo("add") == 0) {
            String address = stringTokenizer.nextToken();
            Pattern pattern = Pattern.compile(ADDRESS_FORMAT);
            Matcher matcher = pattern.matcher(address);
            if (matcher.matches()) {
                this.addSubNetwork(address);
            } else {
                System.out.println("The given address is not valid");
            }
        } else {
            System.out.println("Unknown command");
        }
    }

    public void fileTokenizer() {

    }


    public static void main(String[] args) {
        As as = new As();
        as.consoleTokenizer("add 192.168.0.0");
        as.consoleTokenizer("add 999.999.999");
    }
}
