package database;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;
import org.junit.Before;
import org.junit.Test;

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
		DataModelI.getInstance().removeNode("1");
		DataModelI.getInstance().removeNode("Xlmao2004");

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
		Node addedNode = DataModelI.getInstance().addNode(5, 5, "s", "s", "t", "w", "t", 3, 5, 2);
		System.out.println(addedNode.getNodeID());
		//assertTrue(DataModelI.getInstance().getNodeByID(addedNode.getNodeID()) != null);
		assertTrue(DataModelI.getInstance().removeNode(addedNode.getNodeID()) == true);
		//assertTrue(DataModelI.getInstance().getNodeByID(addedNode.getNodeID())==null);
	}

	/*
	@Test
	public void getByBuildingTypeFloor_ReturnsCorrectNodeList() {
		List<Node> testList = DataModelI.getInstance().getNodesByBuildingTypeFloor("Shapiro", "CONF", "1");
		assertTrue(testList.get(0).getBuilding().equals("Shapiro"));
		assertTrue(testList.get(0).getNodeType().equals("CONF"));
		assertTrue(testList.get(0).getFloor().equals("1"));
	}

	@Test
	public void getByAdjacentNodes_ReturnsCorrectList() {
		Node node = DataModelI.getInstance().getNodeByID("GCONF02001");
		List<String> testList = DataModelI.getInstance().getAdjacentNodesFromNode(node);
		for(String n : testList) {
			System.out.println(n);
			//assertTrue(!n.getLongName().isEmpty());
		}

	}

	@Test
	public void getByAdjacentNodes_ReturnsCorrectList2() {
		Node node = DataModelI.getInstance().getNodeByID("GEXIT001L1");
		List<Node> testList = DataModelI.getInstance().getAdjacentNodesFromNode(node);
		for(Node n : testList) {
			System.out.println(n.getLongName());
			assertTrue(!n.getLongName().isEmpty());
		}

	}

	@Test
	public void getByAdjacentNodes_ReturnsCorrectList3() {
		Node node = DataModelI.getInstance().getNodeByID("WHALL002L2");
		List<Node> testList = DataModelI.getInstance().getAdjacentNodesFromNode(node);
		for(Node n : testList) {
			System.out.println(n.getLongName());
			assertTrue(!n.getLongName().isEmpty());
		}

	}

	@Test
	public void getByAdjacentNodes_ReturnsCorrectList4() {
		Node node = DataModelI.getInstance().getNodeByID("GHALL013L2");
		List<Node> testList = DataModelI.getInstance().getAdjacentNodesFromNode(node);
		for(Node n : testList) {
			System.out.println(n.getLongName());
			assertTrue(!n.getLongName().isEmpty());
		}

	} */

	@Test
	public void testAdjacentNodes() {
		listOfNodes = DataModelI.getInstance().retrieveNodes();
		listOfNodes.contains("hi");
	}


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



