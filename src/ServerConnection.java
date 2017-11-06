import java.io.*;
import java.net.Socket;

public class ServerConnection extends Thread {

    private As as;
    private Socket clientSocket;

    public ServerConnection(As as, Socket clientSocket) {
        this.as = as;
        this.clientSocket = clientSocket;
    }


    public void listenMessages() throws Exception {

        BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);

        String updateMessage = null;
        while (updateMessage == null) {
            updateMessage = in.readLine();
        }
        out.println(this.as.getUpdateMessage(updateMessage.substring(0, updateMessage.indexOf('*'))));

        this.clientSocket.close();

        this.as.parseUpdateMessage(updateMessage);
    }

    @Override
    public void run() {
        try {
            this.listenMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
