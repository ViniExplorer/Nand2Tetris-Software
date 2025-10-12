import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class SymbolTable extends AssemblyBaseListener {

    public HashMap<String, Integer> symbolTable = new HashMap<>(Map.ofEntries(
            entry("R0", 0),
            entry("R1", 1),
            entry("R2", 2),
            entry("R3", 3),
            entry("R4", 4),
            entry("R5", 5),
            entry("R6", 6),
            entry("R7", 7),
            entry("R8", 8),
            entry("R9", 9),
            entry("R10", 10),
            entry("R11", 11),
            entry("R12", 12),
            entry("R13", 13),
            entry("R14", 14),
            entry("R15", 15),
            entry("SP", 0),
            entry("LCL", 1),
            entry("ARG", 2),
            entry("THIS", 3),
            entry("THAT", 4),
            entry("SCREEN", 16384),
            entry("KBD", 24576)
    ));

    private int lineNum = 0;

    private int varPointer = 16;

    private final ArrayList<String> allLabels;

    public SymbolTable() {
        allLabels = new ArrayList<>();
    }

    // The purpose of this is simply to find all label symbols to prevent them from being added as variables instead
    @Override
    public void enterInit(AssemblyParser.InitContext ctx) {
        ArrayList<AssemblyParser.CommandContext> allCommands = new ArrayList<>(ctx.command());
        for (AssemblyParser.CommandContext cmd : allCommands) {
            if (cmd instanceof AssemblyParser.LCommandContext) {
                String label = ((AssemblyParser.LCommandContext) cmd).LABEL().getText();
                // Remove ( and )
                label = label.substring(1, label.length() - 1);
                allLabels.add(label);
            }
        }
        // DEBUG: System.out.println(allLabels);
    }

    @Override
    public void exitCCommand(AssemblyParser.CCommandContext ctx) {
        lineNum++;
    }

    @Override
    public void exitACommand(AssemblyParser.ACommandContext ctx) {
        // removes the @ at the start
        String address = ctx.ADDRESS().getText().substring(1);

        // Check if the address is not a number
        try {
            Integer.parseInt(address);
        } catch (Exception e) { // If it is not, it might be a variable reference
            if (!contains(address)) {
                // If this is not in a Label command, it is a variable
                if (!allLabels.contains(address)) {
                    addEntry(address, varPointer);
                    varPointer++;
                }
            }
        }
        lineNum++;
    }

    @Override
    public void exitLCommand(AssemblyParser.LCommandContext ctx) {
        String symbol = ctx.LABEL().getText();

        // Eliminate ( and )
        symbol = symbol.substring(1, symbol.length() - 1);

        if (!contains(symbol)) {
            addEntry(symbol, lineNum);
            // System.out.printf("Adding symbol %s with value %d%n", symbol, lineNum + 1);
        }
    }

    public void addEntry(String symbol, int address) {
        symbolTable.put(symbol, address);
    }

    public boolean contains(String symbol) {
        return symbolTable.containsKey(symbol);
    }

    public int getAddress(String symbol) {
        if (contains(symbol)) {
            return symbolTable.get(symbol);
        }
        return -1;
    }

}