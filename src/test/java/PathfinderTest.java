import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.ScoredNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.manlyminotaurs.nodes.Restroom;
import com.manlyminotaurs.core.Pathfinder;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.Edge;

import java.util.ArrayList;
import java.util.LinkedList;

public class PathfinderTest {
    private Pathfinder pf;
    private NodesEditor ne;
    private LinkedList<Node> fixture;

    Node retrieveNode(String nodeID) {
        for (Node node: ne.nodeList) {
            if (node.getID() == nodeID) {
                return node;
            }
        }
        return null;
    }

    @Before
    public void createFixtures(){
        pf = new Pathfinder();
        ne = new NodesEditor();
        ne.retrieveNodes();
        ne.retrieveEdges();

        fixture = new LinkedList<>();
    }

    @Test
    public void Find_WhenOnlyOneNode_ShouldReturnNode(){
        Node node1 = retrieveNode("GCONF02001");
        fixture.add(node1);

        LinkedList<Node> result = pf.find(node1, node1);
        assertEquals(fixture, result);
    }

    @Test
    public void Find_WhenOnlyTwoNodes_ShouldReturnPath(){
        fixture.add(restroom1);
        fixture.add(restroom2);

        LinkedList<Node> result = pf.find(restroom1, restroom2);
        assertEquals(fixture, result);
    }

    @Test
    public void Find_WhenManyNodes_ShouldReturnPath(){
        fixture.add(restroom1);
        fixture.add(restroom2);
        fixture.add(restroom4);

        LinkedList<Node> result = pf.find(restroom1, restroom4);
        assertEquals(fixture, result);
    }

}

