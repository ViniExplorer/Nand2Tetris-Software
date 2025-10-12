package JackCompiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JackTokenizerTester {

    private static ArrayList<File> filesToTranslate;
    private static FileWriter fileWriter;

    public static void main(String[] args) throws IOException {
        setup(args);

        for (File currentFile : filesToTranslate) {

            String filePath = currentFile.getPath();
            System.out.println(filePath);

            String programDirWithoutExtension = filePath.split("\\.")[0];
            String translatedProgramDir = programDirWithoutExtension + "T.xml";

            JackTokenizer tokenizer = new JackTokenizer(filePath);

            fileWriter = new FileWriter(translatedProgramDir);

            fileWriter.write("<tokens>\n");

            while (tokenizer.hasMoreTokens()) {
                tokenizer.advance();
                switch (tokenizer.tokenType()) {
                    case SYMBOL -> writeXML("symbol", String.valueOf(tokenizer.symbol()));
                    case KEYWORD -> writeXML("keyword", String.valueOf(tokenizer.keyWord()).toLowerCase());
                    case INT_CONST -> writeXML("integerConstant", String.valueOf(tokenizer.intVal()));
                    case STRING_CONST -> writeXML("stringConstant", tokenizer.stringVal());
                    case IDENTIFIER -> writeXML("identifier", tokenizer.identifier());
                }

            }

            fileWriter.write("</tokens>\n");
            fileWriter.close();
        }
    }

    private static void writeXML(String tag, String content) {
        try {
            String xmlToWrite = "  <%s> %s </%s>\n".formatted(tag, content, tag);
            fileWriter.write(xmlToWrite);
            System.out.println(xmlToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setup(String[] args) {

        // DEBUG
        // System.out.println(newFileDir);

        // Work out whether args[0] is a file or folder
        File pathOfProgram = new File(args[0]);
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

                        // If the file is a jack file
                        if (filename.endsWith(".jack")) {
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
