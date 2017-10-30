
public class Controller {

    // ALT+SHIFT+F10, Right, E, Enter, Tab  : para poner comandos en el main en IntelliJ
    public static void main(String[] args) {
        if (args.length > 0) {
            Controller controller = new Controller(args[0]);
            controller.listenToTerminal();
        } else {
            System.err.println("Please, enter the file name!");
        }
    }

    private As autonomousSystem;
    private final String ADDRESS_FORMAT = "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";

    public Controller(String fileName) {
        FileParser parser = new FileParser();
        try {
           this.autonomousSystem = parser.createAS(fileName);
        } catch (Exception e) {
            System.err.println("Error parsing the file " + fileName);
        }
    }

    private void listenToTerminal() {
        Terminal terminal = new Terminal();
        terminal.helloMessage(this.autonomousSystem.getId());
        String command;
        while(!(command = terminal.receiveCommand()).equals("stop")) {
            command = command.trim().replaceAll(" +", " ");  // removes unwanted whitespace
            this.parseCommand(command);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        // the "stop" command was used
        this.stopCommand();
    }

    private void parseCommand(String command) {
        if (command.equals("help")) {
            this.helpCommand();

        } else if (command.equals("start")) {
            this.startCommand();

        } else if(command.startsWith("add ") && command.split(" ").length == 2) { //whitespace at the end to ensure that a parameter is present (string is trimmed)
            String ipAddress = command.split(" ")[1];
            if (ipAddress.matches(ADDRESS_FORMAT)) {
                this.addSubnetCommand(ipAddress);  //sends only the address as parameter

            } else {
                System.out.println("Invalid input ip address: " + ipAddress);
            }

        } else if (command.equals("show routes")) {
            this.showRoutesCommand();

        } else {
            System.out.println("Unsupported command or wrong quantity of parameters, use the \"help\" command if needed");
        }
    }

    private void startCommand() {
        this.autonomousSystem.start();
    }

    private void helpCommand() {
        System.out.println("The supported commands are: \n -help \n -start \n -stop \n -show routes \n -add <<subnet address>>");
    }

    private void showRoutesCommand() {
        this.autonomousSystem.showRoutes();
    }

    private void addSubnetCommand(String subnet) {
        this.autonomousSystem.addSubNetwork(subnet);
    }

    private void stopCommand() {
        this.autonomousSystem.stop();
    }
}
