grammar Assembly_OLD;

init : ( (command | comment)? NL )+ ;

command : dest? comp jmp?    # cCommand
        | label              # lCommand
        | address            # aCommand
        ;

dest : DEST_CHARS+ '=';

comp : ('-' DEST_CHARS ) | ( DEST_CHARS (('-' | '!' | '&' | '|' | '+') DEST_CHARS)?) ;

jmp: ';' JMP_CHARS;

label : LABEL_TEXT ;

address : ADDRESS_TEXT ;

comment : LINE_COMMENT ;

LABEL_TEXT : '(' SYMBOL ')' ;
ADDRESS_TEXT : '@' ( NUMBER | SYMBOL ) ;

DEST_CHARS : [ADM01] ;
JMP_CHARS : 'J' [EGLMNPQT]+;

SYMBOL : [a-zA-Z_][a-zA-Z0-9_]* ;
NUMBER: DIGIT+;
DIGIT : [0-9];


LINE_COMMENT : '//' ~[\r\n]+ ;

NL : '\r\n' | ('\r' | '\n');
WS   : [ \t]+ -> skip;