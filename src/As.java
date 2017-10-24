import java.util.ArrayList;
import java.util.HashMap;

public class As {

    private int id;
    private int port;
    private ArrayList<String> knownSubnetworks;
    private HashMap<String, String> bgpNeighbors;
    private RoutingTable routingTable;

    public As(int id, int port, ArrayList<String> knownSubnetworks, HashMap<String, String> bgpNeighbors) {
        this.id = id;
        this.port = port;
        this.knownSubnetworks = knownSubnetworks;
        this.bgpNeighbors = bgpNeighbors;

        this.routingTable = new RoutingTable();
        this.routingTable.addRoute("192.168.1.2", new String[]{"AS1", "AS2", "AS3"});
        this.routingTable.addRoute("192.168.1.2", new String[]{"AS1", "AS2", "AS3", "AS4", "AS5"});
        this.routingTable.addRoute("192.168.1.2", new String[]{"AS1", "AS2", "AS3", "AS7"});
        this.routingTable.addRoute("192.168.1.2", new String[]{"AS1", "AS6", "AS5"});
        this.routingTable.sort();
        this.routingTable.print();
    }

    public void start() {
        System.out.println("Start");
    }

    public void stop() {
        System.out.println("Stop");
    }

    public void showRoutes() {
        System.out.println("Show Routes");
    }

    public void addSubNetwork(String address) {
        System.out.println("Add "+ address);
    }

    public void help() {
        System.out.println("Help");
    }
}
