import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;


public class CodeWriterTester {

    private static CodeWriter codeWriter;

    @BeforeAll
    public static void setupCodeWriter() {
    }

    @Test
    public void testIfCommandsMatch() throws IOException {

        codeWriter = new CodeWriter("TestOutput.vm");

        // push local 7
        codeWriter.writePushPop(Parser.CommandType.C_PUSH, "local", 7);
        // pop static 3
        codeWriter.writePushPop(Parser.CommandType.C_POP, "static", 3);
        // add
        codeWriter.writeArithmetic("add");
        // eq
        codeWriter.writeArithmetic("eq");
        // not
        codeWriter.writeArithmetic("not");

        assertEquals(Files.readString(Path.of("src/TestOutput.cmp")), Files.readString(Path.of("TestOutput.asm")));

    }
}
