package GUI_FILES;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SignInForm extends JFrame {

    private JTextField usernameField;
    private MainGUI2 MainGUI2;
    private JPasswordField passwordField;
    private List<USERR> users; // Assuming you have a list of users

    public SignInForm() {
        this.users = loadUsersFromFile("src/GUI_FILES/usersdata.txt"); // Initialize the users list

        setTitle("Sign In");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

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

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Welcome to Hands Talk!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Adjust the size as needed
        add(titleLabel, BorderLayout.PAGE_START);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton signInButton = new JButton("Sign In");
        signInButton.setFont(new Font("Arial", Font.BOLD, 18)); // Adjust the size as needed
        signInButton.setBackground(Color.BLUE); // Choose the color you prefer
        signInButton.setForeground(Color.WHITE); // This changes the color of the text on the button

        JButton backToHomeButton = new JButton("Back to Home");
        backToHomeButton.setFont(new Font("Arial", Font.BOLD, 18)); // Adjust the size as needed
        backToHomeButton.setBackground(Color.BLUE); // Choose the color you prefer
        backToHomeButton.setForeground(Color.WHITE); // This changes the color of the text on the button

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signIn();
            }
        });
        backToHomeButton.addActionListener(e -> {
            dispose();
            MainGUI.main(new String[]{});
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Placeholder
        panel.add(signInButton);
        panel.add(backToHomeButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    private void signIn() {
        String username = usernameField.getText();
        String passwordString = new String(passwordField.getPassword());

        for (USERR user : users) {
            if (username.equals(user.getUsername()) && passwordString.equals(new String(user.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Sign In successful!\nWelcome, " + user.getFirstname() + "!");

                // Open MainGUI2

                // Close the current sign-in form
                GUI_FILES.MainGUI2.main(new String[]{});
                this.dispose();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
    }

    private static void setIconImage(JFrame frame) {
        ImageIcon icon = new ImageIcon("resources_images/Handstalk logo.png");
        frame.setIconImage(icon.getImage());
    }

    private List<USERR> loadUsersFromFile(String filePath) {
        List<USERR> loadedUsers = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] userData = line.split(",");
                if (userData.length == 6) { // Assuming 6 fields: firstname, lastname, email, password, gender, username
                    String firstname = userData[0].trim();
                    String lastname = userData[1].trim();
                    String email = userData[2].trim();
                    String userPassword = userData[3].trim();
                    GENDER gender = GENDER.valueOf(userData[4].trim());
                    String userUsername = userData[5].trim();

                    USERR user = new USERR(firstname, lastname, email, userPassword, gender, userUsername);
                    loadedUsers.add(user);
                }
            }
            System.out.println("User data loaded successfully. Number of users: " + loadedUsers.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("File not found: " + e.getMessage());
        }

        return loadedUsers;
    }
}