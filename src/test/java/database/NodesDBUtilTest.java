package database;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;


public class NodesDBUtilTest {

	final int THREED_STARTX = 950;
	final int THREED_ENDX = 1600;
	final int THREED_STARTY = 1500;
	final int THREED_ENDY = 2100;

	List<Node> listOfNodes = new ArrayList<>();

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
		listOfBuildings.add("beachHouse");
		listOfBuildings.add("FL320");
		listOfBuildings.add("HL420");
		listOfBuildings.add("Shapiro2");


		int i = 0;
		while (i < listOfNodes.size()) {
			assertTrue(listOfBuildings.contains(listOfNodes.get(i).getBuilding()));
			i++;
		}
	}

	@Test
	public void addNode_CorrectlyAddsNode() {
		Node addedNode = DataModelI.getInstance().addNode("lmao", 5, 3, "4", "Hello", "lmao", "yoo", "yo", 48, 20);
		assertTrue(DataModelI.getInstance().doesNodeExist(addedNode.getID()));
		DataModelI.getInstance().removeNode(addedNode.getID());
		assertFalse(DataModelI.getInstance().doesNodeExist(addedNode.getID()));
	}

}



