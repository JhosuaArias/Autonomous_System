import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileParser {
    private final String ADDRESS_FORMAT = "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";

    public As createAS(String fileName) throws Exception {
        As createdAs = null;

        int asId = 0;
        int asPort = 0;
        ArrayList<String> knownSubnetworks = new ArrayList<>();
        HashMap<String, Integer> bgpNeighbors = new HashMap<>();

        File file = new File(fileName);

        Scanner input = new Scanner(file);
        int index = -1;

        while(input.hasNext()) {
            String nextLine = input.nextLine();

            nextLine = nextLine.trim();
            nextLine = nextLine.replace(" ","");
            if (!nextLine.isEmpty()) {
                if(nextLine.contains("#")) {
                    index++;
                } else {
                    switch (index) {
                        case 0: //id
                            try {
                                asId = Integer.parseInt(nextLine);
                            } catch (NumberFormatException nfe) {
                                System.err.println("Supplied ID in file is not numeric");
                            }
                            break;
                        case 1: //known networks
                            if (nextLine.matches(ADDRESS_FORMAT)) {
                                knownSubnetworks.add(nextLine);
                            } else {
                                System.err.println("IP address for known subnetwork in file does not have the correct format");
                            }

                            break;
                        case 2: //BGP neighbors
                            Object[] ipAndPort = parseNeighbor(nextLine);
                            bgpNeighbors.put( (String) ipAndPort[0], (int) ipAndPort[1]);
                            System.out.println(bgpNeighbors.size());
                            break;
                        case 3: //listen neigbors
                            try {
                                asPort = Integer.parseInt(nextLine);
                            } catch (NumberFormatException nfe) {
                                System.err.println("Supplied port for AS in file is not numeric");
                            }
                            break;
                    }
                }
            }
        }

        createdAs = new As(asId, asPort, knownSubnetworks, bgpNeighbors);

        input.close();

        return createdAs;
    }

    Object[] parseNeighbor(String bgpNeighbor) {
        String ip = bgpNeighbor.split(":")[0];
        String port = bgpNeighbor.split(":")[1];
        Object[] ipAndPort = new Object[2];
        if (ip.matches(ADDRESS_FORMAT)) {
            ipAndPort[0] = ip;
        } else {
            System.err.println("IP address in file does not have the correct format");
        }

        try {
            ipAndPort[1] = Integer.parseInt(port);
        } catch (NumberFormatException nfe) {
            System.err.println("Specified port in file is not numeric");
        }
        return ipAndPort;
    }
}

