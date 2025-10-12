grammar Assembly;

init : ( (command | LINE_COMMENT)? NL )+ ;

command : DEST? COMP JMP?    # cCommand
        | LABEL              # lCommand
        | ADDRESS            # aCommand
        ;

DEST : DEST_CHARS+ '=';
COMP : ('-' DEST_CHARS ) | ( DEST_CHARS (('-' | '!' | '&' | '|' | '+') DEST_CHARS)?) ;
JMP : ';' JMP_CHARS ;
LABEL : '(' SYMBOL ')' ;
ADDRESS : '@' ( NUMBER | SYMBOL ) ;
LINE_COMMENT : '//' ~[\r\n]+ ;

DEST_CHARS : [ADM01] ;
JMP_CHARS : 'J' [EGLMNPQT]+;
SYMBOL : [a-zA-Z_][a-zA-Z0-9_]* ;
NUMBER: DIGIT+;
DIGIT : [0-9];

NL : '\r\n' | ('\r' | '\n');
WS   : [ \t]+ -> skip;