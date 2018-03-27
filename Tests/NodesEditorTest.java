import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.Conference;
import com.manlyminotaurs.nodes.Department;
import com.manlyminotaurs.nodes.Node;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NodesEditorTest {

    NodesEditor editor = new NodesEditor();
    Node node = new Conference("Duncan Reid Conference Room", "Conf B0102", "BCONF00102", "CONF", 2150, 1025, 2, "45 Francis");
    Node addNode = new Conference("Andrew", "lmao", "whut", "BAMF", 9999, 9999, 9, "Was good");

    @Test
    public void RetrieveData_Data_Created() {
        editor.retrieveData();
        assertTrue(editor.nodeList.get(0).getNodeType().equals("CONF"));
    }

    @Test
    public void RetrieveData_Objects_Created() {
        int i = 0;
        editor.retrieveData();
        while(i < editor.nodeList.size()) {
            System.out.println("Object " + i + ": " + editor.nodeList.get(i).getLongName());
            assertTrue(editor.nodeList.get(i) != null);
            i++;
        }
    }

    @Test
    public void RetrieveData_Data_Size() {
        editor.retrieveData();
        assertTrue(editor.nodeList.size() > 0);
    }

    @Test
    public void RetrieveData_Data_Created2() {
        editor.retrieveData();
        assertTrue(editor.nodeList.contains(node));
    }

    @Test
    public void addNode_Data_Correct() {
        editor.retrieveData();
        editor.addNode(addNode);
        assertTrue(editor.nodeList.contains(addNode));
        editor.retrieveData();
        assertTrue(editor.nodeList.contains(addNode));
    }

    @Test
    public void addNode_Data_Correct2() {
        editor.retrieveData();
        assertTrue(editor.nodeList.contains(addNode));
    }
}
