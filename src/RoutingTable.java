import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoutingTable {

    private HashMap<String, ArrayList<String[]>> routes; // <IP Address, List<ASx - ASy - ASz ... >>

    /**
     *
     */
    public RoutingTable () {
        this.routes = new HashMap<>();
    }

    /**
     *
     * @param newIPAddress
     * @param newPath
     */
    public void addRoute (String newIPAddress, String[] newPath) {
        ArrayList<String[]> possiblePaths;

        if (this.routes.containsKey(newIPAddress)) {
            //If the address is already registered, use the already created list
            possiblePaths = this.routes.get(newIPAddress);
            possiblePaths.add(newPath); //Add the new path

        } else {
            //If the address is not registered, make a new path list
            possiblePaths = new ArrayList<>();
            possiblePaths.add(newPath); //Add the new path
            this.routes.put(newIPAddress, possiblePaths); //Add the new IP Address to the list
        }

    }

    /**
     * Sorts all the possible paths and puts the shortest one first.
     */
    public void sort () {

        for (ArrayList<String[]> possiblePaths : this.routes.values()) {

            String[] temp;
            //Insertion Sort
            for (int indexOne = 1; indexOne < possiblePaths.size(); indexOne++) {

                for(int indexTwo = indexOne ; indexTwo > 0 ; indexTwo--){
                    if(possiblePaths.get(indexTwo).length < possiblePaths.get(indexTwo-1).length){
                        //Swap
                        temp = possiblePaths.get(indexTwo);
                        possiblePaths.set(indexTwo, possiblePaths.get(indexTwo-1));
                        possiblePaths.set(indexTwo - 1, temp);

                    }
                }

            }

        }

    }

    /**
     *
     * @param asID
     */
    public void deleteRoutesPropagatedByAS (String asID) {

        for (ArrayList<String[]> possiblePaths : this.routes.values()) {

            for (String[] path : possiblePaths) {

                if (path.length > 0) {



                } else {
                    System.err.println("An unvalid path was detected!!");
                    System.exit(1);
                }

            }

        }
    }

    /**
     *
     * @param receiverAS
     * @return
     */
    public String generateUpdateMessage (String receiverAS) {


        return "";
    }

    /**
     * Used to debug the table
     */
    public void print () {
        for (Map.Entry<String, ArrayList<String[]>> entry: this.routes.entrySet()) {
            System.out.println(entry.getKey());

            for (String[] path : entry.getValue()) {
                System.out.print("------------ ");
                for (int i = 0; i < path.length; i++) {
                    System.out.print(path[i] + " - ");
                }
                System.out.println("");
            }
            System.out.println("");

        }
    }
}
