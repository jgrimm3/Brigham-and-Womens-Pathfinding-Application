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
    private Restroom restroom1;
    private Restroom restroom2;
    private Restroom restroom3;
    private Restroom restroom4;
    private Restroom restroom5;
    private Restroom restroom6;
    private Edge edge1_2;
    private Edge edge2_3;
    private Edge edge2_4;
    private Edge edge4_5;
    private Edge edge3_5;
    private LinkedList<Node> fixture;

    @Before
    public void createFixtures(){
        pf = new Pathfinder();
        restroom1 = new Restroom("a", "a", "1", "test", 1, 1, "1", "bldg", new ArrayList<>());
        restroom2 = new Restroom("a", "a", "2", "test", 2, 2, "1", "bldg", new ArrayList<>());
        restroom3 = new Restroom("a", "a", "3", "test", 3, 3, "1", "bldg", new ArrayList<>());
        restroom4 = new Restroom("a", "a", "4", "test", 4, 4, "1", "bldg", new ArrayList<>());
        restroom5 = new Restroom("a", "a", "5", "test", 5, 5, "1", "bldg", new ArrayList<>());
        restroom6 = new Restroom("a", "a", "6", "test", 6, 6, "1", "bldg", new ArrayList<>());
        edge1_2 = new Edge(restroom1, restroom2, "test", 0);
        edge2_3 = new Edge(restroom2, restroom3, "test", 0);
        edge2_4 = new Edge(restroom2, restroom4, "test", 0);
        edge4_5 = new Edge(restroom4, restroom5, "test", 0);
        edge3_5 = new Edge(restroom3, restroom5, "test", 0);
        restroom1.getEdges().add(edge1_2);
        restroom2.getEdges().add(edge1_2);
        restroom2.getEdges().add(edge2_3);
        restroom2.getEdges().add(edge2_4);
        restroom3.getEdges().add(edge2_3);
        restroom3.getEdges().add(edge3_5);
        restroom4.getEdges().add(edge2_4);
        restroom4.getEdges().add(edge4_5);

        fixture = new LinkedList<>();
    }

    @Test
    public void Find_WhenOnlyOneNode_ShouldReturnNode(){
        fixture.add(restroom1);

        LinkedList<Node> result = pf.find(restroom1, restroom1);
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

