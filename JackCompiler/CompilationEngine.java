package JackCompiler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static JackCompiler.JackTokenizer.TokenType.*;
import static JackCompiler.JackTokenizer.Keyword.*;

public class CompilationEngine {

    private FileWriter fileWriter;
    private JackTokenizer tokenizer;

    int indentLevel = 0;
    final int INDENT_INCREMENT = 4;

    public CompilationEngine(String fileName, JackTokenizer jackTokenizer) {
        try {
            fileWriter = new FileWriter(fileName);
        } catch (IOException e) {
            System.out.println("Error: Could not set up file to write in. Please try again. ");
        }

        tokenizer = jackTokenizer;
    }

    private void writeXML(String tag, String content) {
        writeRaw(" ".repeat(indentLevel) + "<%s> %s </%s>".formatted(tag, content, tag));
    }

    private void openXMLTag(String tag) {
        writeRaw("<%s>".formatted(tag));
        indentLevel += INDENT_INCREMENT;
    }

    private void closeXMLTag(String tag) {
        writeRaw("</%s>".formatted(tag));
        indentLevel -= INDENT_INCREMENT;
    }

    private void writeRaw(String content) {
        try {
            String xmlToWrite = "%s\n".formatted(content);
            fileWriter.write(xmlToWrite);
            System.out.println(xmlToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    private void processSymbol(String symbolToCompare) {
        if (tokenizer.tokenType() == SYMBOL && tokenizer.symbol().equals(symbolToCompare)) {
            writeXML("symbol", symbolToCompare);
            tokenizer.advance();
        } else {
            throw new RuntimeException("Syntax error!");
        }
    }

    private void processKeyword(JackTokenizer.Keyword keyword) {
        if (tokenizer.tokenType() == KEYWORD && tokenizer.keyWord() == keyword) {
            writeXML("keyword", keyword.toString().toLowerCase());
            tokenizer.advance();
        } else {
            throw new RuntimeException("Syntax error!");
        }
    }

    private void processKeyword(JackTokenizer.Keyword... allowedValues) {
        if (Arrays.asList(allowedValues).contains(tokenizer.keyWord())) {
            processKeyword(tokenizer.keyWord());
        } else {
            throw new RuntimeException("Syntax error!");
        }
    }

    private void processIdentifier() {
        if (tokenizer.tokenType() == IDENTIFIER) {
            writeXML("identifier", tokenizer.identifier());
            tokenizer.advance();
        } else {
            throw new RuntimeException("Syntax error!");
        }
    }

    public void compileClass() {
        openXMLTag("class");

        processKeyword(CLASS);

        processIdentifier();

        processSymbol("{");

        while (tokenizer.tokenType() == KEYWORD
                && List.of(STATIC, FIELD).contains(tokenizer.keyWord())) {
            compileClassVarDec();
        }

        // If it's the start of a subroutine declaration
        while (tokenizer.tokenType() == KEYWORD
                && (List.of(CONSTRUCTOR, FUNCTION, METHOD).contains(tokenizer.keyWord()))) {
            compileSubroutine();
        }

        processSymbol("}");

        closeXMLTag("class");
    }

    public void compileClassVarDec() {
        openXMLTag("classVarDec");

        processKeyword(STATIC, FIELD);

        compileType();

        processIdentifier();

        while (tokenizer.tokenType() == SYMBOL && tokenizer.symbol().equals(",")) {
            processSymbol(",");
            processIdentifier();
        }

        processSymbol(";");

        closeXMLTag("classVarDec");
    }

    public void compileSubroutine() {
        processKeyword(CONSTRUCTOR, FUNCTION, METHOD);

        if (tokenizer.tokenType() == KEYWORD && tokenizer.keyWord() == VOID) {
            processKeyword(VOID);
        } else if (tokenizer.tokenType() == IDENTIFIER) {
            processIdentifier();
        }

        processIdentifier();

        processSymbol("(");

        compileParameterList();

        processSymbol(")");

        compileSubroutineBody();

    }

    public void compileParameterList() {

    }

    public void compileSubroutineBody() {

    }

    public void compileVarDec() {

    }

    public void compileStatements() {

    }

    public void compileLet() {

    }

    public void compileIf() {

    }

    public void compileWhile() {

    }

    public void compileDo() {

    }

    public void compileReturn() {

    }

    public void compileExpression() {

    }

    public void compileTerm() {

    }

    public int compileExpressionList() {
        return 0;
    }

    private void compileType() {
        if (tokenizer.tokenType() == KEYWORD && List.of(INT, CHAR, BOOLEAN).contains(tokenizer.keyWord())) {
            processKeyword(INT, CHAR, BOOLEAN);

        } else if (tokenizer.tokenType() == IDENTIFIER) {
            processIdentifier();
        }
    }



}
