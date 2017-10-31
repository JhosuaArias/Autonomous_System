
import java.util.*;


public class As {

    private volatile int id;
    private volatile int port;
    private volatile ArrayList<String> knownSubnetworks;
    private volatile HashMap<String, Integer> bgpNeighbors;
    private volatile RoutingTable routingTable;


    private volatile Server listenerServer;
    private volatile ArrayList<Client> clients;

    public As(int id, int port, ArrayList<String> knownSubnetworks, HashMap<String, Integer> bgpNeighbors) {
        this.id = id;
        this.port = port;
        this.knownSubnetworks = knownSubnetworks;
        this.bgpNeighbors = bgpNeighbors;
        this.routingTable = new RoutingTable();
        this.clients = new ArrayList<>();


    }

    public void start() {

        this.listenerServer = new Server(this, this.port);
        this.listenerServer.start();

        for (Map.Entry<String, Integer> entry : this.bgpNeighbors.entrySet()) {
            Client client = new Client(this, entry.getKey(), entry.getValue());
            this.clients.add(client);
            client.start();
        }

    }

    public void stop() {

        if (this.listenerServer != null) {
            this.listenerServer.kill();
        }

        for (Client client : this.clients) {
            client.kill();
        }

    }

    public void showRoutes() {
        System.out.println("Local Subnetworks:");
        for (String localSubnet: this.knownSubnetworks) {
            System.out.println("* " + localSubnet);
        }
        System.out.println("Known routes:");
        System.out.println(this.routingTable.print());
    }

    public synchronized void parseUpdateMessage(String message) {

        if (message != null && message.indexOf('*') + 1 < message.length()) {

            String senderAS = message.substring(0, message.indexOf('*'));
            this.routingTable.deleteRoutesPropagatedByAS(senderAS);
            message = message.substring(message.indexOf('*') + 1);

            String[] tokenizedMessage = message.split(",");
            for (String token : tokenizedMessage) {
                String[] address_path_Array = token.split(":");
                if (!this.isSubnetworkLocal(address_path_Array[0])) {

                    String[] tokenizedPath = address_path_Array[1].split("-");
                    ArrayList<String> newPath = new ArrayList<>(Arrays.asList(tokenizedPath));
                    this.routingTable.addRoute(address_path_Array[0], newPath);

                }
            }

            this.routingTable.sort();

        }

    }

    private boolean isSubnetworkLocal (String subnet) {
        return this.knownSubnetworks.contains(subnet);
    }

    public synchronized String getUpdateMessage (String receivingAS) {
        String message = "AS" + this.id  + "*" + this.generateLocalSubnetworksMessage()
                + this.routingTable.generateUpdateMessage(receivingAS, "AS" + this.id);

        if (message.length() > 0 && message.charAt(message.length()-1) == ',') {
            message = message.substring(0, message.length() - 1);
        }

        return message;
    }

    private String generateLocalSubnetworksMessage() {
        String message = "";

        for (String localSubnet: this.knownSubnetworks) {
            message += localSubnet + ":" + "AS" + this.id + ",";
        }

        return message;
    }

    public synchronized void addSubNetwork(String address) {
        this.knownSubnetworks.add(address);
    }

    public int getId() {
        return id;
    }

}
