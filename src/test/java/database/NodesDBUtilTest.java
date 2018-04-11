package database;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;


public class NodesDBUtilTest {

	final int THREED_STARTX = 950;
	final int THREED_ENDX = 1600;
	final int THREED_STARTY = 1500;
	final int THREED_ENDY = 2100;

	List<Node> listOfNodes = new ArrayList<>();

	@Before
	public void before() {
		DataModelI.getInstance().startDB();
	}
/*
	// Tests
	@Test
	public void retrieveNodes_returnsCorrectList_xCoord3DandyCoord3D() {
		listOfNodes = DataModelI.getInstance().retrieveNodes();

		int i = 0;
		while (i < listOfNodes.size()) {
			assertTrue(listOfNodes.get(i).getXCoord3D() < THREED_ENDX && listOfNodes.get(i).getXCoord3D() > THREED_STARTX);
			assertTrue(listOfNodes.get(i).getYCoord3D() < THREED_ENDY && listOfNodes.get(i).getYCoord3D() > THREED_STARTY);
			i++;

		}
	}*/

	@Test
	public void retrieveNodes_time() {
		long startTime = System.nanoTime();
		DataModelI.getInstance().retrieveNodes();
		long endTime = System.nanoTime();
		long timeTaken = (endTime - startTime) / 1000000;

		System.out.println("It takes " + timeTaken + " ms to retrieve nodes");
	}

	@Test
	public void printNodes() {
		int i = 0;
		listOfNodes = DataModelI.getInstance().retrieveNodes();
		while(i < listOfNodes.size()) {
			System.out.println(listOfNodes.get(i).getLongName());
			i++;
		}
	}

	@Test
	public void retrieveNodes_returnsCorrectList_Floor() {
		List<Node> listOfNodes = DataModelI.getInstance().retrieveNodes();

		List<String> listOfFloors = new ArrayList<>();
		listOfFloors.add("1");
		listOfFloors.add("2");
		listOfFloors.add("3");
		listOfFloors.add("L1");
		listOfFloors.add("L2");

		int i = 0;
		while (i < listOfNodes.size()) {
			assertTrue(listOfFloors.contains(listOfNodes.get(i).getFloor()));
			i++;
		}
	}

	@Test
	public void retrieveNodes_returnsCorrectList_Building() {
		List<Node> listOfNodes = DataModelI.getInstance().retrieveNodes();

		List<String> listOfBuildings = new ArrayList<>();
		listOfBuildings.add("Shapiro");
		listOfBuildings.add("dff"); // !!! this is necessary because one of the file actually has this string
		listOfBuildings.add("45 Francis");
		listOfBuildings.add("Tower");
		listOfBuildings.add("15 Francis");
		listOfBuildings.add("BTM");

		int i = 0;
		while (i < listOfNodes.size()) {
			assertTrue(listOfBuildings.contains(listOfNodes.get(i).getBuilding()));
			i++;
		}
	}

	@Test
	public void addNode_CorrectlyAddsNode() {
		List<Node> oldList = DataModelI.getInstance().retrieveNodes();
		Node addedNode = DataModelI.getInstance().addNode(5, 5, "2", "Shapiro", "CONF", "Longname~", "shortname~", 3, 5, 2);
		List<Node> newList = DataModelI.getInstance().retrieveNodes();
		boolean trueOrFalse = newList.contains(addedNode);
		Node compareNode = newList.get(582);
		assertTrue(trueOrFalse);
	}


	@Test
	public void getByBuildingTypeFloor_ReturnsCorrectNodeList() {
		List<Node> testList = DataModelI.getInstance().getNodesByBuildingTypeFloor("Shapiro", "CONF", "1");
		assertTrue(testList.get(0).getBuilding().equals("Shapiro"));
		assertTrue(testList.get(0).getNodeType().equals("CONF"));
		assertTrue(testList.get(0).getFloor().equals("1"));
	}

	@Test
	public void getByAdjacentNodes_ReturnsCorrectList() {
		List<Node> listOfNodes = DataModelI.getInstance().retrieveNodes();
		Node node = DataModelI.getInstance().getNodeByIDFromList("GCONF02001", listOfNodes);
		List<String> testList = DataModelI.getInstance().getAdjacentNodesFromNode(node);
		for(String n : testList) {
			System.out.println(n);
			assertTrue(!n.isEmpty());
		}
	}

	@Test
	public void getByAdjacentNodes_ReturnsCorrectList2() {
		List<Node> listOfNodes = DataModelI.getInstance().retrieveNodes();
		Node node = DataModelI.getInstance().getNodeByIDFromList("GEXIT001L1",listOfNodes);
		List<String> testList = DataModelI.getInstance().getAdjacentNodesFromNode(node);
		for(String n : testList) {
			System.out.println(n);
			assertTrue(!n.isEmpty());
		}
	}

