package GUI_FILES;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

import static GUI_FILES.ImageToTextt.imagetotext;
import static GUI_FILES.TextToSpeechUI.createStyledButton;

public class ImageToTextUI {
    public static void display() {
        JFrame frame = new JFrame("Image to Text");
        frame.setSize(300, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        JTextField textField = new JTextField(20);
        JButton generateButton = createStyledButton("Generate Text");
        JButton backToHomeButton = createStyledButton("Back to Home");

        backToHomeButton.addActionListener(e -> {
            frame.dispose();
            MainGUI2.main(new String[]{});
        });

        frame.setLayout(new FlowLayout());
        frame.add(textField);
        frame.add(generateButton);
        frame.add(backToHomeButton);
        frame.setVisible(true);

        ImageIcon icon = new ImageIcon("resources_images/Handstalk logo.png"); // Replace with the path to your image
        frame.setIconImage(icon.getImage());
    }

    public static long calculateImageSum(String imagePath) {
        // Load the image using Imgcodecs
        Mat image = Imgcodecs.imread(imagePath);
        long sum = 0;
        for (int r = 0; r < image.height(); r++) {
            for (int c = 0; c < image.width(); c++) {
                double[] colors = image.get(r, c);
                for (double color : colors) {
                    sum += (long) color;
                }
            } 
        }

        return sum;
    }

    private static void setIconImage(JFrame frame) {
        ImageIcon icon = new ImageIcon("resources_images/Handstalk logo.png");
        frame.setIconImage(icon.getImage());
    }

    public static String getStringFromImage(ArrayList<String> imagePathList) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String imagePath : imagePathList) {
            long imageSum = calculateImageSum(imagePath);
            String text = imagetotext.get(imageSum); // <-- Where is imagetotext defined?
            if (text != null) {
                stringBuilder.append(" ").append(text);
            }
        }

        return stringBuilder.toString();
    }

    public static String getText(String imagePath) {
        return imagetotext.get(calculateImageSum(imagePath)); // <-- Where is imagetotext defined?
    }


    public static ArrayList<String> getImagePaths() {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> imageList = new ArrayList<>();
        String s;

        do {
            System.out.println("Enter image path. If you're finished, enter 'DONE': ");
            s = sc.nextLine();

            if (!s.equals("DONE") && !s.isEmpty()) {
                imageList.add(s);
            }
        } while (!s.equals("DONE"));

        return imageList;
    }

}