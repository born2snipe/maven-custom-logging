package integration;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import project.TestExecutor;

import java.io.File;
import java.io.IOException;

public class AbstractMavenVersionSupportTestCase {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldWork() throws IOException {
            File folder = temporaryFolder.newFolder();
            TestExecutor.execute(folder);
    }
}
