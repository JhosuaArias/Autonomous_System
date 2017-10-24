import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnection extends Thread {

    private As as;
    private ServerSocket serverSocket;
    private Socket socket;

    public ServerConnection(As as, ServerSocket serverSocket, Socket socket) {
        this.as = as;
        this.serverSocket = serverSocket;
        this.socket = socket;
    }

    @Override
    public  void run() {

    }

}
