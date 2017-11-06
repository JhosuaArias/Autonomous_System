

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

        long initialTime = System.currentTimeMillis();
        long currentTime = initialTime;

        while (currentTime - initialTime <= 30000 && updateMessage == null) {
            updateMessage = in.readLine();
            currentTime = System.currentTimeMillis();
            System.err.println(currentTime-initialTime);
        }

        if (currentTime - initialTime > 30000) {
            this.finishConnection();
        }

        if (this.neighborAsId.equals("")) {
            this.neighborAsId = updateMessage.substring(0, updateMessage.indexOf('*'));
        }

        this.clientSocket.close();
        this.clientSocket = null;
        this.as.parseUpdateMessage(updateMessage);
    }

    public void kill() {
        if (this.clientSocket != null) {
            try {
                this.clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.err.println("Client stopped listening");
        this.stop();
    }

    private void finishConnection () {

        this.as.deleteAllRoutesWithAS(this.neighborAsId);
        this.kill();

    }

    @Override
    public void run() {
        boolean retry = true;
        while (true){

            try {
                this.sendMessage();
            } catch (IOException e) {
                if(!this.neighborAsId.equals("")) {
                    this.finishConnection();
                }
            }

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {

            }
        }
    }

}
