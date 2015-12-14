package imageeditor;

/** Pixel
 *
 * A class to store data about a single pixel in a .ppm (P3) file.
 * Stored in a 2d Array in @Image.
 *
 * @member red, green, blue: integers representing the color values of the pixel.
 *
 * @author Zachary Lohner
 */
public class Pixel {
    private int red;
    private int green;
    private int blue;

    public Pixel(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Pixel copy() {
        return new Pixel(this.red, this.green, this.blue);
    }

    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }

    public void setRed(int newRed) { this.red = newRed; }
    public void setGreen(int newGreen) { this.green = newGreen; }
    public void setBlue(int newBlue) { this.blue = newBlue; }
}
