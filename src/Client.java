
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class Client extends Thread{
    private As as;
    private Socket socket;
    private String ip;
    private int port;
    private String neighborAsId;
    private boolean firstMessage;

    public Client(As as, String ip, int port) {

        this.ip = ip;
        this.port = port;
        this.as = as;
        this.firstMessage = true;

    }


    public  void sendMessage() throws IOException{
        this.socket = new Socket(this.ip, this.port);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);



        /*BufferedReader userInputBR = new BufferedReader(new InputStreamReader(System.in));
        String userInput = userInputBR.readLine();*/
        //TODO UNCOMMENT WHEN THE METHOD EXISTS
        if(this.firstMessage) {
            getNeighborId(br.readLine());
        }

        out.println(as.getUpdateMessage("AS"));

        //System.out.println("server says:" + br.readLine());
    }

    private void getNeighborId(String message) {
        this.neighborAsId = message;
        this.firstMessage = false;
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
                this.sendMessage();
                Thread.sleep(3000);
            } catch (InterruptedException|IOException e) {
                retry = false;
            }
        }
    }

}
