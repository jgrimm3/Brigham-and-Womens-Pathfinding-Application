import com.manlyminotaurs.nodes.Hallway;
import com.manlyminotaurs.nodes.Room;
import com.manlyminotaurs.pathfinding.ClosestStrategyI;
import com.manlyminotaurs.pathfinding.PathNotFoundException;
import com.manlyminotaurs.pathfinding.PathfinderUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.manlyminotaurs.pathfinding.AStarStrategyI;
import com.manlyminotaurs.nodes.Node;

import java.util.ArrayList;
import java.util.LinkedList;

public class ClosestStrategyITest {
    private ClosestStrategyI pf;
    private LinkedList<Node> expected;
    private Node node0_0, node1_0, node2_0, node0_1, node1_1, node2_1, node0_2, node1_2, node2_2, node0_3, node1_3, nodeOrphan;
    private PathfinderUtil pu;

    @Before
    public void createFixtures(){
        pf = new ClosestStrategyI();
        pu = new PathfinderUtil();


        node0_0 = new Hallway("N0X0Y",0,0,"F1","BUILD1","HALL","Node 0, 0", "n0x0y",1,0,0);
        node1_0 = new Hallway("N1X0Y",1,0,"F1","BUILD1","HALL","Node 1, 0", "n1x0y",1,0,0);
        node2_0 = new Hallway("N2X0Y",2,0,"F1","BUILD1","HALL","Node 2, 0", "n2x0y",1,0,0);
        node0_1 = new Hallway("N0X1Y",0,1,"F1","BUILD1","HALL","Node 0, 1", "n0x1y",1,0,0);
        node1_1 = new Hallway("N1X1Y",1,1,"F1","BUILD1","HALL","Node 1, 1", "n1x1y",1,0,0);
        node2_1 = new Room("N2X1Y",2,1,"F1","BUILD1","REST","Node 2, 1", "n2x1y",1,0,0);
        node0_2 = new Hallway("N0X2Y",0,2,"F1","BUILD1","HALL","Node 0, 2", "n0x2y",1,0,0);
        node1_2 = new Hallway("N1X2Y",1,2,"F1","BUILD1","HALL","Node 1, 2", "n1x2y",1,0,0);
        node2_2 = new Hallway("N2X2Y",2,2,"F1","BUILD1","HALL","Node 2, 2", "n2x2y",1,0,0);
        node0_3 = new Hallway("N0X3Y",0,3,"F1","BUILD1","HALL","Node 0, 3", "n0x3y",1,0,0);
        node1_3 = new Room("N1X3Y",1,3,"F1","BUILD1","REST","Node 1, 3", "n1x3y",1,0,0);
        nodeOrphan = new Hallway("NORPH",3,4,"F1","BUILD1","Orphaned Node", "nOrph",  "HALL", 1,0,0);
/*

*/
        // Populate edges
        node0_0.addAdjacentNode(node1_0);
        node1_0.addAdjacentNode(node0_0);
        node1_0.addAdjacentNode(node2_0);
        node2_0.addAdjacentNode(node1_0);
        node2_0.addAdjacentNode(node2_1);
        node0_1.addAdjacentNode(node1_1);
        node0_1.addAdjacentNode(node0_2);
        node1_1.addAdjacentNode(node0_1);
        node1_1.addAdjacentNode(node0_3);
        node1_1.addAdjacentNode(node1_2);
        node1_1.addAdjacentNode(node2_1);
        node2_1.addAdjacentNode(node1_1);
        node2_1.addAdjacentNode(node2_2);
        node0_2.addAdjacentNode(node0_1);
        node0_2.addAdjacentNode(node0_3);
        node1_2.addAdjacentNode(node1_1);
        node1_2.addAdjacentNode(node2_2);
        node1_2.addAdjacentNode(node1_3);
        node2_2.addAdjacentNode(node2_1);
        node2_2.addAdjacentNode(node1_2);
        node0_3.addAdjacentNode(node0_2);
        node0_3.addAdjacentNode(node1_1);
        node0_3.addAdjacentNode(node1_3);

        expected = new LinkedList<Node>();
    }

    @Test
    public void Find_WhenStartAndEndNodesAreSame_ShouldReturnNode(){
        expected.add(node0_0);
        LinkedList<Node> result = new LinkedList<>();
        try {
            result = pf.find(node0_0, node0_0);
        } catch (PathNotFoundException e){
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void Find_WhenStartAndEndNodesAreOnSameEdge_ShouldReturnPath(){
        int x = 0;
        expected.add(node0_3);
        expected.add(node1_3);
        LinkedList<Node> result = new LinkedList<>();
        try {
            result = pf.find(node0_3, node1_3);
        } catch (PathNotFoundException e){
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void Find_WhenManyNodesWithNoBranching_ShouldReturnPath(){
        int x = 0;
        expected.add(node0_0);
        expected.add(node1_0);
        expected.add(node2_0);
        expected.add(node2_1);
        LinkedList<Node> result = new LinkedList<>();
        try {
            result = pf.find(node0_0, node2_1);
        } catch (PathNotFoundException e){
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test(expected = PathNotFoundException.class)
    public void Find_WhenGivenOrphanStartNode_ShouldThrowPathNotFoundException() throws PathNotFoundException{
        LinkedList<Node> result = pf.find(nodeOrphan, node1_1);
    }

    @Test(expected = PathNotFoundException.class)
    public void Find_WhenGivenOrphanEndNode_ShouldThrowPathNotFoundException() throws PathNotFoundException{
        LinkedList<Node> result = pf.find(node1_1, nodeOrphan);
    }


}
