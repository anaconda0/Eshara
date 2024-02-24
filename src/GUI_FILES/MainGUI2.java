package GUI_FILES;

import javax.swing.*;
import java.awt.*;

public class MainGUI2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = createMainFrame();
            JButton textToSpeechButton = createStyledButton("Text to Speech");
            JButton speechToTextButton = createStyledButton("Speech to Text");
            JButton textToImageButton = createStyledButton("Text to Image");
            JButton imageToTextButton = createStyledButton("Image to Text");
            JButton Logout = createStyledButton2("Logout");

            textToSpeechButton.addActionListener(e -> {
                frame.dispose();
                TextToSpeechUI.display();
            });
            speechToTextButton.addActionListener(e -> {
                frame.dispose();
                SpeechToTextUI.display();
            });
            textToImageButton.addActionListener(e -> {
                frame.dispose();
                TextToImageUI.display();
            });
            imageToTextButton.addActionListener(e -> {
                frame.dispose();
                ImageToTextGUI.display();
            });

            Logout.addActionListener(e -> {
                frame.dispose();
                MainGUI.main(new String[]{});
            });

            addComponentsToMainFrame(frame, textToSpeechButton, speechToTextButton, textToImageButton, imageToTextButton, Logout);
        });
    }

    private static JFrame createMainFrame() {
        JFrame frame = new JFrame("Text, Speech, and Image Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new GridLayout(5, 1));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.BLACK);
        setIconImage(frame);
        return frame;
    }

    private static void setIconImage(JFrame frame) {
        ImageIcon icon = new ImageIcon("resources_images/ESHARA LOGO.png");
        frame.setIconImage(icon.getImage());
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 25));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        button.setPreferredSize(new Dimension(250, 150));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.RED);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK);
            }
        });

        return button;
    }

    private static JButton createStyledButton2 (String text)
    {
        JButton button2 = new JButton(text);
        button2.setBackground(Color.RED);
        button2.setForeground(Color.YELLOW);
        button2.setFont(new Font("Arial", Font.BOLD, 25));
        button2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        button2.setPreferredSize(new Dimension(250, 150));
        button2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button2.setBackground(Color.ORANGE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button2.setBackground(Color.RED);
            }
        });

        return button2;
    }

    private static void addComponentsToMainFrame(JFrame frame, JButton... buttons) {
        for (JButton button : buttons) {
            frame.add(button);
        }

        JButton backToHomeButton = createStyledButton("Back to Home");
        backToHomeButton.addActionListener(e -> frame.dispose());


        frame.setVisible(true);
    }
}