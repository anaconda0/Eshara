package GUI_FILES;

import nu.pattern.OpenCV;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class TextToImageUI {
    static {
        if (System.getProperty("java.version").startsWith("1.")) {
            // For Java versions 11 and below
            OpenCV.loadShared();
        } else {
            // For Java versions 12 and above
            OpenCV.loadLocally();
        }
    }


    public static void display() {
        JFrame frame = new JFrame("Text to Image");
        frame.setSize(300, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        JTextField textField = new JTextField(20);
        JButton generateButton = createStyledButton("Generate Image");
        JButton backToHomeButton = createStyledButton("Back to Home");

        ImageIcon icon = new ImageIcon(""); // Replace with the path to your image
        frame.setIconImage(icon.getImage());

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                TextToImage.displayImages(TextToImage.textToImageArrayList(text), 800, frame);
            }
        });

        backToHomeButton.addActionListener(e -> {
            frame.dispose();
            MainGUI2.main(new String[]{});
        });

        frame.setLayout(new FlowLayout());
        frame.add(textField);
        frame.add(generateButton);
        frame.add(backToHomeButton);
        frame.setVisible(true);
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setPreferredSize(new Dimension(150, 50));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLUE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.RED);
            }
        });
        return button;
    }

    private static void setIconImage(JFrame frame) {
        ImageIcon icon = new ImageIcon("resources_images/Handstalk logo.png");
        frame.setIconImage(icon.getImage());
    }

    public static class TextToImage {
        private static final HashMap<String, Mat> textToImage = new HashMap<>();
        static {
            String filePath = "src/GUI_FILES/TTIHashMap.txt";
            uploadHashMapFromFile(filePath);
        }

        public static BufferedImage Mat2BufferedImage(Mat m) {
            if (m == null) {
                System.err.println("Input Mat is null.");
                return null;
            }

            if (!m.isContinuous()) {
                System.err.println("The data in Mat is not continuous.");
                return null;
            }

            int type = BufferedImage.TYPE_BYTE_GRAY;
            if (m.channels() > 1) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }

            int bufferSize = m.channels() * m.cols() * m.rows();
            byte[] b = new byte[bufferSize];
            m.get(0, 0, b);

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
        public static ArrayList<BufferedImage> textToImageArrayList(String S) {

            String s1 = S.toLowerCase();
            String[] wordArray = s1.split("\\s+");
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

        public static void displayImages(java.util.List<BufferedImage> textToImageList, int delay, JFrame parentFrame) {
            JFrame jFrame = new JFrame("Image Viewer");
            jFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
            jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            jFrame.getContentPane().setBackground(Color.BLACK);
            jFrame.setSize(800, 600);
            jFrame.setResizable(false);

            // Set the frame location slightly to the left and up from the center
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int)(screenSize.getWidth() / 2 - jFrame.getWidth() / 2) - 80;// 100 pixels left from center
            int y = (int)(screenSize.getHeight() / 2 - jFrame.getHeight() / 2) - 80;// 50 pixels up from center
            jFrame.setLocation(x, y);

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
                            jFrame.pack();
                            jFrame.revalidate();
                            jFrame.repaint();
                            index[0]++;
                        }
                    } else {
                        timer.stop();
                        jFrame.dispose(); // Close the frame after displaying all images
                    }
                }
            });

            timer.start();

            JButton backToParentButton = createStyledButton("Back");
            backToParentButton.addActionListener(e -> {
                jFrame.dispose();
                parentFrame.setVisible(true);
            });

            jFrame.add(backToParentButton, BorderLayout.SOUTH);
            jFrame.setVisible(true);
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


    }}
