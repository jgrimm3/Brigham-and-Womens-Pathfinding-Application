package database;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.messaging.RequestFactory;
import com.manlyminotaurs.messaging.RequestType;
import com.manlyminotaurs.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class RequestDBUtilTest {
    @Before
    public void before() {
        DataModelI.getInstance().startDB();
    }

    //retrieve request
    @Test
    public void retrieveRequests_givenCSV_returnCorrectSize(){
        assertTrue(DataModelI.getInstance().retrieveRequests().size() == 2);
    }

    //test add Request
    @Test
    public void addRequest_GivenARequest_ReturnsCorrectList(){
        DataModelI dataModelI = DataModelI.getInstance();
        RequestFactory rFactory = new RequestFactory();
        Node a_node = DataModelI.getInstance().addNode(100,200,"3","building","type","longname","shortname",2,100,2000);
        Request a_request = rFactory.genNewRequest(RequestType.MedicalRequest,a_node,"need a cup of water","3", 2);
        List<Request> listOfRequests = DataModelI.getInstance().retrieveRequests();
        assertTrue(DataModelI.getInstance().retrieveRequests().contains(a_request));
    }

    //remove request
    //modify request
    //retrieve request
    //search requestBySender
    //search requestByReceiver
}
