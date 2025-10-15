package JackCompiler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JackTokenizer {

    private final FileReader fileReader;
    private String currentToken;
    private TokenType currentTokenType;
    private char currentChar;

    public enum TokenType {
        KEYWORD,
        SYMBOL,
        IDENTIFIER,
        INT_CONST,
        STRING_CONST
    }

    public enum Keyword {
        CLASS,
        CONSTRUCTOR,
        FUNCTION,
        METHOD,
        FIELD,
        STATIC,
        VAR,
        INT,
        CHAR,
        BOOLEAN,
        VOID,
        TRUE,
        FALSE,
        NULL,
        THIS,
        LET,
        DO,
        IF,
        ELSE,
        WHILE,
        RETURN
    }

    public static final String SYMBOLS = "{}()[].,;+-*/&|<>=~";

    public static final HashMap<Character, String> XML_EQUIVALENTS = new HashMap<>(Map.ofEntries(
            Map.entry('<', "&lt;"),
            Map.entry('>', "&gt;"),
            Map.entry('"', "&quot;"),
            Map.entry('&', "&amp;")
    ));

    public static final List<String> KEYWORDS = List.of(
            "class",
            "constructor",
            "function",
            "method",
            "field",
            "static",
            "var",
            "int",
            "char",
            "boolean",
            "void",
            "true",
            "false",
            "null",
            "this",
            "let",
            "do",
            "if",
            "else",
            "while",
            "return"
    );

    private static final String typeMismatchError = "Error: Token '%s' is not a %s. Please try again. ";

    private String cache;

    public JackTokenizer(String fileName) {
        try {
            fileReader = new FileReader(fileName);
            getCurrentChar();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error: Could not load file. Please try again. ");
        }
    }

    public void close() {
        try {
            fileReader.close();
        } catch (IOException e) {
            throw new RuntimeException("Error: could not close file!");
        }
    }

    public boolean hasMoreTokens() {
        skipToNextToken();
        return isOpen;
    }

    public void advance()  {
        // This is a do while as after we've advanced beyond the end of a token (as is always the case),
        // when advance() is called again, we will skip a character in the tokenizing process
        do {
            skipToNextToken();

            // There's a possibility this may be a comment
            if (SYMBOLS.contains(String.valueOf(currentChar))) {
                currentToken = String.valueOf(currentChar);
                currentTokenType = TokenType.SYMBOL;
                // Get the next character
                getCurrentChar();
                return;

            } else if (Character.isLetter(currentChar)) {
                processIdentifierOrKeyword();
                return;

            } else if (Character.isDigit(currentChar)) {
                processNumber();
                return;

            } else if (currentChar == '"') {
                processStringConstant();
                return;
            }

        } while (getCurrentChar());
    }

    public TokenType tokenType() {
        return currentTokenType;
    }

    public Keyword keyWord() {
        if (tokenType() == TokenType.KEYWORD) {
            return Keyword.valueOf(currentToken.toUpperCase());
        } else {
            throw new RuntimeException(typeMismatchError.formatted(currentToken, "keyword"));
        }
    }

    public String symbol() {
        if (tokenType() == TokenType.SYMBOL && currentToken.length() == 1) {
            if (XML_EQUIVALENTS.containsKey(currentToken.charAt(0))) return XML_EQUIVALENTS.get(currentToken.charAt(0));
            return currentToken;
        } else {
            throw new RuntimeException(typeMismatchError.formatted(currentToken, "symbol"));
        }
    }

    public String identifier() {
        if (tokenType() == TokenType.IDENTIFIER) {
            return currentToken;
        } else {
            throw new RuntimeException(typeMismatchError.formatted(currentToken, "identifier"));
        }
    }

    public int intVal() {
        if (tokenType() == TokenType.INT_CONST) {
            return Integer.parseInt(currentToken);
        } else {
            throw new RuntimeException(typeMismatchError.formatted(currentToken, "integer constant"));
        }
    }

    public String stringVal() {
        if (tokenType() == TokenType.STRING_CONST) {
            return currentToken;
        } else {
            throw new RuntimeException(typeMismatchError.formatted(currentToken, "string constant"));
        }
    }


    private boolean isOpen = true;
    /** Gets the next character in the file if the file reader
     * is open and if there are remaining characters.
     * Use it within an if statement as it returns whether the character
     * fetching was successful or not. */
    private boolean getCurrentChar() {
        // If we potentially skipped some characters
        if (cache != null) {
            char charToReturn = cache.charAt(0);
            if (cache.length() > 1) {
                cache = cache.substring(1);
            } else {
                cache = null;
            }
            currentChar = charToReturn;
            return true;
        }

        int charCode;
        try {
            if (!isOpen) return false;

            if ((charCode = fileReader.read()) != -1) {
                currentChar = (char) charCode;
                // DEBUG: System.out.println(currentChar);
                return true;
            } else {
                fileReader.close();
                isOpen = false;
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean currentCharIsNewLine() {
        return List.of('\n', '\r', '\u0085', '\u2028', '\u2029').contains(currentChar);
    }

    private void processSingleLineComment() {
        // This will keep getting the current char until it is a newline
        while (getCurrentChar() && !currentCharIsNewLine()) ;
    }

    private void processMultiLineComment() {
        // This will keep getting the current char until it and the next char form the string "*/"
        while (getCurrentChar()) {
            if (currentChar == '*') {
                if (getCurrentChar() && currentChar == '/') {
                    break;
                }
            }
        }

    }

    private void processIdentifierOrKeyword() {
        StringBuilder newToken = new StringBuilder(String.valueOf(currentChar));

        // Advance and add new characters until this is no longer the case
        while (getCurrentChar() && (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
            newToken.append(currentChar);
        }

        currentToken = newToken.toString();

        if (KEYWORDS.contains(currentToken)) {
            currentTokenType = TokenType.KEYWORD;
        } else {
            currentTokenType = TokenType.IDENTIFIER;
        }

    }

    private void processNumber() {
        StringBuilder newToken = new StringBuilder(String.valueOf(currentChar));

        // Advance and add new characters until this is no longer the case
        while (getCurrentChar() && Character.isDigit(currentChar)) {
            newToken.append(currentChar);
        }

        currentToken = newToken.toString();
        currentTokenType = TokenType.INT_CONST;
    }

    private void processStringConstant() {
        StringBuilder newToken = new StringBuilder();

        // Advance and add new characters until this is no longer the case
        while (getCurrentChar() && currentChar != '"') {
            if (currentCharIsNewLine()) {
                throw new RuntimeException("Error: String constants must NOT contain a newline. Please try again.");
            }
            newToken.append(currentChar);
        }

        getCurrentChar(); // Advance beyond the ending "

        currentToken = newToken.toString();
        currentTokenType = TokenType.STRING_CONST;
    }

    private void skipToNextToken() {
        // This is not a do while as if you called it multiple times, and it was a do-while,
        // you'd be skipping characters and make a further, unnecessary use of the cache.
        while (isOpen && (skippable() || currentChar == '/')) {

            // There's a possibility this may be a comment
            if (currentChar == '/') {
                if (getCurrentChar()) {
                    cache = "/" + currentChar;
                    if (currentChar == '/') {
                        cache = null;
                        processSingleLineComment();
                    } else if (currentChar == '*') {
                        cache = null;
                        processMultiLineComment();
                    } else {
                        // Revert current char to / and add the rest to the cache
                        currentChar = cache.charAt(0);
                        cache = cache.substring(1);
                        return;
                    }
                }
            }

            // Get next character;
            getCurrentChar();
        }
    }

    private boolean skippable() {
        return currentCharIsNewLine() || Character.isWhitespace(currentChar);
    }

}
