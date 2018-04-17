import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.messaging.RequestFactory;
import com.manlyminotaurs.nodes.Node;
import org.junit.BeforeClass;
import org.junit.Test;


import java.time.LocalDateTime;

import static com.manlyminotaurs.messaging.RequestType.JanitorialRequest;
import static org.junit.Assert.assertFalse;

public class DatabaseServiceRequestTest {
    @BeforeClass
    public static void setup(){
        DataModelI.getInstance().startDB();
    }

    @Test
    public void genNewRequest(){
        RequestFactory rf = new RequestFactory();
        Node testNode = DataModelI.getInstance().getNodeByID("GHALL00201");

        assertFalse(rf.genNewRequest(JanitorialRequest, testNode, "test", "4", 1) == null);
    }

    @Test
    public void genExistingRequest(){
        RequestFactory rf = new RequestFactory();

        assertFalse(rf.genExistingRequest("1","Janitorial", 1, false, false, LocalDateTime.now(), LocalDateTime.now(), "GHALL00201", "1", "test") == null);
    }

}
