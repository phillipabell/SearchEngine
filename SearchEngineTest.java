import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SearchEngineTest {

    @Test
    public void testIndexCommand() {
        SearchEngine engine = new SearchEngine();

        assertEquals("index ok 1", engine.processCommand("index 1 soup tomato cream salt"));
        assertEquals("index ok 2", engine.processCommand("index 2 cake sugar eggs flour sugar cocoa cream butter"));
        assertEquals("index ok 1", engine.processCommand("index 1 bread butter salt"));
        assertEquals("index ok 3", engine.processCommand("index 3 soup fish potato salt pepper"));
    }

    @Test
    public void testInvalidIndexCommand() {
        SearchEngine engine = new SearchEngine();

        assertEquals("index error For input string: \"ok\"", engine.processCommand("index ok doc-id"));
    }
}
