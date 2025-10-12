import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Parser {

    public enum CommandType {
        C_ARITHMETIC,
        C_PUSH,
        C_POP,
        C_LABEL,
        C_GOTO,
        C_IF,
        C_FUNCTION,
        C_RETURN,
        C_CALL
    }


    // TODO: Add branching and function commands
    public static final HashMap<String, CommandType> COMMAND_LOOKUP = new HashMap<>(Map.ofEntries(
            Map.entry("add", CommandType.C_ARITHMETIC),
            Map.entry("sub", CommandType.C_ARITHMETIC),
            Map.entry("neg", CommandType.C_ARITHMETIC),
            Map.entry("eq", CommandType.C_ARITHMETIC),
            Map.entry("gt", CommandType.C_ARITHMETIC),
            Map.entry("lt", CommandType.C_ARITHMETIC),
            Map.entry("and", CommandType.C_ARITHMETIC),
            Map.entry("or", CommandType.C_ARITHMETIC),
            Map.entry("not", CommandType.C_ARITHMETIC),
            Map.entry("push", CommandType.C_PUSH),
            Map.entry("pop", CommandType.C_POP),
            Map.entry("label", CommandType.C_LABEL),
            Map.entry("goto", CommandType.C_GOTO),
            Map.entry("if-goto", CommandType.C_IF),
            Map.entry("call", CommandType.C_CALL),
            Map.entry("function", CommandType.C_FUNCTION),
            Map.entry("return", CommandType.C_RETURN)
    ));

    private class Line {

        private final CommandType commandType;
        private final String command;
        private final String[] args;

        public Line(CommandType commandType, String command, String[] args) {
            this.commandType = commandType;
            this.command = command;
            this.args = args;
        }

        public CommandType getCommandType() {
            return commandType;
        }

        public String getCommand() {
            return command;
        }

        public String getArg(int index) {
            return args[index];
        }
    }

    ///////////// FIELDS //////////////
    private Scanner lineReader;
    private Line currentLine;

    private boolean isClosed = false;

    ///////////// METHODS //////////////

    public Parser(String fileName) throws FileNotFoundException {
        File fileToRead = new File(fileName);
        lineReader = new Scanner(fileToRead);
    }

    public boolean hasMoreLines() {
        if (isClosed) {
            return false;
        } else {
            if (lineReader.hasNextLine()) {
                return true;
            } else {
                lineReader.close();
                isClosed = true;
                return false;
            }
        }
    }

    public void advance() {
        if (hasMoreLines()) {
            String currentRawLine = lineReader.nextLine().trim();

            if (currentRawLine.startsWith("//")) {
                advance();
                return;
            } else if (currentRawLine.isBlank()) {
                advance();
                return;
            }

            String[] components = currentRawLine.split("\\s+");
            CommandType commandType = COMMAND_LOOKUP.get(components[0]);

            String[] args;
            if (components.length > 1) {
                args = Arrays.copyOfRange(components, 1, components.length);
            } else {
                args = new String[0];
            }

            currentLine = new Line(commandType, components[0], args);

            // DEBUG
            // System.out.printf("LINE: %s%n", currentRawLine);
            // System.out.printf("COMMAND: %s%n", components[0]);
        }
    }

    public CommandType commandType() {
        return currentLine.getCommandType();
    }

    public String arg1() {
        if (commandType() == CommandType.C_ARITHMETIC) {
            return currentLine.getCommand();
        } else if (commandType() == CommandType.C_RETURN) {
            throw new RuntimeException("Error: No arguments for command of type C_RETURN.");
        } else {
            return currentLine.getArg(0);
        }
    }

    public int arg2() {
        final List<CommandType> ALLOWED_TYPES = Arrays.asList(
                CommandType.C_PUSH,
                CommandType.C_POP,
                CommandType.C_FUNCTION,
                CommandType.C_CALL
        );

        if (ALLOWED_TYPES.contains(currentLine.getCommandType())) {
            return Integer.parseInt(currentLine.getArg(1));
        } else {
            throw new RuntimeException("Error: Command does not have two arguments.");
        }
    }

}
