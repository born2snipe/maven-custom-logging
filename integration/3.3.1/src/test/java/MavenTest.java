import integration.AbstractMavenVersionSupportTestCase;
import org.junit.BeforeClass;

public class MavenTest extends AbstractMavenVersionSupportTestCase {
    @BeforeClass
    public static void init() {
        multiModuleSystemProperty = true;
    }
}
