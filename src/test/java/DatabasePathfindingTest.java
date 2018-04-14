import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.pathfinding.AStarStrategyI;
import com.manlyminotaurs.pathfinding.PathNotFoundException;
import com.manlyminotaurs.pathfinding.PathfindingContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabasePathfindingTest {

    @Test
    public void PathfindWithDBNodes(){
        List<Node> nodes = DataModelI.getInstance().retrieveNodes();
        Node startNode = DataModelI.getInstance().getNodeByIDFromList("GHALL00201", nodes);
        Node endNode = DataModelI.getInstance().getNodeByIDFromList("GHALL00201", nodes);

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
        List<Node> nodes = DataModelI.getInstance().retrieveNodes();
        Node startNode = DataModelI.getInstance().getNodeByIDFromList("GHALL00201", nodes);
        Node endNode = DataModelI.getInstance().getNodeByIDFromList("GHALL00301", nodes);

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
        List<Node> nodes = DataModelI.getInstance().retrieveNodes();
        Node startNode = DataModelI.getInstance().getNodeByIDFromList("GHALL02401", nodes);
        Node endNode = DataModelI.getInstance().getNodeByIDFromList("GDEPT02402", nodes);
        // GHALL02401, GELEV00N01, GELEV00N02, GDEPT02402

        LinkedList<Node> expected = new LinkedList<>();
        expected.add(DataModelI.getInstance().getNodeByIDFromList("GHALL02401", nodes));
        expected.add(DataModelI.getInstance().getNodeByIDFromList("GELEV00N01", nodes));
        expected.add(DataModelI.getInstance().getNodeByIDFromList("GELEV00N02", nodes));
        expected.add(DataModelI.getInstance().getNodeByIDFromList("GDEPT02402", nodes));

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


}
