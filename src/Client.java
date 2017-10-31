

import java.io.*;
import java.net.Socket;

public class Client extends Thread{
    private As as;
    private Socket clientSocket;
    private String ip;
    private int port;
    private String neighborAsId;

    public Client(As as, String ip, int port) {

        this.ip = ip;
        this.port = port;
        this.as = as;
        this.neighborAsId = "";

    }


    public  void sendMessage() throws IOException{

        this.clientSocket = new Socket(this.ip, this.port);

        BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);

        out.println(this.as.getUpdateMessage(this.neighborAsId));
        String updateMessage = null;
        while (updateMessage == null) {
            updateMessage = in.readLine();
        }
        this.neighborAsId = updateMessage.substring(0, updateMessage.indexOf('*'));

        this.clientSocket.close();

        System.out.println("Just receive :" + updateMessage);
        this.as.parseUpdateMessage(updateMessage);
    }

    public void kill() {
        try {
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    @Override
    public void run() {
        boolean retry = true;
        while (true){

            try {
                System.err.println("Trying to send message");
                this.sendMessage();
            } catch (IOException e) {

            }

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {

            }
        }
    }

}
