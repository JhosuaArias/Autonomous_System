import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class As {

    private int id;
    private int port;
    private ArrayList<String> knownSubnetworks;
    private HashMap<String, Integer> bgpNeighbors;
    private RoutingTable routingTable;

    private Server listenerServer;
    private ArrayList<Client> clients;

    public As(int id, int port, ArrayList<String> knownSubnetworks, HashMap<String, Integer> bgpNeighbors) {
        this.id = id;
        this.port = port;
        this.knownSubnetworks = knownSubnetworks;
        this.bgpNeighbors = bgpNeighbors;
        this.routingTable = new RoutingTable();
        this.clients = new ArrayList<>();
    }

    public void start() {

        //this.listenerServer = new Server(this, this.port);
        //this.listenerServer.start()

        for (Map.Entry<String, Integer> entry : this.bgpNeighbors.entrySet()) {
            //Client client = new Client(this, entry.getKey(), entry.getValue());
            //this.clients.add(client);
            //client.start();
        }

    }

    public void stop() {

        //this.listenerServer.forceStop();
        for (Client client : this.clients) {
            //client.forceStop();
        }

    }

    public String showRoutes() {
        return this.routingTable.print();
    }

    public void addSubNetwork(String address) {
        System.out.println("Add "+ address);
    }

    public void help() {
        System.out.println("Help");
    }

    public synchronized String getUpdateMessage (String receivingAS) {
        return "AS" + this.id  + "*" + this.routingTable.generateUpdateMessage(receivingAS);
    }
}
