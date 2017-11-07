package IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

public class Terminal {
    private BufferedReader stdIn;
    private ArrayList<String> messageQueue;

    public Terminal() {
        this.stdIn = new BufferedReader(new InputStreamReader(System.in));
        this.messageQueue = new ArrayList<>();
    }

    public String receiveCommand() {
        String userInput = "";
        try {
            System.out.print(">> ");
            userInput = stdIn.readLine();
        } catch (IOException e) {
            System.out.println("IOException, could not read from std in");
        }
        return userInput;
    }

    public void helloMessage(int asId) {
        System.out.println("Welcome to the AS #" + asId +" terminal!");
    }

    public synchronized void addNewMessage(String msg) {
        this.messageQueue.add(msg);
    }

    public synchronized void readMessages() {
        System.out.println();
        System.out.println("-------------------Messages-------------------");
        for (String msg : this.messageQueue) {
            System.out.println("*** " + msg);
        }
        this.messageQueue.clear();
        System.out.println("----------------------------------------------");
        System.out.println();
    }

    public String isMessage() {
        if (this.messageQueue.isEmpty()) {
            return "No new messages.";
        } else {
            if (this.messageQueue.size() > 1) {
                return "There are (" + this.messageQueue.size() + ") new messages.";
            } else {
                return "There is (" + this.messageQueue.size() + ") new message.";
            }
        }
    }
}
