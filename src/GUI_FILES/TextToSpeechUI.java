package GUI_FILES;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class TextToSpeechUI {

    private static TextToSpeech tts = new TextToSpeech(); // Initialize GUI_FILES.TextToSpeechUI.TextToSpeech

    public static TextToSpeech getTts() {
        return tts;
    }

    public static void setTts(TextToSpeech tts) {
        TextToSpeechUI.tts = tts;
    }

    public static void display() {
        JFrame frame = new JFrame("Text to Speech");
        frame.setSize(400, 200);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        JTextField textField = new JTextField(20);
        JButton speakButton = createStyledButton("Speak");
        JButton backToHomeButton = createStyledButton("Back to Home");

        ImageIcon icon = new ImageIcon(""); // Replace with the path to your image
        frame.setIconImage(icon.getImage());

        speakButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                tts.speak(text);
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                tts.close(); // Deallocate synthesizer resources
            }
        });

        backToHomeButton.addActionListener(e -> {
            frame.dispose();
            MainGUI2.main(new String[]{});
        });

        frame.setLayout(new FlowLayout());
        frame.add(textField);
        frame.add(speakButton);
        frame.add(backToHomeButton);
        frame.setVisible(true);
    }

    private static void setIconImage(JFrame frame) {
        ImageIcon icon = new ImageIcon("C:\\Users\\yhayt\\Desktop\\Semester 3\\OneNot Notebooks\\Object Oriented Programming (CCS2303)\\Material\\HandsTalk Project\\Creative Elements\\Logo\\Handstalk logo.png");
        frame.setIconImage(icon.getImage());
    }

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

    public static class TextToSpeech {

        private Synthesizer synthesizer;

        public TextToSpeech() {
            initSynthesizer();
        }

        private void initSynthesizer() {
            System.setProperty("freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            try {
                Central.registerEngineCentral("com.sun.speech.freetts" +
                        ".jsapi.FreeTTSEngineCentral");
                synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
                synthesizer.allocate();
                synthesizer.resume();
            } catch (EngineException | AudioException e) {
                throw new RuntimeException(e);
            }
        }

        public void speak(String text) {
            try {
                synthesizer.speakPlainText(text, null);
                synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void close() {
            if (synthesizer != null) {
                try {
                    synthesizer.deallocate();
                } catch (EngineException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}