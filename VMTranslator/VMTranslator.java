import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class VMTranslator {

    private static Parser parser;
    private static CodeWriter codeWriter;
    private static ArrayList<File> filesToTranslate;

    public static void main(String[] args) throws FileNotFoundException {

        setup(args);

        for (File currentFile : filesToTranslate) {
            codeWriter.setFileName(currentFile.getAbsolutePath());
            parser = new Parser(currentFile.getAbsolutePath());

            while (parser.hasMoreLines()) {
                parser.advance();

                Parser.CommandType commandType = parser.commandType();

                // DEBUG
                // System.out.println(parser.commandType());

                switch (commandType) {

                    case C_PUSH:
                        codeWriter.writePushPop(Parser.CommandType.C_PUSH, parser.arg1(), parser.arg2());
                        break;

                    case C_POP:
                        codeWriter.writePushPop(Parser.CommandType.C_POP, parser.arg1(), parser.arg2());
                        break;

                    case C_ARITHMETIC:
                        codeWriter.writeArithmetic(parser.arg1());
                        break;

                    case C_LABEL:
                        codeWriter.writeLabel(parser.arg1());
                        break;

                    case C_GOTO:
                        codeWriter.writeGoto(parser.arg1());
                        break;

                    case C_IF:
                        codeWriter.writeIf(parser.arg1());
                        break;

                    case C_CALL:
                        codeWriter.writeCall(parser.arg1(), parser.arg2());
                        break;

                    case C_FUNCTION:
                        codeWriter.writeFunction(parser.arg1(), parser.arg2());
                        break;

                    case C_RETURN:
                        codeWriter.writeReturn();
                        break;

                }
            }
        }

        codeWriter.close();

    }


    private static void setup(String[] args) throws FileNotFoundException {

        // DEBUG
        // System.out.println(newFileDir);

        // Work out whether args[0] is a file or folder
        File pathOfProgram = new File(args[0]);
        codeWriter = new CodeWriter(pathOfProgram.getAbsolutePath());
        filesToTranslate = new ArrayList<>();

        if (pathOfProgram.exists()) {

            if (pathOfProgram.isFile()) {
                filesToTranslate.add(pathOfProgram);

            } else {
                File[] filesInProgramPath = pathOfProgram.listFiles();
                assert filesInProgramPath != null;

                for (File file : filesInProgramPath) {

                    if (file.isFile()) {
                        String filename = file.getName();

                        // If the file is a VM file
                        if (filename.endsWith(".vm")) {
                            filesToTranslate.add(file);
                        }
                    }
                }
            }

        } else {
            throw new RuntimeException("Error: could not find program based on path given. Please try again");
        }

    }

}
