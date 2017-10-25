package Server_Client_Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{
    ServerSocket serverSocket;
    Socket socket;
    public ServerThread(ServerSocket serverSocket ,Socket socket) {
        this.serverSocket = serverSocket;
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            while (true) {

                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                pw.println("What's you name?");


                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String str = br.readLine();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pw.println("Hello, " + str);
                pw.close();
                socket.close();

                System.out.println("Just said hello to:" + str);

                this.socket = serverSocket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
