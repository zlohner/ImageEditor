package imageeditor;

/** Image
 *
 * A data structure to hold information from a .ppm (P3) file.
 *
 * @member width, height: dimensions of the image.
 * @member maxColorValue: maximum color value for @Pixel red, green, blue.
 * @member pixels: array of pixels representing the image grid.
 *
 * @author Zachary Lohner
 */
public class Image {

    private int width;
    private int height;
    private int maxColorValue;
    private Pixel [][] pixels;

    public Image(int width, int height, int maxColorValue) {
        setWidth(width);
        setHeight(height);
        setMaxColorValue(maxColorValue);
        pixels = new Pixel[height][width];
    }

    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setMaxColorValue(int maxColorValue) { this.maxColorValue = maxColorValue; }
    public void setPixel(int x, int y, Pixel toSet) { pixels[x][y] = toSet; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getMaxColorValue() { return maxColorValue; }
    public Pixel[][] getPixels() { return pixels; }

}
