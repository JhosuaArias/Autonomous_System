package AsDynamics;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class Client extends Thread {

    private String ip;
    private int port;

    private As as;
    private Socket clientSocket;
    private String neighborAsId;
    private boolean retry;

    private BufferedReader in;
    private PrintWriter out;

    Client(As as, String ip, int port) {

        this.ip = ip;
        this.port = port;
        this.as = as;
        this.neighborAsId = "";

    }


    private void sendMessage() throws IOException {

        if (this.clientSocket == null) {
            this.clientSocket = new Socket(this.ip, this.port);
        }

        if (this.in == null) {
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        }

        if (this.out == null) {
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        }

        this.out.println(this.as.getUpdateMessage(this.neighborAsId));
        String updateMessage = null;

        long initialTime = System.currentTimeMillis();
        long currentTime = initialTime;

        while (currentTime - initialTime <= 30000 && updateMessage == null) {
            updateMessage = this.in.readLine();
            currentTime = System.currentTimeMillis();
        }

        if (updateMessage == null) {
            this.finishConnection();
        } else {

            if (this.neighborAsId.equals("")) {
                this.neighborAsId = updateMessage.substring(0, updateMessage.indexOf('*'));
                this.as.depositMessage("Client successfully connected with " + neighborAsId + ".");
            }

            this.as.depositMessage("New update message from " + this.neighborAsId);
            this.as.parseUpdateMessage(updateMessage);
        }
    }

    void kill() {

        if (this.in != null) {
            try {
                this.in.close();
            } catch (IOException e) {
                //Do nothing
            }
        }

        if (this.out != null) {
            this.out.close();
        }

        if (this.clientSocket != null) {
            try {
                this.clientSocket.close();
            } catch (IOException e) {
                //Do nothing
            }
        }

        this.retry = false;

    }

    private void finishConnection () {
        this.as.depositMessage("Server of " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " didn't respond, finishing connection.");
        this.as.deleteAllRoutesWithAS(this.neighborAsId);
        this.as.depositMessage("All routes with " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " have been deleted.");
        this.kill();

    }

    @Override
    public void run() {
        this.retry = true;
        this.as.depositMessage("Client with AS?? started.");
        while (retry){

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
                //Nothing should happen
            }
        }

    }

}
