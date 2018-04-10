package database;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.databases.UserDBUtil;
import com.manlyminotaurs.users.User;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class UserDBUtilTest {
    @Before
    public void before() {
        DataModelI.getInstance().startDB();
    }

    @Test
    public void retrieveUsers_returnsCorrectList() {
        UserDBUtil userDBUtil = new UserDBUtil();
        User a_user = userDBUtil.userBuilder("3","Christian","","Cedrone","Spanish","interpreter");
        List<User> listOfUsers= userDBUtil.retrieveUsers();
        for(User given_user: listOfUsers) {
            if(given_user.getUserID().equals(a_user.getUserID())) {
                assertTrue(given_user.getUserID().equals(a_user.getUserID()));
                assertTrue(given_user.getFirstName().equals(a_user.getFirstName()));
                assertTrue(given_user.getLastName().equals(a_user.getLastName()));
                assertTrue(given_user.getLanguage().equals(a_user.getLanguage()));
                assertTrue(given_user.getUserType().equals(a_user.getUserType()));
            }
        }
    }

    @Test
    public void retrieveUsers_returnsCorrectList2() {
        UserDBUtil userDBUtil = new UserDBUtil();
        User a_user = userDBUtil.userBuilder("11","Ebenezer","","Ampiah","Greek","admin");
        List<User> listOfUsers= userDBUtil.retrieveUsers();
        for(User given_user: listOfUsers) {
            if(given_user.getUserID().equals(a_user.getUserID())) {
                assertTrue(given_user.getUserID().equals(a_user.getUserID()));
                assertTrue(given_user.getFirstName().equals(a_user.getFirstName()));
                assertTrue(given_user.getLastName().equals(a_user.getLastName()));
                assertTrue(given_user.getLanguage().equals(a_user.getLanguage()));
                assertTrue(given_user.getUserType().equals(a_user.getUserType()));
            }
        }
    }

    @Test
    public void addUser_addCorrectUser() {
        UserDBUtil userDBUtil = new UserDBUtil();
        List<User> listOfUsers= DataModelI.getInstance().retrieveUsers();
        User a_user = DataModelI.getInstance().addUser("Oreo","","Thins","Spanish","janitor","janitorUserName","janitorPassword");
/*
        for(int i =0;i < listOfUsers.size();i++) {
            if(given_user.getUserID().equals(a_user.getUserID())) {
                assertTrue(given_user.getUserID().equals(a_user.getUserID()));
                assertTrue(given_user.getFirstName().equals(a_user.getFirstName()));
                assertTrue(given_user.getLastName().equals(a_user.getLastName()));
                assertTrue(given_user.getLanguage().equals(a_user.getLanguage()));
                assertTrue(given_user.getUserType().equals(a_user.getUserType()));
            }
        }*/
    }

}
