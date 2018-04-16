package database;

import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;


public class NodesDBUtilTest {

	final int THREED_STARTX = 950;
	final int THREED_ENDX = 1600;
	final int THREED_STARTY = 1500;
	final int THREED_ENDY = 2100;

	List<Node> listOfNodes = new ArrayList<>();

	@Before
	public void before() {
		DataModelI.getInstance().startDB();
		KioskInfo.currentUserID = "2";
	}

	@Test
	public void getNodeByID_correctlyReturnsNode(){
		Node a_node = DataModelI.getInstance().getNodeByID("IHALL00103");
		Node a_node2 = DataModelI.getInstance().getNodeByID("DSTAI00802");
		List<Node> listNodes =  DataModelI.getInstance().getNodeList();
		Map<String, Node> nodeMap = DataModelI.getInstance().getNodeMap();
		assertTrue(nodeMap.containsValue(a_node));
		assertTrue(nodeMap.containsValue(a_node2));
	}

//	@Test
//	public void compareGetNodeMap_time() {
//		long startTime = System.nanoTime();
//		DataModelI.getInstance().retrieveNodes();
//		long endTime = System.nanoTime();
//		long timeTaken = (endTime - startTime) / 1000000;
//		System.out.println("RetrieveNode takes " + timeTaken + " ms to retrieve nodes");
//
//		startTime = System.nanoTime();
//		DataModelI.getInstance().getNodeMap();
//		endTime = System.nanoTime();
//		timeTaken = (endTime - startTime) / 1000000;
//		System.out.println("getNodeMap takes " + timeTaken + " ms to retrieve nodes");
//	}

	/*
	@Test
	public void printNodes() {
		Map<String, Node> nodeMap = DataModelI.getInstance().getNodeMap();
		for(Node a_node: nodeMap.values()){
			System.out.println(a_node.getLongName());
		}
	}
*/
	@Test
	public void getNodeMap_returnsCorrectList_Floor() {

		List<String> listOfFloors = new ArrayList<>();
		listOfFloors.add("1");
		listOfFloors.add("2");
		listOfFloors.add("3");
		listOfFloors.add("L1");
		listOfFloors.add("L2");

		Map<String, Node> nodeMap = DataModelI.getInstance().getNodeMap();
		for(Node a_node: nodeMap.values()){
			assertTrue(listOfFloors.contains(a_node.getFloor()));
		}
	}

	@Test
	public void getNodeMap_returnsCorrectList_Building() {

		List<String> listOfBuildings = new ArrayList<>();
		listOfBuildings.add("Shapiro");
		listOfBuildings.add("45 Francis");
		listOfBuildings.add("Tower");
		listOfBuildings.add("15 Francis");
		listOfBuildings.add("BTM");

		Map<String, Node> nodeMap = DataModelI.getInstance().getNodeMap();
		for(Node a_node: nodeMap.values()){
			assertTrue(listOfBuildings.contains(a_node.getBuilding()));
		}
	}

	@Test
	public void addNode_CorrectlyAddsNode() {
		Map<String, Node> oldNodeMap = DataModelI.getInstance().getNodeMap();
		Node addedNode = DataModelI.getInstance().addNode(5, 5, "2", "Shapiro", "CONF", "Longname~", "shortname~", 3, 5, 2);
		Node addedNode2 = DataModelI.getInstance().addNode(5, 5, "1", "Shapiro", "CONF", "Longname~", "shortname~", 3, 5, 2);

		Map<String, Node> newNodeMap = DataModelI.getInstance().getNodeMap();
		boolean oldNodeMapBool = oldNodeMap.containsValue(addedNode);
		boolean newNodeMapBool = newNodeMap.containsValue(addedNode);
		Node getNodeByIDNode = DataModelI.getInstance().getNodeByID(addedNode.getNodeID());
		boolean getNodeByIDBool = newNodeMap.containsKey(getNodeByIDNode.getNodeID());

		assertTrue(oldNodeMapBool);
        assertTrue(newNodeMapBool);
        assertTrue(getNodeByIDBool);
	}


//	@Test
//	public void getByBuildingTypeFloor_ReturnsCorrectNodeList() {
//		List<Node> testList = DataModelI.getInstance().getNodesByBuildingTypeFloor("Shapiro", "CONF", "1");
//		assertTrue(testList.get(0).getBuilding().equals("Shapiro"));
//		assertTrue(testList.get(0).getNodeType().equals("CONF"));
//		assertTrue(testList.get(0).getFloor().equals("1"));
//	}

	@Test
	public void getByAdjacentNodes_ReturnsCorrectList() {
		Node node = DataModelI.getInstance().getNodeByID("GCONF02001");
		List<String> testList = DataModelI.getInstance().getAdjacentNodes(node);
		for(String n : testList) {
			System.out.println(n);
			assertTrue(!n.isEmpty());
		}
	}

	@Test
	public void getByAdjacentNodes_ReturnsCorrectList2() {
		Node node = DataModelI.getInstance().getNodeByID("GEXIT001L1");
		List<String> testList = DataModelI.getInstance().getAdjacentNodes(node);
		for(String n : testList) {
			System.out.println(n);
			assertTrue(!n.isEmpty());
		}
	}

