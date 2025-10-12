import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileNotFoundException;

public class ParserTester {

    private static Parser parser;

    public static void main(String[] args) throws FileNotFoundException {
        parser = new Parser("src/MemoryAccess/BasicTest/BasicTest.vm");

        while (parser.hasMoreLines()) {
            parser.advance(); // Will display raw line
            System.out.printf("COMMAND TYPE: %s%n", parser.commandType());

            System.out.print("ARGUMENT 1: ");
            try {
                System.out.printf("%s%n", parser.arg1());
            } catch (Exception e) {
                System.out.println("None");
            }

            System.out.print("ARGUMENT 2: ");
            try {
                System.out.printf("%s%n", parser.arg2());
            } catch (Exception e) {
                System.out.println("None");
            }

            System.out.printf("HAS MORE LINES: %b%n", parser.hasMoreLines());

            System.out.println();
        }
    }


}
