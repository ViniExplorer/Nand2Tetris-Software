package JackCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class JackAnalyser {

    private static ArrayList<File> filesToTranslate;

    public static void main(String[] args) throws FileNotFoundException {

        setup(args);

        for (File currentFile : filesToTranslate) {
            CompilationEngine compilationEngine = new CompilationEngine(currentFile.getAbsolutePath());
            compilationEngine.compileClass();
            compilationEngine.close();
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

                        // If the file is a VM file
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

