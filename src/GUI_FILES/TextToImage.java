package GUI_FILES;

import org.opencv.core.Mat;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.*;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class TextToImage  {

    private static HashMap<String, Mat> textToImage = new HashMap<>();
    static {
        String filePath = "src/GUI_FILES/TTIHashMap.txt";
        uploadHashMapFromFile(filePath);
    }

    public static BufferedImage Mat2BufferedImage(Mat m) {
        if (m == null) {
            System.err.println("Input Mat is null.");
            return null;
        }

        int type = BufferedImage.TYPE_BYTE_GRAY;

        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];

        if (m.isContinuous()) {
            m.get(0, 0, b);
        } else {

            System.err.println("The data in Mat is not continuous.");
            return null;
        }

        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);

        return image;
    }
    private static JLabel loadImage(BufferedImage image) {
        if (image != null) {
            ImageIcon icon = new ImageIcon(image);
            return new JLabel(icon);
        } else {
            return null;
        }
    }
    public static void displayImages(List<BufferedImage> textToImageList, int delay) {
        JFrame jFrame = new JFrame("Image Viewer");
        jFrame.setLayout(new FlowLayout(FlowLayout.LEFT)); // Set left alignment
        jFrame.setPreferredSize(new Dimension(600, 1000));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Timer timer = new Timer(delay, null);
        int[] index = {0};

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index[0] < textToImageList.size()) {
                    BufferedImage bufferedImage = textToImageList.get(index[0]);
                    JLabel imageLabel = loadImage(bufferedImage);
                    if (imageLabel != null) {
                        jFrame.getContentPane().removeAll();
                        jFrame.add(imageLabel);
                        jFrame.revalidate();
                        jFrame.repaint();
                        index[0]++;
                    }
                } else {
                    timer.stop();
                }
            }
        });

        timer.start();

        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static ArrayList<BufferedImage> textToImageArrayList(String S) {

        String s1 = S.toUpperCase();
        String[] wordArray = S.split("\\s+");
        ArrayList<BufferedImage> textToImageList = new ArrayList<>();

        for (String word : wordArray) {
            BufferedImage image = Mat2BufferedImage(textToImage.get(word));
            if (image != null) {
                textToImageList.add(image);
            } else {
                char[] charArray = word.toCharArray();
                for (char ch : charArray) {
                    BufferedImage charImage = Mat2BufferedImage(textToImage.get(String.valueOf(ch)));
                    if (charImage != null) {
                        textToImageList.add(charImage);
                    }
                }
            }
        }       return textToImageList;
    }

    public static void uploadHashMapFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 2) { // Check if the array has at least two elements
                    try {
                        String key = parts[1];
                        Mat value = imread(parts[0]);
                        textToImage.put(key, value);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid format for key: " + parts[1]);
                    }
                } else {
                    System.err.println("Invalid line format: " + Arrays.toString(parts));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}