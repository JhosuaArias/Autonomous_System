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
        int index = -1;

        while(input.hasNext()) {
            String nextLine = input.nextLine();
            if(nextLine.contains("#")) {
                index++;
            } else {
                switch (index) {
                    case 0: //id
                        asId = Integer.parseInt(nextLine);
                        System.out.println(nextLine);
                        break;
                    case 1: //port
                        asPort = Integer.parseInt(nextLine);
                        System.out.println(nextLine);
                        break;
                    case 2: //ip
                        asIp = nextLine;
                        System.out.println(nextLine);
                        break;
                    case 3: //known networks
                        knownSubnetworks.add(nextLine);
                        System.out.println(nextLine);
                        break;
                    case 4: //BGP neighbors
                        bgpNetworks.add(nextLine);
                        System.out.println(nextLine);
                        break;
                    case 5: //listen neigbors
                        listenNeighbors = Integer.parseInt(nextLine);
                        System.out.println(nextLine);
                        break;
                }
            }
        }

        //createdAs = new As(asId,asPort,asIp,knownSubnetworks,bgpNetworks,listenNeighbors);

        input.close();

        return createdAs;
    }

    public static void main(String[] args) {
        FileParser fileParser = new FileParser();
        try {
            fileParser.createAS("hola.txt");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
