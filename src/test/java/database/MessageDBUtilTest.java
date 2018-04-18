package database;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.messaging.Message;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessageDBUtilTest {
    @Before
    public void before() {
        DataModelI.getInstance().startDB();
    }

    @Test
    public void afterRestoreMessage_returnsCorrectList(){
        Message aMessage = new Message("","Testing adding message", true, DataModelI.getInstance().convertStringToDate("2018-04-17").toLocalDate(),"4","5");
        assertFalse(DataModelI.getInstance().retrieveMessages().contains(aMessage));

        Message addedMessage = DataModelI.getInstance().addMessage(aMessage);
        assertTrue(aMessage.equals(addedMessage));

        assertTrue(DataModelI.getInstance().retrieveMessages().contains(aMessage));
        assertTrue(DataModelI.getInstance().retrieveMessages().contains(addedMessage));


        List<Message> listOfMessages = DataModelI.getInstance().retrieveMessages();
        DataModelI.getInstance().removeMessage(addedMessage.getMessageID());
        List<Message> listOfMessages2 = DataModelI.getInstance().retrieveMessages();
        assertFalse(DataModelI.getInstance().retrieveRequests().contains(addedMessage));
        assertFalse(DataModelI.getInstance().retrieveRequests().contains(aMessage));

        DataModelI.getInstance().restoreMessage(aMessage.getMessageID());
        assertTrue(DataModelI.getInstance().retrieveRequests().contains(aMessage));
        assertTrue(DataModelI.getInstance().retrieveRequests().contains(addedMessage));
    }
}
