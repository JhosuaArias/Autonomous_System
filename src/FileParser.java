import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class FileParser {
    public As createAS(String fileName) throws Exception {
        As createdAs = null;

        int asId;
        int asPort;
        String asIp;
        ArrayList<String> knownSubnetworks = new ArrayList<>();
        ArrayList<String> bgpNetworks = new ArrayList<>();
        int listenNeighbors;

        File file = new File(fileName);

        Scanner input = new Scanner(file);

        while(input.hasNext()) {
            int index = -1;
            String nextLine = input.nextLine();
            if(nextLine.contains("#")) {
                index++;
            } else {
                switch (index) {
                    case 0: //id
                        asId = Integer.parseInt(nextLine);
                        break;
                    case 1: //port
                        asPort = Integer.parseInt(nextLine);
                        break;
                    case 2: //ip
                        asIp = nextLine;
                        break;
                    case 3: //known networks
                        knownSubnetworks.add(nextLine);
                        break;
                    case 4: //BGP neighbors
                        bgpNetworks.add(nextLine);
                        break;
                    case 5: //listen neigbors
                        listenNeighbors = Integer.parseInt(nextLine);
                        break;
                }
            }
        }

        //createdAs = new As(asId,asPort,asIp,knownSubnetworks,bgpNetworks,listenNeighbors);

        input.close();

        return createdAs;
    }
}
