package test.substance;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.pushingpixels.flamingo.api.common.*;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonPopupOrientationKind;
import org.pushingpixels.flamingo.api.common.popup.*;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.GeminiSkin;

import test.svg.transcoded.*;

public class MultiLevelMenu extends JFrame {

	public MultiLevelMenu() {
		super("Multi level menu");

		JCommandButton main = new JCommandButton("click me");
		main.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
		main.setDisplayState(CommandButtonDisplayState.MEDIUM);
		main.setFlat(false);

		// first level menu
		main.setPopupCallback(new PopupPanelCallback() {
			@Override
			public JPopupPanel getPopupPanel(JCommandButton commandButton) {
				JCommandPopupMenu result = new JCommandPopupMenu();

				result.addMenuButton(new JCommandMenuButton("Copy",
						new edit_copy()));
				result.addMenuButton(new JCommandMenuButton("Cut",
						new edit_cut()));
				result.addMenuButton(new JCommandMenuButton("Paste",
						new edit_paste()));

				result.addMenuSeparator();

				JCommandMenuButton second = new JCommandMenuButton("Find", null);
				second.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
				// second level
				second.setPopupCallback(new PopupPanelCallback() {
					@Override
					public JPopupPanel getPopupPanel(
							JCommandButton commandButton) {
						JCommandPopupMenu result = new JCommandPopupMenu();

						result.addMenuButton(new JCommandMenuButton("Find",
								new edit_find()));
						result.addMenuButton(new JCommandMenuButton(
								"Find replace", new edit_find_replace()));

						return result;
					}
				});
				second
						.setPopupOrientationKind(CommandButtonPopupOrientationKind.SIDEWARD);
				result.addMenuButton(second);

				return result;
			}
		});

		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		this.add(main);

		this.setSize(300, 200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SubstanceLookAndFeel.setSkin(new GeminiSkin());
				JFrame.setDefaultLookAndFeelDecorated(true);

				new MultiLevelMenu().setVisible(true);
			}
		});
	}

}
