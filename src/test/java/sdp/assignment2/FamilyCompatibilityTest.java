package sdp.assignment2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import javax.tools.*;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FamilyCompatibilityTest {
    @TempDir Path directory;

    @Test void matchingFamilyCompilesAndMixedFamilyDoesNot() throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull(compiler, "Run tests with a JDK, not a JRE");
        String classpath = Path.of(FabricationFactory.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI()).toString();
        String source = """
                import sdp.assignment2.*;
                class Combination {
                    void use() {
                        FabricationJob<PrintingFamily> job = new PrintingFactory()
                            .createJob(new Design("Sign", 2, 10));
                        Machine<%s> machine = new %s().createMachine();
                        machine.enqueue(job);
                    }
                }
                """;
        assertTrue(compile(compiler, classpath,
                source.formatted("PrintingFamily", "PrintingFactory")));
        assertFalse(compile(compiler, classpath,
                source.formatted("LaserFamily", "LaserFactory")));
    }

    private boolean compile(JavaCompiler compiler, String classpath, String source) throws Exception {
        Path file = directory.resolve("Combination.java");
        Files.writeString(file, source);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager files = compiler.getStandardFileManager(diagnostics, null, null)) {
            return compiler.getTask(null, files, diagnostics,
                    List.of("--release", "21", "-classpath", classpath, "-d", directory.toString()),
                    null, files.getJavaFileObjects(file.toFile())).call();
        }
    }
}
