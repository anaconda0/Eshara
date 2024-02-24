package GUI_FILES;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ImageToTextt {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static final HashMap<Long, String> imagetotext;

    static {
        imagetotext = new HashMap<>();
        String filePath = "src/GUI_FILES/outputfiled file.txt";
        uploadHashMapFromFile(filePath);
    }

    public static long calculateImageSum(String imagePath) {
        // Load the image
        Mat image = Imgcodecs.imread(imagePath);
        long sum = 0;
        for (int r = 0; r < image.height(); r++) {
            for (int c = 0; c < image.width(); c++) {
                double[] colors = image.get(r, c);
                for (double v : colors) {
                    sum += (long) v;
                }
            }
        }

        return sum;
    }

    public static String getStringfromImage(ArrayList<String> ImagePath) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String imagePath : ImagePath) {
            String text = imagetotext.get(calculateImageSum(imagePath));
            stringBuilder.append(" ").append(text);
        }

        return stringBuilder.toString();
    }

    public static void uploadHashMapFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                try {
                    long key = Long.parseLong(parts[1].trim());
                    String value = parts[0].trim();
                    imagetotext.put(key, value);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid format for key: " + parts[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}












