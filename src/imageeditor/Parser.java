package imageeditor;

import java.util.*;

/** Parser
 *
 * A class to parse a .ppm (P3) file. Creates a data structure (@Image) that can be used to edit the image.
 *
 * @method parsePPMFile: creates a @Lexer and performs lexical analysis on and parses a .ppm file.
 * @return image: a data structure holding information about the .ppm image.
 *
 * @author: Zachary Lohner
 */
public class Parser {

    private Vector<Token> tokens;
    private ListIterator<Token> tokenList;
    private Token current;
    private Image image;
    private boolean endTokens;

    public Parser() { endTokens = false; }

    public Image parsePPMFile(String filename) throws Exception {

        Lexer lex = new Lexer();

        tokens = lex.lexFile(filename);

        tokenList = tokens.listIterator();
        nextToken();

        parseHeader();
        parseAllPixels();

        return image;
    }

    public void parseHeader() throws Exception {

        int width;
        int height;
        int maxColorValue;

        // Magic Number
        if (current.getType() == Token.Type.P3) {
            nextToken();
        } else { throw new Exception("Invalid File Format (Missing 'P3')"); }

        // Width
        if (current.getType() == Token.Type.Number) {
            width = current.getValue();
            nextToken();
        } else { throw new Exception("Invalid File Format (Missing Width)"); }

        // Height
        if (current.getType() == Token.Type.Number) {
            height = current.getValue();
            nextToken();
        } else { throw new Exception("Invalid File Format (Missing Height)"); }

        // Max Color Value
        if (current.getType() == Token.Type.Number) {
            maxColorValue = current.getValue();
            if (maxColorValue != ImageEditor.MAX_COLOR_VALUE) { throw new Exception("Invalid File Format (Invalid Max Color Value)"); }
            nextToken();
        } else { throw new Exception("Invalid File Format (Missing Max Color Value)"); }

        // Create Image
        image = new Image(width, height, maxColorValue);

    }

    public void parseAllPixels() throws Exception {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                parsePixel(i, j);
            }
        }
    }

    public void parsePixel(int i, int j) throws Exception {

        int red;
        int green;
        int blue;

        // Red
        if (current.getType() == Token.Type.Number) {
            red = current.getValue();
            if (red > image.getMaxColorValue() || red < 0) {
                throw new Exception("Invalid File Format ((" + i + "," + j + ") Red Value too high)");
            }
            nextToken();
        } else { throw new Exception("Invalid File Format (Missing Pixel Color Value - Red)"); }

        // Green
        if (current.getType() == Token.Type.Number) {
            green = current.getValue();
            if (green > image.getMaxColorValue() || green < 0) {
                throw new Exception("Invalid File Format ((" + i + "," + j + ") Green Value too high)");
            }
            nextToken();
        } else { throw new Exception("Invalid File Format (Missing Pixel Color Value - Green)"); }

        // Blue
        if (current.getType() == Token.Type.Number) {
            blue = current.getValue();
            if (blue > image.getMaxColorValue() || blue < 0) {
                throw new Exception("Invalid File Format ((" + i + "," + j + ") Green Value too high)");
            }
            nextToken();
        } else { throw new Exception("Invalid File Format (Missing Pixel Color Value - Blue)"); }

        // Set Pixel
        if (image != null) { image.setPixel(i, j, new Pixel(red, green, blue)); }

    }

    public void nextToken() throws Exception {
        if (endTokens) { throw new Exception("Invalid File Format (Reached End of File)"); }
        if (tokenList.hasNext()) { current = tokenList.next(); } else { endTokens = true; }
    }
}
