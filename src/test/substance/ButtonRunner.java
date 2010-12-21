package test.substance;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import org.pushingpixels.flamingo.api.common.*;
import org.pushingpixels.flamingo.api.common.icon.EmptyResizableIcon;
import org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel;

public class ButtonRunner extends JFrame {
	// /////////////////////////// Class Attributes
	// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	private static final long serialVersionUID = -1706181618991931007L;

	// //////////////////////////// Class Methods
	// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);
				System.setProperty("sun.awt.noerasebackground", "true");
				System.setProperty("substancelaf.noExtraElements", "");

				try {
					UIManager.setLookAndFeel(new SubstanceRavenLookAndFeel());
				} catch (UnsupportedLookAndFeelException ex) {
					Logger.getLogger(ButtonRunner.class.getName()).log(
							Level.SEVERE, null, ex);
				}

				ButtonRunner test = new ButtonRunner();

				Thread heapThread = new Thread() {
					@Override
					public void run() {
						while (true) {
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
							}
							long heapSize = Runtime.getRuntime().totalMemory();
							long heapFreeSize = Runtime.getRuntime()
									.freeMemory();

							int heapSizeKB = (int) (heapSize / 1024);
							int takenHeapSizeKB = (int) ((heapSize - heapFreeSize) / 1024);
							System.out.println(new SimpleDateFormat(
									"dd/MM/yyyy HH:mm:ss.SSS")
									.format(new Date())
									+ " "
									+ takenHeapSizeKB
									+ "KB / "
									+ heapSizeKB + "KB");
						}
					}
				};
				heapThread.setDaemon(true);
				heapThread.start();

			}
		});
	}

	// ////////////////////////////// Attributes
	// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	// ///////////////////////////// Constructors
	// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	public ButtonRunner() {
		super("Test lots of buttons");

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setLayout(new BorderLayout());
		JCommandButtonPanel panel = new JCommandButtonPanel(
				CommandButtonDisplayState.MEDIUM);
		add(new JScrollPane(panel), BorderLayout.CENTER);

		panel.addButtonGroup("Test");
		for (int count = 0; count < 400; ++count)
			panel.addButtonToLastGroup(new JCommandButton(Integer
					.toString(count), new EmptyResizableIcon(0)));

		setSize(new Dimension(800, 600));
		setVisible(true);
	}

	// //////////////////////////////// Methods
	// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	// ------------------------ Implements:

	// ------------------------ Overrides:

	// ---------------------------- Abstract Methods
	// -----------------------------

	// ---------------------------- Utility Methods
	// ------------------------------

	// ---------------------------- Property Methods
	// -----------------------------
}
