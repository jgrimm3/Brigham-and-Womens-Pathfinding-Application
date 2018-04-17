import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.messaging.RequestFactory;
import com.manlyminotaurs.nodes.Node;
import org.junit.BeforeClass;
import org.junit.Test;

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

}
