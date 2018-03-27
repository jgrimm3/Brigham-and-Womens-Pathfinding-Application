import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.Conference;
import com.manlyminotaurs.nodes.Department;
import com.manlyminotaurs.nodes.Node;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NodesEditorTest {

    NodesEditor editor = new NodesEditor();
    Node node = new Conference("Duncan Reid Conference Room", "Conf B0102", "BCONF00102", "CONF", 2150, 1025, "2", "45 Francis");
    Node addNode = new Conference("Andrew", "lmao", "whut", "BAMF", 9999, 9999, "9", "Was good");

    @Test
    public void RetrieveData_Data_Created() {
        editor.retrieveData();
        assertTrue(editor.nodeList.get(0).getNodeType().equals("CONF"));
    }

    @Test
    public void RetrieveData_Objects_Created() {
        editor.retrieveData();
        int i = 0;
        while(i < editor.nodeList.size()) {
            System.out.println("Object " + i + ": " + editor.nodeList.get(i).getLongName());
            assertTrue(editor.nodeList.get(i) != null);
            i++;
        }
    }

    @Test
    public void RetrieveData_Data_Size() {
        editor.retrieveData();
        assertTrue(editor.nodeList.size() > 0);
    }

    @Test
    public void addAndRemoveNode_Data_Correct() {
        editor.retrieveData();
        editor.addNode(addNode);
        assertTrue(editor.nodeList.contains(addNode));
        editor.removeNode(addNode);
        assertTrue(!editor.nodeList.contains(addNode));
    }

    @Test
    public void modifyNode_Data_Building(){
        editor.retrieveData();
        editor.modifyNodeBuilding(editor.nodeList.get(1),"Hello Building");
        assertEquals("Hello Building",editor.nodeList.get(1).getBuilding());
    }

    @Test
    public void modifyNode_Data_Floor(){
        editor.retrieveData();
        editor.modifyNodeFloor(editor.nodeList.get(3),"L5");
        assertEquals("L5",editor.nodeList.get(3).getFloor());
    }

    @Test
    public void modifyNode_Data_xCoord(){
        editor.retrieveData();
        editor.modifyNodeXcoord(editor.nodeList.get(10),12356);
        assertEquals(12356,editor.nodeList.get(10).getXCoord());
    }

    @Test
    public void modifyNode_Data_yCoord(){
        editor.retrieveData();
        editor.modifyNodeYcoord(editor.nodeList.get(15),45353);
        assertEquals(45353,editor.nodeList.get(15).getYCoord());
    }

    //@Test
    public void modifyNode_Data_NodeID(){
        editor.retrieveData();
        //editor.modifyNodeID(editor.nodeList.get(20),"TestID");
        assertEquals("TestID",editor.nodeList.get(20).getID());
    }

    @Test
    public void modifyNode_Data_LongName(){
        editor.retrieveData();
        editor.modifyNodeLongName(editor.nodeList.get(6),"Test Long lNgBuilding");
        assertEquals("Test Long lNgBuilding",editor.nodeList.get(6).getLongName());
    }

    @Test
    public void modifyNode_Data_ShortName(){
        editor.retrieveData();
        editor.modifyNodeShortName(editor.nodeList.get(12),"Short short test");
        assertEquals("Short short test",editor.nodeList.get(12).getShortName());
    }

    @Test
    public void modifyNode_Data_NodeType(){
        editor.retrieveData();
        editor.modifyNodeType(editor.nodeList.get(18),"YOUE");
        assertEquals("YOUE",editor.nodeList.get(18).getNodeType());
    }



}
