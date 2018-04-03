import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.ScoredNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.manlyminotaurs.core.Pathfinder;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.Edge;

import java.util.ArrayList;
import java.util.LinkedList;

public class PathfinderTest {
    private Pathfinder pf;
    private NodesEditor ne;
    private LinkedList<Node> fixture;
    Node node1;
    Node node2;
    Node node3;
    Node node4;

    @Before
    public void createFixtures(){
        pf = new Pathfinder();
        ne = new NodesEditor();
        ne.retrieveNodes();
        ne.retrieveEdges();

        node1 = ne.getNodeFromList("GCONF02001");
        node2 = ne.getNodeFromList("GDEPT01901");
        node3 = ne.getNodeFromList("GELEV00Q01");
        node4 = ne.getNodeFromList("GHALL01701");



        fixture = new LinkedList<Node>();
    }

    @Test
    public void Find_WhenOnlyOneNode_ShouldReturnNode(){
        fixture.add(node1);

        LinkedList<Node> result = pf.find(node1, node1);
        assertEquals(fixture, result);
    }

    @Test
    public void Find_WhenOnlyTwoNodes_ShouldReturnPath(){
        fixture.add(node1);
        fixture.add(node2);

        LinkedList<Node> result = pf.find(node1, node2);
        assertEquals(fixture, result);
    }

    @Test
    public void Find_WhenManyNodesWithNoBranching_ShouldReturnPath(){
        fixture.add(node1);
        fixture.add(node2);
        fixture.add(node3);
        fixture.add(node4);

        LinkedList<Node> result = pf.find(node1, node4);
        assertEquals(fixture, result);
    }

    @Test
    public void GetEdges_WhenGivenNodeWithOneEdge_ShouldReturnThatEdge(){
        LinkedList<Edge> edgeFixture = new LinkedList<>();
        Edge testEdge = new Edge(node2, node1, "GDEPT01901_GCONF02001");

        edgeFixture.add(testEdge);
        ArrayList<Edge> result = pf.getEdges(new ScoredNode(node1, null, -1, -1, -1), new ArrayList<Edge>(ne.getEdgeList()));
        assertEquals(edgeFixture, result);
    }

}

