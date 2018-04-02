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
            if (node.getID().equals(nodeID)) {
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

        fixture = new LinkedList<Node>();
    }

    @Test
    public void Find_WhenOnlyOneNode_ShouldReturnNode(){
        Node node1 = ne.getNodeFromList("GCONF02001");
        fixture.add(node1);

        LinkedList<Node> result = pf.find(node1, node1);
        assertEquals(fixture, result);
    }

    @Test
    public void Find_WhenOnlyTwoNodes_ShouldReturnPath(){
        Node node1 = ne.getNodeFromList("GCONF02001");
        Node node2 = ne.getNodeFromList("GDEPT01901");
        System.out.println(node1);
        System.out.println(node2);


        fixture.add(node1);
        fixture.add(node2);

        LinkedList<Node> result = pf.find(node1, node2);
        assertEquals(fixture, result);
    }
//
//    @Test
//    public void Find_WhenManyNodes_ShouldReturnPath(){
//        fixture.add(restroom1);
//        fixture.add(restroom2);
//        fixture.add(restroom4);
//
//        LinkedList<Node> result = pf.find(restroom1, restroom4);
//        assertEquals(fixture, result);
//    }

    @Test
    public void GetEdges_WhenGivenNodeWithOneEdge_ShouldReturnThatEdge(){
        LinkedList<Edge> edgeFixture = new LinkedList<>();
        Node node1 = ne.getNodeFromList("GCONF02001");
        Node node2 = ne.getNodeFromList("GDEPT01901");
        Edge testEdge = new Edge(node2, node1, "GDEPT01901_GCONF02001");

        edgeFixture.add(testEdge);
        ArrayList<Edge> result = pf.getEdges(new ScoredNode(node1, null, -1, -1, -1), new ArrayList<Edge>(ne.edgeList));
        assertEquals(edgeFixture, result);
    }

}

