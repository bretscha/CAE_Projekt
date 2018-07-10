package utilities;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

/**
 * executes a Sparql update command
 */
public class Update_Execute {
	
	/**
	 * executes a Sparql update command
	 * @param frame main frame gui
	 * @param updateString String with update command
	 * @param dsLocation sparql endpoint
	 */
	public static void executeUpdate(JFrame frame, String updateString, String dsLocation) {
		
		UpdateRequest req = UpdateFactory.create();
		try {
			req.add(updateString);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame,
					"Fehler beim Erstellen des Query/Update Strings! /n Eingaben in den Textfeldern überprüfen und ToolTips beachten.");

			return;
		}

		try {
			UpdateProcessor exeProc = UpdateExecutionFactory.createRemote(req, dsLocation + "update");
			exeProc.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame,
					"Fehler beim Ausführen des Befehls! /n Bitte Verbindung zum Server überprüfen.");

			return;
		}

		
	}
}
