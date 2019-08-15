package pacman.core.terminal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

 import org.apache.log4j.Logger;

 import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

 public class MockGameScreen extends GameScreen {

     private static final Logger log = Logger.getLogger(GameScreen.class);

 	private BlockingQueue<KeyStroke> keyStrokes;
	public MockGameScreen(ByteArrayInputStream in, ByteArrayOutputStream out, int rows, int cols) throws IOException {
		super(in, out, rows, cols);
		this.keyStrokes = new LinkedBlockingQueue<>();
	}

 	@Override
	protected KeyStroke pollInput() throws IOException {
		try {
			return keyStrokes.take();
		} catch (InterruptedException e) {
			log.error("Interrupted while waiting for input",e);
			return new KeyStroke(KeyType.Unknown);
		}
	}

 	public void sendKeyPress(KeyType keyType) { 
		try {
			keyStrokes.put(new KeyStroke(keyType));
			getKeyPress();
		} catch (InterruptedException e) {
			log.error("Interrupted while adding keystroke",e);
		}
	}
}