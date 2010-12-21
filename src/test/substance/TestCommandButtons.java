package test.substance;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.OfficeBlue2007Skin;

public class TestCommandButtons extends test.common.TestCommandButtons {

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		UIManager
				.installLookAndFeel("Substance Office Blue 2007",
						"org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SubstanceLookAndFeel.setSkin(new OfficeBlue2007Skin());
				TestCommandButtons frame = new TestCommandButtons();
				frame.setSize(800, 400);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
	}
}
