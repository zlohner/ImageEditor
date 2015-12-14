package imageeditor;

/** Token
 *
 * A class to hold data from a .ppm (P3) file to be parsed.
 * Used by @Lexer and @Parser.
 *
 * @author Zachary Lohner
 */
public class Token {

    private int value;
    private Type type;

    enum Type { P3, Number }

    public Token(int value, Type type) {
        this.value = value;
        this.type = type;
    }
    public Token(Type type) {
        this.value = 0;
        this.type = type;
    }

    public Type getType() { return type; }
    public int getValue() { return value; }

    public String toString() {
        String s = "(" + value + "," + type + ")";
        return s;
    }
}
