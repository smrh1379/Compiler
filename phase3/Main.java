import java.io.*;
import Lexer.*;
import codeGenerator.*;
import parser.*;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputCoolFilePath = "./input.cool";
        String tablePath = "./parser/table.npt";

        Lexer scanner = new Lexer(new FileReader(inputCoolFilePath));
        CodeGen codeGenerator = new CodeGen(scanner);
        // Parser parser = new Parser(scanner, codeGenerator, tablePath);
        Parser parser1 = new Parser(scanner, codeGenerator,tablePath ,true);
        parser1.parse();

        }}