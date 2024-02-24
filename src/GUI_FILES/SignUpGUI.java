package GUI_FILES;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//import static GUI_FILES.TextToSpeechUI.createStyledButton;

public class SignUpGUI extends JFrame {
    static JButton createStyledButton(String text) {
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

    private JTextField firstnameField;
    private JTextField lastnameField;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    public JComboBox<GENDER> genderComboBox;

    private List<USERR> users;

    public SignUpGUI() {
        // Set up the frame
        setTitle("User Sign Up");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize user list
        users = loadUserDataFromFile();

        // Create components
        firstnameField = new JTextField(20);
        lastnameField = new JTextField(20);
        emailField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        genderComboBox = new JComboBox<>(GENDER.values());

        JButton signupButton = new JButton("Sign Up");
        JButton backToHomeButton = createStyledButton("Back to Home");
        backToHomeButton.addActionListener(e -> {
            dispose();
            MainGUI.main(new String[]{});
        });

        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to the frame with larger labels
        add(createLabel("First Name:"), gbc);
        gbc.gridx = 1;
        add(firstnameField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        add(createLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        add(lastnameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(createLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        add(createLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        add(createLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        add(createLabel("Gender:"), gbc);
        gbc.gridx = 1;
        add(genderComboBox, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(new JLabel("")); // Placeholder
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(signupButton, gbc);
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(backToHomeButton, gbc);

        // Add action listener
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpUser();
            }
        });
    }

    private JLabel createLabel(String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16)); // Set font size to 16
        return label;
    }

    private List<USERR> loadUserDataFromFile() {
        List<USERR> loadedUsers = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("src/GUI_FILES/usersdata.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] userData = line.split(",");
                if (userData.length == 6) { // Assuming 6 fields: firstname, lastname, email, password, gender, username
                    String firstname = userData[0].trim();
                    String lastname = userData[1].trim();
                    String email = userData[2].trim();
                    String password = userData[3].trim();
                    GENDER gender = GENDER.valueOf(userData[4].trim());
                    String username = userData[5].trim();

                    USERR user = new USERR(firstname, lastname, email, password, gender, username);
                    loadedUsers.add(user);
                }
            }
            System.out.println("User data loaded successfully.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("File not found: ");
        }

        return loadedUsers;
    }

    public void saveUserDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/GUI_FILES/usersdata.txt"))) {
            for (USERR user : users) {
                // Format the user data and write to the file
                writer.println(user.getFirstname() + "," +
                        user.getLastname() + "," +
                        user.getEmail() + "," +
                        user.getPassword() + "," +
                        user.getGender() + "," +
                        user.getUsername());
            }
            System.out.println("User data saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving user data to file.");
        }
    }

    private void signUpUser() {
        String firstname = firstnameField.getText();
        String lastname = lastnameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword()); // Get plain text password
        GENDER gender = (GENDER) genderComboBox.getSelectedItem();

        // Check if the username is already taken
        if (isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(this, "Username already taken. Please choose another.");
            return;
        }

        // Create a new user and add to the list
        USERR newUser = new USERR(firstname, lastname, email, password, gender, username);
        users.add(newUser);

        // Save user data to a file
        saveUserDataToFile();

        JOptionPane.showMessageDialog(this, "Sign Up successful!\n" + newUser.toString());
    }


    private boolean isUsernameTaken(String username) {
        for (USERR user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}