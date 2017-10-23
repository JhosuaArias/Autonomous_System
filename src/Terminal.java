import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Terminal {
    BufferedReader stdIn;

    Terminal() {
        stdIn = new BufferedReader(new InputStreamReader(System.in));
    }

    String receiveCommand() {
        String userInput = "";
        try {
            userInput = stdIn.readLine();
        } catch (IOException e) {
            System.out.println("IOException, could not read from std in");
        }
        return userInput;
    }
}
