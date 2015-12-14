package imageeditor;

import java.util.*;
import java.io.*;

/** Lexer
 *
 * A class to perform lexical analysis on a .ppm (P3) file.
 *
 * @method lexFile: returns a vector of Tokens to be read by a @Parser.
 *
 * @author Zachary Lohner
 */
public class Lexer {

    private int nextChar;
    private Vector<Token> tokens;
    private Reader inFile;

    public Lexer() { tokens = new Vector<>(); }

    public Vector<Token> lexFile(String filename) throws IOException {

        inFile = new FileReader(filename);

        nextChar = inFile.read();
        while (nextChar != -1) {

            if (Character.isDigit((char)nextChar)) {
                int value = readNumber();
                tokens.add(new Token(value, Token.Type.Number));
            }
            else if (Character.isSpaceChar((char)nextChar) || nextChar == '\n' || nextChar == '\r' || nextChar == '\t') {
                // Skip it
            }
            else if (nextChar == 'P') {
                nextChar = inFile.read();
                if (nextChar == '3') {
                    tokens.add(new Token(Token.Type.P3));
                } else {
                    inFile.close();
                    throw new IOException("Error - Invalid Token (P3): " + nextChar);
                }
            }
            else if (nextChar == '#') {
                skipComment();
            }
            else {
                inFile.close();
                throw new IOException("Error - Invalid Token (Default): " + nextChar);
            }

            nextChar = inFile.read();
        }

        return tokens;
    }

    public void skipComment() throws IOException {
        while (nextChar != '\n') {
            nextChar = inFile.read();
        }
    }

    public int readNumber() throws IOException {

        int number = 0;

        while (Character.isDigit((char)nextChar)) {
            if (number > 0) { number *= 10; }
            number += (nextChar - '0');
            nextChar = inFile.read();
        }

        return number;
    }

    public void closeStream() throws IOException { inFile.close(); }

    public String tokensToString() {
        String allTokens = "";
        for (int i = 0; i < tokens.size(); i++) {
            allTokens += ( tokens.elementAt(i).toString() + "\n" );
        }
        return allTokens;
    }
}
