package GUI_FILES;

import nu.pattern.OpenCV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static GUI_FILES.TextToSpeechUI.createStyledButton;
import static java.lang.System.err;


public class ImageToTextGUI {
    static {
        if (System.getProperty("java.version").startsWith("1.")) {
            // For Java versions 11 and below
            OpenCV.loadShared();
        } else {
            // For Java versions 12 and above
            OpenCV.loadLocally();
        }
    }


    private static class MyButton implements Comparable<MyButton>{
        public String path;
        public ImageIcon icon;
        public boolean isSelected = false;
        public  JToggleButton button;
        public static int order = 0;
        public int myOrder=-1;
        public String label; // Corresponding letter or number
        private JTextArea textArea; // Reference to the textArea


        MyButton(String imgPath, JTextArea textArea) {
            this.textArea = textArea;
            // Extract the letter or number from the image path
            this.label = extractLabelFromPath(imgPath);
            this.icon = createImageIcon(imgPath); // Adjust the path
            Image scaledImage = icon.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            this.button = new JToggleButton(scaledIcon);
            this.button.setBackground(Color.WHITE);
            this.button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    // Append the label of the clicked button
                    textArea.append(label);
                    myOrder = MyButton.order;
                    MyButton.order++;
                    if(isSelected) {
                        button.setSelected(false);
                        isSelected = false;
                    }else{
                        button.setSelected(true);
                        isSelected=true;
                    }
                }
            });
        }

        private static String extractLabelFromPath(String path) {
            // Logic to extract the label (letter or number) from the path
            // Example: Extract "A" from "C:\\path\\to\\A.jpg"
            File file = new File(path);
            String name = file.getName();
            int dotIndex = name.lastIndexOf('.');
            if(dotIndex > 0) {
                name = name.substring(0, dotIndex);
            }
            return name; // Returns the name of the file without extension
        }

        @Override
        public int compareTo(MyButton myButton) {
            return this.myOrder-myButton.myOrder;

        }
    }

    private static final ArrayList<MyButton> buttons =  new ArrayList<>();
    private static final Dimension buttonSize = new Dimension(100, 100);

    private static final List<String> imagePaths = readImagePathsFromFile("src/GUI_FILES/copy image pathes.txt");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            display();
        });
    }

    static void display() {



        JFrame frame = new JFrame("GridLayout Button Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel(new BorderLayout());

        JTextArea textArea = new JTextArea(3, 20);
        textArea.setFont(new Font("Arial", Font.BOLD, 40));
        JScrollPane textScrollPane = new JScrollPane(textArea);


        JPanel buttonPanel = new JPanel(new GridLayout(0, 10));
        JButton backToHomeButton = createStyledButton("Back to Home");

        backToHomeButton.addActionListener(e -> {
            frame.dispose();
            MainGUI2.main(new String[]{});
        });


        for (int i = 0; i < imagePaths.size(); i++) {
            MyButton button = new MyButton(imagePaths.get(i), textArea);
            buttons.add(button);
            buttonPanel.add(button.button);
        }



        JScrollPane buttonScrollPane = new JScrollPane(buttonPanel);
        buttonScrollPane.setPreferredSize(new Dimension(1920, 1080));
        buttonScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        buttonScrollPane.setForeground(Color.BLACK);

        // New code to set faster scrolling speed
        buttonScrollPane.getVerticalScrollBar().setUnitIncrement(20); // You can adjust this value
        buttonScrollPane.getVerticalScrollBar().setBlockIncrement(60); // You can adjust this value

        mainPanel.add(textScrollPane, BorderLayout.NORTH);
        mainPanel.add(buttonScrollPane, BorderLayout.CENTER);

        // Create processButton with a smaller size
        JButton processButton = new JButton("GENERATE");
        Dimension processButtonSize = new Dimension(100, 50);
        Dimension processButtonSize2 = new Dimension(200, 100);
        processButton.setPreferredSize(processButtonSize);
        processButton.setBackground(Color.BLACK);
        processButton.setForeground(Color.WHITE);
        processButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                processButton.setBackground(Color.RED);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                processButton.setBackground(Color.BLACK);
            }
        });

        JButton clearButton = new JButton("clear");
        processButton.setPreferredSize(processButtonSize2);
        clearButton.setPreferredSize(processButtonSize);
        clearButton.setBackground(Color.BLACK);
        clearButton.setForeground(Color.WHITE);
        processButton.setFont(new Font("Arial", Font.BOLD, 20));
        clearButton.setFont(new Font("Arial", Font.BOLD, 25));
        clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clearButton.setBackground(Color.RED);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                clearButton.setBackground(Color.BLACK);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for(int i=0;i<buttons.size();i++) {
                    MyButton button = buttons.get(i);
                    if(button.isSelected) {
                        button.button.setSelected(false);
                        button.isSelected = false;
                    }
                }
                textArea.setText("");
            }
        });

        // Create processPanel and add processButton to it
        JPanel processPanel = new JPanel();
        processPanel.add(clearButton);
        processPanel.add(backToHomeButton);
        // Add processPanel to the south of the main panel
        mainPanel.add(processPanel, BorderLayout.SOUTH);

        // Add action listener for the process button
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> selectedPaths = new ArrayList<>();
                Collections.sort(buttons);
                for(int i=0;i<buttons.size();i++){
                    MyButton button = buttons.get(i);
                    if(button.isSelected){
                        selectedPaths.add(button.path);
                    }
                }

                // Handle the process button click
                String text = ImageToTextt.getStringfromImage(selectedPaths);
                textArea.setText(text);
            }
        });

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(1920, 1000);
        frame.setVisible(true);

        JButton spaceButton = new JButton("Space");
        spaceButton.setPreferredSize(new Dimension(300, 50));
        spaceButton.setBackground(Color.BLACK);
        spaceButton.setForeground(Color.WHITE);
        spaceButton.setFont(new Font("Arial", Font.BOLD, 25));
        spaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.append(" "); // Append a space to the text
            }
        });
        processPanel.add(spaceButton); // Add space button to processPanel
    }

    private static List<String> readImagePathsFromFile(String filePath) {
        List<String> imagePaths = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                imagePaths.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return imagePaths;
    }


    private static ImageIcon createImageIcon(String path) {
        try {
            return new ImageIcon(path);
        } catch (Exception e) {
            err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
