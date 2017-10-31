import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RoutingTable {


    private volatile HashMap<String, ArrayList< ArrayList<String> > > routes; // <IP Address, List<ASx - ASy - ASz ... >>


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
    public void addRoute (String newIPAddress, ArrayList<String> newPath) {
        ArrayList<ArrayList<String>> possiblePaths;

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

        for (ArrayList<ArrayList<String>> possiblePaths : this.routes.values()) {

            ArrayList<String> temp;
            //Insertion Sort
            for (int indexOne = 1; indexOne < possiblePaths.size(); indexOne++) {

                for(int indexTwo = indexOne ; indexTwo > 0 ; indexTwo--){
                    if(possiblePaths.get(indexTwo).size() < possiblePaths.get(indexTwo-1).size()){
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

        for (Iterator< Map.Entry<String,ArrayList<ArrayList<String>>> > iterator = this.routes.entrySet().iterator(); iterator.hasNext(); ) {

            Map.Entry<String,ArrayList<ArrayList<String>>> mapEntry = iterator.next();

            ArrayList<ArrayList<String>> possiblePaths = mapEntry.getValue();
            //Make a copy of the paths, so we just add the ones we want
            ArrayList<ArrayList<String>> possiblePathsCopy = (ArrayList<ArrayList<String>>) possiblePaths.clone();
            possiblePaths.clear();

            for (ArrayList<String> path : possiblePathsCopy) {

                //If the last AS in the path is not the AS we are looking for, put it back in the list
                if (path.size() > 0 && !path.get(path.size()-1).equals(asID)) {
                    possiblePaths.add(path);
                }

            }

            //If the address has no paths, delete it.
            if (possiblePaths.size() == 0) {
                iterator.remove();
            }
        }
    }

    /**
     *
     * @param receiverAS
     * @return
     */
    public String generateUpdateMessage (String receiverAS, String currAS) {
        String message = "";

        for (Map.Entry<String, ArrayList<ArrayList<String>>> entry : this.routes.entrySet()) {
            ArrayList<String> path = entry.getValue().get(0);

            if (!path.get(path.size()- 1).equals(receiverAS)) {
                message += entry.getKey() + ":" + currAS + "-";


                for (String as : path) {
                    message += as + "-";
                }
                if(message.charAt(message.length()-1) == '-') {

                    message = message.substring(0, message.length() - 1);
                }

                message += ",";
            }

        }
        return message;
    }

    /**
     * Used to debug the table
     */
    public String print () {
        String result = "";
        int counter;

        for (Map.Entry<String, ArrayList<ArrayList<String>>> entry : this.routes.entrySet()) {
            result += entry.getKey() + ":\n";
            counter = 0;

            for (ArrayList<String> path : entry.getValue()) {
                result += "------------ ";
                for (String as : path) {
                    result += as + " - ";
                }

                if (path.size() > 0) {
                    result = result.substring(0, result.length() - 2);
                }
                if (counter == 0) {
                    result += " *";
                }

                counter++;
                result += "\n";
            }
            result += "\n";
        }

        return result;
    }
}
