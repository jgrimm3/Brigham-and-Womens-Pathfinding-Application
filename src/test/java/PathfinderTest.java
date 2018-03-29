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
    private Edge edge1;
    private LinkedList<Node> fixture;

    @Before
    public void createFixtures(){
        pf = new Pathfinder();
        restroom1 = new Restroom("a", "a", "1", "test", 1, 1, "1", "bldg", new ArrayList<>());
        restroom2 = new Restroom("a", "a", "2", "test", 2, 2, "1", "bldg", new ArrayList<>());
        edge1 = new Edge(restroom1, restroom2, "test", 0);
//        System.out.println(restroom1.getEdges());
        restroom1.getEdges().add(edge1);
        restroom2.getEdges().add(edge1);

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


}

