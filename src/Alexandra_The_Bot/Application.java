package Alexandra_The_Bot;

import javax.sound.sampled.LineUnavailableException;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;

import net.sourceforge.javaflacencoder.FLACFileWriter;

/**
 * This is where all begins .
 * 
 * @author GOXR3PLUS
 *
 */
public class Application {
	private final Microphone mic = new Microphone(FLACFileWriter.FLAC);
	private final GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
	String oldText = "";
	
	/**
	 * Constructor
	 */
	public Application() {
		
		//Duplex Configuration
		duplex.setLanguage("en");
		
		duplex.addResponseListener(new GSpeechResponseListener() {
			
			public void onResponse(GoogleResponse googleResponse) {
				String output = "";
				
				//Get the response from Google Cloud
				output = googleResponse.getResponse();
				System.out.println(output);
				if (output != null) {
					makeDecision(output);
				} else
					System.out.println("Output was null");
			}
		});
		
		//Start the Speech Recognition
		startSpeechRecognition();
		
	}
	public void makeDecision(String output) {
		output = output.trim();
		//System.out.println(output.trim());
		
		//We don't want duplicate responses
		if (!oldText.equals(output))
			oldText = output;
		else
			return;

 if (output.contains("stop speech recognition")) {//Stop Speech Recognition
			stopSpeechRecognition();
			
		} else { //Nothing matches here ?
			System.out.print("");
		}
		
	}
	
	/**
	 * Calls the MaryTTS to say the given text
	 * 
	 * @param text
	 */
	public void speak(String text) {
		System.out.println(text);
		
	}
	
	/**
	 * Starts the Speech Recognition
	 */
	public void startSpeechRecognition() {
		//Start a new Thread so our application don't lags
		new Thread(() -> {
			try {
				duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
			} catch (LineUnavailableException | InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	/**
	 * Stops the Speech Recognition
	 */
	public void stopSpeechRecognition() {
		mic.close();
		System.out.println("Stopping Speech Recognition...." + " , Microphone State is:" + mic.getState());
	}
	
	public static void main(String[] args) {
		new Application();
	}
	
}
