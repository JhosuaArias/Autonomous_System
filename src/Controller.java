
public class Controller implements Runnable {

    //AS autonomousSystem;

    Controller(String fileName) {
        //FileParser parser = new FileParser();
        //this.autonomousSystem= parser.createAS();
    }

    private void listenToTerminal() {
        Terminal terminal = new Terminal();
        String command;
        while(!(command = terminal.receiveCommand()).equals("stop")) {
            command = command.trim().replaceAll(" +", " ");  // removes unwanted whitespace
            parseCommand(command);
        }
    }

    private void parseCommand(String command) {
        if (command.equals("help")) {
            this.helpCommand();
        } else if (command.equals("start")) {
            this.startCommand();
        } else if(command.startsWith("add ") && command.split(" ").length == 2) { //whitespace at the end to ensure that a parameter is present (string is trimmed)
            this.addSubnetCommand(command.split(" ")[1]);  //sends only the address as parameter
        } else if (command.equals("show routes")) {
            this.showRoutesCommand();
        } else {
            System.out.println("Unsupported command or wrong quantity of parameters, use the \"help\" command if needed");
        }
    }

    public void run() {
        this.listenToTerminal();
    }

    private void startCommand() {
        //this.autonomousSystem.start();
    }

    private void helpCommand() {
        System.out.println("The supported commands are: \n -help \n -start \n -stop \n -show routes \n -add <<subnet address>>");
    }

    private void showRoutesCommand() {
        //this.autonomousSystem.showRoutes();
    }

    private void addSubnetCommand(String subnet) {
        //this.autonomousSystem.addSubNetwork();
    }
}
