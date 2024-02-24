package GUI_FILES;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainGUI extends JFrame {

    public MainGUI() {
        setTitle("ESHARA");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }
    private void initComponents() {
        // Create a welcome label
        JLabel welcomeLabel = new JLabel("Welcome to ESHARA", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 50));
        welcomeLabel.setForeground(Color.white);

        // Create buttons
        JButton signInButton = new JButton("Sign In");
        signInButton.setFont(new Font("Arial", Font.BOLD, 25)); // Adjust the size as needed
        signInButton.setBackground(Color.BLUE); // Choose the color you prefer
        signInButton.setForeground(Color.WHITE); // This changes the color of the text on the button

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 25)); // Adjust the size as needed
        signUpButton.setBackground(Color.BLUE); // Choose the color you prefer
        signUpButton.setForeground(Color.WHITE); // This changes the color of the text on the button

        // Add action listeners to buttons
        signInButton.addActionListener(e -> openSignInForm());
        signUpButton.addActionListener(e -> openSignUpForm());

        // Create a background panel with an image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage image = ImageIO.read(new File("resources_images/back ver 2.jpeg")); // Replace with your image path
                    g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Set layout for the background panel
        backgroundPanel.setLayout(new BorderLayout());

        // Create a panel for buttons and set its layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false); // Make the button panel transparent

        // Add buttons to the button panel
        buttonPanel.add(signInButton);
        buttonPanel.add(signUpButton);

        // Add components to the background panel
        backgroundPanel.add(welcomeLabel, BorderLayout.NORTH);
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        // Set content pane to the background panel
        setContentPane(backgroundPanel);
    }



    private void openSignInForm() {
        SignInForm signInForm;
        signInForm = new SignInForm();
        signInForm.setVisible(true);
        this.setVisible(false);
    }

    private void openSignUpForm() {
        SignUpGUI signUpGUI = new SignUpGUI();
        signUpGUI.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI mainGUI = new MainGUI();
            mainGUI.setVisible(true);
        });
    }
}