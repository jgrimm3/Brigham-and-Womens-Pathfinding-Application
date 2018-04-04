import com.manlyminotaurs.core.PathNotFoundException;
import com.manlyminotaurs.core.PathfinderUtil;
import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.ScoredNode;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.manlyminotaurs.nodes.Restroom;
import com.manlyminotaurs.core.Pathfinder;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.Edge;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;


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
    public void Find_WhenStartAndEndNodesAreSame_ShouldReturnNode(){
        fixture.add(node1);

        LinkedList<Node> result = pf.find(node1, node1);
        assertEquals(fixture, result);
    }

    @Test
    public void Find_WhenStartAndEndNodesAreOnSameEdge_ShouldReturnPath(){
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


    @Test(expected = PathNotFoundException.class)
    public void Find_WhenGivenOrphanNode_ShouldThrowPathNotFoundException() throws PathNotFoundException{
        PriorityQueue<ScoredNode> openList = new PriorityQueue<>();
        HashMap<String, ScoredNode> closedList = new HashMap<>();

        ScoredNode scoredStart = new ScoredNode(fakeNode, null, -1, -1, -1);
        ScoredNode scoredEnd = new ScoredNode(node1, null, -1, -1, -1);

        NodesEditor ne = new NodesEditor();
        ne.retrieveNodes();
        ne.retrieveEdges();
        ArrayList<Node> nodes = new ArrayList<>(ne.getNodeList());
        ArrayList<Edge> edges = new ArrayList<>(ne.getEdgeList());


        LinkedList<ScoredNode> result = pf.calcPath(scoredStart, scoredEnd, openList, closedList, nodes, edges);
    }

    @Test
    public void Find_TbtDirectionsForPath_ShouldReturnDirectionList() {
        ArrayList<String> fixture = new ArrayList<>();
        fixture.add("Turn left at Staircase Node 13 Floor 1");
        fixture.add("Turn left at Hallway Node 6 Floor 1");
        fixture.add("Turn right at Hallway Node 7 Floor 1");
        fixture.add("Continue straight at Hallway Node 8 Floor 1");
        assertEquals(fixture, pu.angleToText(pf.find(node5, node10)));
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