	@Test
	public void getByAdjacentNodes_ReturnsCorrectList3() {
		List<Node> listOfNodes = DataModelI.getInstance().retrieveNodes();
		Node node = DataModelI.getInstance().getNodeByIDFromList("WHALL002L2",listOfNodes);
		List<String> testList = DataModelI.getInstance().getAdjacentNodesFromNode(node);
		for(String n : testList) {
			System.out.println(n);
			assertTrue(!n.isEmpty());
		}
	}

	@Test
	public void testAdjacentNodesAfterMakeEdge() {
		listOfNodes = DataModelI.getInstance().retrieveNodes();
		Node a_node = DataModelI.getInstance().getNodeByIDFromList("WHALL002L2",listOfNodes);
		Node new_node = DataModelI.getInstance().addNode(360, 1200, "L1", "15 Francis", "DEPT", "ex longname", "ex shortname", 1, 500, 1000);
		DataModelI.getInstance().addEdge(a_node, new_node);
		assertTrue(a_node.getAdjacentNodes().contains(new_node));
	}

	@Test
	public void testAdjacentNodesWhenAddedNodeStatusNotOne() {
		listOfNodes = DataModelI.getInstance().retrieveNodes();
		Node a_node = DataModelI.getInstance().getNodeByIDFromList("WHALL002L2",listOfNodes);
		Node new_node = DataModelI.getInstance().addNode(360, 1200, "L1", "15 Francis", "DEPT", "ex longname", "ex shortname", 2, 500, 1000);
		DataModelI.getInstance().addEdge(a_node, new_node);
		assertTrue(a_node.getAdjacentNodes().contains(new_node));
	}

    @Test
    public void testRetrieveNodesWhenAddedNodeStatusNotOne() {
        listOfNodes = DataModelI.getInstance().retrieveNodes();
        Node a_node = DataModelI.getInstance().getNodeByIDFromList("WHALL002L2",listOfNodes);
        Node new_node = DataModelI.getInstance().addNode(360, 1200, "L1", "15 Francis", "DEPT", "ex longname", "ex shortname", 2, 500, 1000);
		listOfNodes = DataModelI.getInstance().retrieveNodes();
        Node retrieved_node = DataModelI.getInstance().getNodeByIDFromList(new_node.getNodeID(),listOfNodes);
        assertTrue(retrieved_node == null);
    }

	@Test
	public void testRetrieveNodesWhenAddedNodeStatusISOne() {
		listOfNodes = DataModelI.getInstance().retrieveNodes();
		Node a_node = DataModelI.getInstance().getNodeByIDFromList("WHALL002L2",listOfNodes);
		Node new_node = DataModelI.getInstance().addNode(360, 1200, "L1", "15 Francis", "DEPT", "ex longname", "ex shortname", 1, 500, 1000);
		listOfNodes = DataModelI.getInstance().retrieveNodes();
		Node retrieved_node = DataModelI.getInstance().getNodeByIDFromList(new_node.getNodeID(),listOfNodes);
		assertTrue(retrieved_node != null);
	}

	//test adjacentNode with status 2 or 0 or 3


	/*@Test
	public void modifyNode_editsNode() {
		Node addedNode = DataModelI.getInstance().addNode("lmao", 5, 3, "4", "Hello", "lmao", "yoo", "yo", 48, 20);
		DataModelI.getInstance().modifyNode(addedNode.getNodeID(), 5, 3, "4", "yolo", "lmao", "yoo", "yo", 48, 20);
		List<Node> nodeList = DataModelI.getInstance().getNodesByBuilding("yolo");
		System.out.println(DataModelI.getInstance().getNodeByID(addedNode.getNodeID()).getNodeID());
		System.out.println(DataModelI.getInstance().getNodeByID(addedNode.getNodeID()).getBuilding()); // Why does this return shapiro?
		assertTrue(nodeList.get(0).getBuilding().equals("yolo"));
		//DataModelI.getInstance().removeNode(addedNode.getID());
		} */

	/*@Test EDGES ARE PRIVATE
	public void getEdgesFromNodes () {
		Node node = DataModelI.getInstance().getNodeByID("GHALL013L2");
		List<Edge> edgeList = DataModelI.getInstance().getEdgesFromNode(node);
		for(Edge e: edgeList) {
			System.out.println("Edge is: " + e.getEdgeID());
			assertTrue(!e.getEdgeID().isEmpty());
		}
	}*/

}



