package test.substance;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.OfficeBlue2007Skin;

public class TestToggleMenuButtons extends test.common.TestToggleMenuButtons {

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		UIManager
				.installLookAndFeel("Substance Office Blue 2007",
						"org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SubstanceLookAndFeel.setSkin(new OfficeBlue2007Skin());
				TestToggleMenuButtons frame = new TestToggleMenuButtons();
				frame.setSize(300, 200);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
	}
}
