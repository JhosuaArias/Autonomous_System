

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread{
    private As as;
    private Socket socket;
    private String ip;
    private int port;
    private String neighborAsId;
    public Client(As as, String ip, int port) {

        this.ip = ip;
        this.port = port;
        this.as = as;

    }


    public  void sendMessage() throws IOException{
        this.socket = new Socket(this.ip, this.port);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println(as.getUpdateMessage("AS2"));

        //System.out.println("server says:" + br.readLine());
    }

    public void kill() {
        try {
            this.socket.close();
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
                Thread.sleep(30000);
            } catch (InterruptedException|IOException e) {
                retry = false;
            }
        }
    }

}
