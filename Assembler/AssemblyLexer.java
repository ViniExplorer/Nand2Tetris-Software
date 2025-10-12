// Generated from Assembly.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class AssemblyLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DEST=1, COMP=2, JMP=3, LABEL=4, ADDRESS=5, LINE_COMMENT=6, DEST_CHARS=7, 
		JMP_CHARS=8, SYMBOL=9, NUMBER=10, DIGIT=11, NL=12, WS=13;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"DEST", "COMP", "JMP", "LABEL", "ADDRESS", "LINE_COMMENT", "DEST_CHARS", 
			"JMP_CHARS", "SYMBOL", "NUMBER", "DIGIT", "NL", "WS"
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


	public AssemblyLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Assembly.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\ra\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0001\u0000\u0004\u0000\u001d\b\u0000\u000b"+
		"\u0000\f\u0000\u001e\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0003\u0001(\b\u0001\u0003\u0001*\b\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u00046\b\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0004\u0005<\b\u0005"+
		"\u000b\u0005\f\u0005=\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"+
		"\u0004\u0007D\b\u0007\u000b\u0007\f\u0007E\u0001\b\u0001\b\u0005\bJ\b"+
		"\b\n\b\f\bM\t\b\u0001\t\u0004\tP\b\t\u000b\t\f\tQ\u0001\n\u0001\n\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0003\u000bY\b\u000b\u0001\f\u0004\f\\"+
		"\b\f\u000b\f\f\f]\u0001\f\u0001\f\u0000\u0000\r\u0001\u0001\u0003\u0002"+
		"\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013"+
		"\n\u0015\u000b\u0017\f\u0019\r\u0001\u0000\b\u0005\u0000!!&&++--||\u0002"+
		"\u0000\n\n\r\r\u0004\u000001AADDMM\u0005\u0000EEGGLNPQTT\u0003\u0000A"+
		"Z__az\u0004\u000009AZ__az\u0001\u000009\u0002\u0000\t\t  j\u0000\u0001"+
		"\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005"+
		"\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001"+
		"\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000"+
		"\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000"+
		"\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000"+
		"\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000"+
		"\u0000\u0000\u0001\u001c\u0001\u0000\u0000\u0000\u0003)\u0001\u0000\u0000"+
		"\u0000\u0005+\u0001\u0000\u0000\u0000\u0007.\u0001\u0000\u0000\u0000\t"+
		"2\u0001\u0000\u0000\u0000\u000b7\u0001\u0000\u0000\u0000\r?\u0001\u0000"+
		"\u0000\u0000\u000fA\u0001\u0000\u0000\u0000\u0011G\u0001\u0000\u0000\u0000"+
		"\u0013O\u0001\u0000\u0000\u0000\u0015S\u0001\u0000\u0000\u0000\u0017X"+
		"\u0001\u0000\u0000\u0000\u0019[\u0001\u0000\u0000\u0000\u001b\u001d\u0003"+
		"\r\u0006\u0000\u001c\u001b\u0001\u0000\u0000\u0000\u001d\u001e\u0001\u0000"+
		"\u0000\u0000\u001e\u001c\u0001\u0000\u0000\u0000\u001e\u001f\u0001\u0000"+
		"\u0000\u0000\u001f \u0001\u0000\u0000\u0000 !\u0005=\u0000\u0000!\u0002"+
		"\u0001\u0000\u0000\u0000\"#\u0005-\u0000\u0000#*\u0003\r\u0006\u0000$"+
		"\'\u0003\r\u0006\u0000%&\u0007\u0000\u0000\u0000&(\u0003\r\u0006\u0000"+
		"\'%\u0001\u0000\u0000\u0000\'(\u0001\u0000\u0000\u0000(*\u0001\u0000\u0000"+
		"\u0000)\"\u0001\u0000\u0000\u0000)$\u0001\u0000\u0000\u0000*\u0004\u0001"+
		"\u0000\u0000\u0000+,\u0005;\u0000\u0000,-\u0003\u000f\u0007\u0000-\u0006"+
		"\u0001\u0000\u0000\u0000./\u0005(\u0000\u0000/0\u0003\u0011\b\u000001"+
		"\u0005)\u0000\u00001\b\u0001\u0000\u0000\u000025\u0005@\u0000\u000036"+
		"\u0003\u0013\t\u000046\u0003\u0011\b\u000053\u0001\u0000\u0000\u00005"+
		"4\u0001\u0000\u0000\u00006\n\u0001\u0000\u0000\u000078\u0005/\u0000\u0000"+
		"89\u0005/\u0000\u00009;\u0001\u0000\u0000\u0000:<\b\u0001\u0000\u0000"+
		";:\u0001\u0000\u0000\u0000<=\u0001\u0000\u0000\u0000=;\u0001\u0000\u0000"+
		"\u0000=>\u0001\u0000\u0000\u0000>\f\u0001\u0000\u0000\u0000?@\u0007\u0002"+
		"\u0000\u0000@\u000e\u0001\u0000\u0000\u0000AC\u0005J\u0000\u0000BD\u0007"+
		"\u0003\u0000\u0000CB\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000"+
		"EC\u0001\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000F\u0010\u0001\u0000"+
		"\u0000\u0000GK\u0007\u0004\u0000\u0000HJ\u0007\u0005\u0000\u0000IH\u0001"+
		"\u0000\u0000\u0000JM\u0001\u0000\u0000\u0000KI\u0001\u0000\u0000\u0000"+
		"KL\u0001\u0000\u0000\u0000L\u0012\u0001\u0000\u0000\u0000MK\u0001\u0000"+
		"\u0000\u0000NP\u0003\u0015\n\u0000ON\u0001\u0000\u0000\u0000PQ\u0001\u0000"+
		"\u0000\u0000QO\u0001\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000R\u0014"+
		"\u0001\u0000\u0000\u0000ST\u0007\u0006\u0000\u0000T\u0016\u0001\u0000"+
		"\u0000\u0000UV\u0005\r\u0000\u0000VY\u0005\n\u0000\u0000WY\u0007\u0001"+
		"\u0000\u0000XU\u0001\u0000\u0000\u0000XW\u0001\u0000\u0000\u0000Y\u0018"+
		"\u0001\u0000\u0000\u0000Z\\\u0007\u0007\u0000\u0000[Z\u0001\u0000\u0000"+
		"\u0000\\]\u0001\u0000\u0000\u0000][\u0001\u0000\u0000\u0000]^\u0001\u0000"+
		"\u0000\u0000^_\u0001\u0000\u0000\u0000_`\u0006\f\u0000\u0000`\u001a\u0001"+
		"\u0000\u0000\u0000\u000b\u0000\u001e\')5=EKQX]\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}