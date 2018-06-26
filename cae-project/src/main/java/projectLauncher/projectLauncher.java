package projectLauncher;

import java.io.IOException;
import java.net.MalformedURLException;

import org.UserInterface.GUI;
import org.daverog.tripliser.exception.TripliserException;

public class projectLauncher {

    /**
     * start method
     * 
     * @param args
     *            start arguments
     * @throws TripliserException
     * @throws IOException
     * @throws MalformedURLException
     */
    public static void main(String[] args) {
	GUI gui = new GUI();
	gui.buildGUI();
    }
}
