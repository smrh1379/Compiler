import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class Main {
    public static void main(String[] args) throws IOException {
        String inputAddress = "code.txt";
        Lexer lexer = new Lexer(new FileReader (inputAddress));
        while(true){
        Lexer.Symbol temp=lexer.next_token();
    System.out.println(temp.content.toString() + " " + temp.type);
        if (lexer.yyatEOF()){
            break;
        }
    }
}}