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
IntegerNumbers = ({HexaDecimal} | {Zero} | {Decimal})
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
                  return symbol("LET");}
    "void"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("VOID");} 
    "int"     { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("INT");}
    "real"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("REAL");}
    "bool"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("BOOL");}
    "string"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("STRING");}
    "static"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("STATIC");}
    "class"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("CLASS");}
    "for"     { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("FOR");}
    "len"     { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("LEN");}
    "loop"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("LOOP");}
    "print"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("PRINT");}
    "while"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("WHILE");}
     "if"     { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("IF"); }
    "else"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("ELSE"); }
    "range"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("RANGE");}
    "func"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("FUNC");}
    "return"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("RETURN");}
    "break"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("BREAK");}
    "inputStr"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("INPUTSTR");}
    "new"     { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("NEW");}
    "continue"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("CONTINUE");}
    "inputInt"  { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("INPUTINT");}
    "in"      { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("IN");}
    "true"    { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("TRUE");}
    "false"   { htmlHighlighter.reservedKeyWords(yytext());
                  return symbol("FALSE");}
    
	  "+"				  {  htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("ADD"); }
	  "*"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("PROD"); }
	  "+="				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("ADDASS"); }
	  "*="				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("PRODASS"); }
	  "++"				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("INC"); }
    "<"			    { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("LESS"); }
    ">"				  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("GR"); }
    "=="			  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("EQ"); }
    "!="			  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("NOTEQ"); }
    "="         { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("ASSIGN"); }
	  "&&"				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("LOGAND"); }
    "&" 				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("BITAND"); }
	  "!" 				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("NOT"); }
    ","	  			{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("COLON"); }
	  "["					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("OPENBRACES"); }
	  "]"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("CLOSBRACES"); }
	  "{"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("OPENCURLY"); }
	  "}"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("CLOSCURLY"); }
	  "("					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("OPENPAREN"); }
	  ")"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("CLOSPAREN"); }
    "."					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("DOT"); }
	  "^"			  	{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("XOR"); }
	  "|"				  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("BITOR"); }
	  "||"			  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("LOGICOR"); }
	  "%"				  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("MOD"); }
    "<="			  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("LESSEQ"); }
    ">="			  { htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("GREQ"); }
	  "-"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("MINUS"); }
	  "--"				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("DEC"); }
	  "-="				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("SUBASS"); }
	  "/="				{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("DIVASS"); }
	  "/"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("DIV"); }
	  ";"					{ htmlHighlighter.operatorsAndPunctuations(yytext());
                    return symbol("SEMICOLON"); }
   

    {Identifier}  { htmlHighlighter.Identifiers(yytext());
                      return symbol("ID",yytext());}
    {Real}      { htmlHighlighter.realNumbers(yytext().toString());
                    return symbol("REALNUMBER",yytext());}
    {IntegerNumbers} {htmlHighlighter.integerNumbers(yytext().toString());
                        return symbol("INTNUMBER",yytext());}
    {ScientificFloat} {htmlHighlighter.realNumbers(yytext());
                        return symbol("SCIFLOAT",yytext());}
    {Comment}       {htmlHighlighter.comments(yytext());
                    return symbol("Comment");}
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