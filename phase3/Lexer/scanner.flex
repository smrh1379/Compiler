import HtmlHighlighter.HtmlHighlighter;

%%
%class Lexer
%unicode
%line
%column
%type Symbol
%public
%function next_token
%state STRING
%implements Lexical
// %standalone 
%state CHAR
%{
public int ICV=0;
public static String Value ="";
public static HtmlHighlighter htmlHighlighter = new HtmlHighlighter();
  class Symbol {
  public String type;
	public String content;
	public int  yyline, yycolumn;
  	public Symbol( String tokenType, int yyline, int yycolumn, String content ) {
		this.content = content;
		this.type = tokenType;
		this.yyline = yyline;
		this.yycolumn = yycolumn;
	}
	}
    String string="";
    StringBuilder character;
    String STP="";
    public Symbol symbol(String type) {
		return new Symbol(type, yyline, yycolumn,"");
    }

    public Symbol symbol(String type, String value) {
    	return new Symbol(type, yyline, yycolumn, value);
    }

  public String nextToken(){
  try {
    Symbol current = next_token();
    Value = current==null?"":current.content;
    return current == null ? "$" : current.type;
  }catch(Exception e){
    e.printStackTrace();
    return null;
  }
}
%}


Alphabet=[A-Za-z]
Digit = [0-9]
underline = "_"
Identifier = {Alphabet}(({Alphabet}|{Digit}|{underline}) {0,30})
Zero = 0
HexaDecimal = {Zero}["X"|"x"]({Digit}|{Alphabet})+
Decimal = [1-9][0-9]*
IntegerNumbers = ( {Zero} | {Decimal})
Real = (({Digit}+\.{Digit}*))
ScientificFloat = (({Decimal} | {Real})[Ee][\+|\-]?{Decimal})
endLine= [\n | \r | \r\n]
InputCharacter = [^\r\n]
WhiteSpace = {endLine} | [\t\f]   
MultilineComment = "/*" [^*] ~ "*/" | "/*" "*"+ "/"
EndOfLinecomment = "//" {InputCharacter}* {endLine}?
Comment = {MultilineComment} | {EndOfLinecomment}
StringPattern = ([^\"\\\n]|\\.|\\\n)*

%%
<YYINITIAL> {
    // Reserved keywords
    "let"     { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("LET",yytext());}
    "void"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("VOID",yytext());} 
    "int"     { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("INT",yytext());}
    "real"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("REAL",yytext());}
    "bool"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("BOOL",yytext());}
    "string"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("STRING",yytext());}
    "static"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("STATIC",yytext());}
    "class"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("CLASS",yytext());}
    "for"     { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("FOR",yytext());}
    "len"     { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("LEN",yytext());}
    "loop"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("LOOP",yytext());}
    "print"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("PRINT",yytext());}
    "while"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("WHILE",yytext());}
     "if"     { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("IF",yytext()); }
    "else"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("ELSE",yytext()); }
    "range"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("RANGE",yytext());}
    "func"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("FUNC",yytext());}
    "return"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("RETURN",yytext());}
    "break"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("BREAK",yytext());}
    "inputStr"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("INPUTSTR",yytext());}
    "new"     { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("NEW",yytext());}
    "continue"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("CONTINUE",yytext());}
    "inputInt"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("INPUTINT",yytext());}
    "in"      { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("IN",yytext());}
    "true"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("TRUE",yytext());}
    "false"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("FALSE",yytext());}
    
	  "+"				  {  htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("ADD",yytext()); }
	  "*"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("PROD",yytext()); }
	  "+="				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("ADDASS",yytext()); }
	  "*="				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("PRODASS",yytext()); }
	  "++"				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("INC",yytext()); }
    "<"			    { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("LESS",yytext()); }
    ">"				  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("GR",yytext()); }
    "=="			  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("EQ",yytext()); }
    "!="			  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("NOTEQ",yytext()); }
    "="         { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("ASSIGN",yytext()); }
	  "&&"				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("LOGAND",yytext()); }
    "&" 				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("BITAND",yytext()); }
	  "!" 				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("NOT",yytext()); }
    ","	  			{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("COLON",yytext()); }
	  "["					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("OPENBRACES",yytext()); }
	  "]"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("CLOSBRACES",yytext()); }
	  "{"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("OPENCURLY",yytext()); }
	  "}"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("CLOSCURLY",yytext()); }
	  "("					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("OPENPAREN",yytext()); }
	  ")"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("CLOSPAREN",yytext()); }
    "."					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("DOT",yytext()); }
	  "^"			  	{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("XOR",yytext()); }
	  "|"				  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("BITOR",yytext()); }
	  "||"			  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("LOGICOR",yytext()); }
	  "%"				  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("MOD",yytext()); }
    "<="			  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("LESSEQ",yytext()); }
    ">="			  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("GREQ",yytext()); }
	  "-"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("MINUS",yytext()); }
	  "--"				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("DEC",yytext()); }
	  "-="				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("SUBASS",yytext()); }
	  "/="				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("DIVASS",yytext()); }
	  "/"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("DIV",yytext()); }
	  ";"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("SEMICOLON",yytext()); }
   
    {HexaDecimal} {htmlHighlighter.integerNumbers(yytext().toString());
                        return symbol("HEXNUMBER",yytext());}
    {Identifier}  { htmlHighlighter.Identifiers(yytext());
                      return symbol("ID",yytext());}
    {Real}      { htmlHighlighter.realNumbers(yytext().toString());
                    return symbol("REALNUMBER",yytext());}
    {IntegerNumbers} {htmlHighlighter.integerNumbers(yytext().toString());
                        return symbol("INTNUMBER",yytext());}
    {ScientificFloat} {htmlHighlighter.realNumbers(yytext());
                        return symbol("SCIFLOAT",yytext());}
    {Comment}       {htmlHighlighter.comments(yytext());
                    return symbol("Comment",yytext());}
     "\""       {yybegin(STRING); string = "" +yytext();
     STP=string;}
     [\n] {htmlHighlighter.newLine(yyline+1);}
     [\s]      {htmlHighlighter.space();}
     
     "\\n"       { htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\n" ); }
	   "\\t"       { htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\t" ); }
	    "\\r"       { htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\r" ); }
	   "\\\\"      { htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\\\" ); }
	    "\\'"       { htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\'" ); }
	    "\\\""      {htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\\"" ); }
}

<STRING>{
  "\""    {
    STP = STP + yytext() ;
    htmlHighlighter.stringsAndCharacters (string+yytext());
		yybegin(YYINITIAL);
		return symbol("STRLITERAL", STP);
		}
  "\\n"       { htmlHighlighter.stringsAndCharacters (string);
                string = "";
                  htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\n" ); }
	"\\t"       { htmlHighlighter.stringsAndCharacters (string);
                string = "";
                  htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\t" ); }
	"\\r"       { htmlHighlighter.stringsAndCharacters (string);
                string = "";
                  htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\r" ); }

	"\\\\"      { htmlHighlighter.stringsAndCharacters (string);
                string = "";
                  htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\\\" ); }
	"\\'"       { htmlHighlighter.stringsAndCharacters (string);
                string = "";
                  htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\'" ); }
	"\\\""      { htmlHighlighter.stringsAndCharacters (string);
                string = "";
                  htmlHighlighter.specialCharacters(yytext());
                  return symbol("Special_CHAR", "\\\"" ); }
   .          { string = string + yytext();
                STP= STP + yytext();}
              }
<CHAR>{
  "\'"  {
    htmlHighlighter.stringsAndCharacters(yytext());
    yybegin(YYINITIAL);
    return symbol("CHAR",yytext());
  }
}
[^] {htmlHighlighter.undefinedToken(yytext());}
<<EOF>>      { htmlHighlighter.endFile();
return null;
}