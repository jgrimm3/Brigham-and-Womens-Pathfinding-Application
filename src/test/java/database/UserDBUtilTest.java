package database;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.databases.UserDBUtil;
import com.manlyminotaurs.users.User;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserDBUtilTest {
    @Before
    public void before() {
        DataModelI.getInstance().startDB();
    }


    @Test
    public void retrieveUsersCorrectSize(){
        int list_size = DataModelI.getInstance().retrieveUsers().size();
        assertTrue(list_size == 11);
    }

    @Test
    public void afterRestoreUser_returnsCorrectList(){
        List<String> languages = new ArrayList<>();
        languages.add("Korean");
        languages.add("English");
        languages.add("Latin");
        User aUser = DataModelI.getInstance().addUser("", "Edward", "Bong", "Jang",languages, "visitor", "qw933", "tempPassword");
        assertTrue(DataModelI.getInstance().retrieveUsers().contains(aUser));

        List<User> listOfUsers = DataModelI.getInstance().retrieveUsers();
        DataModelI.getInstance().removeUser(aUser);
        List<User> listOfUsers2 = DataModelI.getInstance().retrieveUsers();
        assertFalse(DataModelI.getInstance().retrieveUsers().contains(aUser));

        System.out.println(DataModelI.getInstance().restoreUser(aUser.getUserID()));
        List<User> listOfUsers3 = DataModelI.getInstance().retrieveUsers();
        assertTrue(listOfUsers3.contains(aUser));
    }
//
//    @Test
//    public void retrieveUsersReturnsCorrectList() {
//        UserDBUtil userDBUtil = new UserDBUtil();
//
//
//        User a_user = userDBUtil.userBuilder("3","Christian","","Cedrone","Spanish","interpreter");
//        List<User> listOfUsers= userDBUtil.retrieveUsers();
//        assertTrue(listOfUsers.contains(a_user));
//    }
//    //test size
//    //test someone exists
//    //
//
//    @Test
//    public void retrieveUsers_returnsCorrectList2() {
//        UserDBUtil userDBUtil = new UserDBUtil();
//        User a_user = userDBUtil.userBuilder("11","Ebenezer","","Ampiah","Greek","admin");
//        List<User> listOfUsers= userDBUtil.retrieveUsers();
//        assertTrue(listOfUsers.contains(a_user));
//        DataModelI.getInstance().removeUser(a_user);
//    }
//
//    @Test
//    public void addUser_addCorrectUser() {
//        UserDBUtil userDBUtil = new UserDBUtil();
//        User a_user = DataModelI.getInstance().addUser("Oreo","","Thins","Spanish","janitor","janitorUserName","janitorPassword");
//        List<User> listOfUsers= DataModelI.getInstance().retrieveUsers();
//        System.out.println(listOfUsers.contains(a_user));
//        assertTrue(listOfUsers.contains(a_user));
//        DataModelI.getInstance().removeUser(a_user);
//    }
//
//
//    @Test
//    public void addUser_removeCorrectUser() {
//        UserDBUtil userDBUtil = new UserDBUtil();
//        User a_user = DataModelI.getInstance().addUser("Wafers","Price","Vinilla","English","Doctor","doctorUserName","doctorPassword");
//        User a_user2 = DataModelI.getInstance().addUser("Oreo","","Thins","Spanish","janitor","janitorUserName","janitorPassword");
//        List<User> listOfUsers= DataModelI.getInstance().retrieveUsers();
//        assertTrue(listOfUsers.contains(a_user));
//        assertTrue(listOfUsers.contains(a_user2));
//
//        DataModelI.getInstance().removeUser(a_user);
//        listOfUsers = DataModelI.getInstance().retrieveUsers();
//        assertFalse(listOfUsers.contains(a_user));
//        assertTrue(listOfUsers.contains(a_user2));
//
//        DataModelI.getInstance().removeUser(a_user2);
//        listOfUsers = DataModelI.getInstance().retrieveUsers();
//        assertFalse(listOfUsers.contains(a_user));
//        assertFalse(listOfUsers.contains(a_user2));
//    }

}