	@Test
	public void getByAdjacentNodes_ReturnsCorrectList3() {
		Node node = DataModelI.getInstance().getNodeByID("WHALL002L2");
		Map<String, Node> nodeMap = DataModelI.getInstance().getNodeMap();
		/* //check why!!!
		List<String> testList = DataModelI.getInstance().getAdjacentNodes(node);
		for(String n : testList) {
			System.out.println(n);
			assertTrue(!n.isEmpty());
		}*/


		List<Node> testList = node.getAdjacentNodes();
		for(Node n : testList) {
			System.out.println(n.getNodeID());
			assertTrue(!n.getNodeID().isEmpty());
		}
	}

	@Test
	public void testAdjacentNodesAfterMakeEdge() {
		Node a_node = DataModelI.getInstance().getNodeByID("WHALL002L2");
		Node new_node = DataModelI.getInstance().addNode(360, 1200, "L1", "15 Francis", "DEPT", "ex longname", "ex shortname", 1, 500, 1000);
		DataModelI.getInstance().addEdge(a_node, new_node);
		assertTrue(a_node.getAdjacentNodes().contains(new_node));
	}

	@Test
	public void testAdjacentNodesWhenAddedNodeStatusNotOne() {
		Node a_node = DataModelI.getInstance().getNodeByID("WHALL002L2");
		Node new_node = DataModelI.getInstance().addNode(360, 1200, "L1", "15 Francis", "DEPT", "ex longname", "ex shortname", 2, 500, 1000);
		DataModelI.getInstance().addEdge(a_node, new_node);
		assertTrue(a_node.getAdjacentNodes().contains(new_node));
	}


	@Test
	public void testGetLongNamesAutoComplete_returnsCorrectLongNames(){
		List<String> listOfNames = DataModelI.getInstance().getLongNames();
		for(String longName : listOfNames){
			System.out.println(longName);
		}
		assertTrue(listOfNames.contains("Hallway Node 25 Floor 1"));
	}
/* // now we return everything regardless of status
    @Test
    public void testRetrieveNodesWhenAddedNodeStatusNotOne() {
        Node a_node = DataModelI.getInstance().getNodeByID("WHALL002L2");
        Node new_node = DataModelI.getInstance().addNode(360, 1200, "L1", "15 Francis", "DEPT", "ex longname", "ex shortname", 2, 500, 1000);
        Node retrieved_node = DataModelI.getInstance().getNodeByID(new_node.getNodeID());
        assertTrue(retrieved_node == null);
    }*/

	@Test
	public void testRetrieveNodesWhenAddedNodeStatusISOne() {
		Node a_node = DataModelI.getInstance().getNodeByID("WHALL002L2");
		Node new_node = DataModelI.getInstance().addNode(360, 1200, "L1", "15 Francis", "DEPT", "ex longname", "ex shortname", 1, 500, 1000);
		Node retrieved_node = DataModelI.getInstance().getNodeByID(new_node.getNodeID());
		assertTrue(retrieved_node != null);
	}

	@Test
	public void getLongNameByBuildingTypeFloor_returnCorrectList(){
		List<String> longNameList = DataModelI.getInstance().getLongNameByBuildingTypeFloor("Shapiro","HALL","L2");
		System.out.println("hello");
	}

/*
	@Test
	public void modifyNode_editsNode() {
		Node addedNode = DataModelI.getInstance().addNode("lmao", 5, 3, "4", "Hello", "lmao", "yoo", "yo", 48, 20);
		DataModelI.getInstance().modifyNode(addedNode.getNodeID(), 5, 3, "4", "yolo", "lmao", "yoo", "yo", 48, 20);
		List<Node> nodeList = DataModelI.getInstance().getNodesByBuilding("yolo");
		System.out.println(DataModelI.getInstance().getNodeByID(addedNode.getNodeID()).getNodeID());
		System.out.println(DataModelI.getInstance().getNodeByID(addedNode.getNodeID()).getBuilding()); // Why does this return shapiro?
		assertTrue(nodeList.get(0).getBuilding().equals("yolo"));
		//DataModelI.getInstance().removeNode(addedNode.getID());
	}*/

	@Test
	public void getEdgeList_returnsCorrectList(){
		List<Edge> edgeList = DataModelI.getInstance().getEdgeList();
		Edge edge = new Edge("GHALL02201","GSERV02301","GHALL02201_GSERV02301");
		assertTrue(edgeList.contains(edge));
//		for(Edge a_edge: edgeList){
//			if(a_edge.getEdgeID().equals(edge.getEdgeID())){
//				System.out.println("found edge");
//			}
//		}
	}

	@Test
	public void getNodeByLongName_returnsCorrectNode(){
		Node a_node = DataModelI.getInstance().getNodeByLongName("Restroom D elevator Floor 2");
		Node a_node2 = DataModelI.getInstance().getNodeByLongName("Hallway Node 8 Floor L2");
		assertTrue(DataModelI.getInstance().getNodeMap().containsValue(a_node));
		assertTrue(DataModelI.getInstance().getNodeMap().containsValue(a_node2));
		System.out.print("hello");
	}

	@Test
	public void getNamesByBuildingFloorType_returnsCorrectList(){
		List<String> nameList = DataModelI.getInstance().getNamesByBuildingFloorType("Shapiro","L1","");
		assertTrue(nameList.contains("L1 Stairs"));

		List<String> nameList2 = DataModelI.getInstance().getNamesByBuildingFloorType(null,null,"BATH");
		assertFalse(nameList2.contains("L1 Stairs"));
	}

}



