import Controller.Controller;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            Controller controller = new Controller(args[0]);
            controller.listenToTerminal();
        } else {
            System.err.println("Please, enter the file name!");
        }
    }

}
