import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.manlyminotaurs.nodes.Restroom;
import com.manlyminotaurs.core.Pathfinder;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.Edge;

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
        restroom1 = new Restroom();
        restroom2 = new Restroom();
        edge1 = new Edge(restroom1, restroom2, "test", 0);

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

