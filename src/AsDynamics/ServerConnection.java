package AsDynamics;

import java.io.*;
import java.net.Socket;

public class ServerConnection extends Thread {

    private As as;
    private Socket clientSocket;
    private boolean retry;
    private String neighborAsId;
    boolean closed;

    private DataInputStream in;
    private DataOutputStream out;

    ServerConnection (As as, Socket clientSocket) {
        this.as = as;
        this.clientSocket = clientSocket;
        this.neighborAsId = "";
    }


    private void listenMessages() throws IOException {

        if (this.in == null) {
            this.in = new DataInputStream(this.clientSocket.getInputStream());
        }

        if (this.out == null) {
            this.out = new DataOutputStream(this.clientSocket.getOutputStream());
        }

        String updateMessage = null;

        long initialTime = System.currentTimeMillis();
        long currentTime = initialTime;

        while (currentTime - initialTime <= 30000 && updateMessage == null) {
            updateMessage = in.readUTF();
            currentTime = System.currentTimeMillis();
        }

        if (updateMessage == null) {
            this.finishConnection();
        } else {

            if (this.neighborAsId.equals("")) {
                this.neighborAsId = updateMessage.substring(0, updateMessage.indexOf('*'));
                this.as.depositMessage("Server successfully connected with " + this.neighborAsId + ".");
                this.as.getLogWriter().writeIntoLog("Server successfully connected with " + this.neighborAsId + ".");
            }

            out.writeUTF (this.as.getUpdateMessage(updateMessage.substring(0, updateMessage.indexOf('*'))));

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
                //Do nothing
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
        this.closed = true;

    }

    private void finishConnection () {

        this.as.depositMessage("Client of " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " didn't respond, finishing connection");
        this.as.getLogWriter().writeIntoLog("Client of " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " didn't respond, finishing connection");
        this.as.deleteAllRoutesPropagatedByAS(this.neighborAsId);
        this.as.depositMessage("All routes with " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " have been deleted.");
        this.as.getLogWriter().writeIntoLog("All routes with " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " have been deleted.");
        this.kill();

    }

    @Override
    public void run() {
        this.retry = true;

        this.as.depositMessage("Server with AS?? started.");
        this.as.getLogWriter().writeIntoLog("Server with AS?? started.");
        while (this.retry) {
            try {
                this.listenMessages();
            } catch (IOException e) {
                this.finishConnection();
            }

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                //Nothing should happen
            }
        }

    }

}
