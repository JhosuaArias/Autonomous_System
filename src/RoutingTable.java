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

        for (Map.Entry<String,ArrayList<String[]>> mapEntry : this.routes.entrySet()) {
            ArrayList<String[]> possiblePaths = mapEntry.getValue();
            //Make a copy of the paths, so we just add the ones we want
            ArrayList<String[]> possiblePathsCopy = (ArrayList<String[]>) possiblePaths.clone();
            possiblePaths.clear();

            for (String[] path : possiblePathsCopy) {

                //If the last AS in the path is not the AS we are looking for, put it back in the list
                if (path.length > 0 && !path[path.length-1].equals(asID)) {
                    possiblePaths.add(path);
                }

            }

            //If the address has no paths, delete it.
            if (possiblePaths.size() == 0) {
                this.routes.remove(mapEntry.getKey());
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

        for (Map.Entry<String, ArrayList<String[]>> entry : this.routes.entrySet()) {
            String[] path = entry.getValue().get(0);

            if (!path[path.length - 1].equals(receiverAS)) {
                message += entry.getKey() + ":";


                for (int i = 0; i < path.length; i++) {
                    message += path[i] + "-";
                }
                message += currAS;


                message += ",";
            }
        }
        if (message.length() > 0) {
            message = message.substring(0, message.length() - 1);
        }

        return message;
    }

    /**
     * Used to debug the table
     */
    public String print () {
        String result = "";
        int counter;

        for (Map.Entry<String, ArrayList<String[]>> entry : this.routes.entrySet()) {
            result += entry.getKey() + ":\n";
            counter = 0;

            for (String[] path : entry.getValue()) {
                result += "------------ ";
                for (int i = 0; i < path.length; i++) {
                    result += path[i] + " - ";
                }

                if (path.length > 0) {
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
