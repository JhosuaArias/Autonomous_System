package Server_Client_Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerTest {
    public static void main(String[] args)  throws IOException {
        final int portNumber = 81;
        System.out.println("Creating server socket on port " + portNumber);
        ServerSocket serverSocket = new ServerSocket(portNumber);

        ArrayList<ServerThread> allConnections = new ArrayList<>();

        while (true) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(serverSocket,socket);
            allConnections.add(serverThread);
            serverThread.start();

        }
    }
}
