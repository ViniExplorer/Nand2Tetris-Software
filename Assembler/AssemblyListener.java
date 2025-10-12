// Generated from Assembly.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AssemblyParser}.
 */
public interface AssemblyListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AssemblyParser#init}.
	 * @param ctx the parse tree
	 */
	void enterInit(AssemblyParser.InitContext ctx);
	/**
	 * Exit a parse tree produced by {@link AssemblyParser#init}.
	 * @param ctx the parse tree
	 */
	void exitInit(AssemblyParser.InitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cCommand}
	 * labeled alternative in {@link AssemblyParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCCommand(AssemblyParser.CCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cCommand}
	 * labeled alternative in {@link AssemblyParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCCommand(AssemblyParser.CCommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lCommand}
	 * labeled alternative in {@link AssemblyParser#command}.
	 * @param ctx the parse tree
	 */
	void enterLCommand(AssemblyParser.LCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lCommand}
	 * labeled alternative in {@link AssemblyParser#command}.
	 * @param ctx the parse tree
	 */
	void exitLCommand(AssemblyParser.LCommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aCommand}
	 * labeled alternative in {@link AssemblyParser#command}.
	 * @param ctx the parse tree
	 */
	void enterACommand(AssemblyParser.ACommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aCommand}
	 * labeled alternative in {@link AssemblyParser#command}.
	 * @param ctx the parse tree
	 */
	void exitACommand(AssemblyParser.ACommandContext ctx);
}