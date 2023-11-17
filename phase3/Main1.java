import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputCoolFilePath = "../test/in/35_assignment4.cool";
        String outputFilePath = "output.txt";
        String tablePath = "table.npt";


        Lexer scaner = new Lexer(new FileReader(inputCoolFilePath));
        Imp codeGenerator = new Imp();
        Parser parser = new Parser( scaner, codeGenerator, tablePath , true);
        FileWriter fileWriter = new FileWriter(outputFilePath);
        try {
            parser.parse();
            fileWriter.write("Syntax is correct!");
        } catch (Exception e) {
            fileWriter.write("Syntax is wrong!");
        }
        
        fileWriter.close();
    }
}