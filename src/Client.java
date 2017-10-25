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
    private double pastTimeMessage;
    public Client(As as, String ip, int port) {

        try {
            socket = new Socket(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ip = ip;
        this.port = port;
        this.as = as;

    }


    public  void sendMessage() throws IOException{
        this.socket = new Socket(this.ip, this.port);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("server says:" + br.readLine());

        /*BufferedReader userInputBR = new BufferedReader(new InputStreamReader(System.in));
        String userInput = userInputBR.readLine();*/
        //TODO UNCOMMENT WHEN THE METHOD EXISTS
        out.println(as.generateUpdateMessage());

        System.out.println("server says:" + br.readLine());
    }


    @Override
    public void run() {
        boolean retry = true;
        while (retry){

            try {
                this.sendMessage();
                Thread.sleep(3000);
            } catch (InterruptedException|IOException e) {
                retry = false;
            }
        }
    }


    public static void main(String[] args) {
        Client client = new Client(new As(1,1,null,null),"localhost",81);
        client.start();
    }
}
