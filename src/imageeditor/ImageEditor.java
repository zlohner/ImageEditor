package imageeditor;

import java.io.*;

/** ImageEditor
 *
 * A command line tool that edits a .ppm (P3) file (invert, grayscale, emboss, motionblur).
 *
 * @author Zachary Lohner
 */
public class ImageEditor {

    public static final int MAX_COLOR_VALUE = 255;
    public static String[] cArgs;


    public static void main(String[] args) throws Exception {
        new ImageEditor().run(args);
    }

    public void run(String[] args) throws Exception {
        try {
            cArgs = args;
            checkUsage(cArgs, "USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");

            Parser parse = new Parser();
            Image image = parse.parsePPMFile(cArgs[0]);

            editImage(image, cArgs[2]);

            outFile(image, cArgs[1]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /** checkUsage
     *
     * Checks the command line arguments for correct usage.
     *
     * @param cArgs: command line arguments
     * @param usage: message to display if usage is incorrect
     * @throws Exception: if argument are incorrect
     */
    public void checkUsage(String[] cArgs, String usage) throws Exception {
        if (cArgs.length < 3) { throw new Exception(usage); }
        if (!cArgs[2].equals("motionblur") && cArgs.length != 3) { throw new Exception(usage); }
        if (cArgs[2].equals("motionblur")) {
            if (cArgs.length != 4) { throw new Exception(usage); }
            try {
                if (Integer.parseInt(cArgs[3]) < 0) {
                    throw new Exception(usage);
                }
            } catch (NumberFormatException e){
                throw new Exception(usage);
            }
        }
        if ( !cArgs[2].equals("invert") && !cArgs[2].equals("grayscale") && !cArgs[2].equals("emboss") && !cArgs[2].equals("motionblur") ) {
            throw new Exception(usage);
        }

    }

    /** editImage
     *
     * Edits the image (one pixel at a time).
     * Calls @invertPixel, @grayscalePixel, @embossPixel, or @blurPixel based on type.
     *
     * @param image: image to edit
     * @param editType: type of edit
     */
    public void editImage(Image image, String editType) {
        Pixel[][] pixels = image.getPixels();
        Pixel[][] oldPixels = new Pixel[image.getHeight()][image.getWidth()];

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                oldPixels[i][j] = pixels[i][j].copy();
            }
        }

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                Pixel p = pixels[i][j];
                switch (editType) {
                    case "invert": invertPixel(p); break;
                    case "grayscale": grayscalePixel(p); break;
                    case "emboss": embossPixel(p, i, j, oldPixels); break;
                    case "motionblur": blurPixel(p, i, j, pixels, Integer.parseInt(cArgs[3])); break;
                }
            }
        }

        switch (editType) {
            case "invert": System.out.println("Image Inverted"); break;
            case "grayscale": System.out.println("Image Grayscaled"); break;
            case "emboss": System.out.println("Image Embossed"); break;
            case "motionblur": System.out.println("Image Blurred (" + cArgs[3] + ")"); break;
        }
    }

    /** invertPixel
     *
     * Inverts a single pixel. Called by @editImage.
     *
     * @param p: pixel to invert
     */
    public void invertPixel(Pixel p) {
        p.setRed(MAX_COLOR_VALUE - p.getRed());
        p.setGreen(MAX_COLOR_VALUE - p.getGreen());
        p.setBlue(MAX_COLOR_VALUE - p.getBlue());
    }

    /** grayscalePixel
     *
     * Grayscales a single pixel. Called by @editImage.
     *
     * @param p: pixel to grayscale
     */
    public void grayscalePixel(Pixel p) {
        int average = (p.getRed() + p.getGreen() + p.getBlue()) / 3;
        p.setRed(average);
        p.setGreen(average);
        p.setBlue(average);
    }

    /** embossPixel
     *
     * Embosses a single pixel. Called by @editImage.
     *
     * @param p: pixel to emboss
     * @param r: row (in array)
     * @param c: column (in array)
     * @param oldPixels: the original image
     */
    public void embossPixel(Pixel p, int r, int c, Pixel[][] oldPixels) {
        int v = 128;
        if ((r - 1) >= 0 && (c - 1) >= 0) {
            Pixel ref = oldPixels[r - 1][c - 1];
            int greatestDiff = 0;
            int diff;

            diff = p.getRed() - ref.getRed();
            if (Math.abs(diff) > Math.abs(greatestDiff)) { greatestDiff = diff; }

            diff = p.getGreen() - ref.getGreen();
            if (Math.abs(diff) > Math.abs(greatestDiff)) { greatestDiff = diff; }

            diff = p.getBlue() - ref.getBlue();
            if (Math.abs(diff) > Math.abs(greatestDiff)) { greatestDiff = diff; }

            v = greatestDiff + 128;
            if (v < 0) { v = 0; }
            if (v > MAX_COLOR_VALUE) { v = MAX_COLOR_VALUE; }
        }

        p.setRed(v);
        p.setGreen(v);
        p.setBlue(v);
    }

    /** blurPixel
     *
     * Blurs a single pixel. Called by @editImage.
     *
     * @param p: pixel to blur
     * @param r: row (in array)
     * @param c: column (in array)
     * @param pixels: the image grid
     * @param blurValue: value at which to blur (cArgs[3])
     */
    public void blurPixel(Pixel p, int r, int c, Pixel[][] pixels, int blurValue) {
        int average;
        int averageCount;

        // Red
        average = 0;
        averageCount = 0;
        for (int i = c + (blurValue - 1); i >= c; i--) {
            if (i < pixels[r].length) {
                averageCount++;
                average += pixels[r][i].getRed();
            }
        }
        if ( averageCount > 0 ) { average /= averageCount; }
        p.setRed(average);

        // Green
        average = 0;
        averageCount = 0;
        for (int i = c + (blurValue - 1); i >= c; i--) {
            if (i < pixels[r].length) {
                averageCount++;
                average += pixels[r][i].getGreen();
            }
        }
        if ( averageCount > 0 ) { average /= averageCount; }
        p.setGreen(average);

        // Blue
        average = 0;
        averageCount = 0;
        for (int i = c + (blurValue - 1); i >= c; i--) {
            if (i < pixels[r].length) {
                averageCount++;
                average += pixels[r][i].getBlue();
            }
        }
        if ( averageCount > 0 ) { average /= averageCount; }
        p.setBlue(average);

    }

    /** outFile
     *
     * Writes the data of an @Image object to a file.
     *
     * @param image: image to write
     * @param filename: file to write to
     * @throws IOException
     */
    public void outFile(Image image, String filename) throws Exception {
        if (image == null) { throw new Exception("No Image Loaded"); }
        else {
            Writer outFile = new FileWriter(filename);
            StringBuilder sb = new StringBuilder();

            // Header
            sb.append("P3\n");
            sb.append("# Created by ImageEditor (C) zlohner 2015\n");
            sb.append(image.getWidth() + " " + image.getHeight() + "\n");
            sb.append(image.getMaxColorValue() + "\n");

            // Pixels
            Pixel[][] pixels = image.getPixels();
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    Pixel p = pixels[i][j];
                    sb.append(p.getRed() + "\n" + p.getGreen() + "\n" + p.getBlue() + "\n");
                }
            }

            outFile.write(sb.toString());

            outFile.close();
        }
    }

}
