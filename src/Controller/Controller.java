package Controller;

import AsDynamics.As;
import IO.FileParser;
import IO.LogWriter;
import IO.Terminal;
import java.io.FileNotFoundException;

public class Controller {

    private As autonomousSystem;
    private boolean started;

    public Controller(String fileName) {
        FileParser parser = new FileParser();
        started = false;
        try {
           this.autonomousSystem = parser.createAS(fileName);
        } catch (Exception e) {
            System.err.println("Error parsing the file " + fileName);
        }

        try {
            LogWriter logWriter = new LogWriter(this.autonomousSystem.getId());
            this.autonomousSystem.setLogWriter(logWriter);
        } catch (FileNotFoundException e) {
            System.out.println("Something went wrong and the Log could not be created");
        }
    }

    public void listenToTerminal() {
        Terminal terminal = new Terminal();
        terminal.helloMessage(this.autonomousSystem.getId());
        this.autonomousSystem.setTerminal(terminal);

        String command;
        while(!(command = terminal.receiveCommand()).equals("stop")) {
            command = command.trim().replaceAll(" +", " ");  // removes unwanted whitespace
            this.parseCommand(command, terminal);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        // the "stop" command was used
        this.stopCommand();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //Do nothing
        }
        System.exit(0);
    }

    private void parseCommand(String command, Terminal terminal) {
        final String ADDRESS_FORMAT = "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";

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

        } else if (command.equals("msg?")) {
            System.out.println(terminal.isMessage());

        } else if (command.equals("show msg")) {
            terminal.readMessages();

        } else {
            System.out.println("Unsupported command or wrong quantity of parameters, use the \"help\" command if needed");
        }
    }

    private void startCommand() {
        if (!this.started) {
            this.started = true;
            this.autonomousSystem.start();
        } else {
            System.err.println("The AS is already started");
        }

    }

    private void helpCommand() {
        System.out.println("The supported commands are: \n -help \n -start \n -stop \n -show routes \n -add <<subnet address>> \n -show msg \n -msg?");
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
