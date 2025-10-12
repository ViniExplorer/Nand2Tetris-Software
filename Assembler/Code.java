import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class Code extends AssemblyBaseListener {

    private final static String C_INSTRUCTION_START = "111";
    private final static String A_INSTRUCTION_START = "0";

    private final static HashMap<String, String> COMP_SYMBOLS = new HashMap<>(Map.ofEntries(
            entry("0","0101010"),
            entry("1","0111111"),
            entry("-1","0111010"),
            entry("D","0001100"),
            entry("A","0110000"),
            entry("!D","0001101"),
            entry("!A","0110001"),
            entry("-D","0001111"),
            entry("-A","0110011"),
            entry("D+1","0011111"),
            entry("A+1","0110111"),
            entry("D-1","0001110"),
            entry("A-1","0110010"),
            entry("D+A","0000010"),
            entry("D-A","0010011"),
            entry("A-D","0000111"),
            entry("D&A","0000000"),
            entry("D|A","0010101"),
            // when a==1
            entry("M","1110000"),
            entry("!M","1110001"),
            entry("-M","1110011"),
            entry("M+1","1110111"),
            entry("M-1","1110010"),
            entry("D+M","1000010"),
            entry("D-M","1010011"),
            entry("M-D","1000111"),
            entry("D&M","1000000"),
            entry("D|M","1010101")
    ));

    private final static HashMap<String, String> DEST_SYMBOLS = new HashMap<>(Map.ofEntries(
            entry("", "000"),
            entry("M","001"),
            entry("D","010"),
            entry("DM","011"),
            entry("A","100"),
            entry("AM","101"),
            entry("AD","110"),
            entry("ADM","111")
    ));

    private final static HashMap<String, String> JUMP_SYMBOLS = new HashMap<>(Map.ofEntries(
            entry("", "000"),
            entry("JGT","001"),
            entry("JEQ","010"),
            entry("JGE","011"),
            entry("JLT","100"),
            entry("JNE","101"),
            entry("JLE","110"),
            entry("JMP","111")
    ));

    private SymbolTable symbolTable;

    PrintStream outFile;

    public Code(SymbolTable symbolTable, String fileName) throws IOException {
        // DEBUG: System.out.println(symbolTable.symbolTable);
        outFile = new PrintStream(fileName);
        this.symbolTable = symbolTable;
    }

    @Override
    public void exitCCommand(AssemblyParser.CCommandContext ctx) {

        String comp = ctx.COMP().getText();

        String dest = "";
        String jump = "";

        if (ctx.DEST() != null) {
            dest = extractDestSymbol(ctx.DEST().getText());
        }

        if (ctx.JMP() != null) {
            jump = extractJumpSymbol(ctx.JMP().getText());
        }

        String destBinary = DEST_SYMBOLS.get(dest);
        String compBinary = COMP_SYMBOLS.get(comp);
        String jumpBinary = JUMP_SYMBOLS.get(jump);

        if (destBinary == null || compBinary == null || jumpBinary == null)
        {
            ArrayList<String> errorComponentsArray = new ArrayList<>();
            if (destBinary == null) {
                errorComponentsArray.add(dest);
            }
            if (compBinary == null) {
                errorComponentsArray.add(ctx.COMP().getText());
            }
            if (jumpBinary == null) {
                errorComponentsArray.add(jump);
            }
            String errorComponents = String.join(",", errorComponentsArray);

            throw new RuntimeException("Error: symbol(s) not found at command " + ctx.getText() + ": " + errorComponents);
        }

        String binaryInstruct = C_INSTRUCTION_START + compBinary + destBinary + jumpBinary;

        outFile.println(binaryInstruct);
    }

    @Override
    public void exitACommand(AssemblyParser.ACommandContext ctx) {

        String addressText = extractAddressSymbol(ctx.ADDRESS().getText());

        int address;

        if (symbolTable.contains(addressText)) {
            address = symbolTable.getAddress(addressText);
        } else {
            address = Integer.parseInt(addressText);
        }

        String binaryAddress = String.format("%15s", Integer.toBinaryString(address)).replace(' ', '0');

        outFile.println(A_INSTRUCTION_START + binaryAddress);
    }

    private String extractAddressSymbol(String text) {
        return text.substring(1);
    }

    @Override
    public void exitInit(AssemblyParser.InitContext ctx) {
        outFile.close();
    }

    private String extractDestSymbol(String text) {
        // Removes the = at the end
        return text.substring(0, text.length() - 1);
    }

    private String extractJumpSymbol(String text) {
        // Removes the ; at the start
        return text.substring(1);
    }
}
