import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.pathfinding.AStarStrategyI;
import com.manlyminotaurs.pathfinding.PathNotFoundException;
import com.manlyminotaurs.pathfinding.PathfindingContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabasePathfindingTest {
    @BeforeClass
    public static void setup(){
        DataModelI.getInstance().startDB();
    }


    @Test
    public void PathfindWithDBNodes(){
        Node startNode = DataModelI.getInstance().getNodeByID("GHALL00201");
        Node endNode = DataModelI.getInstance().getNodeByID("GHALL00201");

        LinkedList<Node> expected = new LinkedList<>();
        expected.add(startNode);

        PathfindingContext pf = new PathfindingContext();
        LinkedList<Node> result = new LinkedList<>();
        try {
            result = pf.getPath(startNode, endNode, new AStarStrategyI());
        } catch (PathNotFoundException e){
            e.printStackTrace();
        }
        assertEquals(expected, result);

    }

    @Test
    public void PathfindWithDBNodes2(){
        Node startNode = DataModelI.getInstance().getNodeByID("GHALL00201");
        Node endNode = DataModelI.getInstance().getNodeByID("GHALL00301");

        LinkedList<Node> expected = new LinkedList<>();
        expected.add(startNode);
        expected.add(endNode);

        PathfindingContext pf = new PathfindingContext();
        LinkedList<Node> result = new LinkedList<>();
        try {
            result = pf.getPath(startNode, endNode, new AStarStrategyI());
        } catch (PathNotFoundException e){
            e.printStackTrace();
        }
        assertEquals(expected, result);

    }

    @Test
    public void PathfindWithDBNodesWithFloorChange(){
        Node startNode = DataModelI.getInstance().getNodeByID("GHALL02401");
        Node endNode = DataModelI.getInstance().getNodeByID("GDEPT02402");
        // GHALL02401, GELEV00N01, GELEV00N02, GDEPT02402

        LinkedList<Node> expected = new LinkedList<>();
        expected.add(DataModelI.getInstance().getNodeByID("GHALL02401"));
        expected.add(DataModelI.getInstance().getNodeByID("GELEV00N01"));
        expected.add(DataModelI.getInstance().getNodeByID("GELEV00N02"));
        expected.add(DataModelI.getInstance().getNodeByID("GDEPT02402"));

        PathfindingContext pf = new PathfindingContext();
        LinkedList<Node> result = new LinkedList<>();
        try {
            result = pf.getPath(startNode, endNode, new AStarStrategyI());
        } catch (PathNotFoundException e){
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void LongPathfindingTest(){

        Node startNode = DataModelI.getInstance().getNodeByID("ALABS001L2");
        Node endNode = DataModelI.getInstance().getNodeByID("CDEPT002L1");

        PathfindingContext pf = new PathfindingContext();
        LinkedList<Node> result = new LinkedList<>();
        try {
            result = pf.getPath(startNode, endNode, new AStarStrategyI());
        } catch (PathNotFoundException e){
            e.printStackTrace();
        }
        System.out.println(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void BreakingPathfindingTest(){
        Node startNode = DataModelI.getInstance().getNodeByID("FHALL03101");
        Node endNode = DataModelI.getInstance().getNodeByID("GHALL01401");

        PathfindingContext pf = new PathfindingContext();
        LinkedList<Node> result = new LinkedList<>();
        try {
            result = pf.getPath(startNode, endNode, new AStarStrategyI());
        } catch (PathNotFoundException e){
            e.printStackTrace();
        }
        System.out.println(result);
        assertFalse(result.isEmpty());
    }

}
