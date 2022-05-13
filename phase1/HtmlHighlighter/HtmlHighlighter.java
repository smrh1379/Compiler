package HtmlHighlighter;
import java.io.FileWriter;
import java.io.IOException;
public class HtmlHighlighter {
    private StringBuilder mainText =
            new StringBuilder("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body style=\"background-color:#222222\">" +
                    "\n"+
                   "<p style=\"display:inline;color:#69676C\">"+(1)+ "</p>&nbsp;");

    public void reservedKeyWords(String text) {
        addTags(text, "#B667F1", true, false);
    }

    public void Identifiers(String text) {
        addTags(text, "#FFFFFF", false, false);
    }

    public void integerNumbers(String text) {
        addTags(text, "#FFC300", false, false);
    }

    public void realNumbers(String text) {
        addTags(text, "#FFC300", false, true);
    }

    public void stringsAndCharacters(String text) {
        addTags(text, "#00C897", false, false);
    }

    public void specialCharacters(String text) {
        addTags(text, "#B8FFF9", false, true);
    }

    public void comments(String text) {
        addTags(text, "#69676C", false, false);
    }

    public void operatorsAndPunctuations(String text) {
        addTags(text, "#90E0EF", false, false);
    }

    public void undefinedToken(String text) {
        addTags(text, "#FF1818", false, false);
    }
    public void Linenum(Integer text) {
        addTags(Integer.toString(text), "#69676C", false, false);
    }
    public void newLine(Integer linenum) {
        mainText.append("<br>\n");
        mainText.append("<p style=\"display:inline;color:#69676C\">"+(linenum+1)+ "</p>&nbsp;" );
    }

    public void space() {
        mainText.append("<p style=\"display:inline;\">&nbsp;</p>\n");
    }

    public void endFile() {
        mainText.append("\n" +
                "</body>\n" +
                "</html>\n");
        try {
            FileWriter fileWriter = new FileWriter("output.html");
            fileWriter.write(mainText.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTags(String text, String color, boolean bold, boolean italic ) {
        if (bold) {
            mainText.append("<b style=\"display:inline;color:" + color + "\">" + text + "</b>\n");
        } else if (italic) {
            mainText.append("<i style=\"display:inline;color:" + color + "\">" + text + "</i>\n");

        } else {
            mainText.append("<p style=\"display:inline;color:" + color + "\">" + text + "</p>\n");

        }
    }
}