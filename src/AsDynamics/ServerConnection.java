package AsDynamics;

import java.io.*;
import java.net.Socket;

public class ServerConnection extends Thread {

    private As as;
    private Socket clientSocket;
    private boolean retry;
    private String neighborAsId;

    private BufferedReader in;
    private PrintWriter out;

    ServerConnection (As as, Socket clientSocket) {
        this.as = as;
        this.clientSocket = clientSocket;
        this.neighborAsId = "";
    }


    private void listenMessages() throws IOException {

        if (this.in == null) {
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        }

        if (this.out == null) {
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        }

        String updateMessage = null;

        long initialTime = System.currentTimeMillis();
        long currentTime = initialTime;

        while (currentTime - initialTime <= 30000 && updateMessage == null) {
            updateMessage = in.readLine();
            currentTime = System.currentTimeMillis();
        }

        if (updateMessage == null) {
            this.finishConnection();
        } else {

            if (this.neighborAsId.equals("")) {
                this.neighborAsId = updateMessage.substring(0, updateMessage.indexOf('*'));
                this.as.depositMessage("Server successfully connected with " + this.neighborAsId + ".");
            }

            out.println(this.as.getUpdateMessage(updateMessage.substring(0, updateMessage.indexOf('*'))));

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

        this.as.depositMessage("Client of " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " didn't respond, finishing connection");
        this.as.deleteAllRoutesWithAS(this.neighborAsId);
        this.as.depositMessage("All routes with " + (this.neighborAsId.equals("")? "AS??":this.neighborAsId) + " have been deleted.");
        this.kill();

    }

    @Override
    public void run() {
        this.retry = true;

        this.as.depositMessage("Server with AS?? started.");
        while (this.retry) {
            try {
                this.listenMessages();
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
