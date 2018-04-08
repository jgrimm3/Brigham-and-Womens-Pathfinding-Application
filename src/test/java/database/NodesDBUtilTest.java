package database;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;


public class NodesDBUtilTest {

	// Variables
	DataModelI dataModelI = DataModelI.getInstance();

	final int THREED_STARTX = 950;
	final int THREED_ENDX = 1500;
	final int THREED_STARTY = 1500;
	final int THREED_ENDY = 2100;

	// Tests
	@Test
	public void retrieveNodes_returnsCorrectList_xCoord3DandyCorrd3D() {
		List<Node> listOfNodes = dataModelI.retrieveNodes();
		int i = 0;
		while(i<listOfNodes.size()) {
			assertTrue(listOfNodes.get(i).getXCoord3D() < THREED_ENDX && listOfNodes.get(i).getXCoord3D() > THREED_STARTX);
			assertTrue(listOfNodes.get(i).getYCoord3D() < THREED_ENDY && listOfNodes.get(i).getYCoord3D() > THREED_STARTY);
			i ++;

		}
	}

	@Test
	public void retrieveNodes_returnsCorrectList_Floor() {
		List<Node> listOfNodes = dataModelI.retrieveNodes();

		List<String> listOfFloors = new ArrayList<>();
		listOfFloors.add("Floor 1");
		listOfFloors.add("Floor 2");
		listOfFloors.add("Floor 3");
		listOfFloors.add("L1");
		listOfFloors.add("L2");

		int i = 0;
		while(i<listOfNodes.size()) {
			assertTrue(listOfFloors.contains(listOfNodes.get(i).getFloor()));
			i++;
		}
	}

	@Test
	public void retrieveNodes_returnsCorrectList_Building() {
		List<Node> listOfNodes = dataModelI.retrieveNodes();

		List<String> listOfBuildings = new ArrayList<>();
		listOfBuildings.add("Shapiro");
		listOfBuildings.add("beachHouse");
		listOfBuildings.add("FL320");
		listOfBuildings.add("HL420");
		listOfBuildings.add("Shapiro2");


		int i = 0;
		while(i<listOfNodes.size()) {
			assertTrue(listOfBuildings.contains(listOfNodes.get(i).getBuilding()));
			i++;
		}
	}




}



