package GUI_FILES;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpeechToTextUI {

    private static GSpeechDuplex duplex;
    private static Microphone mic;

    public static GSpeechDuplex getDuplex() {
        return duplex;
    }

    public static void setDuplex(GSpeechDuplex duplex) {
        SpeechToTextUI.duplex = duplex;
    }

    public static Microphone getMic() {
        return mic;
    }

    public static void setMic(Microphone mic) {
        SpeechToTextUI.mic = mic;
    }

    public static void display() {
        JFrame frame = new JFrame("Speech to Text");
        frame.setSize(1920, 1000);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(1800, 1000)); // Set preferred size

        JButton startButton = createStyledButton("Start Listening");
        JButton stopButton = createStyledButton("Stop Listening");
        JButton backToHomeButton = createStyledButton("Back to Home");
        stopButton.setEnabled(false);

        ImageIcon icon = new ImageIcon("resources_images/ESHARA LOGO.png");
        frame.setIconImage(icon.getImage());

        // Initialize the speech recognizer and microphone
        initSpeechRecognizer(textArea);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                startSpeechRecognition();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        });
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                stopSpeechRecognition();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        });

        backToHomeButton.addActionListener(e -> {
            frame.dispose();
            MainGUI2.main(new String[]{});
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(backToHomeButton);

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
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

    private static void initSpeechRecognizer(JTextArea responseArea) {
        mic = new Microphone(FLACFileWriter.FLAC);
        duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw"); // Replace with your API key
        duplex.setLanguage("en");
        duplex.addResponseListener(new GSpeechResponseListener() {
            @Override
            public void onResponse(GoogleResponse googleResponse) {
                String output = googleResponse.getResponse();
                if (output != null) {
                    responseArea.append(output + "\n");
                } else {
                    responseArea.append("(no response)\n");
                }
            }
        });
    }

    private static void startSpeechRecognition() {
        new Thread(() -> {
            try {
                duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error starting the speech recognition engine.");
            }
        }).start();
    }

    private static void stopSpeechRecognition() {
        mic.close();
        duplex.stopSpeechRecognition();
    }
}