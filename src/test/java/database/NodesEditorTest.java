package database;

import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.Conference;
import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Node;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class NodesEditorTest {

    NodesEditor editor = new NodesEditor();
    Node node = new Conference("Duncan Reid Conference Room", "Conf B0102", "BCONF00102", "CONF", 2150, 1025, "2", "45 Francis");
    Node addNode = new Conference("Andrew", "lmao", "whut", "BAMF", 9999, 9999, "9", "Was good");
//Edge addEdge = new Edge("Start", "End", "ID");

    @Test
    public void retrieveNodes_Data_Created() {
        editor.retrieveNodes();
        assertTrue(editor.getNodeList().get(0).getNodeType().equals("CONF"));
    }

    @Test
    public void retrieveNodes_Objects_Created() {
        editor.retrieveNodes();
        int i = 0;
        while(i < editor.getNodeList().size()) {
            assertTrue(editor.getNodeList().get(i) != null);
            i++;
        }
    }

    @Test
    public void printTest_CheckObjects_Created() {
        editor.retrieveNodes();
        editor.printNodeList();
    }

   // @Test
    public void retrieveNodes_Data_Size() {
        editor.retrieveNodes();
        assertTrue(editor.nodeList.size() > 0 && editor.nodeList.size() < 200);
    }

    //@Test
//    public void addAndRemoveNode_Data_Correct() {
//        editor.retrieveNodes();
//        editor.addNode(addNode);
//        assertTrue(editor.nodeList.contains(addNode));
//        editor.removeNode(addNode);
//        assertTrue(!editor.nodeList.contains(addNode));
//    }

    //@Test
//    public void addAndRemoveNodebyID_Data_Correct() {
//        editor.retrieveNodes();
//        editor.addNode(addNode);
//        assertTrue(editor.nodeList.contains(addNode));
//        editor.removeNode(addNode.getID());
//        assertTrue(!editor.nodeList.contains(addNode));
//    }

	@Test
	public void printEdges_CheckObjects_Created() {
		editor.retrieveEdges();
		editor.printEdgeList();
	}

	@Test
	public void addAndRemoveEdge_Data_Correct() {
		editor.retrieveEdges();
	//	editor.addEdge(addEdge);
	//	assertTrue(editor.edgeList.contains(addEdge));
//		editor.removeEdge(addEdge);
//		assertTrue(!editor.edgeList.contains(addEdge));
	}

    //@Test
    public void modifyNode_Data_Building(){
        editor.retrieveNodes();
        editor.modifyNodeBuilding(editor.getNodeList().get(1),"Hello Building");
        assertEquals("Hello Building",editor.getNodeList().get(1).getBuilding());
    }

    //@Test
    public void modifyNode_Data_Floor(){
        editor.retrieveNodes();
        editor.modifyNodeFloor(editor.getNodeList().get(3),"L5");
        assertEquals("L5",editor.getNodeList().get(3).getFloor());
    }

    //@Test
    public void modifyNode_Data_xCoord(){
        editor.retrieveNodes();
        editor.modifyNodeXcoord(editor.getNodeList().get(10),12356);
        assertEquals(12356,editor.getNodeList().get(10).getXCoord());
    }

    //@Test
    public void modifyNode_Data_yCoord(){
        editor.retrieveNodes();
        editor.modifyNodeYcoord(editor.getNodeList().get(15),45353);
        assertEquals(45353,editor.getNodeList().get(15).getYCoord());
    }

    //@Test
    public void modifyNode_Data_NodeID(){
        editor.retrieveNodes();
        //editor.modifyNodeID(editor.getNodeList().get(20),"TestID");
        assertEquals("TestID",editor.getNodeList().get(20).getID());
    }

    //@Test
    public void modifyNode_Data_LongName(){
        editor.retrieveNodes();
        editor.modifyNodeLongName(editor.getNodeList().get(6),"Test Long lNgBuilding");
        assertEquals("Test Long lNgBuilding",editor.getNodeList().get(6).getLongName());
    }

    //@Test
    public void modifyNode_Data_ShortName(){
        editor.retrieveNodes();
        editor.modifyNodeShortName(editor.getNodeList().get(12),"Short short test");
        assertEquals("Short short test",editor.getNodeList().get(12).getShortName());
    }

    //@Test
    public void modifyNode_Data_NodeType(){
        editor.retrieveNodes();
        editor.modifyNodeType(editor.getNodeList().get(18),"YOUE");
        assertEquals("YOUE",editor.getNodeList().get(18).getNodeType());
    }

}
