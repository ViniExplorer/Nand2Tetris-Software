// Generated from Assembly.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class AssemblyParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DEST=1, COMP=2, JMP=3, LABEL=4, ADDRESS=5, LINE_COMMENT=6, DEST_CHARS=7, 
		JMP_CHARS=8, SYMBOL=9, NUMBER=10, DIGIT=11, NL=12, WS=13;
	public static final int
		RULE_init = 0, RULE_command = 1;
	private static String[] makeRuleNames() {
		return new String[] {
			"init", "command"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "DEST", "COMP", "JMP", "LABEL", "ADDRESS", "LINE_COMMENT", "DEST_CHARS", 
			"JMP_CHARS", "SYMBOL", "NUMBER", "DIGIT", "NL", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Assembly.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AssemblyParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InitContext extends ParserRuleContext {
		public List<TerminalNode> NL() { return getTokens(AssemblyParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(AssemblyParser.NL, i);
		}
		public List<CommandContext> command() {
			return getRuleContexts(CommandContext.class);
		}
		public CommandContext command(int i) {
			return getRuleContext(CommandContext.class,i);
		}
		public List<TerminalNode> LINE_COMMENT() { return getTokens(AssemblyParser.LINE_COMMENT); }
		public TerminalNode LINE_COMMENT(int i) {
			return getToken(AssemblyParser.LINE_COMMENT, i);
		}
		public InitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AssemblyListener ) ((AssemblyListener)listener).enterInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AssemblyListener ) ((AssemblyListener)listener).exitInit(this);
		}
	}

	public final InitContext init() throws RecognitionException {
		InitContext _localctx = new InitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_init);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(9); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(6);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case DEST:
				case COMP:
				case LABEL:
				case ADDRESS:
					{
					setState(4);
					command();
					}
					break;
				case LINE_COMMENT:
					{
					setState(5);
					match(LINE_COMMENT);
					}
					break;
				case NL:
					break;
				default:
					break;
				}
				setState(8);
				match(NL);
				}
				}
				setState(11); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 4214L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CommandContext extends ParserRuleContext {
		public CommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_command; }
	 
		public CommandContext() { }
		public void copyFrom(CommandContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LCommandContext extends CommandContext {
		public TerminalNode LABEL() { return getToken(AssemblyParser.LABEL, 0); }
		public LCommandContext(CommandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AssemblyListener ) ((AssemblyListener)listener).enterLCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AssemblyListener ) ((AssemblyListener)listener).exitLCommand(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CCommandContext extends CommandContext {
		public TerminalNode COMP() { return getToken(AssemblyParser.COMP, 0); }
		public TerminalNode DEST() { return getToken(AssemblyParser.DEST, 0); }
		public TerminalNode JMP() { return getToken(AssemblyParser.JMP, 0); }
		public CCommandContext(CommandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AssemblyListener ) ((AssemblyListener)listener).enterCCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AssemblyListener ) ((AssemblyListener)listener).exitCCommand(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ACommandContext extends CommandContext {
		public TerminalNode ADDRESS() { return getToken(AssemblyParser.ADDRESS, 0); }
		public ACommandContext(CommandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AssemblyListener ) ((AssemblyListener)listener).enterACommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AssemblyListener ) ((AssemblyListener)listener).exitACommand(this);
		}
	}

	public final CommandContext command() throws RecognitionException {
		CommandContext _localctx = new CommandContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_command);
		int _la;
		try {
			setState(22);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DEST:
			case COMP:
				_localctx = new CCommandContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(14);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DEST) {
					{
					setState(13);
					match(DEST);
					}
				}

				setState(16);
				match(COMP);
				setState(18);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==JMP) {
					{
					setState(17);
					match(JMP);
					}
				}

				}
				break;
			case LABEL:
				_localctx = new LCommandContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(20);
				match(LABEL);
				}
				break;
			case ADDRESS:
				_localctx = new ACommandContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(21);
				match(ADDRESS);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\r\u0019\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0001"+
		"\u0000\u0001\u0000\u0003\u0000\u0007\b\u0000\u0001\u0000\u0004\u0000\n"+
		"\b\u0000\u000b\u0000\f\u0000\u000b\u0001\u0001\u0003\u0001\u000f\b\u0001"+
		"\u0001\u0001\u0001\u0001\u0003\u0001\u0013\b\u0001\u0001\u0001\u0001\u0001"+
		"\u0003\u0001\u0017\b\u0001\u0001\u0001\u0000\u0000\u0002\u0000\u0002\u0000"+
		"\u0000\u001d\u0000\t\u0001\u0000\u0000\u0000\u0002\u0016\u0001\u0000\u0000"+
		"\u0000\u0004\u0007\u0003\u0002\u0001\u0000\u0005\u0007\u0005\u0006\u0000"+
		"\u0000\u0006\u0004\u0001\u0000\u0000\u0000\u0006\u0005\u0001\u0000\u0000"+
		"\u0000\u0006\u0007\u0001\u0000\u0000\u0000\u0007\b\u0001\u0000\u0000\u0000"+
		"\b\n\u0005\f\u0000\u0000\t\u0006\u0001\u0000\u0000\u0000\n\u000b\u0001"+
		"\u0000\u0000\u0000\u000b\t\u0001\u0000\u0000\u0000\u000b\f\u0001\u0000"+
		"\u0000\u0000\f\u0001\u0001\u0000\u0000\u0000\r\u000f\u0005\u0001\u0000"+
		"\u0000\u000e\r\u0001\u0000\u0000\u0000\u000e\u000f\u0001\u0000\u0000\u0000"+
		"\u000f\u0010\u0001\u0000\u0000\u0000\u0010\u0012\u0005\u0002\u0000\u0000"+
		"\u0011\u0013\u0005\u0003\u0000\u0000\u0012\u0011\u0001\u0000\u0000\u0000"+
		"\u0012\u0013\u0001\u0000\u0000\u0000\u0013\u0017\u0001\u0000\u0000\u0000"+
		"\u0014\u0017\u0005\u0004\u0000\u0000\u0015\u0017\u0005\u0005\u0000\u0000"+
		"\u0016\u000e\u0001\u0000\u0000\u0000\u0016\u0014\u0001\u0000\u0000\u0000"+
		"\u0016\u0015\u0001\u0000\u0000\u0000\u0017\u0003\u0001\u0000\u0000\u0000"+
		"\u0005\u0006\u000b\u000e\u0012\u0016";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}