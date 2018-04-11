package database;

import com.manlyminotaurs.databases.DataModelI;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataModelITest {
    @Test
    public void testCreateConnection(){
        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCloseConnection(){
        Connection connection = DataModelI.getInstance().getNewConnection();
        assertTrue(DataModelI.getInstance().closeConnection());
        assertFalse(DataModelI.getInstance().closeConnection());
    }
}
