package JackCompiler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static JackCompiler.JackTokenizer.TokenType.*;
import static JackCompiler.JackTokenizer.Keyword.*;

public class CompilationEngine {

    private final FileWriter fileWriter;
    private final JackTokenizer tokenizer;
    private final ArrayList<String> tokenCache;

    int indentLevel = 0;
    final int INDENT_INCREMENT = 2;

    String errorMsg = "Syntax error! Expected token of type %s %s but got %s token '%s'";

    public CompilationEngine(String fileName) {
        try {
            String programDirWithoutExtension = fileName.split("\\.")[0];
            fileWriter = new FileWriter(programDirWithoutExtension + ".xml");
            tokenCache = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Error: Could not set up file to write in. Please try again. ");
        }

        tokenizer = new JackTokenizer(fileName);
        tokenizer.advance();
    }

    public void close() {
        tokenizer.close();
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error: could not close file!");
        }
    }

    private void writeXML(String tag, String content) {
        writeRaw(" ".repeat(indentLevel) + "<%s> %s </%s>".formatted(tag, content, tag));
    }

    private void openXMLTag(String tag) {
        writeRaw(" ".repeat(indentLevel) + "<%s>".formatted(tag));
        indentLevel += INDENT_INCREMENT;
    }

    private void closeXMLTag(String tag) {
        indentLevel -= INDENT_INCREMENT;
        writeRaw(" ".repeat(indentLevel) + "</%s>".formatted(tag));
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

    private String getTokenVal() {
        String tokenValue = "";
        JackTokenizer.TokenType tokenType = tokenizer.tokenType();
        switch (tokenType) {
            case SYMBOL -> {
                tokenValue = tokenizer.symbol();
            }
            case INT_CONST -> {
                tokenValue = String.valueOf(tokenizer.intVal());
            }
            case STRING_CONST -> {
                tokenValue = tokenizer.stringVal();
            }
            case IDENTIFIER -> {
                tokenValue = tokenizer.identifier();
            }
            case KEYWORD -> {
                tokenValue = String.valueOf(tokenizer.keyWord()).toLowerCase();
            }
        }
        return tokenValue;

    }

    private void processSymbol(String symbolToCompare) {
        boolean condition = getTokenVal().equals(symbolToCompare);
        if (!condition) {
            throw new RuntimeException(errorMsg.formatted(SYMBOL, symbolToCompare, tokenizer.tokenType(), getTokenVal()));
        }
        process(SYMBOL, true);
    }

    private void processSymbol(String... allowedSymbols) {
        boolean condition = Arrays.asList(allowedSymbols).contains(getTokenVal());

        if (!condition || tokenizer.tokenType() != SYMBOL) {
            throw new RuntimeException(errorMsg.formatted(SYMBOL, "(e.g. %s)".formatted(Arrays.toString(allowedSymbols)), tokenizer.tokenType(), getTokenVal()));
        }
        process(SYMBOL, true);
    }


    private void processKeyword(JackTokenizer.Keyword keyword) {
        process(KEYWORD, tokenizer.keyWord() == keyword);
    }

    private void processKeyword(JackTokenizer.Keyword... allowedValues) {
        if (tokenizer.tokenType() != KEYWORD) {
            throw new RuntimeException(errorMsg.formatted(KEYWORD, "(e.g. %s)".formatted(Arrays.toString(allowedValues)), tokenizer.tokenType(), getTokenVal()));
        }
        boolean condition = Arrays.asList(allowedValues).contains(tokenizer.keyWord());
        process(KEYWORD, condition);
    }

    private void processIdentifier() {
        process(IDENTIFIER, true);
    }

    private void processIntegerConstant() {
        process(INT_CONST, true);
    }

    private void processStringConstant() {
        process(STRING_CONST, true);
    }

    private void process(JackTokenizer.TokenType tokenType, boolean condition) {
        // this is just for the corner case of token lookahead in term compilation
        if (!tokenCache.isEmpty()) {
            // process the identifier that was there
            String identifierToken = tokenCache.getFirst();
            tokenCache.removeFirst();

            writeXML("identifier", identifierToken);
            return;
        }

        if (tokenizer.tokenType() == tokenType && condition) {
            String tokenValue = "";
            String xmlTagName = "";
            switch (tokenType) {
                case SYMBOL -> {
                    tokenValue = tokenizer.symbol();
                    xmlTagName = "symbol";
                }
                case INT_CONST -> {
                    tokenValue = String.valueOf(tokenizer.intVal());
                    xmlTagName = "integerConstant";
                }
                case STRING_CONST -> {
                    tokenValue = tokenizer.stringVal();
                    xmlTagName = "stringConstant";
                }
                case IDENTIFIER -> {
                    tokenValue = tokenizer.identifier();
                    xmlTagName = "identifier";
                }
                case KEYWORD -> {
                    tokenValue = String.valueOf(tokenizer.keyWord()).toLowerCase();
                    xmlTagName = "keyword";
                }
            }
            writeXML(xmlTagName, tokenValue);
            tokenizer.advance();
        } else {
            throw new RuntimeException(errorMsg.formatted(tokenType, "", tokenizer.tokenType(), getTokenVal()));
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
        openXMLTag("subroutineDec");

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

        closeXMLTag("subroutineDec");
    }

    public void compileParameterList() {
        openXMLTag("parameterList");

        // If it's an empty parameter list
        if (tokenizer.tokenType() == SYMBOL && tokenizer.symbol().equals(")")) {
            closeXMLTag("parameterList");
            return;
        }

        // Get the first variable
        compileType();
        processIdentifier();

        while (tokenizer.tokenType() == SYMBOL && tokenizer.symbol().equals(",")) {
            processSymbol(",");
            compileType();
            processIdentifier();
        }

        closeXMLTag("parameterList");
    }

    public void compileSubroutineBody() {
        openXMLTag("subroutineBody");

        processSymbol("{");

        while (tokenizer.tokenType() == KEYWORD && tokenizer.keyWord() == VAR) {
            compileVarDec();
        }

        compileStatements();

        processSymbol("}");

        closeXMLTag("subroutineBody");
    }

    public void compileVarDec() {
        openXMLTag("varDec");

        processKeyword(VAR);
        compileType();
        processIdentifier();

        // If there are more variables
        while (tokenizer.tokenType() == SYMBOL && tokenizer.symbol().equals(",")) {
            processSymbol(",");
            processIdentifier();
        }

        processSymbol(";");

        closeXMLTag("varDec");
    }

    public void compileStatements() {
        openXMLTag("statements");
        while (tokenizer.tokenType() == KEYWORD && List.of(LET, IF, WHILE, DO, RETURN).contains(tokenizer.keyWord())) {
            switch (tokenizer.keyWord()) {
                case LET -> {
                    compileLet();
                }
                case IF-> {
                    compileIf();
                }
                case WHILE -> {
                    compileWhile();
                }
                case DO -> {
                    compileDo();
                }
                case RETURN -> {
                    compileReturn();
                }
            }
        }
        closeXMLTag("statements");
    }

    public void compileLet() {
        openXMLTag("letStatement");

        processKeyword(LET);
        processIdentifier();
        if (tokenizer.tokenType() == SYMBOL && tokenizer.symbol().equals("[")) {
            processSymbol("[");
            compileExpression();
            processSymbol("]");
        }

        processSymbol("=");

        compileExpression();

        processSymbol(";");

        closeXMLTag("letStatement");
    }

    public void compileIf() {
        openXMLTag("ifStatement");

        processKeyword(IF);

        processSymbol("(");
        compileExpression();
        processSymbol(")");

        processSymbol("{");
        compileStatements();
        processSymbol("}");

        if (tokenizer.tokenType() == KEYWORD && tokenizer.keyWord() == ELSE) {
            processKeyword(ELSE);
            processSymbol("{");
            compileStatements();
            processSymbol("}");
        }


        closeXMLTag("ifStatement");
    }

    public void compileWhile() {
        openXMLTag("whileStatement");
        processKeyword(WHILE);

        processSymbol("(");
        compileExpression();
        processSymbol(")");

        processSymbol("{");
        compileStatements();
        processSymbol("}");

        closeXMLTag("whileStatement");
    }

    public void compileDo() {
        openXMLTag("doStatement");

        processKeyword(DO);

        compileExpression();

        processSymbol(";");

        closeXMLTag("doStatement");
    }

    public void compileReturn() {
        openXMLTag("returnStatement");

        processKeyword(RETURN);

        if (!(tokenizer.tokenType() == SYMBOL && tokenizer.symbol().equals(";"))) {
            compileExpression();
        }

        processSymbol(";");

        closeXMLTag("returnStatement");
    }

    List<String> ops = List.of("+", "-", "*", "/", "&amp;", "|", "&lt;", "&gt;", "=");

    public void compileExpression() {
        openXMLTag("expression");

        compileTerm();
        while (tokenizer.tokenType() == SYMBOL && ops.contains(tokenizer.symbol())) {
            processSymbol(ops.toArray(new String [0]));
            compileTerm();
        }

        closeXMLTag("expression");
    }

    public void compileTerm() {
        openXMLTag("term");

        switch (tokenizer.tokenType()) {
            case INT_CONST -> {
                processIntegerConstant();
            }
            case STRING_CONST -> {
                processStringConstant();
            }
            case SYMBOL -> {
                if (tokenizer.symbol().equals("-")) {
                    processSymbol("-");
                    compileTerm();
                } else if (tokenizer.symbol().equals("~")) {
                    processSymbol("~");
                    compileTerm();
                } else if (tokenizer.symbol().equals("(")) {
                    processSymbol("(");
                    compileExpression();
                    processSymbol(")");
                }
            }
            case IDENTIFIER -> {
                tokenCache.add(tokenizer.identifier());
                tokenizer.advance();

                // Token lookahead!
                // If it's a [, it's an array access
                // If it's a ( or ., it's a subroutine call
                if (tokenizer.tokenType() == SYMBOL) {
                    if (tokenizer.symbol().equals("[")) {
                        processIdentifier();
                        processSymbol("[");
                        compileExpression();
                        processSymbol("]");
                    } else if (tokenizer.symbol().equals("(") || tokenizer.symbol().equals(".")) {
                        compileSubroutineCall();
                    } else {
                        processIdentifier();
                    }
                }
            }

            case KEYWORD -> {
                process(KEYWORD, List.of(TRUE, FALSE, NULL, THIS).contains(tokenizer.keyWord()));
            }
        }

        closeXMLTag("term");
    }

    public int compileExpressionList() {
        openXMLTag("expressionList");
        // If it's an empty list, return 0.
        if (tokenizer.tokenType() == SYMBOL && tokenizer.symbol().equals(")")) {
            closeXMLTag("expressionList");
            return 0;
        }

        int counter = 0;
        compileExpression();
        while (tokenizer.tokenType() == SYMBOL && tokenizer.symbol().equals(",")) {
            processSymbol(",");
            compileExpression();
            counter++;
        }

        closeXMLTag("expressionList");
        return counter;
    }

    private void compileType() {
        if (tokenizer.tokenType() == KEYWORD && List.of(INT, CHAR, BOOLEAN).contains(tokenizer.keyWord())) {
            processKeyword(INT, CHAR, BOOLEAN);

        } else if (tokenizer.tokenType() == IDENTIFIER) {
            processIdentifier();
        }
    }

    private void compileSubroutineCall() {
        processIdentifier();

        if (tokenizer.tokenType() != SYMBOL) {
            throw new RuntimeException("Syntax error!");
        }

        if (tokenizer.symbol().equals(".")) {
            processSymbol(".");
            processIdentifier();
        }

        processSymbol("(");
        compileExpressionList();
        processSymbol(")");

    }


}
