import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.manlyminotaurs.nodes.Restroom;
import com.manlyminotaurs.core.Pathfinder;
import com.manlyminotaurs.nodes.Node;

import java.util.LinkedList;



public class PathfinderTest {

    @Test
    public void Find_AtGoal_ReturnPath(){
        Pathfinder pf = new Pathfinder();
        Restroom testNode = new Restroom();

        LinkedList<Node> fixture = new LinkedList<>();
        fixture.add(testNode);

        LinkedList<Node> result = pf.find(testNode, testNode);
        assertEquals(fixture, result);
    }
}

