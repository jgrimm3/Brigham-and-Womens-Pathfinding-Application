import com.manlyminotaurs.core.PathfinderUtil;
import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.ScoredNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    Node node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11, node12, fakeNode;
    private PathfinderUtil pu;

    @Before
    public void createFixtures(){
        pf = new Pathfinder();
        ne = new NodesEditor();
        pu = new PathfinderUtil();
        ne.retrieveNodes();
        ne.retrieveEdges();

        node1 = ne.getNodeFromList("GCONF02001");
        node2 = ne.getNodeFromList("GDEPT01901");
        node3 = ne.getNodeFromList("GELEV00Q01");
        node4 = ne.getNodeFromList("GHALL01701");
        /* used in 'Find_WhenManyNodesWithBranching_ShouldReturnPath()' */
        node5 = ne.getNodeFromList("GHALL00201");
        node6 = ne.getNodeFromList("GEXIT00101");
        node7 = ne.getNodeFromList("GSTAI01301");
        node8 = ne.getNodeFromList("GHALL00601");
        node9 = ne.getNodeFromList("GHALL00701");
        node10 = ne.getNodeFromList("GHALL00801");
        node11 = ne.getNodeFromList("GHALL01401");
        node12 = ne.getNodeFromList("GHALL01501");
        /* used in 'Find_NodeWithNoPath_ShouldReturnNullAndErrorMSG()' */
        fakeNode = ne.getNodeFromList("GZQ111000");


        fixture = new LinkedList<Node>();
    }
    @Test
    public void Find_angleBetween3Nodes_ShouldReturnDirection() {
        ScoredNode sNode1 = new ScoredNode(node5, null, -1, -1, -1);
        ScoredNode sNode2 = new ScoredNode(node6, null, -1, -1, -1);
        ScoredNode sNode3 = new ScoredNode(node7, null, -1, -1, -1);
        assertEquals("not straight", pu.angleToText(sNode1, sNode2, sNode3));
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
    public void Find_WhenManyNodesWithBranching_ShouldReturnPath(){
        fixture.add(node5);
        fixture.add(node6);
        fixture.add(node7);
        fixture.add(node8);
        fixture.add(node9);
        fixture.add(node10);

        LinkedList<Node> result = pf.find(node5, node10);
        assertEquals(fixture, result);
    }

    @Test
    public void Find_NodeWithNoPath_ShouldReturnNullAndErrorMSG(){
        LinkedList<Node> result = pf.find(fakeNode, node1);
        assertEquals(null, result);
    }

    @Test
    public void GetEdges_WhenGivenNodeWithOneEdge_ShouldReturnThatEdge(){
        LinkedList<Edge> edgeFixture = new LinkedList<>();
        Edge testEdge = new Edge(node2, node1, "GDEPT01901_GCONF02001");
        edgeFixture.add(testEdge);
        ArrayList<Edge> result = pf.getEdges(new ScoredNode(node1, null, -1, -1, -1), new ArrayList<Edge>(ne.getEdgeList()));
        System.out.println(result.get(0).getStartNode().getID());
        System.out.println(result.get(0).getEndNode().getID());
        assertEquals(edgeFixture, result);
    }

    @Test
    public void GetEdges_WhenGivenNodeWithMultipleEdges_ShouldReturnEdgeList(){
        LinkedList<Edge> edgeFixture = new LinkedList<>();
        Edge testEdge = new Edge(node11, node10, "GHALL01401_GHALL00801");
        Edge testEdge2 = new Edge(node11, node12, "GHALL01401_GHALL01501");
        edgeFixture.add(testEdge);
        edgeFixture.add(testEdge2);
        ArrayList<Edge> result = pf.getEdges(new ScoredNode(node11, null, -1, -1, -1), new ArrayList<Edge>(ne.getEdgeList()));
        System.out.println(result.size());
        assertEquals(edgeFixture, result);
    }
}

