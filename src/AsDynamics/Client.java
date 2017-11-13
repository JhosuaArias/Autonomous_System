package AsDynamics;

import java.io.*;
import java.net.Socket;

public class Client extends Thread {

    private String ip;
    private int port;

    private As as;
    private Socket clientSocket;
    private String neighborAsId;
    private boolean retry;

    private DataInputStream in;
    private DataOutputStream out;

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
            this.in = new DataInputStream(this.clientSocket.getInputStream());
        }

        if (this.out == null) {
            this.out = new DataOutputStream(this.clientSocket.getOutputStream());
        }

        this.out.writeUTF(this.as.getUpdateMessage(this.neighborAsId));
        String updateMessage = null;

        long initialTime = System.currentTimeMillis();
        long currentTime = initialTime;

        while (currentTime - initialTime <= 30000 && updateMessage == null) {
            updateMessage = this.in.readUTF();
            currentTime = System.currentTimeMillis();
        }

        if (updateMessage == null) {
            throw new IOException();
        } else {

            if (this.neighborAsId.equals("")) {
                this.neighborAsId = updateMessage.substring(0, updateMessage.indexOf('*'));
                this.as.depositMessage("Client successfully connected with " + neighborAsId + ".");
                this.as.getLogWriter().writeIntoLog("Client successfully connected with " + neighborAsId + ".");
            }

            this.as.depositMessage("New update message from " + this.neighborAsId);
            this.as.getLogWriter().writeIntoLog("New update message from " + this.neighborAsId);
            this.as.parseUpdateMessage(updateMessage);
        }
    }

    synchronized void kill() {

        if (this.in != null) {
            try {
                this.in.close();
            } catch (IOException e) {
                //Do nothing
            }
        }

        if (this.out != null) {
            try {
                this.out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private void handleNoConnection() {
        this.as.depositMessage("Server of " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " didn't respond, retrying connection.");
        this.as.getLogWriter().writeIntoLog("Server of " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " didn't respond, retrying connection.");

        if(!this.neighborAsId.equals("")) {
            this.as.deleteAllRoutesPropagatedByAS(this.neighborAsId);
            this.as.depositMessage("All routes with " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " have been deleted.");
            this.as.getLogWriter().writeIntoLog("All routes with " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " have been deleted.");
        }

        this.clientSocket = null;
        this.in = null;
        this.out = null;
    }

    @Override
    public void run() {
        this.retry = true;
        this.as.depositMessage("Client with AS?? started.");
        this.as.getLogWriter().writeIntoLog("Client with AS?? started.");
        while (retry){

            try {
                this.sendMessage();
            } catch (IOException e) {
                this.handleNoConnection();
            }

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                //Nothing should happen
            }
        }

    }

}
