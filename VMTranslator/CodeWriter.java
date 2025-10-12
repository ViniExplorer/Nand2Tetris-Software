import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CodeWriter {

    private final FileWriter fileWriter;
    private int comparison_counter;
    private String fileName;
    private String currentFunction;

    private static final HashMap<String, String> COMMAND_TO_SYMBOL = new HashMap<>(Map.ofEntries(
            Map.entry("add", "+"),
            Map.entry("sub", "-"),
            Map.entry("neg", "-"),
            Map.entry("eq", "EQ"),
            Map.entry("gt", "GT"),
            Map.entry("lt", "LT"),
            Map.entry("and", "&"),
            Map.entry("or", "|"),
            Map.entry("not", "!")
    ));

    private static final HashMap<String, String> SEGMENT_TO_SYMBOL = new HashMap<>(Map.ofEntries(
            Map.entry("local", "LCL"),
            Map.entry("argument", "ARG"),
            Map.entry("this", "THIS"),
            Map.entry("that", "THAT")
    ));

    /*
     * A Hashmap containing the function's name and the index (i.e. number of times it has called a function within
     * itself).
     */
    private final HashMap<String, Integer> returnAddrIndexes;

    public CodeWriter(String programPath) {
        comparison_counter = 0;
        returnAddrIndexes = new HashMap<>();

        String programDirWithoutExtension = programPath.split("\\.")[0];
        String translatedProgramDir = programDirWithoutExtension + ".asm";

        try {
            fileWriter = new FileWriter(translatedProgramDir);
        } catch (IOException e) {
            throw new RuntimeException("Error: Could not create or load file. Please try again.\n" + e);
        }

        // Write initial bootstrap code
        writeAssembly(
                "@256\n" +
                "D=A\n" +
                "@SP\n" +
                "M=D\n" +
                "@Sys.init\n" +
                "0;JMP\n"
        );
    }

    public void setFileName(String fileName) {
        String fileDirWithoutExtension = fileName.split("\\.")[0];

        var dirSections = fileDirWithoutExtension.split("\\\\");

        this.fileName = dirSections[dirSections.length - 1];
    }

    public void writeArithmetic(String command) {

        String assemblyCode = "";
        String commandSymbol = COMMAND_TO_SYMBOL.get(command);

        assemblyCode += popToDReg();

        if (!command.equals("neg") && !command.equals("not")) {
            assemblyCode +=
                    "@R13\n" +
                    "M=D\n" +
                    popToDReg();
        }

        if (List.of("add", "sub", "and", "or").contains(command)) {
            assemblyCode +=
                    // Do operation D op R13 and store on D register
                    "@R13\n" +
                            "D=D%sM\n".formatted(commandSymbol) +
                            // Store it on top of the stack
                            "@SP\n" +
                            "A=M\n" +
                            "M=D\n";

        } else if (List.of("neg", "not").contains(command)) {
            assemblyCode +=
                    // Do operation on D register's value
                    "D=%sD\n".formatted(commandSymbol) +
                            // Store it back on the stack
                            "M=D\n";

        } else if (List.of("eq", "gt", "lt").contains(command)) {
            assemblyCode +=
                    // Do operation D - R13 and store on D register
                    "@R13\n" +
                            "D=D-M\n" +
                /* If it's more than 0, then we jump to the code that sets RAM[SP] to -1
                   x is the index for that comparison (each time we make a new comparison,
                   we have to generate this code again. To not cause confusion and make it jump
                   into a completely different part of the program, we must make an index each time
                   we generate it) */
                            "@push_true_%d\n".formatted(comparison_counter) +
                            "D;J%s\n".formatted(commandSymbol) +
                            "@SP\n" +
                            "A=M\n" +
                            "M=0\n" +
                            "@continue_%d\n".formatted(comparison_counter) +
                            "0;JMP\n" +
                            "(push_true_%d)\n".formatted(comparison_counter) +
                            "@SP\n" +
                            "A=M\n" +
                            "M=-1\n" +
                            "(continue_%d)\n".formatted(comparison_counter);

            comparison_counter++;

        } else {
            throw new RuntimeException("Error: Command not recognised. Please try again.");
        }

        assemblyCode += increaseStackPointer();
        writeAssembly(assemblyCode);
    }

    public void writePushPop(Parser.CommandType commandType, String segment, int index) {
        String assemblyCode = "";

        if (commandType == Parser.CommandType.C_PUSH) {
            assemblyCode += pushInstructs(segment, index);

        } else if (commandType == Parser.CommandType.C_POP) {
            assemblyCode += popInstructs(segment, index);
        } else {
            throw new RuntimeException("Error: Command type must be C_PUSH or C_POP. Please try again.");
        }

        writeAssembly(assemblyCode);
    }

    public void writeLabel(String label) {
        writeAssembly("(%s)\n".formatted(getLabelSymbol(label)));
    }

    public void writeGoto(String label) {
        writeAssembly("@%s\n".formatted(getLabelSymbol(label)) +
                "0;JMP\n");
    }

    public void writeIf(String label) {
        writeAssembly(popToDReg() +
                "@%s\n".formatted(getLabelSymbol(label)) +
                "D;JNE\n");
    }

    public void writeFunction(String functionName, int nVars) {
        if (!functionName.contains(".")) {
            functionName = fileName + "." + functionName;
        }

        currentFunction = functionName;

        String setupLoopLabel = functionName + ".setup";

        writeAssembly(
                "(%s)\n".formatted(functionName)
        );

        if (nVars > 0) {
            writeAssembly(
                    // Counter for number of iterations
                    "@R13\n" +
                    "M=0\n" +
                    // Store nVars in R14
                    "@%d\n".formatted(nVars) +
                    "D=A\n" +
                    "@R14\n" +
                    "M=D\n" +
                    // LOOP FOR SETUP
                    "(%s)\n".formatted(setupLoopLabel)
            );

            // The setup simply pushes 0 n times to the stack to act as the local variables
            writePushPop(Parser.CommandType.C_PUSH, "constant", 0);

            writeAssembly(
                    "@R13\n" +
                            "M=M+1\n" +
                            "D=M\n" +
                            "@R14\n" +
                            "D=D-M\n" +
                            "@%s\n".formatted(setupLoopLabel) +
                            "D;JLT\n"
            );
        }
    }

    public void writeCall(String functionName, int nArgs) {
        /* If the function being called has a . specified in its name, we know it is from another file. Otherwise,
         * we assume that it is in the current file and add the prefix 'fileName.' to the address of the function */
        if (!functionName.contains(".")) {
            functionName = fileName + "." + functionName;
        }

        // Get index of return address for that function.
        // Default is 0, and then it's added to the hashmap returnAddrIndexes
        int callIndex = 0;
        if (returnAddrIndexes.containsKey(currentFunction)) {
            callIndex = returnAddrIndexes.get(currentFunction);
        }
        // Update callIndex by incrementing it by 1
        returnAddrIndexes.put(currentFunction, callIndex + 1);

        // String returnAddr = "%s.%s$ret.%d".formatted(fileName, functionName, callIndex);
        String returnAddr = currentFunction + "$" + "ret.%d".formatted(callIndex);

        String functionAddr = functionName;

        String assemblyCode =
                // Push return address
                "@%s\n".formatted(returnAddr) +
                "D=A\n" +
                pushDReg();

        List<String> registers = List.of("LCL", "ARG", "THIS", "THAT");

        for (String currentReg : registers) {
            assemblyCode +=
                    "@%s\n".formatted(currentReg) +
                    "D=M\n" +
                    pushDReg();
        }

        assemblyCode +=
                // ARG = SP-5-nArgs
                "@SP\n" +
                "D=M\n" +
                "@5\n" +
                "D=D-A\n" +
                "@%d\n".formatted(nArgs) +
                "D=D-A\n" +
                "@ARG\n" +
                "M=D\n" +
                // LCL = SP
                "@SP\n" +
                "D=M\n" +
                "@LCL\n" +
                "M=D\n" +
                // goto f
                // if there's a dot, meaning a fileName is specified,
                // this address is specifiedFile.f, otherwise it's fileName.f,
                // where fileName is the current file's name
                "@%s\n".formatted(functionAddr) +
                "0;JMP\n" +
                "(%s)\n".formatted(returnAddr);

        writeAssembly(assemblyCode);

    }

    public void writeReturn() {
        String assemblyCode =
                // frame = LCL
                // frame is stored in R13
                "@LCL\n" +
                "D=M\n" +
                "@R13\n" +
                "M=D\n" +
                // retAddr = *(frame-5)
                // retAddr is stored in R14
                "@5\n" +
                "D=D-A\n" +
                "A=D\n" +
                "D=M\n" +
                "@R14\n" +
                "M=D\n" +
                // *ARG = pop()
                popToDReg() +
                "@ARG\n" +
                "A=M\n" +
                "M=D\n" +
                // SP = ARG+1
                "@ARG\n" +
                "D=M+1\n" +
                "@SP\n" +
                "M=D\n";

        List<String> registers = List.of("THAT", "THIS", "ARG", "LCL");
        int sub = 1;

        for (String currentReg : registers) {
            assemblyCode +=
                    "@R13\n" +
                    "D=M\n" +
                    "@%d\n".formatted(sub) +
                    "D=D-A\n" +
                    "A=D\n" +
                    "D=M\n" +
                    "@%s\n".formatted(currentReg) +
                    "M=D\n";
            // Increment sub at the end
            sub++;
        }

        // goto retAddr
        assemblyCode +=
                "@R14\n" +
                "A=M\n" +
                "0;JMP\n";

        writeAssembly(assemblyCode);
    }


    public void close() {
        //        // INFINITE LOOP AT THE END
        //        writeAssembly("(INFINITE_END)\n" +
        //                "@INFINITE_END\n" +
        //                "0;JMP\n");

        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error: Could not close file. Please try again.");
        }
    }

    private String pushInstructs(String segment, int index) {

        String assemblyCode = "";

        // FOR STORING VALUE ON D REGISTER BEFORE PUSHING IT ON THE STACK
        // For local, argument, this and that
        if (SEGMENT_TO_SYMBOL.containsKey(segment)) {

            String symbolSegment = SEGMENT_TO_SYMBOL.get(segment);
            assemblyCode +=
                    // Work out address of 'segment index'
                    "@%s\n".formatted(symbolSegment) +
                            "D=M\n" +
                            "@%d\n".formatted(index) +
                            "D=D+A\n" +
                            // Go to 'segment index' and store its value on D register
                            "A=D\n" +
                            "D=M\n";

        } else if (segment.equals("pointer")) {

            if (index == 0) {
                assemblyCode +=
                        "@THIS\n" +
                                "D=M\n";
            } else if (index == 1) {
                assemblyCode +=
                        "@THAT\n" +
                                "D=M\n";
            } else {
                throw new RuntimeException("Error: index must be 0 or 1 for segment 'pointer'. Please try again.");
            }

        } else if (segment.equals("temp")) {

            if (index >= 0 && index <= 7) {
                assemblyCode +=
                        "@%d\n".formatted(5 + index) +
                                "D=M\n";
            } else {
                throw new RuntimeException("Error: index must be between 0 and 7 for segment 'temp'. Please try again.");
            }

        } else if (segment.equals("constant")) {

            assemblyCode +=
                    "@%d\n".formatted(index) +
                            "D=A\n";

        } else if (segment.equals("static")) {

            assemblyCode +=
                    "@%s.%d\n".formatted(fileName, index) +
                            "D=M\n";

        }

        assemblyCode +=
                // Store D register's value on top of stack
                "@SP\n" +
                        "A=M\n" +
                        "M=D\n" +
                        increaseStackPointer();

        return assemblyCode;
    }

    private String popInstructs(String segment, int index) {
        String assemblyCode = "";

        if (SEGMENT_TO_SYMBOL.containsKey(segment)) {

            String symbolSegment = SEGMENT_TO_SYMBOL.get(segment);
            assemblyCode +=
                    // Work out address of 'segment index'
                    "@%s\n".formatted(symbolSegment) +
                            "D=M\n" +
                            "@%d\n".formatted(index) +
                            "D=D+A\n" +
                            // Store it in R13 as a temporary variable
                            "@R13\n" +
                            "M=D\n" +
                            // Get top of stack on D register
                            popToDReg() +
                            // Go to address of 'segment' index
                            "@R13\n" +
                            "A=M\n";

        } else if (List.of("pointer", "static", "temp").contains(segment)) {

            assemblyCode +=
                    // Get top of stack on D register
                    popToDReg();

            switch (segment) {
                case "pointer" -> {

                    if (index == 0) {
                        assemblyCode +=
                                "@THIS\n";
                    } else if (index == 1) {
                        assemblyCode +=
                                "@THAT\n";
                    } else {
                        throw new RuntimeException("Error: index must be 0 or 1 for segment 'pointer'. Please try again.");
                    }
                }
                case "static" -> assemblyCode += "@%s.%d\n".formatted(fileName, index);
                case "temp" -> {
                    if (index >= 0 && index <= 7) {
                        assemblyCode += "@%d\n".formatted(5 + index);
                    } else {
                        throw new RuntimeException("Error: index must be between 0 and 7 for segment 'temp'. Please try again.");
                    }
                }
            }

        }

        assemblyCode +=
                // Set it to the D register's value
                "M=D\n";

        return assemblyCode;
    }

    private String increaseStackPointer() {
        return "@SP\n" +
                "M=M+1\n";
    }

    // Pushes the value of the D register onto the stack
    private String pushDReg() {
        return "@SP\n" +
               "A=M\n" +
               "M=D\n" +
                increaseStackPointer();
    }

    // Pops the top value on the stack into the D register
    private String popToDReg() {
        return "@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "M=0\n";
    }

    private String getLabelSymbol(String label) {
        if (currentFunction.contains(".")) {
            return currentFunction + "$" + label;
        }
        return fileName + "." + currentFunction + "$" + label;
    }

    private void writeAssembly(String assemblyCode) {
        try {
            fileWriter.write(assemblyCode);
        } catch (IOException e) {
            throw new RuntimeException("Error: Could not write to file. Please try again.");
        }
    }



}
