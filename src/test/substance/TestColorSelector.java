package test.substance;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.OfficeBlue2007Skin;

public class TestColorSelector extends test.common.TestColorSelector {

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		UIManager
				.installLookAndFeel("Substance Office Blue 2007",
						"org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SubstanceLookAndFeel.setSkin(new OfficeBlue2007Skin());
				TestColorSelector frame = new TestColorSelector();

				frame.centerPanel.putClientProperty(
						SubstanceLookAndFeel.COLORIZATION_FACTOR, new Double(
								1.0));

				frame.setSize(500, 400);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
	}
}
