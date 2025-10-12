import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;

public class Assembler {
    public static void main(String[] args) throws IOException {

        String inputFile = null;
        if (args.length > 0) inputFile = args[0];
        assert inputFile != null;
        InputStream is = new FileInputStream(inputFile);

        // CharStream that reads from standard input
        ANTLRInputStream input = new ANTLRInputStream(is);
        // Lexer that feeds off of input CharStream
        AssemblyLexer lexer = new AssemblyLexer(input);
        // Buffer of tokens pulled from lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Parser that feeds off of tokens buffer
        AssemblyParser parser = new AssemblyParser(tokens);
        ParseTree tree = parser.init(); // Parse starting at init rule

        ParseTreeWalker walker = new ParseTreeWalker();
        SymbolTable symbolTable = new SymbolTable();

        // Before running the actual assembler, set up the file
        String fileName = inputFile.split("\\.")[0] + ".hack";


        walker.walk(symbolTable, tree);
        walker.walk(new Code(symbolTable, fileName), tree);

    }
}
