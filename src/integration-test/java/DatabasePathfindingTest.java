import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.pathfinding.AStarStrategyI;
import com.manlyminotaurs.pathfinding.PathNotFoundException;
import com.manlyminotaurs.pathfinding.PathfindingContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class DatabasePathfindingTest {
    static DataModelI dbm = DataModelI.getInstance();

    @Test
    public void PathfindWithDBNodes(){
        ArrayList<Node> nodes = new ArrayList<>(dbm.retrieveNodes());
        Node startNode = dbm.getNodeByID("GHALL00201");
        Node endNode = dbm.getNodeByID("GHALL00201");

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
}
