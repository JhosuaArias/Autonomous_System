
import java.util.ArrayList;
import java.util.Arrays;


public class TestRoute {

    public static void main(String[] args) {
        As testAS = new As(1, 5555, new ArrayList<>(Arrays.asList("10.20.1.3", "125.3.4.6")), null);
        testAS.parseUpdateMessage("AS1*10.20.1.3:AS1,125.3.4.6:AS1,10.168.1.2:AS2-AS3-AS6,192.168.1.2:AS2-AS5-AS3");
        testAS.parseUpdateMessage("AS1*10.168.1.2:AS4-AS8-AS2-AS3-AS6,192.168.1.2:AS2-AS3");
        System.out.println(testAS.getUpdateMessage("AS5"));
        testAS.showRoutes();
    }

}
