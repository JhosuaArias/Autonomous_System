import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnection extends Thread {

    private As as;
    private ServerSocket serverSocket;
    private Socket socket;

    public ServerConnection(As as, ServerSocket serverSocket, Socket socket) {
        this.as = as;
        this.serverSocket = serverSocket;
        this.socket = socket;
    }


    public void listenMessages() throws Exception{
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.println("Receiving message");


        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String str = br.readLine();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pw.println("Got it: " + str);
        pw.close();
        socket.close();

        System.out.println("Just receive :" + str);

        this.socket = serverSocket.accept();
    }

    @Override
    public  void run() {
        boolean retry = true;
        while(retry) {
            try {
                this.listenMessages();
            } catch (Exception e) {
                try {
                    this.socket.close();
                } catch (IOException e1) {
                    //e1.printStackTrace();
                }
               // e.printStackTrace();
                retry = false;
            }
        }
    }

}
